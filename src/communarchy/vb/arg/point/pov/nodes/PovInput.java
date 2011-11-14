package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Stance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.PointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class PovInput extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static PovInput INSTANCE;
	private PovInput() {}
	
	private static String P_ACTION = "action";
	private static String P_USER_STANCE = "userStance";
	
	public static PovInput get() {
		if(INSTANCE == null) {
			INSTANCE = new PovInput();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/PovInput.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {

		SoyMapData pMap = new SoyMapData();
		IUserStance stance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getKey(), user.getUserId());
		
		pMap.put(P_ACTION, String.format("/point/pov/new/%s/%d", 
				Stance.getStanceUrlPath(stance.getStance()), scopedResource.getKey().getId()));
		pMap.put(P_USER_STANCE, Stance.getStanceUrlPath(stance.getStance()));
		
		return pMap;
	}
}