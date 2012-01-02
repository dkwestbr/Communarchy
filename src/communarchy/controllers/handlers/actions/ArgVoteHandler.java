package communarchy.controllers.handlers.actions;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.Transaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.facts.PMSession;
import communarchy.facts.actions.ArgVote;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Argument;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.GetArgVoteQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class ArgVoteHandler extends AbstractActionHandler<Argument> {

	private static final Logger log =
		      Logger.getLogger(ArgVoteHandler.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Argument getResource(long id, PMSession pmSession) 
			throws CommunarchyPersistenceException {
		
		Key argKey = KeyFactory.createKey(Argument.class.getSimpleName(), id);
		return pmSession.getMapper(BasicMapper.class).select(Argument.class, argKey);
	}

	@Override
	protected String getMatcherPattern() {
		return "/arg/vote/(up|down)/([0-9]+)";
	}

	@Override
	protected boolean handlesAction(String action) {
		return action.equals("up") || action.equals("down");
	}

	private static int MAX_TRIES = 3;
	@Override
	protected void performAction(HttpServletRequest request,
			HttpServletResponse response, Argument resource, String action,
			ApplicationUser user, PMSession pmSession) throws IOException {
		
		try {
			boolean isSuccessful = false;
			GetArgVoteQuery voteExistsQuery = new GetArgVoteQuery(resource.getKey(), user.getUserId(), Integer.MIN_VALUE);
			ArgVote vote = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(voteExistsQuery);
			
			if(vote == null) {
				if(action.equals("up")) {
					voteExistsQuery.setState(ArgVote.VOTE_STATE_UP);
					resource.upVote();
				} else if(action.equals("down")) {
					voteExistsQuery.setState(ArgVote.VOTE_STATE_DOWN);
					resource.downVote();
				}
				
				int tries = 0;
				while(!isSuccessful && tries < MAX_TRIES) {
					Transaction tx = pmSession.getPM().currentTransaction();
					try {
						tx.begin();
						pmSession.getMapper(UniqueEntityMapper.class).insertUnique(voteExistsQuery);
						pmSession.getMapper(BasicMapper.class).update(resource);
						tx.commit();
					} finally {
						if(tx.isActive()) {
							tx.rollback();
							++tries;
						} else {
							isSuccessful = true;
						}
					}
				}
			} else if(!(vote.getVoteState().equals(ArgVote.VOTE_STATE_UP) && action.equals("up"))
					&& !(vote.getVoteState().equals(ArgVote.VOTE_STATE_DOWN) && action.equals("down"))) {
			
				if(vote.getVoteState().equals(ArgVote.VOTE_STATE_UP) && action.equals("down")) {
					voteExistsQuery.setState(ArgVote.VOTE_STATE_NEUTRAL);
					resource.downVote();
				} else if(vote.getVoteState().equals(ArgVote.VOTE_STATE_DOWN) && action.equals("up")) {
					voteExistsQuery.setState(ArgVote.VOTE_STATE_NEUTRAL);
					resource.upVote();
				} else if(vote.getVoteState().equals(ArgVote.VOTE_STATE_NEUTRAL)) {
					if(action.equals("up")) {
						voteExistsQuery.setState(ArgVote.VOTE_STATE_UP);
						resource.upVote();
					} else if(action.equals("down")) {
						voteExistsQuery.setState(ArgVote.VOTE_STATE_DOWN);
						resource.downVote();
					}
				}
				
				int tries = 0;
				while(!isSuccessful && tries < MAX_TRIES) {
					Transaction tx = pmSession.getPM().currentTransaction();
					try {
						tx.begin();
						pmSession.getMapper(UniqueEntityMapper.class).updateUnique(voteExistsQuery, voteExistsQuery.getNewEntity());
						pmSession.getMapper(BasicMapper.class).update(resource);
						tx.commit();
					} finally {
						if(tx.isActive()) {
							tx.rollback();
							++tries;
						} else {
							isSuccessful = true;
						}
					}
				}
			}
			
		} catch (CommunarchyPersistenceException e) {
			log.warning(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}	
		
		response.sendRedirect("/");
	}
}