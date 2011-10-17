package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import communarchy.inputValidation.IUserInput;
import communarchy.inputValidation.UserInputValidator;
import communarchy.inputValidation.ValidationResult;

public abstract class AbstractInputHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract void performPost(HttpServletRequest request, HttpServletResponse response);
	
	protected abstract String getValidationKey();
	
	protected Map<String, IUserInput> requiredFieldMap;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		boolean isValid = true;
		for(String key : requiredFieldMap.keySet()) {
			String val = request.getParameter(key);
			ValidationResult result = UserInputValidator.getInstance().validateUserInput(requiredFieldMap.get(key), val);
			isValid = result.isValid();
			results.add(result);
		}
		
		if(isValid) {
			performPost(request, response);
		} else {
			request.getSession().setAttribute(getValidationKey(), results);
			response.sendRedirect(request.getRequestURI());
		}
	}
}