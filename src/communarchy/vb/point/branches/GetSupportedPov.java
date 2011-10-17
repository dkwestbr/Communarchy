package communarchy.vb.point.branches;

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
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.pov.nodes.UserSupports;

public class GetSupportedPov extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {

	private static GetSupportedPov INSTANCE;
	private GetSupportedPov() {}

	public static final String P_POVS = "povs";
	public static final String P_NO_VOTES_REMAINING = "noVotesRemaining";
	
	public static GetSupportedPov get() {
		if(INSTANCE == null) {
			INSTANCE = new GetSupportedPov();
			INSTANCE.possiblePaths.add(UserSupports.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetSupportedPov.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		IPointMapper pointMapper = pmSession.getMapper(PointMapper.class);
		Integer voteCount = pointMapper.getVoteCount(scopedResource.getPointId(), user.getUserId());
		if(voteCount == pointMapper.getMaxVoteCount(scopedResource.getPointId(), subset)) {
			pMap.put(P_NO_VOTES_REMAINING, " ");
		}
		
		List<IPointOfView> povs = pointMapper.getPovsByStance(scopedResource.getPointId(), subset);
		SoyListData povList = new SoyListData();
		
		for(IPointOfView pov : povs) {
			povList.add(UserSupports.get().getParams(pmSession, user, request, pov));
		}
		
		pMap.put(P_POVS, povList);
		
		return pMap;
	}
}