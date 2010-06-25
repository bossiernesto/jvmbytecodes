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

package exe.tn_dp_string_distance_via_design_pattern;

import java.util.*;
import java.io.*;
import exe.*;

public abstract class DynamProgTemplate  {

    // For visualization purpose
    String TITLE;
    ShowFile show;
    
    GAIGStext message; // for status messages
    GAIGStree vizTree;   // The tree to visualize
    GAIGSarray vizMatrix; // The matrix to visualize the 2d array
    String docURL;
    String pseudoURL;
    String rowLabels;
    String colLabels;

    Hashtable PairVal;  	// The value associated with a Pair
    Hashtable PairPair;		// The actual Pair associated with a string rep of the Pair
    Hashtable PairChildren;	// The children of a Pair in the recursive call tree
    int m;
    int n;
    ArrayList<TreeNode> nLst;
    static final TreeNode dummyNode = new TreeNode ( (new Pair(-1, -1)).toString()  ); // Treenode corresponding to matrix entry
                                                        // that is not actually part of recursive call tree

    DynamProgTemplate ( int m, int n, String TITLE, ShowFile show, String docURL, String pseudoURL, String rowLabels, String colLabels ) {
	this.show = show;
	this.TITLE = TITLE;
	this.m = m;
	this.n = n;
	this.docURL = docURL; 
	this.pseudoURL = pseudoURL;
	this.rowLabels = rowLabels;
	this.colLabels = colLabels;
        nLst = new ArrayList<TreeNode>();
	PairVal = new Hashtable();
	PairPair = new Hashtable();
	PairChildren = new Hashtable();
    }



    //    Integer initial_call ( String source, String target, String TITLE, String show, String docURL, String pseudoURL ) {
    Integer initial_call ( int m, int n ) throws IOException {

	this.m = m;
	this.n = n;


    	// Create a Tree to be visualized
    	// The false parameter in the constructor indicates this is NOT a 
    	// binary tree
    	
    	vizTree = new GAIGStree(false,"Recursion Tree","#FFFFFF",
				0.2,0.05,0.8,0.35,0.1); 
	vizTree.setSpacing(0.7,1.25); 
    	vizMatrix = new GAIGSarray(m+1,n+1,"Matrix","#FFFFFF",
				   0.0,0.5,1.0,0.9,0.1);
	message = new GAIGStext(0.1,-0.7,"");
	message.setFontsize(0.07);

	setLabelsOfColumns();
	setLabelsOfRows();

    	// Calculate the entire tree of recursive calls 

	Pair p = new Pair(m,n);
	Integer ret_val = recursive_formulation(p, null);

	DFS(vizTree.getRoot());
	// Write a snapshot with the tree and the initially empty matrix 
    	show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);

	// Now time to fill the matrix
	// Create a snapshort after each entry is calculated
	for(int i = 0;i <= m;i++) {
	    for(int j = 0;j <= n;j++) {
		calcEntry(new Pair(i,j));
	    }
	}

	// Now highlight the squares in the matrix that correspond to the optimal solution
	calcSolution();
    	show.close();

