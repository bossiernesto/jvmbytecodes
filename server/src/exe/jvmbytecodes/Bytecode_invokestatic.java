package exe.jvmbytecodes;
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
//			String x = (String) Driver.runTimeStack.pop();
//			Driver.runTimeStack.push(x, Driver.lightGray);        
			writeMethodSnap();
			Driver.currentMethod = index;
			int numParameters = parameters.length;

        	Frame_ f2 = new Frame_(Driver.currentMethod);
			Driver.runTimeStack.push(path, f2.CURRENT_FRAME_COLOR);
			int j = numParameters-1;
			for(int i = 0; i < numParameters; i++)
			{
				if(parameters[i].equals("I"))
				{
					int var;
					var = popInteger();
					f2._localVariableArray[j] = Integer.toString(var);
					f2.localVariableArray.set(Integer.toString(var), j);
				}
				else if(parameters[i].equals("J"))
				{
					long var = popLong();
					f2._localVariableArray[j] = Long.toString(var);
					f2.localVariableArray.set(var, j);
					f2._localVariableArray[j-1] = "";
					f2.localVariableArray.set("", j-1);
					j--;
				}
				else if(parameters[i].equals("F"))
				{
					float var;
					var = popFloat();
					f2._localVariableArray[j] = Float.toString(var);
					f2.localVariableArray.set(Float.toString(var), j);
				}
				else if(parameters[i].equals("D"))
				{
					double var = popDouble();
					f2._localVariableArray[j] = Double.toString(var);
					f2.localVariableArray.set(var, j);
					f2._localVariableArray[j-1] = "";
					f2.localVariableArray.set("", j-1);
					j--;
				}
				else
					System.out.println("not working");

				j--;
			}
			Driver._runTimeStack.push(f2);
			next = 0;
        	return next;
        }
}

