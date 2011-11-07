package communarchy.vb.newarg.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class NewArgTitle extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<String> {

	private static NewArgTitle INSTANCE;
	private NewArgTitle() {}
	
	public static NewArgTitle get() {
		if(INSTANCE == null) {
			INSTANCE = new NewArgTitle();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/nodes/Title.soy";
	}

	private static final String P_CONTENT = "title";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_CONTENT, scopedResource);
		return pMap;
	}
}
