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

package exe.kruskalrichard;

import java.io.*;   //Now we can use simple names for all classes in java.io
                    //      Notably, the PrintWriter class
import java.awt.*;          //For graphics
import java.awt.event.*;    //For event listeners
import java.util.regex.*;

/**
 * A class to store information about edges in a visual graph
 *   Such as color, highlighting, weight, and positioning
 *
 * @author Jeff Lucas
 * @version April 29th, 2003
 */

/**
 *  NOTE: Colors are represented by JAVA-style color strings. (28 options)
 *        The valid colors include black, blue, blue2, blue3, blue4, white
 *        brown2, brown3, brown4, cyan, cyan2, cyan3, cyan4, gray, green,
 *        green2, green3, green4, magenta, magenta2, magenta3, magenta4, 
 *        pink, pink2, pink3, pink4, red, red2, red3, red4, white, & yellow
 *  NOTE2: Each color has an "edge opposite color."  When an edge is
 *         highlighted, it is set to its edge opposite color            */


public class VisEdge
{
    private String color;            //Stores the color of the edge
    private boolean highlighted;     //Is the edge highlighted or not?
    private boolean activated;       //Is the edge activated?
    private double weight;           //Stores the weight of this edge
    private double sx, sy;           //The start point for the edge
    private double ex, ey;           //The end point for the edge
                                     //(Note: points in Cartesian coordinate
                                     // on a normalized [0,1] grid)
                                     //(Note2:  If (sx == ex) and (sy == ey)
                                     // then the edge is a self-loop)

    //  utility variables... these are used ONLY by this class
    private boolean idExists;        //This becomes true when an ANIMAL ID
                                     //     exists for this particular edge
    private boolean isColorChanged;  //This is true when edge color changes
    private boolean isChanged;       //This is true when the edge has been
                                     //     moved or a weight changed

    private String hexColor;         //Stores the color hex of the edge (gaigs adaptation)

    /** Constructor for objects of class VisEdge 
     *Pre:  Takes nothing
     *Post: Initializes all the objects within an edge */
    public VisEdge()
    {
        // Initialize all variables
        color = "black";        //Default color is black
        hexColor = "#999999";        //Default color is grey
        sx = 0.5;    sy = 0.5;  //Default edge is a self-loop at the center
        ex = 0.5;    ey = 0.5;
        weight = 0;             //Default weight is zero
        highlighted = false;    //Highlighting is off by default
        activated = false;      //Not activated until you say so
        idExists = false;       //No ANIMAL ID has been created yet.
        isColorChanged = false; //No color changes yet
        isChanged = true;       //We want to print the first time
    }



    /**  Constructor that is given beginning values
     *Pre:  Takes values for all the attributes of an edge
     *Post: Initializes the edge with the attributes given */
    public VisEdge(String c, double my_weight, double my_sx, double my_sy, 
                   double my_ex, double my_ey, boolean my_active, 
                   boolean my_hlight)
    {
        // Set all variables
        if(!isValidEdgeColor(c)) color = "black";
            else color.copyValueOf(c.toCharArray());
        weight = my_weight;
        sx = my_sx;    sy = my_sy;
        ex = my_ex;    ey = my_ey;
        highlighted = my_hlight;    
        activated = my_active;
        idExists = false;
        isColorChanged = false;
        isChanged= true;
        hexColor = "#999999";        //Default color is grey
    }



    /**Function clearEdge
     * Pre:  Nothing     
     * Post:  Sets Edge's values to the defaults */
    public void clearEdge()
    {
        // Initialize all variables to the defaults
        color = "black";        //Default color is black
        sx = 0.5;   sy = 0.5;   //Default edge is a self-loop at the center
        ex = 0.5;   ey = 0.5;
        weight = 0;             //Default weight is zero
        highlighted = false;    //Highlighting is off by default
        isColorChanged = false; //Unused edge, color can't be changed
        activated = false;      //Not activated until you say so
        isChanged = true;       //We want it to print the next time
    }

/*************************************************************************/
/**                         QUERY NODE VALUES                           **/
/*************************************************************************/

