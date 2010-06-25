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
Bubble sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

/*	snap reference
	snap = 0 : start of pass or snapshots
	snap = 2 : shot that shows a swap
	snap = 3 : shot that shows two items being compared
	snap = 4 : pass is finished
	snap = 5 : shows that all elements are sorted
	
	snap >= 6 : other	
*/

package exe.bubblemyles;

import java.io.*;
import java.lang.Math.*;
import exe.*;

public class bubbleSort{

 private static int swaps, comparsions, sorted, answerv, answeri, fibq, counter, pass;
 private static boolean wfa;		//"waiting for answer"
 private static PrintWriter out;
 private static questionCollection Questions;
 private static fibQuestion fibQuest;
 private static tfQuestion tfQuest;
 private static mcQuestion mcQuest; 

 public static void main(String[] args) throws IOException
 {
	int[] list = new int[args.length - 1];
	
	//read list off the command line
	for(int x = 1; x < args.length; x++)
	{
		list[x - 1] = Integer.parseInt(args[x]);
	}
	wfa = false;
	
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	out.println("view scale .85");
	Questions = new questionCollection(out, 10, (int)((Math.pow(args.length - 2, 2) / 2)));
	out.println("VIEW DOCS bubblesort.html");	
	//initialize global variables
	swaps = 0;	
	comparsions = 0;
	answerv = 0;	//holds a value that is important to the answer
	answeri = 0;	//holds an index that is important to the answer
	fibq = 0;
	counter = 0;
	pass = 1;
	sorted = list.length;

	sort(list);
	Questions.writeQuestionsAtEOSF();
        out.close();
 }
 
 //bubble sort algorithm with lines added to generate pseudo code and snapshots
 public static void sort(int [] list)
 {
        Holder stuff = new Holder();
	int l = list.length;
	genPseudo(1, stuff);
	snapShot(list, -1, -1, 6);
	int k = 0;
	stuff.K = "0";
	boolean change = true;

	genPseudo(2, stuff);
	snapShot(list, -1, -1, 6);	
	while(change)
	{
	    change = false;
	    genPseudo(3, stuff);
	    snapShot(list, -1, -1, 6);
	    k++;
	    stuff.K = "" + k; 
	    genPseudo(4, stuff);
	    snapShot(list, -1, -1, 6);
	    for(int x = 0; x < l - k; x++)
	    {
		stuff.X = "" + x;
		genPseudo(5, stuff);
		snapShot(list, x, x + 1, 3);
		comparsions++;
		if(list[x] > list[x+1])
		{
		    genPseudo(6, stuff);
		    snapShot(list, x, x + 1, 2);
		    swap(list, x, x+1);
		    change = true;
		    swaps += 3;
		}
	        genPseudo(4, stuff);
	        snapShot(list, x, x + 1, 6);
	    }
	    stuff.X = "-";
	    sorted = l - k;
	    genPseudo(2, stuff);
	    snapShot(list, -1, -1, 4);	
	    pass++;
	}
	sorted = -1;
	genPseudo(0, stuff);
	snapShot(list, -1, -1, 5);
 }

 //simple method that swaps two values in the list
 public static void swap(int [] list, int i, int j)
 {
	int temp;
	temp = list[j];
	list[j] = list[i];
	list[i] = temp;
 }

 //method used to generate appropriate pseudo code for bubble sort
 public static void genPseudo(int line, Holder stuff)
 {
	out.println("VIEW ALGO index.php?line=" + line + "&var[k]=" + stuff.K + "&var[x]=" + stuff.X);
 }

 //Method that generates a snapshot.  Each time it is called it calls the questionGen method.
 //Snap contains a number that distinguishes between different snapshots.
 //Refer to the snap reference at the top for descriptions of each type.
 public static void snapShot(int [] list, int comp1, int comp2, int snap)
 {	
	questionGen(list, comp1, comp2, snap);

	out.println("bar_test");
	out.println("1");
	out.println("Bubble Sort");
	out.println("***\\***");
	out.println(list.length);
	//print the list with appropriate color
	for(int i = 0; i < list.length; i++)
	{
		if((i == comp1 || i == comp2) && snap != 6)
			out.println("\\B" + list[i]);
		else if(i  >= sorted)
			out.println("\\R" + list[i]);
		else
			out.println(list[i]);
	}
	out.println("-1");
	out.println(swaps);
	out.println(comparsions);
	if(snap == 0)
	    out.println("Begin Sorting");
	else if (snap == 5)
	    out.println("Sorted");
	else
	    out.println("Pass: " + pass);
	out.println("***^***");
 } 

