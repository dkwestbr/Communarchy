package communarchy.controllers.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.appengine.api.memcache.MemcacheServiceFactory;

import communarchy.vb.arg.ArgRoot;
import communarchy.vb.login.LoginRoot;
import communarchy.vb.main.MainRoot;
import communarchy.vb.newarg.NewArgRoot;

public final class StartUpContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		MemcacheServiceFactory.getAsyncMemcacheService().clearAll();
		ArgRoot.get().init(context);
		MainRoot.get().init(context);
		LoginRoot.get().init(context);
		NewArgRoot.get().init(context);
	}
}
