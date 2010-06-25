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

public class Clip {

  /* FIELDS
  ****************************************************************************/
  static final String TITLE = "Sutherland Hodgman Clipping";
  static String FILE = "exe/sutherlandhodgmanclipping/templateMain.xml";
  static GAIGStext legend = new GAIGStext(0.0, 0.0, "S ->\nP ->");

  static String stack = "";

  static PseudoCodeDisplay pseudo;	// The pseudocode

  protected static PolygonToBeClipped ptc;
  protected static ClippingPolygon cp;
  protected static MasterQuestion masterQuestion;
  protected static int screenNum;

  protected enum MODE {STANDARD, EXERCISE};
  /****************************************************************************/

  /* MAIN
  ****************************************************************************/
  public static void main(String args[]) throws IOException {;
    stack = "main()";


    masterQuestion = new MasterQuestion(20, false);

    try {
	    pseudo = new PseudoCodeDisplay(FILE);
    } catch (JDOMException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    }

    if(determineMode(args) == MODE.STANDARD) {
      standardMain(args);
    } else {
      exerciseMain(args);
    }
  }
  /***************************************************************************/

  /* STANDARD METHODS
  ****************************************************************************/
  public static void standardMain(String args[]) throws IOException {
    ShowFile show = new ShowFile(args[0]);
    GAIGSprimitiveCollection pc = new GAIGSprimitiveCollection();

    //output buffer
    ArrayList<Point> output = new ArrayList<Point>();
    ArrayList<String> tempLabels = new ArrayList<String>();
    char letter = 'a';
    /*
    ArrayList<Point> input = new ArrayList<Point>();
    ArrayList<Point> points = new ArrayList<Point>();
    System.out.println("before generatePolygons");
    generatePolygons(input,points);
    System.out.println("after generatePolygon");
    */
    //input buffer (polygon to be clipped)
    ArrayList<Point> input = new ArrayList<Point>();
    input.add(new Point(0.5, 0.2, 0));
    input.add(new Point(0.2, 0.5, 0));
    input.add(new Point(0.5, 0.7, 0));
    input.add(new Point(0.55, 0.6, 0));
    input.add(new Point(0.9, 0.5, 0));

    //edges of clipping polygon
    ArrayList<Point> points = new ArrayList<Point>();
    points.add(new Point(.3, .3, 0));
    points.add(new Point(.8, .3, 0));
    points.add(new Point(.8, .8, 0));
    points.add(new Point(.3, .8, 0));

    ptc = new PolygonToBeClipped(input, "#0000FF");
    cp = new ClippingPolygon(points, "#FF0000", "#FF0000");

    ArrayList<Edge> edges = cp.getEdges();
    stack="main() \n SutherlandHodgmanClip(vertices, edges)";
    screenNum = -1;
    standardDraw(pc, show, output, tempLabels, -1, -1, -1, (new int[]{0}));

    for(int i = 0; i < edges.size(); ++i) {
      letter = 'a';

      //Select Edge
      Edge e = edges.get(i);
      cp.setColor("#AAAAAA");
      cp.setEdgeIndex(i);

      Point ref = edges.get( (i + 2) % edges.size()).getP1();

      //Select Starting Point
      Point s = input.get(input.size() - 1);
      ptc.setStartIndex(input.size() - 1);

      for(int j = 0; j < input.size(); ++j) {
        Point p = input.get(j);
        ptc.setEndIndex(j);

        changePseudoCodeWindow(0);
        screenNum = j == 0 ? 5 : 0;
        standardDraw(pc, show, output, tempLabels, j == 0 ? input.size() - 1 : j - 1, j, -1,
          enterLines(j));

        int casenum = determineCaseNumber(e, s, p, ref);

        screenNum = 1;
        standardDraw(pc, show, output, tempLabels, j == 0 ? input.size() - 1 : j - 1, j, casenum,
          findDetermineLines(casenum));

        //changes labels of S and P back
        ptc.setStartText("V" + (j==0 ? input.size() - 1 : j - 1));
        ptc.setEndText("V" + j);
        
        changePseudoCodeWindow(0);
        int [] temp = {7};
        screenNum = 2;
        standardDraw(pc, show, output, tempLabels, j == 0? input.size() - 1 : j -1, j, casenum, temp);

        changePseudoCodeWindow(2);
        
        switch (casenum) {
          case 1:
            output.add(p);
            tempLabels.add("V"+j);
            break;
          case 2:
            Point pi = e.computeIntersection(s, p);
            output.add(pi);
            tempLabels.add("V"+letter);
            letter++;
            break;
          case 4:
            Point pa = e.computeIntersection(s, p);
            output.add(pa);
            tempLabels.add("V"+letter);
            letter++;

            output.add(p);
            tempLabels.add("V"+j);
            break;
        }

        screenNum = 3;
        standardDraw(pc, show, output, tempLabels, j == 0 ? input.size() - 1 : j - 1, j,
          casenum, findExecuteLines(casenum));

        s = p;
        ptc.setStartIndex(j);
      }
      input = (ArrayList<Point>)output.clone();
      ptc.update(input);

      changePseudoCodeWindow(0);
      screenNum = 4;
      standardDrawSpecial(pc, show, output, tempLabels, -1, -1, -1, (new int[]{10, 11}));
      output.clear();
      tempLabels.clear();
    }

    //Final Screen Shot
    cp.setEdgeIndex(-1);
    pc.clearPrimitives();

    cp.setColor("#FF0000");
    cp.addToPrimitiveCollection(pc);

    ptc.addOriginalPolygon(pc);
    ptc.setColor("#009900");
    ptc.fillToPrimitiveCollection(pc, "#00CC00");
    ptc.addToPrimitiveCollection(pc);
    stack="main() \n SutherlandHodgmanClip(vertices, edges)";

    show.writeSnap(TITLE, doc_uri(), make_uri(-1, -1, -1, "", (new int[]{0})), pc);

    show.close();
  }

