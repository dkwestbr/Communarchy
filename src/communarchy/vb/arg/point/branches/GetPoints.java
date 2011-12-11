package communarchy.vb.arg.point.branches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.strategies.displayRank.PointRankStrategy;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.PointsByArgument;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.nodes.PointView;
import communarchy.vb.global.nodes.ThickBorder;

public class GetPoints extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Argument> {

	private static GetPoints INSTANCE;
	private GetPoints() {}
	
	public static GetPoints get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPoints();
			INSTANCE.possiblePaths.add(PointView.get());
			INSTANCE.possiblePaths.add(ThickBorder.get());
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
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		List<Point> points = null;
		try {
			points = pmSession.getMapper(QueryMapper.class).runListQuery(new PointsByArgument(scopedResource.getArgId()));
		} catch (CommunarchyPersistenceException e) {
			e.printStackTrace();
		} finally {
			points = points == null ? new ArrayList<Point>() : points;
		}
		
		Collections.sort(points, new PointRankStrategy(pmSession));
		
		SoyListData pointList = new SoyListData();
		for(Point point : points) {
			pointList.add(PointView.get().getParams(pmSession, user, request, point));
		}
		
		pMap.put(P_POINT_SEPERATOR, ThickBorder.get().getParams(pmSession, user, request));
		pMap.put(P_POINT_SET, pointList);
		
		return pMap;
	}
}