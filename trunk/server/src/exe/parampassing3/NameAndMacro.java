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
/* Discription: The NameAndMacro class generates problems and solves them by using  */
/* by Name and Macro.  Variables are stored in the DataStorage class                */
/* ProblemGenerator is used to organize data and print                              */
/*                                                                                  */
/************************************************************************************/

package exe.parampassing3;
 
import exe.*;
import java.util.*;
import java.io.*;
import java.text.*;


class NameAndMacro{
 
    //**************************** STATIC VARIABLES ********************************
    //
    //Variables used for probability of occurance
    //
    private static final double ARG_PROB = 1.0;  //arguments will be passed twice
    private static final double VAL_PROB = .45;  //variable will be created
    private static final double VAR_PROB = .50;  //variable will equal a variable
    private static final double ARR_PROB = .50;  //array will be created
    private static final double GLB_PROB = .40;  //global will be in an expression
    private static final double FUNC_PROB = .95; //func. var. will be in  an expression
    private static final double IDX_PROB = .5;   //a varialbe will be used as an array index
    private static final double NOMAIN_PROB = .5; //no variables will be used in main

    //
    //Variable Constants
    //
    private static final int MAX = 2;  //max. # of expression will be created
    private static final int MIN = 3;  //min. # of variables will be created
    private static final int MINOFFSET = 1; //offset of min.variables created
    private static final int MAXOFFSET = 3; //offset of max. expression created
    private static final int OFFSET = 1;  //offset of varaibles values randomly created
    private static final int RANGE = 10;  //range of variables values randomly created
    private static final int VOFFSET = 1;  //offset when variable passes as array index
    private static final int VRANGE = 3;  //range when variable passes as array index
    private static final int USE = 1;  //1 if the user wants to use that feature
    private static final int NOUSE = 2; //2 if user does not want to use that feature

    private static int arrayNumUsed = 0; //# of arrays used in a scope(should only be 1)
    private static int varUsedFirst = 0; //tries to make sure if a var is going to be
    					 //passed as an array index, the var will be declared
					 //before the array
  

    //**************************** PRIVATE VARIABLES ********************************
    private double arg_prob;  //prob. that an argument will be passed more than once
    private double idx_prob;  //prob. that a var will be the index of an array
    private int global;  //determigne if globals should be created
    private int array;  //determine if arrays should be used
    private ProblemGenerator program; //object of ProblemGenerator
    private boolean varIdx;  //true when a variable will be used as an array index
    private boolean arrow;  //is true if an arrow is needed to be drawn from a func_arg
    			    //to either a variable in main or func. in by macro


    //**************************** CONSTRUCTOR ********************************
    //
    //the constructor takes three arguments each an int rangeing in value from 0 - 1
    //(one is true/yes, 0 is false/no) the first argument indicates if an argument
    //should be passed twice, the second argument indicates if globals should be used
    //and the third argument indicates if arrays should be used
    //
    public NameAndMacro(int global, int array){
	program = new ProblemGenerator();
	
	arg_prob = 0;  //arguments passed twice is no longer used in this version
		       //the code to produce it, however, remains  -- arg_prob set to 
		       //0 will not allow it to happen
	    
	//randomly decide if a variable should 
	//be used as an array index
	if(IDX_PROB < Math.random())
	    idx_prob = 1;
	else
	    idx_prob = 0;
      
	this.global = global;
	this.array = array;
	varIdx = false;
	program.setEvaluate(program.NAME);
	arrow = false;
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
	
	int tempMainSize;
	 
	//if the random number is greater than
	//the probability that no variables will be
	//used in main, then randomly choose a main size from 0 - MIN -1
	//else add an offset so that main will absolutly have variables
	if(NOMAIN_PROB < Math.random()) {
	    tempMainSize = (int)(Math.random() * MIN);
	    if (tempMainSize == 0) tempMainSize = 1; // kludge to fix programs with no function names called (TLN - 3/12/06)
	}
	else {
	    tempMainSize = (int)((Math.random() * MIN) + MINOFFSET);
	    if (tempMainSize == 0) tempMainSize = 1; // kludge to fix programs with no function names called (TLN - 3/12/06)
	}

	//creates all global and main variables
	if(global == USE)
	    setData_values("global", (int) ( (Math.random() * MIN) + MINOFFSET), 0);
	
	setData_values("main", tempMainSize, 0);

	if(program.mainSize() > 0)
	   arg_list(); //create the argument list
	   
   
	//create function variables
	setData_values("function", (int) ( (Math.random() * MIN) + MINOFFSET), 0);
	
	expression( (int) ( (Math.random() * MAX) + MAXOFFSET)); //print the expressions

	program.printMain(); //generate the main program
    	program.printFunction();//generate the function body
    	evaluate_byName(); //solve by Name

	reset(); //reset to origional values
	
	evaluate_byMacro(); //solve by Macro
	
	program.printFile(program.NAME); //print all data -- pass in reference so know that
	                                //the program must evaluate by copy/reference
    }//createProgram
	
  
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
	    double random_number;
	    
