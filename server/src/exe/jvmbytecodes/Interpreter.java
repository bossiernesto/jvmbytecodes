package exe.jvmbytecodes;

import java.io.IOException;

/*
 * Interprets the byte code that resides within the *.class file
 */
public class Interpreter {

	/*
	 * Cycles through byte code
	 */
	static public void interpret() throws IOException {
		Bytecode_ bc = Driver.classes[0].methods.get(Driver.currentMethod).bytecodes[0];
		bc.execute();

		while (bc.next != -1) {
			Bytecode_[] b = Driver.classes[0].methods.get(Driver.currentMethod).bytecodes;
			for (Bytecode_ x : b) {
				if (x.getLineNumber() == bc.next) {
					x.execute();
					bc = x;
				} else
					;
			}
		}
	}
}
