package communarchy.facts.results;

import java.util.List;

public class PageSet<T> {
	
	private String startCursor;
	private String endCursor;
	private List<T> pages;
	
	public PageSet(List<T> pages, String startCursor, String endCursor) {
		this.pages = pages;
		this.startCursor = startCursor;
		this.endCursor = endCursor;
	}
	
	public String getLast() {
		return startCursor;
	}
	
	public String getNext() {
		return endCursor;
	}
	
	public List<T> getPages() {
		return pages;
	}
}