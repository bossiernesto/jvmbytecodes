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
 * Abstract class Loop - write a description of the class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */

import java.io.*;
import java.util.*;

public abstract class Loop
{

    // abstract method to execute the loop
    abstract public void execute(String showfile, String pseudofile) throws IOException;

    public static final int MAX_SIZE = 12;   // max number of elements in the array
    public static final int MIN_SIZE = 6;    // min number of elements in the array
    public static final int MAX_ABSOLUTE_VALUE = 10;  // array elements are randomly chosen between
        // the negative of this value and the value itself (inclusive)

    public static final String[] ARRAY_NAMES = {
        "a", "array", "table", "list", "vector", "column", "series", "data", "values", "numbers",
        "info" };

    public static final String[] VARIABLE_NAMES = {
        "i", "j", "k", "index", "position", "location", "subscript" };

    protected static final String ASSIGNED_COLOR = "#BBDDBB";

    // parameters of the array to be manipulated by the loop
    public int[] a;         // the array
    protected int size;        // the size of the array
    public String name;     // the array name

    // loop control parameters
    public String variable;       // the loop variable
    public Direction direction;   // direction of index scan (up or down)
    protected int firstIndex;        // first location to be modified
    protected int lastIndex;         // index used in termination condition
    protected RelOp terminationRelOp;
    protected int absoluteIncrement; //
    protected int increment;

    // loop body parameters
    public boolean mayAccessNextIndex;
    public boolean mayAccessPrevIndex;

    protected Random random;

    // instance variables used for questions
    public int iterationCount;
    public int valueChangedCount;
    public boolean valueChanged[];
    public int newValue[];

    public Loop()
    {
        random = new Random();    // random seed

        // choose array name and size
        name = getRandomArrayName();
        size = getRandomArraySize();

        // instantiate array
        a = new int[ size ];
	valueChanged = new boolean[ size ];
	newValue = new int[ size ];
        // initialise the array
        for(int i=0; i<size; i++)
	{
            a[ i ] = getRandomValue();
	    newValue[ i ] = a[ i ];
	}

        // choose name of the index variable
        variable = getRandomVariableName();

        // choose direction of array scan (up or down)
        direction = getRandomDirection();


        // choose whether the loop body accesses previous and/or next locations
        mayAccessNextIndex = random.nextBoolean();
        mayAccessPrevIndex = random.nextBoolean();

        // choose start and last indices

        if (direction == Direction.UP)
        {
            firstIndex = random.nextInt( 3 );
            if (mayAccessPrevIndex && (firstIndex==0))
                firstIndex++;

            lastIndex = size - (random.nextInt( 2 ) + 1);
            if (mayAccessNextIndex && (lastIndex == size-1))
                lastIndex--;
        }
        else  // scanning backwards
        {
            firstIndex = size - (random.nextInt( 2 ) + 1);
            if (mayAccessNextIndex && (firstIndex==size-1))
                firstIndex--;

            lastIndex = random.nextInt( 3 );
            if (mayAccessPrevIndex && (lastIndex == 0))
                lastIndex++;
        }

        // choose relational operator in termination condition
        if (direction == Direction.UP)
        {
            if (random.nextBoolean())
                terminationRelOp = new RelOp( "<=" );
            else
                terminationRelOp = new RelOp( "<" );
        }
        else
        {
            if (random.nextBoolean())
                terminationRelOp = new RelOp( ">=" );
            else
                terminationRelOp = new RelOp( ">" );
        }

        // choose the loop increment
        absoluteIncrement = 1 + random.nextInt( 3 );
        if (direction == Direction.UP)
            increment = absoluteIncrement;
        else
            increment = -absoluteIncrement;

        // displayArray();


    }// Loop constructor



    /**
     * Returns a random integer between MIN_SIZE and MAX_SIZE (inclusive)
     */
    public int getRandomArraySize()
    {
        return MIN_SIZE + random.nextInt( MAX_SIZE - MIN_SIZE + 1);
    }

