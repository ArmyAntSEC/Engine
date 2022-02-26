import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;

/**
* The Buttons class handles the input from the user. It contains a variety of buttons and 
* checkboxes that let the user start and stop the simulation, as well as move the viewpoint. 
* The user can further change certain aspects of the rendering, as well as choose to show 
* the help-files and documentation in a popup window.
*
*@author Daniel Armyr
*/

public class Buttons extends Panel implements ActionListener, AdjustmentListener, ItemListener
{
    /**
     * Pause button
     */   
    public Checkbox pause;
    /**
	* Strafe or move the camera.
	*/   
    public Checkbox strafe;
    /**
	* Orbit/Rotate the camera.
	*/   
    public Checkbox rotate;
    /**
	* Track a certain particle.
	*/  
    public Checkbox track;
    /**
	* Screw the camera.
	*/ 
    public Checkbox screw;
    /**
	* Show kinematics.
	*/ 
    public Checkbox acc;
    /**
	* The group of buttons that define camera-motion modes.
	*/ 
    public CheckboxGroup modeButtons;
    /**
     * Displays the help.
     */ 
    public Button help;

    /**
     *Restarts the applet
     */
    public Button restart;
    /**
     * Adjusts the gravitational constant.
     */
    public Scrollbar gravity;

    private View view;
    private Clock clock;
    private Scene scene;
    private Engine engine;
    private AppletContext context;
    private URL helpURL;
	
    /** 
 	* Constructs a <code>Buttons</code> object. 
 	* @param v The view to be controlled by this set of buttons.
 	* @param s The thread that the buttons should control.
 	* @param c The scene that the buttons should pass their input to.
 	* @param con The current AppletContext. It will be used to display the help
 	* @param helpU The complete URL to the help files.
 	*/
    public Buttons ( View v, Clock s, Scene c, Engine e, AppletContext con, URL helpU )
    {
	view = v;
	clock = s;
	scene = c;
	engine = e;
	context = con;
	helpURL = helpU;

	modeButtons = new CheckboxGroup();

	pause = new Checkbox ( "Pause" );
	pause.addItemListener( this );
	pause.setBounds ( 500, 0, 100, 50 );
	
	acc = new Checkbox ( "Kinematics" );
	acc.addItemListener ( this );
	acc.setBounds ( 600, 0, 100, 50 );

	strafe = new Checkbox ( "Move", modeButtons, false );
	strafe.addItemListener ( this );
	strafe.setBounds ( 500, 50, 100, 50 );

	rotate = new Checkbox ( "Orbit", modeButtons, false );
	rotate.addItemListener ( this );
	rotate.setBounds ( 600, 50, 100, 50 );

	screw = new Checkbox ( "Screw", modeButtons, false );
	screw.addItemListener ( this );
	screw.setBounds ( 500, 100, 100, 50 );
	
	track = new Checkbox ( "Track", modeButtons, false );
	track.addItemListener ( this );
	track.setBounds ( 600, 100, 100, 50 );
	
	help = new Button ( "Help" );
	help.addActionListener ( this );
	help.setBounds ( 500, 150, 100, 50 );

	restart = new Button ( "Restart" );
	restart.addActionListener ( this );
	restart.setBounds ( 600, 150, 100, 50 );

	gravity = new Scrollbar ( Scrollbar.VERTICAL, 50, 5, 0, 105 );
	gravity.addAdjustmentListener ( this );
	gravity.setBounds ( 700, 0, 20, 500 );
	
	scene.setGrav ( 50 );
    }
    
    /**
    * Handles all the button pressings. In the current implementation only the help is a button. 
    * If the Help button is pressed, the browser will display the help files in a separate window.
    */
    public void actionPerformed ( ActionEvent e )
    {
	if ( e.getSource() == help ) {
	    try {
		context.showDocument( helpURL , "_blank" );
	    } catch( Exception ex ) {
		//Can't be bothered
	    }
	} else if ( e.getSource() == restart ) {
	    engine.init();
	}

    }                                   
    
    /**
 	* Informs the scene that the gravity has been changed.
 	*/ 
    public void adjustmentValueChanged ( AdjustmentEvent e ) {
    
    	if ( e.getSource() == gravity ) {   
	    scene.setGrav ( e.getValue() );		    
    	}
    }
    
    /**
  	* Handles the checkbox events. It will inform the View how to interpret mouse events, 
  	* tells threads to start and stop as well as telling the scene what rendering options to use.
  	*/
    public void itemStateChanged ( ItemEvent e )
    {
	if ( e.getSource() == acc ) {
	    scene.toggleKin();
	    //modeButtons.setSelectedCheckbox ( null ); //Does not appear necessary.
	} else if ( e.getSource() == pause ) {
	    if ( pause.getState() ) {  //Took away an == true
		clock.setActive ( false );
		context.showStatus( "Simulation paused" );
		//view.setCursor ( new Cursor ( Cursor.WAIT_CURSOR ) );
	    } else if ( !pause.getState() ) { //took away an == false
		clock.setActive ( true );
		context.showStatus( "Simulation resumed" );	       
		//view.setCursor ( new Cursor ( Cursor.DEFAULT_CURSOR ) );
	    }
	    //modeButtons.setSelectedCheckbox ( null ); //Does not appear necessary.
	} else if ( e.getSource() == track ) {
	    view.setMouseMode ( "Track" );
	    context.showStatus( "Tracking Mode" );
	    view.setCursor ( new Cursor ( Cursor.CROSSHAIR_CURSOR ) );
	} else if ( e.getSource() == strafe ) {
	    view.setMouseMode ( "Strafe" );
	    context.showStatus( "Move Mode" );
	    view.setCursor ( new Cursor ( Cursor.HAND_CURSOR ) );
	} else if ( e.getSource() == rotate ) {
	    view.setMouseMode ( "Rotate" );
	    context.showStatus( "Orbit Mode" );
	    view.setCursor ( new Cursor ( Cursor.MOVE_CURSOR ) );
	} else if ( e.getSource() == screw ) {
	    view.setMouseMode ( "Screw" );
	    context.showStatus( "Screw Mode" );
	    view.setCursor ( new Cursor ( Cursor.NW_RESIZE_CURSOR ) );
	} else {
	    context.showStatus ( "Engine/actionPerformed: Unhandled button event." );
	}                                      	
    }

}



