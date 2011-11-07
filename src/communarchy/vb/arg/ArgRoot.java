package communarchy.vb.arg;

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
import communarchy.vb.arg.nodes.ArgViewWrapper;
import communarchy.vb.global.branches.HeaderWrapper;
import communarchy.vb.global.nodes.NavigationBarWrapper;

public class ArgRoot extends AbstractTemplateWrapper implements IRootTemplate, IResourceTemplateWrapper<IArgument> {
	
	private static ArgRoot INSTANCE;
	private ArgRoot() {}
	
	private static final String PARAM_KEY_ARGBAR = "argBarData";
	private static final String PARAM_KEY_VIEW = "viewData";
	private static final String PARAM_KEY_HEADER = "headerParams";
	
	@Override
	public void init(ServletContext context) {
		Builder builder = new SoyFileSet.Builder();
		registerTemplates(builder, new HashMap<String, String>());
		
		SoyFileSet argSet = builder.build();
		context.setAttribute(IHttpSessionConstants.ARG_VIEW_TEMPLATE, argSet.compileToJavaObj());
	}
	
	public static ArgRoot get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgRoot();
			INSTANCE.possiblePaths.add(NavigationBarWrapper.get());
			INSTANCE.possiblePaths.add(ArgViewWrapper.get());
			INSTANCE.possiblePaths.add(HeaderWrapper.get());
		}
		
		return INSTANCE;
	}
	
	public String getTemplate() {
		return "./templates/html/arg/ArgRoot.soy";
	}

	@Override
	public String getRenderTarget() {
		return "communarchy.templates.html.arg.Root";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IArgument arg) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(PARAM_KEY_ARGBAR, NavigationBarWrapper.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_VIEW, ArgViewWrapper.get().getParams(pmSession, user, request, arg));
		pMap.put(PARAM_KEY_HEADER, HeaderWrapper.get().getParams(ArgRoot.class));
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
			STYLES.add(new SoyMapData("styleSheet", "/dbg/css/main.css"));
			STYLES.add(new SoyMapData("styleSheet", "/dbg/css/arg.css"));
		}
		
		return STYLES;
	}
}