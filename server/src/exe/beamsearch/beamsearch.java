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

// file: beamsearch.java
// author: Andrew Jungwirth
// date: 31 May 2006

// This class uses the XMLParameterParser to add the user's inputs to a hash
// table and then sends these inputs to the BeamSearch.java program that 
// produces the visualization. This eliminates the previous need for a Perl
// script to parse the input generator inputs.

package exe.beamsearch;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class beamsearch{
    public static void main(String args[]) throws IOException {
	// Send the XML file name from the server to the parser.
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	String[] params = new String[5];

	// Set the first parameter as the file name from the server.
	params[0] = (args[0] + ".sho");
	// Set the remaining parameters with the values from the hash table.
	params[1] = (String)hash.get("Choose search graph:");
	params[2] = (String)hash.get("Choose beam width (B):");
	params[3] = (String)hash.get("Choose memory size (M):");
	params[4] = (String)hash.get("Choose level of detail:");

	// Call the script-producing with the proper parameters.
	AndrewBeamSearch.main(params);
    }
}

