package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
//import exe.GAIGSprimitiveCollection.*;
import exe.GAIGSlegend;
import exe.GAIGSItem;
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
		f = (Frame_) Driver._runTimeStack.peek();
		
	    exe.GAIGSprimitiveCollection pc = new exe.GAIGSprimitiveCollection( f.methodName + " frame");
	    pc.addPolygon(
			    4,
			    new double[] { 0.45, 0.45, 0.95, 0.95 },
			    new double[] { 0.05, 0.95, 0.95, 0.05 },
			    f.CURRENT_FRAME_COLOR,  // fill color
			    f.CURRENT_FRAME_COLOR,  // outline color
			    "#FFFFFF",  // label color
			    ""          // polygon label
			  );
		int[] colorArray;
		if(next > lineNumber)
			colorArray = new int[] {PseudoCodeDisplay.GREEN, PseudoCodeDisplay.RED};
		else
			colorArray = new int[] {PseudoCodeDisplay.RED, PseudoCodeDisplay.GREEN};

       if (f.stackSize > 0){
	    Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(new int[] {next, lineNumber}, colorArray, f), 
				  pc,
				  Driver.runTimeStack, 
				  f.stack, 
				  Driver.heap,
				  f.localVariableArray, f.jvmLegend
				  );
		}
		else{
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(new int[] {next, lineNumber}, colorArray, f), 
				  pc,
				  Driver.runTimeStack,
				  Driver.heap,
				  f.localVariableArray, f.jvmLegend
				  );
			}
		}

	//highlights the next line we want to execute red
	public void writeSnapReturn() throws IOException
	{
		f = (Frame_) Driver._runTimeStack.peek();
	    exe.GAIGSprimitiveCollection pc = new exe.GAIGSprimitiveCollection( f.methodName + " frame");
	    pc.addPolygon(
			    4,
			    new double[] { 0.45, 0.45, 0.95, 0.95 },
			    new double[] { 0.05, 0.95, 0.95, 0.05 },
			    f.CURRENT_FRAME_COLOR,  // fill color
			    f.CURRENT_FRAME_COLOR,  // outline color
			    "#FFFFFF",  // label color
			    ""          // polygon label
			  );

		if (f.stackSize > 0)
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(new int[] {next}, new int[] 
						   {PseudoCodeDisplay.RED}, f), 
				  pc,
				  Driver.runTimeStack, 
				  f.stack, 
				  Driver.heap,
				  f.localVariableArray, f.jvmLegend
				  );
		else
			Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(new int[] {next}, new int[] 
						   {PseudoCodeDisplay.RED}, f), 
				  pc,
				  Driver.runTimeStack,
				  Driver.heap,
				  f.localVariableArray, f.jvmLegend
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
				  Driver.runTimeStack, f.jvmLegend,
				  Driver.heap);
	}

	//last snap of the entire slideshow, called in Driver
	public void writeFinalSnap() throws IOException
	{
		Driver.show.writeSnap(Driver.TITLE, 
				  MakeURI.doc_uri(lineNumber, f), 
				  MakeURI.make_uri(lineNumber, 
						   PseudoCodeDisplay.GREEN, f), 
				  Driver.runTimeStack, f.jvmLegend,
				  Driver.heap);
	}

	//double
	public void pushDouble(double d) throws IOException
	{
		f._stack.push("");
		f._stack.push(d);
		f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(d, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		if(opcode.contains("return"))
			writeSnapReturn();
		else
			writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.lightGray);
			f.stackColor = true;
		}	
	}

	public Double popDouble()
	{
		Double temp;
		temp = (Double) f._stack.pop();
		f._stack.pop();
		f.stack.set("", f.currentStackHeight++);			
		f.stack.set("", f.currentStackHeight++);
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight-2, Driver.lightGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight-2, Driver.lightGray);
			f.stackColor = true;
		}
		return temp;
	}

	public void storeDouble(Double x) throws IOException
	{
		int index = Integer.parseInt(arguments.get(0));
		f._localVariableArray[index] = String.valueOf(x);
		f._localVariableArray[index+1] = "";
		f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.localVariableArray.set("", index+1, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.localVariableArray.setColor(index, f._colorLocalVariableArray[index]);
		f.localVariableArray.setColor(index+1, f._colorLocalVariableArray[index+1]);
	}

	public void loadDouble(Double x) throws IOException
	{
		f._stack.push("");
		f._stack.push(x);
		f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.lightGray);
			f.stackColor = true;
		}
	}

	//long
	public void pushLong(long l) throws IOException
	{
		f._stack.push("");
		f._stack.push(l);
		f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(l, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		if(opcode.contains("return"))
			writeSnapReturn();
		else
			writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.lightGray);
			f.stackColor = true;
		}		
	}

	public Long popLong()
	{
		Long temp;
		temp = (Long) f._stack.pop();
		f._stack.pop();
		f.stack.set("", f.currentStackHeight++);			
		f.stack.set("", f.currentStackHeight++);
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight-2, Driver.lightGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight-2, Driver.lightGray);
			f.stackColor = true;
		}
		return temp;
	}

	public void storeLong(Long x) throws IOException
	{
		int index = Integer.parseInt(arguments.get(0));
		f._localVariableArray[index] = String.valueOf(x);
		f._localVariableArray[index+1] = "";
		f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.localVariableArray.set("", index+1, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.localVariableArray.setColor(index, f._colorLocalVariableArray[index]);
		f.localVariableArray.setColor(index+1, f._colorLocalVariableArray[index+1]);
	}

	public void loadLong(Long x) throws IOException
	{
		f._stack.push("");
		f._stack.push(x);
		f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stack.setColor(f.currentStackHeight+1, Driver.lightGray);
			f.stackColor = true;
		}
	}

	//int
	public void pushInteger(int i) throws IOException
	{
		f._stack.push(i);
		f.stack.set(i, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		if(opcode.contains("return"))
			writeSnapReturn();
		else
			writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stackColor = true;
		}
	}

	public Integer popInteger()
	{
		Integer temp = (Integer) f._stack.pop();
		f.stack.set("", f.currentStackHeight++);
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stackColor = true;
		}
		return temp;
	}

	public void storeInteger(Integer x) throws IOException
	{
		int index = Integer.parseInt(arguments.get(0));
		f._localVariableArray[index] = String.valueOf(x);
		f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.localVariableArray.setColor(index, f._colorLocalVariableArray[index]);
	}

	public void loadInteger(Integer x) throws IOException
	{
		f._stack.push(x);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stackColor = true;
		}
	}

	//float
	public void pushFloat(float fl) throws IOException
	{
		f._stack.push(fl);
		f.stack.set(fl, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		if(opcode.contains("return"))
			writeSnapReturn();
		else
			writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stackColor = true;
		}	
	}

	public Float popFloat()
	{
		Float temp = (Float) f._stack.pop();
		f.stack.set("", f.currentStackHeight++);
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight-1, Driver.lightGray);
			f.stackColor = true;
		}
		return temp;
	}

	public void storeFloat(Float x) throws IOException
	{
		int index = Integer.parseInt(arguments.get(0));
		f._localVariableArray[index] = String.valueOf(x);
		f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.localVariableArray.setColor(index, f._colorLocalVariableArray[index]);
	}

	public void loadFloat(Float x) throws IOException
	{
		f._stack.push(x);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		if(f.stackColor){
			f.stack.setColor(f.currentStackHeight, Driver.darkGray);
			f.stackColor = false;
		}		
		else{
			f.stack.setColor(f.currentStackHeight, Driver.lightGray);
			f.stackColor = true;
		}
	}



	/*
	 * Splices out the elements needed within a byte code string
	 */
	public void parse(String s) {
		entireOpcode = s;
		String[] s2 = s.split(":");
		entireOpcode = s2[1];	

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
			System.out.println("in the parser, path:" + path);
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
