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

package exe.tn_dp_lcs_via_design_pattern;

public class Pair {
    private int x;
    private int y;
    private boolean in_solution;
    /** Creates a new instance of Pair */
    public Pair() {
  
    }
    
    public Pair(int x,int y) {
        this.x = x;
        this.y = y;
	in_solution = false;
    }
    
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void set_in_solution () {
	in_solution = true;
    }

    public boolean is_in_solution () {
	return in_solution;
    }

    public String toString() {
        return "("+x+","+y+")";
    }

}
