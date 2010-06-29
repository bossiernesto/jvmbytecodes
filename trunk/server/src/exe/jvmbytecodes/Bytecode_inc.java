package exe.jvmbytecodes;

import java.io.IOException;
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
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Inc		next = lineNumber + 1;
		writeNextLineSnap();
		
		// iinc
		if (opcode.contains("ii")) {
			int index = Integer.parseInt(arguments.get(0));
			Integer x, y;
			x = Integer.parseInt(arguments.get(1));
			y = Integer.parseInt(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2]);
			int z = x + y;
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = String.valueOf(z);
			f.localVariableArray.set(z, index, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
		}

		next += 2;
		f.returnAddress = next;
		return next;
	}
}
