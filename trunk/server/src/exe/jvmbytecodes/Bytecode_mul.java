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
		
		next = lineNumber+1;
	
		//pop and grab objects on the stack
		Object x, y;
		x = Driver._stack.pop();
		Driver.stack.set("",Driver.currentStackHeight++);

		y = Driver._stack.pop();
		Driver.stack.set("",Driver.currentStackHeight++);

		//determine if the opcode is int long float or double
		if(opcode.contains("i")) //the opcode is imul
		{
			System.out.println("Enter imul");
			Driver._stack.push((Integer) x * (Integer) y);
			Driver.stack.set((Integer) x * (Integer) y,--Driver.currentStackHeight);
			writeSnap();
		}
		else if(opcode.contains("l")) //the opcode lmul
		{
			Driver._stack.push((Long) x * (Long) y);
			Driver.stack.set((Long) x * (Long) y,--Driver.currentStackHeight);
			writeSnap();
		}
		else if(opcode.contains("f")) //the opcode fmul
		{
			Driver._stack.push((Float) x * (Float) y);
			Driver.stack.set((Float) x * (Float) y,--Driver.currentStackHeight);
			writeSnap();
		}

		//dmul
		else if(opcode.contains("d"))
		{
			Driver._stack.push((Double) x * (Double) y);
			Driver.stack.set((Double) x * (Double) y,--Driver.currentStackHeight);
			writeSnap();
		}
		else
		{
			System.err.println("Unrecognized opcode");
		}

		return next;
	}
}
