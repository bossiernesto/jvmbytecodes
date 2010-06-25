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

package exe.david;

import java.io.*;
import exe.*;

import java.io.*;
import java.util.*;

public class Driver
{
    private static Loop loop;

    public static void main(String[] args) throws IOException
    {
	
	Random random = new Random();
	
	// randomly choose a type of loop
	int loopId = random.nextInt( 100000 ) % 3;
		
	switch (loopId) 
	{
	    case 0: loop = new SimpleLoop(); break;
	    case 1: loop = new IfThenLoop(); break;
	    case 2: loop = new IfThenElseLoop(); break;
	    // switch body is functional but without questions
	    //case 3: loop = new SwitchLoop(); break;
	}

        loop.execute(args[0], args[1]);
    }// main method
}// Driver class
