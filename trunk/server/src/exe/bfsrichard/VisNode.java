/* 
This file is part of JHAVE -- Java Hosted Algorithm Visualization
Environment, developed by Tom Naps, David Furcy (both of the
University of Wisconsin - Oshkosh), Myles McNally (Alma College), and
numerous other contributors who are listed at the http://jhave.org
site

JHAVE is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your
option) any later version.

JHAVE is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with the JHAVE. If not, see:
<http://www.gnu.org/licenses/>.
*/

package exe.bfsrichard;

import java.io.*;   //Now we can use simple names for all classes in java.io
                    //      Notably, the PrintWriter class
import java.awt.*;          //For graphics
import java.awt.event.*;    //For event listeners
import java.util.regex.*;		//For gaigs hexColor String

/**
 * A class to store information about nodes (vertices) in a visual graph
 *   Such as color, highlighting, position, and the character stored inside
 *
 * @author Jeff Lucas
 * @version April 29th, 2003
 * Revised for gaigs dijkstra - Richard Teviotdale 06/23/04
 */

/**
 *  NOTE: Colors are represented by JAVA-style color strings. (28 options)
 *        The valid colors include black, blue, blue2, blue3, blue4, white
 *        brown2, brown3, brown4, cyan, cyan2, cyan3, cyan4, gray, green,
 *        green2, green3, green4, magenta, magenta2, magenta3, magenta4, 
 *        pink, pink2, pink3, pink4, red, red2, red3, red4, white, & yellow
 *  NOTE2: Each color has an "node opposite color."  When a node is
 *         highlighted, it is set to its node opposite color            
 *  NOTE3: If you EVER change the node character, make sure to deactivate
 *         the node and do a print first (to hide the old node) otherwise
 *         you'll be in BIG trouble.  Trust me. (id based on node char  */


public class VisNode
{
    //Data members
    private String color;        // Saves the color (inside) of this node
    private String text;         // comment field - gaigs
    private int cost;            // used for dijkstra's - gaigs
    private boolean closed;      // used for dijkstra's - gaigs
    private boolean highlighted; // Is this node highlighted or not?
    private char cindex;         // The character stored in this node
    private double x, y;         // The cartesian coordinates of the node,
                                 // normalized to be E[0, 1]
    private boolean activated;   // Used in the current structure?

    //  Utility variables... these are used ONLY by this class
    private boolean idExists;        //This becomes true when an ANIMAL ID
                                     //     exists for this particular node
    private boolean isColorChanged;  //This is true when node color changes
    private boolean isChanged;       //This is true when the node has been
                                     //     moved or a weight changed

    private String hexColor;         //Stores the color hex of the edge (gaigs adaptation)


    /**Default Constructor for objects of class VisNode  
     * Pre:  Nothing
     * Post:  Set all the attributes of the node to default values */
    public VisNode()
    {
        // Initialize all variables
        color = "white";        //Default color is white
        hexColor = "#FFFFFF";        //Default color is white
        cindex = 'A';           //Default character is A
        x = 0.5;    y = 0.5;    //Default location is at the center
        highlighted = false;    //Highlight is off by default
        activated   = false;    //Node starts deactivated
        idExists = false;       //We have no ANIMAL ID out there yet
        isColorChanged = false; //No color changes yet
        isChanged = false;      //Nothing has been changed yet
	text = "";		//gaigs
	closed = false;		//gaigs
	cost = 0;		//gaigs
    }


    /**  Constructor that is given beginning values 
     *Pre:  Read in values for all the attributes of a node
     *Post: Set all the attributes of the node to the appropriate values */
    public VisNode(char my_c, String my_col, double my_x, double my_y, 
                   boolean my_hlight, boolean my_active)
    {
        // Set all attributes
        if(!isValidNodeColor(my_col)) color = "white";
            else color.copyValueOf(my_col.toCharArray());
        cindex = my_c;
        x = my_x;
        y = my_y;
        highlighted = my_hlight;    
        activated = my_active;
        idExists = false;
        isColorChanged = false;
        isChanged = true;           //We want to draw the node now!
        hexColor = "#FFFFFF";        //Default color is white
    }


    /**Function clearNode
     * Pre:  Nothing     
     * Post:  Sets Node's values to the defaults  */
    public void clearNode()
    {
        color = "white";        //Default color is white
        cindex = 'A';           //Default character is A
        x = 0.5;                //Default location is at the center
        y = 0.5;
        highlighted = false;    //Highlight is off by default
        activated = false;      //Not activated until you say so
        isColorChanged = false; //Unused node can't have a color
        isChanged = false;      //Not changed... disappeared!
    }


/*************************************************************************/
/**                       QUERY NODE ATTRIBUTES                         **/
/*************************************************************************/

