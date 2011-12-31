package communarchy.vb.arg.nodes;


import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetPointView;
import communarchy.vb.global.nodes.ThickBorder;
import communarchy.vb.global.nodes.UserWithRep;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

public class ArgViewWrapper extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static ArgViewWrapper INSTANCE;
	
	private static final String ID_KEY = "id";
	private static final String TITLE_KEY = "title";
	private static final String CONTENT_KEY = "content";
	private static final String POINT_KEY = "pointSet";
	private static final String P_AUTHOR_PARAMS = "authorParams";
	
	public static ArgViewWrapper get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgViewWrapper();
			INSTANCE.possiblePaths.add(GetPointView.get());
			INSTANCE.possiblePaths.add(ThickBorder.get());
			INSTANCE.possiblePaths.add(UserWithRep.get());
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
		
		Locale clientLocale = request.getLocale();  
		Calendar calendar = Calendar.getInstance(clientLocale);
		String action = String.format("%s %s", "posted", NumberFormatter.DisplayTime(arg.getCreatedDate(), calendar.getTimeZone()));
		paramMap.put(P_AUTHOR_PARAMS, UserWithRep.get().getParams(pmSession, user, request, arg.getPosterId(), action));
		
		paramMap.put(POINT_KEY, GetPointView.get().getParams(pmSession, user, request, arg));
		paramMap.put(PARAM_BORDER_SET, ThickBorder.get().getParams(pmSession, user, request));
		
		return paramMap;
	}
}