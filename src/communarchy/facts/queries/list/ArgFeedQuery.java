package communarchy.facts.queries.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.Query;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;

public class ArgFeedQuery implements IPagedQuery<Argument> {

	@SuppressWarnings("unused")
	private ArgFeedQuery() {}
	
	private static final int NUM_ARGS = 20;
	private String startCursor;
	
	private String memcacheKey;
	
	private List<String> checkInKeys;
	
	public ArgFeedQuery(String startCursor) {
		this.startCursor = startCursor;
		
		this.memcacheKey = String.format("%s_%s", ArgFeedQuery.class.getName(), startCursor);
		this.checkInKeys = new ArrayList<String>();
		this.checkInKeys.add(Argument.class.getName());
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
	public List<String> getCheckInKeys() {
		return this.checkInKeys;
	}

	@Override
	public Class<Argument> getResourceType() {
		return Argument.class;
	}

	@Override
	public String getStartCursorString() {
		return startCursor;
	}
}