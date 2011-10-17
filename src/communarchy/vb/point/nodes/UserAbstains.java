package communarchy.vb.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.point.branches.GetUnsupportedPov;

public class UserAbstains extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<IPoint, Integer> {

	private static UserAbstains INSTANCE;
	private UserAbstains() {}
	
	public static final String P_POVS = "povs";
	public static final String P_POV_ROOT = "povRoot";
	
	public static UserAbstains get() {
		if(INSTANCE == null) {
			INSTANCE = new UserAbstains();
			INSTANCE.possiblePaths.add(GetUnsupportedPov.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/UserAbstains.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_POVS, GetUnsupportedPov.get()
				.getParams(pmSession, user, request, scopedResource, subset));
		pMap.put(P_POV_ROOT, UserStance.getStanceUrlPath(subset));
		
		return pMap;
	}
}