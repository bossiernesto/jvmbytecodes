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
            String[] methodName = arguments.get(2).split(":");
            String name = methodName[0];
            int index = 0;
            for(Method_ m : Driver.classes[0].methods)
            {
                    if(m.name.equals(name))
                    {
                            break;
                    }
                    index++;
            }
			String x = (String) Driver.runTimeStack.pop();
			Driver.runTimeStack.push(x, "#999999");        
			Driver.runTimeStack.push(name, Driver.CURRENT_FRAME_COLOR);
			writeMethodSnap();
			Driver.currentMethod = index;
        	Frame_ f2 = new Frame_(Driver.currentMethod);
			Driver._runTimeStack.push(f2);
			System.out.println("Driver.runTimeStack.size(): " + Driver.runTimeStack.size());
			System.out.println(Driver.runTimeStack.peek());
			next = 0;
        	return next;
        }
}

