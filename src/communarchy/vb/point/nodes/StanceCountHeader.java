package communarchy.vb.point.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.PointMapper;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;

public class StanceCountHeader extends AbstractTemplateWrapper implements
	IResourceSubsetWrapper<IPoint, Integer> {

	private static StanceCountHeader INSTANCE;
	private StanceCountHeader() {}

	public static final String P_PERSON_IMG = "personImg";
	public static final String P_STANCE_COUNT = "stanceCount";
	
	public static StanceCountHeader get() {
		if(INSTANCE == null) {
			INSTANCE = new StanceCountHeader();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/nodes/StanceCountHeader.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_PERSON_IMG, UserStance.getStanceUrlPath(subset));
		pMap.put(P_STANCE_COUNT, pmSession.getMapper(PointMapper.class)
				.getPointCountByStance(scopedResource.getPointId(), subset));
		
		return pMap;
	}

}
