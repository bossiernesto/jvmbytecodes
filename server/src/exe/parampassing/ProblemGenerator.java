/* 
This file is part of JHAVE -- Java Hosted Algorithm Visualization
Environment, developed by Tom Naps, David Furcy (both of the
University of Wisconsin - Oshkosh), Myles McNally (Alma College), and
numerous other contributors who are listed at the http://jhave.org
site

JHAVE is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your
option) any later version.

JHAVE is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with the JHAVE. If not, see:
<http://www.gnu.org/licenses/>.
*/

/************************************************************************************/
/*                                                                                  */
/* Authors: Jessica Gowey & Orjola Kajo                                             */
/* Date: Fall 2003-Spring 2004                                                      */
/* Project: Using algorithum visualiastion to illistrate paramater                  */
/* passing technique                                                                */
/*                                                                                  */
/* Discription: The ProblemGenerator is used to organize data and printing          */
/*                                                                                  */
/************************************************************************************/

package exe.parampassing;
 
import exe.*;
import java.util.*;
import java.text.*;
import java.io.*;

class ProblemGenerator {

    //**************************** STATIC VARIABLES ********************************
    private static final double PROBABILITY = .75; //the prob. a var will occur
    private static final int NAMEPOS = 1; //where the variable is located in name array
    private static final int TYPEPOS = 0; //where the type of expression is specified
    private static final int FIRSTPOS = 2; //first value variable is equal to
    private static final int SNDPOS = 3; //second variable/value variable is equal to
    private static final int RANGE = 3; //range of variables values randomly created
    private static final String VARIABLE_NAMES[] = {
	"a", "b", "c", "d", "e", "f", "g", "h",
	"i", "j", "k", "l", "m", "n", "o", "p"}; //variable names
    private static final int PHASE = 8; //stores the array position that the phase 
                                        //variable can be found at

    private static String function_name; //stores the current name of the function

    //files that store snapshots & html file
    private static String outfile, outfileCpy, outPgm; 

  
   
    //**************************** PUBLIC STATIC VARIABLES *****************************
    //
    //these variables are public so they can be used by all classes
    //
    public static final int REF = 0; //evaluate value that represents by reference
    public static final int CPY = 1; //evaluate value that represents cpy/restore
    public static final int MACRO = 2; //evaluate value that represents Macro
    public static final int NAME = 3; //evaluate value that represents by name
    public static final int CHANGE = 0; //change has occured in a value
    public static final int NOCHANGE = 2; //no change in value has occures
    public static final int ARROW = 1; //change has occured and an arrow is needed
  
  
    //**************************** PRIVATE VARIABLES ********************************
    private int evaluate;	//indicates what is being evaluated (0 = ref,  
                                //1 = copy/restore, 2 = macro, 3 = name 
    private int lineNumber; //keeps track of the line numbers the program is printed
    private int qIndex; //index of vector for questions for by Ref
    private int qIndexCpy; //index of vector for questions for cpyRestore
    private int qIndexMacro; //index of vector for questions for macro
    private int qIndexName; //index of vector for questions for by name
    private int idxRef; //index pointer for questRef
    private int idxCpy; //index pointer for questCpy
    private int idxMacro; //index pointer for questmacro 
    private int idxName; //index pointer for questName 
       

    //***************************** ARRAYLISTS ***********************
    //
    //arrayLists that hold objects of DataStorage class
    //these are the variables being used in the program
    //
    private ArrayList global;
    private ArrayList main;
    private ArrayList func_argument;
    private ArrayList function;

    //arrayLists to hold the names of the variables being used
    private ArrayList globalNames;
    private ArrayList mainNames;
    private ArrayList funcArgNames;
    private ArrayList funcNames;

    //holds the names and operators used in an expression
    //in the order they are used
    private ArrayList expression;

    //holds a list of variables that appear on the left of
    //the = in an expression
    private ArrayList assignVar;
  
    //stores the lists used to create the outfile
    private ArrayList refList;
    private ArrayList cpyList;
    private ArrayList nameList;
    private ArrayList macroList;
    private ArrayList pgmList;

