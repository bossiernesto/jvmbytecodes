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
* <p><code>Bytecode_ldc</code> provides a representation of a "ldc" bytecode in the JVM.
* Use the <code>Bytecode_ldc</code> constructor to parse a line of output from javap into a 
* <code>Bytecode_ldc</code> object. Use the <code>execute</code> method through polymorphism to 
* simulate loading a constant.
*
* @author Caitlyn Pickens
* @author Cory Sheeley
* @author William Clements
* @version 7/11/2010
*/

//ldc implemented
public class Bytecode_ldc extends Bytecode_ {

	/**
	 * Constructor
	 * @param	str		The string to be parsed into a Bytecode_ldc object
	 */
	Bytecode_ldc(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException,JDOMException {
		f = (Frame_) Driver._runTimeStack.peek(); //get the current frame
		next = lineNumber + 2; //update the line number

		if(objectType.contains("int")) //is it an int constant?
			pushInteger(Integer.parseInt(path)); //load the int
		else if(objectType.contains("float")) //is it a float constant?
			pushFloat(Float.parseFloat(path)); //load the float
		else
			System.out.println("Unrecognized bytecode");
			
		f.returnAddress = next; //update the return address
		return next; //return the next line number
	}
}
