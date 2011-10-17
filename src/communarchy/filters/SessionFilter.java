package communarchy.filters;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import communarchy.constants.IHttpSessionConstants;
import communarchy.facts.PMF;
import communarchy.facts.PMSession;

public class SessionFilter implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		PMSession session = PMSession.getOpenSession(pm);
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			request.getSession().setAttribute(IHttpSessionConstants.PM_SESSION_KEY, session);
			
			chain.doFilter(req, resp);
			request.getSession().removeAttribute(IHttpSessionConstants.PM_SESSION_KEY);
		} finally {
			session.closeIt();
			
			if(!pm.isClosed()) {
				pm.close();
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {}
}