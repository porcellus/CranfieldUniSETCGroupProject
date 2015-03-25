package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements LoginListener, LogoutListener
{

    private LoginDialog loginDialog;
    private SessionPanel sessionPanel;


    public MainFrame() {
    	setResizable(false);

    	sessionPanel = new SessionPanel();
    	sessionPanel.setMinimumSize(new Dimension(600, 800));
    	sessionPanel.addLogoutListener(this);
    	getContentPane().add(sessionPanel, BorderLayout.CENTER);
    	
    	loginDialog = new LoginDialog(this, true);
    	loginDialog.addLoginListener(this);
    	loginDialog.setVisible(true);
    	
    	getContentPane().setBackground(Color.BLACK);
    	pack();
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	
    }

	@Override
	public void logoutButtonPressed()
	{
		sessionPanel.stopSession();

		this.setVisible(false);	
		loginDialog.setVisible(true);
	}

	@Override
	public void openButtonPressed(String sessionName, char[] password)
	{
		setTitle("LiftDrag - session: " + sessionName);
		loginDialog.setVisible(false);
		
		sessionPanel.loginSession(sessionName, password);
		sessionPanel.startSession();
		this.setVisible(true);
	}

	@Override
	public void createButtonPressed(String session, char[] password)
	{
		// TODO Auto-generated method stub
		
	}


}
