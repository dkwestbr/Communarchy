package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

@SuppressWarnings("rawtypes")
public class NoVotesRemaining extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static NoVotesRemaining INSTANCE;
	private NoVotesRemaining() {}
	
	public static final String P_UPVOTE_PARAMS = "upvoteParams";
	public static final String P_RECLAIM_VOTE_PARAMS = "reclaimvoteParams";
	
	public static NoVotesRemaining get() {
		if(INSTANCE == null) {
			INSTANCE = new NoVotesRemaining();
			INSTANCE.possiblePaths.add(UpvoteActiveDisabled.get());
			INSTANCE.possiblePaths.add(ReclaimVoteActive.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/NoVotesRemaining.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_UPVOTE_PARAMS, UpvoteInactive.get().getParams(pmSession, user, request, scopedResource));
		pMap.put(P_RECLAIM_VOTE_PARAMS, ReclaimVoteActive.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}