package communarchy.vb.global.nodes;

import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetUserCountQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;
import communarchy.vb.global.branches.GetAccountManager;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

public class NavigationBarWrapper extends AbstractTemplateWrapper implements IParamBuilder {

	private static final Logger log =
		      Logger.getLogger(NavigationBarWrapper.class.getName());
	
	private static NavigationBarWrapper INSTANCE;
	private SoyMapData rootArgBar;
	
	private static final String TEMPL_KEY_CREATE_ARG_ACTION = "createArgAction";
	private static final String TEMPL_KEY_MAIN_ACTION = "mainAction";
	private static final String TEMPL_KEY_ACCOUNT_MANAGER = "accountManagerParams";
	private static final String TEMPL_KEY_USER_COUNT = "userCount";
	
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
		Integer userCount = 0;
		
		try {
			userCount = pmSession.getMapper(CountMapper.class).getCount(new GetUserCountQuery());
		} catch (CommunarchyPersistenceException e) {
			log.warning(e.getMessage());
			e.printStackTrace();
		}
		
		argBar.put(TEMPL_KEY_USER_COUNT, NumberFormatter.CommaSeparatedNumber(userCount, Locale.US));
		
		return argBar;
	}
}
