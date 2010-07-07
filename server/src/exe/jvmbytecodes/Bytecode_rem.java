package exe.jvmbytecodes;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes all byte codes that contain mul
 * irem, lrem, frem, drem implemented
 */
class Bytecode_rem extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_rem(String str) {
		parse(str);
	}

	/*
	 * executes the current line
	 * @return line number
	 */
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;

		//irem
		if(opcode.contains("i"))
		{
			Integer x = popInteger();
			Integer y = popInteger();
			Integer z = (y % x);
			pushInteger(z);
		}
		//lrem
		else if(opcode.contains("l"))
		{
			Long x = popLong();
			Long y = popLong();
			Long z = (y % x);
			pushLong(z);
		}
		//frem
		else if(opcode.contains("f"))
		{
			Float x = popFloat();
			Float y = popFloat();
			Float z = (y % x);
			pushFloat(z);
		}
		//drem
		else if(opcode.contains("d"))
		{
			Double x = popDouble();
			Double y = popDouble();
			Double z = (y % x);
			pushDouble(z);
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
