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

package exe.primrichard;

import java.io.*;   //Now we can use simple names for all classes in java.io
//      Notably, the PrintWriter class
import java.lang.*; //Now we can use simple names all classes in java.lang,
//      Notably, the Character class
import java.math.*; //Now we can use simple names for all math classes
//      Notably, for random numbers
import java.util.*; // Allows use of vectors and enumerations
import java.awt.*;          //For graphics
import java.awt.event.*;    //For event listeners

import exe.*;       // added for gaigs question classes

/**
 *
 * A class to create visual graphs using Animal/JHAVE
 * This class is meant to be used as an educational tool
 * Vertices can have any unique alphanumeric (A-Z, a-z, 0-9) value in them
 * Therefore, there is a maximum of 62 vertices available, and
 *    a maximum of 3844 edges
 *
 * @author Jeff Lucas
 * @version March 3rd, 2003
 */

public class VisualGraph {
    //Various constants for the class
    public static final int MAX_NODES = 62;     //Constant # of nodes
    private static final int NODE_SIZE = 15;    //Constant node size
    private static final int ITERLIM1 = 600;    //Limits on Kamada iteration
    private static final int ITERLIM2 = 60;
    private static final double EP1 = 0.00001;   //Epsilon values
    private static final double EP2 = 0.000001;
    private static final int K = 1;             //Constant for Kamada
    private static final int BIG_NUM = Integer.MAX_VALUE / 2 - 1;
    //private static final int BIG_NUM = Integer.MAX_VALUE - MAX_NODES - 1;
    //Big # to use for Kamada
    
    // static variables added for gaigs adaptations by Richard
    private static final double NODE_TOEDGE_MIN = 0.015;
    private static final double EDGE_CENTER_MIN = 0.04;
    private static final double EDGE_LENGTH_MIN = 0.09;
    
    
    // Data members of the class
    protected VisNode[] my_nodeset;   // The set of nodes used by the graph
    protected VisEdge[][] my_edgeset; // The set of edges used by the graph
    protected int num_nodes;          // The number of nodes in the graph
    protected int num_edges;          // The number of edges in the graph
    private boolean weighted;         // Is the graph weighted or not?
    private boolean directed;         // Is the graph directed or not?
    private boolean isChanged;        // Has the graph been changed?
    private boolean isConcurrent;     // Is ANIMAL doing concurrent drawing?
    private boolean isXML;            // Are we outputting the file in XML?
    private boolean isFirst;          // Is this the first frame for XML
    // animation printing?
    private PrintWriter my_fout;      // The output file for ANIMAL
    private int my_node_radius;   // The radius of a panel node, a good constant
    
    //Visual-interface related jazz
    //private VisualGraphFrame my_frame;  //This is used for visual interfaces
    //public boolean isInterfaceOpen;     //This is used to tell if the interface
    // is open or not
    
    /*************************************************************************/
    /**                         Visual Interface                            **/
    /*************************************************************************/
    
    /** Function:  OpenInterface
     *  Pre:  Nothing
     *  Post: Opens a visual interface for Visual Graph
     *        the graph currently stored by Visual Graph                 */
    /*
    public void OpenInterface()
    {
        my_frame.show();
        isInterfaceOpen = true;
    }
     */
    
    /** Function:  CloseInterface
     *  Pre:  Nothing
     *  Post: Closes any open visual interface for Visual Graph          */
    /*
    public void CloseInterface()
    {
        my_frame.hide();
        isInterfaceOpen = false;
    }
     */
    
    /*************************************************************************/
    /**                     END Visual Interface                            **/
    /*************************************************************************/
    
    /*************************************************************************/
    /**                           CONSTRUCTORS                              **/
    /*************************************************************************/
    
    
    /** Vanilla VisualGraph Contructor
     *  Pre:  Takes a printwriter for output, for ANIMAL use
     *        Also takes a boolean that tells if we're in XML or not
     *  Post: Makes a plain vanilla empty graph, to be user-modded  */
    public VisualGraph(PrintWriter fout, boolean am_i_XML) {
        initializeGraph();      //The graph should be clear to start
        isConcurrent = false;   //We haven't started concurrent yet
        my_fout = fout;         //Assign the print writer
        isXML = am_i_XML;       //Assign the XML-output variable
        
        //Set up the visual interface
        //my_frame = new VisualGraphFrame(this, fout);
        
        //Print the header in the ANIMAL file
        if(!am_i_XML) {
            my_fout.println("% Animal 2 600 * 600");
            my_fout.println("title \"A Graph Animation for ANIMAL created by " +
                    "Jeff Lucas' VisualGraph Java class.\"");
            my_fout.println("author \"VisualGraph written by Jeff Lucas, " +
                    "(enstrim@aol.com, enstrim@yahoo.com)\"");
        }
        //Otherwise, print a header for the XML file
        else {
            my_fout.println("<?xml version = \"1.0\" standalone = \"no\"?>");
            my_fout.println("<!DOCTYPE graphAnimation SYSTEM " +
                    "\"GraphAnimation.dtd\">");
            my_fout.println("<graphAnimation>");
            isFirst = true;
        }
    }
    
    
    
    /** Buffered Reader VisualGraph Contructor
     *  Pre:  Takes a printwriter for output, for ANIMAL use
     *        Also takes a buffered reader to get an input graph from
     *        Also takes a boolean am_XML that says if the animation is XML
     *  Post: Holds the graph from the input buffer reader             */
    public VisualGraph(BufferedReader fin, PrintWriter fout, boolean am_XML)
    throws IOException {
        initializeGraph();      //The graph should be clear to start
        loadGraph(fin);         //Load the graph from the buffered reader
        isConcurrent = false;   //We haven't started concurrent yet
        my_fout = fout;         //Assign the print writer
        isXML = am_XML;         //Assign the XML-output variable
        //my_frame = new VisualGraphFrame(this, fout);   //Set up the visual interface
        
        //Print the header in the ANIMAL file
        if(!isXML) {
            my_fout.println("% Animal 2 600 * 600");
            my_fout.println("title \"A Graph Animation for ANIMAL created by " +
                    "Jeff Lucas' VisualGraph Java class.\"");
            my_fout.println("author \"VisualGraph written by Jeff Lucas, " +
                    "(enstrim@aol.com, enstrim@yahoo.com)\"");
        }
        //Otherwise, print a header for the XML file
        else {
            my_fout.println("<?xml version = \"1.0\" standalone = \"no\"?>");
            my_fout.println("<!DOCTYPE graphAnimation SYSTEM " +
                    "\"GraphAnimation.dtd\">");
            my_fout.println("<graphAnimation>");
            isFirst = true;
        }
    }
    
    
    
    /** File Reader VisualGraph Contructor
     *  Pre:  Takes a printwriter for output, for ANIMAL use
     *        Also takes a string filename to get an input graph from
     *        Also takes a boolean am_XML that says if the animation is XML
     *  Post: Holds the graph from the input file from the string      */
    public VisualGraph(String filename, PrintWriter fout, boolean am_XML)
    throws IOException {
        initializeGraph();      //The graph should be clear to start
        loadGraph(filename);    //Load the graph from the file "filename"
        isConcurrent = false;   //We haven't started concurrent yet
        my_fout = fout;         //Assign the print writer
        isXML = am_XML;         //Assign the XML-output variable
        //my_frame = new VisualGraphFrame(this, fout);   //Set up the visual interface
        
        //Print the header in the ANIMAL file
        if(!isXML) {
            my_fout.println("% Animal 2 600 * 600");
            my_fout.println("title \"A Graph Animation for ANIMAL created by " +
                    "Jeff Lucas' VisualGraph Java class.\"");
            my_fout.println("author \"VisualGraph written by Jeff Lucas, " +
                    "(enstrim@aol.com, enstrim@yahoo.com)\"");
        }
        //Otherwise, print a header for the XML file
        else {
            my_fout.println("<?xml version = \"1.0\" standalone = \"no\"?>");
            my_fout.println("<!DOCTYPE graphAnimation SYSTEM " +
                    "\"GraphAnimation.dtd\">");
            my_fout.println("<graphAnimation>");
            isFirst = true;
        }
    }
    
    
    /**Function:  initializeGraph
     * Pre:  Nothing
     * Post: Initializes the nodeset and edgeset arrays so that
     *       their values aren't all nulls, and clears the graph         */
    private void initializeGraph() {
        my_nodeset = new VisNode[MAX_NODES];
        my_edgeset = new VisEdge[MAX_NODES][MAX_NODES];
        for(int x = 0; x < MAX_NODES; x++) {
            my_nodeset[x] = new VisNode();
            for(int y = 0; y < MAX_NODES; y++)
                my_edgeset[x][y] = new VisEdge();
        }
        clearGraph();
    }
    
    
    /*************************************************************************/
    /**                    GRAPH-EDITING FUNCTIONS                          **/
    /*************************************************************************/
    
    /**Function:  addNode
     * Pre:  Takes a location on a [0,1] plane (my_x, my_y) for a node,
     *       a color for a node (r, g, b), a character for a node c,
     *       and a highlight for a node highlighted.
     * Post:  Adds a node to our graph with the above qualities
     * Note:  If the node with character 'c' is already activated, this
     *        function will return without doing anything.               */
    public void addNode(char c, double my_x, double my_y, String my_col,
            boolean highl) {
        int my_index = translateCharIndex(c);     //Get the new node's index
        
        //If the node is already activated, we shouldn't change it.
        if(my_nodeset[my_index].isActivated()) return;
        
        //Set the node's color, position, highlight and character
        my_nodeset[my_index].setColor(my_col);
        my_nodeset[my_index].setX(my_x);
        my_nodeset[my_index].setY(my_y);
        my_nodeset[my_index].setChar(c);
        if(highl) my_nodeset[my_index].setHighlighted();
        else my_nodeset[my_index].setUnhighlighted();
        
        //Activate node and increase number of nodes we have
        my_nodeset[my_index].activate();
        num_nodes++;
    }
    
    
    
    /**Function:  addNode
     * Pre:  Takes a color for a node my_col, a character for a
     *       node c, and a highlight for a node highlighted.
     * Post:  Adds a node to our graph with the above qualities
     * Note:  If the node with character 'c' is already activated, this
     *        function will return without doing anything.                */
    public void addNode(char c, String my_col, boolean highl) {
        int my_index = translateCharIndex(c);     //Get the new node's index
        
        //If the node is already activated, we shouldn't change it.
        if(my_nodeset[my_index].isActivated()) return;
        
        //Set the node's color, character, & highlighting
        my_nodeset[my_index].setColor(my_col);
        my_nodeset[my_index].setChar(c);
        if(highl) my_nodeset[my_index].setHighlighted();
        else my_nodeset[my_index].setUnhighlighted();
        
        //Activate the node, increase the number of nodes we have, and
        //make sure "isChanged" is true, so we determine the node's position
        my_nodeset[my_index].activate();
        num_nodes++;
        isChanged = true;
    }
    
    
    /**Function: addEdge
     * Pre:  Takes a char for a node to start at 'start', a char for
     *       a node to end at 'end', a weight, whether the edge has
     *       highlighting or not, and a color for the edge my_col
     * Post: Assigns the values given to the edge between node
     *       'start' and node 'end' and activates the edge
     * Note:  If the edge the user requests here is already activated,
     *        this function will return without doing anything.          */
    public void addEdge(char start, char end, double weight, boolean highl,
            String my_col) {
        //Get the new edge's indices
        int s_ind = translateCharIndex(start);
        int e_ind = translateCharIndex(end);
        
        //If the edge is already activated, we shouldn't change it.
        if(my_edgeset[s_ind][e_ind].isActivated()) return;
        
        //Set the edge's color, weight, & hightlight;
        my_edgeset[s_ind][e_ind].setColor(my_col);
        my_edgeset[s_ind][e_ind].setWeight(weight);
        if(highl) my_edgeset[s_ind][e_ind].setHighlighted();
        else my_edgeset[s_ind][e_ind].setUnhighlighted();
        
        //Set the location for the edge ends
        my_edgeset[s_ind][e_ind].setSX(my_nodeset[s_ind].getX());
        my_edgeset[s_ind][e_ind].setSY(my_nodeset[s_ind].getY());
        my_edgeset[s_ind][e_ind].setEX(my_nodeset[e_ind].getX());
        my_edgeset[s_ind][e_ind].setEY(my_nodeset[e_ind].getY());
        
        //If the graph is not directed, add the opposite edge as well
        if(!directed) {
            //Set the edge's color, weight, & highlight; activate!
            my_edgeset[e_ind][s_ind].setColor(my_col);
            my_edgeset[e_ind][s_ind].setWeight(weight);
            if(highl) my_edgeset[e_ind][s_ind].setHighlighted();
            else my_edgeset[e_ind][s_ind].setUnhighlighted();
            my_edgeset[e_ind][s_ind].activate();
            
            //Set the location for the edge ends
            my_edgeset[e_ind][s_ind].setSX(my_nodeset[e_ind].getX());
            my_edgeset[e_ind][s_ind].setSY(my_nodeset[e_ind].getY());
            my_edgeset[e_ind][s_ind].setEX(my_nodeset[s_ind].getX());
            my_edgeset[e_ind][s_ind].setEY(my_nodeset[s_ind].getY());
        }
        
        //Make sure "isChanged" is true, so we determine the edge's position
        //Increase the number of edges we have, activate the edge
        isChanged = true;
        num_edges++;
        my_edgeset[s_ind][e_ind].activate();
    }
    
    
    
    /**Function: removeNode
     * Pre:  Takes a char for a node to remove 'c'
     * Post: If the node is activated, removes said node from the graph,
     *       clearing it's data.  Also removes all edges connected to
     *       that node from the graph.
     * Note: If the node is deactivated, there is no need to remove it,
     *       so this function simply returns                          */
    public void removeNode(char c) {
        int index = translateCharIndex(c);  //Get the node's index
        
        //If the node is already deactivated, we needn't change it.
        if(!my_nodeset[index].isActivated()) return;
        
        my_nodeset[index].clearNode();      //Clear the node data
        my_nodeset[index].deactivate();     //Deactivate the node
        
        //Go through each edge that was connected to the node,
        //Clearing the data and deactivating each one!
        //Only bother with the already-activated edges!
        for(int endex = 0; endex < MAX_NODES; endex++)
            if(my_edgeset[index][endex].isActivated()) {
            my_edgeset[index][endex].clearEdge();
            my_edgeset[index][endex].deactivate();
            
            //If the graph is not directed, we have to remove both edges
            if(!directed) {
                my_edgeset[endex][index].clearEdge();
                my_edgeset[endex][index].deactivate();
            }
            
            //Decrease the number of edges we have
            num_edges--;
            }
        
        //Decrease the number of nodes we have
        num_nodes--;
    }
    
    
    
