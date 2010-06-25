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

/** BSTree.java
 *  USAGE: java BSTree targetFile
 *  Produces SHOW file for animation of binary search
 *  tree with ordering property.   
 *  <p>
 *  Alex Zimmerman and Tom Naps 28 June 2001.
 */
package exe.bstreealex;

import java.util.*;
import java.io.*;
import java.lang.String.*;

/** Implementation of a single node in
 *  a binary search tree.
 *
 *  @author  Tom Naps
 *  @author  Alex Zimmerman
 *  @version 2.0
 */
class Node {

    int data;
    Node leftChild;
    Node rightChild;
    
/** Constructor for class <code>Node</code>.
 * 
 *  @param i        Integer held in Node's data field.
 *                  -1 means a null node.
 */
    Node(int i) {
        data = i;
        leftChild = null;
        rightChild = null;
    }

}

/** Implementation of binary search tree,
 *  with methods to produce SHOW file.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */
public class BSTree {

    Node root;

/** Generates random array of data and calls
 *  appropriate methods to write SHOW file.
 *
 *  @param args     Command line arguments:
 *                  First argument must be name of SHOW
 *                  file to generate, with or without
 *                  <b>.SHO</b> extension.
 *  @throws IOException     Error creating or
 *                          writing to file.
 */
    public static void main(String[] args) {
       try {
           FileOutputStream fs = new FileOutputStream(args[0]);
           System.setOut(new PrintStream(fs));
           Random r = new Random();
           int arraySize = r.nextInt(4) + 8;
           int[] array = new int[arraySize];
           for(int i = 0; i < arraySize; i++)
               array[i] = r.nextInt(99) + 1;
           BSTree tree = new BSTree();
           tree.root = new Node(-1);
           tree.addNode(tree.root, array, 0);
           for(int j = 1; j < arraySize; j++)
               tree.addNode(tree.root, array, j);
           fs.close();
       } catch(IOException ioe) {
           ioe.printStackTrace();
       }
    }
       

/** Inserts node into appropriate place in tree,
 *  calling <code>printTree()</code> to output to 
 *  SHOW file.
 *
 *  @param start    Node from which to begin searching
 *                  for spot to add target number.
 *  @param array    Array of integers comprising tree.
 *  @param index    Index in array pointing to integer
 *                  to be added to tree.
 */
    void addNode(Node start, int[] array, int index) {
        if(start.data == -1) {
            start.data = array[index];
            printTree(array, index, array[index], "Red Node Added", "\\R");
        }
        else if(start.data == array[index])
            printTree(array, index, array[index], "Key Already in Tree", "\\G");
        else if(start.data > array[index]) {
            String cap = "Comparing " + array[index] + " to Blue Node";
            printTree(array, index, start.data, cap, "\\B");
            if(start.leftChild == null)
                start.leftChild = new Node(-1);
            addNode(start.leftChild, array, index);
        }
        else {
            String cap = "Comparing " + array[index] + " to Blue Node";
            printTree(array, index, start.data, cap, "\\B");
            if(start.rightChild == null)
                start.rightChild = new Node(-1);
            addNode(start.rightChild, array, index);
        }
    }
   
/** Sends output to SHOW file to generate animation of
 *  binary search tree.
 *
 *  @param array    Array of integers comprising tree.
 *  @param index    Index in array pointing to integer
 *                  currently being added to tree.
 *  @param key      Integer specifying data field of node
 *                  to be colored in snapshot of tree.
 *  @param caption  String to appear as title of snapshot.
 *  @param color    String of format
 *                  <code>/X</code>
 *                  where X specifies the color to paint
 *                  node identified by <code>key</code>.
 */
    void printTree(int[] array, int index, int key, String caption, String color) {
        System.out.println("VIEW DOCS bstree.htm");
        System.out.println("BinaryTree");
        System.out.println("" + 1);
        String order = "Order of arrival:  ";
        for(int i = 0; i < array.length; i++) {
            if(i == index)
                order = order + "  **" + array[i] + "**";
            else
                order = order + "  " + array[i];
        }
        System.out.println(order);
        System.out.println(caption);
        System.out.println("***\\***");
        printBody(root, 0, 'R', key, color);
        System.out.println("***^***");
    }

/** Called by <code>printTree</code> to send output of
 *  each node to SHOW file as tree is traversed.  Marked
 *  private as it should not be called except from within
 *  <code>printTree()</code>.
 *
 *  @param start    Node from which to begin traversing tree.
 *  @param level    Integer specifying the level <code>start</code>
 *                  occupies in the tree.
 *  @param childType    'L' if <code>start</code> is a left child,
 *                      'R' if a right child or root node.
 *  @param key      Same as in <code>printTree()</code>.
 *  @param color    Same as in <code>printTree()</code>.
 */
    private void printBody(Node start, int level, char childType, int key, String color) {
        System.out.println("" + level);
        System.out.println("" + childType);
        if(start.data == key)
            System.out.print(color);
        System.out.println("" + start.data);
        if(start.leftChild != null)
            printBody(start.leftChild, level + 1, 'L', key, color);
        if(start.rightChild != null)
            printBody(start.rightChild, level + 1, 'R', key, color);
    }

}


