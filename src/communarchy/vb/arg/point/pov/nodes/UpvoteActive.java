package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

@SuppressWarnings("rawtypes")
public class UpvoteActive extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UpvoteActive INSTANCE;
	private UpvoteActive() {}
	
	public static final String P_UPVOTE_ACTION = "upvoteAction";
	
	public static UpvoteActive get() {
		if(INSTANCE == null) {
			INSTANCE = new UpvoteActive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UpvoteActive.soy";
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