  public static int [] enterLines(int j) {
    if(j == 0) {
      int [] t1 = {1,2,3,4,5};
      return t1;
    }
    int [] t2 = {4,5,8};
    return t2;
  }

  public static int [] findDetermineLines(int casenum) {
    switch(casenum) {
      case 1:
        int[] t1 = {1, 2, 3};
        return t1;
      case 2:
        int[] t2 = {1, 7, 8};
        return t2;
      case 3:
        int[] t3 = {1, 8};
        return t3;
      case 4:
        int[] t4 = {1, 4};
        return t4;
    }
    return (new int[]{0});
  }

  public static int [] findExecuteLines(int casenum) {
    switch(casenum) {
      case 1:
        return (new int[]{1,2,3});
      case 2:
        return (new int[]{4,5,6});
      case 3:
        return (new int[]{7});
      case 4:
        return (new int[]{8,9,10,11});
    }
    return (new int[]{0});
  }

  public static int determineCaseNumber(Edge e, Point s, Point p, Point r) throws IOException {
    changePseudoCodeWindow(1);

    if(Edge.isPointInsideEdge(e,r,p)) {
      if(Edge.isPointInsideEdge(e,r,s)) {
        ptc.setStartText("in");
        ptc.setEndText("in");
        return 1;
      }
      ptc.setStartText("out");
      ptc.setEndText("in");
      return 4;
    } else {
      if(Edge.isPointInsideEdge(e,r,s)) {
        ptc.setStartText("in");
        ptc.setEndText("out");
        return 2;
      }
      ptc.setStartText("out");
      ptc.setEndText("out");
      return 3;
    }

  }

