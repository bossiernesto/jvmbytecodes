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

/*Program to create a sho file for the Jhave environment showing a binary search tree insertion
 *structure.
 *
 *
 *Author: Stu Bachner
 *Date: June 14, 2005
 */

package exe.bintreemyles;

import java.io.*;
import java.util.*;
import java.lang.Math.*;
import exe.*;


public class BinarySearchTree{

 //colors
 private static String b = "\\B";
 private static String lb = "\\L";
 private static String m = "\\M";
 
 private static PrintWriter out; // sho file for outputs
 private static BinaryNode root; // root node of tree
 private static BinaryNode currnode; // root node of tree
 private static int l;           // level used in creating snapshots
 private static int bint;        // number used for the blue nodes array to keep track of last node
 private static int[] empty;     // empty array used to clear out arrays when needed
 private static int pitem;	 // used to store item for sending to pseudocode
 private static int pline;	 // used to store line for sending to pseudocode
 private static String pcommand;    // stores the command that is currently being executed (delete or insert)
 private static String ppage;    // variable used to store which pseudocode page to display
 private static String pcall = "main";     // variable used to store callstack for pseudocode
 private static questionCollection Questions;	//needed for questions
 private static qHolder qholder = new qHolder();  // used for storing information about questions to be asked
 private static boolean fibAskedRecently = false;	//used to keep from asking Fill in the blanks questions too often
 private static int[] orderarray;	// used to store array of values for the ordering sequences
 private static int fibq;		// int used to store which type of order is being questions
 private static int treecount = 0;	// value to keep track of the number of values in the tree to keep from asking useless ordering questions
 private static boolean fix;
 private static String execution;     //variable used to store execution title for slides
 private static int marker = 0;
 private static double mcnum = .75;
 
