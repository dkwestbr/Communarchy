package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class ReclaimVoteInactive extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static ReclaimVoteInactive INSTANCE;
	private ReclaimVoteInactive() {}
	
	public static final String P_RECLAIM_VOTE_ACTION = "reclaimvoteAction";
	
	public static ReclaimVoteInactive get() {
		if(INSTANCE == null) {
			INSTANCE = new ReclaimVoteInactive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/ReclaimVoteInactive.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		pMap.put(P_RECLAIM_VOTE_ACTION, String.format("/pov/vote/reclaim/%d", scopedResource.getPovId().getId()));
		return pMap;
	}
}