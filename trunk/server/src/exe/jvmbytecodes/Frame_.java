package exe.jvmbytecodes;
import exe.GAIGSlegend;
import exe.GAIGSItem;
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
    GAIGSnewArray stack;
	String[] _localVariableArray;
	boolean stackColor = false;
    GAIGSnewArray localVariableArray;
	String[] _colorLocalVariableArray;
    int stackSize = 0;
    int currentStackHeight;
    String methodName;
    int returnAddress;
    int methodIndex;
	public GAIGSItem color1, color2, color3;
	public int rows = 1, columns = 3;
	public GAIGSlegend jvmLegend = new GAIGSlegend(rows, columns,"", -.15, -.08, 1.15, .55, 1.5);
  	String CURRENT_FRAME_COLOR;

	public Frame_(int currentMethod)
	{
		if (Driver.classes[0].methods.get(currentMethod).numLocals > 20)
			throw new InvalidClassFileException("Please limit the number of local variables in a frame of \n"
				+"the program to 20.");
	
		stackSize = Driver.classes[0].methods.get(currentMethod).stackSize;
		currentStackHeight = Driver.classes[0].methods.get(currentMethod).stackSize;
		methodName = Driver.classes[0].methods.get(currentMethod).name;
		stack = new GAIGSnewArray(stackSize, "Operand Stack", "#999999", 0.75, 0.1, 0.95, 0.9, 0.15);
		methodIndex = currentMethod;
		_localVariableArray = new String[Driver.classes[0].methods.get(currentMethod).numLocals];
		_colorLocalVariableArray = new String[Driver.classes[0].methods.get(currentMethod).numLocals];
		
		System.out.println("CURRENTMETHOD" + (Driver._runTimeStack.size()%3));
		CURRENT_FRAME_COLOR = Driver.runTimeStackColors[Driver._runTimeStack.size()%3];

		color1 = new GAIGSItem("After Execution", "#CCFFCC"); 
		color2 = new GAIGSItem("Before Execution", "#FFDDDD"); 
		color3 = new GAIGSItem("Current Frame", CURRENT_FRAME_COLOR); 
		jvmLegend.setItem(0, 0, color1);
		jvmLegend.setItem(0, 1, color2);
		jvmLegend.setItem(0, 2, color3);
		jvmLegend.disableBox();

		//set stack to initial values, rather than "null"
		for (int i = 0; i < stackSize; i++)
			stack.set("", i);

		//create, set, and sort local var array
		localVariableArray = new GAIGSnewArray(Driver.classes[0].methods.get(currentMethod).numLocals,
				"Local Variables", "#999999", 0.15, 0.1, 0.55, 0.9, 0.15);

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
		for(int j = 0; j < _colorLocalVariableArray.length; j++) {
			boolean label1 = localVariableArray.getRowLabel(j).contains("|");
			boolean label2 = false;
			if(j != _colorLocalVariableArray.length-1)
				label2 = !localVariableArray.getRowLabel(j+1).contains("|");
			else {
				if(num == 0)
					_colorLocalVariableArray[j] = Driver.lightGray;
				else
					_colorLocalVariableArray[j] = Driver.darkGray;
				break;			
			}
			if (label1 && label2) {
				if(num == 0)
				{
					_colorLocalVariableArray[j] = Driver.lightGray;
					_colorLocalVariableArray[j+1] = Driver.lightGray;
					num = 1;
					j++;
				}
				else {
					_colorLocalVariableArray[j] = Driver.darkGray;
					_colorLocalVariableArray[j+1] = Driver.darkGray;
					num = 0;
					j++;
				}
			}
			else {
				if(num == 0) {
					_colorLocalVariableArray[j] = Driver.lightGray;
					num = 1;
				}
				else {
					_colorLocalVariableArray[j] = Driver.darkGray;
					num = 0;
				}
			}
		}
		for(int i = 0; i < _colorLocalVariableArray.length; i++)
			localVariableArray.setColor(i, _colorLocalVariableArray[i]);


		for(int i = 0; i < stackSize; i++)
			stack.setColor(i, Driver.lightGray);
	}
}
