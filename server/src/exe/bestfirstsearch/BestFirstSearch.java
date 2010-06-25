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

/* file: BestFirstSearch.java
 * author: Andrew Jungwirth
 * date: 18 July 2005
 *
 * This class contains methods for generating GAIGS snapshots to demonstrate
 * the execution of the Heuristic (Best-First) Search Algorithm. The main 
 * method receives the file name for the showfile via a command-line argument 
 * and then calls the necessary methods to write a series of snapshots to this 
 * file depicting a (more-or-less) randomly generated Best-First Search. There 
 * is a 40% probability that the graph generated will be a 
 * randomAStarSearchGraph.
 */

package exe.bestfirstsearch;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class BestFirstSearch{

    // These class constants are used for coloring nodes and edges.
    public static final String NEW_OPEN = "#00A1F4";
    public static final String OLD_OPEN = "#88E6F4";
    public static final String NEW_CLOSED = "#44FF44";
    public static final String OLD_CLOSED = "#AAFFAA";
    public static final String CURRENT_PATH = "#FFFF00";

    // PrintWriter for output to the GAIGS showfile. The snapshots of the 
    // algorithm visualization are written to the file indicated by the String
    // in the first command line argument using the new XML snapshot
    // definitions.
    private PrintWriter out;

    // Stores the current state of the search graph.
    private VisualGraph graph;

    // Stores the current state of the accompanying search tree.
    private Tree tree;

    // Reference to the list of nodes in the VisualGraph. Allows changes to the
    // nodes' appearances and information and the creation of the corresponding
    // tree nodes.
    private VisNode[] nodes;

    // Reference to the list of edges in the VisualGraph. Allows changes to the
    // edges' appearances and information and the creation of the corresponding
    // tree edges.
    private VisEdge[][] edges;

    // The number of nodes in the graph.
    private int num_nodes;

    // The index of the starting vertex in the nodes array.
    private int start;

    // The index of the goal vertex in the nodes array.
    private int goal;

    // This vector keeps track of the open list so that it is easy to output.
    private Vector open;

    // This vector keeps track of the closed list so that it is easy to output.
    private Vector closed;

    public static void main(String[] args){
	if(args.length > 1){
	    BestFirstSearch algo = new BestFirstSearch(args[0], true);
	}else{
	    BestFirstSearch algo = new BestFirstSearch(args[0], false);
	}
    }

    public BestFirstSearch(String filename, boolean new_data){
	Random rand = new Random();
	int prob;

	graph = new VisualGraph();
	graph.setBounds(0.0, 0.0, 1.0, 0.95);
	int[] start_and_goal = new int[2];
	if(!new_data){
	    try{
		graph.readGAIGSXMLGraph(filename);
		start_and_goal = graph.readStartGoal(filename);
	    }
	    catch(FileNotFoundException what){
		System.err.println("Could not find file with old data: " +
				   what.toString());
		new_data = true;
	    }
	    catch(IOException doh){
		System.err.println("Could not load old data: " + 
				   doh.toString());
		new_data = true;
	    }
	    catch(NoSuchElementException evil){
		System.err.println("Could not find start and goal in title: " +
				   evil.toString());
		new_data = true;
	    }

	    if(!new_data && !graph.hasHeuristics()){
		graph.initializeHValues(start_and_goal[1]);
	    }
	}
	if(new_data){
	    prob = rand.nextInt(5);
	    if(prob == 0 || prob == 1){
		start_and_goal = graph.randomAStarSearchGraph(0, -1);
	    }else{
		start_and_goal = graph.randomHeuristicGraph(0, -1);
	    }
	}
	start = start_and_goal[0];
	goal = start_and_goal[1];
	nodes = graph.getNodes();
	edges = graph.getEdges();
	num_nodes = graph.getNumNodes();

	tree = new Tree(false);
	tree.setBounds(0.575, 0.3, 1.085, 1.0);
	tree.setFontSize(0.04);
	tree.setSpacing(1.3, 1.3);

	open = new Vector(num_nodes * 3);
	closed = new Vector(num_nodes);

	try{
	    out = 
		new PrintWriter(new BufferedWriter(new FileWriter(filename)));
	}
	catch(IOException what){
	    System.err.println("Problem loading file: " + what.toString());
	}

	tree.setOut(out);

	// Initialization is complete.
	// Now, start generating the visualization.

	VisNode current, successor;
	TreeNode currentT, successorT;
	int currentI;
	Vector frontier = new Vector(num_nodes * 2);
	boolean reopen = false, boring = false;
	String path = "";
	String open1 = "", open2 = "", open3 = "", open4 = "";
	int[] qWeights = { -1, -1, 0, 1, 2, 2, 3 };
	int qRef = 0, qLast = -1, qPath = -1;
	XMLfibQuestion fibQ = null;
	XMLmcQuestion mcQ = null;
	XMLquestionCollection questions = new XMLquestionCollection(out);

	out.println("<?xml version = \"1.0\" encoding = \"UT-8\"?>");
	//	out.println("<!DOCTYPE show SYSTEM \"gaigs_sho.dtd\">");
	out.println("<!DOCTYPE show PUBLIC \"-//JHAVE//DTD GAIGS SHO//EN\" \"gaigs_sho.dtd\">");
	out.println("<show>");
	out.println("<snap>");
	out.println("<title>Best-First Search Initial State with Start Node " +
		    nodes[start].getChar() + " and Goal Node " +
		    nodes[goal].getChar() + ":</title>");
	out.println("<doc_url>bestfirstsearch.html</doc_url>");
	out.println("<pseudocode_url>index.php?</pseudocode_url>");
	writeStructs(false);
	out.println("</snap>");

	graph.setBounds(0.0, 0.2, 0.51, 0.9);
	graph.setFontSize(0.04);
	nodes[start].setHexColor(NEW_OPEN);
	current = new VisNode(nodes[start]);
	current.setCost(0);
	open.add(current);

	currentT = new TreeNode(current);
	currentT.setValue(current.getChar() + "\n" + current.getCost());
	currentT.setHexColor(NEW_OPEN);
	tree.setRoot(currentT);

	writeLists();
	out.println("<pseudocode_url>index.php?line=1</pseudocode_url>");
	writeStructs(true);

	try{
	    current = (VisNode) open.remove(0);
	}
	catch(ArrayIndexOutOfBoundsException huh){
	    System.err.println("Tried to remove from empty list: " + 
			       huh.toString());
	}
	currentI = graph.translateCharIndex(current.getChar());
	while(current.getChar() != nodes[goal].getChar()){
	    if(!nodes[currentI].isClosed()){
		current.setClosed(true);
		current.setHexColor(NEW_CLOSED);
		closed.add(current);
		nodes[currentI] = current;

		currentT = new TreeNode(current);
		currentT.setValue(current.getChar() + "\n" + 
				  current.getCost());
		currentT = closeNode(currentT);

		for(int n = 0; n < frontier.size(); n++){
		    successorT = (TreeNode) frontier.get(n);

		    if(successorT.getValue() == currentT.getValue() &&
		       successorT.getPred() == currentT.getPred()){
			frontier.removeElementAt(n);
			break;
		    }
		}

		switch(qLast){
		case 0:
		    fibQ.insertQuestion();
		    break;
		case 1:
		    fibQ.insertQuestion();
		    break;
		case 2:
		    fibQ.insertQuestion();
		    break;
		case 3:
		    if(open4.length() <= 2){
			boring = true;
			break;
		    }

		    mcQ.insertQuestion();
		}

		out.println("</snap>");

		writeLists();
		out.println("<pseudocode_url>index.php?line=4</pseudocode_url>");
		writeStructs(true);

		for(int n = 0; n < num_nodes; n++){
		    if(edges[currentI][n].isActivated()){
			if(!nodes[n].isClosed()){
			    nodes[n].setHexColor(NEW_OPEN);
			    edges[currentI][n].setHexColor(NEW_OPEN);
			    edges[n][currentI].setHexColor(NEW_OPEN);
			    successor = new VisNode(nodes[n]);
			    successor.setCost(current.getCost() + (int) 
					      edges[currentI][n].getWeight());
			    successor.setPred(current.getChar());
			    try{
				addOpen(successor);
			    }
			    catch(ArrayIndexOutOfBoundsException buh){
				System.err.println("Open insertion failed: " +
						   buh.toString());
			    }
			    boring = false;

			    successorT = new TreeNode(successor);
			    successorT.setValue(successor.getChar() + "\n" +
						successor.getCost());
			    successorT.setHexColor(NEW_OPEN);
			    currentT.setChildWithEdge(successorT);
			    successorT.getLineToParent().setHexColor(NEW_OPEN);
			    frontier.add(successorT);

			    open1 = open1 + successor.getChar() + " ";
			    open2 = open2 + successor.getChar() + " ";
			    prob = rand.nextInt(4);
			    if(prob == 0 || prob == 1 || prob == 2){
				open3 = open3 + successor.getChar() + " ";
			    }

			    out.println("</snap>");

			    writeLists();
			    out.println("<pseudocode_url>index.php?line=6</pseudocode_url>");
			    writeStructs(true);
			}else if((current.getCost() + 
				  (int) edges[currentI][n].getWeight()) < 
				 nodes[n].getCost()){
			    reopen = true;
			    boring = false;

			    open2 = open2 + nodes[n].getChar() + " ";
			    prob = rand.nextInt(4);
			    if(prob == 0 || prob == 1 || prob == 2){
				open3 = open3 + nodes[n].getChar() + " ";
			    }
			}
			if(nodes[n].getChar() != current.getPred()){
			    open4 = open4 + nodes[n].getChar() + " ";
			}
		    }
		}

		switch(qLast){
		case 0:
		    questions.addQuestion(fibQ);
		    fibQ.setAnswer("" + current.getChar());
		    fibQ.setAnswer("" + 
				   Character.toLowerCase(current.getChar()));
		    break;
		case 1:
		    questions.addQuestion(fibQ);
		    fibQ.setAnswer("" + current.getCost());
		    break;
		case 2:
		    questions.addQuestion(fibQ);
		    fibQ.setAnswer("" + (open1.length() / 2));
		    break;
		case 3:
		    if(open4.length() <= 2){
			boring = true;
			break;
		    }

		    questions.addQuestion(mcQ);

		    int remove = 0;
		    String temp = "";
		    int length;
		    boolean up = false, changed = false, valid = true;
		    Character add;

		    if(open1.length() < 2){
			mcQ.addChoice("No nodes will be added to the open queue.");
		    }else{
			mcQ.addChoice(open1);
		    }

		    length = open2.length();
		    if(open1.compareTo(open2) == 0){
			if(length >= 2){
			    remove = rand.nextInt(length);
			    if((remove % 2) == 1){
				remove--;
			    }
			    for(int i = 0; i < length; i += 2){
				if(i != remove){
				    temp = temp + open2.substring(i, i + 2);
				}
			    }
			    open2 = temp;
			    temp = "";
			}else{
			    for(int e = 0; e < num_nodes; e++){
				if(edges[currentI][e].isActivated()){
				    add = new Character(nodes[e].getChar());

				    for(int i = 0; i < length; i += 2){
					if(add.compareTo(new Character(
                                           open2.charAt(i))) < 0){
					    open2 = open2.substring(0, i) +
						add.charValue() + " " + 
						open2.substring(i + 2, length);
					    break;
					}else if(add.compareTo(new Character(
                                                 open2.charAt(i))) == 0){
					    break;
					}else if(i == (length - 2)){
					    open2 = open2 + 
						add.charValue() + " ";
					    break;
					}
				    }
				}
			    }
			}
		    }

		    if(open2.length() < 2){
			if(open1.length() >= 2){
			    mcQ.addChoice("No nodes will be added to the open queue.");
			}
		    }else{
			mcQ.addChoice(open2);
		    }

		    while(open3.length() < 2 ||
			  open1.compareTo(open3) == 0 || 
			  open2.compareTo(open3) == 0){
			length = open3.length();

			if(length > 2 && !up){
			    remove = rand.nextInt(length);
			    if((remove % 2) == 1){
				remove--;
			    }
			    for(int i = 0; i < length; i += 2){
				if(i != remove){
				    temp = temp + open3.substring(i, i + 2);
				}
			    }
			    open3 = temp;
			    temp = "";
			}else{
			    up = true;

			    for(int e = 0; e < num_nodes; e++){
				if(edges[currentI][e].isActivated()){
				    add = new Character(nodes[e].getChar());

				    for(int i = 0; i < length; i += 2){
					if(add.compareTo(new Character(
                                           open3.charAt(i))) < 0){
					    open3 = open3.substring(0, i) + 
						add.charValue() + " " + 
						open3.substring(i + 2, length);
					    changed = true;
					    break;
					}else if(add.compareTo(new Character(
                                                 open3.charAt(i))) == 0){
					    break;
					}else if(i == (length - 2)){
					    open3 = open3 + 
						add.charValue() + " ";
					    changed = true;
					    break;
					}
				    }

				    if(changed){
					break;
				    }
				}
			    }

			    if(!changed){
				for(int e = 0; e < num_nodes; e++){
				    if(!edges[currentI][e].isActivated()){
					add = 
					    new Character(nodes[e].getChar());

					for(int i = 0; i < length; i += 2){
					    if(add.compareTo(new Character(
                                               open3.charAt(i))) < 0){
						open3 = open3.substring(0, i) +
						    add.charValue() + " " +
						    open3.substring(i + 2, 
								    length);
						changed = true;
						break;
					    }else if(add.compareTo(
                                                     new Character(
                                                     open3.charAt(i))) == 0){
						break;
					    }else if(i == (length - 2)){
						open3 = open3 + 
						    add.charValue() + " ";
						changed = true;
						break;
					    }
					}

					if(changed){
					    break;
					}
				    }
				}
			    }
			
			    if(!changed){
				valid = false;
				break;
			    }
			}

			changed = false;
		    }

		    if(valid){
			mcQ.addChoice(open3);
		    }else{
			valid = true;
		    }
		    up = false;

		    while(open4.length() < 2 ||
			  open1.compareTo(open4) == 0 ||
			  open2.compareTo(open4) == 0 ||
			  open3.compareTo(open4) == 0){
			length = open4.length();

			if(length > 2 && !up){
			    remove = rand.nextInt(length);
			    if((remove % 2) == 1){
				remove--;
			    }
			    for(int i = 0; i < length; i += 2){
				if(i != remove){
				    temp = temp + open4.substring(i, i + 2);
				}
			    }
			    open4 = temp;
			    temp = "";
			}else{
			    up = true;

			    for(int e = 0; e < num_nodes; e++){
				if(edges[currentI][e].isActivated()){
				    add = new Character(nodes[e].getChar());

				    for(int i = 0; i < length; i += 2){
					if(add.compareTo(new Character(
                                           open4.charAt(i))) < 0){
					    open4 = open4.substring(0, i) + 
						add.charValue() + " " + 
						open4.substring(i + 2, length);
					    changed = true;
					    break;
					}else if(add.compareTo(new Character(
                                                 open4.charAt(i))) == 0){
					    break;
					}else if(i == (length - 2)){
					    open4 = open4 + 
						add.charValue() + " ";
					    changed = true;
					    break;
					}
				    }

				    if(changed){
					break;
				    }
				}
			    }

			    if(!changed){
				for(int e = 0; e < num_nodes; e++){
				    if(!edges[currentI][e].isActivated()){
					add = 
					    new Character(nodes[e].getChar());

					for(int i = 0; i < length; i += 2){
					    if(add.compareTo(new Character(
                                               open4.charAt(i))) < 0){
						open4 = open4.substring(0, i) +
						    add.charValue() + " " +
						    open4.substring(i + 2, 
								    length);
						changed = true;
						break;
					    }else if(add.compareTo(
                                                     new Character(
                                                     open4.charAt(i))) == 0){
						break;
					    }else if(i == (length - 2)){
						open4 = open4 + 
						    add.charValue() + " ";
						changed = true;
						break;
					    }
					}

					if(changed){
					    break;
					}
				    }
				}
			    }
			
			    if(!changed){
				valid = false;
				break;
			    }
			}

			changed = false;
		    }

		    if(valid){
			mcQ.addChoice(open4);
		    }

		    mcQ.setAnswer(1);
		    mcQ.shuffle();
		    break;
		}

		{
		    String nodeC;
		    String edgeC;

		    for(int n = 0; n < num_nodes; n++){
			nodeC = nodes[n].getHexColor();
		    
			if(nodeC == NEW_CLOSED || nodeC == CURRENT_PATH){
			    nodes[n].setHexColor(OLD_CLOSED);
			}else if(nodeC == NEW_OPEN){
			    nodes[n].setHexColor(OLD_OPEN);
			}
		    
			for(int e = 0; e < num_nodes; e++){
			    if(edges[n][e].isActivated()){
				edgeC = edges[n][e].getHexColor();
				
				if(edgeC == NEW_CLOSED || 
				   edgeC == CURRENT_PATH){
				    edges[n][e].setHexColor(OLD_CLOSED);
				    edges[e][n].setHexColor(OLD_CLOSED);
				}else if(edgeC == NEW_OPEN){
				    edges[n][e].setHexColor(OLD_OPEN);
				    edges[e][n].setHexColor(OLD_OPEN);
				}
			    }
			}
		    }

		    currentT.setHexColor(OLD_CLOSED);
		    if(currentT.getPred() != '~'){
			currentT.getLineToParent().setHexColor(OLD_CLOSED);
		    }
		    successorT = currentT.getChild();
		    while(successorT != null){
			successorT.setHexColor(OLD_OPEN);
			successorT.getLineToParent().setHexColor(OLD_OPEN);
			successorT = successorT.getSibling();
		    }

		    reopen = false;
		    
		    open1 = open2 = open3 = open4 = "";
		}
	    }else{
		// If the node removed from the front of the open queue was 
		// already closed, then clean up the search tree so that this
		// node is no longer highlighted as being in the open queue.
		for(int n = 0; n < frontier.size(); n++){
		    currentT = (TreeNode) frontier.get(n);

		    if(currentT.getChar() == current.getChar() &&
		       currentT.getCost() == current.getCost() &&
		       currentT.getPred() == current.getPred()){
			frontier.removeElementAt(n);
			currentT.setHexColor("#FFFFFF");
			currentT.getLineToParent().setHexColor("#999999");
			break;
		    }
		}

		boring = true;

		out.println("</snap>");

		writeLists();
		out.println("<pseudocode_url>index.php?line=8</pseudocode_url>");
		writeStructs(true);
	    }
       
	    if(!boring){
		int temp = qWeights[rand.nextInt(qWeights.length)];
		while(temp == qLast){
		    temp = qWeights[rand.nextInt(qWeights.length)];
		}
		qLast = temp;

		// This reduces the probability of asking the same type of 
		// question again.
		for(int i = 0; i < qWeights.length; i++){
		    if(qWeights[i] == qLast){
			qWeights[i] = -1;
			break;
		    }
		}

		switch(qLast){
		case 0:
		    fibQ = new XMLfibQuestion(out, 
					      new Integer(qRef++).toString());
		    fibQ.setQuestionText("Which node will be closed in the next time through the while loop?");
		    break;
		case 1:
		    fibQ = new XMLfibQuestion(out, 
					      new Integer(qRef++).toString());
		    fibQ.setQuestionText("What will be the cost of the next node that is closed?");
		    break;
		case 2:
		    fibQ = new XMLfibQuestion(out,
					      new Integer(qRef++).toString());
		    fibQ.setQuestionText("How many nodes will be added to the open queue in the next time through the while loop?");
		    break;
		case 3:
		    mcQ = 
			new XMLmcQuestion(out, new Integer(qRef++).toString());
		    mcQ.setQuestionText("Which nodes will be added to the open queue in the next time through the while loop?");
		    break;
		}
	    }

	    try{
		current = (VisNode) open.remove(0);
	    }
	    catch(ArrayIndexOutOfBoundsException bigmiss){
		System.err.println("Best-First failed to find a path to the goal: " +
				   bigmiss.toString());
		out.close();
		System.exit(1);
	    }
	    currentI = graph.translateCharIndex(current.getChar());

	}

	current.setClosed(true);
	current.setHexColor(NEW_CLOSED);
	closed.add(current);
	nodes[currentI] = current;

	currentT = new TreeNode(current);
	currentT.setValue(current.getChar() + "\n" + current.getCost());
	closeNode(currentT);

	out.println("</snap>");

	writeLists();
	out.println("<pseudocode_url>index.php?line=10</pseudocode_url>");
	writeStructs(true);
	out.println("</snap>");

	{
	    String nodeC;
	    String edgeC;

	    for(int n = 0; n < num_nodes; n++){
		nodeC = nodes[n].getHexColor();

		if(nodeC == NEW_CLOSED || nodeC == CURRENT_PATH){
		    nodes[n].setHexColor(NEW_CLOSED);
		}else{
		    nodes[n].setHexColor("#FFFFFF");
		}

		for(int e = 0; e < num_nodes; e++){
		    if(edges[n][e].isActivated()){
			edgeC = edges[n][e].getHexColor();

			if(edgeC == NEW_CLOSED || edgeC == CURRENT_PATH){
			    edges[n][e].setHexColor(NEW_CLOSED);
			    edges[e][n].setHexColor(NEW_CLOSED);
			    nodes[n].setHexColor(NEW_CLOSED);
			    nodes[e].setHexColor(NEW_CLOSED);
			}else{
			    edges[n][e].setHexColor("#999999");
			    edges[e][n].setHexColor("#999999");
			}
		    }
		}
	    }
	}

	graph.setBounds(0.0, 0.0, 1.0, 0.95);
	graph.setFontSize(0.03);
	tree.setRoot(null);

	out.println("<snap>");
	out.println("<title>Best-First Search Shortest Path from Start Node " +
		    nodes[start].getChar() + " to Goal Node " +
		    nodes[goal].getChar() + ":</title>");
	out.println("<pseudocode_url>index.php?line=11</pseudocode_url>");
	writeStructs(false);
	out.println("</snap>");

	questions.writeQuestionsAtEOSF();

	out.println("</show>");
	out.close();
    }

    private void addOpen(VisNode current)throws ArrayIndexOutOfBoundsException{
	int size = open.size();

	if(size == 0){
	    open.add(current);
	}else{
	    for(int n = 0; n < size; n++){
		if(current.compareToNoCost((VisNode) open.get(n)) < 0){
		    open.add(n, current);
		    break;
		}else if(n == (size - 1)){
		    open.add(current);
		}
	    }
	}
    }

    private TreeNode closeNode(TreeNode closing){
	String path = "";
	char pred = closing.getPred();
	int node1, node2;
	TreeNode parent, child;

	if(pred == '~'){
	    tree.getRoot().setHexColor(NEW_CLOSED);
	    return tree.getRoot();
	}else{
	    path = pred + path;
	    node1 = graph.translateCharIndex(pred);
	    node2 = graph.translateCharIndex(closing.getChar());
	    edges[node1][node2].setHexColor(NEW_CLOSED);
	    edges[node2][node1].setHexColor(NEW_CLOSED);
	    pred = nodes[node1].getPred();

	    while(pred != '~'){
		path = pred + path;
		node2 = node1;
		node1 = graph.translateCharIndex(pred);
		edges[node1][node2].setHexColor(CURRENT_PATH);
		edges[node2][node1].setHexColor(CURRENT_PATH);
		pred = nodes[node1].getPred();
	    }

	    parent = tree.getRoot();

	    for(int n = 1; n < path.length(); n++){
		parent = parent.getChild();
		while(parent.getChar() != path.charAt(n)){
		    parent = parent.getSibling();
		}
	    }

	    child = parent.getChild();
	    if(child != null){
		while(child.getChar() != closing.getChar()){
		    child = child.getSibling();
		}
	    }
	    child.setHexColor(NEW_CLOSED);
	    child.getLineToParent().setHexColor(NEW_CLOSED);
	    return child;
	}
    }

    private void writeLists(){
	VisNode current;

	out.println("<snap>");
	out.print("<title>open = { ");
	if(open.isEmpty()){
	    out.println("empty }");
	}else{
	    for(int i = 0; (i < 10) && (i < open.size()); i++){
		current = (VisNode)open.get(i);
		out.print(current.getChar() + "(" + current.getPred() + "-" +
			  current.getCost() + "+" + 
			  current.getHeuristic() + ") ");
	    }
	    if(open.size() > 10){
		out.println("... }");
	    }else{
		out.println("}");
	    }
	}

	out.print("closed = { ");
	if(closed.isEmpty()){
	    out.println("empty }</title>");
	}else{
	    for(int i = 0; i < closed.size(); i++){
		current = (VisNode)closed.get(i);
		out.print(current.getChar() + "(" + current.getPred() + "-" +
			  current.getCost() + ") ");
	    }
	    out.println("}</title>");
	}
    }

    private void writeStructs(boolean show_key){
	graph.writeGAIGSXMLGraph(out);
	tree.writeGAIGSXMLTree();

	if(show_key){
	    out.println("<array>");
	    out.println("<bounds x1 = \"0.0\" y1 = \"-0.2\" x2 = \"1.02\" y2 = \"0.4\" fontsize = \"0.04\"/>");
	    out.println("<column_label>Color Key</column_label>");
	    out.println("<column>");
	    out.println("<list_item color = \"" + NEW_OPEN + "\">");
	    out.println("<label>New Open</label>");
	    out.println("</list_item>");
	    out.println("<list_item color = \"" + OLD_OPEN + "\">");
	    out.println("<label>Old Open</label>");
	    out.println("</list_item>");
	    out.println("</column>");
	    out.println("<column>");
	    out.println("<list_item color = \"" + NEW_CLOSED + "\">");
	    out.println("<label>New Closed</label>");
	    out.println("</list_item>");
	    out.println("<list_item color = \"" + OLD_CLOSED + "\">");
	    out.println("<label>Old Closed</label>");
	    out.println("</list_item>");
	    out.println("</column>");
	    out.println("<column>");
	    out.println("<list_item color = \"" + CURRENT_PATH + "\">");
	    out.println("<label>Current Path</label>");
	    out.println("</list_item>");
	    out.println("<list_item color = \"#999999\">");
	    out.println("<label>Unvisited Path</label>");
	    out.println("</list_item>");
	    out.println("</column>");
	    out.println("</array>");
	}
    }
}

