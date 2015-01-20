package fr.eseo.si.security.filesystem.managers.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final String TAG ="1ézdsS5dfggvsssdxgvssfg0xxsx54354893";

	public static void encrypt(String key, File inputFile, File outputFile) {
		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}

	public static void decrypt(String key, File inputFile, File outputFile) {
		doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
	}

	private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) {
		try {
			Key secretKey = new SecretKeySpec(Arrays.copyOf(Base64Util.decode(key), 16) , ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);
			if(cipherMode == Cipher.DECRYPT_MODE){
				removeFirstLine(inputFile);
				removeFirstLine(inputFile);
			}
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			if(cipherMode == Cipher.ENCRYPT_MODE){
				outputStream.write(new StringBuilder().append(TAG).toString().getBytes());
				if(inputFile.isFile()){
					outputStream.write("\n-- File --\n".getBytes());
				}else {
					outputStream.write("\n-- Folder --\n".getBytes());
				}
			}
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
			//throw new Exception("Error encrypting/decrypting file", ex);
		}
	}

	@SuppressWarnings("resource")
	public static boolean isEncrypt(File file){
		boolean isEncrypt = false;
		if(file.isDirectory()) return isEncrypt;
		try {
			FileReader namereader = new FileReader(file);
			BufferedReader in = new BufferedReader(namereader);
			String line = in.readLine();
			if(line.equals(TAG)) { 
				isEncrypt = true;
			}
		}
		catch (Exception e){}

		return isEncrypt;
	}

	public static void removeFirstLine(File file) throws IOException {  
		RandomAccessFile raf = new RandomAccessFile(file, "rw");          
		//Initial write position                                             
		long writePosition = raf.getFilePointer();                            
		raf.readLine();                                                       
		// Shift the next lines upwards.                                      
		long readPosition = raf.getFilePointer();                             

		byte[] buff = new byte[1024];                                         
		int n;                                                                
		while (-1 != (n = raf.read(buff))) {                                  
			raf.seek(writePosition);                                          
			raf.write(buff, 0, n);                                            
			readPosition += n;                                                
			writePosition += n;                                               
			raf.seek(readPosition);                                           
		}                                                                     
		raf.setLength(writePosition);                                         
		raf.close();                                                          
	} 
}