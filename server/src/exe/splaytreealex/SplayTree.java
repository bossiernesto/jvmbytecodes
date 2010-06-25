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

/** Creates a visual demonstration of a Splay Tree, writing output to a .SHO
 *  file. Usage:
 *  { java SplayTree fileName }
 *  where fileName is the name of the desired output file, without extension.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */

package exe.splaytreealex;

import java.io.*;
import java.util.*;
import exe.*;

/** Implementation of a single node of a Splay Tree.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class SplayNode {
    int info;
    SplayNode leftChild;
    SplayNode rightChild;
    SplayNode parent;

/** Constructor for <code>SplayNode</code> object.
 */
    SplayNode() {
        info = -1;
        leftChild = null;
        rightChild = null;
        parent = null;
    }
}

/** Implementation of Splay Tree framework.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
public class SplayTree {
    private SplayNode root;
    private PrintWriter out;
    private questionCollection questions;
    private int qIndex = 0;
    private int snapshot = 0;

/** Constructor for <code>SplayTree</code> object.
 *
 *  @param start        The root of the Splay Tree.
 *  @param gaigsFile    String containing the name of the output file.
 */
    public SplayTree(SplayNode start, String gaigsFile) {
        root = start;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(gaigsFile)));
            questions = new questionCollection(out);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Creates array of data and calls appropriate procedures to manipulate
 *  Splay Tree.
 *
 *  @param args         Array of command line arguments.
 */
    public static void main(String[] args) {
        String gaigsFile = args[0];
        SplayNode start = new SplayNode();
        SplayTree tree = new SplayTree(start, gaigsFile);
        Random r = new Random();
        int arraySize = r.nextInt(3) + 18;
        int[] array = new int[arraySize];
        for(int i = 0; i < arraySize; i++)
            array[i] = r.nextInt(98) + 1;
        for(int j = 0; j < arraySize; j++)
            tree.add(array, j);
        tree.questions.writeQuestionsAtEOSF();
        tree.out.close();
    }

/** Adds an integer to a Splay Tree as if it were a Binary Search Tree,
 *  which must be accomplished before any splay operations are performed.
 *
 *  @param start        The node from which to start searching for the 
 *                      appropriate place to add a given integer.
 *  @param data         The integer to be added.
 *  @return             The new node created to hold <code>data</code>, on which
 *                      splay operations may now be performed. Will return
 *                      <code>null</code> if <code>data</code> is already present.
 */
    SplayNode binaryAdd(SplayNode start, int data) {
        if(start.info == data)
            return null;
        else if(start.info > data)
            if(start.leftChild == null) {
                SplayNode insNode = new SplayNode();
                insNode.info = data;
                insNode.parent = start;
                start.leftChild = insNode;
                return insNode;
            }
            else
                return binaryAdd(start.leftChild, data);
        else
            if(start.rightChild == null) {
                SplayNode insNode = new SplayNode();
                insNode.info = data;
                insNode.parent = start;
                start.rightChild = insNode;
                return insNode;
            }
            else
                return binaryAdd(start.rightChild, data);
    }
        
/** Performs a left splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 */
    void performL(SplayNode child, SplayNode p) {
        p.leftChild = child.rightChild;
        if(p.leftChild != null)
            p.leftChild.parent = p;
        child.rightChild = p;
        child.parent = null;
        p.parent = child;
        root = child;
        child.parent = null;
    }

/** Performs a right splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 */
    void performR(SplayNode child, SplayNode p) {
        p.rightChild = child.leftChild;
        if(p.rightChild != null)
            p.rightChild.parent = p;
        child.leftChild = p;
        child.parent = null;
        p.parent = child;
        root = child;
        child.parent = null;
    }
        
/** Performs a left-left splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 *  @param gp           The parent of <code>p</code>.
 */
    void performLL(SplayNode child, SplayNode p, SplayNode gp) {
        if(gp.parent == null) {
            root = child;
            child.parent = null;
        }
        else if(gp.parent.leftChild == gp) {
            gp.parent.leftChild = child;
            child.parent = gp.parent;
        }
        else {
            gp.parent.rightChild = child;
            child.parent = gp.parent;
        }
        gp.leftChild = p.rightChild;
        if(gp.leftChild != null)
            gp.leftChild.parent = gp;
        p.rightChild = gp;
        gp.parent = p;
        p.leftChild = child.rightChild;
        if(p.leftChild != null)
            p.leftChild.parent = p;
        child.rightChild = p;
        p.parent = child;
    }
    
/** Performs a right-right splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 *  @param gp           The parent of <code>p</code>.
 */
    void performRR(SplayNode child, SplayNode p, SplayNode gp) {
        if(gp.parent == null) {
            root = child;
            child.parent = null;
        }
        else if(gp.parent.leftChild == gp) {
            gp.parent.leftChild = child;
            child.parent = gp.parent;
        }
        else {
            gp.parent.rightChild = child;
            child.parent = gp.parent;
        }
        gp.rightChild = p.leftChild;
        if(gp.rightChild != null)
            gp.rightChild.parent = gp;
        p.leftChild = gp;
        gp.parent = p;
        p.rightChild = child.leftChild;
        if(p.rightChild != null)
            p.rightChild.parent = p;
        child.leftChild = p;
        p.parent = child;
    }

