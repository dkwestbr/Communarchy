package communarchy.controllers.handlers.input;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.ArgumentMapper;
import communarchy.inputValidation.IUserInput;
import communarchy.vb.login.LoginRoot;

public class NewArgController extends AbstractInputHandler {

	private static final Logger log =
		      Logger.getLogger(NewArgController.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static String RENDER_TARGET;
	private static SoyTofu NEW_ARG_TEMPL;
	
	@Override
	public void init() throws ServletException {
		
		RENDER_TARGET = LoginRoot.get().getRenderTarget();
		NEW_ARG_TEMPL = (SoyTofu) getServletContext().getAttribute(IHttpSessionConstants.NEW_ARG_VIEW_TEMPLATE);
		
		requiredFieldMap = new HashMap<String, IUserInput>();
		requiredFieldMap.put("content", new IUserInput() {
			@Override
			public int getMaxLength() { return 60000; }

			@Override
			public int getMinLength() { return 20; }

			@Override
			public boolean htmlAllowed() { return false; }

			@Override
			public String getContentName() { return "content"; }
		});
		
		requiredFieldMap.put("title", new IUserInput() {
			@Override
			public int getMaxLength() { return 500; }

			@Override
			public int getMinLength() { return 20; }

			@Override
			public boolean htmlAllowed() { return false; }

			@Override
			public String getContentName() { return "title"; }
		});
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		try {
			PMSession pmSession = (PMSession) session.getAttribute(IHttpSessionConstants.PM_SESSION_KEY);
			SoyMapData map = LoginRoot.get().getParams(pmSession, user, request);
			response.getWriter().write(NEW_ARG_TEMPL.render(RENDER_TARGET, map, null));
			response.flushBuffer();
		} catch (Exception e) {
			getServletContext().log("In " + NewArgController.class.getCanonicalName() + ":", e);
			response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
	    } finally {
			out.close();
		}
	}

	@Override
	public void performPost(HttpServletRequest request,
			HttpServletResponse response) {
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		ApplicationUser user = (ApplicationUser) request.getSession().getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		PMSession pmSession = (PMSession) request.getSession().getAttribute(IHttpSessionConstants.PM_SESSION_KEY);
		
		Argument arg = new Argument(user.getUserId(), title, content);
		pmSession.getMapper(ArgumentMapper.class).insertNewPost(arg);
		
		try {
			response.sendRedirect(String.format("/arg/%d", arg.getArgId().getId()));
		} catch (IOException e) {
			log.severe("Argument id for new argument invalid");
		}
	}

	@Override
	protected String getValidationKey() {
		return IHttpSessionConstants.VALIDATION_RESULTS_NEW_ARG;
	}
}