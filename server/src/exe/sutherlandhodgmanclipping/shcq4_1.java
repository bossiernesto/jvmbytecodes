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

public class shcq4_1 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  //Given the current setup, which points will be added to the output buffer in the next step?
  public shcq4_1() {
    text = new String[3];
    text[0] = "4.1 Given the selected red edge, start vertex (S), and end vertex (P), ";
    //mc
    text[1] = " how many additional vertices";
    //tf
    text[2] = "  will be added to outvertices in this call to executeCase?";
    
    rand = new Random(System.currentTimeMillis());
    counter = 0;
  }
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges)
  {
    int n = edges.indexOf(e);
    Point ref = edges.get( (n + 2) % edges.size()).getP1();
    
    int newPoints=0;
    
    question q = null;
    int switchval = rand.nextInt(3);
    
    for(int i = 0; i < output.size(); ++i) {
      boolean newPoint = true;
      for(int j = 0; j <input.size(); ++j){
          if(output.get(i)==input.get(j)){
            newPoint = false;
          }
      }
      if(newPoint){
        newPoints++;
      }
    }
    if(MasterQuestion. determineCaseNum(e,ref,s,p)==1)
      newPoints=2;
    else if(MasterQuestion. determineCaseNum(e,ref,s,p)==2)
      newPoints=2;
    else if(MasterQuestion. determineCaseNum(e,ref,s,p)==3)
      newPoints=1;
    else if(MasterQuestion. determineCaseNum(e,ref,s,p)==4)
      newPoints=3;
    
    switch (switchval) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show, "shcq4_1" + counter);
        mc.setQuestionText(text[0]+text[1]+text[2]);
        mc.addChoice("0");
        mc.addChoice("1");
        mc.addChoice("2");
        mc.setAnswer(newPoints);
        q = mc;
        break;
      case 1:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq4_1" + counter);
        int temp = rand.nextInt(3);
        tf.setQuestionText(text[0] + " will " + temp + (temp == 1 ? " vertex" : " vertices")
          + " be added to outvertices in this call to executeCase?");
        tf.setAnswer((temp+1) == newPoints);
        q = tf;
        break;
      case 2:
        XMLfibQuestion fib = new XMLfibQuestion(show, "shcq4_1" + counter);
        fib.setQuestionText(text[0]+text[1]+text[2] + " (Enter only the number)");
        switch(newPoints-1) {
          case 0:
            fib.setAnswer("zero");
            fib.setAnswer("0");
            break;
          case 1:
            fib.setAnswer("one");
            fib.setAnswer("1");
            break;
          case 2:
            fib.setAnswer("two");
            fib.setAnswer("2");
            break;
          
        }
        q = fib;
        break;

    }
    
    ++counter;
    return q;
  }
}