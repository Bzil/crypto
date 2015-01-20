package fr.eseo.si.security.filesystem.managers.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class UserUtil {

	public static String PATH = System.getProperty("user.dir");

	public static String FILE_NAME = "users.properties";

	//private static byte[] SALT = {'a', 5, 'f', 54 };

	private UserUtil(){}
	
	public static Properties getProperties(){
		InputStream input = null;
		OutputStream output = null;
		Properties prop = new Properties();
		try {
			input = new FileInputStream(FILE_NAME);
			prop.load(input);
		} catch (Exception e ){
			try {
				output = new FileOutputStream(FILE_NAME);
				prop.store(output, "Save user");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static String getHashKey(String username){
		Properties prop = new Properties();
		InputStream input = null;
		String hashKey = null;
		try {
			input = new FileInputStream(FILE_NAME);
			prop.load(input);
			hashKey = prop.getProperty("user." + username);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hashKey;
	}

	public static String hashPassword(char[] password, String hash){
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance(hash);
			md.update(getByteTabFromCharTab(password));
			byte[] bytes = md.digest(); //SALT	
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static char[] getBlanckArray(char[] tab){
		int i =0, length = tab.length;
		char[] t = new char [length];
		while(i<length){
			t[i] = '\u0000';
			++i;
		}
		return t;
	}
	
	private static byte[] getByteTabFromCharTab(char[] tab) {
		byte[] result = new byte[tab.length];
		for(int i=0; i<tab.length; i++) {
			result[i] = (byte) tab[i];
		}
		return result;
	}

}