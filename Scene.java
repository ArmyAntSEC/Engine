/**
* This is the interface that defines a scene. It is completely generic, meaning anyone 
* can implement any scene with any math in it. The only speciffic function is the gravity 
* related one. However, it isn't worth the trouble to change it.
*/
public interface Scene 
{ 
    public void init ( SceneInfo scinf ); //Greates all dynamic objects.
    /** 
     * Tells the scene to draw itself. Scene should simply pass this on to idividual objects, 
     * after perhaps doing some housekeeping.
     */
    public void frame( WinDesc w ); //Draw the frame. 
    /** Tells the scene to update itself, meaning one step in the simulation has passed. */
    public void runFunc ( ); 
    /** Rotate the camera in local space. */
    
    public void camRot ( double x, double y, double z );    
    /** Roll the camera in local space */
    public void camRoll ( double a );
    /** Strafe camera in local space. */
    public void camStrafe ( double x, double y, double z ); 
    /** Ask camera if any object is at x and y on screen */
    public Primitive collDetect ( double x, double y ); 
    /** Sort all objects in scene depending on distance dfrom camera. Using brute force. Should use better sort... */
    public void camTrack( Location object ); 
    /** Adjust gravity in scene. */
    public void setGrav ( int value ); 
    /** Show acc or not */
    public void toggleKin ();  
    /** @deprecated Noone except scen should handle individual objects. */
    public Part getO( int n );
    /** @deprecated Noone except scen should handle individual objects. */ 
    public int getNO (); 
    /** @return The camera curerntly being used for rendering. */
    public Camera getCam();
    /** Prints out the curent status of the scene. */
    public void printStatus();
    /** Returns the object currently being tracked by the rendering camera */
    public Location getTrack();
}
