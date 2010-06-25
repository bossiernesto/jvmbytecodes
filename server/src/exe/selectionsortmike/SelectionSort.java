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

package exe.selectionsortmike;

import java.util.*;
import java.io.*;
import exe.*;

/**
 *
 * SelectionSort builds a .asu file for the Animal
 * Visualization tool.
 *
 * @author Michael J. Marsiglia
 * @version 1.0
 *
 * September 2001
 */

public class SelectionSort {

    /** the array to be sorted */
    private int[] selection;

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
     * Builds a Selection Sort
     *
     * @param size Size of the array
     * @param minDomain Min size for a number in the array
     * @param maxDomain Max size for a number in the array
     * @param asuFile Name of the asu file to create
     * @param noRepeats If true then no number will be repeated in the array.
     */
    public SelectionSort(int size, int minDomain, int maxDomain,
                         String asuFile, boolean noRepeats) {
        selection = new int[size];
        fillArray(minDomain, maxDomain, noRepeats);
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
     * Builds a Selection Sort, with numbers ranging from 0 to 100,
     * and does not allow duplicate numbers.
     *
     * @param size Size of the array
     * @param asuFile Name of the asu file to create
     */
    public SelectionSort(int size, String asuFile) {
        this(size, 0, 100, asuFile, true);
    }
        
    /**
     * Builds a Selection Sort, of array size 10 with numbers ranging
     * from 0 to 100, that does not allow duplicates.
     *
     * @param asuFile Name of the asu file to create
     */
    public SelectionSort(String asuFile) {
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
            while (a < selection.length) {
                int input = (r.nextInt(amount) + 1);
                if (dummyArray[input] != -1) {
                    dummyArray[input] = -1;
                    selection[a] = (input + min);
                    a++;
                } 
            }
        } else {
            for (int i = 0; i < selection.length; i++) {
                selection[i] = ((r.nextInt(amount) + 1) + min);
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
        for (int i = 0; i < selection.length; i++) {
            numbers += "\"" + selection[i] + "\"";
        }
        return numbers;
    }

    /**
     * Runs the program, building the script.
     */
    public void run() {
        out.println("%Animal 2");
        out.println("title Selection Sort");
        out.println("author michael j. marsiglia <marsiglm@student.gvsu.edu>");
        out.println("{");
	//        out.println("documentation \"http://csf9.acs.uwosh.edu/jhave/html_root/doc/selectionsortmike/selectionsort.html\"");
        out.println("documentation \"doc/selectionsortmike/selectionsort.html\"");
        out.println("text \"header\" \"SELECTION SORT\" at (100,50)" +
                    " color red font SansSerif size 24 bold");
        out.println("array \"selection\" (260,80) color black" +
                    " fillColor white elementColor black elemHighlight" +
                    " red cellHighlight blue vertical length " +
                    selection.length + " " + printArray());
        if (selection.length > 1) {
            out.println("arrayMarker \"original\"" +
                        " on \"selection\" atIndex 0 label \"k                     \"");
            out.println("arrayMarker \"first\"" +
                        " on \"selection\" atIndex 0 label \"minIndex\"");
            out.println("arrayMarker \"second\"" +
                        " on \"selection\" atIndex 1 label \"j                 \"");
        }
        makeCode();
        out.println("}");
        out.println("{");
        selectionSort();
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
     * Runs the selection sort algorithm on the array
     */
    private void selectionSort() {
        tfQuestion tfQ = null;
        mcQuestion mcQ = null;
        int size = selection.length;
        int temp;
        int choice;
        int magicNumber = 0;
        boolean maybe = false;
        Vector v = null;
        for (int i = 0; i < size - 1; i++) {
            // possible question
            if (r.nextInt(2) == magicNumber && i != 0) {
                maybe = true;
            }
            out.println("{");
            out.println("moveArrayIndex \"original\" to position " + i);
            out.println("}");
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (maybe) {
                    if (r.nextInt(4) == magicNumber) {
                        // insert question
                        if (r.nextInt(2) == magicNumber) {
                            // tf question
                            tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                            choice = r.nextInt(size - j) + j;   
                            if (r.nextInt(2) == magicNumber) {
                                tfQ.setQuestionText("\"Will minIndex move to array index " +
                                                    choice + "?\"");
                                tfQ.setAnswer(choice == minIndex);
                            } else {
                                tfQ.setQuestionText("\"Will j move to array index " +
                                                    choice + "?\"");
                                tfQ.setAnswer(choice == j);
                            }
                            questions.addQuestion(tfQ);
                        } else {
                            // mc
                            mcQ = new mcQuestion(out, Integer.toString(currentQuestion));
                            if (r.nextInt(2) == magicNumber) {
                                // first index
                                mcQ.setQuestionText(
                                                    "\"Which array index will minIndex move to?\"");
                                v = generateChoices(minIndex, size);
                                for (int a = 0; a < v.size(); a++) {
                                    mcQ.addChoice("\"" + ((String) v.elementAt(a)) + "\"");
                                }
                                mcQ.setAnswer("\"" + Integer.toString(minIndex) + "\"");
                            } else {
                                // second index
                                mcQ.setQuestionText(
                                                    "\"Which array index will minIndex move to?\"");
                                v = generateChoices(minIndex, size);
                                for (int a = 0; a < v.size(); a++) {
                                    mcQ.addChoice("\"" + ((String) v.elementAt(a)) + "\"");
                                }
                                mcQ.setAnswer("\"" + Integer.toString(minIndex) + "\"");
                            }
                            questions.addQuestion(mcQ);
                        }
                        out.println("{");
                        questions.animalInsertQuestion(currentQuestion);
                        out.println("}");
                        currentQuestion ++;
                        maybe = false;
                    }
                }       
                out.println("{");
                out.println("moveArrayIndex \"first\" to position " + minIndex);
                out.println("moveArrayIndex \"second\" to position " + j);                                      
                if (selection[j] < selection[minIndex]) {
                    minIndex = j;
                    out.println("moveArrayIndex \"first\" to position " + j);
                }
                out.println("}");
            }
            if (maybe) {
                if (r.nextInt(3) == magicNumber) {
                    tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                    tfQ.setQuestionText("\"Will a swap occur?\"");
                    tfQ.setAnswer(minIndex != i);
                    questions.addQuestion(tfQ);
                }
                out.println("{");
                questions.animalInsertQuestion(currentQuestion);
                out.println("}");
                currentQuestion ++;
                maybe = false;
            }                           
            if (minIndex != i) {
                out.println("{");
                out.println("arraySwap on \"selection\" position" +
                            " " + i + " with " + minIndex + " within " +
                            TIME + " ms");                              
                temp = selection[i];
                selection[i] = selection[minIndex];
                selection[minIndex] = temp;
                out.println("}");
            } 
            out.println("{");
            out.println("highlightArrayCell on \"selection\" position " + i);
            out.println("}");
        }               
        out.println("{");
        out.println("highlightArrayCell on \"selection\" position " + (size-1));
        out.println("}");       
                                        
    }

    /**
     * private method used to generate choices in a random
     * order.
     *
     * @param answer the correct answer
     * @param size high range
     * @return random order <code>Vector</code>
     * of Strings
     */

    private Vector generateChoices(int answer, int size) {
        Vector v = new Vector();
        String ans = Integer.toString(answer);
        int i = 0;
        while (i < 3) {
            String data = Integer.toString(r.nextInt(size));
            if ((! v.contains(data)) && (! ans.equals(data))) {
                v.add(data);
                i ++;
            }
        }
        v.insertElementAt(ans, r.nextInt(4));
        return v;
    }

    /**
     * main program
     * currently the command is really hard coded, maybe in
     * future versions it will be nicer.
     *
     * usage: java exe.bubblesortmike.SelectionSort
     *        arraySize minDomain maxDomain outputFile noRepeats
     *
     */
    public static void main(String[] args) {
        String usage = "java exe.bubblesortmike.SelectionSort " +
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
                SelectionSort ss = new SelectionSort(size, min, max, fName, repeats);
                ss.run();
            } catch (Exception e) {
                System.out.println(usage);
                System.exit(1);
            }
        } else {
            System.out.println(usage);
        }

    }
        
}










