package communarchy.vb.global.nodes;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Key;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.ApplicationUser;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.BasicMapper;
import communarchy.facts.mappers.CountMapper;
import communarchy.facts.queries.list.GetRepCount;
import communarchy.utils.exceptions.CommunarchyPersistenceException;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceSubsetWrapper;

public class UserWithRep extends AbstractTemplateWrapper implements
		IResourceSubsetWrapper<Key, String> {

	private static final Logger log =
		      Logger.getLogger(UserWithRep.class.getName());
	
	private static UserWithRep INSTANCE;
	private UserWithRep() {}
	
	public static UserWithRep get() {
		if(INSTANCE == null) {
			INSTANCE = new UserWithRep();
		}
		
		return INSTANCE;
	}
	 
	private static final String P_USER_HREF = "userHref";
	private static final String P_REP = "rep";
	private static final String P_USER_DISPLAY_NAME = "userDisplayName";
	private static final String P_SIGNATURE_ACTION = "signatureAction";

	@Override
	public String getTemplate() {
		return "./templates/html/global/nodes/UserWithRep.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser viewer,
			HttpServletRequest request, Key targetUserKey, String action) {
		
		SoyMapData pMap = new SoyMapData();
		ApplicationUser targetUser = null;
		try {
			targetUser = pmSession.getMapper(BasicMapper.class).select(ApplicationUser.class, targetUserKey);
			pMap.put(P_SIGNATURE_ACTION, action);
			pMap.put(P_REP, pmSession.getMapper(CountMapper.class).getCount(new GetRepCount(targetUserKey)));
			pMap.put(P_USER_DISPLAY_NAME, targetUser.getDisplayName());
			pMap.put(P_USER_HREF, targetUser.getHref());
		} catch (CommunarchyPersistenceException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		return pMap;
	}
}