    public String getColor() { return color;  } //Get the node's color
    public double getX()     { return x;      } //Get the node's x-coord
    public double getY()     { return y;      } //Get the node's y-coord
    public char   getChar()  { return cindex; } //Get the node's character
    public boolean isHighlighted() { return highlighted; }  //Highlighted?
    public boolean isActivated()   { return activated;   }  //Activated?
    public boolean isClosed()      { return closed;      }  //gaigs
    public String  getText()       { return text;        }  //gaigs
    public int     getCost()   { return cost;    }  //gaigs
    public String getHexColor() { return hexColor; }     //Get edge's color

/*************************************************************************/
/**                         SET NODE ATTRIBUTES                         **/
/*************************************************************************/

    /**Sets the node color if valid, otherwise defaults to white  */
    public void setColor(String c)
    {
        if(!isValidNodeColor(c)) color = "white";
            else color = c; //.copyValueOf(c.toCharArray());
        isColorChanged = true;
    }
    
    public void setHexColor(String c)                    //Set edge color
    {
	    //Pattern p = new Pattern.compile("#[0-9a-fA-F]{6}");
	    //Matcher m = new p.Matcher(c);

	    if( Pattern.compile("#[0-9a-fA-F]{6}").matcher(c).matches() )
		    hexColor = c;
    }
    
    /**Sets the node text - gaigs*/
    public void setText(String newText)
    {
            text = newText;
    }
    
    /**Sets the node cost - gaigs*/
    public void setCost(int newCost)
    {
            cost = newCost;
    }

    /**Sets the node closed variable - gaigs*/
    public void setClosed(boolean newClosed)
    {
            closed = newClosed;
    }

    /**These functions set the center point (my_x, my_y) for the node, or
     * the char for the node.  If location is changed, isChanged = true */
    public void setX(double my_x)
    { 
        if(x != my_x) isChanged = true;  
        x = my_x; 
    }
    public void setY(double my_y)
    { 
        if(y != my_y) isChanged = true;  
        y = my_y; 
    }

    /**These functions set the center point (my_x, my_y) for the node, or
     * the char for the node.  If location is changed, isChanged = true */
    /**These limit the values of x and y to [0,1]                      **/
    public void setLimitedX(double my_x)
    { 
        if(x != my_x) isChanged = true;  
        x = my_x; 
        if(x < 0) x = 0;
        if(x > 1) x = 1;
    }
    public void setLimitedY(double my_y)
    { 
        if(y != my_y) isChanged = true;  
        y = my_y; 
        if(y < 0) y = 0;
        if(y > 1) y = 1;
    }


    public void setChar(char my_c) { cindex = my_c; }

    /**Set highlighting -- adjust highlighting color appropriately   */
    public void setHighlighted()   { if(!highlighted) toggleHighlighted(); }  
    public void setUnhighlighted() { if( highlighted) toggleHighlighted(); }
    public void toggleHighlighted()
    {
        color = color.copyValueOf(getOtherNodeColor(color).toCharArray());
        isColorChanged = true;
        highlighted = !highlighted;
    }

    //Set activation
    public void activate()          {   activated = true;  }  //Activate
    public void deactivate()        {   activated = false; }  //Deactivate

/*************************************************************************/
/**                       PRINT NODE FUNCTIONS                          **/
/*************************************************************************/


    /**Function:  printNode
     * Pre:  Takes a PrintWriter fout and two ints (x,y) defining
     *       the file to send ANIMAL printing out to and the screen size
     *       Also, takes an appropriate radius for each node r
     * Post: Prints out this node to the file appropriately            */
    public void printNode(PrintWriter fout, int x_size, int y_size, int r)
    {
      //Find the color for the text, it's used often
      String textcol = getOtherTextColor(color);
      int px = translatePixel(x_size, r, x);    //X & Y positions
      int py = translatePixel(y_size, r, y);

      //First, check if the node is activated.
      //If not, hide the node (if shown) & leave
      if(!activated)
        if(idExists)  //Hide now-inactivated visibles!
        {
          fout.println("  hide \"" + cindex + "-node\" \"" 
                        + cindex + "-text\"");
          idExists = false;
        }
        else;
      else      //Otherwise, there's something out there
      {
        //Make sure the color is correct no matter what
        if(isColorChanged && idExists)
        {
          fout.println("  color \"" + cindex + "-node\" type " +
                       "\"color\" " + textcol);
          fout.println("  color \"" + cindex + "-node\" type " + 
                       "\"fillColor\" " + color);
          fout.println("  color \"" + cindex + "-text\" type " +
                       "\"color\" " + textcol);
        }

        if(!idExists)      //If the node doesn't exist, create it
        {
          fout.println("  circle \"" + cindex + "-node\" (" + px + ", "
                       + py + ") radius " + r + " color " + textcol + 
                       " depth 1 filled fillcolor " + color);
          fout.println("  text \"" + cindex + "-text\" \"" + cindex + 
                       "\" (" + px + ", " + (py + 5) + ") centered color "
                       + textcol + " depth 0 font Serif size 12 bold");
          idExists = true;
        }
        else if(isChanged)  //Otherwise, the node exists & changed, jump!
        {
          fout.println("  jump \"" + cindex + "-node\" to (" + 
                        (px - r) + ", " + (py - r) + ")");
          fout.println("  jump \"" + cindex + "-text\" to (" + 
                        (px - 4) + ", " + (py + 4 - r) + ")");
        } //End checking node existance
      } //End checking for changes only

      //Finally... we know these variables are accounted for now
      isChanged = false;
      isColorChanged = false;
    }


