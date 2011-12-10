package communarchy.vb.arg.point.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IRestrictions;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.pov.branches.GetPovView;

public class GetPovs extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Point> {

	private static GetPovs INSTANCE;
	private GetPovs() {}

	public static GetPovs get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovs();
			INSTANCE.possiblePaths.add(GetPovView.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/branches/GetPovs.soy";
	}

	private static final String P_POV_SET = "povSets";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Point scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		SoyListData povList = new SoyListData();
		for(int stance : IRestrictions.POSSIBLE_STANCES) {
			povList.add(GetPovView.get().getParams(pmSession, user, request, scopedResource, stance));
		}
		
		pMap.put(P_POV_SET, povList);
		
		return pMap;
	}
}