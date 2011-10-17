package communarchy.vb.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IRestrictions;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.point.branches.GetPovView;
import communarchy.vb.point.branches.GetPovViewHeaderWrapper;
import communarchy.vb.point.branches.GetPovViewInput;

public class PointContentWrapper extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static PointContentWrapper INSTANCE;
	private PointContentWrapper() {}
	
	public static final String P_ID = "id";
	public static final String P_POINT = "point";
	public static final String P_AGREE_POVS = "agreePovs";
	public static final String P_NEUTRAL_POVS = "neutralPovs";
	public static final String P_DISAGREE_POVS = "disagreePovs";
	public static final String P_POV_HEADER_SET = "povHeaderSet";
	public static final String P_COUNT_SET = "countSet";
	public static final String P_INPUT_SET = "inputSet";
	public static final String P_CONTENT_SET = "contentSet";
	
	public static PointContentWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new PointContentWrapper();
			INSTANCE.possiblePaths.add(GetPovViewHeaderWrapper.get());
			INSTANCE.possiblePaths.add(StanceCountHeader.get());
			INSTANCE.possiblePaths.add(GetPovViewInput.get());
			INSTANCE.possiblePaths.add(GetPovView.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/PointContent.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_ID, scopedResource.getPointId());
		pMap.put(P_POINT, scopedResource.getPoint());
		
		pMap.put(P_INPUT_SET, GetPovViewInput.get().getParams(pmSession, user, request, scopedResource));
		
		for(int stance : IRestrictions.POSSIBLE_STANCES) {
			SoyMapData povSet = new SoyMapData();
			
			povSet.put(P_POV_HEADER_SET, GetPovViewHeaderWrapper.get().getParams(pmSession, user, request, 
					scopedResource, stance));
			povSet.put(P_COUNT_SET, StanceCountHeader.get().getParams(pmSession, user, request, scopedResource, 
					stance));
			povSet.put(P_CONTENT_SET, GetPovView.get().getParams(pmSession, user, request,
					scopedResource, stance));
			
			pMap.put(UserStance.getPovStringByStance(stance), povSet);
		}
		
		return pMap;
	}
}