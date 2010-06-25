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

/**
 * DP_String_Distance : A visualization of a Dynamic Programming example using Jhave
 * Calculate the minimum editing distance
 * The main objective of this code is to check if it possible
 * to integrate Christian's code for the minimum editing distance
 * into the framework that Tom wrote
 */
public class DP_String_Distance extends DynamProgTemplate {
       
    
    String source;
    String target;

    DP_String_Distance ( String source, String target, String TITLE, ShowFile show, String docURL, String pseudoURL, String rowLabels, String colLabels ) {

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
	    return new Integer(y);
    }
	 else
	 if (y == 0) {
		return new Integer(x);
	 }
	 else return null;
    }

    Integer recursive_case ( Pair p, TreeNode t )  {

        int x = p.getX();
	     int y = p.getY();

        int temp1 = recursive_formulation(new Pair(x,y-1), t).intValue() + 1;
		  int temp2 = recursive_formulation(new Pair(x-1,y), t).intValue() + 1;
		  int temp3;
	     if (source.charAt(x-1) == target.charAt(y-1)) {
	        temp3 = (recursive_formulation( new Pair(x-1,y-1), t).intValue());
	     }
	     else {
	        /*if (source.charAt(x-1) != target.charAt(y-1))*/ 
		     temp3 = (recursive_formulation( new Pair(x-1,y-1), t).intValue()) + 2;   		    
	     }
		  return (new Integer(Math.min(temp1,Math.min(temp2,temp3))));
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

	if (i == 0 && j == 0) {
		 current_loc.set_in_solution();
	    return null;
	}
	else if (i == 0 && j>0) {
	    current_loc.set_in_solution();
	    return new Pair(i,j-1);
	}
	else if (i > 0 && j == 0) {
		 current_loc.set_in_solution();
		 return new Pair(i-1,j);
	}
	else if (i > 0 && j > 0) {
	// Examine the three neighboring cells: West, North and Northwest
	// Choose the minimum one
		// Check West first 
		if (mat[i][j-1] <= mat[i-1][j-1]  && mat[i][j-1] <= mat[i-1][j]) {
			current_loc.set_in_solution();
			return new Pair(i,j-1);
		}
		else // Now check North
		if (mat[i-1][j] <= mat[i-1][j-1]  && mat[i-1][j] <= mat[i][j-1]) {
			current_loc.set_in_solution();
			return new Pair(i-1,j);
		}	
		else // Finally check Northwest
		if (mat[i-1][j-1] <= mat[i][j-1]  && mat[i-1][j-1] <= mat[i-1][j]) {
			current_loc.set_in_solution();
			return new Pair(i-1,j-1);
		}
	  } 
	  // This point should not be reached...
	  System.out.println("There is something wrong in next_location_to_examine.");
	  return null;  
   }

	
    /**
     * @param args the command line arguments:
     * The first argument will be the name of the .sho file that will be generated
     * The second argument will be the source string
     * The third argument will be the target string
     */
    public static void main(String[] args) throws IOException {
    	
    	DP_String_Distance dp_sd = new DP_String_Distance(args[1], args[2], "Minimum Editing Distance between two strings ",
			    new ShowFile(args[0]), "DPDescription.html",
				   "DP.php", "^"+args[1], "^"+args[2]);
	   dp_sd.initial_call( args[1].length(), args[2].length() );
    	System.exit(0);
    }
}
