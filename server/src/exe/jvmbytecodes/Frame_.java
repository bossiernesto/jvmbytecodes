package exe.jvmbytecodes;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
//import exe.GAIGSprimitiveCollection.*;
import java.io.*;
import java.util.*; 
import java.net.*;
import exe.*;


class Frame_{

    Stack _stack = new Stack();
    GAIGSarray stack;
	String[] _localVariableArray;
    GAIGSarray localVariableArray;
    int stackSize = 0;
    int currentStackHeight;
    String methodName;
    int returnAddress;
    int methodIndex;

	public Frame_(int currentMethod)
	{
		stackSize = Driver.classes[0].methods.get(currentMethod).stackSize;
		currentStackHeight = Driver.classes[0].methods.get(currentMethod).stackSize;
		methodName = Driver.classes[0].methods.get(currentMethod).name;
		stack = new GAIGSarray( stackSize  , false, "Operand Stack", "#999999", 0.5, 0.1, 0.9, 0.5, 0.1);
		methodIndex = currentMethod;
		 _localVariableArray = new String[Driver.classes[0].methods.get(currentMethod).numLocals];
		
		//set stack to initial values, rather than "null"
		for (int i = 0; i < stackSize; i++)
			stack.set("", i);

		//create, set, and sort local var array
		localVariableArray = new GAIGSarray(Driver.classes[0].methods.get(currentMethod).numLocals, false,
				"Local Variables", "#999999", 0.5, 0.5, 0.9, 0.9, 0.1);

		String[][] array = Driver.classes[0].methods.get(currentMethod).localVariableTable;
		Arrays.sort(array, new Compare());

		for (int i = 0; i < _localVariableArray.length; i++) {
			localVariableArray.set("", i);
			localVariableArray.setRowLabel(Integer.toString(i), i);
		}
		
		for (int i = 0; i < Driver.classes[0].methods.get(currentMethod).localVariableTable.length; i++) {
			int index = Integer.parseInt(Driver.classes[0].methods.get(currentMethod).localVariableTable[i][0]);
			localVariableArray.setRowLabel(array[i][1] + " | " + array[i][0], index);
		}

		for(int i = 0; i < _localVariableArray.length; i++)
			System.out.println("localVarArray: " + localVariableArray.get(i));
	}
}
