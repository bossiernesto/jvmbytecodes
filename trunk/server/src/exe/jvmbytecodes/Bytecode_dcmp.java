package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//dcmp implemented
class Bytecode_dcmp extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_dcmp(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();

		//dcmp
		//total: 1

		//lcmp
		if(opcode.contains("d"))
		{
			Double x = popDouble();
			Double y = popDouble();
			int z;

			z = y.compareTo(x);
/*			if(x < y)
				z = 1;
			else if(x == y)
				z = 0;
			else
				z = -1;
*/

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
