import java.math.*;
import java.awt.*;
import java.awt.color.*;
import java.util.*;

/**
This class only keeps track of registered primitives making sure that they are all drawn and moved.
It does no math. All math should be done by Primitive and it's children
However, it is also responsible for providing the objects with scene-global info.
This class only implements one camera at the time. Many can be defined, but this is strongly disrecommended


Warning: Collision-detection does not work. Planets are swallowed up and then dissapear. 
Solution: They got their mass as size. Hence out of screen.... ;) Stupid me.

New error: Collided objects do not move arount. They are drawn, but are static, even if they report both velocity and acceleration.
Horay. Long live debugging!!! How could I have guessed that the objects weren't activated....

I can't seem to get the machine to place out random elements. Indexoutofbounds in nO > 2
Fixed, but added O(n) searching of vector. Unnecesary.
*/

public class Solar implements Scene
{
    private Vector objects; //All dynamic particles
    private int nO; //number of dynamic particles. Deprecated.
    private Star[] stars; //Some stars
    
    //private Tripod tri; //And a cordinate system. Apparently confusing.
    private Camera camera; //A camera is nice
    private boolean depth = false; //Wireframe does not need depth-testing            
    private Part selected = null; //Nothing initially selected
    private Dummy dummy = new Dummy(); //A hook for the camera
    private double mouseStrafeScaleFactor = .5; //A nice value. used to strafe camera
    private double mouseRotateScaleFactor = .01; //Same here. used to rotate camera.
  
    /** Creates a new solar system. */
    public Solar () // SceneInfo scinfo )
    {
	
	//tri = new Tripod ( new Vector3 ( 0, 0, 0 ), 10 ); //Not for the final version. Confuses people
	
	camera = new Perspec ( new Vector3 (400, 0, 0 ), //New camera
			       new Vector3 ( 0, .5, 0 ),
			       new Vector3 ( 0, 0, .5 ) );
	
	//System.out.println ( "Size: " + objects.size() + "\n" + objects );	

	objects = new Vector(); //Start making objects

	//init ( scinfo );

	stars = new Star[500]; //Many stars
	
	for ( int i = 0; i < stars.length; i++ ) {
	    double t = Math.random()*100; //One angle
	    double p = Math.random()*100; //A second angle
	    stars[i] = new Star ( t, p, Double.MAX_VALUE, 10, Color.white ); //And initialize
	}
	camera.initTrack ( dummy ); //Follow that dummy!
    }

    public synchronized void init ( SceneInfo scinfo )
    {
	//position
	double x, y, z;
	
	for ( int i = 0; i < scinfo.nO; i++ ) { //Place them in one by one.
	    if ( scinfo.galType == SceneInfo.PLANAR ) {
		x = 0;
	    } else {
		x = (Math.random()*2-1)*scinfo.galSize;
	    }
	    y = (Math.random()*2-1)*scinfo.galSize;
	    z = (Math.random()*2-1)*scinfo.galSize;
	    
	    objects.addElement ( new Part ( new Vector3 ( x, y, z), Math.random()*2*scinfo.avMass ) );
	}
	
	for ( int i = 0; i < objects.size(); i++ ) { //Give them a velocity
	    Vector3 p = ((Part)objects.elementAt(i)).getPos();
	    ((Part)objects.elementAt(i)).setVel( p.cross( new Vector3(1, 0, 0 ) ).div( 100 ) );
	}
	
	
	for ( int i = 0; i < objects.size(); i++ ) { //Tell them to get moving. 
	    try {
		((Part)objects.elementAt(i)).activate();
	    } catch ( NullPointerException e ) {
		System.out.println ( "Scene/<init>: Object " + i + 
				     " not declared" );
		System.exit(-1);
	    }    
	}
    }


    public synchronized void frame( WinDesc w )
    {	
	Primitive.initFrame ( camera, w ); //File window information
	//tri.frame(); //Take out. Confuses people.
	camera.doTrack();
	
	if ( depth ) //If depth-testing is enabled
	    sortO();

	int i; //Helps debug with jdb.

	//Draw stars first, they tend to be behind.
	for ( i = 0; i < stars.length; i++ ) {
	    stars[i].frame();
	}	

	//Comes from runFunc. Better to have it here, but It looks like it doesn't work.... Fixed now....
	for ( i = 0; i < objects.size(); i++ ) {    
	    //Make sure objects know if they are selected
	    if ( objects.elementAt(i) == selected ) {
		((Part)objects.elementAt(i)).setFocus ( true );
	    } else {
		((Part)objects.elementAt(i)).setFocus ( false );
	    }
	    ((Part)objects.elementAt(i)).frame ( ); //And draw.
	} 	
    }

