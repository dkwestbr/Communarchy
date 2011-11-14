package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class UpvoteInactive extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UpvoteInactive INSTANCE;
	private UpvoteInactive() {}
	
	public static final String P_UPVOTE_ACTION = "upvoteAction";
	
	public static UpvoteInactive get() {
		if(INSTANCE == null) {
			INSTANCE = new UpvoteInactive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UpvoteInactive.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_UPVOTE_ACTION, 
				String.format("/pov/vote/up/%d", scopedResource.getKey().getId()));
		
		return pMap;
	}
}