    //stores the questions and answers to be used in snapshots
    private ArrayList questRef;
    private ArrayList questCpy;
    private ArrayList questName;
    private ArrayList questMacro;
  
  
    //***************************** CONSTRUCTOR **********************
    public ProblemGenerator() {
	lineNumber = 0;
	qIndex = 0;
	qIndexCpy = 0;
	qIndexName = 0;
	qIndexMacro = 0;
	idxRef = 0;
	idxCpy = 0;
	idxName = 0;
	idxMacro = 0;
	evaluate = REF;
	global = new ArrayList();
	globalNames = new ArrayList();
	main = new ArrayList();
	mainNames = new ArrayList();
	func_argument = new ArrayList();
	funcArgNames = new ArrayList();
	function = new ArrayList();
	funcNames = new ArrayList();
	expression = new ArrayList();
	assignVar = new ArrayList();
	refList = new ArrayList();
	cpyList = new ArrayList();
	macroList = new ArrayList();
	nameList = new ArrayList();
	pgmList = new ArrayList();
	questRef = new ArrayList();
	questCpy = new ArrayList();
	questName = new ArrayList();
	questMacro = new ArrayList();
    } //default constructor
  
  
    //*************************** ADD METHODS ************************
    public void addCpyList(ArrayList temp){
	cpyList.add(temp);
    }//addCpyList
  
    public void addRefList(ArrayList temp){
	refList.add(temp);
    }//addRefList

    public void addNameList(ArrayList temp){
	nameList.add(temp);
    }//addCpyList
  
    public void addMacroList(ArrayList temp){
	macroList.add(temp);
    }//addRefList
  
    //    
    //adds a question to the correct arrayList
    //
    public void addQuestion(int type, String question, String answer){
	if(type == REF){
	    questRef.add(question);
	    questRef.add(answer);
	}//if
	else if(type == CPY){
	    questCpy.add(question);
	    questCpy.add(answer);
	}//else if
	else if(type == MACRO){
	    questMacro.add(question);
	    questMacro.add(answer);
	}//else if
	else if(type == NAME){
	    questName.add(question);
	    questName.add(answer);
	}//else if
	else
	    System.out.println("Error: Cannot add to question arrayList!");
    }//addQuestion
   
  
    //*************************** CREATE FUNCTIONS *********************
    //
    //loops through all variabls & creates a snapshot then returns an arraylist that 
    //represents the entire snapshot. Type indicates what happens with the variable 
    //(1 = something has changed, 2 = an arrow is needed, anything else and nothing 
    //has happened), loc is the location of the variable, index is the position that 
    //the variable is located in its arraylist, ptname and ptindex are the previous 
    //name and index postions the variable refers to, phase is either evaluate or 
    //declare -- it lets the graphics file know what is happening with the variable
    //for color purposes
    //
    public ArrayList createFileArray(int type, String loc, int index, String ptname, 
				     String ptloc, String phase){
     
	ArrayList list, temp;
	ArrayList header = new ArrayList();  //stores the snapshot header info
	ArrayList footer = new ArrayList();  //stores the snapshot footer info
	ArrayList printList = new ArrayList(); //stores the entire snapshot
	DataStorage data;
	String chgloc;

       
	//header info for snapshot is stored
	//header.add("VIEW DOCS" + " param1.php?file=" + outPgm + "&line=" + lineNum);

        header.add("VIEW DOCS" + " param1.php?file=" + outPgm);
	header.add("Alg_vis");
	header.add("1");
	header.add("Variable Colors: Black = created/changed, Green = global");
        header.add("Red = main, Magenta = arguments, Blue = function ");
	header.add("Key: \"*\" indicates reference pointer (ex: \"*k\")");
	header.add("***\\***");
    
	//compute the total num of variables
	int size = globalSize() + mainSize() + func_argumentSize() + functionSize();
	header.add(String.valueOf(size));
	header.add(phase);
	printList.add(header);  //add the header info to the snapshot
	
	//determine the location and use chgloc to store the first letter of the location
	if(loc.compareTo("main") == 0)
	    chgloc = "m";
	else if(loc.compareTo("global") == 0)
	    chgloc = "g";
	else if(loc.compareTo("func_argument") == 0)
	    chgloc = "a";
	else
	    chgloc = "f";
	
	//loop through every variable and add that portion of the snapshot to printList
	for(int i = 0; i < globalSize(); i++){
	    //get the variables one at a time
	    list = getGlobal(i);
	    data = (DataStorage)list.get(getNamePos());
	    temp = createFileList(data, type, chgloc, "g", index, i, ptname, ptloc);
	    printList.add(temp);	    
	}//for
   
    
	for(int i = 0; i < mainSize(); i++){
	    //get the variables one at a time
	    list = getMain(i);
	    data = (DataStorage)list.get(getNamePos());
	    temp = createFileList(data, type, chgloc, "m", index, i, ptname, ptloc);
	    printList.add(temp);	    
	}//for
        
	for(int i = 0; i < func_argumentSize(); i++){
	    //get the variables one at a time
	    list = getFunc_argument(i);
	    data = (DataStorage)list.get(getNamePos());
	    temp = createFileList(data, type, chgloc, "a", index, i, ptname, ptloc);
	    printList.add(temp);	    
	}//for
    
	for(int i = 0; i < functionSize(); i++){
	    //get the variables one at a time
	    list = getFunction(i);
	    data = (DataStorage)list.get(getNamePos());
	    temp = createFileList(data, type, chgloc, "f", index, i, ptname, ptloc);
	    printList.add(temp);	    
	}//for
     
	//add footer info to the snapshot
	footer.add("***^***");
	printList.add(footer);
        
        //lineNum++;
	return printList; //return printList (one snapshot)
    }//creatFileArray

