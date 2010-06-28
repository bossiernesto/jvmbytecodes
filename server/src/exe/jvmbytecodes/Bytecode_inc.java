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
		System.out.println("Enter Bytecode_inc constructor");
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Inc		next = lineNumber + 1;
		// iinc
		if (opcode.contains("ii")) {
			System.out.println("Enter iinc");
			int index = getLocalVariableTable(arguments.get(0));
			System.out.println("Index is: " + index);
			Integer x, y;
			x = Integer.parseInt(arguments.get(1));
			y = Integer.parseInt(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2]);
			int z = x + y;
			System.out.println("z is: " + z);
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = String.valueOf(z);
			f.localVariableArray.set(z, index);
			writeSnap();
			System.out.println("hi");
		}

		for (int r = 0; r < Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable.length; r++) {
			for (int c = 0; c < Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[r].length; c++) {
				System.out.println(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[r][c]);
			}
		}

		next += 2;
		System.out.println(next);
		return next;
	}
}
