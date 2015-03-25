package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;

import session.Session;
import visualize.WingPanel;
import astral.AstralControl;
import astral.OptimizationControl;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JRadioButton;

import database.SQLiteConnection;
import database.SessionControl;

public class SessionPanel extends JPanel implements ActionListener
{
	private JButton logButton;
	private JButton logoutButton;
	
	private LogoutListener logoutListener;
	
	private Session session;
	private SessionControl sessionControl;
	private final OptimizationControl optControl;
	private WingPanel wingPanel;
	private Boolean sessionRunning;
	
	private JScrollPane textArea1Pane;
	private JTextArea textArea1;
	private JScrollPane textArea2Pane;
	private JRadioButton connectedRadioButton;
	private JRadioButton runningRadioButton;
	private JLabel connectionStatusLabel;
	private JLabel computationStatuslabel;
	
	/**
	 * Create the panel.
	 */
	public SessionPanel()
	{
		sessionRunning = false;
		optControl = new AstralControl((JFrame) this.getParent());
		wingPanel = new WingPanel();
		
		logButton = new JButton("Download logs");
		logButton.addActionListener(this);
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(this);
		
		JScrollPane textArea1Pane = new JScrollPane();
		textArea1Pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea1Pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textArea1Pane.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		textArea1 = new JTextArea();
		textArea1.setEditable(false);
		textArea1.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea1Pane.setViewportView(textArea1);
		
		connectedRadioButton = new JRadioButton("");
		connectedRadioButton.setSelected(false);
		connectedRadioButton.setEnabled(false);
		
		runningRadioButton = new JRadioButton("");
		runningRadioButton.setSelected(false);
		runningRadioButton.setEnabled(false);
		
		connectionStatusLabel = new JLabel("Connection status:");
		
		computationStatuslabel = new JLabel("Computation status:");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(logButton)
					.addPreferredGap(ComponentPlacement.RELATED, 540, Short.MAX_VALUE)
					.addComponent(logoutButton)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(wingPanel, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(421, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(105)
					.addComponent(connectionStatusLabel)
					.addGap(8)
					.addComponent(connectedRadioButton)
					.addPreferredGap(ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
					.addComponent(computationStatuslabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(runningRadioButton)
					.addGap(105))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textArea1Pane, GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(logButton)
						.addComponent(logoutButton))
					.addGap(18)
					.addComponent(textArea1Pane, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(computationStatuslabel, Alignment.TRAILING)
								.addComponent(connectionStatusLabel, Alignment.TRAILING)))
						.addComponent(connectedRadioButton)
						.addComponent(runningRadioButton))
					.addGap(9)
					.addComponent(wingPanel, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		setLayout(groupLayout);

		Timer timer = new Timer(100, this);
        timer.start();
        sessionControl = new SQLiteConnection();
        SQLiteConnection.connection();
        session = null;
	}
	
	public void addLogoutListener(LogoutListener logoutListener)
	{
		this.logoutListener = logoutListener;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (optControl.getStatus() == OptimizationControl.RUNNING)
		{
			connectedRadioButton.setSelected(true);
			runningRadioButton.setSelected(true);
            wingPanel.setParameters(optControl.readResults());
            //resultPanel.updateResult(optControl.readResults());
            if (session != null)
            {
            	session.logResult(optControl.readResults());
            	textArea1.append(optControl.readResults().toString() + "\n");
            }
            repaint();
        }
		else if(optControl.getStatus() == OptimizationControl.CONNECTING)
		{
			if(!connectedRadioButton.isSelected())
				connectedRadioButton.setSelected(true);
			else
				connectedRadioButton.setSelected(false);
			runningRadioButton.setSelected(false);
		}
		else if (optControl.getStatus() == OptimizationControl.READY)
		{
			connectedRadioButton.setSelected(false);
			runningRadioButton.setSelected(false);
		}
		
		if(e.getSource() == logoutButton && logoutListener != null)
		{
			int choice = JOptionPane.showConfirmDialog(
							this,
							"Are you sure you want to logout?",
							"Logging out",
							JOptionPane.YES_NO_OPTION);
			
			if(choice == 0)
			{
				stopSession();
				logoutListener.logoutButtonPressed();
			}
			
			
		}
		else if(e.getSource() == logButton)
		{
			// TODO: get logs
			
			JFrame parentFrame = new JFrame();
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save the logs...");  
			fileChooser.setSelectedFile(new File("logs.txt"));
			
			int userSelection = fileChooser.showSaveDialog(parentFrame);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave = fileChooser.getSelectedFile();
		    
				try
				{
					PrintWriter writer = new PrintWriter(fileToSave.getAbsolutePath(), "UTF-8");
					// TODO: print logs to file
					writer.println("first log");
					writer.println("second log");
					writer.close();
					
					JOptionPane.showMessageDialog(new JFrame(),
						    "The logs have been successfully saved to " + fileToSave.getName() + ".",
						    "Logs saved",
						    JOptionPane.INFORMATION_MESSAGE);

				} catch (IOException ex)
				{
					JOptionPane.showMessageDialog(new JFrame(),
						    ex.getMessage(),
						    "Error while saving",
						    JOptionPane.ERROR_MESSAGE);  
				}
			}
		}
	}

	public void loginSession(String sessionName, char[] password)
	{
		try
		{
			session = sessionControl.loginSession(sessionName, new String(password));
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}      
	}
	
	public void startSession()
	{
		// TODO Auto-generated method stub
		if (session == null)
			System.err.println("Session not created.");
		else
		{
			wingPanel.start();
			optControl.startSession(session);
		}
	}
	
	public void stopSession() {
		//wingPanel.setParameters(optControl.readResults());
        optControl.stopSession();
    }
}
