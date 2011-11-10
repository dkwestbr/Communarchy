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
import communarchy.facts.interfaces.IUser;
import communarchy.vb.main.MainRoot;

/**
 * Servlet implementation class Main
 */
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE = "page";
	
	private static SoyTofu mainTmpl = null;

	public void init() throws ServletException {
		mainTmpl = (SoyTofu) getServletContext().getAttribute(IHttpSessionConstants.MAIN_VIEW_TEMPLATE);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		PMSession pmSession = PMSession.getOpenSession();
		
		try {
			IUser user = (IUser) session.getAttribute(IHttpSessionConstants.USER_SESSION_KEY);
			String scopedResource = request.getParameter(PAGE);
			
			SoyMapData templateParams = MainRoot.get().getParams(pmSession, user, request, scopedResource);
			
			response.getWriter().write(mainTmpl.render(MainRoot.get().getRenderTarget(), templateParams, null));
			response.flushBuffer();
		} catch (Exception e) {
			getServletContext().log("In " + Main.class.getCanonicalName() + ":", e);
			response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
	    } finally {
			out.close();
		}
	}
}