package communarchy.vb.arg.point.pov.branches;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.handlers.input.validation.ValidationResult;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.pov.nodes.PovInput;

public class GetPovViewInput extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static GetPovViewInput INSTANCE;
	private GetPovViewInput() {}
	
	public static final String P_NEW_POV = "newPov";
	
	public static GetPovViewInput get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovViewInput();
			INSTANCE.possiblePaths.add(PovInputWithErrors.get());
			INSTANCE.possiblePaths.add(PovInput.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetPovViewInput.soy";
	}

	private static final String P_HAS_ERRORS = "hasErrors";
	private static final String P_POV_SET = "povSet";
	private static final String P_STANCE_TAKEN = "stanceTaken";
	
	@Override
	@SuppressWarnings("unchecked")
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		if (user.isAuthenticated() && user.getUserId() != null &&
				pmSession.getMapper(UniqueEntityMapper.class).getUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId())) != null) {
		
			pMap.put(P_STANCE_TAKEN, "true");
			Map<String, ValidationResult> results = (Map<String, ValidationResult>) request.getSession().getAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_POV);
			if(results != null && results.get("pov") != null && !results.get("pov").isValid()) {
				pMap.put(P_HAS_ERRORS, "true");
				pMap.put(P_POV_SET, PovInputWithErrors.get().getParams(pmSession, user, request, scopedResource));
			} else {
				
				pMap.put(P_HAS_ERRORS, "");
				pMap.put(P_POV_SET, PovInput.get().getParams(pmSession, user, request, scopedResource));
			}
		} else {
			pMap.put(P_STANCE_TAKEN, "");
			pMap.put(P_HAS_ERRORS, "");
			pMap.put(P_POV_SET, "");
		}
		
		return pMap;
	}
}