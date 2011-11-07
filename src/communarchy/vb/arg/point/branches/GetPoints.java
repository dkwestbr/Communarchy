package communarchy.vb.arg.point.branches;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.ArgumentMapper;
import communarchy.rankingStrategies.PointRankStrategy;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.nodes.PointSeperator;
import communarchy.vb.arg.point.nodes.PointView;

public class GetPoints extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static GetPoints INSTANCE;
	private GetPoints() {}
	
	public static GetPoints get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPoints();
			INSTANCE.possiblePaths.add(PointView.get());
			INSTANCE.possiblePaths.add(PointSeperator.get());
		}
		
		return INSTANCE;
	}
	 
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetPoints.soy";
	}

	private static final String P_POINT_SET = "pointSet";
	private static final String P_POINT_SEPERATOR = "pointSeperator";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		List<IPoint> points = pmSession.getMapper(ArgumentMapper.class).selectChildrenPosts(scopedResource.getArgId());
		Collections.sort(points, new PointRankStrategy(pmSession));
		
		SoyListData pointList = new SoyListData();
		for(IPoint point : points) {
			pointList.add(PointView.get().getParams(pmSession, user, request, point));
		}
		
		pMap.put(P_POINT_SEPERATOR, PointSeperator.get().getParams(pmSession, user, request));
		pMap.put(P_POINT_SET, pointList);
		
		return pMap;
	}
}