package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain store
 * only iload implemented
 */
public class Bytecode_store extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_store(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		// Store
		next = lineNumber + 1;

		// istore
		if (opcode.contains("i")) {
			System.out.println("Enter istore");

			int index = getLocalVariableTable(arguments.get(0));
			Integer x;
			x = (Integer) Driver._stack.pop();
			Driver.classes[0].methods.get(1).localVariableTable[index][2] = String.valueOf(x);
			Driver.stack.set("", Driver.currentStackHeight++);
			Driver.localVariableArray.set(String.valueOf(x), index, "#FFCC11");
			writeSnap();
			Driver.localVariableArray.setColor(index, "#999999");
		}

		if (underscore.compareTo("_") == 0)
			return next;
		else
			next += 1;
		return next;
	}
}