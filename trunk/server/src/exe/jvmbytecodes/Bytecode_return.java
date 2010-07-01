package exe.jvmbytecodes;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain goto
 * only goto
 */
public class Bytecode_return extends Bytecode_ {

	/*
	 * Constructor
	 */
	Bytecode_return(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		writeNextLineSnap();
		if(opcode.equals("return"))
		{		
			if(Driver.runTimeStack.size() > 1)
			{
				writeSnap();
				Driver._runTimeStack.pop();
				Driver.runTimeStack.pop();
				f = (Frame_) Driver._runTimeStack.peek();
				next = f.returnAddress;
				String x = (String) Driver.runTimeStack.pop();
				Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
				Driver.currentMethod = f.methodIndex;
			}
			else
			{
				writeSnap();
				f = (Frame_) Driver._runTimeStack.pop();
				Driver.runTimeStack.pop();
				next = -1;
				writeFinalSnap();
			}
		}
		else if(opcode.equals("ireturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			int integer = (Integer) f2._stack.pop();
			f._stack.push(integer);
			f.stack.set(integer, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			next = f.returnAddress;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
			Driver.currentMethod = f.methodIndex;
		}
		else if(opcode.equals("lreturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			String type = (String) f2._stack.pop();
			long var = (Long) f2._stack.pop();
			f._stack.push(var);
			f._stack.push(type);
			f.stack.set(var, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(type, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
			next = f.returnAddress;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
			Driver.currentMethod = f.methodIndex;
		}
		else if(opcode.equals("freturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			float var = (Float) f2._stack.pop();
			f._stack.push(var);
			f.stack.set(var, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			next = f.returnAddress;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
			Driver.currentMethod = f.methodIndex;
		}
		else if(opcode.equals("dreturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			String type = (String) f2._stack.pop();
			double var = (Double) f2._stack.pop();
			f._stack.push(var);
			f._stack.push(type);
			f.stack.set(var, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			f.stack.set(type, --f.currentStackHeight, Driver.CURRENT_HIGHLIGHT_COLOR);
			writeSnap();
			f.stack.setColor(f.currentStackHeight, "#999999");
			f.stack.setColor(f.currentStackHeight+1, "#999999");
			next = f.returnAddress;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
			Driver.currentMethod = f.methodIndex;
		}
		else
			System.out.println("Unrecognized opcode");

		// return
		return next;
	}
}
