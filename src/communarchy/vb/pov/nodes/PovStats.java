package communarchy.vb.pov.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.PovMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class PovStats extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static PovStats INSTANCE;
	private PovStats() {}
	
	public static final String P_POV_VOTE_COUNT = "povVoteCount";
	
	public static PovStats get() {
		if(INSTANCE == null) {
			INSTANCE = new PovStats();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/pov/nodes/PovStats.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_POV_VOTE_COUNT, pmSession.getMapper(PovMapper.class).getPovVoteCount(scopedResource.getPovId()));
		
		return pMap;
	}
}