    /**Function:  removeEdge
     * Pre:  Takes a char for a starting node 'start' and a
     *       char for an ending node 'end' for an edge
     * Post: If the said edge is activated, removes it from the graph,
     *       clearing it's data
     * Note: If the said is already deactivated, the function simply returns
     */
    public void removeEdge(char start, char end) {
        //Get the edge's indices
        int s_ind = translateCharIndex(start);
        int e_ind = translateCharIndex(end);
        
        //If the edge is already deactivated, we shouldn't change it.
        if(!my_edgeset[s_ind][e_ind].isActivated()) return;
        
        my_edgeset[s_ind][e_ind].clearEdge();   //Clear the edge
        my_edgeset[s_ind][e_ind].deactivate();  //Deactivate the edge
        
        //If the graph is not directed, we have to remove both edges
        if(!directed) {
            my_edgeset[e_ind][s_ind].clearEdge();
            my_edgeset[e_ind][s_ind].deactivate();
        }
        
        //Decrease the number of edges we have
        num_edges--;
    }
    
    
    /*************************************************************************/
    /**                      TOGGLE NODE/EDGE ATTRIBUTES                    **/
    /*************************************************************************/
    
    /**Function:  setNodeColor
     * Pre:  Takes a character for a node c and a color my_col to set
     * Post: Changes the node color appropriately
     * Note: If the node isn't activated, nothing is changed             */
    public void setNodeColor(char c, String my_col) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //If the node isn't activated, we shouldn't change it.
        if(!my_nodeset[my_index].isActivated()) return;
        
