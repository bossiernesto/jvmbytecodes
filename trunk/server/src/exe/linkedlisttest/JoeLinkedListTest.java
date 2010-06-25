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

// ****************************************************************************
// File: JoeLinkedListTest.java
// Author: Joseph Naps
// Date: 07-19-2005
// ****************************************************************************

package exe.linkedlisttest;

import java.io.*;
import exe.*;

public class JoeLinkedListTest
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
	ShowFile show = new ShowFile(args[0]);
	String pc = "";
	GAIGSlist list = new GAIGSlist();
	for( int i = 1; i < args.length; i++)
	{
	    if(args[i].length() == 3) 
	    {
		pc = "index.php?line=1";
		list.remove(list.size()-1);
	    }
	    else
	    {
		pc = "index.php?line=2";
		String s = args[i].substring(3);
		if (list.size() % 2 == 0) list.add(s,"red");
		else list.add(s,"blue");
	    }
	    if( i % 3 == 0 && i + 1 < args.length )
	    {
		if( args[i+1].equals("rem"))
		{
		    fib = new XMLfibQuestion(show, new Integer(qr++).toString());
		    fib.setQuestionText("What value will be removed from the list?");
		    fib.setAnswer((String)list.get(list.size()-1));
		    show.writeSnap("List Test", null, pc, fib, list);
		}
		else
		{
		    mc = new XMLmcQuestion(show, new Integer(qr++).toString());
		    mc.setQuestionText("What will the color of the next item be?");
		    mc.addChoice("red");
		    mc.addChoice("blue");
		    if( list.size() % 2 == 0 ) mc.setAnswer(1);
		    else mc.setAnswer(2);
		    show.writeSnap("List Test", null, pc, mc, list);
		}
	    }
	    else show.writeSnap("List Test", null, pc, list);
	}
	show.close();
    }
}

