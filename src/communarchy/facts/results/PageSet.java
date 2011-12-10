package communarchy.facts.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import communarchy.facts.PMSession;
import communarchy.facts.interfaces.IEntity;
import communarchy.facts.mappers.BasicMapper;
import communarchy.utils.caching.ListUtils;
import communarchy.utils.exceptions.CommunarchyPersistenceException;

public class PageSet<T extends IEntity<T>> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String startCursor;
	private String endCursor;
	private String pageIds;
	
	private Class<T> type;
	
	public PageSet(List<T> pages, Class<T> pageType, String startCursor, String endCursor) {
		this.pageIds = ListUtils.joinKeys(pages);
		this.startCursor = startCursor;
		this.endCursor = endCursor;
		this.type = pageType;
	}
	
	public String getLast() {
		return startCursor;
	}
	
	public String getNext() {
		return endCursor;
	}
	
	public List<T> getPages(PMSession pmSession) {
		List<Key> entityKeys = ListUtils.keyStringToKeyList(pageIds, type.getSimpleName());
		List<T> pages = new ArrayList<T>();
		try {
			for(Key key : entityKeys) {
				pages.add(pmSession.getMapper(BasicMapper.class).select(type, key));
			}
		} catch(CommunarchyPersistenceException e) {
			e.printStackTrace();
		}
		
		return pages;
	}
}