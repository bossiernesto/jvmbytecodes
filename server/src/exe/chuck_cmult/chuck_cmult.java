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

// file: chuck_knap.java;
// author: Tom Naps
// date: 10 July 2007

// This is the front-end program for Chuck Leska's Chain Multiplication
// Problem visualization.  It takes the command-line parameters from
// the server, of which in this case there are two -- the sho file
// name, the string for the array p[] which contains the dimension of the
// matrices to be multiplied - the number of rows and columns of the first 
// and the number of columns of the other matrices.  The elements for the
// array should be entered separated by blanks; e.g. "12 8 5 2" 

package exe.chuck_cmult; 
 
import exe.*;
import exe.XMLParameterParser;
import java.io.*;
import java.lang.*;
import java.util.*;

public class chuck_cmult {
    public static void main(String args[]) throws IOException {

    String[] params;
    
    Hashtable hash = XMLParameterParser.parseToHash(args[2]);

    params = new String[2];
    params[1] = (String)hash.get("The dimension array p");
    params[0] = args[0] + ".sho";
    CMDrv.main(params);
    }
}
