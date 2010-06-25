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

package exe.chuck_knap; 
 
import exe.*;
import exe.ShowFile;
import java.io.*;
import java.util.*;

public class KDrv {
    public static void main(String[] args) throws IOException {
        // the arguments are the showFile name, the array c, the array a and the max weight b
        //the array elements are presented as a string separated by a blank space
        // such as "5 4 3 2"
        String cStr = args[1];
        StringTokenizer tokens = new StringTokenizer(cStr);
        int cnt = tokens.countTokens();
        int[] c = new int[cnt];
        int idx = 0;
        while (tokens.hasMoreTokens()) {
            c[idx] = Integer.parseInt(tokens.nextToken());
            idx++;
        }
        String aStr = args[2];
        tokens = new StringTokenizer(aStr);
        cnt = tokens.countTokens();
        int[] a = new int[cnt];
        idx = 0;
        while (tokens.hasMoreTokens()) {
            a[idx] = Integer.parseInt(tokens.nextToken());
            idx++;
        }
        int b = Integer.parseInt(args[3]);
        ShowFile show = new ShowFile(args[0]);
        Knapsack k = new Knapsack(c, a, b, show);
        k.recur(c.length, b);
        k.solve();
        
        show.close();
    }
}
