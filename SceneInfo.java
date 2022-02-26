/** A structure containing some info about how to build the scene. Used as a messanger between Query and Scene. */
public class SceneInfo 
{
    /** The number of particles */
    public int nO = 0;
    /* Their average mass */ 
    public double avMass = 0;
    /* Size of the area to spread particles over */
    public double galSize = 0;
    /* The type of galaxy to generate.*/
    public int galType = 0;
    
    /** A flat galaxy */
    public static int PLANAR = 0;
    /** A more round galaxy */
    public static int SPHERICAL = 1;
}



