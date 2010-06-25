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

public class ForLoop
{
    private int size;//size of one dimension of the array
    private String varName;//count variable of the loop
    
    private RandomizedVariables rand;
    private Random random = new Random();
    private int count = 0;// number of loop iterations 
    
    private int iterationCount;//what the loop will go up by
    private String relOp;//
    private String dir;//direction the iteration count will be 
    private int terminalNum;//the terminating condition of the loop
    private int initialNum;//the initial value of varName
    
    private static final String[] VARIABLE_NAMES = {"index", "i", "j", "k", "position", "location"};
    
    /**
     * Constructor for objects of class ForClass
     */
    public ForLoop(int min, int max)
    {
       rand = new RandomizedVariables();
       
       //randomizing all of the variables of the loop
       varName = rand.randName(VARIABLE_NAMES);
       size = rand.randInt(min,max);
       
       dir = rand.randIncDec();
       iterationCount = rand.randInt(1,3);
       relOp = rand.randRelOpFor(dir);
       //the initialNum and terminalNum will be set differently depending on the dir 
       if(dir == "+"){
           initialNum = rand.randInt(1,2);
           terminalNum = rand.randInt(size-2,size-1);
       }else{
           initialNum = rand.randInt(size-2, size-1);
           terminalNum = rand.randInt(1, 2);
       }
    }// ForLoop constructor
    public ForLoop()
    {
       rand = new RandomizedVariables();
       //randomizing all of the variables of the loop
       varName = rand.randName(VARIABLE_NAMES);
       size = rand.randInt(50,70);
       
       dir = rand.randIncDec();

       relOp = rand.randRelOpFor(dir);

       //the initialNum and terminalNum will be set differently depending on the dir along with the iterationCount 
       if(dir == "+"){
           initialNum = rand.randInt(4,10);
           terminalNum = rand.randInt(size/2,size-1);
            int opId = random.nextInt( 2 );
                switch (opId) {
                    case 0: dir = "+";
                    		iterationCount = rand.randInt(4,10);
                    		break;
                    case 1: dir = "*";
                    		iterationCount = rand.randInt(2,3);
                    		break;
                    default: dir =  "+";
                    		 iterationCount = rand.randInt(4,10);
                    		 break;
                }
       }else{
           initialNum = rand.randInt(size/2, size-1);
           terminalNum = rand.randInt(4, 10);
	   int opId = random.nextInt( 2 );
                switch (opId) {
                    case 0: dir =  "-";
                    		iterationCount = rand.randInt(4,10);
                    		break;
                    case 1: dir = "/";
                    		iterationCount = rand.randInt(2,3);
                    		break;
                    default: dir = "-";
                    		 iterationCount = rand.randInt(4,10);
                    		 break;
       }
     }
    }// ForLoop constructor

    
    public String test()
    {
    	String op = relOp.equals("<") ? "%26lt;" : relOp;
    	op = relOp.equals("<=") ? "%26lt;=" : op;
        return "for(int " + varName + " = " + initialNum + "; " + varName 
                + " " + op + " " + terminalNum + "; " + varName + " = " + varName + " " + dir + " "
                + iterationCount + ")";
    }
    
    public String incrementedVariableName()
    {    	
    	return varName + rand.randNumIndex();
    }
    
    public void setNewVariableName()
    {
       varName = rand.randName(VARIABLE_NAMES);
    }
    
    public String getVariableName()
    {
        return varName;
    }
    
    public void setVariableName(String newName)
    {
        varName = newName;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void setSize(int newSize)
    {
        size = newSize;
    }
    public int getinitialNum()
    {
        return initialNum;
    }
    public void setinitialNum(int newNum)
    {
        initialNum = newNum;
    }
    public int getTerminalNum()
    {
        return terminalNum;
    }
    public void setTerminalNum(int newNum)
    {
        terminalNum = newNum;
    }
    public String getrelOp()
    {
        return relOp;
    }
    public void setrelOp(String newRelop)
    {
        relOp = newRelop;
    }
     public int getiterationCount()
    {
        return iterationCount;
    }
    public void setiterationCount(int newIcount)
    {
        iterationCount = newIcount;
    }
      public String getdir()
    {
        return dir;
    }
    public void setdir(String newdir)
    {
        dir = newdir;
    }
    
    public void setCount(int i)
    {
    	count = i;
    }
    
    public int getCount()
    {
    	return count;
    }
     
}// ForLoop Class
