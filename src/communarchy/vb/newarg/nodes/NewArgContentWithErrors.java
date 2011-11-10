package communarchy.vb.newarg.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.handlers.input.validation.ValidationResult;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.branches.GetErrors;

public class NewArgContentWithErrors extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<ValidationResult> {

	private static NewArgContentWithErrors INSTANCE;
	private NewArgContentWithErrors() {}
	
	public static NewArgContentWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgContentWithErrors();
			INSTANCE.possiblePaths.add(GetErrors.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/ContentWithErrors.soy";
	}
	
	private static final String P_CONTENT = "content";
	private static final String P_CONTENT_ERRORS = "contentErrors";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, ValidationResult scopedResource) {
	
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_CONTENT, scopedResource.getContent());
		pMap.put(P_CONTENT_ERRORS, GetErrors.get().getParams(pmSession, user, request, scopedResource.getErrors()));
		
		return pMap;
	}
}