    //   
    //returns an arraylist representing the snapshot of 1 variable, data is the variable, 
    //Type indicates what happens with the variable (1 = something has changed, 2 = an 
    //arrow is needed, anything else and nothing happened), chgloc is the loc of the
    //variable that has changed, loc is the location of data, 
    //index is the position in the arraylist the changed variable is at, i is the 
    //location in the arraylist data is at ptname and ptindex are the previous name 
    //and index postions the variable refers to
    //
    public ArrayList createFileList(DataStorage data, int type, String chgloc, 
				    String loc, int index, int i, String ptname, 
				    String ptloc){

	ArrayList temp = new ArrayList();
    
	temp.add(data.getName());  //add the var name
	temp.add(loc);	//add the location of the var

	//if type is 0 and the variable index == i
	//list has been reached, no arrow is needed
	if(type == CHANGE && i == index && loc.compareTo(chgloc) == 0){
	    temp.add("true");	
	    temp.add("false");
	}//if

	//change and arrow are both true if type is 1 and i
	//equals the index that has changed
	else if(type == ARROW && index == i && loc.compareTo(chgloc) == 0){
	    temp.add("true");
	    temp.add("true");
	    temp.add(ptname);
	    temp.add(ptloc);
	}//else if
    
	else if(type == ARROW && (data.getName()).compareTo(ptname) == 0 && 
    		ptloc.compareTo(loc) == 0){
	    temp.add("true");
	    temp.add("false");
	}//else if

	else{
	    temp.add("false");
	    temp.add("false");
	}//else

	//return true or false if the var is an array
	temp.add(String.valueOf(data.getIsArray()));
	temp.add(String.valueOf(data.getIndex()));	//add index position of array

	//if variable is an array add all values of the array
	if(data.getIsArray()){
	    int tmp[] = data.getArray();
	    for(int x = 0; x < DataStorage.getArraySize(); x++){
		//if evaluate is by ref, then value is a reference to 
		//the variable in main else store the value itself
		if(evaluate == REF && loc.compareTo("a") == 0 && x == data.getIndex())
		    temp.add("*" + data.getPrevName());
		else if (x == data.getIndex())
		    temp.add(String.valueOf(data.getValue()));
		else
		    temp.add(String.valueOf(tmp[x]));
	    }//for
	}//if
	else
	    //if evaluate is by ref, then value is a reference to 
	    //the variable in main else store the value itself
	    if(evaluate == REF && loc.compareTo("a") == 0)
		temp.add("*" + data.getPrevName());
	    else
		temp.add(String.valueOf(data.getValue()));
    
	return temp;
    }//createFileList
    

    //  
    //creates a question and adds it to the end of the correct snapshot file
    //recieves one file and one int which represents the type of parameter passing
    //technique that is being utilized
    //
    public void createQuestion(PrintWriter out, int type, questionCollection Question){
 
	//if the question is needed for by reference
	if(type == REF){
	    fibQuestion quest = new fibQuestion(out, 
						(new Integer(qIndex)).toString());
	    qIndex++;
	    quest.setQuestionText((String)questRef.get(idxRef++));
	    quest.setAnswer((String)questRef.get(idxRef++));
	    Question.addQuestion(quest);
	    quest.insertQuestion();
	}//if

	//else if the question is needed for cpy/resotre
	else if(type == CPY){
	    fibQuestion quest = new fibQuestion(out, 
						(new Integer(qIndexCpy)).toString());
	    qIndexCpy++;
	    quest.setQuestionText((String)questCpy.get(idxCpy++));
	    quest.setAnswer((String)questCpy.get(idxCpy++));
	    Question.addQuestion(quest);
	    quest.insertQuestion();
	}//else if

	//else if the question is needed for by name
	else if(type == NAME){
	    fibQuestion quest = new fibQuestion(out, 
						(new Integer(qIndexName)).toString());
	    qIndexName++;
	    quest.setQuestionText((String)questName.get(idxName++));
	    quest.setAnswer((String)questName.get(idxName++));
	    Question.addQuestion(quest);
	    quest.insertQuestion();
	}//else if

	//else if the question is needed for Macro
	else if(type == MACRO){
	    fibQuestion quest = new fibQuestion(out, 
						(new Integer(qIndexMacro)).toString());
	    qIndexMacro++;
	    quest.setQuestionText((String)questMacro.get(idxMacro++));
	    quest.setAnswer((String)questMacro.get(idxMacro++));
	    Question.addQuestion(quest);
	    quest.insertQuestion();
	}//else if

	//else error
	else
	    System.out.println("Error: Do not know evaluation type, no question created!");
    }//createQuestion	
    

