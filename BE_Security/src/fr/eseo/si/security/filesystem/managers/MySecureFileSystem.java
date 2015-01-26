package fr.eseo.si.security.filesystem.managers;

import java.io.File;
import java.util.Properties;

import fr.eseo.si.security.filesystem.managers.util.CryptoUtil;
import fr.eseo.si.security.filesystem.managers.util.DeleteUtil;
import fr.eseo.si.security.filesystem.managers.util.SignatureUtil;
import fr.eseo.si.security.filesystem.managers.util.UserUtil;
import fr.eseo.si.security.filesystem.managers.util.ZipUtil;

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
			switch(CryptoUtil.isEncrypt(file)){
			case CryptoUtil.FILE_CRYPT : 
				System.out.println("Déchiffrement fichier");
				CryptoUtil.decrypt(this.pwh, file, file);
				break;
			case CryptoUtil.FILE_CLEAR :
				System.out.println("Chiffrement fichier");
				CryptoUtil.encrypt(this.pwh, file, file);
				break;
			case CryptoUtil.FOLDER_CRYPT : 
				System.out.println("Déchiffrement dossier");
				CryptoUtil.decrypt(pwh,file,file);
				ZipUtil unZip = new ZipUtil();
				unZip.unZipIt(file.getAbsolutePath(),file.getParent());
				break;
			case CryptoUtil.FOLDER_CLEAR :
				System.out.println("Chiffrement dossier");
				ZipUtil appZip = new ZipUtil();
				appZip.generateFileList(file.getParent(),file);
				String ZippedDirectory = new StringBuilder().append(file.getAbsolutePath()).append(".zip").toString();
				appZip.zipIt(file.getParent(), ZippedDirectory);
				File zippedFile = new File(ZippedDirectory);
				DeleteUtil.delete(file);
				CryptoUtil.encrypt(pwh, zippedFile, zippedFile);
				break;
			}
		}
	}

	@Override
	public void delete(File[] files) {
		System.out.println("Suppression fichier / dossiers");
		for (File file : files){
			DeleteUtil.delete(file);
		}
	}

	@Override
	public void sign(File[] files) {
		System.out.println("Signature de fichiers");
		SignatureUtil.keyPairGenerator();
		for(File f : files){
			SignatureUtil.recursiveSign(f);
		}
	}

	@Override
	public void verify(File[] files) {
		System.out.println("Vérification de fichiers");
		SignatureUtil.keyPairGenerator();
		for(File f : files){
			SignatureUtil.recursiveVerify(f);

		}
	}
}
