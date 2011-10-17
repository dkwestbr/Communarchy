package communarchy.facts.mappers;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultList;

import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IArgMapper;

public class ArgumentMapper extends AbstractMapper<ArgumentMapper> implements IArgMapper {
	
	public ArgumentMapper() {}

	@Override
	public void insertNewPost(IArgument post) {
		pmSession.getPM().makePersistent(post);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IPoint> selectChildrenPosts(Key id) {
		List<IPoint> points;
		Query q = pmSession.getPM().newQuery(Point.class);
		try {
			q.setFilter("parentArgId == parentIdParam");
			q.declareParameters(String.format("%s parentIdParam", Key.class.getName()));
			points = (List<IPoint>) q.execute(id);	
		} finally {
			q.closeAll();
		}
		
		return points;
	}

	@Override
	public QueryResultList<Entity> buildArgFeed(int numArgs, String startCursor) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(Argument.class.getName());
        PreparedQuery pq = datastore.prepare(q);
        int pageSize = 15;
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        if (startCursor != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
        }
        
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);

        return results;
	}

	@Override
	public IArgument selectPostById(Key id) {
		return pmSession.getPM().getObjectById(Argument.class, id);
	}
}