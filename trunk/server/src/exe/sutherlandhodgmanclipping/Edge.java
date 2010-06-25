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

package exe.sutherlandhodgmanclipping;

import java.awt.*;
import java.awt.geom.Line2D;

public class Edge {
  protected Point p1;
  protected Point p2;
  
  public Edge(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
  }
  
  public Point computeIntersection(Point s, Point p) {
    double edgex = p1.getX() - p2.getX();
    double linex = s.getX() - p.getX();
    
    double m1 = (s.getY() - p.getY()) / (s.getX() - p.getX());
    double m2 = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
    double b1 = s.getY() - m1 * s.getX();
    double b2 = p1.getY() - m2 * p1.getX();
    
    double x = edgex == 0.0f ? p1.getX() : 
      linex == 0.0f ? p.getX() : (b2 - b1) / (m1 - m2);
    double y = m1 * x + b1;
    
    return new Point(x,y,0);
  }
  
  public static boolean isPointInsideEdge(Edge e, Point ref, Point p) {
    boolean ret = true;
    Vector ve = e.p2.sub(e.p1);
    Vector vr = ref.sub(e.p1);
    Vector vp = p.sub(e.p1);
    
    Vector A = ve.cross(vr);
    Vector B = ve.cross(vp);
   
    ret = (A.getZ() < 0 && B.getZ() < 0) || (A.getZ() > 0 && B.getZ() > 0);
    return ret;
  }
  
  public Point getP1() {
    return p1;
  }
  
  public Point getP2() {
    return p2;
  }
  
  public Point generatePointOnEdge(double t) {
    return p1.add(p2.mul(t));
  }
}
