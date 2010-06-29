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
		writeSnap();
		
		if(Driver.runTimeStack.size() > 1)
		{
			Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			f = (Frame_) Driver._runTimeStack.peek();
			next = f.returnAddress;
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, Driver.CURRENT_FRAME_COLOR);
			Driver.currentMethod = f.methodIndex;
			writeSnap();
		}
		else
		{
			f = (Frame_) Driver._runTimeStack.pop();
			Driver.runTimeStack.pop();
			next = -1;
			writeFinalSnap();
		}

		// return
		return next;
	}
}
