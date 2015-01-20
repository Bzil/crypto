package fr.eseo.si.security.filesystem.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 * The FileSystemTreeRenderer is a renderer to customise how the information is displayed
 * in the JTree, primarily showing the difference between:
 * <ul>
 * <li> open folders
 * <li> closed folders
 * <li> files
 * </ul>
 * It also customises the highlighting of the selected file(s) and folder(s)
 * @author rwoodward
 *
 */
public class FileSystemTreeRenderer extends JLabel implements TreeCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -829681233972439591L;
	private Color textSelectionColour;
	private Color textNonSelectionColour;
	private Color backSelectionColour;
	private Color backNonSelectionColour;
	
	private static final ImageIcon FOLDER_SHUT = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/folder_shut.png")); //$NON-NLS-1$
	private static final ImageIcon FOLDER_OPEN = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/folder_open.png")); //$NON-NLS-1$
	private static final ImageIcon FILE = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/file.png")); //$NON-NLS-1$
	
	private LinearGradientPaint selectionFade;
	
	private boolean isSelected;
	
	public FileSystemTreeRenderer(){
		super();
		textSelectionColour = UIManager.getColor("Tree.selectionForeground"); //$NON-NLS-1$
		textNonSelectionColour = UIManager.getColor("Tree.textForeground"); //$NON-NLS-1$
		backSelectionColour = UIManager.getColor("Tree.selectionBackground"); //$NON-NLS-1$
		backNonSelectionColour = UIManager.getColor("Tree.textBackground"); //$NON-NLS-1$
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		File file = (File)value;
		String componentLabel = file.getName();
		setText(componentLabel);
		setFont(tree.getFont());
		setForeground(selected ? textSelectionColour : 
			textNonSelectionColour);
		setBackground(selected ? backSelectionColour : backNonSelectionColour);
		if(file.isDirectory()){
			if(expanded)
				setIcon(FOLDER_OPEN);
			else
				setIcon(FOLDER_SHUT);
		}
		else
			setIcon(FILE);
		isSelected = selected;
		return this;
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g.create();
		Color backColour = getBackground();
		Icon icon = getIcon();
		
		int offset = 0;
		if(icon != null && getText() != null){
			if(isSelected){
				offset = (icon.getIconWidth()+getIconTextGap());
				selectionFade = new LinearGradientPaint(new Point2D.Double(offset-2,getHeight()/2),new Point2D.Double(getWidth()+offset,getHeight()/2), new float[]{0.0f,1f}, new Color[]{backColour,new Color(255,255,255,0)});
				g2.setPaint(selectionFade);
				g2.fillRoundRect(offset-2, 1, getWidth()-offset+4, getHeight()-2,6,6);
				selectionFade = new LinearGradientPaint(new Point2D.Double(offset-2,getHeight()/2),new Point2D.Double(getWidth()+offset,getHeight()/2), new float[]{0.0f,1f}, new Color[]{backColour.darker().darker(),new Color(255,255,255,0)});
				g2.setPaint(selectionFade);
				g2.drawRoundRect(offset-2, 1, getWidth()-offset+4, getHeight()-2,6,6);
			}
		}
		g2.dispose();
		super.paintComponent(g);
		
	}
}
