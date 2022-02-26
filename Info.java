import java.awt.*;
/**
* This is a simple wrapper-class for the TextArea. The difference is that it defaults to non-editable, 
* and that is about it.
*/
public class Info extends TextArea
{
	/** Creates a new, non-editable TextArea object. */
    public Info( String p )
    {
	super ( p, 0, 0, 1 );
	setEditable ( false );
    }
    /** 
	* Tells the Info object to display a string of text. 
	* @param s The String to display.
	*/
    public void print ( String s )
    {
	setText ( s );
    }	    
}
