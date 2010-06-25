package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * A representation of a Java class
 */
class Class_ {
	private String modifiers;
	private String packageName;
	String name;
	private String superClassName;
	private String sourceFileName;
	private ArrayList<Field_> fields;
	ArrayList<Method_> methods;
	public final String indent = "   ";

	/*
	 * Constructor
	 */
	Class_(String modifiers, String packageName, String name, String superClassName) {
		this.modifiers = modifiers;
		this.packageName = packageName;
		this.name = name;
		this.superClassName = superClassName;
		fields = new ArrayList<Field_>();
		methods = new ArrayList<Method_>();
	}

	/*
	 * sets the source file
	 */
	public void setSourceFileName(String fileName) {
		sourceFileName = fileName;
	}

	/*
	 * Adds a field.
	 */
	public void addField(Field_ f) {
		fields.add(f);
	}

	/*	
	 * Adds a method.
	 */
	public void addMethod(Method_ m) {
		methods.add(m);
	}

	/*
	 * Formated representation.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer s = new StringBuffer("// Source file: " + sourceFileName + "\n");
		s.append("class ");
		if (packageName != null)
			s.append(packageName + ".");
		s.append(name);
		s.append(" extends ");
		s.append(superClassName);
		s.append(" {\n");

		for (Field_ f : fields) {
			s.append(indent);
			s.append(f);
			s.append("\n");
		}
		s.append("\n");

		for (Method_ m : methods) {
			s.append(m.toString());
			s.append("\n");
		}
		s.append("}\n");

		return s.toString();
	}

}// Class_ class
