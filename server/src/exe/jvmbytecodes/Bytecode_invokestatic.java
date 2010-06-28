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
		System.out.println("INVOKESTATIC");
		f = (Frame_) Driver._runTimeStack.peek();

                String[] methodName = arguments.get(2).split(":");
                String name = methodName[0];
                System.out.println("Name: " + name);
                int index = 0;
                for(Method_ m : Driver.classes[0].methods)
                {
                        if(m.name.equals(name))
                        {
                                Driver.currentMethod = index;
                                break;
                        }
                        index++;
                }
		System.out.println("Driver.currentMethod: " + Driver.currentMethod);
                Frame_ f2 = new Frame_(Driver.currentMethod);
                Driver.runTimeStack.push(name);
		Driver._runTimeStack.push(f2);
		next = 0;
                return next;
        }
}

