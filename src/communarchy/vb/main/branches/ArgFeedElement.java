package communarchy.vb.main.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class ArgFeedElement extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Argument> {

	private static ArgFeedElement INSTANCE;
	private ArgFeedElement() {}
	
	public static ArgFeedElement get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgFeedElement();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/main/nodes/ArgFeedElement.soy";
	}
	
	public static final String P_ID = "id";
	public static final String P_TITLE = "title";
	public static final String P_HREF = "href";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, Long.toString(scopedResource.getArgId().getId()));
		pMap.put(P_TITLE, scopedResource.getTitle());
		pMap.put(P_HREF, String.format("/arg/%s/%s", scopedResource.getArgId().getId(), scopedResource.getWebFriendlyTitle()));
		
		return pMap;
	}
}