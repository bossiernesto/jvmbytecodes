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
		//System.out.println("Enter interpreter");		
		Bytecode_ bc = Driver.classes[0].methods.get(Driver.currentMethod).bytecodes[0];
		System.out.println("bc.opcode: " + bc.opcode);
		System.out.println("bc.path: " + bc.path);
		bc.execute();

		while (bc.next != -1) {
			Bytecode_[] b = Driver.classes[0].methods.get(Driver.currentMethod).bytecodes;
			for (Bytecode_ x : b) {
				if (x.getLineNumber() == bc.next) {
					System.out.println("Begin interpreting " + x.opcode);
					x.execute();
					System.out.println("end" + x.opcode);
					bc = x;
				} else
					;
			}
		}
		System.out.println("Done");
	}
}
