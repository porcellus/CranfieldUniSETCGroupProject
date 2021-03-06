package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.ComponentOrientation;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import session.Session;
import database.SQLiteConnection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class ParamDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	
	private JLabel camberLabel;
	private JLabel thicknessLabel;
	private JLabel angleLabel;
	private JLabel minLabel;
	private JLabel maxLabel;
	
	private JSpinner camberMinSpinner;
	private JSpinner camberMaxSpinner;
	private JSpinner thicknessMinSpinner;
	private JSpinner thicknessMaxSpinner;
	private JSpinner angleMinSpinner;
	private JSpinner angleMaxSpinner;
	
	/**
	 * Create the dialog.
	 */
	public ParamDialog(JFrame parent, boolean modal, final String sessionName, final char[] password)
	{
		super(parent, modal);
		setResizable(false);
		
		setTitle("Input parameters");
		setBounds(100, 100, 300, 240);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		
		camberLabel = new JLabel("Camber");
		thicknessLabel = new JLabel("Thickness");
		angleLabel = new JLabel("Angle");
		
		minLabel = new JLabel("Min");
		maxLabel = new JLabel("Max");
		
		camberMinSpinner = new JSpinner();
		camberMinSpinner.setModel(new SpinnerNumberModel(-20.0, -20.0, 20.0, 0.1));
		camberMaxSpinner = new JSpinner();
		camberMaxSpinner.setModel(new SpinnerNumberModel(20.0, -20.0, 20.0, 0.1));
		thicknessMinSpinner = new JSpinner();
		thicknessMinSpinner.setModel(new SpinnerNumberModel(1.0, 1.0, 20.0, 0.1));
		thicknessMaxSpinner = new JSpinner();
		thicknessMaxSpinner.setModel(new SpinnerNumberModel(20.0, 1.0, 20.0, 0.1));
		angleMinSpinner = new JSpinner();
		angleMinSpinner.setModel(new SpinnerNumberModel(-15.0, -15.0, 15.0, 0.1));
		angleMaxSpinner = new JSpinner();
		angleMaxSpinner.setModel(new SpinnerNumberModel(15.0, -15.0, 15.0, 0.1));
		
		
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(camberLabel)
								.addComponent(thicknessLabel)
								.addComponent(angleLabel))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(angleMinSpinner)
								.addComponent(camberMinSpinner)
								.addComponent(thicknessMinSpinner)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(minLabel)
							.addGap(18)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(angleMaxSpinner)
								.addComponent(camberMaxSpinner)
								.addComponent(thicknessMaxSpinner)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(35)
							.addComponent(maxLabel)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(maxLabel)
							.addGap(18)
							.addComponent(camberMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(thicknessMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(angleMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(minLabel)
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(camberMinSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(camberLabel))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(thicknessMinSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(thicknessLabel))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(angleMinSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(angleLabel))))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if((double) angleMinSpinner.getValue() >= (double) angleMaxSpinner.getValue()
								|| (double) thicknessMinSpinner.getValue() >= (double) thicknessMaxSpinner.getValue()
								|| (double) camberMinSpinner.getValue() >= (double) camberMaxSpinner.getValue())
						{
							JOptionPane.showMessageDialog(null, "The maximum values must be bigger than minimum values.", "Wrong values", JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							try
							{
								SQLiteConnection database = new SQLiteConnection();
								database.connection();
								Session s = database.createSession(sessionName, new String(password));
								s.setParameters((double) angleMinSpinner.getValue(),
												(double) angleMaxSpinner.getValue(),
												(double) thicknessMinSpinner.getValue(),
												(double) thicknessMaxSpinner.getValue(),
												(double) camberMinSpinner.getValue(),
												(double) camberMaxSpinner.getValue());
		
								database.setSessionParameters(s);
								
								JOptionPane.showMessageDialog(null, "Session \"" + sessionName + "\" has been created.");
							}
							catch (SQLException ex)
							{
								ex.printStackTrace();
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
							
							dispose();
						}	
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
