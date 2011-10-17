package communarchy.exceptions;

public class CommunarchyPersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommunarchyPersistenceException(Exception e) {
		super(e);
	}

	public CommunarchyPersistenceException(String message) {
		super(message);
	}
}
