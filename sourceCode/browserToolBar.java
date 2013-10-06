import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.net.*;
/*
 *This is my browserToolBar class in which i create most of functionality within my browser. 
 * My toolbar and hyperlink update is within this class and all of my navigation around web pages is done here.
 *Within this class is my JEditor pane in which all my HTML pages are displayed and all my navigational icons
 *are read in from this classes constructor.
 */
public class browserToolBar extends JToolBar implements HyperlinkListener, MouseListener{
	URL url;
	ArrayList<String> pageList;
	JEditorPane editorPane = new JEditorPane();
	public JButton back, forward, go, home, refresh;
	public JTextField webPageEditor = new JTextField(25);
	JPopupMenu pop = new JPopupMenu();
	progress bar=new progress();
	JMenuItem itemCopy, itemPaste;
	
	/*
	 *This is the classes constructor which intialises my array list which stores all my webpages. I also intialise
	 *and create all my buttons within this constructor. I deal with all the major functionality within this constructor such
	 *as: copy, paste, navigation, refresh, home page etc
	 */
	public browserToolBar(){
		pageList = new ArrayList<String>();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(this);
    	webPageEditor.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent event) {
		try {
          URL url = new URL(webPageEditor.getText());
          showUrl(url, false);
        }
        catch (Exception urlException) {
          showError("Invalid URL");
        }
      
      }
    	
    });

	//Copy and paste popup menu!
			itemCopy = new JMenuItem("Copy");
			itemPaste = new JMenuItem("Paste");
	    	itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)); 
	    	itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	    	pop.add(itemCopy); pop.addSeparator(); pop.add(itemPaste);
			pop.addMouseListener(this);
	
			webPageEditor.addMouseListener(this);
			editorPane.addMouseListener(this);
			
			copy();
			paste();
			
	//first button
   		back = new JButton(new ImageIcon("back.gif"));
       	back.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent event) {
        	actionBack();
     	}//method
    	});
    	back.setEnabled(false);
    //Second button
    	forward = new JButton(new ImageIcon("forward.gif"));
    	forward.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent event) {
        	actionForward();
     	}//method
    	});	
    	forward.setEnabled(false);
    //Third button
    	home = new JButton(new ImageIcon("home.gif"));
    	home.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent event) {
        	actionHome();
     	}//method
    	});	
    //wepPageEditor KeyListener
		webPageEditor.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Go();
				}
			}//method
		});	
    //Fourth button
    	go = new JButton(new ImageIcon("go.gif"));
    	go.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent event) {
       		Go();
        }//method
      });
     
    //Fifth Button  
      	refresh = new JButton(new ImageIcon("refresh.gif"));
    	refresh.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent event) {
        	//Go();
     	}//method
    	});			
    		add(back);
    		add(forward);
   			add(home);
    		add(webPageEditor);
    		add(go);
    		add(refresh);
    		add(bar.progressBar);
        	bar.start();
        
    }//constructor
    /*
     *By the name given its obvious to assume all the code within 
     *this class does the copy function.
     */
    public void copy(){
    	itemCopy.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent h){ 
				editorPane.copy();
				webPageEditor.copy();
				}//method
			});
    }//method
    /*
     *Likewise, this method contains code to paste all information
     *copied.
     */
    public void paste(){
    	itemPaste.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent h) { 
 				webPageEditor.paste();
			}//method
		});	
    }//method
    /*
     *This method deals with the validation of entered webpages. If
     *a webpage is entered with no http:// its entered for the end user.
     */
	public String validation(String n){
		n.toLowerCase();
		if(!n.startsWith("http://")){
			n="http://"+n;	
		}//if
		return n;
	}//method
	/*
	 *The go method controls the shift from one page to another by validating all inputted
	 *data first.
	 */
	public void Go(){
		try {
		  String page=validation(webPageEditor.getText());
		  webPageEditor.setText(page);	 
		  URL url = new URL(page);	
          showUrl(url,true);
		  System.out.println("ArrayList size " +pageList.size());
        	}
        catch (Exception urlException) {
          showError("Invalid URL");
        }//catch	
	
	}//method
	/*
	 *This method controls what happens when an end user pressed backwards. It will 
	 *shift to the previously viewed webpage.
	 */	
	public void actionBack(){
		URL currentUrl = editorPane.getPage();
		//System.out.println(pageList ==  null);
		System.out.println(pageList.size());
		
			int pageIndex = pageList.indexOf(currentUrl.toString());
			try {
				//int pageIndex = pageList.indexOf(currentUrl.toString());
				showUrl(new URL((String) pageList.get(pageIndex - 1)), false);
				System.out.println("ArrayList size " +pageList.size()+" pageNo: "+pageIndex);
				}catch (Exception e) {}
	}//method
	/*
	 *This method controls what will happen when an end user requires to an intially viewed web
	 *page. The page will shift from the past to the present page.
	 */
	public void actionForward(){
		URL currentUrl = editorPane.getPage();
			int pageIndex = pageList.indexOf(currentUrl.toString());
				try {
					showUrl(new URL((String) pageList.get(pageIndex + 1)), false);
					System.out.println("ArrayList size " +pageList.size()+" pageNo: "+pageIndex);
					}catch (Exception e) {}
				
				}//method
	/*
	 *This method controls what happens when you click the home button. This method directs you to the 
	 *http://nottingham.ac.uk website.
	 */
	public void actionHome(){
		//System.out.println("isAlive: "+bar.isAlive());
		URL homeUrl = null;
		try{
			homeUrl = new URL("http://nottingham.ac.uk");
			pageList.add(homeUrl.toString());
			System.out.println("ArrayList size " +pageList.size());
			showUrl(homeUrl, false);
			actionUpdate();
			}catch(Exception net){
				showError("Invalid Home URL");	
			}//try/catch
	
	}//method
	
	/*
	 *This method updates all the buttons according to how the end user interacts
	 *with the system. The buttons will be appropriately enabled or disabled.
	 */
	public void actionUpdate(){
		if (pageList.size() < 2) {
			back.setEnabled(false);
			forward.setEnabled(false);
		  	}else{
				URL currentUrl = editorPane.getPage();
					int pageIndex = pageList.indexOf(currentUrl.toString());
					back.setEnabled(pageIndex > 0);
					forward.setEnabled(pageIndex < (pageList.size() - 1));
			}//else
		  
		}//method
	/*
	 *This method is the key mechanism of how the pages change. The prgress bar will update with
	 *the changing page and the wait cursor will be intialised and dependant upon whether the method parameters 
	 *are met then the method will accordingly change web page.
	 */
	public void showUrl(URL page, boolean addPagetoList){
		bar.loading=true;
		System.out.println("showURL");
		// Show progress cursor while loading is under way.
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			// Get URL of page currently being displayed.
			URL currentUrl = editorPane.getPage();

			// Load and display specified page.
			editorPane.setPage(page);

			// Get URL of new page being displayed.
			URL newUrl = editorPane.getPage();
 			
      		// Add page to list if specified.
				if (addPagetoList) {
					System.out.println("addPagetoList");
					int listSize = pageList.size();
						if (listSize > 0) {
							int currentPageIndex =pageList.indexOf(currentUrl.toString());
								
								if (currentPageIndex <= listSize - 1) {
									for (int i = listSize - 1; i > currentPageIndex; i--) {
										pageList.remove(i);
						}//for
				
					}//3rd if
					
				}//2nd if
		
				pageList.add(newUrl.toString());
			}//1st if
		
		// Update location text field with URL of current page.
		webPageEditor.setText(newUrl.toString());
			
		// Update buttons based on the page being displayed.
		actionUpdate();
    	} catch (Exception exception) {
      		showError("Invalid URL");

    	}finally{
		// Return to default cursor.
			setCursor(Cursor.getDefaultCursor());
		}//finally
			bar.loading=false;
	}//method

	/*
	 *This method deals with all error messages taht popup when something is done
	 *incorrectly.
	 */
	private void showError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage,
		"Error", JOptionPane.ERROR_MESSAGE);}
	/*
	 *This is the main powerhouse for the program to be able to interpret HTML and make this
	 *web borwser possible.
	 */
	public void hyperlinkUpdate(HyperlinkEvent event) {
      HyperlinkEvent.EventType eventType = event.getEventType();
		if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
			if (event instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
				HTMLDocument document =(HTMLDocument) editorPane.getDocument();
				document.processHTMLFrameHyperlinkEvent(linkEvent);
				} else {
					showUrl(event.getURL(), true);
				}//if/else
			
			}//1st if		
    
  	}//method
	
	/*
     *The following 6 methods are MouseListener methods and the check for trigger method 
     * is so as the JPopup displays.
     */	
	public void checkForTriggerEvent(MouseEvent e) { 
				if (e.isPopupTrigger() ) 
					pop.show(e.getComponent(), e.getX(), e.getY()); 
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
	

}//class