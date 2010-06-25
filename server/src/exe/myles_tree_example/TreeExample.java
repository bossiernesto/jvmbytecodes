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

// file: TreeTest.java
// author: Andrew Jungwirth
// date: 19 July 2005
//
// Modified by Myles McNally to use the GAIGS XML support classes
// File name changed to TreeExample.java
// date: 28 May 2006
// 
// This class is intended to demonstrate the tree-building capabilities of the
// Tree.java class. The main method accepts a series of command-line arguments
// that specify the operations to be performed on the tree. The first argument
// is the name of the file to which the resulting animation's showfile is to be
// written. The second argument must be a b if the tree is to be a binary tree
// or a g if the tree is to be a general tree. The third argument must be the
// name of the root node of the tree. After these arguments the tree can be 
// modified using a series of the following commands (each change to the tree
// constitutes a separate snapshot in the animation):
//  Add Node Commands:
//   Binary Tree:
//    addlx,y -> Adds a left child named x to parent node y. If the specified 
//               parent node is not found, the tree is unchanged. If the parent
//               node already has a left child, that child will be overwritten.
//    addrx,y -> Adds a right child named x to parent node y. If the specified
//               parent node is not found, the tree is unchanged. If the parent
//               node already has a right child, that child will be 
//               overwritten.
//   General Tree:
//    addx,y ->  Inserts a child named x as the rightmost child of parent node
//               y. If the specified parent node is not found, the tree is
//               unchanged.
//  Remove Node Command:
//    remx ->    Removes the node named x from the tree. Any children under x
//               will also be removed. If the specified node is not found, the
//               tree is unchanged.
//  Color Node Command:
//    colx,y ->  Colors the node named x the color specified by y. The value
//               of y must be a six-digit hex color string representing the 
//               desired color of the node. Note that the line to the node's
//               parent will also be colored in this example program, even
//               though the Tree.java class allows edges and nodes to be 
//               colored separately.
//
// Sample binary tree user input:
//  b A addlB,A addrC,A
// Produces (hopefully your text editor uses a monospaced font):
//     A
//    / \
//   B   C
//
// Sample general tree user input:
//  g A addB,A addC,A addD,A
// Produces:
//     A
//    /|\
//   B C D
//
// Note that when this program searches for a node to remove it, change its 
// color, or add a child it does a preorder traversal to find the node.
// Therefore, if there are multiple nodes with the same name in the tree, the
// one that will be modified will be the one that is found first in a preorder
// traversal.

package exe.myles_tree_example;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class TreeExample {

    static final String title = "Tree Test";

    public static void main(String[] args) throws IOException {

        ShowFile show = new ShowFile(args[0]);

        GAIGStree tree;
        TreeNode parent, current, remove;
        String key, color;
        boolean binary;

        if(Character.toLowerCase(args[1].charAt(0)) == 'b'){
            tree = new GAIGStree(true);
            binary = true;
        } else {
            tree = new GAIGStree(false);
            binary = false;
        }

        current = new TreeNode(args[2]);
        tree.setRoot(current);
	tree.setName("");
	tree.setBounds(0.0, 0.0, 1.0, 0.95);
    
        show.writeSnap(title, tree);

    for(int i = 3; i < args.length; i++){
        key = args[i].substring(0, 3);

        if(key.equalsIgnoreCase("add")){
        if(binary){
            if(Character.toLowerCase(args[i].charAt(3)) == 'l'){
            parent = findNode(args[i].substring
                      (args[i].indexOf(',') + 1,
                       args[i].length()),
                      tree.getRoot());
            if(parent != null){
                current = new TreeNode(args[i].substring
                           (4, args[i].indexOf(',')));
                parent.insertLeftChild(current);
            }
            }else{
            parent = findNode(args[i].substring
                      (args[i].indexOf(',') + 1,
                       args[i].length()),
                      tree.getRoot());
            if(parent != null){
                current = new TreeNode(args[i].substring
                           (4, args[i].indexOf(',')));
                parent.insertRightChild(current);
            }
            }
        }else{
            parent = findNode(args[i].substring
                      (args[i].indexOf(',') + 1,
                       args[i].length()),
                      tree.getRoot());
            if(parent != null){
            current = new TreeNode(args[i].substring 
                           (3, args[i].indexOf(','))); // there was an error here, was 3, should be 4 - MFM
	    // Actually, the value of 3 is correct because the add instruction
	    // for general trees is just addA,B, whereas the add instruction
	    // for binary trees has the extra r or l and is addlA,B. Using a 
	    // value of 4 here parses off the first character of the node name,
	    // which causes the program to crash; I changed it back to 3. - AMJ
            parent.setChildWithEdge(current);
            }
        }
        }else if(key.equalsIgnoreCase("rem")){
        remove = findNode(args[i].substring(3, args[i].length()),
                  tree.getRoot());
        if(remove != null){
            if((remove.getValue()).equals(tree.getRoot().getValue())){
            tree.setRoot(null);
            }else if(binary){
            parent = remove.getParent();
            current = parent.getChild();

            if(!current.isPlaceHolder() &&
               (current.getValue()).equals(remove.getValue())){
                if(current.getSibling() != null){
                current.setValue("");
                current.setLeftChild(false);
                current.setPlaceHolder(true);
                current.setChild(null);
                }else{
                parent.setChild(null);
                }
            }else{
                current.setSibling(null);
            }
            }else{
            parent = remove.getParent();
            current = parent.getChild();

            if((current.getValue()).equals(remove.getValue())){
                parent.setChild(current.getSibling());
            }else{
                if((current.getSibling().getValue()).equals
                   (remove.getValue())){
                current.setSibling
                    ((current.getSibling()).getSibling());
                }else{
                while(true){
                    current = current.getSibling();
                    if((current.getSibling().getValue()).equals
                       (remove.getValue())){
                    current.setSibling
                     ((current.getSibling()).getSibling());
                    break;
                    }
                }
                }
            }
            }
        }
        }else if(key.equalsIgnoreCase("col")){
        current = findNode(args[i].substring(3, args[i].indexOf(',')),
                   tree.getRoot());
        if(current != null){
            color = "#" + args[i].substring(args[i].indexOf(',') + 1,
                            args[i].length());
            current.setHexColor(color);
            if(current.getParent() != null){
            current.getLineToParent().setHexColor(color);
            }
        }
        }

        show.writeSnap(title + ": " + args[i], tree);
    }

   //-------- Close the show file ---------------------------------------------------------------

         show.close();   
    }
    

    private static TreeNode findNode(String target, TreeNode current){
        if (target.equals(current.getValue()))
            return current;
        else if (current.getParent() == null) {
            if (current.getChild() != null)
                return findNode(target, current.getChild());
            else
                return null;
        } else {
            if (current.getChild() != null) {
                TreeNode ret = findNode(target, current.getChild());
                if (ret != null)
                    return ret;
            }
            if (current.getSibling() != null)
                return findNode(target, current.getSibling());
            else
                return null;
        }
    }
}
