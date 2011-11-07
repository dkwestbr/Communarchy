package communarchy.vb.arg.point.pov.branches;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.PovMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.pov.nodes.AllowVote;
import communarchy.vb.arg.point.pov.nodes.AlreadyVoted;
import communarchy.vb.arg.point.pov.nodes.NoVotesRemaining;

public class GetVoteButtons extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static GetVoteButtons INSTANCE;
	private GetVoteButtons() {}
	
	public static final String P_VOTE_PARAMS = "voteParams";
	public static final String P_IS_DISABLED = "isDisabled";
	public static final String P_USER_IS_AUTHOR = "userIsAuthor";
	public static final String P_NO_VOTES_REMAINING = "noVotesRemaining";
	
	public static GetVoteButtons get() {
		if(INSTANCE == null) {
			INSTANCE = new GetVoteButtons();
			INSTANCE.possiblePaths.add(AlreadyVoted.get());
			INSTANCE.possiblePaths.add(NoVotesRemaining.get());
			INSTANCE.possiblePaths.add(AllowVote.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetVoteButtons.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		IPointMapper pointMapper = pmSession.getMapper(PointMapper.class);
		
		IVote vote = pmSession.getMapper(PovMapper.class).selectVote(scopedResource.getPovId(), user.getUserId());
		if(vote != null) {
			pMap.put(P_IS_DISABLED, " ");
			pMap.put(P_VOTE_PARAMS, AlreadyVoted.get().getParams(pmSession, user, request, scopedResource));
		} else if(user.getUserId().equals(scopedResource.getPosterId())) {
			pMap.put(P_USER_IS_AUTHOR, " ");
			pMap.put(P_VOTE_PARAMS, NoVotesRemaining.get().getParams(pmSession, user, request, scopedResource));
		} 
		
		/*
		else if(pointMapper.getVoteCount(scopedResource.getParentPointId(), user.getUserId()) 
				== pointMapper.getMaxVoteCount(scopedResource.getParentPointId(), scopedResource.getStance())) {
			pMap.put(P_NO_VOTES_REMAINING, " ");
			pMap.put(P_VOTE_PARAMS, NoVotesRemaining.get().getParams(pmSession, user, request, scopedResource));
		} 
		*/
		
		else {
			pMap.put(P_VOTE_PARAMS, AllowVote.get().getParams(pmSession, user, request, scopedResource));
		}
		
		return pMap;
	}
}