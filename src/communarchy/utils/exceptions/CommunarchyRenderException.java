package communarchy.utils.exceptions;

public class CommunarchyRenderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CommunarchyRenderException(Exception e) {
		super(e);
	}

	public CommunarchyRenderException(String message) {
		super(message);
	}
}
