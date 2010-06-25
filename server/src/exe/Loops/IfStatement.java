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

package exe.Loops;

import java.util.Random;

public class IfStatement
{
    private RandomizedVariables random;
    private Random rand;
    private final String[] ARRAY_NAMES = {"array", "table", "matrix", "values", "data", "info", "results", "a"};
    private final String[] COUNT_NAMES = {"count", "cntr", "c", "counter", "size", "total", "tally"};
  
    private String arrayName;
    private String mathOp;
    private String relOp;
    private String countName;
    private int perLine;
    private int num1;
    private int num2;

    /**
     * Constructor for objects of class IfStatement
     */
    public IfStatement()
    {
        random = new RandomizedVariables();
        rand = new Random();
        
        arrayName = random.randName(ARRAY_NAMES);
        mathOp = random.randMathOperation();
        relOp = random.randRelOpIfs();
        
        if(mathOp.equals("%")){
	        num1 = random.randInt(3,7);
	        num2 = rand.nextInt(num1-2)+1;
        }else if(mathOp.equals("*")){
        	num1 = random.randInt(2,5);
        	num2 = random.randInt(25,40);
        }else{
        	num1 = random.randInt(2,5);
        	num2 = random.randInt(10,20);
        }
    }
    public IfStatement(boolean simple)
    {
    	random = new RandomizedVariables();
        rand = new Random();
    	countName = random.randName(COUNT_NAMES);
    	num1 = random.randInt(1,3); //num1 is the counter's starting value
        perLine = random.randInt(2,4);
    	mathOp = "%";
    	relOp = random.randRelOpIfs();
    	if(relOp == "<" || relOp == "<=")
    	{
    		if(perLine == 2)
    			num2 = 1;
    		else if (perLine == 3)
    			num2 = random.randInt(1,2);
    		else
    			num2 = random.randInt(1,3);
    	}
    	else if (relOp == ">" || relOp == ">=")
    	{
    		if(perLine == 2)
    			num2 = 0;
    		else if (perLine == 3)
    			num2 = 1;
    		else
    			num2 = random.randInt(1,2);
    	}
    }


    /**
     *  
     */
    public String test(String varName)
    {
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "if (" + arrayName + "[" + varName +"] "+ mathOp +" "+ num1 +" "+ op +" "+ num2 +")";
    }
    
    public String test(String varName1, String varName2)
    {
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "if ("+ arrayName + "[" + varName1 +"]["+ varName2 +"] "+ mathOp +" "+ num1 +" "+ op +" "+ num2 +")";
    }
    
    public String testWithoutArrayNameFirst(String varName)
    {
    	if(mathOp.equals("%")){
	        num1 = random.randInt(2,5);
	        num2 = rand.nextInt(num1);
        }else{
        	num1 = random.randInt(2,5);
        	num2 = random.randInt(8,15);
        }
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "if (" + varName +" "+ mathOp +" "+ num1 +" "+ op +" "+ num2 +")";
    }
    public String testWithoutArrayNumFirst(String varName)
    {
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "if (" + num2 +" "+ mathOp +" "+ num1 +" "+ op +" "+ varName +")";
    }
    public String ModifiedIfTest()
    {
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "if (" + countName +" "+ mathOp +" "+ perLine +" "+ op +" "+ num2 + ")";
    }
    public int getperLine()
    {
    	return perLine;
    }
   public String getcountName()
   {
	   return countName;
   }
    public String getArrayName()
    {
        return arrayName;
    }
    
    public String getMathOp()
    {
        return mathOp;
    }
    
    public int getConstant()
    {
        return num1;
    }
    
    public int getTermNum()
    {
        return num2;
    }
    
    public String getRelOp()
    {
        return relOp;
    }
}
