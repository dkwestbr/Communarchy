package communarchy.vb.arg.point.pov.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetVoteCountQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

@SuppressWarnings("rawtypes")
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
		return "./templates/html/arg/point/pov/nodes/PovStats.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		try {
			pMap.put(P_POV_VOTE_COUNT, pmSession.getMapper(CountMapper.class).getCount(new GetVoteCountQuery(scopedResource.getKey())));
		} catch (CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		return pMap;
	}
}