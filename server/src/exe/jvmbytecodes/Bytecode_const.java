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
		writeNextLineSnap();
		// iconst -1, 0, 1, 2, 3, 4, 5
		if (opcode.contains("i")) {
			if (arguments.get(0).compareTo("m1") == 0) {
				f._stack.push((int) -1);
				f.stack.set((int) -1, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
				writeSnap();
				f.stack.setColor(f.currentStackHeight, "#999999");
			}
			else {
				f._stack.push(Integer.parseInt(arguments.get(0)));
				f.stack.set(arguments.get(0), --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
				writeSnap();
				f.stack.setColor(f.currentStackHeight, "#999999");
			}
		}
		// lconst 0, 1
		else if (opcode.contains("l")) {
			f._stack.push(Long.parseLong(arguments.get(0)));
			f._stack.push("");
			System.out.println(f._stack);
			f.stack.set(arguments.get(0)+"l", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
		}
		// fconst_ 0, 1, 2
		else if (opcode.contains("f")) {
			f._stack.push(Float.parseFloat(arguments.get(0)));
			f.stack.set(arguments.get(0) + "f", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		// dconst 0, 1
		else if (opcode.contains("d")) {
			f._stack.push(Double.parseDouble(arguments.get(0)));
			f._stack.push("");
			f.stack.set(arguments.get(0), --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set("", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
		}
		else if (opcode.contains("aconst")) {
			f._stack.push(null);
			f.stack.set("null", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			Driver.heap.push("null");
			Driver._heap.add(null);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		else {
			System.out.println("Unrecognized opcode");
		}
		f.returnAddress = next;
		return next;
	}
}
