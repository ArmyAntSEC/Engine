import java.awt.*;
import java.math.*;

public class Sphere extends Primitive {

    /* 	Inherited
	protected Vector3 pos; //Position of current 
	protected static Vector3 screen; //Should only be used by frame();
	protected static Camera cam; //Camera used to render the current object.
	protected static Graphics g; //The current graphics object. Don't edit yourself.
	protected Color col;
	protected Vector3 light = new Vector3 ( 1, 1, 1 );
    */
	
	/** The current sphere's radius */
    protected double rad;

	/** 
    * Create a new sphere.
    * @param pos The current sphere's position.
    * @param size The current sphere's radius.
    * @param color The current sphere's color.
    */    
    public Sphere ( Vector3 pos, double size, Color color )
    {
	super( pos, color );
	rad = size;
	col = color;
    }
 	
 	/**	Tells the sphere to draw itself. */
    public void frame ( )
    {
	Vector3 point = null; 
	try {
	    point = cam.project ( pos );
	} catch ( NullPointerException e ) {
	    System.out.println ( "Sphere/frame: Null Pointer I" );
	}

	if ( point == null ) {
	    //System.out.println ( "Sphere/frame: Point is behind camera" );
	} else {
	    try {
		//drawOcta ( cam, rad );
		//Implemented to alpha level, but requitres alot of work to be publishable
		//Actually, all it needs is back-facing. Backfacing fixed, but shading gives too much green
		//Has now reached pre-beta. If I can do my own HSV algorithm, it will be implemented. However, 
		//pre-beta it is. Gives a touch of seriosity.
		fillOcta( cam, rad );	    
	    }
	    catch ( NullPointerException e ) {
		System.out.println ( "Sphere/frame: Null pointer II" );
	    }

	}

    }
    
    /** Draws an octahedron using the given camera to the given viewport. */
    protected void drawOcta( Camera cam, double s )
    {
	try {
	    //g.setColor ( col );
	
	    Vector3 p1 = cam.project ( pos.x, pos.y, pos.z + s );
	    Vector3 p2 = cam.project ( pos.x - s, pos.y, pos.z );
	    Vector3 p3 = cam.project ( pos.x, pos.y - s, pos.z );
	    Vector3 p4 = cam.project ( pos.x+s, pos.y, pos.z );
	    Vector3 p5 = cam.project ( pos.x, pos.y+s, pos.z );
	    Vector3 p6 = cam.project ( pos.x, pos.y, pos.z-s );
	    

	    if  ( p1 == null ||  p2 == null ||  p3 == null ||  p4 == null ||  p5 == null ||  p6 == null ) {
	    	//System.out.println ( "Points behind camera" );
		/*
		  If a point of the tetra is behind the camera, don't draw 
		  it al all. This eliminates the possibilty od a sky sphere, 
		  but it is alot faster. THe alternative is to introduce 
		  face-drawing, and then clip each individual face, 
		  if the SPARCS are fast enough. No need. Stars do the job.
		*/
	    } else {
		
		drawLine ( p1, p2 );
		drawLine ( p1, p3 );
		drawLine ( p1, p4 );
		drawLine ( p1, p5 );

		drawLine ( p2, p3 );
		drawLine ( p3, p4 );
		drawLine ( p4, p5 );
		drawLine ( p5, p2 );

		drawLine ( p6, p2 );
		drawLine ( p6, p3 );
		drawLine ( p6, p4 );
		drawLine ( p6, p5 );
	    }
	} 
	catch ( NullPointerException e ) {
	    //System.out.println ( "Sphere/drawOcta: Null Pointer" );
	}
    }

    /** 
     * Draws a filled octahedron, using flat shading and the given camera to the given viewport. 
     * @see Primitive#doTri( Vector3, Vector3, Vector3, double )
     */ 
    protected void fillOcta( Camera cam, double s )
    {
	try {
	    Vector3 p1 = new Vector3 ( pos.x, pos.y, pos.z + s );
	    Vector3 p2 = new Vector3 ( pos.x - s, pos.y, pos.z );
	    Vector3 p3 = new Vector3 ( pos.x, pos.y - s, pos.z );
	    Vector3 p4 = new Vector3 ( pos.x + s, pos.y, pos.z );
	    Vector3 p5 = new Vector3 ( pos.x, pos.y + s, pos.z );
	    Vector3 p6 = new Vector3 ( pos.x, pos.y, pos.z - s );
	    
	    
	    doTri ( p1, p2, p3, s );
	    doTri ( p1, p3, p4, s );
	    doTri ( p1, p4, p5, s );
	    doTri ( p1, p5, p2, s );
	    
	    
	    doTri ( p6, p3, p2, s );
	    doTri ( p6, p4, p3, s );
	    doTri ( p6, p5, p4, s);	    
	    doTri ( p6, p2, p5, s );
	    
	} 
	catch ( NullPointerException e ) {
	    //System.out.println ( "Sphere/drawOcta: Null Pointer" );
	}
    } 
    
    /** Return the spheres radius */
    public double getRad()
    {
	return rad;
    }
}



    
