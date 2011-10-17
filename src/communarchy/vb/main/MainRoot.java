package communarchy.vb.main;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.SoyFileSet.Builder;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.IRootTemplate;
import communarchy.vb.global.branches.HeaderWrapper;
import communarchy.vb.global.nodes.NavigationBarWrapper;
import communarchy.vb.main.branches.GetArgFeed;

public class MainRoot extends AbstractTemplateWrapper implements IRootTemplate, IResourceTemplateWrapper<String> {

	private static MainRoot INSTANCE;
	private MainRoot() {}
	
	@Override
	public void init(ServletContext context) {
		Builder builder = new SoyFileSet.Builder();
		registerTemplates(builder, new HashMap<String, String>());
		SoyTofu mainTmpl = builder.build().compileToJavaObj();
		context.setAttribute(IHttpSessionConstants.MAIN_VIEW_TEMPLATE, mainTmpl);
	}
	
	public static MainRoot get() {
		if(INSTANCE == null) {
			INSTANCE = new MainRoot();
			INSTANCE.possiblePaths.add(NavigationBarWrapper.get());
			INSTANCE.possiblePaths.add(HeaderWrapper.get());
			INSTANCE.possiblePaths.add(GetArgFeed.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getRenderTarget() {
		return "communarchy.templates.html.main.Root.getView";
	}

	@Override
	public String getTemplate() {
		return "./templates/html/main/MainRoot.soy";
	}
	
	private static String PARAM_KEY_ARGBAR = "argBarData";
	private static String PARAM_KEY_VIEW = "args";
	private static String PARAM_KEY_HEADER = "headerParams";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(PARAM_KEY_ARGBAR, NavigationBarWrapper.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_VIEW, GetArgFeed.get().getParams(pmSession, user, request));
		pMap.put(PARAM_KEY_HEADER, HeaderWrapper.get().getParams(MainRoot.class));
		
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
		}
		
		return STYLES;
	}
}