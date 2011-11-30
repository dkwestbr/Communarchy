package communarchy.controllers.handlers.actions;

import java.io.IOException;
import javax.jdo.Transaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.facts.PMSession;
import communarchy.facts.actions.Vote;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.GetVoteCountShard;
import communarchy.facts.queries.entity.GetVoteQuery;
import communarchy.facts.queries.entity.IEntityQuery;

public class PovVoteHandler extends AbstractActionHandler<PointOfView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void performAction(HttpServletRequest request,
			HttpServletResponse response, PointOfView resource, String action,
			ApplicationUser user, PMSession pmSession) throws IOException {
		
		IEntityQuery<Vote> voteExistsQuery = new GetVoteQuery(resource.getParentPointId(), resource.getKey(), user.getUserId());
		Vote vote = pmSession.getMapper(UniqueEntityMapper.class).getUnique(voteExistsQuery);
		if(action.equals("up") && vote == null) {
			if(vote == null) {
				Transaction tx = pmSession.getPM().currentTransaction();
				try {
					tx.begin();
					pmSession.getMapper(UniqueEntityMapper.class).persistUnique(voteExistsQuery);
					pmSession.getMapper(CountMapper.class).increment(new GetVoteCountShard(resource.getKey()));
					tx.commit();
				} finally {
					if(tx.isActive()) {
						tx.rollback();
					}
				}
			}
		} else if(action.equals("reclaim") && vote != null) {
			Transaction tx = pmSession.getPM().currentTransaction();
			try {
				tx.begin();
				pmSession.getMapper(UniqueEntityMapper.class).deleteUnique(voteExistsQuery);
				pmSession.getMapper(CountMapper.class).decrement(new GetVoteCountShard(resource.getKey()));
				tx.commit();
			} finally {
				if(tx.isActive()) {
					tx.rollback();
				}
			}
		}
		
		IPoint point = pmSession.getMapper(BasicMapper.class).getById(Point.class, resource.getParentPointId());
		response.sendRedirect(String.format("/arg/%d", point.getParentId().getId()));
	}

	@Override
	protected PointOfView getResource(long id, PMSession pmSession) {
		Key povKey = KeyFactory.createKey(PointOfView.class.getSimpleName(), id);
		return pmSession.getMapper(BasicMapper.class).getById(PointOfView.class, povKey);
	}

	@Override
	protected String getMatcherPattern() {
		return "/pov/vote/(up|reclaim)/([0-9]+)";
	}

	@Override
	protected boolean handlesAction(String action) {
		return action.equals("up") || action.equals("reclaim");
	}
}