package communarchy.vb.global.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class ErrorView extends AbstractTemplateWrapper implements IResourceTemplateWrapper<String> {

	private static ErrorView INSTANCE;
	private ErrorView() {}
	
	public static ErrorView get() {
		if(INSTANCE == null) {
			INSTANCE = new ErrorView();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/ErrorView.soy";
	}

	private static final String P_ERROR = "error";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ERROR, scopedResource);
		
		return pMap;
	}
}