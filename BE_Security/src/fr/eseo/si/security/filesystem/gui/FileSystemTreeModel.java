package fr.eseo.si.security.filesystem.gui;

import java.io.File;
import java.io.FileFilter;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * This is the Model which is used to represent in a JTree the File System starting from the current users home folder.
 * @author rwoodward
 *
 */
public class FileSystemTreeModel implements TreeModel {

	private String root;
	private HiddenFileFilter fileFilter;
	
	
	/**
	 * Default Constructor that creates a Tree Model of all files starting from the users home folder.
	 */
	public FileSystemTreeModel(){
		this(System.getProperty("user.home")); //$NON-NLS-1$
	}
	/**
	 * Constructor that creates a Tree Model of all files starting from the folder given as a parameter.
	 * The application does not display Hidden files.
	 * @param rootDirectory the root folder for the JTree
	 */
	public FileSystemTreeModel(String rootDirectory){
		root = rootDirectory;
		fileFilter = new HiddenFileFilter();
	}
	

	@Override
	public Object getRoot() {
		return new File(root);
	}

	
	@Override
	public Object getChild(Object parent, int index) {
		File directory = (File)parent;
		File [] children = directory.listFiles(fileFilter);
		return new File(directory, children[index].getName());
	}

	@Override
	public int getChildCount(Object parent) {
		File fileSystemEntity = (File)parent;
		if(fileSystemEntity.isDirectory())
			return fileSystemEntity.listFiles(fileFilter).length;
		else
			return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((File)node).isFile();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		File directory = (File) parent;
		File fileSystemEntity = (File)child;
		File [] children = directory.listFiles(fileFilter);
		int result = -1;
		for( int i = 0; i < children.length; ++i){
			if(fileSystemEntity.equals(children[i])){
				result = i;
				break;
			}
		}
		return result;
			
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This filter is used to hide all the hidden files and folders from being displayed in the JTree.
	 * @author rwoodward
	 *
	 */
	private class HiddenFileFilter implements FileFilter{
		public boolean accept(File pathname){
			return (! pathname.isHidden());
		}
	}

}
