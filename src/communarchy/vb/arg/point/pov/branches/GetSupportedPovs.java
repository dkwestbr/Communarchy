package communarchy.vb.arg.point.pov.branches;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.GetPovsByStance;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.nodes.UserSupports;

@SuppressWarnings("rawtypes")
public class GetSupportedPovs extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {

	private static GetSupportedPovs INSTANCE;
	private GetSupportedPovs() {}

	public static final String P_POVS = "povs";
	public static final String P_NO_VOTES_REMAINING = "noVotesRemaining";
	
	public static GetSupportedPovs get() {
		if(INSTANCE == null) {
			INSTANCE = new GetSupportedPovs();
			INSTANCE.possiblePaths.add(UserSupports.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetSupportedPovs.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		GetPovsByStance query = new GetPovsByStance(scopedResource.getKey(), subset);
		List<PointOfView> povs = null;
		
		try {
			povs = pmSession.getMapper(QueryMapper.class).runListQuery(query);
		} catch (CommunarchyPersistenceException e) {
			e.printStackTrace();
		} finally {
			povs = povs == null ? new ArrayList<PointOfView>() : povs;
		}
		
		SoyListData povList = new SoyListData();
		for(IPointOfView pov : povs) {
			povList.add(UserSupports.get().getParams(pmSession, user, request, pov));
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}