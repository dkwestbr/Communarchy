package communarchy.controllers.handlers.actions;

import java.io.IOException;
import java.util.List;

import javax.jdo.Transaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.facts.PMSession;
import communarchy.facts.actions.Vote;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.Stance;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.GetStanceCountShard;
import communarchy.facts.queries.entity.GetVoteCountShard;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.facts.queries.list.VotesCastQuery;

public class TakeStanceHandler extends AbstractActionHandler<Point> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMatcherPattern() {
		return "/point/takestance/(agree|neutral|disagree)/([0-9]+)";
	}

	@Override
	protected boolean handlesAction(String action) {
		return action.equals("agree") || action.equals("neutral") || action.equals("disagree");
	}

	@Override
	protected Point getResource(long id, PMSession pmSession) {
		Key key = KeyFactory.createKey(Point.class.getSimpleName(), id);
		return pmSession.getMapper(BasicMapper.class).getById(Point.class, key);
	}
	
	@Override
	protected void performAction(HttpServletRequest request,
			HttpServletResponse response, Point resource, String command,
			ApplicationUser user, PMSession pmSession) throws IOException {
		
		UserStance existingStance = pmSession.getMapper(UniqueEntityMapper.class)
				.getUnique(new UserStanceQuery(resource.getKey(), user.getUserId()));

		if(existingStance == null) {
			Transaction tx = pmSession.getPM().currentTransaction();
			try {
				tx.begin();
				existingStance = new UserStance(user.getUserId(), resource.getKey(), 
						Stance.getStanceAsId(command));
				pmSession.getMapper(CountMapper.class).increment(new GetStanceCountShard(existingStance.getPoint(), existingStance.getStance()));
				pmSession.getMapper(BasicMapper.class).persist(existingStance);
				tx.commit();
			} finally {
				if(tx.isActive()) {
					tx.rollback();
				}
			}
		} else if(!existingStance.getStance().equals(Stance.getStanceAsId(command))) {
			
			/*
			 * This transaction sucks; but GAE doesn't support transactions on more than 5 entity types or on more than 5 operations on 
			 * the same entity type; therefor it had to be broken up.  There is likely problems with this; but it will work most of the time,
			 * so I'm ok with that for now.
			 */
			Transaction tx = pmSession.getPM().currentTransaction();
			List<Vote> votes = pmSession.getMapper(QueryMapper.class).runListQuery(new VotesCastQuery(resource.getKey(), user.getUserId()));
			
			boolean isSuccesful = false;
			try {
				tx.begin();
				pmSession.getMapper(CountMapper.class).decrement(new GetStanceCountShard(existingStance.getPoint(), existingStance.getStance()));
				existingStance.setStance(Stance.getStanceAsId(command));
				pmSession.getMapper(CountMapper.class).increment(new GetStanceCountShard(existingStance.getPoint(), existingStance.getStance()));
				pmSession.getMapper(BasicMapper.class).persist(existingStance);
				tx.commit();
			} finally {
				if(tx.isActive()) {
					tx.rollback();
				} else {
					isSuccesful = true;
				}
			}
			
			if(isSuccesful == true) {
				for(Vote deleteMe : votes) {
					tx = pmSession.getPM().currentTransaction();
					try {
						tx.begin();
						pmSession.getMapper(CountMapper.class).decrement(new GetVoteCountShard(deleteMe.getPovKey()));
						pmSession.getMapper(BasicMapper.class).delete(deleteMe);
						tx.commit();
					} finally {
						if(tx.isActive()) {
							tx.rollback();
						}
					}
				}
			}
		}
		
		response.sendRedirect(String.format("/arg/%d", resource.getParentId().getId()));
	}
}