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

// file: jvmbytecodes.java
// This is the front-end program for the Sort.java program in this 
// directory. It takes the command-line parameters from the server, sends the
// input-generator file to the XMLParameterParser to get the user's inputs, and
// then processes these inputs so they can be properly fed into 
// Sort.java.
package exe.jvmbytecodes;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * Entry point for the server program.
 */
public class jvmbytecodes {
	/*
	 * Calls the Driver program.
	 * @param args[0] ex: ../../src/exe/jvmbytecodes
	 * @param args[1] ex: Test.java
	 * @param args[2] ex: ../../src/exe/jvmbytecodes/1.xml
	 */
	public static void main(String args[]) throws IOException {
		System.out.println("jvmbytecodes.java args[0]: " + args[0] + " args[1]: " + args[1]);

		// Send the XML file name from the server to the parser.
		Hashtable hash = XMLParameterParser.parseToHash(args[2]);

		String[] params = new String[3];
		params[0] = args[0];
		params[1] = (String) hash.get("File name:");
		params[2] = (String) hash.get("Code to visualize:");

		Driver.main(params);
	}
}
