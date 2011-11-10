package communarchy.vb.newarg.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.inputValidation.ValidationResult;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.branches.GetErrors;

public class NewArgTitleWithErrors extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<ValidationResult> {

	private static NewArgTitleWithErrors INSTANCE;
	private NewArgTitleWithErrors() {}
	
	public static NewArgTitleWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgTitleWithErrors();
			INSTANCE.possiblePaths.add(GetErrors.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/TitleWithErrors.soy";
	}
	
	private static final String P_TITLE = "title";
	private static final String P_TITLE_ERRORS = "titleErrors";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, ValidationResult scopedResource) {
	
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_TITLE, scopedResource.getContent());
		pMap.put(P_TITLE_ERRORS, GetErrors.get().getParams(pmSession, user, request, scopedResource.getErrors()));
		
		return pMap;
	}
}