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

// file: exe.christian_dp;
// author: Tom Naps
// date: 2 July 2007

// This is the front-end program for Christian Trefftz's edit distance
// dynamic programming program.  It takes the command-line parameters
// from the server, of which in this case there are three -- the sho
// file name, the source string, and the destination.  The source and
// destination string should be kept short (<= 5chars) in the interest
// of holding down the exponential tree size.

package exe.christian_dp;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class christian_dp{
    public static void main(String args[]) throws IOException {

	String[] params;
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	params = new String[3];
	params[1] = (String)hash.get("Source string");
	params[2] = (String)hash.get("Destination string");

	params[0] = args[0] + ".sho";

	DP.main(params);
    }
}