    //************************** GET FUNCTIONS ***********************
    public static double getProbability() {
	return PROBABILITY;
    } //getProbability

    public static int getNamePos() {
	return NAMEPOS;
    } //getNamePos

    public static int getTypePos() {
	return TYPEPOS;
    } //getTypePos
  
    public int getEvaluate(){
	return evaluate;
    }//getEvaluate

    public static int getFirstPos() {
	return FIRSTPOS;
    } //getFirstPos

    public static int getSndPos() {
	return SNDPOS;
    } //getSndPos

    public static String getVariable(int index) {
	return VARIABLE_NAMES[index];
    } //getVariable
  
    public static String getFunctionName(){
	return function_name;
    } //get function_name
  
    public DataStorage getAssignVar(int i) {
	return (DataStorage) assignVar.get(i);
    } //getAssignvar

    public DataStorage getExpression(int i) {
	return (DataStorage) expression.get(i);
    } //getExpression

    public String getExpressionStr(int i) {
	return (String) expression.get(i);
    } //getExpresion

    public ArrayList getFunc_argument(int i) {
	return (ArrayList) func_argument.get(i);
    } //getFunc_argument

    public ArrayList getFunction(int i) {
	return (ArrayList) function.get(i);
    } //getFunction

    public ArrayList getGlobal(int i) {
	return (ArrayList) global.get(i);
    } //getGlobal

    public ArrayList getMain(int i) {
	return (ArrayList) main.get(i);
    } //getMain

       
    //****************************** SET FUNCTIONS ****************************
    public void setEvaluate(int n){
	evaluate = n;
    }//setEvaluate
  
    //
    //sets the function_name
    //
    public static void setFunctionName(int newName) {
	if (newName == 1) {
	    function_name = "By_Reference/CopyRestore";
	} //if
	else {
	    function_name = "By_Name/Macro";
	} //else
    } //function_name

    public static void setFileName(String file1, String file2, String file3){
	outfile = file1;
	outfileCpy = file2;
	outPgm = file3;
    }//setFIleName
  
    public void setAssignVar(String temp) {
	assignVar.add(temp);
    } //setAssignvar
    
    //
    //new_position is where the variable is being created
    //new_variableis the variable to be stored
    //setData will store the new variable in the correct array list
    //
    public void setData(String new_position, ArrayList new_variable) {
	//determine which array to add the variable too
	for(int a = 0; a < 2; a++){
	    ArrayList temp = new ArrayList();
	    if (new_position == "global") {
		if(a == 0) setGlobal(new_variable);
		temp = createFileArray(CHANGE, new_position, globalSize() - 1, 
				       "NotUsed", "NotUsed", "declare");
	    } //if
	    else if (new_position == "main") {
		if(a == 0) setMain(new_variable);
		temp = createFileArray(CHANGE, new_position, mainSize() - 1, 
				       "NotUsed", "NotUsed", "declare");
	    } //else if
	    else if (new_position == "func_argument") {
		if(a == 0) setFunc_argument(new_variable);
		temp = createFileArray(CHANGE, new_position, func_argumentSize() - 1, 
				       "NotUsed", "NotUsed", "declare");
	    } //else if
	    else if (new_position == "function") {
		if(a == 0) setFunction(new_variable);
		temp = createFileArray(CHANGE, new_position, functionSize() - 1, 
				       "NotUsed", "NotUsed", "declare");
	    } //else if
	    else {
		System.out.println("Error in assigning variables to arrayLists");
	    } //else
	
	    if(evaluate == REF){
		addRefList(temp);
		evaluate = CPY;
	    }//if
	    else if (evaluate == CPY){
		addCpyList(temp);
		evaluate = REF;
	    }//else
	    else if (evaluate == NAME){
		addNameList(temp);
		evaluate = MACRO;
	    }//else
	    else if (evaluate == MACRO){
		addMacroList(temp);
		evaluate = NAME;
	    }//else
	
	}//for
       
    } //setData
    
