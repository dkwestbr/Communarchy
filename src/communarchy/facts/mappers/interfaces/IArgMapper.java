package communarchy.facts.mappers.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Entity;

import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;

public interface IArgMapper extends IPostMapper<IArgument>, IPostHasChildrenMapper<IPoint> {
	List<Entity> buildArgFeed(int numArgs, String startCursor);
}