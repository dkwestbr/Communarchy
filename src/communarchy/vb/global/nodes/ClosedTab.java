package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;

public class ClosedTab extends AbstractTemplateWrapper implements IResourceSubsetWrapper<String, String> {
	
	private static ClosedTab INSTANCE;
	private ClosedTab() {}
	
	public static ClosedTab get() {
		if(INSTANCE == null) {
			INSTANCE = new ClosedTab();
		}
		
		return INSTANCE;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/ClosedTab.soy";
	}

	private static final String P_TAB_NAME = "tabName";
	private static final String P_TAB_ACTION = "tabAction";
	private static final String P_IS_OPENNED = "isOpenned";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String tabName, String action) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_TAB_NAME, tabName);
		pMap.put(P_TAB_ACTION, action);
		pMap.put(P_IS_OPENNED, "");
		
		return pMap;
	}
}