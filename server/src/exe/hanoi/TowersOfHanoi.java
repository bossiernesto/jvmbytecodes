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

package exe.hanoi;

import java.util.*;
import java.io.*;
import exe.*;

/**
 *
 * TowersOfHanoi builds a .asu file for the Animal
 * Visualization tool.
 *
 */


public class TowersOfHanoi {

    /** the file to write to */
    private static PrintWriter out;

    /** Questions */
    private questionCollection questions;

    /** current question */
    private int currentQuestion;        

    /** Animation ID counter */
    private int animID;

    /** variable to easily set the time between array switches, in ms */
    private final int TIME = 400;

    /** variable to easily set the SHORT time in ms */
    private final int SHORT = 50;

    /** constant to establish chance of a question - n means a 1-out-of-n chance*/
    private final int chance_of_question = 2;

    /** constant to establish  whether we even consider asking questions*/
    private static boolean asking_questions;

    /** random number generator */
    private Random r = new Random();

    // (x,y) positions of the disks
    int posx [];
    int posy [];

    int numDisks, from, to, via;

    int lastLineHighlighted;

    Stack towers[];

    public TowersOfHanoi(int num, int from, int to, int via,
			 String asuFile) {
        try {
            if (! (asuFile.toLowerCase().endsWith(".asu"))) {
                asuFile = asuFile.concat(".asu");
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(asuFile)));
            questions = new questionCollection(out);
            currentQuestion = 0;
	    animID = 0;
	    numDisks = num;
	    this.from = from;
	    this.to = to;
	    this.via = via;
	    lastLineHighlighted = -1;
	    posx = new int [3];
	    posx[0] = 35;
	    for (int tow = 1; tow < 3; tow++) {
		posx[tow] = posx[0] + tow*75;
	    }
	    posy = new int [5];
	    posy [0] = 110;
	    for (int dis = 1; dis < 5; dis++) {
		posy [dis] = posy[0] - dis*10;
	    }
	    towers = new Stack [3];
	    towers[0] = new Stack();
	    towers[1] = new Stack();
	    towers[2] = new Stack();
	    for (int i = 0; i < numDisks; i++)
		towers[from].push(new Integer(i));
        } catch (IOException ex) {
            System.err.println("PrintWriter could not be created.  File: " +
                               asuFile);
            //ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Runs the program, building the script.
     */
    public void run() {
        out.println("%Animal 2");
        out.println("title Towers Of Hanoi");
        out.println("author tom naps");
        out.println("{");

	//out.println("documentation \"http://csf11.acs.uwosh.edu/jhave/html_root/doc/hanoi/hanoi.html\"");
        out.println("documentation \"doc/hanoi/hanoi.html\"");
	out.println("polygon \"tower0\" (35, 40) (35, 120) (45, 120) (45, 40) color black filled fillColor dark Gray");
	out.println("polygon \"tower1\" (110, 40) (110, 120) (120, 120) (120, 40) color black filled fillColor dark Gray");
	out.println("polygon \"tower2\" (185, 40) (185, 120) (195, 120) (195, 40) color black filled fillColor dark Gray");
	out.println("text \"A\" \"A\" at (40, 140) centered font SansSerif size 16 bold");
	out.println("text \"B\" \"B\" at (115, 140) centered font SansSerif size 16 bold");
	out.println("text \"C\" \"C\" at (190, 140) centered font SansSerif size 16 bold");
	out.println("codegroup \"hanoicode\" at (20,145) font Monospaced size 14");
	out.println("addCodeLine \"void hanoi(int N, char From, char To, char Via) {\" to \"hanoicode\"");
	out.println("addCodeLine \"  if (N == 1)\" to \"hanoicode\"");
	out.println("addCodeLine \"    move(From, To);\" to \"hanoicode\"");
	out.println("addCodeLine \"  else {\" to \"hanoicode\"");
	out.println("addCodeLine \"    hanoi(N-1, From, Via, To);\" to \"hanoicode\"");
	out.println("addCodeLine \"    move(From, To);   // Return Pt. 1\" to \"hanoicode\"");
	out.println("addCodeLine \"    hanoi(N-1, Via, To, From);\" to \"hanoicode\"");
	out.println("addCodeLine \"  }                   \" to \"hanoicode\"");
	out.println("addCodeLine \"  return;             // Return Pt. 2\" to \"hanoicode\"");
	out.println("addCodeLine \"}\" to \"hanoicode\"");

	for (int i = 0; i < numDisks; i++) {
	    out.println("rectangle \"disk" + i + "\" offset (-" + (25-i*5) + ",-" + i*10 + ") from \"tower" + from + "\" S " 
			+ "offset (" + (25-i*5) + ",-" + (i+1)*10 + ") from \"tower" + from + "\" S color black filled fillColor gold");
	}
	out.print("array \"stack\" (20,330) color black fillColor white elementColor black elemHighlight red cellHighlight blue vertical length "
		  + numDisks + " ");
	for (int i = 0; i < numDisks - 1; i++) {
	    out.print(" \"\" ");
	}
	out.print(stackFrameLine( numDisks, from, to, via, new String("MAIN")));
	out.println(" font Monospaced size 12");
	

        out.println("}");
        //out.println("{");
	// Here call the recursive function
	do_hanoi(numDisks, from, to, via);
        //out.println("}");
        if (currentQuestion > 0) {
            questions.animalWriteQuestionsAtEOSF();
        }                               
        out.close();
        // all the script code
        System.out.println("Script Written Successfully");
    }

    private void do_hanoi(int n, int from, int to, int via) {
	//System.out.println("In do_hanoi");

	highlight(1);
	if (n == 1) {
	    // Mysteriously I get better performance on the highlighting when I include the braces,
	    // which seemingly shouldn't make a difference
	    out.println("{");
	    highlight(2);
	    out.println("}");
	    move(n, from, to);
	}
	else {
	    
	    // Ask a question?
	    if (asking_questions && r.nextInt(chance_of_question) == 0) {
		if (r.nextInt(2) == 0) 
		    question2(n-1,from,via,to);
		else
		    question1(n-1, from, via);

	    }
	    
	    out.println("{");
	    out.println("arrayPut " + stackFrameLine( n - 1, from, via, to, new String(" -1-"))
			+ " on \"stack\" position " + (n-2));
	    highlight(4);
	    out.println("}");

	    // The recursive call
	    do_hanoi(n-1, from, via, to);
	    out.println("{");
	    out.println("arrayPut \"" +  "                                              "
			+ "\" on \"stack\" position " + (n-2));
	    highlight(5);
	    out.println("}");


	    // The move after returning from recursive call
	    move(n, from, to);


	    // Ask a question?
	    if (asking_questions && r.nextInt(chance_of_question) == 0) {
		if (r.nextInt(2) == 0)
		    question2(n-1,via,to,from);
		else
		    question1(n-1,via,to);

	    }
	    
	    out.println("{");
	    out.println("arrayPut " + stackFrameLine( n - 1, via, to, from, new String(" -2-"))
			+ " on \"stack\" position " + (n-2));
	    highlight(6);
	    out.println("}");


	    // The second recursive call
	    do_hanoi(n-1, via, to , from);
	    out.println("{");
	    out.println("arrayPut \"" + "                                              "
			+ "\" on \"stack\" position " + (n-2));
	    highlight(8);
	    out.println("}");
	}
	if (lastLineHighlighted != 8) highlight(8);
    }

    private void question2(int n, int from, int to, int via) {
	fibQuestion fibQ = null;

		fibQ = new fibQuestion(out, Integer.toString(currentQuestion));
		fibQ.setQuestionText(
				    "\"In the space below indicate what the argument values will be in the next recursive call.  For example, the response 4 C B A would indicate that 'N' will be 4, 'From' will be C, 'To' will be B, and 'Via' will be A\"");
		fibQ.setAnswer("\"" + n + " " + letter(from) + " " + letter(to) + " " + letter(via) + "\"");
		questions.addQuestion(fibQ);
		out.println("{");
		questions.animalInsertQuestion(currentQuestion);
		out.println("}");
		currentQuestion++;
    }

    private void question1(int n, int from, int to) {
	fibQuestion fibQ = null;

		fibQ = new fibQuestion(out, Integer.toString(currentQuestion));
		fibQ.setQuestionText(
				    "\"At the next level of recursion, ___ disks will be moved from tower ___ to tower ___.  Indicate below the number of disks and character designations of the towers that should fill these blanks.  For example, the response 4 B C would indicate that '4 disks will move from B to C'\"");
		fibQ.setAnswer("\"" + n + " " + letter(from) + " " + letter(to) + "\"");
		questions.addQuestion(fibQ);
		out.println("{");
		questions.animalInsertQuestion(currentQuestion);
		out.println("}");
		currentQuestion++;
    }


    private void move(int n, int from, int to) {
	int num_on_from = towers[from].size();
	int num_on_to = towers[to].size();
	out.println("{");
	//	out.print("polyline \"" + animID + "\"  (35, 100) (35, 20) (185, 20) (185, 110) color black hidden");
	out.println("polyline \"" + animID + "\" " + createCoord(posx[from], posy[num_on_from]) +
		  " " + createCoord(posx[from], 20) + " " + createCoord(posx[to], 20) +
		  " " + createCoord(posx[to], posy[num_on_to] - 10) + " color black hidden");
	out.println("move \"disk" + ((Integer)(towers[from].peek())).toString() + "\" via \"" + animID + "\" after 0 ticks within 10 ticks");
	out.println("}");
	towers[to].push(towers[from].pop());
	animID++;
    }

    private String createCoord(int x, int y) {
	return new String ("(" + x + ", " + y + ")");
    }
    /**
     * helper method used to insert the code to highlight a
     * certain line
     *
     * @param line Line number to highlight
     */
    private void highlight(int line) {
	//        out.println("{");
	//        out.println("highlightCode on \"hanoicode\" line " +
	//          line + " within " + SHORT + " ms");
        if (lastLineHighlighted != -1 && line != lastLineHighlighted)
	    out.println("unhighlightCode on \"hanoicode\" line " +
			lastLineHighlighted);
        if (line != lastLineHighlighted) {
	    out.println("highlightCode on \"hanoicode\" line " +
			line);
	    lastLineHighlighted = line;
	}
        //out.println("}");
    }

    private String letter(int tower_num) {
	if (tower_num == 0) return new String("A");
	else if (tower_num == 1) return new String("B");
	else return new String("C");
    }

    private String stackFrameLine(int numDisks, int from, int to, int via, String ret_pt) {
	return new String ("\"N: " + numDisks
			   + " | From: " + letter(from) + " | To: " + letter(to) + " | Via: " + letter(via)
			   +  " | Ret Pt: " + ret_pt + "\"");
    }



    public static void main(String[] args) {
        String usage = "USAGE: java exe.hanoi.TowersOfHanoi " +
            "numDisks from to via Yes/No(for questions) outputFile";
                
        if (args.length == 6) {
            try {
                int num = new Integer(args[0]).intValue();
                int from = new Integer(args[1]).intValue();
                int to = new Integer(args[2]).intValue();
                int via = new Integer(args[3]).intValue();
		asking_questions = (args[4].equals("Yes"));
                String fName = args[5];
                TowersOfHanoi toh = new TowersOfHanoi(num, from, to, via, fName);
		//out.println(args[0]+args[1]+args[2]+args[3]+args[4]+args[5]);
                toh.run();
            } catch (Exception e) {
                System.out.println(usage);
                System.exit(1);
            }
        } else {
            System.out.println(usage);
        }

    }
        
}

        











