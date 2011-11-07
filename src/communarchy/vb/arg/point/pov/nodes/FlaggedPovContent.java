package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class FlaggedPovContent extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static FlaggedPovContent INSTANCE;
	private FlaggedPovContent() {}
	
	public static final String P_REASON = "reason";
	public static final String P_REMOVE = "remove";
	public static final String P_REMOVE_ACTION = "removeAction";
	public static final String P_REMOVE_ALL = "removeAll";
	public static final String P_REMOVE_ALL_ACTION = "removeAllAction";
	
	public static FlaggedPovContent get() {
		if(INSTANCE == null) {
			INSTANCE = new FlaggedPovContent();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/FlaggedPovContent.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_REASON, "");
		pMap.put(P_REMOVE, "");
		pMap.put(P_REMOVE_ACTION, "");
		pMap.put(P_REMOVE_ALL, "");
		pMap.put(P_REMOVE_ALL_ACTION, "");
		
		return pMap;
	}
}