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
// author: Chuck Leska
// date: 28 July 2009

// This is the front-end program for Chuck Leska's Typesetting
// Problem visualization.  It takes the command-line parameters from
// the server, of which in this case there are four -- the sho file
// name, one string for the array l[], a string for the
// maximum number of characters in a line, and a string for the number of
// characters between words on a line.  The elements for the
// arrays should be entered separated by blanks; e.g. "12 8 5 2 8 12 10"

package exe.chuck_typeset; 
 
import exe.*;
import exe.XMLParameterParser;
import java.io.*;
import java.lang.*;
import java.util.*;

public class chuck_typeset {
    public static void main(String args[]) throws IOException {

    String[] params;
    
    Hashtable hash = XMLParameterParser.parseToHash(args[2]);

    params = new String[4];
    params[1] = (String)hash.get("The array of word lengths");
    params[2] = (String)hash.get("The max characters per line");
    params[3] = (String)hash.get("The space between words");
    params[0] = args[0] + ".sho";
    TSDrv.main(params);
    }
}
