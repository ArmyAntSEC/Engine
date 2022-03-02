import java.awt.*;

/** 
 * This is the class for all objects that interact with the gravitational field in the simulation. 
 * They are graphically represented a spheres with a radius proportial to the cube root of the mass.
 * They further feature graphic displaying of acceleration and velocity. This is shown as two 
 * lines in the respective directions with lengths directly proportional to the relevant
 * kinematical quantity.
 */
public class Part extends Sphere implements Inertial
{
    private Vector3 v; //Velocity
    private Vector3 a; //Acceleration    
    private boolean active = false;
    private double m; //Mass
    private static double G = .001; //Universal gravity constant    
    private boolean focus = false;
    private static boolean kin = false;    
   
   	/** 
     * Creates a new particle. Newly created particles have no initial velocity, and are not affected by the 
     * gravitational field, although they themselves contribute. 
     * @param pos The initial position of the particle
     * @param mass The mass of the particle
     */
    public Part (  Vector3 pos, double mass )
    {
	super( pos, Math.pow( mass, .3 ), null ); //Density is 1.
	//Check that we don't have a negative mass
	if ( mass < 0 ) {
	    throw ( new NegativeMassException() );
	}
	//Set color depending on mass
	col = massColor ( mass );		

	v = new Vector3( 0, 0, 0 );
	a = new Vector3( 0, 0, 0 );
	m = mass;
    }  
    
    /**
     * Tells the particle to adjust it's acceleration to the current gravitational field. 
     * In effect this function tells the particle to start taking an active part in the simulation.
     */
    public void activate ( )
    {
	active = true;
    }

    public void frame ()
    {
	if ( focus ) {
	    g.setColor ( Color.white );
     	} else {
	    g.setColor ( col );
	}
	super.frame();
	/*
	  Draws a line with length and direction of the current acceleration. 
	  Should have a button for enabling and disabling.
	*/
	if ( kin ) { //Took away an == true
	    try {
		Vector3 p = cam.project ( pos );
		Vector3 acc = cam.project ( pos.add( a.mult( 100 ) ) );
		Vector3 vel = cam.project ( pos.add( v.mult( 10 ) ) );
		
		g.setColor( Color.white );
		drawLine ( p, acc );
		
		g.setColor ( Color.yellow );
		drawLine ( p, vel );
	    } catch ( NullPointerException e ) {
			System.out.println ( "Part/frame: Null pointer" );
	    }
	}
    }
   
    /**
     * This function tells the object to move through a distance matched by the time dt. 
     * In the particles case, this means adjusting the velocity by the acceleration, and them moving.
     * @param dt The time to move through.
     */
    public void move ( int dt )
    {
	if ( active ) { //Don't move objects that arn't mobile. Took away an == true
	    v = v.add ( a.mult( dt ) );
	    //System.out.println ( v.x + " " + v.y + " " + v.z + "\t" + a.x + " " + a.y + " " + a.z  );
	    pos = pos.add ( v.mult( dt ) );
	}
    }
   
   	/**
     * This function tells the particle to adjust it's acceleration in accordance to the current gravitational 
     * field. Only the acceleration is actually changed by calling the function. In order to actually move
     * the object, <code>move</code> must be called. However, if too objects are too close, they are merged together
     * by the throwing of a collision exception.
     * @param grav An array of gravitons that define the current gravitational field.
     * @exception CollisisonEvent If two objects are too close, they will be merged together 
     * into one larger particle. No more calculations will be performed on the current object 
     * in that frame.
     */
    public void fall ( Graviton[] grav ) throws CollisionEvent
    {
	Vector3 e = new Vector3();
	double f = 0;
	Vector3 force = new Vector3();
	/*
	  Loop through and calculate the gravitational field at the point 
	  where this particle is, ignoring the infinite gravity of the 
	  particle we are counting for.
	*/
	for ( int i = 0; i < grav.length; i++ ) {
	    if ( grav[i].id != this ) {
		//Get direction of force...
		e = grav[i].pos.sub( this.pos ); 
		if ( e.length() <= this.rad + grav[i].id.getRad() ) {
		    throw ( new CollisionEvent ( this, grav[i].id, i ) ); //Objects merge....
		    //e = new Vector3(); //To avoid dividebyzero error.
		} else {
		    f = G * grav[i].m * this.m /  //Newtons gravity.....
			this.pos.sub( grav[i].pos ).length();
		    e = e.unit();
		}
		force = force.add ( e.mult( f ) );
	    }
	}
	//Calculate acc...
	a = force.div ( this.m );

    }
    
    //Sets the current acceleration.
    public void setAcc ( Vector3 a )
    {
	this.a.cp ( a );
    } 
    
    public void setVel ( Vector3 v )
    {
	this.v.cp ( v );
    }  
    
    /** 
    * Generates a Graviton object. Used to inform the scene how 
    * this particle affects the current gravitational field.
    * @return The graviton describing the current object.
    */
    public Graviton getGrav()
    {
	return new Graviton ( new Vector3( pos ), m, this );
    }
    
    /**
    * Tells this object weather it has focus or not. Focus means that 
    * the mouse points to the current object. The particle's normal 
    * response should be to repaint itself in some other style or colour.
    * @param b Weather this object has focus or not.
    */
    public void setFocus ( boolean b )
    {
	focus = b;
    }
    
    /** Sets the Gravitational constant for all particles scene. */
    public static void setGrav ( double value )
    {
	G = value;
    }
    
    /** Toggles weather to show kinematical information graphically or not. */
    public static void toggleKin ()
    { 
	if ( kin ) //Took awayn an == true
	    kin = false;
	else
	    kin = true;
    }

    /**Sets the kinematics state. Used to make sure the proper state is set when program starts. */
    public static void toggleKin ( boolean state )
    {
	kin = state;
    }

	/** Returns a string with a short description of the current particle. For debugging. */    
    public String toString()
    {
	return "Mass: " + (float)m + "\nRadius: " + (float)rad + "\nSpeed: " + (float)v.length() + "\n";
	    
    }
    
    /** Returns the particles current velocity */
    public Vector3 getVel ()
    {
	return v;
    }
    
    /** Returns the particles current mass */
    public double getMass()
    {
	return m;
    }
    
    // Returns a colour depending on the colour of the entered object.
    private Color massColor ( double mass )
    {
	int[] color = { 150 + (int)(mass / 3), (int)(mass), 10 };
	for ( int i = 0; i < 3; i++ ) {	   
	    if ( color[i] > 255 )
		color[i] = 255;
	    if ( color[i] < 0 )
		color[i] = 0;
	}
	return new Color ( color[0], color[1], color[2] );
    }
}





