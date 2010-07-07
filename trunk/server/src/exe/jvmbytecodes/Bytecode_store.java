package exe.jvmbytecodes;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain store
 * istore, lstore, fstore, dstore
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
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Store
		next = lineNumber + 1;

		// istore
		if (opcode.contains("i")) {
			storeInteger(popInteger());
		}
		// lstore
		//check and make sure that the localvartable is being modified at the correct indices
		else if (opcode.contains("l")) {
			storeLong(popLong());
		}
		//fstore
		else if (opcode.contains("f")) {
			storeFloat(popFloat());
		}
		// dstore
		//check and make sure that the localvartable is being modified at the correct indices
		else if (opcode.contains("d")) {
			storeDouble(popDouble());
		}
		//aconst_null
		else if (opcode.contains("astore")) {
			int index = Integer.parseInt(arguments.get(0));
			Object x;
			x = f._stack.pop();
			f._localVariableArray[index] = " ";
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set("null", index, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
		}
		else
			System.out.println("store bytecode not found");

		//we may need to increment next for long and double
		if (underscore.compareTo("_") == 0)
			return next;
		else
			next += 1;
		f.returnAddress = next;
		return next;
	}
}

