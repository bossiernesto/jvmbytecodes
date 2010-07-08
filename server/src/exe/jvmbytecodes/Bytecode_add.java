package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import org.jdom.JDOMException;

class Bytecode_add extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_add(String str) {
		System.out.println("Enter Bytecode_add constructor");
		parse(str);
		System.out.println("Complete Bytecode_parse");
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();
		//Add
		//total: 4

		//iadd
		if(opcode.contains("i"))
		{
			Integer x = popInteger();
			Integer y = popInteger();
			Integer z = (y + x);
			pushInteger(z);
		}
		//ladd
		else if(opcode.contains("l"))
		{
			Long x = popLong();
			Long y = popLong();
			Long z = (y + x);
			pushLong(z);
		}
		//fadd
		else if(opcode.contains("f"))
		{
			Float x = popFloat();
			Float y = popFloat();
			Float z = (y + x);
			pushFloat(z);
		}
		//dadd
		else if(opcode.contains("d"))
		{
			Double x = popDouble();
			Double y = popDouble();
			Double z = (y + x);
			pushDouble(z);
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}


/*
			Object x, y, a;
			a = f._stack.pop();
			x = f._stack.pop();
			f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);


			f._stack.push(z);
			f._stack.push(a);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(a, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
*/

