package communarchy.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MemcacheAdmin extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pattern queryPattern = Pattern.compile("/admin/memsaywhat/([a-zA-Z_\\(\\)0-9]+)");
		Matcher queryMatcher = queryPattern.matcher(request.getRequestURI());
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if(user != null) {
			if(queryMatcher.find()) {
				String key = queryMatcher.group(1);
				try {
					Future<Object> val = MemcacheServiceFactory.getAsyncMemcacheService().get(key);
					String v = null;
					int tries = 10000;
					while(v == null && tries > 0) {
						v = (String) val.get();
						--tries;
					}
					response.getWriter().write(String.format("<html><body><div>Key: %s</div><div>Val: %s</div></body></html>", key, val));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
        } else {
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
	}
}