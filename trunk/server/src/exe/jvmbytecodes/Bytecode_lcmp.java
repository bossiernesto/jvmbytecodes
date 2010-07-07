package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//lcmp implemented
class Bytecode_lcmp extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_lcmp(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();

		//lcmp
		//total: 1

		//lcmp
		if(opcode.contains("l"))
		{
			Long x, y;
			int z;
			x = popLong();
			y = popLong();

			if(x < y)
				z = 1;
			else if(x == y)
				z = 0;
			else
				z = -1;

			pushInteger(z);
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
