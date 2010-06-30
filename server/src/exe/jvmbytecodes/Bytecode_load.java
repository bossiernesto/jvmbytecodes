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
		writeNextLineSnap();
		//Load
		//iload
		if(opcode.contains("i"))
		{
			int index = Integer.parseInt(arguments.get(0));

			f._stack.push(Integer.parseInt(f._localVariableArray[index]));
			f.stack.set((String)f._localVariableArray[index], --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);

			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//lload
		else if(opcode.contains("ll"))
		{
			int index = Integer.parseInt(arguments.get(0));
			Long x;
			String y;
			y =f._localVariableArray[index];
			x = Long.parseLong(f._localVariableArray[index+1]);
			f._stack.push(x);
			f._stack.push(y);
			System.out.println(f._stack);
			f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(y, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);

			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight + 1, "#999999");
		}
		//fload
		else if(opcode.contains("f"))
		{
			int index = Integer.parseInt(arguments.get(0));

			f._stack.push(Float.parseFloat(f._localVariableArray[index]));
			f.stack.set((String)f._localVariableArray[index], --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);

			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//dload
		else if(opcode.contains("dl"))
		{
			int index = Integer.parseInt(arguments.get(0));
			Double x;
			String y;
			y =f._localVariableArray[index];
			x = Double.parseDouble(f._localVariableArray[index+1]);

			f._stack.push(x);
			f._stack.push(y);
			f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(y, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);

			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight + 1, "#999999");
		}

		//we may need to increment next for long and double
		if(underscore.compareTo("_") == 0)
		{
			;
		}
		else
		{
			next += 1;
		}
		f.returnAddress = next;
		return next;
	}

}
