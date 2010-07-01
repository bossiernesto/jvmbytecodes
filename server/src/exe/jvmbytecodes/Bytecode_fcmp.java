package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

//fcmp implemented
class Bytecode_fcmp extends Bytecode_ {
	
	/*
	 * Constructor
	 */
	Bytecode_fcmp(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		next = lineNumber + 1;
		f = (Frame_) Driver._runTimeStack.peek();
		writeNextLineSnap();
		//lcmp
		//total: 1

		//lcmp
		if(opcode.contains("f"))
		{
			Object x, y;
			x = f._stack.pop();
			y = f._stack.pop();
			f.stack.set("",f.currentStackHeight++);
			f.stack.set("",f.currentStackHeight++);
			int z;
			if( (Float) x < (Float) y)
				z = 1;
			else if( (Float) x == (Float) y)
				z = 0;
			else
				z = -1;
			f._stack.push(z);
			f.stack.set(z, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
