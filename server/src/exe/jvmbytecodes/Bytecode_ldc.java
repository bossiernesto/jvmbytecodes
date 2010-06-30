package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain ldc
 */
public class Bytecode_ldc extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_ldc(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// ldc
		// total: 1
		next = lineNumber + 2;
		writeNextLineSnap();

		if(objectType.contains("int"))
			f._stack.push(Integer.parseInt(path));
		else if(objectType.contains("long"))
			f._stack.push(Long.parseLong(path));
		else if(objectType.contains("float"))
			f._stack.push(Float.parseFloat(path));
		else if(objectType.contains("double"))
			f._stack.push(Double.parseDouble(path));
		else
			System.out.println("Unrecognized bytecode");

		f.stack.set(path, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
			
		f.returnAddress = next;
		return next;
	}
}
