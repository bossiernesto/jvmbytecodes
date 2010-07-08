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
 * Recognizes bytecodes that contain dup_x1
 */
public class Bytecode_dup_x1 extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_dup_x1(String str) 
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

		Object x, y;
		x = f._stack.pop();
		y = f._stack.pop();
		f._stack.push(x);
		f._stack.push(y);
		f._stack.push(x);
		f.stack.set(x, f.currentStackHeight+2, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight+2, "#999999");
		f.returnAddress = next;
		return next;
	}
}
