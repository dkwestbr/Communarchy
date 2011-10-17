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

public class HeaderUserSupports extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static HeaderUserSupports INSTANCE;
	private HeaderUserSupports() {}

	public static final String P_TAKE_STANCE_ACTION = "takeStanceAction";
	public static final String P_STANCE = "stance";
	
	public static HeaderUserSupports get() {
		if(INSTANCE == null) {
			INSTANCE = new HeaderUserSupports();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/HeaderUserSupports.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		IUserStance userStance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getPointId(), user.getUserId());
		
		pMap.put(P_TAKE_STANCE_ACTION, String.format("/point/takeStance/%s/%d", 
				UserStance.getStanceUrlPath(userStance.getStance()), scopedResource.getPointId()));
		
		pMap.put(P_STANCE, UserStance.getStanceAsString(userStance.getStance()));
		
		return pMap;
	}
}