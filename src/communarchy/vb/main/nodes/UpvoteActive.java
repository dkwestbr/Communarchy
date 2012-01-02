package communarchy.vb.main.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class UpvoteActive extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static UpvoteActive INSTANCE;
	private UpvoteActive() {}
	
	public static final String P_UPVOTE_ACTION = "upvoteAction";
	
	public static UpvoteActive get() {
		if(INSTANCE == null) {
			INSTANCE = new UpvoteActive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/main/nodes/UpvoteActive.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_UPVOTE_ACTION, 
				String.format("/arg/vote/up/%d", scopedResource.getKey().getId()));
		
		return pMap;
	}
}