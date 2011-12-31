package communarchy.vb.arg.point.pov.nodes;


import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.pov.branches.GetVoteButtons;
import communarchy.vb.global.nodes.UserWithRep;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

@SuppressWarnings("rawtypes")
public class UserDoesntSupport extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<IPointOfView> {

	private static UserDoesntSupport INSTANCE;
	private UserDoesntSupport() {}
	
	private static final String P_ID = "id";
	private static final String P_CONTENT = "content";
	private static final String P_VOTE_BUTTON_PARAMS = "voteButtonParams";
	private static final String P_AUTHOR_PARAMS = "authorParams";
	
	public static UserDoesntSupport get() {
		if(INSTANCE == null) {
			INSTANCE = new UserDoesntSupport();
			INSTANCE.possiblePaths.add(PovStats.get());
			INSTANCE.possiblePaths.add(GetVoteButtons.get());
			INSTANCE.possiblePaths.add(UserWithRep.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/pov/nodes/UserDoesntSupport.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, IPointOfView scopedResource) {

		SoyMapData pMap = new SoyMapData();
		
		pMap.put(P_ID, String.format("%d", scopedResource.getKey().getId()));
		pMap.put(P_CONTENT, scopedResource.getPov());
		pMap.put(P_VOTE_BUTTON_PARAMS, GetVoteButtons.get().getParams(pmSession, user, request, scopedResource));
		
		Locale clientLocale = request.getLocale();  
		Calendar calendar = Calendar.getInstance(clientLocale);
		String action = String.format("%s %s", "posted", NumberFormatter.DisplayTime(scopedResource.getCreatedDate(), calendar.getTimeZone()));
		
		pMap.put(P_AUTHOR_PARAMS, UserWithRep.get().getParams(pmSession, user, request, scopedResource.getPosterId(), action));
		
		return pMap;
	}
}