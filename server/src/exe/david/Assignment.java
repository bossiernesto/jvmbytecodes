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
import java.util.*;

public class Assignment
{
    public boolean twoTermsOnRHS;  // two array accesses if true, else 1 access
    public int termOnLHS;          // 0->index, -1->index-1, 1->index+1
    public int firstTermOnRHS;     // same
    public int secondTermOnRHS;    // same
    public ArithmeticOp operator;  // operation on RHS
    public int secondTerm;         // if not an array access, then a constant

    private Loop loop;

    public Assignment(Loop loop)
    {

        this.loop = loop;

        // choose assignment parameters

        // choose whether RHS of assignment involves one or two array elements
        if ( ((loop.firstIndex==0) && (loop.lastIndex==loop.size-1)) ||
        	 ((loop.firstIndex==loop.size-1) && (loop.lastIndex==0)) )
        	 twoTermsOnRHS = false; // to avoid RHS of the form a[i] <op> a[i]
       	else
        	 twoTermsOnRHS = loop.random.nextBoolean();

        // choose index in LHS term
        termOnLHS = loop.getRandomIndexModifier();

        // choose index in first RHS term
        firstTermOnRHS = loop.getRandomIndexModifier();

        // choose index in second RHS term
        if (twoTermsOnRHS)
        {
            secondTermOnRHS = loop.getRandomIndexModifier();

            if (firstTermOnRHS == secondTermOnRHS ) // avoid same index in both RHS array accesses
            {
				if ( firstTermOnRHS == -1)
					secondTermOnRHS++;
				else if (firstTermOnRHS == 1)
					firstTermOnRHS--;
			  	else if ( ( (loop.direction == Direction.UP) &&
			  			    (loop.firstIndex > 0) )
			  			    ||
			  			  ( (loop.direction == Direction.DOWN) &&
			  			    (loop.lastIndex > 0) ) )
			  		firstTermOnRHS--;
			  	else if ( ( (loop.direction == Direction.UP) &&
			  			    (loop.lastIndex < loop.size -1) )
			  			    ||
			  			  ( (loop.direction == Direction.DOWN) &&
			  			    (loop.firstIndex < loop.size -1) ) )
			  		secondTermOnRHS++;
			  	else
			  	{
					System.out.println( "must remain unchanged because of array out of bounds exception");
				}
			}
		}
        else
            secondTerm = 1 + loop.random.nextInt(7);

         // choose RHS arithmetic operator
         int opId = loop.random.nextInt( 4 );
         switch ( opId ){
             case 0 :
                        operator = new ArithmeticOp( "+" );
                        break;
             case 1 :
                        operator = new ArithmeticOp( "-" );
                        break;
             case 2 :
                        operator = new ArithmeticOp( "*" );
                        if (!twoTermsOnRHS && (secondTerm==1))
                            secondTerm++; // avoid multiplying by 1
                        break;
             case 3 :
			            operator = new ArithmeticOp( "/" );
			            if (!twoTermsOnRHS && (secondTerm==1))
			                secondTerm++; // avoid dividing by 1
			            else if (twoTermsOnRHS)  // avoid dividing by 0
			            {	// remove zero's from array
							for(int i=0; i<loop.a.length; i++)
								if (loop.a[i]==0)
								{
									//System.out.println("eliminate zero");
									loop.a[i] += loop.random.nextInt(2)==0 ? 1 : -1;
								}
						}
                        break;
         }// switch
    }



    public String toString()
    {
        String output = loop.name + "[ " + loop.variable;
		output += getIndexModifier( termOnLHS );
		output += " ] = " + loop.name + "[ " + loop.variable;
		output += getIndexModifier( firstTermOnRHS );
		output += " ] " + operator + " ";
		if (twoTermsOnRHS)
		{
		      output += loop.name + "[ " + loop.variable;
		      output += getIndexModifier( secondTermOnRHS ) + " ];";
		}
		else
		{
		      output += secondTerm + ";";
        }
        return output;
    }


	public String getEncoding()
	{
		String output = loop.name + "[ " + loop.variable;
		output += getIndexModifierEncoding( termOnLHS );
		output += " ] = " + loop.name + "[ " + loop.variable;
		output += getIndexModifierEncoding( firstTermOnRHS );
		output += " ] " + operator.getEncoding()  + " ";
		if (twoTermsOnRHS)
		{
			  output += loop.name + "[ " + loop.variable;
			  output += getIndexModifierEncoding( secondTermOnRHS ) + " ];";
		}
		else
		{
			  output += secondTerm + ";";
		}
		return output;
    }

    private String getIndexModifierEncoding( int flag)
	    {
	        switch ( flag ) {
	            case -1:   return " - 1";
	            case 1:    return " plus 1";
	        }
	        return "";
    }

    private String getIndexModifier( int flag)
    {
        switch ( flag ) {
            case -1:   return " - 1";
            case 1:    return " + 1";
        }
        return "";
    }

    public boolean equals( Assignment a )
    {
		boolean same;

		if ( (twoTermsOnRHS) && (a.twoTermsOnRHS) )
		{
			same = ( (this.termOnLHS == a.termOnLHS) &&
				   	 (this.firstTermOnRHS == a.firstTermOnRHS) &&
					 (this.operator.equals( a.operator )) &&
			         (this.secondTermOnRHS == a.secondTermOnRHS) );
		}
		else if ( (!twoTermsOnRHS) && (!(a.twoTermsOnRHS)) )
		{
			same = ( (this.termOnLHS == a.termOnLHS) &&
			         (this.firstTermOnRHS == a.firstTermOnRHS) &&
			         (this.operator.equals( a.operator )) &&
			         (this.secondTerm == a.secondTerm) );
		}
		else same = false;

		return same;
	}

    public void execute( int i ) throws UnknownOperatorException
    {

	//System.out.println(" Element at index " + (i+termOnLHS) + " is set to "+
	//		     loop.a[ i + firstTermOnRHS] + operator.string +
	//     ( twoTermsOnRHS ? "" + loop.a[ i + secondTermOnRHS] : "" + secondTerm ) );
	
	loop.a[ i + termOnLHS ] = operator.compute( loop.a[ i + firstTermOnRHS ],
						    ( twoTermsOnRHS ? loop.a[ i + secondTermOnRHS] : secondTerm ) );
    }
}
