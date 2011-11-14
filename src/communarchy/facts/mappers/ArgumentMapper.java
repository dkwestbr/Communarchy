package communarchy.facts.mappers;

import java.util.ArrayList;
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
import communarchy.utils.caching.CommunarchyCache;
import communarchy.utils.caching.ListUtils;

public class ArgumentMapper extends AbstractMapper<ArgumentMapper> implements IArgMapper {
	
	public ArgumentMapper() {}

	@Override
	public void insertNewPost(Argument post) {
		if(post == null) {
			return;
		}
		
		pmSession.getPM().makePersistent(post);
	}
	
	private static final List<IPoint> EMPTY_RESULT = new ArrayList<IPoint>();
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<IPoint> selectChildrenPosts(Key id) {

		CommunarchyCache.getInstance();
		String key = CommunarchyCache.buildKey(ArgumentMapper.class, 
				"selectChildrenPosts", id.toString());
		List<IPoint> points = null;
		if(CommunarchyCache.getInstance().contains(key)) {
			String pointIds = CommunarchyCache.getInstance().get(key);
			if(pointIds == null) {
				return EMPTY_RESULT;
			}
			
			List<Key> pointKeys = ListUtils.keyStringToKeyList(pointIds, Point.class.getSimpleName());
			points = new ArrayList<IPoint>();
			for(Key pointKey : pointKeys) {
				points.add(pmSession.getMapper(PointMapper.class).selectPostById(pointKey));
			}
		} else {
			Query q = pmSession.getPM().newQuery(Point.class);
			try {
				q.setFilter("parentArgId == parentIdParam");
				q.declareParameters(String.format("%s parentIdParam", Key.class.getName()));
				points = (List<IPoint>) q.execute(id);	
			} finally {
				q.closeAll();
			}
			
			CommunarchyCache.getInstance().putList(id.toString(), key, ((List) points));
		}
		
		return points == null ? EMPTY_RESULT : points;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageSet<Argument> buildPostFeed(int numArgs, String startCursor) {
		
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
		if(id == null) {
			return null;
		}
		
		CommunarchyCache.getInstance();
		String key = CommunarchyCache.buildKey(ArgumentMapper.class, 
				"selectPostById", id.toString());
		
		Argument arg = null;
		if(CommunarchyCache.getInstance().contains(key)) {
			arg = CommunarchyCache.getInstance().get(key);
		} else {
			arg = pmSession.getPM().getObjectById(Argument.class, id);
			CommunarchyCache.getInstance().putEntity(id.toString(), key, arg);
		}
		
		return arg;
	}
}