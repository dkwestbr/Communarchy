package communarchy.vb.arg.point.pov.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;
import communarchy.vb.arg.point.nodes.HeaderUserDoesntSupport;
import communarchy.vb.arg.point.nodes.HeaderUserSupports;

public class GetPovViewHeader extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<Point, Integer> {

	private static GetPovViewHeader INSTANCE;
	private GetPovViewHeader() {}
	
	public static final String P_POV_SET = "povHeaderSet";
	public static final String P_USER_SUPPORTS = "userSupports";
	
	public static GetPovViewHeader get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovViewHeader();
			INSTANCE.possiblePaths.add(HeaderUserSupports.get());
			INSTANCE.possiblePaths.add(HeaderUserDoesntSupport.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/branches/GetPovViewHeader.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Point scopedResource,
			Integer subset) {
	
		SoyMapData pMap = new SoyMapData();
		
		IUserStance userStance = null;
		if(user.getUserId() != null) {
			try {
				userStance = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId(), subset));
			} catch (CommunarchyPersistenceException e) {
				e.printStackTrace();
			}
		}
		
		if(userStance != null && userStance.getStance().equals(subset)) {
			pMap.put(P_POV_SET, HeaderUserSupports.get().getParams(pmSession, user, request, 
																	scopedResource));
			
			pMap.put(P_USER_SUPPORTS, " ");
		} else {
			pMap.put(P_POV_SET, HeaderUserDoesntSupport.get().getParams(pmSession, user, request, 
					scopedResource, subset));
		}
		
		return pMap;
	}
}