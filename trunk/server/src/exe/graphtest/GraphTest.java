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

// file: GraphTest.java
// author: Andrew Jungwirth
// date: 20 July 2005
//
// This class is intended to demonstrate simple use of the VisualGraph.java
// class. The main method accepts a series of command-line arguments that 
// specify the operations to be performed on the graph. The first argument is
// the name of the file to which the resulting animation's showfile is to be
// written. The second argument must be the initial number of nodes to appear
// in the graph. The third argument must be the initial number of edges to 
// appear in the graph. After these arguments, the graph can be modified using
// a series of the following commands (each change to the graph constitutes a
// separate snapshot in the animation):
//  Add Node Command:
//   addx   -> Adds a new node to the graph with the name x (node names must be
//             single letters). This node will have no edges connected to it
//             until the user adds connecting edges. If the node already
//             exists, the graph is unchanged.
//  Add Edge Command:
//   adex,y -> Adds a new edge between node x and node y (node names must be
//             single letters). If the edge already exists or one or both of
//             the specified nodes do not exist, the graph is unchanged.
//  Remove Node Command:
//   remx   -> Removes the node specified by x (node names must be single
//             letters). If the specified node does not exist, the graph is
//             unchanged.
//  Remove Edge Command:
//   rmex,y -> Removes the edge between node x and node y (node names must be
//             single letters). If the edge does not exist or if one or both of
//             the nodes do not exist, the graph is unchanged.
//  Color Node Command:
//   colx,y -> Colors the node specified by x (node names must be single
//             letters) the color specified by y. The value of y must be a 
//             six-digit hex color string representing the color for the node.
//
// Sample graph user input:
//  5 0 addA addB addC addD addE adeA,B adeA,C adeA,D adeB,E adeB,C adeC,D adeE,D rmeA,B colC,FFFF00 remE
//
// Note that the VisualGraph.java class only supports node names of a
// single letter. Also, the initial nodes and edges are randomly
// placed via the class's randomGraph method. After this initial graph
// is generated, it is organized using the Kamada algorithm.  Because
// of the limitations of the randomGraph method, the initial number of
// edges specified should be less than the initial number of nodes.
// This is because randomGraph apparently tries to construct a random
// disconnected graph.  If you want as many or more edges than nodes,
// then add them after the initial numbering for nodes and edges (as
// indicated in the sample input above) or change this program to call
// on VisualGraph's randomConnectedGraph method, which allows the
// number of edges to match or exceed the number of nodes.

package exe.graphtest;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class GraphTest{
    public static void main(String[] args) throws IOException{
	PrintWriter out = new PrintWriter(new FileWriter(args[0]));

	out.println("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
	out.println("<!DOCTYPE show PUBLIC \"-//JHAVE//DTD GAIGS SHO//EN\" \"gaigs_sho.dtd\">");
	//	out.println("<!DOCTYPE show SYSTEM \"gaigs_sho.dtd\">");
	out.println("<show>");

	VisualGraph graph = new VisualGraph();
	graph.setBounds(0.0, 0.0, 1.0, 0.9);
	String key, color;
	boolean error;
	int nodes = (Integer.decode(args[1])).intValue();
	int edges = (Integer.decode(args[2])).intValue();
	int index;
	VisNode[] node_set;

	do{
	    error = false;

	    try{
		graph.randomGraph(nodes, edges, false, false, false, 1.0, 9.0);
	    }
	    catch(RuntimeException e){
		System.err.println("Problem generating random graph: " + 
				   e.toString());
		error = true;
	    }
	    if(!error){
		try{
		    graph.organizeGraph();
		}
		catch(IOException e){
		    System.err.println("Kamada layout error: " + e.toString());
		    error = true;
		}
	    }
	}while(error);

	node_set = graph.getNodes();

	out.println("<snap>");
	out.println("<title>New Random Graph with " + nodes + " Nodes and " +
		    edges + " Edges</title>");
	graph.writeGAIGSXMLGraph(out);
	out.println("</snap>");

	for(int i = 3; i < args.length; i++){
	    key = args[i].substring(0, 3);

	    if(key.equalsIgnoreCase("add")){
		graph.addNode(args[i].charAt(3), "white");
	    }else if(key.equalsIgnoreCase("ade")){
		graph.addEdge(args[i].charAt(3), args[i].charAt(5),
			      0.0, "gray");
	    }else if(key.equalsIgnoreCase("rem")){
		graph.removeNode(args[i].charAt(3));
	    }else if(key.equalsIgnoreCase("rme")){
		graph.removeEdge(args[i].charAt(3), args[i].charAt(5));
	    }else if(key.equalsIgnoreCase("col")){
		index = graph.translateCharIndex(args[i].charAt(3));
		if(index != -1){
		    color = "#" + args[i].substring(args[i].indexOf(',') + 1);
		    node_set[index].setHexColor(color);
		}
	    }

	    if(Character.toLowerCase(key.charAt(0)) == 'a'){
		try{
		    graph.organizeGraph();
		}
		catch(IOException e){
		    System.err.println("Kamada layout error: " + e.toString());
		}
	    }
		
	    out.println("<snap>");
	    out.println("<title>" + args[i] + "</title>");
	    graph.writeGAIGSXMLGraph(out);
	    out.println("</snap>");
	}

	out.println("</show>");
	out.close();
    }
}

