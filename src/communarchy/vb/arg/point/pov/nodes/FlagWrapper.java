package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class FlagWrapper extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static FlagWrapper INSTANCE;
	private FlagWrapper() {}
	
	public static FlagWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new FlagWrapper();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/Flag.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		// TODO Auto-generated method stub
		return null;
	}
}