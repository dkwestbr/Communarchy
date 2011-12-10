package communarchy.vb.arg.nodes;


import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetPointView;
import communarchy.vb.global.nodes.ThickBorder;

public class ArgViewWrapper extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static ArgViewWrapper INSTANCE;
	
	private static final String ID_KEY = "id";
	private static final String TITLE_KEY = "title";
	private static final String CONTENT_KEY = "content";
	private static final String POINT_KEY = "pointSet";
	
	public static ArgViewWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgViewWrapper();
			INSTANCE.possiblePaths.add(GetPointView.get());
			INSTANCE.possiblePaths.add(ThickBorder.get());
		}
		
		return INSTANCE;
	}
	
	private ArgViewWrapper() {}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/nodes/ArgView.soy";
	}

	private static final String PARAM_BORDER_SET = "borderSet";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			Argument arg) {

		SoyMapData paramMap = new SoyMapData();
		
		paramMap.put(ID_KEY, String.format("%d", arg.getArgId().getId()));
		paramMap.put(TITLE_KEY, arg.getTitle());
		paramMap.put(CONTENT_KEY, arg.getContent());
		paramMap.put(POINT_KEY, GetPointView.get().getParams(pmSession, user, request, arg));
		paramMap.put(PARAM_BORDER_SET, ThickBorder.get().getParams(pmSession, user, request));
		
		return paramMap;
	}
}