    public void setExpression(DataStorage temp) {
	expression.add(temp);
    } //setExpression

    public void setExpression(String temp) {
	expression.add(temp);
    } //setExpression

    public void setFunction(ArrayList temp) {
	function.add(temp);
    } //setFunction

    public void setFunc_argument(ArrayList temp) {
	func_argument.add(temp);
    } //setFunc_argument

    public void setGlobal(ArrayList temp) {
	global.add(temp);
    } //setGlobal

    public void setMain(ArrayList temp) {
	main.add(temp);
    } //setMain

    //
    //adds the string name to the appropriate array list
    //
    public void setNames(String position, String name) {
	//determine which array to check
	if (position == "global") {
	    globalNames.add(name);
	} //if
	else if (position == "main") {
	    mainNames.add(name);
	} //else if
	else if (position == "func_argument") {
	    funcArgNames.add(name);
	} //else if
	else if (position == "function") {
	    funcNames.add(name);
	} //else if
	else {
	    System.out.println("Error in checking lists");
	} //else
    } //setNames
    
  
    //********************************* SIZE FUNCTIONS *****************************
    public int assignVarSize() {
	return assignVar.size();
    } //assignVarSize

    public int expressionSize() {
	return expression.size();
    } //expressionSize

    public int func_argumentSize() {
	return func_argument.size();
    } //func_argumentSize

    public int functionSize() {
	return function.size();
    } //functionSize

    public int globalSize() {
	return global.size();
    } //globalSize

    public int mainSize() {
	return main.size();
    } //mainSize

  
    //*********************** CONTAINS FUNCTIONS ********************************  
    //
    //checks to see if the variable name is already been used in that position
    //
    public boolean containsName(String position, String name) {
	//determine which array to check
	if (position == "global") {
	    return globalNames.contains(name);
	} //if
	else if (position == "main") {
	    return mainNames.contains(name);
	} //else if
	else if (position == "func_argument") {
	    return funcArgNames.contains(name);
	} //else if
	else if (position == "function") {
	    return funcNames.contains(name);
	} //else if
	else {
	    System.out.println("Error in checking lists");
	    return true;
	} //else
    } //containsName

    public boolean containsAssign(String name) {
	return assignVar.contains(name);
    } //containsAssign
   
  
    //******************************* INSERT FUNCTIONS **************************
    //
    //replaces a dataStorage object in position[index] with lhs
    //
    public void modifyData(String position, int index, DataStorage lhs) {
	ArrayList list;

	if (position == "main") {
	    list = getMain(index);
	    list.set(ProblemGenerator.getNamePos(), lhs);
	    main.set(index, list);
	} //if

	if (position == "function") {
	    list = getFunction(index);
	    list.set(ProblemGenerator.getNamePos(), lhs);
	    function.set(index, list);
	} //if

	if (position == "global") {
	    list = getGlobal(index);
	    list.set(ProblemGenerator.getNamePos(), lhs);
	    global.set(index, list);
	} //if
    } //modifyData
     
    //
    //new_position is where the variable is being created returns a variable name
    //
    public String var_name(String position) {
	int index;
	//loops through randomly generated index values until an acceptable
	//variable name is found while variable_check is true
	do {
	    index = (int) (Math.random() * (VARIABLE_NAMES.length));
	}while (containsName(position, getVariable(index))); 

	return getVariable(index);
    } //var_name

        
    //********************************** PRINT FUNCTIONS ************************
    //
    //print all the information about one variable
    //
    public void print_data(ArrayList list, String loc) {
	String str; 
	DataStorage temp = (DataStorage) list.get(NAMEPOS); //get var. out of arraylist

	if(loc.compareTo("g") == 0)
	    str = ( (lineNumber++) + "     "); 
	else
	    str = ( (lineNumber++) + "       ");
    
	str = str + "int " + temp.getName(); //print type and name of variable

	//if temp is = to just a value (=val)
	if ( (String) list.get(TYPEPOS) == "=val") {
	    //if temp is an array loop and print the values
	    //else print the temp value
	    if (temp.getIsArray()) {
		str = str + "[" + DataStorage.getArraySize() + "] = {";

		//loop while thought the array and print all values
		for (int i = 0; i < DataStorage.getArraySize(); i++) {
		    str = str + temp.getArray(i);

		    if (i + 1 == DataStorage.getArraySize()) {
			str = str + "};";
			pgmList.add(str);
		    } //if

		    else {
			str = str + ", ";
		    } //else
		} //for
	    } //if

	    else{
		str = str + " = " + temp.getValue() + ";";
		pgmList.add(str);
	    }//else
	} //if

	//checks if variable = variable
	else if ( (String) list.get(TYPEPOS) == "=var") {
	    DataStorage temp2 = (DataStorage) list.get(FIRSTPOS);
      
	    if (temp2.getIsArray()){ //check if array
		str = str + " = " + temp2.getName() + "[" + temp2.getIdxPos() + "];";
		pgmList.add(str);
	    }//if
	    else {
		str = str + " = " + temp2.getName() + ";";
		pgmList.add(str);
	    }
	} //else if

	//checks if variable = variable + variable
	else {
	    DataStorage temp2 = (DataStorage) list.get(FIRSTPOS);

	    if (temp2.getIsArray())  //check if array
		str = str + " = " + temp2.getName() + "[" + temp2.getIdxPos() + "]";
 
	    else
		str = str + " = " + temp2.getName();

	    temp2 = (DataStorage) list.get(SNDPOS);

	    if (temp2.getIsArray()){
		str = str + " + " + temp2.getName() + "[" + temp2.getIdxPos() + "];";
		pgmList.add(str);
	    }//if

	    else{
		str = str +  " + " + temp2.getName() + ";";
		pgmList.add(str);
	    }//else
	} //else
    } //print_data
   

