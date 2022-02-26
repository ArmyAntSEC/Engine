import java.awt.*;
import java.awt.event.*;

/**
* This is a small frame that opens during the beginning of the program. It allows
* the entry of several options, of wich all have a recomended value pretyped.
* Fairly much a textbook thing....
*/
public class Query extends XFrame implements ActionListener
{
    TextField avMass = new TextField( "50" ); //Average Mass
    TextField galSize = new TextField( "200" ); //Galaxy Radius
    TextField nO = new TextField ( "100" ); //Number of planets
    CheckboxGroup galType = new CheckboxGroup(); 
    Checkbox galCyl = new Checkbox( "Planar", galType, true ); //Plane galaxy
    Checkbox galSphere = new Checkbox( "Spherical", galType, false ); //Round galaxy
    Button submit = new Button ( "Start" ); //The big red button...
    SceneInfo scinfo; //Info about how to build the scene
    Scene scene; //The scene itself

  	/** 
  	* Creates a Query object. 
  	* @param s The scene to affect.
  	* @param title The title of the Query object. Passed on to Frame.
  	*/
    public Query( Scene s, String title )
    {
		super( title );
		scene = s;
    }

	/**
	* Does the meat of the creation work. It may seem a bit redundant to have a separate method for this, but it
	* is an inheritance from the way XFrame is made. 
	* @param w The width of the window.
	* @param h The height of the param object.
	* @param scinf A structure to contain the info generated about the scene.
	*/
    public void init ( int w, int h, SceneInfo scinf )
    {
	scinfo = scinf; //Store a reference
	super.init( w, h ); //Get the windo running.
	setBackground ( new Color( 192, 192, 192 ) ); //A nice Win95-gray
	GridLayout grid = new GridLayout ( 6, 2 ); //Quick and dirty...
	setLayout ( grid ); 

	add ( new Label( "Average Mass" ) );
	add ( avMass );

	add ( new Label( "Galaxy Size" ) );
	add ( galSize );

	add ( new Label( "Number of particles" ) );
	add ( nO );

	add ( new Label( "Type of Galaxy" ) );
	add ( new Label( "" ) );

	add ( galCyl );
	add ( galSphere );

	add ( submit );
	submit.addActionListener( this );
	
    }
    
    /**
    * Kicks in when the user puches the submit button. This function parses 
    * and stores the data. Passes it to the scene and then commits
    * suicide.
    */
    public void actionPerformed( ActionEvent e )
    {
	if ( e.getSource() == submit ) { //Security check.
	    //Parse info.
	    scinfo.avMass = Integer.parseInt( avMass.getText() );
	    scinfo.galSize = Integer.parseInt( galSize.getText() );
	    scinfo.nO = Integer.parseInt( nO.getText() );
	    if ( galType.getSelectedCheckbox().getLabel().equals ( "Planar" ) ) { //Homemade parser
		scinfo.galType = SceneInfo.PLANAR;
	    } else {
		scinfo.galType = SceneInfo.SPHERICAL;
	    }
	    scene.init( scinfo ); //Pass info on.
	    this.dispose(); //Goodby cruel world.
	}
    }
}

