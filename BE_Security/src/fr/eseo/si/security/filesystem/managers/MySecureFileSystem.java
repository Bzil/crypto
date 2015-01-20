package fr.eseo.si.security.filesystem.managers;

import java.io.File;
import java.util.Properties;

import fr.eseo.si.security.filesystem.managers.util.CryptoUtil;
import fr.eseo.si.security.filesystem.managers.util.UserUtil;

public class MySecureFileSystem extends DefaultFileSystemManager {

	private String pwh;

	public MySecureFileSystem(){
		this.pwh = new String();
	}

	@Override
	public boolean isPasswordCorrect(String user, char[] password) {
		boolean isCorrect = false;
		Properties prop = UserUtil.getProperties();
		if(prop.stringPropertyNames().contains("user." + user)){
			if(prop.getProperty("user." + user).equals(UserUtil.hashPassword(password, "MD5"))){
				this.pwh = UserUtil.hashPassword(password, "SHA-256");
				password = UserUtil.getBlanckArray(password);
				isCorrect = true;
			}
		}
		return isCorrect;
	}

	@Override
	public void  encrpytDecrypt(File[] files) {
		for (File file : files) {
			if(CryptoUtil.isEncrypt(file)){
				//System.out.println("Déchiffrement");
				CryptoUtil.decrypt(this.pwh, file, file);
			}
			else {
				//System.out.println("Chiffrement");
				CryptoUtil.encrypt(this.pwh, file, file);
			}
		}
	}                      

}
