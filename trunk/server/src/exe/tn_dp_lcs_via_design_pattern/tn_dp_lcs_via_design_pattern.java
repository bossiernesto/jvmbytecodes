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

// file: tn_dp_lcs_via_design_pattern.java;
// author: Tom Naps
// date: 17 July 2007

// This is the front-end program for Christian Trefftz's re-casting of
// Chuck Leska's lcs program into the Template design pattern.  It
// takes the command-line parameters from the server, of which in this
// case there are three -- the sho file name and the two strings for
// which we seek a longest common subsequence. The source and
// destination string should be kept short in the interest of holding
// down the exponential tree size.

package exe.tn_dp_lcs_via_design_pattern;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class tn_dp_lcs_via_design_pattern {
    public static void main(String args[]) throws IOException {

	String[] params;
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);

	params = new String[3];
	params[1] = (String)hash.get("First string");
	params[2] = (String)hash.get("Second string");

	params[0] = args[0] + ".sho";

	DP_LCS.main(params);
    }
}
