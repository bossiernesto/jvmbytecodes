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

package exe.christian_dp_lcs;

import java.util.*;
import java.io.*;
import exe.*;


/**
 * DP_LCS : A visualization of a Dynamic Programming example using Jhave
 * Calculate the longest common subsequence
 * The main objective of this code is to check if it possible
 * to integrate Chuck's code for the Longest Common Subsequence Problem
 * into the framework that Christian tried to write
 */
public class DP_LCS {
       
    // For visualization purpose
    static String TITLE = "Longest Common Subsequence between two strings \n";   
    static ShowFile show;
    
    static GAIGStext message; // for status messages
    static GAIGStree vizTree;   // The tree to visualize
    static GAIGSarray vizMatrix; // The matrix to visualize the 2d array
    // TO DO: These two URLs need to be changed later so that they point to the
    // proper web pages 
    static final String docURL = "LCSDescription.html";   
    static final String pseudoURL = "LCS.php";   
    
    static String source;
    static String target;
    /**
     * calcBranches: Find the branches of a node in the tree of recursive calls
     * @param A Pair that contains the position row,col in the matrix
     * @return A LinkedList with the branches coming out of this node
     * For the LCS problem the possible cases are:
     * It might be:
     * - An empty set -> no branches (the row or the column is -1)
     * - A set with one element - One branch (the characters on both strings match)
     * - A set with two elements - Two branches (the characters do not match)
     * Chuck's code in the method recurLCS in the file lcseq was used as the base for the
     * changes introduced in this method 
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
            if (row > 0 && col > 0) {
		Pair newNode = new Pair(row-1,col-1);
		result.add(newNode);
            }
	    return result;
        }
	else {
	    Pair newNode1 = new Pair(row-1,col);
	    Pair newNode2 = new Pair(row,col-1);
	    if (row > 0) {
		result.add(newNode1);
	    }
	    if (col > 0) {
		result.add(newNode2);
	    }
	    return result;
	}
	  
    }

    /**
     * calcEntry: Calculate the value of entry Pair.row, Pair.col in the matrix to be visualized
     * @param A Pair that contains the position row,col in the matrix being calculated
    
     * Again this method has been modified to match Chuck's code
     */
    public static void calcEntry(Pair p)
    {
       
        int row = p.getX();
	int col = p.getY();
	if (row == 0) {
	    // TODO: Set the color according to Chuck's logic
	    vizMatrix.set(0,row,col);
        }
	else
	    if (col == 0) {
		// TODO: Set the color according to Chuck's logic
		vizMatrix.set(0,row,col);
	    }
	    else 
		if (source.charAt(row-1) == target.charAt(col-1)) {
		    int temp = (Integer) vizMatrix.get(row-1,col-1);
		    temp++;
		    // TODO: Set the color according to Chuck's logic
		    // lcsMat.setColor(r-1,c-1,"#F7FFCE");
		    vizMatrix.set(temp,row,col);
		}
		else
		    if (source.charAt(row-1) != target.charAt(col-1)) {
			int temp1 = (Integer) vizMatrix.get(row,col-1);
			int temp2 = (Integer) vizMatrix.get(row-1,col);
			int temp = Math.max(temp1,temp2);
			// TODO: Set the color according to Chuck's logic
			vizMatrix.set(temp,row,col);
		    }
    }
    /**
     * calcTree: Recursive procedure to calculate the tree of recursive calls
     * This method did not change when implementing Chuck's LCS code
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
     * The same as before
     */
    public static void setLabelsOfColumns() {
	vizMatrix.setColLabel("^",0);
	for(int i = 0;i < target.length();i++) {
	    vizMatrix.setColLabel(""+target.charAt(i),i+1);
	}
    }
    /**
     * setLabelsOfRows
     * The same as before
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
	if (source.charAt(row-1) != target.charAt(col-1))
	    return "15";
	return "";
    }

    public static void calcOptimalPath(int m,int n) throws java.io.IOException {
	/* This is Chuck's code:
	   while (lcs[m][n] >= 1) {            
	   int val = lcs[m][n];
	   while(lcs[m-1][n] == val) {
	   lcsMat.setColor(m,n,"#FFB573");
	   m--;
	   message.setText("Searching for component of solution");
	   show.writeSnap(TITLE, 0.1, message, lcsMat);
	   }
	   lcsMat.setColor(m,n,"#FFB573");
	   message.setText("Searching for component of solution");
	   show.writeSnap(TITLE, 0.1, message, lcsMat);
	   while (n>0 && lcs[m][n-1] == val){
	   n--;
	   lcsMat.setColor(m,n,"#FFB573");
	   show.writeSnap(TITLE, 0.1, message, lcsMat);
	   }
	   soln[idx-1] = s1[m-1];
	   idx--;
	   lcsMat.setColor(m,n,"#C6EFF7");
	   message.setText("Found an element of the solution");
	   show.writeSnap(TITLE, 0.1, message, lcsMat);
	   // move up to next possible matching character
	   m--;
	   lcsMat.setColor(m,n,"#FFB573");
	   show.writeSnap(TITLE, 0.1,  message, lcsMat);
	   while (n>0 && lcs[m][n-1] == val-1){
	   n--;
	   lcsMat.setColor(m,n,"#FFB573");
	   show.writeSnap(TITLE, 0.1,  message, lcsMat);
           }
	   }
	*/
	/* This was the original code
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
	*/
	// Copy the visualization matrix into a matrix of integers
	// called lcs

