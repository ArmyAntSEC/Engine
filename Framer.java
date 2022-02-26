/**
* This little class deals with redrawing the applet, at appropriate intervals.
*/
public class Framer extends Thread
{
    private Engine engine; //A reference.
    private int framePeriod; //Time to wait.
  
  	/** 
 	* Creates a new Framer instance. 
 	* @param fp The time to wait between each frame.
 	* @param e The applet to redraw.
 	*/
    public Framer ( int fp, Engine e )
    {
		framePeriod = fp;
		engine = e;	
    }

    /** 
	* Does the actual calls to update the applet every few milliseconds
	*/
    public void run ()
    {
		while ( true ) {
		    try {
				Thread.sleep( framePeriod );
		    }
		    catch ( InterruptedException e ) {
			}
		    engine.redraw(); //Draw up next frame
		}
    }
}
