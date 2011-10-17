package communarchy.vb.newarg.branches;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.newarg.nodes.NewArgView;

public class GetNewArgView extends AbstractTemplateWrapper implements IResourceTemplateWrapper<IArgument> {

	private static GetNewArgView INSTANCE;
	
	private GetNewArgView() {}
	
	public static GetNewArgView get() {
		if(INSTANCE == null) {
			INSTANCE = new GetNewArgView();
			INSTANCE.possiblePaths.add(NewArgView.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/newarg/branches/GetView.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {
		
		return NewArgView.get().getParams(pmSession, user, request, scopedResource);
	}
}
