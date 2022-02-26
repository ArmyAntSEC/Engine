import java.awt.*;
import java.awt.event.*;

/**
* This class is taken straight from the Java Direct book. It is a simple Frame with the
* exceptption that it can be closed properly. 
*/
public class XFrame extends Frame  
{
    /** 
    * Creates an instance of XFrame
    * @param title The title of the window to be created.
    */
    public XFrame( String title ) 
    {
	super ( title );
	addWindowListener ( closeCheck ); //In case the user wants to kill the window
    } 
    
    /**
    * Tells the XFrame to prepare for action. This method contains all cosmetics as well as the actual 
    * showing of the window. It is necessary to separate it from the constructor in order to make inheritance 
    * reasonably simple.
    * @param width The window's width.
    * @param height The window's height.
    */
    public void init ( int width, int height ) //Makes a window
    {
	setResizable ( false ); //If you want fixed-size windows
	setBackground ( Color.black ); //Standard background color
	setBounds ( 10, 10, width, height ); //define extents of window

	//show(); Show the window. Should be called last.
	//setVisible ( true ); //Necessary function
                                                                               
    }
    
    private WindowAdapter closeCheck = new WindowAdapter() {
	    public void windowClosing ( WindowEvent e ) { 
		dispose();
		//System.exit(0);
		//System.out.println ( "XFrame: Window closed..." );
	    }
	    public void windowMoved( WindowEvent e ) {
		//System.out.println ( "XFrame/windowMoved" );
	    }
	}; 
	
}
