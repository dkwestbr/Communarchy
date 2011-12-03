package communarchy.vb.arg.point.pov.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Stance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class PovInputWithErrors extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static PovInputWithErrors INSTANCE;
	private PovInputWithErrors() {}
	
	public static String P_ACTION = "action";
	public static String P_USER_STANCE = "userStance";
	
	public static PovInputWithErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new PovInputWithErrors();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/PovInputWithErrors.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {

		SoyMapData pMap = new SoyMapData();
		IUserStance stance = null;
		if(user.getUserId() != null) {
			stance = pmSession.getMapper(UniqueEntityMapper.class).getUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId(), null));
		}
		
		pMap.put(P_ACTION, String.format("/point/pov/new/%s/%d", 
				Stance.getStanceUrlPath(stance.getStance()), scopedResource.getKey().getId()));
		pMap.put(P_USER_STANCE, Stance.getStanceUrlPath(stance.getStance()));
		
		
		// TODO fix this
		//addErrors(request.getSession(), pMap);
		
		return pMap;
	}
}