package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Stance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetStanceCount;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;

@SuppressWarnings("rawtypes")
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
		return "./templates/html/arg/point/nodes/StanceCountHeader.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource,
			Integer subset) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_PERSON_IMG, Stance.getStanceUrlPath(subset));
		Integer count = 0;
		try {
			count = pmSession.getMapper(CountMapper.class).getCount(new GetStanceCount(scopedResource.getKey(), subset));
		} catch (CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		pMap.put(P_STANCE_COUNT, count);
		
		return pMap;
	}

}
