package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class TabBorderSpacer extends AbstractTemplateWrapper implements IParamBuilder {
	
	private static TabBorderSpacer INSTANCE;
	private TabBorderSpacer() {}
	
	public static TabBorderSpacer get() {
		if(INSTANCE == null) {
			INSTANCE = new TabBorderSpacer();
		}
		
		return INSTANCE;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/TabBorderSpacer.soy";
	}

	private static final String P_TAB_BORDER_PARAMS = "tabBorderParams";
	private static final SoyMapData EMPTY_MAP = new SoyMapData();
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request) {
	
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_TAB_BORDER_PARAMS, EMPTY_MAP);
		
		return pMap;
	}
}