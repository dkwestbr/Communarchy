package communarchy.vb.point.branches;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.ArgumentMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.point.nodes.PointContentWrapper;

public class PointsController extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static PointsController INSTANCE;
	private PointsController() {}
	
	public static final String P_POINT_SET = "pointSet";
	
	public static PointsController get() {
		if(INSTANCE == null) {
			INSTANCE = new PointsController();
			INSTANCE.possiblePaths.add(PointContentWrapper.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetPoints.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {
		
		SoyListData pointMaps = new SoyListData();
		List<IPoint> points = pmSession.getMapper(ArgumentMapper.class).selectChildrenPosts(scopedResource.getArgId());
		for(IPoint point : points) {
			pointMaps.add(PointContentWrapper.get().getParams(pmSession, user, 
					request, point));
		}
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_POINT_SET, pointMaps);
		
		return pMap;
	}
}