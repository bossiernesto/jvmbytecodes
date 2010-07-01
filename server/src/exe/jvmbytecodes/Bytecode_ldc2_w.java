package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain ldc2_w
 */
public class Bytecode_ldc2_w extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_ldc2_w(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// ldc2_w
		// total: 1
		next = lineNumber + 3;
		writeNextLineSnap();

		if(objectType.contains("long"))
		{
			f._stack.push(Long.parseLong(path));
			f._stack.push("L");
			f.stack.set(path, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set("L", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);

		}
		else if(objectType.contains("double"))
		{
			f._stack.push(Double.parseDouble(path));
			f._stack.push("D");
			f.stack.set(path, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set("D", --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		}
		else
			System.out.println("Unrecognized bytecode");

		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.stack.setColor(f.currentStackHeight+1, "#999999");
			
		f.returnAddress = next;
		return next;
	}
}
