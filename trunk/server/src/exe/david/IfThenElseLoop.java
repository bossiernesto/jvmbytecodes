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

public class IfThenElseLoop extends IfThenLoop
{
    private Assignment elseBlock;

    static GAIGSarray array;
    static ShowFile show;

    public IfThenElseLoop()
    {
        super(); // takes care of loop header parameters

	elseBlock = new Assignment( this );
    }


    public void execute(String showfile, String pseudofile) throws IOException
    {

	createPHPfile( pseudofile );
	String docURL = "arrayPractice.html";

	show  = new ShowFile(showfile);
	array = new GAIGSarray( size ,false,
				name,"white", 0.1,0.1,0.9,0.9,0.07);

	for(int i=0; i<size; i++)
	    {
		array.set( a[i]+"", i, "white" );
        	array.setRowLabel(i+"", i);
	    }

	pseudofile = pseudofile.replaceAll( "\\\\", "/");
	String pseudoURL = "Loop.php?file=" + pseudofile + "&amp;vname=" + variable;


	try {

	    /*************************************************************************
	     *          First execution of the loop for question generation
	     **************************************************************************/

	    // backup the array's original values
	    int backup[] = new int[ size ];
	    for(int i=0; i<size; i++) backup[i] = a[i];

	    iterationCount = 0;
	    assignmentCount = 0;
	    valueChangedCount = 0;
	    for(int i=0; i<size; i++) valueChanged[i] = false;

	    for(int i = firstIndex;
		terminationRelOp.test(i,lastIndex);
		i = i + (direction == Direction.UP ? absoluteIncrement :
			 -absoluteIncrement) )
	    {
		assignmentCount++;

		if (bExp.eval( i ))
		{
		    int oldVal = a[ i + thenBlock.termOnLHS ];

		    a[ i + thenBlock.termOnLHS ] = thenBlock.operator.compute(
					      a[ i + thenBlock.firstTermOnRHS ],
					      ( thenBlock.twoTermsOnRHS ?
						a[ i + thenBlock.secondTermOnRHS] :
						thenBlock.secondTerm ) );

		    if ( oldVal != a[ i + thenBlock.termOnLHS ] )
		    {
			valueChangedCount++;
			valueChanged[ i + thenBlock.termOnLHS ] = true;
		    }
		    newValue[  i + thenBlock.termOnLHS ] = a[  i + thenBlock.termOnLHS ];

		}
		else
		{
		    int oldVal = a[ i + elseBlock.termOnLHS ];

		    a[ i + elseBlock.termOnLHS ] = elseBlock.operator.compute(
					      a[ i + elseBlock.firstTermOnRHS ],
					      ( elseBlock.twoTermsOnRHS ?
						a[ i + elseBlock.secondTermOnRHS] :
						elseBlock.secondTerm ) );

		    if ( oldVal != a[ i + elseBlock.termOnLHS ] )
		    {
			valueChangedCount++;
			valueChanged[ i + elseBlock.termOnLHS ] = true;
		    }
		    newValue[  i + elseBlock.termOnLHS ] = a[  i + elseBlock.termOnLHS ];


		}
		iterationCount++;


	    }


	    // revert the array to its original values
	    for(int i=0; i<size; i++) a[i] = backup[i];


	    /*************************************************************************
	     *          Generate questions about the array itself (line 1)
	     **************************************************************************/
	    question q1[] = Questions.generateLine1Questions( show, a, name );

	    /*************************************************************************
	     *          Generate questions about line 3
	     **************************************************************************/
	    question q3[] = Questions.generateLine3Questions( show, this );



	    /*************************************************************************
	     *          Generate questions about line 5 (boolean expression)
	     **************************************************************************/
	    question q5[] = Questions.generateBooleanExpressionQuestions( show, this );

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
		show.writeSnap("",docURL, pseudoURL + "&amp;line=3&amp;vvalue=-",
			       q3[ random.nextInt( q3.length ) ], array);
	    else
		show.writeSnap("",docURL, pseudoURL + "&amp;line=3&amp;vvalue=-", array);




	    if (direction == Direction.UP)
	    {
		for(int i = firstIndex;
		    terminationRelOp.test(i,lastIndex);
		    i = i + absoluteIncrement)

		    executeBody( i, docURL, pseudoURL, q3, q5 );
	    }
	    else // backward scanning
	    {
		for(int i = firstIndex;
		    terminationRelOp.test(i,lastIndex);
		    i = i - absoluteIncrement)

		    executeBody( i, docURL, pseudoURL, q3, q5 );
	    }

	    show.writeSnap("", docURL, pseudoURL + "&amp;line=100&amp;vvalue=-", array);

	    show.close();
	} catch (UnknownOperatorException e)
	    { System.out.println( "Unknown operator: " + e); }

    }

