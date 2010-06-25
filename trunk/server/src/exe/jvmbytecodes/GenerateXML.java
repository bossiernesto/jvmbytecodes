package exe.jvmbytecodes;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GenerateXML {
	/*
	 * generate the Java XML file that is used to display java code in the visualizer.
	 */
	static void generateJavaXMLFile(String pathName, String file) throws IOException {

		String bytecodeStr = "";
		bytecodeStr += "<?xml version=\"1.0\"?>\n";
		bytecodeStr += "<pseudocode>\n";
		bytecodeStr += "\t<stack><replace var=\"class_name\" /></stack>\n";
		bytecodeStr += "\t<code>\n";
		bytecodeStr += "\t<signature>Java Code</signature>\n";

		String path = pathName;
		String fileName = file;
		Process cat = null;
		int catStatus;
		try {
			String catCommand = "cat " + path + "/" + fileName;
			System.out.println(catCommand);
			cat = Runtime.getRuntime().exec(catCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(cat.getInputStream()));
		ArrayList<String> javaCode = new ArrayList<String>();
		String tempStr;
		try {
			int i = 0;
			while ((tempStr = br.readLine()) != null) {
				tempStr = insertEscapeChar(tempStr);
				javaCode.add(tempStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Driver.numberOfLinesInJavaFile = javaCode.size();
		for (int k = 0; k < Driver.numberOfLinesInJavaFile; k++) {
			bytecodeStr += "\t<line num=\"" + k + "\">" + javaCode.get(k) + "</line>\n";
		}

		bytecodeStr += "\t</code>\n";
		bytecodeStr += "</pseudocode>\n";

		String XML = "exe/jvmbytecodes/classTemplate.xml";
		FileOutputStream out = new FileOutputStream(XML);
		out.write(bytecodeStr.getBytes());
		out.flush();
		out.close();
	}

	/*
	 * When a character is found in the XML file that will generate an error, the character is replaced by the HTML code
	 */
	static String insertEscapeChar(String temp) {
		String returnStr = "";

		for (int i = 0; i < temp.length(); i++) {
			if (temp.charAt(i) == '<')
				returnStr += "&#60;";
			else
				returnStr += temp.charAt(i);
		}
		return returnStr;
	}

	/*
	 * generate the byte code XML file that is used to display java code in the visualizer.
	 */
	static void generateXMLfile() throws IOException {

		String bytecodeStr = "";
		bytecodeStr += "<?xml version=\"1.0\"?>\n";
		bytecodeStr += "<pseudocode>\n";
		bytecodeStr += "\t<stack><replace var=\"class_name\" /></stack>\n";
		bytecodeStr += "\t<code>\n";
		bytecodeStr += "\t<signature>Java Byte Code</signature>\n";

		for (int k = 1; k < Driver.classes[Driver.currentClass].methods.size(); k++)
			for (int l = 0; l < Driver.classes[Driver.currentClass].methods.get(k).bytecodes.length; l++) {
				bytecodeStr += "\t<line num=\""
						+ Driver.classes[Driver.currentClass].methods.get(k).bytecodes[l].lineNumber + "\">"
						+ Driver.classes[Driver.currentClass].methods.get(k).bytecodes[l].entireOpcode;
				bytecodeStr += "</line>\n";
			}

		bytecodeStr += "\t</code>\n";
		bytecodeStr += "\t<vars>\n";
		bytecodeStr += "\t<var>heap height = <replace var=\"heap_height\" /></var>\n";
		bytecodeStr += "\t<var>stack height = <replace var=\"stack_height\" /></var>\n";
		bytecodeStr += "\t</vars>\n";
		bytecodeStr += "</pseudocode>\n";

		String XML = "exe/jvmbytecodes/template.xml";
		FileOutputStream out = new FileOutputStream(XML);
		out.write(bytecodeStr.getBytes());
		out.flush();
		out.close();
	}
}
