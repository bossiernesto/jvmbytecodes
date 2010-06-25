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
/* Discription: The DataStorage class will store all the information needed about   */
/* any variable used in the ProblemGenorator, CopyAndRef, NameAndMacro  classes     */
/*                                                                                  */
/************************************************************************************/

package exe.parampassing3;
 
import exe.*;
import java.util.*;
import java.text.*;

class DataStorage {

    //**************************** STATIC VARIABLES ********************************
    private static final int ARRAY_SIZE = 4; //const size of the array


    //**************************** PRIVATE VARIABLES ********************************
    private int array[]; //value of array
    private ArrayList history; //stores the history of the variable values
    private ArrayList historyName; // stores the history of the copyrestore variables
    private int idxPos; //index that the variable is located in its arrayList
    private int index; //index of the array position being used
    private boolean isArray; //false if the object does not store an array
    private String name; //current variable name
    private String position; //(global, main, func_argument, function)
    private int prevIdxPos; //pointer to the index in main the variable refers
    private String prevName; //name of the variable in main that the variable refers
    private int value; //value of variable
    private int varIdx; //stores the idx of the variable used as an array idx
    private String varPreName; //stores the name of the variable used as an array idx

    //**************************** CONSTRUCTORS ********************************
    //    
    //construstor called when variable is not an array
    //
    public DataStorage(String name, String position, int value, int idxPos) {
	history = new ArrayList();
	historyName = new ArrayList();
	addHistory(value);
	addHistoryName(value);
	index = -1;
	isArray = false;
	this.name = name;
	this.position = position;
	this.idxPos = idxPos;
	prevIdxPos = -1;
	this.value = value;
	varIdx = -1;
	varPreName = "";
    } //constructor


    //
    //constructor called when variable is an array
    //
    public DataStorage(String name, String position, int array[], int index,
		       int idxPos) {
	history = new ArrayList();
	historyName = new ArrayList();
	this.array = array;
	this.index = index;
	isArray = true;
	this.name = name;
	this.position = position;
	this.idxPos = idxPos;
	prevIdxPos = -1;
	varIdx = -1;
	value = getArray(index);
	addHistory(getArray(index));
	addHistoryName(getArray(index));
	varPreName = "";
    } //constructor

  
    //**************************** ADD METHODS ********************************
    public void addHistory(int value) {
	String str = String.valueOf(value);
	history.add(str);
    } //addHisotry
    
    public void addHistory(int value[]) {
	history.add(value);
    } //addHisotry
  
    public void addHistoryName(int value) {
	String str = String.valueOf(value);
	historyName.add(str);
    } //addHisotry

     public void addHistoryName( int value[]) {
	historyName.add(value);
    } //addHisotry

  
    //**************************** GET METHODS ********************************
    public int getArray(int index) {
	return array[index];
    } //getArray

    public int[] getArray() {
	return array;
    } //getArray

    public static int getArraySize() {
	return ARRAY_SIZE;
    } //getArraySize

    public ArrayList getHistory() {
	return history;
    } //getHistory
  
    public ArrayList getHistoryName() {
	return historyName;
    } //getHistory

    public int getIdxPos() {
	return idxPos;
    } //getIdxPos

    public int getIndex() {
	return index;
    } //getIndex

    public boolean getIsArray() {
	return isArray;
    } //getisArray

    public String getName() {
	return name;
    } //getName

    public String getPosition() {
	return position;
    } //getPosition

    public int getPrevIdxPos() {
	return prevIdxPos;
    } //getPrevName
  
    public String getPrevName(){
	return prevName;
    }//getPrevName

    public int getValue() {
	return value;
    } //getValue
    
    public int getVarIdx(){
        return varIdx;
    }//getVarIdx
    
    public String getVarPreName(){
       return varPreName;
    }

  
    //**************************** SET METHODS ********************************
    public void setArray(int value, int index) {
	array[index] = value;
    } //setArray

    public void setArray(int array[]) {
	this.array = array;
    } //setArray

    public void setIdxPos(int idxPos) {
	this.idxPos = idxPos;
    } //setIdxPos

    public void setIndex(int index) {
	this.index = index;
    } //setIndex

    public void setPrevIdxPos(int prevIdxPos) {
	this.prevIdxPos = prevIdxPos;
    } //setPrevName
  
    public void setPrevName(String prevName){
	this.prevName = prevName;
    }//setPrevName

    public void setValue(int value) {
	this.value = value;
    } //setValue
    
    public void setVarIdx(int varIdx){
       this.varIdx = varIdx;
    }
    
    public void setVarPreName(String varPreName){
        this.varPreName = varPreName;
    }//setvarPreName
} //class DataStorage
