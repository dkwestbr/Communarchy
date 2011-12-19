package communarchy.vb.arg.point.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.pov.branches.GetVoteButtons;

@SuppressWarnings("rawtypes")
public class UserSupports extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UserSupports INSTANCE;
	private UserSupports() {}
	
	private static final String P_ID = "id";
	private static final String P_CONTENT = "content";
	private static final String P_VOTE_BUTTON_PARAMS = "voteButtonParams";
	
	public static UserSupports get() {
		if(INSTANCE == null) {
			INSTANCE = new UserSupports();
			INSTANCE.possiblePaths.add(PovStats.get());
			INSTANCE.possiblePaths.add(GetVoteButtons.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UserSupports.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, String.format("%d", scopedResource.getKey().getId()));
		pMap.put(P_CONTENT, scopedResource.getPov());
		pMap.put(P_VOTE_BUTTON_PARAMS, GetVoteButtons.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}