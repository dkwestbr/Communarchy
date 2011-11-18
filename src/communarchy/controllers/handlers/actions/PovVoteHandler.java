package communarchy.controllers.handlers.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.PointOfView;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.PointMapper;
import communarchy.facts.mappers.PovMapper;

public class PovVoteHandler extends AbstractActionHandler<PointOfView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void performAction(HttpServletRequest request,
			HttpServletResponse response, PointOfView resource, String action,
			ApplicationUser user, PMSession pmSession) throws IOException {
		
		if(action.equals("up")) {
			pmSession.getMapper(PovMapper.class).insertVote(resource.getParentPointId(), resource.getKey(), user.getUserId());
		} else if(action.equals("reclaim")) {
			pmSession.getMapper(PovMapper.class).reclaimVote(resource.getKey(), user.getUserId());
		}
		
		IPoint point = pmSession.getMapper(PointMapper.class).selectPostById(resource.getParentPointId());
		response.sendRedirect(String.format("/arg/%d", point.getParentId().getId()));
	}

	@Override
	protected PointOfView getResource(long id, PMSession pmSession) {
		Key povKey = KeyFactory.createKey(PointOfView.class.getSimpleName(), id);
		return (PointOfView) pmSession.getMapper(PovMapper.class).selectPostById(povKey);
	}

	@Override
	protected String getMatcherPattern() {
		return "/pov/vote/(up|reclaim)/([0-9]+)";
	}

	@Override
	protected boolean handlesAction(String action) {
		return action.equals("up") || action.equals("reclaim");
	}
}