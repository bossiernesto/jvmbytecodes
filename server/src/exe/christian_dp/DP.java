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

package exe.christian_dp;

import java.util.*;
import java.io.*;
import exe.*;

/**
 * DP : A visualization of a Dynamic Programming example using Jhave
 * Calculate the shortest editing distance
 
 */
public class DP {
       
    // For visualization purpose
    static String TITLE = "Shortest Editing Distance between two strings \n";   
    static ShowFile show;
    
    static GAIGSlabel message;  // for status messages
    static GAIGStree vizTree;   // The tree to visualize
    static GAIGSarray vizMatrix; // The matrix to visualize the 2d array

    static final String docURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/DPDescription.html";   
    static final String pseudoURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/DP.php";   
    
    static String source;
    static String target;
    /**
      * calcBranches: Find the branches of a node in the tree of recursive calls
      * @param A Pair that contains the position row,col in the matrix
      * @return A LinkedList with the branches coming out of this node
      * It might be:
      * - An empty set -> no branches (the row or the column is 0)
      * - A set with one element - One branch (the characters in the source and target string match)
      * - A set with three elements - Three branches (the characters do not match)
    */
    public static LinkedList<Pair> calcBranches(Pair p)
    {
        LinkedList<Pair> result = new LinkedList<Pair>();
        int row = p.getX();
	  int col = p.getY();
	  if ((row == 0) || (col == 0)) {
		return result; // No branches coming out of this node. Border case
        }
        if (source.charAt(row-1) == target.charAt(col-1)) {
		Pair newNode = new Pair(row-1,col-1);
		result.add(newNode);
		return result;
        }
	  else {
		Pair newNode1 = new Pair(row,col-1);
		Pair newNode2 = new Pair(row-1,col-1);
		Pair newNode3 = new Pair(row-1,col);
		result.add(newNode1);
		result.add(newNode2);
		result.add(newNode3);
		return result;
	  }
	  
    }

    /**
      * calcEntry: Calculate the value of entry Pair.row, Pair.col in the matrix to be visualized
      * @param A Pair that contains the position row,col in the matrix being calculated
    */
    public static void calcEntry(Pair p)
    {
       
        int row = p.getX();
	  int col = p.getY();
	  if (row == 0) {
		vizMatrix.set(col,row,col);
        }
	  else
	  if (col == 0) {
		vizMatrix.set(row,row,col);
	  }
        else 
	  if (source.charAt(row-1) == target.charAt(col-1)) {
		int temp = (Integer) vizMatrix.get(row-1,col-1);
		vizMatrix.set(temp,row,col);
        }
	  else
        if (source.charAt(row-1) != target.charAt(col-1)) {
		int temp1 = (Integer) vizMatrix.get(row,col-1);
		int temp2 = (Integer) vizMatrix.get(row-1,col-1);
		int temp3 = (Integer) vizMatrix.get(row-1,col);
		temp1++;
		temp2 = temp2 + 2;
		temp3++;
		int temp = Math.min(temp1,Math.min(temp2,temp3));
		vizMatrix.set(temp,row,col);
        }
	}
	/**
      * calcTree: Recursive procedure to calculate the tree of recursive calls
      * @param TreeNode currentRoot The current node in the tree that is the root of the subtree being built
      * @param Pair p The row and column values in the matrix that correspond to this node
      */

    public static void calcTree(TreeNode currentRoot,Pair p) 
    {
	LinkedList<Pair> possibleMoves = calcBranches(p);
	for (Pair branch : possibleMoves) {
		TreeNode newNode = new TreeNode(branch.toString());
        	currentRoot.setChildWithEdge(newNode);
		calcTree(newNode,branch);
      }
    }

    /**
	* setLabelsOfColumns
	*/
    public static void setLabelsOfColumns() {
	vizMatrix.setColLabel("^",0);
	for(int i = 0;i < target.length();i++) {
		vizMatrix.setColLabel(""+target.charAt(i),i+1);
      }
    }
    /**
	* setLabelsOfRows
	*/
    public static void setLabelsOfRows() {
	vizMatrix.setRowLabel("^",0);
	for(int i = 0;i < source.length();i++) {
		vizMatrix.setRowLabel(""+source.charAt(i),i+1);
      }
    }
    /**
	* lineInPseudoCode
      * Returns, as a String, the line in the pseudocode that
      * that is being executed when calculating the entries in the matrix
	* @param Pair that contains the row and col of the entry being calculated
      * @return A string containing the line number in the pseudo-code that should be highlighted
	*/
    public static String lineInPseudoCode(Pair p) {
      int row = p.getX();
      int col = p.getY();
	if (row == 0)
		return "6";
      if (col == 0)
            return "9";
      if (source.charAt(row-1) == target.charAt(col-1))
		return "12";
      else
            return "15";
    }

