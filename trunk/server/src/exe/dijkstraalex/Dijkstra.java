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

/** Creates a visual representation of Dijkstra's Shortest Path Algorithm,
 *  sequentially finding the shortest path from one random node to all others
 *  in a graph. Usage:
 *  { java Dijkstra datFile gaigsFile }
 *  Where datFile is the .DAT file to be read and gaigsFile is the .SHO file
 *  to create. The datFile must contain information specifying the (x,y)-coord-
 *  inates of each node as well as all connections and edge weights.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 *  @version 2.0
 */

package exe.dijkstraalex;

import java.io.*;
import java.util.*;
import exe.*;

/** Implementation of a node in a graph used to demonstrate Dijkstra's
 *  Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class DNode {
    int path;
    int ident;
    float x, y;
    DEdge parentPath;
    DNode parentNode;
    boolean computed;
    boolean open;

/** Constructor for the <code>DNode</code> object.
 *
 *  @param xPos         The horizontal coordinate of the node, generally
 *                      between 0 and 1.
 *  @param yPos         The vertical coordinate of the node, generally
 *                      between 0 and 1.
 *  @param i            An integer which uniquely identifies the node, for
 *                      printing purposes.
 */
    DNode(float xPos, float yPos, int i) {
        x = xPos;
        y = yPos;
        ident = i;
        path = 0;
        parentPath = null;
        parentNode = null;
        computed = false;
        open = false;
    }
}

/** Implementation of an edge in a graph used to demonstrate Dijkstra's
 *  Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
class DEdge {
    DNode left;
    DNode right;
    int weight;
    boolean currentPath;

/** Constructor for the <code>DEdge</code> object.
 *
 *  @param l            One endpoint of the edge, arbitrarily designated
 *                      left.
 *  @param r            The other endpoint of the edge, arbitrarily desig-
 *                      nated right.
 *  @param w            The edge weight of the connection from <code>l</code> to <code>r</code>.
 */
    DEdge(DNode l, DNode r, int w) {
        left = l;
        right = r;
        weight = w;
        currentPath = false;
    }
}

/** Framework for the demonstration of Dijkstra's Algorithm.
 *
 *  @author Tom Naps
 *  @author Alex Zimmerman
 */
