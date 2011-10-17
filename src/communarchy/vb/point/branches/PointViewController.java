package communarchy.vb.point.branches;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.ISessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.point.nodes.ViewWithErrorsWrapper;
import communarchy.vb.point.nodes.ViewWrapper;

public class PointViewController extends AbstractTemplateWrapper implements IResourceTemplateWrapper<IArgument> {

	private static PointViewController INSTANCE;
	
	private PointViewController() {}
	
	public static PointViewController get() {
		if(INSTANCE == null) {
			INSTANCE = new PointViewController();
			INSTANCE.possiblePaths.add(ViewWrapper.get());
			INSTANCE.possiblePaths.add(ViewWithErrorsWrapper.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetPointView.soy";
	}

	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {

		HttpSession session = request.getSession();
		Map<String, Map<String, Object>> errors = (Map<String, Map<String, Object>>) session.getAttribute(ISessionConstants.ERRS_FOUND);
		
		if(request.getPathInfo().contains("arg/") && errors != null) {
			return ViewWithErrorsWrapper.get().getParams(pmSession, user, request, scopedResource);
		} else {
			return ViewWrapper.get().getParams(pmSession, user, request, scopedResource);
		}
	}
}