package communarchy.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.UnauthenticatedUser;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.UserMapper;

/**
 * Servlet implementation class LogOut
 */
public class LogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PMSession pmSession = (PMSession) session.getAttribute(IHttpSessionConstants.PM_SESSION_KEY);
		
		String cb = (String) session.getAttribute(IHttpSessionConstants.CALL_BACK_URL);
		if(cb == null || cb == "") {
			cb="/";
		}
		
		IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
		if(user.isAuthenticated()) {
			pmSession.getMapper(UserMapper.class).updateUser((ApplicationUser) user);
		}
		
		user = UnauthenticatedUser.getNewUser();
		session.setAttribute(IHttpSessionConstants.USER_SESSION_KEY, user);
		response.sendRedirect(cb);
	}
}