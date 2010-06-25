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

public class MasterQuestion {
  protected ScreenQuestionCollection [] collection;
  protected Random rand;
  
  protected int totalQuestionCount;
  protected int questionCount;
  protected int [] counters;
  
  protected static final int SCREEN_TWO_COUNT = 10;
  protected static final int SCREEN_THREE_COUNT = 2;
  protected static final int SCREEN_FOUR_COUNT = 2;
  protected static final int SCREEN_FIVE_COUNT = 2;
  
  protected boolean askAlways;
  
  public MasterQuestion(int numQuestions, boolean askAlways) {
    collection = new ScreenQuestionCollection[6];
    counters = new int[6];
    
    for(int i = 0; i < 6; ++i) {
      collection[i] = new ScreenQuestionCollection(i);
      counters[i] = 0;
    }
    
    totalQuestionCount = numQuestions;
    questionCount = 0;
    
    rand = new Random(System.currentTimeMillis());
    this.askAlways = askAlways;
    
    populateQuestions();
  }
  
  public question askQuestion(ShowFile show, int screen, Point s, Point p, Edge e, 
    ArrayList<Point> input, ArrayList<Point> output, ArrayList<Edge> edges) 
  {
    //do cool stuff here
    
    question q = null;
    if(questionCount < totalQuestionCount || askAlways) {
      switch (screen) {
        case 0:
          if((rand.nextInt(10) < 3 && counters[0] < SCREEN_TWO_COUNT) || askAlways) {
            q = collection[screen].askQuestion(show, s, p, e, input, output, edges);
            counters[0] += 1;
          }
          break;
        case 1:
          if((rand.nextInt(10) < 1 && counters[1] < SCREEN_THREE_COUNT) || askAlways) {
            q = collection[screen].askQuestion(show, s, p, e, input, output, edges);
            counters[1] += 1;
          }
          break;
        case 2:
          if((rand.nextInt(10) < 1 && counters[2] < SCREEN_FOUR_COUNT) || askAlways) {
            q = collection[screen].askQuestion(show, s, p, e, input, output, edges);
            counters[2] += 1;
          }
          break;
        case 3:
          if((rand.nextInt(10) < 1 && counters[3] < SCREEN_FIVE_COUNT) || askAlways) {
            q = collection[screen].askQuestion(show, s, p, e, input, output, edges);
            counters[3] += 1;
          }
          break;
        case 5:
          if((rand.nextInt(10) < 3 && counters[0] < SCREEN_TWO_COUNT) || askAlways) {
            q = collection[screen].askQuestion(show, s, p, e, input, output, edges);
            counters[0] += 1;
          }
          break;
      }
      questionCount = counters[0] + counters[1] + counters[2] + counters[3] + counters[4] + counters[5];
    }
    
    return q;
  }
  
  public static int determineCaseNum(Edge e, Point r, Point s, Point p) {
    if(Edge.isPointInsideEdge(e,r,p)) {
      if(Edge.isPointInsideEdge(e,r,s)) {
        return 1;
      }
      return 4;
    } else {
      if(Edge.isPointInsideEdge(e,r,s)) {
        return 2;
      }
      return 3;
    }
  }
  
  public static int countNewVerticesCreated(Edge e, ArrayList<Point> input, ArrayList<Edge> edges) {
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
  
  public static int countEliminatedVertices(Edge e, ArrayList<Point> input, ArrayList<Edge> edges) {
    int count = 0;
    int i = edges.indexOf(e);
    
    Point r = edges.get( (i + 2) % edges.size()).getP1();
    
    for(int j = 0; j < input.size(); ++j) {
      Point t = input.get(j);
      count += (!Edge.isPointInsideEdge(e, r, t)) ? 1 : 0;
    }
    
    return count;
  }
  
  protected void populateQuestions() {
    
    collection[0].addQuestion(new shcq2_3());
    collection[0].addQuestion(new shcq2_5());
    
    collection[1].addQuestion(new shcq3_1());
    
    collection[2].addQuestion(new shcq4_1());
    
    collection[3].addQuestion(new shcq5_2());
    
    collection[5].addQuestion(new shcq2_1());
    collection[5].addQuestion(new shcq2_2());
  }
}