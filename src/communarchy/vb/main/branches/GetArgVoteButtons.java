package communarchy.vb.main.branches;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.actions.ArgVote;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.GetArgVoteQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.main.nodes.ArgVoteCount;
import communarchy.vb.main.nodes.DownvoteActive;
import communarchy.vb.main.nodes.DownvoteInactive;
import communarchy.vb.main.nodes.UpvoteActive;
import communarchy.vb.main.nodes.UpvoteInactive;

public class GetArgVoteButtons extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static final Logger log =
		      Logger.getLogger(GetArgVoteButtons.class.getName());
	
	private static GetArgVoteButtons INSTANCE;
	private GetArgVoteButtons() {}
	
	public static GetArgVoteButtons get() {
		if(INSTANCE == null) {
			INSTANCE = new GetArgVoteButtons();
			INSTANCE.possiblePaths.add(ArgVoteCount.get());
			INSTANCE.possiblePaths.add(UpvoteActive.get());
			INSTANCE.possiblePaths.add(UpvoteInactive.get());
			INSTANCE.possiblePaths.add(DownvoteActive.get());
			INSTANCE.possiblePaths.add(DownvoteInactive.get());
		}
		
		return INSTANCE;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/main/branches/GetArgVoteButtons.soy";
	}
	
	private static final String P_UP_VOTED = "upvoted";
	private static final String P_DOWN_VOTED = "downvoted";
	private static final String P_UPVOTE_PARAMS = "upvoteParams";
	private static final String P_VOTE_COUNT_PARAMS = "voteCountParams";
	private static final String P_DOWNVOTE_PARAMS = "downvoteParams";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		if(user == null || !user.isAuthenticated()) {
			pMap.put(P_UP_VOTED, "");
			pMap.put(P_DOWN_VOTED, "");
			pMap.put(P_VOTE_COUNT_PARAMS, ArgVoteCount.get().getParams(pmSession, user, request, scopedResource));
			pMap.put(P_UPVOTE_PARAMS, UpvoteActive.get().getParams(pmSession, user, request, scopedResource));
			pMap.put(P_DOWNVOTE_PARAMS, DownvoteActive.get().getParams(pmSession, user, request, scopedResource));
		} else {
			GetArgVoteQuery voteExistsQuery = new GetArgVoteQuery(scopedResource.getKey(), user.getUserId(), Integer.MIN_VALUE);
			ArgVote vote = null;
			try {
				vote = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(voteExistsQuery);
			} catch (CommunarchyPersistenceException e) {
				log.warning(e.getMessage());
				e.printStackTrace();
			}
			
			if(vote == null || vote.getVoteState().equals(ArgVote.VOTE_STATE_NEUTRAL)) {
				pMap.put(P_UP_VOTED, "");
				pMap.put(P_DOWN_VOTED, "");
				pMap.put(P_VOTE_COUNT_PARAMS, ArgVoteCount.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_UPVOTE_PARAMS, UpvoteActive.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_DOWNVOTE_PARAMS, DownvoteActive.get().getParams(pmSession, user, request, scopedResource));
			} else if(vote.getVoteState().equals(ArgVote.VOTE_STATE_UP)) {
				pMap.put(P_UP_VOTED, "true");
				pMap.put(P_DOWN_VOTED, "");
				pMap.put(P_VOTE_COUNT_PARAMS, ArgVoteCount.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_UPVOTE_PARAMS, UpvoteInactive.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_DOWNVOTE_PARAMS, DownvoteActive.get().getParams(pmSession, user, request, scopedResource));
			} else if(vote.getVoteState().equals(ArgVote.VOTE_STATE_DOWN)) {
				pMap.put(P_UP_VOTED, "");
				pMap.put(P_DOWN_VOTED, "true");
				pMap.put(P_VOTE_COUNT_PARAMS, ArgVoteCount.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_UPVOTE_PARAMS, UpvoteActive.get().getParams(pmSession, user, request, scopedResource));
				pMap.put(P_DOWNVOTE_PARAMS, DownvoteInactive.get().getParams(pmSession, user, request, scopedResource));
			}
		}
		
		return pMap;
	}
}