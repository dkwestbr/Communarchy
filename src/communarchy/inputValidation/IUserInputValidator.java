package communarchy.inputValidation;

import java.io.IOException;

public interface IUserInputValidator {
	ValidationResult validateUserInput(IUserInput input, String content)
			throws IOException;
}