  private static void standardDraw(GAIGSprimitiveCollection pc, ShowFile show,
    ArrayList<Point> output, ArrayList<String> tempLabels, int s,
    int p, int casenum, int [] lines) throws IOException
  {
    pc.clearPrimitives();
    pc.addCircle(0.049, 0.033, .015, "#999900", "#999900", "#999900", "s");
    pc.addCircle(0.049, -0.007, .015, "#009999", "#009999", "#009999", "p");
    cp.addToPrimitiveCollection(pc);
    ptc.addToPrimitiveCollection(pc);
    for(int k = 0; k < output.size(); ++k) {
      pc.addCircle(output.get(k).getX(), output.get(k).getY(), .021, "#009900", "#009900",
        "#FFFFFF", tempLabels.get(k));
    }
    ptc.addStartAndEndToPC(pc);
    String outputString = "";
    for(int i = 0; i < tempLabels.size(); ++i) {
      outputString += tempLabels.get(i) + ", ";
    }
    outputString = outputString == "" ? "" : outputString.substring(0, outputString.length() - 2);

    ArrayList<Point> tempInput = ptc.getVertices();
    Point tempS = tempInput.get(ptc.getStartIndex());
    Point tempP = tempInput.get(ptc.getEndIndex());

    question q = masterQuestion.askQuestion(show, screenNum, tempS, tempP, cp.getCurrentEdge(),
      tempInput, output, cp.getEdges());
    if(q != null) {
      show.writeSnap(TITLE, doc_uri(), make_uri(s, p, casenum, outputString, lines), q, legend, pc);
    } else {
      show.writeSnap(TITLE, doc_uri(), make_uri(s, p, casenum, outputString, lines), legend, pc);
    }
  }

  private static void standardDrawSpecial(GAIGSprimitiveCollection pc, ShowFile show,
    ArrayList<Point> output, ArrayList<String> tempLabels, int s,
    int p, int casenum, int [] lines) throws IOException
  {
    pc.clearPrimitives();
    cp.addToPrimitiveCollection(pc);
    ptc.addToPrimitiveCollection(pc);
    for(int k = 0; k < output.size(); ++k) {
      pc.addCircle(output.get(k).getX(), output.get(k).getY(), .021, "#009900", "#009900",
        "#FFFFFF", tempLabels.get(k));
    }

    String outputString = "";
    for(int i = 0; i < tempLabels.size(); ++i) {
      outputString += tempLabels.get(i) + ", ";
    }
    outputString = outputString == "" ? "" : outputString.substring(0, outputString.length() - 2);

    ArrayList<Point> tempInput = ptc.getVertices();
    Point tempS = tempInput.get(ptc.getStartIndex());
    Point tempP = tempInput.get(ptc.getEndIndex());

    question q = masterQuestion.askQuestion(show, screenNum, tempS, tempP, cp.getCurrentEdge(),
      tempInput, output, cp.getEdges());
    //System.out.println(screenNum);
    if(q != null) {
      show.writeSnap(TITLE, doc_uri(), make_uri(s, p, casenum, outputString, lines), q, pc);
    } else {
      show.writeSnap(TITLE, doc_uri(), make_uri(s, p, casenum, outputString, lines), pc);
    }
  }

