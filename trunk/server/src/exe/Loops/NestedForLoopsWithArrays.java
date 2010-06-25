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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import exe.*;

public class NestedForLoopsWithArrays
{
   private ForLoop forLoop1, forLoop3;
   private ForLoop forLoop2;
   private static IfStatement ifState;
   private Statements state, state2;
   private PseudoCode pseudoCode;
   private NestedForLoopQuestions questClass;
   
   private Operations oper, oper2, oper3, oper4, oper5, oper6, oper7, oper8, oper9, oper10, oper11, oper12, oper13, oper14, oper7b, oper7c, oper8b, oper8c,oper10b, oper10c,oper11b, oper11c;
   private RandomizedVariables random;
   private static Random randInt;
   private int[][] a;
   private int[][] result;
   ArrayList<String> pseudo = new ArrayList<String>();
   ArrayList<question> askOnceQuest = new ArrayList<question>();
   ArrayList<Integer> nextResult = new ArrayList<Integer>();
   private int nextResultCount = 0;
   static  String title = "";
   static int rowSize=0;
   static int colSize=0;
   static GAIGSarray items;
   static GAIGStext line;
   private static ShowFile show;
   boolean flag = false, flag2 = false;
   private static boolean experiencedUser = true;

   int x,y;

   String audio = new String();     //audo which may play at the beginning of a frame initiated by writeSnap()

   Vector<String> line_one = new Vector<String>();
   Vector<String> line_three = new Vector<String>();
   Vector<String> line_five = new Vector<String>();
   Vector<String> line_six = new Vector<String>();
   Vector<String> line_ten = new Vector<String>();
   Vector<String> line_eleven = new Vector<String>();
   Vector<String> line_thirteen = new Vector<String>();

   ArrayList<String> line_one_played = new ArrayList<String>();
	ArrayList<String> line_three_played = new ArrayList<String>();
	ArrayList<String> line_five_played = new ArrayList<String>();
	ArrayList<String> line_six_played = new ArrayList<String>();
	ArrayList<String> line_ten_played = new ArrayList<String>();
	ArrayList<String> line_eleven_played = new ArrayList<String>();
	ArrayList<String> line_thirteen_played = new ArrayList<String>();
   
   public static void main (String [] args) throws IOException {
	    NestedForLoopsWithArrays nfl = new NestedForLoopsWithArrays();
	    
	    if(args[1].equals("E"))
	    	experiencedUser = true;
	    else
	    	experiencedUser = false;
	   
      	show = new ShowFile(args[0]+ ".sho", 10);
		// define the two structures in the show snapshots
		items = new GAIGSarray(rowSize, colSize, title, 
				       "#999999", 0.1, 0.1, 0.9, 0.9, 0.08);
		line = new GAIGStext(0.5, 0.1);
		line.setFontsize(0.055);
		
		try
		{
			int example = randInt.nextInt(3);
			switch (2) {
	        case 0: 
	        	nfl.template1Test();
	    		nfl.template1Execute();
	    		break;
	        case 1:
	        	nfl.template2Test();
	        	nfl.template2Execute();
	        	break;
	        case 2:
	        	nfl.template3Test();
	    		nfl.template3Execute();
	    		break;
	    	default:
	    		nfl.template2Test();
	    		nfl.template2Execute();
	    		break;
			}
		}
		catch (ArithmeticException e)
		{
			show.close();
			NestedForLoopsWithArrays.main(args);
		}
		catch (ElementTooBigException e)
		{
			show.close();
			NestedForLoopsWithArrays.main(args);
		}
		catch (IndexOutOfBoundsException e)
		{
			show.close();
			NestedForLoopsWithArrays.main(args);
		}
	    
	    show.close();
   }
   
    /**
     * Constructor for NestedForLoopsWithArrays class
     */
    public NestedForLoopsWithArrays()
    {

        //populate comment vectors with useful statements
        line_one.add("one_conditional1.wav");
        line_one.add("one_conditional2.wav");
        line_one.add("one_increment.wav");
        line_one.add("one_parts.wav");
        line_one.add("one_rows_or_cols.wav");
        line_one.add("for_loop_outer_body.wav");

        line_three.add("threee_var.value.wav");
        line_three.add("three_if_conditional.wav");

        line_five.add("five_nested.wav");
        line_five.add("one_conditional1.wav");
        line_five.add("one_conditional2.wav");
        line_five.add("one_increment.wav");
        line_five.add("one_parts.wav");
        line_five.add("one_rows_or_cols.wav");

        line_ten.add("five_nested.wav");
        line_ten.add("one_conditional1.wav");
        line_ten.add("one_conditional2.wav");
        line_ten.add("one_increment.wav");
        line_ten.add("one_parts.wav");
        line_ten.add("one_rows_or_cols.wav");

        line_six.add("six_entire_right.wav");

        line_eleven.add("six_rightside_algebra.wav");
        line_eleven.add("six_entire_right.wav");

        line_thirteen.add("thirteen_line1_for.wav");

        //end vector population

        random = new RandomizedVariables();
        randInt = new Random();
        
        pseudoCode = new PseudoCode();
        questClass = new NestedForLoopQuestions();
                    
        forLoop1 = new ForLoop(5,8);
        forLoop2 = new ForLoop(5,8);
        forLoop3 = new ForLoop(5,8);
        
        // checks to make sure the two loop variables are different names
        while(forLoop1.getVariableName().equals(forLoop2.getVariableName())){
            forLoop2.setNewVariableName();
        }
        
        forLoop3.setVariableName(forLoop2.getVariableName());
        
        ifState = new IfStatement();
       
        state = new Statements(forLoop1.getVariableName(),forLoop1.getSize(),forLoop2.getVariableName(),forLoop2.getSize());
        state2 = new Statements(forLoop1.getVariableName(),forLoop1.getSize(),forLoop2.getVariableName(),forLoop2.getSize());
        
        rowSize = forLoop1.getSize();
        if(forLoop2.getSize() >= forLoop3.getSize())
        	colSize = forLoop2.getSize();
        else
        	colSize = forLoop3.getSize();
        if(state.getindexName2() == forLoop1.getVariableName() ) 
        {
        	state.checkIndexOutOfBoundsInner(forLoop1);
        	state.checkIndexOutOfBoundsOuter(forLoop2);
        }
 
        if(state2.getindexName2() == forLoop1.getVariableName())
        {
        	state2.checkIndexOutOfBoundsInner(forLoop3);
        	state2.checkIndexOutOfBoundsInner(forLoop2);
        	state2.checkIndexOutOfBoundsOuter(forLoop1);
        }
       
        state.checkIndexOutOfBoundsOuter(forLoop1);
    	state.checkIndexOutOfBoundsInner(forLoop2);
        state2.checkIndexOutOfBoundsOuter(forLoop1);
    	state2.checkIndexOutOfBoundsInner(forLoop2);
    	state2.checkIndexOutOfBoundsInner(forLoop3);
    	
        oper = new Operations(forLoop1.getrelOp());
        oper2 = new Operations(forLoop1.getdir());
        oper3 = new Operations(forLoop2.getrelOp());
        oper4 = new Operations(forLoop2.getdir());
        oper5 = new Operations(ifState.getRelOp());
        oper6 = new Operations(ifState.getMathOp());
        oper7 = new Operations(state.getindexNum1());
        oper8 = new Operations(state.getindexNum2());
        oper9 = new Operations(state.getmathop());
        oper10 = new Operations(state2.getindexNum1());
        oper11 = new Operations(state2.getindexNum2());
        oper12 = new Operations(state2.getmathop());
        oper13 = new Operations(forLoop3.getrelOp());
        oper14 = new Operations(forLoop3.getdir());
        oper7b = new Operations(state.getindexNum3());
        oper8b = new Operations(state.getindexNum4());
        oper7c = new Operations(state.getindexNum5());
        oper8c = new Operations(state.getindexNum6());
        oper10b = new Operations(state2.getindexNum3());
        oper11b = new Operations(state2.getindexNum4());
        oper10c = new Operations(state2.getindexNum5());
        oper11c = new Operations(state2.getindexNum6());
      
    	a = new int[rowSize][colSize];
    	result = new int[rowSize][colSize];
     
        int randomIntForArray;
        	for(int i = 0; i < rowSize; i++)
            {
                for(int j = 0; j < colSize; j++)
                    {
                		randomIntForArray = random.randInt(1,25);
                		a[i][j] = randomIntForArray;
                		result[i][j] = randomIntForArray;
                    }   
            }
    }// constructor
    
