package communarchy.vb.arg.nodes;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.inputValidation.ValidationResult;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.branches.GetErrors;
import communarchy.vb.global.nodes.ThickBorder;

public class NewPointWithErrors extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<ValidationResult> {

	private static NewPointWithErrors INSTANCE;
	private NewPointWithErrors() {}
	
	public static NewPointWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new NewPointWithErrors();
			INSTANCE.possiblePaths.add(ThickBorder.get());
			INSTANCE.possiblePaths.add(GetErrors.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/NewPointWithErrors.soy";
	}

	private static final String PARAM_NEW_POINT_ACTION = "newPointAction";
	private static final String PARAM_BORDER_SET = "borderSet";
	private static final String PARAM_POINT = "point";
	private static final String PARAM_POINT_ERRORS = "pointErrors";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, ValidationResult scopedResource) {
	
		Pattern argIdPattern = Pattern.compile("/arg/([0-9]+)");
		Matcher argIdMatcher = argIdPattern.matcher(request.getRequestURI());
		Long id = null;
		if(argIdMatcher.find()) {
			id = Long.parseLong(argIdMatcher.group(1));
		}
		
		SoyMapData paramMap = new SoyMapData();
		paramMap.put(PARAM_NEW_POINT_ACTION, String.format("/arg/point/new/%d", id));
		
		HttpSession session = request.getSession();
		
		Map<String, ValidationResult> requiredParamMap = (Map<String, ValidationResult>) session.getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_POINT);
		ValidationResult pointErrors = requiredParamMap.get("point");
		
		paramMap.put(PARAM_POINT_ERRORS, GetErrors.get().getParams(pmSession, user, request, pointErrors.getErrors()));
		paramMap.put(PARAM_POINT, pointErrors.getContent());
		paramMap.put(PARAM_BORDER_SET, ThickBorder.get().getParams(pmSession, user, request));
		
		return paramMap;
	}
}