package communarchy.facts.implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.interfaces.IEntity;
import communarchy.facts.interfaces.IUser;

@PersistenceCapable
public class ApplicationUser implements IUser, Serializable, IEntity<ApplicationUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key userId;
	
	@Persistent
	private int userRoleId;
	
	@Persistent
	private String displayName;
	
	@Persistent
	private String href;
	
	private List<String> checkOutKeys;
	
	private String TEMPL_HREF_KEY = "href";
	private String TEMPL_DISPLAY_NAME_KEY = "displayName";
	
	private Map<String, String> templateMap = null;
	
	public ApplicationUser(){}
	
	public ApplicationUser(String displayName) {
		this(displayName, -1, "");
	}
	
	public ApplicationUser(String displayName, Integer userRoleId, String href) {
		this.displayName = displayName;
		this.userRoleId = userRoleId;
		this.href = href;
		
		InitCheckOutKeys(this);
	}
	
	private static void InitCheckOutKeys(ApplicationUser user) {
		user.checkOutKeys = new ArrayList<String>();
		user.checkOutKeys.add(String.format("%s(%s)", ApplicationUser.class.getName(), user.getDisplayName()));
	}
	
	@Override
	public Key getUserId() {
		return userId;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getHref() {
		if(href.equals("")) {
			href = "/users/" + userId.getId();
		}
		return href;
	}
	
	public Map<String, String> getTemplateMap() {
		if(templateMap == null) {
			templateMap = new HashMap<String, String>();
		}
		
		templateMap.put(TEMPL_DISPLAY_NAME_KEY, displayName);
		templateMap.put(TEMPL_HREF_KEY, getHref());
		
		return templateMap;
	}

	@Override
	public Integer getUserRoleId() {
		return userRoleId;
	}

	@Override
	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public void setUserRoleId(Integer id) {
		this.userRoleId = id;
		InitCheckOutKeys(this);
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public Key getKey() {
		return this.userId;
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			InitCheckOutKeys(this);
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(ApplicationUser updateValue) {
		this.displayName = updateValue.getDisplayName();
		this.userRoleId = updateValue.getUserRoleId();
		this.checkOutKeys = updateValue.getCheckOutKeys();
	}
}
