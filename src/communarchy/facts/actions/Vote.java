package communarchy.facts.actions;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.IEntity;
import communarchy.facts.actions.interfaces.IVote;

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
	private Key userKey;
	
	public Vote() {}
	
	public Vote(Key povId, Key userId) {
		if(povId == null || userId == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
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
}