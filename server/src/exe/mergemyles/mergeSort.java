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
Merge sort algorithm with lines added to generate pseudo code and snapshots
Date: 6-13-2005
*/

package exe.mergemyles;

import java.io.*;
import exe.*;

public class mergeSort
{
 private static PrintWriter out;
 private static int swaps, comparsions, index, qnumber, counter, answerv, numPos;
 private static boolean wfa; //"waiting for answer"
 private static String stack;
 private static questionCollection Questions;
 private static fibQuestion fibQuest;
 private static tfQuestion tfQuest;
 private static mcQuestion mcQuest; 

 public static void main(String[] args) throws IOException
 {
	int[] listA = new int[args.length - 1];
	int[] listB = new int[args.length - 1];

	//initialize global variables
	swaps = 0;	
	comparsions = 0;
	qnumber = 0;
	counter = 0;
	answerv = 0;
	numPos = 0;
	wfa = false;

	BinaryTreeNode nodes = null;
	Holder stuff = new Holder();
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));

	//read list off the command line
	for(int x = 1; x < args.length; x++)
	{
		listA[x - 1] = Integer.parseInt(args[x]);
		listB[x - 1] = Integer.parseInt(args[x]);
	}
	
	out.println("VIEW DOCS MergeSort.html");
	nodes = sort(listA, listB, 0, listA.length - 1, null, 0, "R", true);
	Questions = new questionCollection(out, 10, numPos);
	stack = "mergeSort";
	sort(listA, listB, 0, listA.length - 1, nodes, 0, "R", false);

	genPseudo(1, 0, stuff);
	makeSnap(nodes, 0, 0, 19);
	Questions.writeQuestionsAtEOSF();
        out.close();	
 }

 //Merge sort algorithm with lines added to generate pseudo code and snapshots.
 //This method is called twice.  The first time is used to find out the dimensions of the tree.
 //The second call actually does the merge sort and generates the pseudo code and snapshots.
 public static BinaryTreeNode sort(int [] listA, int [] listB, int first, int last, BinaryTreeNode p, int lev, String cType, boolean build)
 {
	Holder stuff = new Holder();
	BinaryTreeNode nodes = null;
	if(build)
	{
		if(p == null)
			nodes = buildNode(listA, first, last, p, lev, (last-first)+1, cType, false);
		else
			nodes = buildNode(listA, first, last, p, lev, (last-first)+1, cType, true);
	}
	else
	{
		stuff.one = "" + first;
		stuff.two = "" + last;
		recordNode(listA, first, last, p, -1, -1, -1, false);
	        genPseudo(1, 1, stuff);
		makeSnap(p, 0, 0, 1);
	}
	if(first < last)
	{
		int mid = (first+last)/2;
		if(build)
		{
			numPos++;
			sort(listB, listA, first, mid, nodes, lev + 1, "L", build);
			numPos++;
			sort(listB, listA, mid+1, last, nodes, lev + 1, "R", build);
			numPos += (last - first);
		}
		else
		{
	        	genPseudo(1, 2, stuff);
			makeSnap(p, 0, 0, 2);
			stuff.three = "" + mid;
	       		genPseudo(1, 3, stuff);
			makeSnap(p, 0, 0, 3);
			recordNode(listA, first, last, p, first, mid, -1, false);
			stack += "/mergeSort";
			sort(listB, listA, first, mid, p.lChild, lev + 1, "L", build);
			stack = stack.substring(0, stack.length() - 10);
	        	genPseudo(1, 4, stuff);
			makeSnap(p, 0, 0, 4);
			recordNode(listA, first, last, p, first, last, -1, false);
			stack += "/mergeSort";
			sort(listB, listA, mid+1, last, p.rChild, lev + 1, "R", build);
			stack = stack.substring(0, stack.length() - 10);
	        	genPseudo(1, 5, stuff);
			makeSnap(p, 0, 0, 5);
			merge(listA, listB, first, mid, (mid + 1), last, p);
		}
	}
	return nodes;
 }

 //Method used to construct the binary tree that will be used to show the animation of
 //merge sort.  Returns a new BinaryTreeNode that is a child of the parent p.
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
	for(int x = 0; x < node.values.length; x++)
		node.values[x] = "!" + node.values[x];
	
 }

 //Method similar to buildNode except that it is re-recording nodes that already exist.  
 //Used during the actual sorting process.  It is passed parameters sGray and eGray which specify
 //the range of indexes that should be colored gray.  It is also passed arrow which specifies
 //an index that should be pointed to by an arrow in the animation window.
 public static BinaryTreeNode recordNode(int [] list, int first, int last, BinaryTreeNode node, int sGray, int eGray, int arrow, boolean hide)
 {
	int y = 0;

	for(int x = first; x <= last; x++)
	{
		if(sGray >= 0 && x >= sGray && x <= eGray)
			node.values[y] = "\\S" + list[x] + "\n";
		else
			node.values[y] = list[x] + "\n";

		if(x == arrow)
			node.values[y] = "\\A" + node.values[y] + "\n";
		y++;
	}
	return node;
 }

 //Method that merges two sections of listA specified by the parameters into the corresponding 
 //section in listB in ascending order with lines added to generate pseudo code and snapshots.
 public static void merge(int [] listA, int [] listB, int leftFirst, int leftLast, int rightFirst, int rightLast, BinaryTreeNode parent)
 {
	int saveLFirst = leftFirst;
	int saveRFirst = rightFirst;
	int index = leftFirst;
	int arrow = -1;
	Holder stuff = new Holder();
	stuff.two = "" + leftFirst;
	stuff.three = "" + rightFirst;

	recordNode(listB, saveLFirst, index, parent, leftFirst, rightLast, index, false);
	recordNode(listA, saveLFirst, leftFirst, parent.lChild, -1, -1, leftFirst, false);
	recordNode(listA, saveRFirst, rightFirst, parent.rChild, -1, -1, rightFirst, false);

	genPseudo(2, 1, stuff);
	makeSnap(parent, 0, 0, 6);
	stuff.one = "" + index;
	genPseudo(2, 2, stuff);
	makeSnap(parent, 0, 0, 7);
	
	//while left and right section still have more values
	while((leftFirst <= leftLast) && (rightFirst <= rightLast))
	{
		genPseudo(2, 3, stuff);
		makeSnap(parent, listA[leftFirst], listA[rightFirst], 8);
		
		//if next left value is smaller merge it
		if(listA[leftFirst] < listA[rightFirst])
		{
			comparsions += 1;
			genPseudo(2, 4, stuff);
			makeSnap(parent, 0, 0, 9);
			listB[index] = listA[leftFirst];
			swaps += 1;
			arrow = leftFirst + 1;
			//if not last item in list draw new arrow
			if(leftFirst < leftLast && index < rightLast)
			{
				leftFirst++;
				recordNode(listA, saveLFirst, leftFirst, parent.lChild, saveLFirst, leftFirst - 1, arrow, false); 
			}
			//don't draw arrow
			else
			{
				recordNode(listA, saveLFirst, leftFirst, parent.lChild, saveLFirst, leftFirst, arrow, false);
				leftFirst++;
			}
			stuff.two = "" + leftFirst;
		}
		//if next right value is smaller merge it
		else
		{
			comparsions += 1;
			genPseudo(2, 5, stuff);
			makeSnap(parent, 0, 0, 10);
			genPseudo(2, 6, stuff);
			makeSnap(parent, 0, 0, 11);
			listB[index] = listA[rightFirst];
			swaps += 1;
			arrow = rightFirst + 1;
			//if not last item in list draw new arrow
			if(rightFirst < rightLast)
			{
				rightFirst++;
				recordNode(listA, saveRFirst, rightFirst, parent.rChild, saveRFirst, rightFirst - 1, arrow, false);
			}
			//don't draw arrow
			else
			{
				recordNode(listA, saveRFirst, rightFirst, parent.rChild, saveRFirst, rightFirst, arrow, false);
				rightFirst++;
			}
			stuff.three = "" + rightFirst;
		}
		if(index < rightLast)
		{
			index++;
			recordNode(listB, saveLFirst, index, parent, index, rightLast, index, false);
		}
		else
		{
			recordNode(listB, saveLFirst, index, parent, index + 1, rightLast, -1, false);
			index++;
		}
		stuff.one = "" + index;
		genPseudo(2, 2, stuff);
		makeSnap(parent, 0, 0, 12);
	}
	
	genPseudo(2, 7, stuff);
	makeSnap(parent, 0, 0, 13);

	//if there are more values remaining in the left half merge them in
	while(leftFirst <= leftLast)
	{
		genPseudo(2, 8, stuff);
		makeSnap(parent, 0, 0, 14);
		listB[index] = listA[leftFirst];
		swaps += 1;
		arrow = leftFirst + 1;
		//if not last item in list draw new arrow
		if(leftFirst < leftLast && index < rightLast)
		{
			leftFirst++;
			recordNode(listA, saveLFirst, leftFirst, parent.lChild, saveLFirst, leftFirst - 1, arrow, false);
		}
		//don't draw arrow
		else
		{
			recordNode(listA, saveLFirst, leftFirst, parent.lChild, saveLFirst, leftFirst, arrow, false);
			leftFirst++;
		}
		if(index < rightLast)
		{
			index++;
			recordNode(listB, saveLFirst, index, parent, index, rightLast, index, false);
		}
		else
		{
			recordNode(listB, saveLFirst, index, parent, index + 1, rightLast, -1, false);
			index++;
		}
		stuff.one = "" + index;
		stuff.two = "" + leftFirst;
		genPseudo(2, 7, stuff);
		makeSnap(parent, 0, 0, 15);
	}

	genPseudo(2, 9, stuff);
	makeSnap(parent, 0, 0, 16);

	//if there are more values remaining in the right half merge them in
	while(rightFirst <= rightLast)
	{
		genPseudo(2, 10, stuff);
		makeSnap(parent, 0, 0, 17);
		listB[index] = listA[rightFirst];
		swaps += 1;
		arrow = rightFirst + 1;
		//if not last item in list draw new arrow
		if(rightFirst < rightLast)
		{
			rightFirst++;
			recordNode(listA, saveRFirst, rightFirst, parent.rChild, saveRFirst, rightFirst - 1, arrow, false);
		}
		//don't draw arrow
		else
		{
			recordNode(listA, saveRFirst, rightFirst, parent.rChild, saveRFirst, rightFirst, arrow, false);
			rightFirst++;
		}
		if(index < rightLast)
		{
			index++;
			recordNode(listB, saveLFirst, index, parent, index, rightLast, index, false);
		}
		else
		{
			recordNode(listB, saveLFirst, index, parent, index + 1, rightLast, -1, false);
			index++;
		}
		stuff.one = "" + index;
		stuff.three = "" + rightFirst;
		genPseudo(2, 9, stuff);
		makeSnap(parent, 0, 0, 18);
	}
	hideNode(parent.lChild);
	hideNode(parent.rChild);
 }

 //Method used to generate appropriate pseudo code for merge sort.
 public static void genPseudo(int algo, int line, Holder stuff)
 {
	//Pseudo Code stuff
	if(algo == 1)
	    out.println("VIEW ALGO index.php?line=" + line + "&var[s]=" + stack + "&var[first]=" + stuff.one + "&var[last]=" + stuff.two + "&var[mid]=" + stuff.three);
	else if(algo == 2)
	    out.println("VIEW ALGO merger.php?line=" + line + "&var[s]=" + stack + "&var[index]=" + stuff.one + "&var[lIndex]=" + stuff.two + "&var[rIndex]=" + stuff.three);
 }

 //Method that generates a snapshot.  Each time it is called it calls the questionGen method. 
 //Snap contains a number that distinguishes between different snapshots.
 //Each call to snapShot has a unique number snap.
 public static void makeSnap(BinaryTreeNode nodes, int comp1, int comp2, int snap)
 {
	questionGen(comp1, comp2, snap);
	out.println("recursiveTree");
	out.println("1");
	out.println("Merge Sort");
	out.println("***\\***");
	out.println("Movements: " + swaps + "      Comparisons: " + comparsions);
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
 public static void questionGen(int comp1, int comp2, int snap)
 {
	double rand;
	if(!wfa)
	{
		if(snap == 8)
		{
			rand = Math.random();
			if(rand <= .35)
				tfGen(comp1, comp2);
			else if(rand > .35 && rand <= .7)
				fibGen();
			else
				mcGen(comp1, comp2, snap);
		}
		if(snap == 2 || snap == 4)
			mcGen(comp1, comp2, snap);
	}
	else if(wfa)
		fibAnsGen(snap);

	//If waiting for an answer and a question opportunity is missed, 
	//inform question collection!  This updates the probability of asking questions.
	if((snap == 2 || snap == 4 || snap == 8) && wfa)
		Questions.incPos();
 }

 //Generates a true false question and answer.
 public static void tfGen(int comp1, int comp2)
 {
	double rand;
	tfQuest = new tfQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(tfQuest))
	{
		rand = Math.random();
		if(rand <=.5)
		{
			tfQuest.setQuestionText("A value from the left will merge next.");
			if(comp1 < comp2)
				tfQuest.setAnswer(true);
			else
				tfQuest.setAnswer(false);
		}
		else
		{
			tfQuest.setQuestionText("A value from the right will merge next.");
			if(comp1 > comp2)
				tfQuest.setAnswer(true);
			else
				tfQuest.setAnswer(false);
		}
		Questions.insertQuestion(tfQuest.getID());
	}
	counter++;
 }

 //Generates a fill in the blank question.
 public static void fibGen()
 {
	fibQuest = new fibQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(fibQuest))
	{
		Questions.insertQuestion(fibQuest.getID());
		fibQuest.setQuestionText("There will be ___ comparisons from this point until the end of this call to merge().");
		answerv = comparsions;
		qnumber = 1;
		wfa = true;
	}
	counter++;
 }

 //Generates an answer for the fill in the blank question asked.
 public static void fibAnsGen(int snap)
 {
	//if fib question	
	if(qnumber == 1)
	{	
		if(snap == 13)
		{
			fibQuest.setAnswer("" + (comparsions - answerv));
			wfa = false;
			answerv = 0;
			qnumber = -1;	
		}
	}
 }

 //Generates a multiple choice question and answer.
 public static void mcGen(int comp1, int comp2, int snap)
 {
	mcQuest = new mcQuestion(out, (new Integer(counter)).toString());
	if(Questions.addQuestion(mcQuest))
	{
		mcQuest.setQuestionText("Which of these events will happen next?");
		mcQuest.addChoice("Branch to the left.");
		mcQuest.addChoice("Branch to the right.");
		mcQuest.addChoice("Merge a value up from the left.");
		mcQuest.addChoice("Merge a value up from the right.");
		if(snap == 2)
			mcQuest.setAnswer(1);
		else if(snap == 4)
			mcQuest.setAnswer(2);
		else if(snap == 8)
		{
			if(comp1 > comp2)
				mcQuest.setAnswer(3);
			else
				mcQuest.setAnswer(4);
		}
		counter++;	
		Questions.insertQuestion(mcQuest.getID());
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