    public String getColor() { return color; }     //Get edge's color
    public String getHexColor() { return hexColor; }     //Get edge's color
    public double getSX()    { return sx; }        //Get edge's start-x
    public double getSY()    { return sy; }        //Get edge's start-y
    public double getEX()    { return ex; }        //Get edge's end-x
    public double getEY()    { return ey; }        //Get edge's end-y
    public double getWeight(){ return weight; }    //Get edge's weight
    public boolean isHighlighted() { return highlighted; }      //Highlight?
    public boolean isActivated()   { return activated;   }      //Activated?

/*************************************************************************/
/**                         SET EDGE VALUES                             **/
/*************************************************************************/

    public void setColor(String c)                    //Set edge color
    {   if(!isValidEdgeColor(c)) color = "black";
            else color = c;   
        isColorChanged = true;
    }

    public void setHexColor(String c)                    //Set edge color
    {
	    //Pattern p = new Pattern.compile("#[0-9a-fA-F]{6}");
	    //Matcher m = new p.Matcher(c);

	    if( Pattern.compile("#[0-9a-fA-F]{6}").matcher(c).matches() )
		    hexColor = c;
    }
    
    /**These functions set the starting point (my_sx, my_sy) and the
     * ending point (my_ex, my_ey) for the edge; or, the weight of the edge.
     * If any of these are used, isChanged it set to true               */
    public void setSX(double m_sx) 
    { if(m_sx != sx) isChanged = true;  sx = m_sx; }
    public void setSY(double m_sy) 
    { if(m_sy != sy) isChanged = true;  sy = m_sy; }
    public void setEX(double m_ex) 
    { if(m_ex != ex) isChanged = true;  ex = m_ex; }
    public void setEY(double m_ey) 
    { if(m_ey != ey) isChanged = true;  ey = m_ey; }
    public void setWeight(double my_w)
    { if(my_w != weight) isChanged = true; weight = my_w; } 

    // Set Highlight -- adjust highlighting color appropriately
    public void setHighlighted()   { if(!highlighted) toggleHighlighted(); }
    public void setUnhighlighted() { if( highlighted) toggleHighlighted(); }
    public void toggleHighlighted()
    { 
        color.copyValueOf(getOtherEdgeColor(color).toCharArray());
        isColorChanged = true;
        highlighted = !highlighted;
    }

    //Set activation
    public void activate()          {   activated = true;  }  // Activate
    public void deactivate()        {   activated = false; }  // Deactivate

/*************************************************************************/
/**                        PRINT EDGE FUNCTIONS                         **/
/*************************************************************************/



