package communarchy.vb.point.nodes;


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

public class InputWithErrors extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static InputWithErrors INSTANCE;
	private InputWithErrors() {}
	
	public static String P_ACTION = "action";
	public static String P_USER_STANCE = "userStance";
	
	public static InputWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new InputWithErrors();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/InputWithErrors.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {

		SoyMapData pMap = new SoyMapData();
		IUserStance stance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getPointId(), user.getUserId());
		
		pMap.put(P_ACTION, String.format("/point/%s", scopedResource.getPointId().toString()));
		pMap.put(P_USER_STANCE, UserStance.getStanceUrlPath(stance.getStance()));
		
		
		// TODO fix this
		//addErrors(request.getSession(), pMap);
		
		return pMap;
	}
}