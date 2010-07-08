package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes bytecodes that contain swap
 */
public class Bytecode_swap extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_swap(String str) 
	{
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException 
	{
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;

		Object x, y;
		x = f._stack.pop();
		y = f._stack.pop();
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f._stack.push(x);
		f._stack.push(y);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(y, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.stack.setColor(f.currentStackHeight + 1, "#999999");
		f.returnAddress = next;
		return next;
	}
}
