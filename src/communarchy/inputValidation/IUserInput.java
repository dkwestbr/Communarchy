package communarchy.inputValidation;

public interface IUserInput {
	public int getMaxLength();
	public int getMinLength();
	public boolean htmlAllowed();
	public String getContentName();
}
