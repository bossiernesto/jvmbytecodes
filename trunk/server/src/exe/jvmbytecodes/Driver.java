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
	static final String FILE = "exe/jvmbytecodes/template.xml";
	static int arraySize; // # of items to sort
	static GAIGSarray items; // the array of items
	static PseudoCodeDisplay pseudo; // The pseudocode
	static boolean success;
	static final String CLASSFILE = "exe/jvmbytecodes/classTemplate.xml";
	static PseudoCodeDisplay realCode;
	static ShowFile show;
	static int numberOfLinesInJavaFile = 1;
	static int stackSize = 0;
	static int heapSize = 0;
	static int currentStackHeight;
	static int currentMethod = 1;
	static GAIGSarray stack;
	static GAIGSarray localVariableArray;
	static GAIGSstack runTimeStack;
	static GAIGSstack heap;
	static Stack _runTimeStack = new Stack();
	static Stack _stack = new Stack();
	static ArrayList _heap;
	static int currentClass;
	static int questionID;
	static Class_[] classes;

	/*
	 * Main driver for the client
	 * 
	 * args[0] is the full path and number for naming showfile args[1] is the name of the Java source file or "" args[2]
	 * is the contents of the Java file
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

		String[] tmp = { args[0] + ".sho", args[0], args[1] };

		show = new ShowFile(args[0] + ".sho", 5); // first argument is the script foo.sho
		// String[] temp = {"../../src/exe/jvmbytecodes/Test","Factorial4.java"};
		classes = GenerateBytecodes.getClasses(tmp);

		// create visual stack and heap using the predetermined sizes
		stackSize = classes[0].methods.get(1).stackSize;
		currentStackHeight = classes[0].methods.get(1).stackSize;
		stack = new GAIGSarray(stackSize, false, "Operand Stack", "#999999", 0.5, 0.1, 0.9, 0.5, 0.1);
		heap = new GAIGSstack("Heap", "#999999", 0.01, 0.5, 0.3, 0.9, 0.15);
		runTimeStack = new GAIGSstack("Run Time Stack", "#999999", 0.01, 0.1, 0.3, 0.5, 0.15);

		// new xml file
		currentClass = 0;
		currentMethod = 1;
		GenerateXML.generateXMLfile();
		GenerateXML.generateJavaXMLFile(args[1], args[2]);

		try {
			pseudo = new PseudoCodeDisplay(FILE);
			realCode = new PseudoCodeDisplay(CLASSFILE);
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		// set stack to initial values
		questionID = 0;
		for (int i = 0; i < stackSize; i++)
			stack.set("", i);

		// get a random color for the stack
		String mainColor = getRandomColor();
		runTimeStack.push(classes[0].methods.get(1).name, mainColor);

		localVariableArray = new GAIGSarray(classes[0].methods.get(1).localVariableTable.length, false,
				"Local Variables", "#999999", 0.5, 0.5, 0.9, 0.9, 0.1);

		for (int i = 0; i < classes[0].methods.get(1).localVariableTable.length; i++) {
			String[][] array = classes[0].methods.get(1).localVariableTable;
			Arrays.sort(array, new Compare());
			localVariableArray.set("", i);
			localVariableArray.setRowLabel(array[i][1] + " | " + array[i][0], i);
		}

		// begin interpreter
		Interpreter.interpret();

		show.close();
	}

	/*
	 * Initializes the stack that local variable reside on
	 */
	void setStackToInitialValues(GenerateBytecodes gbc) throws IOException {
		questionID = 0;
		for (int i = 0; i < stackSize; i++)
			stack.set("", i);

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
