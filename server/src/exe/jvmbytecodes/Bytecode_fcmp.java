package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//fcmp implemented
class Bytecode_fcmp extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_fcmp(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();

		//fcmp
		//total: 1

		//fcmp
		if(opcode.contains("f"))
		{
			Float x = popFloat();
			Float y = popFloat();
			
			int z;
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
