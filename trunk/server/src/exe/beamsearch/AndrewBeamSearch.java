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

/* file: AndrewBeamSearch.java
 * author: Andrew Jungwirth
 * date: 19 December 2005
 *
 * This class contains methods for generating GAIGS snapshots to demonstrate
 * the execution of the Beam Search Algorithm. The main method receives the
 * file name for the showfile and the beam size via command-line arguments and
 * then calls the necessary methods to write a series of snapshots to the given
 * file that depict a randomly generated Beam Search.
 */

package exe.beamsearch;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class AndrewBeamSearch{

    // These class constants are used for coloring nodes and edges.
    public static final String NEW_SET = "#00A1F4";
    public static final String OLD_SET = "#88E6F4";
    public static final String NEW_BEAM = "#44FF44";
    public static final String OLD_BEAM = "#AAFFAA";
    public static final String HASH = "#FFFF00";
    public static final String HASHED = "#FF9900";
    public static final String START = "#FFAAAA";
    public static final String GOAL = "#FF3232";

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

    // The number of nodes kept at each level in the search, the beam width.
    private int beam_size;

    // The number of nodes that can be stored in memory before memory is full.
    private int memory_size;

    // This vector keeps track of the current set of frontier nodes.
    private Vector set;

    // This vector holds the current set of frontier nodes in the tree.
    private Vector setT;

    // This vector keeps track of the nodes in the current beam in the search.
    private Vector beam;

    // The current depth of the search.
    private int g;

    // This array serves as the hash table for nodes that have been visited. 
    // Its size is specified by the constructor's M parameter. If it becomes 
    // full before the goal node is found, the search is terminated 
    // unsuccessfully due to running out of memory.
    private char[] hash_table;

    public static void main(String[] args){
	AndrewBeamSearch algo = new AndrewBeamSearch(args[0], args[1] + ".sho",
					 Integer.valueOf(args[2]).intValue(),
					 Integer.valueOf(args[3]).intValue(),
					 args[4]);
    }

    public AndrewBeamSearch(String out_file, String in_file, int B, int M, String d){
	beam_size = B;
	memory_size = M;

	graph = new VisualGraph();
	graph.setBounds(0.0, 0.0, 1.0, 0.95);
	int[] start_and_goal = new int[2];
	try{
	    graph.readGAIGSXMLGraph("exe/beamsearch/graphs/" + in_file);
	    start_and_goal = graph.readStartGoal("exe/beamsearch/graphs/" + 
						 in_file);
	}
	catch(FileNotFoundException what){
	    System.err.println("Could not find file with old data: " +
			       what.toString());
	    System.exit(1);
	}
	catch(IOException doh){
	    System.err.println("Could not load old data: " + 
			       doh.toString());
	    System.exit(1);
	}
	catch(NoSuchElementException evil){
	    System.err.println("Could not find start and goal in title: " +
			       evil.toString());
	    System.exit(1);
	}
	start = start_and_goal[0];
	goal = start_and_goal[1];
	nodes = graph.getNodes();
	edges = graph.getEdges();
	num_nodes = graph.getNumNodes();

	tree = new Tree(false);
	tree.setBounds(0.55, 0.3, 1.06, 0.99);
	tree.setSpacing(1.2, 1.5);
	switch(beam_size){
	case 1:
	    tree.setFontSize(0.035);
	    break;
	case 2:
	    tree.setFontSize(0.035);
	    break;
	case 3:
	    tree.setFontSize(0.034);
	    break;
	case 4:
	    tree.setFontSize(0.033);
	    break;
	case 5:
	    tree.setFontSize(0.033);
	    break;
	default:
	    tree.setFontSize(0.035);
	    break;
	}

	set = new Vector(num_nodes * 3);
	beam = new Vector(beam_size);
	hash_table = new char[memory_size];

	try{
	    out = 
		new PrintWriter(new BufferedWriter(new FileWriter(out_file)));
	}
	catch(IOException what){
	    System.err.println("Problem loading file: " + what.toString());
	}

	tree.setOut(out);

	for(int i = 0; i < memory_size; i++){
	    hash_table[i] = '~';
	}

	// Initialization is complete.
	// Now, start generating the visualization.

	VisNode current, successor;
	TreeNode currentT, successorT;
	int currentI = start, current_beam_size = 1;
	boolean goal_found = false, memory_full = false, show_detail;
	boolean question_answered = true;
	int qRef = 0, qLast = -1, qAsked = 0;
	XMLfibQuestion fibQ = null;
	XMLtfQuestion tfQ = null;
	XMLfibQuestion beamQ = null;
	XMLquestionCollection questions = new XMLquestionCollection(out);
	Random rand = new Random();

	if(d.charAt(0) == 'L'){
	    show_detail = false;
	}else{
	    show_detail = true;
	}

	out.println("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
	//	out.println("<!DOCTYPE show SYSTEM \"gaigs_sho.dtd\">");
	out.println("<!DOCTYPE show PUBLIC \"-//JHAVE//DTD GAIGS SHO//EN\" \"gaigs_sho.dtd\">");
	out.println("<show>");
	out.println("<snap>");
	out.println("<title>Beam Search Initial State with Start Node " +
		    nodes[start].getChar() + " and Goal Node " +
		    nodes[goal].getChar() + ":</title>");
	out.println("<doc_url>beamsearch.html</doc_url>");
	out.println("<pseudocode_url>index.php?</pseudocode_url>");
	writeStructs(false);
	out.println("</snap>");

	graph.setBounds(-0.1, 0.2, 0.41, 0.9);
	graph.setFontSize(0.035);

	nodes[start].setHexColor(START);
	nodes[goal].setHexColor(GOAL);

	g = 0;

	writeLists("");
	out.println("<pseudocode_url>index.php?line=2</pseudocode_url>");
	writeStructs(true);
	out.println("</snap>");

	nodes[start].setHexColor(NEW_BEAM);
	current = new VisNode(nodes[start]);
	current.setCost(0);
	beam.add(current);

	currentT = new TreeNode(current);
	currentT.setValue(current.getChar() + "\n" + current.getHeuristic());
	tree.setRoot(currentT);

	hash_table[hash(start)] = current.getChar();

	writeLists("start = " + current.getChar() + ", ");
	out.println("<pseudocode_url>index.php?line=3</pseudocode_url>");
	writeStructs(true);
	if(!show_detail){
	    out.println("</snap>");
	}

	current.setHexColor(OLD_BEAM);
	nodes[start].setHexColor(OLD_BEAM);
	currentT.setHexColor(OLD_BEAM);

	while(!beam.isEmpty()){
	    int[] qWeights1 = { -1, 1, -1, -1, 3, -1, 4, 
				-1, 5, -1, -1, 6, -1 };
	    int[] qWeights2 = { 2, -1, 7, -1, 8, -1, 9 };
	    beamQ = null;

	    set.clear();

	    for(int n = 0; n < current_beam_size; n++){
		try{
		    current = (VisNode)beam.remove(0);
		}
		catch(ArrayIndexOutOfBoundsException impossible){
		    break;
		}
	    
		currentI = graph.translateCharIndex(current.getChar());
		nodes[currentI] = current;
		for(int e = 0; e < num_nodes; e++){
		    if(edges[currentI][e].isActivated()){
			if(e == goal){
			    goal_found = true;
			    nodes[e].setHexColor(GOAL);
			    edges[currentI][e].setHexColor(GOAL);
			    edges[e][currentI].setHexColor(GOAL);
			    nodes[e].setCost(current.getCost() + 1);
			    nodes[e].setPred(current.getChar());

			    successorT = new TreeNode(nodes[e]);
			    successorT.setValue(nodes[e].getChar() + "\n" +
						nodes[e].getHeuristic());
			    successorT.setSibling(currentT.getChild());
			    successorT.setParent(currentT);
			    currentT.setChild(successorT);
			    successorT.setLineToParent(new Edge(currentT, 
								successorT));
			    successorT.getLineToParent().setHexColor(GOAL);

			    if(show_detail){
				out.println("</snap>");
				writeLists("state = " + current.getChar() + 
					   ", s = " + nodes[e].getChar() + 
					   ", ");
				out.println("<pseudocode_url>index.php?line=5</pseudocode_url>");
				writeStructs(true);
				out.println("</snap>");
			    }

			    break;
			}else{
			    int set_size = set.size();
			    VisNode check = null;
			    boolean in_set = false;

			    if(show_detail && question_answered){
				int length = qWeights1.length;
				int temp = qWeights1[rand.nextInt(length)];
				while(temp != -1 && temp == qLast){
				    temp = qWeights1[rand.nextInt(length)];
				}
				qLast = temp;

				if(set_size == 0 &&(qLast == 3 || qLast == 6)){
				    qLast = -1;
				}else{			    
				    // This reduces the probability of asking
				    // the same type of question again.
				    for(int i = 0; i < length; i++){
					if(qWeights1[i] == qLast){
					    qWeights1[i] = -1;
					    break;
					}
				    }
				}
			    
				switch(qLast){
				case 1: 
				    fibQ = new XMLfibQuestion
					(out, new Integer(qRef++).toString());
				    fibQ.setQuestionText("Which state (not already in the SET) will be added to SET next?");
				    fibQ.insertQuestion();
				    break;
				case 3:
				    tfQ = new XMLtfQuestion
					(out, new Integer(qRef++).toString());
				    tfQ.setQuestionText("The state that will be generated next is already in the SET of successors.");
				    tfQ.insertQuestion();
				    break;
				case 4:
				    tfQ = new XMLtfQuestion
					(out, new Integer(qRef++).toString());
				    tfQ.setQuestionText("The state that will be generated next is already in the hash table.");
				    tfQ.insertQuestion();
				    break;
				case 5:
				    fibQ = new XMLfibQuestion
					(out, new Integer(qRef++).toString());
				    fibQ.setQuestionText("What is the depth of the state that will be generated next?");
				    fibQ.insertQuestion();
				    break;
				case 6:
				    tfQ = new XMLtfQuestion
					(out, new Integer(qRef++).toString());
				    tfQ.setQuestionText("The loop that generates the SET of successors is now complete, and the beam will now be built.");
				    tfQ.insertQuestion();
				    break;
				}
			    }

			    for(int i = 0; i < set_size; i++){
				try{
				    check = (VisNode)set.get(i);
				}
				catch(IndexOutOfBoundsException what){
				    System.err.println("Check set failed: " +
						       what.toString());
				}

				if(check.getChar() == nodes[e].getChar()){
				    in_set = true;
				    break;
				}
			    }

			    if(in_set){
				switch(qLast){
				case 1:
				    question_answered = false;
				    break;
				case 3:
				    questions.addQuestion(tfQ);
				    tfQ.setAnswer(true);
				    question_answered = true;
				    break;
				case 4:
				    questions.addQuestion(tfQ);
				    tfQ.setAnswer(findHash(e));
				    question_answered = true;
				    break;
				case 5:
				    questions.addQuestion(fibQ);
				    fibQ.setAnswer("" + (g + 1));
				    question_answered = true;
				    break;
				case 6:
				    qAsked++;
				    question_answered = false;
				    break;
				}

				nodes[e].setHexColor(NEW_SET);
				edges[currentI][e].setHexColor(NEW_SET);
				edges[e][currentI].setHexColor(NEW_SET);

				successorT = currentT.getChild();
				while(successorT.getChar() != 
				      nodes[e].getChar()){
				    successorT = successorT.getSibling();
				}
				successorT.setHexColor(NEW_SET);
				successorT.getLineToParent().setHexColor
				    (NEW_SET);
			    }else{
				switch(qLast){
				case 1:
				    questions.addQuestion(fibQ);
				    fibQ.setAnswer("" + nodes[e].getChar());
				    fibQ.setAnswer("" + Character.toLowerCase(nodes[e].getChar()));
				    question_answered = true;
				    break;
				case 3:
				    questions.addQuestion(tfQ);
				    tfQ.setAnswer(false);
				    question_answered = true;
				    break;
				case 4:
				    questions.addQuestion(tfQ);
				    tfQ.setAnswer(findHash(e));
				    question_answered = true;
				    break;
				case 5:
				    questions.addQuestion(fibQ);
				    fibQ.setAnswer("" + (g + 1));
				    question_answered = true;
				    break;
				case 6:
				    qAsked++;
				    question_answered = false;
				    break;
				}

				nodes[e].setHexColor(NEW_SET);
				edges[currentI][e].setHexColor(NEW_SET);
				edges[e][currentI].setHexColor(NEW_SET);
				successor = new VisNode(nodes[e]);
				successor.setCost(current.getCost() + 1);
				successor.setPred(current.getChar());
			    
				try{
				    addSet(set, successor);
				}
				catch(ArrayIndexOutOfBoundsException what){
				    System.err.println("Add to set failed: " +
						       what.toString());
				}
				
				successorT = new TreeNode(successor);
				successorT.setValue(successor.getChar() + "\n"+
						    successor.getHeuristic());
				insertNode(currentT, successorT);
				successorT.getLineToParent().setHexColor
				    (NEW_SET);
			    }
			    
			    if(show_detail){
				out.println("</snap>");
				writeLists("state = " + current.getChar() + 
					   ", s = " + nodes[e].getChar() + 
					   ", ");
				out.println("<pseudocode_url>index.php?line=5</pseudocode_url>");
				writeStructs(true);
			    }
			    
			    nodes[e].setHexColor(OLD_SET);
			    edges[currentI][e].setHexColor(OLD_SET);
			    edges[e][currentI].setHexColor(OLD_SET);
			    
			    successorT.setHexColor(OLD_SET);
			    successorT.getLineToParent().setHexColor(OLD_SET);
			}
		    }
		}
		
		if(goal_found){
		    break;
		}
	    }

	    // Break out of loop to finish the visualization if goal was found.
	    if(goal_found){
		break;
	    }

	    if(qLast == 6){
		if(qAsked == 0){
		    questions.addQuestion(tfQ);
		    tfQ.setAnswer(true);
		    question_answered = true;
		}else{
		    questions.addQuestion(tfQ);
		    tfQ.setAnswer(false);
		    question_answered = true;
		}
	    }

	    if(!show_detail){
		writeLists("");
		out.println("<pseudocode_url>index.php?line=5</pseudocode_url>");		
		writeStructs(true);
		out.println("</snap>");
	    }else{
		out.println("</snap>");
	    }

	    // Trim the tree so only the beam remains.
	    int set_size = set.size();
	    int new_beam_nodes = 0;
	    boolean add_to_beam = true;
	    TreeNode previous = null;
	    successorT = currentT.getChild();

	    for(int n = 0; n < num_nodes; n++){
		if(nodes[n].getHexColor() == OLD_BEAM){
		    if(n == start){
			nodes[n].setHexColor(START);
		    }else{
			int e = graph.translateCharIndex(nodes[n].getPred());

			nodes[n].setHexColor(HASH);
			if(edges[e][n].getHexColor() != OLD_SET){
			    edges[e][n].setHexColor(HASH);
			    edges[n][e].setHexColor(HASH);
			}
		    }
		}
	    }

	    Edge line = currentT.getLineToParent();
	    if(line == null){
		currentT.setHexColor(START);
	    }else{
		currentT.setHexColor(HASH);
		currentT.getLineToParent().setHexColor(HASH);
	    }
	    
	    g++;
	    
	    writeLists("");
	    out.println("<pseudocode_url>index.php?line=6</pseudocode_url>");
	    writeStructs(true);

	    for(int s = 0; s < set_size; s++){
		try{
		    successor = (VisNode)set.remove(0);
		}
		catch(ArrayIndexOutOfBoundsException huh){
		    break;
		}
		
		currentI = graph.translateCharIndex(successor.getPred());
		int successorI = graph.translateCharIndex(successor.getChar());
		
		if(add_to_beam){
		    if(question_answered){
			int length = qWeights2.length;
			int temp = qWeights2[rand.nextInt(length)];
			while(temp != -1 && temp == qLast){
			    temp = qWeights2[rand.nextInt(length)];
			}
			qLast = temp;
			
			if(beam.size() == 0 && (qLast == 8 || qLast == 9)){
			    qLast = -1;
			}else{			    
			    // This reduces the probability of asking the same
			    // type of question again.
			    for(int i = 0; i < length; i++){
				if(qWeights2[i] == qLast){
				    qWeights2[i] = -1;
				    break;
				}
			    }
			}
			    
			switch(qLast){
			case 2: 
			    fibQ = new XMLfibQuestion
				(out, new Integer(qRef++).toString());
			    fibQ.setQuestionText("Which state will be added to the hash table and the BEAM next? (Enter '~' if no other states can be added to the BEAM in this iteration.)");
			    fibQ.insertQuestion();
			    break;
			case 7:
			    tfQ = new XMLtfQuestion
				(out, new Integer(qRef++).toString());
			    tfQ.setQuestionText("The state that will be considered next in SET will be added to the BEAM.");
			    tfQ.insertQuestion();
			    break;
			case 8:
			    tfQ = new XMLtfQuestion
				(out, new Integer(qRef++).toString());
			    tfQ.setQuestionText("There is room for more states in the BEAM.");
			    tfQ.insertQuestion();
			    break;
			case 9:
			    beamQ = new XMLfibQuestion
				(out, new Integer(qRef++).toString());
			    beamQ.setQuestionText("How many states will be in the BEAM at this level?");
			    beamQ.insertQuestion();
			    break;
			}
		    }

		    int slot = hash(successorI);
		    switch(slot){
		    case -2:
			switch(qLast){
			case 2:
			    questions.addQuestion(fibQ);
			    fibQ.setAnswer("~");
			    question_answered = true;
			    break;
			case 7:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(false);
			    question_answered = true;
			    break;
			case 8:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(true);
			    question_answered = true;
			    break;
			}

			out.println("</snap>");
			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=8</pseudocode_url>");
			writeStructs(true);
			out.println("</snap>");

			nodes[successorI].setHexColor(HASH);
			edges[currentI][successorI].setHexColor(HASH);
			edges[successorI][currentI].setHexColor(HASH);

			successorT.setHexColor(HASH);
			successorT.getLineToParent().setHexColor(HASH);

			memory_full = true;

			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=9</pseudocode_url>");
			writeStructs(true);

			break;
		    case -1:
			switch(qLast){
			case 2:
			    question_answered = false;
			    break;
			case 7:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(false);
			    question_answered = true;
			    break;
			case 8:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(true);
			    question_answered = true;
			    break;
			}

			nodes[successorI].setHexColor(HASHED);
			edges[currentI][successorI].setHexColor(HASHED);
			edges[successorI][currentI].setHexColor(HASHED);

			successorT.setHexColor(HASHED);
			successorT.getLineToParent().setHexColor(HASHED);

			out.println("</snap>");
			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=8</pseudocode_url>");
			writeStructs(true);
			out.println("</snap>");

			if(successorI == start){
			    nodes[successorI].setHexColor(START);
			    int pred = graph.translateCharIndex
				(nodes[currentI].getPred());
			    if(pred == successorI){
				edges[currentI][successorI].setHexColor(HASH);
				edges[successorI][currentI].setHexColor(HASH);
			    }else{
				edges[currentI][successorI].setHexColor
				    ("#999999");
				edges[successorI][currentI].setHexColor
				    ("#999999");
			    }
			}else{
			    nodes[successorI].setHexColor(HASH);
			    int pred = graph.translateCharIndex
				(nodes[successorI].getPred());
			    if(pred == currentI){
				edges[currentI][successorI].setHexColor(HASH);
				edges[successorI][currentI].setHexColor(HASH);
			    }else{
				pred = graph.translateCharIndex
				    (nodes[currentI].getPred());
				if(pred == successorI){
				    edges[successorI][currentI].setHexColor
					(HASH);
				    edges[currentI][successorI].setHexColor
					(HASH);
				}else{
				    edges[currentI][successorI].setHexColor
					("#999999");
				    edges[successorI][currentI].setHexColor
					("#999999");
				}
			    }
			}

			if(previous == null){
			    successorT = successorT.getSibling();
			    currentT.setChild(successorT);
			}else{
			    successorT = successorT.getSibling();
			    previous.setSibling(successorT);
			}

			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=8</pseudocode_url>");
			writeStructs(true);

			break;
		    default:
			switch(qLast){
			case 2:
			    questions.addQuestion(fibQ);
			    fibQ.setAnswer("" + successor.getChar());
			    fibQ.setAnswer
				("" + 
				 Character.toLowerCase(successor.getChar()));
			    question_answered = true;
			    break;
			case 7:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(true);
			    question_answered = true;
			    break;
			case 8:
			    questions.addQuestion(tfQ);
			    tfQ.setAnswer(true);
			    question_answered = true;
			    break;
			}

			out.println("</snap>");
			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=8</pseudocode_url>");
			writeStructs(true);
			out.println("</snap>");

			hash_table[slot] = successor.getChar();

			successor.setHexColor(NEW_BEAM);
			nodes[successorI] = successor;
			edges[currentI][successorI].setHexColor(NEW_BEAM);
			edges[successorI][currentI].setHexColor(NEW_BEAM);
			beam.add(successor);

			successorT.setHexColor(NEW_BEAM);
			successorT.getLineToParent().setHexColor(NEW_BEAM);

			new_beam_nodes++;

			writeLists("state = " + successor.getChar() + ", ");
			out.println("<pseudocode_url>index.php?line=10</pseudocode_url>");
			writeStructs(true);

			successor.setHexColor(OLD_BEAM);
			nodes[successorI].setHexColor(OLD_BEAM);
			edges[currentI][successorI].setHexColor(OLD_BEAM);
			edges[successorI][currentI].setHexColor(OLD_BEAM);

			successorT.setHexColor(OLD_BEAM);
			successorT.getLineToParent().setHexColor(OLD_BEAM);

			previous = successorT;
			successorT = previous.getSibling();

			break;
		    }
		}else{
		    if(!question_answered && qLast == 2){
			questions.addQuestion(fibQ);
			fibQ.setAnswer("~");
			question_answered = true;
		    }

		    if(findHash(successorI)){
			if(successorI == start){
			    nodes[successorI].setHexColor(START);
			    edges[currentI][successorI].setHexColor("#999999");
			    edges[successorI][currentI].setHexColor("#999999");
			}else{
			    nodes[successorI].setHexColor(HASH);
			    edges[currentI][successorI].setHexColor("#999999");
			    edges[successorI][currentI].setHexColor("#999999");
			    int pred = graph.translateCharIndex
				(nodes[successorI].getPred());
			    if(edges[pred][successorI].getHexColor()!=OLD_SET){
				edges[pred][successorI].setHexColor(HASH);
				edges[successorI][pred].setHexColor(HASH);
			    }
			}
		    }else{
			nodes[successorI].setHexColor("#FFFFFF");
			edges[currentI][successorI].setHexColor("#999999");
			edges[successorI][currentI].setHexColor("#999999");
		    }
		    
		    if(previous == null){
			successorT = successorT.getSibling();
			currentT.setChild(successorT);
		    }else{
			successorT = successorT.getSibling();
			previous.setSibling(successorT);
		    }
		}

		if(memory_full){
		    break;
		}

		if(new_beam_nodes == beam_size){
		    add_to_beam = false;
		}
	    }

	    current_beam_size = beam.size();

	    if(beamQ != null){
		questions.addQuestion(beamQ);
		beamQ.setAnswer("" + current_beam_size);
	    }

	    // Beam search has failed to find a path to the goal.
	    if(memory_full || current_beam_size == 0){
		break;
	    }

	    currentT = makeBeamNode(currentT);

	    for(int n = 0; n < num_nodes; n++){
		String color = nodes[n].getHexColor();

		if(!(color == HASH || color == OLD_BEAM || 
		     color == START || color == GOAL)){
		    nodes[n].setHexColor("#FFFFFF");
		}

		for(int e = 0; e < num_nodes; e++){
		    if(edges[n][e].isActivated()){
			color = edges[n][e].getHexColor();
			
			if(!(color == HASH || color == OLD_BEAM)){
			    edges[n][e].setHexColor("#999999");
			    edges[e][n].setHexColor("#999999");
			}
		    }
		}
	    }

	    for(int hash = 0; hash < memory_size; hash++){
		if(hash_table[hash] != '~'){
		    int n = graph.translateCharIndex(hash_table[hash]);
		    if(nodes[n].getHexColor() != OLD_BEAM){
			if(n == start){
			    nodes[n].setHexColor(START);
			}else{
			    int e = 
				graph.translateCharIndex(nodes[n].getPred());
			
			    nodes[n].setHexColor(HASH);
			    edges[n][e].setHexColor(HASH);
			    edges[e][n].setHexColor(HASH);
			}
		    }
		}
	    }

	    // Display search state when only the beam remains.
	    out.println("</snap>");
	    writeLists("");
	    out.println("<pseudocode_url>index.php?line=11</pseudocode_url>");
	    writeStructs(true);
	    if(!show_detail){
		out.println("</snap>");
	    }
	}

	if(goal_found){
	    for(int n = 0; n < num_nodes; n++){
		for(int e = 0; e < num_nodes; e++){
		    nodes[n].setHexColor("#FFFFFF");

		    if(edges[n][e].isActivated()){
			edges[n][e].setHexColor("#999999");
			edges[e][n].setHexColor("#999999");
		    }
		}
	    }

	    currentI = goal;
	    int predI = graph.translateCharIndex(nodes[currentI].getPred());

	    while(predI != -1 && currentI != start){
		nodes[currentI].setHexColor(GOAL);
		edges[predI][currentI].setHexColor(GOAL);
		edges[currentI][predI].setHexColor(GOAL);

		currentI = predI;
		predI = graph.translateCharIndex(nodes[predI].getPred());
	    }
	    nodes[currentI].setHexColor(GOAL);

	    graph.setBounds(0.0, 0.0, 1.0, 0.95);
	    graph.setFontSize(0.03);
	    tree.setRoot(null);

	    out.println("<snap>");
	    out.println("<title>Beam Search Shortest Path from Start Node " +
			nodes[start].getChar() + " to Goal Node " + 
			nodes[goal].getChar() + ":</title>");
	    out.println("<pseudocode_url>index.php?line=5</pseudocode_url>");
	    writeStructs(false);
	    out.println("</snap>");
	}else if(memory_full){
	    for(int n = 0; n < num_nodes; n++){
		for(int e = 0; e < num_nodes; e++){
		    nodes[n].setHexColor("#FFFFFF");

		    if(edges[n][e].isActivated()){
			edges[n][e].setHexColor("#999999");
			edges[e][n].setHexColor("#999999");
		    }
		}
	    }

	    for(int hash = 0; hash < memory_size; hash++){
		if(hash_table[hash] != '~'){
		    int n = graph.translateCharIndex(hash_table[hash]);
		    int e = graph.translateCharIndex(nodes[n].getPred());
		    
		    nodes[n].setHexColor(HASH);
		    if(e > -1){
			edges[n][e].setHexColor(HASH);
			edges[e][n].setHexColor(HASH);
		    }
		}
	    }

	    nodes[start].setHexColor(START);
	    nodes[goal].setHexColor(GOAL);
	    
	    graph.setBounds(0.0, 0.0, 1.0, 0.95);
	    graph.setFontSize(0.03);
	    tree.setRoot(null);

	    out.println("</snap>");
	    out.println("<snap>");
	    out.println("<title>Beam Search Final State: Memory Full:</title>");
	    out.println("<pseudocode_url>index.php?line=9</pseudocode_url>");
	    writeStructs(false);
	    out.println("</snap>");
	}else{
	    for(int n = 0; n < num_nodes; n++){
		for(int e = 0; e < num_nodes; e++){
		    nodes[n].setHexColor("#FFFFFF");

		    if(edges[n][e].isActivated()){
			edges[n][e].setHexColor("#999999");
			edges[e][n].setHexColor("#999999");
		    }
		}
	    }

	    for(int hash = 0; hash < memory_size; hash++){
		if(hash_table[hash] != '~'){
		    int n = graph.translateCharIndex(hash_table[hash]);
		    int e = graph.translateCharIndex(nodes[n].getPred());
		    
		    nodes[n].setHexColor(HASH);
		    if(e > -1){
			edges[n][e].setHexColor(HASH);
			edges[e][n].setHexColor(HASH);
		    }
		}
	    }

	    nodes[start].setHexColor(START);
	    nodes[goal].setHexColor(GOAL);

	    graph.setBounds(0.0, 0.0, 1.0, 0.95);
	    graph.setFontSize(0.03);
	    tree.setRoot(null);

	    out.println("</snap>");
	    out.println("<snap>");
	    out.println("<title>Beam Search Final State: Beam Empty:</title>");

	    out.println("<pseudocode_url>index.php?line=13</pseudocode_url>");
	    writeStructs(false);
	    out.println("</snap>");
	}
	
	questions.writeQuestionsAtEOSF();
	
	out.println("</show>");
	out.close();
    }

    private void addSet(Vector add_set, Object current)
	throws ArrayIndexOutOfBoundsException{
	int size = add_set.size();

	if(size == 0){
	    add_set.add(current);
	}else{
	    for(int n = 0; n < size; n++){
		if(((VisNode)current).compareTo((VisNode)add_set.get(n)) < 0){
		    add_set.add(n, current);
		    break;
		}else if(n == (size - 1)){
		    add_set.add(current);
		}
	    }
	}
    }

    private void insertNode(TreeNode parent, TreeNode new_child){
	TreeNode current = parent.getChild();

	if(current == null){
	    parent.setChildWithEdge(new_child);
	}else if(((VisNode)new_child).compareTo((VisNode)current) < 0){
	    new_child.setSibling(current);
	    parent.setChild(new_child);
	    new_child.setParent(parent);
	    new_child.setLineToParent(new Edge(parent, new_child));
	}else{
	    TreeNode previous = current;
	    current = previous.getSibling();

	    while(true){
		if(current == null){
		    previous.setSibling(new_child);
		    new_child.setParent(parent);
		    new_child.setLineToParent(new Edge(parent, new_child));
		    break;
		}else if(((VisNode)new_child).compareTo((VisNode)current) < 0){
		    new_child.setSibling(current);
		    previous.setSibling(new_child);
		    new_child.setParent(parent);
		    new_child.setLineToParent(new Edge(parent, new_child));
		    break;
		}else{
		    previous = current;
		    current = previous.getSibling();
		}
	    }
	}
    }

    private int hash(int node){
	int slot = node % memory_size;
	int index = slot;
	char value = nodes[node].getChar();

	while(hash_table[index] != '~'){
	    if(hash_table[index] == value){
		return -1;
	    }

	    index = (index + 1) % memory_size;

	    if(index == slot){
		return -2;
	    }
	}

	return index;
    }

    private boolean findHash(int node){
	int slot = node % memory_size;
	int index = slot;
	char value = nodes[node].getChar();

	while(hash_table[index] != '~'){
	    if(hash_table[index] == value){
		return true;
	    }

	    index = (index + 1) % memory_size;

	    if(index == slot){
		return false;
	    }
	}

	return false;
    }

    private TreeNode makeBeamNode(TreeNode parent){
	TreeNode beam_node = new TreeNode();

	parent.setChild(beam_node);
	beam_node.setParent(parent);
	beam_node.setLineToParent(new Edge(parent, beam_node));

	beam_node.setHexColor(OLD_BEAM);
	beam_node.getLineToParent().setHexColor(OLD_BEAM);

	String value = " ";
	String heuristics = " ";
	int size = beam.size();
	VisNode current;

	for(int i = 0; i < size; i++){
	    try{
		current = (VisNode)beam.get(i);
	    }
	    catch(ArrayIndexOutOfBoundsException doh){
		break;
	    }

	    value += current.getChar() + " ";
	    heuristics += current.getHeuristic() + " ";
	}

	beam_node.setValue("   " + value + "   \n   " + heuristics + "   ");

	return beam_node;
    }

    private void writeLists(String extra){
	int last_index = memory_size - 1;

	out.println("<snap>");
	out.print("<title>hash_table = { ");
	for(int i = 0; i < memory_size; i++){
	    if(hash_table[i] == '~'){
		out.print("_");
	    }else{
		out.print(hash_table[i]);
	    }

	    if(i < last_index){
		out.print(", ");
	    }
	}
	out.println(" }\n" + extra + "g = " + g + ", B = " + beam_size +
		    ", M = " + memory_size + "</title>");
    }

    private void writeStructs(boolean show_key){
	graph.writeGAIGSXMLGraph(out);
	tree.writeGAIGSXMLTree();

	if(show_key){
	    out.println("<array>\n<bounds x1 = \"0.2\" y1 = \"-0.2\" x2 = \"0.82\" y2 = \"0.4\" fontsize = \"0.035\"/>\n<column_label>Color Key</column_label>\n<column>\n<list_item color = \"" + NEW_SET + 
			"\">\n<label>New Set Node</label>\n</list_item>\n<list_item color = \"" + OLD_SET + 
			"\">\n<label>Old Set Node</label>\n</list_item>\n</column>\n<column>\n<list_item color = \"" + NEW_BEAM + 
			"\">\n<label>New Beam Node</label>\n</list_item>\n<list_item color = \"" + OLD_BEAM + 
			"\">\n<label>Old Beam Node</label>\n</list_item>\n</column>\n<column>\n<list_item color = \"" + HASH + 
			"\">\n<label>Node in Hash Table</label>\n</list_item>\n<list_item color = \"" + HASHED + 
			"\">\n<label>Node Already in Hash Table</label>\n</list_item>\n</column>\n<column>\n<list_item color = \"" + START + 
			"\">\n<label>Start Node</label>\n</list_item>\n<list_item color = \"" + GOAL + 
			"\">\n<label>Goal Node</label>\n</list_item>\n</column>\n</array>");
	}
    }
}

