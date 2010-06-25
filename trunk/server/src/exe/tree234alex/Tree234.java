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

/** Tree234.java: Creates visual representations of 2-3-4 Trees using the
 *  GAIGS system. Usage -- <code>{ java Tree234</code> targetFile }, where targetFile
 *  is the name of the text file to be generated, without .SHO extension.
 *  <p>
 *  Alex Zimmerman and Tom Naps, 13 July 2001.
 */

package exe.tree234alex;

import java.io.*;
import java.util.*;
import exe.*;

/** Implementation of one node in a 2-3-4 Tree.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */
class Node234 {
    int firstInfo;
    int secondInfo;
    int thirdInfo;
    Node234 firstChild;
    Node234 secondChild;
    Node234 thirdChild;
    Node234 fourthChild;

/** Constructor for 2-3-4 Node object.
 */
    Node234() {
        firstInfo = -1;
        secondInfo = -1;
        thirdInfo = -1;
        firstChild = null;
        secondChild = null;
        thirdChild = null;
        fourthChild = null;
    }

}

/** Implementation of the 2-3-4 Tree framwork.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */
public class Tree234 {
    private Node234 root;
    private PrintWriter out;
    private questionCollection questions;
    private int qIndex = 0;

/** Constructor for 2-3-4 Tree object.
 *
 *  @param start        The node to make the root of the tree.
 *  @param gaigsFile    The string containing the desired .SHO file name.
 *  @throws IOException Error if file cannot be created.
 */
    Tree234(Node234 start, String gaigsFile) {
        root = start;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(gaigsFile)));
            questions = new questionCollection(out);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Creates random array of data and calls appropriate methods to create
 *  .SHO file.
 *
 *  @param args         Array of command line arguments. Only the first is used.
 */
    public static void main(String[] args) {
            int dummyVariable;
            String gaigsFile = args[0];
            Random r = new Random();
            int arraySize = r.nextInt(3) + 18;
            int[] array = new int[arraySize];
            for(int i = 0; i < arraySize; i++)
                array[i] = r.nextInt(99) + 1;
            Node234 start = new Node234();
            Tree234 tree = new Tree234(start, gaigsFile);
            for(int j = 0; j < arraySize; j++)
                dummyVariable = tree.add(array, j);
            tree.questions.writeQuestionsAtEOSF();
            tree.out.close();
    }

/** Determines if a given node is a 2-Node, a 3-Node, or a 4-Node.
 *
 *  @param test         The node with type to be determined.
 *  @return             An integer specifying a 2-, 3-, or 4-Node, or 0
 *                      if the test node is null.
 */
    int nodeType(Node234 test) {
        if(test.firstInfo == -1)
            return 0;
        else if(test.secondInfo == -1)
            return 2;
        else if(test.thirdInfo == -1)
            return 3;
        return 4;
    }
   
/** Determines which of a given node's child pointers should be followed when
 *  searching for the correct place to add a given integer.
 *
 *  @param test         The node which must be searched.
 *  @param data         The integer to be added to the tree.
 *  @return             An integer specifying one of the first through fourth
 *                      pointers, -1 if <code>data</code> is contained in <code>test</code>,
 *                      or 0 if <code>test</code> is a leaf node.
 */
    int whichChild(Node234 test, int data) {
        if(test.firstInfo == data || test.secondInfo == data || test.thirdInfo == data)
            return -1;
        else if(test.firstChild == null)
            return 0;
        else if(data < test.firstInfo)
            return 1;
        else if(test.secondChild != null && data < test.secondInfo)
            return 2;
        else if (data < test.secondInfo)
            return 3;
        else if(data < test.thirdInfo)
            return 3;
        return 4;
    }

/** Adds a given integer to a given node.
 *
 *  @param holder       The node to which a number is to be added.
 *  @param data         The integer to be added.
 */
    void insertData(Node234 holder, int data) {
        if(data < holder.firstInfo) {
            holder.thirdInfo = holder.secondInfo;
            holder.secondInfo = holder.firstInfo;
            holder.firstInfo = data;
        }
        else if(data < holder.secondInfo) {
            holder.thirdInfo = holder.secondInfo;
            holder.secondInfo = data;
        }
        else {
            if(holder.secondInfo == -1)
                holder.secondInfo = data;
            else
                holder.thirdInfo = data;
        }
    }
   
