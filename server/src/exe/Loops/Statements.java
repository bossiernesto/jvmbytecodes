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

// package exe.nes_array 
package exe.Loops;
import java.util.*;
public class Statements
{
    RandomizedVariables rand;
    static Random random = new Random();
    int choice;
    //the first two indicies for the assignment
    private String indexNum1;
    private String indexNum2;
    private String indexName1;
    private String indexName2;
    //math operation performed on the array using the index1 and index2
    private String mathop;
    private String controlVar;
    private int countIter;
    private int num1;
    //two possible choices of what to do to array[index1][index2]
    //the first if perform a mathematical operation based off a number in a specific place in the array
    private String indexNum3 = "";
    private String indexNum4 = "";
    private String indexNum5 = "";
    private String indexNum6 = "";
    //the second is to pick a random number to use
    private int number = -1;
    //string used to create pseudocode
    private String s;
    
    //****If assignments are done to a 1D array you will use index1 as the spot in the array and index3 as the possible random number to perform a mathematical operation on array[index1] 

    public Statements(String name, int size)
    {
        setVariables(false, name, size);
    }

 
    public Statements(String name, int size, String name2, int size2)
    {
        setVariables(true, name, size);
        setNested(name, name2, size2);
    }      
    public Statements(String name2)
    {
    	rand = new RandomizedVariables();
    	controlVar = name2;
    	countIter = rand.randInt(1, 3);
    	mathop = rand.randMathOperation();
    	if(mathop == "+" || mathop == "-")
    		num1 = rand.randInt(1,10);
    	else if (mathop == "*")
    		num1 = rand.randInt(2, 4);
    	else if (mathop == "/")
    		num1 = rand.randInt(70, 85);
    	else if (mathop == "%")
    	{
    		mathop = "+";
    		num1 = rand.randInt(1, 10);
    	}
    }
    public String test()
    {
    	s = "System.out.print(("+ num1 + " " + mathop + " " + controlVar + ") + \"\\t\");";
    	return s;
    }
    public String test(boolean nested, String array)
    {
    	
        if (nested)
        {
        	//pseudocode for the 2D array
            s = array + "[" + indexName1 + indexNum1 +  "][" +indexName2 + indexNum2 + "] = "+ array + "[" + indexName1 + indexNum3 +  "][" + indexName2 + indexNum4  + "] " + mathop;
            if( number > 0)
                s = s + " " + number + ";";
            else
               s = s + " " + array + "[" + indexName1 + indexNum5 + "][" +indexName2 + indexNum6 + "];";
        }
        else
        {
        	//pseudocode for 1D array
             s = s + array + "[" + indexName1 + indexNum1 +  "] = " + array + "[" + indexName1 + indexNum3 +  "] " + mathop;
            if( number > 0)
                s = s + " " + number + ";";
            else
                s = s + " " + array + "[" + indexName1 + indexNum5 + "];";
        }
        return s;
    }
    public void setVariables(boolean nested, String name, int size)
    {
         rand = new RandomizedVariables();
         indexNum1 = rand.randNumIndex();//set IndexNum1
         indexNum3 = rand.randNumIndex();
         mathop = rand.randMathOperation();//set mathop
         //picks randomly if the mathop will use a number or an array spot
         choice = random.nextInt( 2 );
                switch (choice) {
                    case 0: number = rand.randInt(2,5);
                            break;
                    case 1: indexNum5 = rand.randNumIndex();
                            break;
                    default:number = rand.randInt(2,5);
                }
        if(!nested)
            indexName1 = name;//sets the name of index 1 to the string in name
    }//setVariables
    
    public void setNested(String name, String name2, int size2)
    {
        indexNum2 = rand.randNumIndex();//set IndexNum2
        indexNum4 = rand.randNumIndex();
            if (choice == 1)//takes choice from setVariables and if it is 1 then index4 is set
            {
                indexNum6 = rand.randNumIndex();
                while(indexNum6 == indexNum4)
                	indexNum6 = rand.randNumIndex();
            }
            	//indexName1 and IndexName2 are set ***Randomizing the names can be made using the commented lines        ***
            	//                                  *** A check will have to be made so the array doesn't go out of bounds***
                //indexName1 = name;
                //indexName2 = name2;//indexName2 = rand.nameIndex(name2, name);
                indexName1 = rand.nameIndex(name, name2);
                if(indexName1 == name)
                	indexName2 = name2;
                else
                	indexName2 = name;
    }//setNested
    
