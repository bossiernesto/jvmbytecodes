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
import java.io.*;
import exe.*;



/**
 *
 * @author trefftzc
 */
public class Main {
    public static final int SIZE = 4;
    
    public static final int RIGHT = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int UP = 3;
    
    public static boolean solutionFound = false;
    
    private static String moveEquivalence[] = {"R","D","L","U"};   

    
    
    
    
    // public static boolean solutionFound = false;
    // The matrix is represented in this way:
    //          col 0  col 1 col 2 col 3
    //  row 0    0,0   0,1   0,2   0,3
    //  row 1    1,0   1,1   1,2   1,3
    //  row 2    2,0   2,1   2,2   2,3
    //  row 3    3,0   3,1   3,2   3,3
    
    // private static final int matrix[][] = new int[SIZE][SIZE];
    // For testing:
    // This one has a solution:
    /*
    private static final int matrix[][] = { {4,1,2,3},
                                            {0,5,6,7},
                                            {8,9,10,11},
                                            {12,13,14,15}};
    */
    private static final int matrix[][] = { {1,0,2,3},
        {4,5,6,7},
        {8,9,10,11},
        {12,13,14,15}};
    /* This one does not have a solution
        private static final int matrix[][] = { {13,1,2,4},
                                            {5,0,3,7},
                                            {9,6,10,12},
                                            {15,8,11,14}};
    */
    
    // For visualization purpose
    static final String TITLE = "15 Puzzle";   // no title
    static ShowFile show;
    static GAIGSarray puzzle;    // the array of items
    static GAIGSlabel message;  // for status messages
    static GAIGStree vizTree;   // The tree to visualize
    static GAIGSarray vizMatrix; // The matrix to visualize the 2d array

    static final String docURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/15puzzleDescription.html";   
    static final String pseudoURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/15puzzle.php";   
    
    /** Creates a new instance of MainSingleThread */
    public Main() {
    }
    // Read the problem from the file fileName
    // Store in the matrix
    
    public static void read15Puzzle(String fileName)
    {
        
    	String [] numbers;
    	try {            
    		Scanner sc = new Scanner(new File(fileName));            
    		
    		// Read line by line                                  
    		for(int rowCounter = 0;rowCounter < SIZE;rowCounter++) {    		
    			for(int colCounter = 0;colCounter < SIZE;colCounter++) { 
    				// There should be 4 numbers in each line
    				matrix[rowCounter][colCounter] = sc.nextInt();
    				
    			}
    		}
    		// System.out.println("Read was succesful");
    		
    	}
    	catch (Exception e)
    	{
    		System.out.println(e.toString());
    	}
    }
    
    public static TreeSet<Integer> calcPossibleMoves(int row,int col)
    {
        TreeSet<Integer> result = new TreeSet<Integer>();
        switch(row) {
            case 0 :if (col == 0) {
                        result.add(RIGHT);
                        result.add(DOWN);
                        return result;
                    }
                    if ((col == 1) || (col == 2)) {
                        result.add(RIGHT);
                        result.add(DOWN);
                        result.add(LEFT);
                        return result;
                    }
                    if (col == 3) {
                        result.add(DOWN);
                        result.add(LEFT);
                        return result;
                    }
                    break;
            case 1 :
            case 2 :
                if (col == 0) {
                        result.add(RIGHT);
                        result.add(DOWN);
                        result.add(UP);
                        return result;
                    }
                    if ((col == 1) || (col == 2)) {
                        result.add(RIGHT);
                        result.add(DOWN);
                        result.add(LEFT);
                        result.add(UP);
                        return result;
                    }
                    if (col == 3) {
                        result.add(DOWN);
                        result.add(LEFT);
                        result.add(UP);
                        return result;
                    }
                    break;
            case 3 :
                    if (col == 0) {
                        result.add(RIGHT);
                        result.add(UP);
                        return result;
                    }
                    if ((col == 1) || (col == 2)) {
                        result.add(RIGHT);
                        result.add(LEFT);
                        result.add(UP);
                        return result;
                    }
                    if (col == 3) {
                        result.add(LEFT);
                        result.add(UP);
                        return result;
                    }
                    break;
        }
        System.out.println("Error! Wrong arguments to function!");
        return result;
    }
    
    public static Pair find0(int matrix[][]) {
        Pair wrong = new Pair();
        for(int i = 0;i < SIZE;i++)
            for(int j = 0;j < SIZE;j++)
                if (matrix[i][j] == 0) {
                    Pair result = new Pair(i,j);
                    return result;
                }
        System.out.println("Something is wrong! 0 is not in the matrix!");
        return wrong;
    }
    
    public static void fillVizMatrix(int matrix[][]) {
    	for(int i = 0;i < 4; i++)
    		for(int j = 0;j < 4;j++)
    			vizMatrix.set(matrix[i][j],i,j);
    }
    
    public static String convertPossibleMovesToString(TreeSet<Integer> possibleMoves) {
    	String result = "";
    	for(Integer direction: possibleMoves) {
    	/*
    	 * public static final int RIGHT = 0;
    	 public static final int DOWN = 1;
    	 public static final int LEFT = 2;
    	 public static final int UP = 3;
    	 */
    		switch (direction) {
    			case(RIGHT): result = result+"R ";
    						 break;
    			case(DOWN): result = result+"D ";
				 break;	
    			case(LEFT): result = result+"L ";
				 break;	
    			case(UP): result = result+"U ";
				 break;	
    		}
    	}
    	return result;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    	// TODO code application logic here
    	// The file name that contains the puzzle is passed
    	// as a command line argument;
    	// read15Puzzle(args[0]);
    	Pair result = find0(matrix);
    	TreeSet<Integer> possibleMoves = calcPossibleMoves(result.getX(),result.getY());
    	System.out.println("Possible initial moves: "+convertPossibleMovesToString(possibleMoves));
    	
    	// For visualization purposes:
    	
    	// The snapshot file that will be created:
    	 show = new ShowFile(args[0]);
    	
    	

    	// Create a Tree to be visualized
    	// The false parameter in the constructor indicates this is NOT a 
    	// binary tree
    	
    	vizTree = new GAIGStree(false,"Backtracking Tree","#FFFFFF",
    			0.0,0.0,1.0,0.6,0.1);
    	vizMatrix = new GAIGSarray(4,4,"Puzzle State","#FFFFFF",
    			0.0,0.6,1.0,1.0,0.1);
    	
    	TreeNode root = new TreeNode(convertPossibleMovesToString(possibleMoves));
    	
    	vizTree.setRoot(root);
	vizTree.setSpacing(0.5,1.25); // Added by TN 9/1/06
    	fillVizMatrix(matrix);
    	show.writeSnap(TITLE,docURL,pseudoURL,vizTree,vizMatrix);
    	
    	int i = 0;
    	long start = System.currentTimeMillis();
    	for(Integer direction: possibleMoves) {
    		
    		SearchThread searcher = new SearchThread(matrix,direction,root);
    		searcher.start();
    		
    		// Wait for the thread to finish
    		try {
    			searcher.join();
    		}
    		catch (InterruptedException ie) {
    			System.out.println(ie);
    		}
    		i++;
    	}
    	// Wait for all threads to die
    	
    	
    	if (!Main.solutionFound)
    		System.out.println("No solution was found.");
    	long end = System.currentTimeMillis();
    	System.out.println("Required time: "+(end-start)+" milliseconds.");
    	show.close();
    	System.exit(0);
    }
    
    
    
    
}
