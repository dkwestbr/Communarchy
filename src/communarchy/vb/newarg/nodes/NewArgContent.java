package communarchy.vb.newarg.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class NewArgContent extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<String> {

	private static NewArgContent INSTANCE;
	private NewArgContent() {}
	
	public static NewArgContent get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgContent();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/Content.soy";
	}

	private static final String P_CONTENT = "content";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_CONTENT, scopedResource);
		return pMap;
	}
}