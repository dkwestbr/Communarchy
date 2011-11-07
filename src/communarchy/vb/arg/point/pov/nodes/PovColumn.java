package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.branches.GetPovView;
import communarchy.vb.arg.point.pov.branches.GetPovViewHeader;

public class PovColumn extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<IPoint, Integer> {

	private static PovColumn INSTANCE;
	private PovColumn() {}
	
	public static PovColumn get() {
		if(INSTANCE == null) {
			INSTANCE = new PovColumn();
			INSTANCE.possiblePaths.add(GetPovViewHeader.get());
			INSTANCE.possiblePaths.add(GetPovView.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/PovColumn.soy";
	}
	
	private static final String P_BUTTON_SET = "buttonSet";
	private static final String P_POVS = "povs";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource, Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_BUTTON_SET, GetPovViewHeader.get().getParams(pmSession, user, request, scopedResource, subset));
		pMap.put(P_POVS, GetPovView.get().getParams(pmSession, user, request, scopedResource, subset));
		
		return pMap;
	}
}