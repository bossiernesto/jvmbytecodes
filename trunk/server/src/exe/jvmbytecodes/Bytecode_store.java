package exe.jvmbytecodes;

import java.io.IOException;
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
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		// Store
		next = lineNumber + 1;
		writeNextLineSnap();

		// istore
		if (opcode.contains("i")) {
			//System.out.println("Enter istore");

			int index = Integer.parseInt(arguments.get(0));
			Integer x;
			x = (Integer) f._stack.pop();
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = String.valueOf(x);
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
		}
		// lstore
		//check and make sure that the localvartable is being modified at the correct indices
		else if (opcode.contains("l")) {
			int index = Integer.parseInt(arguments.get(0));
			Long x;
			String y;
			y = (String) f._stack.pop();
			x = (Long) f._stack.pop();
			System.out.println("Index: " + index);
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = y;
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index+1][2] = String.valueOf(x);

for(int i = 0; i < Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable.length; i++)
	System.out.println(Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[i][2]);

			f.stack.set("", f.currentStackHeight++);			
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set(String.valueOf(y), index, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.localVariableArray.set(String.valueOf(x), index+1, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
			f.localVariableArray.setColor(index+1, "#999999");
		}
		//fstore
		else if (opcode.contains("f")) {
			int index = Integer.parseInt(arguments.get(0));
			Float x;
			x = (Float) f._stack.pop();
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = String.valueOf(x);
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set(String.valueOf(x), index, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
		}
		// dstore
		//check and make sure that the localvartable is being modified at the correct indices
		else if (opcode.contains("d")) {
			int index = Integer.parseInt(arguments.get(0));
			Double x;
			String y;
			y = (String) f._stack.pop();
			x = (Double) f._stack.pop();
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index][2] = y;
			Driver.classes[0].methods.get(Driver.currentMethod).localVariableTable[index+1][2] = String.valueOf(x);
			f.stack.set("", f.currentStackHeight++);			
			f.stack.set("", f.currentStackHeight++);
			f.localVariableArray.set(String.valueOf(y), index, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.localVariableArray.set(String.valueOf(x), index+1, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.localVariableArray.setColor(index, "#999999");
			f.localVariableArray.setColor(index+1, "#999999");
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
