package communarchy.vb.point.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.PointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.point.nodes.HeaderUserDoesntSupport;
import communarchy.vb.point.nodes.HeaderUserSupports;

public class GetPovViewHeaderWrapper extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {

	private static GetPovViewHeaderWrapper INSTANCE;
	private GetPovViewHeaderWrapper() {}
	
	public static final String P_POV_SET = "povHeaderSet";
	public static final String P_USER_SUPPORTS = "userSupports";
	
	public static GetPovViewHeaderWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovViewHeaderWrapper();
			INSTANCE.possiblePaths.add(HeaderUserSupports.get());
			INSTANCE.possiblePaths.add(HeaderUserDoesntSupport.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetPovViewHeader.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
	
		SoyMapData pMap = new SoyMapData();
		
		IUserStance userStance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getPointId(), user.getUserId());
		
		if(userStance.getStance() == subset) {
			pMap.put(P_POV_SET, HeaderUserSupports.get().getParams(pmSession, user, request, 
																	scopedResource));
			
			pMap.put(P_USER_SUPPORTS, " ");
		} else {
			pMap.put(P_POV_SET, HeaderUserDoesntSupport.get().getParams(pmSession, user, request, 
					scopedResource));
		}
		
		return pMap;
	}
}