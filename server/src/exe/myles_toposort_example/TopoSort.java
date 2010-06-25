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

package exe.myles_toposort_example;

import java.io.*;
import java.util.Scanner;
import exe.*;


/**
 * This example is still under development.
 * 
 * <p>Example Command Line Parameters: "topo.sho", "7", "9"</p>
 * 
 * @author Myles McNally 
 * @version 5/28/06
 */

public class TopoSort {

    static final String title = " ";
    static final String DEFAULT_GRAPH_COLOR = "#444444";
    static final String DEFAULT_QUEUE_COLOR = "#444444";
    static final String ACTIVE_GRAPH_COLOR = "#FF0000";
    static final String REMOVED_GRAPH_COLOR = "#DDDDDD";
            
    static VisEdge[][] edges;
    static VisNode[] nodes;
    static int[] inDegrees;
            
    public static void main(String args[]) throws IOException {
        
   //-------- Initialize the various data structures ------------------------------------------
   
        ShowFile show = new ShowFile(args[0]);
        GAIGSgraph graph = new GAIGSgraph(false, true, false, " ", DEFAULT_GRAPH_COLOR, 0.15, 0.2, 0.65, 0.7, 0.075);
        GAIGSqueue queue = new GAIGSqueue(" ", DEFAULT_QUEUE_COLOR, 0.75, 0.25, 1.25, 0.75, 0.075);
        GAIGSlabel mainTitle = new GAIGSlabel("Topological Sort", 0.15, 0.3, 1.25, 1.3, 0.05);
        GAIGSlabel graphTitle = new GAIGSlabel("Acyclic Directed Graph", 0.15, -0.25, 0.65, 0.25, 0.08);
        GAIGSlabel queueTitle = new GAIGSlabel("Queue of Sorted Values", 0.75, -0.25, 1.25, 0.25, 0.08);
                
   //-------- Create the graph to be sorted -----------
        
        int nodeCount = Integer.parseInt(args[1]);
        int edgeCount = Integer.parseInt(args[2]);
        graph.randomDAcyclicGraph(nodeCount, edgeCount, false, false, 0, 0);
        graph.organizeGraph();
        
        edges = graph.getEdges();
        nodes = graph.getNodes();
        
        inDegrees = new int[nodeCount];
        
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++)
                if (edges[j][i].isActivated()) {
                    inDegrees[i]++;
                    edges[j][i].setHexColor(DEFAULT_GRAPH_COLOR);
                }
//            System.out.println(i + "  " + inDegrees[i]);
        }
        
        for (int i = 0; i < nodeCount; i++)
            nodes[i].setHexColor(DEFAULT_GRAPH_COLOR);        
        
        
        show.writeSnap(title, graph, queue, mainTitle, graphTitle, queueTitle);
        
   //--------  -------------

        for (int n = 0; n < nodeCount; n++) {
            
            // identify next node to be added to sort
            int current = findInDegreeZeroNode();
            nodes[current].setHexColor(ACTIVE_GRAPH_COLOR);
            for (int i = 0; i < nodeCount; i++)
                if (edges[current][i].isActivated())
                    edges[current][i].setHexColor(ACTIVE_GRAPH_COLOR);
            show.writeSnap(title, graph, queue, mainTitle, graphTitle, queueTitle);  
            
            // add it to sort            
            nodes[current].setHexColor(REMOVED_GRAPH_COLOR);
            for (int i = 0; i < nodeCount; i++)
                if (edges[current][i].isActivated())
                    edges[current][i].setHexColor(REMOVED_GRAPH_COLOR); 
            queue.enqueue(nodes[current].getChar());
            show.writeSnap(title, graph, queue, mainTitle, graphTitle, queueTitle); 
            
            resetInDegrees(current);
        }
           
   //-------- Close the show file ---------------------------------------------------------------

         show.close();                    
    }
    
    private static int findInDegreeZeroNode() {
        for (int i = 0; i < inDegrees.length; i++)
            if (inDegrees[i] == 0)
                return i;
        return -1;
    }
    
    private static void resetInDegrees(int current) {
        inDegrees[current] = -1;
        for (int i = 0; i < inDegrees.length; i++)
            if (edges[current][i].isActivated())
                inDegrees[i]--;
    }

}

