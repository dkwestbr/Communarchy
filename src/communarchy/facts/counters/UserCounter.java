package communarchy.facts.counters;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import communarchy.facts.implementations.ApplicationUser;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserCounter extends AbstractCounter<ApplicationUser, UserCounter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> checkOutKeys;
	
	public UserCounter(int shardNum) {
		super(shardNum);
	}

	@Override
	public List<String> getCheckOutKeys() {
		if(this.checkOutKeys == null || this.checkOutKeys.isEmpty()) {
			this.checkOutKeys = new ArrayList<String>();
			this.checkOutKeys.add(ApplicationUser.class.getName());
			
		}
		
		return this.checkOutKeys;
	}

	@Override
	public void update(UserCounter updateValue) {
		this.count = updateValue.count;
	}
}