	    //if a variable is passed as an array index, make sure a additon
	    //expression is never generated
	    if(idx_prob == 1)
	    	random_number = 2;
	    else  
	        random_number = Math.random(); //create a random number 0-1
		
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
      
	    else if(program.func_argumentSize() != 0)
	        list = program.getFunc_argument((int) (Math.random() * program.func_argumentSize()));
		
	    else{
		list = program.getFunction((int) (Math.random() * program.functionSize()));
	    } //else if
	} //if

	//if random_number is 1 then make temp equal to the object
	//at a random position in the func_argument ArrayList
	else if (rand > GLB_PROB && rand < FUNC_PROB && program.mainSize() > 0) {
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

	if(count >= 33)
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
        int randomIndex = 0;	//random index used in array
	int varIndex = -1;	//will hold the index so the variable can be found later
	DataStorage var;
	String preVarName = "";
	
	//terminates recursive loop
	if(loop == 0){
	    arrayNumUsed = 0;
	    return;
	}//if
	
	//if a variable should be passed as an array index
	else if (idx_prob == 1 ){
	    DataStorage temp;
	    ArrayList list;
	    double random_number = (Math.random()); //random number 0-1
	    
	    //chance that a variable will be assigned
	    if((random_number > VAL_PROB) && (random_number < VAR_PROB)){
		list = setEqVal(new_position, index);
	    }//else if
	    
	    //if an arry should be used 
	    //declare a new DataStorage object for an array
	    else if((array == USE) && (random_number < ARR_PROB) && arrayNumUsed < 1 && varUsedFirst > 0){
	    		
	        //create an array of size ARRAY_SIZE
		int temp_array[] = new int[DataStorage.getArraySize()]; 
		
		//if the array is being created in the function and there
		//is already a variable in the function
		if(new_position == "function" && program.functionSize() > 0){
		    //get a random number to use as an index to the variables
		    //in the function arraylist
		    randomIndex = (int)(Math.random() * program.functionSize());
		    
		    //retrieve that variable
		    var = (DataStorage)(program.getFunction(randomIndex).
				get(ProblemGenerator.getNamePos()));
		    
		    //stores the index in the arraylist that the variable is located
		    varIndex = randomIndex;
		    
		    //sets the starting value of the index
		    randomIndex = var.getValue();
		    
		    //sets the previous name of the variable
		    preVarName = var.getName();
		}//if
		
		else if (new_position == "main" && program.mainSize() > 0){
		    //get a random number to use as an index to the variables
		    //in the main arraylist
		    randomIndex = (int)(Math.random() * program.mainSize());
		    
		    //retrieve the variable
		    var = (DataStorage)(program.getMain(randomIndex).
				get(ProblemGenerator.getNamePos()));
		    
		    //stores the index of the arrayList that the variable is located
		    varIndex = randomIndex;
		    
		    //sets the starting value of the index
		    randomIndex = var.getValue();
		    
		    //sets the previous name of the variable
		    preVarName = var.getName();
		}//else if
		
		else if (new_position == "global" && program.globalSize() > 0){
		    //get a random number to use as an index to the variables
		    //in the global arraylist
		    randomIndex = (int)(Math.random() * program.globalSize());
		    
		    //retrieve the variable
		    var = (DataStorage)(program.getGlobal(randomIndex).
				get(ProblemGenerator.getNamePos()));
				
		    //stores the index of the arrayList that the variable is located
		    varIndex = randomIndex;
		    
		    //set the starting value of the index
		    randomIndex = var.getValue();
		    
		    //set the previous variable name
		    preVarName = var.getName();
		}//else if
		
		else{
		  
		  //get a random number  4for the index of an array
		  randomIndex = (int)(Math.random() * DataStorage.getArraySize());
		  varIndex = -1; //default varIndex
		  preVarName = ""; //empty string name
		}//else
		
			
		//fill the array with values
		for (int i = 0; i < DataStorage.getArraySize(); i++) {
		    temp_array[i] = ( (int)(Math.random() * DataStorage.getArraySize()));
		} //for
		temp = new DataStorage(program.var_name(new_position), new_position, 
				       temp_array, randomIndex, index);

		temp.setVarIdx(varIndex);  //sets the prev. variable index
		temp.setVarPreName(preVarName);
		
		program.setNames(new_position, temp.getName());
		list = new ArrayList();
		list.add("=val");
		list.add(temp);
		arrayNumUsed++;
		varUsedFirst = 0;
	    } //if
	    
	    //chance that a value will be assigned
	    else{
	        int value = (int)(Math.random() * DataStorage.getArraySize());

		temp = new DataStorage(program.var_name(new_position), new_position, 
				       value, index);
		program.setNames(new_position, temp.getName());
	    
		list = new ArrayList();
		list.add("=val");
		list.add(temp);
		varUsedFirst++;
	    }//else if

	    program.setData(new_position, list);
	    setData_values(new_position, loop-1, index+1);
		
	}//else if
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
		randomIndex = (int)(Math.random() * DataStorage.getArraySize());
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
    //returns the arraylist of an entire variable declaration when 
    //the variable = variable
    //
    public ArrayList setEqVal(String new_position, int index){
	DataStorage temp, temp2;
	ArrayList list, list2;
        int value = 0;
	
	if (idx_prob == 1 )
	  value = (int)(Math.random() * DataStorage.getArraySize());
	else
	  value = ( (int)(Math.random() * RANGE) + OFFSET);
	   
	//checks if the new_position is in main or a global. If so, it also checks to make
	//sure globals and main are not empty (so there are values to assign, 
	//its not null)
	if(new_position == "global"){
	    temp = new DataStorage(program.var_name(new_position), new_position, 
				   value, index);
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
				   value, index);
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
  

    //**************************** EVALUATE METHODS ********************************
    
    //========================= Methods for evaluating By_Name ===================
    
     //
    //function that takes an ArrayList of expressions 
    //evaluates the expressions and updates the values of the lhs variables
    //by using ByName parameter passing method
    //
    public void evaluate_byName(){
    
	int i = 0; //loop counter variable
	//left, first right, and second right hand side variables
	DataStorage lhs, first_rhs, second_rhs;
	//values of the first and second right hand sides
	int f_rhs_value, s_rhs_value;

	while (i < program.expressionSize()){ 
	    if(program.getExpressionStr(i) == "+"){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expressions on the right hand side 
		first_rhs = program.getExpression(++i);
		second_rhs = program.getExpression(++i);
	       
		//get the values of the right hand sides
		f_rhs_value = rhs_valueByName(first_rhs);
		s_rhs_value = rhs_valueByName(second_rhs);
	       
	       
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueName(lhs, f_rhs_value + s_rhs_value);
		
		i++;
	    }//if it is an addition
	   
	    else if(program.getExpressionStr(i) == "="){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expression on the right hand side 
		first_rhs = program.getExpression(++i);

		//get the value of the right hand side 
		f_rhs_value = rhs_valueByName(first_rhs);
	      
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueName(lhs, f_rhs_value);
		i++;
	    }//if it is an assignmentgetValue()
	}//while

    }//evaluate_byName

    //Return the value of a right-hand side variable when using ByName
    public int rhs_valueByName(DataStorage rhs){
    	String position; //which array rhs is stored in
	int index; //the array index in which it is stored in
	
	DataStorage temp; //temporary datastorage object
	
	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//get the position and index of the DataStorage object on the left
	position = rhs.getPosition(); 

	
	//if the rhs variable is a function argument or 
	//if it is a global and a variable with the same name exists in main
	//and the variables are either both arrays or both normal variables
	if((position == "func_argument") || ((position == "global") && (program.getfuncArgNames()).contains(rhs.getName()) &&
		( ((DataStorage)(program.getGlobal(rhs.getIdxPos()).get(ProblemGenerator.getNamePos()))).getIsArray() ==
		((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).indexOf(rhs.getName())).
		get(ProblemGenerator.getNamePos()))).getIsArray()) )){
		
		//get the corresponding index in main 
		if(position == "global")
			//get the index of the variable in main that the func arg with the same name as the global refers to
			index = ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).indexOf(rhs.getName())).
			          get(ProblemGenerator.getNamePos()))).getPrevIdxPos();
		else
			index = rhs.getPrevIdxPos();
			
		//get the corresponding variable from main
		rhs = ((DataStorage)(program.getMain(index).
			get(ProblemGenerator.getNamePos())));

		if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
		   //get the value of the index variable from main
		   temp = ((DataStorage)(program.getMain(rhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));
			    
		    //return the array of rhs
		    array = rhs.getArray();
		    //return the value of array at index with value of temp
		    return array[temp.getValue()]; 
		}//if
		//else if the index is a fixed value or if it is not an array
		else{
		    return rhs.getValue();	    
		}//else
	}//if a function argument or global and in main
	
	//if the variable is a function local
	//or if it is a global but there is a variable with the same name that is a function local
	//changes are made in the function array	
