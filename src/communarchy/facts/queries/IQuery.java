package communarchy.facts.queries;

import communarchy.facts.interfaces.IEntity;

public interface IQuery<T extends IEntity> {
	public static final String PARAM_FORMAT = "%sParam";
	
	public String getMemcacheInnerKey();
}