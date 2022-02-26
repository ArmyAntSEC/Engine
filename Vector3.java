/** 
* A 3-space vector. Includes all the vector math necessary for the physics and graphics. 
* In reality the core of the whole program.
*/
public class Vector3
{   
    /** The cordinates of the vector */
    public double x, y, z;
    
    /** Creates a new vector */
    public Vector3 ( double x, double y, double z ) 
    {
		this.x = x;
		this.y = y;
		this.z = z;
    }
    
    /** Creates a new vector */
    public Vector3 ()
    {
		this ( 0, 0, 0 );
    }                          
    
    /** Creates a new vector */
    public Vector3 ( Vector3 v )
    {
		this( v.x, v.y, v.z );
    }
    
    /** Creates a new vector with only x and y defined. */
    public Vector3 ( double x, double y )
    {
    	this( x, y, 0 );
    }

	/** Copies the argument into the current vector. */
    public void cp( Vector3 p )
    {
	x = p.x;
	y = p.y;
	z = p.z;
    }

	/** Returns the sum of this vector and the argument. */
    public Vector3 add ( Vector3 v )
    {
		return new Vector3 ( this.x + v.x, this.y+v.y, this.z+v.z );
    }
    
    /** Returns the difference between this vector and the argument. */
    public Vector3 sub ( Vector3 v )
    {
	return new Vector3 ( this.x - v.x, this.y - v.y, this.z - v.z );
    }

    /** Wrapper function for <code>Vector3 add ( Vector3 v )</code>. */ 
    public Vector3 add ( double x, double y, double z )
    {
		return this.add( new Vector3 ( x, y, z ) );
    }
    
    /** Returns the product of the current vector and the scalar argument. */
    public Vector3 mult ( double x )
    {
	return new Vector3 ( this.x*x, this.y*x, this.z*x );
    }

	/** Returns the quotient between the current vector and the scalar argument. */
    public Vector3 div ( double x )
    {
	return new Vector3 ( this.x/x, this.y/x, this.z/x );
    }

	/** Returns the Euklidian scalar product between this vector and the argument. */
    public double dot ( Vector3 p )
    {
	return x*p.x + y*p.y + z*p.z;
    }

	/** Returns the vector cross product bewteen this vector and the argument */
    public Vector3 cross ( Vector3 p )
    {
	return new Vector3 ( this.y*p.z - this.z*p.y, 
			     this.z*p.x - this.x*p.z,
			     this.x*p.y - this.y*p.x );
    
    }

	/** Returns the Euklidian length of the current vector. */
    public double length ()
    {
	return Math.sqrt ( x*x + y*y + z*z );
    }

    /** Returns the unit direction of this vector. */
    public Vector3 unit()
    {
	return new Vector3 ( x/length(), y/length(), z/length() );
    }

    /** Rotate this vector in world space */
    public void rot ( double x, double y, double z )
    {
	rotX ( x );
	rotY ( y );	
	rotZ ( z );
    }

    /** Return a string with the elements of the current vector. */
    public String toString ()
    {
	return x + " " + y + " " + z;
    }
    
    // Rotates this vector around the X-axis.
    private void rotX ( double a )
    {
	double xt = x;
	double yt = y*Math.cos(a) - z*Math.sin(a); 
	double zt = y*Math.sin(a) + z*Math.cos(a);
	x=xt;
	y=yt;
	z=zt;
    } 
    
    // Rotates this vector around the Y-axis. 
    private void rotY ( double a )
    {
	double xt = x*Math.cos(a) + z*Math.sin(a);
	double yt = y;
	double zt = -x*Math.sin(a) + z*Math.cos(a);
	x=xt;
	y=yt;
	z=zt;

    } 
    
    /** Rotates this vector around the Z-axis. */    
    private void rotZ ( double a )
    {
	double xt = x*Math.cos(a) - y*Math.sin(a);
	double yt = x*Math.sin(a) + y*Math.cos(a);
	double zt = z;
	x=xt;
	y=yt;
	z=zt;

    }

	/** 
	* Rotates this vector around an arbitratry vector. <BR>
	* The matrix used to rotate is copied directly from Elementary Linear Algebra. 
	* @param a The ammount to rotate.
	* @param u The vector to rotate around. Should be a unit vector. 
	*/
    public void rotA ( double a, Vector3 u )
    {
	//Calculate transformation matrix.
	double v[][] =
	{
	    {
		u.x*u.x*(1-cos(a)) +     cos(a), 
		u.x*u.y*(1-cos(a)) - u.z*sin(a),  
		u.x*u.z*(1-cos(a)) + u.y*sin(a)
	    },
	    {
		u.x*u.y*(1-cos(a)) + u.z*sin(a),
		u.y*u.y*(1-cos(a)) +     cos(a),
		u.y*u.z*(1-cos(a)) - u.x*sin(a)
	    },
	    {
		u.x*u.z*(1-cos(a)) - u.y*sin(a),
		u.y*u.z*(1-cos(a)) + u.x*sin(a),
		u.z*u.z*(1-cos(a)) +     cos(a)
	    }
	};
 
	//Apply
	double xt = v[0][0]*x + v[0][1]*y + v[0][2]*z;  
	double yt = v[1][0]*x + v[1][1]*y + v[1][2]*z;  
	double zt = v[2][0]*x + v[2][1]*y + v[2][2]*z;  

	//Implement
	x = xt;
	y = yt;	
	z = zt;


    }
    //A wrapper meahod.
    private double cos( double x )
    {
	return Math.cos( x );
    }   
    
    //A wrapper meathod
    private double sin( double x )
    {
	return Math.sin( x );
    }
}



