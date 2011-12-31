package communarchy.vb.arg.point.nodes;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.interfaces.IUserStance;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.UserStanceQuery;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.arg.point.branches.GetStanceCountHeaders;
import communarchy.vb.arg.point.pov.branches.GetPovViewInput;
import communarchy.vb.arg.point.pov.nodes.PovColumn;
import communarchy.vb.global.nodes.UserWithRep;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

public class PointView extends AbstractTemplateWrapper implements
		IResourceTemplateWrapper<Point> {

	private static PointView INSTANCE;
	private PointView() {}
	
	public static PointView get() {
		if(INSTANCE == null) {
			INSTANCE = new PointView();
			INSTANCE.possiblePaths.add(PovColumn.get());
			INSTANCE.possiblePaths.add(GetPovViewInput.get());
			INSTANCE.possiblePaths.add(GetStanceCountHeaders.get());
			INSTANCE.possiblePaths.add(UserWithRep.get());
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/arg/point/nodes/PointView.soy";
	}

	private static final String P_ID = "id";
	private static final String P_POINT = "point";
	private static final String P_COLUMN_SET_FORMAT = "%sColumnSet";
	private static final String P_INPUT_SET = "inputSet";
	private static final String P_SELECTED_STANCE = "selectedStance";
	private static final String P_COUNT_HEADERS = "countHeaders";
	private static final String P_AUTHOR_PARAMS = "authorParams";
	
	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user, HttpServletRequest request,
			Point scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_ID, String.format("%d", scopedResource.getKey().getId()));
		pMap.put(P_POINT, scopedResource.getPoint());
		
		Locale clientLocale = request.getLocale();  
		Calendar calendar = Calendar.getInstance(clientLocale);
		String action = String.format("%s %s", "posted", NumberFormatter.DisplayTime(scopedResource.getCreatedDate(), calendar.getTimeZone()));
		pMap.put(P_AUTHOR_PARAMS, UserWithRep.get().getParams(pmSession, user, request, scopedResource.getPosterId(), action));
		
		IUserStance userStance = null;
		if(scopedResource != null && scopedResource.getKey() != null
				&& user != null && user.getUserId() != null) {
			try {
				userStance = pmSession.getMapper(UniqueEntityMapper.class).selectUnique(new UserStanceQuery(scopedResource.getKey(), user.getUserId(), null));
			} catch (CommunarchyPersistenceException e) {
				e.printStackTrace();
			}
		}
		
		if(userStance == null) {
			pMap.put(P_SELECTED_STANCE, "selected-none");
		} else {
			pMap.put(P_SELECTED_STANCE, String.format("selected-%s", UserStance.getStanceUrlPath(userStance.getStance())));
		}
		
		pMap.put(P_COUNT_HEADERS, GetStanceCountHeaders.get().getParams(pmSession, user, request, scopedResource));
		
		for(int stance : UserStance.STANCE_ARRAY) {
			pMap.put(String.format(P_COLUMN_SET_FORMAT, UserStance.getStanceUrlPath(stance)),
					PovColumn.get().getParams(pmSession, user, request, scopedResource, stance));
		}
		
		pMap.put(P_INPUT_SET, GetPovViewInput.get().getParams(pmSession, user, request, scopedResource));
		
		return pMap;
	}
}