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

// ***********************************************************
// File: JoeArrayTest.java
// Author: Joseph Naps
// Date: 07-19-2005
// ***********************************************************

package exe.arraytest;

import java.io.*;
import exe.*;

public class JoeArrayTest
{
    // ***************************************************************
    // Takes in a series of add and remove commands at the command
    // line.  The first arg is the file that the XML will be output
    // to.  After that "rem" is used to remove the last item from the
    // list and "addx" is used to add x to the end of the list.
    // ***************************************************************
    public static void main(String args[]) throws IOException
    {
	XMLfibQuestion fib;
	XMLtfQuestion tf;
	XMLmcQuestion mc;
	int qr = 0;
	int numItems = 0;
	ShowFile show = new ShowFile(args[0]);
	String pc = "";
	GAIGSarray list = new GAIGSarray(10);
	for( int i = 1; i < args.length; i++)
	{
	    if(args[i].length() == 3) 
	    {
		pc = "index.php?line=1";
		list.set(null, numItems-1);
		list.setRowLabel(null, numItems-1);
		numItems--;
	    }
	    else
	    {
		pc = "index.php?line=2";
		String s = args[i].substring(3);
		if(numItems % 2 == 0) list.set(s, numItems, "red");
		else list.set(s, numItems, "blue");
		list.setRowLabel((new Integer(numItems)).toString(), numItems);
		numItems++;
	    }
	    if( i % 3 == 0 && i + 1 < args.length )
	    {
		if( args[i+1].equals("rem") )
		{
		    fib = new XMLfibQuestion(show, new Integer(qr++).toString());
		    fib.setQuestionText("What value will be removed from the list?");
		    fib.setAnswer((String)list.get(numItems-1));
		    show.writeSnap("Array Test", null, pc, fib, list);
		}
		else
		{
		    mc = new XMLmcQuestion(show, new Integer(qr++).toString());
		    mc.setQuestionText("What will the color of the next item be?");
		    mc.addChoice("red");
		    mc.addChoice("blue");
		    if( numItems % 2 == 0 ) mc.setAnswer(1);
		    else mc.setAnswer(2);
		    show.writeSnap("Array Test", null, pc, mc, list);
		}
	    }
	    else show.writeSnap("Array Test", null, pc, list);
	}
	//show.writeSnap("Array Test", null, pc, null, list);
	show.close();
    }
}

