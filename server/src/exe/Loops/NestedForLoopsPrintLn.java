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

import exe.*;

public class NestedForLoopsPrintLn {
   private ForLoop forLoop1, forLoop3;
   private ForLoop forLoop2;
   private IfStatement ifState;
   private char randChar;
   private PseudoCode pseudoCode;
   //private NestedForLoopQuestions questClass;
   
   private Operations oper, oper2, oper3, oper4, oper5, oper6, oper7, oper8;
   private RandomizedVariables random;
   private String varName;
   private static Random randInt;
   private String printString = "";
   
   ArrayList<String> pseudo = new ArrayList<String>();
   static  String title = null;	// no title
   static int rowSize=0;
   static int colSize=0;
   static GAIGStext text;
   static GAIGStext line;
   private static ShowFile show;
   
   public static void main (String [] args) throws IOException {
	   NestedForLoopsPrintLn nfl = new NestedForLoopsPrintLn();
	   
      	show = new ShowFile(args[0]);
		// define the two structures in the show snapshots
		text = new GAIGStext(0.5, 0.9, "Program Output");
		text.setFontsize(0.055);
		
		line = new GAIGStext(0.5, 0.4, "");
		line.setFontsize(0.055);
		
		nfl.template1Test();
		nfl.template1Execute();
		
		/*int example = randInt.nextInt(3);
		switch (example) {
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
		}*/
	    
	    show.close();
   }
   
    /**
     * Constructor for NestedForLoopsPrintLn class
     */
    public NestedForLoopsPrintLn()
    {
        random = new RandomizedVariables();
        randInt = new Random();
        
        pseudoCode = new PseudoCode();
        //questClass = new NestedForLoopQuestions();
                    
        forLoop1 = new ForLoop(5,8);
        forLoop2 = new ForLoop(5,8);
        forLoop3 = new ForLoop(5,8);
        
        while(forLoop1.getVariableName().equals(forLoop2.getVariableName())){
            forLoop2.setNewVariableName();
        }
        forLoop3.setVariableName(forLoop2.getVariableName());
        
        varName = random.randVarName(forLoop1.getVariableName(), forLoop2.getVariableName());
        
        ifState = new IfStatement();
        randChar = random.randChar();
        
        rowSize = forLoop1.getSize();
        colSize = forLoop2.getSize();

        oper = new Operations(forLoop1.getrelOp());
        oper2 = new Operations(forLoop1.getdir());
        oper3 = new Operations(forLoop2.getrelOp());
        oper4 = new Operations(forLoop2.getdir());
        oper5 = new Operations(ifState.getRelOp());
        oper6 = new Operations(ifState.getMathOp());
        oper7 = new Operations(forLoop3.getrelOp());
        oper8 = new Operations(forLoop3.getdir());
        
    }
    
    public void writeSnapshot(int lineNum, question quest,int rowindex, int colindex) throws IOException
    {
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
    	show.writeSnap(title, null, pseudoCode.pseudo_uri(lineNum, pseudo, "<html><head><title>Nested For Loops</title></head><body><h1>Nested For Loops</h1>"), quest, text, line);
    	
    	pseudo.remove(pseudo.size()-1);
    	pseudo.remove(pseudo.size()-1);
    	
    }
    
