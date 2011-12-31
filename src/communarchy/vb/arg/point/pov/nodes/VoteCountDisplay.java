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
public class VoteCountDisplay extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static VoteCountDisplay INSTANCE;
	private VoteCountDisplay() {}

	private static final String P_UPVOTE_PARAMS = "upvoteParams";
	private static final String P_COUNT = "count";
	
	public static VoteCountDisplay get() {
		if(INSTANCE == null) {
			INSTANCE = new VoteCountDisplay();
			INSTANCE.possiblePaths.add(UpvoteActive.get());
			INSTANCE.possiblePaths.add(ReclaimVoteInactive.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/VoteCountDisplay.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_UPVOTE_PARAMS, UpvoteActive.get().getParams(pmSession, user, request, scopedResource));
		
		try {
			pMap.put(P_COUNT, pmSession.getMapper(CountMapper.class).getCount(new GetVoteCountQuery(scopedResource.getKey())));
		} catch (CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		return pMap;
	}
}