package exe.jvmbytecodes;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain inc
 * only iinc implemented
 */
public class Bytecode_inc extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_inc(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Inc		next = lineNumber + 1;
		next += 2;
		
		// iinc
		if (opcode.contains("ii")) {
			int index = Integer.parseInt(arguments.get(0));
			Integer x, y;
			x = Integer.parseInt(arguments.get(1));
			y = Integer.parseInt(f._localVariableArray[index]);
			int z = x + y;
			storeInteger(z);
		}

		f.returnAddress = next;
		return next;
	}
}
