package communarchy.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UserMapper;
import communarchy.facts.mappers.interfaces.IUserMapper;
import communarchy.vb.login.LoginRoot;

/**
 * Servlet implementation class LogIn
 */
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String RENDER_TARGET;
	private static SoyTofu LOGIN_TEMPL;
	
	public void init() throws ServletException {
		
		RENDER_TARGET = LoginRoot.get().getRenderTarget();
		LOGIN_TEMPL = (SoyTofu) getServletContext().getAttribute(IHttpSessionConstants.LOGIN_VIEW_TEMPLATE);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		try {
			PMSession pmSession = PMSession.getOpenSession();
			SoyMapData map = LoginRoot.get().getParams(pmSession, user, request);
			response.getWriter().write(LOGIN_TEMPL.render(RENDER_TARGET, map, null));
			response.flushBuffer();
		} catch (Exception e) {
			getServletContext().log("In " + LogIn.class.getCanonicalName() + ":", e);
			response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
	    } finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		PMSession pmSession = PMSession.getOpenSession();
		
		String userName = request.getParameter("user");
		
		try {
			IUserMapper mapper = pmSession.getMapper(UserMapper.class);
			
			System.out.println("Logging in: " + userName + " -------");
			ApplicationUser user = mapper.getUserByDisplayName(userName);
			if(user == null) {
				user = new ApplicationUser(userName);
				mapper.updateUser(user);
				request.getSession().removeAttribute(IHttpSessionConstants.LOGIN_MESSAGE);
			}
			
			session.setAttribute(IHttpSessionConstants.USER_SESSION_KEY, user);
	
			String cb = (String) request.getSession().getAttribute(IHttpSessionConstants.CALL_BACK_URL);
			session.removeAttribute(IHttpSessionConstants.CALL_BACK_URL);
			response.sendRedirect(cb != null && cb != "" ? cb : "/");
		} catch (Exception e) {
			getServletContext().log("GENERIC EXCEPTION OCCURRED: " + e.getMessage(), e);
	    }
	}
}