/**
* This exception is thrown whenever any two particles collide. In it's constructor it 
* automatically calculates the new object which is created, conserving mass and momentum. 
*/
public class CollisionEvent extends Throwable
{ 
	/** The first object in the collision */
    public Part part1;
    /** The second object in the collision */
    public Part part2;
    /** The object resulting from the merger of the above two. */
    public Part result;
    /** The index of the second object in the collision. Used to remove the object from the scene. */
    public int index2;

    /**
	* Simple constructor. Not so simple code inside.....
	* @param p1 Particle 1
	* @param p2 particle 2
	* @param i The index of the second particle. Used for removal.
	*/
    public CollisionEvent ( Part p1, Part p2, int i )
    {
	part1 = p1; //Store some references
	part2 = p2;
	index2 = i; 

	Vector3 moment1 = p1.getVel().mult ( p1.getMass() ); //Calculate the individual moments.
	Vector3 moment2 = p2.getVel().mult ( p2.getMass() );

	Vector3 moment = moment1.add ( moment2 ); //Add them

	double mass = p1.getMass() + p2.getMass(); //Find the total mass	
	
	Vector3 v = moment.div ( mass ); //What velocity to give it?
	
	result = new Part ( part1.getPos(), mass ); //Make the result
	result.setVel( v ); //Give it a speed
	result.activate(); //Get it moving

    }
}
    
