package fr.eseo.si.security.filesystem;

import javax.swing.SwingUtilities;

import fr.eseo.si.security.filesystem.gui.SecurityApplicationFrame;
import fr.eseo.si.security.filesystem.managers.MySecureFileSystem;
import fr.eseo.si.security.filesystem.managers.SecureFileSystemManager;

public class SecurityApplication  {

	public static void main(String [] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				SecureFileSystemManager manager = new MySecureFileSystem();
				new SecurityApplicationFrame(manager);
			}
		});
	}
}
