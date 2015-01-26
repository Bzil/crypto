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
/**
 * Utiliataire de chiffrement
 * @author bzil
 * @version 1.0
 */
public class CryptoUtil {
	/**
	 * Algorithme et transformation de cryptage
	 */
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	
	/**
	 * Tag de fichier
	 */
	private static final String TAG_FILE ="1ézdsS5dfggvsssdxgvssfg0xxsx54354893";
	/**
	 * Tag de dossier
	 */
	private static final String TAG_FOLDER ="1SDKJILI7:kjhgyBHFGHMçJBHgfVGF";
		
	public static final int FILE_CRYPT = 1;
	public static final int FILE_CLEAR = -1;
	public static final int FOLDER_CRYPT = 2;
	public static final int FOLDER_CLEAR = -2;

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
			}
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			if(cipherMode == Cipher.ENCRYPT_MODE){
				if(inputFile.getAbsolutePath().endsWith(ZipUtil.EXT_ZIP)){
					outputStream.write(new StringBuilder().append(TAG_FOLDER).append("\n").toString().getBytes());
				}else {
					outputStream.write(new StringBuilder().append(TAG_FILE).append("\n").toString().getBytes());
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
	/**
	 * Method to know if a file or a folder is encryped
	 * @param file
	 * @return 1 pour fichier chiffré 2 pour dossier chiffré -1 pour fichier non chiffré -2 pour dossier non chiffré 
	 */
	@SuppressWarnings("resource")
	public static int isEncrypt(File file){
		int isEncrypt = FILE_CLEAR;
		if(file.isDirectory()) return FOLDER_CLEAR;
		try {
			FileReader namereader = new FileReader(file);
			BufferedReader in = new BufferedReader(namereader);
			String line = in.readLine();
			if(line.equals(TAG_FILE)) { 
				isEncrypt = FILE_CRYPT;
			}
			else if (line.equals(TAG_FOLDER)) { 
				isEncrypt = FOLDER_CRYPT;
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