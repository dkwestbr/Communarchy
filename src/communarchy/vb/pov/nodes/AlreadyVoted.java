package communarchy.vb.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class AlreadyVoted extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static AlreadyVoted INSTANCE;
	private AlreadyVoted() {}
	
	public static final String P_UPVOTE_PARAMS = "upvoteParams";
	public static final String P_RECLAIM_VOTE_PARAMS = "reclaimVoteParams";
	
	public static AlreadyVoted get() {
		if(INSTANCE == null) {
			INSTANCE = new AlreadyVoted();
			INSTANCE.possiblePaths.add(UpvoteInactive.get());
			INSTANCE.possiblePaths.add(ReclaimVoteActive.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/pov/nodes/AlreadyVoted.soy";
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