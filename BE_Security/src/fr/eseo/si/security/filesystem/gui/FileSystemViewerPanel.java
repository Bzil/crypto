package fr.eseo.si.security.filesystem.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import fr.eseo.si.security.filesystem.managers.SecureFileSystemManager;

public class FileSystemViewerPanel extends JPanel {

	
	
	
	/**************************************************************
	 * JPanel is Serializable, therefore need a serialVersionUID. *
	 **************************************************************/
	private static final long serialVersionUID = 164963009904552420L;
	
	/********************************************************
	 * ImageIcons to use for the different toolbar buttons. *
	 ********************************************************/
	private static final ImageIcon ENCRYPT = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/encrypt.png")); //$NON-NLS-1$
	private static final ImageIcon CREATE_SIGN = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/create_signature.png")); //$NON-NLS-1$
	private static final ImageIcon VERIFY_SIGN = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/verify_signature.png")); //$NON-NLS-1$
	private static final ImageIcon DELETE = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/delete.png")); //$NON-NLS-1$
	private static final ImageIcon REFRESH = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/refresh.png")); //$NON-NLS-1$
	private static final ImageIcon EXIT = new ImageIcon(FileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/si/security/filesystem/gui/res/exit.png")); //$NON-NLS-1$
	
	/* The JFrame that contains this JPanel */
	private JFrame parentFrame;
	/* The JToolBar for the application */
	private JToolBar fsvpToolbar;
	/* JTextField containing fully qualified name of selected file / folder */
	private JTextField fsvpSelected;
	/* JScrollPane that contains the JTree */
	private JScrollPane fsvpScrollPane;
	/* The JTree that is used to represent the File System */
	private JTree fsvpTreeView;
	/* The model for the JTree */
	private FileSystemTreeModel fsm;
	
	/* The Secure File Manager which adds the security functionality to the application */
	private SecureFileSystemManager manager;
	
	/* Action that is carried out when the Encrypt / Decrypt button / menu option is pressed */
	private Action encryptDecrypt;
	/* Action that is carried out when the Sign button / menu option is pressed */
	private Action createSignature;
	/* Action that is carried out when the Verify Signature button / menu option is pressed */
	private Action verifySignature;
	/* Action that is carried out when the Safe Delete button / menu option is pressed */
	private Action safeDelete;
	/* The JPopupMenu that will be associated with the nodes on the JTree */
	private JPopupMenu fsvpPopupMenu;
	/* Action that is carried out when the Exit button is pressed */
	private Action exit;
	/* Action that is carried out when the Refresh button is pressed */
	private Action refresh;

	/**
	 * Constructor that is used to create this FileSystemViewerPanel (via the initActions(),
	 * initComponents() and layoutComponents() methods) and associates the Panel with the given
	 * JFrame and SecureFileSystemManager.
	 * @param parentFrame the JFrame that contains this Panel
	 * @param manager The SecureFileSystemManager to be used for this Panel
	 */
	public FileSystemViewerPanel(JFrame parentFrame, SecureFileSystemManager manager){
		this.manager = manager;
		manager.setFileSystemViewPanel(this);
		this.parentFrame = parentFrame;
		fsvpToolbar = new JToolBar();
		fsvpSelected = new JTextField();
		fsm = new FileSystemTreeModel();
		
		initActions();
		initComponents();
		if(!manager.authorize()){
			System.exit(1);
		}
		layoutComponents();
	}
	
	/**
	 * The initActions method is responsible for initialising the different actions available in
	 * the application. Each action has associated with it:
	 * <ul><li>A textual description (for tooltip / menu)
	 * <li>An icon (for toolbar / menu)
	 * <li>An action performed method that calls the relevant method in the relevant class.
	 * </ul>
	 * Any new functionality must be implemented via a new action that is then added to the
	 * toolbar and / or popup menu as appropriate.  The actions are added to the toolbar and
	 * popup menu in the initComponents method. 
	 * The default actions (which call methods in the  {@link SecureFileSystemManager} class) are:
	 * <ul><li>Encrpyt / Decrypt
	 * <li>Create Signature
	 * <li>Verify Signature
	 * <li>Safe Deletion
	 * </ul>
	 * The other default actions which do not concern the security manager are:
	 * <ul><li>Refresh - to refresh the tree view
	 * <li>Exit - to exit the program
	 * <ul>
	 */
	private void initActions(){
		encryptDecrypt = new AbstractAction(Messages.getString("FileSystemViewerPanel.EncryptDecrypt"),ENCRYPT) { //$NON-NLS-1$
			
			private static final long serialVersionUID = 7303606417383379529L;

			@Override
			public void actionPerformed(ActionEvent e) {
				manager.encrpytDecrypt(getFiles(fsvpTreeView.getSelectionPaths()));
				fsvpTreeView.repaint();
			}
		};
		encryptDecrypt.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.EncryptDecrypt")); //$NON-NLS-1$
		safeDelete = new AbstractAction(Messages.getString("FileSystemViewerPanel.Delete"),DELETE) { //$NON-NLS-1$
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1551243590836898886L;

			@Override
			public void actionPerformed(ActionEvent e) {
				manager.delete(getFiles(fsvpTreeView.getSelectionPaths()));
				fsvpTreeView.repaint();
				
			}
		};
		safeDelete.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.Delete")); //$NON-NLS-1$
		createSignature = new AbstractAction(Messages.getString("FileSystemViewerPanel.Sign"),CREATE_SIGN) { //$NON-NLS-1$
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8199607340186170164L;

			@Override
			public void actionPerformed(ActionEvent e) {
				manager.sign(getFiles(fsvpTreeView.getSelectionPaths()));
				fsvpTreeView.repaint();
				
			}
		};
		createSignature.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.Sign")); //$NON-NLS-1$

		verifySignature = new AbstractAction(Messages.getString("FileSystemViewerPanel.VerifySignature"),VERIFY_SIGN) { //$NON-NLS-1$
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8199607340186170164L;

			@Override
			public void actionPerformed(ActionEvent e) {
				manager.verify(getFiles(fsvpTreeView.getSelectionPaths()));
				fsvpTreeView.repaint();
				
			}
		};
		verifySignature.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.VerifySignature")); //$NON-NLS-1$

		
		
		exit = new AbstractAction(Messages.getString("FileSystemViewerPanel.Exit"),EXIT) { //$NON-NLS-1$
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 975418798340329192L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Frame frame : Frame.getFrames())
				{
					if (frame.isActive())
					{
						WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
						frame.dispatchEvent(windowClosing);
					}
				}
			}
		};
		exit.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.Exit")); //$NON-NLS-1$

		
		refresh = new AbstractAction(Messages.getString("FileSystemViewerPanel.Refresh"),REFRESH) { //$NON-NLS-1$
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -5338766584580545929L;

			@Override
			public void actionPerformed(ActionEvent e) {
				fsvpTreeView.repaint();
				
			}
		};
		refresh.putValue(AbstractAction.SHORT_DESCRIPTION, Messages.getString("FileSystemViewerPanel.Refresh")); //$NON-NLS-1$

