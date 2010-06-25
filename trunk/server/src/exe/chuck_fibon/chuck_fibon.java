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

// file: chuck_fibon.java;
// author: Chuck Leska
// date: 11 July 2007

// This is the front-end program for Chuck Leska's Fibonacci number
// visualization.  It takes the command-line parameters from
// the server, of which in this case there are two -- the sho file
// name and a string which is the fibonacci number to calculate.

package exe.chuck_fibon;
 
import exe.*;
import exe.XMLParameterParser;
import java.io.*;
import java.lang.*;
import java.util.*;

public class chuck_fibon {
    public static void main(String args[]) throws IOException {

    String[] params;
    
    Hashtable hash = XMLParameterParser.parseToHash(args[2]);

    params = new String[2];
    params[1] = (String)hash.get("Which number to calculate?");
    params[0] = args[0] + ".sho";
    FibDrv.main(params);
    }
}
