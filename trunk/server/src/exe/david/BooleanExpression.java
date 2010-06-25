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

public class BooleanExpression
{
    public final static int MODULO_EXPRESSION = 0;    // i % c > c', (i-1) % c == c', etc
    public final static int CONSTANT_EXPRESSION = 1;  // a[i+/-1] < c, etc.
    public final static int ICONSTANT_EXPRESSION = 2; // a[i+/-1] >= i + c, etc
    public final static int ACONSTANT_EXPRESSION = 3; // a[i+/-1] != a[i+/-1] + c, etc
    public int expressionType;

    
    public int LHSconstant;  // only used if expressionType is MODULO_EXPRESSION
    public int RHSconstant;

    public int termOnLHS;    // 0->index, -1->index-1, 1->index+1
    public int termOnRHS;    // 0->index, -1->index-1, 1->index+1


    public RelOp operator;

    private Loop loop;

    public BooleanExpression(Loop loop)
    {

        this.loop = loop;

        // choose type of boolean expression
        expressionType = loop.random.nextInt( 4 ) ;

	//expressionType = 0;
		// parameters common to all expressions
		operator = RelOp.getRandomRelOp();

		// choose index in LHS term
		termOnLHS = loop.getRandomIndexModifier();

        switch ( expressionType) {

	case MODULO_EXPRESSION:
	    LHSconstant = 2 + loop.random.nextInt( 4 );     // between 2 and 5
	    RHSconstant = 1+ loop.random.nextInt( LHSconstant-1 ); // between 1 and termOnLHS
	    break;

	case CONSTANT_EXPRESSION:
	    RHSconstant = 1 + loop.random.nextInt( 5 );        // between -5 and 5 except 0
	    if ( loop.random.nextBoolean() )
		RHSconstant *= -1;
	    break;


	case ICONSTANT_EXPRESSION:
	    RHSconstant = 1 + loop.random.nextInt( 5 );        // between -5 and 5 except 0
	    if ( loop.random.nextBoolean() )
		RHSconstant *= -1;
	    break;

	case ACONSTANT_EXPRESSION:
	    RHSconstant = 1 + loop.random.nextInt( 5 );        // between -5 and 5 except 0
	    if ( loop.random.nextBoolean() )
		RHSconstant *= -1;

	    // choose index in RHS term
	    termOnRHS = loop.getRandomIndexModifier();
	    
	    System.out.println( "LHS -> " + termOnLHS + "   RHS -> " + termOnRHS);
	    if (termOnLHS == termOnRHS ) // avoid same index in both RHS array accesses
		{
		    if ( termOnRHS == -1)
			termOnRHS++;
		    else if (termOnRHS == 1)
			termOnRHS--;
		    else if ( ( (loop.direction == Direction.UP) &&
				(loop.firstIndex > 0) )
			      ||
			      ( (loop.direction == Direction.DOWN) &&
				(loop.lastIndex > 0) )
			      ||
			      ( (loop.direction == Direction.DOWN) &&
				(loop.lastIndex == 0)              &&
				(loop.terminationRelOp.string.equals( ">" ) ) ) )
			termOnRHS--;
		    else if ( ( (loop.direction == Direction.UP) &&
				(loop.lastIndex < loop.size -1) )
			      ||
			      ( (loop.direction == Direction.UP) &&
				(loop.lastIndex == loop.size -1) &&
				(loop.terminationRelOp.string.equals( "<" ) ) )
			      ||
			      ( (loop.direction == Direction.DOWN) &&
				(loop.firstIndex < loop.size -1) ) )
			
			
			termOnRHS++;
		    else
			{
			    System.out.println("must remain unchanged because of array out of bounds exception");
			}
		}
	    break;
	}// switch
    }



    public String toString()
    {
        String output = "( ";


        switch ( expressionType) {

			case MODULO_EXPRESSION:
				if (termOnLHS == 0)
					output += loop.variable;
				else
					output += "(" + loop.variable + getIndexModifier( termOnLHS ) + ")";
				output += " % " + LHSconstant + " " + operator + " " + RHSconstant;
				break;

			case CONSTANT_EXPRESSION:
				output += loop.name + "[ " + loop.variable + getIndexModifier( termOnLHS );
				output += " ] " + operator + " " + RHSconstant;
				break;


			case 2:
				output += loop.name + "[ " + loop.variable + getIndexModifier( termOnLHS );
				output += " ] " + operator + " " + loop.variable;
				if (RHSconstant>0)
					output += " + " + RHSconstant;
				else
					output += " - " + (- RHSconstant);
				break;

			case 3:
				output += loop.name + "[ " + loop.variable + getIndexModifier( termOnLHS );
				output += " ] " + operator + " ";
				output += loop.name + "[ " + loop.variable + getIndexModifier( termOnRHS );
				output += " ]";
				if (RHSconstant>0)
					output += " + " + RHSconstant;
				else
					output += " - " + (- RHSconstant);
				break;
		}// switch

        output += " )";

        return output;
    }


    public boolean eval(int i)
    {
	try {
	    switch ( expressionType) {

	    case MODULO_EXPRESSION:
		
		return operator.test( (i+termOnLHS) % LHSconstant, RHSconstant );
		
	    case CONSTANT_EXPRESSION:
		
		return operator.test( loop.a[i + termOnLHS], RHSconstant );
		
	    case ICONSTANT_EXPRESSION:
		
		return operator.test( loop.a[i + termOnLHS], i + RHSconstant );
		
	    case ACONSTANT_EXPRESSION:
		
		return operator.test( loop.a[i + termOnLHS], loop.a[i + termOnRHS] + RHSconstant );
		
	    }// switch
	} catch (Exception e)
	    { System.out.println( "Unknown operator in eval(): " + e); }
	
	return true;
    }
    
    private String getIndexModifier( int flag )
    {
        switch ( flag ) {
            case -1:   return " - 1";
            case 1:    return " + 1";
        }
        return "";
    }
}
