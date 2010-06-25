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
/* passing techniques                                                               */
/*                                                                                  */
/* Discription: The CopyAndRef class generates problems and solves them by using    */
/* by Reference and Copy/Restore.  Variables are stored in the DataStorage class    */
/* ProblemGenerator is used to organize data and print                              */
/*                                                                                  */
/************************************************************************************/

package exe.parampassing;
 
import exe.*;
import java.util.*;
import java.io.*;
import java.text.*;


class CopyAndRef{
 
    //**************************** STATIC VARIABLES ********************************
    //
    //Variables used for probability of occurance
    //
    private static final double ARG_PROB = 1.0;  //arguments will be passed twice
    private static final double VAL_PROB = .45;  //variable will be created
    private static final double VAR_PROB = .50;  //variable will equal a variable
    private static final double ARR_PROB = 1.0;  //array will be created
    private static final double GLB_PROB = .40;  //global will be in an expression
    private static final double FUNC_PROB = .95; //func. var. will be in  an expression

    //
    //Variable Constants
    //
    private static final int MAX = 6;  //max. # of variables/expression will be created
    private static final int MIN = 3;  //min. # of variables/expression will be created
    private static final int MINOFFSET = 1; //offset of min.variables/expression created
    private static final int MAXOFFSET = 3; //offset of max.variables/expression created
    private static final int OFFSET = 1;  //ofset of varaibles values randomly created
    private static final int RANGE = 10;  //range of variables values randomly created  
    private static final int USE = 1;  //1 if the user wants to use that feature
    private static final int NOUSE = 2; //2 if user does not want to use that feature

    private static int arrayNumUsed = 0; //# of arrays used in a scope(should only be 1)
  

    //**************************** PRIVATE VARIABLES ********************************
    private double arg_prob;  //prob. that an argument will be passed more than once
    private int global;  //determine if globals should be created
    private int array;  //determine if arrays should be used
    private ProblemGenerator program; //object of ProblemGenerator

    //**************************** CONSTRUCTOR ********************************
    //
    //the constructor takes 3 variables (ints) in the range of 0 - 1
    //0 if something should not be used, and 1 if it should
    //the 1st param specifies if arguments should be passed twice
    //the 2nd param specifies if global variables should be used
    //the 3rd param specifies if arrays should be used
    //
    public CopyAndRef(int arg, int global, int array){
	program = new ProblemGenerator();
	if(arg == USE)
	    arg_prob = ARG_PROB;
	else
	    arg_prob = 0;
      
	this.global = global;
	this.array = array;
    }//default constructor


    //**************************** ARGUMENT METHODS ********************************
    //    
    //sets the values(objects) found in the function parameter list
    //
    public void arg_list() {
	int size = program.mainSize();  //holds the size of main
	int random = (int) (Math.random() * size); //random # for main index
	DataStorage data, mData;  //temporary DataStorage objects
	int index = 0;
	ArrayList list; 

	//if the random number is equal to ARG_PROB then dupiclate a
	//variable in the function arguments
	if (arg_prob == ARG_PROB) {
	    arg_list_helper(random);
	    index = index + 2;
	} //if

	else {
	    random = size + OFFSET; //set out of index of array so the values at
	                            //this position are not entered again
	} //else

	//loop to fill the rest func_argument list i is the loop index
	for (int i = 0; i < size; i++) {
	    //if the variable is already in the list do nothing
	    if (i == random) { } //if

	    else {
		list = program.getMain(i); //get variable from main
		mData = (DataStorage)list.get(ProblemGenerator.getNamePos());

		//check to see if an array is going to be passed, and if so,
		//then randomly choose only one variable of the array to be passed
		//into the function
		if (mData.getIsArray()){
		    //create a new DataStorage object with a new name
		    data = new DataStorage(program.var_name("func_argument"),  
					   "func_argument",  mData.getArray(), 
					   mData.getIndex(), index);
		} //if
	    
		else{
		    //create a new DataStorage object with a new name
		    data = new DataStorage(program.var_name("func_argument"),  
					   "func_argument", mData.getValue(), index);
		}//else
	    
		//set previous index and name and set the name of the
		//function argument to make sure it is not used again
		data.setPrevIdxPos(mData.getIdxPos());
		data.setPrevName(mData.getName());
		program.setNames("func_argument", data.getName());
	    
		//create a new expression arraylist and save it
		ArrayList fa = new ArrayList();
		fa.add("=var");
		fa.add(data);
		fa.add(mData);
		program.setData("func_argument", fa);
	    
		index++; //incrament
	    } //else
	} //for
    } //arg_list


