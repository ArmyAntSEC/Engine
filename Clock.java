/**
* This little class keeps track of the physics, making sure objects 
* are moved around in time. Nothing unusual about it.
*/
public class Clock extends Thread
{
    
    private Scene scene; //The scene controlled by this thread
    //private int timeLeft;
    private int framePeriod; //Time between frames
    private boolean active = true; //If thread should run or not.

  	/**
    * Constructs a new clock.
    * @param fp The time in milliseconds to wait between each frame.
    * @param s The scene object to be taken care of.
    */	
    public Clock ( int fp, Scene s )
    {
	framePeriod = fp;
	scene = s;
    }

    /** 
     * Sets the state of this thread. It can either be active, 'true', and will 
     * then execute the scene's runFunc(), or inactive, 'false', and wont.<P>
     * This function has been implemented since the deprecated <code>stop()</code> and <code>resume()</code>
     * quite consistently hang not only the Java machine, but the whole computer.
     * <code>wait()</code> and <code>notify()</code> have been suggested as alternatives, but I haven't found any 
     * suitable documentation for those functions.
     * @param state To run or not to run.
     */
    public void setActive ( boolean state )
    {
	active = state;
    }

    /** 
     * First waits a moment, then calls the scenes runFunc function.
     */
    public void run ()
    {
	while ( true ) {
	    try {
		Thread.sleep( framePeriod );
	    }
	    catch ( InterruptedException e ) {}
	    
	    if ( active ) {
		scene.runFunc( );
	    }
	}
    }
}

