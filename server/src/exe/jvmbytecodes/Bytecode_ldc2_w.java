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
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek();
		// ldc2_w
		// total: 1
		next = lineNumber + 3;

		if(objectType.contains("long"))
		{
			pushLong(Long.parseLong(path));
		}
		else if(objectType.contains("double"))
		{
			pushDouble(Double.parseDouble(path));
		}
		else
			System.out.println("Unrecognized bytecode");
	
		f.returnAddress = next;
		return next;
	}
}
