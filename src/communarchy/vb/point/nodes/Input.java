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

public class Input extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static Input INSTANCE;
	private Input() {}
	
	private static String P_ACTION = "action";
	private static String P_USER_STANCE = "userStance";
	
	public static Input get() {
		if(INSTANCE == null) {
			INSTANCE = new Input();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/Input.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {

		SoyMapData pMap = new SoyMapData();
		IUserStance stance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getPointId(), user.getUserId());
		
		pMap.put(P_ACTION, String.format("/point/%s", scopedResource.getPointId().toString()));
		pMap.put(P_USER_STANCE, UserStance.getStanceUrlPath(stance.getStance()));
		
		return pMap;
	}
}