package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

;

/*
 * Hold the generated byte codes that reside within a .class file
 */
class GenerateBytecodes {
	static long timeStamp;
	static BufferedReader br;
	static Class_[] classes;

	/*
	 * Constructor
	 */
	static Class_[] getClasses(String[] args) {
		run(args);
		return classes;
	}

	/*
	 * A generation of a class object from a class file
	 * @param args[0] is the path
	 * @param args[1] is the source File containing a class
	 */
	public static void run(String[] args) {

		timeStamp = System.currentTimeMillis();
		String path = args[1];
		String sourceFileName = args[2];
		System.out.println("before compile");
		String[] newClassFiles = compile(path, sourceFileName);
		System.out.println("after compile");

		if (newClassFiles == null)
			// should not get here
			System.out.println("No class files were generated");
		else {
			classes = new Class_[newClassFiles.length];
			System.out.println(newClassFiles.length);
			for (int i = 0; i < newClassFiles.length; i++) {

				System.out.println(newClassFiles[i]);

				if (newClassFiles[i].startsWith("Error message")) {
					System.out.println(newClassFiles[i]);
					System.exit(1);
				} else {
					try {
						classes[i] = load_bytecodes(path, newClassFiles[i]);
					} catch (InvalidClassFileException e) {
						System.out.println("Your Java code does not " + "satisfy the constraints of the "
								+ "simulator:\n" + "    " + e);
						System.exit(1);
					}
					System.out.println(classes[i]);
					if (classes[i] == null) {
						System.out.println("Could not load bytecodes");
						break;
					}
				}// no error during compilation
			}
		}
	}// main method

	/**
	 * Returns the list of names of the class files generated by compiling the Java source code file named
	 * "path/fileName"
	 */
	public static String[] compile(String path, String fileName) {
		long timeStamp = System.currentTimeMillis();
		Process javac = null;
		int javacStatus;
		try {
			String javacCommand = "javac -g " + path + "/" + fileName;
			System.out.println(javacCommand);
			javac = Runtime.getRuntime().exec(javacCommand);
		} catch (IOException e) {
			return new String[] { "Error message 1" };
		}
		try {
			javacStatus = javac.waitFor();
		} catch (InterruptedException e) {
			return new String[] { "Error message 2" };
		}
		if (javacStatus == 0) { // no compilation error
			return new File(path).list(new ClassNameFilter());
		} else {
			return new String[] { "Error message 3" };
		}
	}// compile method

