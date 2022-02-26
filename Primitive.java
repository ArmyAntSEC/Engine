import java.awt.*;
import java.awt.color.*;

/**
* This class is the parent for all shapes. It includes some general methods for calculations that shapes 
* must do to be able to draw them selves.
*/
public abstract class Primitive implements Location
{
    /** Position of current object */
    protected Vector3 pos;
    /** The dimentions of the current screen. Should not be edited by hand. */ 
    protected static Vector3 screen; 
    /** Camera used to render the current object. */
    protected static Camera cam; 
    /** The current graphics object. Don't edit yourself. */
    protected static Graphics g;
    /** The current objects color. */
    protected Color col;
    
    /** 
    * An ambient light used for the shading.
    */
    protected Vector3 light = new Vector3 ( 1, 1, 1 );

    /** 
    * Creates a new primitive object.
    * @param pos The primitives location.
    * @param col The primitives initial color.
    */
    public Primitive ( Vector3 pos, Color col )
    {
	this.pos = new Vector3( pos );
	this.col = col;
    }

    /**
    * Draws the current Primitive in a window defined by win as seen from 
    * the camera cam. Each Primitive subclass needs to define their own version
    * of this method since it is a shape-unique method.
    */
    public abstract void frame ( );

    /**
    * These are frame specific informatia that need to be configred. 
    * They only need to be done once per frame for the whole scene, 
    * as it initializes the software-internal graphics system. 
    * @param camera Tells all objects what camera to render from.
    * @param win Information on the current viewport.
    */
    public static void initFrame ( Camera camera, WinDesc win )
    {
	screen = new Vector3 ( win.w, win.h);
	g = win.g;
	cam = camera;
    }

    public Vector3 getPos ()
    {
	return pos;
    }

    /** 
     * Rotates the object around the origin. Serves absoulutely no purpose in the 
     * gravitation simulation, but allows freedom of implementation.
     */    
    public void rot ( double x, double y, double z )
    {
	pos.rot ( x, y, z );
    }
    /** Moves the current object */
    public void trans ( double x, double y, double z )
    {
	
	pos.cp ( pos.add ( x, y, z ) );
    }

    /** Returns the screen cordinates of entered point on the viewplane. */
    protected Vector3 cord ( double x, double y )
    {
	return new Vector3 ( screen.x/2*( 1 + x / cam.size.x ) , screen.y/2*( 1 - y/cam.size.y ), 0);
    }
 
    /** Wrapper function for <code>Vector3 cord ( double x, double y )</code>. */
    protected Vector3 cord ( Vector3 p )
    {
	return cord ( p.x, p.y );
    }

    /** Draws a circle on the viewplane. The function automatically transforms to screen-cordinates. */
    protected void drawCircle ( double x, double y, double r, Color col )
    {
	Vector3 p = cord ( x, y );

	//g.setColor ( col );
	g.drawOval ( (int)(p.x - r/2), (int)(p.y - r/2), (int)(2*r), (int)(2*r) );
	
	//System.out.println ( "Prim/drawCirc: " + p.x + " " + p.y );

    }
    
    /** Draws a line on the viewplane. The function automatically transforms to screen-cordinates. */
    protected void drawLine( Vector3 p1, Vector3 p2 )
    {
	if ( p1 == null || p2 == null) {
	    /*
	      This simply means that the point is behind the camera. This is a
	      measure to make sure we get a single sided projection cone, 
	      and not a camera that can see in both directions.
	    */
	} else {
	    Vector3 a = cord( p1 );
	    Vector3 b = cord( p2 );
	    
	    //g.setColor ( col );
	    g.drawLine ( (int)a.x, (int)a.y, (int)b.x, (int)b.y );
	}
    }
    
    /** Draws a point on the viewplane. The function automatically transforms to screen-cordinates. */
    protected void drawPoint ( Vector3 p ) 
    {
	if ( p == null ) {
	    //Do nothing, point behind camera...
	} else {
	    Vector3 a = cord ( p );
	    //g.setColor ( col );
	    g.drawRect ( (int)a.x, (int)a.y, 1, 1 );
	}
    }

    /** 
	* Draws a filled triangle on the viewplane, scaling to 
	* screen-coordinates automatically. The function takes back-facing into account, and 
	* uses a flat-shading algorithm, which unfortunately seems to change the hue even if
	* only the brightness should change. This is due to a malfunction in the getHSVcolor() 
	* function.
	*/
    protected void doTri ( Vector3 p1, Vector3 p2, Vector3 p3, double s ) 
    //Draws a triangle, using curent camera, taking note of lights
    {
	//Project points
	Vector3 d1 = cam.project ( p1 );
	Vector3 d2 = cam.project ( p2 );
	Vector3 d3 = cam.project ( p3 );

	if ( d1 == null || d2 == null || d3 == null ) {
	    /*
	      This simply means that the point is behind the camera. This is a
	      measure to make sure we get a single sided projection cone, 
	      and not a camera that can see in both directions.
	    */
	} else {
	    Vector3 a = cord( d1 );
	    Vector3 b = cord( d2 );
	    Vector3 c = cord( d3 );
	    
 	    int x[] = { (int)a.x, (int)b.x, (int)c.x };
	    int y[] = { (int)a.y, (int)b.y, (int)c.y };

	    //Do we have a backface?
	    //1) Find cross product.
	    Vector3 v1 = a.sub ( b );
	    Vector3 v2 = b.sub ( c );
	    Vector3 n = v1.cross ( v2 );
	    //2) Is z-component negative?
	    if ( n.z < 0 ) {
		
		//Check if the current color is white. If it isn't, start shading.
		if ( g.getColor().equals ( Color.white ) ) {
		    //Set the color to white, and get drawing....
		    g.setColor ( Color.white );
		} else {
		    //Find normal
		    v1 = p2.sub ( p1 );
		    v2 = p3.sub ( p1 );	
		    n = v1.cross( v2 );
		    
		    //Find cos of angle between normal and some predefined vector Light.
		    double cos = n.dot ( light ) /  ( n.length() * light.length() );
		    		  
		    //Convert color to better format
		    float hsb[] = Color.RGBtoHSB ( col.getRed(), col.getGreen(), col.getBlue(), null );
		    
		    //Adjust brightness, cos e [-1,1] and adds some to loose the gloomy effect
		    hsb[1] -= cos / 2 + .5;

		    if ( hsb[1] < 0 )
			hsb[1] = 0;
		    if ( hsb[1] > 1 )
			hsb[1] = 1;
		    		    
		    //Extremely ugly
		    g.setColor ( Color.getHSBColor ( hsb[0], hsb[1], hsb[2] ) );
		}
		//Draw Triangle
		g.fillPolygon ( x, y, x.length );
	    }
	}
    }
}




  
