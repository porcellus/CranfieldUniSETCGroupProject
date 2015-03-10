package gui;

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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import visualize.WingPanel;
import astral.AstralControl;
import astral.OptimizationControl;

public class SessionPanel extends JPanel implements ActionListener
{
	private JButton logButton;
	private JButton logoutButton;
	
	private JLabel param1Label;
	private JLabel param2Label;
	private JLabel param3Label;
	private JLabel param4Label;
	
	private JTextField param1Field;
	private JTextField param2Field;
	private JTextField param3Field;
	private JTextField param4Field;
	
	private LogoutListener logoutListener;
	
	private final OptimizationControl optControl;
	private WingPanel wingPanel;
	private JButton startButton;
	private Boolean sessionRunning;
	
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
		
		param1Label = new JLabel("parameter 1:");
		param2Label = new JLabel("parameter 2:");
		param3Label = new JLabel("parameter 3:");
		param4Label = new JLabel("parameter 4:");
		
		param1Field = new JTextField();
		param1Field.setColumns(10);
		
		param2Field = new JTextField();
		param2Field.setColumns(10);
		
		param3Field = new JTextField();
		param3Field.setColumns(10);
		
		param4Field = new JTextField();
		param4Field.setColumns(10);
		
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(logButton)
							.addPreferredGap(ComponentPlacement.RELATED, 441, Short.MAX_VALUE)
							.addComponent(logoutButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(wingPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(param2Label, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
										.addComponent(param1Label))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(param1Field, 216, 216, 216)
										.addComponent(param2Field, 216, 216, 216))))
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(param3Label, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
								.addComponent(param4Label, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(param4Field, 216, 216, 216)
								.addComponent(param3Field, 216, 216, 216))
							.addGap(7))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(295)
							.addComponent(startButton)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(logButton)
						.addComponent(logoutButton))
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(param3Field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(param3Label))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(param4Field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(param4Label)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(param1Field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(param1Label))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(param2Field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(param2Label))))
					.addGap(14)
					.addComponent(startButton)
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addComponent(wingPanel, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
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
