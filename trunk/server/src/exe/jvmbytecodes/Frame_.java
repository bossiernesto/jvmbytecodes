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
	boolean stackColor = false;
    GAIGSarray localVariableArray;
	String[] _colorLocalVariableArray;
    int stackSize = 0;
    int currentStackHeight;
    String methodName;
    int returnAddress;
    int methodIndex;
  	String CURRENT_FRAME_COLOR;

	public Frame_(int currentMethod)
	{
		stackSize = Driver.classes[0].methods.get(currentMethod).stackSize;
		currentStackHeight = Driver.classes[0].methods.get(currentMethod).stackSize;
		methodName = Driver.classes[0].methods.get(currentMethod).name;
		stack = new GAIGSarray( stackSize  , false, "Operand Stack", "#999999", 0.5, 0.1, 0.9, 0.5, 0.1);
		methodIndex = currentMethod;
		_localVariableArray = new String[Driver.classes[0].methods.get(currentMethod).numLocals];
		_colorLocalVariableArray = new String[Driver.classes[0].methods.get(currentMethod).numLocals];
		
		System.out.println("CURRENTMETHOD" + (Driver._runTimeStack.size()%4));
		CURRENT_FRAME_COLOR = Driver.runTimeStackColors[Driver._runTimeStack.size()%4];

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
			localVariableArray.setColor(i, Driver.lightGray);
		}
		
		for (int i = 0; i < Driver.classes[0].methods.get(currentMethod).localVariableTable.length; i++) {
			int index = Integer.parseInt(Driver.classes[0].methods.get(currentMethod).localVariableTable[i][0]);
			localVariableArray.setRowLabel(array[i][1] + " | " + array[i][0], index);
		}

		for (int i = 0; i < _localVariableArray.length; i++) {
			if(localVariableArray.getRowLabel(i).contains("|"))
				;
			else
				localVariableArray.setRowLabel("", i);
		}

		int num = 0;
		for(int j = 0; j < _colorLocalVariableArray.length; j++)
		{
			boolean label1 = localVariableArray.getRowLabel(j).contains("|");
			boolean label2 = false;
			if(j != _colorLocalVariableArray.length-1)
				label2 = !localVariableArray.getRowLabel(j+1).contains("|");
			else
			{
				if(num == 0)
				{
					_colorLocalVariableArray[j] = Driver.lightGray;
				}
				else
				{
					_colorLocalVariableArray[j] = Driver.darkGray;
				}	
				break;			
			}
			if (label1 && label2)
			{
				if(num == 0)
				{
					_colorLocalVariableArray[j] = Driver.lightGray;
					_colorLocalVariableArray[j+1] = Driver.lightGray;
					num = 1;
					j++;
				}
				else
				{
					_colorLocalVariableArray[j] = Driver.darkGray;
					_colorLocalVariableArray[j+1] = Driver.darkGray;
					num = 0;
					j++;
				}
			}
			else
			{
				if(num == 0)
				{
					_colorLocalVariableArray[j] = Driver.lightGray;
					num = 1;
				}
				else
				{
					_colorLocalVariableArray[j] = Driver.darkGray;
					num = 0;
				}
			}
		}


		for(int i = 0; i < stackSize; i++)
			stack.setColor(i, Driver.lightGray);
	}
}
