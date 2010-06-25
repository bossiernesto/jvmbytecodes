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
Shell sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

package exe.shellmyles;

import java.io.*;
import java.lang.Math.*;
import exe.*;


public class shellSort
{
 
 public static int swaps, comparsions, startIndex, answerv, answeri, counter, fibq, pass;

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

	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	out.println("view scale .85");
	out.println("VIEW DOCS ShellSort.html");

	Questions = new questionCollection(out, 10, numPos(list));

	//initialize global variables
	swaps = 0;	
	comparsions = 0;
	wfa = false;
	answerv = 0;	//holds a value that is important to the answer
	answeri = 0;	//holds an index that is important to the answer
	fibq = -1;

	sort(list);
	Questions.writeQuestionsAtEOSF();
        out.close();		
 }

 //Method that calculates and returns the number of snapshots that a question could be asked in.
 public static int numPos(int [] list)
 {
	int pos = 0, index, increment;
	int k = (int)Math.log((double)list.length);

	while(k >= 1)
	{
		increment = (int)Math.pow(2, k) - 1;
		for(index = increment; index < list.length; index ++)			
			pos += 2 + (index/increment);
		k--;
	}
	return pos;
 }

 //Simple method that swaps two values in the list.
 public static void swap(int [] list, int i, int j)
 {
	int temp;
	temp = list[j];
	list[j] = list[i];
	list[i] = temp;
 }

 //Shell sort algorithm with lines added to generate pseudo code and snapshots.
 public static void sort(int[] list)
 {
	int increment, index;
	int l = list.length;
	int k = (int)Math.log((double)l);
	Holder stuff = new Holder();

	genPseudo(1, 1, stuff);
	snapShot(list, -1, 0, -1, 0);
	stuff.one = "" + k;
	genPseudo(1, 2, stuff);
	snapShot(list, -1, 0, -1, 0);
	while(k >= 1)
	{
		genPseudo(1, 3, stuff);
		snapShot(list, -1, 0, -1, 0);
		increment = (int)Math.pow(2, k) - 1;
		stuff.two = "" + increment;
		genPseudo(1, 4, stuff);
		snapShot(list, -1, 0, -1, 0);
		for(index = increment; index < l; index ++)
		{
			stuff.three = "" + index;

			genPseudo(1, 5, stuff);
			snapShot(list, -1, 0, -1, 0);
			inSort(list, increment, index);
			genPseudo(1, 4, stuff);
			snapShot(list, -1, 0, -1, 0);
		}
		genPseudo(1, 6, stuff);
		snapShot(list, -1, 0, -1, 0);
		k--;
		stuff.one = "" + k;
		genPseudo(1, 2, stuff);
		snapShot(list, -1, 0, -1, 0);
	}
	genPseudo(1, 10, stuff);
	snapShot(list, 0, 0, -2, 10);
 }

 //Method that preforms one pass of the insertion sort algorithm with a specified increment.
 public static void inSort(int[] list, int increment, int start)
 {
	int vacant, xLoc;
	int l = list.length;
	int current = 0;
	boolean change = true;
	Holder stuff = new Holder();
	
	startIndex = start;
	stuff.one = "" + increment;
	stuff.two = "" + start;

	xLoc = start;
	genPseudo(2, 1, stuff);
	snapShot(list, start, current, increment, 9);
	swaps += 1;
	current = list[start];
	stuff.three = "" + current;
	genPseudo(2, 2, stuff);
	snapShot(list, start, current, increment, 1);
	vacant = start;
	stuff.four = "" + vacant;
	list[start] = 0;

	genPseudo(2, 3, stuff);
	snapShot(list, -1, current, -1, 2);
	while(vacant > (increment - 1) && change)
	{
		change = false;
		genPseudo(2, 4, stuff);
		snapShot(list, (vacant-increment), current, increment, 3);
		comparsions += 1;
		if(list[vacant-increment] > current)
		{
			change = true;
			genPseudo(2, 5, stuff);
			snapShot(list, vacant, current, increment, 4);
			swaps += 3;
			swap(list, vacant, vacant-increment);
			xLoc = vacant-increment;
			genPseudo(2, 6, stuff);
			snapShot(list, vacant, current, increment, 5);
			vacant-=increment;
			stuff.four = "" + vacant;
		}
		pass++;
		genPseudo(2, 3, stuff);
		snapShot(list, -1, current, -1, 6);
	}
	
	genPseudo(2, 7, stuff);
	snapShot(list, xLoc, current, increment, 7);
	swaps += 1;
	list[xLoc] = current;
	current = 0;
	genPseudo(2, 8, stuff);
	snapShot(list, -1, current, increment, 8);
 }

 //Method used to generate appropriate pseudo code for shell sort.
 public static void genPseudo(int algo, int line, Holder stuff)
 {
	//Pseudo Code stuff
	if(algo == 1)
	    out.println("VIEW ALGO index.php?line=" + line + "&var[k]=" + stuff.one + "&var[increment]=" + stuff.two + "&var[index]=" + stuff.three);
	else if(algo == 2)
	    out.println("VIEW ALGO inSort.php?line=" + line + "&var[increment]=" + stuff.one + "&var[start]=" + stuff.two + "&var[current]=" + stuff.three + "&var[vacant]=" + stuff.four);
 }

 
 //Method that generates a snapshot.  Each time it is called it calls the questionGen method. 
 //Snap contains a number that distinguishes between different snapshots.
 //Each call to snapShot has a unique number snap.
 public static void snapShot(int [] list, int comp, int current, int increment, int snap)
 {	
	questionGen(list, comp, current, snap);
	//Snapshot output
	int number = comp%increment;
	out.println("bar_test");
	out.println("1");
	out.println("Shell Sort");
	out.println("***\\***");
	out.println(list.length);
	for(int i = 0; i < list.length; i++)
	{
		if((snap == 3 || snap == 1) && (i == comp))
			out.println("\\B" + list[i]);
		else if((snap == 2 || snap == 4) && i == comp)
			out.println("\\B" + + list[i]);
		else if(snap == 10)
			out.println("\\R" + list[i]);
		else
			out.println(list[i]);
	}
	out.println("\\B" + current);
	out.println(swaps);
	out.println(comparsions);
	if(snap < 10)
		out.println("Pass: " + pass);
	else
		out.println("Sorted");
	out.println("***^***");
 }

 //Method that randomly picks from a set of questions and generates an answer.
 public static void questionGen(int [] list, int comp, int current, int snap)
 {
	double rand;
	
	//question and answer generation
	if(!wfa)
	{
		rand = Math.random();
		if(snap == 3)
		{
			//True False question
			if(rand < .5)
				tfGen(list, comp, current);
			//Fill In The Blank question
			else if(rand >= .5 && rand <= .8)
				fibGen(list);
			//Multiple choice question
			else
				mcGen(list, comp, snap);
		}
		else if(snap == 2 || snap == 5)
		{
			if(rand < .6)
				fibGen(list);
			else
				mcGen(list, comp, snap);
		}
		else if(snap == 9)
			mcGen(list, comp, snap);
	}
	//waiting for an answer to question that was last asked
	else if(wfa)
	{
		//if fib question
		if(fibq > 0)
			fibAnsGen(list, comp, snap);
		//if mc question
		else 
			mcAnsGen(list, comp, snap);
	}

	//If waiting for an answer and a question opportunity is missed, 
	//inform question collection!  This updates the probability of asking questions.
	if((snap == 2 || snap == 3 || snap == 5 || snap == 9) && wfa)
		Questions.incPos();
 }

 //Generates a true false question and answer.
 public static void tfGen(int [] list, int comp, int current)
 {
	boolean added;
	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	added = Questions.addQuestion(tfQuest);
	if(added)
	{
		tfQuest.setQuestionText("The value in " + comp + " will swap with the vacant spot.");
		if(list[comp] > current)
			tfQuest.setAnswer(true);
		else
			tfQuest.setAnswer(false);
		Questions.insertQuestion(tfQuest.getID());
	}
	counter++;
 }

 //Generates a fill in the blank question.
 public static void fibGen(int [] list)
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
			fibQuest.setQuestionText("The vacant spot will end up in position ___.");
			fibq = 1;
		}
		else
		{
			fibQuest.setQuestionText("There will be ___ swaps from this point until the end of this pass.");
			answerv = swaps;
			fibq = 2;
		}
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for whichever fill in the blank question was asked.
 public static void fibAnsGen(int [] list, int comp, int snap)
 {
 	if(snap == 7)
	{	
		if(fibq == 1)
		{
			fibQuest.setAnswer("" + comp);
			wfa = false;
			fibq = -1;
		}
		else if(fibq == 2)
		{
			fibQuest.setAnswer("" + ((swaps - answerv) / 3));
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
		mcQuest.setQuestionText("Which of these will happen next?");
		mcQuest.addChoice("Insert temp into vacant"); 
		mcQuest.addChoice("Remove value to temp");
		mcQuest.addChoice("Swap highlighted value with vacant spot");
		mcQuest.addChoice("Compare temp with next item.");
		mcQuest.addChoice("None of the other choices");
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for whichever multiple choice question was asked.
 public static void mcAnsGen(int [] list, int comp1, int snap)
 {
	if (snap == 7)
	{
		mcQuest.setAnswer(1);
		wfa = false;
	}
	else if(snap == 1)
	{
		mcQuest.setAnswer(2);
		wfa = false;
	}
	else if(snap == 4)
	{
		mcQuest.setAnswer(3);
		wfa = false;
	}
	else if(snap == 3)
	{
		mcQuest.setAnswer(4);
		wfa = false;
	}
	else 
    {
		mcQuest.setAnswer(5);
		wfa = false;
	}
 }
}

//Class used to hold all the variable values to send to pseudo code window.
class Holder
{
    String one;
    String two;
    String three;
    String four;
    public Holder()
    {
	one = "-";
	two = "-";
	three = "-";
	four = "-";
    }
}