    /**Function:  printXMLNode
     * Pre:  Takes a PrintWriter fout defining the file to send XML printing 
     *       out to and a boolean is_animation stating whether we are
     *       printing an animated node or simple a graph-node
     *       MOOF - add boolean for jump vs slide here in the future
     * Post: Prints out this node to the file appropriately            */
    public void printXMLNode(PrintWriter fout, boolean is_animation)
    {
      //First, check if the node is activated.
      //If not, hide the node (if shown) & leave for animations
      if(!activated)
        if(idExists && is_animation)  //remove now-inactivated visibles!
        {
          fout.println("\t<removevertex id = \"" + cindex + "\" />");
          idExists = false;
        }
        else;
      
      //Otherwise, if the ID already exists and it's an animation...
      else if(idExists && is_animation)
      {
        //If the position has changed, do a jump
        if(isChanged)
          fout.println("\t<jump id = \"" + cindex + "\" x = \"" + x +
                    "\" y = \"" + y + "\" />");

        //If the color has changed, adjust the color
        if(isColorChanged)
          fout.println("\t<colorvertex id = \"" + cindex + 
                        "\" color = \"" + color + "\" />");  
      }
      //Otherwise, the node doesn't exist and must be added
      else
      {
        //If this is an animation, we actually need to add the node if
        //   we get to this point
        if(is_animation) fout.print("\t<addvertex>\n\t");

        //No matter what, we need to define the vertex appropriately
        fout.print("\t<vertex id = \"" + cindex + "\" x = \"" + x +
                   "\" y = \"" + y + "\" color = \"" + color + 
                    "\" highlight = \"");
        if(highlighted) fout.println("true\" />");
            else fout.println("false\" />");

        // Make sure that idExists is true, the ID is certainly out there!
        idExists = true;

        //If this is an animation, we need to output the end-addvertex tag
        if(is_animation) fout.println("\t</addvertex>");
      } //End checking for changes only

      //Finally... we know these variables are accounted for now
      isChanged = false;
      isColorChanged = false;
    }

/*************************************************************************/
/**                   DRAW NODE FUNCTIONS (VISUAL)                      **/
/*************************************************************************/



    /**Function:  drawNode
     * Pre:  Takes a graphics object g to draw on.
     *       Also takes two ints (x_size, y_size) defining the screen size
     *       Also takes a node radius r                                  
     * Post: Prints out this node to the screen appropriately            */
    public void drawNode(Graphics g, int x_size, int y_size, int r)
    {
      //Don't print nodes that aren't activated
      if(!activated) return;

      //First, get some stats based on the Graphics area size
      //In particular, the size of the Node
      int px = (int) (x_size * x) - r;    //X & Y positions
      int py = (int) (y_size * y) - r;

      //Set the font to appropriate no matter what
      //int sizor;    //The size for the text, based on radius
      g.setFont(new Font("Serif", Font.BOLD, r));

      //Find the color for the text, it's used often... we'll do this later now
      //String textcol = getOtherTextColor(color);  MOOF!  UPDATE THIS LATER!
      //Make sure the color is correct no matter what... adjust also later!

      //Node insides... get this color correct later, moof!
      g.setColor(getMyJavaColor());
      //g.setColor(Color.white);
      g.fillOval(px, py, r * 2, r * 2);

      //Node border
      g.setColor(getJavaColor(getOtherNodeColor(color)));
      g.drawOval(px, py, r * 2, r * 2);

      //Text inside... set up to be centered
      py = py + r + (int) (g.getFontMetrics().getAscent() / 2.2);
      px = px + r - (g.getFontMetrics().stringWidth("" + cindex) / 2);
      g.drawString("" + cindex, px, py);
    }


/*************************************************************************/
/**                      NODE UTILITY FUNCTIONS                         **/
/*************************************************************************/


