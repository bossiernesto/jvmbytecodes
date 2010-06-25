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
import javax.swing.*;


/**
 * Write a description of class SimpleLoop here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */


public class Questions
{

    static Random random = new Random();

	/* given an array of integers, shuffles its elements
	 */
	private static void shuffle( int numbers[] )
	{
		for(int swaps=0; swaps < 500; swaps++)
		{
			int n1 = random.nextInt( numbers.length );
			int n2 = random.nextInt( numbers.length );
			int tmp;
			tmp = numbers[ n1 ];
			numbers[ n1 ] = numbers[ n2 ];
			numbers[ n2 ] = tmp;
		}
	}// shuffle method


    /* Given two integers k and n with k <= n, returns an array of
     * k randomly selected distinct integers between 0 and n-1
     * listed from smallest to largest
     */
    private static int[] pickKoutOfN( int k, int n )
    {
	boolean b[] = new boolean[ n ];
	for(int i=0; i<n; i++) b[i] = false;
	int count = 0;
	while ( count < k )
	{
	    int r = random.nextInt( n );
	    if ( b[r] != true )
	    {
		count++;
		b[r] = true;
	    }
	}
	int a[] = new int[ k ];
	int aIndex = 0, bIndex = 0;
	while ( bIndex < n )
	{
	    if ( b[ bIndex ] )
	    {
		a[ aIndex ] =  bIndex;
		aIndex++;
	    }
	    bIndex++;
	}
	
	return a;
    }// pickKoutOfN method

    /* Given two ordered lists of indices for true and false options
     * returns a combined, randomized list of indices in which false options have 
     * negative indices and true options have positive indices
     */
    private static int[] generateMSOptions( int t[], int f[] )
    {
	int n = t.length + f.length;
	int list[] = new int[ n ];
	
	for(int i=0; i<t.length; i++)
	    list[ i ] = t[i];
	for(int i=0; i<f.length; i++)
	    list[ t.length + i ] = -(f[i]+1);   // add 1 to distinguish between 0 and -0
	
	shuffle( list );
	
	return list;
    } // generateMSOptions method
    
    /* Given a showfile, question id, two lists of Strings (full list of true and false
     * options), the total number of options, and the number of true options,
     * returns a multiple-selection question where both the choice of options and the 
     * order in which they appear in the question are randomized
     */
    private static XMLmsQuestion generateMSquestion( ShowFile show, String id,
	String trueOptions[], String falseOptions[], int numOptions, int numCorrect )
	
    {
	int trueIndex[] = pickKoutOfN( numCorrect, trueOptions.length );
	int falseIndex[] = pickKoutOfN( numOptions - numCorrect, falseOptions.length );
	
	for(int i=0; i<trueIndex.length; i++)
	    System.out.print( trueIndex[i] + " ");
	System.out.println();

	for(int i=0; i<falseIndex.length; i++)
	    System.out.print( falseIndex[i] + " ");
	System.out.println();

	int options[] = generateMSOptions( trueIndex, falseIndex );

	for(int i=0; i<options.length; i++)
	    System.out.print( options[i] + " ");
	System.out.println();

	
	XMLmsQuestion q = new XMLmsQuestion( show, id );
	q.setQuestionText( "Check all true statements." );
	
	for(int i=0; i < options.length; i++)
	{
	    System.out.println( i + " " + options[i]);
	    if ( options[i] >= 0 )
	    {
		q.addChoice( trueOptions[ options[i] ] );
		q.setAnswer( i+1 );
	    }
	    else
		q.addChoice( falseOptions[ -options[i]-1 ] );
	}
	return q;
	
    }// generateMSquestion method
    
