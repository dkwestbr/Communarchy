package communarchy.controllers.handlers.actions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;

public abstract class AbstractActionHandler<T> extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected abstract void performAction(HttpServletRequest request, HttpServletResponse response, 
			T resource, String action, ApplicationUser user, PMSession pmSession) throws IOException;
	protected abstract T getResource(long id, PMSession pmSession);
	protected abstract String getMatcherPattern();
	protected abstract boolean handlesAction(String action);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IUser user = (IUser) request.getSession().getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		if(user.isAuthenticated()) {
			doPost(request, response);
		} else {
			request.getSession().setAttribute(IHttpSessionConstants.CALL_BACK_URL, request.getRequestURI());
			response.sendRedirect("/login");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		PMSession pmSession = PMSession.getOpenSession();
		
		try {
			ApplicationUser user = (ApplicationUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
			Pattern idPattern = Pattern.compile(getMatcherPattern());
			Matcher idMatcher = idPattern.matcher(request.getRequestURI());
			
			if(idMatcher.find()) {
				String action = idMatcher.group(1);
				long id = Long.parseLong(idMatcher.group(2));
				T resource = getResource(id, pmSession);
				
				if(resource == null || !handlesAction(action)) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					performAction(request, response, resource, action, user, pmSession);
				}
			}
		} catch(NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} finally {
			pmSession.close();
		}
	}
}