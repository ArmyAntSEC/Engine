/**
* This interface defines an object that has a velocity, an acceleration and can move.
*/
public interface Inertial extends Location
{
    /** 
  	* Tells the object to move a certain distance.
  	* @param dt The time the object should move through.
  	*/
    public void move ( int dt ); //Tells the object to move go to where it should be.
    /**
    * Sets the acceleration of the current object.
    * @param a The objects new acceleration
    */
    public void setAcc ( Vector3 a ); //Sets the current acceleration. Not final.
    /**
    * Sets the velocity of the current object.
    * @param v The objects new velocity
    */
    public void setVel ( Vector3 v ); //Sets current speed
    /** 
    * Tells the object to begin moving. All objects should begin deactivated. 
    * This assures no objects start running around until everything is ready.
    */
    public void activate( );
}
