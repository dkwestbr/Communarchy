package communarchy.vb.login;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.IRootTemplate;
import communarchy.vb.global.branches.HeaderWrapper;
import communarchy.vb.login.branches.GetMessage;

public class LoginRoot extends AbstractTemplateWrapper implements IRootTemplate, IParamBuilder {

	private static LoginRoot INSTANCE;
	private LoginRoot() {}
	
	public static LoginRoot get() {
		if(INSTANCE == null) {
			INSTANCE = new LoginRoot();
			INSTANCE.possiblePaths.add(HeaderWrapper.get());
			INSTANCE.possiblePaths.add(GetMessage.get());
		}
		
		return INSTANCE;
	}

	public static final String P_ACTION = "action";
	public static final String P_HEADER_PARAMS = "headerParams";
	public static final String P_MESSAGE_PARAMS = "messageParams";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_ACTION, "/login");
		pMap.put(P_HEADER_PARAMS, HeaderWrapper.get().getParams(LoginRoot.class));
		pMap.put(P_MESSAGE_PARAMS, GetMessage.get().getParams(pmSession, user, request));
		
		return pMap;
	}

	@Override
	public String getRenderTarget() {
		return "communarchy.templates.html.login.Root";
	}

	private static SoyListData SCRIPTS;

	@Override
	public SoyListData getScripts() {
		if(SCRIPTS == null) {
			SCRIPTS = new SoyListData();
		}
		
		return SCRIPTS;
	}
	
	private static SoyListData STYLES;

	@Override
	public SoyListData getStlyes() {
		if(STYLES == null) {
			STYLES = new SoyListData();
			
			STYLES.add(new SoyMapData("styleSheet", "/dbg/css/global.css"));
			STYLES.add(new SoyMapData("styleSheet", "/dbg/css/login.css"));
		}
		
		return STYLES;
	}

	@Override
	public void init(ServletContext context) {
		Builder builder = new SoyFileSet.Builder();
		registerTemplates(builder, new HashMap<String, String>());
		
		SoyFileSet loginSet = builder.build();
		context.setAttribute(IHttpSessionConstants.LOGIN_VIEW_TEMPLATE, loginSet.compileToJavaObj());
	}

	@Override
	public String getTemplate() {
		return "./templates/html/login/LoginRoot.soy";
	}
}