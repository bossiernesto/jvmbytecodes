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
Heap sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

package exe.heapmyles;

import java.io.*;
import exe.*;

public class heapSort
{

 private static int swaps, comparsions, answerv, answeri, qnumber, counter;
 private static boolean wfa; //"waiting for answer" and "not just asked"
 private static PrintWriter out;
 private static questionCollection Questions;
 private static fibQuestion fibQuest;
 private static tfQuestion tfQuest;
 private static mcQuestion mcQuest; 


 public static void main(String[] args) throws IOException
 {
	int[] list = new int[args.length - 1];
	Holder stuff = new Holder();
	
	//read list off the command line
	for(int x = 1; x < args.length; x++)
	{
		list[x - 1] = Integer.parseInt(args[x]);
	}
	
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	out.println("view scale .85");
	out.println("VIEW DOCS HeapSort.html");

//	Questions = new questionCollection(out, 10,  numPos(list));
	Questions = new questionCollection(out);   // After disabling all MC questions, this constructor was used to get a 
	                                           // sufficient number of TF and FIB questions
	
	swaps = 0;	
	comparsions = 0;
	wfa = false;
	answerv = 0;	//holds a value that is important to the answer
	answeri = 0;	//holds an index that is important to the answer
	qnumber = -1;

	list = sort(list);
	genPseudo(1, 10, stuff);
	arraySnap(list, -1, -1, -1, -1, 5);
	Questions.writeQuestionsAtEOSF();
        out.close();		
 }

