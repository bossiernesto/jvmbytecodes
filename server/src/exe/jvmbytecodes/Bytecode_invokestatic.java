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

        public int execute() throws IOException 
        {
			next = lineNumber+3;
			f = (Frame_) Driver._runTimeStack.peek();
			f.returnAddress = next;
			writeNextLineSnap();
            int index = 0;
            for(Method_ m : Driver.classes[0].methods)
            {
                    if(m.name.equals(path))
                    {
                            break;
                    }
                    index++;
            }
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, "#999999");        
			Driver.runTimeStack.push(path, Driver.CURRENT_FRAME_COLOR);
			writeMethodSnap();
			Driver.currentMethod = index;
			int numParameters = parameters.length;

        	Frame_ f2 = new Frame_(Driver.currentMethod);
			int j = 0;
			for(int i = 0; i < numParameters; i++)
			{
				if(parameters[i].equals("I"))
				{
					int var;
					var = (Integer) f._stack.pop();
					f.stack.set("",f.currentStackHeight++);
					f2._localVariableArray[j] = Integer.toString(var);
					f2.localVariableArray.set(Integer.toString(var), j);
				}
				else if(parameters[i].equals("J"))
				{
					long var;
					String type;
					type = (String) f._stack.pop();
					var = (Long) f._stack.pop();
					f.stack.set("",f.currentStackHeight++);
					f.stack.set("",f.currentStackHeight++);
					f2._localVariableArray[j] = type;
					f2.localVariableArray.set(type, j);
					f2._localVariableArray[j+1] = Long.toString(var);
					f2.localVariableArray.set(Long.toString(var), j+1);
					j++;
				}
				else if(parameters[i].equals("F"))
				{
					float var;
					var = (Float) f._stack.pop();
					f.stack.set("",f.currentStackHeight++);
					f2._localVariableArray[j] = Float.toString(var);
					f2.localVariableArray.set(Float.toString(var), j);
				}
				else if(parameters[i].equals("D"))
				{
					double var;
					String type;
					type = (String) f._stack.pop();
					var = (Double) f._stack.pop();
					f.stack.set("",f.currentStackHeight++);
					f.stack.set("",f.currentStackHeight++);
					f2._localVariableArray[j] = type;
					f2.localVariableArray.set(type, j);
					f2._localVariableArray[j+1] = Double.toString(var);
					f2.localVariableArray.set(Double.toString(var), j+1);
					j++;
				}
				else
					System.out.println("not working");

				j++;
			}
			Driver._runTimeStack.push(f2);
			next = 0;
        	return next;
        }
}

