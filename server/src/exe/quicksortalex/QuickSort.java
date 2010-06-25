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

/** Creates a visual representation of the Quick Sort Algorithm, using the
 *  Animal scripting system. Usage:
 *  { java QuickSort asuFile }
 *  Where asuFile is the .ASU file to be created.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 1.0
 */

package exe.quicksortalex;

import exe.*;
import java.io.*;
import java.util.*;

/** Framework for the demonstration of the Quick Sort Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
public class QuickSort {
    private PrintWriter out;
    private questionCollection questions;
    private int qIndex = 0;

/** Creates output stream.
 *
 *  @param asuFile      Name of the .ASU file to be writen.
 *  @throws IOException Error creating file stream.
 */
    public QuickSort(String asuFile) {
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(asuFile)));
            questions = new questionCollection(out);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Writes header information and calls necessary procedures.
 *
 *  @param args         Array of command line parameters.
 */
    public static void main(String[] args) {
        String asuFile = args[0];
        QuickSort demo = new QuickSort(asuFile);
        Random r = new Random();
        int num = r.nextInt(11) + 10;
        int[] array = new int[num];
	// Forgive me God, this is an ugly way to guarantee no repeats, but the
	// the array is small and computers are fast!
	for(int i = 0; i < array.length; i++) {
	    boolean repeat;
	    do {
		array[i] = r.nextInt(99) + 1;
		repeat = false;
		for (int j = 0; j < i; j++ ) {
		    repeat = repeat || (array[j] == array[i]);
		}
	    } while (repeat);
	    System.out.println(""+array[i]);
	}
        int visArrayLen = array.length + 2;
        int lastNum = array.length - 1;
        demo.out.println("%Animal 2");
        demo.out.println("{");
        demo.out.print("array \"qarray\" (100,10) color black fillColor white");
        demo.out.print(" elementColor black elemHighlight red cellHighlight yellow");
        demo.out.print(" vertical length ");
        demo.out.print("" + visArrayLen + " ");
        for(int j = 0; j < array.length; j++)
            demo.out.print("\"" + array[j] + "\"");
        demo.out.println("\"Pivot\"\"X\"");
        demo.out.println("highlightArrayCell on \"qarray\" position " + array.length);
        demo.out.println("arrayMarker \"lo\" on \"qarray\" atIndex 0 label \"lo\"");
        demo.out.print("arrayMarker \"hi\" on \"qarray\" atIndex ");
        demo.out.println("" + lastNum + " label \"hi\"");
        demo.out.println("}");
        demo.out.println("{");
        demo.sort(array, 0, array.length - 1);
        demo.out.println("}");
        demo.questions.animalWriteQuestionsAtEOSF();
        demo.out.close();
    }

/** Recursive function to sort array of integers using the Quick Sort
 *  Algorithm.
 *
 *  @param array        Array of integers to sort.
 *  @param begin        The starting index in <code>array</code> of the range
 *                      to be sorted.
 *  @param end          The ending index in <code>array</code> of the range to
 *                      be sorted.
 */
    void sort(int[] array, int begin, int end) {
        int[] arraycpy = new int[array.length];
        for(int i = 0; i < array.length; i++)
            arraycpy[i] = array[i];
        if(begin < end) {
            //fibQuestion q = new fibQuestion(out, (new Integer(qIndex)).toString());
	    // Remember that Animal wants quotes around the question text
            //q.setQuestionText("\"Which number in the array will be chosen as pivot index?\"");
	    // questions.addQuestion(q);
	    // out.println("{");
            //questions.animalInsertQuestion(qIndex);
            //out.println("}");
            int pivIndex = partition(array, begin, end);
	    // After calling on partition, pivIndex is used to set the answer
	    // for the question
	    // And, remember that Animal wants quotes around the question answer
            //q.setAnswer("\"" + arraycpy[pivIndex] + "\"");
            //qIndex++;
            sort(array, begin, pivIndex - 1);
            sort(array, pivIndex + 1, end);
        }
        else {
            out.println("{");
            out.println("highlightArrayCell on \"qarray\" position " + begin);
            out.println("}");
        }
    }

