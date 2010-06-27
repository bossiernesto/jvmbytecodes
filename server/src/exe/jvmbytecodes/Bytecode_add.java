package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

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
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		System.out.println("Enter Bytecode_add execute");
		//Add
		//total: 4
		//I have tested each path in this block 15/6/10
		if(opcode.contains("add"))
		{
		Object x, y;
		x = f._stack.pop();
		y = f._stack.pop();
		//iadd
		if(opcode.contains("i"))
		{
		f._stack.push((Integer) x + (Integer) y);
		System.out.println(f._stack.peek() + " " + f._stack.peek().getClass());
		}
		//ladd
		else if(opcode.contains("l"))
		{
		f._stack.push((Long) x + (Long) y);
		System.out.println(f._stack.peek() + " " + f._stack.peek().getClass());
		}
		//fadd
		else if(opcode.contains("f"))
		{
		f._stack.push((Float) x + (Float) y);
		System.out.println(f._stack.peek() + " " + f._stack.peek().getClass());
		}
		//dadd
		else if(opcode.contains("d"))
		{
		f._stack.push((Double) x + (Double) y);
		System.out.println(f._stack.peek() + " " + f._stack.peek().getClass());
		}
		else
		{
		System.out.println("Unrecognized opcode");
		}
		writeSnap();
		}
		return 1;
	}
}