	/*
	 * creation of a class object from a *.class file
	 * @param path -path to the file
	 * @param classFileName - the class name only ex: Foo 
	 */
	public static Class_ load_bytecodes(String path, String classFileName) throws InvalidClassFileException {
		Process javap = null;
		int javapStatus;
		classFileName = classFileName.substring(0, classFileName.length() - 6);
		try {
			String javapCommand = "javap -verbose -private -classpath " + path + " " + classFileName;
			System.out.println(javapCommand);
			javap = Runtime.getRuntime().exec(javapCommand);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("here");

		br = new BufferedReader(new InputStreamReader(javap.getInputStream()));
		String line;
		Class_ c = null;
		try {
			c = loadClass();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("there");

		return c;
	}

	/*
	 * load all of the methods into the class
	 */
	private static Class_ loadClass() throws IOException, InvalidClassFileException {
		String line = br.readLine(); // skip "Compiled from" line
		Class_ c = parseClassNameAndSuperName();
		parseSourceFileName(c);
		skipConstantPool();
		line = parseFields(c);
		parseMethods(line, c);
		return c;
	}// loadClass method

	/*
	 * parses modifiers, etc.
	 */
	private static Class_ parseClassNameAndSuperName() throws IOException, InvalidClassFileException {
		System.out.println("parse");
		String line = br.readLine().trim();

		int implementsIndex = line.indexOf("implements");
		if (implementsIndex != -1)
			line = line.substring(0, implementsIndex);

		int classIndex = line.indexOf("class");
		String classModifiers = null;
		if (classIndex > 0) {
			classModifiers = line.substring(0, classIndex - 1);
			if (classModifiers.contains("abstract"))
				throw new InvalidClassFileException("Abstract classes are not supported");

			line = line.substring(classIndex);
		} else {
			if (line.indexOf("interface ") >= 0)
				throw new InvalidClassFileException("Interfaces are not supported");
		}

		String[] tokens = line.split("\\s+");
		int lastDotPosition = tokens[1].lastIndexOf(".");
		String packageName = null;
		String className = null;
		if (lastDotPosition == -1) {
			className = tokens[1];
		} else {
			packageName = tokens[1].substring(0, lastDotPosition);
			className = tokens[1].substring(lastDotPosition + 1);
		}

		String superName = tokens[3];
		return new Class_(classModifiers, packageName, className, superName);
	}// parseClassNameAndSuperName method

	/*
	 * parses the class name
	 */
	private static void parseSourceFileName(Class_ c) throws IOException, InvalidClassFileException {
		String[] tokens = br.readLine().trim().split("\\s+");
		c.setSourceFileName(tokens[1].substring(1, tokens[1].length() - 1));
	}// parseSourceFileName method

	/*
	 * skip constant pool
	 */
	private static void skipConstantPool() throws IOException, InvalidClassFileException {
		String line;
		do {
			line = br.readLine().trim();
			if (line.startsWith("InnerClass:"))
				throw new InvalidClassFileException("Inner classes are not supported");
		} while (!line.equals("{"));
	}// skipConstantPool method

	/*
	 * take out empty or indented lines from the javap generated file
	 */
	private static String skipEmptyOrIndentedLines() throws IOException {
		String line;
		do
			line = br.readLine();
		while ((line.equals("")) || (line.startsWith(" ")));
		return line;
	}

	/*
	 * skip Local Variabl table or non a indented line
	 */
	private static String skipUntilLVTorNonIndentedLine() throws IOException {
		String line;
		do
			line = br.readLine();
		while ((!line.contains("LocalVariableTable:")) && (line.startsWith(" ")));
		return line.trim();
	}

	/*
	 * parse the fields from the javap generated file
	 */
	private static String parseFields(Class_ c) throws IOException, InvalidClassFileException {
		String line = br.readLine();

		while ((!line.contains("(")) && // beginning of a method
				(!line.startsWith("}"))) // end of class
		{
			String[] tokens = line.trim().split("\\s+|;");
			String name;
			String type;
			String access = null;
			int index = 0;
			if (tokens.length == 3) // there is an access modifier
			{
				access = tokens[index];
				index++;
			}
			type = tokens[index];
			name = tokens[index + 1];
			c.addField(new Field_(access, type, name));

			line = skipEmptyOrIndentedLines();
		} // loop on fields
		return line;
	}// parseFields method

	/*
	 * parse the methods from the javap generated file
	 */
	private static void parseMethods(String line, Class_ c) throws IOException, InvalidClassFileException {
		int indexOfLeftParen = -1;
		int indexOfRightParen = -1;

		line = line.trim();

		while (!line.equals("}")) {
			if (line.trim().equals("static {};"))
				throw new InvalidClassFileException("Static blocks (including statically initialized arrays)"
						+ " are not supported");
			// line is the first line of a method
			indexOfLeftParen = line.indexOf("(");
			indexOfRightParen = line.indexOf(")");

			String parameterTypes = line.substring(indexOfLeftParen + 1, indexOfRightParen);
			line = line.substring(0, indexOfLeftParen);
			String[] tokens = line.trim().split("\\s");
			int n = tokens.length - 1;
			String name = tokens[n];
			String modifiers = null;
			String returnType = null;
			for (int i = 0; i < n; i++) {
				if (isModifier(tokens[i]))
					if (modifiers == null)
						modifiers = tokens[i];
					else
						modifiers += " " + tokens[i];
				else
					returnType = tokens[i];
			}
			br.readLine(); // skip "Code:"
			line = br.readLine().trim();
			tokens = line.split("\\D+");
			int stackSize = Integer.parseInt(tokens[1]);
			int numLocals = Integer.parseInt(tokens[2]);
			int numArgs = Integer.parseInt(tokens[3]);

			// ********** parse bytecodes ************************
			ArrayList<String> bytecodes = new ArrayList<String>();
			if ((modifiers != null) && modifiers.indexOf("abstract") != -1) { // TO DO: handle this error
			} else
				// at least one bytecode
				line = br.readLine().trim();

			// assumes that every method has a line number table
			while (!line.equals("LineNumberTable:")) {
				// assumes that every method has a line number table
				// that immediately follows the bytecodes
				bytecodes.add(line);
				line = br.readLine().trim();
			}

			// ********** parse line number table ***************
			ArrayList<String> lineNumbers = new ArrayList<String>();
			line = br.readLine();
			while (!line.equals("")) {
				lineNumbers.add(line);
				line = br.readLine();
			}
			int[][] lineNumberTable = new int[lineNumbers.size()][2];
			for (int i = 0; i < lineNumbers.size(); i++) {
				tokens = lineNumbers.get(i).split("\\D+");
				lineNumberTable[i][0] = Integer.parseInt(tokens[1]);
				lineNumberTable[i][1] = Integer.parseInt(tokens[2]);
			}

			line = skipUntilLVTorNonIndentedLine();

			if (line.equals("}"))
				break;

			// ********** parse local variable table ***************
			String[][] localVars = null;
			if (line.equals("LocalVariableTable:")) {
				ArrayList<String> varLines = new ArrayList<String>();
				line = br.readLine(); // skip column headers
				line = br.readLine(); // read in first row of table
				while (!line.equals("")) {
					varLines.add(line);
					line = br.readLine();
				}
				localVars = new String[varLines.size()][3];
				for (int i = 0; i < varLines.size(); i++) {
					tokens = varLines.get(i).split("\\s+");
					localVars[i][0] = tokens[3];
					localVars[i][1] = tokens[4];
					localVars[i][2] = tokens[5];
				}
			}

			c.addMethod(new Method_(modifiers, returnType, name, parameterTypes, stackSize, numLocals, numArgs,
					createBytecodeObjects(bytecodes), lineNumberTable, localVars, c.indent));

			line = skipEmptyOrIndentedLines();
		}// end of current method
	}// parseMethods method

	/*
	 * get a class file within a folder
	 */
	static class ClassNameFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return name.endsWith(".class");
			/*
			 * System.out.println(name); return name.endsWith(".class") && (new File(dir,name).lastModified() >
			 * GenerateBytecodes.timeStamp);
			 */
		}
	}// ClassNameFilter class