    public synchronized void runFunc ( )
    {
	Graviton grav[] = new Graviton [ objects.size() ]; //Make some space
	int i; //For jdb.

      	//Loop through to make gravity field
	for ( i = 0; i < objects.size(); i++ ) {
	    grav[i] = ((Part)objects.elementAt(i)).getGrav();
	}
	
	//Set the current acceleration for all objects. 
	for ( i = 0; i < objects.size(); i++ ) {
	    try {
			((Part)objects.elementAt(i)).fall( grav );
	    } catch ( CollisionEvent e ) {		
		//Start easy. Erase secondary object. 
		objects.removeElement( e.part2 ); //O(n)
		//Now try to replace first element with new element.
		objects.setElementAt( e.result, i );
		//Make sure we don't lose track. He He...
		if ( camera.getTrack() == e.part1 || camera.getTrack() == e.part2 ) {
		    camera.initTrack( e.result ); 
		}
		
	    }
	}
    
      
	//And finally, move the objects to their new positions.
	for ( i = 0; i < objects.size(); i++ ) {
	    ((Part)objects.elementAt(i)).move( 1 );
	}
	
	//Move camera to match tracked object's movements.
	camera.doTrack();
    }
    
    public void camRoll ( double a )
    {
	double s =  mouseRotateScaleFactor;
	camera.roll ( a*s );
    }
    public void camRot ( double x, double y, double z )
    {
	// 0, y, x
	//Warning. Orbits around some arbitrary, undefined point, but it looks good. Works perfectly now.
	double s =  mouseRotateScaleFactor;
	camera.orbit( -x*s, y*s, z*s );
    }

    public void camStrafe ( double x, double y, double z )
    {
	double s = mouseStrafeScaleFactor; //Too much writing.
	camera.strafe ( -x*s, y*s, -z*s ); //Take inverted screen into account
    }

    public Primitive collDetect ( double x, double y )
    {
	
	selected = null; //Start clean.
	for ( int i = 0; i < objects.size(); i++ ) { //Loop the loop
	    if ( camera.collDetect ( new Vector3 ( x, y ), ((Part)objects.elementAt(i)).pos,  ((Part)objects.elementAt(i)).rad ) ) { //Do the magic.
		selected = (Part)objects.elementAt(i); // Register as selected.
		return selected; //Return.
	    } 
	}
	
	return null;
    }

    public void camTrack( Location object )
    {                    
	if ( object == null ) { 
	    camera.initTrack ( dummy ); //Stand still
	} else {
	    camera.initTrack ( object ); //Follow and object.
	}
    }
    
    public void setGrav ( int value )
    {
	Part.setGrav ( (double)(100 - value)/50000 ); //Play God. Sets the universal gravitational constant.
    }
    
    public void toggleKin ()
    {
	Part.toggleKin(); //Pass on.
    }

    /* 
       This algorithm uses brute force. One that can take 
       advantage of the fact that the list is usually well
       -sorted would really help. Merge sort has been recommended. 
       Ask Peter Modin.
    */
	private void sortO ( ) 
    {
	//Bubble sort routine. Hopefully obsolete
	Object temp;
	boolean swap;
	for ( int i = 0; i < objects.size() - 1; i++) {
	    swap = false;
	    for ( int j = 0; j < objects.size() - 1; j++) {
		//find distances
		double dist1 = camera.getPos().sub( ((Part)objects.elementAt(j)).getPos() ).length();
		double dist2 = camera.getPos().sub( ((Part)objects.elementAt(j+1)).getPos() ).length();
		if ( dist1 < dist2 ) {
		    //Swap places
		    temp = objects.elementAt(j);;
		    objects.insertElementAt( objects.elementAt(j+1), j );
		    objects.insertElementAt( temp, j+1 );
		    swap = true;
		}
	    }
	    if ( !swap )
		break;
	}
	
    }

    public Camera getCam()
    {
	return camera; 
    }

    public Part getO( int n )
    {
	System.out.println( "Scene/getO: Deprecated" );
	return (Part)objects.elementAt(n);
    }
    public int getNO () 
    {
	System.out.println( "Scene/getNO: Deprecated" );
	return objects.size();;
    }
    public void printStatus()
    {
	System.out.println ( objects );
    }
    public Location getTrack()
    {
	return camera.getTrack();
    }

}










