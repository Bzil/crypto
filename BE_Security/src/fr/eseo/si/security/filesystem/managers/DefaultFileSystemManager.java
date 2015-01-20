package fr.eseo.si.security.filesystem.managers;

import java.io.File;

import javax.swing.JOptionPane;

import fr.eseo.si.security.filesystem.gui.PasswordInputDialog;

public class DefaultFileSystemManager extends SecureFileSystemManager {

	@Override
	public boolean authorize() {
		boolean result = false;
		PasswordInputDialog passwordDialog = new PasswordInputDialog(this,getFileSystemViewPanel());
		result = passwordDialog.isPasswordValid();
		return result;
	}

	@Override
	public void delete(File[] files) {
		JOptionPane.showMessageDialog(getFileSystemViewPanel(),Messages.getString("DefaultFileSystemManager.SafeDeletionMessage"), //$NON-NLS-1$
				Messages.getString("DefaultFileSystemManager.SafeDeletionTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

	}

	@Override
	public void encrpytDecrypt(File[] files) {
		JOptionPane.showMessageDialog(getFileSystemViewPanel(),Messages.getString("DefaultFileSystemManager.EncryptDecryptMessage"), //$NON-NLS-1$
				Messages.getString("DefaultFileSystemManager.EncryptDecryptTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

	}

	@Override
	public boolean isPasswordCorrect(String user, char[] password) {
		JOptionPane.showMessageDialog(getFileSystemViewPanel(),Messages.getString("DefaultFileSystemManager.LoginMessage1")+ //$NON-NLS-1$
				Messages.getString("DefaultFileSystemManager.LoginMessage2") + user + Messages.getString("DefaultFileSystemManager.LoginMessage3") + new String(password), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("DefaultFileSystemManager.LoginTitle"), JOptionPane.INFORMATION_MESSAGE //$NON-NLS-1$
		);
		return true;
		
	}

	@Override
	public void sign(File[] files) {
		JOptionPane.showMessageDialog(getFileSystemViewPanel(),Messages.getString("DefaultFileSystemManager.SignMessage"), //$NON-NLS-1$
				Messages.getString("DefaultFileSystemManager.SignTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

	}

	@Override
	public void verify(File[] files) {
		JOptionPane.showMessageDialog(getFileSystemViewPanel(),Messages.getString("DefaultFileSystemManager.VerifyMessage"), //$NON-NLS-1$
				Messages.getString("DefaultFileSystemManager.VerifyTitle"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

	}
	

}
