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


public class SimpleLoop extends Loop
{
    private Assignment assignment;


    static GAIGSarray array;
    static ShowFile show;

    public SimpleLoop()
    {
        super(); // takes care of loop header paramters
	assignment = new Assignment( this );
    }// constuctor

    public void execute(String showfile, String pseudofile ) throws IOException
    {

	createPHPfile( pseudofile );
	String docURL = "arrayPractice.html";

	show  = new ShowFile(showfile);
	array = new GAIGSarray( size ,false, name,"white", 0.2,0.2,0.8,0.8,0.07);

	for(int i=0; i<size; i++)
	    {
		array.set( a[i]+"", i, "white" );
        	array.setRowLabel(i+"", i);
	    }

	pseudofile = pseudofile.replaceAll( "\\\\", "/");
	String pseudoURL = "Loop.php?file=" + pseudofile + "&amp;vname=" + variable;


    try{


	/*************************************************************************
	*          First execution of the loop for question generation
	**************************************************************************/

	// backup the array's original values
	int backup[] = new int[ size ];

	for(int i=0; i<size; i++) backup[i] = a[i];

	iterationCount = 0;
	valueChangedCount = 0;
	for(int i=0; i<size; i++) valueChanged[i] = false;

	for(int i = firstIndex;
			terminationRelOp.test(i,lastIndex);
			i = i + (direction == Direction.UP ? absoluteIncrement : 
				 -absoluteIncrement) )
	{

		int oldVal = a[ i + assignment.termOnLHS ];
		a[ i + assignment.termOnLHS ] = assignment.operator.compute(
					    a[ i + assignment.firstTermOnRHS],
					      ( assignment.twoTermsOnRHS ?
						a[ i + assignment.secondTermOnRHS]
						 : assignment.secondTerm ) );

		if ( oldVal != a[ i + assignment.termOnLHS ] )
		{
			valueChangedCount++;
			valueChanged[ i + assignment.termOnLHS ] = true;
		}
		newValue[  i + assignment.termOnLHS ] = a[  i + assignment.termOnLHS ];

		iterationCount++;
	}

	// revert the array to its original values
	for(int i=0; i<size; i++) a[i] = backup[i];


	/*************************************************************************
	*          Generate questions about the array itself (line 1)
	**************************************************************************/
	System.out.println( "before q1");
	question q1[] = Questions.generateLine1Questions( show, a, name );

	System.out.println( "after q1");

	/*************************************************************************
	*          Generate questions about line 3
	**************************************************************************/

	question q3[] = Questions.generateLine3Questions( show, this );


	/*************************************************************************
	*          Generate questions about line 5 (assignment)
	**************************************************************************/

        // question q5[] = Questions.generateAssignmentQuestions( show, this );


	/*************************************************************************
	*          Second execution of the loop to build the showfile
	**************************************************************************/

	int qNum;


	// First snapshot --------------------------------------------------
	show.writeSnap("", docURL, pseudoURL + "&amp;line=1&amp;vvalue=-");

	if (  (iterationCount < 4) || // force question on array
	      ( random.nextInt( 10000 )%2 == 0 ) )
	{
	    if (random.nextInt( 1000 )%2 == 0)
		qNum = 14; // multiple selection question
	    else
		qNum = random.nextInt( q1.length );

	    show.writeSnap("", docURL, pseudoURL + "&amp;line=1&amp;vvalue=-", q1[ qNum ]);
	}

	// Second snapshot --------------------------------------------------
	// to give students opportunity to zoom/drag array into view
	show.writeSnap("", docURL, pseudoURL + "&amp;line=2&amp;vvalue=-", array);

	// Third snapshot --------------------------------------------------
	if (  (iterationCount < 4) || // force question on array
	      ( random.nextInt( 1000 )%2 == 0 ) )
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=-",
			   q3[ random.nextInt( q3.length ) ], array);
	else
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=-", array);

	
	int iteration  = 0;
	
	if (direction == Direction.UP)
	{
	    for(int i = firstIndex;
		terminationRelOp.test(i,lastIndex);
		i = i + absoluteIncrement)
	    {

		//array.setColor( i + assignment.termOnLHS, "red" );
		
		if (  (iterationCount < 4) || // force question at every iteration
		      ( random.nextInt( 10000 )%10 < 8 ) )
		    
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i, 
				   Questions.generateAssignmentQuestion( 
						   show, this,
						   i + assignment.termOnLHS ),
				   array);
		
		else
		    
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i, 
				   array);

		a[ i + assignment.termOnLHS ] = assignment.operator.compute(
						a[ i + assignment.firstTermOnRHS],
						( assignment.twoTermsOnRHS ?
						  a[ i + assignment.secondTermOnRHS]
						  : assignment.secondTerm ) );


		array.set( a[i+assignment.termOnLHS]+"", i+assignment.termOnLHS, ASSIGNED_COLOR );
		show.writeSnap("", docURL, pseudoURL + "&amp;line=6&amp;vvalue=" + i,array);

		array.setColor( i + assignment.termOnLHS, "white" );
		
		if (  (iterationCount < 4) || // force question on array
		      ( random.nextInt( 1000 )%2 == 0 ) )
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=" + i,
				   q3[ random.nextInt( 9 ) ], array);
		else
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue="+ i, array);
		