/** Performs the section of the Quick Sort Algorithm which creates partitions.
 *
 *  @param array        The array of integers to be sorted.
 *  @param first        The first index in <code>array</code> of the range to
 *                      be partitioned.
 *  @param last         The last index in <code>array</code> of the range to
 *                      be partitioned.
 */
    int partition(int[] array, int first, int last) {
        int pivVal = array[first];
        int up = first;
        int down = last;
        int tmp;
        int lastIndex = array.length + 1;
	fibQuestion q = null;
        out.println("{");
        out.println("moveArrayIndex \"lo\" to position " + first);
        out.println("moveArrayIndex \"hi\" to position " + last);
        out.print("arraySwap on \"qarray\" position " + first + " with " + lastIndex);
        out.println(" within 500 ms");
        out.println("}");
	boolean question_at_end = false;
        while(up < down) {

	    // The next two loops are "fakes", doing a preliminary pass to 
	    // see if we need to generate a question
	    int up_dup = up;
	    int down_dup = down;
            while((array[up_dup] <= pivVal) && (up_dup < last)) 
                up_dup++;
            while((array[down_dup] > pivVal) && (down_dup > first)) 
                down_dup--;

	    if ( (up_dup < down_dup) ) {
		q = new fibQuestion(out, (new Integer(qIndex)).toString());
		// Remember that Animal wants quotes around the question text
		q.setQuestionText("\"Which numbers in the array will swap next?\"");
		questions.addQuestion(q);
		out.println("{");
		questions.animalInsertQuestion(qIndex);
		out.println("}");
	    }
	    else {
		question_at_end = true;
		q = new fibQuestion(out, (new Integer(qIndex)).toString());
		// Remember that Animal wants quotes around the question text
		q.setQuestionText("\"At what number in the array will the pointers meet?\"");
		questions.addQuestion(q);
		out.println("{");
		questions.animalInsertQuestion(qIndex);
		out.println("}");
	    }

            while((array[up] <= pivVal) && (up < last)) {
                up++;
                out.println("{");
                out.println("moveArrayIndex \"lo\" to position " + up);
                out.println("}");
            }
            while((array[down] > pivVal) && (down > first)) {
                down--;
                out.println("{");
                out.println("moveArrayIndex \"hi\" to position " + down);
                out.println("}");
            }
            if(up < down) {
		// And, remember that Animal wants quotes around the question answer
		q.setAnswer("\"" + array[up] + " " + array[down] + "\"" + "\n"
			    + "\"" + array[down] + " " + array[up] + "\"");
		qIndex++;
                tmp = array[up];
                array[up] = array[down];
                array[down] = tmp;
                out.println("{");
                out.print("arraySwap on \"qarray\" position " + up + " with " + down);
                out.println(" within 500 ms");
                out.println("}");
            }
        }
	// And, remember that Animal wants quotes around the question answer
	// There was no swap but the pointers met
	if (question_at_end) {
	    q.setAnswer("\"" + array[up] + "\"");
	    qIndex++;
	}

        if(first != down) {
	    // And, remember that Animal wants quotes around the question answer
	    //q.setAnswer("\"" + array[first] + " " + array[down] + "\"" + "\n"
	    //	+ "\"" + array[down] + " " + array[first] + "\"");
	    //qIndex++;
            tmp = array[first];
            array[first] = array[down];
            array[down] = tmp;
            out.println("{");
            out.print("arraySwap on \"qarray\" position " + first + " with " + down);
            out.println(" within 500 ms");
            out.println("}");
        }
        out.println("{");
        out.print("arraySwap on \"qarray\" position " + down + " with " + lastIndex);
        out.println(" within 500 ms");
        out.println("highlightArrayCell on \"qarray\" position " + down);
        out.println("}");
        return down;
    }

}
        
        