    //
    //takes a randomly generated number and places twice into func_argument
    //the variable at that position in main
    //
    public void arg_list_helper(int random) {
	DataStorage data, data1, mData;  //temp objects
	ArrayList list;  //temp arrayList
    
	//get an object from main at position random
	list = program.getMain(random); //get variable from main
	mData = (DataStorage)list.get(ProblemGenerator.getNamePos());
    
	if (mData.getIsArray()) {
	    //create two new variables with the same values as mData
	    //give them new names and new position
	    data = new DataStorage(program.var_name("func_argument"), "func_argument", 
				   mData.getArray(), mData.getIndex(), 0);
	    program.setNames("func_argument", data.getName());
	    data1 = new DataStorage(program.var_name("func_argument"), "func_argument", 
				    mData.getArray(), mData.getIndex(), 1);
	} //if

	else{
	    //create two new variables with the same values as mData
	    //give them new names and new position
	    data = new DataStorage(program.var_name("func_argument"),  "func_argument", 
				   mData.getValue(), 0);
	    program.setNames("func_argument", data.getName());
	    data1 = new DataStorage(program.var_name("func_argument"),  "func_argument", 
				    mData.getValue(), 1);
	}//else
    
	data.setPrevIdxPos(mData.getIdxPos());
	data.setPrevName(mData.getName());
	data1.setPrevIdxPos(mData.getIdxPos());
	data1.setPrevName(mData.getName());

	program.setNames("func_argument", data1.getName());
    
	//add data and data1 to the func_argument list
	ArrayList farg = new ArrayList();
	farg.add("=var");
	farg.add(data);
	farg.add(mData);

	//program.setFunc_argument(farg);
	program.setData("func_argument", farg);

	ArrayList farg1 = new ArrayList();
	farg1.add("=var");
	farg1.add(data1);
	farg1.add(mData);

	//program.setFunc_argument(farg1);
	program.setData("func_argument", farg1);  
    } //arg_list_helper
  
  
    //**************************** CREATE PROGRAM ********************************
    //
    //creates every variable that is going to be used in the program
    //
    public void createProgram() throws IOException{
	//creates all global and main variables
	if(global == USE)
	    setData_values("global", (int) (Math.random() * MIN + MINOFFSET), 0);
	
	setData_values("main", (int) ( (Math.random() * MIN) + MINOFFSET), 0);

	arg_list(); //create the argument list
    
	//create function variables
	setData_values("function", (int) ( (Math.random() * MIN) + MINOFFSET), 0);
    
	expression( (int) ( (Math.random() * MAX) + MAXOFFSET)); //print the expressions

	program.printMain(); //generate the main program
    	program.printFunction();//generate the function body
    	evaluate_byReference(); //solve by reference
	reset(); //reset to origional values
	evaluate_byCopyRestore(); //solve by copy/restore
	program.printFile(program.REF); //print all data -- pass in reference so know that
	                                //the program must evaluate by copy/reference
    }//createGlobals
	
  
    //**************************** EXPRESSION METHODS ********************************
    //	
    //loop is the number of variable expressions that are to be created
    //expression will call itself recursively until loop is zero
    //it will randomly create epxressions
    //
    public void expression(int loop) {
	//base case
	if (loop == 0) { return; } //if

	else {
	    double random_number = Math.random(); //create a random number 0-1
	    DataStorage data; //temperary DataStorage variable
	    int randomNumber = 0;

	    //chance that the expression being generated will be
	    //an addition expression else is will be an assignment statement
	    if (random_number < program.getProbability()) {
		data = exp_var(); //get one more variable
		program.setExpression("+");
		expression_helper();
			
		//place values in appropriate array lists
		program.setExpression(data);
	    } //if

	    else {
		program.setExpression("=");
		expression_helper();
	    } //else

	    expression(loop - 1); //recursive call
	} //else
    } //expression
	
    //
    //helper method for the expression method. Sets
    //the first part of the expression
    //
    public void expression_helper() {
	DataStorage temp, temp1;
	int counter = 0; //limits the num. of times the loop tires to find a new variable
	int randomNumber = 0;
	ArrayList list;
    
	//get two variables
	temp = exp_var();
	temp1 = exp_var();

	//ensures that the first variable used is always different
	while (program.containsAssign(temp.getName()) && (counter < 60)) {
	    temp = exp_var();
	    counter++;
	} //while

	//prevent self assignment
	while (temp1 == temp) {
	    temp1 = exp_var();
	} //while

	//add variable names tot he appropriate array lists to keep track of them
	program.setAssignVar(temp.getName());
	program.setExpression(temp);
	program.setExpression(temp1);
    } //expression_helper
  

