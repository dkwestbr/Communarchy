package communarchy.controllers.handlers.input.validation;

import java.io.IOException;

public interface IUserInputValidator {
	ValidationResult validateUserInput(IUserInput input, String content)
			throws IOException;
}
