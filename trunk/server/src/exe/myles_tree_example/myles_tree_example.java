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

// file: myles_tree_example.java
// author: Andrew Jungwirth
// date: 5 June 2006

// This is the front-end program for the TreeExample.java program in this
// directory. It takes the command-line parameters from the server, sends the
// input-generator file to the XMLParameterParser to get the user's input, and
// then processes these inputs so they can be properly fed into 
// TreeExample.java.

package exe.myles_tree_example;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class myles_tree_example{
    public static void main(String args[]) throws IOException {
	// Send the XML file name from the server to the parser.
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	Vector params = new Vector();

	params.add(args[0] + ".sho");
	params.add((String)hash.get("Choose binary or general tree:"));
	params.add((String)hash.get("Enter name for root node:"));

	StringTokenizer more = 
	    new StringTokenizer((String)hash.get("Enter more operations (((addlx,y | addrx,y) | (addx,y) {add x to parent y - l and r for binary}) | remx | colx,y {color x hex string y})*"));
	while(more.hasMoreTokens()){
	    params.add(more.nextToken());
	}
    
	// Call the script-producing with the proper parameters.
	String[] string_params = new String[params.size()];
	TreeExample.main((String[])params.toArray((Object[])string_params));

    }
}
