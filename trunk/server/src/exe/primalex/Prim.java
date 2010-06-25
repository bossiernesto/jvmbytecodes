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

/** Creates a visual representation of a Minimum Spanning Tree using Prim's
 *  Algorithm. Usage:
 *  { java Prim dataFile outputFile }
 *  Where dataFile is the .DAT file produced by the prim_data executable 
 *  included in the directory and outputFile is the name of the desired
 *  .SHO file. The .DAT file must contain a table of (x,y)-coordinates for
 *  each node in the graph as well as a list of all connections and edge
 *  weights.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */

package exe.primalex;

import java.io.*;
import java.util.*;
import exe.*;

/** Implementation of a node in a graph used to demonstrate Prim's Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class PNode {
    float x;
    float y;
    boolean add = false;
    boolean open = false;

/** Constructor for <code>PNode</code> object.
 *
 *  @param xPos         Horizontal coordinate; generally between 0 and 1.
 *  @param yPos         Vertical coordinate; generally between 0 and 1.
 */
    PNode(float xPos, float yPos) {
        x = xPos;
        y = yPos;
    }

/** Same as above, with no parameters.
 */
    PNode() {
        x = 0;
        y = 0;
    }
}

/** Implementation of an edge in a graph used to demonstrate Prim's Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class PEdge {
    int left;
    int right;
    int weight;
    boolean add = false;

/** Constructor for <code>PEdge</code> object.
 *
 *  @param l        The number of the node at one end of the edge, arbitrarily 
 *                  designated left, as it appears on screen. Nodes will appear 
 *                  on screen numbered from 1 to <code>nodeArray.length</code>.
 *  @param r        The other end of the edge, designated right.
 *  @param w        The edge weight of the instantiation.
 */
    PEdge(int l, int r, int w) {
        left = l;
        right = r;
        weight = w;
    }

/** Same as above, with no parameters.
 */
    PEdge() {
        left = 0;
        right = 0;
        weight = 0;
    }
}

