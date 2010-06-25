package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
//import exe.GAIGSprimitiveCollection.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*
 * A representation of a byte code within the Java Virtual Machine.
 */
abstract class Bytecode_ {

	public String opcode;
	public int lineNumber;
	public String comments;
	public ArrayList<String> arguments = new ArrayList<String>();
	public char type;
	public int next;
	public String underscore;
	public String entireOpcode;

	public abstract int execute() throws IOException;

	/*
	 * Writes a snapshot for the visualization
	 */
	public void writeSnap() throws IOException {
	    exe.GAIGSprimitiveCollection pc = new exe.GAIGSprimitiveCollection();
	    pc.addPolygon(
			    4,
			    new double[] { 0.45, 0.45, 0.95, 0.95 },
			    new double[] { 0.05, 0.95, 0.95, 0.05 },
			    Driver.CURRENT_FRAME_COLOR,  // fill color
			    Driver.CURRENT_FRAME_COLOR,  // outline color
			    "#FFFFFF",  // label color
			    ""          // polygon label
			  );

	    Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber), 
				  MakeURI.make_uri(lineNumber, 
						   Driver.pseudo.RED), 
				  pc,
				  Driver.runTimeStack, 
				  Driver.stack, Driver.heap,
				  Driver.localVariableArray
				  );
	}

	/*
	 * Splices out the elements needed within a byte code string
	 */
	public void parse(String s) {
		entireOpcode = s;
		String[] s2 = s.split(":");
		entireOpcode = s2[1];
		next = lineNumber + 1;
		System.out.println(s);
		System.out.println("Enter Bytecode_parse");
		if (s.contains(";")) {
			System.out.println("Enter contains(;) if");
			String[] temp = s.split(";");
			comments = temp[1];
			temp = temp[0].split("[ \t]");
			lineNumber = Integer.parseInt(temp[0].substring(0, temp[0].length() - 2));

			int j = 1;
			while (temp[j].compareTo("") == 0)
				j++;
			opcode = temp[j];

			if (opcode.contains("_")) {
				underscore = "_";
				String[] temp1 = opcode.split("_");
				opcode = temp1[0];
				arguments.add(temp1[1]);
				// System.out.println(arguments);
			} else {
				underscore = " ";
			}

			if (temp.length <= 1) {
				;
				// System.out.println("no arguments");
			} else {

				// System.out.println("enter else");

				for (int i = 1; i < temp.length; i++) {
					if (temp[i].contains(",")) {
						temp[i] = temp[i].substring(0, temp[i].length() - 2);
					}
					arguments.add(temp[i]);
				}
			}
		} else {
			System.out.println("Enter contains(;) else");
			String[] temp = s.split("[ \t]");
			lineNumber = Integer.parseInt(temp[0].substring(0, temp[0].length() - 1));

			int j = 1;
			while (temp[j].compareTo("") == 0)
				j++;
			opcode = temp[j];

			if (opcode.contains("_")) {
				underscore = "_";
				String[] temp1 = opcode.split("_");
				opcode = temp1[0];
				arguments.add(temp1[1]);
			} else {
				underscore = "";
			}

			for (int i = 2; i < temp.length; i++) {
				if (temp[i].contains(",")) {
					temp[i] = temp[i].substring(0, temp[i].length() - 1);
				}
				arguments.add(temp[i]);
			}
		}
		System.out.println(opcode + " " + lineNumber + " " + arguments + " " + comments + " " + next);
	}

	/*
	 * Current line number being executed within the byte code
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}

	/*
	 * Local variables that reside within the frame of the Java Virtual Machine
	 */
	public int getLocalVariableTable(String parameter) {
		int index = 0;
		for (int i = 0; i < Driver.classes[0].methods.get(1).localVariableTable.length; i++) {
			if (Driver.classes[0].methods.get(1).localVariableTable[i][0].compareTo(parameter) == 0) {
				index = i;
				System.out.println("The index we found is: " + index);
				break;
			}
		}
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return opcode + " " + arguments + " " + lineNumber;
	}
}
