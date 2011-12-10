package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

@SuppressWarnings("rawtypes")
public class Flags extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static Flags INSTANCE;
	private Flags() {}
	
	public static Flags get() {
		if(INSTANCE == null) {
			INSTANCE = new Flags();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/Flags.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		// TODO Auto-generated method stub
		return null;
	}
}