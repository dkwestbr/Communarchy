package communarchy.facts.queries.list;
import communarchy.facts.interfaces.IEntity;

public interface IPagedQuery<T extends IEntity<T>> extends IListQuery<T> {
	public String getStartCursorString();
}
