package communarchy.vb.arg.point.pov.branches;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.PointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.nodes.UserDoesntSupport;

public class GetUnsupportedPov extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {

	private static GetUnsupportedPov INSTANCE;
	private GetUnsupportedPov() {}
	
	public static final String P_POVS = "povs";
	
	public static GetUnsupportedPov get() {
		if(INSTANCE == null) {
			INSTANCE = new GetUnsupportedPov();
			INSTANCE.possiblePaths.add(UserDoesntSupport.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetUnsupportedPovs.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {

		SoyMapData pMap = new SoyMapData();
	
		SoyListData povList = new SoyListData();
		List<IPointOfView> povs = pmSession.getMapper(PointMapper.class).getPovsByStance(scopedResource.getPointId(), subset);
		for(IPointOfView pov : povs) {
			povList.add(UserDoesntSupport.get().getParams(pmSession, user, request, pov));
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}