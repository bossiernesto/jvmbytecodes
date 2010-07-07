package exe.jvmbytecodes;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes bytecodes that contain pop2
 */
public class Bytecode_pop2 extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_pop2(String str) 
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

		f._stack.pop();
		f._stack.pop();
		f.stack.set("",f.currentStackHeight++, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set("",f.currentStackHeight++, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.stack.setColor(f.currentStackHeight-1, "#999999");
		f.returnAddress = next;
		return next;
	}
}