/** Performs all the necessary permutations in order to structure the tree
 *  and find the correct node to which each new integer is to be added.
 *
 *  @param array        Array of all the integers to be added.
 *  @param index        The index in <code>array</code> pointing to the current
 *                      integer.
 *  @return             A dummy integer, always 0, providing a graceful
 *                      way to break out of infinite loops.
 */
    int add(int[] array, int index) {
        if(root.firstInfo == -1) {
            root.firstInfo = array[index];
            printTree(array, index, 0, false);
            return 0;
        }
        else if(nodeType(root) == 4) {
            printTree(array, index, root.firstInfo, true);
            Node234 leftChild = new Node234();
            Node234 rightChild = new Node234();
            leftChild.firstInfo = root.firstInfo;
            leftChild.firstChild = root.firstChild;
            leftChild.fourthChild = root.secondChild;
            rightChild.firstInfo = root.thirdInfo;
            rightChild.firstChild = root.thirdChild;
            rightChild.fourthChild = root.fourthChild;
            root.firstInfo = root.secondInfo;
            root.secondInfo = -1;
            root.thirdInfo = -1;
            root.firstChild = leftChild;
            root.fourthChild = rightChild;
            root.secondChild = null;
            root.thirdChild = null;
        }
        Node234 p = root;
        Node234 prev = new Node234();
        while(true) {
            if(nodeType(p) == 4) {
                if(nodeType(prev) == 2) {
                    if(prev.firstChild == p) {
                        printTree(array, index, p.firstInfo, true);
                        Node234 leftChild = new Node234();
                        Node234 rightChild = new Node234();
                        leftChild.firstInfo = p.firstInfo;
                        leftChild.firstChild = p.firstChild;
                        leftChild.fourthChild = p.secondChild;
                        rightChild.firstInfo = p.thirdInfo;
                        rightChild.firstChild = p.thirdChild;
                        rightChild.fourthChild = p.fourthChild;
                        prev.secondInfo = prev.firstInfo;
                        prev.firstInfo = p.secondInfo;
                        prev.firstChild = leftChild;
                        prev.thirdChild = rightChild;
                        p = prev;
                        prev = new Node234();
                    }
                    else {
                        printTree(array, index, p.firstInfo, true);
                        Node234 leftChild = new Node234();
                        Node234 rightChild = new Node234();
                        leftChild.firstInfo = p.firstInfo;
                        leftChild.firstChild = p.firstChild;
                        leftChild.fourthChild = p.secondChild;
                        rightChild.firstInfo = p.thirdInfo;
                        rightChild.firstChild = p.thirdChild;
                        rightChild.fourthChild = p.fourthChild;
                        prev.secondInfo = p.secondInfo;
                        prev.thirdChild = leftChild;
                        prev.fourthChild = rightChild;
                        p = prev;
                        prev = new Node234();
                    }
                }
                else {
                    if(prev.firstChild == p) {
                        printTree(array, index, p.firstInfo, true);
                        Node234 leftChild = new Node234();
                        Node234 rightChild = new Node234();
                        leftChild.firstInfo = p.firstInfo;
                        leftChild.firstChild = p.firstChild;
                        leftChild.fourthChild = p.secondChild;
                        rightChild.firstInfo = p.thirdInfo;
                        rightChild.firstChild = p.thirdChild;
                        rightChild.fourthChild = p.fourthChild;
                        prev.thirdInfo = prev.secondInfo;
                        prev.secondInfo = prev.firstInfo;
                        prev.firstInfo = p.secondInfo;
                        prev.firstChild = leftChild;
                        prev.secondChild = rightChild;
                        p = prev;
                        prev = new Node234();
                    }
                    else if(prev.thirdChild == p) {
                        printTree(array, index, p.firstInfo, true);
                        Node234 leftChild = new Node234();
                        Node234 rightChild = new Node234();
                        leftChild.firstInfo = p.firstInfo;
                        leftChild.firstChild = p.firstChild;
                        leftChild.fourthChild = p.secondChild;
                        rightChild.firstInfo = p.thirdInfo;
                        rightChild.firstChild = p.thirdChild;
                        rightChild.fourthChild = p.fourthChild;
                        prev.thirdInfo = prev.secondInfo;
                        prev.secondInfo = p.secondInfo;
                        prev.secondChild = leftChild;
                        prev.thirdChild = rightChild;
                        p = prev;
                        prev = new Node234();
                    }
                    else {
                        printTree(array, index, p.firstInfo, true);
                        Node234 leftChild = new Node234();
                        Node234 rightChild = new Node234();
                        leftChild.firstInfo = p.firstInfo;
                        leftChild.firstChild = p.firstChild;
                        leftChild.fourthChild = p.secondChild;
                        rightChild.firstInfo = p.thirdInfo;
                        rightChild.firstChild = p.thirdChild;
                        rightChild.fourthChild = p.fourthChild;
                        prev.thirdInfo = p.secondInfo;
                        prev.secondChild = leftChild;
                        prev.fourthChild = rightChild;
                        p = prev;
                        prev = new Node234();
                    }
                }
            }
            int nextStep = whichChild(p, array[index]);
            if(nextStep == -1) {
                printTree(array, index, p.firstInfo, false);
                printTree(array, index, 0, false);
                return 0;
            }
            else if(nextStep == 0) {
                printTree(array, index, p.firstInfo, false);
                insertData(p, array[index]);
                printTree(array, index, 0, false);
                return 0;
            }
            else if(nextStep == 1) {
                printTree(array, index, p.firstInfo, false);
                prev = p;
                p = p.firstChild;
            }
            else if(nextStep == 2) {
                printTree(array, index, p.firstInfo, false);
                prev = p;
                p = p.secondChild;
            }
            else if(nextStep == 3) {
                printTree(array, index, p.firstInfo, false);
                prev = p;
                p = p.thirdChild;
            }
            else if(nextStep == 4) {
                printTree(array, index, p.firstInfo, false);
                prev = p;
                p = p.fourthChild;
            }
        }
    }

