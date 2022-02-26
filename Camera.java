/**
* This is the super-class for all cameras. It holds some general 
* methods like translating, rotating, and a generalized constructor. 
*/
public abstract class Camera implements Location
{
  	/** The cameras current position */
    protected Vector3 pos; 
    /** A vector defining the right limit of the view-port */
    protected Vector3 right;
    /** A vector defining the upper limit of the view-port */ 
    protected Vector3 up;
    /** A vector pointing in the view direction of the camera */ 
    protected Vector3 dir;
    /** A 2d vector defining half the width and height of the view-port. Used for calculations. */ 
    protected Vector3 size;                                                                      
    
    private Location frame; 
    private Vector3 frameVector; 

    /**   
     * Creates an instance of Camera. 
     * @param pos The initial position of the camera.
     * @param right A vector defining the right limit of the view-port  
     * @param up A vector defining the upper limit of the view-port 
     */
    public Camera ( Vector3 pos, Vector3 right, Vector3 up )
    {
	this.pos = new Vector3();
	this.right = new Vector3();
	this.up = new Vector3();
	this.size = new Vector3();

	this.pos.cp( pos );

	this.right.cp( right );
	this.right = this.right.unit();

	this.up.cp( up );
	this.up = this.up.unit();

	dir = up.cross ( right );
	dir = dir.unit();

	size.x = right.length();
	size.y = up.length();

    }
	/**
	* Rotates the camera around in local space. The system uses 
	* openGL standard with x being the right axel, y being the up axel, and
	* z being the axel into the screen.
	*/
    public void rot( double x, double y, double z )
    {   
    
	up.rotA( x, dir );
	right.rotA( x, dir );
	
	up.rotA( -y, right );
	dir.rotA( -y, right );

	right.rotA( -z, up );
	dir.rotA( -z, up );
	
    }
   
   	/**
	Moves the camera in local space. The system uses 
	* openGL standard with x being the right axel, y being the up axel, and
	* z being the axel into the screen. 
	*/
    public void strafe ( double x, double y, double z )
    {
	//System.out.println ( x + " " + y + " " + z );
	pos = pos.add ( right.mult( x ) ); 
	pos = pos.add ( up.mult( y ) ); 
	pos = pos.add ( dir.mult( z ) ); 

	frameVector = frameVector.add ( right.mult( x ) ); 
	frameVector = frameVector.add ( up.mult( y ) ); 
	frameVector = frameVector.add ( dir.mult( z ) ); 

    }
 
    /** 
     * Orbits the camera around the currently tracked object. Initially, the camera is 
     * hooked to the dummy at the origin, which means it will rotate around the origin.
     * The system uses openGL standard with x being the right axel, y being the up axel, and
     * z being the axel into the screen.
     */
    public void orbit( double x, double y, double z )
    {
	Vector3 oldFrame = new Vector3( frameVector );
	
	double s = 3.14;

	up.rotA( x, dir );
	right.rotA( x, dir );
	frameVector.rotA( x, dir );
	
	up.rotA( -y, right );
	dir.rotA( -y, right );
	frameVector.rotA( -y, right );

	right.rotA( -z, up );
	dir.rotA( -z, up );
	frameVector.rotA( -z, up );
    }

    /**
     *Rolls the camera around current view axel. 
     */
    public void roll ( double a )
    {
	Vector3 oldFrame = new Vector3( frameVector );

	up.rotA( a, dir );
	right.rotA( a, dir );
	frameVector.rotA( a, dir );
    }
    
    /** 
    * A wrapper function for <code>Vector3 project ( Vector3 p3 )</code>.
    */ 
    public Vector3 project ( double x, double y, double z )
    {
	return project ( new Vector3 ( x, y, z ) );
    }
    
    /** 
     * Projects a point in 3-space onto the view-plane of the camera. It is up 
     * to each sub class how to do this. The value returned should be scaled 
     * relative to the values in size. The camera also has the alternative 
     * to return null which will signal the point will not be drawn. 
     * This should include points behind the camera, or other points that
     * return coordinates on-screen but should not be drawn.
     * @param p3 A point in 3-space to be projected.
     */
    public abstract Vector3 project ( Vector3 p3 );  
    
    /**
     * This method queries if sPoint in view-plane coordinates corresponds to a line through oPoint
     * in 3-space. An error smaller than rad will be tolerated. This is used for selection and 
     * querying.
     * @param sPoint A point on the view-plane.   
     * @param oPoint A point in 3-space.
     * @param rad The tolerance.
     * @return true if the two points correspond.
     */
    public abstract boolean collDetect ( Vector3 sPoint, Vector3 oPoint, double rad );
    
   	/** 
    * Returns the cameras current position 
    * @return The cameras position. 
    */
    public Vector3 getPos()
    {
	return pos;
    } 
    
    /** Moves the camera to any position in space. */
    public void setPos( Vector3 p )
    {
	pos.cp(p);
    }
    
    /**
	* Tells the camera to begin tracking an object.
	* @param o The <code>Location</code> to track.
	*/
    public void initTrack ( Location o )
    {
		frame = o;
		frameVector = pos.sub ( o.getPos() );
    }
   
   	/**
   	* Tell the camera to move a distance corresponding to the distance the tracked object 
   	* has moved, in the same direction.
   	*/
    public void doTrack ()
    {
	if ( frame != null && frameVector != null ) {
	    pos = frame.getPos().add ( frameVector );
	} 
    } 
    
    /**
    * Find what object is being tracked.
    * @return The object being tracked.
    */
    public Location getTrack()
    { 
	return frame;
    } 
    
    /**
    * Returns a string describing the current state of the camera. Mostly for debugging.
    * @return A short status report from the camera.
    */
    public String toString()
    {
	return "Pos: " + pos + " Right: " + right + " Up: " + up + " size: " + size;
    }
}




