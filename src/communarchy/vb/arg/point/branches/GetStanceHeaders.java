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
import communarchy.vb.arg.point.pov.branches.GetPovViewHeader;

public class GetStanceHeaders extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static GetStanceHeaders INSTANCE;
	private GetStanceHeaders() {}

	public static GetStanceHeaders get() {
		if(INSTANCE == null) {
			INSTANCE = new GetStanceHeaders();
			INSTANCE.possiblePaths.add(GetPovViewHeader.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetStanceHeaders.soy";
	}

	private static final String P_HEADER_SETS = "headerSets";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		SoyListData stanceCountList = new SoyListData();
		for(int stance : IRestrictions.POSSIBLE_STANCES) {
			stanceCountList.add(GetPovViewHeader.get().getParams(pmSession, user, request, scopedResource, stance));
		}
		
		pMap.put(P_HEADER_SETS, stanceCountList);
		
		return pMap;
	}
}