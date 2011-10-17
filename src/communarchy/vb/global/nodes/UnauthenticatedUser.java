package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class UnauthenticatedUser extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static UnauthenticatedUser INSTANCE;
	private UnauthenticatedUser() {}
	
	public static UnauthenticatedUser get() {
		if(INSTANCE == null) {
			INSTANCE = new UnauthenticatedUser();
		}
		
		return INSTANCE;
	}
	
	private static final String P_USER_HREF = "userHref";
	private static final String P_USER_DISPLAY_NAME = "userDisplayName";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_USER_DISPLAY_NAME, user.getDisplayName());
		pMap.put(P_USER_HREF, user.getHref());
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/UnauthenticatedUser.soy";
	}
}