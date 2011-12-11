package communarchy.vb.arg.point.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class EmptyVoteCount extends AbstractTemplateWrapper 
	implements IResourceTemplateWrapper<Integer> {

	private static EmptyVoteCount INSTANCE;
	private EmptyVoteCount() {}
	
	public static EmptyVoteCount get() {
		if(INSTANCE == null) {
			INSTANCE = new EmptyVoteCount();
		}
		
		return INSTANCE;
	}
	
	private static final SoyMapData EMPTY_MAP = new SoyMapData();
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Integer scopedResource) {
		
		return EMPTY_MAP;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/EmptyVoteCount.soy";
	}
}