    //
    //Function body is printed to the screen
    //
    public void printFunction() {
	DataStorage temp, temp1, temp2;
	String str;

	pgmList.add( "" + (lineNumber++) );
	str = ((lineNumber++) + "    void " + function_name + "(");

	//loops to print all the names of the fucntion argument and prints them
	for (int i = 0; i < funcArgNames.size(); i++) {
	    str = str + "int " + (String) funcArgNames.get(i);

	    if (i + 1 == funcArgNames.size()) {
		str = str + "){";
		pgmList.add(str);
	    } //if

	    else {
		str = str + ", ";
	    } //else
	} //for

	//prints values in the function arrayList
	for (int i = 0; i < function.size(); i++) {
	    print_data( (ArrayList) function.get(i), "f");  
	} //for

	pgmList.add( "" + (lineNumber++));

	//loops thought all variables in the expression list and prints them
	for (int i = 0; i < expression.size(); i++) {
	    String str2 = (String) expression.get(i++);

	    temp = (DataStorage) expression.get(i);
	    temp1 = (DataStorage) expression.get(++i);

	    //if temp is an array and/or is in the function or a global, 
	    //print out the array index
	    if (((temp.getIsArray()) && (temp.getPosition() == "function")) ||
		(temp.getIsArray() && temp.getPosition() == "global")) 
		str = ( (lineNumber++) + "       " + temp.getName() + "[" +
			temp.getIndex() + "] = ");
	
	    else 
		str = ( (lineNumber++) + "       " + temp.getName() + " = ");

	    if (((temp1.getIsArray()) && (temp1.getPosition() == "function")) ||
		(temp1.getIsArray() && temp1.getPosition() == "global"))
		str = str + (temp1.getName() + "[" + temp1.getIndex() + "]");

	    else 
		str = str + (temp1.getName());

	    if (str2 == "="){ 
		str = str + (";");
		pgmList.add(str);
	    }//if

	    else {
		temp2 = (DataStorage) expression.get(++i);
		if (((temp2.getIsArray()) && (temp2.getPosition() == "function")) ||
		    (temp2.getIsArray() && temp2.getPosition() == "global")){
		    str = str + (" + " + temp2.getName() + "[" + temp2.getIndex() +
				 "];");
		    pgmList.add(str);
		}//if

		else {
		    str = str + (" + " + temp2.getName() + ";");
		    pgmList.add(str);
		}//else
	    } //else
	} //for
   
	pgmList.add((lineNumber++) + "    }" );
    } //end function
   