	int [][] lcs = new int[m+1][n+1];
	for(int i = 0;i <= m;i++) 
	    for(int j = 0;j <= n;j++)
		lcs[i][j] = (Integer) vizMatrix.get(i,j); 
			
	
	while (lcs[m][n] >= 1) {            
            int val = lcs[m][n];
            while(lcs[m-1][n] == val) {
		vizMatrix.setColor(m,n,"#FFB573");
                m--;
                message.setText("Searching for component of solution");
		    
                // show.writeSnap(TITLE, 0.1, message, vizMatrix);
		show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
            }
            vizMatrix.setColor(m,n,"#FFB573");
            message.setText("Searching for component of solution");
            // show.writeSnap(TITLE, 0.1, message, vizMatrix);
	    show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
            while (n>0 && lcs[m][n-1] == val){
                n--;
                vizMatrix.setColor(m,n,"#FFB573");
                // show.writeSnap(TITLE, 0.1, message, vizMatrix);
		show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
            }
            // soln[idx-1] = s1[m-1];
            // idx--;
            vizMatrix.setColor(m,n,"#C6EFF7");
            message.setText("Found an element of the solution");
            // show.writeSnap(TITLE, 0.1, message, vizMatrix);
	    show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
            // move up to next possible matching character
            m--;
            vizMatrix.setColor(m,n,"#FFB573");
            // show.writeSnap(TITLE, 0.1,  message, vizMatrix);
	    show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
            while (n>0 && lcs[m][n-1] == val-1){
                n--;
                vizMatrix.setColor(m,n,"#FFB573");
                // show.writeSnap(TITLE, 0.1,  message, vizMatrix);
		show.writeSnap(TITLE,docURL,"",vizTree,vizMatrix);
	    }
        }
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
	TITLE = TITLE + source + " - "+target;
    	// Find the lengths of the source(m) and target(n) strings
	int m = source.length();
	int n = target.length();

    	// Create a Tree to be visualized
    	// The false parameter in the constructor indicates this is NOT a 
    	// binary tree
    	
    	vizTree = new GAIGStree(false,"Recursion Tree","#FFFFFF",
				//    			0.0,0.0,1.0,0.5,0.1); // Christian's original
				0.4,0.15,0.6,0.35,0.1); // Experimentation by TN to shrink tree -- 07/10/07
    	vizMatrix = new GAIGSarray(m+1,n+1,"LCS Matrix","#FFFFFF",
				   0.0,0.6,1.0,1.0,0.1);
	message = new GAIGStext(0.1,-0.7,"");
	message.setFontsize(0.07);
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
	// calcEntry is rewritten for each new application to be visualized
	for(int i = 0;i <= m;i++) {
	    for(int j = 0;j <= n;j++) {
			
		calcEntry(new Pair(i,j));
		String pseudoURLline = pseudoURL+"?line="+lineInPseudoCode(new Pair(i,j));
		show.writeSnap(TITLE,docURL,pseudoURLline,vizTree,vizMatrix);
	    }
	}
	// Now highlight the squares in the matrix that correspond to the optimal solution
	// Again, this needs to be rewritten for each new application
	calcOptimalPath(m,n);  

    	show.close();
    	System.exit(0);
    }
}
