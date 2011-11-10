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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import communarchy.utils.constants.ISessionConstants;

public class DevelopmentWallFilter implements Filter {

	@Override
	public void destroy() {
		// dummy
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
     
        if(user != null) {
        	request.getSession().removeAttribute(ISessionConstants.FRONTLINE);
        	chain.doFilter(req, resp);
        } else {
		    if (request.getSession().getAttribute(ISessionConstants.FRONTLINE) == null) {
		    	request.getSession().setAttribute(ISessionConstants.FRONTLINE, request.getRequestURI());
		        response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		    } else if(userService.createLoginURL((String) request.getSession().getAttribute(ISessionConstants.FRONTLINE))
		    				.split("\\?")[0].equals(request.getRequestURI())) {
		    	
		    	chain.doFilter(req, resp);
		    }
        }
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// dummy
	}
}