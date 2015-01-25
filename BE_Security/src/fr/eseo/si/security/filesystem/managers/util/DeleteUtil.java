package fr.eseo.si.security.filesystem.managers.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

public class DeleteUtil {
	
	public static void delete(File file){
			if(!file.exists()) return;
			if(file.isDirectory()){
				for (File node : file.listFiles()){
					secureDelete(node);
				}
			}
			secureDelete(file);
	}

	private static void secureDelete(File file) {
		if (file.exists()) {
			long length = file.length();
			SecureRandom random = new SecureRandom();
			RandomAccessFile raf;
			try {
				raf = new RandomAccessFile(file, "rws");
				raf.seek(0);
				raf.getFilePointer();
				byte[] data = new byte[64];
				int pos = 0;
				while (pos < length) {
					random.nextBytes(data);
					raf.write(data);
					pos += data.length;
				}
				raf.close();
			} catch (Exception e) {}

			file.delete();
		}
	}
}
