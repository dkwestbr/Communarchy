package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class UpvoteActiveDisabled extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UpvoteActiveDisabled INSTANCE;
	private UpvoteActiveDisabled() {}
	
	public static final String P_UPVOTE_ACTION = "upvoteAction";
	
	public static UpvoteActiveDisabled get() {
		if(INSTANCE == null) {
			INSTANCE = new UpvoteActiveDisabled();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UpvoteActiveDisabled.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_UPVOTE_ACTION, 
				String.format("/pov/vote/up/%d", scopedResource.getPovId().getId()));
		
		return pMap;
	}
}