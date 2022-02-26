/** A simple interface used by all objects for which a location is relevant */
public interface Location
{
    /** 
	* Get the location of the current object. 
	* @return The objects current position
	*/
    public Vector3 getPos();
}
