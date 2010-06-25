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
 * Write a description of class SimpleLoop here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.*;

public class IntegerExpression
{
	public final static int MODULO_EXPRESSION = 0;    // (x*i+y) % z
	public final static int DIVIDE_EXPRESSION = 1;    // (x*i+y) / z
	public final static int SQRT_EXPRESSION   = 2;    // (int) (Math.sqrt(x*i+y))

	public int expressionType;


	public int x, y, z;

    private Loop loop;

    private int numCases;

    public IntegerExpression(Loop loop)
    {

        this.loop = loop;

        // choose type of boolean expression
        expressionType = loop.random.nextInt( 3 );

        switch ( expressionType) {

		case MODULO_EXPRESSION:
			x = 1 + loop.random.nextInt( 5 );
			y = loop.random.nextInt( 11 );
			z = 2 + loop.random.nextInt( 4 );     // between 2 and 5
			numCases = z;
			break;

		case DIVIDE_EXPRESSION:
			x = 1 + loop.random.nextInt( 3 );
			y = loop.random.nextInt( 8 );
			z = 5 + loop.random.nextInt( 3 );
			numCases = Math.min( 7, 1 + ( x*loop.size + y) / z);
			break;


	 	case SQRT_EXPRESSION:
			x = 1+loop.random.nextInt( 5 );
			y = loop.random.nextInt( 11 );
			numCases = Math.min( 7, (int) Math.sqrt( x*loop.size + y ) + 1);
			break;


		}// switch
    }



    public String toString()
    {
        String output = "( ";


        switch ( expressionType) {

			case MODULO_EXPRESSION:
				output += getLinearExpression() + " % " + z;
				break;

			case DIVIDE_EXPRESSION:
				output += getLinearExpression() + " / " + z;
				break;


			case SQRT_EXPRESSION:
				output += "(int) Math.sqrt" + getLinearExpression();
				break;

		}// switch

        output += " )";

        return output;
    }


	private String getLinearExpression()
	{
	 	String i = loop.variable;

		if ( (x==1) && (y==0) ) return i;
		else if (x==1)          return "(" + i + " + " + y + ")";
		else if (y==0)  		return "(" + x + "*" + i + ")";
		else                    return "(" + x + "*" + i + " + " + y + ")";

	}

	public int getNumCases()
	{
		return numCases;
	}

	public int eval( int i )
	{
		int value = 0;

		switch ( expressionType) {

			case MODULO_EXPRESSION:
				value = (x * i + y)  % z;
				break;

			case DIVIDE_EXPRESSION:
				value = (x * i + y)  / z;
				break;


			case SQRT_EXPRESSION:
				value = (int) Math.sqrt( x * i + y );
				break;

		}// switch

		return value;
	}
}
