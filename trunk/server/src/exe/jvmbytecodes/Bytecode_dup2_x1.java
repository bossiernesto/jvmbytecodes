package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
import org.jdom.JDOMException;
/*
 * Recognizes bytecodes that contain dup2_x1
 */
public class Bytecode_dup2_x1 extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_dup2_x1(String str) 
	{
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException
	{
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;

		Object x, y, z;
		x = f._stack.pop();
		y = f._stack.pop();
		z = f._stack.pop();
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f._stack.push(y);
		f._stack.push(x);
		f._stack.push(z);
		f._stack.push(y);
		f._stack.push(x);
		f.stack.set(y, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(z, --f.currentStackHeight, "#999999");
		f.stack.set(y, --f.currentStackHeight, "#999999");
		f.stack.set(x, --f.currentStackHeight, "#999999");
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.stack.setColor(f.currentStackHeight+1, "#999999");
		f.stack.setColor(f.currentStackHeight+2, "#999999");
		f.stack.setColor(f.currentStackHeight+3, "#999999");
		f.stack.setColor(f.currentStackHeight+4, "#999999");
		f.returnAddress = next;
		return next;
	}
}
