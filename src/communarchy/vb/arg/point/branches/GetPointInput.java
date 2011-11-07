package communarchy.vb.arg.point.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class GetPointInput extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static GetPointInput INSTANCE;
	private GetPointInput() {}
	
	public static GetPointInput get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPointInput();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetPointInput.soy";
	}

	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		
		SoyMapData pMap = new SoyMapData();
/*
 * 
 * 
 *      IF YOU DON't remember what this is; just delete this whole class
 * 
 * 
 * 
 * 		
		Map<String, ValidationResult> results = (Map<String, ValidationResult>) request.getSession().getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_POINT);
		if(results != null) {
			ValidationResult title = results.get("point");
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
*/		
		return pMap;
	}
}