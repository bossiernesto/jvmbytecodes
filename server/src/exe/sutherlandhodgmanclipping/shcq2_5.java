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
import exe.question;

public class shcq2_5 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  
  public shcq2_5() {
    text = new String[5];
    text[0] = "2.5 Given the selected red edge, is vertex ";
    //mc
    text[1] = " inside or outside?";
    //tf
    text[2] = " inside?";
    text[3] = " outside?";
    //ms
    text[4] = "2.5 Given the selected red edge, which vertices are ";
    
    rand = new Random(System.currentTimeMillis());
    counter = 0;
  }
  
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges)
  {
    int j = edges.indexOf(e);
    Point ref = edges.get( (j + 2) % edges.size()).getP1();
    
    
    question q = null;
    int switchval = rand.nextInt(3);
    
    int val = rand.nextInt(input.size());
    Point temp = input.get(val);
    boolean res = Edge.isPointInsideEdge(e, ref, temp);
    int inOrOut = rand.nextInt(2);
    
    switch (switchval) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show, "shcq2_5" + counter);
        
        mc.setQuestionText(text[0] + "V" + val + text[1]);
        mc.addChoice("Inside");
        mc.addChoice("Outside");
        mc.setAnswer(res ? 1 : 2);
        q = mc;
        break;
      case 1:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq2_5" + counter);
    
        tf.setQuestionText(text[0] + "V" + val + text[inOrOut + 2]);
        boolean answer = res == (inOrOut == 0);
        tf.setAnswer(answer);
        q = tf;
        break;
      case 2:
        XMLmsQuestion ms = new XMLmsQuestion(show, "shcq2_5" + counter);      
    
        ms.setQuestionText(text[4] + text[inOrOut + 2]);
        for(int i = 0; i < input.size(); ++i) {
          boolean inside = Edge.isPointInsideEdge(e, ref, input.get(i));
          ms.addChoice("V" + i);
          if(inside == (inOrOut == 0)) {
            ms.setAnswer(i+1);
          }
        }
        q = ms;
        break;

    }
    
    ++counter;
    return q;
  }
}