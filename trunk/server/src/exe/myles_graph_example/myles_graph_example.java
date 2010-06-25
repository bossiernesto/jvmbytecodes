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

// file: myles_graph_example.java
// author: Andrew Jungwirth
// date: 5 June 2006

// This is the front-end program for the GraphExample.java program in this 
// directory. It takes the command-line parameters from the server, sends the
// input-generator file to the XMLParameterParser to get the user's inputs, and
// then processes these inputs so they can be properly fed into 
// GraphExample.java.

package exe.myles_graph_example;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class myles_graph_example{
    public static void main(String args[]) throws IOException {
	// Send the XML file name from the server to the parser.
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	Vector params = new Vector();

	params.add(args[0] + ".sho");

	// User selected random data generation (option box option "Random").
	if(((String)hash.get("Select how graph is generated:")).equals("Random")){
	    Random rand = new Random();
	    String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", 
				 "J", "K", "L", "M", "N", "O" };

	    // Starting number of nodes in the range [3,6].
	    int nodes = rand.nextInt(4) + 3;
	    params.add("" + nodes + "");
	    // Starting number of edges in the range [4,8].
	    int edges = rand.nextInt(5) + 4;
	    params.add("" + edges + "");


	    int loops = rand.nextInt(10) + 7;
	    for(int i = 0; i < loops; i++){
		int choice = rand.nextInt(4);

		switch(choice){
		case 0:
		    params.add("add" + letters[nodes++]);
		    break;
		case 1:
		    params.add("rem" + letters[nodes-- - 1]);
		    break;
		case 2:
		    params.add("ade" + letters[rand.nextInt(nodes)] + 
			       "," + letters[rand.nextInt(nodes)]);
		    break;
		case 3:
		    params.add("col" + letters[rand.nextInt(nodes)] + 
			       ",FF3232");
		    break;
		}
	    }
	// User selected "User-defined" so get the user's inputs.
	}else{
	    params.add((String)hash.get("Enter starting number of nodes:"));
	    params.add((String)hash.get("Enter starting number of edges:"));

	    StringTokenizer more = 
		new StringTokenizer((String)hash.get("Enter more operations (addx | adex,y | remx | rmex,y {add or rem node x or edge x,y} | colx,y {color node x hex string y})*:"));
	    while(more.hasMoreTokens()){
		params.add(more.nextToken());
	    }
	}

	// Call the script-producing with the proper parameters.
	String[] string_params = new String[params.size()];
	GraphExample.main((String[])params.toArray((Object[])string_params));
    }
}
