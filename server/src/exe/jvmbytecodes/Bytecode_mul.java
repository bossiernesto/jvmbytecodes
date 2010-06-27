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
 * Working on 18/6/10
 */
class Bytecode_mul extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_mul(String str) {
		parse(str);
	}

	/*
	 * executes the current line
	 * @return line number
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		
		next = lineNumber+1;
	
		//pop and grab objects on the stack
		Object x, y;
		x = f._stack.pop();
		f.stack.set("",f.currentStackHeight++);

		y = f._stack.pop();
		f.stack.set("",f.currentStackHeight++);

		//determine if the opcode is int long float or double
		if(opcode.contains("i")) //the opcode is imul
		{
			System.out.println("Enter imul");
			f._stack.push((Integer) x * (Integer) y);
			f.stack.set((Integer) x * (Integer) y,--f.currentStackHeight);
			writeSnap();
		}
		else if(opcode.contains("l")) //the opcode lmul
		{
			f._stack.push((Long) x * (Long) y);
			f.stack.set((Long) x * (Long) y,--f.currentStackHeight);
			writeSnap();
		}
		else if(opcode.contains("f")) //the opcode fmul
		{
			f._stack.push((Float) x * (Float) y);
			f.stack.set((Float) x * (Float) y,--f.currentStackHeight);
			writeSnap();
		}

		//dmul
		else if(opcode.contains("d"))
		{
			f._stack.push((Double) x * (Double) y);
			f.stack.set((Double) x * (Double) y,--f.currentStackHeight);
			writeSnap();
		}
		else
		{
			System.err.println("Unrecognized opcode");
		}

		return next;
	}
}
