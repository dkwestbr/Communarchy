package communarchy.vb.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class UserDoesntSupport extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UserDoesntSupport INSTANCE;
	private UserDoesntSupport() {}
	
	public static final String P_ID = "id";
	public static final String P_CONTENT = "content";
	public static final String P_POV_STATS = "povStats";
	
	public static UserDoesntSupport get() {
		if(INSTANCE == null) {
			INSTANCE = new UserDoesntSupport();
			INSTANCE.possiblePaths.add(PovStats.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/pov/nodes/UserDoesntSupport.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, scopedResource.getPovId());
		pMap.put(P_CONTENT, scopedResource.getPov());
		pMap.put(P_POV_STATS, PovStats.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}