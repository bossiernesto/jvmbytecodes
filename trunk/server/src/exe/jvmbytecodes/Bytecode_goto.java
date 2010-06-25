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
public class Bytecode_goto extends Bytecode_
{
	/*
	 * Constructor
	 */
	Bytecode_goto(String str) 
	{
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException 
	{
		//Goto
		next = Integer.parseInt(arguments.get(0));
		writeSnap();
		return next;
	}
}
