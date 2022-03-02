import java.awt.*;

/** 
* Draws a fix cordinate system at the origin. I used in in the begining to 
* maintain my orientation before the stars. Now obsolete, as it seems to confuse the
* users. However, if movement seems erratic, the cordinate system is a nice and handy
* reference point in debugging.
*/
public class Tripod extends Primitive
{    
  	/** Creates a new Tripod. */
    public Tripod ( Vector3 pos, int size )
    {
	super( pos, null );	
    }
    
    /**  Tells the Tripod to redraw itself. */
    public void frame ( )
    {
		Vector3 point = null; 
		try {
		    point = cam.project ( pos );
		} catch ( NullPointerException e ) {
		    System.out.println ( "Tripod/frame: Null Pointer" );
		}
	
		if ( point == null ) {
		    //Point is behind camera
		} else {
		    Vector3 O = cam.project ( pos );
		    Vector3 p1 = cam.project ( pos.add( new Vector3 ( 1, 0, 0 ) ) );
		    Vector3 p2 = cam.project ( pos.add( new Vector3 ( 0, 1, 0 ) ) );
		    Vector3 p3 = cam.project ( pos.add( new Vector3 ( 0, 0, 1 ) ) );
	
		    try {
			    	
				g.setColor ( Color.red );
				drawLine( O, p1 );
		
				g.setColor ( Color.green );
				drawLine( O, p2 );
		
				g.setColor ( Color.blue );
				drawLine( O, p3 );
				
		    } catch ( NullPointerException e ) {
				if ( g == null ) {
				    throw new GraphicsException();
				} else {
			    	System.out.println ( "Tripod/frame: Unknown Null pointer" );
				}
		    }
		}
    }
}