    public void checkIndexOutOfBoundsOuter(ForLoop forLoop)
    {
    	//Checks to see if the initialNum or terminalNum of the outer for loop is in range 
        if(this.getindexNum1().equals("+1")&& forLoop.getdir().equals("+")){
            if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                forLoop.setTerminalNum(forLoop.getSize() - 2);
            }
        }else if(this.getindexNum1().equals("+1") && forLoop.getdir().equals("-")){
            if(forLoop.getinitialNum() == forLoop.getSize()-1){
                forLoop.setinitialNum(forLoop.getSize() - 2);
            }
        }else if(this.getindexNum3().equals("+1")&& forLoop.getdir().equals("+")){
            if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                    forLoop.setTerminalNum(forLoop.getSize() - 2);
            }
        }else if(this.getindexNum3().equals("+1") && forLoop.getdir().equals("-")){
                if(forLoop.getinitialNum() == forLoop.getSize()-1){
                    forLoop.setinitialNum(forLoop.getSize() - 2);
                }
        }else if(this.getindexNum5().equals("+1")&& forLoop.getdir().equals("+")){
            if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                    forLoop.setTerminalNum(forLoop.getSize() - 2);
            }
        }else if(this.getindexNum5().equals("+1") && forLoop.getdir().equals("-")){
                if(forLoop.getinitialNum() == forLoop.getSize()-1){
                    forLoop.setinitialNum(forLoop.getSize() - 2);
                }
         }      
                
        if(this.getindexNum1().equals("-1")&& forLoop.getdir().equals("+")){
            if(forLoop.getinitialNum() == 0){
                forLoop.setinitialNum(1);
            }
        }else if(this.getindexNum1().equals("-1")&& forLoop.getdir().equals("-")){
            if(forLoop.getTerminalNum() == 0){
                forLoop.setTerminalNum(1);
            }  
        }else if(this.getindexNum3().equals("-1")&& forLoop.getdir().equals("+")){
            if(forLoop.getinitialNum() == 0){
                forLoop.setinitialNum(1);
            }
        }else if(this.getindexNum3().equals("-1")&& forLoop.getdir().equals("-")){
            if(forLoop.getTerminalNum() == 0){
                forLoop.setTerminalNum(1);
            } 
        }else if(this.getindexNum5().equals("-1")&& forLoop.getdir().equals("+")){
            if(forLoop.getinitialNum() == 0){
                forLoop.setinitialNum(1);
            }
        }else if(this.getindexNum5().equals("-1")&& forLoop.getdir().equals("-")){
            if(forLoop.getTerminalNum() == 0){
                forLoop.setTerminalNum(1);
            }  
        }    
        
    }
    
    public void checkIndexOutOfBoundsInner(ForLoop forLoop)
    {
    	//Checks to see if the initialNum or terminalNum of the inner for loop is in range 
    	  if(this.getindexNum2().equals("+1")&& forLoop.getdir().equals("+")){
              if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                  forLoop.setTerminalNum(forLoop.getSize() - 2);
              }
          }else if(this.getindexNum2().equals("+1") && forLoop.getdir().equals("-")){
              if(forLoop.getinitialNum() == forLoop.getSize()-1){
                  forLoop.setinitialNum(forLoop.getSize() - 2);
              }
          }else if(this.getindexNum4().equals("+1")&& forLoop.getdir().equals("+")){
              if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                      forLoop.setTerminalNum(forLoop.getSize() - 2);
              }
          }else if(this.getindexNum4().equals("+1") && forLoop.getdir().equals("-")){
                  if(forLoop.getinitialNum() == forLoop.getSize()-1){
                      forLoop.setinitialNum(forLoop.getSize() - 2);
                  }
          }else if(this.getindexNum6().equals("+1")&& forLoop.getdir().equals("+")){
              if(forLoop.getTerminalNum() == forLoop.getSize()-1){
                      forLoop.setTerminalNum(forLoop.getSize() - 2);
              }
          }else if(this.getindexNum6().equals("+1") && forLoop.getdir().equals("-")){
                  if(forLoop.getinitialNum() == forLoop.getSize()-1){
                      forLoop.setinitialNum(forLoop.getSize() - 2);
                  }
           }      
                  
          if(this.getindexNum2().equals("-1")&& forLoop.getdir().equals("+")){
              if(forLoop.getinitialNum() == 0){
                  forLoop.setinitialNum(1);
              }
          }else if(this.getindexNum2().equals("-1")&& forLoop.getdir().equals("-")){
              if(forLoop.getTerminalNum() == 0){
                  forLoop.setTerminalNum(1);
              }  
          }else if(this.getindexNum4().equals("-1")&& forLoop.getdir().equals("+")){
              if(forLoop.getinitialNum() == 0){
                  forLoop.setinitialNum(1);
              }
          }else if(this.getindexNum4().equals("-1")&& forLoop.getdir().equals("-")){
              if(forLoop.getTerminalNum() == 0){
                  forLoop.setTerminalNum(1);
              }  
          }else if(this.getindexNum6().equals("-1")&& forLoop.getdir().equals("+")){
              if(forLoop.getinitialNum() == 0){
                  forLoop.setinitialNum(1);
              }
          }else if(this.getindexNum6().equals("-1")&& forLoop.getdir().equals("-")){
              if(forLoop.getTerminalNum() == 0){
                  forLoop.setTerminalNum(1);
              }  
          }     
    }
    
    public String getindexNum1()
    {
        return indexNum1;
    }
    public String getindexNum2()
    {
        return indexNum2;
    }
    public String getindexNum3()
    {
        return indexNum3;
    }
    public String getindexNum4()
    {
        return indexNum4;
    }
    public String getindexNum5()
    {
        return indexNum5;
    }
    public String getindexNum6()
    {
        return indexNum6;
    }

    public String getindexName1()
    {
        return indexName1;
    }
    public String getindexName2()
    {
        return indexName2;
    }
    public String getmathop()
    {
        return mathop;
    }
    public int getnumber()
    {
        return number;
    }
    public void setmathop(String newMathOp)
    {
        mathop = newMathOp;
    }
    public int getcountIter()
    {
    	return countIter;
    }
    public int getnum1()
    {
    	return num1;
    }
}
