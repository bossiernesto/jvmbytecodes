package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes all byte codes that contain const
 * only i implemented
 */
public class Bytecode_load extends Bytecode_
{

	/*
	 * Constructor
	 */
	Bytecode_load(String str) 
	{
		//System.out.println("Enter Bytecode_load constructor");
		parse(str);
		//System.out.println("Complete Bytecode_parse");
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException 
	{
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;
		//Load
		System.out.println("Next starts as: " + next);
		//iload
		if(opcode.contains("i"))
		{
			System.out.println("Enter iload");

			int index = getLocalVariableTable(arguments.get(0));

			f._stack.push(Integer.parseInt(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2]));
			f.stack.set((String) Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2], --f.currentStackHeight, "#FFCC11");

			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}

		if(underscore.compareTo("_") == 0)
		{
			;
		}
		else
		{
			next += 1;
		}
		return next;
	}

}
