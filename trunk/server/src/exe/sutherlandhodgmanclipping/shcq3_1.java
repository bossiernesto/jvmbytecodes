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

public class shcq3_1 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  
  public shcq3_1() {
    text = new String[5];
    //mc
    text[0] = "3.1 Given the selected red edge, start vertex (S), and end vertex (P), " + 
    "will the algorithm compute an intersection point during the next call to executeCase?";
    //tf
    text[1] = "3.1 Given the selected red edge, start vertex (S), and end vertex (P), the next call to executeCase " + 
    "will compute an intersection point.";
    text[2] = "3.1 Given the selected red edge, start vertex (S), and end vertex (P), the next call to executeCase " + 
    "will not compute an intersection point.";
    
    rand = new Random(System.currentTimeMillis());
    counter = 0;
  }
  
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges)
  {
    int j = edges.indexOf(e);
    Point ref = edges.get( (j + 2) % edges.size()).getP1();
    int caseNum = MasterQuestion.determineCaseNum(e, ref, s, p);
    
    int switchval = rand.nextInt(2);
    question q = null;
    
    switch(switchval) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show, "shcq3_1" + counter);
        mc.setQuestionText(text[0]);
        mc.addChoice("Yes");
        mc.addChoice("No");
        mc.setAnswer(caseNum == 2 || caseNum == 4 ? 1 : 2);
        q = mc;
        break;
      case 1:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq3_1" + counter);
        int willOrNot = rand.nextInt(2);
        tf.setQuestionText(text[willOrNot + 1]);
        boolean res = caseNum == 2 || caseNum == 4;
        tf.setAnswer(res == (willOrNot == 0));
        q = tf;
        break;
    }
    
    ++counter;
    return q;
  }
}