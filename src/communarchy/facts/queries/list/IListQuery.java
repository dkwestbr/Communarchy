package communarchy.facts.queries.list;

import java.util.List;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IEntity;
import communarchy.facts.queries.IQuery;

public interface IListQuery<T extends IEntity<T>> extends IQuery<T> {
	public List<T> runListQuery(PMSession pmSession);
	public Class<T> getResourceType();
}