/** Performs a left-right splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 *  @param gp           The parent of <code>p</code>.
 */
    void performLR(SplayNode child, SplayNode p, SplayNode gp) {
        if(gp.parent == null) {
            root = child;
            child.parent = null;
        }
        else if(gp.parent.leftChild == gp) {
            gp.parent.leftChild = child;
            child.parent = gp.parent;
        }
        else {
            gp.parent.rightChild = child;
            child.parent = gp.parent;
        }
        gp.leftChild = child.rightChild;
        if(gp.leftChild != null)
            gp.leftChild.parent = gp;
        child.rightChild = gp;
        gp.parent = child;
        p.rightChild = child.leftChild;
        if(p.rightChild != null)
            p.rightChild.parent = p;
        child.leftChild = p;
        p.parent = child;
    }

/** Performs a right-left splay operation on a given node.
 *
 *  @param child        The node on which to perform the operation.
 *  @param p            The parent of <code>child</code>.
 *  @param gp           The parent of <code>p</code>.
 */
    void performRL(SplayNode child, SplayNode p, SplayNode gp) {
        if(gp.parent == null) {
            root = child;
            child.parent = null;
        }
        else if(gp.parent.leftChild == gp) {
            gp.parent.leftChild = child;
            child.parent = gp.parent;
        }
        else {
            gp.parent.rightChild = child;
            child.parent = gp.parent;
        }
        gp.rightChild = child.leftChild;
        if(gp.rightChild != null)
            gp.rightChild.parent = gp;
        child.leftChild = gp;
        gp.parent = child;
        p.leftChild = child.rightChild;
        if(p.leftChild != null)
            p.leftChild.parent = p;
        child.rightChild = p;
        p.parent = child;
    }

/** Inserts a given integer into the Splay Tree and performs the correct
 *  splay operations to bring the inserted node to the root of the tree.
 *
 *  @param array        Array of all the integers to be added to the tree.
 *  @param index        Index in <code>array</code> of integer currently being
 *                      added.
 */
    void add(int[] array, int index) {
        if(root.info == -1) {
            root.info = array[index];
            printTree(array, index, 0, -1);
        }
        else {
            SplayNode child = binaryAdd(root, array[index]);
            SplayNode p = new SplayNode();
            SplayNode gp = new SplayNode();
            if(child == null)
                printTree(array, index, 0, 0);
            else {
                while(child != root) {
                    p = child.parent;
                    gp = p.parent;
                    if(gp == null) {
                        if(child == p.leftChild) {
                            printTree(array, index, child.info, 1);
                            performL(child, p);
                        }
                        else {
                            printTree(array, index, child.info, 2);
                            performR(child, p);
                        }
                    }
                    else if(p == gp.leftChild) {
                        if(child == p.leftChild) {
                            printTree(array, index, child.info, 3);
                            performLL(child, p, gp);
                        }
                        else {
                            printTree(array, index, child.info, 5);
                            performLR(child, p, gp);
                        }
                    }
                    else {
                        if(child == p.leftChild) {
                            printTree(array, index, child.info, 6);
                            performRL(child, p, gp);
                        }
                        else {
                            printTree(array, index, child.info, 4);
                            performRR(child, p, gp);
                        }
                    }
                }
                printTree(array, index, 0, 0);
            }
        }
    }

/** Creates a snapshot of the Splay Tree, writing data to the .SHO file.
 *
 *  @param array        Array of all integers to be added to the tree.
 *  @param index        Index in <code>array</code> of integer currently being
 *                      added.
 *  @param color        Integer of node to be colored yellow in snapshots
 *                      with associated multiple choice questions; if snap-
 *                      shot has no question, 0 should be used.
 *  @param answer       Integer (1-6) of correct multiple choice answer.
 *                      Ignored if snapshot has no question.
 */
    void printTree(int[] array, int index, int color, int answer) {
        out.println("VIEW DOCS splaytr.htm");
        if(color != 0) {
            mcQuestion q = new mcQuestion(out, (new Integer(qIndex)).toString());
            q.setQuestionText("What kind of rotation will occur at the yellow node?");
            q.addChoice("L");
            q.addChoice("R");
            q.addChoice("LL");
            q.addChoice("RR");
            q.addChoice("LR");
            q.addChoice("RL");
            q.setAnswer(answer);
            questions.addQuestion(q);
            questions.insertQuestion(qIndex);
            qIndex++;
        }
        out.println("BinaryTree");
        out.println("1 1.1 1.1");
        out.println("Splay tree");
        out.println("Snapshot #" + snapshot);
        snapshot++;
        out.print("Order of arrival: ");
        for(int i = 0; i < array.length; i++) {
            if(i == index)
                out.print(" **" + array[i] + "** ");
            else
                out.print(" " + array[i]);
        }
        out.print("\n");
        out.println("***\\***");
        printData(root, 0, 'R', color);
        out.println("***^***");
    }

/** Inner function to recursively write each node to .SHO file.
 *
 *  @param start        The node from which to start printing.
 *  @param level        The level in the tree of <code>start</code>.
 *  @param direction    Character specifying whether <code>start</code> is the
 *                      root or a right node ('R') or a left node ('L').
 *  @param color        Integer to be colored yellow if found in the tree.
 */
    void printData(SplayNode start, int level, char direction, int color) {
        out.println("" + level);
        out.println("" + direction);
        if(color == start.info)
            out.print("\\Y");
        out.println("" + start.info);
        if(start.leftChild != null)
            printData(start.leftChild, level + 1, 'L', color);
        if(start.rightChild != null)
            printData(start.rightChild, level + 1, 'R', color);
    }
}
                    
                        
                        
                    
            


    
