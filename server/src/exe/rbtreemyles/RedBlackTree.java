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

/*Program to create a sho file for the Jhave environment showing a Red Black Tree insertion
 * and deletion structure.
 *
 *
 *Author: Stu Bachner
 *Date: June 23, 2005
 */

// TN note (10/23/05) -- all of stu's Boolean had to be changed to
// boolean to make this compile.  Hmmm ...

package exe.rbtreemyles;

import java.io.*;
import java.util.*;
import java.lang.Math.*;
import exe.*;


public class RedBlackTree{

 //colors
 private static int red = 1;
 private static int black = 0;
 
 private static PrintWriter out; // sho file for outputs
 private static BinaryNode root; // root node of tree
 private static int l;           // level used in creating snapshots
 private static String pitem;	 // used to store item for sending to pseudocode
 private static int pline;	 // used to store line for sending to pseudocode
 private static String pcommand;    // stores the command that is currently being executed (delete or insert)
 private static String ppage;    // variable used to store which pseudocode page to display
 private static String pcall = "main";     // variable used to store callstack for pseudocode
 private static String pv;		 // variable to store v value to send for remedyDoubleRed and remedyDoubleBlack
 private static String px;		 // variable to store x value to send for remedyDoubleRed and remedyDoubleBlack
 private static String py;		 // variable to store y value to send for remedyDoubleRed and remedyDoubleBlack
 private static String execution;     //variable used to store execution title for slides
 private static questionCollection Questions;	//needed for questions
 private static qHolder qholder = new qHolder();  // used for storing information about questions to be asked
 private static boolean fibAskedRecently = false;	//used to keep from asking Fill in the blanks questions too often
 private static int[] orderarray;	// used to store array of values for the ordering sequences
 private static int fibq;		// int used to store which type of order is being questions
 private static int treecount = 0;	// value to keep track of the number of values in the tree to keep from asking useless ordering questions
 private static boolean fix;
 
 public static void main(String[] args) throws IOException
 {	
 	pcommand = "";
 	ppage = "blank.xml";
	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));
	//	out.println("VIEW DOCS http://localhost/jhave/html_root/doc/RB_treeMyles/documentation.html");
	out.println("VIEW DOCS documentation.html");
	root = null;
	Questions = new questionCollection(out, (args.length-2), (args.length-2)*2);
	//get values to be entered into tree
	String tem = "";
	orderarray = new int[args.length+1];
	for(int x = 1; x < args.length; x++)
	{	
		tem = args[x];
		//if there is a d before the number delete it
		if(tem.charAt(0) == 'd' && root != null){
			tem = tem.substring(1);
			RBdelete(Integer.parseInt(tem), root);
			pcall = "main";
			pline = -1;
		}
		else{
			fix = true;
			root = RBinsert(Integer.parseInt(args[x]), root, null);
			if(fix)
				RBfix(Integer.parseInt(args[x]), root);
			pcall = "main";
		}
		
	}
	pline = -1;
	ppage = "blank.xml";
	snapShot(root, 4);	
	Questions.writeQuestionsAtEOSF();
	out.close();
 }


//////////////////////////////////////////////////////////////////********************************************************/

