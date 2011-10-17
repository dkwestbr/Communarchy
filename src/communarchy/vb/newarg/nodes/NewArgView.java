package communarchy.vb.newarg.nodes;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.inputValidation.ValidationResult;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.branches.GetErrors;

public class NewArgView extends AbstractTemplateWrapper implements IResourceTemplateWrapper<IArgument> {

	private static NewArgView INSTANCE;
	
	public static NewArgView get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgView();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/NewArg.soy";
	}
	
	private static final String P_CREATE_ARG_ACTION = "createArgAction";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {
		
		SoyMapData paramMap = new SoyMapData();
		paramMap.put(P_CREATE_ARG_ACTION, "/arg/new");
		
		List<ValidationResult> results = (List<ValidationResult>) request.getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG);
		for(ValidationResult result : results) {
			paramMap.put(result.getName(), result.getContent());
			paramMap.put(String.format("%sErrors", result.getName()), GetErrors.get().getParams(pmSession, user, request, result.getErrors()));
		}
		
		return paramMap;
	}
}