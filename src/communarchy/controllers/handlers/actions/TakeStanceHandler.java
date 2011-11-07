package communarchy.controllers.handlers.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.implementations.Point;
import communarchy.facts.implementations.Stance;
import communarchy.facts.implementations.UserStance;
import communarchy.facts.mappers.PointMapper;

public class TakeStanceHandler extends AbstractActionHandler<Point> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMatcherPattern() {
		return "/point/takestance/(agree|neutral|disagree)/([0-9]+)";
	}

	@Override
	protected boolean handlesAction(String action) {
		return action.equals("agree") || action.equals("neutral") || action.equals("disagree");
	}

	@Override
	protected Point getResource(long id, PMSession pmSession) {
		Key key = KeyFactory.createKey(Point.class.getSimpleName(), id);
		return pmSession.getMapper(PointMapper.class).selectPostById(key);
	}
	
	@Override
	protected void performAction(HttpServletRequest request,
			HttpServletResponse response, Point resource, String command,
			ApplicationUser user, PMSession pmSession) throws IOException {
		
		PointMapper mapper = pmSession.getMapper(PointMapper.class);
		try {
			UserStance existingStance = mapper.selectStance(resource.getPointId(), user.getUserId());
			if(existingStance == null) {
				mapper.insertNewStance(new UserStance(user.getUserId(), resource.getPointId(), 
						Stance.getStanceAsId(command)));
			} else if(!existingStance.getStance().equals(Stance.getStanceAsId(command))) {
				Integer oldStance = existingStance.getStance();
				existingStance.setStance(Stance.getStanceAsId(command));
				mapper.updateStance(existingStance, oldStance);
			}
		} catch (CommunarchyPersistenceException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		response.sendRedirect(String.format("/arg/%d", resource.getParentId().getId()));
	}
}