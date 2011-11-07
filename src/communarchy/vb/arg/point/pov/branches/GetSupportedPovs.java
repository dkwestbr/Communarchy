package communarchy.vb.arg.point.pov.branches;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.interfaces.IPointMapper;
import communarchy.rankingStrategies.PovRankStrategy;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.nodes.UserSupports;

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
		
		IPointMapper pointMapper = pmSession.getMapper(PointMapper.class);
		Integer voteCount = pointMapper.getVoteCount(scopedResource.getPointId(), user.getUserId());
		
		/*
		if(voteCount == pointMapper.getMaxVoteCount(scopedResource.getPointId(), subset)) {
			pMap.put(P_NO_VOTES_REMAINING, " ");
		}
		*/
		
		List<IPointOfView> povs = pointMapper.getPovsByStance(scopedResource.getPointId(), subset);
		Collections.sort(povs, new PovRankStrategy(user.getUserId(), pmSession));
		
		SoyListData povList = new SoyListData();
		for(IPointOfView pov : povs) {
			povList.add(UserSupports.get().getParams(pmSession, user, request, pov));
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}