  public static void generatePolygons(ArrayList<Point> sp, ArrayList<Point> cp){
    Random rand = new Random(System.currentTimeMillis());

    int inCount=0,outCount=0;
    Point ref1;
    Point ref2;
    Point ref3;
    double MIN_DIS = .05;
    double MIN_ANGLE = 3.14159/6.0;
    
    //Generate Clipping Polygon
    do{
    ref1 = new Point(rand.nextDouble()*0.9+0.05,rand.nextDouble()*0.9+0.05,0);
    ref2 = new Point(rand.nextDouble()*0.9+0.05,rand.nextDouble()*0.9+0.05,0);
    ref3 = new Point(rand.nextDouble()*0.9+0.05,rand.nextDouble()*0.9+0.05,0);
    }while(ref1.sub(ref2).length2() < MIN_DIS && ref1.sub(ref3).length2() < MIN_DIS &&
      ref2.sub(ref3).length2() < MIN_DIS && ref1.sub(ref2).angleBetween(ref1.sub(ref3))>MIN_ANGLE &&
      ref1.sub(ref3).angleBetween(ref2.sub(ref3))>MIN_ANGLE);

    cp.add(ref1);
    cp.add(ref2);
    cp.add(ref3);

    Edge e1 = new Edge(ref1,ref2);
    Edge ext;
    
    //Extend the edge to the bounds of the screen
    double [] edgex = { e1.getP1().getX(), e1.getP2().getX() };
    double [] edgey = { e1.getP1().getY(), e1.getP2().getY() };
    double xval = (edgex[1] - edgex[0]);
    if(xval != 0) {
      double m = (edgey[1] - edgey[0]) / xval;
      double b = edgey[1] - m * edgex[1];
      double [] x = {0, 1};
      double [] y = {b, m + b};
      ext = new Edge(new Point(x[0], y[0], 0), new Point(x[1], y[1], 0));
    } else {
      double [] x = {edgex[0], edgex[0]};
      double [] y = {0, 1};
      ext = new Edge(new Point(x[0], y[0], 0), new Point(x[1], y[1], 0));
    }
    
    Point p1;
    Point p2;
    
    //Generate Intersection Points along the edge
    do {
      p1 = ext.generatePointOnEdge(rand.nextDouble());
      p2 = ext.generatePointOnEdge(rand.nextDouble());
    } while(p1.sub(p2).length2() < MIN_DIS && p1.sub(ref1).length2() < MIN_DIS && p1.sub(ref2).length2() < MIN_DIS
      && ref1.sub(p2).length2() < MIN_DIS && ref2.sub(p2).length2() < MIN_DIS);
    
  }

  /***************************************************************************/

  /* EXERCISE METHODS
  ****************************************************************************/
  public static void exerciseMain(String [] args) throws IOException {
    ShowFile show = new ShowFile(args[0]);
    GAIGSprimitiveCollection pc = new GAIGSprimitiveCollection();
    ArrayList<Point> input = new ArrayList<Point>();
    ArrayList<Point> points = new ArrayList<Point>();
    
    int cpCount = Integer.parseInt(args[2]);
    int endmark = 0;
    for(int i = 0; i < cpCount; ++i) {
      points.add(new Point(Double.parseDouble(args[3 + (i * 2)]),
        Double.parseDouble(args[3 + (i*2+1)]), 0));
      endmark = 4 + (i*2+1);
    }

    int spCount = Integer.parseInt(args[endmark]);
    
    int newend = 0;
    for(int i = 0; i < spCount; ++i) {
      input.add(new Point(Double.parseDouble(args[endmark + 1 + (i*2)]),
        Double.parseDouble(args[endmark + 1 + (i*2+1)]), 0));
      newend = endmark + 2 + (i*2+1);
    }
    
    ptc = new PolygonToBeClipped(input, "#0000FF");
    cp = new ClippingPolygon(points, "#FF0000", "#FF0000");
    
    boolean convex = convexTest(points);
    if(!convex) {
      GAIGStext text = new GAIGStext(0.5, 0.5, "ERROR: The clipping polygon is not convex.");
      pc.clearPrimitives();
      cp.addToPrimitiveCollection(pc);
      ptc.addToPrimitiveCollection(pc);
      show.writeSnap(TITLE, doc_uri(), make_uri(-1, -1, -1, "", (new int[]{0})), pc, text);
      show.close();
      return;
    }
    
    int exerciseNum = Integer.parseInt(args[newend]);
    System.out.println("Exercise #: " + exerciseNum);
    
    Exercise e = null;
    switch(exerciseNum) {
      case 1:
        int c1 = Integer.parseInt(args[newend + 1]);
        int t1 = Integer.parseInt(args[newend + 2]);
        e = new Exercise1(show, ptc, cp, pc, c1, t1);
        break;
      case 2:
        int c2 = Integer.parseInt(args[newend + 1]);
        boolean b = Boolean.parseBoolean(args[newend + 2]);
        e = new Exercise2(show, ptc, cp, pc, c2, b);
        break;
      case 3:
        int t2 = Integer.parseInt(args[newend + 1]);
        e = new Exercise3(show, ptc, cp, pc, t2);
        break;
      case 4:
        int ei1 = Integer.parseInt(args[newend + 1]);
        int t3 = Integer.parseInt(args[newend + 2]);
        e = new Exercise4(show, ptc, cp, pc, ei1, t3);
        break;
      case 5:
        int ei2 = Integer.parseInt(args[newend + 1]);
        int size1 = Integer.parseInt(args[newend + 2]);
        int [] ordering = new int[size1];
        for(int i = 0; i < size1; ++i) {
          ordering[i] = Integer.parseInt(args[newend + 3 + i]);
        }
        e = new Exercise5(show, ptc, cp, pc, ei2, size1, ordering);
        break;
      case 6:
        int ei3 = Integer.parseInt(args[newend + 1]);
        int size2 = Integer.parseInt(args[newend + 2]);
        int [] casesAllowed = new int[size2];
        for(int i = 0; i < size2; ++i) {
          casesAllowed[i] = Integer.parseInt(args[newend + 3 + i]);
        }
        e = new Exercise6(show, ptc, cp, pc, ei3, size2, casesAllowed);
        break;
      case 7:
        int ss = Integer.parseInt(args[newend + 1]);
        int es = Integer.parseInt(args[newend + 2]);
        e = new Exercise7(show, ptc, cp, pc, ss, es);
        break;
    }
    /*
    //input buffer (polygon to be clipped)
    ArrayList<Point> input = new ArrayList<Point>();
    input.add(new Point(0.5, 0.2, 0));
    input.add(new Point(0.2, 0.5, 0));
    input.add(new Point(0.5, 0.7, 0));
    input.add(new Point(0.55, 0.6, 0));
    input.add(new Point(0.9, 0.5, 0));

    //edges of clipping polygon
    ArrayList<Point> points = new ArrayList<Point>();
    points.add(new Point(.3, .3, 0));
    points.add(new Point(.8, .3, 0));
    points.add(new Point(.8, .8, 0));
    points.add(new Point(.3, .8, 0));
    */
    
    e.generateVisualization();
    
    show.close();
  }

