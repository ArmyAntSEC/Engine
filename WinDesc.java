import java.awt.*;

/** This class contains info about the current window. Allows objects to draw themselves. */
public class WinDesc 
{
	/** The graphics object used to draw with */
    public Graphics g;
	/** The width of the viewport */
    public int w;                
    /** The height of the viewport */           
    public int h;

	/** Create a new Windesc object.
    * @param width The wieport width.
    * @param height The viewport height.
    * @param gr The current graphics object.
    */
    public WinDesc ( int width, int height, Graphics gr )
    {
	g = gr;
	w = width;
	h = height;
    } 
    
    /** Diet version. Used if no graphics object is available, for example during initialization. */
    public WinDesc ( int width, int height )
    {
	w = width;
	h = height;
    }       
    
    /** Sets the current graphics object. */
    public void setG ( Graphics gr )
    {
	g = gr;
    }
}
