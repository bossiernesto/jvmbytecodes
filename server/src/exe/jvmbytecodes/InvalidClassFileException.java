package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * Handles errors 
 */
class InvalidClassFileException extends RuntimeException {

	String errorMessage;

	/*
	 * Constructor
	 */
	InvalidClassFileException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/*
	 * toString method for returning the error
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		return errorMessage;
	}
}
