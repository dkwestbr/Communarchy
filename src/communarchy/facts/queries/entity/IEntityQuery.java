package communarchy.facts.queries.entity;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IEntity;
import communarchy.facts.queries.IQuery;

public interface IEntityQuery<T extends IEntity> extends IQuery<T> {
	public T getNewEntity();
	public T runQuery(PMSession pmSession);
}