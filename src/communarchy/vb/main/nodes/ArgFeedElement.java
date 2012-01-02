package communarchy.vb.main.nodes;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.global.nodes.UserWithRep;
import communarchy.vb.main.branches.GetArgVoteButtons;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

public class ArgFeedElement extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Argument> {

	private static ArgFeedElement INSTANCE;
	private ArgFeedElement() {}
	
	public static ArgFeedElement get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgFeedElement();
			INSTANCE.possiblePaths.add(UserWithRep.get());
			INSTANCE.possiblePaths.add(GetArgVoteButtons.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/main/nodes/ArgFeedElement.soy";
	}
	
	private static final String P_ID = "id";
	private static final String P_TITLE = "title";
	private static final String P_HREF = "href";
	private static final String P_VOTE_BUTTON_PARAMS = "voteButtonParams";
	private static final String P_AUTHOR_PARAMS = "authorParams";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, Long.toString(scopedResource.getArgId().getId()));
		pMap.put(P_TITLE, scopedResource.getTitle());
		pMap.put(P_HREF, String.format("/arg/%s/%s", scopedResource.getArgId().getId(), scopedResource.getWebFriendlyTitle()));
		
		Locale clientLocale = request.getLocale();  
		Calendar calendar = Calendar.getInstance(clientLocale);
		String action = String.format("%s %s", "posted", NumberFormatter.DisplayTime(scopedResource.getCreatedDate(), calendar.getTimeZone()));
		pMap.put(P_AUTHOR_PARAMS, UserWithRep.get().getParams(pmSession, user, request, scopedResource.getPosterId(), action));
		
		pMap.put(P_VOTE_BUTTON_PARAMS, GetArgVoteButtons.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}