/*	else if((position == "function") || (position == "global" && (program.getglobalNames()).contains(rhs.getName()) &&
		((DataStorage)(program.getGlobal(rhs.getIdxPos()).get(ProblemGenerator.getNamePos()))).getIsArray() ==
		((DataStorage)(program.getFunction((program.getfuncNames()).indexOf(rhs.getName())).
		get(ProblemGenerator.getNamePos()))).getIsArray())){*/
		
	else if((position == "function") || (position == "global" && (program.getfuncNames()).contains(rhs.getName()) &&
		((DataStorage)(program.getGlobal(rhs.getIdxPos()).get(ProblemGenerator.getNamePos()))).getIsArray() ==
		((DataStorage)(program.getFunction((program.getfuncNames()).indexOf(rhs.getName())).
		get(ProblemGenerator.getNamePos()))).getIsArray())){
		
		//get the corresponding index in the function array
		if(position == "function")
			index = rhs.getIdxPos();
		else
			index = (program.getfuncNames()).indexOf(rhs.getName());
		
		//get a dataStorage object from the function
		rhs = (DataStorage)(program.getFunction(index).
			get(ProblemGenerator.getNamePos()));

		//check if it is an array and its index is a variable
		if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
		   //get the value of the index variable from function
		   temp = ((DataStorage)(program.getFunction(rhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));
				    
		    //return the array of lhs
		    array = rhs.getArray();
		    //return the value of array at index with value of temp
		    return array[temp.getValue()];
		}//if
		//else if the index is a fixed value or if it is not an array
		else{
		    return rhs.getValue();
		}//else
	}//if function or global & function
	
	//if the variable is a global and there are no variables with the same name in the function or in main
	else{
		//get the global variable at the index of lhs
		index = rhs.getIdxPos();
		rhs = (DataStorage)(program.getGlobal(index).
			get(ProblemGenerator.getNamePos()));
			
		//check if it is an array and its index is a variable
		if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
		   //get the value of the index variable from function
		   temp = ((DataStorage)(program.getGlobal(rhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));
			
		    //return the array of lhs
		    array = rhs.getArray();
		    //return the value of array at index with value of temp
		    return array[temp.getValue()];
		}//if
		//else if the index is a fixed value or if it is not an array
		else{
		    return rhs.getValue();
		}//else
	}//else
 }//rhs_valueByName
    
        
    //assuming the information for each variable comes from 
    //the expression array and has to be used in order to solve By_Name
    public void setValueName(DataStorage lhs, int value){
    
        String position; //which array lhs is stored in
	int index = 0; //the array index in which it is stored in
	int oldIdx = lhs.getIdxPos();
	
	DataStorage temp; //temporary datastorage object
	
	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//Question to be printed to the user
	String question = "What is the value of " + lhs.getName() + 
	" after the next expression is evaluated?";
	
	//get the position and index of the DataStorage object on the left
	//checks to see if the variable can be used in the expression
	position = lhs.getPosition(); 
	
	//check in what array it appears
	//retreive the value from the array into lhs 
	//update the history of lhs
	//change the value of lhs
	//overwrite the postion in the array where lhs lives
	
	//if the lhs variable is a function argument or 
	//if it is a global and a variable with the same name exists in main
	//and the variables are either both arrays or both normal variables
	if((position == "func_argument") || ((position == "global") && (program.getfuncArgNames()).contains(lhs.getName()) &&
		( ((DataStorage)(program.getGlobal(lhs.getIdxPos()).get(ProblemGenerator.getNamePos()))).getIsArray() ==
		((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).indexOf(lhs.getName())).
		get(ProblemGenerator.getNamePos()))).getIsArray()) )){	
		//get the corresponding index in main 
		if(position == "global")
			//get the index of the variable in main that the func arg with the same name as the global refers to
			index = ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).indexOf(lhs.getName())).
			          get(ProblemGenerator.getNamePos()))).getPrevIdxPos();
		else 
		 	index = lhs.getPrevIdxPos();
			
		//get the corresponding variable from main
		lhs = ((DataStorage)(program.getMain(index).
			get(ProblemGenerator.getNamePos())));
			
		//create a snapshot that does not change values/color
	        //this will be a stopping point to the user can answer the
	        //question before seeing the answer
	        program.addNameList(program.createFileArray(program.NOCHANGE, "func_argument", 
				   oldIdx, lhs.getName(), "m", "question"));
		
		//add a question
		program.addQuestion(program.NAME, question, String.valueOf(value));
		
		//check if it is an array and its index is a variable
		if(lhs.getIsArray() && lhs.getVarIdx() >= 0){
		   //get the value of the index variable from main
		   temp = ((DataStorage)(program.getMain(lhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));

		    //change the value of the element of the array at the index
		    //with the value of temp
		    
		    //return the array of lhs
		    array = lhs.getArray();
		    //change the value of array at index with value of temp
		    array[temp.getValue()] = value;
		    //set the new index position
		    lhs.setIndex(value);
		    //add the array to the history for byName
		    lhs.addHistoryName(array);
		    //set the value for lhs array
		    lhs.setArray(value, temp.getValue());
		    //modify the program data
		    program.modifyData("main", index, lhs); 
		}//if
		//else if the index is a fixed value or if it is not an array
		else{    
		    //set values and history
		    lhs.addHistoryName(value);
		    lhs.setValue(value);
		    program.modifyData("main", index, lhs); // modify data
		}//else
		
		//add a snapshot to illistrate the change (with an arrow)
	        program.addNameList(program.createFileArray(program.ARROW, "func_argument", 
			            oldIdx, lhs.getName(), "m", "equation"));
	}//if a function argument
		
	
	//if the variable is a function local
	//or if it is a global but there is a variable with the same name that is a function local
	//changes are made in the function array	
	else if((position == "function") || (position == "global" && (program.getfuncNames()).contains(lhs.getName()) &&
		((DataStorage)(program.getGlobal(lhs.getIdxPos()).get(ProblemGenerator.getNamePos()))).getIsArray() ==
		((DataStorage)((program.getFunction((program.getfuncNames()).indexOf(lhs.getName()))).get(ProblemGenerator.getNamePos()))).getIsArray())){
		//get the corresponding index in the function array
		
		if(position == "function")
			index = lhs.getIdxPos();
		else
			index = (program.getfuncNames()).indexOf(lhs.getName());
		
		//get a dataStorage object from the function
		lhs = (DataStorage)(program.getFunction(index).
			get(ProblemGenerator.getNamePos()));

		//create a snapshot that does not change values/color
	        //this will be a stopping point to the user can answer the
	        //question before seeing the answer
	        program.addNameList(program.createFileArray(program.NOCHANGE, "function", 
				                           index, "NotUsed", "NotUsed", 
						           "question"));

		//add a question
		program.addQuestion(program.NAME, question, String.valueOf(value));

		//check if it is an array and its index is a variable
		if(lhs.getIsArray() && lhs.getVarIdx()  >= 0){
		   //get the value of the index variable from function
		   temp = ((DataStorage)(program.getFunction(lhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));

		    //change the value of the element of the array at the index
		    //with the value of temp
		    
		    //return the array of lhs
		    array = lhs.getArray();
		    //change the value of array at index with value of temp
		    array[temp.getValue()] = value;
		    //set the new index position
		    lhs.setIndex(value);
		    //add the array to the history for byName
		    lhs.addHistoryName(array);
		    //set the value for lhs array
		    lhs.setArray(value, temp.getValue());
		    //modify the program data
		    program.modifyData("function", index, lhs); 
		    
		}//if
		//else if the index is a fixed value or if it is not an array
		else{
		    //set values and history
		    lhs.addHistoryName(value);
		    lhs.setValue(value);
		    program.modifyData("function", index, lhs); // modify data
		}//else
		
		//add a snapshot to illistrate the change (no arrow)
	        program.addNameList(program.createFileArray(program.CHANGE, "function", 
						           index, "NotUsed", "NotUsed", 
						           "equation")); 	
	}//if function or global & function
	
	//if the variable is a global and there are no variables with the same name in 
	//the function or in main
	else{
	
		//get the global variable at the index of lhs
		index = lhs.getIdxPos();
		lhs = (DataStorage)(program.getGlobal(index).
			get(ProblemGenerator.getNamePos()));
			
		//create a snapshot that does not change values/color
	        //this will be a stopping point to the user can answer the
	        //question before seeing the answer
                program.addNameList(program.createFileArray(program.NOCHANGE, "global", 
						       index, "NotUsed", "NotUsed", 
						       "question"));

                //add a question
		program.addQuestion(program.NAME, question, String.valueOf(value));
			
		//check if it is an array and its index is a variable
		if(lhs.getIsArray() && lhs.getVarIdx() >= 0){
		   //get the value of the index variable from function
		   temp = ((DataStorage)(program.getGlobal(lhs.getVarIdx()).
			get(ProblemGenerator.getNamePos())));

		    //change the value of the element of the array at the index
		    //with the value of temp
		    
		    //return the array of lhs
		    array = lhs.getArray();
		    //change the value of array at index with value of temp
		    array[temp.getValue()] = value;
		    //set the new index position
		    lhs.setIndex(value);
		    //add the array to the history for byName
		    lhs.addHistoryName(array);
		    //set the value for lhs array
		    lhs.setArray(value, temp.getValue());
		    //modify the program data
		    program.modifyData("global", index, lhs); 
		}//if
		//else if the index is a fixed value or if it is not an array
		else{
		    //set values and history
		    lhs.addHistoryName(value);
		    lhs.setValue(value);
		    program.modifyData("global", index, lhs); // modify data
		}//else
		
		//add a snapshot to illistrate the change (no arrow)
	        program.addNameList(program.createFileArray(program.CHANGE, "global", 
						       index, "NotUsed", "NotUsed", 
						       "equation"));	
	}//else
    }//setValueName
    
    
    
