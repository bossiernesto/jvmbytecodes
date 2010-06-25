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

package exe.christian_trefftz;

import java.util.*;
import exe.*;
import java.io.*;
/**
 *
 * @author trefftzc
 */
public class SearchThread extends Thread {
    private static int callCounter = 0;
	
    private int matrix[][] = new int[Main.SIZE][Main.SIZE];
    private int direction;
    private ArrayList<Integer> moves = new ArrayList<Integer>();
    private String moveEquivalence[] = {"R","D","L","U"};
    private TreeNode initialRoot;
    
    private static final int solution[][] = { { 0,1,2,3},
					      { 4,5,6,7},
					      { 8,9,10,11},
					      {12,13,14,15}};


    // private static final int DEPTHLIMIT = 14;
    private static final int DEPTHLIMIT = 5;

    /** Creates a new instance of SearchThread */
    public SearchThread(int extMatrix[][],Integer direction,TreeNode initialRoot) {
        super();
        for(int i = 0;i < Main.SIZE;i++)
            for(int j = 0;j < Main.SIZE;j++)
                matrix[i][j] = extMatrix[i][j];
        this.direction = direction;
        this.initialRoot = initialRoot;
    }

    public void run() {
        moves.add(direction);
        if (search(matrix,direction,moves,0,initialRoot)) {
            System.out.println("Solution found!");
            for(Integer move : moves) {
                System.out.print(moveEquivalence[move]+" ");
            }
            System.out.println();
        }
    }
    
   private boolean isSolution(int mat[][]) {
       
       boolean result = true;
       
       for(int i = 0;i < Main.SIZE;i++)
           for(int j = 0;j < Main.SIZE;j++)
               if (mat[i][j] != solution[i][j])
                   return false;
       
       System.out.println("Solution Found after "+callCounter+" nodes!");
       Main.solutionFound = true;
       return result;       
   }
   
   public static int calcOpposite(int direction) {
	   int result = 0;
	   	switch(direction) {
	   		case(Main.RIGHT):
			    result = Main.LEFT;
			    break;
	   		case(Main.DOWN): 
			    result = Main.UP;
			    break;
	   		case(Main.LEFT): 
			    result = Main.RIGHT;
			    break;
	   		case(Main.UP):   
			    result = Main.DOWN;
			    break;
	   }
	   return result;
   }
    /**
      * A recursive search procedure based on backtracking
    */

    public boolean search(int mat[][],Integer direction,
            ArrayList<Integer> moves,int depth,TreeNode currentRoot) 
     {
        // If another thread found a solution, stop working
        if (Main.solutionFound)
            return false;
        /*
        System.out.println("In search. Depth: "+depth);
        for(int i = 0;i < Main.SIZE;i++){
            for(int j = 0;j < Main.SIZE;j++)
                System.out.print(mat[i][j]+" ");
            System.out.println();
        }
         */
	// If the depthlimit is reached, give up and return false
        if (depth == DEPTHLIMIT)
            return false;
        // If the matrix passed as a parameter is the solution, return true
        if (isSolution(mat)) {   
            return true;
        }
        // System.out.println(mat);
	// Copy the matrix
        int localMat[][] = new int[Main.SIZE][Main.SIZE];
        for(int i = 0;i < Main.SIZE;i++)
            for(int j = 0;j < Main.SIZE;j++)
                localMat[i][j] = mat[i][j];
	// Locate the 0
        Pair zeroLocation = Main.find0(mat);
        int temp;
        // Perform the move received as a parameter
        switch(direction) {
            case(Main.RIGHT):temp = localMat[zeroLocation.getX()][zeroLocation.getY()+1];
                             localMat[zeroLocation.getX()][zeroLocation.getY()+1] = 0;
                             localMat[zeroLocation.getX()][zeroLocation.getY()] = temp;
                             break;
            case(Main.DOWN): temp = localMat[zeroLocation.getX()+1][zeroLocation.getY()];
                             localMat[zeroLocation.getX()+1][zeroLocation.getY()] = 0;
                             localMat[zeroLocation.getX()][zeroLocation.getY()] = temp;
                             break;
                             
            case(Main.LEFT): temp = localMat[zeroLocation.getX()][zeroLocation.getY()-1];
                             localMat[zeroLocation.getX()][zeroLocation.getY()-1] = 0;
                             localMat[zeroLocation.getX()][zeroLocation.getY()] = temp;
                             break;
           
            case(Main.UP):   temp = localMat[zeroLocation.getX()-1][zeroLocation.getY()];
                             localMat[zeroLocation.getX()-1][zeroLocation.getY()] = 0;
                             localMat[zeroLocation.getX()][zeroLocation.getY()] = temp;
                             break;
        }
        // Find the new position of the 0
        Pair result = Main.find0(localMat);
        // Calculate the possible new moves based on the current position of 0
        TreeSet<Integer> possibleMoves = Main.calcPossibleMoves(result.getX(),result.getY());
        // But do not go back to the previous place,
        // thus remove from possibleMoves the opposite of direction
        int opposite = calcOpposite(direction);
	possibleMoves.remove(opposite);
        // Explore each of those possible moves and check if they lead to a solution
        // For visualization
        TreeNode newNode = new TreeNode(Main.convertPossibleMovesToString(possibleMoves));
        currentRoot.setChildWithEdge(newNode);
        /*
         Main.out.println("<snap>");
         Main.out.println("<title>New Move " + currentRoot.getValue() + 
         "</title>");
         Main.tree.writeGAIGSXMLTree();
         Main.out.println("</snap>");
         */
        try {
        	callCounter++;
        	if (callCounter < 200) {
        			Main.fillVizMatrix(localMat);
        			Main.show.writeSnap(Main.TITLE, Main.vizTree,Main.vizMatrix);
        	}
        }
        catch (IOException io) {
        	System.out.println(io.toString());
        }
        // end for visualization
        for(Integer newDirection: possibleMoves) {
              if (search(localMat,newDirection,moves,depth+1,newNode)) {
                  moves.add(newDirection);
                  return true;
              }
          }
	// If none of the moves found a solution, return false
        return false;
    }
    
    
}
