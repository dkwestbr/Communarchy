package communarchy.vb.arg.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class NewPoint extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static NewPoint INSTANCE;
	private NewPoint() {}
	
	private static String PARAM_NEW_POINT_ACTION = "newPointAction";
	
	public static NewPoint get() {
		if(INSTANCE == null) {
			INSTANCE = new NewPoint();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/NewPoint.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument scopedResource) {
		
		SoyMapData params = new SoyMapData();
		params.put(PARAM_NEW_POINT_ACTION, String.format("/arg/%s", scopedResource.getArgId().toString()));
		
		return params;
	}
}