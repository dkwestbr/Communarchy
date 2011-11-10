package communarchy.vb.login.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class Message extends AbstractTemplateWrapper implements IParamBuilder {

	private static Message INSTANCE;
	private Message() {}
	
	public static Message get() {
		if(INSTANCE == null) {
			INSTANCE = new Message();
		}
		
		return INSTANCE;
	}
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {

		SoyMapData pMap = new SoyMapData();
		pMap.put("message", request.getSession().getAttribute(IHttpSessionConstants.LOGIN_MESSAGE));
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/login/nodes/Message.soy";
	}

}