	return ret_val;
    }

    Integer recursive_formulation ( Pair p, TreeNode currentRoot ) {

	PairPair.put(p.toString(), p);
	TreeNode t = new TreeNode(p.toString());
	if (currentRoot == null) {
	    // Create the root of the tree of recursive calls
	    // Set it as the root of the tree that will be visualized
	    vizTree.setRoot(t);
	}
	else {
	    currentRoot.setChildWithEdge(t);
	    if ( PairChildren.get(currentRoot.getValue()) == null ) {
		ArrayList<Pair> l = new ArrayList<Pair>();
		l.add(p);
		PairChildren.put(currentRoot.getValue(), l);
	    }
	    else {
		((ArrayList<Pair>)(PairChildren.get(currentRoot.getValue()))).add(p);
	    }
		
	}

	Object o = base_case ( p );
	
	if ( o != null ) {
	    Integer retVal = (Integer) o;
	    PairVal.put(p.toString(), retVal);
	    return retVal;
	}
	else {
	    Integer retVal = recursive_case ( p, t );
	    PairVal.put(p.toString(), retVal);
	    return retVal;
	}
    }


    public void setLabelsOfColumns() {
	for(int i = 0;i < colLabels.length();i++) {
	    vizMatrix.setColLabel(""+colLabels.charAt(i),i);
	}
    }

    public void setLabelsOfRows() {
	for(int i = 0;i < rowLabels.length();i++) {
	    vizMatrix.setRowLabel(""+rowLabels.charAt(i),i);
	}
    }

    public void calcEntry(Pair p) throws IOException
    {
	ArrayList<Pair> l = null;
        int row = p.getX();
	int col = p.getY();

	// Sometimes a matrix entry wasn't actually used or needed in the recursive call tree
	if (PairVal.get(p.toString()) == null) {

	    Object o = base_case ( p );
	    if ( o != null ) {
		Integer retVal = (Integer) o;
		PairVal.put(p.toString(), retVal);
	    }
	    else
		{
		    Integer retVal = recursive_case ( p, dummyNode );
		    PairVal.put(p.toString(), retVal);
		}
	}
	else {
	    if (base_case(p) == null) {
		l = (ArrayList<Pair>)(PairChildren.get(p.toString()));
		for (int j = 0; j < l.size(); j++) {
		    Pair q = l.get(j);
		    vizMatrix.setColor( q.getX(), q.getY(), "#C6EFF7"); 
		    colorList(q.toString());
		}
	    }
	}
	vizMatrix.set( (Integer)PairVal.get(p.toString()), row,col, "#FFFF9C");
	String pseudoURLline = pseudoURL+"?line="+lineInPseudoCode(p);
	show.writeSnap(TITLE,docURL,pseudoURLline,vizTree,vizMatrix);
	vizMatrix.set( (Integer)PairVal.get(p.toString()), row,col, "#FFFFFF");
	if (l != null) {
	    for (int j = 0; j < l.size(); j++) {
		Pair q = l.get(j);
		vizMatrix.setColor( q.getX(), q.getY(), "#FFFFFF"); 
		uncolorList(q.toString());
	    }
	}
    }

    void calcSolution ()  throws IOException {
 	int [][] mat = new int[m+1][n+1];
 	for(int i = 0;i <= m;i++) 
 	    for(int j = 0;j <= n;j++)
 		mat[i][j] = (Integer) vizMatrix.get(i,j); 
			
	ArrayList<Pair> l;
	
	for(int i = 0;i <= m;i++) {
	    for(int j = 0;j <= n;j++) {
		vizMatrix.setColor(i, j, "#FFFFFF");
	    }
	}
	
	Pair current_loc = new Pair (m, n);
	vizMatrix.setColor( current_loc.getX(), current_loc.getY(), "#FFFF9C"); 
	if (base_case(current_loc) == null) {
	    l = (ArrayList<Pair>)(PairChildren.get(current_loc.toString()));
	    for (int j = 0; j < l.size(); j++) {
		Pair q = l.get(j);
		vizMatrix.setColor( q.getX(), q.getY(), "#C6EFF7"); 
	    }
	}
	show.writeSnap(TITLE,docURL,vizTree,vizMatrix);
	if (base_case(current_loc) == null) {
	    l = (ArrayList<Pair>)(PairChildren.get(current_loc.toString()));
	    for (int j = 0; j < l.size(); j++) {
		Pair q = l.get(j);
		vizMatrix.setColor( q.getX(), q.getY(), "#FFFFFF"); 
	    }
	}

	Pair next_loc;
	next_loc = next_location_to_examine(mat, current_loc);
	while (next_loc != null) {
	    if ( current_loc.is_in_solution() ) {
		vizMatrix.setColor( current_loc.getX(), current_loc.getY(), "#FF0000"); 
	    }

	    current_loc = next_loc;

	    vizMatrix.setColor( current_loc.getX(), current_loc.getY(), "#FFFF9C"); 
	    if (base_case(current_loc) == null) {
		l = (ArrayList<Pair>)(PairChildren.get(current_loc.toString()));
		for (int j = 0; j < l.size(); j++) {
		    Pair q = l.get(j);
		    vizMatrix.setColor( q.getX(), q.getY(), "#C6EFF7"); 
		}
	    }
	    show.writeSnap(TITLE,docURL,vizTree,vizMatrix);
	    if (base_case(current_loc) == null) {
		l = (ArrayList<Pair>)(PairChildren.get(current_loc.toString()));
		for (int j = 0; j < l.size(); j++) {
		    Pair q = l.get(j);
		    vizMatrix.setColor( q.getX(), q.getY(), "#FFFFFF"); 
		}
	    }
	    next_loc = next_location_to_examine(mat, current_loc);
	} // while
	if ( current_loc.is_in_solution() ) {
	    vizMatrix.setColor( current_loc.getX(), current_loc.getY(), "#FF0000"); 
	}
	show.writeSnap(TITLE,docURL,vizTree,vizMatrix);
    }  // calcSolution  

    private void DFS(TreeNode src) {
        
        if (src != null) {
           nLst.add(src);
           TreeNode p = src.getChild();
           DFS(p);
           while (src.getSibling() != null) {
              src = src.getSibling();
              DFS(src);
          }
        }
    }
    
    private void colorList(String target) {
        for (int j = 0; j < nLst.size(); j++) {
           TreeNode p = nLst.get(j);
           if (p.getValue().equals(target))
               p.setHexColor("#C6EFF7");
        }
    }
     
    private void uncolorList(String target) {
        for (int j = 0; j < nLst.size(); j++) {
           TreeNode p = nLst.get(j);
           if (p.getValue().equals(target))
               p.setHexColor("#FFFFFF");
        }
    }

    /**
       Given p -- a pair of parameters (i,j) corresponding to a matrix
       location -- return null if this is not a base case for the
       recursion and otherwise return the computed value for that base
       case
     */   
    abstract Object base_case ( Pair p );

    /**
       Given p -- a pair of parameters (i,j) corresponding to a matrix
       location that needs to be computed recursively -- call
       recursively tom compute the returned value.  To make the
       "recursive call" call the method recursive_formulation in the
       abstract base class.  When you make the call, pass along the
       TreeNode t so the recursive_formulation method in the base
       class can build and decorate the tree appropriately.
     */   
    abstract Integer recursive_case ( Pair p, TreeNode t );

    /**
       This method is given the computed matrix of cached values and
       the current location in that matrix.  If the current location
       is part of the final solution, call the method set_in-solution
       on that location (see the Pair class for this method).  Finally
       return the next location to examine in the matrix or null if
       there is not another location to examine, that is, if the
       current location corresponds to a base case in the recursion.
     */   
    abstract  Pair next_location_to_examine (int [][] mat, Pair current_loc );

    abstract String lineInPseudoCode(Pair p);

}
