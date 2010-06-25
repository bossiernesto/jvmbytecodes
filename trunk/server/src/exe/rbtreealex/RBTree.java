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

/** Creates visual demonstrations of Red-Black Trees. Usage:
 *  { java RBTree fileName }
 *  where fileName is the name of the target .SHO file, without extension.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */

package exe.rbtreealex;

import java.io.*;
import java.util.*;
import exe.*;

/** Implementation of a single node in a Red-Black Tree.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class RBNode {
    RBNode leftChild;
    RBNode rightChild;
    int info;
    boolean leftBlack;
    boolean rightBlack;

/** Constructor for <code>RBNode</code> object.
 */
    RBNode() {
        leftChild = null;
        rightChild = null;
        info = -1;
        leftBlack = true;
        rightBlack = true;
    }
}

/** Implementation of the Red-Black Tree framework.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
public class RBTree {
    private RBNode root;
    private PrintWriter out;
    private questionCollection questions;
    private int qIndex = 0;
    
/** Constructor for <code>RBTree</code> object.
 *
 *  @param start        The root node of the Red-Black Tree.
 *  @param gaigsFile    String containing name of output file.
 *  @throws IOException Error opening output stream to gaigsFile.
 */
    public RBTree(RBNode start, String gaigsFile) {
        try {
            root = start;
            out = new PrintWriter(new BufferedWriter(new FileWriter(gaigsFile)));
            questions = new questionCollection(out);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Creates random array of data and calls appropriate procedures to add
 *  data to Red-Black Tree.
 *
 *  @param args         Array of command line arguments.
 */
    public static void main(String[] args) {
            int dummyVariable;
            String gaigsFile = args[0];
            Random r = new Random();
            //int arraySize = 19;
            //int[] array = { 15,36,72,62,61,92,51,39,81,92,80,52,33,98,51,69,57,66,40 };
            int arraySize = r.nextInt(3) + 18;
            int[] array = new int[arraySize];
            for(int i = 0; i < arraySize; i++)
	       array[i] = r.nextInt(98) + 1;
            RBNode start = new RBNode();
            RBTree tree = new RBTree(start, gaigsFile);
            for(int j = 0; j < arraySize; j++)
                dummyVariable = tree.add(array, j);
            tree.questions.writeQuestionsAtEOSF();
            tree.out.close();
    }

/** Determines if the given node forms part of a 2-Node, a 3-Node, or a
 *  4-Node, or if that node is null.
 *
 *  @param test         The node to test, which must be the top node if
 *                      part of a 3-Node or 4-Node.
 *  @return             An integer clarifying the type of node, or 0 if
 *                      <code>test</code> is null.
 */
    int nodeType(RBNode test) {
        if(test.info == -1)
            return 0;
        else if((!test.leftBlack) && (!test.rightBlack))
            return 4;
        else if(!(test.leftBlack && test.rightBlack))
            return 3;
        return 2;
    }    
   
/** Determines the appropriate child node to visit next given the integer
 *  to be added to the tree.
 *
 *  @param test         The node from which to begin.
 *  @param data         The integer currently being added.
 *  @return             An integer (1-4) with the appropriate path number,
 *                      -1 if <code>test</code> contains <code>data</code>, or
 *                      0 if it is a leaf node.
 */
    int whichChild(RBNode test, int data) {
        if((!test.leftBlack && test.leftChild.info == data) ||
           (!test.rightBlack && test.rightChild.info == data) ||
           test.info == data)
            return -1;
        else if((test.leftChild == null || 
               (!test.leftBlack && test.leftChild.leftChild == null &&
               test.leftChild.rightChild == null)) &&
               (test.rightChild == null ||
               (!test.rightBlack && test.rightChild.leftChild == null &&
               test.rightChild.rightChild == null)))
            return 0;
        else if((!test.leftBlack && (data < test.leftChild.info)) ||
               (test.leftBlack && (data < test.info)))
            return 1;
        else if((!test.leftBlack && data < test.info) ||
               (!test.rightBlack && data < test.rightChild.info))
            return 2;
        else if((!test.leftBlack && !test.rightBlack) &&
               (data < test.rightChild.info))
            return 3;
        return 4;
    }

/** Adds a given integer to a given node.
 *
 *  @param holder       Node to which integer is to be added; must be
 *                      the top node if part of a 3-Node or 4-Node.
 *  @param data         Integer to be added.
 */
    void insertData(RBNode holder, int data) {
        if(holder.leftBlack && holder.rightBlack) {
            if(data < holder.info) {
                RBNode insNode = new RBNode();
                insNode.info = data;
                holder.leftChild = insNode;
                holder.leftBlack = false;
            }
            else {
                RBNode insNode = new RBNode();
                insNode.info = data;
                holder.rightChild = insNode;
                holder.rightBlack = false;
            }
        }
        else if(!holder.leftBlack) {
            if(data < holder.leftChild.info) {
                RBNode insNode = new RBNode();
                insNode.info = holder.info;
                holder.info = holder.leftChild.info;
                holder.rightChild = insNode;
                holder.rightBlack = false;
                holder.leftChild.info = data;
            }
            else if(data < holder.info) {
                RBNode insNode = new RBNode();
                insNode.info = holder.info;
                holder.info = data;
                holder.rightChild = insNode;
                holder.rightBlack = false;
            }
            else {
                RBNode insNode = new RBNode();
                insNode.info = data;
                holder.rightChild = insNode;
                holder.rightBlack = false;
            }
        }
        else {
            if(data < holder.info) {
                RBNode insNode = new RBNode();
                insNode.info = data;
                holder.leftChild = insNode;
                holder.leftBlack = false;
            }
            else if(data < holder.rightChild.info) {
                RBNode insNode = new RBNode();
                insNode.info = holder.info;
                holder.info = data;
                holder.leftChild = insNode;
                holder.leftBlack = false;
            }
            else {
                RBNode insNode = new RBNode();
                insNode.info = holder.info;
                holder.info = holder.rightChild.info;
                holder.leftChild = insNode;
                holder.leftBlack = false;
                holder.rightChild.info = data;
            }
        }
    }
                             
/** Finds appropriate spot in the Red-Black Tree to add a given integer.
 *
 *  @param array        Array of all the integers to be added.
 *  @param index        Index within <code>array</code> which is currently being
 *                      added.
 *  @return             Dummy integer, always 0, providing a graceful way to
 *                      break out of infinite loops.
 */
    int add(int[] array, int index) {
        if(root.info == -1) {
            root.info = array[index];
            printTree(array, index, 0, false);
            return 0;
        }
        if(nodeType(root) == 4) {
            printTree(array, index, root.info, true);
            root.leftBlack = true;
            root.rightBlack =  true;
            printTree(array, index, 0, false);
        }
        RBNode p = root;
        RBNode prev = new RBNode();
        RBNode holder = new RBNode();
        while(true) {
            if(nodeType(p) == 4) {
                if(nodeType(prev) == 2) {
                    printTree(array, index, p.info, true);
                    if(prev.leftChild == p) {
                        prev.leftBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                    }
                    else {
                        prev.rightBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                    }
                    printTree(array, index, 0, false);
                }
                else {
                    printTree(array, index, p.info, true);
                    if(prev.leftChild.leftChild == p) {
                        holder.info = prev.info;
                        holder.leftChild = prev.leftChild.rightChild;
                        holder.rightChild = prev.rightChild;
                        prev.info = prev.leftChild.info;
                        prev.leftChild = p;
                        prev.rightChild = holder;
                        prev.leftBlack = false;
                        prev.rightBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                      }
                    else if(prev.leftChild == p) {
                        prev.leftBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                    }
                    else if(prev.leftChild.rightChild == p) {
                        holder.info = prev.info;
                        holder.leftChild = p.rightChild;
                        holder.rightChild = prev.rightChild;
                        prev.info = p.info;
                        prev.rightChild = holder;
                        prev.leftChild.rightChild = p.leftChild;
                        prev.leftBlack = false;
                        prev.rightBlack = false;
                        p = prev;
                    }
                    else if(prev.rightChild.leftChild == p) {
                        holder.info = prev.info;
                        holder.leftChild = prev.leftChild;
                        holder.rightChild = p.leftChild;
                        prev.info = p.info;
                        prev.leftChild = holder;
                        prev.rightChild.leftChild = p.rightChild;
                        prev.leftBlack = false;
                        prev.rightBlack = false;
                        p = prev;
                    }
                    else if(prev.rightChild == p) {
                        prev.rightBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                    }
                    else {
                        holder.info = prev.info;
                        holder.rightChild = prev.rightChild.leftChild;
                        holder.leftChild = prev.leftChild;
                        prev.info = prev.rightChild.info;
                        prev.leftChild = holder;
                        prev.rightChild = p;
                        prev.leftBlack = false;
                        p.leftBlack = true;
                        p.rightBlack = true;
                        p = prev;
                    }
                    printTree(array, index, 0, false);
                }
            }
            int nextStep = whichChild(p, array[index]);
            if(nextStep == -1) {
                printTree(array, index, 0, false);
                return 0;
            }
            else if(nextStep == 0) {
                insertData(p, array[index]);
                printTree(array, index, 0, false);
                return 0;
            }
            else if(nextStep == 1) {
                prev = p;
                printTree(array, index, p.info, false);
                if(p.leftBlack) {
                    p = p.leftChild;
	        }
                else {
                    p = p.leftChild.leftChild;
		}
                printTree(array, index, 0, false);
            }
            else if(nextStep == 2) {
            prev = p;
            printTree(array, index, p.info, false);
            if(!p.leftBlack) {
                p = p.leftChild.rightChild;
            }
            else if(!p.rightBlack) {
                p = p.rightChild.leftChild;
	    }
            printTree(array, index, 0, false);
            }
            else if(nextStep == 3) {
                printTree(array, index, p.info, false);
            prev = p;
            p = p.rightChild.leftChild;
            printTree(array, index, 0, false);
            }
            else {
                printTree(array, index, p.info, false);
            prev = p;
            if(p.rightBlack) {
                p = p.rightChild;
	    }
            else {
                p = p.rightChild.rightChild;
	    }
            printTree(array, index, 0, false);
            }
        }
    }

/** Creates a snapshot of the Red-Black Tree, writing data to a .SHO file.
 *
 *  @param array        Array of all integers to be held by final tree.
 *  @param index        Index within <code>array</code> currently being added.
 *  @param color        If snapshot contains a question, the integer in the
 *                      tree to be colored yellow, otherwise 0.
 *  @param answer       The answer to the true/false question for this snap-
 *                      shot, ignored if snapshot has no question.
 */
    void printTree(int[] array, int index, int color, boolean answer) {
            if(color != 0) {
                tfQuestion q = new tfQuestion(out, (new Integer(qIndex)).toString());
                String qText = new String("Inserting " + array[index] +" -- after visiting ");
                qText += color + " its children will change in color or value:";
                q.setQuestionText(qText);
                q.setAnswer(answer);
                questions.addQuestion(q);
                questions.insertQuestion(qIndex);
                qIndex++;
            }
            out.println("VIEW DOCS rbtree-cs34.htm");
            out.println("BinaryTree");
            out.println("1 1.1 1.1");
            out.println("Red Black Tree");
            out.print("Order of arrival: ");
            for(int i = 0; i < array.length; i++) {
                if(i == index)
                    out.print(" **" + array[i] + "** ");
                else
                    out.print(" " + array[i]);
            }
            out.print("\n");
            out.println("***\\***");
            printData(root, root, 0, color);
            out.println("***^***");
    }

/** Inner function to recursively write each node to .SHO file in sequence.
 *
 *  @param start        The node from which to begin writing data.
 *  @param prev         Parent node of <code>start</code>, needed to determine
 *                      if it is a red or black node.
 *  @param level        The level <code>start</code> occupies in the tree.
 *  @param color        Integer in the tree to be colored yellow, for
 *                      question snapshots. Should be 0 otherwise.
 */
    void printData(RBNode start, RBNode prev, int level, int color) {
        out.println("" + level);
        if(start == root) {
            out.println("R");
            if(start.info == color)
                out.print("\\Y");
            else
                out.print("\\X");
            out.println("" + start.info);
        }
        else {
            if(start == prev.leftChild) {
                out.println("L");
                if(start.info == color)
                    out.print("\\Y");
                else if(prev.leftBlack)
                    out.print("\\X");
                else
                    out.print("\\R");
            }
            else {
                out.println("R");
                if(start.info == color)
                    out.print("\\Y");
                else if(prev.rightBlack)
                    out.print("\\X");
                else
                    out.print("\\R");
            }
            out.println("" + start.info);
        }
        if(start.leftChild != null)
            printData(start.leftChild, start, level + 1, color);
        if(start.rightChild != null)
            printData(start.rightChild, start, level + 1, color);
    }
} 
