package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.branches.GetUnsupportedPov;

@SuppressWarnings("rawtypes")
public class UserAbstains extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<IPoint, Integer> {

	private static UserAbstains INSTANCE;
	private UserAbstains() {}
	
	private static final String P_POVS = "povs";
	
	public static UserAbstains get() {
		if(INSTANCE == null) {
			INSTANCE = new UserAbstains();
			INSTANCE.possiblePaths.add(GetUnsupportedPov.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UserAbstains.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_POVS, GetUnsupportedPov.get()
				.getParams(pmSession, user, request, scopedResource, subset));
		
		return pMap;
	}
}