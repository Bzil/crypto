package fr.eseo.si.security.filesystem.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

/**
 * The Panel that requests the users username and password fields
 * @author rwoodward
 *
 */
public class PasswordInputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258652801055487463L;
	private JPasswordField pwdPassword;
	private JLabel icon;
	private JLabel lblUsername;
	private JTextField txtUsername;
	private JPanel innerPanel;
	private JButton btnOk;
	private JButton btnCancel;
	private JLabel lblPassword;
	private PasswordInputDialog dialog;

	public PasswordInputPanel(PasswordInputDialog dialog) {
		this.dialog = dialog;
		
		initComponents();
		layoutComponents();
	}

	private void initComponents(){
		icon = new JLabel();
		lblUsername = new JLabel();
		lblPassword = new JLabel();
		txtUsername = new JTextField();
		pwdPassword = new JPasswordField();
		innerPanel = new JPanel();
		btnOk = new JButton();
		btnCancel = new JButton();
		icon.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/encrypt_large.png"))); //$NON-NLS-1$
		btnCancel.setText(Messages.getString("PasswordInputPanel.btnCancel")); //$NON-NLS-1$
		btnOk.setText(Messages.getString("PasswordInputPanel.btnOk")); //$NON-NLS-1$
		lblUsername.setText(Messages.getString("PasswordInputPanel.lblUsername")); //$NON-NLS-1$
		lblPassword.setText(Messages.getString("PasswordInputPanel.lblPassword")); //$NON-NLS-1$
		txtUsername.setText(""); //$NON-NLS-1$
		pwdPassword.setText(""); //$NON-NLS-1$
		
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				processUserPassword();
				
			}
			
		});
		
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				zeroOutPassword();
				System.exit(0);
				
			}
			
		});
	}

	private void layoutComponents(){
		GroupLayout innerPanelLayout = new GroupLayout(innerPanel);
        innerPanel.setLayout(innerPanelLayout);
        innerPanelLayout.setHorizontalGroup(
            innerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(innerPanelLayout.createSequentialGroup()
                .addGroup(innerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(innerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(innerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, innerPanelLayout.createSequentialGroup()
                                .addGap(0, 78, Short.MAX_VALUE)
                                .addComponent(btnOk)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancel))
                            .addGroup(innerPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtUsername))
                            .addGroup(innerPanelLayout.createSequentialGroup()
                                .addGroup(innerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUsername)
                                    .addComponent(lblPassword))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(innerPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(pwdPassword)))
                .addContainerGap())
        );
        innerPanelLayout.setVerticalGroup(
            innerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, innerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUsername)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pwdPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(innerPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOk))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(innerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon)
                .addContainerGap())
            .addComponent(innerPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
	}
	
	private void zeroOutPassword(){
		Arrays.fill(pwdPassword.getPassword(), '0');
		pwdPassword.setText(""); //$NON-NLS-1$
		pwdPassword.selectAll();
		pwdPassword.requestFocusInWindow();
	}
	
	private void processUserPassword(){
		if(txtUsername.getText()==null || txtUsername.getText().equals("")) //$NON-NLS-1$
			JOptionPane.showMessageDialog(this,Messages.getString("PasswordInputPanel.invalidEntry"), //$NON-NLS-1$
					Messages.getString("PasswordInputPanel.problem"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		else if(dialog.isPasswordValid(txtUsername.getText(), pwdPassword.getPassword())){
			zeroOutPassword();
			dialog.setVisible(false);
		}
		else{
			zeroOutPassword();
			JOptionPane.showMessageDialog(this,Messages.getString("PasswordInputPanel.invalidEntry"), //$NON-NLS-1$
					Messages.getString("PasswordInputPanel.problem"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		}
	}
}
