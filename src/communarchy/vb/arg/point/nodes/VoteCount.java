package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class VoteCount extends AbstractTemplateWrapper 
	implements IResourceTemplateWrapper<Integer> {

	private static VoteCount INSTANCE;
	private VoteCount() {}
	
	public static VoteCount get() {
		if(INSTANCE == null) {
			INSTANCE = new VoteCount();
		}
		
		return INSTANCE;
	}
	
	private static final String P_COUNT = "count";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Integer scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_COUNT, scopedResource);
		
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/VoteCount.soy";
	}
}