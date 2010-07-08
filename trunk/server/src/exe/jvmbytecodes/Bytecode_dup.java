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
 * Recognizes bytecodes that contain dup
 */
public class Bytecode_dup extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_dup(String str) 
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

		Object x;
		x = f._stack.peek();
		f._stack.push(x);
		f.stack.set(x, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.returnAddress = next;
		return next;
	}
}
