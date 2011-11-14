package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.PointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetStanceCountHeaders;
import communarchy.vb.arg.point.pov.branches.GetPovViewInput;
import communarchy.vb.arg.point.pov.nodes.PovColumn;

public class PointView extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static PointView INSTANCE;
	private PointView() {}
	
	public static PointView get() {
		if(INSTANCE == null) {
			INSTANCE = new PointView();
			INSTANCE.possiblePaths.add(PovColumn.get());
			INSTANCE.possiblePaths.add(GetPovViewInput.get());
			INSTANCE.possiblePaths.add(GetStanceCountHeaders.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/PointView.soy";
	}

	private static final String P_ID = "id";
	private static final String P_POINT = "point";
	private static final String P_COLUMN_SET_FORMAT = "%sColumnSet";
	private static final String P_INPUT_SET = "inputSet";
	private static final String P_SELECTED_STANCE = "selectedStance";
	private static final String P_COUNT_HEADERS = "countHeaders";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_ID, String.format("%d", scopedResource.getKey().getId()));
		pMap.put(P_POINT, scopedResource.getPoint());
		
		IUserStance userStance = pmSession.getMapper(PointMapper.class).selectStance(scopedResource.getKey(), user.getUserId());
		if(userStance == null) {
			pMap.put(P_SELECTED_STANCE, " ");
		} else {
			pMap.put(P_SELECTED_STANCE, String.format("selected-%s", UserStance.getStanceUrlPath(userStance.getStance())));
		}
		
		pMap.put(P_COUNT_HEADERS, GetStanceCountHeaders.get().getParams(pmSession, user, request, scopedResource));
		
		for(int stance : UserStance.STANCE_ARRAY) {
			pMap.put(String.format(P_COLUMN_SET_FORMAT, UserStance.getStanceUrlPath(stance)),
					PovColumn.get().getParams(pmSession, user, request, scopedResource, stance));
		}
		
		pMap.put(P_INPUT_SET, GetPovViewInput.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}