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

package exe.easttowest;

import exe.*;
import java.io.*;
import java.util.*;

public class FactorialViz {

    static final String TITLE = "Factorial visualization";
    static int arg;       // argument to factorial
    static GAIGSlabel initMsg;
    static GAIGSlabel finalMsg;
    static SnapStack stack;
    static ShowFile show;
   

    static int answer = -1; /*Keep the factorial result.*/
    static int numOfChoices = 3; /*The number of choices associated with each question*/
    static int[] choiceFlag = new int[numOfChoices]; /*Whether a question has been assigned to the corresponding choice slot.*/
    final static int numOfQuestions = 3;
    static int [] solution = new int[numOfQuestions]; /*Keep the correct solution.*/
    static XMLmcQuestion [] mc = new XMLmcQuestion[numOfQuestions];
    static String[] questionArray = new String[numOfChoices];
    
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        show = new ShowFile (args[0]);
        arg = Integer.parseInt(args[1]);
        initMsg = new GAIGSlabel ("Initial call: fact (" + arg + ")", 0.1, -0.45, 0.9, 0.35, 0.07);
        finalMsg = new GAIGSlabel ("Final result: " + fact (arg), 0.1, -0.45, 0.9, 0.35, 0.07);
        stack = new SnapStack ( );


        /*Define questions.*/
 
        /*The first question asks the factorial result for a given value. */
        answer = fact(arg);

        /*Define first question*/
        int idIndex = 0;
        mc[idIndex] = new XMLmcQuestion(show, idIndex + "");
        mc[idIndex].setQuestionText("WHAT SHOULD BE THE VALUE OF fact("+arg+")?");
 
       /*-2 indicates that the corresponding choice slot is not assigned a question yet.*/ 
       for(int i=0; i<numOfChoices; i++)
   	       choiceFlag[i] = -2;

       
       int j=0;
       boolean notExitLoop = true;
       Random rand = new Random();
       for(int i=0; i<numOfChoices; i++)
       {
           while(notExitLoop == true)
           {
              j = rand.nextInt(numOfChoices);
              if(choiceFlag[j] != -2)
                 j = rand.nextInt(numOfChoices);
              else{
                 choiceFlag[j]=i-1;
                 questionArray[j]=answer+2*choiceFlag[j]+"";

                 if(i==1)
   	     	        solution[idIndex] = j+1;
       
                 notExitLoop = false;
              }
           }
           notExitLoop = true;
           
       }

       for(int i=0; i<numOfChoices; i++)
   	       mc[idIndex].addChoice(questionArray[i]+"");

       /*Define 2nd question */
       idIndex++;
       mc[idIndex] = new XMLmcQuestion(show, idIndex + "");
       mc[idIndex].setQuestionText("WILL fact(-1) BE EVALUATED? WHY OR WHY NOT?");
       
       mc[idIndex].addChoice("NO. ONLY ONE BRANCH OF AN if..then..else IS EVALUATED.");
       mc[idIndex].addChoice("NO. 0 IS ALWAYS A BASE CASE.");
       mc[idIndex].addChoice("YES. EVERY CALL TO fact IS EVALUATED.");
       solution[idIndex] = 1;
  	       
       
       /*Define 3rd question */
       idIndex++;
       mc[idIndex] = new XMLmcQuestion(show, idIndex + "");
       mc[idIndex].setQuestionText("HOW MANY MULTIPLICATIONS WOULD BE NEEDED TO EVALUATE fact(2) IF THE BASE CASE WERE if(n==1) return 1?");
      
     
       mc[idIndex].addChoice("1");
       mc[idIndex].addChoice("2");
       mc[idIndex].addChoice("3");
       solution[idIndex] = 1;
      
       show.writeSnap (TITLE, 0.07, "info.html", initMsg);
       
      
       vizfact (arg, stack);
       show.writeSnap (TITLE, 0.07, "info.html", finalMsg);
       show.close();                    
       }
	
       static void vizfact (int n, SnapStack stack) throws IOException {
		stack.push (n);

		
		if(n==arg)
		{
		   show.writeSnap(TITLE, "info.html", null, mc[0], stack.myStack);
	       mc[0].setAnswer(solution[0]);
		}  		
		stack.display(show);
		
		if (n == 0) { //Add second question.
			show.writeSnap(TITLE, "info.html", null, mc[1], stack.myStack);
            mc[1].setAnswer(solution[1]);
			stack.pop();
		} else {
			
			
			vizfact (n-1, stack);
			stack.replace(n);
			stack.display(show);
			stack.pop();
			
			if(n == 2){ //Add third question.
              	  show.writeSnap(TITLE, "info.html", null, mc[2], stack.myStack);
                  mc[2].setAnswer(solution[2]);
            }
			
			
		}
       }

       static int fact (int n) {
		if (n == 0) {
			return 1;
		} else {
			return n * fact(n-1);
		}
       }
}


class SnapCode {
	
	String mySnapCode;
	
	public SnapCode (int arg) {
		mySnapCode = "if (" + arg + "==0) return 1; else return " + arg + " * fact (" + arg + "-1)";
	}

	public SnapCode (int arg, boolean returning) {
		if (!returning) {
			mySnapCode = "if (" + arg + "==0) return 1; else return " + arg + " * fact (" + arg + "-1)";
		} else {
			mySnapCode = "if (" + arg + "==0) return 1; else return " + arg + " * " + FactorialViz.fact (arg-1)
				+ "\n// " + FactorialViz.fact(arg-1) + " is fact(" + (arg-1) + ")";
		}
	}
	
	public String toString ( ) {
		return mySnapCode;
	}
}

class SnapStack {
	
	GAIGSstack myStack;
	
	public SnapStack () {
	    myStack = new GAIGSstack ("Copies of fact", "#FFFFFF", 0, 0, 1, 1, 0.034);
	}
	
	public void push (int arg) {
		myStack.push (new SnapCode (arg));
	}
	
	public void pop ( ) {
		myStack.pop ( );
	}
	
	public void replace (int arg) {
		myStack.pop ( );
		myStack.push (new SnapCode (arg, true));
	}
	
	public void display (ShowFile show) throws IOException {
		show.writeSnap (FactorialViz.TITLE, 0.07, "info.html", myStack);
	}
}
