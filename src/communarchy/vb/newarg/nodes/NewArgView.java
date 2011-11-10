package communarchy.vb.newarg.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.newarg.branches.GetArgContent;
import communarchy.vb.newarg.branches.GetArgTitle;

public class NewArgView extends AbstractTemplateWrapper implements IParamBuilder {

	private static NewArgView INSTANCE;
	
	public static NewArgView get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgView();
			INSTANCE.possiblePaths.add(GetArgContent.get());
			INSTANCE.possiblePaths.add(GetArgTitle.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/NewArg.soy";
	}

	private static final String P_CREATE_ARG_ACTION = "createArgAction";
	private static final String P_TITLE_PARAMS = "titleParams";
	private static final String P_CONTENT_PARAMS = "contentParams";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		SoyMapData paramMap = new SoyMapData();
		
		paramMap.put(P_CREATE_ARG_ACTION, "/arg/new");
		paramMap.put(P_TITLE_PARAMS, GetArgTitle.get().getParams(pmSession, user, request));
		paramMap.put(P_CONTENT_PARAMS, GetArgContent.get().getParams(pmSession, user, request));
		request.getSession().removeAttribute(IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG);
		
		return paramMap;
	}
}