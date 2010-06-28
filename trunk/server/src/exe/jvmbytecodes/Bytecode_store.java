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
		f = (Frame_) Driver._runTimeStack.peek();
		// Store
		next = lineNumber + 1;

		// istore
		if (opcode.contains("i")) {
			System.out.println("Enter istore");

			int index = getLocalVariableTable(arguments.get(0));
			Integer x;
			x = (Integer) f._stack.pop();
			System.out.println("Driver.currentMethod: " + Driver.currentMethod);
			System.out.println("Index: " + index);
			System.out.println("Arguments: " + arguments);
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = String.valueOf(x);
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set(String.valueOf(x), index, "#FFCC11");
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
		}

		if (underscore.compareTo("_") == 0)
			return next;
		else
			next += 1;
		return next;
	}
}
