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

		if(objectType.contains("int"))
			pushInteger(Integer.parseInt(path));
		else if(objectType.contains("float"))
			pushFloat(Float.parseFloat(path));
		else
			System.out.println("Unrecognized bytecode");
			
		f.returnAddress = next;
		return next;
	}
}
