package communarchy.vb.arg.point.nodes;

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

public class HeaderUserSupports extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static HeaderUserSupports INSTANCE;
	private HeaderUserSupports() {}

	private static final String P_TAKE_STANCE_ACTION = "takestanceAction";
	private static final String P_LABEL = "label";
	private static final String P_STANCE = "stance";
	
	public static HeaderUserSupports get() {
		if(INSTANCE == null) {
			INSTANCE = new HeaderUserSupports();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/HeaderUserSupports.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		IUserStance userStance = pmSession.getMapper(PointMapper.class)
				.selectStance(scopedResource.getKey(), user.getUserId());
		
		pMap.put(P_TAKE_STANCE_ACTION, String.format("/point/takestance/%s/%d", 
				Stance.getStanceUrlPath(userStance.getStance()), scopedResource.getKey().getId()));
		
		pMap.put(P_LABEL, Stance.getStanceAsString(userStance.getStance()));
		pMap.put(P_STANCE, Stance.getStanceUrlPath(userStance.getStance()));
		
		return pMap;
	}
}