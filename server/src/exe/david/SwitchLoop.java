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
import java.io.*;
import exe.*;

public class SwitchLoop extends Loop
{
	private Assignment[] assignments;
	private IntegerExpression iExp;
	private int numCases;

    static GAIGSarray array;
    static ShowFile show;

    public SwitchLoop( )
    {
        super(); // takes care of loop header paramters

        iExp = new IntegerExpression( this );

	numCases = iExp.getNumCases();

	assignments = new Assignment[ numCases ];
	Assignment prev = null;
	for(int i=0; i<assignments.length; i++)
	    {
		assignments[i] = new Assignment( this );
		if ( (i>0) && (assignments[i].equals( prev )) )
		    System.out.println( "cases could be combined" );
		prev = assignments[i];
	    }
    }


    public void execute(String showfile, String pseudofile) throws IOException
    {

	createPHPfile( pseudofile );

	show  = new ShowFile(showfile);
	array = new GAIGSarray( size ,false,
				name,"white", -0.1,-0.1,0.7,0.7,0.07);

	for(int i=0; i<size; i++)
	    {
		array.set( a[i]+"", i, "white" );
        	array.setRowLabel(i+"", i);
	    }

	pseudofile = pseudofile.replaceAll( "\\\\", "/");
	String pseudoURL = "Loop.php?file=" + pseudofile + "&amp;vname=" + variable;
	
	//System.out.println("PseudoURL = "  + pseudoURL);

	show.writeSnap("", null, pseudoURL + "&amp;line=1&amp;vvalue=-");

	show.writeSnap("", null, pseudoURL + "&amp;line=3&amp;vvalue=-", array);



	
        try{
            if (direction == Direction.UP)
		{
		    for(int i = firstIndex;
			terminationRelOp.test(i,lastIndex);
			i = i + absoluteIncrement)
			{
			    int value = iExp.eval( i );

			    show.writeSnap("", null, pseudoURL + "&amp;line=5&amp;vvalue=" + i, array);			    
			    for(int c = 0; c < numCases; c++)
				{
				    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+7) + "&amp;vvalue=" + i, array);
				    if ( c == value)
					{

					    array.setColor( i + assignments[c].termOnLHS, "#BBDDBB" );
					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+7) + "&amp;vvalue=" + i, array);					    
					    assignments[c].execute( i );
					    
					    array.set( a[i+assignments[c].termOnLHS]+"", i+assignments[c].termOnLHS, "#EEBB99" );

					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+8) + "&amp;vvalue=" + i, array);					    
					    
					    array.setColor( i + assignments[c].termOnLHS, "white" );

					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (5+2*(numCases+1)+1) + "&amp;vvalue=" + i, array);					    

					    show.writeSnap("", null, pseudoURL + "&amp;line=3&amp;vvalue=" + i, array);					    
					    break;
					}
				}
			    
			    
			}

			    
		}
            else // backward scanning
		{
		    for(int i = firstIndex;
			terminationRelOp.test(i,lastIndex);
			i = i - absoluteIncrement)
			{
			    int value = iExp.eval( i );

			    show.writeSnap("", null, pseudoURL + "&amp;line=5&amp;vvalue=" + i, array);			    
			    for(int c = 0; c < numCases; c++)
				{
				    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+7) + "&amp;vvalue=" + i, array);
				    if ( c == value)
					{

					    array.setColor( i + assignments[c].termOnLHS, "#BBDDBB" );
					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+7) + "&amp;vvalue=" + i, array);					    
					    assignments[c].execute( i );
					    
					    array.set( a[i+assignments[c].termOnLHS]+"", i+assignments[c].termOnLHS, "#EEBB99" );

					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (2*c+8) + "&amp;vvalue=" + i, array);					    
					    
					    array.setColor( i + assignments[c].termOnLHS, "white" );

					    show.writeSnap("", null, pseudoURL + "&amp;line=" + (5+2*(numCases+1)+1) + "&amp;vvalue=" + i, array);					    

					    show.writeSnap("", null, pseudoURL + "&amp;line=3&amp;vvalue=" + i, array);					    
					    break;
					}
				}
			    
			    
			}
		}
	    show.writeSnap("", null, pseudoURL + "&amp;line=-1&amp;vvalue=-", array);					    
	    show.close();

	}
	catch (UnknownOperatorException e)
	    { System.out.println( "Unknown operator: " + e); }
    }

    public String toString()
    {
        return getHeaderString() + getBodyString();
    }

    public String getBodyString()
    {

	String output = "\" 5      switch " + iExp + "\",\n";
	output +=       "\" 6      {\",\n";
	int lineNumber = 7;
	for(int i=0; i<numCases; i++)
	    {
		output += "\" " + lineNumber++ + ( (lineNumber<11) ? "         " : "        ") + "case " + i + ": " + assignments[i] + "\",\n";
		output += "\" " + lineNumber++ + ( (lineNumber<11) ? "         " : "        ") + "        break;\",\n";
	    }

	output += "\" " + lineNumber++ + ( (lineNumber<11) ? "      " : "     ") + " }\",\n";
	output += "\" " + lineNumber + ( (lineNumber<11) ? "   " : "  ") + " }\",\n";
	output += "\n);\n";
        return output;
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