    public static boolean alreadyInArray( int[] a, int max, int val)
    {
	for( int i=0; i<max; i++ )
	    if ( a[i]==val ) return true;
	return false;
    }
    public static void 	generateRandomNumbersOtherThan( int val, int[] numbers )
    {
	numbers[0] = val;
	numbers[1] = val + 1 + random.nextInt(5);

	for(int i=2; i<numbers.length; i++)
	{
	    do
		numbers[i] = -20 + random.nextInt( 40 );
	    while ( alreadyInArray(numbers, i, numbers[i]) );
	}   
	shuffle( numbers );

    }

	/*************************************************************************
	*          Generate questions about the array itself (line 1)
	**************************************************************************/
	public static question[] generateLine1Questions( ShowFile show,
						int a[], String aname )
	{
		question q[] = new question[ 15 ];

		int count = 0;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 0 - 8 have to do with the length of the array on line 1
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( aname + "[] contains " + a.length + " locations." );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( aname + "[] contains " + (a.length + 1) + " locations." );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( aname + "[] contains " + (a.length - 1) + " locations." );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLfibQuestion( show, "l1-" + count );
		q[count].setQuestionText( "How many locations does " + aname + "[] contain? ");
		((XMLfibQuestion) q[count]).setAnswer( "" + a.length );
		count++;

		q[count] = new XMLmcQuestion( show, "l1-" + count );
		q[count].setQuestionText( "How many locations does " + aname + "[] contain? ");
		// one option:   a.length
		// other option: a.length - 1
		// last option:  a.length +1, +2, or -2
		int thirdOption = 1;
		switch ( random.nextInt( 3 ) )
		{
			case 1: thirdOption++; break;
			case 2: thirdOption -= 3;
			default: break;
		}
		int options[] = { 0, 1, 2 };
		shuffle( options );
		for(int option=0; option < 3; option++)
		{
			switch ( options[ option ] )
			{
				case 0:
					((XMLmcQuestion) q[count]).setAnswer( option+1 );
					((XMLmcQuestion) q[count]).addChoice( "" + a.length );
					break;
				case 1:
					((XMLmcQuestion) q[count]).addChoice( "" + ( a.length - 1) );
					break;
				default:
					((XMLmcQuestion) q[count]).addChoice( "" + ( a.length + thirdOption) );
					break;
			}
		}
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( "The value of the Java expression " +
							aname + ".length is " + a.length );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( "The value of the Java expression " +
									aname + ".length is " + (a.length + 1) );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		q[count].setQuestionText( "The value of the Java expression " +
									aname + ".length is " + (a.length - 1));
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLfibQuestion( show, "l1-" + count );
		q[count].setQuestionText( "What is the value of the following Java expression? " +
									aname + ".length");
		((XMLfibQuestion) q[count]).setAnswer( "" + a.length );
		count++;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 9 - 13 have to do with the value of one of the array elements
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		int element = random.nextInt( a.length );
		q[count].setQuestionText( "The value of " + aname + "[" + element +  "] is " +
			a[element] );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		element = random.nextInt( a.length );
		int element2 =  element==0 ? element+1 : element-1;
		q[count].setQuestionText( "The value of " + aname + "[" + element +  "] is " +
			a[element2] );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLtfQuestion( show, "l1-" + count );
		element = random.nextInt( a.length );
		element2 =  element==a.length-1 ? element-1 : element+1;
		q[count].setQuestionText( "The value of " + aname + "[" + element +  "] is " +
					a[element2] );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLfibQuestion( show, "l1-" + count );
		element = random.nextInt( a.length );
		q[count].setQuestionText( "What is the value of " + aname + "[" + element + "]?" );
		((XMLfibQuestion) q[count]).setAnswer( "" + a[ element ] );
		count++;

		q[count] = new XMLmcQuestion( show, "l1-" + count );
		element = random.nextInt( a.length );
		q[count].setQuestionText( "What is the value of " + aname + "[" + element + "]?" );
		// one option: a[element]
		// 2nd option: a[element-1] or undefined
		// 3rd option: a[element+1] or undefined
		int opt[] = { 0, 1, 2 };
		shuffle( opt );
		for(int option=0; option < 3; option++)
		{
			switch ( opt[ option ] )
			{
				case 0:
					((XMLmcQuestion) q[count]).setAnswer( option+1 );
					((XMLmcQuestion) q[count]).addChoice( "" + a[element] );
					break;
				case 1:
					if (element==0)
						((XMLmcQuestion) q[count]).addChoice( "undefined" );
					else
					    if ( a[element - 1] != a[element] )
					    	((XMLmcQuestion) q[count]).addChoice( "" + a[element - 1]);
					    else
					    	((XMLmcQuestion) q[count]).addChoice( "" + (a[element - 1] + 1));
					break;
				default:
					if (element==a.length-1)
						((XMLmcQuestion) q[count]).addChoice( "undefined" );
					else
						if ( a[element + 1] != a[element] )
					    	((XMLmcQuestion) q[count]).addChoice( "" + a[element + 1]);
					    else
							((XMLmcQuestion) q[count]).addChoice( "" + (a[element + 1] - 1));
					break;
			}
		}
		count++;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * question 14 has to do with the type of statement on line 1
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		String trueOptions[] = new String[15];
		trueOptions[0] = "Line 1 is a variable declaration.";
		trueOptions[1] = "Line 1 declares a one-dimensional array.";
		trueOptions[2] = "Line 1 performs an instantiation.";
		trueOptions[3] = "Line 1 performs an initialization.";
		trueOptions[4] = "Line 1 is an assignment statement.";
		trueOptions[5] = "Line 1 refers to a primitive type.";
		trueOptions[6] = aname + ".length is equal to " + a.length + ".";
		element = random.nextInt( a.length );
		trueOptions[7] = "The value of " + aname + "[" + element +  "] is " +
			a[element];
		trueOptions[8] = "Line 1 is NOT a method declaration.";
		trueOptions[9] = "Line 1 does NOT perform any method call.";
		trueOptions[10] = "Line 1 does NOT manipulate double numbers.";
		trueOptions[11] = "Line 1 is NOT a return statement.";
		trueOptions[12] = "Line 1 is NOT a loop statement.";
		trueOptions[13] = "Line 1 does NOT refer to any loop variable.";
		trueOptions[14] = "Line 1 does NOT declare a two-dimensional array.";

		String falseOptions[] = new String[15];
		falseOptions[0] = "Line 1 is a method declaration.";
		falseOptions[1] = "Line 1 perform a method call.";
		falseOptions[2] = "Line 1 manipulates double numbers.";
		falseOptions[3] = "Line 1 is a return statement.";
		falseOptions[4] = "Line 1 is a loop statement.";
		falseOptions[5] = "Line 1 refers to a loop variable.";
		falseOptions[6] = "Line 1 declares a two-dimensional array.";
		falseOptions[7] = aname + ".length is equal to " + (a.length-1) + ".";
		element2 = element==0 ? a[element+1] : a[element-1];
		if (element2 == element) element2++;
		falseOptions[8] = "The value of " + aname + "[" + element +  "] is " + element2;
		falseOptions[9] = "Line 1 is NOT a variable declaration.";
		falseOptions[10] = "Line 1 does NOT declare a one-dimensional array.";
		falseOptions[11] = "Line 1 does NOT perform any instantiation.";
		falseOptions[12] = "Line 1 does NOT perform an initialization.";
		falseOptions[13] = "Line 1 is NOT an assignment statement.";
		falseOptions[14] = "Line 1 does NOT refer to a primitive type.";	;
		int totalOptions = 3 + random.nextInt( 4 ); // # of options to choose from
		int correctOptions = Math.min( totalOptions, 1 + random.nextInt( 4 ) );
		q[count] = generateMSquestion( show, "l1-" + count, trueOptions, falseOptions,
		                               totalOptions, correctOptions );
		count++;

		return q;
	}// generateLine1Questions method