//methods used when working with a Red/Black tree
//////////////////////////////////////////////////////////////////********************************************************/
/**
 * Method to insert a new node in the tree
 *
 * @param  addItem      the value being added to the tree
 * @param  r      	the top of the subtree currently being examined to find a location for the new node 
 * @param  p     	the parent node of the current r node 
 * @return      	the sub tree after the insertion, gets passed back through the call stack till the tree is rebuilt 
 * 
 */
   public static BinaryNode RBinsert(int addItem, BinaryNode r, BinaryNode p){
   	execution = "Inserting " + addItem + " into tree";
   	qholder.question = "Inserting " + addItem + " will cause a problem?";
 	qholder.item = "the insertion of " +addItem;
	ppage = "RBinsert.xml";
 	pcall = pcall.concat("/RBinsert");
	pcommand = "insert";
	pitem = "" + addItem;
	if(r !=null)
		r.color = "\\B";
	pv = null;
	if(r != null)
		pv = "" + r.element;
	pline = 1;
	pcommand = "addItem";
	
	//uses a random generator as well as the fibAskedRecently variable and the number of items to determine whether to 
	//make a fill in the blank question
	if(Math.random() > .75 && fibAskedRecently != true && treecount > 4){
		snapShot(root, 4);
		qholder.fibCount++;
		fibAskedRecently = true;
		}
	else{
		snapShot(root, -1);
		fibAskedRecently = false;
		}
		
 	//if the node you are searching is null add the new node
	if(r == null){
		treecount++;
		pline = 2;
		snapShot(root, 0);
		BinaryNode temp = new BinaryNode(addItem, null, null);
		temp.parent = p;
		if(p == null){
			temp.icolor = black;  //if add item is root make it black
			}
		else
			temp.icolor = red;   //else make it red
		
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
		r.left = RBinsert(addItem, r.left, r);
		clearcolors(root);
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
		r.right = RBinsert(addItem, r.right, r);
		clearcolors(root);
	}
	
	//if node is already in tree color it red and do nothing else
	else if(r.element == addItem){
		pline = 7;
		if(qholder.wfaTF)
 			tfAnsGen(false);
 		snapShot(root, -1);
 		fix = false;
	}
	return r;
 }
 
 /**
 * Method to call the remedyDoubleRed with the location of the most recently added node.
 *
 * @param  addItem      the value being added to the tree
 * @param  r      	the top of the subtree currently being examined to find a location for the new node 
 * 
 */
 public static void RBfix(int addItem, BinaryNode r){
	if(addItem == root.element){}
	else if(addItem == r.element && r.left == null && r.right == null){
		pline = 8;
		snapShot(root, -1);
		remedyDoubleRed(r);
	}
	else if(addItem > r.element){
		RBfix(addItem, r.right);
	}
	else if(addItem < r.element){
		RBfix(addItem, r.left);
	}
	else{}
 }

/**
 * Method to examine a tree after an insertion and fix any errors that may have arrisen from
 * the insertion of the new node.
 *
 * @param  z      the node you want to have examined from up the rest of the tree 
 * 
 */
 public static void remedyDoubleRed(BinaryNode z){
	pcall = pcall.concat("/remedyDoubleRed");
 	pitem = "" + z.element;
	BinaryNode v = z.parent;
	pv = "" + v.element;
 	execution = "Fixing any double red issues in tree";
 	qholder.question = "Is node " + z.element + " a problem node?";
	ppage = "RemedyDoubleRed.xml";
	pline = 2;
	snapShot(root, 2);
	
	//the added nodes parent is the root do nothing
 	if(v.parent == null){
 		pline = 3;
 		if(qholder.wfaTF)
 			tfAnsGen(false);  //set the TF answer to false if there is a TF question asked
 		qholder.ansMC = 3;        // set variable to set MC question to answer 3
 		snapShot(root, 3);
		return;
 	}
 	
 	//if the added nodes parent is not red
 	if(v.icolor != red){
 		pline = 4;
 		if(qholder.wfaTF)
 			tfAnsGen(false);  //set TF answer to false if TF asked
 		snapShot(root, -1);
 		pline = 5;
 		qholder.ansMC = 3; 	// set variable to set MC question to answer 3
 		snapShot(root, 3);	// send snapshot to set MC answer
		return;
 	}
 	if(v.findSib() == null || v.findSib().icolor != red){
 		pline = 4;
 		if(qholder.wfaTF)
 			tfAnsGen(true);	//set TF answer to false if TF asked
		snapShot(root, -1);
		pline = 6;
		qholder.ansMC = 1;
 		snapShot(root, 3);	// send snapshot to set MC answer
 		pline = 7;
 		snapShot(root, -1);
		v = restructure(z);
 		snapShot(root, -1);
 		pline = 8;
 		snapShot(root, -1);
 		v.icolor = black;
 		snapShot(root, -1);
 		if(v.left != null){
 			pline = 9;
 			snapShot(root, -1);
 			v.left.icolor = red;
 			snapShot(root, -1);
 		}
 		if(v.right != null){
 			pline = 10;
 			snapShot(root, -1);
 			v.right.icolor = red;
 			snapShot(root, -1);
 		}
 	}
 	else{
 		pline = 4;
 		if(qholder.wfaTF)
 			tfAnsGen(true);	//set TF answer to false if TF asked
 		snapShot(root, -1);
 		pline = 6;
 		qholder.ansMC = 2;
 		snapShot(root, 3);	// send snapshot to set MC answer
 		pline = 11;
 		snapShot(root, -1);
 		pline = 12;
 		snapShot(root, -1);
 		v.icolor = black;
 		snapShot(root, -1);
 		if(v.findSib() != null){
			pline = 13;
 			snapShot(root, -1);
 			v.findSib().icolor = black;
 			snapShot(root, -1);
 		}
 		BinaryNode u = v.parent;
 		if(u.parent == null){
 			pline = 15;
 			snapShot(root, -1);
 			pline = 16;
 			snapShot(root, -1);
 			return;
 		}
 		pline = 17;
 		snapShot(root, -1);
 		u.icolor = red;
 		snapShot(root, -1);
 		pline = 18;
 		snapShot(root, -1);
 		qholder.item = "previous recoloring";
 		remedyDoubleRed(u);
 	}
 
 }
 
