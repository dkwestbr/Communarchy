package communarchy.vb.global.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.global.nodes.AuthenticatedUser;
import communarchy.vb.global.nodes.UnauthenticatedUser;

public class GetAccountManager extends AbstractTemplateWrapper implements
		IParamBuilder {

	private static GetAccountManager INSTANCE;
	private GetAccountManager() {}
	
	public static GetAccountManager get() {
		if(INSTANCE == null) {
			INSTANCE = new GetAccountManager();
			INSTANCE.possiblePaths.add(AuthenticatedUser.get());
			INSTANCE.possiblePaths.add(UnauthenticatedUser.get());
		}
		
		return INSTANCE;
	}
	
	private static final String P_IS_AUTHENTICATED = "isAuthenticated";
	private static final String P_USER_PARAMS = "userParams";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		
		SoyMapData pMap = new SoyMapData();
		
		if(user.isAuthenticated()) {
			pMap.put(P_IS_AUTHENTICATED, " ");
			pMap.put(P_USER_PARAMS, AuthenticatedUser.get().getParams(pmSession, user, request));
		} else {
			pMap.put(P_IS_AUTHENTICATED, "");
			pMap.put(P_USER_PARAMS, UnauthenticatedUser.get().getParams(pmSession, user, request));
		}
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/branches/GetAccountManager.soy"; 
	}
}