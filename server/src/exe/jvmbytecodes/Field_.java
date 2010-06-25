package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * A representation of a Java field
 */
class Field_ {
	String access;
	String type;
	String name;

	/*
	 * Constructor
	 */
	Field_(String access, String type, String name) {
		this.access = access;
		this.type = type;
		this.name = name;
	}

	/*
	 * formated representation
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (access != null ? access + " " : "") + type + " " + name + ";";
	}
}// Field_ class

