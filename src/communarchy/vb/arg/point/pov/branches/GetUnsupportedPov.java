package communarchy.vb.arg.point.pov.branches;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.strategies.displayRank.PovRankStrategy;
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
		Integer differential = pmSession.getMapper(CountMapper.class).getCount(new GetStanceCount(scopedResource.getKey(), subset));
		if(differential > 1) {
			differential = (int) Math.floor(Math.sqrt(differential));
		} else {
			differential = 0;
		}
		List<PointOfView> povs = pmSession.getMapper(QueryMapper.class).runListQuery(new GetPovsByStance(scopedResource.getKey(), subset));
		Collections.sort(povs, new PovRankStrategy(pmSession));
		for(IPointOfView pov : povs) {
			Integer voteCount = pmSession.getMapper(CountMapper.class).getCount(new GetVoteCountQuery(pov.getKey()));
			if(voteCount < differential) {
				break;
			}
			
			povList.add(UserDoesntSupport.get().getParams(pmSession, user, request, pov));
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}