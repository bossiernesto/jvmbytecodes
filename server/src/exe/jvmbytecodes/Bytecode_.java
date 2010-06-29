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
import exe.pseudocode.*;

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
	public Frame_ f;

	public abstract int execute() throws IOException;

	/*
	 * Writes a snapshot for the visualization
	 * the line is highlighted green for executing right now!
	 */
	public void writeSnap() throws IOException {
		//System.out.println("LineNumber: " + lineNumber + " and currentStackHeight: " + f.currentStackHeight + " and stacksize: " + f.stackSize + " and methodName: " + f.methodName);
		f = (Frame_) Driver._runTimeStack.peek();
	    exe.GAIGSprimitiveCollection pc = new exe.GAIGSprimitiveCollection( f.methodName + " frame");
	    pc.addPolygon(
			    4,
			    new double[] { 0.45, 0.45, 0.95, 0.95 },
			    new double[] { 0.05, 0.95, 0.95, 0.05 },
			    Driver.CURRENT_FRAME_COLOR,  // fill color
			    Driver.CURRENT_FRAME_COLOR,  // outline color
			    "#FFFFFF",  // label color
			    ""          // polygon label
			  );

       if (f.stackSize > 0)
	    Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.GREEN, f), 
				  pc,
				  Driver.runTimeStack, 
				  f.stack, 
				  Driver.heap,
				  f.localVariableArray
				  );
		else
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.GREEN, f), 
				  pc,
				  Driver.runTimeStack,
				  Driver.heap,
				  f.localVariableArray
				  );
		}

	//highlights the next line we want to execute red
	public void writeNextLineSnap() throws IOException
	{
		f = (Frame_) Driver._runTimeStack.peek();
	    exe.GAIGSprimitiveCollection pc = new exe.GAIGSprimitiveCollection( f.methodName + " frame");
	    pc.addPolygon(
			    4,
			    new double[] { 0.45, 0.45, 0.95, 0.95 },
			    new double[] { 0.05, 0.95, 0.95, 0.05 },
			    Driver.CURRENT_FRAME_COLOR,  // fill color
			    Driver.CURRENT_FRAME_COLOR,  // outline color
			    "#FFFFFF",  // label color
			    ""          // polygon label
			  );

		if (f.stackSize > 0)
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.RED, f), 
				  pc,
				  Driver.runTimeStack, 
				  f.stack, 
				  Driver.heap,
				  f.localVariableArray
				  );
		else
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.RED, f), 
				  pc,
				  Driver.runTimeStack,
				  Driver.heap,
				  f.localVariableArray
				  );
	}

	//we may need this to show that we are starting to execute a new method
	//i think it's called in invoke static
	public void writeMethodSnap() throws IOException
	{
		Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.GREEN, f), 
				  Driver.runTimeStack,
				  Driver.heap);
	}

	//last snap of the entire slideshow, called in Driver
	public void writeFinalSnap() throws IOException
	{
		Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.RED, f), 
				  Driver.runTimeStack,
				  Driver.heap);
	}

	/*
	 * Splices out the elements needed within a byte code string
	 */
	public void parse(String s) {
		entireOpcode = s;
System.out.println("entire opcode: " + s);
		String[] s2 = s.split(":");
		entireOpcode = s2[1];

		if (s.contains(";")) {
			String[] temp = s.split(";");
			comments = temp[1];
			temp = temp[0].split("[ \t]");
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
				underscore = " ";
			}

			if (temp.length <= 1) {
				;
			} else {
				for (int i = 2; i < temp.length; i++) {
					if (temp[i].contains(",")) {
						temp[i] = temp[i].substring(0, temp[i].length() - 1);
					}
					arguments.add(temp[i]);
				}
			}

                        if(!comments.equals(null))
                        {
				String[] commentsString = comments.split(" ");

                                        for( int i = 1; i < commentsString.length; i++)
                                        {
                                                String returnType = "";
                                                if(commentsString[i].contains(":"))
                                                {
                                                        returnType = commentsString[i].substring(commentsString[i].length() - 1, commentsString[i].length());
                                                        commentsString[i] = commentsString[i].substring(0, commentsString[i].length()-1);
                                                }
                                                arguments.add(commentsString[i]);
                                                if(returnType != "")
                                                        arguments.add(returnType);
                                        }
                         }

		} else {
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
		next = lineNumber + 1;
		System.out.println(opcode + " " + lineNumber + " " + arguments + " " + comments + " " + next);
	}

	/*
	 * Current line number being executed within the byte code
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}


        public int getLocalVariableTable(String parameter)
        {
                int index = 0;
                for(int i = 0; i < Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable.length; i++)
                {
                        if(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[i][0].compareTo(parameter) == 0)
                        {
                                index = i;
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
