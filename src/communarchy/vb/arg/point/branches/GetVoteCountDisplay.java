package communarchy.vb.arg.point.branches;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.controllers.strategies.limits.VoteLimit;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.nodes.EmptyVoteCount;
import communarchy.vb.arg.point.nodes.VoteCount;

public class GetVoteCountDisplay extends AbstractTemplateWrapper
	implements IResourceTemplateWrapper<UserStance> {

	private static final Logger log =
		      Logger.getLogger(GetVoteCountDisplay.class.getName());
	
	private static GetVoteCountDisplay INSTANCE;
	private GetVoteCountDisplay() {}
	
	public static GetVoteCountDisplay get() {
		if(INSTANCE == null) {
			INSTANCE = new GetVoteCountDisplay();
			INSTANCE.possiblePaths.add(EmptyVoteCount.get());
			INSTANCE.possiblePaths.add(VoteCount.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetVoteCountDisplay.soy";
	}

	private static final String P_COUNT = "count";
	private static final String P_VIEW_PARAMS = "viewParams";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, UserStance scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		try {
			Integer displayVotes = VoteLimit.GetVotesRemaining(pmSession, scopedResource.getPoint(), 
					scopedResource.getStance(), user.getUserId());
			
			pMap.put(P_COUNT, displayVotes);
			if(displayVotes > -1) {
				pMap.put(P_VIEW_PARAMS, VoteCount.get().getParams(pmSession, user, request, displayVotes));
			} else {
				pMap.put(P_VIEW_PARAMS, EmptyVoteCount.get().getParams(pmSession, user, request, displayVotes));
			}
		} catch(CommunarchyPersistenceException e) {
			log.warning(e.getMessage());
			e.printStackTrace();
		}
		
		return pMap;
	}
}
