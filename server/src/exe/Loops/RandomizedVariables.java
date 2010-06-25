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

// package exe.nes_array 
package exe.Loops;
import java.util.*;

public class RandomizedVariables
{
        public final char[] RANDOM_CHARS = {'*', '$', '#', '!','~', 'X'};
        
        private Random random = new Random();

        public int randInt( int min, int max)
        {
                // returns a random int between min and max
        	if(min < 0 && max < 0){
        		int newMin = Math.abs(min);
        		int newMax = Math.abs(max);
        		return -(newMax + random.nextInt(newMin - newMax));
        	}else
                return min + random.nextInt(max - min);
        }

        public String randMathOperation()
        {
                // returns +, -, *, /, %
                int opId = random.nextInt( 5 );
                switch (opId) {
                    case 0: return "+";
                    case 1: return "-";
                    case 2: return "*";
                    case 3: return "/";
                    case 4: return "%";
                    default: return "+";
                }
        } 
        
      /*  public String randStatementMathOperation()
        {
                // returns +, -, *
                int opId = random.nextInt( 3 );
                switch (opId) {
                    case 0: return "+";
                    case 1: return "-";
                    case 2: return "*";
                    case 3: return "/";
                    case 4: return "%";
                    default: return "+";
                }
        }*/ 
        
        // returns + or -; used in for loop to determine if the count is incrementing or decrementing
        public String randIncDec()
        {
                int opId = random.nextInt( 2 );
                switch (opId) {
                    case 0: return "+";
                    case 1: return "-";
                    default: return "+";
                }
        }

        // returns <, <=, >, >=, ==, !=
        public String randRelOpIfs()
        {
            int opId = random.nextInt( 4 );
                switch (opId) {
                    case 0: return "<";
                    case 1: return "<=";
                    case 2: return ">";
                    case 3: return ">=";
                    //case 4: return "!=";
                    //case 5: return "==";
                    default: return "<";
                }

        }

        // returns <, <=, >, >= depending on dir 
        public String randRelOpFor(String dir)
        {
                int opId = random.nextInt( 2 );
                if( dir == "+"){
                    switch (opId) {
                        case 0: return "<";
                        case 1: return "<=";
                        default: return "<";
                    }// switch
                }else{
                    switch (opId) {
                        case 0: return ">";
                        case 1: return ">=";
                        default: return ">";
                    }// switch
                }// else
        }// randRelOpFor method

        // returns a string from the name array
        public String randName(String[] name)
        {              
                return name[ random.nextInt(name.length)];
        }

        // returns a char from the letter array
        public char randChar()
        {        
                return RANDOM_CHARS[ random.nextInt(RANDOM_CHARS.length)];
        }

        // returns either name1 or name2 randomly with either plus 0 , 1 or minus 1
        public String randNumIndex()
        {
                    int opId = random.nextInt( 3 );
                          switch (opId) {
                              case 0: return "";
                              case 1: return "+1";
                              case 2: return "-1";
                              default: return "";
                            }//switch
        }//randIndexName
        
        public String randVarName(String name, String name2)
        {
            int nameRand = random.nextInt( 2 );
                switch (nameRand) {
                    case 0: return  name ;
                    case 1: return  name2;
                    default: return name; 
                }//switch
        }
        //Randomly returns either name or name2 as the index variable
        public String nameIndex(String name, String name2)
        {
            int nameRand = random.nextInt( 12);
                switch (nameRand) {
                    case 0: return  name ;
                    case 1: return  name2;
                    default: return name; 
                }//switch
        }
}