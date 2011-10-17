package communarchy.vb.arg.branches;

import javax.servlet.http.HttpServletRequest;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.nodes.ArgViewWrapper;

public class GetArgView extends AbstractTemplateWrapper implements IResourceTemplateWrapper<IArgument> {

	private static GetArgView INSTANCE;
	
	private GetArgView() {}
	
	public static GetArgView get() {
		if(INSTANCE == null) {
			INSTANCE = new GetArgView();
			INSTANCE.possiblePaths.add(ArgViewWrapper.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/branches/GetArgView.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument scopedResource) {
		
		return ArgViewWrapper.get().getParams(pmSession, user, request, scopedResource);
	}
}
