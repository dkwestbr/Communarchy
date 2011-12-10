package communarchy.facts.queries;

import java.util.List;

import communarchy.facts.interfaces.IEntity;

public interface IQuery<T extends IEntity<T>> {
	public static final String PARAM_FORMAT = "%sParam";
	
	public String getMemcacheInnerKey();
	public List<String> getCheckInKeys();
}