    /**
     * Returns a random integer between -MAX_ABSOLUTE_VALUE
     * and MAX_ABSOLUTE_VALUE (inclusive)
     */
    public int getRandomValue()
    {
        return random.nextInt() % (MAX_ABSOLUTE_VALUE + 1);
    }

    /**
     * Returns a random direction of scanning (UP or DOWN)
     */
    public Direction getRandomDirection()
    {
        boolean UP = random.nextBoolean();
        if (UP)
            return Direction.UP;
        else
            return Direction.DOWN;
    }

    /**
     * Returns a name for the array randomly chosen from a static list
     */
    public String getRandomArrayName()
    {
        return ARRAY_NAMES[ random.nextInt( ARRAY_NAMES.length ) ];
    }

    /**
     * Returns a name for the loop variable randomly chosen from a static list
`     */
    public String getRandomVariableName()
    {
        return VARIABLE_NAMES[ random.nextInt( VARIABLE_NAMES.length ) ];
    }

    void displayArray()
    {
        for(int i=0; i<size; i++)
            System.out.print( a[ i ] + " " );
        System.out.println();
    }

    public String getHeaderString()
    {
        String output = "\"<b>&nbsp; Array Practice</b><br><br>\",\n";
        output += "\" 1   int[] " + name + " = " + getArrayValues() + ";\",\n";
	output += "\" 2\",\n";
        output += "\" 3   for(int " + variable + " = " + firstIndex + "; ";
        output += variable + " " + terminationRelOp + " " + lastIndex + "; ";
        output += variable + " = " + variable + " ";
        if (direction == Direction.UP)
            output += "+";
        else
            output += "-";
        output += " " + absoluteIncrement + ")\",\n";
        output += "\" 4   {\",\n";

        return output;
    }

    protected String getArrayValues()
    {

		String output = "{ " + a[0];

		for(int i=1; i<size; i++)
		    output += " , " + a[i] ;
        output += " }";
        return output;
	}

    public int getRandomIndexModifier()
	{
		int modifier;

		if ( mayAccessPrevIndex && mayAccessNextIndex)
		{	// could be 0, -1, or +1
			int rand = random.nextInt( 10000 ) % 3;
			switch (rand) {
				case 0: return -1;
				case 1: return 1;
				default: return 0;
			}
		}
		else if (mayAccessPrevIndex)
		{	// could be 0 or -1
			if (random.nextInt(1000)%2 == 0)
				return 0;
			else
				return -1;
		}
		else if (mayAccessNextIndex)
		{	// could be 0 or 1
			if (random.nextInt(1000)%2 == 0)
				return 0;
			else
				return 1;
		}
		else // must be 0
			return 0;
	}

    protected void createPHPfile( String pseudofile )
    {
    	// create the php file for the pseudocode pane
	try {
	    PrintWriter writer = new PrintWriter( new FileWriter( new File( pseudofile ) ) );

	    writer.write("<?php\n" + "$pgm = array(\n");

	    writer.write( this.encode() );
	    writer.write(
			 "for($i = 0; $i < count($pgm); $i++){\n" +
			 "if($i ==$line){\n" +
			 "print(\"<font color = 'red'>$pgm[$i]</font><br>\");\n" +
			 "}\n" +
			 "else\n" +
			 "print(\"$pgm[$i]<br>\");\n" +
			 "}\n" +
			 "?>" );
	    writer.close();
	} catch (IOException  e)
	    { javax.swing.JOptionPane.showMessageDialog( null, "IO exception in SimpleLoop constructor" );}
    }

    public String encode()
    {
	String s =  this.toString().replaceAll(  " < ", " &lt; ");
	s = s.replaceAll( " <= ", " &lt;= ");
	s = s.replaceAll( " > ", " &gt; ");
	s = s.replaceAll( " >= ", " &gt;= ");

	return s;
    }

}
