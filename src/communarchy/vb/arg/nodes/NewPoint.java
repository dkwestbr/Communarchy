package communarchy.vb.arg.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.nodes.ThickBorder;

@SuppressWarnings("rawtypes")
public class NewPoint extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static NewPoint INSTANCE;
	private NewPoint() {}
	
	public static NewPoint get() {
		if(INSTANCE == null) {
			INSTANCE = new NewPoint();
			INSTANCE.possiblePaths.add(ThickBorder.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/NewPoint.soy";
	}

	private static final String PARAM_NEW_POINT_ACTION = "newPointAction";
	private static final String PARAM_BORDER_SET = "borderSet";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument scopedResource) {
		
		SoyMapData params = new SoyMapData();
		params.put(PARAM_NEW_POINT_ACTION, String.format("/arg/point/new/%d", scopedResource.getArgId().getId()));
		params.put(PARAM_BORDER_SET, ThickBorder.get().getParams(pmSession, user, request));
		
		return params;
	}
}