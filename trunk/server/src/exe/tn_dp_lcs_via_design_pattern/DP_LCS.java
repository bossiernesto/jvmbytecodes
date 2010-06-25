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

package exe.tn_dp_lcs_via_design_pattern;

import java.util.*;
import java.io.*;
import exe.*;


/**
 * DP_LCS : A visualization of a Dynamic Programming example using Jhave
 * Calculate the longest common subsequence
 * The main objective of this code is to check if it possible
 * to integrate Chuck's code for the Longest Common Subsequence Problem
 * into the framework that Christian and then Tom tried to write
 */
public class DP_LCS extends DynamProgTemplate {
       
    
    String source;
    String target;

    DP_LCS ( String source, String target, String TITLE, ShowFile show, String docURL, String pseudoURL, String rowLabels, String colLabels ) {

	super(source.length(), target.length(), TITLE + source + " - " + target, show, docURL, pseudoURL, rowLabels, colLabels);
	this.source = source;
	this.target = target;
	int m = source.length();
	int n = target.length();
    }

    Object base_case ( Pair p ) {

        int x = p.getX();
	int y = p.getY();
	if (x == 0) {
	    return new Integer(0);
        }
	else
	    if (y == 0) {
		return new Integer(0);
	    }
	    else return null;
    }

    Integer recursive_case ( Pair p, TreeNode t )  {

        int x = p.getX();
	int y = p.getY();

	if (source.charAt(x-1) == target.charAt(y-1)) {
	    return new Integer (recursive_formulation( new Pair(x-1,y-1), t).intValue() + 1);
	}
	else
	    /*if (source.charAt(x-1) != target.charAt(y-1))*/ {
		int temp1 = recursive_formulation(new Pair(x,y-1), t);
		int temp2 = recursive_formulation(new Pair(x-1,y), t);
		return new Integer(Math.max(temp1,temp2));
	    }
    }	
	
    /**
     * lineInPseudoCode
     * Returns, as a String, the line in the pseudocode that
     * that is being executed when calculating the entries in the matrix
     * @param Pair that contains the row and col of the entry being calculated
     * @return A string containing the line number in the pseudo-code that should be highlighted
     */
    public String lineInPseudoCode(Pair p) {
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

    public  Pair next_location_to_examine (int [][] mat, Pair current_loc ) {
	int i = current_loc.getX();
	int j = current_loc.getY();

	if (i == 0 || j == 0) {
	    return null;
	}
	else {
	    if (source.charAt(i - 1) == target.charAt(j - 1)) {
		current_loc.set_in_solution();
		return new Pair (i-1, j-1);
	    }
	    else if (mat [i-1][j] > mat[i][j-1])
		return new Pair ( i - 1, j);
	    else
		return new Pair ( i, j - 1 );
	}
    }

	
    /**
     * @param args the command line arguments:
     * The first argument will be the name of the .sho file that will be generated
     * The second argument will be the source string
     * The third argument will be the target string
     */
    public static void main(String[] args) throws IOException {
    	
    	DP_LCS dp_lcs = new DP_LCS(args[1], args[2], "Longest Common Subsequence between two strings ",
			    new ShowFile(args[0]), "http://www.cis.gvsu.edu/~trefftzc/jhave/LCSDescription.html",
				   "http://www.cis.gvsu.edu/~trefftzc/jhave/LCS.php", "^"+args[1], "^"+args[2]);
	dp_lcs.initial_call( args[1].length(), args[2].length() );
    	System.exit(0);
    }
}
