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

import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UniqueEntityMapper;
import communarchy.facts.queries.entity.GetUserByName;
import communarchy.utils.constants.IHttpSessionConstants;
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
		PMSession pmSession = PMSession.getOpenSession();
		
		try {
			SoyMapData map = LoginRoot.get().getParams(pmSession, user, request);
			response.getWriter().write(LOGIN_TEMPL.render(RENDER_TARGET, map, null));
			response.flushBuffer();
		} catch (Exception e) {
			getServletContext().log("In " + LogIn.class.getCanonicalName() + ":", e);
			response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
	    } finally {
			out.close();
			pmSession.close();
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
			UniqueEntityMapper mapper = pmSession.getMapper(UniqueEntityMapper.class);
			
			System.out.println("Logging in: " + userName + " -------");
			GetUserByName query = new GetUserByName(userName);
			
			ApplicationUser user = mapper.getUnique(query);
			if(user == null) {
				user = mapper.persistUnique(query);
				request.getSession().removeAttribute(IHttpSessionConstants.LOGIN_MESSAGE);
			}
			
			session.setAttribute(IHttpSessionConstants.USER_SESSION_KEY, user);
	
			String cb = (String) request.getSession().getAttribute(IHttpSessionConstants.CALL_BACK_URL);
			session.removeAttribute(IHttpSessionConstants.CALL_BACK_URL);
			response.sendRedirect(cb != null && cb != "" ? cb : "/");
		} catch (Exception e) {
			getServletContext().log("GENERIC EXCEPTION OCCURRED: " + e.getMessage(), e);
	    } finally {
	    	pmSession.close();
	    }
	}
}