 //Method that calculates and returns the number of snapshots that a question could be asked in.
 public static int numPos(int [] list)
 {
	int pos = 0;

	for(int parent = ((list.length/2)-1); parent >= 0; parent--)
	{
		pos += 3 * (Math.log((double)list.length) / Math.log(2.0));
	}
	for(int x = list.length - 1; x >= 1; x--)
	{
		pos += 3 * (Math.log((double)x) / Math.log(2.0));
	}
	System.out.println(pos);
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

 //Heap sort algorithm with lines added to generate pseudo code and snapshots. 
 public static int [] sort(int[] list)	
 {
	Holder stuff = new Holder();
	int x, temp, mover, vacant;
	int l = list.length;	
	stuff.one = "heapSort";	
	
	genPseudo(1, 1, stuff);
	arraySnap(list, -1, -1, l, 1, 0);
	//turn the array into a heap	
	for(int parent = ((list.length/2)-1); parent >= 0; parent--)
	{
		stuff.two = "" + parent;
		genPseudo(1, 2, stuff);
		arraySnap(list, -1, -1, l, 1, 1);	
		genPseudo(1, 3, stuff);
		arraySnap(list, -1, -1, l, 1, 2);
		fixHeap(list, list.length, parent, 1);
		genPseudo(1, 1, stuff);
		arraySnap(list, -1, -1, l, 1, 3);
	}

	genPseudo(1, 4, stuff);
	arraySnap(list, -1, l - 1, l, 2, 4);
	//pop off root and move it to last position, move the last value into appropriate spot
	for(x = l - 1; x >= 1; x--)
	{
		stuff.two = "" + x;
		genPseudo(1, 5, stuff);
		arraySnap(list, 0, x, l, 2, 5);
		swaps += 3;	
		temp = list[x];
		list[x] = list[0];		
		//increment size down by one to ignor last position	
		l = x;
		vacant = 0;
		list[vacant] = temp;
		genPseudo(1, 6, stuff);
		arraySnap(list, -1, -1, l, 2, 6);
		fixHeap(list, l, 0, 2);
		genPseudo(1, 4, stuff);
		arraySnap(list, -1, x - 1, l, 2, 7);
       	}
	return list;
 }

 //Method that rebuilds the heap. Passed an array of size l, a root value and a phase number
 //that distinguishes between building the initial heap and rebuilding the heap after the root
 //has been swapped to the end.  
 public static void fixHeap(int[] list, int l, int root, int phase)
 { 
	int vacant = root, mover, largerHeap;
	Holder stuff = new Holder();
	
	stuff.one = "heapSort/shiftDown";
	stuff.two = "" + list[vacant];
	genPseudo(2, 1, stuff);
	arraySnap(list, -1, -1, l, phase, 8);

	//if the node has at least one child
	if((vacant * 2 ) + 1 <= l - 1)
	{
	    genPseudo(2, 2, stuff);
	    arraySnap(list, vacant, -1, l, phase, 9);

	    //if the node has only one child	
	    if((((vacant * 2 ) + 1) == (l - 1)))
	    {	
		genPseudo(2, 3, stuff);
		arraySnap(list, -1, -1, l, phase, 10);	
		largerHeap = (vacant * 2 ) + 1;
		stuff.three = "" + list[largerHeap];
	    }
	    else
	    {
		genPseudo(2, 4, stuff);
	       	arraySnap(list, (vacant * 2 ) + 1, (vacant * 2 ) + 2, l, phase, 11);		

		//if left child is larger then the right child	
		if(list[(vacant * 2 ) + 1] >= list[((vacant * 2 ) + 1) + 1])			
		{
			genPseudo(2, 5, stuff);
			arraySnap(list, -1, -1, l, phase, 12);
			comparsions += 1;
			largerHeap = (vacant * 2 ) + 1;
			stuff.three = "" + list[largerHeap];
			genPseudo(2, 5, stuff);
		}
		//else right child is largest
		else
		{
			genPseudo(2, 6, stuff);
			comparsions += 1;
			arraySnap(list, (vacant * 2 ) + 1, (vacant * 2 ) + 2, l, phase, 13);
			genPseudo(2, 7, stuff);
			arraySnap(list, -1, -1, l, phase, 14);
			largerHeap = ((vacant * 2 ) + 1) + 1;
			stuff.three = "" + list[largerHeap];

		}

	    }
	    genPseudo(2, 8, stuff);
	    arraySnap(list, vacant, largerHeap, l, phase, 15);
	    comparsions += 1;
	    
    	    //if parent is smaller then largest child swap them
	    if(list[vacant] < list[largerHeap])
	    {
		genPseudo(2, 9, stuff);
		arraySnap(list, vacant, largerHeap, l, phase, 16);
		mover = list[largerHeap];
		list[largerHeap] = list[vacant];
		list[vacant] = mover;
		swaps += 3;
		//recursive call
		fixHeap(list, l, largerHeap, phase);
	    }
	}
 }

 //Method used to generate appropriate pseudo code for heap sort.
 public static void genPseudo(int algo, int line, Holder stuff)
 {
	//Pseudo Code stuff
	if(algo == 1)
	    out.println("VIEW ALGO index.php?line=" + line + "&var[path]=" + stuff.one + "&var[x]=" + stuff.two);
	else if(algo == 2)
	    out.println("VIEW ALGO fhtemp.php?line=" + line + "&var[p]=" + stuff.one + "&var[currentNode]=" + stuff.two + "&var[biggestChild]=" + stuff.three);
 }

 //Method that generates a snapshot.  Each time it is called it calls the questionGen method. 
 //Snap contains a number that distinguishes between different snapshots.
 //Each call to snapShot has a unique number snap.
 public static void arraySnap(int [] list, int comp1, int comp2, int l, int phase, int snap)
 {	
	questionGen(list, comp1, comp2, snap);

	out.println("Heap");
	out.println("1");
	out.println("Heap Sort");
	out.println("***\\***");
	out.println(list.length);

	//print the list with appropriate color
	for(int i = 0; i < list.length; i++)
	{
		
		if((i == comp1) || (i == comp2))
			out.println("\\B" + list[i]);
		else if(i >= l)
			out.println("\\R" + list[i]);
		else
			out.println(list[i]);
	}
	out.println(swaps);
	out.println(comparsions);
	if(phase == 1)
		out.println("Making Heap");
	else if(phase == 2)
		out.println("Heap Sort");
	else
		out.println("Sorted");
	out.println("***^***");
 }

 //Method that randomly picks from a set of questions and generates an answer.
 // NOTE: Since MC questions were producing occasionally wrong answers, the
 // code changes below eliminate, for the time being, the possibility of getting 
 // a MC question
 public static void questionGen(int [] list, int comp1, int comp2, int snap)
 {
	double rand;
	if(!wfa)
	{
		if(snap == 15)
		{
			rand = Math.random();
//			if(rand <= .35)
			if(rand <= .35)
				tfGen(list, comp1, comp2);
//			else if(rand > .35 && rand <= .8)
			else if(rand > .35 && rand <= 1.0)
				fibGen(list, comp1);
//			else
//				mcGen1(list, snap);
		}
//		else if(snap == 8 || snap == 16)
//			mcGen1(list, snap);
//		if(snap == 9)
//			mcGen2(list, comp1, comp2, snap);
	}
	else if(wfa)
	{
		fibAnsGen(list, comp1, comp2, snap);
//		mcAnsGen(list, comp1, comp2, snap);
	}

	//If waiting for an answer and a question opportunity is missed, 
	//inform question collection!  This updates the probability of asking questions.
//	if((snap == 8 || snap == 9 || snap == 15 || snap == 16) && wfa)
	if((snap == 15) && wfa)
		Questions.incPos();
 }

 //Generates a true false question and answer.
 public static void tfGen(int [] list, int comp1, int comp2)
 {
	double rand;
	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	rand = Math.random();
	if(Questions.addQuestion(tfQuest))
	{
		if(rand <=.5)
		{
			tfQuest.setQuestionText("The value in " + comp1 + " will swap with the value in " + comp2 + ".");
			qnumber = 1;
		}
		else
		{
			tfQuest.setQuestionText("The highlighted parent is the root of a heap.");
			qnumber = 2;
		}
		if(qnumber == 1)
		{
			if(list[comp1] < list[comp2])
				tfQuest.setAnswer(true);
			else
				tfQuest.setAnswer(false);
		}
		else
		{
			if(list[comp1] > list[comp2])
				tfQuest.setAnswer(true);
			else
				tfQuest.setAnswer(false);
		}
		Questions.insertQuestion(tfQuest.getID());
	}
	counter++;
	qnumber = -1;
 }

 //Generates a fill in the blank question.
 public static void fibGen(int [] list, int comp1)
 {
	double rand;
	fibQuest = new fibQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(fibQuest))
	{
		Questions.insertQuestion(fibQuest.getID());
		rand = Math.random();
		if(rand <=.4)
		{
			fibQuest.setQuestionText("There will be ___ swaps from this point until the end of this call to shiftDown.");
			answerv = swaps;
			qnumber = 1;
		}
		else
		{
			fibQuest.setQuestionText("The value in position " + comp1 + " will end up in position ___ after this heap down");			
			qnumber = 2;	
			answeri = comp1;
			answerv = list[comp1];			
		}
		wfa = true;
	}
	counter++;
 }

//Generates an answer for whichever fill in the blank question was asked.
 public static void fibAnsGen(int [] list, int comp1, int comp2, int snap)
 {
	//if fib question	
	if(qnumber == 1)
	{	
		if(snap == 3 || snap == 7)
		{
			fibQuest.setAnswer("" + ((swaps - answerv) / 3));
			wfa = false;
			answerv = 0;
			qnumber = -1;	
		}
	}
	else if(qnumber == 2)
	{
		if(snap == 3 || snap == 7)
		{
			fibQuest.setAnswer("" + answeri);
			wfa = false;
			qnumber = -1;
		}
		else if(snap == 16)
		{
			if(list[comp1] == answerv)
				answeri = comp2;
			else if(list[comp2] == answerv)
				answeri = comp1;
		}
	}
 }
 
