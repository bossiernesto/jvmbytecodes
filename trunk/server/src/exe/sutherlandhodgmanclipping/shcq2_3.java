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

public class shcq2_3 implements ScreenQuestion {
  String [] text;
  Random rand;
  int counter;
  
  public shcq2_3() {
    text = new String[2];
    text[0] = "2.3 Given the selected red edge, start vertex (S), and end vertex (P), what case does this fall into?";
    text[1] = "2.3 Given the selected red edge, start vertex (S), and end vertex (P), is this case ";
    rand = new Random(System.currentTimeMillis());
    counter = 0;
  }
  
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges)
  {
    int i = edges.indexOf(e);
    Point ref = edges.get( (i + 2) % edges.size()).getP1();
    int caseNum = MasterQuestion.determineCaseNum(e, ref, s, p);
    
    question q = null;
    int val = rand.nextInt(3);
    switch (val) {
      case 0:
        XMLmcQuestion mc = new XMLmcQuestion(show , "shcq2_3" + counter);
        mc.setQuestionText(text[0]);
        mc.addChoice("Case 1 (Both Inside)");
        mc.addChoice("Case 2 (P Outside, S Inside)");
        mc.addChoice("Case 3 (Both Outside)");
        mc.addChoice("Case 4 (S Outside, P Inside)");
        mc.setAnswer(caseNum);
        q = mc;
        break;
      case 1:
        XMLfibQuestion fib = new XMLfibQuestion(show, "shcq2_3" + counter);
        fib.setQuestionText(text[0] + " (Enter only the number)");
        switch(caseNum) {
          case 1:
            fib.setAnswer("one");
            fib.setAnswer("1");
            break;
          case 2:
            fib.setAnswer("two");
            fib.setAnswer("2");
            break;
          case 3:
            fib.setAnswer("three");
            fib.setAnswer("3");
            break;
          case 4:
            fib.setAnswer("four");
            fib.setAnswer("4");
            break;
        }
        q = fib;
        break;
      case 2:
        XMLtfQuestion tf = new XMLtfQuestion(show, "shcq2_3" + counter);
        int temp = rand.nextInt(4) + 1;
        tf.setQuestionText(text[1] + temp + "?");
        tf.setAnswer(caseNum == temp);
        q = tf;
        break;
    }
    
    ++counter;
    return q;
  }
}