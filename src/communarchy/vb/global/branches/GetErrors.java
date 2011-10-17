package communarchy.vb.global.branches;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;


import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;

public class GetErrors extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<List<String>> {

	private static GetErrors INSTANCE;
	private GetErrors() {}
	
	public static GetErrors get() {
		if(INSTANCE == null) {
			INSTANCE = new GetErrors();
		}
		
		return INSTANCE;
	}
	
	private static final String P_ERRORS = "errors";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, List<String> scopedResource) {

		SoyMapData pMap = new SoyMapData();
		SoyListData errors = new SoyListData();
		
		for(String err : scopedResource) {
			errors.add(err);
		}
		
		pMap.put(P_ERRORS, errors);
		return pMap;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/global/branches/GetErrorsView.soy";
	}
}