  private static boolean convexTest(ArrayList<Point> points) {
    boolean ret = true;
    double PI = 3.14159;
    double angleSum = 0;
    boolean oldzsign = true;
    double EPSILON = .01;
    
    for(int i = 0; i < points.size(); ++i) {
      Point p = points.get(i);
      Point r1 = points.get((i == 0 ? points.size() - 1 : i - 1));
      Point r2 = points.get((i+1) % points.size());
      
      Vector v1 = r1.sub(p);
      Vector v2 = r2.sub(p);
      
      double angle = v1.angleBetween(v2);
      boolean zsign = v1.cross(v2).getZ() > 0;
      oldzsign = (i == 0 ? zsign : oldzsign);
       
      if(zsign != oldzsign) {
        ret = false;
        break;
      }
      angleSum += angle;
    }

    ret = ret && (Math.abs(angleSum - (PI * (points.size() - 2))) <= EPSILON);
    return ret;
  }
  
  /***************************************************************************/
  
  private static String make_uri(int s, int p, int caseNum, String output, int[] lines) {
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
  private static String doc_uri() {

    String content = "<html><head><title>Sutherland Hodgman Clipping</title></head><body><h1>Sutherland-Hodgman Clipping</h1>The famous clipping algorithm.</body></html>";

    URI uri = null;
    try {
        uri = new URI("str", content, "");
    }
    catch (java.net.URISyntaxException e) {
    }
    return uri.toASCIIString();
  }

  private static void changePseudoCodeWindow(int num) throws IOException {
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

  private static MODE determineMode(String [] args) {
    int val = Integer.parseInt(args[1]);
    return val == 0 ? MODE.STANDARD : MODE.EXERCISE;
  }


}
