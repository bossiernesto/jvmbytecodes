package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain goto
 * only goto
 */
public class Bytecode_return extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_return(String str) {
		// System.out.println("Enter Bytecode_if constructor");
		parse(str);
		// System.out.println("Complete Bytecode_parse");
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// return
		next = -1;

		writeSnap();
		System.out.println("Enter goto");
		System.out.println("Goto line " + next);
		return next;
	}
}
