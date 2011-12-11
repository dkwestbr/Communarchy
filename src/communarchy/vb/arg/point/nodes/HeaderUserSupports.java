package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Stance;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetVoteCountDisplay;

@SuppressWarnings("rawtypes")
public class HeaderUserSupports extends AbstractTemplateWrapper implements
	IResourceTemplateWrapper<IPoint> {

	private static HeaderUserSupports INSTANCE;
	private HeaderUserSupports() {}

	private static final String P_TAKE_STANCE_ACTION = "takestanceAction";
	private static final String P_LABEL = "label";
	private static final String P_STANCE = "stance";
	private static final String P_VOTES_DISPLAY_PARAMS = "voteDisplayParams";
	
	public static HeaderUserSupports get() {
		if(INSTANCE == null) {
			INSTANCE = new HeaderUserSupports();
			INSTANCE.possiblePaths.add(GetVoteCountDisplay.get());
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
		
		try {
			UserStance userStance = null;
			if(user.getUserId() != null) {
				userStance = pmSession.getMapper(UniqueEntityMapper.class)
					.selectUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId(), null));
			}
			
			pMap.put(P_TAKE_STANCE_ACTION, String.format("/point/takestance/%s/%d", 
					Stance.getStanceUrlPath(userStance.getStance()), scopedResource.getKey().getId()));
			
			pMap.put(P_VOTES_DISPLAY_PARAMS, GetVoteCountDisplay.get().getParams(pmSession, user, request, userStance));
			
			pMap.put(P_LABEL, Stance.getStanceAsString(userStance.getStance()));
			pMap.put(P_STANCE, Stance.getStanceUrlPath(userStance.getStance()));
		} catch(CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		return pMap;
	}
}