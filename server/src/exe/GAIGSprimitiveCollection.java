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

package exe;
import java.util.*;
import java.lang.*;

/**
 * <p><code>GAIGSprimitiveCollection</code> provides the ability to draw 2D graphics for use in visualizations.
 * This class supports a variety of 2D graphics primitives, including lines, polygons, circles, ellipises, etc.
 * Creation of the primitives adheres to the other GAIGS classes and colors are specified with the standard color string.
 * </p>
 *  
 * @author Shawn Recker 
 * @version 6/22/2010
 */

public class GAIGSprimitiveCollection implements GAIGSdatastr {
  
  protected interface Primitive { }
  
  protected class Circle implements Primitive {
    public double x;
    public double y;
    public double r;
    public String fcolor;
    public String ocolor;
    public String lcolor;
    public String label;
    
    public Circle(double cx, double cy, double r, String fcolor, String ocolor, String lcolor, String label) {
      this.x = cx;
      this.y = cy;
      this.r = r;
      this.fcolor = fcolor;
      this.ocolor = ocolor;
      this.lcolor = lcolor;
      this.label = label;
    }
  }
  
  protected class Polygon implements Primitive {
    public int nSides;
    public double [] ptsX;
    public double [] ptsY;
    public String fcolor;
    public String ocolor;
    public String lcolor;
    public String label;
	
	public Polygon (int nSides , double ptsX [], double ptsY [], String fcolor, String ocolor, String lcolor, String label){
      this.nSides = nSides;
	  this.ptsX = new double[nSides];
	  this.ptsY = new double[nSides];
	  for(int i=0;i<nSides;++i)
	  {
		this.ptsX[i]=ptsX[i];
		this.ptsY[i]=ptsY[i];
	  }
      this.fcolor = fcolor;
      this.ocolor = ocolor;
      this.lcolor = lcolor;
      this.label = label;
    }
  }
  
  protected class StraightLine implements Primitive {
    public double x[] = new double[2];
    public double y[] = new double[2];
    public String color;
    public String lcolor;
    public String label;
	
    public StraightLine(double x[], double y[], String color, String lcolor, String label){
      for(int i=0;i<2;++i)
      {
        this.x[i]=x[i];
        this.y[i]=y[i];
      }
      this.color = color;
      this.lcolor = lcolor;
      this.label = label;
    }
  }
  
  protected class Ellipse implements Primitive {
    public double x;
    public double y;
    public double stAngle;
    public double endAngle;
    public double xR;
    public double yR;
    public String color;
    public String lcolor;
    public String label;
	
    public Ellipse(double x, double y, double stAngle, double endAngle, double xR,
      double yR, String color, String lcolor, String label){
      this.x = x;
      this.y = y;
      this.stAngle = stAngle;
      this.endAngle = endAngle;
      this.xR = xR;
      this.yR = yR;
      this.color = color;
      this.lcolor = lcolor;
      this.label = label;
    }
  }
  
  /**
   * The Current collection of graphical primitives
  */
  protected ArrayList<Primitive> primitives;
  
  /**
   * The Name of the collection of graphical primitives
  */
  protected String name;
  
  /**
   * Creates an empty primitive collection with no name
  */
  public GAIGSprimitiveCollection() {
    primitives = new ArrayList<Primitive>();
	  name = "";
  }
  
  /**
   * Creates an empty primitive collection with the specified name
  */
  public GAIGSprimitiveCollection(String name) {
    primitives = new ArrayList<Primitive>();
    this.name = name;
  }
  
  /**
   * Sets the name of the primitive collection
   * @param name  The name of the collection
  */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Returns the name of the primitive collection
   * @return The name of the primitive collection
  */
  public String getName() {
    return name;
  }
  
