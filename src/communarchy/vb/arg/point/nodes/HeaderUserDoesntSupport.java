package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Stance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;

@SuppressWarnings("rawtypes")
public class HeaderUserDoesntSupport extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {
	
	private static HeaderUserDoesntSupport INSTANCE;
	private HeaderUserDoesntSupport() {}
	
	public static final String P_TAKE_STANCE_ACTION = "takestanceAction";
	public static final String P_STANCE = "stance";
	
	public static HeaderUserDoesntSupport get() {
		if(INSTANCE == null) {
			INSTANCE = new HeaderUserDoesntSupport();
		}
		
		return INSTANCE;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/HeaderUserDoesntSupport.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource, Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_TAKE_STANCE_ACTION, String.format("/point/takestance/%s/%d", 
				Stance.getStanceUrlPath(subset), scopedResource.getKey().getId()));
		
		pMap.put(P_STANCE, Stance.getStanceAsString(subset));
		
		return pMap;
	}
}