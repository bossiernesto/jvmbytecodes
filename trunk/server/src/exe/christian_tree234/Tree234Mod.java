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

package exe.christian_tree234;

import java.io.*;
import java.util.ArrayList;

import exe.*;
/**
 * Tree234Mod: Visualization of the insertion of integer values in 2-3-4 Trees
 * Original code written by Mitchell Waite and  Robert Lafor and available in their book
 * "Data Structures and Algorithms in Java"
 * Jhave annotations by Sudhakar Pandurangan
 */
public class Tree234Mod
{

    Tree234Mod()
    {
        root = new Node();
    }

    public int find(long l)
    {
        Node node = root;
        do
        {
            int i;
            if((i = node.findItem(l)) != -1)
                return i;
            if(node.isLeaf())
                return -1;
            node = getNextChild(node, l);
        } while(true);
    }

    public void insert(long l)
    {
        Node node = root;
        DataItem dataitem = new DataItem(l);
        do
        {
            for(; node.isFull(); node = getNextChild(node, l))
            {
                split(node);
                node = node.getParent();
            }

            if(!node.isLeaf())
            {
                node = getNextChild(node, l);
            } else
            {
                node.insertItem(dataitem);
                return;
            }
        } while(true);
    }

    public void split(Node node)
    {
        DataItem dataitem1 = node.removeItem();
        DataItem dataitem = node.removeItem();
        Node node2 = node.disconnectChild(2);
        Node node3 = node.disconnectChild(3);
        Node node4 = new Node();
        Node node1;
        if(node == root)
        {
            root = new Node();
            node1 = root;
            root.connectChild(0, node);
        } else
        {
            node1 = node.getParent();
        }
        int i = node1.insertItem(dataitem);
        int j = node1.getNumItems();
        for(int k = j - 1; k > i; k--)
        {
            Node node5 = node1.disconnectChild(k);
            node1.connectChild(k + 1, node5);
        }

        node1.connectChild(i + 1, node4);
        node4.insertItem(dataitem1);
        node4.connectChild(0, node2);
        node4.connectChild(1, node3);
    }

    public Node getNextChild(Node node, long l)
    {
        int j = node.getNumItems();
        int i;
        for(i = 0; i < j; i++)
            if(l < node.getItem(i).dData)
                return node.getChild(i);

        return node.getChild(i);
    }

   void displayTree()
    {
	recDisplayTree(root, 0, 0);
   		
      	try {
	    show.writeSnap(TITLE,docURL,pseudoURL,vizTree);
    	}
	catch (IOException ioe) {
    		System.out.println(ioe.toString());
    	}

    }

    private void recDisplayTree(Node node, int i, int j)
    {
        String strTreeLabel = null;
    	// System.out.print("level=" + i + " child=" + j + " ");
    	strTreeLabel = node.displayNode();
    	TreeNode gigsTreeNode  = new TreeNode(strTreeLabel); 
    	if (i == 0 ) { //Checking for root node
    		gigsRootTreeNode = gigsTreeNode;
    		vizTree.setRoot(gigsRootTreeNode);
    		aTreeNodeList.add(0,gigsTreeNode);
    	}
	else {
    		gigsParentTreeNode = (TreeNode) aTreeNodeList.get(i-1);
    		gigsParentTreeNode.setChildWithEdge(gigsTreeNode);
    		aTreeNodeList.add(i-1,gigsParentTreeNode);
    		aTreeNodeList.add(i,gigsTreeNode);
    	}
    	
    	int k = node.getNumItems();
        for(int l = 0; l < k + 1; l++)
        {
            Node node1 = node.getChild(l);
            if(node1 != null)
                recDisplayTree(node1, i + 1, l);
            else
                return;
        }

    }

    private Node root;
    //Code from Tre234App has been moved here to use the GAIGS variables.
    static ShowFile show;
    static GAIGSlabel message;
    static GAIGStree vizTree;   // The tree to visualize
    static final String TITLE = "Tree234";   // no title


    // Needs to be changed later if needed

    static final String docURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/234treeDescription.html";
    static final String pseudoURL = "http://www.cis.gvsu.edu/~trefftzc/jhave/234tree.php";

    ArrayList aTreeNodeList = new ArrayList();
    TreeNode gigsParentTreeNode = null;
    
    TreeNode gigsRootTreeNode = null;

    
    public static void main(String args[]) throws IOException

    {
        Tree234Mod tree234 = new Tree234Mod();

        //show = new ShowFile("234vis.sho");

        // get the file name as input parameter.

        show = new ShowFile(args[0]);


		vizTree = new GAIGStree(false,"234 Tree","#FFFFFF",0.0,0.0,1.0,0.6,0.1);


/*
        tree234.insert(50L);
        tree234.insert(40L);
        tree234.insert(60L);
        tree234.insert(30L);
        tree234.insert(70L);
*/

	// The first parameter to the program is the .sho file name.



	for(int i = 1;i < args.length;i++) 
	    {
	   //System.out.println("Processing Parameter *****"+i);
		int valueToInsert = (new Integer(args[i])).intValue();
		tree234.insert(valueToInsert);
		tree234.displayTree();
	    }

        show.close();

	/*
        do

        {
            System.out.print("Enter first letter of ");
            System.out.print("show, insert, or find: ");
            char c = getChar();
            switch(c)
            {
            case 115: // 's'
                tree234.displayTree();
                break;

            case 105: // 'i'
                System.out.print("Enter value to insert: ");
                long l = getInt();
                tree234.insert(l);
                break;

            case 102: // 'f'
                System.out.print("Enter value to find: ");
                long l1 = getInt();
                int i = tree234.find(l1);
                if(i != -1)
                    System.out.println("Found " + l1);
                else
                    System.out.println("Could not find " + l1);
                break;

            default:
                System.out.print("Invalid entry\n");
                break;
            }
        } while(true);
	*/

    }

    public static String getString() throws IOException
    {
        InputStreamReader inputstreamreader = new InputStreamReader(System.in);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        String s = bufferedreader.readLine();
        return s;
    }

    public static char getChar()
        throws IOException
    {
        String s = getString();
        return s.charAt(0);
    }

    public static int getInt()
        throws IOException
    {
        String s = getString();
        return Integer.parseInt(s);
    }



}
