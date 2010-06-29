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
 * ineg, lneg, fneg, dneg implemented
 */
class Bytecode_neg extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_neg(String str) {
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
	
		//ineg
		if(opcode.contains("i"))
		{
			Integer x;
			x = (Integer) f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			Integer z = -x.intValue();
			f._stack.push(z);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//lneg
		else if(opcode.contains("l"))
		{
			Object a;
			Long x;
			a = f._stack.pop();
			x = (Long) f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Long z = -x.longValue();
			f._stack.push(z);
			f._stack.push(a);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(a, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
		}
		//fneg
		else if(opcode.contains("f"))
		{
			Float x;
			x = (Float) f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			Float z = -x.floatValue();
			f._stack.push(z);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		//dneg
		else if(opcode.contains("d"))
		{
			Object a;
			Double x;
			a = f._stack.pop();
			x = (Double) f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			Double z = -x.doubleValue();
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
