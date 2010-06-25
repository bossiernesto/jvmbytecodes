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

// ******************************
// File: JoeStackTest.java
// Author: Joseph Naps
// Date: 07-16-2005
// ******************************

package exe.stacktest;

import java.io.*;
import exe.*;

public class JoeStackTest
{
    // *****************************************************************
    // Takes in a series of stack push and pop commands at the command
    // line. The first command line arg is the path of the file you
    // wish to output the XML to.  To initiate a pop simply the string
    // "pop" is needed.  To initiate a push the string "pushx" is used
    // where x is what you wish to push on to the stack.  Keep in mind
    // that an exception will be thrown if a pop is called on an empty
    // stack.
    // *****************************************************************
    public static void main(String args[]) throws IOException
    {
	XMLfibQuestion fib;
	XMLtfQuestion tf;
	XMLmcQuestion mc;
	int qr = 0;
	ShowFile show = new ShowFile(args[0]);
	String pc = "";
	GAIGSstack stack = new GAIGSstack();
	for( int i = 1; i < args.length; i++)
	{
	    if(args[i].length() == 3) 
	    {		
		pc = "index.php?line=1";
		stack.pop();
	    }
	    else
	    {
		pc = "index.php?line=2";
		String s = args[i].substring(4);
		if(stack.size() % 2 == 0) stack.push(s,"red");
		else stack.push(s,"blue");
	    }
	    if( i % 3 == 0 && i + 1 < args.length )
	    {
		if( args[i+1].equals("pop"))
		{
		    fib = new XMLfibQuestion(show, new Integer(qr++).toString());
		    fib.setQuestionText("What value will be popped from the stack?");
		    fib.setAnswer((String)stack.peek());
		    show.writeSnap("Stack Test", null, pc, fib, stack);
		}
		else
		{
		    mc = new XMLmcQuestion(show, new Integer(qr++).toString());
		    mc.setQuestionText("What will the color of the next stack item be?");
		    mc.addChoice("red");
		    mc.addChoice("blue");
		    if( (stack.size() % 2) == 0) mc.setAnswer(1);
		    else mc.setAnswer(2);
		    show.writeSnap("Stack Test", null, pc, mc, stack);
		}
	    }
	    else show.writeSnap("Stack Test", null, pc, stack);
	}
	show.close();
    }
}