    public void template1Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test() + "</pre>");
    	pseudo.add("<pre>2        " + forLoop2.test() + "</pre>");
    	pseudo.add("<pre>3            " + ifState.testWithoutArrayNameFirst(varName) + "</pre>");
    	pseudo.add("<pre>4                System.out.print("+ randChar + ") + \" \"" + "</pre>");
    	pseudo.add("<pre>5        }</pre>"); 
    	pseudo.add("<pre>6     }</pre>");
    	pseudo.add("<pre>     </pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    	
        
    }// template1 Test method
    
    public void template1Execute() throws IOException 
    {
    	writeSnapshot(-1, null, -1,-1);//prints the code. No line is highlighted
    	line.setColor("#EE0000");//colors the spot the array is on
    	
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	writeSnapshot(0, null,i,-1);//prints the code. Line 1 is highlighted
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	writeSnapshot(1, null,i,j);//prints the code with line 2 highlighted
            	line.setColor("#EE0000");//colors the spot the array is on
            	writeSnapshot(2, null,i,j);//prints the code with line 3 highlighted
  /*IF*/        if(varName.equalsIgnoreCase(forLoop1.getVariableName()))//checks to see if forLoop1 or forLoop2's variable is being used
            		if( oper5.testing(oper6.compute(i, ifState.getConstant()),ifState.getTermNum())){
            			printString = printString + randChar + " ";
            			line.setText(printString);
            			writeSnapshot(3, null,i,-1);
            		}
            	else
            		if( oper5.testing(oper6.compute(j, ifState.getConstant()),ifState.getTermNum())){
            			printString = printString + randChar + " ";
            			line.setText(printString);
            			writeSnapshot(3, null,i,-1);
            		}
                line.setColor("#999999");
                writeSnapshot(4, null,i,j);//highlights the closing bracket for the inner loop
            }// inner for
            writeSnapshot(5, null,i,-1);//highlights the closing bracket for the outer loop
        }// outer for
    }// template1 Execute method
    /*
    public void template2Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test()+ "</pre>");
    	pseudo.add("<pre>2        " + forLoop2.test()+ "</pre>");
    	pseudo.add("<pre>3            " + ifState.test(forLoop1.getVariableName(), forLoop2.getVariableName())+ "</pre>");
    	pseudo.add("<pre>4                "+ state.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>5            "+"else</pre>" );
    	pseudo.add("<pre>6                "+ state2.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>7        }</pre>");
    	pseudo.add("<pre>8     }</pre>");  
    	pseudo.add("<pre>     </pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    }// template2 Test method
    
    public void template2Execute() throws IOException
    {
    	copyArrayToGAIGS(a,-1, null,-1,-1);//prints the code. No line is highlighted
    	
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	copyArrayToGAIGS(a,0, questClass.generateArraySizeQuestion(forLoop1, forLoop2, ifState), i, -1);//prints the code with line 1 highlighted
            for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount()))
            {
            	copyArrayToGAIGS(a,1, questClass.generateGeneralArrayQuestion(forLoop1, forLoop2, ifState),i,j);//prints the code with line 2 highlighted
            	items.setColor(i, j, "#EE0000");//colors the spot the array is on
           	    copyArrayToGAIGS(a,2, null,i,j);//prints the code with line 3 highlighted
 /*IF    if( oper5.testing(oper6.compute(a[i][j], ifState.getConstant()),ifState.getTermNum())){
     			if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
     			{
     					if(i != i +oper7.conversion() || j != j + oper8.conversion())//checks if that assignment statement
     					{													   	     //is using the same spot in the array
     						items.setColor(i, j, "#999999");
     						items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#EE0000");
     						copyArrayToGAIGS(a,3, null,i,j);//highlights assignment statement
     					}
     					a[i +oper7.conversion()][j + oper8.conversion()] = oper9.compute(a[i +oper7.conversion()][j + oper8.conversion()], state.getnumber());
     					items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#0000EE");
     					copyArrayToGAIGS(a,3, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
     					items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#999999");
     			}else{ //using an array spot to perform the math operation 
     				if(i != i +oper7.conversion() || j != j + oper8.conversion())//checks if that assignment statement
     				{																 //is using the same spot in the array
     					items.setColor(i, j, "#999999");
     					items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#EE0000");
     					copyArrayToGAIGS(a,3, null,i,j);//highlights assignment statement
     				}
     				a[i +oper7.conversion()][j + oper8.conversion()] = oper9.compute(a[i +oper7.conversion()][j + oper8.conversion()],a[state.getindex3()][state.getindex4()] );
     				items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#0000EE");
     				copyArrayToGAIGS(a,3, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
     				items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#999999");
     				}		
 /*ELSE      }else{
            	   copyArrayToGAIGS(a,4, null,i,j);
                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {
                	   	if(i != i +oper10.conversion() || j != j + oper11.conversion())//checks if that assignment statement
                	   	{															  //is using the same spot in the array
                	   		items.setColor(i, j, "#999999");
                	   		items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#EE0000");
                	   		copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                	   	}
                	   	//assignment statement
                        a[i +oper10.conversion()][j + oper11.conversion()] = oper12.compute(a[i +oper10.conversion()][j + oper11.conversion()], state2.getnumber());
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#999999");
                   }//if
                    else//using an array spot to perform the math operation
                    {
                    	if(i != i +oper10.conversion() || j != j + oper11.conversion())//checks if that assignment statement
                	   	{															  //is using the same spot in the array
                	   		items.setColor(i, j, "#999999");
                	   		items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#EE0000");
                	   		copyArrayToGAIGS(a,5, null,i,j);//highlights assignment statement
                	   	}
                    	//assignment statement
                        a[i +oper10.conversion()][j + oper11.conversion()] = oper12.compute(a[i +oper10.conversion()][j + oper11.conversion()],a[state2.getindex3()][state2.getindex4()] );
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,5, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#999999");
                    }//inner else
               }//outer else
                items.setColor(i, j, "#999999");
                copyArrayToGAIGS(a,6, questClass.generateArraySizeQuestion(forLoop1, forLoop2, ifState),i,j);//highlighting closing bracket for inner for loop
            }// inner for
            copyArrayToGAIGS(a,7, questClass.generateGeneralArrayQuestion(forLoop1, forLoop2, ifState),i,-1);//highlights closing bracket of the outer for loop
        }// outer for
    }// template2 Execute method
       
    public void template3Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test()+ "</pre>");
    	pseudo.add("<pre>2        " + ifState.testWithoutArray(forLoop1.getVariableName())+ "</pre>");
    	pseudo.add("<pre>3        {</pre>");
    	pseudo.add("<pre>4            " +forLoop2.test()+ "</pre>");
    	pseudo.add("<pre>5                "+ state.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>6        }</pre>");
    	pseudo.add("<pre>7        " + "else</pre>" );
    	pseudo.add("<pre>8        {</pre>");
    	pseudo.add("<pre>9                "+ forLoop3.test()+ "</pre>");
    	pseudo.add("<pre>10                   "+ state2.test(true, ifState.getArrayName())+ "</pre>");
    	pseudo.add("<pre>11       }</pre>");
    	pseudo.add("<pre>12    }</pre>");
    	pseudo.add("<pre>     </pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    	System.out.println(pseudo);
    	System.out.println(forLoop1.getSize());
    	System.out.println(forLoop2.getSize());
    	System.out.println(forLoop3.getSize());             
    }// template3 Test method
    
    public void template3Execute() throws IOException

    {
    	copyArrayToGAIGS(a,-1, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),-1,-1);//prints the code. No line is highlighted
    	
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	copyArrayToGAIGS(a,0, null,i,-1);//prints the code with line 1 highlighted
        	copyArrayToGAIGS(a,1, questClass.generateArraySizeQuestion(forLoop1, forLoop2, ifState),i,-1);//highlights line 2 
 /*IF     if( oper5.testing(oper6.compute(i, ifState.getConstant()),ifState.getTermNum()))
            {
            	copyArrayToGAIGS(a,2, questClass.generateGeneralArrayQuestion(forLoop1, forLoop2, ifState),i,-1); //highlights line 3
                for(int j = forLoop2.getinitialNum(); oper3.testing(j,forLoop2.getTerminalNum()); j = oper4.compute(j,forLoop2.getiterationCount())){
                	
                	items.setColor(i, j, "#EE0000");
               	 	 copyArrayToGAIGS(a,3, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highights line 4
                     if(state.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                     {
                    	 if(i != i +oper7.conversion() || j != j + oper8.conversion())//checks if that assignment statement
                 	   	 { 															  //is using the same spot in the array
                    		 items.setColor(i, j, "#999999");
                    		 items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#EE0000");
                    		 copyArrayToGAIGS(a,4, null,i,j);//highlights assignment statement
                    	 }
                    	//assignment statement
                        a[i +oper7.conversion()][j + oper8.conversion()] = oper9.compute(a[i +oper7.conversion()][j + oper8.conversion()], state.getnumber());   
                        //items.setColor(i,j,"#999999");
                        items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,4, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#999999");
                        copyArrayToGAIGS(a,5, null,i,j); //highlight the closing bracket of the if
                     }
                     else//using an array spot to perform the math operation
                     {
                    	 if(i != i +oper7.conversion() || j != j + oper8.conversion())//checks if that assignment statement
                  	     {															  //is using the same spot in the array
                    		 items.setColor(i, j, "#999999");
                    		 items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#EE0000");
                    		 copyArrayToGAIGS(a,4, null,i,j);//highlights assignment statement
                    	 }
                    	//assignment statement
                        a[i +oper7.conversion()][j + oper8.conversion()] = oper9.compute(a[i +oper7.conversion()][j + oper8.conversion()],a[state.getindex3()][state.getindex4()] ); 
                        items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,4, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper7.conversion(),j + oper8.conversion(), "#999999");
                        copyArrayToGAIGS(a,5, null,i,j); //highlights the closing bracket of the if
                     }
              }//inner for loop
 /*ELSE   }else{
            	copyArrayToGAIGS(a,6, null,i,-1);//highlights the else
            	copyArrayToGAIGS(a,7, questClass.generateGeneralArrayQuestion(forLoop1, forLoop2, ifState), i,-1); //highlights the opening bracket
                for(int j = forLoop3.getinitialNum(); oper13.testing(j,forLoop3.getTerminalNum()); j = oper14.compute(j,forLoop3.getiterationCount())){
                   items.setColor(i, j, "#EE0000");
               	   copyArrayToGAIGS(a,8, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);
                   if(state2.getnumber() > 0)//checks whether or not the math operation is using a number or an array spot
                   {
                	   if(i != i +oper10.conversion() || j != j + oper11.conversion())//checks if that assignment statement
               	   	{															  //is using the same spot in the array
                  		 items.setColor(i, j, "#999999");
                  		 items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#EE0000");
                  		 copyArrayToGAIGS(a,9, null,i,j);//highlights assignment statement
                  	 }
                		//assignment statement
                        a[i +oper10.conversion()][j + oper11.conversion()] = oper12.compute(a[i +oper10.conversion()][j + oper11.conversion()], state2.getnumber());
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,9, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#999999");
                   }//outer if
                    else//using an array spot to perform the math operation
                    {
                    	 if(i != i +oper10.conversion() || j != j + oper11.conversion())
                      	 {
                      		 items.setColor(i, j, "#999999");
                      		 items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#EE0000");
                      		 copyArrayToGAIGS(a,9, null,i,j);//highlights assignment statement
                      	 }
                    	//assignment statement 
                        a[i +oper10.conversion()][j + oper11.conversion()] = oper12.compute(a[i +oper10.conversion()][j + oper11.conversion()],a[state2.getindex3()][state2.getindex4()] );
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#0000EE");
                        copyArrayToGAIGS(a,9, questClass.generateArrayElementQuestion(forLoop1, forLoop2, ifState),i,j);//highlights assignment statement
                        items.setColor(i +oper10.conversion(),j + oper11.conversion(), "#999999"); 
                    }
                 items.setColor(i, j, "#999999");
               	copyArrayToGAIGS(a,10, questClass.generateArraySizeQuestion(forLoop1, forLoop2, ifState),i,j);//highlights the closing bracket for else
               }// inner for
            }// else
            copyArrayToGAIGS(a, 11, questClass.generateGeneralArrayQuestion(forLoop1, forLoop2, ifState),i,-1);//highlights the closing bracket for the outer for loop
        }// outer for
    }// template3Execute method


    public class NestedForLoopQuestions
    {
    	private int id = 0;
    	private Random random;
    	
    	public NestedForLoopQuestions()
    	{
    		random = new Random();
    	}
    	
    	public ArrayList<question> generateAskOnceQuestions(ForLoop forLoop1, ForLoop forLoop2, IfStatement ifState)
    	{
    		ArrayList<question> q = new ArrayList<question>();
    		
    		XMLfibQuestion question1;
            XMLtfQuestion question2;
            XMLmcQuestion question3;
    		
            int questionNum = random.nextInt(6);
        	
		    id++;
		    questionNum = random.nextInt(5);
		    
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
	            	question2.setQuestionText("The name of the array is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 4:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the array?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(1);
	            	q.add(question3);break;
		    }
		    
		    id++;
		    questionNum = random.nextInt(5);
		    
		    switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the outer For loop's variable?");
	            	question1.setAnswer(forLoop1.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the outer For loop's variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 4:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the outer For loop's variable?");
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
	            	question2.setQuestionText("The outer For loop's variable is incremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("+"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The outer For loop's variable is decremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("-"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
		    }
        	
		    id++;
		    questionNum = random.nextInt(5);
		    
		    switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the inner For loop's variable?");
	            	question1.setAnswer(forLoop2.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the inner For loop's variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 4:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the inner For loop's variable?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(3);
	            	q.add(question3);break;
		    }
		    
        	return q;
				
    	}// ask once questions
    	
    }//Inner Class - Questions
*/
}// NestedForLoopsPrintLn
