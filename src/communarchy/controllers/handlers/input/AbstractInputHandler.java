package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import communarchy.controllers.handlers.input.validation.IUserInput;
import communarchy.controllers.handlers.input.validation.UserInputValidator;
import communarchy.controllers.handlers.input.validation.ValidationResult;
import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;

public abstract class AbstractInputHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract void performPost(HttpServletRequest request, HttpServletResponse response, Map<String, ValidationResult> validInputs) throws IOException;
	public abstract void performGet(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	protected abstract String getValidationKey();
	protected abstract String getRedirectURI(String originatingURI, PMSession pmSession) throws MalformedURLException;
	
	protected Map<String, IUserInput> requiredFieldMap;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IUser user = (IUser) request.getSession().getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		Map<String, String[]> params = (Map<String, String[]>) request.getSession().getAttribute(IHttpSessionConstants.CALL_BACK_PARAMS);
		request.getSession().removeAttribute(IHttpSessionConstants.CALL_BACK_PARAMS);
		if(user.isAuthenticated() && params != null) {
			Map<String, ValidationResult> results = new HashMap<String, ValidationResult>();
			boolean isValid = true;
			for(String key : requiredFieldMap.keySet()) {
			
				String val;
				if(!params.containsKey(key) || params.get(key)[0] == null) {
					val = "";
				} else {
					val = params.get(key)[0];
				}
				
				ValidationResult result = UserInputValidator.getInstance().validateUserInput(requiredFieldMap.get(key), val);
				isValid = result.isValid();
				results.put(result.getName(), result);
			}
			
			if(isValid) {
				request.getSession().removeAttribute(getValidationKey());
				performPost(request, response, results);
			} else {
				PMSession pmSession = PMSession.getOpenSession();
				try {
				request.getSession().setAttribute(getValidationKey(), results);
				response.sendRedirect(getRedirectURI(request.getRequestURI(), pmSession));
				} finally {
					pmSession.close();
				}
			}
		} else {
			performGet(request, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PMSession pmSession = PMSession.getOpenSession();
		try {
			Map<String, ValidationResult> results = new HashMap<String, ValidationResult>();
			boolean isValid = true;
			for(String key : requiredFieldMap.keySet()) {
				String val;
				if(!request.getParameterMap().containsKey(key) || request.getParameter(key) == null) {
					val = "";
				} else {
					val = request.getParameter(key);
				}
				
				ValidationResult result = UserInputValidator.getInstance().validateUserInput(requiredFieldMap.get(key), val);
				isValid = result.isValid();
				results.put(result.getName(), result);
			}
			
			if(isValid) {
				request.getSession().removeAttribute(getValidationKey());
				performPost(request, response, results);
			} else {
				request.getSession().setAttribute(getValidationKey(), results);
				response.sendRedirect(getRedirectURI(request.getRequestURI(), 
						PMSession.getOpenSession()));
			}
		} finally {
			pmSession.close();
		}
	}
}