package communarchy.vb.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class FlagInactive extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static FlagInactive INSTANCE;
	private FlagInactive() {}
	
	public static final String P_FLAG_ACTION = "flagAction";
	public static final String P_FLAG_LABEL = "flagLabel";
	
	public static FlagInactive get() {
		if(INSTANCE == null) {
			INSTANCE = new FlagInactive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/pov/nodes/FlagInactive.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		return pMap;
	}
}