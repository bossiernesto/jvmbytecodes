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
import java.lang.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import exe.question;

public class shcq2_2 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  
  public shcq2_2() {
    text = new String[5];
    //mc & fib
    text[0] = "2.2 How many vertices will be eliminated given this selected red edge?";
    //tf
    text[1] = "2.2 With respect to the selected red edge, there will be ";
    text[2] = " vertices eliminated.";
    
    rand = new Random(System.currentTimeMillis());
    counter = 0;
  }
  
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges)
  {
    int numElim = MasterQuestion.countEliminatedVertices(e, input, edges);
    
    int switchval = rand.nextInt(3);
    question q = null;
    
    switch(switchval) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show, "shcq2_2" + counter);
        mc.setQuestionText(text[0]);
        mc.addChoice(numElim + "");
        mc.addChoice((numElim + 1) + "");
        int temp = (numElim - 1) < 0 ? (numElim + 2) : (numElim - 1);
        mc.addChoice(temp + "");
        mc.setAnswer(1);
        q = mc;
        break;
      case 1:
        XMLfibQuestion fib = new XMLfibQuestion(show, "shcq2_2" + counter);
        fib.setQuestionText(text[0] + " (Enter the numeric value)");
        fib.setAnswer(numElim + "");
        q = fib;
        break;
      case 2:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq2_2" + counter);
        int val = rand.nextInt(numElim + 1);
        tf.setQuestionText(text[1] + val + text[2]);
        tf.setAnswer(numElim == val);
        q = tf;
        break;
    }
    
    ++counter;
    return q;
  }
}