 //randomly picks from a set of questions and generates an answer
 public static void questionGen(int [] list, int comp1, int comp2, int snap)
 {
	double rand;
	rand = Math.random();
	//question and answer generation
	if(snap == 3 && !wfa)
	{
		rand = Math.random();
		//True False question
		if((rand >= .4) && (rand < .65))
			tfGen(list, comp1, comp2);
		//Fill In The Blank question
		else if(rand >= .65)
			fibGen(list, comp1, comp2);
		//Multiple choice question
		else if (rand < .4)
			mcGen(list, comp1, comp2);
	}
	//waiting for an answer to question that was last asked
	else if(wfa)
	{	
		//if fib question
		if(fibq > 0)
			fibAnsGen(list, comp1, comp2, snap);
		//if mc question
		else 
			mcAnsGen(list, comp1, snap);	

		//If waiting for an answer and a question opportunity is missed, 
		//inform question collection!  This updates the probability of asking questions.
		if(snap == 3)
			Questions.incPos();
	}
 }

 //generates a true false question and answer
 public static void tfGen(int [] list, int comp1, int comp2)
 {
	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(tfQuest))
	{
		Questions.insertQuestion(tfQuest.getID());
		tfQuest.setQuestionText("Items " + comp1 + " and " + comp2 + " will swap.");
		if(list[comp1] > list[comp2])
			tfQuest.setAnswer(true);
		else
			tfQuest.setAnswer(false);
	}
	counter++;
 }

 //generates a fill in the blank question
 public static void fibGen(int [] list, int comp1, int comp2)
 {
	double rand;
	rand = Math.random();
	fibQuest = new fibQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(fibQuest))
	{
		rand = Math.random();
		
		if(rand <=.5 || comp1 == 0)
		{
			fibQuest.setQuestionText("The value in position " + comp1 + " will end up in position ___ after this pass");
			fibq = 1;
			answeri = comp1;
			answerv = list[comp1];
		}
		else
		{
			fibQuest.setQuestionText("There will be ___ swaps from this point until the end of this pass.");
			answerv = swaps;
			fibq = 2;
		}
		Questions.insertQuestion(fibQuest.getID());
		wfa = true;
	}
 }

 //generates an answer for whichever fill in the blank question was asked
 public static void fibAnsGen(int [] list, int comp1, int comp2, int snap)
 {
	//if fib question 1
	if(fibq == 1)
	{
		if(snap != 4 && list[comp1] == answerv)
			answeri = comp1;
		else if(snap != 4 && list[comp2] == answerv)
			answeri = comp2;
		else
		{
			fibQuest.setAnswer("" + answeri);
			wfa = false;
			counter++;
			fibq = -1;
		}
	}
	else if(fibq == 2)
	{
		if(snap == 5 || comp1 == 0)
		{
			fibQuest.setAnswer("" + ((swaps - answerv) / 3));
			wfa = false;
			counter++;
			fibq = -1;	
		}
	}
 }

 //generates a multiple choice question
 public static void mcGen(int [] list, int comp1, int comp2)
 {
	mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(mcQuest))
	{
		Questions.insertQuestion(mcQuest.getID());
		mcQuest.setQuestionText("Of these items, which will happen next?");
		mcQuest.addChoice("These items will swap.");
		mcQuest.addChoice("Compare the next two items.");
		mcQuest.addChoice("This pass is finished start back at the begining.");
		wfa = true;
	}
 }

 //generates an answer for the multiple choice question
 public static void mcAnsGen(int [] list, int comp1, int snap)
 {
	if(snap == 2)
		mcQuest.setAnswer(1);
	else if(snap == 6 && comp1 + 2 < sorted)
		mcQuest.setAnswer(2);
	else
		mcQuest.setAnswer(3);
	wfa = false;
	counter++;
 }
}
	
//class used to hold all the variable values to send to pseudo code window
class Holder
{
    String K;
    String X;
    public Holder()
    {
	K = "-";
	X = "-";
    }
}