		iteration++;
	    }
	    //show.writeSnap("", docURL, pseudoURL + "&amp;line=100&amp;vvalue=-",array);
	}
	else // backward scanning
	{
	    for(int i = firstIndex;
		terminationRelOp.test(i,lastIndex);
		i = i - absoluteIncrement)
	    {
		//array.setColor( i + assignment.termOnLHS, "red" );

		if (  (iterationCount < 4) || // force question at every iteration
		      ( random.nextInt( 10000 )%10 < 8 ) )
		    
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i, 
				   Questions.generateAssignmentQuestion( 
						   show, this,
						   i + assignment.termOnLHS ),
				   array);
		
		else
		    
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i, 
				   array);

		a[ i + assignment.termOnLHS ] = assignment.operator.compute(
					a[ i + assignment.firstTermOnRHS],
					( assignment.twoTermsOnRHS ?
					  a[ i + assignment.secondTermOnRHS]
					  : assignment.secondTerm ) );

		array.set( a[i+assignment.termOnLHS]+"", i+assignment.termOnLHS, ASSIGNED_COLOR );
		show.writeSnap("", docURL, pseudoURL + "&amp;line=6&amp;vvalue=" + i,array);

		array.setColor( i + assignment.termOnLHS, "white" );
		
		if (  (iterationCount < 4) || // force question on array
		      ( random.nextInt( 1000 )%3 == 0 ) )
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=" + i,
				   q3[ random.nextInt( 9 ) ], array);
		else
		    show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue="+ i, array);

		iteration++;
	    }
	    //show.writeSnap("", docURL, pseudoURL + "&amp;line=0&amp;vvalue=-",array);
	}
	
	show.writeSnap("", docURL, pseudoURL + "&amp;line=100&amp;vvalue=-", array);

    } catch (UnknownOperatorException e)
	{ System.out.println( "Unknown operator: " + e); }
    
    show.close();
    }

    public String toString()
    {
        return getHeaderString() + getBodyString() + getFooterString();
    }

    public String getBodyString()
    {
        return "\" 5      " +assignment.toString() + "\",\n";
    }

    public String getFooterString()
    {
        return "\" 6   }\"" + "\n);\n";
    }


    private String getIndexModifier( int flag )
    {
        switch ( flag ) {
	case -1:   return " - 1";
	case 1:    return " + 1";
        }
        return "";
    }

    // not used anymore
    public String getPseudoURL( int line, String value)
    {
	String URL = "SimpleLoop.php" + "?line=" + line;

	// fixed substring
	URL += "&amp;var[an]=" + name;
	URL += "&amp;var[av]=" + getArrayValues();
	URL += "&amp;var[vn]=" + variable;
	URL += "&amp;var[fi]=" + firstIndex;
	URL += "&amp;var[li]=" + lastIndex;
	URL += "&amp;var[iv]=" + absoluteIncrement;
	URL += "&amp;var[to]=" + terminationRelOp.getEncoding();
	URL += "&amp;var[io]=" + ( increment>0 ? "plus" : "-" );
	URL += "&amp;var[a]="  + assignment.getEncoding();

	URL += "&amp;var[vv]=" + value;

	return URL.replaceAll(" ","_");
    }

}
