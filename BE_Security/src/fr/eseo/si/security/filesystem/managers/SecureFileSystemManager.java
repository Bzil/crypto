package fr.eseo.si.security.filesystem.managers;

import java.io.File;

import fr.eseo.si.security.filesystem.gui.FileSystemViewerPanel;

public abstract class SecureFileSystemManager {

	private FileSystemViewerPanel fileSystemViewPanel;
	
	public FileSystemViewerPanel getFileSystemViewPanel(){
		return fileSystemViewPanel;
	}
	
	public void setFileSystemViewPanel(FileSystemViewerPanel panel){
		fileSystemViewPanel = panel;
	}
	
	public abstract boolean authorize();
	
	public abstract void delete(File [] files);
	
	public abstract void encrpytDecrypt(File [] files);
	
	public abstract boolean isPasswordCorrect(String user, char [] password);
	
	public abstract void sign(File [] files);

	public abstract void verify(File[] files);
	
}
