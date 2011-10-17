package communarchy.vb.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class FlagSpacer extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static FlagSpacer INSTANCE;
	private FlagSpacer() {}
	
	public static FlagSpacer get() {
		if(INSTANCE == null) {
			INSTANCE = new FlagSpacer();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/pov/nodes/FlagSpacer.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		// TODO Auto-generated method stub
		return null;
	}
}