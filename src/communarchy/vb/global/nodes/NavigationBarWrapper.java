package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.global.branches.GetAccountManager;

public class NavigationBarWrapper extends AbstractTemplateWrapper implements IParamBuilder {

	private static NavigationBarWrapper INSTANCE;
	private SoyMapData rootArgBar;
	
	private static final String TEMPL_KEY_CREATE_ARG_ACTION = "createArgAction";
	private static final String TEMPL_KEY_MAIN_ACTION = "mainAction";
	private static final String TEMPL_KEY_ACCOUNT_MANAGER = "accountManagerParams";
	
	private NavigationBarWrapper() {}
	
	public static NavigationBarWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new NavigationBarWrapper();
			INSTANCE.possiblePaths.add(GetAccountManager.get());
			INSTANCE.rootArgBar = new SoyMapData();
			INSTANCE.rootArgBar.put(TEMPL_KEY_CREATE_ARG_ACTION, "/arg/new");
			INSTANCE.rootArgBar.put(TEMPL_KEY_MAIN_ACTION, "/");
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/NavigationBar.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		SoyMapData argBar = new SoyMapData(rootArgBar.asMap());
		argBar.put(TEMPL_KEY_ACCOUNT_MANAGER, GetAccountManager.get().getParams(pmSession, user, request));
		
		return argBar;
	}
}
