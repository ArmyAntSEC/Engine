import java.awt.*;
import java.awt.event.*;

/** 
* This class defines the viewport. It indirectly handles the redrawing of the scene, and such things. 
* This implementation uses a back buffer to make the animation move more smoothly.
*/
public class View extends Canvas implements MouseMotionListener, MouseListener
{
    //References.
    private Scene scene;
    private WinDesc win;
    private Info info; 

    private Image buffer; //Back buffer
    private Graphics gBuffer; //Back buffers drawing tools
    private String mouseMode; //How to interprate mouse input.
    private Vector3 lMouse; //The last known position fo the mouse.
  
   	/** 
	* Creates a new viewport.
	* @param s The scene to draw to screen.
	* @param w The size of the viewport.
	* @param b The back buffer.
	* @param i The textarea to write status info to.
	*/  
    public View ( Scene s, WinDesc w, Image b, Info i )
    {
	lMouse = new Vector3();
	
	setBackground ( Color.black ); //Standard background color
	scene = s;
	win = w;
	buffer = b;
	info = i;
	
	try {
	    gBuffer = buffer.getGraphics(); //Enable drawing on back buffer
	} catch ( Exception e ) {
	    System.out.println ( "View/<init>: Null error in gBuffer" );
	}
	addMouseListener ( this );
	addMouseMotionListener ( this );
    }
    
    /** Tells the view object how to interprate mouse events. */
    public void setMouseMode ( String mode ) 
    {
	mouseMode = mode;
    }
    
    /** Returns the current mouse listening mode. */
    public String getMouseMode( )
    {
	return mouseMode;
    }
    
    /** Tells the component to redraw itself. Used to cascade the repaint message through the program. */
    public void redraw ()
    {
	//Update info in the view window.
	try {
	    info.print ( scene.getTrack().toString() );
	} catch ( NullPointerException e ) {
	    // No problem, things aren't allways selected
	}
	
    	repaint( );
    }
    
    /** Do all the double-buffering things. */
    public void update ( Graphics g )
    {	    
	//Double buffer stuff.
	gBuffer.setColor( getBackground() ); //Set the background color
	gBuffer.fillRect ( 0, 0, getSize().width, getSize().height ); //Blit black to the back buffer
	gBuffer.setColor ( getForeground() ); //Restore foreground color
	paint ( g ); //Paint new image
    }
    
    /** Draw the viewport */
    public void paint ( Graphics g )
    {   
	win.setG ( gBuffer ); //Register the curernt graphics object.
	scene.frame ( win ); //File the window information
	
	try {
	    g.drawImage ( buffer, 0, 0, this ); //Swap buffers
	}
	catch ( NullPointerException e ) {
	    System.out.println ( "BFrame/paint: Null pointer." );
	}
	
    }                    
    
    public void mousePressed( MouseEvent e )
    {
	if ( mouseMode.equals ( "Track" )) {
	    
	    double x = (double)( win.w/2 - e.getX() )/win.w; //Translate from screen to viewplane
	    double y = (double)( win.h/2 - e.getY() )/win.h; // - " -
	    Location object = scene.collDetect ( x, y ); //Is there something there?
	    scene.camTrack ( object ); //Track that object, may be null.
	    if ( object != null ) 
		info.print ( object.toString() ); //Show some stats.
	} else {
	    lMouse = new Vector3 ( e.getX(), e.getY() ); //File mouse pos.
	}
    }
    public void mouseReleased( MouseEvent e )
    {
    }
    public void mouseClicked( MouseEvent e )
    {
    }
    public void mouseEntered( MouseEvent e )
    {
    }
    public void mouseExited( MouseEvent e )
    {
    }
    public void mouseMoved( MouseEvent e )
    {
	double x = (double)( win.w/2 - e.getX() )/win.w; //Translate from screen to viewplane
	double y = (double)( win.h/2 - e.getY() )/win.h;
	//System.out.println ( "View/mMove: " + x + " " + y );
        scene.collDetect ( x, y ); //Is there something there?

    }
    public void mouseDragged( MouseEvent e )
    {
	//Move the camera according to the current move.
	if ( mouseMode.equals ( "Strafe" )) {
	    scene.camStrafe ( e.getX() - lMouse.x, e.getY() - lMouse.y, 0 );
 	} else if ( mouseMode.equals ( "Rotate" )) {
	    scene.camRot ( 0, e.getY() - lMouse.y, e.getX() - lMouse.x );
	} else if ( mouseMode.equals ( "Screw" )) {
	    scene.camRoll( e.getX() - lMouse.x );
	    scene.camStrafe ( 0, 0, e.getY() - lMouse.y );
	}
	lMouse = new Vector3 ( e.getX(), e.getY() ); //Register last mouse pos.
    }
}
		











