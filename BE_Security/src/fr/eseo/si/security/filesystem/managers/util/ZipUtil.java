package fr.eseo.si.security.filesystem.managers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil	
{
	private List<String> fileList;

	// TODO Voir librairie Zip4J
	public ZipUtil()
	{
		fileList = new ArrayList<String>();
	}

	/*public static void main(String[] args)
	{
		ZipUtil appZip = new ZipUtil();
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}*/

	public void zipIt(String sourceFolder, String zipFile)
	{
		byte[] buffer = new byte[1024];
		String source = "";
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try
		{
			try
			{
				source = sourceFolder.substring(sourceFolder.lastIndexOf(File.separator) + 1, sourceFolder.length());
			}
			catch (Exception e)
			{
				source = sourceFolder;
			}
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (String file : this.fileList)
			{
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try
				{
					in = new FileInputStream(sourceFolder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0)
					{
						zos.write(buffer, 0, len);
					}
				}
				finally
				{
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				zos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void generateFileList(String sourceFolder, File node)
	{
		// add file only
		if (node.isFile())
		{
			fileList.add(generateZipEntry(sourceFolder, node.toString()));

		}

		if (node.isDirectory())
		{
			String[] subNote = node.list();
			for (String filename : subNote)
			{
				generateFileList(sourceFolder, new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String sourceFolder, String file)
	{
		return file.substring(sourceFolder.length() + 1, file.length());
	}
} 