    //
    //returns a DataStorage object that is the variable you will be using in 
    //the expression
    //
    public DataStorage exp_var() {
	double rand = (Math.random()); //random number 
	DataStorage temp;
	ArrayList list;
	int count = 0;  //keeps track of the number of trys to find a valid variable

	//if rand is 0 and there are global variables
	//make temp equal to the object at a random postion in the
	//global ArrayList
	//else make temp equal to the object at a random position in
	//the func_argument ArrayList
	if (rand <= GLB_PROB) {
	    if (program.globalSize() != 0) {
		do{
		    list = program.getGlobal((int) (Math.random() * 
						    program.globalSize()));
		    temp = (DataStorage) list.get(ProblemGenerator.getNamePos());
		}while(validate(temp.getName(), "global") && (count++ <= 30));
	    } //if
      
	    else{
		list = program.getFunction((int) (Math.random() * program.functionSize()));
	    } //else if
	} //if

	//if random_number is 1 then make temp equal to the object
	//at a random position in the func_argument ArrayList
	else if (rand > GLB_PROB && rand < FUNC_PROB) {
	    do{
		list = program.getFunc_argument((int) (Math.random() * program.func_argumentSize()));
		temp = (DataStorage) list.get(ProblemGenerator.getNamePos());
	    }while(validate(temp.getName(), "func_argument") && (count++ <= 30));
	} //else if

	//if random_number is 2 then make temp equal to the object
	//at a random position in the function ArrayList
	else{
	    list = program.getFunction((int) (Math.random() * program.functionSize()));
	} //else if
    
	if(count >= 20)
	    list = program.getFunction((int) (Math.random() * program.functionSize()));
	
	temp = (DataStorage) list.get(ProblemGenerator.getNamePos());
	return temp;
    } //exp_var
  

    //**************************** GET OBJECT METHOD ********************************
    //
    //returns a random object from the global or main arrayLists
    //
    public ArrayList getObject(){
	double rand = Math.random();
	ArrayList temp;
	int randomM = (int)(Math.random() * program.mainSize());

	//if rand is less than arg_prob and there are globals to use
	if((rand < ARG_PROB) && (program.globalSize() > 0)){
	    
	    //get a random number from 0 to size of global array
	    int randomG = (int)(Math.random() * program.globalSize()); 
	    temp = program.getGlobal(randomG); //get Arraylist at random position
       
	    //check if global variable name is already been declared in main
	    if(program.containsName("main", 
				    ((DataStorage) temp.get(ProblemGenerator.getNamePos())).
				    getName())){
		temp = program.getMain(randomM);
	    }//if
	}//if
	
	else{
	    temp = program.getMain(randomM);
	}//else
	
	return temp;
    }//getObject
  
 
    //**************************** SET METHODS ********************************
    //
    //new_position is where the variable is being created, loop is the loop control
    //recursivly calls itself until loop is 0, creates all new variables
    //
    public void setData_values(String new_position, int loop, int index) {
	if(loop == 0){
	    arrayNumUsed = 0;
	    return;
	}//if
		
	else{
	    DataStorage temp;
	    ArrayList list;
	    double random_number = (Math.random()); //random number 0-1
	
	    //chance that a variable will be assigned
	    if((random_number > VAL_PROB) && (random_number < VAR_PROB)){
		list = setEqVal(new_position, index);
	    }//else if
	
	    //chance that a variable + a variable will be assigned
	    else if((random_number > VAR_PROB) && (random_number < ARR_PROB)){
		list = setPlusVal(new_position, index);
	    }//else if

	    //declare a new DataStorage object for an array
	    else if((array == USE) && (random_number < ARR_PROB) && arrayNumUsed < 1){

		//create an array of size ARRAY_SIZE
		int temp_array[] = new int[DataStorage.getArraySize()]; 
		int randomIndex = (int)(Math.random() * DataStorage.getArraySize());
		//fill the array with values
		for (int i = 0; i < DataStorage.getArraySize(); i++) {
		    temp_array[i] = ( (int)(Math.random() * 10) + 1);
		} //for
	    
		temp = new DataStorage(program.var_name(new_position), new_position, 
				       temp_array, randomIndex, index);
		program.setNames(new_position, temp.getName());
		list = new ArrayList();
		list.add("=val");
		list.add(temp);
		arrayNumUsed++;
	    } //else
	
	    //chance that a value will be assigned
	    else{
		temp = new DataStorage(program.var_name(new_position), new_position, 
				       ( (int)(Math.random() * RANGE) + OFFSET), index);
		program.setNames(new_position, temp.getName());
	    
		list = new ArrayList();
		list.add("=val");
		list.add(temp);
	    }//else if

	    program.setData(new_position, list);
	    setData_values(new_position, loop-1, index+1);
	}//else
    } //setData_values
  

