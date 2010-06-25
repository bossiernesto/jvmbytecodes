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
Insertion sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

/*	snap reference
	snap = 0 : start of pass or snapshots
	snap = 1 : shot that shows an insertion
	snap = 2 : shot that shows a swap
	snap = 3 : shot that shows an element being removed
	snap = 4 : shot that shows two items being compared
	snap = 5 : shows that all elements are sorted
	
	snap >= 6: other
*/

// Note that the insertion sort frequently throws an error with 

//     [java] java.lang.IndexOutOfBoundsException: Index: 3, Size: 3

// I think this occurs because of a situation like that in MCQUESTION
// 1 below (taken from a sample run), where the answer is given as 4
// but there are only three choices.  See the method mcAnsGen below
// for the probable place to fix this bug.

// T.N.  July 6th, 2005

// STARTQUESTIONS
// MCQUESTION 0
// Of these items, which will happen next?
// ENDTEXT
// Insert temp into vacant
// ENDCHOICE
// Remove value to temp
// ENDCHOICE
// Swap highlighted value with vacant spot
// ENDCHOICE
// ANSWER
// 2
// ENDANSWER
// MCQUESTION 1
// Of these items, which will happen next?
// ENDTEXT
// Insert temp into vacant
// ENDCHOICE
// Remove value to temp
// ENDCHOICE
// Swap highlighted value with vacant spot
// ENDCHOICE
// ANSWER
// 4
// ENDANSWER


package exe.insertionmyles;

import java.io.*;
import java.lang.Math.*;
import exe.*;

public class insertionSort
{
 
 private static int moves, comparsions, startIndex, answerv, counter, fibq, pass;
 private static boolean wfa;		//waiting for answer
 private static PrintWriter out;
 private static questionCollection Questions;
 private static fibQuestion fibQuest;
 private static tfQuestion tfQuest;
 private static mcQuestion mcQuest; 
 
 public static void main(String[] args) throws IOException
 {
	int[] list = new int[args.length - 1];

	//initialize global variables
	moves = 0;	
	comparsions = 0;
	counter = 0;
	fibq = -1;
	wfa = false;
	answerv = 0;	//holds a value that is important to the answer
	pass = 1;
	
	//get stuff off command line
	for(int x = 1; x < args.length; x++)
	{
		list[x - 1] = Integer.parseInt(args[x]);
	}
	startIndex = -1;

	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	
	out.println("view scale .85");
	out.println("VIEW DOCS insertionSort.html");
	Questions = new questionCollection(out, 10, (int)((Math.pow(args.length - 2, 2) / 2) * .4));
	sort(list);	
	Questions.writeQuestionsAtEOSF();
        out.close();	
 }

 //Simple method that swaps two values in the list.
 public static void swap(int [] list, int i, int j)
 {
	int temp;
	temp = list[j];
	list[j] = list[i];
	list[i] = temp;
 }

 //Insertion sort algorithm with lines added to generate pseudo code and snapshots
 public static void sort(int[] list) 
 {
	Holder stuff = new Holder();
	int index, x, vacant, xLoc;
	int l = list.length;
	int current = 0;

	genPseudo(0, stuff);
	snapShot(list, -1, current, 0);

	for(index = 1; index < l; index++)
	{
		genPseudo(1, stuff);
		snapShot(list, -1, current, 6);

		stuff.Index = "" + index;
		genPseudo(2, stuff);
		snapShot(list, index, current, 3);

		boolean change = true;
		current = list[index];
		stuff.Current = "" + current;
		vacant = index;
		startIndex = index;
		moves++;
		genPseudo(3, stuff);
		snapShot(list, index, current, 6);

		list[index] = 0;
		stuff.Vacant = "" + vacant;
		xLoc = index;   
		genPseudo(4, stuff);
		snapShot(list, -1, current, 6); 

		while(vacant > 0 && change)
		{
			change = false;
			genPseudo(5, stuff);
			snapShot(list, vacant-1, current, 4);

			comparsions += 1;
			if(list[vacant-1] > current)
			{
				change = true;
				genPseudo(6, stuff);
				snapShot(list, vacant, current, 2);

				moves += 3;
				swap(list, vacant, vacant-1);
				xLoc = vacant-1;
			}
			genPseudo(7, stuff);
			snapShot(list, -1, current, 6);

			vacant--;
			stuff.Vacant = "" + vacant;
			genPseudo(4, stuff);
			snapShot(list, -1, current, 6); 

		}		
		genPseudo(8, stuff);
		snapShot(list, xLoc, current, 1);

		moves++;
		list[xLoc] = current;
		current = 0;
		vacant = -1;
		pass++;
	}
	snapShot(list, -1, current, 5);
 }

 //Method used to generate appropriate pseudo code for Insertion sort.
 public static void genPseudo(int line, Holder stuff)
 {
	//Pseudo Code stuff
	if(line >= 0)
	    out.println("VIEW ALGO index.php?line=" + line + "&var[index]=" + stuff.Index + "&var[current]=" + stuff.Current + "&var[vacant]=" + stuff.Vacant);
	else
	    out.println("VIEW ALGO index.php?&var[index]=" + stuff.Index + "&var[current]=" + stuff.Current + "&var[vacant]=" + stuff.Vacant);
 }