 public static void main(String[] args) throws IOException
 {	
 	pcommand = "";
 	ppage = "BSTinsert.xml";
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	out.println("VIEW DOCS documentation.html");
	root = null;
	
	Questions = new questionCollection(out, (int)(args.length), (int)(args.length/.58));
	orderarray = new int[args.length+1];
	
	//get values to be entered into tree
	String tem = "";
	for(int x = 1; x < args.length; x++)
	{	
		tem = args[x];
		//if there is a d before the number delete it
		if(tem.charAt(0) == 'd' && root != null){
			tem = tem.substring(1);
			root = delete(Integer.parseInt(tem), root);
			pcall = "main";
			//show the tree without colors after the deletion
			if (root != null){
				clearcolors(root);
				pline = -1;
				if(fibAskedRecently){
					snapShot(root, -1);
					fibAskedRecently = false;
					}
				else
					snapShot(root, 4);
				root.color = "\\B";
			}
		}
		//otherwise insert it
		else{
			//pcall = "main";
			root = insert(Integer.parseInt(args[x]), root);
			snapShot(root, -1);
			pcall = "main";
			//show the tree without colors after the insertion
			if (root != null){
				clearcolors(root);
				pline = -1;
				if(fibAskedRecently){
					snapShot(root, -1);
					fibAskedRecently = false;
					}
				else
					snapShot(root, 4);
				root.color = "\\B";
			}
		}
	}
	Questions.writeQuestionsAtEOSF();
	System.out.println("Question count = " + qholder.counter);
	System.out.println("Slide count = " + marker);
	out.close();
 }

	

////Binary Search Tree structure

/**
 * Method used to insert items into the binary search tree in a recursive fashion
 *
 * @param  addItem      value of node to be added
 * @param  r		node of currently evaluating node (searching for a null)
 * @return 		BinaryNode called from the previous call with new node added to subtree
 * 
 */
 public static BinaryNode insert(int addItem, BinaryNode r){
	ppage = "BSTinsert.xml";
	execution = "Inserting value " + addItem + " to tree.";
	pcall = pcall.concat("/insert");
	if(r != null)
		r.color = "\\B";
	pcommand = "insert";
	pitem = addItem;
	pline = 1;
	pcommand = "addItem";
	
	if(r != null && Math.random() > mcnum){
		currnode = r;
		mcnum = .75;
		snapShot(root, 2);
		}
	else{
		snapShot(root, -1);
		mcnum = (mcnum-.01);
		}
	if(r != null)
		r.color = "\\B";	
	//if the node you are searching is null add the new node
	if(r == null){
		pline = 2;
		//snapShot(root, -1);
		BinaryNode temp = new BinaryNode(addItem, null, null);
		temp.color = "\\M";
		treecount++;
		return temp;
	}
	//otherwise, if the addItem is less than current node move to left node and repeat, current node goes light blue left node goes dark blue
	else if(r.element > addItem){
		pline = 3;
		snapShot(root, -1);
		pline = 4;
		snapShot(root, -1);
		if(r.left != null)
			r.color = "\\L";
		else{
			r.color = "\\L";
			r.lcolor = "\\B`~";
			}
		r.left = insert(addItem, r.left);
	}
	//otherwise, if the addItem is greater than current node move to right node and repeat, current node goes light blue right node goes dark blue
	else if(r.element < addItem){
		pline = 3;
		snapShot(root, -1);
		pline = 5;
		snapShot(root, -1);
		pline = 6;
		snapShot(root, -1);
		if(r.right != null)
			r.color = "\\L";
		else{
			r.color = "\\L";
			r.rcolor = "\\B`~";
			}
		r.right = insert(addItem, r.right);
	}
	//if node is already in tree color it red and do nothing else
	else if(r.element == addItem){
		pline = 3;
		snapShot(root, -1);
		pline = 5;
		snapShot(root, -1);
		pline = 7;
		r.color = "\\R";
	}
	return r;
	
 }		

/**
 * Method used to find a node to be deleted from the tree.
 *
 * @param  delItem      value of node to be deleted
 * @param  r		node of currently evaluating node (searching for the delItem)
 * @return		tree after deletion
 */
 public static BinaryNode delete(int delItem, BinaryNode r){
 	
 	pcall = pcall.concat("/delete");
	ppage = "BSTdelete.xml";
	execution = "Deleting value " + delItem + " from tree.";
	pcommand = "delete";
 	pitem = delItem;
 	if(r == null)
 		return null;
 	r.color = "\\B";
 	pline = 1;
 	if(r != null && Math.random() > mcnum){
			currnode = r;
			mcnum = .75;
			snapShot(root, 2);
		}
		else{
			snapShot(root, -1);
			mcnum = (mcnum-.01);
		}
 	//if this node is deleteable node
 	if(r.element == delItem){
 		qholder.question = "Deleting " + delItem + " will require the use of the deleteMin.";
 		pline = 2;
 		if(r.right != null && r.right.left != null)
 			qholder.ansTF = true;
 		else
 			qholder.ansTF = false;
 		snapShot(root,0);
 		treecount--;
 		//if this node's right is null
 		if(r.right == null){
 			pline = 3;
 			snapShot(root, -1);
 			return r.left;
 		}
 		//if this nodes has a right but not a right.left
 		else if(r.right.left == null){
 			pline = 4;
 			snapShot(root, -1);
 			r.element = r.right.element;
 			pline = 5;
 			snapShot(root, -1);
 			r.right = r.right.right;
 			pline = 6;
 			snapShot(root, -1);
 			pline = 7;
 			return r;
 		}
 		//if this node has a right.left move to the right and begin delete min to find replacement node
 		else{
 			pline = 8;
 			snapShot(root, -1);
			r.color = "\\Y";
			pline = 9;
 			snapShot(root, -1);
 			r.element = deleteMin(r.right);
 		}
 	}
 	//if current node is greater than delete item
	else if(r.element > delItem){
		pline = 10;
 		snapShot(root, -1);
 		pline = 11;
 		snapShot(root, -1);
		r.color = "\\L";
		r.left = delete(delItem, r.left);
		return r;
	}
 	//if current node is less than delete item
	else{
 		pline = 10;
 		snapShot(root, -1);
		pline = 13;
 		snapShot(root, -1);
 		pline = 14;
 		snapShot(root, -1);
		r.color = "\\L";
		r.right = delete(delItem, r.right);
		return r;
	}
	return r;
 }

/**
 * Method used to insert items into the binary search tree in a recursive fashion
 *
 * @param  r		node of currently evaluating node (searching for replacement for deleted node)
 * @return		value of node to take the place of deleting node
 */
 public static int deleteMin(BinaryNode r){
 	ppage = "BSTdeleteMin.xml";
 	pcall = pcall.concat("/deleteMin");
 	r.color = "\\G";
 	pline = 1;
	snapShot(root, -1);
	//if there is no left.left return the left value and assign left.right to left
	if(r.left.left == null){
		r.left.color = "\\G";
		pline = 4;
		snapShot(root, -1);
 		int temp = r.left.element;
 		r.left = r.left.right;
 		return temp;
 	}
 	//if there is a left.left continue searching down the left side of the tree
 	else {
 		pline = 5;
		snapShot(root, -1);
		pline = 6;
		snapShot(root, -1);
 		return deleteMin(r.left);
 	}

 }


