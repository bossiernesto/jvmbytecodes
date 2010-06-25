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

package exe.chuck_lcs;

import exe.*;
import exe.ShowFile;
import java.io.*;

public class LcsDrv {
    public static void main(String[] args) throws IOException {
        ShowFile show = new ShowFile(args[0]);
        //String xStr = "GTTCCTAATA";
        //String yStr = "CGATAATTGAGA";
        String xStr = args[1];
        String yStr = args[2];
        char[] x = new char[xStr.length()];
        char[] y = new char[yStr.length()];
        for (int i = 0; i < xStr.length(); i++)
            x[i] = xStr.charAt(i);
        for (int j = 0; j < yStr.length(); j++)
            y[j] = yStr.charAt(j);
        lcseq l = new lcseq(x, y, show);
        l.recur(x.length-1, y.length-1);
        l.dynProgLCS();
        show.close();
    }
}
