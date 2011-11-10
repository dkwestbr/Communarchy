package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofuException;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.ArgumentMapper;
import communarchy.inputValidation.IUserInput;
import communarchy.inputValidation.ValidationResult;
import communarchy.vb.arg.ArgRoot;

public class ArgController extends AbstractInputHandler {
	
	private static final Logger log =
		      Logger.getLogger(ArgController.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private static String RENDER_TARGET;
	private static SoyTofu RENDER_TEMPL;
	
	@Override
	public void init() throws ServletException {
		
		RENDER_TARGET = ArgRoot.get().getRenderTarget();
		RENDER_TEMPL = (SoyTofu) getServletContext().getAttribute(IHttpSessionConstants.ARG_VIEW_TEMPLATE);
		
		requiredFieldMap = new HashMap<String, IUserInput>();
	}
	
	@Override
	public void performGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		PMSession pmSession = PMSession.getOpenSession();
		
		try {
			Pattern argIdPattern = Pattern.compile("/arg/([0-9]+)");
			Matcher argIdMatcher = argIdPattern.matcher(request.getRequestURI());
			if(argIdMatcher.find()) {
				long id = Long.parseLong(argIdMatcher.group(1));
				Key argKey = KeyFactory.createKey(Argument.class.getSimpleName(), id);
				
				IArgument arg = pmSession.getMapper(ArgumentMapper.class).selectPostById(argKey);
				
				if(arg == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					SoyMapData map = ArgRoot.get().getParams(pmSession, user, request, arg);
					response.getWriter().write(RENDER_TEMPL.render(RENDER_TARGET, map, null));
					response.flushBuffer();
				} 
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (JDOObjectNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			log.warning(e.getMessage());
			throw e;
		} catch (SoyTofuException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			log.warning(e.getMessage());
			throw e;
		} finally {
			out.close();
			pmSession.close();
		}
	}

	@Override
	public void performPost(HttpServletRequest request,
			HttpServletResponse response,
			Map<String, ValidationResult> validInputs) throws IOException {

		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	@Override
	protected String getValidationKey() {
		throw new NullPointerException("No validation key define for ArgController because ArgController doesn't handle input");
	}

	@Override
	protected String getRedirectURI(String originatingURI, PMSession pmSession) {
		return originatingURI;
	}
}