    //
    //prints the main body of the program
    //
    public void printMain() {

	String str;
	int randomNumber = 0;

	System.out.println();
	//prints values in the global arrayList
	for (int i = 0; i < global.size(); i++) {
	    print_data( (ArrayList) global.get(i), "g");
	} //for

	if(global.size() > 0)
	    pgmList.add("" + lineNumber++);


	pgmList.add( (lineNumber++) +  "     int main() { " );

	//prints values in the main arrayList
	for (int i = 0; i < main.size(); i++) {
	    print_data( (ArrayList) main.get(i), "m");
	} //for

	pgmList.add("" + (lineNumber++));
	str = (lineNumber++) + "       " + function_name + "(";

	//loop through all func_argument's variables and prints them
	for (int i = 0; i < func_argument.size(); i++) {
	    ArrayList list = getFunc_argument(i);
	    DataStorage temp = (DataStorage) list.get(NAMEPOS);
	    ArrayList list2 = (ArrayList) main.get(temp.getPrevIdxPos());
	    DataStorage mdata = (DataStorage) list2.get(NAMEPOS);
	    str = str + mdata.getName();

	    //check to see if an array is going to be passed, and if so,
	    //then randomly choose only one variable of the array to be passed
	    //into the function
	    if (temp.getIsArray()) {
		str = str + "[" + temp.getIndex() + "]";
	    } //if

	    if (i + 1 == func_argument.size()) {
		str = str + ");";
		pgmList.add(str);
	    } //if

	    else {
		str = str + ", ";
	    } //else
	} //for
	
	pgmList.add((lineNumber++) + "     }" );
	pgmList.add("" + (lineNumber++));
    } //printMain
       