    /**Function:  printEdge
     * <p>Pre:  Takes a PrintWriter fout and two ints (x,y) defining
     *       the file to send ANIMAL printing out to and the screen size
     *       Also, takes an appropriate radius for each node r
     *       Also, takes a string to use to ID the edge
     *       Also takes booleans telling whether the graph this edge is in
     *          is directed, and whether the edge is weighted
     * <p>Post: Prints out this edge to the file appropriately            */
    public void printEdge(PrintWriter fout, int x_size, int y_size, int r,
                            String id, boolean weighted, boolean directed)
    {
      //First, check if the edge is activated.  
      //If not, hide the edge (if shown) & leave right away
      if(!activated)
        if(idExists) //Hide now-inactivated visibles!
        {
           fout.println("  hide \"" + id + "-edge\"");
           if(weighted) fout.println(" \"" + id + "-weight\"");
             else fout.println();
           idExists = false;
        }else;
      //Check if edge was changed (moved)...  If not, just leave.
      else if(!isChanged)
        //Make sure the color is correct
        if(isColorChanged) 
        {
          fout.print("  color \"" + id + "-edge\" ");
          if(weighted) fout.print("\"" + id + "-weight\" ");
          fout.println(color);
        }else;
      else    //Otherwise, the edge was changed!  Draw it!
      {
        int psx, psy, pex, pey;      //The printed edge endpoints
        double theta = getAngle();   //Stores angle where there's a slope

        //If it's the first time printing the edge, just print it
        //  and set the idExists to true
        //  Otherwise, hide it first and then print it.
        if(idExists)
        {
          fout.print("  hide \"" + id + "-edge\"");
          if(weighted) fout.println(" \"" + id + "-weight\"");
            else fout.println();
        } else idExists = true;

        //Determine the starting point (behind/along the node)
        psx = translatePixel(x_size, r, sx);
        psy = translatePixel(y_size, r, sy);

        //If not a self-loop, we've got a line, maybe with an arrow
        if((sx != ex) || (ey != sy))
        {
          //Determine the ending point (behind the node)
          pex = translatePixel(x_size, r, ex);
          pey = translatePixel(y_size, r, ey);

          //If the graph is directed, we need to see those arrows!
          if(directed)
          {
            //Use the angle the edge to place arrow on the node's rim
            pex -= Math.cos(theta) * r;
            pey += Math.sin(theta) * r;
          }

          //Output the edge...
          fout.print("  line \"" + id + "-edge\" (" + psx + ", " + psy +
                        ") (" + pex + ", " + pey + ") color " + color + 
                        " depth 2");
          if(directed) fout.println(" fwArrow"); else fout.println();

          //If the edge is weighted, print a weight at 1/4 the way down
          if(weighted)
          {
            double theta2 = theta + Math.PI / 24;
            double dist = Math.sqrt(Math.pow(pex - psx, 2) + 
                                    Math.pow(pey - psy, 2));

            psx += Math.cos(theta2) * (r + dist / 6);
            psy -= Math.sin(theta2) * (r + dist / 6);
            
            //Print integer weights when possible... print weight!
            if((int) weight == weight)
                fout.print("  text \"" + id + "-weight\" \"" + (int) weight);
            else
                fout.print("  text \"" + id + "-weight\" \"" + weight);
            fout.println("\" (" + psx + ", " +  psy + ") centered color " + 
                         color + " depth 4 font Serif size 12 bold");
          }
        }  //End partition for drawing non-self-loops
        //Otherwise, we have a self-loop.  Print a circle with an arrow
        else
        {
          //Place the self-loop based on the angle
          psx -= Math.cos(theta) * r * 1.5;
          psy += Math.sin(theta) * r * 1.5;
          pex = (int) (psx - Math.cos(theta) * r / 2);
          pey = (int) (psy + Math.sin(theta) * r / 2);

          fout.println("  arc \"" + id +"-edge\" (" + psx + ", " + psy + 
                      ") radius (" + (r + 2) + ", " + (r + 2) + ") angle " +
                      "359 starts " + (int) ((theta - Math.PI / 4) * 
                      180 / Math.PI) + " counterclockwise color " + color + 
                      " depth 2 fwArrow");

          //Weighted self-loops need weight labels!
          if(weighted)
              fout.println("  text \"" + id + "-weight\" \"" + weight + 
                          "\" (" + pex + ", " +  pey + ") centered color " +
                          color + " depth 4 font Serif size 12 bold");
        }//End self-loop portion
      }//End print-new-position portion 

      //Finally... we know these variables have been accounted for
      isChanged = false;
      isColorChanged = false;
    }


    /**Function:  printXMLEdge
     * <p>Pre:  Takes a PrintWriter fout defining the file/stream to send 
     *          XML printing out to
     *       Also, takes a boolean is_animation that tells whether
     *             this output is for an animation slide or not
     *       Also, takes two string ID's, the ID's of the nodes
     *             incident to the edge (s_id and e_id for start/end nodes)
     * <p>Post: Prints out this edge to the file appropriately as XML     */
    public void printXMLEdge(PrintWriter fout, boolean is_animation,
                             String s_id, String e_id)
    {
      //First, check if the edge is activated.  
      //If not, hide the edge (if shown) & leave right away
      if(!activated)
        // Hide now-inactivated visibles for animations! (remove edge)
        if(idExists && is_animation) 
        {
           fout.println("\t<removeedge start_id = \"" + s_id + 
                        "\" end_id = \"" + e_id + "\" />");
           idExists = false;
        }else;
      //Check if edge was changed (moved)...  If not, just leave.
      else if(!isChanged && is_animation)
        //If the color is incorrect, do a change-color event
        if(isColorChanged) 
          fout.println("\t<coloredge start_id = \"" + s_id +
                       "\" end_id = \"" + e_id + "\" color = \"" +
                       color + "\" />");
        else;
      else    //Otherwise, the edge was changed!  Draw it!
      {
        //If it's the first time printing the edge, just print it
        //  and set the idExists to true
        //  Otherwise, remove it first and then re-add it.
        //  We do this because most animation programs can't think it
        //     out themselves.
        if(idExists && is_animation)
          fout.println("\t<removeedge start_id = \"" + s_id + 
                       "\" end_id = \"" + e_id + "\" />");
        else idExists = true;

        //Write the edge out in XML form
        //If the edge is animated currently, we must add it back in
        if(is_animation) fout.print("\t<addedge>\n\t");

        //Print out the edge definition
        fout.print("\t<edge start_id = \"" + s_id + "\" end_id = \"" +
                        e_id + "\" color = \"" + color + 
                        "\" highlight = \"");
        if(highlighted) fout.print("true"); else fout.print("false");
        fout.println("\" weight = \"" + weight + "\" />");

        //If the edge is animated currently, end the add-edge tag
        if(is_animation) fout.println("\t</addedge>");
      }

      //Finally... we know these variables have been accounted for
      isChanged = false;
      isColorChanged = false;
    }


/*************************************************************************/
/**                   EDGE DISPLAY FUNCTIONS (VISUAL)                   **/
/*************************************************************************/