    private void executeBody( int i, String docURL, String pseudoURL, question[] q3, question[] q5)
	throws UnknownOperatorException, IOException
    {

	// select boolean expression question
	question q = null;
	if (random.nextInt(2) == 0)
	    if (bExp.eval( i ))
		q = q5[ random.nextInt(10000) %9];
	    else
		q = q5[ 9 + random.nextInt(10000) %9];


	if ( q==null )
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i,array);
	else
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=5&amp;vvalue=" + i,q,array);


	if (bExp.eval( i ))
	{
	    //array.setColor( i + thenBlock.termOnLHS, "#BBDDBB" );

	    if (  (iterationCount < 4) || // force question at every iteration
		  ( random.nextInt( 10000 )%10 < 6 ) )

		show.writeSnap("", docURL, pseudoURL + "&amp;line=6&amp;vvalue=" + i,
			       Questions.generateAssignmentQuestion(
					  show, this,
					  i + thenBlock.termOnLHS ),
			       array);

	    else

		show.writeSnap("", docURL, pseudoURL + "&amp;line=6&amp;vvalue=" + i,
					   array);


	    thenBlock.execute( i );
	    array.set( a[i+thenBlock.termOnLHS]+"", i+thenBlock.termOnLHS, ASSIGNED_COLOR );
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=9&amp;vvalue=" + i,array);
	    array.setColor( i + thenBlock.termOnLHS, "white" );

	    if (  (iterationCount < 4) || // force question on array
		  ( random.nextInt( 1000 )%3 == 0 ) )
		show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=" + i,
			       q3[ random.nextInt( 9 ) ], array);
	    else
		show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue="+ i, array);

	}
	else
	{
	    //array.setColor( i + elseBlock.termOnLHS, "#BBDDBB" );

	    if (  (iterationCount < 4) || // force question at every iteration
		  ( random.nextInt( 10000 )%10 < 6 ) )

		show.writeSnap("", docURL, pseudoURL + "&amp;line=8&amp;vvalue=" + i,
			       Questions.generateAssignmentQuestion(
					  show, this,
					  i + elseBlock.termOnLHS ),
			       array);

	    else

		show.writeSnap("", docURL, pseudoURL + "&amp;line=8&amp;vvalue=" + i,
					   array);


	    elseBlock.execute( i );
	    array.set( a[i+elseBlock.termOnLHS]+"", i+elseBlock.termOnLHS, ASSIGNED_COLOR );
	    show.writeSnap("", docURL, pseudoURL + "&amp;line=9&amp;vvalue=" + i,array);
	    array.setColor( i + elseBlock.termOnLHS, "white" );

	    if (  (iterationCount < 4) || // force question on array
		  ( random.nextInt( 1000 )%3 == 0 ) )
		show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue=" + i,
			       q3[ random.nextInt( 9 ) ], array);
	    else
		show.writeSnap("", docURL, pseudoURL + "&amp;line=3&amp;vvalue="+ i, array);

	}
    }

    public String toString()
    {
        return getHeaderString() + getBodyString() + getFooterString();
    }

    public String getBodyString()
    {
	String output = "\" 5      if " + bExp + "\",\n";
	output +=       "\" 6          " + thenBlock + "\",\n";
	output +=       "\" 7      else" + "\",\n";
	output +=       "\" 8          " + elseBlock + "\",\n";

        return output;
    }

    public String getFooterString()
    {
        return "\" 9   }\"" + "\n);\n";
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
