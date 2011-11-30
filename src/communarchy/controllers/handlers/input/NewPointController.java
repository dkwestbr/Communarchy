package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.io.PrintWriter;
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

import communarchy.controllers.handlers.input.validation.IUserInput;
import communarchy.controllers.handlers.input.validation.ValidationResult;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.mappers.BasicMapper;
import communarchy.utils.constants.IHttpSessionConstants;

public class NewPointController extends AbstractInputHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		
		requiredFieldMap = new HashMap<String, IUserInput>();
		requiredFieldMap.put("point", new IUserInput() {
			@Override
			public int getMaxLength() { return 60000; }

			@Override
			public int getMinLength() { return 20; }

			@Override
			public boolean htmlAllowed() { return false; }

			@Override
			public String getContentName() { return "point"; }

			@Override
			public String getDisplayName() { return "Point"; }
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
			Pattern argIdPattern = Pattern.compile("/arg/point/new/([0-9]+)");
			Matcher argIdMatcher = argIdPattern.matcher(request.getRequestURI());
			if(argIdMatcher.find()) {
				long id = Long.parseLong(argIdMatcher.group(1));
				Key argKey = KeyFactory.createKey(Argument.class.getSimpleName(), id);
				
				IArgument arg = pmSession.getMapper(BasicMapper.class).getById(Argument.class, argKey);
				
				if(arg == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					Point point = new Point(arg.getArgId(), user.getUserId(), validInputs.get("point").getContent());
					pmSession.getMapper(BasicMapper.class).persist(point);
					response.sendRedirect(getRedirectURI(request.getRequestURI(), pmSession));
				} 
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (SoyTofuException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			throw e;
		} finally {
			out.close();
			pmSession.close();
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
	protected String getRedirectURI(String originatingURI, PMSession pmSession) {
		// /arg/point/new/argId becomes /arg/argId
		return originatingURI.replace("/point/new", "");
	}
}