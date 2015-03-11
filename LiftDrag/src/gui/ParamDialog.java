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
import database.SQLiteConnexion;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ParamDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	
	private JLabel camberLabel;
	private JLabel thicknessLabel;
	private JLabel angleLabel;
	private JLabel minLabel;
	private JLabel maxLabel;
	private JLabel startLabel;
	
	private JSpinner camberMinSpinner;
	private JSpinner camberMaxSpinner;
	private JSpinner camberStartSpinner;
	private JSpinner thicknessMinSpinner;
	private JSpinner thicknessMaxSpinner;
	private JSpinner thicknessStartSpinner;
	private JSpinner angleMinSpinner;
	private JSpinner angleMaxSpinner;
	private JSpinner angleStartSpinner;
	
	/**
	 * Create the dialog.
	 */
	public ParamDialog(JFrame parent, boolean modal, final String session, final char[] password)
	{
		super(parent, modal);
		setResizable(false);
		
		setTitle("Input parameters");
		setBounds(100, 100, 370, 240);
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
		startLabel = new JLabel("Start");
		
		camberMinSpinner = new JSpinner();
		camberMinSpinner.setModel(new SpinnerNumberModel(-20, -20, 20, 1));
		camberMaxSpinner = new JSpinner();
		camberMaxSpinner.setModel(new SpinnerNumberModel(20, -20, 20, 1));
		camberStartSpinner = new JSpinner();
		camberStartSpinner.setModel(new SpinnerNumberModel(0, -20, 20, 1));
		thicknessMinSpinner = new JSpinner();
		thicknessMinSpinner.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		thicknessMaxSpinner = new JSpinner();
		thicknessMaxSpinner.setModel(new SpinnerNumberModel(20, 1, 20, 1));
		thicknessStartSpinner = new JSpinner();
		thicknessStartSpinner.setModel(new SpinnerNumberModel(10, 1, 20, 1));
		angleMinSpinner = new JSpinner();
		angleMinSpinner.setModel(new SpinnerNumberModel(-15, -15, 15, 1));
		angleMaxSpinner = new JSpinner();
		angleMaxSpinner.setModel(new SpinnerNumberModel(15, -15, 15, 1));
		angleStartSpinner = new JSpinner();
		angleStartSpinner.setModel(new SpinnerNumberModel(0, -15, 15, 1));
		
		
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(25, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(camberLabel)
								.addComponent(thicknessLabel)
								.addComponent(angleLabel))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(angleMinSpinner)
								.addComponent(camberMinSpinner)
								.addComponent(thicknessMinSpinner)))
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
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
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(angleStartSpinner)
								.addComponent(camberStartSpinner)
								.addComponent(thicknessStartSpinner)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(32)
							.addComponent(startLabel)))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(21, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(startLabel)
								.addComponent(maxLabel))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(camberStartSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(thicknessStartSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(angleStartSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(camberMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(13)
									.addComponent(thicknessMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(13)
									.addComponent(angleMaxSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
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
					.addContainerGap())
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
						
//						SQLiteConnexion database = new SQLiteConnexion();
//						Session s = database.createSession(session, new String(password));
//						s.setParameters((float) angleMinSpinner.getValue(),
//										(float) angleMaxSpinner.getValue(),
//										(float) thicknessMinSpinner.getValue(),
//										(float) thicknessMaxSpinner.getValue(),
//										(float) camberMinSpinner.getValue(),
//										(float) camberMaxSpinner.getValue());
//
//						database.setSessionParameters(s);
												
						// TODO: add session to database, check availability of session ID
						JOptionPane.showMessageDialog(null, "Session \"" + session + "\" has been created.");
						dispose();
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