    /**Function:  drawEdge
     * <p>Pre:  Takes a Graphics object to draw an edge to
     *          Also, takes (x_size, y_size) the size of the graphics area
     *          Also takes booleans telling whether the graph this edge is in
     *              is directed, and whether the edge is weighted
     *          Also takes a radius for nodes r
     * <p>Post: Prints out this edge to the screen appropriately            */
    public void drawEdge(Graphics g, int x_size, int y_size, boolean weighted, 
                            boolean directed, int r)
    {
      //Check that the edge is activated.  If so, draw it!
      //If the edge isn't activated, then don't draw it silly!
      if(!activated) return;

      //First, get some stats based on the Graphics area size
      //In particular, the size of the Node
      int psx, psy, pex, pey;           //The main edge endpoints
      double theta = getAngle();        //Stores angle where there's a slope

      //Set the font if it's weighted
      if(weighted) g.setFont(new Font("Serif", Font.BOLD, 12));

      //Determine the starting point (behind/along the node)
      psx = (int) (x_size * sx);
      psy = (int) (y_size * sy);

      //If not a self-loop, we've got a line, maybe with an arrow
      if((sx != ex) || (ey != sy))
      {
        //Determine the ending point (behind the node)
        pex = (int) (x_size * ex);
        pey = (int) (y_size * ey);

        //If the graph is directed, we need to see those arrows!
        if(directed)
        {
          //Use the angle the edge to place arrow on the node's rim
          pex -= Math.cos(theta) * r;
          pey += Math.sin(theta) * r;

          //Use the angle to actually MAKE an arrow
          double avg = (x_size + y_size) / 2;   //Average screen size, useful
          for(double i = -1; i <= 1; i += 1 / avg)
            g.drawLine(pex, pey, 
                (int) (pex - Math.cos(theta + (i * Math.PI / 8)) * r * 0.75 * 
                    x_size / avg * ((i * i) / 4 + 0.75)),
                (int) (pey + Math.sin(theta + (i * Math.PI / 8)) * r * 0.75 * 
                    y_size / avg * ((i * i) / 4 + 0.75)) );
        }

        //Output the edge...a single line for now, thicken later?
        g.drawLine(psx, psy, pex, pey);

        //If the edge is weighted, print a weight part way down
        if(weighted)
        {
          double theta2 = theta + Math.PI / 24;
          double dist = Math.sqrt(Math.pow(pex - psx, 2) + 
                                  Math.pow(pey - psy, 2));

          psx += Math.cos(theta2) * (r + dist / 6);
          psy -= Math.sin(theta2) * (r + dist / 6);
        }
      }  //End partition for drawing non-self-loops
      //Otherwise, we have a self-loop.  Print a circle with an arrow
      else
      {
        //Place the self-loop based on the angle
        int psx2 = (int) (psx - Math.cos(theta) * 1.5 * r - r);
        int psy2 = (int) (psy + Math.sin(theta) * 1.5 * r - r);

        g.drawArc(psx2, psy2, (int) (r * 2), (int) (r * 2),
                (int) ((theta - Math.PI / 4) * 180 / Math.PI), 359);

        //Weighted self-loops need weight labels!
        if(weighted)
        {
            psx2 = (int) (psx2 - Math.cos(theta) * r / 2);
            psy2 = (int) (psy2 + Math.sin(theta) * r / 2);
        }

        //Make an arrow for the self-loop (get new theta, psx, and psy)
        theta -= Math.PI / 5;
        psx = (int) (psx - Math.cos(theta - Math.PI / 32) * r);
        psy = (int) (psy + Math.sin(theta - Math.PI / 32) * r);
        theta += Math.PI / 16;
        double avg = (x_size + y_size) / 2;   //Average screen size, useful
        for(double i = -1; i <= 1; i += 1 / avg)
          g.drawLine(psx, psy, 
              (int) (psx - Math.cos(theta + (i * Math.PI / 8)) * r * 0.75 * 
                  x_size / avg * ((i * i) / 4 + 0.75)),
              (int) (psy + Math.sin(theta + (i * Math.PI / 8)) * r * 0.75 * 
                  y_size / avg * ((i * i) / 4 + 0.75)) );
      }//End self-loop portion

      //Draw the weights in the appropriate spot if necessary
      if(weighted)
       //Print integer weights when possible... print weight!
       if((int) weight == weight)
         g.drawString("" + (int) weight, psx, psy);
       else
         g.drawString("" + weight, psx, psy);
    } //End draw-edge function

/*************************************************************************/
/**                       EDGE UTILITY FUNCTIONS                        **/
/*************************************************************************/