 private static int countLess = 0;
 private static int countGreater = 0;
 private static int rootelement;
  /*Method used to return an array of the elements in a tree in inorder
 *
 *@param tree  a binary node reference to the root of the current sub-tree
 *
 */
 public static void balanceCount(BinaryNode tree){
	if(tree != null){
		if(tree.element < rootelement)
			countLess++;
		else if(tree.element > rootelement)
			countGreater++;
 		balanceCount(tree.left);
 		balanceCount(tree.right);
 		}
 }




//// End of Binary Search Tree structure


private static int orderi = 0;

/*Method used to return an array of the elements in a tree in preorder
 *
 *@param tree  a binary node reference to the root of the current sub-tree
 *
 */
 public static void preorder(BinaryNode tree){
 	if(tree != null && tree.element == root.element){
 		orderi = 0;
 		orderarray= arrayClear(orderarray);
 		}
	if(tree != null){
 		orderarray[orderi] = tree.element;
 		orderi++;
 		preorder(tree.left);
 		preorder(tree.right);
 		}
 }
 
 /*Method used to return an array of the elements in a tree in postorder
 *
 *@param tree  a binary node reference to the root of the current sub-tree
 *
 */
 public static void postorder(BinaryNode tree){
 	if(tree != null && tree.element == root.element){
 		orderi = 0;
 		orderarray= arrayClear(orderarray);
 		}
	if(tree != null){
 		postorder(tree.left);
 		postorder(tree.right);
 		orderarray[orderi] = tree.element;
 		orderi++;
 		}
 }
 
  /*Method used to return an array of the elements in a tree in inorder
 *
 *@param tree  a binary node reference to the root of the current sub-tree
 *
 */
 public static void inorder(BinaryNode tree){
 	if(tree != null && tree.element == root.element){
 		orderi = 0;
 		orderarray= arrayClear(orderarray);
 		}
	if(tree != null){
 		inorder(tree.left);
 		orderarray[orderi] = tree.element;
 		orderi++;
 		inorder(tree.right);
 		
 		}
 }
 
 //used to clear array of int values
 private static int[] arrayClear(int[] x){
 	for(int i = 0; i < x.length; i++)
 		x[i] = 0;
 	return x;
 }

//////////////////////////////////////////////////////////////////********************************************************/

//methods used to generate questions for the gaigs environment
//////////////////////////////////////////////////////////////////********************************************************/
/**generates a true false question
 *
 */
 public static void tfGen()
 {
 	
	qholder.tfQuest = new tfQuestion(out, (new Integer(qholder.counter)).toString());
	
	//if random number is above .5 and questions class adds the question continue making question
	if(Questions.addQuestion(qholder.tfQuest))
	{
		qholder.counter++;
		Questions.insertQuestion(qholder.tfQuest.getID());
		qholder.tfQuest.setQuestionText(qholder.question);
		qholder.tfQuest.setAnswer(qholder.ansTF);
		System.out.println(marker + " TF");
	}
	
 }
 
 
 /**generates a Multiple Choice question
 *
 */
 public static void mcGen(BinaryNode x)
 {
 	
	qholder.mcQuest = new mcQuestion(out, (new Integer(qholder.counter)).toString());
	
	//if questions class adds the question continue making question
	if((x.left != null || x.right != null) && Questions.addQuestion(qholder.mcQuest))
	{
		qholder.counter++;
		Questions.insertQuestion(qholder.mcQuest.getID());
		System.out.println(marker + " MC");
		qholder.mcQuest.setQuestionText("How well is the subtree rooted at " +x.element+ " balanced?");
		qholder.mcQuest.addChoice("Left Skewed.");
		qholder.mcQuest.addChoice("Right Skewed.");
		qholder.mcQuest.addChoice("Balanced.");
		mcAnsGen(x);
	}
 }

 /**generates a Multiple Choice answer using an external variable in the qHolder class
 *
 */
 public static void mcAnsGen(BinaryNode x)
 {	
 	countLess = 0;
 	countGreater = 0;
 	rootelement = x.element;
 	balanceCount(x);
 	if(countLess > countGreater)
 		qholder.mcQuest.setAnswer(1);
 	else if(countLess < countGreater)
 		qholder.mcQuest.setAnswer(2);
 	else
 		qholder.mcQuest.setAnswer(3);
	
 }


