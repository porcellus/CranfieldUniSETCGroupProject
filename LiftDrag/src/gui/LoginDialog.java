package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class LoginDialog extends JDialog implements ActionListener
{

	private final JPanel contentPanel = new JPanel();
	
	private JTextField sessionField;
	private JPasswordField passwordField;
	
	private JLabel sessionLabel;
	private JLabel passwordLabel;
	
	private JButton createButton;
	private JButton openButton;

	/**
	 * Create the dialog.
	 */
	public LoginDialog(final JFrame parent, boolean modal)
	{
        super(parent, modal);
        
		setResizable(false);
		setTitle("Session login");
		setBounds(100, 100, 356, 188);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{31, 78, 198, 31, 0};
		gbl_contentPanel.rowHeights = new int[]{36, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			sessionLabel = new JLabel("Session name:");
			GridBagConstraints gbc_sessionLabel = new GridBagConstraints();
			gbc_sessionLabel.insets = new Insets(0, 0, 5, 5);
			gbc_sessionLabel.anchor = GridBagConstraints.EAST;
			gbc_sessionLabel.gridx = 1;
			gbc_sessionLabel.gridy = 1;
			contentPanel.add(sessionLabel, gbc_sessionLabel);
		}
		{
			sessionField = new JTextField();
			GridBagConstraints gbc_sessionField = new GridBagConstraints();
			gbc_sessionField.fill = GridBagConstraints.HORIZONTAL;
			gbc_sessionField.anchor = GridBagConstraints.NORTH;
			gbc_sessionField.insets = new Insets(0, 0, 5, 5);
			gbc_sessionField.gridx = 2;
			gbc_sessionField.gridy = 1;
			contentPanel.add(sessionField, gbc_sessionField);
			sessionField.setColumns(10);
		}
		{
			passwordLabel = new JLabel("Password:");
			GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
			gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
			gbc_passwordLabel.anchor = GridBagConstraints.EAST;
			gbc_passwordLabel.gridx = 1;
			gbc_passwordLabel.gridy = 2;
			contentPanel.add(passwordLabel, gbc_passwordLabel);
		}
		{
			passwordField = new JPasswordField();
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.insets = new Insets(0, 0, 5, 5);
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 2;
			gbc_passwordField.gridy = 2;
			contentPanel.add(passwordField, gbc_passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				createButton = new JButton("Create");
				createButton.addActionListener(this);
				buttonPane.add(createButton);
				getRootPane().setDefaultButton(createButton);
			}
			{
				openButton = new JButton("Open");
				openButton.addActionListener(this);
				buttonPane.add(openButton);
			}
		}
		
		addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent e) { 
                parent.dispose();
                System.exit(0);  
            }  
        });
		
		addComponentListener ( new ComponentAdapter ()
		{
			public void componentShown ( ComponentEvent e )
	        {
				sessionField.requestFocusInWindow();
	        }
		});
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton)
		{
			String session = sessionField.getText();
			char[] password = passwordField.getPassword();
			if(session.isEmpty())
				JOptionPane.showMessageDialog(this, "Session name field is empty.", "Wrong session name", JOptionPane.ERROR_MESSAGE);
			else if(password.length == 0)
				JOptionPane.showMessageDialog(this, "Password field is empty.", "Wrong password", JOptionPane.ERROR_MESSAGE);
			else
			{
				// TODO: Checking if the session name exists, if it does - gtfo
				// TODO: Creating account here
				
				JOptionPane.showMessageDialog(this, "Account \"" + sessionField.getText() + "\" has been created.");
			}
			
			Arrays.fill(password, '0');
			sessionField.setText("");
			passwordField.setText("");
			
			sessionField.requestFocusInWindow();
		}
		else if(e.getSource() == openButton)
		{
			String session = sessionField.getText();
			char[] password = passwordField.getPassword();
			if(session.isEmpty())
				JOptionPane.showMessageDialog(this, "Session name field is empty.", "Wrong session name", JOptionPane.ERROR_MESSAGE);
			else if(password.length == 0)
				JOptionPane.showMessageDialog(this, "Password field is empty.", "Wrong password", JOptionPane.ERROR_MESSAGE);
			else
			{
				// TODO: Checking if the session name exists, if it doesn't or wrong password - gtfo
				// TODO: Creating account here
				
				setVisible(false);
				getParent().setVisible(true);
			}
			
			Arrays.fill(password, '0');
			sessionField.setText("");
			passwordField.setText("");
		}
	}
}
