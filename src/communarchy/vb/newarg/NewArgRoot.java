package communarchy.vb.newarg;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.IRootTemplate;
import communarchy.vb.arg.branches.GetArgView;
import communarchy.vb.global.branches.HeaderWrapper;
import communarchy.vb.global.nodes.NavigationBarWrapper;

public class NewArgRoot extends AbstractTemplateWrapper implements IRootTemplate, IResourceTemplateWrapper<IArgument> {
	
	private static NewArgRoot INSTANCE;
	private NewArgRoot() {}
	
	private static final String PARAM_KEY_ARGBAR = "argBarData";
	private static final String PARAM_KEY_VIEW = "viewData";
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
			INSTANCE.possiblePaths.add(GetArgView.get());
			INSTANCE.possiblePaths.add(HeaderWrapper.get());
		}
		
		return INSTANCE;
	}
	
	public String getTemplate() {
		return "./templates/html/newarg/NewArgRoot.soy";
	}

	@Override
	public String getRenderTarget() {
		return "communarchy.templates.html.newarg.Root";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument arg) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(PARAM_KEY_ARGBAR, NavigationBarWrapper.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_VIEW, GetArgView.get().getParams(pmSession, user, request, arg));
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