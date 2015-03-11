package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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

import visualize.WingPanel;
import astral.AstralControl;
import astral.OptimizationControl;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JRadioButton;

public class SessionPanel extends JPanel implements ActionListener
{
	private JButton logButton;
	private JButton logoutButton;
	
	private LogoutListener logoutListener;
	
	private final OptimizationControl optControl;
	private WingPanel wingPanel;
	private JButton startButton;
	private Boolean sessionRunning;
	
	private JScrollPane textArea1Pane;
	private JTextArea textArea1;
	private JScrollPane textArea2Pane;
	private JTextArea textArea2;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	
	/**
	 * Create the panel.
	 */
	public SessionPanel()
	{
		sessionRunning = false;
		optControl = new AstralControl();
		wingPanel = new WingPanel();
		wingPanel.start();
		
		logButton = new JButton("Download logs");
		logButton.addActionListener(this);
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(this);
		
		startButton = new JButton("Start");
		startButton.setVisible(false);
		startButton.addActionListener(this);
		
		JScrollPane textArea1Pane = new JScrollPane();
		textArea1Pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea1Pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textArea1Pane.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		textArea1 = new JTextArea();
		textArea1.setEditable(false);
		textArea1.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea1Pane.setViewportView(textArea1);
		
		JScrollPane textArea2Pane = new JScrollPane();
		textArea2Pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea2Pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textArea2Pane.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		textArea2 = new JTextArea();
		textArea2.setEditable(false);
		textArea2.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea2Pane.setViewportView(textArea2);
		
		radioButton = new JRadioButton("");
		radioButton.setSelected(true);
		radioButton.setEnabled(false);
		
		radioButton_1 = new JRadioButton("");
		radioButton_1.setEnabled(false);
		radioButton_1.setSelected(true);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(logButton)
					.addGap(166)
					.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
					.addComponent(logoutButton)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(wingPanel, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addGap(336))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(160)
					.addComponent(radioButton)
					.addGap(309)
					.addComponent(radioButton_1)
					.addContainerGap(156, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textArea1Pane, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(textArea2Pane, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(logButton)
							.addComponent(logoutButton))
						.addComponent(startButton))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea2Pane, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
						.addComponent(textArea1Pane, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
							.addComponent(radioButton)
							.addGap(11)
							.addComponent(wingPanel, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(19)
							.addComponent(radioButton_1)
							.addContainerGap())))
		);
		
		setLayout(groupLayout);

	}
	
	public void addLogoutListener(LogoutListener logoutListener)
	{
		this.logoutListener = logoutListener;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logoutButton && logoutListener != null)
		{
			int choice = JOptionPane.showConfirmDialog(
							this,
							"Are you sure you want to logout?",
							"Logging out",
							JOptionPane.YES_NO_OPTION);
			
			if(choice == 0)
			{
				// TODO: Terminate session
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
		else if(e.getSource() == startButton)
		{
			if(sessionRunning == false)
			{
				// TODO: pass credentials and start session
				optControl.startSession("gewsd", "gesdv");
				
				sessionRunning = true;
				startButton.setText("Stop");
			}
			else
			{
				wingPanel.setParameters((int)(Math.random() * 20),
                        (int)(Math.random() * 20),
                        (int)(Math.random() * 30 - 15));
				optControl.stopSession();
				
				sessionRunning = false;
				startButton.setText("Start");
			}
		}
	}
}
