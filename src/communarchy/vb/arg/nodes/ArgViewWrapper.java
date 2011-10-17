package communarchy.vb.arg.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.point.branches.PointViewController;

public class ArgViewWrapper extends AbstractTemplateWrapper implements IResourceTemplateWrapper<IArgument> {

	private static ArgViewWrapper INSTANCE;
	
	private static final String ID_KEY = "id";
	private static final String TITLE_KEY = "title";
	private static final String CONTENT_KEY = "content";
	private static final String POINT_KEY = "pointSet";
	
	public static ArgViewWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgViewWrapper();
			INSTANCE.possiblePaths.add(PointViewController.get());
		}
		
		return INSTANCE;
	}
	
	private ArgViewWrapper() {}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/nodes/ArgView.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			IArgument arg) {

		SoyMapData paramMap = new SoyMapData();
		
		paramMap.put(ID_KEY, arg.getArgId().toString());
		paramMap.put(TITLE_KEY, arg.getTitle());
		paramMap.put(CONTENT_KEY, arg.getContent());
		paramMap.put(POINT_KEY, PointViewController.get().getParams(pmSession, user, request, arg));
		
		return paramMap;
	}

}
