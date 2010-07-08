package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//isub, lsub, fsub, dsub implemented
class Bytecode_sub extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_sub(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();

		//Sub
		//total: 4

		//isub
		if(opcode.contains("i"))
		{
			Integer x = popInteger();
			Integer y = popInteger();
			Integer z = (y - x);
			pushInteger(z);
		}
		//lsub
		else if(opcode.contains("l"))
		{
			Long x = popLong();
			Long y = popLong();
			Long z = (y - x);
			pushLong(z);
		}
		//fsub
		else if(opcode.contains("f"))
		{
			Float x = popFloat();
			Float y = popFloat();
			Float z = (y - x);
			pushFloat(z);
		}
		//dsub
		else if(opcode.contains("d"))
		{
			Double x, y;
			x = popDouble();
			y = popDouble();
			Double z = (y - x);
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
