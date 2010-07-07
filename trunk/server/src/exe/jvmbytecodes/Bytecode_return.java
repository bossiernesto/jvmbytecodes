package exe.jvmbytecodes;

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
	public int execute() throws IOException,JDOMException {

		if(opcode.equals("return"))
		{
			System.out.println("HERE");
			if(Driver.runTimeStack.size() > 1)
			{
				writeSnap();
				Driver._runTimeStack.pop();
				Driver.runTimeStack.pop();
				f = (Frame_) Driver._runTimeStack.peek();
				next = f.returnAddress;
				String x = (String) Driver.runTimeStack.pop();
				Driver.runTimeStack.push(x, f.CURRENT_FRAME_COLOR);
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
			int var = (Integer) f2._stack.pop();

			next = f.returnAddress;
			Driver.currentMethod = f.methodIndex;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, f.CURRENT_FRAME_COLOR);
			pushInteger(var);
		}
		else if(opcode.equals("lreturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			long var = (Long) f2._stack.pop();
			f2._stack.pop();

			next = f.returnAddress;
			Driver.currentMethod = f.methodIndex;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, f.CURRENT_FRAME_COLOR);
			pushLong(var);
		}
		else if(opcode.equals("freturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			float var = (Float) f2._stack.pop();

			next = f.returnAddress;
			Driver.currentMethod = f.methodIndex;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, f.CURRENT_FRAME_COLOR);
			pushFloat(var);
		}
		else if(opcode.equals("dreturn"))
		{
			Frame_ f2 = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			double var = (Double) f2._stack.pop();
			f2._stack.pop();

			next = f.returnAddress;
			Driver.currentMethod = f.methodIndex;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, f.CURRENT_FRAME_COLOR);
			pushDouble(var);
		}
		else
			System.out.println("Unrecognized opcode");

		// return
		return next;
	}
}
