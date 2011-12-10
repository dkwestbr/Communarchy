package communarchy.vb.arg.point.pov.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.pov.nodes.UserAbstains;

public class GetPovView extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<Point, Integer> {

	private static GetPovView INSTANCE;
	private GetPovView() {}
	
	public static GetPovView get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovView();
			INSTANCE.possiblePaths.add(UserAbstains.get());
			INSTANCE.possiblePaths.add(GetSupportedPovs.get());
			INSTANCE.possiblePaths.add(GetUnsupportedPov.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetPovView.soy";
	}

	private static final String P_POV_SET = "povSet";
	private static final String P_USER_ABSTAINS = "userAbstains";
	private static final String P_USER_SUPPORTS = "userSupports";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Point scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		UserStance userStance = null;
		if(user.getUserId() != null) {
			try {
				userStance = pmSession.getMapper(UniqueEntityMapper.class)
					.selectUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId(), subset));
			} catch(CommunarchyPersistenceException e) {
				e.printStackTrace();
			}
		}
		
		if(userStance == null) {
			pMap.put(P_USER_ABSTAINS, "true");
			pMap.put(P_USER_SUPPORTS, "");
			pMap.put(P_POV_SET, UserAbstains.get()
					.getParams(pmSession, user, request, scopedResource, subset));
		} else if(userStance.getStance().equals(subset)) {
			pMap.put(P_USER_ABSTAINS, "");
			pMap.put(P_USER_SUPPORTS, "true");
			pMap.put(P_POV_SET, GetSupportedPovs.get()
					.getParams(pmSession, user, request, scopedResource, subset));
		} else {
			pMap.put(P_USER_ABSTAINS, "");
			pMap.put(P_USER_SUPPORTS, "");
			pMap.put(P_POV_SET, GetUnsupportedPov.get().getParams(pmSession, user, request, scopedResource, subset));
		}
		
		return pMap;
	}
}