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

// *********************************************************
// File: JoeQueueTest.java
// Author: Joseph Naps
// Date: 07-18-2005
// *********************************************************

package exe.queuetest;

import java.io.*;
import exe.*;

public class JoeQueueTest
{
    // ***************************************************************
    // Takes in a series of enqueue and dequeue commands at the
    // command line. The first command line arg is the path of the
    // file you wish to output the XML to.  To initiate a dequeue
    // simply the string "deq" is needed.  To initiate an enqueue the
    // string "enqx" is used where x is what you wish to enqueue.
    // Keep in mind that an exception will be thrown if a dequeue us
    // called on an empty queue.
    // ***************************************************************
    public static void main(String args[]) throws IOException
    {
	XMLfibQuestion fib;
	XMLtfQuestion tf;
	XMLmcQuestion mc;
	int qr = 0;
	ShowFile show = new ShowFile(args[0]);
	String pc = "";
	GAIGSqueue queue = new GAIGSqueue();
	for( int i = 1; i < args.length; i++)
	{
	    if(args[i].length() == 3) 
	    {
		pc = "index.php?line=1";
		queue.dequeue();
	    }
	    else
	    {
		pc = "index.php?line=2";
		String s = args[i].substring(3);
		if(queue.size() % 3 == 0) queue.enqueue(s, "red");
		else if(queue.size() % 3 == 1) queue.enqueue(s, "green");
		else queue.enqueue(s, "blue");
	    }
	    if( i % 2 == 0 && i + 1 < args.length )
	    {
		if( args[i+1].equals("deq") )
		{
		    fib = new XMLfibQuestion(show, new Integer(qr++).toString());
		    fib.setQuestionText("What value will be dequeued from the queue?");
		    fib.setAnswer((String)queue.peek());
		    show.writeSnap("Queue Test", null, pc, fib, queue);
		}
		else
		{
		    mc = new XMLmcQuestion(show, new Integer(qr++).toString());
		    mc.setQuestionText("What will the color of the next queue item be?");
		    mc.addChoice("red");
		    mc.addChoice("green");
		    mc.addChoice("blue");
		    if( queue.size() % 3 == 0 ) mc.setAnswer(1);
		    else if( queue.size() % 3 == 1 ) mc.setAnswer(2);
		    else mc.setAnswer(3);
		    show.writeSnap("Queue Test", null, pc, mc, queue);
		}
	    }
	    else show.writeSnap("Queue Test", null, pc, queue);
	}
	show.close();
    }
}