/**
 * Method used to find a node to be deleted from the tree.
 *
 * @param  delItem      value of node to be deleted
 * @param  r		node of currently evaluating node (searching for the delItem)
 * @return		tree after deletion
 */
 public static void RBdelete(int delItem, BinaryNode r){
 	clearcolors(root);
	if(r==null)
 		return;
 	execution = "Deleting " + delItem + " from tree";
 	qholder.item = "deleting " +delItem;
	pcall = pcall.concat("/RBdelete");
	qholder.question = "Is the deletion of node " + delItem + " going to cause the tree to break a rule?";
	ppage = "RBdelete.xml";
	pcommand = "delete";
 	pitem = "" + delItem;
 	r.color = "\\B";
 	pv = "" + r.element;
 	//if this node is deleteable node
 	if(r.element == delItem){
 		pline = 1;
 		
 		//uses a random generator as well as the fibAskedRecently variable and the number of items to determine whether to 
		//make a fill in the blank question, else standard snapshot.
 		if(Math.random() > .75 && fibAskedRecently != true && treecount > 4){
			snapShot(root, 4);
			qholder.fibCount++;
			fibAskedRecently = true;
			}
		else{
			snapShot(root, -1);
			fibAskedRecently = false;
		}
 		pline = 2;
 		snapShot(root, 0);
 		int rcol = r.icolor;
 		BinaryNode t;
 		treecount--;
 		
 		//if this node's right is null
 		if(r.right == null){
 			pline = 3;
 			snapShot(root, 2);
 			BinaryNode y = r.findSib();
			BinaryNode x = r.parent;
			pline = 6;
			snapShot(root, -1);
			if(r.parent == null)
				root = r.left;
			else if(r.parent.left == r)
				r.parent.left = r.left;
			else
				r.parent.right = r.left;
			if(r.left != null)
				r.left.parent = r.parent;
			snapShot(root, -1);
 			r = r.left;
 			pline = 7;
 			snapShot(root, -1);
 			if(rcol != red){
				RBDelFix(r, y, x);
				if(qholder.wfaTF)
 					tfAnsGen(true);   //set TF answer to false
				}
			else{
				ppage = "RBdelFix.xml";
				execution = "Fixing any double black issues in tree";
				pitem = null;
				pv = null;
				px = null;
				py = null;
				pline = 2;
				snapShot(root, -1);
				pline = 15;
				snapShot(root, -1);
				pline = 22;
				snapShot(root, -1);
				pline = 31;
				if(qholder.wfaTF)
 					tfAnsGen(false);	//set TF answer to false
				qholder.ansMC = 3;		//set MC anser to 3
 				snapShot(root, 3);
				pline = 32;
				snapShot(root, -1);
			}
 		}
 		
 		//if this nodes has a right but not a right.left
 		else if(r.right.left == null){
 			pline = 8;
 			snapShot(root, 2);
 			pline = 9;
 			snapShot(root, -1);
 			r.element = r.right.element;
 			//r.icolor = r.right.icolor;
 			if(r.right.right != null)
 				r.right.right.parent = r;
 			r.right = r.right.right;
 			BinaryNode Sib;
 			snapShot(root, -1);
 			if(r == r.parent.right)
 				Sib = r.parent.left;
 			else
 				Sib = r.parent.right;
 			pline = 10;
 			snapShot(root, -1);
 			if(rcol != red && r.right != null){
				RBDelFix(r, Sib, r.parent);
				if(qholder.wfaTF)
 					tfAnsGen(true);	//set TF answer to true
 			}
			else{
				ppage = "RBdelFix.xml";
				execution = "Fixing any double black issues in tree";
				pitem = null;
				pv = null;
				px = null;
				py = null;
				pline = 2;
				snapShot(root, -1);
				pline = 15;
				snapShot(root, -1);
				pline = 22;
				snapShot(root, -1);
				pline = 31;
				if(qholder.wfaTF)
 					tfAnsGen(false);	//set TF answer to false
				qholder.ansMC = 3;		//set MC anser to 3
 				snapShot(root, 3);
				pline = 32;
				snapShot(root, -1);
			}
 			
 		}
 		//if this node has a right.left move to the right and begin delete min to find replacement node
 		else{
 			pline = 11;
 			snapShot(root, 2);
 			pline = 13;
 			snapShot(root, -1);
 			BinaryNode temp = deleteMin(r.right);
 			r.element = temp.element;
 			r.icolor = temp.icolor;
 			pline = 14;
 			snapShot(root, -1);
 			if(rcol != red){
 				RBDelFix(temp.parent.left, temp.parent.right, temp.parent);
 				if(qholder.wfaTF)
 					tfAnsGen(true);		//set TF answer to true
 				}
 			else{
				ppage = "RBdelFix.xml";
				execution = "Fixing any double black issues in tree";
				pitem = null;
				pv = null;
				px = null;
				py = null;
				pline = 2;
				snapShot(root, -1);
				pline = 15;
				snapShot(root, -1);
				pline = 22;
				snapShot(root, -1);
				pline = 32;
				snapShot(root, -1);
				pline = 31;
				if(qholder.wfaTF)
 					tfAnsGen(false);	//set TF answer to false
				qholder.ansMC = 3;		//set MC anser to 3
 				snapShot(root, 3);
				pline = 32;
				snapShot(root, -1);
			}
 		}
 	}
 	
 	//if current node is greater than delete item
	else if(r.element > delItem){
		pline = 1;
 		snapShot(root, -1);
		pline = 15;
 		snapShot(root, -1);
 		pline = 16;
 		snapShot(root, -1);
		RBdelete(delItem, r.left);   //r.left = 
		if(r.left != null)
			r.left.parent = r;
		
	}
	
 	//if current node is less than delete item
	else{
		pline = 1;
 		snapShot(root, -1);
 		pline = 15;
 		snapShot(root, -1);
		pline = 17;
 		snapShot(root, -1);
 		pline = 18;
 		snapShot(root, -1);
		RBdelete(delItem, r.right);   //r.right = 
		if(r.right != null)
			r.right.parent = r;
		
	}
		
 }


