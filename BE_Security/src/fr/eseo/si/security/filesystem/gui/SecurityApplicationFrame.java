package fr.eseo.si.security.filesystem.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import fr.eseo.si.security.filesystem.managers.SecureFileSystemManager;

public class SecurityApplicationFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2119300020662469134L;

	public SecurityApplicationFrame(SecureFileSystemManager manager) {

		super(Messages.getString("SecurityApplicationFrame.title")); //$NON-NLS-1$
		
		add(new FileSystemViewerPanel(this, manager));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