    public static void calcOptimalPath(int m,int n) throws java.io.IOException {
	int currentRow = m;
      int currentCol = n;
	vizMatrix.setColor(currentRow,currentCol,"#0000FF");
	show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);

	while((currentRow != 0) && (currentCol != 0)) {
		// Find the minimum of the three neighbors
		int left = (Integer) vizMatrix.get(currentRow,currentCol-1);
		int up = (Integer) vizMatrix.get(currentRow-1,currentCol);
		int diagonal = (Integer) vizMatrix.get(currentRow-1,currentCol-1);
		int minValue = Math.min(Math.min(left,up),diagonal);
		if (minValue == left) {
			currentCol = currentCol -1;
		}
		else if (minValue == up) {
			currentRow = currentRow - 1;
		}
		else if (minValue == diagonal) {
			currentRow = currentRow - 1;
			currentCol = currentCol - 1;
		}
		vizMatrix.setColor(currentRow,currentCol,"#0000FF");
		show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);

	}
	vizMatrix.setColor(0,0,"#0000FF");
	show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);

    }
	
    /**
     * @param args the command line arguments:
     * The first argument will be the name of the .sho file that will be generated
     * The second argument will be the source string
     * The third argument will be the target string
     */
    public static void main(String[] args) throws IOException {
    	
    	// For visualization purposes:
    	
    	// The snapshot file that will be created:
    	 show = new ShowFile(args[0]);
    	// The second parameter in the command line is the source string
	source = args[1];
	// The third parameter in the command line is the target string
	target = args[2];
	// Add the strings to the title:
      TITLE = TITLE + source + " -> "+target;
    	// Find the lengths of the source(m) and target(n) strings
	int m = source.length();
	int n = target.length();

    	// Create a Tree to be visualized
    	// The false parameter in the constructor indicates this is NOT a 
    	// binary tree
    	
    	vizTree = new GAIGStree(false,"Recursion Tree","#FFFFFF",
				//    			0.0,0.0,1.0,0.5,0.1); // Christian's original
    			0.4,0.15,0.6,0.35,0.1); // Experimentation by TN to shrink tree -- 07/10/07
    	vizMatrix = new GAIGSarray(m+1,n+1,"Dynamic Programming Matrix","#FFFFFF",
    			0.0,0.6,1.0,1.0,0.1);
	setLabelsOfColumns();
	setLabelsOfRows();
    	// Initially, generate the tree of recursive calls required
	// to find the shortest editing distance between the two strings
	// received as arguments 

	// Create the root of the tree of recursive calls
	Pair p = new Pair(m,n);
    	TreeNode root = new TreeNode(p.toString());
    	// Set it as the root of the tree that will be visualized
    	vizTree.setRoot(root);
	vizTree.setSpacing(0.5,1.25); // Added by TN on 7/10/07
    	// Calculate the entire tree of recursive calls with the recursive procedure calcTree
	// calcTree in turn calls calcBranches, which would need to be modified for a
	// different Dynamic Programming application
      calcTree(root,p);
	// Write a snapshot with the tree and the initially empty matrix 
    	show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);
    	
	// Now time to fill the matrix
	// Create a snapshort after each entry is calculated

	for(int i = 0;i <= m;i++) {
		for(int j = 0;j <= n;j++) {
			
			calcEntry(new Pair(i,j));
			String pseudoURLline = pseudoURL+"?line="+lineInPseudoCode(new Pair(i,j));
			show.writeSnap(TITLE,docURL,pseudoURLline,vizTree,vizMatrix);
		}
	}
	// Now highlight the squares in the matrix that correspond to the optimal solution
	calcOptimalPath(m,n);  

    	show.close();
    	System.exit(0);
    }
    
    
    
    
}