    //      
    //Function creates all files and fills the files with the correct information
    //  
    public void printFile(int type) throws IOException{
	File file1 = new File(outfile);
	File file2 = new File(outfileCpy);
	File prog = new File(outPgm);
	PrintWriter graphics = new PrintWriter(new FileWriter(file1));
	PrintWriter graphicsCpy = new PrintWriter(new FileWriter(file2));
	PrintWriter pgm = new PrintWriter(new FileWriter(prog));

	int lineNum = 0;

	//keeps track of the number of times a var in that
	//scope has had its line number changed
	int globalCount = global.size();
	int mainCount = main.size();
	int argCount = func_argument.size();
	int funcCount = function.size();

	
	//one question object
	questionCollection Quest1 = new questionCollection(graphics); 

	//the second question object
	questionCollection Quest2 = new questionCollection(graphicsCpy); 
    
        if(type == REF){
	    evaluate = REF;  //set current evaulation to by Reference

	    for(int g = 0; g < refList.size(); g++){ // loop for arraylist that stores all snapshots
		ArrayList printList = (ArrayList)refList.get(g);
		for(int h = 0; h < printList.size(); h++){ //loop for arraylist that stores variables in one snapshot
		    ArrayList temp1 = (ArrayList)printList.get(h);
		    for(int a = 0; a < temp1.size(); a++){ //loop for arrayList that stored data in one variable
			//if the first arraylist (header) is being evaluated and the phase is 
			//evaluation, then create a question
			if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") != 0){
			    if(global.size() == 0 && globalCount == 0){
			      lineNum++;
			      globalCount --; //decrament globalCount so never used again
                            }//if there are no global variables, incrament one line

			    if(globalCount > 0 && global.size() > 0 ){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      globalCount--;

			       if(globalCount <= 0){
				lineNum+= 2;
			       }//if there are no more globals, increase the line number by 2
			    }//else if there are globals, add the lineNum, then incrament
			    
			    else if(mainCount > 0 && main.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      mainCount--;
			 
			      if(mainCount <= 0){
				lineNum = lineNum + 5;
			      }//if there are no more main variables, add 5 to lineNum
			    }//else if there are main variables, add the line number and incrament lineNum

			    else if(argCount > 0 && func_argument.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum);
			      argCount--;

			      if(argCount <= 0){
				lineNum++;
			      }//if there are no more arguments, add 1 to lineNum
			    }//else if there are argument variables, add lineNum, but do not incrament lineNum

	    
			    else if(funcCount > 0 && function.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      funcCount--;

			      if(funcCount <= 0){
				  lineNum++;
			      }//if there are no more function variables, add 1 to lineNum
			    }//else if there are still func. variables, add one to lineNum 
			      
			    else if(funcCount <= 0){
				  temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			    }//else if there are no more function variables, keep adding one to the line for 
			    //each expression generated
       
			}//if


			//if the header file is currently being looked at, and a question is suposed to be
			//created, then create the question
		        if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") == 0){
			    createQuestion(graphics, evaluate, Quest1);	
			}//if

			//else just print a line from temp to the file
			else
			    graphics.println((String)temp1.get(a));
		    }//for
		}//for
	    }//for
     
	    evaluate = CPY;  //set current evaulation to Cpy/Restore

	    //reset all variables
	    lineNum = 0;
	    globalCount = global.size();
	    mainCount = main.size();
	    argCount = func_argument.size();
	    funcCount = function.size();
	    

	    for(int g = 0; g < cpyList.size(); g++){
		ArrayList printList = (ArrayList)cpyList.get(g);
		for(int h = 0; h < printList.size(); h++){
		    ArrayList temp1 = (ArrayList)printList.get(h);
		    for(int a = 0; a < temp1.size(); a++){

			if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") != 0){
			    if(global.size() == 0 && globalCount == 0){
			      lineNum++;
			      globalCount --; //decrament globalCount so never used again
                            }//if there are no global variables, incrament one line

			    if(globalCount > 0 && global.size() > 0 ){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      globalCount--;

			       if(globalCount <= 0){
				lineNum+= 2;
			       }//if there are no more globals, increase the line number by 2
			    }//else if there are globals, add the lineNum, then incrament
			    
			    else if(mainCount > 0 && main.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      mainCount--;
			 
			      if(mainCount <= 0){
				lineNum = lineNum + 5;
			      }//if there are no more main variables, add 5 to lineNum
			    }//else if there are main variables, add the line number and incrament lineNum

			    else if(argCount > 0 && func_argument.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum);
			      argCount--;

			      if(argCount <= 0){
				lineNum++;
			      }//if there are no more arguments, add 1 to lineNum
			    }//else if there are argument variables, add lineNum, but do not incrament lineNum

	    
			    else if(funcCount > 0 && function.size() > 0){
			      temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			      funcCount--;

			      if(funcCount <= 0){
				  lineNum++;
			      }//if there are no more function variables, add 1 to lineNum
			    }//else if there are still func. variables, add one to lineNum 
			      
			    else if(funcCount <= 0){
				  temp1.set(a, ((String)temp1.get(a)) + "&line=" + lineNum++);
			    }//else if there are no more function variables, keep adding one to the line for 
			    //each expression generated
       
			}//if

			//if the first arraylist (header) is being evaluated and the phase is 
			//evaluation, then create a question	     
			if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") == 0)
			    createQuestion(graphicsCpy, evaluate, Quest2);	     
			else
			    graphicsCpy.println((String)temp1.get(a));
		    }//for
		}//for
	    }//for
	}//if

	else{
	    evaluate = NAME;  //set current evaulation to by Name

	    for(int g = 0; g < nameList.size(); g++){ // loop for arraylist that stores all snapshots
		ArrayList printList = (ArrayList)nameList.get(g);
		for(int h = 0; h < printList.size(); h++){ //loop for arraylist that stores variables in one snapshot
		    ArrayList temp1 = (ArrayList)printList.get(h);
		    for(int a = 0; a < temp1.size(); a++) //loop for arrayList that stored data in one variable
			//if the first arraylist (header) is being evaluated and the phase is 
			//evaluation, then create a question	     
			if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") == 0)
			    createQuestion(graphicsCpy, evaluate, Quest1);	    
			else
			    graphicsCpy.println((String)temp1.get(a));
		}//for
	    }//for
     
	    evaluate = MACRO;  //set current evaulation to Macro

	    for(int g = 0; g < macroList.size(); g++){
		ArrayList printList = (ArrayList)macroList.get(g);
		for(int h = 0; h < printList.size(); h++){
		    ArrayList temp1 = (ArrayList)printList.get(h);
		    for(int a = 0; a < temp1.size(); a++)
			//if the first arraylist (header) is being evaluated and the phase is 
			//evaluation, then create a question	     
			if(h == 0 && a == 0 && ((String)temp1.get(PHASE)).compareTo("question") == 0)
			    createQuestion(graphics, evaluate, Quest2);	     
			else
			    graphics.println((String)temp1.get(a));
		}//for
	    }//for
	}//else

  

	//create HTML file
	pgm.println("<?php");
        pgm.println("$pgm = array(");

	for(int g = 0; g < pgmList.size(); g++){
	    pgm.print("\"" + (String)pgmList.get(g) + "\", ");
	}//for


        pgm.println("\" \");");
        //pgm.println("print(\"<h3>\");");

        pgm.println("for($i = 0; $i < count($pgm); $i++){");
        pgm.println("if($i ==$line){");
        //pgm.println("<a href=\"#line\"></a>);
        //pgm.println("print(\"<a name='line'>\");");
        pgm.println("print(\"<font color = 'red'>$pgm[$i]</font><br>\");");
        pgm.println("}");
        pgm.println("else");
        pgm.println("print(\"$pgm[$i]<br>\");");
        pgm.println("}");
        pgm.println("?>");

	//write question to the file
	Quest1.writeQuestionsAtEOSF();
	Quest2.writeQuestionsAtEOSF();

	//close all files
	graphics.close();
	graphicsCpy.close();
	pgm.close();
    
    }//printFile
} //Problem Genorator
