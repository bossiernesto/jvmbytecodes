package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes all byte codes that contain mul
 * ineg, lneg, fneg, dneg implemented
 */
class Bytecode_neg extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_neg(String str) {
		parse(str);
	}

	/*
	 * executes the current line
	 * @return line number
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;
	
		//ineg
		if(opcode.contains("i"))
		{
			Integer z = (-popInteger().intValue());
			pushInteger(z);
		}
		//lneg
		else if(opcode.contains("l"))
		{
			Long z = (-popLong().longValue());
			pushLong(z);
		}
		//fneg
		else if(opcode.contains("f"))
		{
			Float z = (-popFloat().floatValue());
			pushFloat(z);
		}
		//dneg
		else if(opcode.contains("d"))
		{
			Double z = (-popDouble().doubleValue());
			pushDouble(z);
		}
		else
		{
			System.out.println("Unrecognized opcode");
		}

		f.returnAddress = next;
		return next;
	}
}
