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
import communarchy.vb.point.nodes.UserAbstains;

public class GetPovView extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<IPoint, Integer> {

	private static GetPovView INSTANCE;
	private GetPovView() {}
	
	public static final String P_POV_SET = "povSet";
	
	public static GetPovView get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovView();
			INSTANCE.possiblePaths.add(UserAbstains.get());
			INSTANCE.possiblePaths.add(GetSupportedPov.get());
			INSTANCE.possiblePaths.add(GetUnsupportedPov.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetPovView.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		IUserStance userStance = pmSession.getMapper(PointMapper.class).selectStance(scopedResource.getPointId(), user.getUserId());
		if(userStance == null) {
			pMap.put(P_POV_SET, UserAbstains.get()
					.getParams(pmSession, user, request, scopedResource, subset));
		} else if(userStance.getStance() == subset) {
			pMap.put(P_POV_SET, GetSupportedPov.get()
					.getParams(pmSession, user, request, scopedResource, subset));
		} else {
			pMap.put(P_POV_SET, GetUnsupportedPov.get().getParams(pmSession, user, request, scopedResource, subset));
		}
		
		return pMap;
	}
}