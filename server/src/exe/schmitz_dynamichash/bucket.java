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


/**
 * Write a description of class bucket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

package exe.schmitz_dynamichash;

public class bucket
{
    // instance variables - replace the example below with your own
    public int x[];
    public boolean isused[];
    public int thecolor;
    public int bd = 1;
    /**
     * Constructor for objects of class bucket
     */
    public bucket()
    {
        // initialise instance variables
        thecolor = -1;
        x = new int[2];
        isused = new boolean[2];
        x[0]=0;
        x[1]=0;
        isused[0]=false;
        isused[1]=false;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public boolean isfull()
    {
            return (isused[0] == true && isused[1] == true);
       
    }
    public boolean isempty() 
    {
        return (isused[0] == false && isused[1] == false);   
        
    }
    public void add_element(int y)
    {
       
        if ( isused[0] ) {
            /*if ( y < x[0] ) {
                x[1] = x[0];
                x[0] = y;
                isused[1] = true;
            } else {
                x[1] = y;  
                isused[1] = true;
                
            }*/
            x[1] = y;
            isused[1]=true;
        } else {
                x[0] = y;  
                isused[0] = true;            
            
        }
        sorty();
         return;
    }//assumes bucket is not full
     public void remove_element(int y)
    {
           if (isused[1] && x[1] == y) {
                isused[1] = false;
            } else if (isused[0] && x[0] == y) {
                isused[0] = false;
 
            }
            
            sorty();
            return;
    }   
    public void sorty() {
        if ( !isused[0] && isused[1])  {
               x[0] = x[1];
               isused[0] = true;
               isused[1] = false;
            
        } else if (isused[0] && isused[1]) {
            int temp = 0;
            if (x[0] > x[1]) {
                temp = x[0];
                x[0] = x[1];
                x[1] = temp;
                
                
            }
            
        }
        
    }
 
}
