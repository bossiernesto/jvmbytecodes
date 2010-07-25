/*
This file is part of JHAVE -- Java Hosted Algorithm Visualization
Environment, developed by Tom Naps, David Furcy (both of the
University of Wisconsin - Oshkosh), Myles McNally (Alma College), and
numerous other contributors who are listed at the http://jhave.org
site

JHAVE is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your
option) any later version.

JHAVE is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with the JHAVE. If not, see:
<http://www.gnu.org/licenses/>.
*/

package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;
import org.jdom.*;
import exe.*;
import exe.pseudocode.*;

/*
* <p><code>Bytecode_store</code> provides a representation of a "store" bytecode in the JVM.
* Use the <code>Bytecode_store</code> constructor to parse a line of output from javap into a 
* <code>Bytecode_store</code> object. Use the <code>execute</code> method through polymorphism to 
* simulate storing a value in the local variable array.
*
* @author Caitlyn Pickens
* @author Cory Sheeley
* @author William Clements
* @version 7/11/2010
*/

//istore, lstore, fstore, dstore implemented
public class Bytecode_store extends Bytecode_ {

	/**
	 * Constructor
	 * @param	str		The string to be parsed into a Bytecode_store object
	 */
	Bytecode_store(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek(); //get current frame
		next = lineNumber + 1; //update line number

		if (underscore.compareTo("_") == 0) //update line number again if necessary
			;
		else
			next += 1;

		// istore
		if (opcode.contains("i")) {
			storeInteger(popInteger()); //store
		}
		// lstore
		else if (opcode.contains("l")) {
			storeLong(popLong()); //store
		}
		//fstore
		else if (opcode.contains("f")) {
			storeFloat(popFloat()); //store
		}
		// dstore
		else if (opcode.contains("d")) {
			storeDouble(popDouble()); //store
		}
		else
			System.out.println("Bytecode not found");

		f.returnAddress = next; //update the return address
		return next; //return the next line number
	}
}