		safeDelete.setEnabled(false);
		encryptDecrypt.setEnabled(false);
		createSignature.setEnabled(false);
		verifySignature.setEnabled(false);
	}
	
	
	
	
	
	/**
	 * The initComponents method is used to initalise the different components that are placed upon
	 * the Panel. For this application, this includes setting up the
	 * <ul>
	 * <li>  JTree - customised to give a correct tooltip - using a FileSystemTreeRenderer and a customised TreeSelectionListener.
	 * <li>  PopupMenu - used to offer actions via the right click on the JTree
	 * <li> The toolbar - used to offer actions via the toolbar.
	 * </ul>
	 */
	private void initComponents(){
		
		fsvpTreeView = new JTree(fsm){
			
			private static final long serialVersionUID = -8153609420335711560L;

			public String getToolTipText(MouseEvent event){
				if(event == null)
					return null;
				TreePath path = fsvpTreeView.getPathForLocation(event.getX(), event.getY());
				if(path != null){
					
					return (path.getLastPathComponent().toString());
				}
				return ""; //$NON-NLS-1$
			}
		};
		fsvpTreeView.setCellRenderer(new FileSystemTreeRenderer());
		fsvpTreeView.setEditable(false);
		fsvpTreeView.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				TreePath path = event.getPath();
				if(path!=null){
					fsvpSelected.setText(path.getLastPathComponent().toString());
					
					
				}
				else
					fsvpSelected.setText(""); //$NON-NLS-1$
				
				createSignature.setEnabled(fsvpTreeView.getSelectionCount()>0);
				verifySignature.setEnabled(fsvpTreeView.getSelectionCount()>0);
				safeDelete.setEnabled(fsvpTreeView.getSelectionCount()>0);
				encryptDecrypt.setEnabled(fsvpTreeView.getSelectionCount()>0);
				
			}
		});
		
		
		
		ToolTipManager.sharedInstance().registerComponent(fsvpTreeView);
		
		
		fsvpPopupMenu = new JPopupMenu();
		fsvpPopupMenu.add(encryptDecrypt);
		fsvpPopupMenu.add(createSignature);
		fsvpPopupMenu.add(verifySignature);
		fsvpPopupMenu.add(safeDelete);
		
		fsvpTreeView.add(fsvpPopupMenu);
		fsvpTreeView.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event){
				mouseReleased(event);
			}
			public void mouseReleased(MouseEvent event){
				int x = event.getX();
				int y = event.getY();
				TreePath path = fsvpTreeView.getPathForLocation(x, y);
				if(path == null){
					fsvpTreeView.clearSelection();
					return;
				}
				if(event.isPopupTrigger()){
					if(event.isControlDown())
						fsvpTreeView.addSelectionPath(path);
					else
						fsvpTreeView.setSelectionPath(path);
					fsvpPopupMenu.show(fsvpTreeView, x, y);
					
				}
			}
		});
		
		fsvpToolbar.add(encryptDecrypt);
		fsvpToolbar.addSeparator();
		
		fsvpToolbar.add(createSignature);
		fsvpToolbar.add(verifySignature);
		fsvpToolbar.addSeparator();
		
		fsvpToolbar.add(safeDelete);
		fsvpToolbar.addSeparator();
		fsvpToolbar.addSeparator();
		fsvpToolbar.add(refresh);
		fsvpToolbar.addSeparator();
		fsvpToolbar.add(exit);
		
		fsvpScrollPane = new JScrollPane(fsvpTreeView);
	}
	
	/**
	 * The layoutComponents method is used to set the layout for the components contained in the panel.
	 */
	private void layoutComponents(){
		GroupLayout fsvpLayout = new GroupLayout(this);
		this.setLayout(fsvpLayout);
		
		fsvpLayout.setHorizontalGroup(
			fsvpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(fsvpToolbar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(fsvpSelected)
			.addComponent(fsvpScrollPane, GroupLayout.DEFAULT_SIZE,400,Short.MAX_VALUE)
		);
		
		fsvpLayout.setVerticalGroup(
			fsvpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(fsvpLayout.createSequentialGroup()
				.addComponent(fsvpToolbar,GroupLayout.PREFERRED_SIZE,36,GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(fsvpSelected,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(fsvpScrollPane,GroupLayout.DEFAULT_SIZE,400,Short.MAX_VALUE)
				.addGap(0,0,0)
			)			
		);
	}
	
	/**
	 * Method to return the file(s) / folder(s) represented by the TreePath(s) given as a parameter.
	 * @param paths An array of TreePaths which corresponds to the TreePath to the selected node(s) in the JTree.
	 * @return An array of Files that match on the File System the node displayed in the JTree.
	 */
	private File[] getFiles(TreePath[] paths){
		File [] files = new File[paths.length];
		for(int i = 0; i < paths.length; i++){
			files[i] = (File)paths[i].getLastPathComponent();
		}
		return files;
	}
	
	
	/**
	 * Returns the parent JFrame of this application
	 * @return
	 */
	public JFrame getFrame() {
		// TODO Auto-generated method stub
		return parentFrame;
	}
}
