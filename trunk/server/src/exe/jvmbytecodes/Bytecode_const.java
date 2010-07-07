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
 */
public class Bytecode_const extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_const(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Const
		// total: 13
		next = lineNumber + 1;

		// iconst -1, 0, 1, 2, 3, 4, 5
		if (opcode.contains("i")) {
			if (arguments.get(0).compareTo("m1") == 0) {
				pushInteger((int) -1);
			}
			else {
				pushInteger(Integer.parseInt(arguments.get(0)));
			}
		}
		// lconst 0, 1
		else if (opcode.contains("l")) {
			pushLong(Long.parseLong(arguments.get(0)));
		}
		// fconst_ 0, 1, 2
		else if (opcode.contains("f")) {
			pushFloat(Float.parseFloat(arguments.get(0)));
		}
		// dconst 0, 1
		else if (opcode.contains("d")) {
			pushDouble(Double.parseDouble(arguments.get(0)));
		}
		else {
			System.out.println("Unrecognized opcode");
		}
		f.returnAddress = next;
		return next;
	}
}
