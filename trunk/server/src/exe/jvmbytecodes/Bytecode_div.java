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
 * idiv, ldiv, fdiv, ddiv implemented
 */
class Bytecode_div extends Bytecode_ {
	
	/*
	 * Constructor that parses the current bytecode 
	 */
	Bytecode_div(String str) {
		parse(str);
	}

	/*
	 * executes the current line
	 * @return line number
	 */
	public int execute() throws IOException {
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber+1;
	
		//idiv
		if(opcode.contains("id"))
		{
			Integer x = popInteger();
			Integer y = popInteger();
			Integer z = (y / x);
			pushInteger(z);
		}
		//ldiv
		else if(opcode.contains("l"))
		{
			Long x = popLong();
			Long y = popLong();
			Long z = (y / x);
			pushLong(z);
		}
		//fdiv
		else if(opcode.contains("f"))
		{
			Float x = popFloat();
			Float y = popFloat();
			Float z = (y / x);
			pushFloat(z);
		}
		//ddiv
		else if(opcode.contains("dd"))
		{
			Double x = popDouble();
			Double y = popDouble();
			Double z = (y / x);
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
