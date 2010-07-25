package exe.jvmbytecodes;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

import exe.*;
import exe.pseudocode.*;
/*
 * Recognizes all bytecodes that contain invokestatic
 *
 */
public class Bytecode_invokestatic extends Bytecode_
{
        
        Bytecode_invokestatic(String str) 
        {
                parse(str);
        }

        public int execute() throws IOException,JDOMException 
        {
			next = lineNumber+3;
			f = (Frame_) Driver._runTimeStack.peek();
			f.returnAddress = next;

            int index = 0;
            for(Method_ m : Driver.classes[0].methods)
            {
                    if(m.name.equals(path))
                    {
                            break;
                    }
                    index++;
            }
        
			writeMethodSnap();
			Driver.currentMethod = index;
			int numParameters = parameters.length;

			int counter = 0;
			for(int i = 0; i < numParameters; i++)
			{
				if(parameters[i].equals("D") || parameters[i].equals("J"))
					counter++;
				else
					;
				counter++;
			}

        	Frame_ f2 = new Frame_(Driver.currentMethod);
			Driver.runTimeStack.push(path, f2.CURRENT_FRAME_COLOR);

			int j = counter-1;
			System.out.println("j: " + j);
		    if(numParameters%2 == 0)
				;
			else
				f2.stackColor = true; 
			for(int i = (numParameters-1); i >= 0; i--)
			{
				System.out.println("Parameters are: " + parameters[i]);
				if(parameters[i].equals("I"))
				{
					int var;
					var = popInteger();
					f2._localVariableArray[j] = Integer.toString(var);
					if(!f2.stackColor){
						f2.localVariableArray.set(Integer.toString(var), j, Driver.darkGray);
						f2.stackColor = true;
					}
					else{
						f2.localVariableArray.set(Integer.toString(var), j, Driver.lightGray);
						f2.stackColor = false;		
					}				
				}
				else if(parameters[i].equals("J"))
				{
					long var = popLong();
					f2._localVariableArray[j-1] = Long.toString(var);
					f2._localVariableArray[j] = "";
					if(!f2.stackColor){
						f2.localVariableArray.set(var, j-1, Driver.darkGray);
						f2.localVariableArray.set("", j, Driver.darkGray);
						f2.stackColor = true;
					}
					else{
						f2.localVariableArray.set(var, j-1, Driver.lightGray);
						f2.localVariableArray.set("", j, Driver.lightGray);
						f2.stackColor = false;
					}
					j--;
				}
				else if(parameters[i].equals("F"))
				{
					float var;
					var = popFloat();
					f2._localVariableArray[j] = Float.toString(var);
					if(!f2.stackColor){
						f2.localVariableArray.set(Float.toString(var), j, Driver.darkGray);
						f2.stackColor = true;
					}
					else{
						f2.localVariableArray.set(Float.toString(var), j, Driver.lightGray);
						f2.stackColor = false;
					}				
				}
				else if(parameters[i].equals("D"))
				{
					double var = popDouble();
					f2._localVariableArray[j-1] = Double.toString(var);
					f2._localVariableArray[j] = "";
					if(!f2.stackColor){
						f2.localVariableArray.set(var, j-1, Driver.darkGray);
						f2.localVariableArray.set("", j, Driver.darkGray);
						f2.stackColor = true;
					}
					else{
						f2.localVariableArray.set(var, j-1, Driver.lightGray);
						f2.localVariableArray.set("", j, Driver.lightGray);
						f2.stackColor = false;
					}
					//f2.localVariableArray.set(var, j-1);
					//f2.localVariableArray.set("", j);
					j--;
				}
				else
					System.out.println("not working");

				j--;
			}
			Driver._runTimeStack.push(f2);
			
			if (Driver.runTimeStack.size() > 50)
		  		throw new InvalidClassFileException("Cannot produce more thatn 50 stack frames.\n"
		  			+"Try less than 50 method calls.");
		  
			next = 0;
        	return next;
        }
}

