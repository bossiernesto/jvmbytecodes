package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//ixor, lxor implemented
class Bytecode_xor extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_xor(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();

		//xor
		//total: 2

		//ixor
		if(opcode.contains("i"))
		{
			Integer x = popInteger();
			Integer y = popInteger();
			Integer z = (y ^ x);
			pushInteger(z);
		}
		//lxor
		else if(opcode.contains("l"))
		{
			Long x = popLong();
			Long y = popLong();
			Long z = (y ^ x);
			pushLong(z);
		}

		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