/** Creates a snapshot of the tree and adds it to the .SHO file.
 *
 *  @param array        Array containing all the integers in the final tree.
 *  @param index        The index in <code>array</code> pointing to the current
 *                      integer.
 *  @param color        If this snapshot contains a question, an integer
 *                      contained in the tree which is to be colored red,
 *                      otherwise 0.
 *  @param answer       The answer to the true/false question generated for this
 *                      snapshot. Ignored if <code>color</code> is 0.
 */
    void printTree(int[] array, int index, int color, boolean answer) {
            if(color != 0) {
                tfQuestion q = new tfQuestion(out, (new Integer(qIndex)).toString());
                q.setQuestionText("The red node will split:");
                q.setAnswer(answer);
                questions.addQuestion(q);
                questions.insertQuestion(qIndex);
                qIndex++;
            }
            out.println("VIEW DOCS a234tree.htm");
            out.println("GeneralTree");
            out.println(" 3 1.1 1.1");
            out.print("Order of arrival:  ");
            for(int i = 0; i < array.length; i++) {
                if(i == index)
                    out.print(" **" + array[i] + "** ");
                else
                    out.print(" " + array[i]);
            }
            out.print("\n");
            out.println("***\\***");
            printData(root, 0, color);
            out.println("***^***");
    }

/** Inner function to recursively describe each node in the .SHO file.
 *
 *  @param start        The node from which to start printing.
 *  @param level        The level in the tree of <code>start</code>.
 *  @param data         Integer in the tree which is to be colored red, or
 *                      0 if this snapshot has no associated question.
 */
    void printData(Node234 start, int level, int data) {
        out.println("" + level);
        if(start.firstInfo == data)
            out.print("\\R");
        out.println("" + start.firstInfo);
        if(start.secondInfo != -1)
            out.println("" + start.secondInfo);
        else
            out.println(".");
        if(start.thirdInfo != -1)
            out.println("" + start.thirdInfo);
        else
            out.println(".");
        if(start.firstChild != null)
            printData(start.firstChild, level + 1, data);
        if(start.secondChild != null)
            printData(start.secondChild, level + 1, data);
        if(start.thirdChild != null)
            printData(start.thirdChild, level + 1, data);
        if(start.fourthChild != null)
            printData(start.fourthChild, level + 1, data);
    }
}
                   

                        
                        
            
            
    
