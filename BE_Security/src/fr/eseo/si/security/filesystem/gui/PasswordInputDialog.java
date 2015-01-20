package fr.eseo.si.security.filesystem.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import fr.eseo.si.security.filesystem.managers.SecureFileSystemManager;

/**
 * When the application is started, the user must give a username and password.
 * If this is not recognised by the system, then the application should exit gracefully.
 * @author rwoodward
 *
 */
public class PasswordInputDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6336161364052842693L;
	/* The SecureFileSystemManager that will be used to verify username and password */
	private SecureFileSystemManager manager;
	/* The FileSystemViewerPanel that is the parent of this dialog */
	private FileSystemViewerPanel parent;
	/* Is the password valid or not */
	boolean passwordValid = false;
	
	/**
	 * Creates the PasswordInputDialog, using the given SecureFileSystemManager and FileSystemViewerPanel.
	 * @param manager
	 * @param panel
	 */
	public PasswordInputDialog(SecureFileSystemManager manager, FileSystemViewerPanel panel) {
		super(panel.getFrame(),true);
		setParent(parent);
		setManager(manager);
		setTitle(Messages.getString("PasswordInputDialog.logonTitle")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		add(new PasswordInputPanel(this));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Uses the SecureFileSystemManager to verify the username / password pair
	 * @param username the given username
	 * @param password the given password
	 * @return true if identification is correct, false otherwise
	 */
	public boolean isPasswordValid(String username, char[] password) {
		if(getManager().isPasswordCorrect(username,password)){
			setPasswordValid(true);
		}
		else{
			setPasswordValid(false);
		}
		return isPasswordValid();
	}
	
	/**
	 * Recuperate the SecureFileSystemManager
	 * @return the secureFileSystemManager
	 */
	private SecureFileSystemManager getManager(){
		return manager;
	}
	
	/**
	 * Sets the SecureFileSystemManager
	 * @param manager the secureFileSystemManger to be used
	 */
	private void setManager(SecureFileSystemManager manager){
		this.manager = manager;
	}
	
	/**
	 * Recuperates the FileSystemViewerPanel that is the parent of this Dialog.
	 * @return the associated FileSystemViewerPanel
	 */
	private FileSystemViewerPanel getParentPanel(){
		return parent;
	}
	
	/**
	 * Sets the FileSystemViewerPanel to be used with this Dialog
	 * @param parent the FileSystemViewerPanel to be associated with this Dialog.
	 */
	private void  setParent(FileSystemViewerPanel parent){
		this.parent = parent;
	}
	
	/**
	 * Getter for the passwordValid field
	 * @return the value stored in passwordValid
	 */
	public boolean isPasswordValid(){
		return passwordValid;
	}
	
	/**
	 * Setter for the passwordValid field
	 * @param valid new value for the passwordValid field
	 */
	private void setPasswordValid(boolean valid){
		passwordValid = valid;
	}
	

}
