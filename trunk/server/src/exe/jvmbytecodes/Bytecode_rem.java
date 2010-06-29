package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes all byte codes that contain mul
 * irem, lrem, frem, drem implemented
 */
class Bytecode_rem extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_rem(String str) {
		parse(str);
	}

	/*
	 * executes the current line
	 * @return line number
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;
		writeNextLineSnap();
		//irem
		if(opcode.contains("i"))
		{
			Object x, y;
			x = f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Integer z = (Integer) x % (Integer) y;
			f._stack.push(z);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//lrem
		else if(opcode.contains("l"))
		{
			Object x, y, a;
			a = f._stack.pop();
			x = f._stack.pop();
			f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Long z = (Long) x % (Long) y;
			f._stack.push(z);
			f._stack.push(a);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(a, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
		}
		//frem
		else if(opcode.contains("f"))
		{
			Object x, y;
			x = f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Float z = (Float) x % (Float) y;
			f._stack.push(z);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//drem
		else if(opcode.contains("d"))
		{
			Object x, y, a;
			a = f._stack.pop();
			x = f._stack.pop();
			f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Double z = (Double) x % (Double) y;
			f._stack.push(z);
			f._stack.push(a);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(a, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