  /**
   * Creates and Returns the GAIGS XML code for the current state of the primitive collection
   * @return A String containing GAIGS XML code for the primitive collection
  */
  public String toXML() {
    String xml;
    xml = "<primitivecollection>\n\t<name>" + name +
      "</name>\n\t<bounds x1=\"0.0\" y1=\"0.0\" x2=\"1.0\" y2=\"1.0\"/>\n\t";
    
    for(int i = 0; i < primitives.size(); ++i) {
      Primitive p = primitives.get(i);
      
      if(p instanceof Circle) {
        Circle c = (Circle)p;
        xml += "\t<circle x=\"" + c.x + "\" y=\"" + c.y + "\" " +
          "r=\"" + c.r + "\" fcolor=\"" + c.fcolor + "\" " +
          "ocolor=\"" + c.ocolor + "\" text=\"" + c.label + "\" lcolor=\"" + c.lcolor + "\"/>\n";
      }
	  
      if(p instanceof Polygon) {
        Polygon pl = (Polygon)p;
        xml += "<polygon nSides=\"" + pl.nSides; 
        for(int j=0; j<pl.nSides ; ++j) {
          xml += "\" ptsX"+ j + "=\"" + pl.ptsX[j] + "\" ptsY"+ j + "=\"" + pl.ptsY[j];
        }
        xml += "\" fcolor=\"" + pl.fcolor + "\" " +
          "ocolor=\"" + pl.ocolor + "\" text=\"" + pl.label + "\" lcolor=\"" + pl.lcolor + "\"/>\n";
      }
    
      if(p instanceof StraightLine) {
        StraightLine l = (StraightLine)p;
        xml += "<polygon nSides=\"" + 2; 
        for(int j=0; j<2 ; ++j) {
          xml += "\" ptsX"+ j + "=\"" + l.x[j] + "\" ptsY"+ j + "=\"" + l.y[j];
        }
        xml += "\" fcolor=\"" + l.color + "\" " +
              "ocolor=\"" + l.color + "\" text=\"" + l.label + "\" lcolor=\"" + l.lcolor + "\"/>\n";
      }
      
      if(p instanceof Ellipse) {
        Ellipse e = (Ellipse)p;
        xml += "\t<ellipse x=\"" + e.x + "\" y=\"" + e.y + "\" " +
          "sa=\"" + e.stAngle + "\" ea=\"" + e.endAngle + "\" rx=\"" + e.xR + "\" ry=\"" + e.yR + "\" color=\"" + e.color + "\" " +
          "text=\"" + e.label + "\" lcolor=\"" + e.lcolor + "\"/>\n";
      }
    }
    xml += "</primitivecollection>\n";
    return xml;
  }
  
  /**
   * Removes all primitives from the collection
  */
  public void clearPrimitives() {
    primitives.clear();
  }
  
  /**
   * Adds a circle to the primitive collection
   * @param cx The center x coordinate of the circle
   * @param cy The center y coordinate of the circle
   * @param r The radius of the circle
   * @param fillColor The internal color of the circle
   * @param outlineColor The color of the circle outline
   * @param labelColor The color of the text in the circle label
   * @param labelText The text to be drawn in the center of the circle
  */
  public void addCircle(double cx, double cy, double r, String fillColor, String outlineColor,
    String labelColor, String labelText)
  {
    primitives.add(new Circle(cx, cy, r, fillColor, outlineColor, labelColor, labelText));
  }
  
  /**
   * Adds a polygon to the primitive collection
   * @param nSides The number of sides to the polygon
   * @param ptsX Array containing the x coordinate values for the polygon
   * @param otsY Array containing the y coordinate values for the polygon
   * @param fillColor The internal color of the polygon
   * @param outlineColor The color of the circle polygon
   * @param labelColor The color of the text in the circle label
   * @param labelText The text to be drawn in the center of the circle
  */
  public void addPolygon(int nSides, double ptsX[], double ptsY[], String fillColor, String outlineColor,
    String labelColor, String labelText)
  {
    primitives.add(new Polygon(nSides, ptsX, ptsY, fillColor, outlineColor, labelColor, labelText));
  }
  
  /**
   * Adds a line to the primitive collection
   * @param x Array of 2 containing the x coordinates for the start point and end point
   * @param y Array of 2 containing the y coordinates for the start point and end point
   * @param color The color of the line
   * @param lcolor The color of the text in the label
   * @param label The text to printed near the line
  */
  public void addLine(double x[], double y[], String color, String lcolor, String label)
  {
    primitives.add(new StraightLine(x, y, color,lcolor, label));
  }
  
  /**
   * Adds an ellipse to the primitive collection.  Does not support a filled ellipse.
   * @param x The lower right hand x coordinate of the ellipse bounds
   * @param y The lower right hand y coordinate of the ellipse bounds
   * @param stAngle The starting angle in radians of the ellipse
   * @param endAngle The ending angle in radians of the ellipse
   * @param xR The radius value along the x axis
   * @param yR The radius value along the y axis
   * @param color The color of the outline of the ellipse
   * @param lcolor The color of the text in the label
   * @param label The text for the label to appear in the center of the ellipse
  */
  public void addEllipse(double x, double y, double stAngle, double endAngle, double xR,
    double yR, String color, String lcolor, String label)
  {
    primitives.add(new Ellipse(x,y,stAngle,endAngle,xR,yR,color,lcolor,label));
  }
}

