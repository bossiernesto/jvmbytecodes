package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes bytecodes that contain pop
 */
public class Bytecode_pop extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_pop(String str) 
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
		writeNextLineSnap();
		f._stack.pop();
		f.stack.set("",f.currentStackHeight++, Driver.CURRENT_HIGHLIGHT_COLOR);
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.returnAddress = next;
		return next;
	}
}