 /**generates a fill in the blank question about either pre, post, or in orderings of the tree.
 *This method also calls the ordering methods to retreive their own answers.
 *
 */
 public static void fibGen()
 {
 	
	double rand;
	rand = Math.random();
	String answer = "";
	qholder.fibQuest = new fibQuestion(out, (new Integer(qholder.counter)).toString());
	//if questions class adds the question continue making question
	   if(Questions.addQuestion(qholder.fibQuest))
	   {
		rand = Math.random();
		System.out.println(marker + " FIB");
		fibAskedRecently = true;
		if(rand <=.29)
		{
			qholder.fibQuest.setQuestionText("What is the inordering of the current tree? (seperate elements with spaces)");
			inorder(root);
			int i = 0;
			
			//build answer from orderarray 
			do{
				answer = answer + " " + orderarray[i];
				i++;
			}while(i < orderarray.length && orderarray[i] != orderarray[i+1]);
			qholder.fibQuest.setAnswer(answer);
		}
		else if(.30 <= rand && rand < .6)
		{
			qholder.fibQuest.setQuestionText("What is the preordering of the current tree? (seperate elements with spaces)");
			preorder(root);
			int i = 0;
			
			//build answer from orderarray 
			do{
				answer = answer + " " + orderarray[i];
				i++;
			}while(i < orderarray.length && orderarray[i] != orderarray[i+1]);
			qholder.fibQuest.setAnswer(answer);
		}
		else
		{
			qholder.fibQuest.setQuestionText("What is the postordering of the current tree? (seperate elements with spaces)");
			postorder(root);
			int i = 0;
			
			//build answer from orderarray 
			do{
				answer = answer + " " + orderarray[i];
				i++;
			}while(i < orderarray.length && orderarray[i] != orderarray[i+1]);
			qholder.fibQuest.setAnswer(answer);
		}
		qholder.counter++;
		Questions.insertQuestion(qholder.fibQuest.getID());
	   }
 }


//////////////////////////////////////////////////////////////////********************************************************/

//Methods used to create snap shots for the Gaigs environment
//////////////////////////////////////////////////////////////////********************************************************/
/**
 * Method used to begin printing a binary tree to the gaigs environment using
 * the recursiveTree class visualization.
 *
 * @param  top      the top node of the tree to be printed off
 * @param  qtype    the int value representing the type of question to possibly ask for this slide (-1 - none, 0 - TF, 1 - TF answer, 2 - Multiple Choice, 3 - Multiple Choice answer, 4 - Fill in the Blank)
 */
 public static void snapShot(BinaryNode top, int qtype){
	//Snapshot output
	out.println("VIEW ALGO index.php?file=" + ppage + "&line=" + pline + "&var[item]=" + pitem + "&var[command]=" + pcommand + "&var[stack]=" + pcall);
	marker++;
	//System.out.println(marker + "  count = " + treecount);
	//if snapShot is called to create a question figure out which type and make it
	if(qtype != -1){
		if(qtype == 0)
			tfGen();
		else if(qtype == 2){
			mcGen(currnode);
			}
		else if(qtype == 4 && treecount >= 4)
			fibGen();
	}
	out.println("recursiveTree .015 .03");
	out.println("1 3.0 2.5");
	out.println("This is a Binary Search Tree.");
	// The next blank println is a kludge to work around the graphic line that otherwise clobbers the text
	out.println(" ");
	out.println("***\\***");
	out.println(execution);
	if(top != null){               // if there is a root node currently
		if(top.color.compareTo("\\B") == 0){
			out.println(pitem + ":");
		}
		else{
			out.println(" ");
		}
		out.println("1");
		out.println(l);
		out.println("R");
		out.print(top.color);
		out.println(top.element);
		if (top.left != null){					//if there is a left child continue down that path looking for children to print
			snapShotL(top.left);
		}
		else{							//if left node is null place a grey termination node there
			if(top.lcolor.compareTo("\\B`~") == 0){
				out.println(pitem + ":");
			}
			else{
				out.println(" ");
			}				
			out.println("1");
			out.println(l+1);
			out.println("L");
			out.println(top.lcolor);
		}
		if (top.right != null){				//if there is a right node condinue down that path looking for children to pring
			snapShotR(top.right);
		}
		else{						//if right node is null place a grey termination node there
			if(top.rcolor.compareTo("\\B`~") == 0){
				out.println(pitem + ":");
			}
			else{
				out.println(" ");
			}
			out.println("1");
			out.println(l+1);
			out.println("R");
			out.println(top.rcolor);
		}
	}
	else{                       					//to eneable a blank screen to have the title with no nodes present
		out.println(" ");
		out.println("1");
		out.println(l);
		out.println("R");
		out.println("!1");
	}
	out.println("***^***");
 } 


/**
 * Method used to print the left children of a binary tree to the gaigs environment using
 * the recursiveTree class visualization.
 *
 * @param  top      the top node of the tree to be printed off
 * 
 */
 public static void snapShotL(BinaryNode top){
	l++;
	if(top.color.compareTo("\\B") == 0){
		out.println(pitem + ":");
	}
	else{
		out.println(" ");
	}
	out.println("1");
	out.println(l);
	out.println("L");
	out.print(top.color);
	out.println(top.element);				
	if (top.left != null){                       //continue down left tree
		snapShotL(top.left);
	}
	else{	
		if(top.lcolor.compareTo("\\B`~") == 0){
			out.println(pitem + ":");
		}
		else{
			out.println(" ");
		}						//place termination node in
		out.println("1");
		out.println(l+1);
		out.println("L");
		out.println(top.lcolor);
	}
	if (top.right != null){	  			//continue down right tree				
		snapShotR(top.right);
	}
	else{	
		if(top.rcolor.compareTo("\\B`~") == 0){
			out.println(pitem + ":");
		}
		else{
			out.println(" ");
		}						//place termination node in
		out.println("1");
		out.println(l+1);
		out.println("R");
		out.println(top.rcolor);
	}
	l--;
	
 }

/**
 * Method used to print the right children of a binary tree to the gaigs environment using
 * the recursiveTree class visualization.
 *
 * @param  top      the top node of the tree to be printed off
 * 
 */
 public static void snapShotR(BinaryNode top){
	l++;
	if(top.color.compareTo("\\B") == 0){
		out.println(pitem + ":");
	}
	else{
		out.println(" ");
	}
	out.println("1");
	out.println(l);
	out.println("R");
	out.print(top.color);
	out.println(top.element);
	if (top.left != null){					//continue down left tree
		snapShotL(top.left);
	}
	else{							//place termination node in
		if(top.lcolor.compareTo("\\B`~") == 0){
			out.println(pitem + ":");
		}
		else{
			out.println(" ");
		}							
		out.println("1");
		out.println(l+1);
		out.println("L");
		out.println(top.lcolor);
	}
	if (top.right != null){					//continue down right tree
		snapShotR(top.right);
	}
	else{							//place termination node in
		if(top.rcolor.compareTo("\\B`~") == 0){
			out.println(pitem + ":");
		}
		else{
			out.println(" ");
		}
		out.println("1");
		out.println(l+1);
		out.println("R");
		out.println(top.rcolor);
	}
	l--;
 }
////end snapshot stuff


/**
 * Method used to clear the color of all the nodes in a root
 *
 * @param  r      the top node of the tree to be cleaned
 * 
 */
 public static void clearcolors(BinaryNode r){
 	r.color = "";
 	r.lcolor = "\\SN";
 	r.rcolor = "\\SN";
 	if(r.left != null)
 		clearcolors(r.left);
 	if(r.right != null)
	 	clearcolors(r.right);
 }
 	
}






