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

// file: exe.christian_tree234;
// author: Tom Naps
// date: 27 June 2007

// This is the front-end program for Christian Trefftz's 234-tree
// program.  It takes the command-line parameters from the server, of
// which in this case there is only one -- the sho file name.  Between
// 10 and 14 integers are then randomly generated for input into the
// tree

package exe.christian_tree234;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class christian_tree234{
    public static void main(String args[]) throws IOException {

	String [] params = new String[10 + ((int) (Math.random() * 5))];

	params[0] = args[0] + ".sho";

	for(int i = 1; i < params.length; i++) params[i] = "" + ((int)(Math.random()*100)+1);

	Tree234Mod.main(params);
    }
}
