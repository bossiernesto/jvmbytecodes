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

package exe.insertionsortmike;

import java.util.*;
import java.io.*;
import exe.*;

/**
 *
 * InsertionSort builds a .asu file for the Animal
 * Visualization tool.
 *
 * @author Michael J. Marsiglia
 * @version 1.0
 *
 * September 2001
 */

public class InsertionSort {

    /** the ArrayList to be sorted */
    private ArrayList insertion;

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
     * Builds a Insertion Sort
     *
     * @param size Size of the array
     * @param minDomain Min size for a number in the array
     * @param maxDomain Max size for a number in the array
     * @param asuFile Name of the asu file to create
     * @param noRepeats If true then no number will be repeated in the array.
     */
    public InsertionSort(int size, int minDomain, int maxDomain,
                         String asuFile, boolean noRepeats) {
        insertion = new ArrayList();
        fillArray(size, minDomain, maxDomain, noRepeats);
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
     * Builds a Insertion Sort, with numbers ranging from 0 to 100,
     * and does not allow duplicate numbers.
     *
     * @param size Size of the array
     * @param asuFile Name of the asu file to create
     */
    public InsertionSort(int size, String asuFile) {
        this(size, 0, 100, asuFile, true);
    }
        
    /**
     * Builds a Insertion Sort, of array size 10 with numbers ranging
     * from 0 to 100, that does not allow duplicates.
     *
     * @param asuFile Name of the asu file to create
     */
    public InsertionSort(String asuFile) {
        this(10, 0, 100, asuFile, true);
    }
        
    /**
     * fills the Array with numbers
     *
     * @param size The size to make the Array
     * @param min The min possible number to enter into the array
     * @param max The max possible number to enter into the array
     * @param noRepeats If true there will be no repeated in the array
     */
    private void fillArray(int size, int min, int max, boolean noRepeats) {
	//        Random r = new Random();
        int amount = (max - min);
        if (noRepeats) {
            int[] dummyArray = new int[amount + 1];
            int a = 0;
            while (a < size) {
                int input = (r.nextInt(amount) + 1);
                if (dummyArray[input] != -1) {
                    dummyArray[input] = -1;
                    insertion.add(new Integer(input + min));
                    a++;
                } 
            }
        } else {
            for (int i = 0; i < size; i++) {
                insertion.add(new Integer((r.nextInt(amount) + 1) + min));
            }
        }
        // add the top two slots
        insertion.add(0, "Insert");
        // blank spot
        insertion.add(1, "");
                
    }

    /**
     * helper method used to print array in the form
     * "X""X""X""X"
     *
     * @return array in special format
     */
    private String printArray() {
        String numbers = "";
        for (int i = 0; i < insertion.size(); i++) {
            numbers += "\"" + insertion.get(i) + "\"";
        }
        return numbers;
    }

    /**
     * Runs the program, building the script.
     */
    public void run() {
        out.println("%Animal 2");
        out.println("title Insertion Sort");
        out.println("author michael j. marsiglia <marsiglm@student.gvsu.edu>");
        out.println("{");
	//        out.println("documentation \"http://csf9.acs.uwosh.edu/jhave/html_root/doc/insertionsortmike/insertionsort.html\"");
        out.println("documentation \"doc/insertionsortmike/insertionsort.html\"");
        out.println("text \"header\" \"INSERTION SORT\" at (100,50)" +
                    " color red font SansSerif size 24 bold");
        out.println("array \"insertion\" (200,80) color black" +
                    " fillColor white elementColor black elemHighlight" +
                    " red cellHighlight blue vertical length " +
                    insertion.size() + " " + printArray());
        if (insertion.size() > 3) {
            out.println("arrayMarker \"zero\"" +
                        " on \"insertion\" atIndex 2 label \"Index 0     \"");
            out.println("arrayMarker \"first\"" +
                        " on \"insertion\" atIndex 3 label \"k\"");
            out.println("arrayMarker \"second\"" +
                        " on \"insertion\" atIndex 2 label \"j\"");
        }
        out.println("highlightArrayCell on \"insertion\" position " + 0);
        out.println("}");
        out.println("{");
        insertionSort();
        out.println("}");
        if (currentQuestion > 0) {
            questions.animalWriteQuestionsAtEOSF();
        }               
        out.close();
        // all the script code
        System.out.println("Script Written Successfully");
    }


    /**
     * Runs the bubble sort algorithm on the array
     */
    private void insertionSort() {

        tfQuestion tfQ = null;
        mcQuestion mcQ = null;          
        //int size = insertion.length;
        int choice;
        int magicNumber = 0;
        boolean maybe = false;
        Vector v = null;                


        int size = insertion.size();
        Integer itemToInsert;
        int j;
                
        for (int i = 3; i < size; i++) {

            // possible question
            if (r.nextInt(1) == magicNumber && i != 3) {
                maybe = true;
            }                   




            itemToInsert = (Integer) insertion.get(i);
            j = (i - 1);

	    // question processing
            if (maybe) {
                if (r.nextInt(3) == magicNumber) {
                    // mult choice question
                    mcQ = new mcQuestion(out, Integer.toString(currentQuestion));
                    mcQ.setQuestionText("\"What index will become the itemToInsert?\"");
                    v = generateChoices(i - 2, size - 2);
                    for (int a = 0; a < v.size(); a++) {
                        mcQ.addChoice("\"" + ((String) v.elementAt(a)) + "\"");
                    }
                    mcQ.setAnswer("\"" + Integer.toString(i - 2) + "\"");
                    questions.addQuestion(mcQ);
                    out.println("{");
                    questions.animalInsertQuestion(currentQuestion);
                    out.println("}");
                    currentQuestion ++;
                    maybe = false;
                }
            }


            out.println("{");
            out.println("unhighlightArrayCell on \"insertion\" position " +
                        (i - 1));                       
            out.println("moveArrayIndex \"first\" to position " + i);
            out.println("moveArrayIndex \"second\" to position " + j);
            out.println("arraySwap on \"insertion\" position 0 with " +
                        i + " within " + TIME + " ms");
            out.println("highlightArrayCell on \"insertion\" position " + i);
            out.println("highlightArrayCell on \"insertion\" position " +
                        0);
            out.println("}");

	    if (r.nextInt(2) == magicNumber) maybe = true;
            // only go til 2
            while (j >= 2) {

		// Question processing
                if (maybe) {
                    if (r.nextInt(3) == magicNumber) {
                        // insert TF question
                        tfQ = new tfQuestion(out, Integer.toString(currentQuestion));
                        //tfQ.setQuestionText("\"Will itemToInsert change value?\"");
			tfQ.setQuestionText("\"Have we found the insertion slot?\"");
			tfQ.setAnswer(itemToInsert.intValue() >= ((Integer)insertion.get(j)).intValue());
                        //tfQ.setAnswer(itemToInsert >= insertion[j]);
                        questions.addQuestion(tfQ);
                        out.println("{");
                        questions.animalInsertQuestion(currentQuestion);
                        out.println("}");
                        currentQuestion ++;
                        maybe = false;
                    }
                }




                if (itemToInsert.intValue() <
                    ((Integer)insertion.get(j)).intValue()) {
                    out.println("{");
                    out.println("arraySwap on \"insertion\" position " + j + " with " +
                                (j + 1) + " within " + TIME + " ms");
                    out.println("highlightArrayCell on \"insertion\" position " + j);
                    out.println("unhighlightArrayCell on \"insertion\" position " +
                                (j + 1));
                    out.println("moveArrayIndex \"second\" to position " + j);
                    insertion.set((j + 1), insertion.get(j));
                    out.println("}");
                    j--;
                } else {
                    break;
                }
            }
            out.println("{");
            out.println("arraySwap on \"insertion\" position " + 0 + " with " +
                        (j + 1) + " within " + TIME + " ms");
            out.println("unhighlightArrayCell on \"insertion\" position " +
                        (j + 1));
            out.println("}");
            insertion.set((j + 1), itemToInsert);
        }
        System.out.println(printArray());
                
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
     * usage: java exe.bubblesortmike.InsertionSort
     *        arraySize minDomain maxDomain outputFile noRepeats
     *
     */
    public static void main(String[] args) {
        String usage = "java exe.insertionsortmike.InsertionSort " +
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
                InsertionSort ss = new InsertionSort(size, min, max, fName, repeats);
                ss.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(usage);
                System.exit(1);
            }
        } else {
            System.out.println(usage);
        }

    }
        
}










