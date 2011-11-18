package communarchy.controllers.filters;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class UUIDFilter implements Filter {

	private static ConcurrentHashMap<UUID, Boolean> REQUESTS_IN_PROGRESS;
	private static final String UUID_SESSION_KEY = "UUID_SESSION_KEY";
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		UUID uuid = (UUID) request.getSession().getAttribute(UUID_SESSION_KEY);
		if(uuid == null) {
			uuid = UUID.randomUUID();
		}
		
		if(!REQUESTS_IN_PROGRESS.contains(uuid)) {
			REQUESTS_IN_PROGRESS.put(uuid, true);
			chain.doFilter(req, resp);
			REQUESTS_IN_PROGRESS.remove(uuid);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		REQUESTS_IN_PROGRESS = new ConcurrentHashMap<UUID, Boolean>();
	}

}
