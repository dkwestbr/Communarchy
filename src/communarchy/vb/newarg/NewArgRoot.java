package communarchy.vb.newarg;

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
import communarchy.vb.global.nodes.NavigationBarWrapper;
import communarchy.vb.newarg.nodes.NewArgView;

// testing change from eclipse
public class NewArgRoot extends AbstractTemplateWrapper implements IRootTemplate, IParamBuilder {
	
	private static NewArgRoot INSTANCE;
	private NewArgRoot() {}
	
	private static final String PARAM_KEY_ARGBAR = "argBarData";
	private static final String PARAM_KEY_VIEW = "newArgData";
	private static final String PARAM_KEY_HEADER = "headerParams";
	
	@Override
	public void init(ServletContext context) {
		Builder builder = new SoyFileSet.Builder();
		registerTemplates(builder, new HashMap<String, String>());
		
		SoyFileSet argSet = builder.build();
		context.setAttribute(IHttpSessionConstants.NEW_ARG_VIEW_TEMPLATE, argSet.compileToJavaObj());
	}
	
	public static NewArgRoot get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgRoot();
			INSTANCE.possiblePaths.add(NavigationBarWrapper.get());
			INSTANCE.possiblePaths.add(NewArgView.get());
			INSTANCE.possiblePaths.add(HeaderWrapper.get());
		}
		
		return INSTANCE;
	}
	
	public String getTemplate() {
		return "./templates/html/newarg/NewArgRoot.soy";
	}

	@Override
	public String getRenderTarget() {
		return "communarchy.templates.html.newArg.Root";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(PARAM_KEY_ARGBAR, NavigationBarWrapper.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_VIEW, NewArgView.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_HEADER, HeaderWrapper.get().getParams(NewArgRoot.class));
		return pMap;
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
			STYLES.add(new SoyMapData("styleSheet", "/dbg/css/newarg.css"));
		}
		
		return STYLES;
	}
}