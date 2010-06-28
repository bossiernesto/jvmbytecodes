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

package exe.jvmbytecodes;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

public class Driver {

    static final String TITLE = null; // no title
    static ArrayList<PseudoCodeDisplay> pseudoBytecodes[];//pseudo;
    static PseudoCodeDisplay pseudoSourceCode[];//realCode;
    static boolean success;
    static ShowFile show;
    static GAIGSstack runTimeStack;
    static Stack _runTimeStack = new Stack();
    static GAIGSstack heap;
    static ArrayList _heap;
    static int currentClass;
    static int questionID;
    static int numberOfLinesInJavaFile = 1;
    static int heapSize = 0;
    static int currentMethod = 1;
    static Class_[] classes;
    static final String CURRENT_FRAME_COLOR = "#990022";
    static GAIGSarray XMLstack;
    static int XMLstackSize = 0;

	/*
	 * Main driver for the client
	 * 
	 * args[0] is the full path and number for naming showfile 
	 * args[1] is the name of the Java source file or "" 
	 * args[2] is the contents of the Java file
	 */
	public static void main(String args[]) throws IOException {


		String file_contents = args[2];
		File pathname = new File("", args[0]);
		String fileName = (args[1].equals("") ? "Test.java" : args[1]);

		try {
			success = (pathname.mkdir());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage() + "here4");
			System.err.println("Error: " + e.getMessage() + "here2");
		}
		try {
			FileWriter fw = new FileWriter(args[0] + "/" + fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter outFile = new PrintWriter(bw);
			outFile.print(file_contents);
			outFile.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage() + "here1");
		}

		    //String[] tmp = { args[0] + ".sho", args[0], args[1] };
		    String[] tmp = { args[0], args[1] };

		show = new ShowFile(args[0] + ".sho", 5); // first argument is the script foo.sho
		
		classes = GenerateBytecodes.getClasses(tmp);

		// create visual stack and heap using the predetermined sizes
		heap = new GAIGSstack("Heap", "#999999", 0.01, 0.5, 0.3, 0.9, 0.15);
		runTimeStack = new GAIGSstack("Run Time Stack", "#999999", 0.01, 0.1, 0.3, 0.5, 0.15);

		// set current method and class
		currentClass = 0;		
		int index = 0;
        for(Method_ m : classes[0].methods)
        {
                if(m.name.equals("main"))
                {
                        currentMethod = index;
                        break;
                }
                index++;
        }
		
        //make the XML files
		GenerateXML.generateBytecodeXML();
		GenerateXML.generateSourceCodeXML(args[0], args[1]);

		pseudoBytecodes = new ArrayList[Driver.classes.length];
		for (int i=0; i<Driver.classes.length; i++)
			pseudoBytecodes[i] = new ArrayList<PseudoCodeDisplay>();
		pseudoSourceCode = new PseudoCodeDisplay[Driver.classes.length];
		
		//make the URI need for the display
		try {
			
			System.out.println("starting uri ");
			
			for (int i=0; i < Driver.classes.length; i++)
				for (int j = 0; j < Driver.classes[i].methods.size(); j++) {
					String signature="";
					for (int m=0; m<Driver.classes[i].methods.get(j).localVariableTable.length; m++)
						signature+=Driver.classes[i].methods.get(j).localVariableTable[m][2];
					signature = GenerateXML.replaceSlashWithDot(signature);
					System.out.println("sinature is: "+signature+" i: "+i+" j: "+j+" "+"exe/jvmbytecodes/"+Driver.classes[i].name+Driver.classes[i].methods.get(j).name+signature+".xml");
					pseudoBytecodes[i].add(new PseudoCodeDisplay("exe/jvmbytecodes/"+Driver.classes[i].name+Driver.classes[i].methods.get(j).name+signature+".xml"));
					System.out.println("found file");
				}
			System.out.println("completed uri ");

			for (int i=0; i < Driver.classes.length; i++)
				pseudoSourceCode[i] = (new PseudoCodeDisplay("exe/jvmbytecodes/" + Driver.classes[i].name + ".xml"));
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		//questionID
		questionID = 0;


		Frame_ f = new Frame_(currentMethod);
		_runTimeStack.push(f);

		// get a random color for the stack
		//String mainColor = getRandomColor();
		String mainColor = CURRENT_FRAME_COLOR;
		show.writeSnap(TITLE, MakeURI.doc_uri(-1, f), MakeURI.make_uri(-1, PseudoCodeDisplay.RED, f), runTimeStack);
		runTimeStack.push(classes[0].methods.get(1).name, mainColor);
		show.writeSnap(TITLE, MakeURI.doc_uri(-1, f), MakeURI.make_uri(-1, PseudoCodeDisplay.RED, f), runTimeStack);



		// begin interpreter
		Interpreter.interpret();

	   show.close();
		
	   // delete files in uid/<number>
	   //Runtime.getRuntime().exec( "rm -f " + args[0] + "/*" );

	}
	
	

	/*
	 * Generates a random string for a hex color in the format: "#000000"
	 */
	static String getRandomColor() {
		String returnStr = "#";
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			Random rand = new Random();
			temp = rand.nextInt(7);
			if (temp == 10)
				returnStr += "a";
			else if (temp == 11)
				returnStr += "b";
			else if (temp == 12)
				returnStr += "c";
			else if (temp == 13)
				returnStr += "d";
			else if (temp == 14)
				returnStr += "e";
			else if (temp == 15)
				returnStr += "f";
			else
				returnStr += (Integer.toString(temp));
		}
		return (returnStr);
	}
}
