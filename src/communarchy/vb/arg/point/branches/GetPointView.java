package communarchy.vb.arg.point.branches;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.inputValidation.ValidationResult;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.nodes.NewPoint;
import communarchy.vb.arg.nodes.NewPointWithErrors;

public class GetPointView extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IArgument> {

	private static GetPointView INSTANCE;
	private GetPointView() {}
	
	public static GetPointView get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPointView();
			INSTANCE.possiblePaths.add(GetPoints.get());
			INSTANCE.possiblePaths.add(NewPoint.get());
			INSTANCE.possiblePaths.add(NewPointWithErrors.get());
		}
		
		return INSTANCE;
	}
	
	private static final String P_ERROR_FLAG = "hasErrors";
	private static final String P_POINT_SET = "pointSet";
	private static final String P_POINT_INPUT_SET = "pointInputSet";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {

		Map<String, ValidationResult> results = (Map<String, ValidationResult>) request.getSession().getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_POINT);
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_POINT_SET, GetPoints.get().getParams(pmSession, user, request, scopedResource));
		
		if(results != null && results.containsKey("point")) {
			pMap.put(P_ERROR_FLAG, "true");
			pMap.put(P_POINT_INPUT_SET, NewPointWithErrors.get().getParams(pmSession, user, request, results.get("point")));
			request.getSession().removeAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_POINT);
		} else {
			pMap.put(P_ERROR_FLAG, "");
			pMap.put(P_POINT_INPUT_SET, NewPoint.get().getParams(pmSession, user, request, scopedResource));
		}
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetPointView.soy";
	}
}