/**
 * Method used to insert items into the binary search tree in a recursive fashion
 *
 * @param  r		node of currently evaluating node (searching for replacement for deleted node)
 * @return		value of node to take the place of deleting node
 */
 public static BinaryNode deleteMin(BinaryNode r){
	//if there is no left.left return the left value and assign left.right to left
	if(r.left.left == null){
 		BinaryNode temp = r.left;
 		r.left = r.left.right;
 		return temp;
 	}
 	//if there is a left.left continue searching down the left side of the tree
 	else {
 		return deleteMin(r.left);
 	}

 }
 

/**
 * Method to examine a tree after a deletion and fix any errors that may have arrisen from
 * the delete of the given node.
 *
 * @param  r      the node you want to have examined from up the rest of the tree 
 * 
 */
 public static void RBDelFix(BinaryNode r, BinaryNode y, BinaryNode x){
 	if(r == null)
 		return;
 	execution = "Fixing any double black issues in tree";
 	pcall = pcall.concat("/remedyDoubleBlack");
 	ppage = "RBdelFix.xml";
	BinaryNode z = null;
 	pitem = null;
	pv = null;
	px = null;
	py = null;
	if(r != null)
 		pitem = "" + r.element;
 	if(y != null)
 		py = "" + y.element;
 	if(x != null)
 		px = "" + x.element;
 	
 	//if sibling is red
	if(y != null && y.icolor ==red){
 			pline = 2;
			qholder.ansMC = 1;
 			snapShot(root, 3);
 			BinaryNode t;
 			if(y == x.right){
 				pline = 4;
				snapShot(root, -1);
				pline = 5;
				snapShot(root, -1);
 				z = y.right;
 				pline = 6;
				snapShot(root, -1);
 				t = y.left;
 			}
 			else{
 				pline = 7;
				snapShot(root, -1);
				pline = 8;
				snapShot(root, -1);
 				z = y.left;
 				pline = 9;
				snapShot(root, -1);
 				t = y.right; 				
 			}
 			pv = "" + z.element;
 			if(z != null){
 				pline = 10;
				snapShot(root, -1);
				pline = 11;
				snapShot(root, -1);
 				restructure(z);
				snapShot(root, -1);
 			}
 			pline = 12;
			snapShot(root, -1);
 			y.icolor = black;
 			snapShot(root, -1);
 			pline = 13;
 			snapShot(root, -1);
 			x.icolor = red;
 			snapShot(root, -1);
 			pline = 14;
 			snapShot(root, -1);
 			RBDelFix(r, t, x);
 		
 	}
 	
 	//sibling node is black but has a red child
 	else if(y != null && y.icolor == black && y.redChild() != null){
 		pline = 15;
 		qholder.ansMC = 1;
 		snapShot(root, 3);
 		pline = 16;
 		snapShot(root, -1);
 		z = y.redChild();
 		pv = "" + z.element;
 		int oldColor = x.icolor;
 		pline = 18;
 		snapShot(root, -1);
 		z = restructure(z);
 		snapShot(root, -1);
 		pline = 19;
 		snapShot(root, -1);
 		z.icolor = oldColor;
 		snapShot(root, -1);
 		pline = 20;
 		snapShot(root, -1);
 		z.left.icolor = black;
 		snapShot(root, -1);
 		pline = 21;
 		snapShot(root, -1);
 		z.right.icolor = black;
 		snapShot(root, -1);
 		
 	}
 	
 	//if sibling node is black with no red child
 	else if(y != null && y.icolor == black){
 		pline = 22;
 		qholder.ansMC = 2;
 		snapShot(root, 3);
 		if(r!= null){
 			pline = 24;
 			snapShot(root, -1);
 			r.icolor = black;
 			snapShot(root, -1);
 			//return;
 		}
 		pline = 25;
 		snapShot(root, -1);
 		y.icolor = red;
 		snapShot(root, -1);
 		if(x.icolor == red){
 			pline = 26;
 			snapShot(root, -1);
			pline = 27;
 			snapShot(root, -1);
			x.icolor = black;
			snapShot(root, -1);
		}
		else if(x.parent != null){
			pline = 29;
			snapShot(root, -1);
			x.icolor = black;
			snapShot(root, -1);
			pline = 30;
			snapShot(root, -1);
 			RBDelFix(x, x.findSib(), x.parent);
 			}
 	}
 	
 	//if there is no problem ( only a marker for answers and snapshots)
 	else{
 		pline = 31;
 		qholder.ansMC = 3;
 		snapShot(root, 3);
 		pline = 32;
 		snapShot(root, -1);
 	}
 
 }



 /**
 * Method to restructure trees/subtrees as needed to get them balanced by 
 * shifting nodes around so that straight lines of nodes as well as hooks in 
 * lines get turned into more balanced distributions.
 *
 * @param  x      the node you want to have its parent and grandparent examined for balancing
 * @return 	  the restructured subtree's top node
 * 
 */
 public static BinaryNode restructure(BinaryNode x){	
   if(x != null){
   	//if restructuring set is right skewed (all right child of their parent)
	if(x == x.parent.right && x.parent == x.parent.parent.right){
		BinaryNode gp = x.parent.parent;
		BinaryNode p = x.parent;
		gp.right = p.left;
		if(gp.right != null)
			gp.right.parent = gp;
		p.parent = gp.parent;
		gp.parent = p;
		p.left = gp;
		if(p.parent == null)
			root = p;
		else if(p.parent.left.element == gp.element)
			p.parent.left = p;
		else
			p.parent.right = p;
		return p;
		
	}
	
	//if restructuring set is left skewed
	else if(x == x.parent.left && x.parent == x.parent.parent.left){
		BinaryNode gp = x.parent.parent;
		BinaryNode p = x.parent;
		gp.left = p.right;
		if(gp.left != null)
			gp.left.parent = gp;
		p.parent = gp.parent;
		gp.parent = p;
		p.right = gp;
		if(p.parent == null)
			root = p;
		else if(p.parent.left.element == gp.element)
			p.parent.left = p;
		else
			p.parent.right = p;
		return p;
	}
	
	//if the restructuring set goes right than left on a bent downward path
	else if(x == x.parent.left && x.parent == x.parent.parent.right){
		BinaryNode gp = x.parent.parent;
		BinaryNode p = x.parent;
		BinaryNode t2 = x.left;
		BinaryNode t3 = x.right;
		p.left = x.right;
		if(p.left != null)
			p.left.parent = p;
		x.right = p;
		x.parent = p.parent;
		p.parent.right = x;
		p.parent = x;
		gp.right = x.left;
		x.left = gp;
		x.parent = gp.parent;
		gp.parent = x;
		if(x.parent == null)
			root = x;
		else if(x.parent.left.element == gp.element)
			x.parent.left = x;
		else
			x.parent.right = x;
		return x;
	}
	
	//if the restructuring set goes left than right on a bent downward path
	else{
		BinaryNode gp = x.parent.parent;
		BinaryNode p = x.parent;
		p.right = x.left;
		if(p.right != null)
			p.right.parent = p;
		x.left = p;
		x.parent = p.parent;
		p.parent.left = x;
		p.parent = x;
		gp.left = x.right;
		x.right = gp;
		x.parent = gp.parent;
		gp.parent = x;
		if(x.parent == null)
			root = x;
		else if(x.parent.left.element == gp.element)
			x.parent.left = x;
		else
			x.parent.right = x;
		return x;
	}
    }
    return null;
 }