 //Generates a multiple choice question.
 public static void mcGen1(int [] list, int snap)
 {
	 // Debug code here will never be seen unless re-introduce the possibility of getting MC question
	mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(mcQuest))
	{
		mcQuest.setQuestionText("(Debug) " + snap + " " + "Which of these items will happen next?");
		mcQuest.addChoice("Swap highlighted nodes."); 
		mcQuest.addChoice("This call to shiftDown is finished.");
		mcQuest.addChoice("Examine current's sub-tree.");
		qnumber = 3;
		wfa = true;
		counter++;	
		Questions.insertQuestion(mcQuest.getID());
	}
 }

 //Generates a multiple choice question.
 public static void mcGen2(int [] list, int comp1, int comp2, int snap)
 {
	 // Debug code here will never be seen unless re-introduce the possibility of getting MC question
	 mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(mcQuest))
	{
		mcQuest.setQuestionText("(Debug) " + snap + " " + "Which child will swap with the parent?");
		mcQuest.addChoice("The parent will swap with the left child.");
		mcQuest.addChoice("The parent will swap with the right child.");
		mcQuest.addChoice("The parent is bigger then both of these childern, no swap will occur.");
		answeri = comp1;
		qnumber = 4;
		wfa = true;
		counter++;	
		Questions.insertQuestion(mcQuest.getID());
	}
 }

 //Generates an answer for whichever multiple choice question was asked.
 public static void mcAnsGen(int [] list, int comp1, int comp2, int snap)
 {
	if(qnumber == 3)
	{
		if(snap == 16)
		{
			mcQuest.setAnswer(1);
			wfa = false;
		}
//		else if(snap == 3 || snap == 5)   // If qnumber = 3 then don't see how snap could be 3 or 5
		else if(snap == 8 || snap == 15)
		{
			mcQuest.setAnswer(2);
			wfa = false;
		}
		else if(snap == 9)
		{
			mcQuest.setAnswer(3);
			wfa = false;
		}
	}
	else if(qnumber == 4)
	{
		if(snap == 15)
		{
			if(list[comp1] >= list[comp2])
			{
				mcQuest.setAnswer(3);
				wfa = false;
			}
		}
		if(snap == 15 && wfa)
		{
			if((comp1 * 2 ) + 1 == comp2)
				mcQuest.setAnswer(1);
			else if((comp1 * 2 ) + 2 == comp2)
				mcQuest.setAnswer(2);
			wfa = false;
		}
	}
 }
}

//Class used to hold all the variable values to send to pseudo code window.
class Holder
{
    String one;
    String two;
    String three;
    public Holder()
    {
	one = "-";
	two = "-";
	three = "-";
    }
}
