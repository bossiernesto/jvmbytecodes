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

// file: myles_seq_align_local.java
// author: Tom Naps
// date: 10 July 2007

// This is the front-end program for Myles McNally's Sequence
// Alignment visualization.  It takes the command-line parameters from
// the server, of which in this case there are three -- the sho file
// name and the two strings for which we seek a longest common
// subsequence.

package exe.myles_seq_align_local;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class myles_seq_align_local {
    public static void main(String args[]) throws IOException {

	String[] params;
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	params = new String[3];
	params[1] = (String)hash.get("First string");
	params[2] = (String)hash.get("Second string");

	if (params[1].length() > 10)
	    params[1] = params[1].substring(0,9);
	if (params[2].length() > 10)
	    params[2] = params[2].substring(0,9);

	params[0] = args[0] + ".sho";

	Matcher2.main(params);
    }
}
