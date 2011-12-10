package communarchy.vb.arg.point.pov.branches;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.GetPovsByStance;
import communarchy.facts.queries.list.GetStanceCount;
import communarchy.facts.queries.list.GetVoteCountQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.nodes.UserDoesntSupport;

@SuppressWarnings("rawtypes")
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
		
		try {
			Integer differential = pmSession.getMapper(CountMapper.class).getCount(new GetStanceCount(scopedResource.getKey(), subset));
			if(differential > 1) {
				differential = (int) Math.floor(Math.sqrt(differential));
			} else {
				differential = 0;
			}
			List<PointOfView> povs = pmSession.getMapper(QueryMapper.class).runListQuery(new GetPovsByStance(scopedResource.getKey(), subset));
			for(IPointOfView pov : povs) {
				Integer voteCount = pmSession.getMapper(CountMapper.class).getCount(new GetVoteCountQuery(pov.getKey()));
				if(voteCount < differential) {
					break;
				}
				
				povList.add(UserDoesntSupport.get().getParams(pmSession, user, request, pov));
			}
		} catch(CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}