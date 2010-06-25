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
Quick sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

package exe.quickmyles;

import java.io.*;
import exe.*;

public class quickSort
{
 private static PrintWriter out;
 private static int swaps, comparisons, index, qnumber, counter, answerv, numPos;
 private static String stack;
 private static questionCollection Questions;
 private static fibQuestion fibQuest;
 private static tfQuestion tfQuest;
 private static mcQuestion mcQuest; 
 private static boolean wfa; //"waiting for answer"

 public static void main(String[] args) throws IOException
 {	
	int[] data1 = new int[args.length - 1];
	int[] data2 = new int[args.length - 1];

	//initialize global variables
	swaps = 0;	
	comparisons = 0;
	qnumber = 0;
	counter = 0;
	answerv = 0;
	numPos = 0;
	wfa = false;

	BinaryTreeNode nodes = null;
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));

	//read in items from the command line
	for(int x = 1; x < args.length; x++)
	{
		data1[x - 1] = Integer.parseInt(args[x]);
		data2[x - 1] = Integer.parseInt(args[x]);
	}
	out.println("VIEW DOCS QuickSort.html");
	nodes = quickSort(data1, 0, data1.length - 1, null, 0, "R", true);
	Questions = new questionCollection(out, 10, numPos);
	stack = "quickSort";
	quickSort(data2, 0, data2.length - 1, nodes, 0, "R", false);
	Questions.writeQuestionsAtEOSF();
        out.close();	
 }

 //Quick sort algorithm with lines added to generate pseudo code and snapshots.
 //This method is called twice.  The first time is used to find out the dimensions of the tree.
 //The second call actually does the quick sort and generates the pseudo code and snapshots.
 public static BinaryTreeNode quickSort(int[] data, int low, int high, BinaryTreeNode parent, int lev, String cType, boolean build) 
 {
	BinaryTreeNode nodes = null;
	if(build && low <= high)
	{
		if(parent == null)
			nodes = buildNode(data, low, high, parent, lev, (high - low) + 1, cType, false);
		else
			nodes = buildNode(data, low, high, parent, lev, (high - low) + 1, cType, true);
		if(low < high)
		{
			int p = pivot(data, low, high, nodes, build);
			quickSort(data, low, p - 1, nodes, lev + 1, "L", build);
			quickSort(data, p + 1, high, nodes, lev + 1, "R", build);
		}
	}
	else if(!build && low <= high)
	{
		Holder stuff = new Holder();
		stuff.one = "" + low;
		stuff.two = "" + high;
		recordNode(data, low, high, parent, -1, -1);
		genPseudo(1, 1, stuff);
		makeSnap(parent, data, 0, 0, 0, 0);
		if(low < high)
		{
			genPseudo(1, 2, stuff);
			makeSnap(parent, data, 0, 0, 0, 1);
			int p = pivot(data, low, high, parent, build);
			stuff.three = "" + p;
			recordNode(data, low, high, parent, -1, -1);
			parent.values[p - low] = "\\R" + parent.values[p - low];
			genPseudo(1, 3, stuff);
			makeSnap(parent, data, 0, 0, 0, 2);
			recordNode(data, low, high, parent, low, p - 1);
			parent.values[p - low] = "\\R" + parent.values[p - low];
			stack += "/quickSort";
			quickSort(data, low, p - 1, parent.lChild, lev + 1, "L", build);
			stack = stack.substring(0, stack.length() - 10);
			recordNode(data, low, high, parent, -1, -1);
			
			for(int x = low; x <= p; x++)
				parent.values[x - low] = "\\R" + parent.values[x - low];

			hideNode(parent.lChild);
			genPseudo(1, 4, stuff);
			makeSnap(parent, data, 0, 0, 0, 3);

			recordNode(data, low, high, parent, p + 1, high);
			for(int x = low; x <= p; x++)
				parent.values[x - low] = "\\R" + parent.values[x - low];
			stack += "/quickSort";
			quickSort(data, p + 1, high, parent.rChild, lev + 1, "R", build);
			stack = stack.substring(0, stack.length() - 10);
			hideNode(parent.rChild);

			recordNode(data, low, high, parent, -1, -1);
			for(int x = low; x <= high; x++)
				parent.values[x - low] = "\\R" + parent.values[x - low];
			makeSnap(parent, data, 0, 0, 0, 4);
		}
		else
			parent.values[0] = "\\R" + parent.values[0];
	}
	return nodes;
 }

 //Method that takes out the pivot item, finds a spot for it ensuring that: 
 //left half <= pivot value < right half
 //the pivot item is then swapped with the value at that index and that index is then returned.
 //This method is called twice.  The first time is used to find out the dimensions of the tree.
 //The second call actually does the merge sort and generates the pseudo code and snapshots.
 public static int pivot(int[] data, int low, int high, BinaryTreeNode node, boolean build)
 {
	int p = data[low] ;
	int left = low + 1;
	int right = high;
	
	if(build)
	{
		while(left <= right) 
		{
			numPos++;
			//move in from the left
			while(left <= right && data[left] <= p) 
				left++;
			numPos++;
			//move in from the right
			while(left <= right && data[right] >= p) 
				right--;		
			numPos++;
			// swap values
			if(left < right) 
			{ 
				int tmp = data[left];
				data[left] = data[right];
				data[right] = tmp ; 
			}
    		}
		//swap pivot value with 
		data[low] = data[right];
		data[right] = p; 
		return right;
	}
	else
	{
		Holder stuff = new Holder();
		stuff.one = "" + left;
		stuff.two = "" + right;
		genPseudo(2, 1, stuff);
		makeSnap(node, data, 0, 0, 0, 5);

		int lArrow = 1;
		int rArrow = node.values.length - 1;

		node.values[0]  = "\\R" + node.values[0];
		drawArrows(node, 0, "P", lArrow, "L", rArrow, "R");
		stuff.three = "" + low;
		genPseudo(2, 2, stuff);
		makeSnap(node, data, 0, 0, 0, 6);

		while(left <= right) 	
		{
			genPseudo(2, 3, stuff);
			makeSnap(node, data, high, (low + 1), high, 7);	
			comparisons++;
			//move in from the left
			while(left <= right && data[left] <= p) 
			{
				genPseudo(2, 4, stuff);
				makeSnap(node, data, 0, 0, 0, 8);	
				left++;	
				lArrow++;	
				recordNode(data, low, high, node, -1, -1);
				node.values[0]  = "\\R" + node.values[0];
				drawArrows(node, 0, "P", lArrow, "L", rArrow, "R");
				stuff.one = "" + left;
				genPseudo(2, 3, stuff);
				makeSnap(node, data, 0, 0, 0, 9);
				comparisons++;	
			}
			genPseudo(2, 5, stuff);
			makeSnap(node, data, 0, 0, 0, 10);	
			comparisons++;
			//move in from the right
			while(left <= right && data[right] >= p) 
			{
				genPseudo(2, 6, stuff);
				makeSnap(node, data, 0, 0, 0, 11);	
				right--;
				rArrow--;
				recordNode(data, low, high, node, -1, -1);
				node.values[0]  = "\\R" + node.values[0];
				drawArrows(node, 0, "P", lArrow, "L", rArrow, "R");
				stuff.two = "" + right;
				genPseudo(2, 5, stuff);
				makeSnap(node, data, 0, 0, 0, 12);
				comparisons++;
			}
			genPseudo(2, 7, stuff);
			makeSnap(node, data, 0, 0, 0, 13);
			// swap values
			if(left < right) 
			{ 
				genPseudo(2, 8, stuff);
				makeSnap(node, data, 0, 0, 0, 14);
				int tmp = data[left];
				data[left] = data[right];
				data[right] = tmp ; 
				swaps += 3;
				recordNode(data, low, high, node, -1, -1);
				node.values[0]  = "\\R" + node.values[0];
				drawArrows(node, 0, "P", lArrow, "L", rArrow, "R");
			}
			genPseudo(2, 2, stuff);
			makeSnap(node, data, 0, 0, 0, 17);
    		}
		genPseudo(2, 9, stuff);
		makeSnap(node, data, high, left, right, 15);
		swaps += 3;
		if(low != right)
		{
			//swap pivot value with 
			data[low] = data[right];
			data[right] = p;
			recordNode(data, low, high, node, -1, -1);
			node.values[right-low]  = "\\R" + node.values[right-low];
			drawArrows(node, 0, "P", lArrow, "L", rArrow, "R");
		}
		genPseudo(2, 10, stuff);
		makeSnap(node, data, 0, 0, 0, 16);
	}
	return right;
 }

 //Method used to construct the binary tree that will be used to show the animation of
 //quick sort.  Returns a new BinaryTreeNode that is a child of the parent p.
 public static BinaryTreeNode buildNode(int [] list, int first, int last, BinaryTreeNode p, int lev, int num, String cType, boolean hide)
 {
	int y = 0;
	BinaryTreeNode node = new BinaryTreeNode(num);
	node.parent = p;
	node.level = lev;
	node.childType = cType;
	if(p != null)
	{
		if(cType == "L")
			p.lChild = node;
		else
			p.rChild = node;
	}
	for(int x = first; x <= last; x++)
	{
		node.values[y] = list[x] + "\n";
		y++;
	}
	if(hide)
		hideNode(node);
	return node;
 }
 
 //Method that adds the delimiter onto the front of each node that specifies that that node
 //should not be drawn in the animation window.
 public static void hideNode(BinaryTreeNode node)
 {
	if(node != null)
	{
		for(int x = 0; x < node.values.length; x++)
			node.values[x] = "!" + node.values[x] + "\n";
	}	
 }

 //Method similar to buildNode except that it is re-recording nodes that already exist.  
 //Used during the actual sorting process.  It is passed parameters sGray and eGray which specify
 //the range of indexes that should be colored gray.  
 public static BinaryTreeNode recordNode(int [] list, int first, int last, BinaryTreeNode node, int sGray, int eGray)
 {
	int y = 0;

	for(int x = first; x <= last; x++)
	{
		if(sGray >= 0 && x >= sGray && x <= eGray)
			node.values[y] = "\\S" + list[x] + "\n";
		else
			node.values[y] = list[x] + "\n";
		y++;
	}
	return node;
 }

 //Method that is passed several indexes that require arrows.  This method inserts
 //the arrow delimiter for each of these values so that an arrow will be
 //drawn for each of them in the animation window.
 public static void drawArrows(BinaryTreeNode node, int pivot, String text1, int low, String text2, int high, String text3)
 {
	if(pivot == high)
	{
		node.values[pivot] = "\\A\\T" + text1 + "," + text3 + ":" + node.values[pivot];
		node.values[low] = "\\A\\T" + text2 + ":" + node.values[low];
	}
	else
	{
		node.values[pivot] = "\\A\\T" + text1 + ":" + node.values[pivot];

		if(low == high)
			node.values[low] = "\\A\\T" + text2 + "," + text3 + ":" + node.values[low];
		else
		{
			node.values[high] = "\\A\\T" + text3 + ":" + node.values[high];
			if(low < node.values.length)
				node.values[low] = "\\A\\T" + text2 + ":" + node.values[low];
		}
	}
 }

 //Method used to generate appropriate pseudo code for quick sort.
 public static void genPseudo(int algo, int line, Holder stuff)
 {
	//Pseudo Code stuff
	if(algo == 1)
	    out.println("VIEW ALGO index.php?line=" + line + "&var[s]=" + stack + "&var[low]=" + stuff.one + "&var[high]=" + stuff.two + "&var[p]=" + stuff.three);
	else if(algo == 2)
	    out.println("VIEW ALGO pivot.php?line=" + line + "&var[s]=" + stack + "&var[left]=" + stuff.one + "&var[right]=" + stuff.two + "&var[p]=" + stuff.three);
 }

 //Method that generates a snapshot.  Each time it is called it calls the questionGen method. 
 //Snap contains a number that distinguishes between different snapshots.
 //Each call to snapShot has a unique number snap.
 public static void makeSnap(BinaryTreeNode nodes, int [] data, int high, int left, int right, int snap)
 {
	questionGen(data, high, left, right, snap);
	out.println("recursiveTree");
	out.println("1");
	out.println("Quick Sort");
	out.println("***\\***");
	out.println("Movements: " + swaps + "       Comparisons: " + comparisons);
	out.println(compileNodes("", getRoot(nodes)));
	out.println("***^***");
 }

 //Method that recursively builds a string that contains all the current information
 //contained in t and its children.  This string is returned.
 public static String compileNodes(String comp, BinaryTreeNode t)
 {
	if(t != null)
	{
		comp += " \n";
		comp += "" + t.numNodes + "\n";
		comp += "" + t.level + "\n";
		comp += t.childType + "\n";
		for(int x = 0; x < t.numNodes; x++)
			comp += t.values[x];
		comp = compileNodes(comp, t.lChild);
		comp = compileNodes(comp, t.rChild);
	}
	return comp;
 }

 //Method that returns the root of the tree that contains node t.
 public static BinaryTreeNode getRoot(BinaryTreeNode t)
 {
	while(t.parent != null)
		t = t.parent;

	return t;	
 }

 //Class used to cunstruct a binary tree
 private static class BinaryTreeNode 
 {
        int level, numNodes;
        String childType;
        BinaryTreeNode lChild,rChild, parent;
	String []values;
        
        public BinaryTreeNode(int num) 
	{
            values = new String[num];
	    numNodes = num;
            lChild = null;
            rChild = null;
	    parent = null;
        }      
 }

 //Method that randomly picks from a set of questions and generates an answer.
 public static void questionGen(int [] data, int high, int left, int right, int snap)
 {
	double rand;
	if(!wfa)
	{
		if(snap == 7)
		{
			rand = Math.random();
			if(rand <= .4)
				tfGen(data, left, right);
			else if(rand > .4 && rand <= .8)
				fibGen();
			else
				mcGen();
		}
		else if(snap == 10 || snap == 13)
			mcGen();
	}
	else if(wfa)
	{
		if(qnumber == 1)
			tfAnsGen(data, right, snap);
		else if(qnumber == 5)
			mcAnsGen(snap);
		else
			fibAnsGen(data, high, left, right, snap);
	}

	//If waiting for an answer and a question opportunity is missed, 
	//inform question collection!  This updates the probability of asking questions.
	if((snap == 7 || snap == 10 || snap == 13) && wfa)
		Questions.incPos();
 }

 //Generates a true false question.
 public static void tfGen(int [] data, int left, int right)
 {
	int index = -1;
	double rand;
	boolean inRange = false;

	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(tfQuest))
	{	
		rand = Math.random();
		if(rand < .5)
		{
			//generate a random index within the range of the values shown in the 
			//current node. 
			while(!inRange)
			{
				rand = Math.random();
				index = (int)(rand * data.length);
				if(index >= left && index <= right)
				{
					inRange = true;
					tfQuest.setQuestionText("The pivot item will swap with the value " + data[index] + ".");
				}
			}
		}
		qnumber = 1;
		Questions.insertQuestion(tfQuest.getID());	
		answerv = index;
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for the true false question that was asked.
 public static void tfAnsGen(int [] data, int right, int snap)
 {
	if(snap == 15)
	{
		//check to see if the random guess was right or not
		if(answerv != -1)
		{
			if(answerv == right)
				tfQuest.setAnswer(true);
			else
				tfQuest.setAnswer(false);
		}
		//if not a random guess show item that will swap with pivot.
		else
		{
			tfQuest.setQuestionText("The pivot item will swap with the value " + data[right] + ".");
			tfQuest.setAnswer(true);
		}
		wfa = false;
	}
 }

 //Generates a fill in the blank question.
 public static void fibGen()
 {
	double rand;
	fibQuest = new fibQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(fibQuest))
	{
		Questions.insertQuestion(fibQuest.getID());
		rand = Math.random();
		if(rand < .5)
		{
			if(rand < .5)
			{
				fibQuest.setQuestionText("What value will left be pointing to at the end of this call to pivot? If left goes out of the range of the current array enter \"out of range\".");
				qnumber = 2;
			}
			else
			{
				fibQuest.setQuestionText("What value will right be pointing at just before the swap with the pivot item at the end of this call to pivot? If left goes out of the range of the current array enter \"out of range\"");
				qnumber = 3;
			}
		}
		else
		{
			fibQuest.setQuestionText("How many swaps (lines 8 and 9) will occur from this point until the end of this call to pivot?");
			answerv = swaps;
			qnumber = 4;
		}
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for whichever fill in the blank question that was asked.
 public static void fibAnsGen(int [] data, int high, int left, int right, int snap)
 {
	//if fib question
	if(snap == 15)
	{	
		if(qnumber == 2)
		{	
			if(left <= high)
				fibQuest.setAnswer("" + data[left]);
			else
				fibQuest.setAnswer("out of range");
			wfa = false;	
		}
		else if(qnumber == 3)
		{	
			fibQuest.setAnswer("" + data[right]);
			wfa = false;	
		}
		else
		{	
			fibQuest.setAnswer("" + (((swaps - answerv)/3) + 1));
			wfa = false;	
		}
	}
 }

 //Generates a multiple choice question.
 public static void mcGen()
 {
	mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(mcQuest))
	{
		mcQuest.setQuestionText("Which of these events will happen next?");
		mcQuest.addChoice("Decrement right.");
		mcQuest.addChoice("Increment left.");
		mcQuest.addChoice("Swap value at left with value at right.");
		mcQuest.addChoice("Swap value at pivot with value at right.");
		counter++;	
		Questions.insertQuestion(mcQuest.getID());
		qnumber = 5;
		wfa = true;
	}
 }

 //Generates an answer for whichever multiple choice question that was asked.
 public static void mcAnsGen(int snap)
 {
	if(qnumber == 5)
	{
		if(snap == 11)
		{
			mcQuest.setAnswer(1);
			wfa = false;
		}
		else if(snap == 8)
		{
			mcQuest.setAnswer(2);
			wfa = false;
		}
		else if(snap == 14)
		{
			mcQuest.setAnswer(3);
			wfa = false;
		}
		else if(snap == 15)
		{
			mcQuest.setAnswer(4);
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
