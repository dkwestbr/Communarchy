package communarchy.vb.point.branches;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.point.nodes.Input;
import communarchy.vb.point.nodes.InputWithErrors;

public class GetPovViewInput extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPoint> {

	private static GetPovViewInput INSTANCE;
	private GetPovViewInput() {}
	
	public static final String P_NEW_POV = "newPov";
	
	public static GetPovViewInput get() {
		if(INSTANCE == null) {
			INSTANCE = new GetPovViewInput();
			INSTANCE.possiblePaths.add(InputWithErrors.get());
			INSTANCE.possiblePaths.add(Input.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/point/branches/GetPovViewInput.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPoint scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		HttpSession session = request.getSession();
		
		// TODO Fix this
		if(true) {
			pMap.put(P_NEW_POV, InputWithErrors.get().getParams(pmSession, user, request, scopedResource));
		} else {
			pMap.put(P_NEW_POV, Input.get().getParams(pmSession, user, request, scopedResource));
		}
		
		return pMap;
	}
}