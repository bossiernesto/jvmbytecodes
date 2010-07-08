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
	public int execute() throws IOException,JDOMException 
	{
		f = (Frame_) Driver._runTimeStack.peek();
		next = lineNumber + 1;
		next = next + 2;
		// If
		//total: 11
		Integer x, y;

		Random rand = new Random();
		int random = rand.nextInt(2);

		if(!underscore.contains("_"))
		{
			if (opcode.contains("ifeq"))
			{
				x = (Integer) f._stack.pop();
				if ( x == 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);	
			}
			else if (opcode.contains("ifne"))
			{
				x = (Integer) f._stack.pop();

				if ( x != 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */	

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);		
			}
			else if (opcode.contains("iflt"))
			{
				x = (Integer) f._stack.pop();

				if ( x < 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */	

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);	
			}
			else if (opcode.contains("ifge"))
			{
				x = (Integer) f._stack.pop();

				if ( x >= 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */		

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);		
			}
			else if (opcode.contains("ifgt"))
			{
				x = (Integer) f._stack.pop();

				if ( x > 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */		

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);		
			}
			else if (opcode.contains("ifle"))
			{
				x = (Integer) f._stack.pop();

				if ( x <= 0) 
					next = Integer.parseInt(arguments.get(0));
				else
					;/* no jump */		

				makePurpleSingle();
				f.stack.set("", f.currentStackHeight++);		
			}
			else
				System.out.println("Not a recognized bytecode");
		}
		//if_cmp
		else
		{
			// if_icmpeq
			if (arguments.get(0).contains("icmpeq")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();

				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				if ( x == y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			// if_icmpne
			else if (arguments.get(0).contains("icmpne")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();

				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				if ( x != y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			// if_icmplt
			else if (arguments.get(0).contains("icmplt")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();

				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				if ( x > y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			// if_icmpge
			else if (arguments.get(0).contains("icmpge")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();

				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				if ( x <= y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			// if_icmpgt
			else if (arguments.get(0).contains("icmpgt")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();
				
				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				/*
				 * if(random == 1) { createQuestion1(x, y); } } else { createQuestion2(x, y); }
				 */
				Driver.questionID++;
				if (x < y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			// if_icmple
			else if (arguments.get(0).contains("icmple")) 
			{
				x = (Integer) f._stack.pop();
				y = (Integer) f._stack.pop();

				/*question block*/
				if (counter == 0) 
				{
					createQuestion1(x, y);
					counter++;
				} 
				else
					createQuestion2(x, y);

				if ( x >= y) 
					next = Integer.parseInt(arguments.get(1));
				else
					;/* no jump */

				makePurple();
				f.stack.set("", f.currentStackHeight++);
				f.stack.set("", f.currentStackHeight++);
			}
			else
				System.out.println("Not a recognized bytecode.");
		}

		f.returnAddress = next;
		return next;
	}

	void makePurple() throws IOException,JDOMException
	{
		f.stack.setColor(f.currentStackHeight, "#6666BB");
		f.stack.setColor(f.currentStackHeight+1, "#6666BB");
		writeSnap();
		f.stack.setColor(f.currentStackHeight, Driver.lightGray);
		f.stack.setColor(f.currentStackHeight+1, Driver.lightGray);
	}

	void makePurpleSingle() throws IOException,JDOMException
	{
		f.stack.setColor(f.currentStackHeight, "#6666BB");
		writeSnap();
		f.stack.setColor(f.currentStackHeight, "#999999");
	}

	/*
	 * Make a true of false question
	 */
	void createQuestion1(int x, int y) throws IOException {
		XMLtfQuestion question = new XMLtfQuestion(Driver.show, Driver.questionID + "");
		question.setQuestionText("The bytecode will jump to line number " + arguments.get(1) + ".");
		question.setAnswer(x <= y);
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
	}

}
