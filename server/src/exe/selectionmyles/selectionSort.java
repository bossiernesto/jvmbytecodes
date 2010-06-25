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

/*
Author: Ben Tidman 
Selection sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

package exe.selectionmyles;

import java.io.*;
import exe.*;
import java.lang.Math.*;

public class selectionSort {

	public static int movements, comparisons, answerv, counter, qnum, passes;
 	public static PrintWriter out;
	public static boolean wfa;
 	private static questionCollection Questions;
 	private static fibQuestion fibQuest;
 	private static tfQuestion tfQuest;
 	private static mcQuestion mcQuest; 

	public static void main(String[] args)throws Exception
	{
		int[] list = new int[args.length - 1];
		int helper = 0;

		//read list off the command line
		for (int i = 0; i < list.length; i++)
		{
		 	list[i] = Integer.parseInt( args[i+1] );
			helper += i;
		}

		out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
		

		int c = (list.length - 1 * 3) + (helper * 2);
		Questions = new questionCollection(out, 10, c);

		//initialize global variables
		movements = 0;
		comparisons = 0;
		answerv = 0;	//holds a value that is important to the answer
		passes = 0;
		wfa = false;

		out.println("VIEW SCALE .85");
		out.println("VIEW DOCS SelectionSort.html");
		sort(list);
		Questions.writeQuestionsAtEOSF();
		out.close();
	}

	//Selection sort algorithm with lines added to generate pseudo code and snapshots
	public static void sort(int [] list)
	{
		Holder stuff = new Holder();
		int i, j;
  		int min = 0, r;
		genPseudo(0, stuff);
		snapShot(list, -1, -1, 0);
		genPseudo(1, stuff);
		snapShot(list, -1, -1, 1);
  		for (i = 0; i < list.length-1; i++)
		{	
			stuff.I = "" + i;
			genPseudo(2, stuff);
			snapShot(list, -1, -1, 2);
    			min = i;
			stuff.MinIndex = "" + min;
			genPseudo(3, stuff);
			snapShot(list, min, -1, 3);
    			for (j = i+1; j < list.length; j++)
			{
				stuff.J = "" + j;
				genPseudo(4, stuff);
				snapShot(list, min, j, 4);
				comparisons++;
      				if (list[j] < list[min])
				{
					genPseudo(5, stuff);
					snapShot(list, min, j, 5);
        				min = j;
					stuff.MinIndex = "" + min;
				}
				genPseudo(3, stuff);
				snapShot(list, min, j, 6);
    			}
			genPseudo(6, stuff);
			snapShot(list, min, i, 7);
			swap(list, i, min);
			passes ++;
			movements +=3;
			genPseudo(1, stuff);
			snapShot(list, -1, -1, 8);
  		}
		passes++;
		snapShot(list, -1, -1, 9);
	}
	
	//simple method that swaps two values in the list
	public static void swap(int [] list, int i, int j)
	{
		int temp;
		temp = list[j];
		list[j] = list[i];
		list[i] = temp;
	}

	//method used to generate appropriate pseudo code for selection sort
 	public static void genPseudo(int line, Holder stuff)
 	{
		//Pseudo Code stuff
		if(line >= 0)
	    		out.println("VIEW ALGO index.php?line=" + line + "&var[minIndex]=" + stuff.MinIndex + "&var[i]=" + stuff.I + "&var[j]=" + stuff.J);
		else
	    		out.println("VIEW ALGO index.php?" + "&var[minIndex]=" + stuff.MinIndex + "&var[i]=" + stuff.I + "&var[j]=" + stuff.J);
	}

	//Method that generates a snapshot.  Each time it is called it calls the questionGen method.
	//Snap contains a number that distinguishes between different snapshots.
	//Each call to snapShot has a unique number snap.
	public static void snapShot(int [] list, int min, int comp, int snap)
	{
		questionGen(list, min, comp, snap);
	
		out.println("bar_test");
		out.println("1");
		out.println("Selection Sort");
		out.println("***\\***");
		out.println(list.length);
		//print the list with appropriate color
		for(int i = 0; i < list.length; i++)
		{
			if(i < passes)
				out.println("\\R" + list[i]);
			else if(i == min)
				out.println("\\G" + list[i]);
			else if(i == comp)
				out.println("\\B" + list[i]);
			else
				out.println(list[i]);
		}
		out.println(-1);
		out.println(movements);
		out.println(comparisons);
		if(snap == 1 || snap == 8)
			out.println("Pass number: " + (passes + 1));	
		else
		{
			double d = (double)((int)(((double) passes / (double) list.length) * 1000)) / 10.0;
			out.println("Percentage of array sorted: " + d + "%");	
		}
		out.println("***^***");
 	}

	//Private method that finds and returns the smallest value in the unsorted part of the array
	private static int lowest(int [] list)
	{
		int d = 10000;
		int e = 0;
		for(int i = passes; i < list.length; i++)
		{
			if(list[i] < d)
			{
				d = list[i];
				e = i;
			}
		}
		return e;
	}

	//Method that randomly picks from a set of questions and generates an answer.
 	public static void questionGen(int [] list, int min, int comp, int snap)
 	{
		double rand;
		//question generation
		if((snap == 3 || snap == 4 || snap == 5 || snap == 6) && !wfa)
		{
			rand = Math.random();
			if(rand <=.5)
				fibGen();
			else
				mcGen(list);
		}
		//waiting for an answer to question that was last asked
		else if(wfa)
		{
			if(qnum > 2)
				mcAnsGen(snap);
			else if(qnum > 0)
				fibAnsGen(min, snap);

			//If waiting for an answer and a question opportunity is missed, 
			//inform question collection!  This updates the probability of asking questions.
			if(snap == 3 || snap == 4 || snap == 5 || snap == 6)
				Questions.incPos();	
		}
			
 	}

	//generates a fill in the blank question
	public static void fibGen()
 	{
		double rand;
		boolean added;
		fibQuest = new fibQuestion(out, (new Integer(counter)).toString());
		added = Questions.addQuestion(fibQuest);
		if(added)
		{
			Questions.insertQuestion(fibQuest.getID());
			rand = Math.random();
			if(rand <=.5)
			{
				fibQuest.setQuestionText("There will be ___ comparisons from this point until the end of this pass.");
				answerv = comparisons;
				qnum = 1;
			}
			else
			{
				fibQuest.setQuestionText("The index of the minimum item in the unsorted part of the array is ___.");
				qnum = 2;
			}
			wfa = true;
		}
		counter++;
 	}

	//generates an answer for whichever fill in the blank question was asked
 	public static void fibAnsGen(int min, int snap)
 	{	
		if(snap == 7)
		{
			if(qnum == 1)
			{
				fibQuest.setAnswer("" + (comparisons - answerv));
				wfa = false;
				qnum = 0;
			}
			else if(qnum == 2)
			{
				fibQuest.setAnswer("" + min);
				wfa = false;
				qnum = 0;	
			}
		}
 	}

	//generates a multiple choice question
	public static void mcGen(int [] list)
 	{
		double rand;
		boolean added;
		mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
		added = Questions.addQuestion(mcQuest);
		if(added)
		{
			rand = Math.random();
			if(rand <=.5)
			{
				mcQuest.setQuestionText("The values at which pair of indexes will swap at the end of this pass?");
				int min = lowest(list);
				mcQuest.setAnswer(2);
				mcQuest.addChoice((passes + 2) + ", " + (min + 3));
				mcQuest.addChoice(passes + ", " + min);
				mcQuest.addChoice(min + ", " + (passes + 2));
				mcQuest.addChoice((min + 3) + ", " + passes);
				mcQuest.shuffle();
				Questions.insertQuestion(mcQuest.getID());
				qnum = 3;
			}	
			else
			{
				mcQuest.setQuestionText("Of these items, which will happen next?");
				mcQuest.addChoice("Compare next item with the minimum item.");
				mcQuest.addChoice("Set new minimum item.");
				mcQuest.addChoice("Swap minimum item with the value at index i.");
				Questions.insertQuestion(mcQuest.getID());
				qnum = 4;
			}
			wfa = true;
		}
		counter++;
	}

	//generates an answer for whichever multiple choice question was asked
 	public static void mcAnsGen(int snap)
 	{
		if(qnum == 3 && snap == 7)
		{
			wfa = false;
			qnum = 0;
		}
		else if (qnum == 4 && snap != 6)
		{
			if(snap == 4)
				mcQuest.setAnswer(1);
			else if(snap == 5)
				mcQuest.setAnswer(2);
			else
				mcQuest.setAnswer(3);
			mcQuest.shuffle();
			wfa = false;
			qnum = 0;
		}
	}
}

//class used to hold all the variable values to send to pseudo code window.
class Holder
{
    String MinIndex;
    String I;
    String J;
    public Holder()
    {
	MinIndex = "-";
	I = "-";
	J = "-";
    }
}
