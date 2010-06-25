package exe.jvmbytecodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.JDOMException;

public class MakeURI {
	/*
	 * Generate the byte code for the visualization
	 */
	static String make_uri(int line, int color) {
		// System.out.println("highlighting line: "+line);
		return make_uri(new int[] { line }, new int[] { color });
	}

	/*
	 * Generate the byte code for the visualization
	 */
	static String make_uri(int[] lines, int[] colors) {
		String sHeight = Driver.stackSize == -1 ? "null" : String.valueOf(Driver.stackSize - Driver.currentStackHeight);
		String hHeight = Driver.heapSize == -1 ? "null" : String.valueOf(Driver.heapSize);
		String current_class_name = Driver.classes[Driver.currentClass].name;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("stack_height", sHeight);
		map.put("heap_height", hHeight);
		map.put("class_name", current_class_name);
		String uri = null;

		try {
			uri = Driver.pseudo.pseudo_uri(map, lines, colors);
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		return uri;
	}

	/*
	 * This method displays the java class
	 */
	static String doc_uri(int line) {
	    String sHeight = Driver.stackSize == -1 ? "null" : String.valueOf(Driver.stackSize - Driver.currentStackHeight);
	    String hHeight = Driver.heapSize == -1 ? "null" : String.valueOf(Driver.heapSize);
	    String current_class_name = Driver.classes[Driver.currentClass].name;

	    HashMap<String, String> map = new HashMap<String, String>();
	    map.put("stack_height", sHeight);
	    map.put("heap_height", hHeight);
	    map.put("class_name", current_class_name);
	    String uri = null;

	    // convert the byte code line number to the java line number
	    // System.out.println("\t\t\t\t\t\tLineNumberTable: "
	    // +Driver.classes[Driver.currentClass].methods.get(Driver.currentMethod).lineNumberTable.length);
	    int[] colors = new int[] { 0 };

	    int javaLine = 0;
	    ArrayList<String> lineArr = new ArrayList<String>();
	    Method_ currentMethod = 
		Driver.classes[Driver.currentClass]
		.methods.get(Driver.currentMethod);
	    for (int i = 0; 
		 i < currentMethod.lineNumberTable.length; 
		 i++) {
		if (currentMethod.lineNumberTable[i][1] <= line) {
		    javaLine = currentMethod.lineNumberTable[i][0] - 1;
		    System.out.println("\t\t\t\t\t\t\t\t\t" +
				   (currentMethod.lineNumberTable[i][0] - 1));
		}
	    }

	    int[] lineToHighlight = new int[] { javaLine };

	    try {
		uri = Driver.realCode.pseudo_uri(map, lineToHighlight, colors);
	    } catch (JDOMException e) {
		e.printStackTrace();
	    }
	    
	    return uri;
	    
	}
}
