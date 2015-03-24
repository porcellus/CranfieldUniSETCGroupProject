package gui;

public interface LoginListener
{
	public void openButtonPressed(String session, char[] password);
	public void createButtonPressed(String session, char[] password);
}
