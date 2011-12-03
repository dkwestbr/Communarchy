package communarchy.facts.actions;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.interfaces.IEntity;

@PersistenceCapable
public class Vote implements IVote, Serializable, IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
	private Key povKey;
    
    @Persistent
	private Key pointKey;
    
    @Persistent
	private Key userKey;
	
    public static final String P_POV_KEY = "povKey";
    public static final String P_USER_KEY = "userKey";
    
	public Vote() {}
	
	public Vote(Key pointId, Key povId, Key userId) {
		if(povId == null || userId == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.pointKey = pointId;
		this.povKey = povId;
		this.userKey = userId;
	}
	
	@Override
	public Key getUserKey() {
		return this.userKey;
	}

	@Override
	public Key getPovKey() {
		return this.povKey;
	}

	@Override
	public Key getKey() {
		return this.key;
	}
	
	@Override
	public Key getPointKey() {
		return this.pointKey;
	}
	
	public static String BuildVoteQueryKey(Key povId, Key userId) {
		return String.format("Vote(%s_%s)", povId.toString(), userId.toString());
	}

	@Override
	public String getMemcacheCheckinKey() {
		return String.format("new_%s_%s_%s", Vote.class.getName(), povKey.toString(), userKey.toString());
	}	
}