    /*************************************************************************
     *          Generate questions about the overall loop (line 3)
     **************************************************************************/
    public static question[] generateLine3Questions( ShowFile show, Loop loop )
    {
		question q[] = new question[ 13 ];
		int count = 0;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 0 - 2 have to do with the name of the loop variable
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "The name of the loop variable is " +  loop.variable );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "The name of the loop variable is " +  loop.name );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLfibQuestion( show, "l3-" + count );
		q[count].setQuestionText( "What is the name of the loop variable? ");
		((XMLfibQuestion) q[count]).setAnswer( loop.variable );
		count++;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 3 - 4 have to do with the direction of the loop
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "The loop variable is incremented during the execution of the loop." );
		((XMLtfQuestion) q[count]).setAnswer( loop.direction == Direction.UP );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "The loop variable is decremented during the execution of the loop." );
		((XMLtfQuestion) q[count]).setAnswer( loop.direction == Direction.DOWN );
		count++;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 5 - 8 have to do with the number of loop iterations
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLfibQuestion( show, "l3-" + count );
		q[count].setQuestionText( "From beginning to end of its execution, how many times is the body of the for loop executed in this program?" );
		((XMLfibQuestion) q[count]).setAnswer( "" + loop.iterationCount );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "From beginning to end of its execution, the body of the for loop is executed " + loop.iterationCount + " time(s) in this program." );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "From beginning to end of its execution, the body of the for loop is executed " + (loop.iterationCount+1) + " time(s) in this program." );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "From beginning to end of its execution, the body of the for loop is executed " + (loop.iterationCount-1) + " time(s) in this program." );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		 *
		 * questions 9 - 12 have to do with the number of array modifications
		 *
		 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

		q[count] = new XMLfibQuestion( show, "l3-" + count );
		q[count].setQuestionText( "How many array elements in total will be modified during " +
			"the execution of the loop?" );
		((XMLfibQuestion) q[count]).setAnswer( "" + loop.valueChangedCount );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "A total of " + loop.valueChangedCount + " array element(s) will " +
			"be modified during the execution of the loop?" );
		((XMLtfQuestion) q[count]).setAnswer( true );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "A total of " + (loop.valueChangedCount+1) + " array element(s) will " +
			"be modified during the execution of the loop?" );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		q[count] = new XMLtfQuestion( show, "l3-" + count );
		q[count].setQuestionText( "A total of " + (loop.valueChangedCount > 0 ?
		                           				   loop.valueChangedCount - 1  :
		                           				 loop.valueChangedCount + 1 )
		                           + " array element(s) will be " +
								     "modified during the execution of the loop?" );
		((XMLtfQuestion) q[count]).setAnswer( false );
		count++;

		return q;
	}// generateLine3Questions method

    public static question generateAssignmentQuestion( ShowFile show, Loop loop, 
						       int index )
    {
	question q[] = new question[ 10 ];
	int count = 0;
	
	int RHSvalue = loop.newValue[ index ];
	
	if ( loop.a[index] != RHSvalue )
	{
	    if (random.nextInt(2)==0)
	    {
		q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" +count );
		q[count].setQuestionText( "Executing this assignment statement will modify " +
					  "the value of one of the array elements." );
		((XMLtfQuestion) q[count]).setAnswer( true );
	    }
	    else
	    {
		q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
		q[count].setQuestionText( "Executing this assignment statement will leave " +
					  "the contents of the array unchanged." );
		((XMLtfQuestion) q[count]).setAnswer( false );	    
	    }
	}
	else
	{
	    if (random.nextInt(2)==0)
	    {
		q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
		q[count].setQuestionText( "Executing this assignment statement will modify " +
					  "the value of one of the array elements." );
		((XMLtfQuestion) q[count]).setAnswer( false );	    
	    }
	    else
	    {
		q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
		q[count].setQuestionText( "Executing this assignment statement will leave " +
					  "the contents of the array unchanged." );
		((XMLtfQuestion) q[count]).setAnswer( true );	    
	    }	    
	}
	count++;

	q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
	q[count].setQuestionText( "The value of the RHS of the assignment is " +
				  RHSvalue );
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;

	// duplicated to increase its likelihood
	q[count] = new XMLtfQuestion( show, "assmt-" + index + "-2-" + count );
	q[count].setQuestionText( "The value of the RHS of the assignment is " +
				  RHSvalue );
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;

	int r = 1 + random.nextInt( 5 );

	q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
	q[count].setQuestionText( "The value of the RHS of the assignment is " +
				  (RHSvalue + r) ); // add r
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;


	q[count] = new XMLtfQuestion( show, "assmt-" + index + "-" + count );
	q[count].setQuestionText( "The value of the RHS of the assignment is " +
				  (RHSvalue - r) ); // subtract r
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;

	q[count] = new XMLfibQuestion( show, "assmt-" + index + "-" + count );
	q[count].setQuestionText( "What is the value of the RHS of the assignment?");
	((XMLfibQuestion) q[count]).setAnswer( "" + RHSvalue );
	count++;

	q[count] = new XMLmcQuestion( show, "assmt-" + index + "-" + count );
	q[count].setQuestionText( "What is the value of the RHS of the assignment?");
	int options[] = new int[ 3 + random.nextInt( 5 )];
	generateRandomNumbersOtherThan( RHSvalue, options );
	for(int i=0; i<options.length; i++)
	{
	    if ( options[i] == RHSvalue)
		((XMLmcQuestion) q[count]).setAnswer( i+1 );
	    ((XMLmcQuestion) q[count]).addChoice( "" + options[i] );
	}
	count++;

	int qNum = random.nextInt( count );

	return q[ qNum ];
    }


    public static question[] generateBooleanExpressionQuestions( ShowFile show, Loop loop)
    {
	question q[] = new question[ 18 ];
	int count = 0;

	//========================================
	// questions for TRUE boolean expession

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The boolean expression evaluates to:");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 6 will be executed next.");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 6 will be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The following assignment statement (Line 6) will NOT be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	q[count] = new XMLfibQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "What is the value of the boolean expression?");
	((XMLfibQuestion) q[count]).setAnswer( "true" );
	count++;

	String trueOptions[] = new String[4];
	trueOptions[0] = "The boolean expression evaluates to true.";
	trueOptions[1] = "The boolean expression does NOT evaluate to false.";
	trueOptions[2] = "The assignment statement on Line 6 will be executed next.";
	trueOptions[3] = "The assignment statement on Line 6 will NOT be skipped.";
	String falseOptions[] = new String[4];
	falseOptions[0] = "The boolean expression evaluates to false.";
	falseOptions[1] = "The boolean expression does NOT evaluate to true.";
	falseOptions[2] = "The assignment statement on Line 6 will NOT be executed next.";
	falseOptions[3] = "The assignment statement on Line 6 will be skipped.";
	int totalOptions = 3 + random.nextInt( 3 ); // # of options to choose from
	int correctOptions = Math.min( totalOptions, 1 + random.nextInt( 3 ) );
	q[count] = generateMSquestion( show, "boolExp-" + count, trueOptions, falseOptions,
				       totalOptions, correctOptions );
	count++;


	// three additional questions for if/then/else loop
	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 8 will be executed next.");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 8 will be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	trueOptions = new String[6];
	trueOptions[0] = "The boolean expression evaluates to true.";
	trueOptions[1] = "The boolean expression does NOT evaluate to false.";
	trueOptions[2] = "The assignment statement on Line 6 will be executed next.";
	trueOptions[3] = "The assignment statement on Line 6 will NOT be skipped.";
	trueOptions[4] = "The assignment statement on Line 8 will NOT be executed next.";
	trueOptions[5] = "The assignment statement on Line 8 will be skipped.";
	falseOptions = new String[6];
	falseOptions[0] = "The boolean expression evaluates to false.";
	falseOptions[1] = "The boolean expression does NOT evaluate to true.";
	falseOptions[2] = "The assignment statement on Line 6 will NOT be executed next.";
	falseOptions[3] = "The assignment statement on Line 6 will be skipped.";
	falseOptions[4] = "The assignment statement on Line 8 will be executed next.";
	falseOptions[5] = "The assignment statement on Line 8 will NOT be skipped.";
	totalOptions = 3 + random.nextInt( 4 ); // # of options to choose from
	correctOptions = Math.min( totalOptions, 1 + random.nextInt( 4 ) );
	q[count] = generateMSquestion( show, "boolExp-" + count, trueOptions, falseOptions,
				       totalOptions, correctOptions );
	count++;

	//========================================
	// questions for FALSE boolean expession

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The boolean expression evaluates to:");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 6 will be executed next.");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	


	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 6 will be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The following assignment statement (Line 6) will NOT be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	

	q[count] = new XMLfibQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "What is the value of the boolean expression?");
	((XMLfibQuestion) q[count]).setAnswer( "false" );
	count++;

	falseOptions = new String[4];
	falseOptions[0] = "The boolean expression evaluates to true.";
	falseOptions[1] = "The boolean expression does NOT evaluate to false.";
	falseOptions[2] = "The assignment statement on Line 6 will be executed next.";
	falseOptions[3] = "The assignment statement on Line 6 will NOT be skipped.";
	trueOptions = new String[4];
	trueOptions[0] = "The boolean expression evaluates to false.";
	trueOptions[1] = "The boolean expression does NOT evaluate to true.";
	trueOptions[2] = "The assignment statement on Line 6 will NOT be executed next.";
	trueOptions[3] = "The assignment statement on Line 6 will be skipped.";
	totalOptions = 3 + random.nextInt( 3 ); // # of options to choose from
	correctOptions = Math.min( totalOptions, 1 + random.nextInt( 3 ) );
	q[count] = generateMSquestion( show, "boolExp-" + count, trueOptions, falseOptions,
				       totalOptions, correctOptions );
	count++;


	// three additional questions for if/then/else loop
	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 8 will be executed next.");
	((XMLtfQuestion) q[count]).setAnswer( true );
	count++;	

	q[count] = new XMLtfQuestion( show, "boolExp" + count );
	q[count].setQuestionText( "The assignment statement on Line 8 will be skipped.");
	((XMLtfQuestion) q[count]).setAnswer( false );
	count++;	

	falseOptions = new String[6];
	falseOptions[0] = "The boolean expression evaluates to true.";
	falseOptions[1] = "The boolean expression does NOT evaluate to false.";
	falseOptions[2] = "The assignment statement on Line 6 will be executed next.";
	falseOptions[3] = "The assignment statement on Line 6 will NOT be skipped.";
	falseOptions[4] = "The assignment statement on Line 8 will be NOT executed next.";
	falseOptions[5] = "The assignment statement on Line 8 will be skipped.";
	trueOptions = new String[6];
	trueOptions[0] = "The boolean expression evaluates to false.";
	trueOptions[1] = "The boolean expression does NOT evaluate to true.";
	trueOptions[2] = "The assignment statement on Line 6 will NOT be executed next.";
	trueOptions[3] = "The assignment statement on Line 6 will be skipped.";
	trueOptions[4] = "The assignment statement on Line 8 will be executed next.";
	trueOptions[5] = "The assignment statement on Line 8 will NOT be skipped.";
	totalOptions = 3 + random.nextInt( 4 ); // # of options to choose from
	correctOptions = Math.min( totalOptions, 1 + random.nextInt( 4 ) );
	q[count] = generateMSquestion( show, "boolExp-" + count, trueOptions, falseOptions,
				       totalOptions, correctOptions );
	count++;
	return q;
    }

}// Questions class
