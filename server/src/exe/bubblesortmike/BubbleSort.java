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

package exe.bubblesortmike;

import java.util.*;
import java.io.*;
import exe.*;

/**
 *
 * BubbleSort builds a .asu file for the Animal
 * Visualization tool.
 *
 * @author Michael J. Marsiglia
 * @version 1.0
 *
 * September 2001
 */

public class BubbleSort {

    /** the array to be sorted */
    private int[] bubble;
        
    /** the array to be sorted */
    private int[] bubble2;

    /** the file to write to */
    private PrintWriter out;

    /** random number generator */
    private Random r = new Random();

    /** Questions */
    private questionCollection questions;

    /** current question */
    private int currentQuestion;        

    /** variable to easily set the time between array switches, in ms */
    private final int TIME = 400;

    /** variable to easily set the SHORT time in ms */
    private final int SHORT = 50;

    /**
     * Builds a Bubble Sort
     *
     * @param size Size of the array
     * @param minDomain Min size for a number in the array
     * @param maxDomain Max size for a number in the array
     * @param asuFile Name of the asu file to create
     * @param noRepeats If true then no number will be repeated in the array.
     */
    public BubbleSort(int size, int minDomain, int maxDomain,
                      String asuFile, boolean noRepeats) {
        bubble = new int[size];
        bubble2 = new int[size];
        fillArray(minDomain, maxDomain, noRepeats);
        // copy array
        for (int i = 0; i < bubble.length; i++) {
            bubble2[i] = bubble[i];
        }
        try {
            if (! (asuFile.toLowerCase().endsWith(".asu"))) {
                asuFile = asuFile.concat(".asu");
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(asuFile)));
            questions = new questionCollection(out);
            currentQuestion = 0;
        } catch (IOException ex) {
            System.err.println("PrintWriter could not be created.  File: " +
                               asuFile);
            //ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Builds a Bubble Sort, with numbers ranging from 0 to 100,
     * and does not allow duplicate numbers.
     *
     * @param size Size of the array
     * @param asuFile Name of the asu file to create
     */
    public BubbleSort(int size, String asuFile) {
        this(size, 0, 100, asuFile, true);
    }
        
    /**
     * Builds a Bubble Sort, of array size 10 with numbers ranging
     * from 0 to 100, that does not allow duplicates.
     *
     * @param asuFile Name of the asu file to create
     */
    public BubbleSort(String asuFile) {
        this(10, 0, 100, asuFile, true);
    }
        
    /**
     * fills the Array with numbers
     *
     * @param min The min possible number to enter into the array
     * @param max The max possible number to enter into the array
     * @param noRepeats If true there will be no repeated in the array
     */
    private void fillArray(int min, int max, boolean noRepeats) {
        int amount = (max - min);
        if (noRepeats) {
            int[] dummyArray = new int[amount + 1];
            int a = 0;
            while (a < bubble.length) {
                int input = (r.nextInt(amount) + 1);
                if (dummyArray[input] != -1) {
                    dummyArray[input] = -1;
                    bubble[a] = (input + min);
                    a++;
                } 
            }
        } else {
            for (int i = 0; i < bubble.length; i++) {
                bubble[i] = ((r.nextInt(amount) + 1) + min);
            }
        }
                
    }

    /**
     * helper method used to print array in the form
     * "X""X""X""X"
     *
     * @return array in special format
     */
    private String printArray() {
        String numbers = "";
        for (int i = 0; i < bubble.length; i++) {
            numbers += "\"" + bubble[i] + "\"";
        }
        return numbers;
    }

    /**
     * Runs the program, building the script.
     */
    public void run() {
        out.println("%Animal 2");
        out.println("title Bubble Sort");
        out.println("author michael j. marsiglia <marsiglm@student.gvsu.edu>");
        out.println("{");
	//        out.println("documentation \"http://csf11.acs.uwosh.edu/jhave/html_root/doc/bubblesortmike/bubblesort.html\"");
        out.println("documentation \"doc/bubblesortmike/bubblesort.html\"");
        out.println("text \"header\" \"BUBBLE SORT (NO BOOLEAN)\" at (100,40)" +
                    " color red font SansSerif size 24 bold");
        out.println("array \"counter\" (260, 80) color black" +
                    " fillColor yellow elementColor red elemHighlight" +
                    " red cellHighlight blue vertical length 1 \"0\"");                 
        out.println("array \"bubble\" (100,200) color black" +
                    " fillColor white elementColor black elemHighlight" +
                    " red cellHighlight blue horizontal length " +
                    bubble.length + " " + printArray());
        // second array
        out.println("text \"header\" \"BUBBLE SORT (WITH BOOLEAN)\" at (100,260)" +
                    " color red font SansSerif size 24 bold");
        out.println("array \"counter2\" (260, 300) color black" +
                    " fillColor yellow elementColor red elemHighlight" +
                    " red cellHighlight blue vertical length 1 \"0\"");                 
        out.println("array \"bubble2\" (100,420) color black" +
                    " fillColor white elementColor black elemHighlight" +
                    " red cellHighlight blue horizontal length " +
                    bubble2.length + " " + printArray());
        out.println("arrayMarker \"top\"" +
                    " on \"counter\" atIndex 0 label \"Compares\"");
        // second array
        out.println("arrayMarker \"top2\"" +
                    " on \"counter2\" atIndex 0 label \"Compares\"");
        if (bubble.length > 1) {
            out.println("arrayMarker \"first\"" +
                        " on \"bubble\" atIndex 0 label \"j\"");
            out.println("arrayMarker \"second\"" +
                        " on \"bubble\" atIndex 1 label \"(j+1)\"");
        }
        // second array
        if (bubble2.length > 1) {
            out.println("arrayMarker \"first2\"" +
                        " on \"bubble2\" atIndex 0 label \"j\"");
            out.println("arrayMarker \"second2\"" +
                        " on \"bubble2\" atIndex 1 label \"(j+1)\"");
        }               
        makeCode();
        out.println("}");
        out.println("{");
        bubbleSort();
        bubbleSort2();
        out.println("}");
        if (currentQuestion > 0) {
            questions.animalWriteQuestionsAtEOSF();
        }                               
        out.close();
        // all the script code
        System.out.println("Script Written Successfully");
    }

    /**
     * helper method used to write code out in the animation
     */
    private void makeCode() {
        /*
          out.println("codeGroup \"code\" at (200, 80) color black" +
          " highlight color red");
          out.println("addCodeLine \"boolean ok;\" to \"code\"");
          out.println("addCodeLine \"int i;\" to \"code\"");
          out.println("addCodeLine \"int temp;\" to \"code\"");
          out.println("addCodeLine \"int size = bubble.length\" to \"code\"");
          out.println("addCodeLine \"do {\" to \"code\"");
          out.println("addCodeLine \"   ok = true;\" to \"code\"");
          out.println("addCodeLine \"   for (i = 0; i < (size - 1); i++) {\"" +
          " to \"code\"");
          out.println("addCodeLine \"      if (bubble[i] > bubble[i + 1]) {\"" +
          " to \"code\"");
          out.println("addCodeLine \"         temp = bubble[i];\" to \"code\"");
          out.println("addCodeLine \"         bubble[i] = bubble[i + 1];\"" +
          " to \"code\"");
          out.println("addCodeLine \"         bubble[i + 1] = temp;\"" +
          " to \"code\"");
          out.println("addCodeLine \"         ok = false;\" to \"code\"");
          out.println("addCodeLine \"      }\" to \"code\"");
          out.println("addCodeLine \"   }\" to \"code\"");
          out.println("addCodeLine \"   size--;\" to \"code\"");
          out.println("addCodeLine \"} while (! ok);\" to \"code\"");
        */
    }

    /**
     * helper method used to insert the code to highlight a
     * certain line
     *
     * @param line Line number to highlight
     */
    private void highlight(int line) {
        out.println("{");
        out.println("highlightCode on \"code\" line " +
                    line + " within " + SHORT + " ms");
        out.println("unhighlightCode on \"code\" line " +
                    line);
        out.println("}");
    }

    /**
     * Runs the bubble sort algorithm on the array, without
     * the boolean
     */
    private void bubbleSort() {
        tfQuestion tfQ = null;
        int magicNumber = 0;
        boolean maybe = false;          
        int compares = 0;
        int size = bubble.length;
        int temp;
        for (int i = 0; i < size - 1; i++) {
            if (r.nextInt(2) == magicNumber) {
                maybe = true;
            }
            for (int j = 0; j < size - i - 1; j++) {                            
                compares++;
                out.println("{");
                out.println("moveArrayIndex \"first\" to position " + j);
                out.println("moveArrayIndex \"second\" to position " + (j + 1));
                if (maybe) {
                    if (r.nextInt(3) == magicNumber) {
                        // true false question
                        tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                        tfQ.setQuestionText("\"Will items at indexes j and (j+1) change positions?\"");
                        tfQ.setAnswer(bubble[j] > bubble[j + 1]);
                        questions.addQuestion(tfQ);
                        out.println("{");
                        questions.animalInsertQuestion(currentQuestion);
                        out.println("}");
                        currentQuestion ++;
                        maybe = false;                                          
                    }
                }
                if (bubble[j] > bubble[j + 1]) {
                    temp = bubble[j];
                    bubble[j] = bubble[j + 1];
                    bubble[j + 1] = temp;
                    out.println("arraySwap on \"bubble\" position " + j +
                                " with " + (j + 1) + " within " + TIME + " ms");
                }
                out.println("arrayPut \"" + compares +
                            "\" on \"counter\" position 0");                            
                out.println("}");
            }
            out.println("{");
            out.println("highlightArrayCell on \"bubble\" position " +
                        (size - i - 1));
            out.println("}");
        }
        out.println("{");
        out.println("highlightArrayCell on \"bubble\" position " + 1);
        out.println("highlightArrayCell on \"bubble\" position " + 0);
        out.println("}");
                
    }

    /**
     * Runs the bubble sort algorithm on the array, with
     * the boolean
     */
    private void bubbleSort2() {
        tfQuestion tfQ = null;
        int magicNumber = 0;
        boolean maybe = false;                  
        int compares = 0;
        int stop = 1;
        int size = bubble2.length;
        int temp;
        for (int i = 0; i < size - 1; i++) {
            if (r.nextInt(3) == magicNumber) {
                maybe = true;
            }
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                compares++;
                out.println("{");
                out.println("moveArrayIndex \"first2\" to position " + j);
                out.println("moveArrayIndex \"second2\" to position " + (j + 1));
                if (bubble2[j] > bubble2[j + 1]) {
                    temp = bubble2[j];
                    bubble2[j] = bubble2[j + 1];
                    bubble2[j + 1] = temp;
                    swapped = true;
                    out.println("arraySwap on \"bubble2\" position " + j +
                                " with " + (j + 1) + " within " + TIME + " ms");
                }
                out.println("arrayPut \"" + compares +
                            "\" on \"counter2\" position 0");                           
                out.println("}");
            }
            if (maybe) {
                if (r.nextInt(3) == magicNumber) {
                    // check if the algorithm is done
                    tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                    tfQ.setQuestionText("\"Have we found the correct order?\"");
                    tfQ.setAnswer(! swapped);
                    questions.addQuestion(tfQ);
                    out.println("{");
                    questions.animalInsertQuestion(currentQuestion);
                    out.println("}");
                    currentQuestion ++;
                    maybe = false;                                      
                }
            }
            if (! swapped) {
                stop = size - i - 1;
                                // prove the end of the algorithm
                                // only if we have less than three questions
                if (currentQuestion < 3) {
                    tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                    tfQ.setQuestionText("\"Have we found the correct order?\"");
                    tfQ.setAnswer(true);
                    questions.addQuestion(tfQ);
                    out.println("{");
                    questions.animalInsertQuestion(currentQuestion);
                    out.println("}");
                    currentQuestion ++;                                         
                }
                break;
            }
            out.println("{");
            out.println("highlightArrayCell on \"bubble2\" position " +
                        (size - i - 1));
            out.println("}");
        }
        out.println("{");
        for (int a = stop; a >= 0; a--) {
            out.println("highlightArrayCell on \"bubble2\" position " + a);
        }
        out.println("}");
    }
        

    /**
     * main program
     * currently the command is really hard coded, maybe in
     * future versions it will be nicer.
     *
     * usage: java exe.bubblesortmike.BubbleSort
     *        arraySize minDomain maxDomain outputFile noRepeats
     *
     */
    public static void main(String[] args) {
        String usage = "java exe.bubblesortmike.BubbleSort " +
            "arraySize minDomain maxDomain noRepeats outputFile";
                
        if (args.length == 5) {
            try {
                int size = new Integer(args[0]).intValue();
                int min = new Integer(args[1]).intValue();
                int max = new Integer(args[2]).intValue();
                boolean repeats = true;
                if (args[3].equalsIgnoreCase("false")) {
                    repeats = false;
                }
                String fName = args[4];
                BubbleSort bs = new BubbleSort(size, min, max, fName, repeats);
                bs.run();
            } catch (Exception e) {
                System.out.println(usage);
                System.exit(1);
            }
        } else {
            System.out.println(usage);
        }

    }
        
}

        











