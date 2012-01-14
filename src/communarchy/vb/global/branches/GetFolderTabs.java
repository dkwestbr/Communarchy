package communarchy.vb.global.branches;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.global.nodes.ClosedTab;
import communarchy.vb.global.nodes.OpennedTab;
import communarchy.vb.global.nodes.TabBorderEnd;
import communarchy.vb.global.nodes.TabBorderSpacer;

public class GetFolderTabs extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<List<String>, String> {

	private static GetFolderTabs INSTANCE;
	private GetFolderTabs() {}
	
	public static GetFolderTabs get() {
		if(INSTANCE == null) {
			INSTANCE = new GetFolderTabs();
			INSTANCE.possiblePaths.add(ClosedTab.get());
			INSTANCE.possiblePaths.add(OpennedTab.get());
			INSTANCE.possiblePaths.add(TabBorderEnd.get());
			INSTANCE.possiblePaths.add(TabBorderSpacer.get());
		}
		
		return INSTANCE;
	}
	
	private static final String P_TABS = "tabs";
	private static final String P_TAB_BORDER_END_PARAMS = "tabBorderEndParams";
	private static final String P_TAB_BORDER_SPACER_PARAMS = "tabBorderSpacerParams";

	
	@Override
	public String getTemplate() {
		return "./templates/html/global/branches/GetFolderTabs.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, List<String> tabList,
			String opennedTab) {

		SoyMapData pMap = new SoyMapData();
		SoyListData tabs = new SoyListData();
		
		for(String tabName : tabList) {
			if(tabName.equals(opennedTab)) {
				tabs.add(OpennedTab.get().getParams(pmSession, user, request, tabName, String.format("/?sort=%s", tabName)));
			} else {
				tabs.add(ClosedTab.get().getParams(pmSession, user, request, tabName, String.format("/?sort=%s", tabName)));
			}
		}
		
		pMap.put(P_TAB_BORDER_END_PARAMS, TabBorderEnd.get().getParams(pmSession, user, request));
		pMap.put(P_TAB_BORDER_SPACER_PARAMS, TabBorderSpacer.get().getParams(pmSession, user, request));
		pMap.put(P_TABS, tabs);
		return pMap;
	}
}