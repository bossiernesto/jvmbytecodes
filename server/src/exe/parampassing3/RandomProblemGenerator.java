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

/************************************************************************************/
/*                                                                                  */
/* Authors: Jessica Gowey & Orjola Kajo                                             */
/* Date: Fall 2003-Spring 2004                                                      */
/* Project: Using algorithum visualiastion to illistrate paramater                  */
/* passing techniques                                                               */
/*                                                                                  */
/* Discription: The RandomProblemGenerator Class is a driver class                  */
/*                                                                                  */
/************************************************************************************/

package exe.parampassing3;
 
import exe.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class RandomProblemGenerator {
    public static void main(String args[]) throws IOException {

	int input;
	BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    
	//sets the function name to NameAnd Macro
	ProblemGenerator.setFunctionName(2);

	//three file names and three ints are passed as arguments
	ProblemGenerator.setFileName(args[0], args[1], args[2]);
    
					     
	NameAndMacro problem1 = new NameAndMacro(Integer.parseInt(args[3]), 
					     Integer.parseInt(args[4]));
    	problem1.createProgram();
	

    } //main
} //RandomProblemGenorator
