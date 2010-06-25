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

// File: finnish_hyphenation.java
// Author: Tuukka & Essi
// Date: October 2007
// Use: Generates command line input for Hyphen.java

package exe.finnish_hyphenation;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class finnish_hyphenation
{
    public static void main(String args[]) throws IOException
    {
	String[] params = new String[2];
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);
	
	// User has chosen to enter their own input data
	if("User-defined-above".equals((String)hash.get("Word to hyphenate")))
	{
	    params[1] = (String)hash.get("Enter your new word here or choose from pull-down list below");
	}
	// User has chosen word from pull-down list
	else
	{
	    params[1] = (String)hash.get("Word to hyphenate");
	}
	params[0] = args[0] + ".sho";
	
	if("Hyphenation ver. 1 (flow chart)".equals((String)hash.get("Choose the version of the program to run")))
	    HyphenVer1Flow.main(params);	// call the main method of the generation program
	else if("Hyphenation ver. 1 (pseudo-code)".equals((String)hash.get("Choose the version of the program to run")))
	    HyphenVer1Pseudo.main(params);	// call the main method of the generation program
	else if("Hyphenation ver. 2 (flow chart)".equals((String)hash.get("Choose the version of the program to run")))
	    HyphenVer2Flow.main(params);	// call the main method of the generation program
	else if("Hyphenation ver. 2 (pseudo-code)".equals((String)hash.get("Choose the version of the program to run")))
	    HyphenVer2Pseudo.main(params);	// call the main method of the generation program
    }
}
