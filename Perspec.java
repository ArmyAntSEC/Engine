/**
* This is the standard camera. It uses the common pinhole projection which it a quite good approximation 
* of most cameras under normal circumstances. The main benefit is that it is very quick, as 
* it doesn't use trigonometry.
*/
public class Perspec extends Camera
{
    /* inheritance
      public Vector3 pos; //Position vector of camera
      public Vector3 right; //Half the vector width of the camera
      public Vector3 up; //Half the vector height of the camera
      public Vector3 dir; //Unit direction of camera
      public Vector3 size; //Cameras clipping values
    */

    /** 
    * Creates a new pinhole camera. This constructor takes the same arguments as camera, but 
    * interprets them differently. The camera is seen as having a plane of size width*height set 
    * 1 unit in front the focus of all the rays. |vector| > 1 is generally unsuitable, 
    * although neither a physical nor programmable problem.
    */
    public Perspec ( Vector3 pos, Vector3 right, Vector3 up )
    {
		super ( pos, right, up );
    }

	/** 
 	* Projects a point in 3-space onto the view-plane. This function uses pinhole projection, 
 	* which is quick, and gives very good results if the FOV isn't too large.<BR>
 	* Make sure to check the super-class documentation for more details.
 	*/
    public Vector3 project ( Vector3 p3 )
    {
	//Make sure location is up to date. Yes!! This looks absolutely professional....
	//doTrack();

	// Return vector;
	Vector3 p = new Vector3();
      
	//Vector between camera centre and p3
	Vector3 PC = new Vector3 ( p3.x - pos.x,  
				   p3.y - pos.y,  
				   p3.z - pos.z );

	if ( dir.dot ( PC )  <= 0 ) {
	    p = null;
	} else {
	    //Do actual projecting
	    p.x = right.dot ( PC ) / dir.dot ( PC );
	    p.y = up.dot( PC ) / dir.dot ( PC );
	    p.z = PC.length();
	}
	//System.out.println ( "Per/project: Call finished." );
	return p;
    }
  
  	
    public boolean collDetect ( Vector3 sPoint, Vector3 oPoint, double rad )
    {
	//Fix point to rastered screen cordinates.

	//Calculate ray
	//1) initial point
	Vector3 p = pos;
	//2) direction vector
	Vector3 isect = new Vector3 ( sPoint.x/(2*size.x), sPoint.y/(2*size.y) );
	Vector3 u = dir;
	u = u.add ( right.mult ( -isect.x ) );
	u = u.add ( up.mult ( isect.y ) );
	u = u.unit();
	//Done....

	//Calculate particles distance from ray
	//1) Find vector from camera to particle
	Vector3 CP = oPoint.sub ( p );
	//2) Project onto u
	Vector3 proj = u.mult ( CP.dot ( u ) );
	//3) Find orthogonal vector from ray to point
	Vector3 orto = CP.sub ( proj );
	//4) Find length
	double dist = orto.length();
	//Done...

	//System.out.println ( "orto: " + orto.length() );


	//final check.
	if( dist < rad ) {
	    //System.out.println ( "Colission" );
	    return true;
	} else {
	    return false;
	}
    }
}



