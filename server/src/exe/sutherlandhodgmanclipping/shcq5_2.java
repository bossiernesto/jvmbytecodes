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

public class shcq5_2 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  //What will be the value of s and p in the next iteration?
  public shcq5_2() {
    text = new String[3];
    text[0] = "5.2 What ";
    //mc
    text[1] = " in the next iteration?";
    
    text[2] = " will be the value of ";
    
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
    
    ArrayList<String> labels = PolygonToBeClipped.getLabels();
    int i=0;
    for(i = 0; i < labels.size()-1 ; ++i){
      if(input.get(i) == s) {
        break;
      }
    }
    String option = rand.nextInt(2) == 0 ? "the starting vertex (S)" : "the ending vertex (P)";
    
    switch (switchval) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show, "shcq5_2" + counter);
        mc.setQuestionText(text[0] + text[2] + option + text[1]);
        for(int j = 0; j < labels.size(); ++j){
          mc.addChoice("V"+j);
        }
        mc.setAnswer(option == "the starting vertex (S)" ? ((i+1)%labels.size()+1):((i+2)%labels.size()+1));
        q = mc;
        break;
      case 1:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq5_2" + counter);
        int temp = rand.nextInt(input.size());
        tf.setQuestionText("5.2 Vertex V"+temp+text[2]+option+text[1]);
        tf.setAnswer(temp == ((option=="the starting vertex (S)" ? ((i+1)%labels.size()):((i+2)%labels.size()))));
        q = tf;
        break;
      case 2:
        XMLfibQuestion fib = new XMLfibQuestion(show, "shcq5_2" + counter);
        fib.setQuestionText(text[0]+text[2]+option+text[1] +" (Enter only the vertex E.G V0/v0)");
        if(option=="the starting vertex (S)"){
            fib.setAnswer("V"+(i+1)%labels.size());
            fib.setAnswer("v"+(i+1)%labels.size());
        }else{
            fib.setAnswer("V"+(i+2)%labels.size());
            fib.setAnswer("v"+(i+2)%labels.size());
        }
        q = fib;
        break;
    }
    
    ++counter;
    return q;
  }
}