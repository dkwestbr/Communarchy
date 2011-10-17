package communarchy.vb.main.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Entity;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class ArgFeedElement extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Entity> {

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
			HttpServletRequest request, Entity scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, scopedResource.getProperty("arg_id"));
		pMap.put(P_TITLE, scopedResource.getProperty("title"));
		pMap.put(P_HREF, String.format("/arg/%s", scopedResource.getProperty("arg_id")));
		
		return pMap;
	}
}