        //Set the node's color
        my_nodeset[my_index].setColor(my_col);
    }
    
    
    /**Function:  setEdgeColor
     * Pre:  Takes two characters for nodes start and end and a
     *       color to set the edge between these two nodes to, my_col
     * Post: Changes the edge color appropriately...
     * Note: If the edge isn't activated, nothing is changed          */
    public void setEdgeColor(char start, char end, String my_col) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, we shouldn't change it.
        if(!my_edgeset[my_start][my_end].isActivated()) return;
        
        //Set the edge's color
        my_edgeset[my_start][my_end].setColor(my_col);
    }
    
    
    /**Function:  highlightNodeToggle
     * Pre:  Takes a node with character c to toggle highlighting
     * Post: Toggles highlighting for the node recognized by char c
     * Note: If the node isn't activated, nothing is changed     */
    public void highlightNodeToggle(char c) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //If the node isn't activated, we shouldn't change it.
        if(!my_nodeset[my_index].isActivated()) return;
        
        //Toggle the node's highlighting
        my_nodeset[my_index].toggleHighlighted();
    }
    
    
    /**Function:  highlightNode
     * Pre:  Takes a node with character c
     * Post: Sets highlighting for the node recognized by char c
     * Note: If the node isn't activated, nothing is changed      */
    public void highlightNode(char c) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //If the node isn't activated, we shouldn't change it.
        if(!my_nodeset[my_index].isActivated()) return;
        
        //Toggle the node's highlighting
        my_nodeset[my_index].setHighlighted();
    }
    
    
    /**Function:  unhighlightNode
     * Pre:  Takes a node with character c
     * Post: Removes highlighting for the node recognized by char c
     * Note: If the node isn't activated, nothing is changed        */
    public void unhighlightNode(char c) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //If the node isn't activated, we shouldn't change it.
        if(!my_nodeset[my_index].isActivated()) return;
        
        //Toggle the node's highlighting
        my_nodeset[my_index].setUnhighlighted();
    }
    
    
    /**Functon:  highlightEdgeToggle
     * Pre:  Takes two characters, start and end
     * Post: Toggle highlighting on the appropriate edge
     * Note: If the edge isn't activated, nothing is changed      */
    public void highlightEdgeToggle(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, we shouldn't change it.
        if(!my_edgeset[my_start][my_end].isActivated()) return;
        
        //Toggle the edge's highlighting
        my_edgeset[my_start][my_end].toggleHighlighted();
    }
    
    
    /**Functon:  highlightEdge
     * Pre:  Takes two characters, start and end
     * Post: Set highlighting on the appropriate edge
     * Note: If the edge isn't activated, nothing is changed     */
    public void highlightEdge(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, we shouldn't change it.
        if(!my_edgeset[my_start][my_end].isActivated()) return;
        
        //Toggle the edge's highlighting
        my_edgeset[my_start][my_end].setHighlighted();
    }
    
    
    /**Functon:  unhighlightEdge
     * Pre:  Takes two characters, start and end
     * Post: Removes highlighting on the appropriate edge
     * Note: If the edge isn't activated, nothing is changed      */
    public void unhighlightEdge(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, we shouldn't change it.
        if(!my_edgeset[my_start][my_end].isActivated()) return;
        
        //Toggle the edge's highlighting
        my_edgeset[my_start][my_end].setUnhighlighted();
    }
    
    
    /**Functon:  setEdgeWeight
     * Pre:  Takes two characters, start and end
     * Post: Set the weight for the appropriate edge
     * Note: If the edge isn't activated, nothing is changed    */
    public void setEdgeWeight(char start, char end, double new_weight) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, we shouldn't change it.
        if(!my_edgeset[my_start][my_end].isActivated()) return;
        
        //Toggle the edge's bidirectionality
        my_edgeset[my_start][my_end].setWeight(new_weight);
    }
    
    
    /**Functon:  setNodePos
     * Pre:  Takes a real position (x,y) in ([0,1],[0,1])
     * Post: Sets the position of the node to (x,y)
     * Note: If the node isn't activated, nothing is changed    */
    public void setNodePos(char index, double x, double y) {
        //If the node isn't activated, simply return
        if(!my_nodeset[translateCharIndex(index)].isActivated()) return;
        
        //Otherwise, set the new position
        my_nodeset[translateCharIndex(index)].setX(x);
        my_nodeset[translateCharIndex(index)].setY(y);
        
        //Now, set any relevant new edge positions
        for(int z = 0; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated()) {
            if(my_edgeset[translateCharIndex(index)][z].isActivated()) {
                my_edgeset[translateCharIndex(index)][z].setSX(
                        my_nodeset[translateCharIndex(index)].getX());
                my_edgeset[translateCharIndex(index)][z].setSY(
                        my_nodeset[translateCharIndex(index)].getY());
            }
            if(my_edgeset[z][translateCharIndex(index)].isActivated()) {
                my_edgeset[z][translateCharIndex(index)].setEX(
                        my_nodeset[translateCharIndex(index)].getX());
                my_edgeset[z][translateCharIndex(index)].setEY(
                        my_nodeset[translateCharIndex(index)].getY());
            }
            }
    }
    
    
    /**Functon:  setLimitedNodePos
     * Pre:  Takes a real position (x,y) in ([0,1],[0,1])
     * Post: Sets the position of the node to (x,y)
     * Note: If the node isn't activated, nothing is changed
     * Note2: The values for x, y are limited to [0,1]          */
    public void setLimitedNodePos(char index, double x, double y) {
        //If the node isn't activated, simply return
        if(!my_nodeset[translateCharIndex(index)].isActivated()) return;
        
        //Otherwise, set the new position
        my_nodeset[translateCharIndex(index)].setLimitedX(x);
        my_nodeset[translateCharIndex(index)].setLimitedY(y);
        
        //Now, set any relevant new edge positions
        for(int z = 0; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated()) {
            if(my_edgeset[translateCharIndex(index)][z].isActivated()) {
                my_edgeset[translateCharIndex(index)][z].setSX(
                        my_nodeset[translateCharIndex(index)].getX());
                my_edgeset[translateCharIndex(index)][z].setSY(
                        my_nodeset[translateCharIndex(index)].getY());
            }
            if(my_edgeset[z][translateCharIndex(index)].isActivated()) {
                my_edgeset[z][translateCharIndex(index)].setEX(
                        my_nodeset[translateCharIndex(index)].getX());
                my_edgeset[z][translateCharIndex(index)].setEY(
                        my_nodeset[translateCharIndex(index)].getY());
            }
            }
    }
    
    
    
    /*************************************************************************/
    /**                    QUERY NODE/EDGE ATTRIBUTES                       **/
    /*************************************************************************/
    
    /**Function:  nodeHighlighted
     * Pre:  Takes a character c
     * Post:  Finds the status (highlight/non-highlighted) of the
     *        node that corresponds to character c
     * Note:  Always returns false for nodes that are not activated      */
    public boolean nodeHighlighted(char c) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //If the node isn't activated, it is not highlighted
        if(!my_nodeset[my_index].isActivated()) return false;
        
        //Return the node's highlighting
        return my_nodeset[my_index].isHighlighted();
    }
    
    
    
    /**Functon:  edgeHighlighted
     * Pre:  Takes two characters, start and end, that define an edge
     * Post: Returns whether the edge in question is highlighted
     * Note:  Always returns false for edges that are not activated      */
    public boolean edgeHighlighted(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, it is not highlighted
        if(!my_edgeset[my_start][my_end].isActivated()) return false;
        
        //Return the edge's highlighting
        return my_edgeset[my_start][my_end].isHighlighted();
    }
    
    
    
    /**Functon:  edgeWeight
     * Pre:  Takes two characters, start and end that define an edge
     * Post: Returns the weight of the edge in question
     * Note:  Always returns 0.0 for edges that are not activated     */
    public double edgeWeight(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //If the edge isn't activated, it is not directed
        if(!my_edgeset[my_start][my_end].isActivated()) return 0.0;
        
        //Return the edge's directedness
        return my_edgeset[my_start][my_end].getWeight();
    }
    
    
    
    /**Functon:  edgeExists
     * Pre:  Takes two characters, start and end
     * Post:  Returns whether the edge that connects the node with
     *        char "start" and the node with char "end" is activated   */
    public boolean edgeExists(char start, char end) {
        //Get the edge's indices
        int my_start = translateCharIndex(start);
        int my_end = translateCharIndex(end);
        
        //Return whether the edge exists or not
        return my_edgeset[my_start][my_end].isActivated();
    }
    
    
    
    /**Function:  nodeExists
     * Pre:  Takes a character c
     * Post:  Returns whether the node that corresponds to
     *        character c is activated or not                         */
    public boolean nodeExists(char c) {
        //Get the node's index
        int my_index = translateCharIndex(c);
        
        //Return whether the node is activated
        return my_nodeset[my_index].isActivated();
    }
    
    /** Enumerations for going through all nodes in graph
     * and all nodes adjacent to a given node respectively.
     * Added by t.n.                                                 */
    
    public Enumeration allNodes() {
        Vector the_nodes = new Vector(1);
        for (int i = 0; i < MAX_NODES; i++) {
            if (my_nodeset[i].isActivated())
                the_nodes.addElement(new Character(translateIndexChar(i)));
        }
        Enumeration enumeration = the_nodes.elements();
        return enumeration;
    }
    
    public Enumeration allAdjacentNodes(char c) {
        int index_c = translateCharIndex(c);
        Vector the_nodes = new Vector(1);
        if (my_nodeset[index_c].isActivated())
            for (int i = 0; i < MAX_NODES; i++) {
            if (my_edgeset[index_c][i].isActivated())
                the_nodes.addElement(new Character(translateIndexChar(i)));
            }
        Enumeration enumeration = the_nodes.elements();
        return enumeration;
    }
    
    /*************************************************************************/
    /**                         SET GRAPH ATTRIBUTES                        **/
    /*************************************************************************/
    
    
    /**Function:  setDirected
     * Pre:  Graph must be empty of all edges
     * Post: Sets the graph to be directed
     * Note: If the graph has edges, nothing is done                     */
    public void setDirected() {
        //We cannot possibly change the directedness of a graph with edges
        if(num_edges > 0) return;
        
        directed = true;            //Set our graph to be directed
    }
    
    
    /**Function:  setUndirected
     * Pre:  Graph must be empty of all edges
     * Post: Sets the graph to be undirected
     * Note: If the graph has edges, nothing is done                  */
    public void setUndirected() {
        //We cannot possibly change the directedness of a graph with edges
        if(num_edges > 0) return;
        
        directed = false;            //Set our graph to be undirected
    }
    
    
    /**Function:  setWeighted
     * Pre:  Nothing
     * Post: Sets the graph to be weighted, sets all the edges
     *       to be weighted                                          */
    public void setWeighted()               {  weighted = true;  }
    
    
    /**Function:  setUnweighted
     * Pre:  Nothing
     * Post: Sets the graph to be unweighted                         */
    public void setUnweighted()             {  weighted = false; }
    
    /*************************************************************************/
    /**                        QUERY GRAPH ATTRIBUTES                       **/
    /*************************************************************************/
    
    /**Function:  empty
     * Pre:  Nothing
     * Post: Returns true if there are no nodes and no edges in the graph*/
    public boolean empty() { return((num_nodes == 0) && (num_edges == 0)); }
    
    /**Function:  full
     * Pre:  Nothing
     * Post: Returns true if the maximum number of nodes in the graph
     *       have been activated                                         */
    public boolean full()            { return(num_nodes == MAX_NODES); }
    
    
    
    /**Function:  fullEdge
     * Pre:  Nothing
     * Post: Returns true if the maximum number of edges in the graph
     *       have been activated                                         */
    public boolean fullEdge() {
        if(directed) return(num_edges == num_nodes * num_nodes);
        else return(num_edges ==
                (num_nodes * num_nodes - num_nodes) / 2 + num_nodes);
    }
    
    
    /**Function:  isWeighted
     * Pre:  Nothing
     * Post: Returns whether the graph is weighted or not      */
    public boolean isWeighted()      { return weighted;         }
    
    
    /**Function:  isDirected
     * Pre:  Nothing
     * Post: Returns whether the graph is directed or not      */
    public boolean isDirected()      { return directed;         }
    
    
    /**Function:  getNumNodes
     * Pre:  Nothing
     * Post: Returns the number of nodes in the current graph  */
    public     int getNumNodes()     { return num_nodes;        }
    
    
    /**Function:  getNumEdges
     * Pre:  Nothing
     * Post: Returns the number of edges in the current graph  */
    public     int getNumEdges()     { return num_edges;        }
    
    
    /*************************************************************************/
    /**                           ANIMAL FUNCTIONS                          **/
    /*************************************************************************/
    
    /**Function:  toggleConcurrent
     * Pre:  Nothing
     * Post: If concurrent is currently off, begins a concurrent group in
     *       the ANIMAL output file with '{' & turns concurrent on.  If
     *       concurrent is currently on, ends a concurrent group in the
     *       ANIMAL output file with '}' & turns concurrent off         */
    public void toggleConcurrent() {
        //If alread concurrent, end concurrent... otherwise, start concur
        if(isConcurrent) my_fout.println("}");
        else my_fout.println("{");
        isConcurrent = !isConcurrent;
    }
    
    
    /**Function:  setConcurrent
     * Pre:  Nothing
     * Post: Prints "{" to the ANIAML file and turns concurrent on    */
    public void setConcurrent() {
        my_fout.println("{");   isConcurrent = true;   }
    
    
    /**Function:  setUnConcurrent
     * Pre:  Nothing
     * Post: Prints "}" to the ANIAML file and turns concurrent off    */
    public void setUnConcurrent() {
        my_fout.println("}");   isConcurrent = false;  }
    
    
    /*************************************************************************/
    /**                          SAVE / LOAD GRAPHS                         **/
    /*************************************************************************/
    
    
    /**Function:  loadGraph (file)
     * Pre:  Takes a string filename, presumably the name of a file
     * Post: Checks if the file exists.  If not, returns.  If so,
     *       clears the graph and reads a new graph from the file     */
    public void loadGraph(String filename)
    throws IOException {
        File my_file = new File(filename);
        
        //Make sure the file exists
        if(!my_file.exists() || !my_file.isFile()) {
            System.out.println("ERROR: '" + filename + " 'does not exist!");
            return;
        }
        
        //Make sure you can read from the file
        if(!my_file.canRead()) {
            System.out.println("ERROR: I cannot read from '" + filename
                    + "' currently.");
            return;
        }
        
        //Load the graph
        StreamTokenizer fin = new StreamTokenizer(
                (Reader) new FileReader(my_file));
        loadGraph(fin);
    }
    
    
    /**Function:  loadGraph (BufferedReader)
     * Pre:  Takes a BufferedReader fin
     * Post: Clears the graph and reads a new graph from the reader     */
    public void loadGraph(BufferedReader fin)
    throws IOException {
        StreamTokenizer cin = new StreamTokenizer(fin);
        loadGraph(cin);
    }
    
    
    /**Function:  loadGraph (StreamTokenizer)
     * Pre:  Takes a StreamTokenizer cin
     * Post: Clears the graph and reads a new graph from the tokenizer
     *       The REAL graph loader!                                     */
    public void loadGraph(StreamTokenizer cin)
    throws IOException {
        clearGraph();               //Clear the graph to start
        String comment;             //Comment line stuff
        boolean our_chars = false;  //Do we auto-number nodes yet?
        int max_edges;              //Max edges value
        double x, y, w;             //Location for a node, edge weight
        int s, e;                   //Start/end locations for the edges
        char cur_node = 'A';        //Character for the current node
        char used_nodes[];          //Array of used nodes
        int n_nodes, n_edges;       //Number of nodes and edges found
        int status;                 //Temporarily holds streamtok status
        used_nodes = new char[MAX_NODES];
        
        //End-of-lines are significant
        cin.eolIsSignificant(true);
        
        //Read in number of nodes and number of edges
        if(cin.nextToken() == cin.TT_NUMBER) n_nodes = (int) cin.nval;
        else throw new IOException("ERROR: Bad node # input");
        if(cin.nextToken() == cin.TT_NUMBER) n_edges = (int) cin.nval;
        else throw new IOException("ERROR: Bad edges # input");
        if(cin.nextToken() != cin.TT_EOL)
            throw new IOException("Bad file form after first line.");
        
        //Read comment line
        if(cin.nextToken() != cin.TT_WORD)
            throw new IOException("ERROR: Bad comment line");
        if(cin.sval.charAt(0) == 'D') directed = true;
        else if(cin.sval.charAt(0) == 'U') directed = false;
        else throw new IOException("ERROR: Bad comment line; directed");
        if(cin.sval.charAt(1) == 'W') weighted = true;
        else if(cin.sval.charAt(1) == 'U') weighted = false;
        else throw new IOException("ERROR: Bad comment line; weighted");
        
        //Determine the maximum amount of edges
        max_edges = n_nodes * n_nodes;
        if(!directed) max_edges = (max_edges - n_nodes) / 2 + n_nodes;
        
        //Check number of edges/nodes for validity
        if((n_nodes < 0) || (n_nodes > MAX_NODES))
            throw new IOException("ERROR: Invalid # of nodes");
        if((n_edges < 0) || (n_edges > max_edges))
            throw new IOException("ERROR: Invalid # of edges");
        
        //Clear out the rest of the comment line
        while(cin.nextToken() != cin.TT_EOL);
        
        //Get all the node definitions
        for(int nodes = 0; nodes < n_nodes; nodes++) {
            if(cin.nextToken() == cin.TT_NUMBER) x = cin.nval;
            else throw new IOException("ERROR: X-coordinate");
            if(cin.nextToken() == cin.TT_NUMBER) y = cin.nval;
            else throw new IOException("ERROR: Y-coordinate");
            
            status = cin.nextToken();
            //If user-defined characters, but none given, then
            //   we'll define the remaining chars ourself, thank you!
            if(!our_chars && (status == cin.TT_EOL)) {
                our_chars = true;
                cur_node = 'A';
            }
            
            //Define our own characters
            if(our_chars) {
                //Get out own character
                while(my_nodeset[translateCharIndex(cur_node)].isActivated())
                    cur_node = translateIndexChar(
                            translateCharIndex(cur_node) + 1);
                
                //Go to the next line
                cin.pushBack();
                while(cin.nextToken() != cin.TT_EOL);
                cin.pushBack();
            } else if(status == cin.TT_WORD)      //Letter
                cur_node = cin.sval.charAt(0);
            else if(status == cin.TT_NUMBER)    //Number
                cur_node = Integer.toString((int) cin.nval).charAt(0);
            else
                throw new IOException("ERROR:  Error while reading nodes");
            
            //Make sure the node doesn't already exist
            if(nodeExists(cur_node)) throw new IOException("ERROR:  Node " +
                    cur_node + " already exists!");
            
            //Finally, add the node & clear the newline
            used_nodes[nodes] = cur_node;
            addNode(cur_node, x, y, "white", false);
            if(cin.nextToken() != cin.TT_EOL)
                throw new IOException("Bad file form at node #" + cur_node);
        }
        
        //Get all the edge definitions
        for(int edges = 0; edges < n_edges; edges++) {
            if(cin.nextToken() == cin.TT_NUMBER) s = (int) cin.nval - 1;
            else throw new IOException("ERROR: Start node for edge" + cin.sval);
            if(cin.nextToken() == cin.TT_NUMBER) e = (int) cin.nval - 1;
            else throw new IOException("ERROR: End node for edge");
            
            //Check end nodes for validity
            if((s >= num_nodes) || (s < 0) || (e >= num_nodes) || (e < 0))
                throw new IOException("ERROR: Edge end-nodes s = " + s +
                        " and e = " + e);
            if(edgeExists(used_nodes[s], used_nodes[e]))
                throw new IOException("ERROR: Edge " + used_nodes[s] +
                        " => " + used_nodes[e] + "already activated");
            
            //If weighted, get a weight
            if(weighted)
                if(cin.nextToken() == cin.TT_NUMBER) w = cin.nval;
                else throw new IOException("ERROR: Weight");
            else
                w = 0.0;
            
            //Finally, add the edge
            addEdge(used_nodes[s], used_nodes[e], w, false, "black");
            if(!directed && (s != e))
                addEdge(used_nodes[e], used_nodes[s], w, false, "black");
            if((cin.nextToken() != cin.TT_EOL) &&
                    (cin.nextToken() != cin.TT_EOF))
                throw new IOException("Bad file form while reading edges");
        }
        
        //Since we loaded the graph from file, we assume it is not changed
        //Also, assign all the edges to the appropriate locations based on
        //      where the nodes are
        isChanged = false;
        assignEdges();
    }
    
    
    /**Function: saveGraph (file)
     * Pre:  Takes a string filename, presumably the name of a file
     * Post: Checks if the file exists and can be written to.
     *       If not, returns.  If so, writes the graph to the file     */
    public void saveGraph(String filename)
    throws IOException {
        File my_file = new File(filename);
        
        //If the file doesn't exist, create it
        if(!my_file.exists())
            if(!my_file.createNewFile())
                throw new IOException("ERROR: Could not create new file '" +
                        filename + "'!");
        
        //Make sure the file exists
        if(!my_file.isFile())
            throw new IOException("ERROR: '" + filename +
                    "' is a bad file name");
        
        //Make sure you can write to the file
        if(!my_file.canWrite())
            throw new IOException("ERROR: I cannot write to '" + filename +
                    "' currently.");
        
        //Save the graph
        PrintWriter fout = new PrintWriter((Writer) new FileWriter(my_file));
        saveGraph(fout);
        fout.close();
    }
    
    
    /**Function: saveGraph (PrintWriter)
     * Pre:  PrintWriter fout
     * Post: Writes the graph to the PrintWriter     */
    public void saveGraph(PrintWriter fout)
    throws IOException {
        char d, w;      //Characters to use for weighted, directed
        char nodes[];   //Store which node is which
        int s, e;       //Index of starting, ending node
        int c_node = 0; //Count nodes outputted
        nodes = new char[MAX_NODES];
        
        //Output the number of nodes and edges
        fout.println(num_nodes + " " + num_edges);
        
        //Get the characters for weighted/directed; output them
        if(directed) fout.print('D'); else fout.print('U');
        if(weighted) fout.print('W'); else fout.print('U');
        fout.println(" My new graph");
        
        //Output each active node (pos_x, pos_y) char_val
        for(int x = 0; x < MAX_NODES; x++)
            if(my_nodeset[x].isActivated()) {
            fout.println(my_nodeset[x].getX() + " " +
                    my_nodeset[x].getY() + " " +
                    translateIndexChar(x));
            nodes[c_node] = translateIndexChar(x);
            c_node++;
            }
        
        //Output each active edge
        for(int xx = 0; xx < MAX_NODES; xx++)
            for(int yy = (directed) ? 0 : xx; yy < MAX_NODES; yy++)
                if(my_edgeset[xx][yy].isActivated()) {
            s=-1;
            e=-1;
            for(int y = 0; (y < c_node) &&
                    ((e == -1) || (s == -1)); y++) {
                if(nodes[y] == translateIndexChar(xx)) s = y + 1;
                if(nodes[y] == translateIndexChar(yy)) e = y + 1;
            }
            
            if((s == -1) || (e == -1))
                throw new IOException("ERROR: WRITING TO PRINTWRITER");
            
            fout.print(s + " " + e);
            //Put weight at end if necessary
            if(weighted) fout.println(" " +
                    my_edgeset[xx][yy].getWeight());
            else fout.println();
                }
        
        fout.flush();               //Flush the PrintWriter, just in case
    }
    
    
    /*************************************************************************/
    /**                           KAMADA FUNCTIONS                          **/
    /**                    USED TO LAYOUT GRAPH PRETTILY                    **/
    /*************************************************************************/
    
    
    /**Function: organizeGraph
     * Pre:  Nothing
     * Post: Uses the Kamada Algorithm on the nodes and edges currently in
     *       the graph to make a pretty-looking graph
     * Uses the following formulas:
     *      m = current node to calculate for
     *     dm = Delta(m) = sqrt(Eq7^2 + Eq8^2)
     * max_dm = max(Delta(m))
     *  (In the following, i is an iterative variable)
     *  diffx = x(m) - x(i)
     *  diffy = y(m) - y(m)
     *  ndist = sqrt({x(m) - x(i)}^2 + {y(m) - y(i)}^2)
     *    Eq7 = dE/dx(m) = Sum(i): k(mi) * (diffx - l(mi) * diffx / ndist)
     *    Eq8 = dE/dy(m) = Sum(i): k(mi) * (diffy - l(mi) * diffy / ndist)
     *   Eq13 = d^2E/dx(m)^2
     *        = Sum(i): k(mi) * [1 - (l(mi) * diffy^2)/ndist^3]
     *   Eq14 = d^2E/dx(m)dy(m)
     *        = Sum(i): k(mi) * [1 - (l(mi) * diffx * diffy)/ndist^3]
     *   Eq16 = d^2E/dy(m)^2
     *        = Sum(i): k(mi) * [1 - (l(mi) * diffx^2)/ndist^3]
     * deltay = (Eq14 * Eq7 - Eq13 * Eq8) / (Eq13 * Eq16 - Eq14^2)
     * deltax = -1 * (Eq16 * deltay + Eq8) / Eq14                        */
    public void organizeGraph() throws IOException {
        int inds[];             //Quick-reference to indices needed for nodes
        int dist[][];           //Distances between the points
        double L[][];           //tension between points
        double k[][];           //spring between points
        double maxDist = 0;     //Maximum distance for the graph
        double bigl;            //Desired edge length for the drawing
        double diffx, diffy;    //Used to determine xm-xi, ym-yi
        double ndist;           //Distance between two nodes
        double Eq7, Eq8, Eq13, Eq14, Eq16;    //The five equations
        int m;                  //The current node to fix
        double dm, max_dm;      //delta m and maximum delta-m
        double iter1, iter2;    //Iteration variables
        double deltaX, deltaY;  //Delta x and Delta y
        double moved = 10;      //How much did I move in the last move?
        int last_char;          //What character did we move last?
        
        //Step 0: Find active indices, return early for num_nodes < 2
        isChanged = false;
        if(num_nodes < 2) return;
        
        //Get indices that we use in inds for quicker access
        inds = new int[num_nodes];
        int temp = 0;
        for(int z = 0; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated()) {
            inds[temp] = z;
            temp++;
            }
        
        //Step 1: Get initial points
        //Make Starintg points for the graph are on a unit circle
        double theta = 2.0 * Math.PI / num_nodes;   //Iteration factor
        for(int w = 0; w < num_nodes; w++) {
            my_nodeset[inds[w]].setX(Math.cos(w * theta) / 2 + 0.5);
            my_nodeset[inds[w]].setY(-1.0 * Math.sin(w * theta) / 2 + 0.5);
        }
        
        //Step 2:  Calculate distances & store (using Floyd's algorithm)
        dist = new int[num_nodes][num_nodes];
        //Part A:  Initial assignments, based upon edge existance
        for(int xx = 0; xx < num_nodes; xx++)
            for(int yy = 0; yy < num_nodes; yy++)
                if(xx == yy) dist[xx][yy] = 0;
                else if(my_edgeset[inds[xx]][inds[yy]].isActivated() ||
                my_edgeset[inds[yy]][inds[xx]].isActivated())
                    dist[xx][yy] = 1;
                else dist[xx][yy] = BIG_NUM;
        
        //Part B:  Check connections through other nodes
        for(int h = 0; h < num_nodes; h++)
            for(int i = 0; i < num_nodes; i++)
                for(int j = 0; j < num_nodes; j++) {
            if(dist[i][j] == BIG_NUM)
                dist[i][j] = Math.min(dist[i][j], dist[i][h] + dist[h][j]);
            if((dist[i][j] < BIG_NUM) && (dist[i][j] > maxDist))
                maxDist = dist[i][j];
                }
        
        //Step 3: Initialize spring/tension arrays used by Kamada's algorithm
        L = new double[num_nodes][num_nodes];
        k = new double[num_nodes][num_nodes];
        //Richard - this makes a difference
        //bigl = 1.0 / maxDist;                     // L = L0 / max d(ij)
        bigl = 0.9 / maxDist;                     // L = L0 / max d(ij)
        for(int xx = 0; xx < num_nodes; xx++)
            for(int yy = 0; yy <= xx; yy++) {
            L[xx][yy] = bigl * dist[xx][yy];    // L(ij) = L * d(ij)
            L[yy][xx] = L[xx][yy];
            if(xx != yy)
                k[xx][yy] = (double) (K) / Math.pow(dist[xx][yy], 2);
            else k[xx][yy] = 0;
            
            k[yy][xx] = k[xx][yy];              // k(ij) = K / d(ij)^2
            }
        
        //Step 4: Take what we have and MAKE IT PRETTY
        //        IE, the BA Loop of Death While Loop-a-mundo.
        //Part A: First, initialize the appropriate variables
        //        Set iterations to zero, find node with max dm
        iter1 = 0;
        max_dm = m = 0;
        for(int f = 0; f < num_nodes; f++) {
            //Part i: Calculate Eq7 and Eq8
            Eq7 = Eq8 = 0;
            for(int i = 0; i < num_nodes; i++)
                if(i != f) {
                //Get diffx, diffy, and ndist
                diffx = my_nodeset[inds[f]].getX() - my_nodeset[inds[i]].getX();
                diffy = my_nodeset[inds[f]].getY() - my_nodeset[inds[i]].getY();
                ndist = Math.sqrt(diffx * diffx + diffy * diffy);
                
                //Sum Eq7 and Eq8
                Eq7 += k[f][i] * (diffx - L[f][i] * diffx / ndist);
                Eq8 += k[f][i] * (diffy - L[f][i] * diffy / ndist);
                }
            
            //Part ii: Calculate dm = sqrt(Eq7^2 + Eq8^2)
            dm = Math.sqrt(Eq7 * Eq7 + Eq8 * Eq8);
            
            //Part iii: Check dm against max_dm, adjust m and max_dm
            if(dm > max_dm) {  m = f;  max_dm = dm;  }
        }
        
        //Part B:  LOOP!  Loop until max_dm is less than epsilon, or
        //         we've run too many iterations anyway
        last_char = m;
        while((max_dm > EP1) && (iter1 < ITERLIM1)) {
            //Initialize inner loop
            dm = max_dm;
            iter2 = 0;
            iter1++;
            
            //Part C: INNER LOOP!  Loop until dm is less than epsilon2, or
            //        we've run too many iterations anyway
            while((moved > EP2) && (dm > EP2) && (iter2 < ITERLIM2)) {
                iter2++;
                //Part D: Calculate Eq7, Eq8, Eq13, Eq14, and Eq16
                //Step i: Initialize to zero
                Eq7 = Eq8 = Eq13 = Eq14 = Eq16 = 0;
                for(int i = 0; i < num_nodes; i++)
                    if(i != m) {
                    //Step ii: Calculate diffx, diffy, and ndist
                    diffx = my_nodeset[inds[m]].getX() -
                            my_nodeset[inds[i]].getX();
                    diffy = my_nodeset[inds[m]].getY() -
                            my_nodeset[inds[i]].getY();
                    ndist = Math.sqrt(diffx * diffx + diffy * diffy);
                    
                    //Step iii: Sum Eq7, Eq8, Eq13, Eq14, and Eq16
                    Eq7 += k[m][i] * (diffx - L[m][i] * diffx / ndist);
                    Eq8 += k[m][i] * (diffy - L[m][i] * diffy / ndist);
                    Eq13 += k[m][i] * (1 - L[m][i] * Math.pow(diffy, 2) /
                            Math.pow(ndist, 3));
                    Eq14 += k[m][i] * (L[m][i] * diffx * diffy /
                            Math.pow(ndist, 3));
                    Eq16 += k[m][i] * (1 - L[m][i] * Math.pow(diffx, 2) /
                            Math.pow(ndist, 3));
                    }  //End if i != m
                
                //Part E:  Determine delta x and delta y
                deltaY = (Eq14 * Eq7 - Eq13 * Eq8) /
                        (Eq13 * Eq16 - Eq14 * Eq14);
                deltaX = -1 * (Eq16 * deltaY + Eq8) / Eq14;
                
                //Part F:  Determine new x and new y
                //MO
                my_nodeset[inds[m]].setX(my_nodeset[inds[m]].getX() + deltaX);
                my_nodeset[inds[m]].setY(my_nodeset[inds[m]].getY() + deltaY);
                
                //Part G:  Find the new dm using new Eq7 and Eq8
                //Part i: Calculate Eq7 and Eq8
                Eq7 = Eq8 = 0;
                for(int i = 0; i < num_nodes; i++)
                    if(i != m) {
                    //Get diffx, diffy, and ndist
                    diffx = my_nodeset[inds[m]].getX() -
                            my_nodeset[inds[i]].getX();
                    diffy = my_nodeset[inds[m]].getY() -
                            my_nodeset[inds[i]].getY();
                    ndist = Math.sqrt(diffx * diffx + diffy * diffy);
                    
                    //Sum Eq7 and Eq8
                    Eq7 += k[m][i] * (diffx - L[m][i] * diffx / ndist);
                    Eq8 += k[m][i] * (diffy - L[m][i] * diffy / ndist);
                    }
                
                //Part ii: Calculate dm = sqrt(Eq7^2 + Eq8^2)
                dm = Math.sqrt(Eq7 * Eq7 + Eq8 * Eq8);
                moved = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                
                //for(int delay = 0; delay < 10000; delay++)
                //  for(int delay2 = 0; delay2 < 10000; delay2++);
            }  //End inner loop (adjust a particular node)
            
            //Part H:  Find max_dm, m, and dm again
            max_dm = m = 0;
            for(int f = 0; f < num_nodes; f++) {
                //Part i: Calculate Eq7 and Eq8
                Eq7 = Eq8 = 0;
                for(int i = 0; i < num_nodes; i++)
                    if(i != f) {
                    //Get diffx, diffy, and ndist
                    diffx = my_nodeset[inds[f]].getX() -
                            my_nodeset[inds[i]].getX();
                    diffy = my_nodeset[inds[f]].getY() -
                            my_nodeset[inds[i]].getY();
                    ndist = Math.sqrt(diffx * diffx + diffy * diffy);
                    
                    //Sum Eq7 and Eq8
                    Eq7 += k[f][i] * (diffx - L[f][i] * diffx / ndist);
                    Eq8 += k[f][i] * (diffy - L[f][i] * diffy / ndist);
                    }
                
                //Part ii: Calculate dm = sqrt(Eq7^2 + Eq8^2)
                dm = Math.sqrt(Eq7 * Eq7 + Eq8 * Eq8);
                
                //Part iii: Check dm against max_dm, adjust m and max_dm
                if(dm > max_dm) {  m = f;  max_dm = dm;  }
            } //End loop checking dm's
            
            if(m == last_char) break; else last_char = m;
        }  //End outer loop (move each node)
        
        //Part I:  We are done!
        
        //Step 5:  Readjust nodes to fit nicely in the screen!
        //First, find the max and minimum nodes
        double minx = 100, miny = 100, maxx = -100, maxy = -100;
        for(int zz = 0; zz < num_nodes; zz++) {
            if(my_nodeset[inds[zz]].getX() < minx)
                minx = my_nodeset[inds[zz]].getX();
            if(my_nodeset[inds[zz]].getY() < miny)
                miny = my_nodeset[inds[zz]].getY();
            if(my_nodeset[inds[zz]].getX() > maxx)
                maxx = my_nodeset[inds[zz]].getX();
            if(my_nodeset[inds[zz]].getY() > maxy)
                maxy = my_nodeset[inds[zz]].getY();
        }
        
        //Now, go through each node & fit to the screen!
        for(int zz = 0; zz < num_nodes; zz++) {
            my_nodeset[inds[zz]].setX( (my_nodeset[inds[zz]].getX() - minx) /
                    (maxx - minx) * 0.90 + 0.05);
            my_nodeset[inds[zz]].setY( (my_nodeset[inds[zz]].getY() - miny) /
                    (maxy - miny) * 0.90 + 0.05);
        }
        
        //Step 6:  Assign start/end values to edges
        assignEdges();
    }   //End Function to set the prettiness of the graph
    
    
    /**Function:  organizeCircle()
     * Pre:  There are more than three nodes in the graph
     * Post: Assigns the nodes to locations around a unit circle, looks pretty!
     *       Also assigns edges appropriately.                               */
    public void organizeCircle() {
        int inds[];             //Quick-reference to indices needed for nodes
        
        //Return early for num_nodes < 2
        if(num_nodes < 2) return;
        
        //Get indices that we use in inds for quicker access
        inds = new int[num_nodes];
        int temp = 0;
        for(int z = 0; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated()) {
            inds[temp] = z;
            temp++;
            }
        
        //Make points for the graph be on a unit circle
        double theta = 2.0 * Math.PI / num_nodes;   //Iteration factor
        for(int w = 0; w < num_nodes; w++) {
            my_nodeset[inds[w]].setX(Math.cos(w * theta) / 2.25 + 0.5);
            my_nodeset[inds[w]].setY(-1.0 * Math.sin(w * theta) / 2.25 + 0.5);
        }
        
        //We're done now, assign the edges appropriately
        assignEdges();
    }
    
    
    /**Function:  assignEdges()
     * Pre: Nothing
     * Post: Takes the current edges and assigns them to the correct
     *       location based on node size & start-, end-node locations*/
    private void assignEdges() {
        //For now, we have all straight edges
        //We use arrows to show direction for directed graphs
        //So, for simplicity, we put the edge lines in the background and have
        //each edge start at the start-node and end at the end-node
        for(int sn = 0; sn < MAX_NODES; sn++)
            for(int en = 0; en < MAX_NODES; en++)
                if(my_edgeset[sn][en].isActivated()) {
            my_edgeset[sn][en].setSX(my_nodeset[sn].getX());
            my_edgeset[sn][en].setSY(my_nodeset[sn].getY());
            my_edgeset[sn][en].setEX(my_nodeset[en].getX());
            my_edgeset[sn][en].setEY(my_nodeset[en].getY());
                }
    }
    
    
    
    /**Function:  isNewGraph
     * Pre:  Nothing
     * Post: Checks to see if all the nodes are located at (0.5, 0.5).  This
     *       is the sign of a new graph.  Returns true for new graphs                  */
    private boolean isNewGraph() {
        boolean isnewgraph = true;
        int node  = 0;
        
        while(isnewgraph && (node < MAX_NODES)) {
            if(my_nodeset[node].isActivated())
                if((my_nodeset[node].getX() != 0.5) ||
                    (my_nodeset[node].getY() != 0.5))
                    isnewgraph = false;
            node++;
        }
        
        return isnewgraph;
    }
    
    /*************************************************************************/
    /**                           LAYOUT FUNCTIONS                          **/
    /*************************************************************************/
    
    
    /**Function:  clearGraph
     * Pre:  Nothing
     * Post: Resets the current graph to a clear graph; IE, a graph with
     *       zero nodes and zero edges, with nothing defined.            */
    public void clearGraph() {
        //Go through activated members of the class
        for(int start = 0; start < MAX_NODES; start++) {
            //Clear all activated nodes and deactivate them
            if(my_nodeset[start].isActivated()) {
                my_nodeset[start].clearNode();
                my_nodeset[start].deactivate();
            }
            
            //Clear all activated edges and deactivate them
            for(int end = 0; end < MAX_NODES; end++)
                if(my_edgeset[start][end].isActivated()) {
                my_edgeset[start][end].clearEdge();
                my_edgeset[start][end].deactivate();
                }
        }
        
        //Set the number of nodes and edges to zero.
        num_nodes = num_edges = 0;
        
        //Since the graph is clear, we set weighted to false (default)
        //      and directed to false (default).  Also, isChanged
        //      is set to false for now; we need no redesign.
        weighted = directed = isChanged = false;
    }  //End clearGraph()
    
    
    
    /**Function:  randomGraph()
     * Pre:  Takes specifications for a random graph, including
     *       the number of nodes and edges (0 and -1 respectively for
     *       random nodes/edges), whether self-loops are allowed, whether
     *       the graph will have weights, whether the graph is directed,
     *       and min/max weight if weighted
     * Post: Clears the current graph and constructs a random graph
     *       using the specifications given (does error checking on
     *       the specifications as well)
     * Note: To keep from wasting time, if the first randomly-selected
     *       node or edge to activate is already active, we simply step
     *       through the remaining nodes/edges to find the next
     *       subsequent deactivated node/edge
     * Note2: If an inappropriate edge count is given, the count will
     *        be upsized or downsized until it is appropriate          */
    public void randomGraph(int node_c, int edge_c, boolean self_loop,
            boolean weights, boolean dircted,
            double min_weight, double max_weight)
            throws RuntimeException {
        int ns_index, ne_index;     //Indices for randomness
        int max_edges;              //Max number of edges
        int edge_holder;            //Use these to randomize edges faster
        boolean first_edge;
        clearGraph();               //Clears the current graph
        
        //If num_nodes is zero, find the number of nodes
        if(node_c == 0) node_c = (int) (Math.random() * MAX_NODES);
        
        //Activate the correct number of nodes
        for(int x = 0; x < node_c; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        //Determine the maximum possible # of edges for this configuration
        max_edges = node_c * node_c;
        if(!dircted)  max_edges  = (max_edges - node_c) / 2 + node_c;
        if(!self_loop) max_edges -= node_c;
        
        //If num_edges is negative or otherwise invalid, find
        //   a valid number of edges
        if(edge_c < 0)         edge_c = (int) (Math.random() * max_edges);
        if(edge_c > max_edges) edge_c = max_edges;
        
        //Activate the correct number of edges
        for(int y = 0; y < edge_c; y++) {
            char booger = randomActiveNode();
            char snooger = randomActiveNode();
            ns_index = translateCharIndex(booger);
            ne_index = translateCharIndex(snooger);
            edge_holder = ns_index;     //Use these to quickly randomize
            first_edge = true;
            
            //Make sure you get a new, appropriate edge
            while((!self_loop && (ns_index == ne_index)) ||
                    (my_edgeset[ns_index][ne_index].isActivated())) {
                //Choose a new node to start/end the edge with
                //Try getting new nodes for the start-node first
                if(first_edge) {
                    ns_index = (ns_index + 1) % MAX_NODES;
                    while((ns_index != edge_holder) &&
                            (!my_nodeset[ns_index].isActivated()))
                        ns_index = (ns_index + 1) % MAX_NODES;
                    
                    if(ns_index == edge_holder) {
                        first_edge = false;
                        edge_holder = ne_index;
                    }
                }
                
                //If that's not working, get new end-nodes
                if(!first_edge) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while((ne_index != edge_holder) &&
                            (!my_nodeset[ne_index].isActivated()))
                        ne_index = (ne_index + 1) % MAX_NODES;
                    
                    //If we STILL can't find a good edge, something is
                    //   seriously wrong... throw a runtimeException
                    if(ne_index == edge_holder)
                        throw new RuntimeException("Prob finding random edge.");
                }  //END iterate end-nodes
            }  //END Get valid edge to activate
            
            //We now have a valid edge... activate it appropriately!
            //If it's weighted, add a random weight
            my_edgeset[ns_index][ne_index].activate();
            if(weights) my_edgeset[ns_index][ne_index].setWeight(
                    min_weight + Math.random() * (max_weight - min_weight));
            
            
            //If the graph is not directed, set the related edge too
            if(!dircted) {
                my_edgeset[ne_index][ns_index].activate();
                my_edgeset[ne_index][ns_index].setWeight(
                        my_edgeset[ns_index][ne_index].getWeight());
            }
        }  //END for(int y = 0; y < edge_c; y++)
        
        //Set various counts and true/false values for the graph
        isChanged = true;
        num_edges = edge_c;
        weighted = weights;
        directed = dircted;
    }  //END randomGraph()
    
    
    /**Function:  randomCompleteGraph()
     * Pre:  Takes specifications for a complete graph, including
     *       the number of nodes (0 for random nodes), whether self-loops
     *       are allowed, whether the graph will have weights,
     *       min/max weight if weighted
     * Post: Clears the current graph and constructs a complete graph
     *       using the specifications given
     * Note: To keep from wasting time, if the first randomly-selected node
     *       to activate is already active, we simply step through the
     *       remaining nodes to find the next subsequent deactivated node */
    public void randomCompleteGraph(int node_c, boolean self_loop,
            boolean weights, double min_weight, double max_weight) {
        int n_index;                //Index for randomness
        int edge_c = 0;             //Number of edges
        clearGraph();               //Clears the current graph
        
        //If num_nodes is zero, find the number of nodes
        if(node_c == 0) node_c = (int) (Math.random() * MAX_NODES);
        
        //Activate the correct number of nodes (randomly)
        for(int x = 0; x < node_c; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        //Activate edges between all active nodes!
        //Also, activate edge between node & self if self_loop
        for(int i = 0; i < MAX_NODES; i++)
            for(int j = 0; j <= i; j++)
                if(((i != j) || ((i == j) && self_loop)) &&
                my_nodeset[i].isActivated() && my_nodeset[j].isActivated()) {
            if(weights)   //Get weights where appropriate
            {
                my_edgeset[i][j].setWeight(min_weight +
                        Math.random() * (max_weight - min_weight));
                my_edgeset[j][i].setWeight(my_edgeset[i][j].getWeight());
            }
            my_edgeset[i][j].activate();
            my_edgeset[j][i].activate();
            edge_c++;
                }
        
        //Set various counts and true/false values for the graph
        isChanged = true;
        num_edges = edge_c;
        weighted  = weights;
        directed  = false;
    }  //END randomCompleteGraph()
    
    
    /**Function:  randomHamiltonianGraph()
     * Pre:  Takes specifications for a random graph, including
     *       the number of nodes and edges (0 and -1 respectively for
     *       random nodes/edges), whether self-loops are allowed, whether
     *       the graph will have weights, whether the graph is directed,
     *       and min/max weight if weighted
     * Post: Clears the current graph and constructs a random graph with
     *       a Hamiltonian cycle using the specifications given
     *       (does error checking on the specifications as well)
     * Note: To keep from wasting time, if the first randomly-selected node
     *       or edge to activate is already active, we simply step through
     *       the remaining nodes/edges to find the next subsequent
     *       deactivated node/edge
     * Note2: If the user specifies more/less edges than are
     *        possible/needed with the number of nodes given, the
     *        number of edges is size up or down appropriatly       */
    public void randomHamiltonianGraph(int node_c, int edge_c,
            boolean self_loop, boolean weights, boolean dircted,
            double min_weight, double max_weight)
            throws RuntimeException {
        int ns_index, ne_index;       //Indices for randomness
        int max_edges, min_edges;     //Max/min number of edges
        int edge_holder;              //Use these to randomize edges faster
        boolean first_edge;
        boolean hc_nodes[];        //Takes note of Hamiltonian cycled nodes
        hc_nodes = new boolean[MAX_NODES];
        
        //Set all the nodes to "not-Hamiltonian-cycled"
        for(int xx = 0; xx < MAX_NODES; xx++)
            hc_nodes[xx] = false;
        
        clearGraph();               //Clears the current graph
        
        //If num_nodes is zero, find the number of nodes
        if(node_c == 0) node_c = (int) (Math.random() * MAX_NODES);
        
        //Activate the correct number of nodes
        for(int x = 0; x < node_c; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        //Determine the max/min possible # of edges for this configuration
        max_edges = node_c * node_c;
        if(!dircted)  max_edges  = (max_edges - node_c) / 2 + node_c;
        if(!self_loop) max_edges -= node_c;
        min_edges = node_c;
        
        //If num_edges is negative or otherwise invalid, find a valid value
        if(edge_c < 0)         edge_c = (int) (Math.random() * max_edges);
        if(edge_c > max_edges) edge_c = max_edges;
        if(edge_c < min_edges) edge_c = min_edges;
        
        //HAMILTONIAN CYCLE ALL THE NODES
        //This is what makes this function special.  Pick a random active
        //    non-Hamiltonian-cycled node until all the nodes are in the
        //    Hamiltonian cycle!  Hoorah!
        //Get the first index and remember it
        ns_index = translateIndexChar(randomActiveNode());
        edge_holder = ns_index;
        
        //Connect up all the nodes into the loop (RANDOMLY) except for
        //  the first edge
        for(int hc = 0; hc < min_edges - 1; hc++) {
            //Get a random node not already in the Hamiltonian cycle
            //  (Note:  NOT the starting node!)
            ne_index = translateCharIndex(randomActiveNode());
            while(hc_nodes[ne_index] || (ne_index == edge_holder))
                ne_index = (ne_index + 1) % MAX_NODES;
            
            //Activate the appropriate edge
            //If it's weighted, add a random weight.
            my_edgeset[ns_index][ne_index].activate();
            if(weights) my_edgeset[ns_index][ne_index].setWeight(
                    min_weight + Math.random() * (max_weight - min_weight));
            
            //If the graph is not directed, set the related edge too
            if(!dircted) {
                my_edgeset[ne_index][ns_index].activate();
                my_edgeset[ne_index][ns_index].setWeight(
                        my_edgeset[ns_index][ne_index].getWeight());
            }
            
            //Add the node to the cycle, and make it the next from-node
            hc_nodes[ne_index] = true;
            ns_index = ne_index;
            
        }  //END getting nodes added to the hamiltonian cycle
        
        //Connect the first edge.  If it's weighted, add a random weight.
        ne_index = edge_holder;
        my_edgeset[ns_index][ne_index].activate();
        if(weights) my_edgeset[ns_index][ne_index].setWeight(
                min_weight + Math.random() * (max_weight - min_weight));
        
        //If the graph is not directed, set the related edge too
        if(!dircted) {
            my_edgeset[ne_index][ns_index].activate();
            my_edgeset[ne_index][ns_index].setWeight(
                    my_edgeset[ns_index][ne_index].getWeight());
        }
        //We've got a Hamiltonian cycle!  Hoorah!
        
        //Then, activate the remaining correct number of edges if needed
        for(int y = min_edges; y < edge_c; y++) {
            ns_index = translateCharIndex(randomActiveNode());
            ne_index = translateCharIndex(randomActiveNode());
            edge_holder = ns_index;     //Use these to quickly randomize
            first_edge = true;
            
            //Make sure you get a new, appropriate edge
            while((!self_loop && (ns_index == ne_index)) ||
                    (my_edgeset[ns_index][ne_index].isActivated())) {
                //Choose a new node to start/end the edge with
                //Try getting new nodes for the start-node first
                if(first_edge) {
                    ns_index = (ns_index + 1) % MAX_NODES;
                    while((ns_index != edge_holder) &&
                            (!my_nodeset[ns_index].isActivated()))
                        ns_index = (ns_index + 1) % MAX_NODES;
                    
                    if(ns_index == edge_holder) {
                        first_edge = false;
                        edge_holder = ne_index;
                    }
                }
                
                //If that's not working, get new end-nodes
                if(!first_edge) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while((ne_index != edge_holder) &&
                            (!my_nodeset[ne_index].isActivated()))
                        ne_index = (ne_index + 1) % MAX_NODES;
                    
                    //If we STILL can't find a good edge, something is
                    //   seriously wrong... throw a runtimeException
                    if(ne_index == edge_holder)
                        throw new RuntimeException("Prob finding random edge.");
                }
            }  //END Get valid edge to activate
            
            //We now have a valid edge... activate it appropriately!
            //If it's weighted, add a random weight.
            my_edgeset[ns_index][ne_index].activate();
            if(weights) my_edgeset[ns_index][ne_index].setWeight(
                    min_weight + Math.random() * (max_weight - min_weight));
            
            //If the graph is not directed, set the related edge too
            if(!dircted) {
                my_edgeset[ne_index][ns_index].activate();
                my_edgeset[ne_index][ns_index].setWeight(
                        my_edgeset[ns_index][ne_index].getWeight());
            }
        }  //END For(y = 0; y < edge_c; y++)
        
        //Set various counts and true/false values for the graph
        isChanged = true;
        num_edges = edge_c;
        weighted = weights;
        directed = dircted;
    }   //END randomHamiltonianGraph
    
    
    
    /**Function:  randomConnectedGraph()
     * Pre:  Takes specifications for a random graph, including
     *       the number of nodes and edges (0 and -1 respectively for
     *       random nodes/edges), whether self-loops are allowed, whether
     *       the graph will have weights, whether the graph is directed,
     *       and min/max weight if weighted
     * Post: Clears the current graph and constructs a random graph
     *       that is completely connected using the specifications given
     *       (does error checking on the specifications as well)
     * Note: To keep from wasting time, if the first randomly-selected
     *       node or edge to activate is already active, we simply step
     *       through the remaining nodes/edges to find the next
     *       subsequent deactivated node/edge
     * Note2: Keeps on adding edges until a connected graph is formed, so
     *        this may give more edges than what the user specifies.
     * Note3: If the user specifies more edges than are possible with the
     *        # of nodes given, the # of edges is sized down approp.    */
    public void randomConnectedGraph(int node_c, int edge_c,
            boolean self_loop, boolean weights, boolean dircted,
            double min_weight, double max_weight)
            throws RuntimeException {
        int ns_index, ne_index;       //Indices for randomness
        int max_edges, my_edge_c;     //Max number of edges, edge count
        int e_h1, e_h2;               //Use these to randomize edges faster
        boolean first_edge;
        boolean completed = false;    //Are all the nodes connected?
        boolean con_nodes[][];     //Takes note of nodes that are connected
        con_nodes = new boolean[MAX_NODES][MAX_NODES];
        
        //Set all the nodes to "not-connected"
        //NOTE:  All nodes are automatically connected to themselves
        for(int xx = 0; xx < MAX_NODES; xx++)
            for(int yy = 0; yy < MAX_NODES; yy++)
                if(xx == yy) con_nodes[xx][yy] = true;
                else con_nodes[xx][yy] = false;
        
        clearGraph();               //Clears the current graph
        
        //If num_nodes is zero, find the number of nodes
        if(node_c == 0) node_c = (int) (Math.random() * MAX_NODES);
        
        //Activate the correct number of nodes
        for(int x = 0; x < node_c; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        //Determine the maximum possible # of edges for this configuration
        max_edges = node_c * node_c;
        if(!dircted)  max_edges  = (max_edges - node_c) / 2 + node_c;
        if(!self_loop) max_edges -= node_c;
        
        //If num_edges is negative or otherwise invalid, find a valid value
        if(edge_c < 0)         edge_c = (int) (Math.random() * max_edges);
        if(edge_c > max_edges) edge_c = max_edges;
        
        //CONNECT ALL THE NODES -- This is what makes this function special
        //Add edges until all nodes are connected
        //Then, activate the remaining correct number of edges if needed
        for(my_edge_c = 0; !completed || (my_edge_c < edge_c); ) {
            ns_index = translateCharIndex(randomActiveNode());
            ne_index = translateCharIndex(randomActiveNode());
            e_h1 = ns_index;            //Use these to quickly randomize
            e_h2 = ne_index;
            first_edge = true;
            
            //Make sure you get a new, appropriate edge
            while((!self_loop && (ns_index == ne_index)) ||
                    (!completed && con_nodes[ns_index][ne_index]) ||
                    (my_edgeset[ns_index][ne_index].isActivated())) {
                //Choose a new node to start/end the edge with
                //If it's the first edge, find the next available end-node
                if(first_edge) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while(!my_nodeset[ne_index].isActivated())
                        ne_index = (ne_index + 1) % MAX_NODES;
                    first_edge = false;
                }
                //Otherwise, if we have more edges to check
                else if((ne_index != e_h2) || (ns_index != e_h1)) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while((ne_index != e_h2) &&
                            (!my_nodeset[ne_index].isActivated()))
                        ne_index = (ne_index + 1) % MAX_NODES;
                    
                    //We checked all the end-nodes for this start-node,
                    //   so move to the next start-node
                    if(ne_index == e_h2) {
                        ns_index = (ns_index + 1) % MAX_NODES;
                        while((ns_index != e_h1) &&
                                (!my_nodeset[ns_index].isActivated()))
                            ns_index = (ns_index + 1) % MAX_NODES;
                    }
                }
                //Otherwise, we checked all possible edges, end program
                else
                    throw new RuntimeException("Prob finding random edge.");
            }  //END Get valid edge to activate
            
            //We now have a valid edge... activate it appropriately!
            //If it's weighted, add a random weight.  Add to the edge count
            my_edge_c++;
            my_edgeset[ns_index][ne_index].activate();
            if(weights) my_edgeset[ns_index][ne_index].setWeight(
                    min_weight + Math.random() * (max_weight - min_weight));
            
            //Connect up the start node with any nodes the end node
            //  is connected to
            for(int others = 0; others < MAX_NODES; others++)
                if(con_nodes[ne_index][others])
                    con_nodes[ns_index][others] = true;
            
            //If the graph is not directed, set the related edge too
            if(!dircted) {
                my_edgeset[ne_index][ns_index].activate();
                my_edgeset[ne_index][ns_index].setWeight(
                        my_edgeset[ns_index][ne_index].getWeight());
                
                //Connect up the end node with any nodes the start node
                //  is connected to
                for(int others2 = 0; others2 < MAX_NODES; others2++)
                    if(con_nodes[ns_index][others2])
                        con_nodes[ne_index][others2] = true;
            }
            
            //If the graph was not "connected" complete on the last turn,
            //  check if it is "connected" now
            if(!completed) {
                //Assume that we are connected until proven otherwise
                //Not connected if any two active nodes are not connected
                completed = true;
                for(int ss = 0; (ss < MAX_NODES) && completed; ss++)
                    for(int ee = 0; (ee < MAX_NODES) && completed; ee++)
                        if(!con_nodes[ss][ee] && my_nodeset[ee].isActivated() &&
                        my_nodeset[ss].isActivated())
                            completed = false;
            }
        }  //END For(y = 0; y < edge_c; y++)
        
        //Set various counts and true/false values for the graph
        isChanged = true;
        num_edges = my_edge_c;
        weighted = weights;
        directed = dircted;
    }   //END randomConnectedGraph
    
    
    /**Function:  randomDAcyclicGraph()
     * Pre:  Takes specifications for a random graph, including
     *       the number of nodes and edges (0 and -1 respectively for
     *       random nodes/edges), whether self-loops are allowed, whether
     *       the graph will have weights, and min/max weights if weighted
     * Post: Clears the current graph and constructs a random directed
     *       acyclic graph using the specifications given
     *       (does error checking on the specifications as well)
     * Note: To keep from wasting time, if the first randomly-selected
     *       node or edge to activate is already active, we simply step
     *       through the remaining nodes/edges to find the next
     *       subsequent deactivated node/edge
     * Note2: If an inappropriate edge count is given, the count will
     *        be upsized until it is appropriate
     * Note3: If we are unable to find an edge that will still keep the
     *        graph acyclic, we cut the edge count short at that point    */
    public void randomDAcyclicGraph(int node_c, int edge_c,
            boolean self_loop, boolean weights,
            double min_weight, double max_weight) {
        int ns_index, ne_index;     //Indices for randomness
        int max_edges, my_edge_c;   //Max number of edges, edge count
        int e_h1, e_h2;             //Use these to randomize edges faster
        boolean first_edge;
        boolean acyc_full = false;  //True when we cannot add more edges
        //  lest we become cyclic
        boolean con_nodes[][];     //Takes note of nodes that are connected
        con_nodes = new boolean[MAX_NODES][MAX_NODES];
        
        //Set all the nodes to "not-connected"
        //NOTE:  All nodes are automatically connected to themselves
        for(int xx = 0; xx < MAX_NODES; xx++)
            for(int yy = 0; yy < MAX_NODES; yy++)
                if(xx == yy) con_nodes[xx][yy] = true;
                else con_nodes[xx][yy] = false;
        
        clearGraph();               //Clears the current graph
        
        //If num_nodes is zero, find the number of nodes
        if(node_c == 0) node_c = (int) (Math.random() * MAX_NODES);
        
        //Activate the correct number of nodes
        for(int x = 0; x < node_c; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        //Determine the maximum possible # of edges for this configuration
        max_edges = node_c * node_c;
        if(!self_loop) max_edges -= node_c;
        
        //If num_edges is negative or otherwise invalid, find
        //   a valid number of edges
        if(edge_c < 0)         edge_c = (int) (Math.random() * max_edges);
        if(edge_c > max_edges) edge_c = max_edges;
        
        //Activate the correct number of edges
        for(my_edge_c = 0; !acyc_full && (my_edge_c < edge_c); ) {
            ns_index = translateCharIndex(randomActiveNode());
            ne_index = translateCharIndex(randomActiveNode());
            e_h1 = ns_index;        //Use these to quickly randomize
            e_h2 = ne_index;
            first_edge = true;
            
            //Make sure you get a new, appropriate edge
            //Note:  We need a new edge if the end connects to the start
            while(((!self_loop && (ns_index == ne_index)) ||
                    (con_nodes[ne_index][ns_index] && (ns_index != ne_index))
                    || (my_edgeset[ns_index][ne_index].isActivated()))
                    && !acyc_full) {
                //Choose a new node to start/end the edge with
                //If it's the first edge, find the next available end-node
                if(first_edge) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while(!my_nodeset[ne_index].isActivated())
                        ne_index = (ne_index + 1) % MAX_NODES;
                    first_edge = false;
                }
                //Otherwise, if we have more edges to check
                else if((ne_index != e_h2) || (ns_index != e_h1)) {
                    ne_index = (ne_index + 1) % MAX_NODES;
                    while((ne_index != e_h2) &&
                            (!my_nodeset[ne_index].isActivated()))
                        ne_index = (ne_index + 1) % MAX_NODES;
                    
                    //We checked all the end-nodes for this start-node,
                    //   so move to the next start-node
                    if(ne_index == e_h2) {
                        ns_index = (ns_index + 1) % MAX_NODES;
                        while((ns_index != e_h1) &&
                                (!my_nodeset[ns_index].isActivated()))
                            ns_index = (ns_index + 1) % MAX_NODES;
                    }
                }
                //If we STILL can't find a good edge, then obviously
                //   we've exhausted the acyclic edges we can use,
                //   so we should stop adding edges
                else
                    acyc_full = true;
            }  //END Get valid edge to activate
            
            //If we aren't adding an edge lest we become cyclical, break
            if(acyc_full) break;
            
            //We now have a valid edge... activate it appropriately!
            //If it's weighted, add a random weight
            my_edgeset[ns_index][ne_index].activate();
            if(weights) my_edgeset[ns_index][ne_index].setWeight(
                    min_weight + Math.random() * (max_weight - min_weight));
            
            //Connect up the start node with any nodes the end node
            //  is connected to (we know they don't include the start node!)
            for(int others = 0; others < MAX_NODES; others++)
                if(con_nodes[ne_index][others])
                    con_nodes[ns_index][others] = true;
        }  //END for(int y = 0; y < edge_c; y++)
        
        //Set various counts and true/false values for the graph
        isChanged = true;
        num_edges = edge_c;
        weighted = weights;
        directed = true;
    }  //END randomDAcyclicGraph()
    
    
    
    /*************************************************************************/
    /**                          UTILITY  FUNCTIONS                         **/
    /*************************************************************************/
    
    
    /**Function:  randomNode()
     * Pre:  Nothing
     * Post: Returns a random node character                             */
    public char randomNode() {
        return translateIndexChar((int) (Math.random() * MAX_NODES)); }
    
    
    
    /**Function:  randomActiveNode()
     * Pre:  Nothing
     * Post: Returns a random node character for an active node
     * Note: To keep from wasting time, if the first randomly-selected node
     *       is deactivated, this simply steps through the nodes to find the
     *       next subsequent active node
     * Note2: If there are currently no activated nodes, this function
     *        will return the invalid value '~'                           */
    public char randomActiveNode() {
        int my_randchar = (int) (Math.random() * MAX_NODES);
        
        //If there are no nodes that are active, return a goof char
        if(num_nodes <= 0) return '~';
        
        //Make sure the random node-char is activated
        //Look for the next activated node-char
        while(!my_nodeset[my_randchar].isActivated())
            my_randchar = (my_randchar + 1) % MAX_NODES;
        
        return translateIndexChar(my_randchar);
    }
    
    
    
    /**Function:  randomNewNode()
     * Pre:  Nothing
     * Post: Returns a random node character for an deactivated node
     * Note: To keep from wasting time, if the first randomly-selected node
     *       is active, this simply steps through the nodes to find the
     *       next subsequent deactivated node
     * Note2: If there are currently no deactivated nodes, this function
     *        will return the invalid value '~'                           */
    public char randomNewNode() {
        int my_randchar = (int) (Math.random() * MAX_NODES);
        
        //If all nodes are active, return a goof char
        if(num_nodes >= MAX_NODES) return '~';
        
        //Make sure the random node-char is deactivated
        //Look for the next deactivated node-char
        while(my_nodeset[my_randchar].isActivated())
            my_randchar = (my_randchar + 1) % MAX_NODES;
        
        return translateIndexChar(my_randchar);
    }
    
    
    
    /**Function:  translateCharIndex
     * Pre:  Takes a character value c
     * Post: Returns the appropriate integer index for the char value
     * Note: If an invalid char is given, return the invalid value -1   */
    public int translateCharIndex(char c) {
        //Capital letters are the first 26 indices
        if(Character.isUpperCase(c)) return ((int) (c - 'A'));
        
        //Lowercase letters are the next 26 indices
        if(Character.isLowerCase(c)) return ((int) (c - 'a') + 26);
        
        //Numbers are the final 10 indices
        if(Character.isDigit(c)) return ((int) (c - '0') + 52);
        
        //Otherwise we return a goof value
        return -1;
    }
    
    
    /**Function:  translateIndexChar
     * Pre:  Takes a integer index value 'index'
     * Post: Returns the appropriate character for the node at that index
     * Note: If an invalid index is given, return the invalid value '~'   */
    public char translateIndexChar(int index) {
        //Capital letters are the first 26 indices
        if((index >= 0) && (index < 26)) return ((char) (index + 'A'));
        
        //Lowercase letters are the next 26 indices
        if((index > 25) && (index < 52)) return ((char) (index - 26 + 'a'));
        
        //Numbers are the final 10 indices
        if((index > 51) && (index < 62)) return ((char) (index - 52 + '0'));
        
        //Otherwise we return a goof value
        return '~';
    }
    
    
    /**Function: nodeDistance(int a, int b)
     * Pre:  Takes two nodes, a and b, assumed to be valid node indices
     * Post: Returns the Euclidian distance between the two nodes      */
    private double nodeDistance(int a, int b) {
        //Make various safety checks
        if((a < 0) || (a > MAX_NODES))
            throw new RuntimeException("nodeDistance, A is out of range");
        if((b < 0) || (b > MAX_NODES))
            throw new RuntimeException("nodeDistance, B is out of range");
        if(!my_nodeset[a].isActivated() || !my_nodeset[b].isActivated())
            throw new RuntimeException("nodeDistance, inactive node refed");
        
        //d = sqrt{ (Xa - Xb)^2 + (Ya - Yb)^2 }
        return Math.sqrt(
                Math.pow(my_nodeset[a].getX() - my_nodeset[b].getX(), 2)
                + Math.pow(my_nodeset[b].getY() - my_nodeset[b].getY(), 2));
    }
    
    
    /**Function: drawGraphPanel
     * Pre:  Takes a graphics object g from a panel
     *       Also, takes a size of the drawing area x_size and y_size
     * Post: Prints the current graph in the graphics object */
    public void drawGraphPanel(Graphics g, int x_size, int y_size) {
        setPanelRadius(x_size, y_size);
        
        //Print all the edges first (some hidden, only active print)
        for(int x = 0; x < MAX_NODES; x++)
            if(my_nodeset[x].isActivated())
                for(int y = x; y < MAX_NODES; y++)
                    if(my_edgeset[x][y].isActivated() ||
                my_edgeset[y][x].isActivated())
                        my_edgeset[x][y].drawEdge(g, x_size, y_size, weighted,
                                (directed && !(my_edgeset[x][y].isActivated() &&
                                my_edgeset[y][x].isActivated())), my_node_radius);
        
        //Print all the nodes second! (some hidden, only active print)
        for(int z = 0; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated())
                my_nodeset[z].drawNode(g, x_size, y_size, my_node_radius);
    }
    
    /**Function: setPanelRadius
     * Pre:  Takes an x_size and y_size of a panel area
     * Post: Sets the radius of nodes for this panel area (to fit nicely)   */
    public void setPanelRadius(int x_size, int y_size) {
        //Set the radius for nodes to be this for now... make it depend upon
        //    how many nodes there are later?
        my_node_radius = (x_size + y_size) / 50;
    }
    
    
    
    /**Function: locateNode
     * Pre:  Takes a real-value position (x,y) from the printing area [0,1]
     * Post: Returns the character index of the first, nearest node to
     *       the given (x,y) position                                      */
    public char locateNode(double x, double y) {
        //Find the first active node
        int index = 0;
        while(!my_nodeset[index].isActivated() && (index < MAX_NODES))
            index++;
        
        //If we have no active nodes, return an error character (~)
        if(index == MAX_NODES) return '~';
        
        //Get out initialized information from this first node (dist, node)
        double min_distance = euclidianDistance(x, y, my_nodeset[index].getX(),
                my_nodeset[index].getY());
        //Find the closest node the location specified
        for(int z = index + 1; z < MAX_NODES; z++)
            if(my_nodeset[z].isActivated())
                if(euclidianDistance(x, y, my_nodeset[z].getX(),
                my_nodeset[z].getY()) < min_distance) {
            min_distance = euclidianDistance(x, y, my_nodeset[z].getX(),
                    my_nodeset[z].getY());
            index = z;
                }
        
        return translateIndexChar(index);
    }
    
    /**Function:  euclidianDistance
     * Pre:  Takes two real-value positions (x1, y1) and (x2, y2)
     * Post: Returns the distance between these two positions            */
    private double euclidianDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
    
    /**Function:  getNextNode
     * Pre:  Nothing
     * Post: Returns the character for the next available disabled node, or '~'
     *       if all the nodes are activated                                   */
    public char getNextNode() {
        int index = 0;
        while(my_nodeset[index].isActivated() && (index < MAX_NODES))
            index++;
        
        if(index == MAX_NODES) return '~';
        else return translateIndexChar(index);
    }
    
    
    /**Function:  isWithin
     * Pre:  Takes a node (by character index) and a position (x_pos, y_pos)
     *       supposedly within the node and a screen size (x_size, y_size)
     * Post: Returns true if the position is within the radius of the node,
     *       returns false otherwise                                        */
    public boolean isWithin(char index, double x_pos, double y_pos,
            int x_size, int y_size) {
        setPanelRadius(x_size, y_size); //Find the good radius for our nodes
        
        //Get the screen positions...
        double x_try = x_pos * (double) x_size;
        double y_try = y_pos * (double) y_size;
        double x_me = my_nodeset[translateCharIndex(index)].getX()
        * (double) x_size;
        double y_me = my_nodeset[translateCharIndex(index)].getY()
        * (double) y_size;
        
        return (euclidianDistance(x_try, y_try, x_me, y_me) < my_node_radius + 1);
    }
    
    
    /*************************************************************************/
    /**                       GRAPH PRINTING FUNCTIONS                      **/
    /*************************************************************************/
    
    
    /**Function: slideCapture
     * Pre:  Takes an x_size and y_size of the printing area
     * Post: Takes a slide of the current graph just as it is      */
    public void slideCapture(int x_size, int y_size) throws IOException {
        //If the graph has been changed, rearrange the nodes appropriately
        if(isChanged)
            isChanged = false;
        
        //If it's not XML, simply print an ANIMAL graph
        if(!isXML) {
            //If not concurrent, make a concurrent group for the graph
            if(!isConcurrent) my_fout.println("{");
            
            //Print ALL nodes & edges (some hidden, only active print)
            for(int x = 0; x < MAX_NODES; x++) {
                my_nodeset[x].printNode(my_fout, x_size, y_size, NODE_SIZE);
                
                for(int y = 0; y < MAX_NODES; y++)
                    my_edgeset[x][y].printEdge(my_fout, x_size, y_size, NODE_SIZE,
                            new String(my_nodeset[x].getChar() + "" +
                            my_nodeset[y].getChar()), weighted, (directed &&
                            !(my_edgeset[x][y].isActivated() &&
                            my_edgeset[y][x].isActivated())));
            }
            
            //If not concurrent, end the concurrent group for the graph
            if(!isConcurrent) my_fout.println("}");
        }
        // Otherwise, it's in XML...
        else {
            // If it's the first print, just print the initial graph
            if(isFirst) {
                //Print the initial graph line, including weighted/directed info
                my_fout.print("\t<graph weighted = \"");
                if(weighted) my_fout.print("true"); else my_fout.print("false");
                my_fout.print("\" directed = \"");
                if(directed) my_fout.print("true"); else my_fout.print("false");
                my_fout.println("\">");
            }
            // Otherwise, print out a new graph animation slide starting
            else
                my_fout.println("\t<graphAnimationSlide>");
            
            //Print out the contents of the graph (works for slide/whole graph)
            
            //Print ALL nodes for the graph
            for(int z = 0; z < MAX_NODES; z++)
                my_nodeset[z].printXMLNode(my_fout, !isFirst);
            
            //Print ALL edges for the graph(some hidden, only active print)
            for(int x = 0; x < MAX_NODES; x++)
                for(int y = 0; y < MAX_NODES; y++)
                    my_edgeset[x][y].printXMLEdge(my_fout, !isFirst,
                            new String(my_nodeset[x].getChar() + ""),
                            new String(my_nodeset[y].getChar() + ""));
            
            //If it's the first time, place an end-graph mark
            //   Otherwise, use an end-animation-slide mark
            if(isFirst) my_fout.println("\t</graph>");
            else my_fout.println("\t</graphAnimationSlide>");
            
            isFirst = false;  //Well, it won't be the first slide anymore!
        }
    }  //End slideCapture function
    
    
    
    /**Function: closeAnimation
     * Pre:  Nothing
     * Post: Closes the animation for writing and closes the file.
     *       VisualGraph will now (traditionally) wait for a new animation
     *       file to use.                                                 */
    public void closeAnimation() {
        //If this is an XML file, put on the end-animation tag
        if(isXML) my_fout.println("</graphAnimation>");
        
        //Close the file and wait for a new PrintWriter or open-file command
        my_fout.close();
    }  // End closeAnimation() function
    
    
    
    /**Function: printGraph
     * Pre:  Takes an x_size and y_size of the printing area
     * Post: Prints the current graph in optimized form         */
    public void printGraph(int x_size, int y_size) throws IOException {
        //If the graph has been changed, rearrange the nodes appropriately
        if(isChanged) {
            isChanged = false;
            organizeGraph();
        }
        
        //If not concurrent, make a concurrent group for the graph
        if(!isConcurrent) my_fout.println("{");
        
        //Print ALL nodes & edges (some hidden, only active print)
        for(int x = 0; x < MAX_NODES; x++) {
            my_nodeset[x].printNode(my_fout, x_size, y_size, NODE_SIZE);
            
            for(int y = 0; y < MAX_NODES; y++)
                my_edgeset[x][y].printEdge(my_fout, x_size, y_size, NODE_SIZE,
                        new String(my_nodeset[x].getChar() + "" +
                        my_nodeset[y].getChar()), weighted, (directed &&
                        !(my_edgeset[x][y].isActivated() &&
                        my_edgeset[y][x].isActivated())));
        }
        
        //If not concurrent, end the concurrent group for the graph
        if(!isConcurrent) my_fout.println("}");
    }  //End printGraph function
    
    /****************************************************
     *         All the adaptations for giags follow     *
     ****************************************************/
    
    /** Constructor to adapt this class for use
     *  with Gaigs and Dijksra's algorythm animations. */
    public VisualGraph(String gaigsFile) {
        initializeGraph(); // setup the graph
        Random r = new Random();
        
        for(int i = 0; i < 1000; i++) {
            randomGaigs(9, 14);
            System.out.print("Trying: " + (i + 1));
            
            try {
                organizeGraph();
            } catch(IOException e) {
                System.out.println("Error: " + e.toString());
            }
            
            if( ! gaigsIsOverlap())
                break;
        }
        
        try {
            PrintWriter out = new PrintWriter(new FileWriter(gaigsFile));
            //fout.write(data);
            gaigs(out, r.nextInt(num_nodes)); //goodStartNode);
            out.close();
        } catch(IOException e) {
            System.out.println("Error: " + e.toString());
        }
        //showGraph();
    }
    
    void dropInt(int[] array, int item, int num) {
        for(int i = item; i < num - 1; i++) {
            array[i] = array[i + 1];
        }
    }
    
    public void randomGaigs(int numNodes, int numEdges) {
        Random r = new Random();
        int ri;
        
        initializeGraph(); // setup the graph
        
        directed = false;
        weighted = true;
        
        if(numEdges > (numNodes * (numNodes + 1)) / 2)
            numEdges = (numNodes * (numNodes + 1)) / 2;
        if(numEdges < numNodes - 1)
            numEdges = numNodes - 1;
        
        // Add a bit of random extra nodes and edges
        ri = r.nextInt(3);
        numNodes += ri;
        numEdges += ri;
        
        
        //Activate the correct number of nodes
        for(int x = 0; x < numNodes; x++) {
            my_nodeset[x].setChar(translateIndexChar(x));
            my_nodeset[x].activate();
            num_nodes++;
        }
        
        /********************************/
        /**	Make a spanning tree	*/
        /********************************/
        int tmp, left, tmp2;
        int[] free = new int[numNodes];
        int[] join = new int[numNodes];
        
        // load the free array with all the nodes (verticies)
        for(int i = 0; i < numNodes; i++) {
            free[i] = i; //my_nodeset[i].getChar();
        }
        
        // choose a random node to begin with
        join[0] = r.nextInt(numNodes);
        dropInt(free, join[0], numNodes);
        left = numNodes - 1;
        
        while(left > 0) {
            tmp = r.nextInt(left);
            tmp2 = r.nextInt(numNodes - left);
            my_edgeset[join[tmp2]][free[tmp]].activate();
            my_edgeset[join[tmp2]][free[tmp]].setWeight(r.nextInt(8) + 1);
            my_edgeset[free[tmp]][join[tmp2]].activate();
            my_edgeset[free[tmp]][join[tmp2]].setWeight(
                    my_edgeset[join[tmp2]][free[tmp]].getWeight());
            join[numNodes - left] = free[tmp];
            dropInt(free, tmp, left);
            num_edges++;
            left--;
        }
        
        /********************************/
        /**	Add the shortcuts	*/
        /********************************/
        
        //numNodes numEdges
        int i = numEdges - numNodes + 1;
        int x, y;
        
        while(i > 0) {
            x = r.nextInt(numNodes - 1);
            y = r.nextInt(numNodes - x - 1) + x + 1;
            if(!my_edgeset[x][y].isActivated()) {
                my_edgeset[x][y].activate();
                my_edgeset[x][y].setWeight(r.nextInt(8) + 1);
                my_edgeset[y][x].activate();
                my_edgeset[y][x].setWeight(
                        my_edgeset[x][y].getWeight());
                i--;
                num_edges++;
            }
        }
    }
    
    public void showGraph() {
        for(int i = 0; i < num_nodes; i++) {
            System.out.println("Node: " + my_nodeset[i].getChar());
            for(int j = i; j < num_nodes; j++)
                if(my_edgeset[i][j].isActivated())
                    System.out.println(j);
        }
    }
    
        /* pre:  graph is set up randomly, given path difference to search for
         * post: returns node array offset of good starting node
         * or -1 if their are no good dijkstra examples
         */
    public int gaigsAcceptable(int diff) {
        for(int startNode = 0; startNode < num_nodes; startNode++) {
            if(gaigsBestPathDiff(startNode) >= diff) return startNode;
        }
        return -1;
    }
    
    public int gaigsBestPathDiff(int current) {
        int bestPathDiff = 0;
        int champ, close, newCost;
        int[] shortestPath = new int[num_nodes];
        
        // set up the nodes data
        for(int i = 0; i < num_nodes; i++) {
            my_nodeset[i].setClosed(false);
            my_nodeset[i].setCost(BIG_NUM);
            shortestPath[i] = 0;
        }
        my_nodeset[current].setCost(0);
        my_nodeset[current].setClosed(true);
        
        // dijkstra's with path length
        for(int i = 0; i < num_nodes - 1; i++) {
            // see if you can close a node
            champ = BIG_NUM;
            close = -1;
            for(int j = 0; j < num_nodes; j++) {
                if(!my_nodeset[j].isClosed() &&
                        my_nodeset[j].getCost() < champ) {
                    champ = my_nodeset[j].getCost();
                    close = j;
                }
            }
            if(close != -1) {
                my_nodeset[close].setClosed(true);
                current = close;
            }
            for(int j = 0; j < num_nodes; j++) {
                if(my_edgeset[current][j].isActivated()) {
                    newCost = (int) my_edgeset[current][j].getWeight() +
                            my_nodeset[current].getCost();
                    if(my_nodeset[j].getCost() > newCost) {
                        my_nodeset[j].setCost(newCost);
                        if(shortestPath[j] == 0) {
                            shortestPath[j] = shortestPath[current] + 1;
                        } else if(bestPathDiff < shortestPath[current]
                                + 1 - shortestPath[j]) {
                            bestPathDiff = shortestPath[current]
                                    + 1 - shortestPath[j];
                        }
                    }
                }
            }
        }
        return bestPathDiff;
    }
    
    /** prints a prim gaigs animation to a given filestream */
    public void gaigs(PrintWriter out, int startNode) {
        String new_closed = "#00FF00";
        String old_closed_n = "#AAFFAA";
        String old_closed_e = "#208220";
        String new_open_e = "#00A1F2";
        String old_open_n = "#C6E6F4";
        String new_open_n = "#61C3F4";
        String white = "#FFFFFF";
        String gray = "#999999";
        
        questionCollection questions = new questionCollection(out);
        fibQuestion fibQ = null;
        mcQuestion visitOrderQ = null;
        int lastQ = 0;
        int[] qWeight = { 4, 2, 4}; //, 1, 2, 2 };
        Random r = new Random();
        String visitOrder = "" + my_nodeset[startNode].getChar();
        
        int champV, champI = 0, champJ = 0;
        
        char startNodeChar = my_nodeset[startNode].getChar();
        int newCost, champ, close, current = startNode;
        
        String title = "Minimum spanning tree using Prim's Algorithm\n" +
                "Starting at node " + my_nodeset[startNode].getChar();
        
// 		out.print("VIEW SCALE 0.75\n");
// 		out.print("VIEW DOCS index.html\n");
        int[] prev = new int[num_nodes];
        
        // initialize the nodes and arrays
        for(int i = 0; i < num_nodes; i++) {
            my_nodeset[i].setClosed(false);
        }
        drawGaigsNetwork(out, title);
        //my_nodeset[startNode].setColor("green");
        my_nodeset[startNode].setHexColor(new_closed);
        my_nodeset[startNode].setClosed(true);
        for(int i = 0; i < num_nodes; i++) {
            if(my_edgeset[startNode][i].isActivated()
            && ! my_nodeset[i].isClosed()) {
                my_edgeset[startNode][i].setHexColor(new_open_e);
                my_edgeset[i][startNode].setHexColor(new_open_e);
                my_nodeset[i].setHexColor(new_open_n);
            }
        }
        
        
        fibQ = new fibQuestion(out, "1");
        fibQ.setQuestionText("Which node will be connected next when it is selected at line 8 of the pseudocode?");
        questions.addQuestion(fibQ);
        questions.insertQuestion(fibQ.getID());
        drawGaigsNetwork(out, title);
        
        my_nodeset[startNode].setHexColor(old_closed_n);
        for(int i = 0; i < num_nodes; i++) {
            if(my_edgeset[startNode][i].isActivated()
            && ! my_nodeset[i].isClosed()) {
                my_edgeset[startNode][i].setHexColor(gray);
                my_edgeset[i][startNode].setHexColor(gray);
                my_nodeset[i].setHexColor(old_open_n);
            }
        }
        
        for(int k = 2; k <= num_nodes; k++) {
            int edgesWeighed = 0;
            champV = BIG_NUM;
            for(int i = 0; i < num_nodes; i++) {
                if(my_nodeset[i].isClosed()) {
                    for(int j = 0; j < num_nodes; j++) {
                        if(my_edgeset[i][j].isActivated() &&
                                !my_nodeset[j].isClosed()) {
                            //my_edgeset[i][j].setColor("pink");
                            //my_edgeset[j][i].setColor("pink");
                            //my_edgeset[i][j].setHexColor(new_open_e);
                            //my_edgeset[j][i].setHexColor(new_open_e);
                            edgesWeighed++;
                            if(my_edgeset[i][j].getWeight() < champV) {
                                champV = (int) my_edgeset[i][j].getWeight();
                                champI = i;
                                champJ = j;
                            }
                        }
                        
                    }
                }
            }
            for(int i = 0; i < num_nodes; i++) {
                if(my_edgeset[champJ][i].isActivated()
                && ! my_nodeset[i].isClosed()) {
                    my_edgeset[champJ][i].setHexColor(new_open_e);
                    my_edgeset[i][champJ].setHexColor(new_open_e);
                    my_nodeset[i].setHexColor(new_open_n);
                }
            }
            
            my_nodeset[champJ].setClosed(true);
            //my_nodeset[champJ].setColor("green");
            //my_edgeset[champI][champJ].setColor("green");
            //my_edgeset[champJ][champI].setColor("green");
            my_nodeset[champJ].setHexColor(new_closed);
            my_edgeset[champI][champJ].setHexColor(new_closed);
            my_edgeset[champJ][champI].setHexColor(new_closed);
            visitOrder += my_nodeset[champJ].getChar();
            
            switch(lastQ) {
                case 0:
                    fibQ.setAnswer("" + my_nodeset[champJ].getChar());
                    break;
                case 2:
                    fibQ.setAnswer("" + edgesWeighed);
                    break;
            }
            
            if(k > num_nodes - 1) {
                lastQ = -1;
            } else {
                lastQ = gaigsWeightedRandom(r, 3, qWeight);
            }
            
            switch(lastQ) {
                case 0:
                    fibQ = new fibQuestion(out, new Integer(k).toString());
                    fibQ.setQuestionText("Which node will be connected next when it is selected at line 8 of the pseudocode?");
                    questions.addQuestion(fibQ);
                    questions.insertQuestion(fibQ.getID());
                    break;
                case 1:
                    visitOrderQ = new mcQuestion(out, new Integer(k).toString());
                    visitOrderQ.setQuestionText("Which is the correct order of addition of nodes to the minumim spanning tree over the entire execution of the algorithm?");
                    questions.addQuestion(visitOrderQ);
                    questions.insertQuestion(visitOrderQ.getID());
                    qWeight[1] = 0;
                    break;
                case 2:
                    fibQ = new fibQuestion(out, new Integer(k).toString());
                    fibQ.setQuestionText("How many edges will have their weights considered next in lines 5-7 of the pseudocode?");
                    questions.addQuestion(fibQ);
                    questions.insertQuestion(fibQ.getID());
                    qWeight[2]--;
                    break;
            }
            if(qWeight[1] != 0) qWeight[1] += 2;
                        /*
                        q = new fibQuestion(out, new Integer(k).toString());
                        q.setQuestionText("Which edge will be selected next? (eg DH)");
                        q.setAnswer("" + my_nodeset[champI].getChar() +
                                my_nodeset[champJ].getChar() + "\n" +
                                my_nodeset[champJ].getChar() +
                                my_nodeset[champI].getChar());
                        questions.addQuestion(q);
                        questions.insertQuestion(q.getID());
                         */
            
            drawGaigsNetwork(out, title);
            //
            //gaigsChangeEdgeColors("pink", "black");
            gaigsChangeHexEdgeColors(new_open_e, gray);
            gaigsChangeHexNodeColors(new_open_n, old_open_n);
            //my_edgeset[champI][champJ].setColor("blue");
            //my_edgeset[champJ][champI].setColor("blue");
            my_edgeset[champI][champJ].setHexColor(old_closed_e);
            my_edgeset[champJ][champI].setHexColor(old_closed_e);
            my_nodeset[champJ].setHexColor(old_closed_n);
        }
        
        // finish the visit order question
        if(visitOrderQ != null) {
            visitOrderQ.addChoice(gaigsStringExplode(visitOrder));
            if(r.nextInt(2) == 0) {
                visitOrder = visitOrder.substring(0, visitOrder.length() - 4) +
                        visitOrder.charAt(visitOrder.length() - 3) +
                        visitOrder.charAt(visitOrder.length() - 4) +
                        visitOrder.substring(visitOrder.length() - 2);
                visitOrderQ.addChoice(gaigsStringExplode(visitOrder));
            } else {
                visitOrderQ.addChoice(gaigsStringExplode(
                        visitOrder.substring(0, visitOrder.length() - 4) +
                        visitOrder.charAt(visitOrder.length() - 3) +
                        visitOrder.charAt(visitOrder.length() - 4) +
                        visitOrder.substring(visitOrder.length() - 2)
                        ));
            }
            visitOrderQ.addChoice(gaigsStringExplode(
                    visitOrder.substring(0, 1) +
                    visitOrder.substring(4) +
                    visitOrder.substring(1, 4)
                    ));
            visitOrderQ.addChoice(gaigsStringExplode(
                    visitOrder.substring(0, 5) +
                    visitOrder.substring(7) +
                    visitOrder.substring(5, 7)
                    ));
            visitOrderQ.setAnswer(1);
        }
        
        // set up the final spanning tree snapshot and output it to the file
        //gaigsChangeEdgeColors("black", "white");
        gaigsChangeHexEdgeColors(gray, white);
        drawGaigsNetwork(out, "Minimum Spanning Tree");
        questions.writeQuestionsAtEOSF();
    }
    
    // gaigsChangeHexNodeColors
    // Richard Teviotdale
    // goes throught the node array and changes any node with the first specified
    // hex color into the second specified hex color
    public void gaigsChangeHexNodeColors(String search, String replace) {
        for(int i = 0; i < num_nodes; i++) {
            if(my_nodeset[i].getHexColor().compareTo(search) == 0) {
                my_nodeset[i].setHexColor(replace);
            }
        }
    }
    
    
    // gaigsChangeHexEdgeColors
    // Richard Teviotdale
    // goes throught the edge array and changes any edges with the first specified
    // hex color into the second specified hex color
    public void gaigsChangeHexEdgeColors(String search, String replace) {
        for(int i = 0; i < num_edges; i++) {
            for(int j = 0; j < num_edges; j++) {
                if(my_edgeset[i][j].getHexColor() == search) {
                    my_edgeset[i][j].setHexColor(replace);
                }
            }
        }
    }
    
    // given a string
    // returns string with spaces between each char in original string
    public String gaigsStringExplode(String in) {
        String retString = "";
        if(in.length() > 0) {
            retString += in.charAt(0);
            for(int i = 1; i < in.length(); i++) {
                retString += " " + in.charAt(i);
            }
        }
        return retString;
    }
    
    // gaigsWeightedRandom
    // Richard Teviotdale
    // given a random object, the number of items to select randomly from and
    // an array with each items relative weight
    // returns a weighted random int from 0 to items - 1
    public int gaigsWeightedRandom(Random r, int items, int[] weights) {
        if(items < 2) return 0;
        Vector pool = new Vector(items * items);
        for(int i = 0; i < items; i++) {
            for(int j = 0; j < weights[i]; j++) {
                pool.add(new Integer(i));
            }
        }
        return ((Integer) pool.elementAt(r.nextInt(pool.size()))).intValue();
    }
    
    
    // gaigsChangeEdgeColors
    // Richard Teviotdale
    // goes throught the edge array and changes any edges with the first specified
    // color into the second specified color
    public void gaigsChangeEdgeColors(String search, String replace) {
        for(int i = 0; i < num_edges; i++) {
            for(int j = 0; j < num_edges; j++) {
                if(my_edgeset[i][j].getColor() == search) my_edgeset[i][j].setColor(replace);
            }
        }
    }
    
    // gaigsIsOverlap
    // Written by Richard Teviotdale
    // adjust with global constants NODE_TOEDGE_MIN EDGE_CENTER_MIN & EDGE_LENGTH_MIN
    // Checks to see if the defined graph has any nodes that fall within a parabola
    // calculated around every edge. (lower NODE_TOEDGE_MIN to decrease tolorance)
    // Checks to make sure all edge lengths meet a minimum length requirement.
    // Checks to make sure no edge centers are too close together.
    public boolean gaigsIsOverlap() {
        boolean reverse = false;
        double x1, y1, x2, y2, x3, y3, min, dist;
        double[] x = new double[num_edges];
        double[] y = new double[num_edges];
        int count = 0;
        
        for(int i = 0; i < num_nodes; i++) {
            for(int j = i + 1; j < num_nodes; j++) {
                if(my_edgeset[i][j].isActivated()) {
                    x1 = my_nodeset[i].getX();
                    x2 = my_nodeset[j].getX();
                    y1 = my_nodeset[i].getY();
                    y2 = my_nodeset[j].getY();
                    x[count] = (x1 + x2) / 2.0;
                    y[count] = (y1 + y2) / 2.0;
                    count++;
                    min = gaigsDistPoints(x1, y1, x2, y2);
                    
                    // make sure edge has some length
                    if(min < EDGE_LENGTH_MIN) {
                        System.out.println(" edge too short");
                        return ! reverse;
                    }
                    
                    min += NODE_TOEDGE_MIN;
                    for(int k = 0; k < num_nodes; k++) {
                        if(k != i && k != j) {
                            x3 = my_nodeset[k].getX();
                            y3 = my_nodeset[k].getY();
                            dist = gaigsDistPoints(x1, y1, x3, y3);
                            dist += gaigsDistPoints(x2, y2, x3, y3);
                            if(dist < min) {
                                System.out.println(" node too close to edge");
                                return ! reverse;
                            }
                        }
                    }
                }
            }
        }
        for(int i = 0; i < num_edges; i++) {
            for(int j = i + 1; j < num_edges; j++) {
                if(gaigsDistPoints(x[i], y[i], x[j], y[j]) < EDGE_CENTER_MIN) {
                    System.out.println(" edge centers too close");
                    return ! reverse;
                }
            }
        }
        System.out.print("\n");
        return reverse;
    }
    
    // calculates distance between two points
    public double gaigsDistPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    
    /** writes a gaigs snapshot to a PrintWriter filestream */
    public void drawGaigsNetwork(PrintWriter out, String comment) {
        out.print("VIEW SCALE 0.75\n");
        out.print("VIEW DOCS index.html\n");
        out.print("Network\n1\n" + comment + "\n***\\***\n");
        for(int n = 0; n < num_nodes; n++) {
            // print node number and coordinates
            out.print((n + 1) + " " +
                    my_nodeset[n].getX() + " " +
                    my_nodeset[n].getY() + "\n");
            for(int e = n + 1; e < num_nodes; e++) {
                if(my_edgeset[n][e].isActivated()
                && my_edgeset[n][e].getHexColor() != "#FFFFFF") {
                    out.print("\\" + my_edgeset[n][e].getHexColor());
                    out.print((e + 1) + "\n" + (int)my_edgeset[n][e].getWeight() + "\n");
                }
            }
            out.print("32767\n");
            out.print("\\" + my_nodeset[n].getHexColor());
            out.print(my_nodeset[n].getChar() + "\n");
        }
        
                /*
                for(int n = 0; n < num_nodes; n++)
                {
                        out.print((n + 1) + " " +
                                my_nodeset[n].getX() + " " +
                                my_nodeset[n].getY() + "\n");
                        int e = n;
                        while(e < num_nodes)
                        {
                                if(my_edgeset[n][e].isActivated() && my_edgeset[n][e].getColor() != "white")
                                {
                                        if(my_edgeset[n][e].getColor() == "red")
                                                out.print("\\R");
                                        else if(my_edgeset[n][e].getColor() == "blue")
                                                out.print("\\B");
                                        else if(my_edgeset[n][e].getColor() == "magenta")
                                                out.print("\\M");
                                        else if(my_edgeset[n][e].getColor() == "green")
                                                out.print("\\G");
                                        else if(my_edgeset[n][e].getColor() == "yellow")
                                                out.print("\\Y");
                                        else if(my_edgeset[n][e].getColor() == "pink")
                                                out.print("\\#FF9933");
                                        out.print((e + 1) + "\n" + (int)my_edgeset[n][e].getWeight() + "\n");
                                }
                                e++;
                        }
                        out.print("32767\n");
                        if(my_nodeset[n].getColor() == "red")
                                out.print("\\R");
                        else if(my_nodeset[n].getColor() == "green")
                                out.print("\\G");
                        else if(my_nodeset[n].getColor() == "yellow")
                                out.print("\\Y");
                        else if(my_nodeset[n].getColor() == "pink")
                                out.print("\\#FF9933");
                        else if(my_nodeset[n].getColor() == "pink2")
                                out.print("\\#FFCC33");
                        out.print(my_nodeset[n].getChar() + "\n"); // +
                                //my_nodeset[n].getText() + "\n");
                }
                 */
        out.print("***^***\n");
    }
    
    public static void main(String[] args) {
        VisualGraph myGraph = new VisualGraph(args[0]);
    }
    
    /****************************************************
     *         end of gaigs adaptations		    *
     ****************************************************/
    
}  //End class VisualGraph
