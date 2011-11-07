package communarchy.inputValidation;

import java.io.IOException;

public class UserInputValidator implements IUserInputValidator {

	public static String MESSAGE_KEY = "message";
	public static String ERROR_KEY = "errors";
	public static String RESOURCE_KEY = "resource";
	public static String CONTENT_KEY = "content";
	
	private static int TOO_LONG_TO_SHOW_LIMIT = 300;
	private static String NULL_OR_EMPTY = " not specified";
	private static String TOO_LONG = " must be less than ";
	private static String TOO_LONG_END = " characters";
	private static String TOO_SHORT = " must be more than ";
	private static String TOO_SHORT_END = " characters";
	private static String TOO_LONG_DONT_SHOW_CHAR_LIMIT = " was long...really long...too long.  Are you sure you can't be more concise?";
	private static UserInputValidator INSTANCE = null;
	
	private UserInputValidator(){}
	
	public static UserInputValidator getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new UserInputValidator();
		}
		
		return INSTANCE;
	}
	
	@Override
	public ValidationResult validateUserInput(IUserInput input,
			String content) throws IOException {
		if(content == null) {
			throw new IOException("Content must not be null");
		}
		return validate(content, input.getMinLength(), input.getMaxLength(), input.htmlAllowed(), input.getContentName(), input.getDisplayName());
	}
	
	private static ValidationResult validate(String input, int min, int max, boolean htmlAllowed, String name, String displayName) {
		
		if(htmlAllowed) {	
			// TODO: protect from xss with html exclusion
		} else {
			// TODO: strip html
		}
		
		ValidationResult result = new ValidationResult(name, input);
		if(input == null) {
			result.addError(String.format("%s%s", displayName, NULL_OR_EMPTY));
			return result;
		}
		
		if(input.length() > max) {
			if(input.length() > TOO_LONG_TO_SHOW_LIMIT) {
				result.addError(String.format("%s%s", displayName, TOO_LONG_DONT_SHOW_CHAR_LIMIT));
			} else {
				result.addError(String.format("%s%s%d%s", displayName, TOO_LONG, max, TOO_LONG_END));
			}
		} else if(input.length() <= min) {
			result.addError(String.format("%s%s%d%s", displayName, TOO_SHORT, min, TOO_SHORT_END));
		}
		
		return result;
	}
}