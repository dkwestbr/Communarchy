package communarchy.controllers.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import communarchy.facts.implementations.UnauthenticatedUser;
import communarchy.facts.interfaces.IUser;
import communarchy.utils.constants.IHttpSessionConstants;
import communarchy.utils.constants.IServletConstants;
import communarchy.utils.constants.ISessionConstants;

public class AuthenticationFilter implements Filter {
	
	@Override
	public void destroy() {
		// dummy.  Leave empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		String devWall = (String) request.getSession().getAttribute(ISessionConstants.FRONTLINE);
		
		if(devWall == null) {
			IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
			
			if(user == null) {
				user = UnauthenticatedUser.getNewUser();
				session.setAttribute(IHttpSessionConstants.USER_SESSION_KEY, user);
			}
			
			if(!request.getRequestURI().equals("/login")
					&& !request.getRequestURI().equals("/logout")) {
				request.getSession().setAttribute(IHttpSessionConstants.CALL_BACK_URL, request.getRequestURI());
			}
			
			if(request.getMethod().equals("POST")
					&& !user.isAuthenticated()
					&& !request.getRequestURI().equals("/login")) {
				
				session.setAttribute(IHttpSessionConstants.LOGIN_MESSAGE, IServletConstants.LOGIN_REQUIRED);
				if(!request.getParameterMap().isEmpty()) {
					session.setAttribute(IHttpSessionConstants.CALL_BACK_PARAMS, request.getParameterMap());
				}
				
				response.sendRedirect("/login");
				response.flushBuffer();
			} else {
				chain.doFilter(req, resp);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {}
}