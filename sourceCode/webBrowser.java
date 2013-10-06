import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.net.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.tree.*;


 /*
  *This is my main class in which the main point of contact with the whole program is the
  * constructor. All my JButtons are created within this class and many other features are also created 
  * such as: tabs, favourites and menu bars.
  */
public class webBrowser extends JFrame implements TreeSelectionListener,MouseListener{
   
   JTabbedPane tabPane = new JTabbedPane(); 
   boolean noTabsLeft = false;
   int tabIndex = 0;
   JMenu menuFile, menuHelp,menuHistory, menuEdit, menuFavourites;
   static JMenuItem menuitemExit,menuitemOptions,menuitemTab,menuitemWindow, menuitemAbout,menuitemCloseTab, 
   menuItemBack, menuItemForward, menuItemAddFavourite, menuItemShowFavourites, menuItemShowHistory, itemNewTab,itemCloseTab;
   browserToolBar toolBar;
   private ArrayList<String> favs;
   private ArrayList<String> History;	
   private final String favText = "favourites.txt";	
   JTree root;
   JPopupMenu Tab;
   	
   	/*
   	 *webBrowser constructor. This is the main powerhouse of the program. I.e. the constructor
   	 * does the most work!
   	 */		
 	public webBrowser(){
   		JFrame frame = new JFrame();
   		JPanel jpanel = new JPanel(new BorderLayout());
   	 		createTab();
   			getContentPane().add(tabPane);	 
   				popTab();
   				windowBar();
   					this.setTitle("G52GUI - Browser");
   						this.setSize(1024,768);
   							this.setVisible(true);
   					
   		}//constructor
   
