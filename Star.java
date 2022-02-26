import java.awt.*;

/**
* Defines a star. A star is a Sphere that only displays as a single dot no matter the distance. 
* Used to maintain orientation in the sometimes caotic rotations of the camera.
*/
public class Star extends Sphere
{
    /** Creates a new star. */
    public Star ( double theta, double phi, double r, int size, Color color )
    {
	super ( new Vector3 ( r*Math.sin(phi)*Math.cos(theta), r*Math.sin(phi)*Math.sin(theta), r*Math.cos(phi) ), size, color );
    }
    
    
    public void frame ( )
    {
	Vector3 point = null; 
	try {
	    point = cam.project ( pos );
	} catch ( NullPointerException e ) {
	    System.out.println ( "Tripod/frame: Null Pointer" );
	}
	if ( point != null ) {
	    g.setColor ( col );
	    drawPoint ( point );
	}		
    }
}
