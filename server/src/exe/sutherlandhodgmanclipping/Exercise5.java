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


public class Exercise5 extends Exercise {
  protected ArrayList<String> tempLabels;
  protected ArrayList<Point> output;
  protected char letter;
  
  protected int edgeIndex;
  protected int size;
  protected int [] caseOrdering;
  protected boolean correct;
  protected int caseIndex;
  
  protected GAIGStext text;
  
  public Exercise5(ShowFile show, PolygonToBeClipped sp, ClippingPolygon cp, GAIGSprimitiveCollection pc,
    int edgeIndex, int size, int [] caseOrdering)
  {
    super(show, sp, cp, pc);
    tempLabels = new ArrayList<String>();
    output = new ArrayList<Point>();
    
    text = null;
    
    this.edgeIndex = edgeIndex;
    this.size = size;
    this.caseOrdering = caseOrdering;
    correct = true;
    caseIndex = 0;
  }
  
  protected int [] enterLines(int j) {
    if(j == 0) {
      int [] t1 = {1,2,3,4,5};
      return t1;
    }
    int [] t2 = {4,5,8};
    return t2;
  }

  protected int [] findDetermineLines(int casenum) {
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

  protected int [] findExecuteLines(int casenum) {
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
  
  public void generateVisualization() throws IOException {
    changePseudoCodeWindow(0);
    text = new GAIGStext(0.5, 0, "The visualization will now skip to edge " + (edgeIndex+1));
    drawResult(output, tempLabels, -1, (new int[]{0}));
    
    for(int i = 0; i < edges.size(); ++i) {
      letter = 'a';

      //Select Edge
      Edge e = edges.get(i);
      cp.setColor("#AAAAAA");
      cp.setEdgeIndex(i);

      Point ref = edges.get( (i + 2) % edges.size()).getP1();

      //Select Starting Point
      Point s = input.get(input.size() - 1);
      sp.setStartIndex(input.size() - 1);

      for(int j = 0; j < input.size(); ++j) {
        Point p = input.get(j);
        sp.setEndIndex(j);
        
        if(edgeIndex == i){
          changePseudoCodeWindow(0);
          draw(output, tempLabels, -1, enterLines(j));
        }
        
        int casenum = determineCaseNum(e, ref, s, p);
        
        if(edgeIndex == i){
          changePseudoCodeWindow(1);
          int tempcase = -1;
          if(caseIndex < size && correct) {
            tempcase = caseOrdering[caseIndex];
            ++caseIndex;
          }
          correct = tempcase == casenum;
          text = new GAIGStext(0.5, 0, (tempcase != -1 ? "Case " + casenum + " occurred and case " + tempcase + 
          " was supposed to occur." : "The polygons did not adhere to the specified\n case ordering at a previous frame."));
          drawResult(output, tempLabels, casenum, findDetermineLines(casenum)); 
        }
        
        if(edgeIndex == i){
          changePseudoCodeWindow(0);
          int [] temp = {7};
          draw(output, tempLabels, casenum, temp);
          changePseudoCodeWindow(2);
        }
        
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
        
        if(edgeIndex == i){
          draw(output, tempLabels, casenum, findExecuteLines(casenum));
        }
        
        s = p;
        sp.setStartIndex(j);
      }
      input = (ArrayList<Point>)output.clone();
      sp.update(input);
      
      if(edgeIndex == i){
        changePseudoCodeWindow(0);
        drawSpecial(output, tempLabels, -1, (new int[]{10,11}));
      }
      output.clear();
      tempLabels.clear();
    }

    //Final Screen Shot
    cp.setEdgeIndex(-1);
    pc.clearPrimitives();

    cp.setColor("#FF0000");
    cp.addToPrimitiveCollection(pc);

    sp.addOriginalPolygon(pc);
    sp.setColor("#009900");
    sp.fillToPrimitiveCollection(pc, "#00CC00");
    sp.addToPrimitiveCollection(pc);
    stack="main() \n SutherlandHodgmanClip(vertices, edges)";
    String outvertices = "";
    correct = correct && (caseIndex == size);
    
    if(correct) 
    {
      text = new GAIGStext(0.5, 0, "Well Done, for edge " + (edgeIndex+1) + " the cases occurred in the specified order.");
      show.writeSnap(Clip.TITLE, doc_uri(), make_uri(sp.getStartIndex(), sp.getEndIndex(),
        -1, stack, outvertices, (new int[]{0}), pseudo), pc, text);
    } else {
      text = new GAIGStext(0.5, 0, "For edge " + (edgeIndex+1) + " the cases did not occur in the specified order.");
      show.writeSnap(Clip.TITLE, doc_uri(), make_uri(sp.getStartIndex(), sp.getEndIndex(),
        -1, stack, outvertices, (new int[]{0}), pseudo), pc, text);
    }
        
    show.close();
  }
  
  protected void drawResult(ArrayList<Point> output, ArrayList<String> tempLabels,
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
      casenum, stack, outputString, lines, pseudo), legend, pc, text);
   
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
    sp.addStartAndEndToPC(pc);
    String outputString = "";
    for(int i = 0; i < tempLabels.size(); ++i) {
      outputString += tempLabels.get(i) + ", ";
    }
    outputString = outputString == "" ? "" : outputString.substring(0, outputString.length() - 2);

    show.writeSnap(Clip.TITLE, doc_uri(), make_uri(sp.getStartIndex(), sp.getEndIndex(),
      casenum, stack, outputString, lines, pseudo), legend, pc);
   
  }
  
}