    public void copyArrayToGAIGS(int [][]a, int lineNum, question quest,int rowindex, int colindex) throws IOException
    {
    	 for(int i = 0; i < rowSize; i++)
         {
    		 items.setRowLabel("[" + i + "]", i);
    		 for(int j = 0; j < colSize; j++)
                 {
                    items.set(a[i][j], i, j);
                    items.setColLabel("[" + j + "]", j);
                 }   
         }
    	 if(rowindex != -1 && colindex != -1)
    	 {
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + rowindex + "</pre>");
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + colindex + "</pre>");
    	 }else if(rowindex == -1 && colindex != -1){
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + "</pre>");
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + colindex + "</pre>");
    	 }else if(rowindex != -1 && colindex == -1){
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + rowindex + "</pre>");
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + "</pre>");
    	 }else{
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + "</pre>");
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + "</pre>");
    	 }

        Random rand = new Random();
        int commentIndex;
        final int PROBABILITY_OF_AUDIO = 70;
        int randomPercent;
		  
		  audio = "";

        if (lineNum+1 == 1)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_one.size());
					 if (!line_one_played.contains(line_one.elementAt(commentIndex)))
                {
					 	audio = line_one.elementAt(commentIndex);
					 	line_one_played.add(line_one.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 3)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_three.size());
					 if (!line_three_played.contains(line_three.elementAt(commentIndex)))
					 {
                	audio = line_three.elementAt(commentIndex);
					 	line_three_played.add(line_three.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 5)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_five.size());
					 if (!line_five_played.contains(line_five.elementAt(commentIndex)))
					 {
                	audio = line_five.elementAt(commentIndex);
					   line_five_played.add(line_five.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 6)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_six.size());
					 if (!line_six_played.contains(line_six.elementAt(commentIndex)))
					 {
                	audio = line_six.elementAt(commentIndex);
					 	line_six_played.add(line_six.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 10)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_ten.size());
					 if (!line_ten_played.contains(line_ten.elementAt(commentIndex)))
					 {
                	audio = line_ten.elementAt(commentIndex);
					 	line_ten_played.add(line_ten.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 11)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_eleven.size());
					 if (!line_eleven_played.contains(line_eleven.elementAt(commentIndex)))
					 {
                	audio = line_eleven.elementAt(commentIndex);
					 	line_eleven_played.add(line_eleven.elementAt(commentIndex));
					 }
            }
        }
        else if (lineNum+1 == 13)
        {
            randomPercent = rand.nextInt(101);
            if (randomPercent <= PROBABILITY_OF_AUDIO)
            {
                commentIndex = rand.nextInt(line_thirteen.size());
					 if (!line_thirteen_played.contains(line_thirteen.elementAt(commentIndex)))
					 {
                	audio = line_thirteen.elementAt(commentIndex);
					 	line_thirteen_played.add(line_thirteen.elementAt(commentIndex));
					 }
            }
        }

    	show.writeSnap(ifState.getArrayName(),0.1,null, pseudoCode.pseudo_uri(lineNum, pseudo,
			"<html><head><title>Nested For Loops with Arrays</title></head><body><h1>Nested For Loops with Arrays</h1>"),
			audio, quest, items, line);
    	
    	pseudo.remove(pseudo.size()-1);
    	pseudo.remove(pseudo.size()-1);
    	
    }
    public void template1Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test() + "</pre>");
    	pseudo.add("<pre>2    {</pre>");
    	pseudo.add("<pre>3        " + forLoop2.test() + "</pre>");
    	pseudo.add("<pre>4        {</pre>");
    	pseudo.add("<pre>5            " + ifState.test(forLoop1.getVariableName(), forLoop2.getVariableName()) + "</pre>");
    	pseudo.add("<pre>6                "+ state.test(true, ifState.getArrayName()) + "</pre>");
    	pseudo.add("<pre>7        }</pre>");
    	pseudo.add("<pre>8    }</pre>");
    	pseudo.add("<pre>9    // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    }// template1 Test method
    
    public void template1Execute() throws IOException 
    {
    	/* Filling result array to produce question answers */
    	int innerForLoopCount = 0;
    	int outerForLoopCount = 0;
    	   	
    	for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {        	
    		outerForLoopCount++;
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	innerForLoopCount++;
  /*IF*/        if( oper5.testing(oper6.compute(result[i][j], ifState.getConstant()),ifState.getTermNum())){
					if(state.getindexName1() == forLoop1.getVariableName())
			    	{
			    		 x = i;
			    		 y = j;
			    	}
			    	else
			    	{
			    		x = j;
			    		y = i;
			    	}
                     if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                     {
                        result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber());
                        nextResult.add(oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber()));
                     }else{ //using an array spot to perform the math operation 
                        result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y+ oper8b.conversion()],result[x +oper7c.conversion()][y + oper8c.conversion()]);
                        nextResult.add(oper9.compute(result[x +oper7b.conversion()][y+ oper8b.conversion()],result[x +oper7c.conversion()][y + oper8c.conversion()]));
                     }
               }// if statement
            }// inner for
        }// outer for

        checkElementSize();

    	forLoop1.setCount(outerForLoopCount);
    	forLoop2.setCount(innerForLoopCount);
        
    	/* Start Generating Snapshots */
    	
    	askOnceQuest = questClass.generateAskOnceQuestions(false);
    	int listIndex;
    	
    	copyArrayToGAIGS(a,-1, null, -1,-1);//prints the code. No line is highlighted
    	copyArrayToGAIGS(a,0, null,forLoop1.getinitialNum(),-1);//prints the code. Line 1 is highlighted
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {        	
        	copyArrayToGAIGS(a,1, null,i,-1);
        	if (experiencedUser)
        		copyArrayToGAIGS(a,2, questClass.generateArrayQuestion(i),i,forLoop2.getinitialNum());//prints the code with line 2 highlighted
        	else
        		copyArrayToGAIGS(a,2, questClass.generateArrayElementQuestion(),i,forLoop2.getinitialNum());//prints the code with line 2 highlighted
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	if(state.getindexName1() == forLoop1.getVariableName())
            	{
            		 x = i;
            		 y = j;
            	}
            	else
            	{
            		x = j;
            		y = i;
            	}
            	copyArrayToGAIGS(a,3, null,i,j);//prints the code with line 4 highlighted
            	items.setColor(x, y, "#EE0000");//colors the spot the array is on
            	copyArrayToGAIGS(a,4, null,i,j);
  /*IF*/        if( oper5.testing(oper6.compute(a[i][j], ifState.getConstant()),ifState.getTermNum())){
                     if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                     {
                    	items.setColor(x, y, "#999999");
                    	items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#EE0000");
                    	copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	nextResultCount++;
                        a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber());
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
                     }else{ //using an array spot to perform the math operation 
                    	items.setColor(x, y, "#999999");
                    	items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#EE0000");
                    	copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	nextResultCount++;
                        a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()],a[x +oper7c.conversion()][y+ oper8c.conversion()] );
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
                     }
               }// if statement
                	items.setColor(x, y, "#999999");
                	copyArrayToGAIGS(a,6, null,i,j);//highlights the closing bracket for the inner loop
                	if(askOnceQuest.isEmpty() ){   	
                		copyArrayToGAIGS(a,2, null,i,oper4.compute(j,forLoop2.getiterationCount()));                	
                	}else{
                		listIndex = randInt.nextInt(askOnceQuest.size());            	
                		copyArrayToGAIGS(a,2, askOnceQuest.get(listIndex),i,oper4.compute(j,forLoop2.getiterationCount()));
                    	askOnceQuest.remove(listIndex);
                	}
            }// inner for
            copyArrayToGAIGS(a,7, questClass.generateArrayElementQuestion(),i,-1);//highlights the closing bracket for the outer loop
            copyArrayToGAIGS(a,0, null,oper2.compute(i,forLoop1.getiterationCount()),-1);
        }// outer for
        copyArrayToGAIGS(a,8, null,-1,-1);
    }// template1 Execute method
    
    public void template2Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test()+ "</pre>");
    	pseudo.add("<pre>2    {</pre>");
    	pseudo.add("<pre>3        " + forLoop2.test()+ "</pre>");
    	pseudo.add("<pre>4        {</pre>");
    	pseudo.add("<pre>5            " + ifState.test(forLoop1.getVariableName(), forLoop2.getVariableName())+ "</pre>");
    	pseudo.add("<pre>6                "+ state.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>7            "+"else</pre>" );
    	pseudo.add("<pre>8                "+ state2.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>9        }</pre>");
    	pseudo.add("<pre>10    }</pre>");  
    	pseudo.add("<pre>11    // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    }// template2 Test method
    
    public void template2Execute() throws IOException
    {
    	/* Filling result array to produce question answers */
    	int innerForLoopCount = 0;
    	int outerForLoopCount = 0;
    	   	
    	for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
    		outerForLoopCount++;
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	innerForLoopCount++;
	 /*IF*/     if( oper5.testing(oper6.compute(result[i][j], ifState.getConstant()),ifState.getTermNum())){
		 			if(state.getindexName1() == forLoop1.getVariableName())
			    	{
			    		 x = i;
			    		 y = j;
			    	}
			    	else
			    	{
			    		x = j;
			    		y = i;
			    	}
	     			if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
	     			{
	     				result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber());
	     				nextResult.add(oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber()));
	     			}else{ //using an array spot to perform the math operation 
	     				result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()],result[x +oper7c.conversion()][y + oper8c.conversion()]);
	     				nextResult.add(oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()],result[x +oper7c.conversion()][y + oper8c.conversion()]));
	     			}		
 /*ELSE*/       }else{
					 if(state2.getindexName1() == forLoop1.getVariableName())
				 	{
				 		 x = i;
				 		 y = j;
				 	}
				 	else
				 	{
				 		x = j;
				 		y = i;
				 	}
                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {
                        result[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber());
                        nextResult.add(oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber()));
                   }//if
                   else//using an array spot to perform the math operation
                   {
                        result[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()],result[x +oper10c.conversion()][y + oper11c.conversion()] );
                        nextResult.add(oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()],result[x +oper10c.conversion()][y + oper11c.conversion()] ));
                   }//inner else
               }//outer else
            }// inner for
        }// outer for
        
    	checkElementSize();
    	
    	forLoop1.setCount(outerForLoopCount);
    	forLoop2.setCount(innerForLoopCount);
    	
    	/* Start Generating Snapshots */
    	
    	askOnceQuest = questClass.generateAskOnceQuestions(false);
    	int listIndex;
    	
    	copyArrayToGAIGS(a,-1, null,-1,-1);//prints the code. No line is highlighted
    	copyArrayToGAIGS(a,0, null, forLoop1.getinitialNum(), -1);//prints the code with line 1 highlighted
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	if(experiencedUser)
        		copyArrayToGAIGS(a,1, questClass.generateArrayQuestion(i),i,-1);//prints the code with line 2 highlighted
        	else
        		copyArrayToGAIGS(a,1, questClass.generateArrayElementQuestion(),i,-1);//prints the code with line 2 highlighted
        	copyArrayToGAIGS(a,2, null,i,forLoop2.getinitialNum());//prints the code with line 2 highlighted
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	copyArrayToGAIGS(a,3, null,i,j);//prints the code with line 4 highlighted
            	items.setColor(i, j, "#EE0000");//colors the spot the array is on
           	    copyArrayToGAIGS(a,4, null,i,j);//prints the code with line 5 highlighted
           	    items.setColor(i,j, "#999999");
 /*IF*/    if( oper5.testing(oper6.compute(a[i][j], ifState.getConstant()),ifState.getTermNum())){
				 if(state.getindexName1() == forLoop1.getVariableName())
			 	{
			 		 x = i;
			 		 y = j;
			 	}
			 	else
			 	{
			 		x = j;
			 		y = i;
			 	}
     			if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
     			{
     				items.setColor(x, y, "#999999");
     				items.setColor(x +oper7.conversion(),y+ oper8.conversion(), "#EE0000");
     				copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
     				a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber());
     				nextResultCount++;
     				items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
     				copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
     				items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
     			}else{ //using an array spot to perform the math operation 
     				items.setColor(x, y, "#999999");
     				items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#EE0000");
     				copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
     				a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()],a[x +oper7c.conversion()][y + oper8c.conversion()]);
     				nextResultCount++;
     				items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
     				copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
     				items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
     				}		
 /*ELSE*/      }else{
	 				items.setColor(x, y, "#999999");
	 				copyArrayToGAIGS(a,6, null,i,j);
					 if(state2.getindexName1() == forLoop1.getVariableName())
				 	{
				 		 x = i;
				 		 y = j;
				 	}
				 	else
				 	{
				 		x = j;
				 		y = i;
				 	}

                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {
                	   	items.setColor(x, y, "#999999");
                	   	items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#EE0000");
                	   	copyArrayToGAIGS(a,7, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                	   	//assignment statement
                        a[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(a[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber());
                        nextResultCount++;
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,7, null,i,j);//highlights assignment statement
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#999999");
                   }//if
                    else//using an array spot to perform the math operation
                    {
                	   	items.setColor(x, y, "#999999");
                		items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#EE0000");
                	   	copyArrayToGAIGS(a,7, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	//assignment statement
                        a[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(a[x +oper10b.conversion()][y + oper11b.conversion()],a[x +oper10c.conversion()][y + oper11c.conversion()] );
                        nextResultCount++;
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,7,null,i,j);//highlights assignment statement
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#999999");
                    }//inner else
               }//outer else
                items.setColor(x, y, "#999999");
                copyArrayToGAIGS(a,8, null,i,j);//highlighting closing bracket for inner for loop
                if (askOnceQuest.isEmpty()){
                	copyArrayToGAIGS(a,2, null,i,oper4.compute(j,forLoop2.getiterationCount()));
            	}else{
                	listIndex = randInt.nextInt(askOnceQuest.size());            	
                	copyArrayToGAIGS(a,2, askOnceQuest.get(listIndex),i,oper4.compute(j,forLoop2.getiterationCount()));
                	askOnceQuest.remove(listIndex);
            	}
            }// inner for
            copyArrayToGAIGS(a,9, questClass.generateArrayElementQuestion(),i,-1);//highlights closing bracket of the outer for loop
            copyArrayToGAIGS(a,0, null,oper2.compute(i,forLoop1.getiterationCount()),-1);
        }// outer for
        copyArrayToGAIGS(a,10, null,-1,-1);
    }// template2 Execute method
       
    public void template3Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test()+ "</pre>");
    	pseudo.add("<pre>2    {</pre>");
    	pseudo.add("<pre>3        " + ifState.testWithoutArrayNameFirst(forLoop1.getVariableName())+ "</pre>");
    	pseudo.add("<pre>4        {</pre>");
    	pseudo.add("<pre>5            " +forLoop2.test()+ "</pre>");
    	pseudo.add("<pre>6                "+ state.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>7        }</pre>");
    	pseudo.add("<pre>8        " + "else</pre>" );
    	pseudo.add("<pre>9        {</pre>");
    	pseudo.add("<pre>10           "+ forLoop3.test()+ "</pre>");
    	pseudo.add("<pre>11               "+ state2.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>12       }</pre>");
    	pseudo.add("<pre>13   }</pre>");
    	pseudo.add("<pre>14   // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");  
    }// template3 Test method
    
    public void template3Execute() throws IOException

    {
    	/* Filling result array to produce question answers */
    	int innerForLoopCount = 0;
    	int innerForLoopCount2 = 0;
    	int outerForLoopCount = 0;

    	for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
    		outerForLoopCount++;
 /*IF*/     if( oper5.testing(oper6.compute(i, ifState.getConstant()),ifState.getTermNum()))
            {
                for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount())){
                	 if(state.getindexName1() == forLoop1.getVariableName())
 				 	{
 				 		 x = i;
 				 		 y = j;
 				 	}
 				 	else
 				 	{
 				 		x = j;
 				 		y = i;
 				 	}
                	innerForLoopCount++;
                     if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                     {
                        result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber()); 
                        nextResult.add(oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber()));
                     }
                     else//using an array spot to perform the math operation
                     {
                        result[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], result[x +oper7c.conversion()][y + oper8c.conversion()] ); 
                        nextResult.add(oper9.compute(result[x +oper7b.conversion()][y + oper8b.conversion()], result[x +oper7c.conversion()][y + oper8c.conversion()] ));
                     }
                }//inner for loop
 /*ELSE*/   }else{
                for(int j = forLoop3.getinitialNum(); oper13.testing(j,forLoop3.getTerminalNum()); j = oper14.compute(j,forLoop3.getiterationCount())){
                	 if(state2.getindexName1() == forLoop1.getVariableName())
 				 	 {
 				 		 x = i;
 				 		 y = j;
 				 	 }
 				 	 else
 				 	 {
 				 		x = j;
 				 		y = i;
 				 	 }
                	innerForLoopCount2++;
                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {	
                        result[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber());
                        nextResult.add(oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber()));
                   }//outer if
                    else//using an array spot to perform the math operation
                    {
                        result[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()],result[x +oper10c.conversion()][y + oper11c.conversion()] );
                        nextResult.add(oper12.compute(result[x +oper10b.conversion()][y + oper11b.conversion()],result[x +oper10c.conversion()][y + oper11c.conversion()] ));
                    } 
                }
            }// else
        }// outer for
    	
    	checkElementSize();
    	
    	forLoop1.setCount(outerForLoopCount);
    	forLoop2.setCount(innerForLoopCount);
    	forLoop3.setCount(innerForLoopCount2);

        
    	/* Start Generating Snapshots */
    	
    	askOnceQuest = questClass.generateAskOnceQuestions(true);
    	int listIndex;
    	
    	copyArrayToGAIGS(a,-1, null,-1,-1);//prints the code. No line is highlighted
    	copyArrayToGAIGS(a,0, null,forLoop1.getinitialNum(),-1);//prints the code with line 1 highlighted
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	copyArrayToGAIGS(a,1, null,i,-1);//highlights line 2 
        	if(experiencedUser)
        		copyArrayToGAIGS(a,2, questClass.generateArrayQuestion(i),i,-1); //highlights line 3
        	else
        		copyArrayToGAIGS(a,2, null,i,-1); //highlights line 3
 /*IF*/     if( oper5.testing(oper6.compute(i, ifState.getConstant()),ifState.getTermNum()))
            {
	 			copyArrayToGAIGS(a,3, null,i,-1);
            	copyArrayToGAIGS(a,4, questClass.generateArrayElementQuestion(),i,forLoop2.getinitialNum()); //highlights line 5
                for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount())){
                	 if(state.getindexName1() == forLoop1.getVariableName())
 				 	 {
 				 		 x = i;
 				 		 y = j;
 				 	 }
 				 	 else
 				 	 {
 				 		x = j;
 				 		y = i;
 				 	 }
                	if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                     {
                   		items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#EE0000");
                   	    copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	//assignment statement
                        a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()], state.getnumber());   
                        nextResultCount++;
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
                        copyArrayToGAIGS(a,4, null,i,oper4.compute(j,forLoop2.getiterationCount()));
                     }
                     else//using an array spot to perform the math operation
                     {
                   		items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#EE0000");
                   		copyArrayToGAIGS(a,5, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	//assignment statement
                        a[x +oper7.conversion()][y + oper8.conversion()] = oper9.compute(a[x +oper7b.conversion()][y + oper8b.conversion()], a[x +oper7c.conversion()][y + oper8c.conversion()] ); 
                        nextResultCount++;
                        items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                        if (askOnceQuest.isEmpty()){
                        	items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
                        	copyArrayToGAIGS(a,4, null,i,oper4.compute(j,forLoop2.getiterationCount()));
                    	}else{
                        	listIndex = randInt.nextInt(askOnceQuest.size());   
                        	items.setColor(x +oper7.conversion(),y + oper8.conversion(), "#999999");
                        	copyArrayToGAIGS(a,4, askOnceQuest.get(listIndex),i,oper4.compute(j,forLoop2.getiterationCount()));
                        	askOnceQuest.remove(listIndex);
                    	}
                     }
                      flag = true;
              }//inner for loop
 /*ELSE*/   }else{
            	copyArrayToGAIGS(a,7, null,i,-1);//highlights the else
            	copyArrayToGAIGS(a,8, null,i,-1);
            	copyArrayToGAIGS(a,9, null, i,forLoop3.getinitialNum()); //highlights the opening bracket
                for(int j = forLoop3.getinitialNum(); oper13.testing(j,forLoop3.getTerminalNum()); j = oper14.compute(j,forLoop3.getiterationCount())){
                	 if(state2.getindexName1() == forLoop1.getVariableName())
 				 	{
 				 		 x = i;
 				 		 y = j;
 				 	}
 				 	else
 				 	{
 				 		x = j;
 				 		y = i;
 				 	}
                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {										
                  		items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#EE0000");
                  		copyArrayToGAIGS(a,10, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
 
                		//assignment statement
                        a[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(a[x +oper10b.conversion()][y + oper11b.conversion()], state2.getnumber());
                        nextResultCount++;
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,10,null,i,j);//highlights assignment statement
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#999999");
                        copyArrayToGAIGS(a,9, null,i,oper14.compute(j,forLoop3.getiterationCount()));
                   }//outer if
                    else//using an array spot to perform the math operation
                    {
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#EE0000");
                      	copyArrayToGAIGS(a,10, questClass.nextResultQuestion(nextResultCount, i, j),i,j);//highlights assignment statement
                    	//assignment statement 
                        a[x +oper10.conversion()][y + oper11.conversion()] = oper12.compute(a[x +oper10b.conversion()][y + oper11b.conversion()],a[x +oper10c.conversion()][y + oper11c.conversion()] );
                        nextResultCount++;
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,10, null,i,j);//highlights assignment statement
                        items.setColor(x +oper10.conversion(),y + oper11.conversion(), "#999999"); 
                        if (askOnceQuest.isEmpty()){
                        	copyArrayToGAIGS(a,9, null,i,oper14.compute(j,forLoop3.getiterationCount()));
                    	}else{
                    		listIndex = randInt.nextInt(askOnceQuest.size());  
                    		copyArrayToGAIGS(a,9, askOnceQuest.get(listIndex),i,oper14.compute(j,forLoop3.getiterationCount()));
                        	askOnceQuest.remove(listIndex);
                    	}
                        flag2 = false;
                    }
                 if (flag)
                 {
                	 copyArrayToGAIGS(a,6, null,i,j); //highlights the closing bracket of the if
                	 flag = false;
                 }
                 if(flag2)
                 {
                	 copyArrayToGAIGS(a,11, null,i,j);//highlights the closing bracket for else
                	 flag2 = false;
                 }
                 
               }// inner for
            }// else
            copyArrayToGAIGS(a, 12, questClass.generateArrayElementQuestion(),i,-1);//highlights the closing bracket for the outer for loop
            copyArrayToGAIGS(a,0, null,oper2.compute(i,forLoop1.getiterationCount()),-1);
        }// outer for
        copyArrayToGAIGS(a,13, null,-1,-1);
    }// template3Execute method
    
    public void checkElementSize() throws ElementTooBigException
    {
    	for(int i=0; i < rowSize; i++){
    		for(int j=0; j < colSize; j++){
    			if(result[i][j] > 999)
    				throw new ElementTooBigException();
    		}
    	}
    }
    
    public class NestedForLoopQuestions
    {
    	private int id = 0;
    	private Random random;
    	
    	public NestedForLoopQuestions()
    	{
    		random = new Random();
    	}
    	
    	public String arrayToString(int[][] array, int x)
    	{
    		String returnString = "";
    		
    		for(int i = x; i == x; x++){
    			for(int j = 0; j < colSize; j++)
    				returnString += " " + array[i][j];
    		}
    		
    		return returnString;
    	}
    	
    	public String arrayToStringColumn(int[][] array, int x)
    	{
    		String returnString = "";
    		
    		for(int i = 0; i < forLoop1.getSize(); i++){
    			for(int j = x; j == x; j++)
    				returnString += " " + array[i][j];
    		}
    		
    		return returnString;
    	}
    	
    	public ArrayList<question> generateAskOnceQuestions(boolean template3)
    	{
    		ArrayList<question> q = new ArrayList<question>();
    		
    		XMLfibQuestion question1;
            XMLtfQuestion question2;
            XMLmcQuestion question3;
    		
            int questionNum = random.nextInt(7);
            
            switch (questionNum) {
            	case 0:
            		question1 = new XMLfibQuestion(show, id + "");
		        	question1.setQuestionText("How many elements does the array " + ifState.getArrayName() + "[ ][ ] contain?");
		        	question1.setAnswer(a.length * a[randInt.nextInt(forLoop1.getSize())].length + "");
		        	q.add(question1);break;
            	case 1:
            		question1 = new XMLfibQuestion(show, id + "");
            		question1.setQuestionText("What is the value of " + ifState.getArrayName() + ".length * " 
                			+ ifState.getArrayName() +"[" + randInt.nextInt(forLoop1.getSize()) + "].length?");
                	question1.setAnswer(a.length * a[0].length + "");
                	q.add(question1);break;
            	case 2:
            		question2 = new XMLtfQuestion(show, id + "");
            		question2.setQuestionText("The array " + ifState.getArrayName() + "[ ][ ] contains " 
        					+ a.length * a[0].length + " elements.");
        			question2.setAnswer(true);
        			q.add(question2);break;
            	case 3:
            		question3 = new XMLmcQuestion(show, id + "");
            		question3.setQuestionText("How many elements does the array " + ifState.getArrayName() + "[ ][ ] contain?");
                	question3.addChoice(a.length * a[randInt.nextInt(forLoop1.getSize())].length + "");
                	question3.addChoice(a.length * a[randInt.nextInt(forLoop1.getSize())].length - (forLoop2.getSize()) + "");
                	question3.addChoice(a.length * a[randInt.nextInt(forLoop1.getSize())].length + (forLoop2.getSize()) + "");
                	question3.addChoice(a.length * a[randInt.nextInt(forLoop1.getSize())].length + (forLoop1.getSize()) + "");
                	question3.addChoice(a.length * a[randInt.nextInt(forLoop1.getSize())].length - (forLoop1.getSize()) + "");
                	question3.setAnswer(1);
                	q.add(question3);break;
            	case 4:
		    		question2 = new XMLtfQuestion(show, id + "");
					question2.setQuestionText("The array " + ifState.getArrayName() + "[ ][ ] contains " 
							+ (a.length * a[0].length -(forLoop2.getSize())) + " elements");
					question2.setAnswer(false);
					q.add(question2);break;
            	case 5:
            		question2 = new XMLtfQuestion(show, id + "");
            		question2.setQuestionText("The array " + ifState.getArrayName() + "[ ][ ] contains " 
        					+ a.length * a[0].length + " elements.");
        			question2.setAnswer(true);
        			q.add(question2);break;
		    	case 6:
		    		question2 = new XMLtfQuestion(show, id + "");
					question2.setQuestionText("The array " + ifState.getArrayName() + "[ ][ ] contains " 
							+ (a.length * a[0].length + (forLoop2.getSize())) + " elements");
					question2.setAnswer(false);
					q.add(question2);break;
            }
		    
		    id++;
		    questionNum = random.nextInt(3);
		    
		    switch (questionNum){
		    	case 0:
		    		question1 = new XMLfibQuestion(show, id + "");
		        	question1.setQuestionText("What is the value of " + ifState.getArrayName() + ".length?");
		        	question1.setAnswer(a.length + "");
		            q.add(question1);break;
		    	case 1:
		    		question2 = new XMLtfQuestion(show, id + "");
		            question2.setQuestionText("The value of " + ifState.getArrayName() + ".length is " + a.length + ".");
		        	question2.setAnswer(true);
		        	q.add(question2);break;
		    	case 2:
		    		question2 = new XMLtfQuestion(show, id + "");
		            question2.setQuestionText("The value of " + ifState.getArrayName() + ".length is " + a[0].length + ".");
		        	if(a.length == a[0].length)
		            	question2.setAnswer(true);
		        	else
		        		question2.setAnswer(false);
		        	q.add(question2);break;
		    }

        	id++;
		    questionNum = random.nextInt(3);
				    
		    switch (questionNum){
		    	case 0:
		    		question1 = new XMLfibQuestion(show, id + "");
		        	question1.setQuestionText("What is the value of " + ifState.getArrayName() + "[" + randInt.nextInt(forLoop1.getSize()) + "].length?");
		        	question1.setAnswer(a[0].length + "");
		            q.add(question1);break;
		    	case 1:
		    		question2 = new XMLtfQuestion(show, id + "");
		            question2.setQuestionText("The value of " + ifState.getArrayName() + "[" + randInt.nextInt(forLoop1.getSize()) + "].length is " + a[0].length + ".");
		        	question2.setAnswer(true);
		        	q.add(question2);break;
		    	case 2:
		    		question2 = new XMLtfQuestion(show, id + "");
		            question2.setQuestionText("The value of " + ifState.getArrayName() + "[" + randInt.nextInt(forLoop1.getSize()) + "].length is " + a.length + ".");
		            if(a.length == a[0].length)
		            	question2.setAnswer(true);
		        	else
		        		question2.setAnswer(false);
		        	q.add(question2);break;
		    }
        	
		    id++;
		    questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
	            case 0: 
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the array?");
	            	question1.setAnswer(ifState.getArrayName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the array is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the array is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the array is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 4:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the array is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 5:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the array?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(1);
	            	q.add(question3);break;
		    }
		    
		    id++;
		    questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the outer For loop's counter variable?");
	            	question1.setAnswer(forLoop1.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's counter variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 4:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 5:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the outer For loop's counter variable?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(2);
	            	q.add(question3);break;
		    }
        	
		    id++;
		    questionNum = random.nextInt(2);
		    
		    switch (questionNum) {		    
	            case 0:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The outer For loop's counter variable is incremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("+"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The outer For loop's counter variable is decremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("-"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
		    }
		    if(template3){
		    	id++;
			    questionNum = random.nextInt(2);
			    
			    switch (questionNum) {		    
		            case 0:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The first inner For loop's counter variable is incremented during the execution of the loop.");
		            	if(forLoop2.getdir().equals("+"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
		            case 1:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The first inner For loop's counter variable is decremented during the execution of the loop.");
		            	if(forLoop2.getdir().equals("-"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
			    }
			    id++;
			    questionNum = random.nextInt(2);
			    
			    switch (questionNum) {		    
		            case 0:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The second inner For loop's counter variable is incremented during the execution of the loop.");
		            	if(forLoop3.getdir().equals("+"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
		            case 1:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The second inner For loop's counter variable is decremented during the execution of the loop.");
		            	if(forLoop3.getdir().equals("-"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
			    }
		    }else{
		    	id++;
			    questionNum = random.nextInt(2);
			    
			    switch (questionNum) {		    
		            case 0:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The inner For loop's counter variable is incremented during the execution of the loop.");
		            	if(forLoop2.getdir().equals("+"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
		            case 1:
		            	question2 = new XMLtfQuestion(show, id + "");
		            	question2.setQuestionText("The inner For loop's counter variable is decremented during the execution of the loop.");
		            	if(forLoop2.getdir().equals("-"))
		            		question2.setAnswer(true);
		            	else
		            		question2.setAnswer(false);
		            	q.add(question2);break;
			    }
		    }
        	
		    id++;
		    questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the inner For loop's counter variable?");
	            	question1.setAnswer(forLoop2.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's counter variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 4:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 5:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the inner For loop's counter variable?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(3);
	            	q.add(question3);break;
		    }
		    id++;
    		questionNum = random.nextInt(5);
		    
		    switch (questionNum) {
			case 0:
	        	question1 = new XMLfibQuestion( show, id + "" );
	    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the outer for loop executed?" );
	    		question1.setAnswer( "" + forLoop1.getCount() );
	    		q.add(question1);break;
			case 1:
	    		question2 = new XMLtfQuestion( show, id + "" );
	    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the outer for loop is executed " + forLoop1.getCount() + " time(s)." );
	    		question2.setAnswer( true );
	    		q.add(question2);break;
			case 2:
	    		question2 = new XMLtfQuestion( show, id + "" );
	    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the outer for loop is executed " + (forLoop1.getCount()+1) + " time(s)." );
	    		question2.setAnswer( false );
	    		q.add(question2);break;
			case 3:
	    		question2 = new XMLtfQuestion( show, id + "" );
	    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the outer for loop is executed " + forLoop1.getCount() + " time(s)." );
	    		question2.setAnswer( true );
	    		q.add(question2);break;
			case 4:
				question2 = new XMLtfQuestion( show, id + "" );
	    		if(forLoop1.getCount() != 0)
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the outer for loop is executed " + (forLoop1.getCount()-1) + " time(s)." );
		    	else
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the outer for loop is executed " + (forLoop1.getCount()+2) + " time(s)." );
	    		question2.setAnswer( false );
	    		q.add(question2);break;
			}
			id++;
			questionNum = random.nextInt(5);
			
			if(template3){
				switch (questionNum) {
	    			case 0:
			        	question1 = new XMLfibQuestion( show, id + "" );
			    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the first inner for loop executed?" );
			    		question1.setAnswer( "" + forLoop2.getCount() );
			    		q.add(question1);break;
	    			case 1:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the first inner for loop is executed " + forLoop2.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 2:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the first inner for loop is executed " + (forLoop2.getCount()+1) + " time(s)." );
			    		question2.setAnswer( false );
			    		q.add(question2);break;
	    			case 3:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the first inner for loop is executed " + forLoop2.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 4:
	    				question2 = new XMLtfQuestion( show, id + "" );
			    		if(forLoop2.getCount() != 0)
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the first inner for loop is executed " + (forLoop2.getCount()-1) + " time(s)." );
				    	else
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the first inner for loop is executed " + (forLoop2.getCount()+2) + " time(s)." );
			    		question2.setAnswer( false );
			    		q.add(question2);break;
				}
				id++;
	    		questionNum = random.nextInt(5);
	    		
				switch (questionNum) {
	    			case 0:
			        	question1 = new XMLfibQuestion( show, id + "" );
			    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the second inner for loop executed?" );
			    		question1.setAnswer( "" + forLoop3.getCount() );
			    		q.add(question1);break;
	    			case 1:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the second inner for loop is executed " + forLoop3.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 2:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the second inner for loop is executed " + (forLoop3.getCount()+1) + " time(s)." );
			    		question2.setAnswer( false );
			    		q.add(question2);break;
	    			case 3:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the second inner for loop is executed " + forLoop3.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 4:
	    				question2 = new XMLtfQuestion( show, id + "" );
			    		if(forLoop3.getCount() != 0)
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the second inner for loop is executed " + (forLoop3.getCount()-1) + " time(s)." );
				    	else
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the second inner for loop is executed " + (forLoop3.getCount()+2) + " time(s)." );
			    		question2.setAnswer( false );
			    		q.add(question2);break;
				}
			}else{
				switch (questionNum) {
	    			case 0:
			        	question1 = new XMLfibQuestion( show, id + "" );
			    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the inner for loop executed?" );
			    		question1.setAnswer( "" + forLoop2.getCount() );
			    		q.add(question1);break;
	    			case 1:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the inner for loop is executed " + forLoop2.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 2:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the inner for loop is executed " + (forLoop2.getCount()+1) + " time(s)." );
			    		question2.setAnswer( false );
			    		q.add(question2);break;
	    			case 3:
			    		question2 = new XMLtfQuestion( show, id + "" );
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the inner for loop is executed " + forLoop2.getCount() + " time(s)." );
			    		question2.setAnswer( true );
			    		q.add(question2);break;
	    			case 4:
	    				question2 = new XMLtfQuestion( show, id + "" );
			    		if(forLoop2.getCount() != 0)
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the inner for loop is executed " + (forLoop2.getCount()-1) + " time(s)." );
				    	else
				    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the inner for loop is executed " + (forLoop2.getCount()+2) + " time(s)." );
				    		question2.setAnswer( false );
				    		q.add(question2);break;
				}// switch
			}// else

			return q;
				
    	}// ask once questions
    	
    	public question generateArrayElementQuestion()
    	{
    		int questionNumber = 0;
    		if(experiencedUser)
    			questionNumber = random.nextInt(20);
    		else
    			questionNumber = random.nextInt(10);
    		
    		int randRow = random.nextInt(forLoop1.getSize()-1);
        	int randCol = random.nextInt(forLoop2.getSize()-1);
        	
        	while (randRow == randCol){
        		randCol = random.nextInt(forLoop2.getSize()-1);
        	}
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLmcQuestion question2 = new XMLmcQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
        	
        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What is the current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "]?");
	            	question1.setAnswer(a[randRow][randCol] + "");
	            	return question1;
	            case 1:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 2:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow+1][randCol]);
	            	if(a[randRow+1][randCol] == a[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 3:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 4:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow][randCol+1]);
	            	if(a[randRow][randCol+1] == a[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 5:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 6:
	            	question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow+1][randCol+1]);
	            	if(a[randRow+1][randCol+1] == a[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 7:
	            	question2.setQuestionText("What is the current value of " + ifState.getArrayName() + "[" + randRow + "][" + randCol + "]?");
	            	question2.addChoice(a[randRow][randCol] + "");
	            	if(a[randRow][randCol] != a[randRow+1][randCol+1])
	            		question2.addChoice(a[randRow+1][randCol+1] + "");
	            	if(a[randRow][randCol] != a[randRow][randCol+1] && a[randRow][randCol+1] != a[randRow+1][randCol+1])
	            		question2.addChoice(a[randRow][randCol+1] + "");
	            	if(a[randRow][randCol] != a[randRow+1][randCol] && a[randRow+1][randCol] != a[randRow+1][randCol+1])
	            		if(a[randRow+1][randCol] != a[randRow][randCol+1])
	            			question2.addChoice(a[randRow+1][randCol] + "");
		            if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize())
		            	if(a[randRow][randCol] != a[randCol][randRow] && a[randRow+1][randCol] != a[randCol][randRow])
		            		if(a[randRow][randCol+1] != a[randCol][randRow] && a[randRow+1][randCol+1] != a[randCol][randRow])
		            			question2.addChoice(a[randCol][randRow] + "");
		            question2.addChoice("None of these values");
	            	question2.setAnswer(1);
	            	return question2;
	            case 8:
	            	if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize()){
	            		question3.setQuestionText("The current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] is " + a[randCol][randRow]);
	            		if(a[randCol][randRow] == a[randRow][randCol])
	            			question3.setAnswer(true);
	            		else
	            			question3.setAnswer(false);
	            	}else{
	            		question3.setQuestionText("The current value of " + ifState.getArrayName() 
		            			+ "[" + randRow + "][" + randCol + "] is " + a[randRow][randCol]);
		            	question3.setAnswer(true); 
		            	return question3;
	            	}
	            	return question3;
	            case 9:
	            	question2.setQuestionText("What is the current value of " + ifState.getArrayName() + "[" + randRow + "][" + randCol + "]?");
	            	question2.addChoice("None of these values");
	            	if(a[randRow][randCol] != a[randRow+1][randCol+1])
	            		question2.addChoice(a[randRow+1][randCol+1] + "");
	            	if(a[randRow][randCol] != a[randRow][randCol+1] && a[randRow][randCol+1] != a[randRow+1][randCol+1])
	            		question2.addChoice(a[randRow][randCol+1] + "");
	            	if(a[randRow][randCol] != a[randRow+1][randCol] && a[randRow+1][randCol] != a[randRow+1][randCol+1])
	            		if(a[randRow+1][randCol] != a[randRow][randCol+1])
	            			question2.addChoice(a[randRow+1][randCol] + "");
		            if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize())
		            	if(a[randRow][randCol] != a[randCol][randRow] && a[randRow+1][randCol] != a[randCol][randRow])
		            		if(a[randRow][randCol+1] != a[randCol][randRow] && a[randRow+1][randCol+1] != a[randCol][randRow])
		            			question2.addChoice(a[randCol][randRow] + "");
	            	question2.setAnswer(1);
	            	return question2;
        		case 10: 
	            	question1.setQuestionText("At the end of the execution of this program, what will be the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "]?");
	            	question1.setAnswer(result[randRow][randCol] + "");
	            	return question1;
	            case 11:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 12:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow+1][randCol]);
	            	if(result[randRow+1][randCol] == result[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 13:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 14:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow][randCol+1]);
	            	if(result[randRow][randCol+1] == result[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 15:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow][randCol]);
	            	question3.setAnswer(true); 
	            	return question3;
	            case 16:
	            	question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow+1][randCol+1]);
	            	if(result[randRow+1][randCol+1] == result[randRow][randCol])
	            		question3.setAnswer(true);
	            	else
	            		question3.setAnswer(false);
	            	return question3;
	            case 17:
	            	question2.setQuestionText("At the end of the execution of this program, what will be the value of " + ifState.getArrayName() + "[" + randRow + "][" + randCol + "]?");
	            	question2.addChoice(result[randRow][randCol] + "");
	            	if(result[randRow][randCol] != result[randRow+1][randCol+1])
	            		question2.addChoice(result[randRow+1][randCol+1] + "");
	            	if(result[randRow][randCol] != result[randRow][randCol+1] && result[randRow][randCol+1] != result[randRow+1][randCol+1])
	            		question2.addChoice(result[randRow][randCol+1] + "");
	            	if(result[randRow][randCol] != result[randRow+1][randCol] && result[randRow+1][randCol] != result[randRow+1][randCol+1])
	            		if(result[randRow+1][randCol] != result[randRow][randCol+1])
	            			question2.addChoice(result[randRow+1][randCol] + "");
		            if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize())
		            	if(result[randRow][randCol] != result[randCol][randRow] && result[randRow+1][randCol] != result[randCol][randRow])
		            		if(result[randRow][randCol+1] != result[randCol][randRow] && result[randRow+1][randCol+1] != result[randCol][randRow])
		            			question2.addChoice(result[randCol][randRow] + "");
		            question2.addChoice("None of these values");
	            	question2.setAnswer(1);
	            	return question2;
	            case 18:
	            	if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize()){
	            		question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "] will be " + result[randCol][randRow] + ".");
	            		if(result[randCol][randRow] == result[randRow][randCol])
	            			question3.setAnswer(true);
	            		else
	            			question3.setAnswer(false);
	            	}else{
	            		question3.setQuestionText("At the end of the execution of this program, the value of " + ifState.getArrayName() 
		            			+ "[" + randRow + "][" + randCol + "] will be " + result[randRow][randCol] + ".");
		            	question3.setAnswer(true); 
		            	return question3;
	            	}
	            	return question3;
	            case 19:
	            	question2.setQuestionText("At the end of the execution of this program, what will be the value of " + ifState.getArrayName() + "[" + randRow + "][" + randCol + "]?");
	            	question2.addChoice("None of these values");
	            	if(result[randRow][randCol] != result[randRow+1][randCol+1])
	            		question2.addChoice(result[randRow+1][randCol+1] + "");
	            	if(result[randRow][randCol] != result[randRow][randCol+1] && result[randRow][randCol+1] != result[randRow+1][randCol+1])
	            		question2.addChoice(result[randRow][randCol+1] + "");
	            	if(result[randRow][randCol] != result[randRow+1][randCol] && result[randRow+1][randCol] != result[randRow+1][randCol+1])
	            		if(result[randRow+1][randCol] != result[randRow][randCol+1])
	            			question2.addChoice(result[randRow+1][randCol] + "");
		            if(randCol < forLoop1.getSize() && randRow < forLoop2.getSize())
		            	if(result[randRow][randCol] != result[randCol][randRow] && result[randRow+1][randCol] != result[randCol][randRow])
		            		if(result[randRow][randCol+1] != result[randCol][randRow] && result[randRow+1][randCol+1] != result[randCol][randRow])
		            			question2.addChoice(result[randCol][randRow] + "");
	            	question2.setAnswer(1);
	            	return question2;
	            default: 
	            	question1.setQuestionText("What is the current value of " + ifState.getArrayName() + "[" + randRow + "][" + randCol + "]?");
	            	question1.setAnswer(a[randRow][randCol] + "");
	            	return question1;
        	}// switch
    	}// generateArrayElementQuestions
    	
    	public question generateArrayQuestion(int current)
    	{
    		int questionNumber = random.nextInt(10);
    		
    		int randRow = random.nextInt(forLoop1.getSize());
        	int randCol = random.nextInt(colSize);
    		int nextRow = 0;
        	
    		// determines the value of nextRow based on the direction, iteration count, and current row.

    		if(forLoop1.getdir() == "+"){
    			if(current < forLoop1.getSize()-forLoop1.getiterationCount()){
        			nextRow = current+forLoop1.getiterationCount();
    			}else
            		questionNumber = -1;
        	}else{
        		if(current > forLoop1.getiterationCount()){
        			nextRow = current-forLoop1.getiterationCount();
        		}else
            		questionNumber = -1;
        	}
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
        	
        	switch (questionNumber) {
	            case 0:
	    			question1 = new XMLfibQuestion( show, id + "" );
	    			question1.setQuestionText("At the end of the execution of this program, what will be the values for the elements in row " + nextRow + "? Format: 1 2 3 4...");
	    			question1.setAnswer(arrayToString(result, nextRow));
	    			return question1;
	    		case 1:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
		        	question3.setAnswer(true);
		        	return question3;
	    		case 2:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			if(nextRow != 0){
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow-1) + ".");
	    				if(arrayToString(result, nextRow-1).equals(arrayToString(result, nextRow)))
	    					question3.setAnswer(true);
		        		else
		        			question3.setAnswer(false);
	    			}else{
		        		question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
	        			question3.setAnswer(true);
	    			}
		        	return question3;
	    		case 3:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
		        	question3.setAnswer(true);
		        	return question3;
	    		case 4:
	    			question3 = new XMLtfQuestion( show, id + "");
    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(a, nextRow) + ".");
	        		if(arrayToString(a, nextRow).equals(arrayToString(result, nextRow)))
    					question3.setAnswer(true);
	        		else
	        			question3.setAnswer(false);
		        	return question3;
	    		case 5:
	    			question1 = new XMLfibQuestion( show, id + "" );
	    			if(nextRow < colSize){
		    			question1.setQuestionText("At the end of the execution of this program, what will be the values for the elements in column " + nextRow + "? Format: 1 2 3 4...");
		    			question1.setAnswer(arrayToStringColumn(result, nextRow));
	    			}else{
		    			question1.setQuestionText("At the end of the execution of this program, what will be the values for the elements in row " + nextRow + "? Format: 1 2 3 4...");
		    			question1.setAnswer(arrayToString(result, nextRow));
	    			}
	    			return question1;
	    		case 6:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			if(nextRow < colSize){
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in column " + nextRow + " will be:" + arrayToStringColumn(result, nextRow) + ".");
			        	question3.setAnswer(true);
	    			}else{
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
			        	question3.setAnswer(true);
	    			}
		        	return question3;
	    		case 7:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			if(nextRow != 0){
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in column " + nextRow + " will be:" + arrayToStringColumn(result, nextRow-1) + ".");
	    				if(arrayToStringColumn(result, nextRow-1).equals(arrayToStringColumn(result, nextRow)))
	    					question3.setAnswer(true);
		        		else
		        			question3.setAnswer(false);
	    			}else{
		        		question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
	        			question3.setAnswer(true);
	    			}
		        	return question3;
	    		case 8:
	    			question3 = new XMLtfQuestion( show, id + "");
	    			if(nextRow < colSize){
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in column " + nextRow + " will be:" + arrayToStringColumn(result, nextRow) + ".");
			        	question3.setAnswer(true);
	    			}else{
	    				question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
			        	question3.setAnswer(true);
	    			}
		        	return question3;
	    		case 9:
	    			question3 = new XMLtfQuestion( show, id + "");
    				if(nextRow < colSize){
		    			question3.setQuestionText("At the end of the execution of this program, the values for the elements in column " + nextRow + " will be:" + arrayToStringColumn(a, nextRow) + ".");
		        		if(arrayToStringColumn(a, nextRow).equals(arrayToStringColumn(result, nextRow)))
	    					question3.setAnswer(true);
		        		else
		        			question3.setAnswer(false);
    				}else{
    					question3.setQuestionText("At the end of the execution of this program, the values for the elements in row " + nextRow + " will be:" + arrayToString(result, nextRow) + ".");
	        			question3.setAnswer(true);
    				}
		        	return question3;
		        default:
		        	question1.setQuestionText("What is the current value of " + ifState.getArrayName() 
	            			+ "[" + randRow + "][" + randCol + "]?");
	            	question1.setAnswer(a[randRow][randCol] + "");
	            	return question1;
        	}// switch
    	}// generateArrayQuestions
    	
    	public question nextResultQuestion(int count, int randRow, int randCol)
    	{
    		int questionNumber = random.nextInt(3);
    		int tfQuestion = random.nextInt(3);
    		
    		id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
    		
    		switch(questionNumber)
    		{
    			case 0:
		    		question1.setQuestionText("What will the highlighted element be changed to?");
		    		question1.setAnswer(nextResult.get(count) + "");
		    		return question1;
		    	case 1:
		    		question3.setQuestionText("The highlighted element will be changed to " + nextResult.get(count));
		    		question3.setAnswer(true);
		    	case 2:
		    		switch(tfQuestion)
		    		{
		    			case 0:
		    				if(randRow != 0)
			    				question3.setQuestionText("The highlighted element will be changed to " + result[randRow-1][randCol]);
					    		if(result[randRow-1][randCol] == nextResult.get(count))
					    			question3.setAnswer(true);
					    		else
					    			question3.setAnswer(false);
					    	return question3;
		    			case 1:
		    				if(randCol != 0)
			    				question3.setQuestionText("The highlighted element will be changed to " + result[randRow][randCol-1]);
					    		if(result[randRow][randCol-1] == nextResult.get(count))
					    			question3.setAnswer(true);
					    		else
					    			question3.setAnswer(false);
					    	return question3;
		    			case 2:
		    				if(randCol != 0 && randRow != 0)
			    				question3.setQuestionText("The highlighted element will be changed to " + result[randRow-1][randCol-1]);
					    		if(result[randRow-1][randCol-1] == nextResult.get(count))
					    			question3.setAnswer(true);
					    		else
					    			question3.setAnswer(false);
					    	return question3;
		    			default:
		    				question1.setQuestionText("What will the highlighted element be changed to?");
				    		question1.setAnswer(nextResult.get(count) + "");
				    		return question1;
		    		}
		    	default:
		    		question1.setQuestionText("What will the highlighted element be changed to?");
		    		question1.setAnswer(nextResult.get(count) + "");
		    		return question1;
    		}
    	}
    }//Inner Class - Questions
}//NestedForLoopsWithArrays

