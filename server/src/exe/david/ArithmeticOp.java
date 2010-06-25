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


public class ArithmeticOp
{
    public String string;

    public ArithmeticOp(String s)
    {
        string  = s;
    }

    public String toString()
    {
        return string;
    }

    public String getEncoding()
    {
		if (string.equals( "+" ))
			return ( "plus" );
		else if (string.equals( "-" ))
			return ( "-" );
		else if (string.equals( "*" ))
			return ( "times" );
		else if (string.equals( "/" ))
			return ( "div" );

		return "invalid op";
	}

    public int compute(int op1, int op2) throws UnknownOperatorException
    {
        if (string.equals( "+" ))
            return (op1 + op2 );
        else if (string.equals( "-" ))
            return (op1 - op2);
        else if (string.equals( "*" ))
            return (op1 * op2);
        else if (string.equals( "/" ))
            return (op1 / op2);
        else throw new UnknownOperatorException( string );
    }

    public boolean equals( ArithmeticOp op )
    {
		return this.string.equals( op.string );
	}


}