/** Framework for demonstration of Prim's Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
public class Prim {
    private PrintWriter out;
    private BufferedReader in;
    private String s;
    private StringTokenizer st;
    private questionCollection questions;
    private int qIndex = 0;
    
/** Sets up necessary input and output streams.
 *
 *  @param datFile      Name of the .DAT file from which to read.
 *  @param gaigsFile    Name of the .SHO file to be written.
 *  @throws IOException Error creating file streams.
 */
    public Prim(String datFile, String gaigsFile) {
        try {
            s = "";
            in = new BufferedReader(new FileReader(datFile));
            out = new PrintWriter(new BufferedWriter(new FileWriter(gaigsFile)));
            questions = new questionCollection(out);
            st = new StringTokenizer(s);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Reads data from input file and calls procedure to execute Prim's Algorithm.
 *  File format of .DAT file:
 *  -------------------------
 *  # of nodes   # of connections
 *  GAIGS
 *  1   x-coord of node 1   y-coord of node 1
 *  ...
 *  n   x-coord of node n   y-coord of node n
 *  start node of connection 1   end node of connection 1   weight
 *  ...
 *  start node of connection x   end node of connection x   weight
 *  {EOF}
 *  -------------------------
 *
 *  @param args         Array of command line parameters.
 */
    public static void main(String[] args) {
        try {
            String datFile = args[0];
            String gaigsFile = args[1];
            float xPos, yPos;
            int node1, node2, weight;
            Prim graph = new Prim(datFile, gaigsFile);
            graph.s = graph.in.readLine();
            graph.st = new StringTokenizer(graph.s);
            int nodeNum = Integer.parseInt(graph.st.nextToken());
            int connNum = Integer.parseInt(graph.st.nextToken());
            PNode[] nodeArray = new PNode[nodeNum];
            graph.s = graph.in.readLine();
            for(int i = 0; i < nodeNum; i++) {
                graph.s = graph.in.readLine();
                graph.st = new StringTokenizer(graph.s);
                xPos = Float.parseFloat(graph.st.nextToken());
                yPos = Float.parseFloat(graph.st.nextToken());
                nodeArray[i] = new PNode(xPos, yPos);
            }
            PEdge[] connArray = new PEdge[connNum];
            for(int l = 0; l < connNum; l++) {
                graph.s = graph.in.readLine();
                graph.st = new StringTokenizer(graph.s);
                node1 = Integer.parseInt(graph.st.nextToken());
                node2 = Integer.parseInt(graph.st.nextToken());
                weight = Integer.parseInt(graph.st.nextToken());
                connArray[l] = new PEdge(node1, node2, weight);
            }
            graph.in.close();
            graph.spanTree(nodeArray, connArray);
            graph.questions.writeQuestionsAtEOSF();
            graph.out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Performs manipulations on data in order to demonstrate Prim's Algorithm.
 *  Particular attention must be payed to the boolean properties of the
 *  <code>PNode</code> and <code>PEdge</code> objects, which are used to determine if
 *  each node or edge is currently included in the minimum spanning tree, as
 *  well as those nodes which are connected to nodes which have already been
 *  added to the tree, and are thus in the open state, displayed yellow on
 *  screen.
 *
 *  @param nodeArray        Array of all nodes in the graph.
 *  @param connArray        Array of all edges in the graph.
 */
    void spanTree(PNode[] nodeArray, PEdge[] connArray) {
        int connections = 0;
        PEdge nextEdge = new PEdge();
        Random r = new Random();
        int start = r.nextInt(nodeArray.length);
        printGraph(nodeArray, connArray, "Prim", 0, 0);
        nodeArray[start].add = true;
        int nextNode = start + 1;
        String header = "Prim Start from " + nextNode;
        printGraph(nodeArray, connArray, header, 0, start + 1);
        while(connections < nodeArray.length - 1) {
            nodeArray[nextNode - 1].add = true;
            for(int i = 0; i < connArray.length; i++) {
                if(connArray[i].left == nextNode) {
                    if(!nodeArray[connArray[i].right - 1].add)
                        nodeArray[connArray[i].right - 1].open = true;
                }
                else if(connArray[i].right == nextNode) {
                    if(!nodeArray[connArray[i].left - 1].add)
                        nodeArray[connArray[i].left - 1].open = true;
                }
            }
            for(int j = connArray.length - 1; j >= 0; j--) {
                if(nodeArray[connArray[j].left - 1].add) {
                    if(!nodeArray[connArray[j].right - 1].add)
                            nextEdge = connArray[j];
                }
                else if(nodeArray[connArray[j].right - 1].add) {
                    if(!nodeArray[connArray[j].left - 1].add)
                            nextEdge = connArray[j];
                }
            }
            for(int k = 0; k < connArray.length; k++) {
                if(nodeArray[connArray[k].left - 1].add) {
                    if((!nodeArray[connArray[k].right - 1].add) && 
                       (connArray[k].weight < nextEdge.weight))
                        nextEdge = connArray[k];
                }
                else if(nodeArray[connArray[k].right - 1].add) {
                    if((!nodeArray[connArray[k].left - 1].add) &&
                       (connArray[k].weight < nextEdge.weight))
                        nextEdge = connArray[k];
                }
            }
            nodeArray[nextNode - 1].add = true;
            if(nodeArray[nextEdge.left - 1].add) {
                printGraph(nodeArray, connArray, header, nextEdge.right, start + 1);
                nextNode = nextEdge.right;
            }
            else {
                printGraph(nodeArray, connArray, header, nextEdge.left, start + 1);
                nextNode = nextEdge.left;
            }
            nextEdge.add = true;
            connections++;
        }
        nodeArray[nextNode - 1].add = true;
        printGraph(nodeArray, connArray, header, 0, start + 1);
    }

/** Creates a snapshot of the graph.
 *
 *  @param nodeArray        Array of all nodes in the graph.
 *  @param connArray        Array of all edges in the graph.
 *  @param header           String to display as snapshot title.
 *  @param answer           The integer answer to the snapshot's associated
 *                          question, or 0 if no such question exists.
 *  @param first            The number of the node which was randomly selected
 *                          as the first addition to the Minimum Spanning
 *                          Tree, and is thus colored red. Use 0 before any
 *                          nodes have been added to the tree.
 */
    void printGraph(PNode[] nodeArray, PEdge[] connArray, String header, int answer, int first) {
        out.println("VIEW DOCS prim.htm");
        if(answer != 0) {
            fibQuestion q = new fibQuestion(out, (new Integer(qIndex)).toString());
            q.setQuestionText("What will be the next node closed?");
            q.setAnswer("" + answer);
            questions.addQuestion(q);
            questions.insertQuestion(qIndex);
            qIndex++;
        }
        out.println("Network");
        out.println("" + 1);
        out.println(header);
        out.println("***\\***");
        for(int i = 1; i <= nodeArray.length; i++) {
            out.println("" + i + " " + nodeArray[i - 1].x + " " + nodeArray[i - 1].y);
            for(int j = 0; j < connArray.length; j++) {
                if(connArray[j].left == i) {
                    if(connArray[j].add)
                        out.print("\\R");
                    out.println("" + connArray[j].right);
                    out.println("" + connArray[j].weight);
                }
                else if(connArray[j].right == i) {
                    if(connArray[j].add)
                        out.print("\\R");
                    out.println("" + connArray[j].left);
                    out.println("" + connArray[j].weight);
                }
            }
            out.println("32767");
            if(first == i)
                out.print("\\R");
            else if(nodeArray[i - 1].add)
                out.print("\\G");
            else if(nodeArray[i - 1].open)
                out.print("\\Y");
            out.println("" + i);
        }
        out.println("***^***");
    }
}



            
                    
            
            

            
                        
        
        
            
            
            
            
        

    
    
