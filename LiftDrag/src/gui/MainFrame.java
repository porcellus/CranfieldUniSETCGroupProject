package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements LogoutListener
{

    private LoginDialog loginDialog;
    private SessionPanel sessionPanel;


    public MainFrame() {
    	setResizable(false);

    	loginDialog = new LoginDialog(this, true);
    	loginDialog.setVisible(true);
    	
    	sessionPanel = new SessionPanel();
    	sessionPanel.setMinimumSize(new Dimension(600, 675));
    	sessionPanel.addLogoutListener(this);
    	getContentPane().add(sessionPanel, BorderLayout.CENTER);
    	
    	getContentPane().setBackground(Color.BLACK);
    	setTitle("LiftDrag");
    	pack();
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	
    }

	@Override
	public void logoutButtonPressed()
	{
		this.setVisible(false);
		loginDialog.setVisible(true);
	}
}
