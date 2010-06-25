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

package exe.david;

import java.io.*;
import exe.*;
import java.lang.Math.*;


/**
 * Write a description of class RelOp here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.*;

public class RelOp
{
    public String string;
    private static Random random;

    /**
     * Constructor for objects of class RelOp
     */
    public RelOp(String s)
    {
        string  = s;
        random = new Random();
    }


    public String toString()
    {
        return string;
    }

    public boolean test(int op1, int op2) throws UnknownOperatorException
    {
        if (string.equals( "<" ))
            return (op1 < op2 );
        else if (string.equals( ">" ))
            return (op1 > op2);
        else if (string.equals( ">=" ))
            return (op1 >= op2);
        else if (string.equals( "<=" ))
            return (op1 <= op2);
        else if (string.equals( "==" ))
            return (op1 == op2);
        else if (string.equals( "!=" ))
            return (op1 != op2);
        else throw new UnknownOperatorException( string );
    }

    public static RelOp getRandomRelOp()
    {
		int opId = random.nextInt( 6 );
		switch (opId) {
			case 0: return new RelOp( "<" );
			case 1: return new RelOp( "<=" );
			case 2: return new RelOp( ">" );
			case 3: return new RelOp( ">=" );
			case 4: return new RelOp( "==" );
			case 5: return new RelOp( "!=" );
			default: return new RelOp( "<" );
		}
	}

	public String getEncoding()
	{
		if (string.equals( "<" ))
			return ( "lt" );
		else if (string.equals( ">" ))
			return ( "gt" );
		else if (string.equals( ">=" ))
			return ( "gteq" );
		else if (string.equals( "<=" ))
			return ("lteq");
		else return string;
	}

}
