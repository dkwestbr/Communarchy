package communarchy.vb.login.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.login.nodes.Message;

public class GetMessage extends AbstractTemplateWrapper implements IParamBuilder {

	private static GetMessage INSTANCE;
	private GetMessage() {}
	
	public static GetMessage get() {
		if(INSTANCE == null) {
			INSTANCE = new GetMessage();
			INSTANCE.possiblePaths.add(Message.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {

		SoyMapData pMap = new SoyMapData();
		if(request.getSession().getAttribute(IHttpSessionConstants.LOGIN_MESSAGE) != null) {
			pMap.put("message", Message.get().getParams(pmSession, user, request));
		}
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/login/branches/GetMessage.soy";
	}
}