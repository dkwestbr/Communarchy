package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.template.soy.tofu.SoyTofuException;
import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.PovMapper;
import communarchy.inputValidation.IUserInput;
import communarchy.inputValidation.ValidationResult;

public class NewPovController extends AbstractInputHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		
		requiredFieldMap = new HashMap<String, IUserInput>();
		requiredFieldMap.put("pov", new IUserInput() {
			@Override
			public int getMaxLength() { return 60000; }

			@Override
			public int getMinLength() { return 20; }

			@Override
			public boolean htmlAllowed() { return false; }

			@Override
			public String getContentName() { return "pov"; }

			@Override
			public String getDisplayName() { return "Point of view"; }
		});
	}
	
	@Override
	public void performPost(HttpServletRequest request,
			HttpServletResponse response,
			Map<String, ValidationResult> validInputs) throws IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		ApplicationUser user = (ApplicationUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		PMSession pmSession = PMSession.getOpenSession();
		
		try {
			Pattern newPovPattern = Pattern.compile("/point/pov/new/(agree|neutral|disagree)/([0-9]+)");
			Matcher newPovMatcher = newPovPattern.matcher(request.getRequestURI());
			if(newPovMatcher.find()) {
				String stance = newPovMatcher.group(1);
				int stanceId = UserStance.getStanceAsId(stance);
				long id = Long.parseLong(newPovMatcher.group(2));
				Key pointKey = KeyFactory.createKey(Point.class.getSimpleName(), id);
				IPoint point = pmSession.getMapper(PointMapper.class).selectPostById(pointKey);
				if(point == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					UserStance userStance = pmSession.getMapper(PointMapper.class)
							.selectStance(point.getPointId(), user.getUserId());
					if(userStance == null || userStance.getStance() != stanceId) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					} else {
						PointOfView pov = new PointOfView(point.getPointId(), user.getUserId(), 
								validInputs.get("pov").getContent(), stanceId);
						pmSession.getMapper(PovMapper.class).insertNewPost(pov);
						response.sendRedirect(getRedirectURI(request.getRequestURI(), pmSession));
					}
				}
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (SoyTofuException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			throw e;
		} finally {
			pmSession.close();
			out.close();
		}
	}

	@Override
	public void performGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	@Override
	protected String getValidationKey() {
		return IHttpSessionConstants.VALIDATION_RESULTS_NEW_POINT;
	}

	@Override
	protected String getRedirectURI(String originatingURI, PMSession pmSession) throws MalformedURLException {
		// /point/pov/new/pointId becomes /arg/argId
		Pattern argIdPattern = Pattern.compile("/point/pov/new/(agree|neutral|disagree)/([0-9]+)");
		Matcher argIdMatcher = argIdPattern.matcher(originatingURI);
		
		Long argId = null;
		if(argIdMatcher.find()) {
			Long id = Long.parseLong(argIdMatcher.group(2));
			Key argKey = KeyFactory.createKey(Point.class.getSimpleName(), id);
			IPoint point = pmSession.getMapper(PointMapper.class).selectPostById(argKey);
			argId = point.getParentId().getId();
		}
		
		if(argId == null) {
			throw new MalformedURLException(String.format("Malformed URI: %s", originatingURI));
		}
		
		return String.format("/arg/%d", argId);
	}
}
