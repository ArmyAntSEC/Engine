/**
* This class defines one particle's contribution to the total gravitational field. 
*/
public class Graviton
{ 
    /** The position of the current particle */	
    public Vector3 pos;
    /** The mass of the current particle */
    public double m;
    /** 
    * The reference of the current particle. Used to id the particle who created 
    * the field, and avoid some DivideByZero errors.
    */
    public Part id;
	
	/**
	* This constructor simply creates a new <code>Graviton</code>.
	* @param p The position of the particle.
	* @param mass The particles mass.
	* @param identity The reference to the current particle.	
	*/
    public Graviton ( Vector3 p, double mass, Part identity )
    {
	pos = p;
	m = mass;
	id = identity;
    }

}
