package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes bytecodes that contain dup2_x2
 */
public class Bytecode_dup2_x2 extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_dup2_x2(String str) 
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
		Object a, b, c, d;
		a = f._stack.pop();
		b = f._stack.pop();
		c = f._stack.pop();
		d = f._stack.pop();
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f.stack.set("",f.currentStackHeight++);
		f._stack.push(b);
		f._stack.push(a);
		f._stack.push(d);
		f._stack.push(c);
		f._stack.push(b);
		f._stack.push(a);
		f.stack.set(b, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(a, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
		f.stack.set(d, --f.currentStackHeight, "#999999");
		f.stack.set(c, --f.currentStackHeight, "#999999");
		f.stack.set(b, --f.currentStackHeight, "#999999");
		f.stack.set(a, --f.currentStackHeight, "#999999");
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
		f.stack.setColor(f.currentStackHeight+1, "#999999");
		f.stack.setColor(f.currentStackHeight+2, "#999999");
		f.stack.setColor(f.currentStackHeight+3, "#999999");
		f.stack.setColor(f.currentStackHeight+4, "#999999");
		f.stack.setColor(f.currentStackHeight+5, "#999999");
		f.returnAddress = next;
		return next;
	}
}
