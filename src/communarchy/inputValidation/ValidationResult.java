package communarchy.inputValidation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	private String name;
	private String content;
	private String message;
	private List<String> errors;
	
	@SuppressWarnings("unused")
	private ValidationResult() {}
	
	public ValidationResult(String name, String content) {
		this.name = name;
		this.content = content;
		this.message = String.format("Problems with the %s you specified", name);
		this.errors = new ArrayList<String>();
	}
	
	public String getName() {
		if(name == null) {
			return " ";
		}
		return name;
	}
	
	public String getContent() {
		if(content == null) {
			return " ";
		}
		return content;
	}
	
	public String getMessage() {
		return message;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public boolean isValid() {
		if(errors.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public void addError(String error) {
		errors.add(error);
	}
}