private static int orderi = 0;

/*Method used to return an array of the elements in a tree in preorder
 *
 *@param tree  a binary node reference to the root of the current sub-tree
 *
 */
 public static void preorder(BinaryNode tree){
 	if(tree != null && tree.parent == null){
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
 	if(tree != null && tree.parent == null){
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
 	if(tree != null && tree.parent == null){
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
	if(Math.random() > .5 && Questions.addQuestion(qholder.tfQuest))
	{
		qholder.counter++;
		Questions.insertQuestion(qholder.tfQuest.getID());
		qholder.tfQuest.setQuestionText(qholder.question);
		System.out.println(" TF");
		qholder.wfaTF = true;
	}
	
 }
 
 /**generates a true false answer using a parameter
 *
 *@param ans	answer to set true false question to
 */
 public static void tfAnsGen(boolean ans)
 {
		qholder.tfQuest.setAnswer(ans);
		qholder.wfaTF = false;
 }

 /**generates a true false answer using an external variable in the qHolder class
 *
 */
 public static void tfAnsGen()
 {
		qholder.tfQuest.setAnswer(qholder.ansTF);
		qholder.wfaTF = false;
 }
 
 /**generates a Multiple Choice question
 *
 */
 public static void mcGen()
 {
 	
	qholder.mcQuest = new mcQuestion(out, (new Integer(qholder.counter)).toString());
	
	//if questions class adds the question continue making question
	if(Questions.addQuestion(qholder.mcQuest))
	{
		qholder.counter++;
		Questions.insertQuestion(qholder.mcQuest.getID());
		System.out.println(" MC");
		qholder.mcQuest.setQuestionText("Of these operations, which will fix the problem caused by " + qholder.item + "?");
		qholder.mcQuest.addChoice("Restructuring the nodes.");
		qholder.mcQuest.addChoice("Recoloring the current node and its parent and sibling.");
		qholder.mcQuest.addChoice("There is no problem.");
		qholder.wfaMC = true;
	}
 }

 /**generates a Multiple Choice answer using an external variable in the qHolder class
 *
 */
 public static void mcAnsGen()
 {
	qholder.mcQuest.setAnswer(qholder.ansMC);
	qholder.wfaMC = false;
	
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
		System.out.println(" FIB");
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
     //	out.println("VIEW ALGO http://localhost/jhave/html_root/doc/RB_treeMyles/index.php?file=" + ppage + "&line=" + pline + "&var[item]=" + pitem + "&var[command]=" + pcommand + "&var[stack]=" + pcall + "&var[v]=" + pv + "&var[x]=" + px + "&var[y]=" + py);
	out.println("VIEW ALGO index.php?file=" + ppage + "&line=" + pline + "&var[item]=" + pitem + "&var[command]=" + pcommand + "&var[stack]=" + pcall + "&var[v]=" + pv + "&var[x]=" + px + "&var[y]=" + py);
	
	//if snapShot is called to create a question figure out which type and make it
	if(qtype != -1){
		if(qtype == 0 && !qholder.wfaTF)
			tfGen();
		else if(qtype == 1 && qholder.wfaTF)
			tfAnsGen();
		else if(qtype == 2 && !qholder.wfaMC && !qholder.wfaTF){
			mcGen();
			}
		else if(qtype == 3 && qholder.wfaMC)
			mcAnsGen();
		else if(qtype == 4)
			fibGen();
	}
	out.println("recursiveTree .015 .03");
	out.println("1 3.0 2.5");
	out.println("This is a Red/Black Tree.");
	//out.println("White Node = Corresponds to a Black Node | Red Node = Corresponds to a Red Node");
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
		out.print(top.color());
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
	out.print(top.color());
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
	out.print(top.color());
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

//used to clear array of int values
 private static int[] arrayClear(int[] x){
 	for(int i = 0; i < x.length; i++)
 		x[i] = 0;
 	return x;
 }


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




//////////////////////////////////////////////////////////////////********************************************************/

//Object Used to store nodes of tree
//////////////////////////////////////////////////////////////////********************************************************/
class BinaryNode
    {
   	private static int red = 1;
 	private static int black = 0;
 
        /**Constructor for leaf node
        *
        *@param theElement	the value the node will have
        */
        BinaryNode( int theElement )
        {
            this( theElement, null, null );
        }


	/**Constructor for other nodes
	*@param theElement	the value the node will have
	*@param lt		left child assignment
	*@param rt		right child assignment
	*/
        BinaryNode( int theElement, BinaryNode lt, BinaryNode rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            lcolor   = "\\SN";
            rcolor   = "\\SN";
        }
        
        /**Method to return the sibling node of this node
        *
        *@return BinaryNode
        */
        BinaryNode findSib(){
		if(this == this.parent.right){
        		if(this.parent.left != null)
        			return this.parent.left;
        		else
        			return null;
        	}
        	else{
        		if(this.parent.right != null)
        			return this.parent.right;
        		else
        			return null;
        	}
        }
        
        /**Method to return the red child of this node if there is one and 
        *null if there isn't one.
        *
        *@return BinaryNode of child
        */
        BinaryNode redChild(){
        	if(this.right != null  && this.right.icolor == red)
        		return this.right;
        	else if(this.left != null && this.left.icolor == red)
        		return this.left;
        	else
        		return null;
        
        }

	/**Method to return color of node according to its icolor number
	*
	*@return String	representing the appropriate output for that color
	*/
        String color(){
        	if(this.icolor == 1)
        		return "\\R";
        	else if(this.icolor == 2)
        		return "\\B";
        	else if(this.icolor == 3)
        		return "\\Y";
        	else
        		return "\\X";
        }

        //data elements of object (left public for easy access to them from elsewhere
        int element;      // The data in the node
        BinaryNode left;         // Left child
        BinaryNode right;        // Right child
        BinaryNode parent;	//parent of current node
        String color = "";		 // color of node
        String lcolor = "";		 // color of node left (for termination only)
        String rcolor = "";		 // color of node right (for termination only)
        int icolor = 0;		 // color of node using ints 
}
