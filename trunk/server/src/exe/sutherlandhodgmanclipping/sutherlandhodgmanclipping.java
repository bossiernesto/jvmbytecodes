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

// file: sutherlandhodgmanclipping.java


package exe.sutherlandhodgmanclipping;

import exe.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class sutherlandhodgmanclipping{
    public static void main(String args[]) throws IOException {
      // Send the XML file name from the server to the parser.
      Hashtable hash = XMLParameterParser.parseToHash(args[2]);
      String temp = (String)hash.get("Choose Mode:");
      String [] vals = temp.split("\\s+");
      
      
      //Use this line when we run locally otherwise leave as above
      //String [] vals = (args[1]).split("\\s+");
      
      String [] params = new String[vals.length + 1];
      params[0] = args[0] + ".sho";
      

      for(int i = 0; i < vals.length; ++i) {
        params[1 + i] = vals[i];
      }
      
      
      Clip.main(params);
    }
}
