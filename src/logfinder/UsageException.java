package logfinder;

/**
 * The UsageException is mainly about providing a catchable exception for end user
 * screw ups
 * 
 * @author tajahem
 *
 */
public class UsageException extends Exception {

	// Should probably look into why eclipse wants this at some point
	private static final long serialVersionUID = 6584478323419777366L;

	private String message;

	public UsageException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Usage Error: " + message;
	}

}
