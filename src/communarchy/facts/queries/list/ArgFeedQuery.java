package communarchy.facts.queries.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.Query;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;

public class ArgFeedQuery implements IListQuery<Argument> {

	@SuppressWarnings("unused")
	private ArgFeedQuery() {}
	
	private static final int NUM_ARGS = 20;
	private String startCursor;
	
	private String memcacheKey;
	
	public ArgFeedQuery(String startCursor) {
		this.startCursor = startCursor;
		
		this.memcacheKey = String.format("%s_%s", ArgFeedQuery.class.getName(), startCursor);
	}
	
	@Override
	public String getMemcacheInnerKey() {
		return memcacheKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Argument> runListQuery(PMSession pmSession) {
        Query query = pmSession.getPM().newQuery(Argument.class);
        query.setRange(0, NUM_ARGS);
        query.setOrdering("createDate desc");
        
        if(startCursor != null) {
        	Map<String, Object> extensionMap = new HashMap<String, Object>();
            extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, Cursor.fromWebSafeString(startCursor));
            query.setExtensions(extensionMap);
        }
        
		List<Argument> results = (List<Argument>) query.execute();
		return results;
	}

	@Override
	public Class<Argument> getType() {
		return Argument.class;
	}

	@Override
	public String getExpiryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRankChangeKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRanked() {
		return true;
	}
}