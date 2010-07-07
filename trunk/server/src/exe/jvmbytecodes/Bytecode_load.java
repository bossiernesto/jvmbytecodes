package exe.jvmbytecodes;
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
	public int execute() throws IOException,JDOMException 
	{
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;

		//Load
		//iload
		if(opcode.contains("i"))
		{
			int index = Integer.parseInt(arguments.get(0));
			loadInteger(Integer.parseInt(f._localVariableArray[index]));
		}
		//lload
		else if(opcode.contains("ll"))
		{
			int index = Integer.parseInt(arguments.get(0));
			loadLong(Long.parseLong(f._localVariableArray[index]));
		}
		//fload
		else if(opcode.contains("f"))
		{
			int index = Integer.parseInt(arguments.get(0));
			pushFloat(Float.parseFloat(f._localVariableArray[index]));
		}
		//dload
		else if(opcode.contains("dl"))
		{
			int index = Integer.parseInt(arguments.get(0));
			loadDouble(Double.parseDouble(f._localVariableArray[index]));
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