    //
    //set the new value of a variable for Copy Restore
    //
    public void setValueCpy(DataStorage lhs, int value){
	String position; //which array lhs is stored in
	int index; //the array index in which it is stored in

	//get the position and index of the DataStorage object on the left
	//checks to see if the variable can be used in the expression
	position = lhs.getPosition(); 
	index = lhs.getIdxPos(); 
     
	//check in what array it appears
	//retreive the value from the array into lhs 
	//update the history of lhs
	//change the value of lhs
	//overwrite the postion in the array where lhs lives
	
	//Question to be printed to the user
	String question = "What is the value of " + lhs.getName() + 
	    " after the next expression is evaluated?";
     
	if(position == "func_argument"){
	    //get a dataStorage object from the func_argument
	    lhs = (DataStorage)(program.getFunc_argument(index).
				get(ProblemGenerator.getNamePos()));

	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question  before seeing the answer
	    program.addCpyList(program.createFileArray(program.NOCHANGE, "func_argument", 
						       index,"NotUsed", "NotUsed", 
						       "question"));
	    
	    //add a question
	    program.addQuestion(program.CPY, question, String.valueOf(value));

	    //set values and history
	    lhs.addHistoryCpy(value);
	    lhs.setValue(value);
	    program.modifyData("func_argument", index, lhs); // modify data

	    //create another snapshot that indicates the change (no arrows to draw)
	    program.addCpyList(program.createFileArray(program.CHANGE, "func_argument",
						       index, "NotUsed", "NotUsed", 
						       "equation"));
	}//if main
     
	if(position == "function"){
	    //get a dataStorage object from the function
	    lhs = (DataStorage)(program.getFunction(index).
				get(ProblemGenerator.getNamePos()));

	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question before seeing the answer
	    program.addCpyList(program.createFileArray(program.NOCHANGE, "function", index, "NotUsed", 
						       "NotUsed", "question"));

	    //add a question
	    program.addQuestion(program.CPY, question, String.valueOf(value));

	    //set values and history
	    lhs.addHistoryCpy(value);
	    lhs.setValue(value);

	    program.modifyData("function", index, lhs); //modify data

	    //create another snapshot that indicates the change (no arrows to draw)
	    program.addCpyList(program.createFileArray(program.CHANGE, "function", index, "NotUsed", 
						       "NotUsed", "equation"));
	}//if function
     
	if(position == "global"){
	    //get a dataStorage object from global
	    lhs = (DataStorage)(program.getGlobal(index).get(ProblemGenerator.
							     getNamePos()));

	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question before seeing the answer
	    program.addCpyList(program.createFileArray(program.NOCHANGE, "global", index, "NotUsed", 
						       "NotUsed", "question"));

	    //add a question
	    program.addQuestion(program.CPY, question, String.valueOf(value));

	    //set values and history
	    lhs.addHistoryCpy(value);
	    lhs.setValue(value);

	    program.modifyData("global", index, lhs);  //modify data

	    //create another snapshot that indicates the change (no arrows to draw)
	    program.addCpyList(program.createFileArray(program.CHANGE, "global", index, "NotUsed", 
						       "NotUsed", "equation"));
	}//if global
    }//setValue  

  
    //
    //set the new value of a variable for by Reference
    //
    public void setValue(DataStorage lhs, int value){
	String position; //which array lhs is stored in
	int index; //the array index in which it is stored in
	int oldIdx = 0;
	String name = "";

	//get the values of the right hand side
	if(lhs.getPosition() == "func_argument"){
	    position =  "main";
	    index = lhs.getPrevIdxPos();
	    oldIdx = lhs.getIdxPos();
	    name = lhs.getName();
	}//if
	       
	else{
	    position = lhs.getPosition();
	    index = lhs.getIdxPos();
	}//else
     
	//check in what array it appears retreive the value from the array into lhs 
	//update the history of lhs change the value of lhs overwrite the postion in the 
	//array where lhs lives
	
	//question to be presented to the user
	String question = "What is the value of " + lhs.getName() + 
	    " after the next expression is evaluated?";
     
	if(position == "main"){
	    //get a variable from Main
	    lhs = (DataStorage)(program.getMain(index).
				get(ProblemGenerator.getNamePos()));
	
	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question before seeing the answer
	  
	    program.addRefList(program.createFileArray(program.NOCHANGE, "func_argument", 
						       oldIdx, lhs.getName(), 
						       "m", "question"));

	    //changes the value of question
	    question = "What is the value of " + name + 
		" after the next expression is evaluated?";

	    //adds a question
	    program.addQuestion(program.REF, question, String.valueOf(value));

	    //set history and value
	    lhs.addHistory(value);
	    lhs.setValue(value);

	    program.modifyData("main", index, lhs); // modify data

	    //add a snapshot to illistrate the change (with an arrow)
	    program.addRefList(program.createFileArray(program.ARROW, "func_argument", 
						       oldIdx, lhs.getName(), "m", 
						       "equation"));
	}//if main
     
	if(position == "function"){
	    //get a variable from the function
	    lhs = (DataStorage)(program.getFunction(index).
				get(ProblemGenerator.getNamePos()));

	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question before seeing the answer
	    program.addRefList(program.createFileArray(program.NOCHANGE, "function", 
						       index, "NotUsed", "NotUsed", 
						       "question"));

	    //adds a question
	    program.addQuestion(program.REF, question, String.valueOf(value));		

	    //sets history and value
	    lhs.addHistory(value);
	    lhs.setValue(value);

	    program.modifyData("function", index, lhs); // modify data

	    //add a snapshot to illistrate the change (no arrow)
	    program.addRefList(program.createFileArray(program.CHANGE, "function", 
						       index, "NotUsed", "NotUsed", 
						       "equation"));
	}//if function
     
	if(position == "global"){
	    //get a global variable
	    lhs = (DataStorage)(program.getGlobal(index).
				get(ProblemGenerator.getNamePos()));

	    //create a snapshot that does not change values/color
	    //this will be a stopping point to the user can answer the
	    //question before seeing the answer
	    program.addRefList(program.createFileArray(program.NOCHANGE, "global", 
						       index, "NotUsed", "NotUsed", 
						       "question"));

	    //adds question
	    program.addQuestion(program.REF, question, String.valueOf(value));	

	    //set history and values
	    lhs.addHistory(value);
	    lhs.setValue(value);

	    program.modifyData("global", index, lhs); //modify data

	    //add a snapshot to illistrate the change (no arrow)
	    program.addRefList(program.createFileArray(program.CHANGE, "global", 
						       index, "NotUsed", "NotUsed", 
						       "equation"));
	}//if global
    }//setValue  
  

