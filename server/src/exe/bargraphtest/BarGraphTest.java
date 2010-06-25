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

// *************************
// File: BarGraphTest.java
// Associated File(s): none
// Author: Joseph Naps
// Date: 07-19-2005
// *************************

package exe.bargraphtest;

import java.io.*;
import exe.*;

public class BarGraphTest
{
    // ****************************************************************
    // Takes in a series of ints as command line args.  The first args
    // is the file that the XML will be output to.  The args that
    // follow should all be non-negative ints that should be
    // represented as a bar graph.
    // ****************************************************************
    public static void main(String args[]) throws IOException
    {
	XMLfibQuestion fib;
	XMLtfQuestion tf;
	XMLmcQuestion mc;
	int qr = 0;
	PrintWriter pw = new PrintWriter(new FileWriter(new File(args[0])));
	XMLquestionCollection questions = new XMLquestionCollection(pw);
	pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n");
	pw.write("<!DOCTYPE show PUBLIC \"-//JHAVE//DTD GAIGS SHO//EN\" \"gaigs_sho.dtd\">"+"\n"+"\n");
	//	pw.write("<!DOCTYPE show SYSTEM \"gaigs_sho.dtd\">"+"\n"+"\n");
	pw.write("<show>"+"\n"+"\n");
	for( int i = 1; i < args.length; i++)
	{
	    pw.write("<snap>"+"\n");
	    pw.write("<title>bargraph</title>"+"\n");
	    if( i > 1 && ((Integer.parseInt(args[i]))<(Integer.parseInt(args[i-1]))))
		pw.write("<pseudocode_url>index.php?line=1</pseudocode_url>"+"\n");
	    else if( i > 1 && ((Integer.parseInt(args[i]))>(Integer.parseInt(args[i-1]))))
		pw.write("<pseudocode_url>index.php?line=2</pseudocode_url>"+"\n");
	    else
		pw.write("<pseudocode_url>index.php?line=3</pseudocode_url>"+"\n");
	    pw.write("<bargraph>"+"\n");
	    pw.write("<bounds x1=\"0.05\" y1=\"0.05\" x2=\"0.8\" y2=\"0.8\"/>");
	    for( int j = 1; j <= i; j++) pw.write("<bar magnitude=\""+args[j]+"\" color=\"#00FF00\"/>"+"\n");
	    pw.write("</bargraph>"+"\n");
	    if( i % 3 == 0 && i + 1 < args.length )
	    {
		tf = new XMLtfQuestion(pw, new Integer(qr++).toString());
		tf.setQuestionText("The next value to be inserted("+args[i+1]+") is less than the last value inserted("+args[i]+")?");
		tf.insertQuestion();
		questions.addQuestion(tf);
		if( Integer.parseInt(args[i+1]) <= Integer.parseInt(args[i]) ) tf.setAnswer(true);
		else tf.setAnswer(false);
	    }
       	    pw.write("</snap>"+"\n"+"\n");
	}
	questions.writeQuestionsAtEOSF();
	pw.write("</show>");
	pw.close();
    }
}
	    
