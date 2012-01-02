package communarchy.vb.main.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class DownvoteInactive extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static DownvoteInactive INSTANCE;
	private DownvoteInactive() {}
	
	public static final String P_DOWNVOTE_ACTION = "downvoteAction";
	
	public static DownvoteInactive get() {
		if(INSTANCE == null) {
			INSTANCE = new DownvoteInactive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/main/nodes/DownvoteInactive.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_DOWNVOTE_ACTION, 
				String.format("/arg/vote/down/%d", scopedResource.getKey().getId()));
		
		return pMap;
	}
}