public class Dijkstra {
    private PrintWriter out;
    private BufferedReader in;
    private String s;
    private StringTokenizer st;
    private questionCollection questions;
    private int qIndex = 0;

/** Creates necessary input and output streams.
 *
 *  @param datFile          Name of the .DAT file from which to read.
 *  @param gaigsFile        Name of the .SHO file to be written.
 *  @throws IOException     Error creating file streams.
 */
    public Dijkstra(String datFile, String gaigsFile) {
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

/** Reads data from input file and calls appropriate procedure to demonstrate
 *  Dijkstra's Algorithm.
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
 *  @param args             Array of command line arguments.
 *  @throws IOException     Error manipulating file streams.
 */
    public static void main(String[] args) {
        try {
            String datFile = args[0];
            String gaigsFile = args[1];
            float xPos, yPos;
            int node1, node2, weight;
            Dijkstra graph = new Dijkstra(datFile, gaigsFile);
            graph.s = graph.in.readLine();
            graph.st = new StringTokenizer(graph.s);
            int nodeNum = Integer.parseInt(graph.st.nextToken());
            int connNum = Integer.parseInt(graph.st.nextToken());
            DNode[] nodeArray = new DNode[nodeNum];
            graph.s = graph.in.readLine();
            for(int i = 0; i < nodeNum; i++) {
                graph.s = graph.in.readLine();
                graph.st = new StringTokenizer(graph.s);
                xPos = Float.parseFloat(graph.st.nextToken());
                yPos = Float.parseFloat(graph.st.nextToken());
                nodeArray[i] = new DNode(xPos, yPos, i + 1);
            }
            DEdge[] connArray = new DEdge[connNum];
            for(int j = 0; j < connNum; j++) {
                graph.s = graph.in.readLine();
                graph.st = new StringTokenizer(graph.s);
                node1 = Integer.parseInt(graph.st.nextToken());
                node2 = Integer.parseInt(graph.st.nextToken());
                weight = Integer.parseInt(graph.st.nextToken());
                connArray[j] = new DEdge(nodeArray[node1 - 1], nodeArray[node2 - 1], weight);
            }
            graph.in.close();
            graph.spanTree(nodeArray, connArray);
            graph.questions.writeQuestionsAtEOSF();
            graph.out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

/** Performs manipulations on data needed to demonstrate Dijkstra's Algorithm.
 *  Particular attention must be payed to the boolean properties of the <code>DNode</code>
 *  and <code>DEdge</code> objects, which are used to determine if each node is currently
 *  included in the tree or has an approximate path weight, or if an edge is
 *  used in connecting any given node to the start node.
 *
 *  @param nodeArray        Array of all nodes in the graph.
 *  @param connArray        Array of all edges in the graph.
 */
    void spanTree(DNode[] nodeArray, DEdge[] connArray) {
        Random r = new Random();
        DEdge least;
        DNode holder;
        int start = r.nextInt(nodeArray.length);
        nodeArray[start].computed = true;
        printGraph(nodeArray, connArray, 0, start + 1);
        for(int i = 0; i < nodeArray.length - 1; i++) {
            for(int j = 0; j < connArray.length; j++) {
                if(connArray[j].left.computed && !connArray[j].right.computed) {
                    if(connArray[j].right.open) {
                        if((connArray[j].weight + connArray[j].left.path) < 
                                (connArray[j].right.path)) {
                            connArray[j].right.path = connArray[j].left.path + 
                                connArray[j].weight;
                            connArray[j].right.parentPath = connArray[j];
                            connArray[j].right.parentNode = connArray[j].left;
                        }
                    }
                    else {
                        connArray[j].right.open = true;
                        connArray[j].right.path = connArray[j].left.path + connArray[j].weight;
                        connArray[j].right.parentPath = connArray[j];
                        connArray[j].right.parentNode = connArray[j].left;
                    }
                }
                else if(connArray[j].right.computed && !connArray[j].left.computed) {
                    if(connArray[j].left.open) {
                        if((connArray[j].weight + connArray[j].right.path) <
                                (connArray[j].left.path)) {
                            connArray[j].left.path = connArray[j].right.path +
                                connArray[j].weight;
                            connArray[j].left.parentPath = connArray[j];
                            connArray[j].left.parentNode = connArray[j].right;
                        }
                    }
                    else {
                        connArray[j].left.open = true;
                        connArray[j].left.path = connArray[j].right.path + connArray[j].weight;
                        connArray[j].left.parentPath = connArray[j];
                        connArray[j].left.parentNode = connArray[j].right;
                    }
                }
            }
            holder = nodeArray[0];
            for(int z = nodeArray.length - 1; z >= 0; z--)
                if(nodeArray[z].open && !nodeArray[z].computed)
                    holder = nodeArray[z];
            for(int k = 0; k < nodeArray.length; k++) {
                if((nodeArray[k].open && !nodeArray[k].computed) &&
                   (nodeArray[k].path < holder.path))
                    holder = nodeArray[k];
            }
            least = holder.parentPath;
            if(least.left.computed)
                printGraph(nodeArray, connArray, least.right.ident, start + 1);
            else
                printGraph(nodeArray, connArray, least.left.ident, start + 1);
            holder = new DNode(0,0,0);
            for(int m = 0; m < connArray.length; m++)
                connArray[m].currentPath = false;
            if(least.left.computed) {
                holder = least.right;
                least.right.computed = true;
            }
            else {
                holder = least.left;
                least.left.computed = true;
            }
            while(holder != nodeArray[start]) {
                holder.parentPath.currentPath = true;
                holder = holder.parentNode;
            }
            if(i == nodeArray.length - 2)
                printGraph(nodeArray, connArray, 0, start + 1);
        }
    }

/** Creates a snapshot of the graph.
 *
 *  @param nodeArray        Array of all nodes in the graph.
 *  @param connArray        Array of all edges in the graph.
 *  @param answer           The answer to the question associated with this
 *                          snapshot. Use 0 if snapshot has no question.
 *  @param start            The integer associated with the node randomly
 *                          chosen as the starting point for the demonstration.
 */
    void printGraph(DNode[] nodeArray, DEdge[] connArray, int answer, int start) {
        out.println("VIEW DOCS shrtpath.htm");
        if(answer != 0) {
            fibQuestion q = new fibQuestion(out, (new Integer(qIndex)).toString());
            q.setQuestionText("What will be the next node closed?");
            q.setAnswer("" + answer);
            questions.addQuestion(q);
            questions.insertQuestion(qIndex);
            qIndex++;
        }
        out.println("Network");
        out.println("" + 2);
        out.println("Find shortest path from " + start + " to all other nodes.");
        out.println("***\\***");
        for(int i = 1; i <= nodeArray.length; i++) {
            out.println("" + i + " " + nodeArray[i - 1].x + " " + nodeArray[i - 1].y);
            for(int j = 0; j < connArray.length; j++) {
                if(connArray[j].left == nodeArray[i - 1]) {
                    if(connArray[j].currentPath)
                        out.print("\\R");
                    out.println("" + connArray[j].right.ident);
                    out.println("" + connArray[j].weight);
                }
                else if(connArray[j].right == nodeArray[i - 1]) {
                    if(connArray[j].currentPath)
                        out.print("\\R");
                    out.println("" + connArray[j].left.ident);
                    out.println("" + connArray[j].weight);
                }
            }
            out.println("32767");
            if(i == start)
                out.print("\\R");
            else if(nodeArray[i - 1].computed)
                out.print("\\G");
            else if(nodeArray[i - 1].open)
                out.print("\\Y");
            out.println("" + i);
            if(nodeArray[i - 1].path == 0)
                out.println("*");
            else
                out.println("" + nodeArray[i - 1].path);
        }
        out.println("***^***");
    }
}
            
            
        



                
                
            
            
            
                        