/**Class designed to hold information used in question creation.
*/
class qHolder
{   
    String item;
    String question;
    String ansFIB;
    int fibCount = 0;
    boolean fibAskedRecently;
    boolean ansTF;
    int ansMC;
    boolean wfaTF = false;		//waiting for TF answer
    boolean wfaMC = false;		//waiting for MC answer
    boolean wfaFIB = false;         //waiting for FIB answer
    fibQuestion fibQuest;    // used for creating fill in the blank questions
    tfQuestion tfQuest;	//used for creating true/false questions
    mcQuestion mcQuest; 	//used for creating multiple choice questions
    int counter = 0;		//used for identifying questions
    
    /**Constructor
    */
    public qHolder()
    {
	question = "";
	ansFIB = "";
    }
}





// BinaryNode object
class BinaryNode
    {
            // Constructors
        BinaryNode( int theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( int theElement, BinaryNode lt, BinaryNode rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            lcolor   = "\\SN";
            rcolor   = "\\SN";
        }

            // Friendly data; accessible by other package routines
        int element;      // The data in the node
        BinaryNode left;         // Left child
        BinaryNode right;        // Right child
        String color = "";		 // color of node
        String lcolor = "";		 // color of node left (for termination only)
        String rcolor = "";		 // color of node right (for termination only)
}
