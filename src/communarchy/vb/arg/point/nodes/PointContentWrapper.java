package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetPovs;
import communarchy.vb.arg.point.branches.GetStanceCountHeaders;
import communarchy.vb.arg.point.branches.GetStanceHeaders;
import communarchy.vb.arg.point.pov.branches.GetPovViewInput;

public class PointContentWrapper extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static PointContentWrapper INSTANCE;
	private PointContentWrapper() {}
	
	public static PointContentWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new PointContentWrapper();
			INSTANCE.possiblePaths.add(GetStanceCountHeaders.get());
			INSTANCE.possiblePaths.add(GetStanceHeaders.get());
			INSTANCE.possiblePaths.add(GetPovs.get());
			INSTANCE.possiblePaths.add(GetPovViewInput.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/PointContent.soy";
	}

	private static final String P_ID = "id";
	private static final String P_POINT = "point";
	private static final String P_COUNT_SETS = "countSets";
	private static final String P_HEADER_SETS = "headerSets";
	private static final String P_CONTENT_SETS = "contentSets";
	private static final String P_INPUT_SET = "inputSet";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_ID, scopedResource.getPointId().getId());
		pMap.put(P_POINT, scopedResource.getPoint());
		
		pMap.put(P_COUNT_SETS, GetStanceCountHeaders.get().getParams(pmSession, user, request, 
				scopedResource));
		
		pMap.put(P_HEADER_SETS, GetStanceHeaders.get().getParams(pmSession, user, request, 
				scopedResource));
		
		pMap.put(P_CONTENT_SETS, GetPovs.get().getParams(pmSession, user, request, 
				scopedResource));
		
		pMap.put(P_INPUT_SET, GetPovViewInput.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}