    /**Function:  isValidNodeColor
     * Pre:  Takes a string color c
     * Post: True if c is a valid color for nodes, false otherwise    */
    public boolean isValidNodeColor(String c)
    {
        return((c.compareTo("black") == 0) || (c.compareTo("white") == 0) ||
             (c.compareTo("gray"  ) == 0) || (c.compareTo("yellow") == 0) ||
             (c.compareTo("blue"  ) == 0) || (c.compareTo("blue2" ) == 0) ||
             (c.compareTo("blue3" ) == 0) || (c.compareTo("blue4" ) == 0) ||
             (c.compareTo("pink"  ) == 0) || (c.compareTo("pink2" ) == 0) ||
             (c.compareTo("pink3" ) == 0) || (c.compareTo("pink4" ) == 0) ||
             (c.compareTo("magenta" ) == 0) || (c.compareTo("red" ) == 0) ||
             (c.compareTo("magenta2") == 0) || (c.compareTo("red2") == 0) ||
             (c.compareTo("magenta3") == 0) || (c.compareTo("red3") == 0) ||
             (c.compareTo("magenta4") == 0) || (c.compareTo("red4") == 0) ||
             (c.compareTo("green"  ) == 0) || (c.compareTo("cyan" ) == 0) ||
             (c.compareTo("green2" ) == 0) || (c.compareTo("cyan2") == 0) ||
             (c.compareTo("green3" ) == 0) || (c.compareTo("cyan3") == 0) ||
             (c.compareTo("green4" ) == 0) || (c.compareTo("cyan4") == 0));
    }


    /**Function: getOtherNodeColor
     * Pre:  Takes an ANIMAL color string c
     * Post: Returns the 'node-opposite' color string for that color   */
    public String getOtherNodeColor(String c)
    {
        if(!isValidNodeColor(c)) return "white";

        String temp = "white";        //Use for the numbered colors

        if(c.compareTo("black") == 0) return "white";
        if(c.compareTo("white") == 0) return "black";
        if(c.compareTo("gray") == 0) return "yellow";
        if(c.compareTo("yellow") == 0) return "gray";
        if(c.startsWith("blue")) temp = "cyan";
        if(c.startsWith("cyan")) temp = "blue";
        if(c.startsWith("pink")) temp = "red";
        if(c.startsWith("red")) temp = "pink";
        if(c.startsWith("magenta")) temp = "green";
        if(c.startsWith("green")) temp = "magenta";

        //Do special cases for the numbered colors
        if(c.endsWith("2")) return temp + "3";
        if(c.endsWith("3")) return temp + "2";
        if(c.endsWith("4")) return temp;

        return temp + "4";
    }

    /**Function: getMyJavaColor
     * Pre: Nothing
     * Post: Returns the java color for this node                    
     *       (Note:  returns "white" as a default)                   */
    public Color getMyJavaColor()
    {  return getJavaColor(color);   }

    /**Function: getJavaColor
     * Pre:  Takes an ANIMAL color string 'c'
     * Post: Returns the java color for this node                    
     *       (Note:  returns "white" as a default)                   */
    private Color getJavaColor(String c)
    {
        if(!isValidNodeColor(c)) return Color.white;

        //Temporary color to use with numbered colors
        Color temp = Color.white;

        if(c.compareTo("black") == 0) return Color.black;
        if(c.compareTo("white") == 0) return Color.white;
        if(c.compareTo("gray") == 0) return Color.gray;
        if(c.compareTo("yellow") == 0) return Color.yellow;
        if(c.startsWith("blue")) temp = Color.blue;
        if(c.startsWith("cyan")) temp = Color.cyan;
        if(c.startsWith("pink")) temp = Color.pink;
        if(c.startsWith("red")) temp = Color.red;
        if(c.startsWith("magenta")) temp = Color.magenta;
        if(c.startsWith("green")) temp = Color.green;

        //Finally, do some final handling for 
        temp = temp.darker().darker();
        if(c.endsWith("2")) return temp.brighter();
        if(c.endsWith("3")) return temp.brighter().brighter();
        if(c.endsWith("4")) return temp.brighter().brighter().brighter();

        return temp; 
    }

    /**Function: getOtherTextColor
     * Pre:  Takes an ANIMAL color string c
     * Post: Returns the text color string for that color for a node  
     *       (Possibilities are white and black only)                */
    public String getOtherTextColor(String c)
    {
        if(!isValidNodeColor(c)) return "black";

        if((c.compareTo("white") == 0) || 
           (c.compareTo("yellow") == 0) ||
           (c.startsWith("pink")))
            return "black";
        else return "white";
    }


    /**Function:  translatePixel
     * Pre:  Takes a screen_size for an axis, a radius r, 
     *       and a position on an axis pos that is in [0, 1]
     * Post: Returns the location on the screen-axis where the point pos
     *       should go, considering the radius shouldn't get mussed up  */
    private int translatePixel(int screen_size, int r, double pos)
    {
      //Cut off edges by radius, in case circles/etc go over the edges
      screen_size -= 6 * r;

      //Place the pixel within the edging
      return (int) (pos * screen_size) + 3 * r;
    }
}
