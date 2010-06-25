package exe.jvmbytecodes;

import java.io.IOException;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;

/*
 * Recognizes all byte codes that contain if
 * only if_cmpgt
 */
public class Bytecode_if extends Bytecode_ {
	int counter = 0;

	/*
	 * Constructor
	 */
	Bytecode_if(String str) {
		parse(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exe.jvmbytecodes.Bytecode_#execute()
	 */
	public int execute() throws IOException {
		next = lineNumber + 1;
		next = next + 2;
		// If
		Integer x, y;
		Random rand = new Random();
		int random = rand.nextInt(2);
		// if_cmpgt

		if (arguments.get(0).contains("icmp")) {
			System.out.println("Enter if_icmp");
			if (arguments.get(0).contains("icmpgt")) {
				System.out.println("Enter if_icmpgt");
				x = (Integer) Driver._stack.pop();

				System.out.println(Driver._stack);
				y = (Integer) Driver._stack.pop();

				if (counter == 0) {
					createQuestion1(x, y);
					counter++;
				} else
					createQuestion2(x, y);

				/*
				 * if(random == 1) { createQuestion1(x, y); } } else { createQuestion2(x, y); }
				 */
				Driver.questionID++;
				if (x > y - 1) { /* no jump */
				} else
					next = Integer.parseInt(arguments.get(1));
				Driver.stack.set("", Driver.currentStackHeight++);
				Driver.stack.set("", Driver.currentStackHeight++);
			}
		}
		return next;
	}

	/*
	 * Make a true of false question
	 */
	void createQuestion1(int x, int y) throws IOException {
		XMLtfQuestion question = new XMLtfQuestion(Driver.show, Driver.questionID + "");
		question.setQuestionText("The bytecode will jump to line number " + arguments.get(1) + ".");
		question.setAnswer(x <= y);
		Driver.stack.setColor(Driver.currentStackHeight, "#CD0000");
		Driver.stack.setColor(Driver.currentStackHeight + 1, "#CD0000");
		writeSnap();
		Driver.stack.setColor(Driver.currentStackHeight, "#999999");
		Driver.stack.setColor(Driver.currentStackHeight + 1, "#999999");
	}

	/*
	 * Make a multiple choice question.
	 */
	void createQuestion2(int x, int y) throws IOException {
		XMLmcQuestion question = new XMLmcQuestion(Driver.show, Driver.questionID + "");
		question.setQuestionText("What line number will the program jump to next?");
		question.addChoice(next + "");
		question.addChoice(arguments.get(1));
		question.addChoice(lineNumber + "");
		if (x > y)
			question.setAnswer(1);
		else
			question.setAnswer(2);
		Driver.stack.setColor(Driver.currentStackHeight, "#CD0000");
		Driver.stack.setColor(Driver.currentStackHeight + 1, "#CD0000");
		writeSnap();
		Driver.stack.setColor(Driver.currentStackHeight, "#999999");
		Driver.stack.setColor(Driver.currentStackHeight + 1, "#999999");
	}

}
