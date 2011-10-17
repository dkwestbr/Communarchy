package communarchy.vb.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.nodes.NewPoint;
import communarchy.vb.point.branches.PointsController;

public class ViewWrapper extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static ViewWrapper INSTANCE;
	private ViewWrapper() {}
	
	private static String PARAM_POINT_SET = "pointSet";
	private static String PARAM_INPUT_SET = "pointInputSet";
	
	public static ViewWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new ViewWrapper();
			INSTANCE.possiblePaths.add(NewPoint.get());
			INSTANCE.possiblePaths.add(PointsController.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/View.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(PARAM_POINT_SET, PointsController.get().getParams(pmSession, user, request, scopedResource));
		pMap.put(PARAM_INPUT_SET, NewPoint.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}