//========================= Methods for evaluating By_Macro ===================
    
     //
    //function that takes an ArrayList of expressions 
    //evaluates the expressions and updates the values of the lhs variables
    //by using ByMacro parameter passing method
    //
    public void evaluate_byMacro(){
	int i = 0; //loop counter variable
	//left, first right, and second right hand side variables
	DataStorage lhs, first_rhs, second_rhs;
	//values of the first and second right hand sides
	int f_rhs_value, s_rhs_value;

	while (i < program.expressionSize()){ 
	    if(program.getExpressionStr(i) == "+"){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expressions on the right hand side 
		first_rhs = program.getExpression(++i);
		second_rhs = program.getExpression(++i);
	       
		//get the values of the right hand sides
		f_rhs_value = rhs_valueByMacro(first_rhs);
		s_rhs_value = rhs_valueByMacro(second_rhs);
	       
	       
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueMacro(lhs, f_rhs_value + s_rhs_value);
		
		i++;
	    }//if it is an addition
	   
	    else if(program.getExpressionStr(i) == "="){
		//get the expression left hand side
		lhs = program.getExpression(++i);
		//get the expression on the right hand side 
		first_rhs = program.getExpression(++i);

		//get the value of the right hand side 
		f_rhs_value = rhs_valueByMacro(first_rhs);
	      
		//call to setValue function to set the value of the left hand side
		//also change the history through this function
		setValueMacro(lhs, f_rhs_value);
		i++;
	    }//if it is an assignment
	}//while

    }//evaluate_byMacro
    
