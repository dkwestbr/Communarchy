package communarchy.controllers.handlers.input.validation;

public interface IUserInput {
	public int getMaxLength();
	public int getMinLength();
	public boolean htmlAllowed();
	public String getContentName();
	public String getDisplayName();
}
