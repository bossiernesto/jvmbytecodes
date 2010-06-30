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
	public ArrayList<String> arguments = new ArrayList<String>();
	public String objectType;
	public String path;
	public String[] parameters;
	public String returnType;
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

		if(s.contains("_"))
			underscore = "_";
		else
			underscore = " ";

		if(s.contains(";"))
		{
			String[] split = s.split("//");

			String[] front = split[0].split("( |\\t|:|,|_|;)+");
			lineNumber = Integer.parseInt(front[0]);
			opcode = front[1];
			for(int i = 2; i < front.length; i++)
				arguments.add(front[i]);

			String[] back = split[1].split("( |\\t|:|,|_|;|\\))+");
			objectType = back[0];
			path = back[1];
			if(back.length > 2)
			{
				String combinedParams = back[2].substring(1, back[2].length());
				char[] charArray = combinedParams.toCharArray();
				parameters = new String[charArray.length];
				for(int i = 0; i < charArray.length; i++)
					parameters[i] = Character.toString(charArray[i]);
			}
			else
				;
			if(back.length > 3)
				returnType = back[3];
			else
				;
		}
		else
		{
			String[] split = s.split("( |\\t|:|,|_)+");
			lineNumber = Integer.parseInt(split[0]);
			opcode = split[1];
			for(int i = 2; i < split.length; i++)
				arguments.add(split[i]);		
		}
		next = lineNumber + 1;
		//System.out.println("Opcode: " + opcode + ", lineNumber: " + lineNumber + ", arguments: " + arguments + ", path: " + path + ", + parameters " + parameters + ", returnType: "+ returnType + ", 			next: " + next);
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