    //
    //returns the arraylist of an entire variable declaration when 
    //the variable = variable
    //
    public ArrayList setEqVal(String new_position, int index){
	DataStorage temp, temp2;
	ArrayList list, list2;
    
	//checks if the new_position is in main or a global. If so, it also checks to make
	//sure globals and main are not empty (so there are values to assign, 
	//its not null)
	if(new_position == "global"){
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   ( (int)(Math.random() * RANGE) + OFFSET), index);
	    program.setNames(new_position, temp.getName());
	    list2 = new ArrayList();
	    list2.add("=val");
	    list2.add(temp);
	}//if    
     
	else if((new_position == "main") && program.mainSize() > 0){
	    //get a value arrayList
	    list = getObject();	

	    //dataStorage object
	    temp2 = (DataStorage)list.get(ProblemGenerator.getNamePos()); 
   
	    //create a new DataStorage object and set its previous position
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   temp2.getValue(), index);
	    temp.setPrevIdxPos(temp2.getPrevIdxPos());
	    program.setNames(new_position, temp.getName());
   
	    //at a new expression (x = y)
	    list2 = new ArrayList();
	    list2.add("=var");
	    list2.add(temp);
	    list2.add(temp2);
	}//if
	    
	else {
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   ( (int)(Math.random() * RANGE) + OFFSET), index);
	    program.setNames(new_position, temp.getName());
	    list2 = new ArrayList();
	    list2.add("=val");
	    list2.add(temp);
	}//else 
    
	return list2;  
    }//setEqVal
  

    //
    //set the variable when then expression is going to be a 
    //variable = a variable + variable
    //returns the entire arrayList
    //
    public ArrayList setPlusVal(String new_position, int index){
	DataStorage temp, temp1, temp2;
	ArrayList list, list1, list2;
	//checks if the new_position is in main or a global. 
	//If so, it also checks to make sure
	//globals and main are not empty (so there are values to assign, its not null)
	if(new_position == "global"){
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   ( (int)(Math.random() * RANGE) + OFFSET), index);
	    program.setNames(new_position, temp.getName());
	    list2 = new ArrayList();
	    list2.add("=val");
	    list2.add(temp);
	}//if    
     
	else if((new_position == "main") && program.mainSize() > 0){
	    //get a value arrayList
	    list = getObject();
	    list1 = getObject();

	    //dataStorage objects
	    temp2 = (DataStorage)list.get(ProblemGenerator.getNamePos()); 
	    temp1 = (DataStorage)list1.get(ProblemGenerator.getNamePos()); 
		
	    int valueStore = temp2.getValue() + temp1.getValue();
		
	    //create a new DataStorage object and set its previous position
	    temp = new DataStorage(program.var_name(new_position), 
				   new_position, valueStore, index);
	    temp.setPrevIdxPos(temp2.getPrevIdxPos());
	    program.setNames(new_position, temp.getName());
	    list2 = new ArrayList();
	    list2.add("+var");
	    list2.add(temp);
	    list2.add(temp2);
	    list2.add(temp1);
	}//else if
    
	else {
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   ( (int)(Math.random() * RANGE) + OFFSET), index);
	    program.setNames(new_position, temp.getName());
	    list2 = new ArrayList();
	    list2.add("=val");
	    list2.add(temp);
	}//else   
    
	return list2;
    }//setPlusVal
  

    //**************************** RHS_VALUE METHODS ********************************
    //
    //returns the int value of the variable at position[index]
    //
    public int rhs_value(String position, int index){
	DataStorage temp;
	if(position == "main"){
	    temp = ((DataStorage)(program.getMain(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//if
    
	else if(position == "function"){
	    temp = ((DataStorage)(program.getFunction(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//else if

	else if(position == "global"){
	    temp = ((DataStorage)(program.getGlobal(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//global

	else{
	    return -1;
	}//else
    }//rhs_value

  
    //
    //returns the int value of the variable at position[index]
    //
    public int rhs_valueCpy(String position, int index){
	DataStorage temp;
	if(position == "func_argument"){
	    temp = ((DataStorage)(program.getFunc_argument(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//if
    
	else if(position == "function"){
	    temp = ((DataStorage)(program.getFunction(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//else if

	else if(position == "global"){
	    temp = ((DataStorage)(program.getGlobal(index).
				  get(ProblemGenerator.getNamePos())));
	    return temp.getValue();
	}//global

	else{
	    return -1;
	}//else
    }//rhs_valueCpy
  
   
    //**************************** RESTORE/RESET METHODS *******************************
    //
    //function that restores the values of the function arguments back to main
    //
    public void restoreValues(){
	int i = 0; //loop control variable
	DataStorage temp1, temp2, temp2Save;
	int restore_val, prev_val, restore_valSave = 0;
	int restore_index, restore_indexSave = 0;
	String tempName = "";
	String question = "";
	String tempNameMain = "";
      
	//go through the func_argument array 
	//restore the values of the arguments to their corresponding position in main
	while(i < program.func_argumentSize()){
	  
	    //get the i-th element of the func_argument array
	    temp1 = (DataStorage)(program.getFunc_argument(i).
	       get(ProblemGenerator.getNamePos()));
	    //get the value of that element
	    restore_val = temp1.getValue();
	    //get the previous index of the argument
	    restore_index = temp1.getPrevIdxPos();
	  
	    //get the element from main with the restore index
	    temp2 = (DataStorage)(program.getMain(restore_index).
				  get(ProblemGenerator.getNamePos()));
	  
	    //if an argument has been passed more than once, and 
	    //i is 0 or 1 (one of the first two positions and there
	    //for the two arguments passed twice
	    if(arg_prob != 0 && i < 2){

		//if the first argument, save the name
		if(i == 0) { tempName = temp1.getName(); }

		//if at the second variable add a question
		else{

		    //create a snapshot that has no change for a question
		    program.addCpyList(program.createFileArray
				       (program.NOCHANGE, "func_argument", i, 
					temp2.getName(), "m", "question"));

		    //create a question
		    question = "What is the value of " + temp2.getName() +
			" after the function return?";

		    //add a question
		    program.addQuestion(program.CPY, question, 
					String.valueOf(restore_val));
		}//else
	    }//if

	    else{
		//if a variable in main has been changed
	        if((temp1.getHistoryCpy()).size() > 1){
		    
		    //add a question snapshot
		    program.addCpyList(program.createFileArray
				       (program.NOCHANGE, "func_argument", i, 
					temp2.getName(), "m", "question"));

		    //create a question
		    question = "What is the value of " + temp2.getName() + 
			" after the function return?";

		    //add a question
		    program.addQuestion(program.CPY, question, 
					String.valueOf(restore_val));
		}//if
	    }//else
	  
	    //store the actual value in the History
	    temp2.addHistoryCpy(restore_val);

	    //set the value of temp2 to value of temp1
	    temp2.setValue(restore_val);
	    
	    temp2Save = temp2; //save the variable before it can change
	  
	  
	    //if an argument has been passed more than once, and 
	    //i is 0 or 1 (one of the first two positions and there
	    //for the two arguments passed twice
	    if(arg_prob != 0 && i < 2){
		
		//if the first at the first variable
		if(i == 0){
		    //save the information about this variable
		    tempNameMain = temp2.getName();
		    restore_indexSave = restore_index;
		    restore_valSave = restore_val;
		}//if

		else{
		    //store the actual value in the History
		    temp2.addHistoryCpy(restore_valSave);
		    
		    //set the value of temp2 to value of temp1
		    temp2.setValue(restore_valSave);
		    program.modifyData("main", restore_indexSave, temp2Save);

		    //add a snapshot
		    program.addCpyList(program.createFileArray
				       (program.ARROW, "func_argument", 
					i-1, tempNameMain, "m", "equation"));
		    //store the actual value in the History
		    temp2.addHistoryCpy(restore_val);
		    //set the value of temp2 to value of temp1
		    temp2.setValue(restore_val);
		    program.modifyData("main", restore_index, temp2);

		    //add a snapshot
		    program.addCpyList(program.createFileArray
				       (program.ARROW, "func_argument", i, 
					temp2.getName(), "m", "equation"));
		}//else
	    }//if
	  
	    else{
		//store the actual value in the History
		temp2.addHistoryCpy(restore_val);
		//set the value of temp2 to value of temp1
		temp2.setValue(restore_val);
	        program.modifyData("main", restore_index, temp2);

		//add a snapshot
	  	program.addCpyList(program.createFileArray
				   (program.ARROW, "func_argument", i, 
				    temp2.getName(), "m", "equation"));
	    }//else
	  
	    i++; //increment i
	}//while      
    }//restoreValues
  

    //
    //restores the original values of the objects so that the program can 
    //be evaluated by Copy and Restore
    //
    public void reset(){
	DataStorage temp;
	int index = 0;  //index position of the first value of the array
    
	//loops through all array lists and resets the origional value so that
	//the program can be evaluated by copy restore
	//all values returned from the history position need to be typed cast back 
	//into ints
	for(int i = 0; i < program.globalSize(); i++){
	    temp = (DataStorage)(program.getGlobal(i).get(ProblemGenerator.getNamePos()));
	    ArrayList t = temp.getHistory();	//gets the history arrayList

	    //convert first value in the array to a string
	    int value = Integer.parseInt((String)t.get(index));

	    //sets the value in the object to what is in postion 1 of the history
	    temp.setValue(value);	
	}//forGlobal
    
	for(int i = 0; i < program.mainSize(); i++){
	    temp = (DataStorage)(program.getMain(i).get(ProblemGenerator.getNamePos()));
	    ArrayList t = temp.getHistory();	//gets the history arrayList

	    //convert first value in the array to a string
	    int value = Integer.parseInt((String)t.get(index));

	    //sets the value in the object to what is in postion 1 of the history
	    temp.setValue(value);	
	}//forMain
    
	for(int i = 0; i < program.func_argumentSize(); i++){
	    temp = (DataStorage)(program.getFunc_argument(i).
				 get(ProblemGenerator.getNamePos()));
	    ArrayList t = temp.getHistory();	//gets the history arrayList

	    //convert first value in the array to a string
	    int value = Integer.parseInt((String)t.get(index)); 

	    //sets the value in the object to what is in postion 1 of the history
	    temp.setValue(value);	
	}//forGlobal
    
	for(int i = 0; i < program.functionSize(); i++){
	    temp = (DataStorage)(program.getFunction(i).get(ProblemGenerator.getNamePos()));
	    ArrayList t = temp.getHistory();	//gets the history arrayList

	    //convert first value in the array to a string
	    int value = Integer.parseInt((String)t.get(index)); 

	    //sets the value in the object to what is in postion 1 of the history
	    temp.setValue(value);	
	}//forfunction

	program.setEvaluate(program.CPY); //change what the program is evaluating
    }//restore   
    
  
    //**************************** EVALUATE METHODS ********************************
    //
    //function that takes an ArrayList of expressions 
    //evaluates the expressions and updates the values of the lhs variables
    //by using copy-restore parameter passing method
    //
    public void evaluate_byCopyRestore(){
	int i = 0; //loop counter variable
	DataStorage lhs, first_rhs, second_rhs;
	int f_rhs_value, s_rhs_value;

	while (i < program.expressionSize()){ 
	    if(program.getExpressionStr(i) == "+"){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expressions on the right hand side 
		first_rhs = program.getExpression(++i);
		second_rhs = program.getExpression(++i);
	       
		//get the values of the right hand sides
		f_rhs_value = rhs_valueCpy(first_rhs.getPosition(), 
					   first_rhs.getIdxPos());
		s_rhs_value = rhs_valueCpy(second_rhs.getPosition(), 
					   second_rhs.getIdxPos());
	       
	       
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueCpy(lhs, f_rhs_value + s_rhs_value);
		
		i++;
	    }//if it is an addition
	   
	    else if(program.getExpressionStr(i) == "="){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expression on the right hand side 
		first_rhs = program.getExpression(++i);

		//get the value of the right hand side 
		f_rhs_value = rhs_valueCpy(first_rhs.getPosition(), 
					   first_rhs.getIdxPos());
	      
		
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueCpy(lhs, f_rhs_value);
		i++;
	    }//if it is an assignment
	}//while

	restoreValues();
    }//evaluate_byCopyRestore


    //
    //1-Parameter function that takes an ArrayList of expressions 
    //evaluates the expressions and updates the values of the lhs variables
    //by using pass-by-refernce parameter passing method
    //
    public void evaluate_byReference(){
	int i = 0; //loop counter variable
	DataStorage lhs, first_rhs, second_rhs;
	int f_rhs_value, s_rhs_value;

	while (i < program.expressionSize()){ 
	    if(program.getExpressionStr(i) == "+"){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expressions on the right hand side 
		first_rhs = program.getExpression(++i);
		second_rhs = program.getExpression(++i);
	       	       
		f_rhs_value = getValue(first_rhs);
	       
		//get the values of the right hand side 
		s_rhs_value = getValue(second_rhs);

		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValue(lhs, f_rhs_value + s_rhs_value);
		
		i++;
	    }//if it is an addition
	   
	    else if(program.getExpressionStr(i) == "="){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expression on the right hand side 
		first_rhs = program.getExpression(++i);

		//get the value of the right hand side 
		f_rhs_value = getValue(first_rhs);
		
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValue(lhs, f_rhs_value);
		i ++;
	    }//if it is an assignment
	}//while
    }//evaluate_byReference

    
    //**************************** GET METHODS ********************************
    public int getValueCpy(DataStorage rhs){
	String position;
	int index;
    
	position = rhs.getPosition();
	index = rhs.getIdxPos();
    	            
	return rhs_valueCpy(position, index); 
    }//getValue
  
    public int getValue(DataStorage first_rhs){
	String position;
	int index;
	if(first_rhs.getPosition() == "func_argument"){
	    position =  "main";
	    index = first_rhs.getPrevIdxPos();
	}//if
	       
	else{
	    position = first_rhs.getPosition();
	    index = first_rhs.getIdxPos();
	}//else
	            
	return rhs_value(position, index); 
    }//getValue

 
    //**************************** VALIDATION METHODS ********************************
    //
    //checks to see if the variable can be used in the expression, 
    //returns true if there is already a variable of the same name
    //declared in a more recent scope
    //
    public boolean validate(String name, String position){
	if(position == "global")
	    if((program.containsName("function", name)) || 
	       (program.containsName("func_argument", name)))
		return true;
	    else
		return false;
	
	
	else if(position == "func_argument")
      	    if(program.containsName("function", name))
      		return true;
	    else
        	return false;
	else
	    return true;
    }    
}//class CopyAndRef
