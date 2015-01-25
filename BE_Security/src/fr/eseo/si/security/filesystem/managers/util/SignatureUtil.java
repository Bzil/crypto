package fr.eseo.si.security.filesystem.managers.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class SignatureUtil {

	private static PublicKey PUBLIC_KEY = null;
	private static PrivateKey PRIVATE_KEY = null;

	private static String EXT_KEY = ".key";
	private static String EXT_SIGN = ".sign";
	

	public static void keyPairGenerator(){
		try {
			addProviderDSTC();    
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "DSTC");
			SecureRandom srandom = SecureRandom.getInstance("SHA256PRNG");
			long userSeed = System.currentTimeMillis();
			srandom.setSeed(userSeed);
			keyPairGenerator.initialize(1024, srandom);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PRIVATE_KEY = keyPair.getPrivate();
			PUBLIC_KEY = keyPair.getPublic();

		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
		}

	}

	public static boolean verify(File file){
		boolean verifies = false;
		try {
			FileReader fileReader = new FileReader(file+EXT_KEY);
			BufferedReader in = new BufferedReader(fileReader);
			String publicKeyEncoded = in.readLine();
			byte[] publicKey = Base64Util.decode(publicKeyEncoded);
			in.close();

			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

			/* input the signature bytes */
			File signatureFile = new File(file.getAbsolutePath()+EXT_SIGN);
			FileInputStream sigfis = new FileInputStream(signatureFile);
			byte[] sigToVerifyEncoded = new byte[sigfis.available()]; 
			sigfis.read(sigToVerifyEncoded);
			sigfis.close();

			byte[] sigToVerify = Base64Util.decode(new String(sigToVerifyEncoded));

			Signature sig = Signature.getInstance("SHA256withRSA", "DSTC");
			sig.initVerify(pubKey);

			/* Update and verify the data */
			FileInputStream datafis = new FileInputStream(file);
			BufferedInputStream bufin = new BufferedInputStream(datafis);

			byte[] buffer = new byte[1024];
			int len;
			while (bufin.available() != 0) {
				len = bufin.read(buffer);
				sig.update(buffer, 0, len);
			};

			bufin.close();
			datafis.close();
			verifies = sig.verify(sigToVerify);
			System.out.println("signature verifies: " + verifies);

		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException | IOException | InvalidKeySpecException e) {
			e.printStackTrace();
		}	
		return verifies;
	}

	public static void sign(File file){
		byte[] realSig = null;
		try {
			/* Save public key for later */
			savePublicKey(file);

			/* Get signature object + signature object initialization */
			Signature dsa = Signature.getInstance("SHA256withRSA", "DSTC"); 
			dsa.initSign(PRIVATE_KEY);

			/* Supply the data to be signed to our signature object */
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bufin = new BufferedInputStream(fis);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bufin.read(buffer)) >= 0) {
				dsa.update(buffer, 0, len);
			};
			bufin.close();
			fis.close();

			/* Generate signature */
			realSig = dsa.sign();
			saveSignature(realSig, file);

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveSignature(byte[] realSig, File file){
		try {
			/* save the signature in a file */
			FileOutputStream sigfos = new FileOutputStream(new StringBuilder().append(file.getAbsolutePath().toString()).append(".sign").toString());
			sigfos.write(Base64Util.encode(realSig).getBytes());
			sigfos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void savePublicKey(File file){
		try {
			/* save the public key in a file */
			String pkey = file.getAbsolutePath()+EXT_KEY;
			if(PUBLIC_KEY.equals(null)){
				keyPairGenerator();
			}
			byte[] key = PUBLIC_KEY.getEncoded();
			FileOutputStream keyfos;
			keyfos = new FileOutputStream(pkey);
			keyfos.write(Base64Util.encode(key).getBytes());
			keyfos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Provider addProviderDSTC() {
		Provider dstc_provider = new com.dstc.security.provider.DSTC();
		int result = Security.addProvider(dstc_provider);
		if (result == -1) {
			return null;
		} else {
			return dstc_provider;
		}
	}

	public static void recursiveSign(File file){
		if(!file.exists()) return;
		if(file.isDirectory()){
			for(File childFile : file.listFiles())
				recursiveSign(childFile);
		}
		if(file.isFile()) sign(file);
	}

	public static void recursiveVerify(File file){
		if(!file.exists()) return;
		if(file.isDirectory()){
			for(File childFile : file.listFiles())
				recursiveVerify(childFile);
		}
		if(file.isFile()){
			if(file.getName().endsWith(EXT_KEY) || file.getName().endsWith(EXT_SIGN)){}
			else verify(file);
		}
	}
}
