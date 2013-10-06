import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.net.*;

/*
 *This class has a simple purpose and that is to create a new thread to run 
 *the progress bar as it makes the prgram far more efficient and quick as it runs in a seperate thread.
 */
public class progress extends Thread implements Runnable{
	JProgressBar progressBar = new JProgressBar();
	boolean loading;
	/*
	 *Progress constructor
	 */
	public progress(){
    	loading=false;
	}//constructor
	
	/*
	 *This method controls what happens when the progress is loading and 
	 *the progress within the bar goes up accordingly.
	 */
	public void run(){
 
    	while(true){
    		while(!loading){
    		
				try {
	         		this.sleep(10);
	         	}catch(InterruptedException ignore){}
	         }//while
		
			for(int i=0;i<=100;i++){
				try {
					this.sleep(18);
					progressBar.setValue(i);
					progressBar.setStringPainted(true);	         	
		         } catch (InterruptedException ignore) {
		         }//catch
		         
	         }//for
	         
	         loading=false;
          
          }//while
    
    }//method
	
}//class