//    ===================== RHS by Macro =======================

    //Return the value of a right-hand side variable when using ByMacro
    public int rhs_valueByMacro(DataStorage rhs){
    	String position; //which array rhs is stored in
		
	//get the position and index of the DataStorage object on the left
	position = rhs.getPosition(); 
	
	//if rhs is a function argument
	if(position == "func_argument"){
		return rhs_funcArg(rhs);
	}//if function argument
	
	if(position == "function"){
		return rhs_function(rhs);
	}//if function
	
	if(position == "global"){
		return rhs_global(rhs);
	}//if global
	
	return -1;
    }//rhs_valueByMacro
	
    //function that gets the value of the rhs if a function
    public int rhs_function(DataStorage rhs){
	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//this is needed because we want to make sure that we are getting
	//the most updated value of the variable
	rhs = ((DataStorage)(program.getFunction(rhs.getIdxPos()).
		get(ProblemGenerator.getNamePos())));

	//if rhs is an array with variable index
	if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
	   //get the value of the index variable from function local
	   temp = (DataStorage)((program.getFunction(rhs.getVarIdx())).
	   get(ProblemGenerator.getNamePos()));

	   //return the array of rhs
	   array = rhs.getArray();
	   //return the value of array at index with value of temp
	   return array[temp.getValue()]; 
	}//if
	//else if the index is a fixed value or if it is not an array
	else return rhs.getValue();
    }//rhs_function

    //function that gets the value of the lhs if a func_arg
    public int rhs_funcArg(DataStorage rhs){
	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];

	//if there is a variable in the function locals with the same name and type
	//get the value from the function locals
	if( (program.getfuncNames()).contains(rhs.getPrevName())
	    /*&& 
	    (rhs.getIsArray() == ((DataStorage)(program.getFunction((program.getfuncNames()).
								    indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray())
	    */
	    )
	{
	    //rhs is now the local with the same name and type
	    rhs = (DataStorage)(program.getFunction((program.getfuncNames()).
	           indexOf(rhs.getPrevName())).get(ProblemGenerator.getNamePos()));

	   return rhs_function(rhs);
	}//if local exists
	//else get the value from main
	else{
	
	
	//rhs is now the variable from main -- Correction (TN, 3/16/06, should get it from globals)
	    //	   rhs = ((DataStorage)(program.getMain(rhs.getPrevIdxPos()).
	   rhs = ((DataStorage)(program.getGlobal(rhs.getPrevIdxPos()).
		   get(ProblemGenerator.getNamePos())));
	   
	   return rhs_main(rhs);
	}//else take value from main
    }//rhs_funcArg

    //function that gets the value of the rhs if it is a global
    public int rhs_global(DataStorage rhs){
    	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//if there is a variable in the function locals with the same name and type
	//get the value from the function locals
	if( (program.getfuncNames()).contains(rhs.getName()) && 
	(rhs.getIsArray() == ((DataStorage)(program.getFunction((program.getfuncNames()).
	indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray()) )
	{
	    //rhs is now the local with the same name and type
	    rhs = (DataStorage)(program.getFunction((program.getfuncNames()).
	          indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()));

	    return rhs_function(rhs);
	}//if local exists
	else if ((program.getfuncArgNames()).contains(rhs.getName()) && 
	    (rhs.getIsArray() == ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).
	    indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray())){
	    
	    //position of the func_arg variable that has the same name as the global
	    int index = ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).
	    indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()))).getPrevIdxPos();
	    
	    //get the variable in main that func_arg refers to
	    rhs = (DataStorage) (program.getMain(index).get(ProblemGenerator.getNamePos()));
	    
	    return rhs_main(rhs);	    
	}//if global and func_arg with same name
	//else get the value from main
	else if( (program.getmainNames()).contains(rhs.getName()) && 
	    (rhs.getIsArray() == ((DataStorage)(program.getMain((program.getmainNames()).
	    indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray()) ) 
	{
	
	    //rhs is now the main with the same name and type
	    rhs = (DataStorage)(program.getMain((program.getmainNames()).
	          indexOf(rhs.getName())).get(ProblemGenerator.getNamePos()));

	    return rhs_main(rhs);
	}//if main exists
	//else get the global value
	else{
	    //get the global variable with the index of rhs
	    rhs = ((DataStorage)(program.getGlobal(rhs.getIdxPos()).
		   get(ProblemGenerator.getNamePos())));
	    //if rhs is an array with variable index
	    if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
		//get the value of the index variable from main
		temp = (DataStorage)((program.getGlobal(rhs.getVarIdx())).
		get(ProblemGenerator.getNamePos()));

		//return the array of rhs
		array = rhs.getArray();
		//return the value of array at index with value of temp
		return array[temp.getValue()]; 
	    }//if
	    //else if the index is a fixed value or if it is not an array
	    else return rhs.getValue();
	}
    }//rhs_global		
		
    //get the value of a rhs from main
    public int rhs_main(DataStorage rhs){
    	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
    	//if rhs is an array with variable index
	if(rhs.getIsArray() && rhs.getVarIdx() >= 0){
	   //get the value of the index variable from main
	   temp = (DataStorage)((program.getMain(rhs.getVarIdx())).
		   get(ProblemGenerator.getNamePos()));
	   
	   //return the array of rhs
	   array = rhs.getArray();
	   //return the value of array at index with value of temp
	   return array[temp.getValue()]; 
	}//if
	//else if the index is a fixed value or if it is not an array
	else return rhs.getValue();
    }//rhs_main

   // ======================= LHS by Macro ================================
    
        //assuming the information for each variable comes from 
    //the expression array and has to be used in order to solve By_Macro
    public void setValueMacro(DataStorage lhs, int value){
    	String position; //which array lhs is stored in
	String question = "What is the value of " + lhs.getName() + 
	" after the next expression is evaluated?";
	
	//get the position and index of the DataStorage object on the left
	position = lhs.getPosition(); 
	
	//if lhs is a function argument
	if(position == "func_argument"){
	   set_funcArg(lhs, value);
	}//if function argument
	
	if(position == "function"){
	   //create a snapshot 
           program.addMacroList(program.createFileArray(program.NOCHANGE, "function", 
						    lhs.getIdxPos(), "NotUsed", "NotUsed", 
						    "question"));
	   //add a question
	   program.addQuestion(program.MACRO, question, String.valueOf(value));
	   set_function(lhs, value, -1);
	}//if function
	
	if(position == "global"){
	   //create a snapshot 
           program.addMacroList(program.createFileArray(program.NOCHANGE, "function", 
						    lhs.getIdxPos(), "NotUsed", "NotUsed", 
						    "question"));
	   //add a question
	   program.addQuestion(program.MACRO, question, String.valueOf(value));
           set_global(lhs, value);
	}//if global
    }//lhs_valueByMacro
	
    //set the value of function lhs 	
    public void set_function(DataStorage lhs, int value, int oldIdx){
	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//we want this because we want the most updated value 
	//of the variable
	lhs = ((DataStorage)(program.getFunction(lhs.getIdxPos()).
	 	get(ProblemGenerator.getNamePos())));
		
	//if lhs is an array with variable index
	if(lhs.getIsArray() && lhs.getVarIdx() >= 0){
	   //get the value of the index variable from function local
	   temp = ((DataStorage)(program.getFunction(lhs.getVarIdx()).
	 	  get(ProblemGenerator.getNamePos())));

	   //return the array of lhs
	   array = lhs.getArray();
	   //change the value of array at index with value of temp
	   array[temp.getValue()] = value;
	   //add the array to the history for byName
	   lhs.addHistory(array);
	   //set the value for lhs array
	   lhs.setIndex(value);
	   lhs.setArray(value, temp.getValue());
	   //modify the program data
	   program.modifyData("function", lhs.getIdxPos(), lhs); 
	   
	}//if
	//else if the index is a fixed value or if it is not an array
	else{
	   lhs.addHistory(value);
           lhs.setValue(value);
	   program.modifyData("function", lhs.getIdxPos(), lhs); // modify data
	}//else
	
	//if an arrow needs to be drawn
	if(arrow){
	  //add a snapshot to illistrate the change (no arrow)
	  program.addMacroList(program.createFileArray(program.ARROW, "func_argument", 
					             oldIdx, lhs.getName(), "f", 
						     "equation")); 
	  arrow = false;
	}//if
	else
	  //add a snapshot to illistrate the change (no arrow)
	  program.addMacroList(program.createFileArray(program.CHANGE, "function", 
					             lhs.getIdxPos(), "NotUsed", "NotUsed", 
						     "equation")); 
    }//lhs_function

    //set the value of a function argument lhs
    public void set_funcArg(DataStorage lhs, int value){
	DataStorage temp; //temporary datastorage object
	String question = "What is the value of " + lhs.getName() + 
	" after the next expression is evaluated?";
	
	int oldIdx = lhs.getIdxPos();
	
	//create a snapshot 
        program.addMacroList(program.createFileArray(program.NOCHANGE, "func_argument", 
						    lhs.getIdxPos(), "NotUsed", "NotUsed", 
						    "question"));
	//add a question
	program.addQuestion(program.MACRO, question, String.valueOf(value));

	//array used to be added in the history
	int[] array; 
	array = new int[4];

	//if there is a variable in the function locals with the same name and type
	//get the value from the function locals
	if( (program.getfuncNames()).contains(lhs.getPrevName())
	    /*&& 
	    (lhs.getIsArray() == ((DataStorage)(program.getFunction((program.getfuncNames()).
	    indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray())*/
	    )
	{
 	    lhs = (DataStorage)(program.getFunction((program.getfuncNames()).
	           indexOf(lhs.getPrevName())).get(ProblemGenerator.getNamePos()));
	    arrow = false;
	    set_function(lhs, value, oldIdx);
	}//if local exists
	//else get the value from main -- (Correction TN, 3/16/06 -- get value from globals)
	else{
	    //	    lhs = ((DataStorage)(program.getMain(lhs.getPrevIdxPos()).
	    lhs = ((DataStorage)(program.getGlobal(lhs.getPrevIdxPos()).
	 	    get(ProblemGenerator.getNamePos())));
	    arrow = true;
	    set_main(lhs, value, oldIdx);
	}//else take value from main
    }//lhs_funcArg
    
    //set the value of a main lhs
    public void set_main(DataStorage lhs, int value, int oldIdx){
    	DataStorage temp; //temporary datastorage object

	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//if lhs is an array with variable index
	if(lhs.getIsArray() && lhs.getVarIdx() >= 0){
		//get the value of the index variable from main
		temp = (DataStorage)((program.getMain(lhs.getVarIdx())).
		get(ProblemGenerator.getNamePos()));

		 //return the array of lhs
		array = lhs.getArray();
		//change the value of array at index with value of temp
		array[temp.getValue()] = value;
		//add the array to the history for byName
		lhs.addHistory(array);
		//set the value for lhs array
		lhs.setIndex(value);
		lhs.setArray(value, temp.getValue());
		//modify the program data
	        program.modifyData("main", lhs.getIdxPos(), lhs); 
	}//if
	//else if the index is a fixed value or if it is not an array
	else{
		lhs.addHistory(value);
		lhs.setValue(value);
		program.modifyData("main", lhs.getIdxPos(), lhs); // modify data
		    		
	}//else 
	
	//if an arrow needs to be drawn
	if(arrow){
	  //add a snapshot to illistrate the change (no arrow)
	  program.addMacroList(program.createFileArray(program.ARROW, "func_argument", 
					             oldIdx, lhs.getName(), "m", 
						     "equation")); 
	  arrow = false;
	}//if
	else
	  //add a snapshot to illistrate the change (no arrow)
	  program.addMacroList(program.createFileArray(program.CHANGE, "main", 
					             lhs.getIdxPos(), "NotUsed", "NotUsed", 
						     "equation")); 
     }//set_main
     
     //set the value of a global lhs
     public void set_global(DataStorage lhs, int value){
    	DataStorage temp; //temporary datastorage object
	int oldIdx = lhs.getIdxPos();
	
	//array used to be added in the history
	int[] array; 
	array = new int[4];
	
	//if there is a variable in the function locals with the same name and type
	//get the value from the function locals
	if( (program.getfuncNames()).contains(lhs.getName()) && 
	    (lhs.getIsArray() == ((DataStorage)(program.getFunction((program.getfuncNames()).
	    indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray()) )
	{
		lhs = (DataStorage)(program.getFunction((program.getfuncNames()).
	    	       indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()));
		set_function(lhs, value, -1);	
	}//if function local exists
	//else if the global and a function argument have the same name
	//change the variable in main that the function argument refers to
	else if ((program.getfuncArgNames()).contains(lhs.getName()) && 
	    (lhs.getIsArray() == ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).
	    indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray())){
	    
	    //position of the func_arg variable that has the same name as the global
	    int index = ((DataStorage)(program.getFunc_argument((program.getfuncArgNames()).
	    indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()))).getPrevIdxPos();
	    
	    //get the variable in main that func_arg refers to
	    lhs = (DataStorage) (program.getMain(index).get(ProblemGenerator.getNamePos()));
	    
	    set_main(lhs, value, -1);	    
	}//if global and func_arg with same name
	//else get the value from main
	else if( (program.getmainNames()).contains(lhs.getName()) && 
	         (lhs.getIsArray() == ((DataStorage)(program.getMain((program.getmainNames()).
	         indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()))).getIsArray()) )
	{
		lhs = (DataStorage)(program.getMain((program.getmainNames()).
	                indexOf(lhs.getName())).get(ProblemGenerator.getNamePos()));
		set_main(lhs, value, -1);
	}//if there is a variable in main
	//else get the global value
	else{
	    lhs = (DataStorage)((program.getGlobal(lhs.getIdxPos())).get(ProblemGenerator.getNamePos()));
	
	    //if rhs is an array with variable index
	    if(lhs.getIsArray() && lhs.getVarIdx() >= 0){
		//get the value of the index variable from main
		temp = (DataStorage)((program.getGlobal(lhs.getVarIdx())).
		get(ProblemGenerator.getNamePos()));

		 //return the array of lhs
		 array = lhs.getArray();
		 //change the value of array at index with value of temp
		 array[temp.getValue()] = value;
		 //add the array to the history for byName
		 lhs.addHistory(array);
		 //set the value for lhs array
		 lhs.setIndex(value);
		 lhs.setArray(value, temp.getValue());
		 //modify the program data
		 program.modifyData("global", lhs.getIdxPos(), lhs); 
	    }//if
	    //else if the index is a fixed value or if it is not an array
	    else {
	         lhs.addHistory(value);
		 lhs.setValue(value);
		 program.modifyData("global", lhs.getIdxPos(), lhs); // modify data
	    }//else
	    
	    //add a snapshot to illistrate the change (no arrow)
	    program.addMacroList(program.createFileArray(program.CHANGE, "global", 
					             oldIdx, "NotUsed", "NotUsed", 
						     "equation")); 
	}//else global
   }//set_global
     
	
  
   
    //**************************** RESET METHODS *******************************
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

	program.setEvaluate(program.MACRO); //change what the program is evaluating
    }//restore   
    
  
    
 
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
    
}//class NameAndMacro
