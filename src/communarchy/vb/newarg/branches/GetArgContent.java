package communarchy.vb.newarg.branches;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.handlers.input.validation.ValidationResult;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.newarg.nodes.NewArgContent;
import communarchy.vb.newarg.nodes.NewArgContentWithErrors;

public class GetArgContent extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static GetArgContent INSTANCE;
	private GetArgContent() {}
	
	public static GetArgContent get() {
		if(INSTANCE == null) {
			INSTANCE = new GetArgContent();
			INSTANCE.possiblePaths.add(NewArgContent.get());
			INSTANCE.possiblePaths.add(NewArgContentWithErrors.get());
		}
		
		return INSTANCE;
	}
	
	private static final String P_HAS_ERRORS = "hasErrors";
	private static final String P_CONTENT_PARAMS = "contentParams";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {

		SoyMapData pMap = new SoyMapData();
		Map<String, ValidationResult> results = (Map<String, ValidationResult>) request.getSession().getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG);
		
		if(results != null) {
			ValidationResult content = results.get("content");
			if(content.isValid()) {
				pMap.put(P_HAS_ERRORS, "");
				pMap.put(P_CONTENT_PARAMS, NewArgContent.get().getParams(pmSession, user, request, content.getContent()));
			} else {
				pMap.put(P_HAS_ERRORS, "true");
				pMap.put(P_CONTENT_PARAMS, NewArgContentWithErrors.get().getParams(pmSession, user, request, content));
			}
			request.getSession().removeAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG);
		} else {
			pMap.put(P_HAS_ERRORS, "");
			pMap.put(P_CONTENT_PARAMS, NewArgContent.get().getParams(pmSession, user, request, ""));
		}
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/newarg/branches/GetArgContent.soy";
	}
}