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

package exe.myles_bubsort_example;

import java.io.*;
import java.util.Scanner;
import java.util.Random;
import exe.*;

/**
 * <p>This simple visualization of the Bubblesort algorithm is designed to demonstrate
 * some of the basics of the GAIGS XML support classes.  The two structures used
 * are <code>GAIGSarray</code> and <code>GAIGSlabel</code>.  The <code>GAIGSarray</code> is
 * shown as a bargraph, and represents the values in the array.  The name of the
 * <code>GAIGSarray</code> is used to give general status information about the sorting
 * process. The <code>GAIGSlabel</code> is used to give the current pass number and the
 * number of comparisons and swaps done so far.</p>
 * 
 * <p>If you change the display mode of the <code>GAIGSarray</code> to array, you will
 * see the array display format.  You will have to move the <code>GAIGSlabel</code>, as
 * in its current position it will overlap with the diplayed array.</p>
 * 
 * <p>Two types of questions are asked:
 *   <ol>
 *      <li>The number of swaps that will be done during a pass (asked just before
 *           a pass begins).</li>
 *      <li>Whether a swap will be done (asked when a comparison is made). </li>
 *   </ol>
 * </p>
 *   
 * <p>To demonstrate the use of probibilistic question asking, no more than
 * <code>MAX_QUESTIONS_TO_BE_ASKED</code> questions will be asked during the visualization.
 * This is accomplished by passing the <code>ShowFile</code> contructor the maximum number
 * of questions to be asked, and the number of possible opportunities for question asking.</p>
 * 
 * <p>Example Command Line Parameters: "bubble.sho", "5"</p>
 * 
 * @author Myles McNally 
 * @version 5/28/06
 */

public class BubbleSort {

    static final String TITLE = "BubbleSort";
    static final String DEFAULT_COLOR  = "#666666";
    static final String ACTIVE_COLOR   = "#FF0000";
    static final String COMPARE_COLOR  = "#FF0000";    
    static final String FINISHED_COLOR = "#0000CC";
    static final int MAX_QUESTIONS_TO_BE_ASKED = 5;
    
    static int arraySize;
    static GAIGSarray items;
    static GAIGSlabel message;
    
    public static void main(String args[]) throws IOException {
   
        // process program parameters and create the show file object
        arraySize = Integer.parseInt(args[1]);
                         // one question per pass + one question at each comparison
        int questionOpportunities = (arraySize-1) + (arraySize * (arraySize-1))/2;
                         // can't ask more questions than there are opportunities
        int questionToBeAsked = Math.min(MAX_QUESTIONS_TO_BE_ASKED, questionOpportunities);
        ShowFile show = new ShowFile(args[0], questionToBeAsked, questionOpportunities);

        // define the two structures in the show snapshots
        items = new GAIGSarray(arraySize, true, null, DEFAULT_COLOR, 0.1, 0.1, 0.9, 0.9, 0.04);
        message = new GAIGSlabel("", 0.1, -0.5, 0.9, 0.3, 0.07);
        
        // operation counts for display
        int comps = 0;
        int swaps = 0;
        
        // question objects and id
        XMLfibQuestion fib;
        XMLtfQuestion tf;
        int idNum = 0;

        // initialize the array to be sorted
        loadArray();     
   
        //sort the array and create the show file
        for (int pass = 1; pass < arraySize; pass++) {

            // pass begins
            items.setName("Beginning of Pass " + pass);
            message.setLabel("Pass: " + pass + "   Comparisons: " + comps + "   Swaps: " + swaps);
            fib = new XMLfibQuestion(show, new Integer(idNum).toString());
            fib.setQuestionText("How many swaps will be made during this pass?");
            idNum++;
            int swapsThisPass = 0;
            show.writeSnap(TITLE, null, null, fib, items, message);
            
            for (int i = 0; i < arraySize-pass; i++) {
            
                // ready to compare
                items.setName("Compare Next Pair of Items");
                items.setColor(i, ACTIVE_COLOR);
		items.setRowLabel(i + "\n^", i);
                items.setColor(i+1, COMPARE_COLOR);
		items.setRowLabel((i+1) + "\n^", i+1);
                tf = new XMLtfQuestion(show, new Integer(idNum).toString());
                tf.setQuestionText("These items will be swapped.");
                idNum++;
                show.writeSnap(TITLE, null, null, tf, items, message);
                
                //compare & outcome
                comps++;
                if ((Integer)(items.get(i)) > (Integer)(items.get(i+1))) {
                    swaps++;
                    swapsThisPass++;
                    swap(i, i+1);
                    items.setColor(i, COMPARE_COLOR);
		    //		    items.setRowLabel(i + "\n^", i);
                    items.setColor(i+1, ACTIVE_COLOR);
		    //		    items.setRowLabel((i+1) + "\n^", i+1);
                    items.setName("Items were swapped");
                    tf.setAnswer(true);
                } else {
                    items.setName("Items were not swapped");
                    tf.setAnswer(false);
                }
                message.setLabel("Pass: " + pass + "      Comparisons: " + comps + "      Swaps: " + swaps);
                show.writeSnap(TITLE, items, message);
                    
                // leave item behind
                items.setColor(i, DEFAULT_COLOR);
		items.setRowLabel(i + "\n ", i);
            }
                        
            // pass is finished
            items.setColor(arraySize-pass, FINISHED_COLOR);
	    items.setRowLabel((arraySize-pass) + "\n ", arraySize-pass);
            items.setName("Pass " + pass + " Completed");
            fib.setAnswer(swapsThisPass +"");
            show.writeSnap(TITLE, items, message);
        }
        
        // sorting completed
        items.setColor(0, FINISHED_COLOR);
        items.setName("Bubblesort Completed");
        message.setLabel("Passes: " + (arraySize-1) + "   Comparisons: " + comps + "   Swaps: " + swaps);
        show.writeSnap(TITLE, items, message);

        show.close();                    
    }
    
 //-------- Support methods ---------------------------------------------------------------
   
   /**
    * Load the array with values from 1 to the array size.
    * Then shuffle these values so that they appear in random order.
    */
   private static void loadArray () {
        Random rand = new Random();
        for (int i = 0; i < arraySize; i++){
            items.set(i+1,i);
	    items.setRowLabel(i + "\n ", i);
	}
        for (int i = 0; i < arraySize-1; i++)
            swap(i, i + (Math.abs(rand.nextInt()) % (arraySize - i)) );
    }
    
   /**
    * Swap two items in the array.
    */
   private static void swap (int loc1, int loc2) {
        Object temp = items.get(loc1);
        items.set(items.get(loc2), loc1);
        items.set(temp, loc2);    
    }
}


