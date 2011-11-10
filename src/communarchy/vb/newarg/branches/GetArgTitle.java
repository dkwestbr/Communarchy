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
import communarchy.vb.newarg.nodes.NewArgTitle;
import communarchy.vb.newarg.nodes.NewArgTitleWithErrors;

public class GetArgTitle extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static GetArgTitle INSTANCE;
	private GetArgTitle() {}
	
	public static GetArgTitle get() {
		if(INSTANCE == null) {
			INSTANCE = new GetArgTitle();
			INSTANCE.possiblePaths.add(NewArgTitle.get());
			INSTANCE.possiblePaths.add(NewArgTitleWithErrors.get());
		}
		
		return INSTANCE;
	}
	
	private static final String P_HAS_ERRORS = "hasErrors";
	private static final String P_TITLE_PARAMS = "titleParams";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {

		SoyMapData pMap = new SoyMapData();
		Map<String, ValidationResult> results = (Map<String, ValidationResult>) request.getSession().getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG);
		
		if(results != null) {
			ValidationResult title = results.get("title");
			if(title.isValid()) {
				pMap.put(P_HAS_ERRORS, "");
				pMap.put(P_TITLE_PARAMS, NewArgTitle.get().getParams(pmSession, user, request, title.getContent()));
			} else {
				pMap.put(P_HAS_ERRORS, "true");
				pMap.put(P_TITLE_PARAMS, NewArgTitleWithErrors.get().getParams(pmSession, user, request, title));
			}
		} else {
			pMap.put(P_HAS_ERRORS, "");
			pMap.put(P_TITLE_PARAMS, NewArgTitle.get().getParams(pmSession, user, request, ""));
		}
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/newarg/branches/GetArgTitle.soy";
	}
}
