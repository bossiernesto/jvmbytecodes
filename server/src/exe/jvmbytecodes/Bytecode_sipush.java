package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain sipush
 */
public class Bytecode_sipush extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_sipush(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Bipush
		// total: 1
		next = lineNumber + 3;
		writeNextLineSnap();

		f._stack.push(Integer.parseInt(arguments.get(0)));
		f.stack.set(arguments.get(0), --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
			
		f.returnAddress = next;
		return next;
	}
}
