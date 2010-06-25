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

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

public abstract class Exercise {

  protected ShowFile show;
  protected PolygonToBeClipped sp;
  protected ClippingPolygon cp;
  protected GAIGSprimitiveCollection pc;
  protected ArrayList<Point> input;
  protected ArrayList<Edge> edges;
  protected PseudoCodeDisplay pseudo;
  protected String stack;
  
  protected static GAIGStext legend = new GAIGStext(0.0, 0.0, "S ->\nP ->");

  public Exercise(ShowFile show, PolygonToBeClipped sp, ClippingPolygon cp, GAIGSprimitiveCollection pc) {
    this.show = show;
    this.sp = sp;
    this.cp = cp;
    this.pc = pc;
    
    input = sp.getVertices();
    edges = cp.getEdges();
  }

  public abstract void generateVisualization() throws IOException;

  public int determineCaseNum(Edge e, Point r, Point s, Point p) {
    if(Edge.isPointInsideEdge(e,r,p)) {
      if(Edge.isPointInsideEdge(e,r,s)) {
        sp.setStartText("in");
        sp.setEndText("in");
        return 1;
      }
      sp.setStartText("out");
      sp.setEndText("in");
      return 4;
    } else {
      if(Edge.isPointInsideEdge(e,r,s)) {
        sp.setStartText("in");
        sp.setEndText("out");
        return 2;
      }
      sp.setStartText("out");
      sp.setEndText("out");
      return 3;
    }
  }

  public int countNewVerticesCreated(Edge e, ArrayList<Point> input, ArrayList<Edge> edges) {
    int count = 0;
    int i = edges.indexOf(e);
    Point s = input.get(input.size() - 1);
    Point r = edges.get( (i + 2) % edges.size()).getP1();

    for(int j = 0; j < input.size(); ++j) {
      Point p = input.get(j);
      int casenum = determineCaseNum(e, r, s, p);
      count += casenum == 2 || casenum == 4 ? 1 : 0;
      s = p;
    }
    return count;
  }

  public int countEliminatedVertices(Edge e, ArrayList<Point> input, ArrayList<Edge> edges) {
    int count = 0;
    int i = edges.indexOf(e);

    Point r = edges.get( (i + 2) % edges.size()).getP1();

    for(int j = 0; j < input.size(); ++j) {
      Point t = input.get(j);
      count += (!Edge.isPointInsideEdge(e, r, t)) ? 1 : 0;
    }

    return count;
  }
  
  public String make_uri(int s, int p, int caseNum, String stack, String output,
    int[] lines, PseudoCodeDisplay pseudo)
  {
    int[] colors = new int[lines.length];
    for(int i = 0; i < colors.length; ++i) {
      colors[i] = PseudoCodeDisplay.BLUE;
    }

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("stack2", stack);
    map.put("s", s == -1 ? "null" : "V"+s);
    map.put("p", p == -1 ? "null" : "V"+p);
    map.put("casenum", caseNum == -1 ? "null" : caseNum + "");
    map.put("outvertices", output);

    String uri = null;
    try {
      uri = pseudo.pseudo_uri(map, lines, colors);
    } catch (JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ArrayIndexOutOfBoundsException e) {
    }
    return uri;
  }

  // Return an appropriate inline string for the info page
  public String doc_uri() {

    String content = "<html><head><title>Sutherland Hodgman Clipping</title></head><body><h1>Sutherland-Hodgman Clipping</h1>The famous clipping algorithm.</body></html>";

    URI uri = null;
    try {
        uri = new URI("str", content, "");
    }
    catch (java.net.URISyntaxException e) {
    }
    return uri.toASCIIString();
  }

  protected void draw(ArrayList<Point> output, ArrayList<String> tempLabels,
    int casenum, int [] lines) throws IOException
  {
    pc.clearPrimitives();
    pc.addCircle(0.049, 0.033, .015, "#999900", "#999900", "#999900", "s");
    pc.addCircle(0.049, -0.007, .015, "#009999", "#009999", "#009999", "p");
    cp.addToPrimitiveCollection(pc);
    sp.addToPrimitiveCollection(pc);
    for(int k = 0; k < output.size(); ++k) {
      pc.addCircle(output.get(k).getX(), output.get(k).getY(), .021, "#009900", "#009900",
        "#FFFFFF", tempLabels.get(k));
    }
    sp.addStartAndEndToPC(pc);
    String outputString = "";
    for(int i = 0; i < tempLabels.size(); ++i) {
      outputString += tempLabels.get(i) + ", ";
    }
    outputString = outputString == "" ? "" : outputString.substring(0, outputString.length() - 2);

    show.writeSnap(Clip.TITLE, doc_uri(), make_uri(sp.getStartIndex(), sp.getEndIndex(),
      casenum, stack, outputString, lines, pseudo), legend, pc);
   
  }
  
  protected void drawSpecial(ArrayList<Point> output, ArrayList<String> tempLabels,
    int casenum, int [] lines) throws IOException
  {
    pc.clearPrimitives();
    pc.addCircle(0.049, 0.033, .015, "#999900", "#999900", "#999900", "s");
    pc.addCircle(0.049, -0.007, .015, "#009999", "#009999", "#009999", "p");
    cp.addToPrimitiveCollection(pc);
    sp.addToPrimitiveCollection(pc);
    for(int k = 0; k < output.size(); ++k) {
      pc.addCircle(output.get(k).getX(), output.get(k).getY(), .021, "#009900", "#009900",
        "#FFFFFF", tempLabels.get(k));
    }

    String outputString = "";
    for(int i = 0; i < tempLabels.size(); ++i) {
      outputString += tempLabels.get(i) + ", ";
    }
    outputString = outputString == "" ? "" : outputString.substring(0, outputString.length() - 2);

    show.writeSnap(Clip.TITLE, doc_uri(), make_uri(sp.getStartIndex(), sp.getEndIndex(),
      casenum, stack, outputString, lines, pseudo), legend, pc);
   
  }
  
  protected void changePseudoCodeWindow(int num) throws IOException {
    String FILE;
    switch(num) {
      case 0:
        FILE = "exe/sutherlandhodgmanclipping/templateMain.xml";
        try {
          pseudo = new PseudoCodeDisplay(FILE);
        } catch (JDOMException exc) {
          // TODO Auto-generated catch block
          exc.printStackTrace();
        }
        stack="main() \n SutherlandHodgmanClip(vertices, edges)";
        break;
      case 1:
        FILE = "exe/sutherlandhodgmanclipping/templateDetermineCase.xml";
        try {
          pseudo = new PseudoCodeDisplay(FILE);
        } catch (JDOMException exc) {
          // TODO Auto-generated catch block
          exc.printStackTrace();
        }
        stack="main() \n SutherlandHodgmanClip(vertices, edges)"  +
                "\n  determineCase(s,p,e)";
        break;
      case 2:
        FILE = "exe/sutherlandhodgmanclipping/templateExecuteCase.xml";
        try {
          pseudo = new PseudoCodeDisplay(FILE);
        } catch (JDOMException exc) {
          // TODO Auto-generated catch block
          exc.printStackTrace();
        }
        stack="main() \n SutherlandHodgmanClip(vertices, edges)" +
                "\n  excuteCase(casenum,s,p,e)";
        break;
    }
  }
}
