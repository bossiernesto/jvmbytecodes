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

// File: stacktest_with_audio.java
// Author: Joseph Naps
// Date: 06-12-2006
// Use: Generates command line input for StackTest.java
// Audio added -- Joe's Dad - 06-15-2007

package exe.stacktest_with_audio;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class stacktest_with_audio
{
    public static void main(String args[]) throws IOException
    {
	String[] params;
	Hashtable hash = XMLParameterParser.parseToHash(args[2]);
	
	// User has chosen to randomly generate input data
	if("Random".equals((String)hash.get("Original Data")))
	{
	    params = new String[11];
	    for(int i = 1; i < params.length; i++) params[i] = "push" + (int)(Math.random()*10);
	}
	// Parse given input into proper format
	else
	{
	    String input = (String)hash.get("Enter sequence of operations here(\"pop\" to pop or \"pushx\" to push \"x\")");
	    StringTokenizer st = new StringTokenizer(input, " ");
	    int count = 1;
	    params = new String[st.countTokens()+1];
	    while(st.hasMoreTokens()) 
	    {
		params[count] = st.nextToken();
		count++;
	    }
	}
	params[0] = args[0] + ".sho";
	JoeStackTest.main(params);	// call the main method of the generation program
    }
}
