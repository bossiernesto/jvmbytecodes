package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * A representation of a Java method
 */
class Method_ {
	String name;
	String parameterTypes;
	String returnType;
	String modifiers;
	Bytecode_[] bytecodes;
	int stackSize, numLocals, numArgs;
	int[][] lineNumberTable;
	String[][] localVariableTable;

	String indent;

	/*
	 * Constructor 
	 */
	Method_(String modifiers, String returnType, String name, String parameterTypes, int stackSize, int numLocals,
			int numArgs, Bytecode_[] bytecodes, int[][] lineNumberTable, String[][] localVariableTable, String i) {

		this.modifiers = modifiers;
		this.returnType = returnType;
		this.name = name;
		this.parameterTypes = parameterTypes;
		this.stackSize = stackSize;
		this.numLocals = numLocals;
		this.numArgs = numArgs;
		this.bytecodes = bytecodes;
		this.lineNumberTable = lineNumberTable;
		this.localVariableTable = localVariableTable;
		this.indent = i;
	}

	/*
	 * Creates a formated representation of the Method object
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		if (modifiers != null) {
			s.append(modifiers);
			s.append(" ");
		}
		if (returnType != null) {
			s.append(returnType);
			s.append(" ");
		}
		s.append(name);
		s.append("(");
		s.append(parameterTypes);
		s.append(")\n");
		s.append(indent);
		s.append("stack size = ");
		s.append(stackSize);
		s.append("\n");
		s.append(indent);
		s.append("number of locals = ");
		s.append(numLocals);
		s.append("\n");
		s.append(indent);
		s.append("number of arguments = ");
		s.append(numArgs);
		s.append("\n");
		for (Bytecode_ bc : bytecodes) {
			s.append(indent);
			s.append(bc);
			s.append("\n");
		}
		s.append(indent);
		s.append("LineNumberTable:\n");
		for (int i = 0; i < lineNumberTable.length; i++) {
			s.append(indent);
			s.append(lineNumberTable[i][0]);
			s.append("\t");
			s.append(lineNumberTable[i][1]);
			s.append("\n");
		}
		s.append(indent);
		s.append("LocalVariableTable:\n");
		for (int i = 0; i < localVariableTable.length; i++) {
			s.append(indent);
			s.append(localVariableTable[i][0]);
			s.append("\t");
			s.append(localVariableTable[i][1]);
			s.append("\t");
			s.append(localVariableTable[i][2]);
			s.append("\n");
		}
		return s.toString();
	}

}// Method_ class

