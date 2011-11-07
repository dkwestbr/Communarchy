package communarchy.vb.arg.point.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.constants.IRestrictions;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.nodes.StanceCountHeader;

public class GetStanceCountHeaders extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static GetStanceCountHeaders INSTANCE;
	private GetStanceCountHeaders() {}

	public static GetStanceCountHeaders get() {
		if(INSTANCE == null) {
			INSTANCE = new GetStanceCountHeaders();
			INSTANCE.possiblePaths.add(StanceCountHeader.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetStanceCountHeaders.soy";
	}

	private static final String P_STANCE_COUNT_SETS = "stanceCountSets";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		SoyListData stanceCountList = new SoyListData();
		for(int stance : IRestrictions.POSSIBLE_STANCES) {
			stanceCountList.add(StanceCountHeader.get().getParams(pmSession, user, request, scopedResource, stance));
		}
		
		pMap.put(P_STANCE_COUNT_SETS, stanceCountList);
		
		return pMap;
	}
}