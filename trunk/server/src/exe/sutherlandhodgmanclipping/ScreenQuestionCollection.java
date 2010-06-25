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

public class ScreenQuestionCollection {
  int id;
  Random rand;
  ArrayList<ScreenQuestion> questions;
  
  public ScreenQuestionCollection(int id) {
    this.id = id;
    rand = new Random(System.currentTimeMillis());
    questions = new ArrayList<ScreenQuestion>();
  }
  
  public int getID() {
    return id;
  }
  
  public question askQuestion(ShowFile show, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges) 
  {
    //branch through list of screen questions and randomly choose one question and invoke the method
    //for the particular question to be asked
    question q = null;
    
    int qnum = rand.nextInt(questions.size());
    q = questions.get(qnum).askQuestion(show, s, p, e, input, output, edges);
    
    return q;
  }
  
  public void addQuestion(ScreenQuestion sq) {
    questions.add(sq);
  }
}