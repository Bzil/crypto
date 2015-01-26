package fr.eseo.si.security.filesystem.managers.util;


// Décalage de dossier + dossier vide -> erreur
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/**
 * Utilitaire de gestion des archives
 * @author bzil
 * @version 1.0
 */
public class ZipUtil {
	
	/**
	 * Extention pour les fichier archives
	 */
	public static final String EXT_ZIP = ".zip";
	
	/**
	 * Liste des fichiers à archiver
	 */
	private List<String> fileList;

	public ZipUtil(){
		fileList = new ArrayList<String>();
	}
	/**
	 * Crée une archive zip à partir d'un dossier
	 * @param sourceFolder dossier à archiver
	 * @param zipFile nom du fichier de sortie en zip
	 */
	public void zipIt(String sourceFolder, String zipFile){
		byte[] buffer = new byte[1024];
		String source = "";
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try{
			try{
				source = sourceFolder.substring(sourceFolder.lastIndexOf(File.separator) + 1, sourceFolder.length());
			}
			catch (Exception e){
				source = sourceFolder;
			}
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			System.out.println("Archive de sortie : " + zipFile);
			FileInputStream in = null;
			for (String file : this.fileList){
				System.out.println("Fichier ajouté : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try{
					in = new FileInputStream(sourceFolder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0){
						zos.write(buffer, 0, len);
					}
				}
				finally{
					in.close();
				}
			}
			zos.closeEntry();
			System.out.println("Compression réussite");
		}
		catch (IOException ex){
			//ex.printStackTrace();
		}
		finally{
			try{
				zos.close();
			}
			catch (IOException e){
				//e.printStackTrace();
			}
		}
	}
	/**
	 * Génére la liste des fichiers / dossier à archiver
	 * @param sourceFolder dossier dans lequel on se situe
	 * @param node noeud courant
	 */
	public void generateFileList(String sourceFolder, File node){
		// add file only
		if (node.isFile()){
			fileList.add(generateZipEntry(sourceFolder, node.toString()));
		}
		if (node.isDirectory()){
			String[] subNote = node.list();
			for (String filename : subNote){
				generateFileList(sourceFolder, new File(node, filename));
			}
		}
	}
	
	/**
	 * Génére le nom de l'archive
	 * @param sourceFolder dossier que l'on veut archiver
	 * @param file dont on veut le nom
	 * @return nom de l'archive
	 */
	private String generateZipEntry(String sourceFolder, String file){
		return file.substring(sourceFolder.length() + 1, file.length());
	}

	/**
	 * Dézippe une archive zip
	 * @param zipFile fichier à dézipper
	 * @param output le dossier dézipper
	 */
	public void unZipIt(String zipFile, String outputFolder){
		byte[] buffer = new byte[1024];
		try{
			//create output directory is not exists
			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdir();
			}
			//get the zip file content
			ZipInputStream zis =  new ZipInputStream(new FileInputStream(zipFile));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			while(ze!=null){
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : "+ newFile.getAbsoluteFile()); 
				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs(); 
				FileOutputStream fos = new FileOutputStream(newFile);              
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();   
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Décompression Réussite");

		}catch(IOException ex){
			//ex.printStackTrace(); 
		}
	}
} 