	/*
	 * determines if the string is a modifier
	 * @param String s - containing a possible modifier
	 * @return boolean true if the string containes a modifier
	 */
	private static boolean isModifier(String s) {
		return s.equals("abstract") || s.equals("final") || s.equals("static") || s.equals("package")
				|| s.equals("private") || s.equals("protected") || s.equals("public") || s.equals("synchronized");
	}// isModifier method

	/*
	 * Converts a string containing a byte code into an object so that the object can be used to store more useful
	 * information about the String.
	 */
	private static Bytecode_[] createBytecodeObjects(ArrayList<String> bc) {
		ArrayList<Bytecode_> arraylist = new ArrayList<Bytecode_>();

		for (String b : bc) {
			if (b.contains("mul")) {
				System.out.println("___________________________");
				arraylist.add(new Bytecode_mul(b));
			} else if (b.contains("add")) {
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				arraylist.add(new Bytecode_add(b));
			} else if (b.contains("const")) {
				System.out.println("***************************");
				arraylist.add(new Bytecode_const(b));
			} else if (b.contains("if")) {
				System.out.println("(((((((((((((((((((((((((((");
				arraylist.add(new Bytecode_if(b));
			} else if (b.contains("inc")) {
				System.out.println(":::::::::::::::::::::::::::");
				arraylist.add(new Bytecode_inc(b));
			} else if (b.contains("load")) {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
				arraylist.add(new Bytecode_load(b));
			} else if (b.contains("store")) {
				System.out.println("###########################");
				arraylist.add(new Bytecode_store(b));
			} else if (b.contains("goto")) {
				System.out.println("+++++++++++++++++++++++++++");
				arraylist.add(new Bytecode_goto(b));
			} else if (b.contains("return")) {
				System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;");
				arraylist.add(new Bytecode_return(b));
			} else
				System.out.println("no bytecodes generated");
			// arraylist.add(new Bytecode_(b));

		}
		for (int i = 0; i < arraylist.size(); i++) {
			System.out.println(arraylist.get(i).toString());
		}
		Bytecode_[] array = arraylist.toArray(new Bytecode_[] {});
		return array;
	}

}// GenerateBytecodes class