 //Method that generates a snapshot.  Each time it is called it calls the questionGen method.
 //Snap contains a number that distinguishes between different snapshots. 
 //Refer to the snap reference at the top for descriptions of each snap #.
 public static void snapShot(int [] list, int comp1, int current, int snap)
 {	
	questionGen(list, comp1, current, snap);

	out.println("bar_test");
	out.println("1");
	out.println("Insertion Sort");
	out.println("***\\***");
	out.println(list.length);
	//print the list with appropriate color
	for(int i = 0; i < list.length; i++)
	{
		if((snap == 3) && (i == comp1))
			out.println("\\B" + list[i]);
		else if(i == comp1)
			out.println("\\B" + list[i]);
		else if(i <= startIndex && snap != 5)
			out.println("\\R" + list[i]);
		else if(snap == 5)
			out.println("\\R" + list[i]);
		else
			out.println(list[i]);		
	}
	
	out.println("\\B" + current);
	out.println(moves);
	out.println(comparsions);
	if(snap <= 4 || snap == 6)
	    out.println("Pass: " + pass);
	else if(snap == 0)
	    out.println("Begin Sorting");
	else if(snap == 5)
	    out.println("Sorted");
	out.println("***^***");
 }

 //Method that randomly picks from a set of questions and generates an answer.
 public static void questionGen(int [] list, int comp1, int current, int snap)
 {
	double rand;
	
	//question generation
	if((snap == 1 || snap == 3 || snap == 4) && !wfa)
	{
		rand = Math.random();
		//True False question
		if(snap == 4 && rand >= .35 && rand < .6)
			tfGen(list, comp1, current);
		//Fill In The Blank question
		else if(snap == 4 && rand >= .6)
			fibGen(list, comp1);
		//Multiple choice question
		else if (rand < .35)
			mcGen(list, comp1, snap);
	}
	//waiting for an answer to question that was last asked
	else if(comp1 >= 0 && wfa && snap != 6)
	{
		//if fib question
		if(fibq > 0)
			fibAnsGen(list, comp1, snap);
		//if mc question
		else 
			mcAnsGen(list, comp1, snap);
	}

	//If waiting for an answer and a question opportunity is missed, 
	//inform question collection!  This updates the probability of asking questions.
	if((snap == 1 || snap == 3 || snap == 4) && wfa)
		Questions.incPos();
 }
 
 //Generates a true false question and answer.
 public static void tfGen(int [] list, int comp1, int current)
 {
	boolean added;
	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	added = Questions.addQuestion(tfQuest);
	if(added)
	{
		tfQuest.setQuestionText("The value in " + comp1 + " will swap with the vacant spot.");
		if(list[comp1] > current)
			tfQuest.setAnswer(true);
		else
			tfQuest.setAnswer(false);
		Questions.insertQuestion(tfQuest.getID());
	}
	counter++;
 }

 //Generates a fill in the blank question.
 public static void fibGen(int [] list, int comp1)
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
			fibQuest.setQuestionText("The vacant spot will end up in position ____.");
			fibq = 1;
		}
		else
		{
			fibQuest.setQuestionText("There will be ___ swaps from this point until the end of this pass.");
			answerv = moves;
			fibq = 2;
		}
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for whichever fill in the blank question was asked.
 public static void fibAnsGen(int [] list, int comp1, int snap)
 {
 	if(fibq == 1)
	{	
		if(snap == 1)
		{
			fibQuest.setAnswer("" + comp1);
			wfa = false;
			fibq = -1;
		}
	}
	else if(fibq == 2)
	{
		if(snap == 1)
		{
			fibQuest.setAnswer("" + ((moves - answerv) / 3));
			wfa = false;
			answerv = 0;
			fibq = -1;	
		}
	}
 }

 //Generates a multiple choice question.
 public static void mcGen(int [] list, int comp1, int snap)
 {
	boolean added;
	mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	added = Questions.addQuestion(mcQuest);
	if(added)
	{
		Questions.insertQuestion(mcQuest.getID());
		mcQuest.setQuestionText("Of these items, which will happen next?");
		mcQuest.addChoice("Insert temp into vacant"); 
		mcQuest.addChoice("Remove value to temp");
		mcQuest.addChoice("Swap highlighted value with vacant spot");
		mcQuest.addChoice("None of the other choices");
		answerv = snap;
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for the multiple choice question asked.
 public static void mcAnsGen(int [] list, int comp1, int snap)
 {
	if (snap == 1)
		mcQuest.setAnswer(1);
	else if(answerv == 3)
		mcQuest.setAnswer(2);
	else if(snap == 2)
		mcQuest.setAnswer(3);
	else 
		mcQuest.setAnswer(4);
	
	wfa = false;
 }
}

//Class used to hold all the variable values to send to pseudo code window.
class Holder
{
    String Index;
    String Current;
    String Vacant;

    public Holder()
    {
	Index = "-";
	Current = "-";
	Vacant = "-";
    }
}
