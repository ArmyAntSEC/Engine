
/** 
* A nice location that sits still in the universe. 
* Handy for hooking camera to so they don't float around too much.
*/
public class Dummy implements Location
{
    private Vector3 pos;

	/** Creates a dummy in the prescribed position */
    public Dummy( Vector3 p )
    {
	pos = p;
    }
    
    /** Creates a dummy at the origin */    
    public Dummy ()
    {
	this( new Vector3( 0, 0, 0 ) );
    }
    
    /** 
	* Returns the position of the dummy. 
	* @return The dummies position.
	*/
    public Vector3 getPos ( )
    {
	return pos;
    }
    
    /** 
    * Make sure the dummy can present itself. 
    * @return An empty string. Dummies should never admit their existence. 
    */
    public String toString()
    {
	return "";
    }
}
