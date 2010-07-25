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
import org.jdom.JDOMException;

/*
* <p><code>Bytecode_const</code> provides a representation of a "const" bytecode in the JVM.
* Use the <code>Bytecode_const</code> constructor to parse a line of output from javap into a 
* <code>Bytecode_const</code> object. Use the <code>execute</code> method through polymorphism to 
* simulate pushing a constant.
*
* @author Caitlyn Pickens
* @author Cory Sheeley
* @author William Clements
* @version 7/11/2010
*/

//iconst_m1, iconst_0, iconst_1, iconst_2, iconst_3, iconst_4, iconst_5, lconst_0, lconst_1, fconst_0, fconst_1, fconst_2, dconst_0, dconst_1 implemented
public class Bytecode_const extends Bytecode_ {

	/**
	 * Constructor
	 * @param	str		The string to be parsed into a Bytecode_const object
	 */
	Bytecode_const(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek(); //update the current stack
		next = lineNumber + 1; //update the next line number

		// iconst -1, 0, 1, 2, 3, 4, 5
		if (opcode.contains("i")) {
			if (arguments.get(0).compareTo("m1") == 0) {
				pushInteger((int) -1);
			}
			else {
				pushInteger(Integer.parseInt(arguments.get(0)));
			}
		}
		// lconst 0, 1
		else if (opcode.contains("l")) {
			if(arguments.get(0).compareTo("0") == 0)
				pushLong(0L);
			else if(arguments.get(0).compareTo("1") == 0)
				pushLong(1L);
			else
				pushLong(Long.parseLong(arguments.get(0)));
		}
		// fconst_ 0, 1, 2
		else if (opcode.contains("f")) {
			if(arguments.get(0).compareTo("0") == 0)
				pushFloat(0F);
			else if(arguments.get(0).compareTo("1") == 0)
				pushFloat(1F);
			else
				pushFloat(Float.parseFloat(arguments.get(0)));
		}
		// dconst 0, 1
		else if (opcode.contains("d")) {
			if(arguments.get(0).compareTo("0") == 0)
				pushDouble(0.0);
			else if(arguments.get(0).compareTo("1") == 0)
				pushDouble(1.0);
			else
				pushDouble(Double.parseDouble(arguments.get(0)));
		}
		else {
			System.out.println("Unrecognized opcode");
		}
		f.returnAddress = next; //update the return address
		return next; //return the next line number
	}
}
