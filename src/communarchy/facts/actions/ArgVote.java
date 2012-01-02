package communarchy.facts.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.actions.interfaces.IArgVote;
import communarchy.facts.interfaces.IEntity;

@PersistenceCapable
public class ArgVote implements IArgVote, Serializable, IEntity<ArgVote> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
	private Key argKey;
    
    @Persistent
	private Key userKey;
    
    @Persistent
    private Integer state;
    
    public static final Integer VOTE_STATE_UP = 1;
    public static final Integer VOTE_STATE_NEUTRAL = 0;
    public static final Integer VOTE_STATE_DOWN = -1;
	
    public static final String P_ARG_KEY = "argKey";
    public static final String P_USER_KEY = "userKey";
    
	public ArgVote() {}
	
	@NotPersistent
	private List<String> checkOutKeys;
	
	public ArgVote(Key argId, Key userId, Integer state) {
		if(argId == null || userId == null) {
			throw new NullPointerException("To preserve integrity, params of type " 
					+ Key.class.getCanonicalName() + " must not be null");
		}
		
		this.argKey = argId;
		this.userKey = userId;
		this.state = state;
	}
	
	@Override
	public Key getUserKey() {
		return this.userKey;
	}

	@Override
	public Key getKey() {
		return this.key;
	}
	
	public static String BuildArgVoteQueryKey(Key argId, Key userId) {
		return String.format("ArgVote(%s_%s)", argId.toString(), userId.toString());
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null) {
			this.checkOutKeys = new ArrayList<String>();
			this.checkOutKeys.add(String.format("%s(%s)", ArgVote.class.getName(), argKey.toString()));
			this.checkOutKeys.add(String.format("%s(%s_%s)", ArgVote.class.getName(), argKey.toString(), userKey.toString()));
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(ArgVote updateValue) {
		this.argKey = updateValue.argKey;
		this.state = updateValue.state;
		this.userKey = updateValue.userKey;
	}

	@Override
	public Key getArgKey() {
		return this.argKey;
	}

	@Override
	public Integer getVoteState() {
		return this.state;
	}
}