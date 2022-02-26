import java.awt.*;
import java.applet.*;
import java.math.*;
import java.util.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;

//Not made to be inherited, in case someone tries....
//This applet is made to run in a 500*720 window for the moment. Other sizes will be supported later.
//This is the main control class. It places the different components, and makes sure that all 
//components that need get info about each other.
//WARNING: Appletviewer ignores showDocument() method. This program must be run in a browser that supports all of java.

//Asse request: Make custom cursors for the move, rotate, track etc. Look at Toolkit.createCustomCursor(), and Component.setCursor().

//Good idea: Program should pop up a window asking how many particles, average mass and so on. 

/**
* This is the main control class. It places the different components, and makes sure that all 
* components that need get info about each other. Otherwise there isn't much special about it. 
* All the magic is handled by other classes.
* This applet is made to run in a 500*720 window for the moment. 
* NOTE: Appletviewer ignores showDocument() method. This program must be run in a browser that 
* supports all of java.
* <P>
* Warning!!!<BR>
* This software is not designed or intended for use in on-line control of<BR>
* aircraft, air traffic, aircraft navigation or aircraft communications; or in<BR>
* the design, construction, operation or maintenance of any nuclear<BR>
* facility. Licensee represents and warrants that it will not use or<BR>
* redistribute the Software for such purposes.<BR>
*/
public final class Engine extends Applet 
{

    private Clock clock; //Handles physical time
    private Framer framer; //Handles frame drawing
    
    private int framePeriod = 50; //ms between frames

    private WinDesc w; //Defines the window. It includes width, eight and a graphics object. Allows all objects to draw themselves.
    private Scene scene; //The main scene. Works as a host for individual objects.
    private View view; //The display window. Defined by win-desc. Works closely with Scene
    private Info info; //A small panel that shows info about stuff-
    private Info help; //A small panel that shows help.
    private Image buffer; //Back buffer.
    private Buttons buttons; //A panel of buttons. Handles almost all input.
  
  	/**
    * This function starts up the whole thing, allocating memory, 
    * starting threads, linking objects, the whole thing. Nothing 
    * special, just a miracle of bureaucracy.
    */
    public void init()
    {
	resize ( 720, 500 ); //In case someone tries to mess with the size.

	AppletContext app = getAppletContext(); //Find out some stuff about our surroundings.
	app.showStatus( "Begin Initializing Applet" ); //Talk to the user.
	
	w = new WinDesc ( 500, 500 ); //Sets the current size.
	scene = new Solar( ); //Make the main scene.
	buffer = createImage ( w.w, w.h ); //Create secondary buffer
	info = new Info( null ); //Make an info panel
	help = new Info ( "Instructions:\n\n" +
			  "1)Pause: Pauses and resumes the simulation.\n\n" +
			  "2)Show Kinematics: Displays each objects velocity and acceleration in the view-port.\n\n" +
			  "3)Move: Click and drag in the view-port to move the camera.\n\n" +
			  "4)Orbit: Click and drag in the view-port to orbit the camera around the currently tracked object.\n\n" +
			  "5)Screw: Click and drag in the view-port to roll and dolly the camera.\n\n" +
			  "6)Track: Clicking allows you to select any object. The camera will track the " +
			  "selected object, fixing it in the view-port. Allows you to see the" + 
			  "simulation from any objects perspective." );

	view = new View( scene, w, buffer, info ); //Make a view-port.

	//Make and start a separate thread to remodel the window.
	clock = new Clock ( framePeriod, scene );  
	
	//Make and start a separate thread to repaint the window.
	framer = new Framer ( framePeriod, this );

	try {
	    URL helpURL = new URL ( getCodeBase() + "help.html" ); //URL to the help
	    buttons = new Buttons ( view, clock, scene, this, app, helpURL ); //Make an input panel
	} catch ( MalformedURLException ex ) {	  
	    System.out.println ( "Engine/<init>: Bad URL" );
	    System.exit(-1);
	}

	// Ask for some info from user.
	Query fr = new Query( scene, "Setup" );
	SceneInfo scinf = new SceneInfo();
	fr.init ( 250, 250, scinf );
	fr.show( );

	//Standard background colour. I want it black, but then 
	//MVM makes the info panels black as well.
	setBackground ( new Color( 192, 192, 192 ) );

	view.setBackground ( Color.black );
	view.setSize ( w.w, w.h );

	info.setBounds ( 500, 230, 200, 70 );
	help.setBounds ( 500, 300, 200, 200 );

	setLayout ( null );

	//Removes any previous stuff
	removeAll();

	add ( view );
	add ( buttons.pause );
	add ( buttons.track );
	add ( buttons.strafe );
	add ( buttons.rotate );
	add ( buttons.screw );
	add ( buttons.acc );
	add ( buttons.gravity );
	add ( info );
	add ( help );
	add ( buttons.help );
	add ( buttons.restart );
	
	/* Removed due to ugly artwork.
	   //I am just so good. This works!!!!!!!!!!!!!!!!!
	   Image cursor = getImage ( getCodeBase(), "cursor.gif" );
	   Toolkit toolkit = getToolkit();
	   setCursor ( toolkit.createCustomCursor( cursor, new Point ( 0, 0 ), "cursor" ) );
	*/

	setVisible ( true ); //Necessary function
	
	Part.toggleKin ( false ); //Need a clean start with the static vars.

	clock.start(); //Let's rock.
	framer.start();


	app.showStatus( "Eng/main: Program initiated" ); //Now tell the user.
	
    }
	
    /**
       This method redraw the whole applet, cascading the call down through the program hierarchy.
    */
    public void redraw()
    {
	repaint();
	view.redraw();
    }

    public String getAppletInfo()
    {
	return "Engine, the gravity simulator. By Daniel Armyr. (c)2001, all rights reserved.";
    }

    public void destroy()
    {
	//This returns errors. Strange.
	//clock.destroy(); 
	//framer.destroy();
    }

}