   /*
    *This method creates all the JMenu items, JMenu bar and JMenus and contains
    * code that will link to this and other classes to provide the buttons with actions 
    * in the webbrowser adding functionality. 
    */
   	public void windowBar(){
   
   		//Creating the file button for the Menu bar	
		menuFile = new JMenu("File");
		menuitemExit = new JMenuItem("Exit");
		//These are hotkeys like ctrl + X for exit as declared below 
		menuitemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
								ActionEvent.CTRL_MASK));
		//The action the exit button does i.e closes program! 					
		menuitemExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				System.exit(0);
			}
		});
				menuitemWindow = new JMenuItem("New Window");
				menuitemWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK)); 					
				menuitemWindow.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a){
					createWindow();
				}
			});
	
					menuitemTab = new JMenuItem("New Tab");
					menuitemTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,ActionEvent.CTRL_MASK)); 					
					menuitemTab.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						updateTab();
						createTab();
				
					}
				});
						menuitemCloseTab = new JMenuItem("Close Tab");
						menuitemCloseTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));					
						menuitemCloseTab.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent a){
								closeTab();
				
							}//method
			
						});
								//adding new window to File
								menuFile.add(menuitemWindow);
								//adding new Tab to File
								menuFile.add(menuitemTab);
								//adding close tab to file
								menuFile.add(menuitemCloseTab);
								//adding Exit to File
								menuFile.add(menuitemExit);
		
								//Creating the History Button for the Menu bar
								menuHistory = new JMenu("History");
								menuItemShowHistory = new JMenuItem("Show History");
								menuItemShowHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));	
								menuItemShowHistory.addActionListener(new ActionListener(){
									public void actionPerformed(ActionEvent a){	
											createHistory();
									}
								});
											menuHistory.add(menuItemShowHistory);
											//Creating the Edit Button for the Menu bar
											menuEdit = new JMenu("Edit");
											menuitemOptions = new JMenuItem("Options");
											menuitemOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 
																	ActionEvent.CTRL_MASK));	
											menuitemOptions.addActionListener(new ActionListener(){
												public void actionPerformed(ActionEvent a){	
													createOptions();
												}
											});
													//adding menuitemOptions to the edit menu
													menuEdit.add(menuitemOptions);
													//Creating the Help Button for the Menu bar
													
													menuHelp = new JMenu("Help");
													//creating the About menu option	
													menuitemAbout = new JMenuItem("About");
													menuitemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
														
													//When about is clicked the text below appears 
													menuitemAbout.addActionListener(new ActionListener(){
													public void actionPerformed(ActionEvent b){
														JOptionPane.showMessageDialog(null, "HTML web browser version 1.0 by Jonathan Nicholas");
													}//method
												});
														menuHelp.add(menuitemAbout);
														//Creating the Favourites Button for the Menu bar
														menuFavourites= new JMenu("Favourites");
														//creating the Add Favourites menu option	
														menuItemAddFavourite = new JMenuItem("Bookmark Page");
														menuItemAddFavourite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.CTRL_MASK));
														//When about is clicked the text below appears 
														menuItemAddFavourite.addActionListener(new ActionListener(){
															public void actionPerformed(ActionEvent f){
											
															}
														}); 
																//creating the show all Favourites menu option	
																menuItemShowFavourites = new JMenuItem("Show all Favourites");
																menuItemShowFavourites.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
																menuItemShowFavourites.addActionListener(new ActionListener(){
																	public void actionPerformed(ActionEvent g){
																		createTree();
																	}//method
									
																});
																	menuFavourites.add(menuItemShowFavourites);
																	menuFavourites.add(menuItemAddFavourite);
																	//Creates a menuBar object and adds File and Help to it	
																	JMenuBar menuBar = new JMenuBar();
																		menuBar.add(menuFile);
																		menuBar.add(menuEdit);
																		menuBar.add(menuFavourites);
																		menuBar.add(menuHistory);
																		menuBar.add(menuHelp);
																		setJMenuBar(menuBar);		
																		
										
										}//method
   	/*
   	 *This method controls the JPopup menu for the open and close tab options.
   	 *If you right click the JTabbedPane, this popup menu comes up allowing the end user
   	 *to add or close tabs at his/her discretion.
   	 */
   	public void popTab(){
	   	Tab = new JPopupMenu();
		itemNewTab = new JMenuItem("New Tab");
		itemCloseTab = new JMenuItem("Close Tab");					  	
		itemNewTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)); 
		itemCloseTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		itemCloseTab.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				closeTab();
				
			}//method
		});
				itemNewTab.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						updateTab();
						createTab();
				
					}//method
				});
						Tab.add(itemNewTab); Tab.addSeparator(); Tab.add(itemCloseTab);
						Tab.addMouseListener(this);
						tabPane.addMouseListener(this);	
   						}//method
   	/*
   	 *handles all data related to trees that run in the history and favourites.
   	 *
   	 */
   	public void valueChanged(TreeSelectionEvent e){
 
	}//method
	
	/*
	 *This method creates the option menu were with time you could change the home page 
	 *to a required or desired home page.
	 */
	public void createOptions(){
		 String input = JOptionPane.showInputDialog("Make a new Home Page:");
	}//method
		
			
	/*
	 *This method creates all my history in a string array and this is made into a frame
	 * with the use of a JTree to give each piece of history a well laid out platform 
	 * and each piece of history is a child of the root of the tree. 
	 */
	public void createHistory(){
		String[] history = getAllHistory();
			JFrame showHistory = new JFrame();
				DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
					for(int i = 0;i<history.length;i++){
						rootNode.add(new DefaultMutableTreeNode(history[i]));
					}//for
						root = new JTree(rootNode);
							root.addTreeSelectionListener(this);
							showHistory.add(new JScrollPane(root));
							showHistory.setSize(300,200);
							showHistory.setTitle("History");
							showHistory.pack();
							showHistory.setVisible(true);
					}//method
	
	/*
	 *This method creates all my Favourites in a string array and this is made into a frame
	 * with the use of a JTree to give each piece of favourites a well laid out platform 
	 * and each piece of my favourties is a child of the root of the tree. 
	 */ 
	public void createTree(){
		String[] favs = getAllFavs();
			JFrame favShow = new JFrame();
				DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
					for(int i = 0;i<favs.length;i++){
						rootNode.add(new DefaultMutableTreeNode(favs[i]));
					}//for
						root = new JTree(rootNode);
							root.addTreeSelectionListener(this);
							favShow.add(new JScrollPane(root));
							favShow.setSize(300,200);
							favShow.setTitle("Favourites");
							favShow.pack();
							favShow.setVisible(true);
		
					}//method
	
	/*
	 *Creates a new window when the create new window button is pressed
	 */
   	public void createWindow(){
   		webBrowser newWindow = new webBrowser();
   	   	}//method
    /*
     *This method updates the tab and its variables when tabs are closed
     * and opened and when not to open and when to disable the specified buttons
     *
     */
    public void updateTab(){
		if(tabPane.getTabCount() >= 0){
    		noTabsLeft = false;
    		menuitemCloseTab.setEnabled(true);
    		itemCloseTab.setEnabled(true);
		}//if
    		}//method
   	/*
   	 *This method is one of the most important methods in the program as it creates the JTabbedPane
   	 * onto the editorPane when the program runs. This method is given a recursive call in the constructor at
   	 * the top of this class
   	 */
   	public void createTab(){
   		JPanel jpanel = new JPanel(new BorderLayout());
   	 		browserToolBar toolbar = new browserToolBar();
    		jpanel.add(toolbar, BorderLayout.NORTH);
    		jpanel.add(new JScrollPane(toolbar.editorPane), BorderLayout.CENTER);
    		tabPane.addTab("Browser "+tabPane.getTabCount(), jpanel);
    		
   	}//method
    
    /*
     *This handles all the possibilities with closing tabs in teh program
     */
    public void closeTab(){
		tabPane.getTabCount();
		tabIndex = tabPane.getSelectedIndex();
   		tabPane.remove(tabIndex);
   		if(tabPane.getTabCount() <= 0){
   			noTabsLeft = true;
   		    menuitemCloseTab.setEnabled(false);   			
   			itemCloseTab.setEnabled(false);
   			}//if
   		
   		}//method
   	/*
   	 *This is the main method in which there is only an instance of the class
   	 * and all the work is then done by the constructor.
   	 */	
    public static void main(String[] args) { 
		webBrowser frame = new webBrowser();
		//frame.show();
    }//main method
    
    
	/*
	 *This is my method which handles the getting of all my History that has been 
	 * input into the History.txt text file. This is were all my history or remembered pages 
	 * are stored. I am using buffered reader to get my URL web pages.
	 */
    public String[] getAllHistory(){
    	ArrayList<String> newHistory = new ArrayList<String>();
   		try {
			BufferedReader list = new BufferedReader(new FileReader("History.txt"));
			try {
				
				String temp = list.readLine();
				while(temp != null){
					newHistory.add(temp);
					temp = list.readLine();
				}
				String[] returnArray = new String[newHistory.size()];
				for(int i = 0;i<newHistory.size();i++){
					returnArray[i] = newHistory.get(i);
				}
				return returnArray;

			} catch(IOException r){ //catch IOException thrown by readLine()
				System.out.println("IOException was thrown when reading from History.txt");
			}
		} catch(FileNotFoundException f) { //catch FileNotFoundException thrown by FileReader()
			System.out.println("FileNotFoundException");
		}
		
  	return null;
    }//method
    
    /*
	 *This is my method which handles the getting of all my favourites that have been 
	 * input into the Bookmarks.txt text file. This is were all my favourites or bookmarked pages 
	 * are stored. I am using Buffered reader class to access all my URL web pages from the bookmarks 
	 * text file.
	 */
    public String[] getAllFavs(){
    ArrayList<String> newFavs = new ArrayList<String>();
   		try {
			BufferedReader listFile = new BufferedReader(new FileReader("Bookmarks.txt"));
			try {
				
				String temp = listFile.readLine();
				while(temp != null){
					newFavs.add(temp);
					temp = listFile.readLine();
				}
				String[] returnArray = new String[newFavs.size()];
				for(int i = 0;i<newFavs.size();i++){
					returnArray[i] = newFavs.get(i);
				}
				return returnArray;

			} catch(IOException r){ //catch IOException thrown by readLine()
				showError("Error in reading bookmarks.txt");
			}
		} catch(FileNotFoundException f) { //catch FileNotFoundException thrown by FileReader()
			showError("The file has not been Found!");
		}
		
  	return null;
  	
  }//method
    
   /*
    * This method was intended as the addition of extra functionality into the system by which i
    * was to add an add to favourites button but didnt come to pass due to time restrictions.
    */ 
    public void saveFavs(){
    	
    }//method
    /*
     *The following 6 methods are MouseListener methods and the check for trigger method 
     * is so as the JPopup displays.
     */
    public void checkForTriggerEvent(MouseEvent e) { 
				if (e.isPopupTrigger() ) 
					Tab.show(e.getComponent(), e.getX(), e.getY()); 
				}//method 
	
	public void mouseExited(MouseEvent e){
		
	}//method
	
	public void mouseEntered(MouseEvent e){
	
	}//method
	
	public void mouseReleased(MouseEvent e){
		checkForTriggerEvent(e);
	}//method
	
	public void mousePressed(MouseEvent e){
		checkForTriggerEvent(e); 
	}//method	
	
	public void mouseClicked(MouseEvent e){	
	
	}//method
		/*
	 *This method deals with all error messages taht popup when something is done
	 *incorrectly.
	 */
	private void showError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage,
		"Error", JOptionPane.ERROR_MESSAGE);}
}//end of class

