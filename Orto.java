/**
* This is a camera that uses orthogonal projection. It is the simplest form and serves mostly as a debugging tool. 
* The images produced are quite boring. However, it is very stable and simple, and objects don't hide behind the 
* camera all the time.
*/

public class Orto extends Camera
{
   	/**   
     * Creates an instance of Orto. 
     * @param pos The initial position of the camera.
     * @param right A vector defining the right limit of the view-port  
     * @param up A vector defining the upper limit of the view-port 
    */
    public Orto ( Vector3 pos, Vector3 right, Vector3 up )
    {
    	/* 	Width and height define the area of the camera plane. They are 
     	 	independent of the size of the canvas to be drawn to. This allows 
     		quick resizing to the window.
    	*/
		super ( pos, right, up );
    }

    public void rot( double x, double y, double z )
    {
	right.rot ( x, y, z );
	up.rot ( x, y, z );
	//System.out.println ( "Camera/rot: " + x + " " + y + " " + z );
    }
  public Vector3 project ( Vector3 p3 )
  {

    // Return vector;
    Vector3 p = new Vector3();
      
    //Vector between camera centre and p3
    Vector3 PC = new Vector3 ( p3.x - pos.x,  
			       p3.y - pos.y,  
			       p3.z - pos.z );
      
    // Project point on camera plane
    p.x = right.dot ( PC );
    p.y = up.dot ( PC );
    p.z = PC.length();
      
    return p;
  }

    public boolean collDetect ( Vector3 sPoint, Vector3 oPoint, double rad )
    {
	return false;
    }    
}