    /**Function:  slopeExists
     * Pre:  Nothing
     * Post: Returns true if this edge has a slope (not a vertical line) */
    public boolean slopeExists()
    { return sy != ey; }


    /**Function:  getSlope
     * Pre:  Nothing
     * Post: Returns the slope of this edge if the edge has a slope;
     *       Otherwise, throws a runtime exception                       */
    public double getSlope()
    {
        if(!slopeExists())
            throw new RuntimeException("ERROR: There is no slope!");

        return (ex - sx) / (ey - sy);
    }


    /**Function:  getAngle
     * Pre:  Nothing
     * Post: Returns the edge angle in radians (away from 0) if (sx, sy) !=
     *       (ex, ey); else determine the angle of the point from a center
     *       at (0.5, 0.5)                                               */
    public double getAngle()
    {
        //If starting point != ending point, find real angle
        if((sx != ex) || (sy != ey))
        {
          if(!slopeExists())
            if(ex > sx) return 0;              //Straight left = 0
              else return Math.PI;             //Straight right = PI
          else
            if(getSlope() != 0)                //Determined by atan
              if(ex > sx) return Math.atan(-1 / getSlope());  
                else return Math.PI + Math.atan(-1 / getSlope());
            else 
              if(ey < sy) return Math.PI / 2;  //Straight up = PI / 2
                else return 3 * Math.PI / 2;   //Straight down = 3 * PI / 2
        }
        else    //Otherwise, pretend (0.5, 0.5) is an end-point
        {
          if(ey == 0.5)
            if(ex > 0.5) return Math.PI;       //Straight left = 0
              else return 0;                   //Straight right = PI
          else
            if(((ex - 0.5) / (ey - 0.5)) != 0) //Determine with atan
              if(ex < 0.5) return Math.atan((0.5 - ey) / (ex - 0.5));  
                else return Math.PI + Math.atan((0.5 - ey) / (ex - 0.5));
            else 
              if(ey > 0.5) return Math.PI / 2; //Straight up = PI / 2
                else return 3 * Math.PI / 2;   //Straight down = 3 * PI / 2
        }
    }



    /**Function:  isValidEdgeColor
     * Pre:  Takes a string color c
     * Post: True if c is a valid color for edges, false otherwise    */
    public boolean isValidEdgeColor(String c)
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



    /**Function: getOtherEdgeColor
     * Pre:  Takes an ANIMAL color string c
     * Post: Returns the 'edge-opposite' color string for that color   */
    public String getOtherEdgeColor(String c)
    {
        if(!isValidEdgeColor(c)) return "black";

        if(c.compareTo("black") == 0) return "gray";
        if(c.compareTo("gray") == 0) return "black";
        if(c.compareTo("white") == 0) return "yellow";
        if(c.compareTo("yellow") == 0) return "white";
        if(c.startsWith("blue")) 
            return "pink" + c.toCharArray()[c.length() - 1];
        if(c.startsWith("pink")) 
            return "blue" + c.toCharArray()[c.length() - 1];
        if(c.startsWith("cyan")) 
            return "red" + c.toCharArray()[c.length() - 1];
        if(c.startsWith("red")) 
            return "cyan" + c.toCharArray()[c.length() - 1];
        if(c.startsWith("magenta")) 
            return "green" + c.toCharArray()[c.length() - 1];
        if(c.startsWith("green")) 
            return "magenta" + c.toCharArray()[c.length() - 1];

        return "black";
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
