package communarchy.facts.mappers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import communarchy.facts.implementations.Argument;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.mappers.interfaces.AbstractMapper;
import communarchy.facts.mappers.interfaces.IArgMapper;
import communarchy.facts.results.PageSet;
import communarchy.rankingStrategies.PointRankStrategy;

public class ArgumentMapper extends AbstractMapper<ArgumentMapper> implements IArgMapper {
	
	public ArgumentMapper() {}

	@Override
	public void insertNewPost(Argument post) {
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
		
		Collections.sort(points, new PointRankStrategy(pmSession));
		return points;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageSet<Argument> buildPostFeeed(int numArgs, String startCursor) {
		
		PersistenceManager pm = pmSession.getPM();
        Query query = pm.newQuery(Argument.class);
        query.setRange(0, numArgs);
        query.setOrdering("createDate desc");
        
        if(startCursor != null) {
        	Map<String, Object> extensionMap = new HashMap<String, Object>();
            extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, Cursor.fromWebSafeString(startCursor));
            query.setExtensions(extensionMap);
        }
        
        List<Argument> results = (List<Argument>) query.execute();

        return new PageSet<Argument>(results, startCursor, JDOCursorHelper.getCursor(results).toWebSafeString());
	}

	@Override
	public Argument selectPostById(Key id) {
		return pmSession.getPM().getObjectById(Argument.class, id);
	}
}