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

public class SingleForLoopPrintLn {
   private ForLoop forLoop1, forLoop2;
   private IfStatement ifState, ifState2, ifState3;
   private PseudoCode pseudoCode;
   private SingleForLoopQuestions questClass;
   private ArrayList<question> askOnceQuest = new ArrayList<question>();
   private Statements state;
   private Operations oper, oper2, oper3, oper4, oper5, oper6, oper7, oper8, oper9, oper10, oper11, oper12, oper13, operPrint;
   private RandomizedVariables random;
   private String varName;
   private static Random randInt;
   
   private static String finalStr = "";
   private static String withColor = "";
   private static String cursor = "";
   private static String[] str1;
   private int string1Count = 0;
   private static String[] str2;
   private int string2Count = 0;
   private static String[] str3;
   private int string3Count = 0;
   private String[] randString = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
   
   private String str1VarName;
   private String str2VarName;
   private String str3VarName;
   
   ArrayList<String> pseudo = new ArrayList<String>();
   static  String title = "";
   static GAIGStext finalString;
   static GAIGStext output;
   static GAIGSarray string1, string2, string3;
   private static ShowFile show;
   private static boolean experiencedUser = true;
   
   public static void main (String [] args) throws IOException {
	    SingleForLoopPrintLn nfl = new SingleForLoopPrintLn();
	    
	    if(args[1].equals("E"))
	    	experiencedUser = true;
	    else
	    	experiencedUser = false;
	    
      	show = new ShowFile(args[0]+ ".sho", 10);
      	
		// define the three structures in the show snapshots
		finalString = new GAIGStext(0.5, 0.0, "finalString = " + finalStr);
		finalString.setFontsize(0.1);
		output = new GAIGStext(0.25, 0.75,GAIGStext.HLEFT, GAIGStext.VTOP, 0.1, "#000000", finalStr);
		
		string1 = new GAIGSarray(1, str1.length,"","#999999",0.1, 0.3, 0.9, 1.2, 0.08);
		string1.setRowLabel("string1 = ",0);
		string2 = new GAIGSarray(1, str2.length,"","#999999",0.1, 0.1, 0.9, 1.0, 0.08);
		string2.setRowLabel("string2 = ",0);
		string3 = new GAIGSarray(1, str3.length,"","#999999",0.1, -0.1, 0.9, 0.8, 0.08);
		string3.setRowLabel("string3 = ",0);
		
		for(int i=0; i < str1.length; i++)
		{
			string1.set(str1[i], 0, i);
			string1.setColLabel(i + "", i);
			string2.set(str2[i], 0, i);
			string2.setColLabel(i + "", i);
			string3.set(str3[i], 0, i);
			string3.setColLabel(i + "", i);
		}//for loop	
		
		int example = randInt.nextInt(3);
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
		}
	    
	    show.close();
   }
   
    /**
     * Constructor for NestedForLoopsPrintLn class
     */
    public SingleForLoopPrintLn()
    {
        random = new RandomizedVariables();
        randInt = new Random();
        
        pseudoCode = new PseudoCode();
        questClass = new SingleForLoopQuestions();
                    
        forLoop1 = new ForLoop(8,10);
        varName = forLoop1.getVariableName();
        ifState = new IfStatement();
        ifState2 = new IfStatement();
        ifState3 = new IfStatement(true);
        forLoop2 = new ForLoop();
        state = new Statements(forLoop2.getVariableName());
        str1VarName = random.randNumIndex();
        str2VarName = random.randNumIndex();
        str3VarName = random.randNumIndex();

        oper = new Operations(forLoop1.getrelOp());
        oper2 = new Operations(forLoop1.getdir());
        oper3 = new Operations(ifState.getRelOp());
        oper4 = new Operations(ifState.getMathOp());
        oper5 = new Operations(ifState2.getRelOp());
        oper6 = new Operations(ifState2.getMathOp());  
        
        oper7 = new Operations(forLoop2.getrelOp());
        oper8 = new Operations(forLoop2.getdir());
        oper9 = new Operations(ifState3.getMathOp());
        oper10 = new Operations(ifState3.getRelOp());
        operPrint = new Operations(state.getmathop());
        
        oper11 = new Operations(str1VarName);
        oper12 = new Operations(str2VarName);
        oper13 = new Operations(str3VarName);
       
        if(forLoop1.getdir().equalsIgnoreCase("+"))
        {
        	str1 = new String[forLoop1.getTerminalNum()+2];
        	str2 = new String[forLoop1.getTerminalNum()+2];
        	str3 = new String[forLoop1.getTerminalNum()+2];        	
        }else{
        	str1 = new String[forLoop1.getinitialNum()+2];
        	str2 = new String[forLoop1.getinitialNum()+2];
        	str3 = new String[forLoop1.getinitialNum()+2];
        }
        
        for(int i=0; i < str1.length; i++)
		{
			str1[i] = random.randName(randString);
			str2[i] = random.randName(randString);
			str3[i] = random.randName(randString);
		}//for loop	
    }
    
    public void writeSnapshot(int lineNum, question quest,int rowindex) throws IOException
    {
    	 if(rowindex != -10 ) // I put -10 because sometimes I need rowindex to equal -1
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + rowindex + "</pre>");
    	 else
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + "</pre>");
    	show.writeSnap(title, 0.1, null, pseudoCode.pseudo_uri(lineNum, pseudo, "<html><head><title>Single For Loops</title></head><body><h1>Single For Loops</h1>"),
    					quest, finalString, string1, string2);
    	pseudo.remove(pseudo.size()-1);
    }
    
    public void writeSnapshot(int lineNum, question quest,int rowindex, boolean template2) throws IOException
    {
    	 if(rowindex != -10 ) // I put -10 because sometimes I need rowindex to equal -1
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + rowindex + "</pre>");
    	 else
    		 pseudo.add("<pre>"+ forLoop1.getVariableName()+ " = " + "</pre>");
    	show.writeSnap(title, 0.1, null, pseudoCode.pseudo_uri(lineNum, pseudo, "<html><head><title>Single For Loops</title></head><body><h1>Single For Loops</h1>"),
    					quest, finalString, string1, string2, string3);
    	pseudo.remove(pseudo.size()-1);
    }
    
    public void writeSnapshot( int lineNum, question quest,int rowindex, int count) throws IOException
    {
    	 if(rowindex != -1) 
    	 {
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + rowindex + "</pre>");
    	 	 pseudo.add("<pre>"+ ifState3.getcountName()+ " = " + count + "</pre>");
    	 }
    	 else
    	 {
    		 pseudo.add("<pre>"+ forLoop2.getVariableName()+ " = " + "</pre>");
    		 pseudo.add("<pre>"+ ifState3.getcountName()+ " = " + count + "</pre>");
    	 }
    	show.writeSnap(title, 0.1, null, pseudoCode.pseudo_uri(lineNum, pseudo, "<html><head><title>Single For Loops</title></head><body><h1>Single For Loops</h1>"),
    					quest, output);
    	pseudo.remove(pseudo.size()-1);
    	pseudo.remove(pseudo.size()-1);
    }
    
    public void template1Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test() + "</pre>");
    	pseudo.add("<pre>2    {</pre>");
    	pseudo.add("<pre>3        " + ifState.testWithoutArrayNameFirst(varName) + "</pre>");
    	pseudo.add("<pre>4            finalString += string1.charAt(" + forLoop1.getVariableName() + str1VarName + ");</pre>");
    	pseudo.add("<pre>5        else</pre>"); 
    	pseudo.add("<pre>6            finalString += string2.charAt(" + forLoop1.getVariableName() + str2VarName+ ");</pre>"); 
    	pseudo.add("<pre>7    }</pre>");
    	pseudo.add("<pre>8    // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variable:</h1>     </pre>");
        
    }// template1 Test method
    
    public void template1Execute() throws IOException 
    {
    	/* Start generating results */
    	
    	String resultStr = "";
    	String wrongResult = "";
    	ArrayList<String> resultArray = new ArrayList<String>();
    	ArrayList<String> wrongResultArray = new ArrayList<String>();
    	int count = 0;
    	int line4Count = 0;
    	int line6Count = 0;
    	
    	for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
  /*IF*/    count++;
    		if( oper3.testing(oper4.compute(i, ifState.getConstant()),ifState.getTermNum())){
	  			wrongResult = resultStr + str2[i + oper12.conversion()];
    			resultStr += str1[i + oper11.conversion()];
    			string1Count++;
    			line4Count++;
  			}
  			else{
  				wrongResult = resultStr + str1[i + oper11.conversion()];
  				resultStr += str2[i + oper12.conversion()];
  				string2Count++;
  				line6Count++;
  			}
    		resultArray.add(resultStr);
    		wrongResultArray.add(wrongResult);
        }//for
    	forLoop1.setCount(count);
    	
    	/* Done generating results */
    	
    	int finalNum = 0;
    	askOnceQuest = questClass.generateAskOnceQuestions();
    	int listIndex = 0;
    	count = 0;
    	writeSnapshot(-1, null, -10);//prints the code. No line is highlighted
    	
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {
        	writeSnapshot(0, null,i);//prints the code. Line 1 is highlighted
        	writeSnapshot(1, questClass.generateNextResultQuestion(resultArray, wrongResultArray, count),i);//prints the code. Line 2 is highlighted
        	if(count == 0 && experiencedUser == true)
				writeSnapshot(2, questClass.executionOfLineX(line4Count, 4),i);
        	else if(count == 1 && experiencedUser == true)
				writeSnapshot(2, questClass.executionOfLineX(line6Count, 6),i);
			else
				writeSnapshot(2, null,i);
  /*IF*/    if( oper3.testing(oper4.compute(i, ifState.getConstant()),ifState.getTermNum())){
  				withColor = finalStr + "\\#EE0000" + str1[i + oper11.conversion()];
	  			finalStr += str1[i + oper11.conversion()];
  				finalString.setText("finalString = " + withColor);
  				string1.setColor(0, i + oper11.conversion(), "#EE0000");
  	        	writeSnapshot(3, null,i);
  	        	string1.setColor(0, i + oper11.conversion(), "#999999");
  	        	finalString.setText("finalString = " + finalStr);
  			}
  			else{
  				if (askOnceQuest.isEmpty()){
  					writeSnapshot(4, null,i);
  				}else{
  	        		listIndex = randInt.nextInt(askOnceQuest.size());  
  	        		writeSnapshot(4, askOnceQuest.get(listIndex),i);
  	            	askOnceQuest.remove(listIndex);
  	        	}
  				withColor = finalStr + "\\#EE0000" + str2[i + oper12.conversion()];
  				finalStr += str2[i + oper12.conversion()];
  				finalString.setText("finalString = " + withColor);
  				string2.setColor(0, i + oper12.conversion(), "#EE0000");
  	        	writeSnapshot(5, null,i);
  	        	string2.setColor(0, i + oper12.conversion(), "#999999");
  	        	finalString.setText("finalString = " + finalStr);
  			}
			if(count < (resultArray.size()-3) && experiencedUser == true)
				writeSnapshot(6, questClass.futureInterationQuestions(resultArray, wrongResultArray, count),i);
			else
				writeSnapshot(6, null,i);
  			finalNum = i;
  			count++;
        }//for
        writeSnapshot(0,null,oper2.compute(finalNum,forLoop1.getiterationCount()));
        writeSnapshot(7,null,-10);        
    }// template1 Execute method
    
    public void template2Test()
    {
    	pseudo.add("<pre>1    "+ forLoop1.test() + "</pre>");
    	pseudo.add("<pre>2    {</pre>");
    	pseudo.add("<pre>3        " + ifState.testWithoutArrayNameFirst(varName) + "</pre>");
    	pseudo.add("<pre>4            finalString += string1.charAt(" + forLoop1.getVariableName() + str1VarName + ");</pre>");
    	pseudo.add("<pre>5        else</pre>"); 
    	pseudo.add("<pre>6            finalString += string2.charAt(" + forLoop1.getVariableName() + str2VarName + ");</pre>"); 
    	pseudo.add("<pre>7        " + ifState2.testWithoutArrayNameFirst(varName) + "</pre>");
    	pseudo.add("<pre>8            finalString += string3.charAt(" + forLoop1.getVariableName() + str3VarName + ");</pre>");
    	pseudo.add("<pre>9    }</pre>");
    	pseudo.add("<pre>10   // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variable:</h1>     </pre>");
    }// template2 Test method
    
    public void template2Execute() throws IOException
    {
    	/* Start generating result*/
    	
    	String resultStr = "";
    	String wrongResult = "";
    	ArrayList<String> resultArray = new ArrayList<String>();
    	ArrayList<String> wrongResultArray = new ArrayList<String>();
    	int count = 0;
    	int line4Count = 0;
    	int line6Count = 0;
    	int line8Count = 0;
    	
    	for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {        	
        	count++;
  /*IF*/    if( oper3.testing(oper4.compute(i, ifState.getConstant()),ifState.getTermNum())){
	  			wrongResult = resultStr + str2[i + oper12.conversion()];
	  			resultStr += str1[i + oper11.conversion()];
	  			string1Count++;
	  			line4Count++;
  			}
  			else{
  				wrongResult = resultStr + str3[i + oper13.conversion()];
  				resultStr += str2[i + oper12.conversion()];
  				string2Count++;
  				line6Count++;
  			}
			if(oper5.testing(oper6.compute(i, ifState2.getConstant()),ifState2.getTermNum())){
				wrongResult = resultStr + str2[i + oper12.conversion()];
				resultStr += str3[i + oper13.conversion()];
				string3Count++;
				line8Count++;
        	}
			wrongResultArray.add(wrongResult);
			resultArray.add(resultStr);
        }//for
    	forLoop1.setCount(count);
    	
    	/* End generating result*/
    	
    	int finalNum = 0;
    	askOnceQuest = questClass.generateAskOnceQuestions();
    	int listIndex = 0;
    	count = 0;
    	writeSnapshot(-1, null, -10, true);//prints the code. No line is highlighted
    	
        for(int i = forLoop1.getinitialNum(); oper.testing(i,forLoop1.getTerminalNum()); i = oper2.compute(i,forLoop1.getiterationCount()))
        {        	
        	writeSnapshot(0, null,i, true);//prints the code. Line 1 is highlighted
        	writeSnapshot(1, questClass.generateNextResultQuestion(resultArray, wrongResultArray, count),i, true);//prints the code. Line 2 is highlighted
        	if(count == 0 && experiencedUser == true)
  				writeSnapshot(2, questClass.executionOfLineX(line4Count, 4),i, true);
        	else if(count == 1 && experiencedUser == true)
  				writeSnapshot(2, questClass.executionOfLineX(line6Count, 6),i, true);
        	else if(count == 2 && experiencedUser == true)
  				writeSnapshot(2, questClass.executionOfLineX(line8Count, 8),i, true);
  			else
  				writeSnapshot(2, null,i, true);
  /*IF*/    if( oper3.testing(oper4.compute(i, ifState.getConstant()),ifState.getTermNum())){
  				withColor = finalStr + "\\#EE0000" + str1[i + oper11.conversion()];
	  			finalStr += str1[i + oper11.conversion()];
  				finalString.setText("finalString = " + withColor);
  				string1.setColor(0, i + oper11.conversion(), "#EE0000");
  	        	writeSnapshot(3, null,i,true);
  	        	string1.setColor(0, i + oper11.conversion(), "#999999");
  	        	finalString.setText("finalString = " + finalStr);
  			}
  			else{
  				writeSnapshot(4, null,i, true);
  				withColor = finalStr + "\\#EE0000" + str2[i + oper12.conversion()];
  				finalStr += str2[i + oper12.conversion()];
  				finalString.setText("finalString = " + withColor);
  				string2.setColor(0, i + oper12.conversion(), "#EE0000");
  	        	writeSnapshot(5, null,i, true);
  	        	string2.setColor(0, i + oper12.conversion(), "#999999");
  	        	finalString.setText("finalString = " + finalStr);
  			}
  			if (askOnceQuest.isEmpty()){
  				writeSnapshot(6, null,i, true);
        	}else{
        		listIndex = randInt.nextInt(askOnceQuest.size());  
        		writeSnapshot(6, askOnceQuest.get(listIndex),i, true);
            	askOnceQuest.remove(listIndex);
        	}
			if(oper5.testing(oper6.compute(i, ifState2.getConstant()),ifState2.getTermNum())){
            	withColor = finalStr + "\\#EE0000" + str3[i + oper13.conversion()];
        		finalStr += str3[i + oper13.conversion()];
        		finalString.setText("finalString = " + withColor);
        		string3.setColor(0, i + oper13.conversion(), "#EE0000");
            	writeSnapshot(7, null,i, true);
            	string3.setColor(0, i + oper13.conversion(), "#999999");
            	finalString.setText("finalString = " + finalStr);
        	}
  			if(count < (resultArray.size()-3) && experiencedUser == true)
  				writeSnapshot(8, questClass.futureInterationQuestions(resultArray, wrongResultArray, count),i, true);
  			else
  				writeSnapshot(8, null,i, true);
  			finalNum = i;
  			count++;
        }//for
        writeSnapshot(0,null,oper2.compute(finalNum,forLoop1.getiterationCount()),true);
        writeSnapshot(9,null,-10, true);
    }// template2 Execute method
       
    public void template3Test()
      {
    	pseudo.add("<pre>1    int "+ifState3.getcountName()+ " = "+ ifState3.getConstant()+";</pre>");
    	pseudo.add("<pre>2    "+ forLoop2.test() + "</pre>");
    	pseudo.add("<pre>3    {</pre>");
    	pseudo.add("<pre>4         "+ state.test()+"</pre>");
    	pseudo.add("<pre>5         " + ifState3.getcountName()+" = "+ ifState3.getcountName() + " + "+ state.getcountIter()+";</pre>");
    	pseudo.add("<pre>6         " + ifState3.ModifiedIfTest() + "</pre>");
    	pseudo.add("<pre>7            System.out.println();</pre>");
    	pseudo.add("<pre>8    }</pre>");
    	pseudo.add("<pre>9    // End of Execution</pre>");
    	pseudo.add("<pre><h1>Variables:</h1>     </pre>");
    }// template3 Test method
    
    public void template3Execute() throws IOException
    {	
    	int count = ifState3.getConstant();
    	int snapshotCount = 0;
    	int forLoopCount = 0;
    	int resultInt = 0;
    	int line7Count = 0;
    	int finalSum = 0;
    	ArrayList<Integer> resultArray = new ArrayList<Integer>();
    	ArrayList<Boolean> booleanArray = new ArrayList<Boolean>();
    	ArrayList<Boolean> wrongBooleanArray = new ArrayList<Boolean>();
    	
    	for(int i = forLoop2.getinitialNum(); oper7.testing(i,forLoop2.getTerminalNum()); i = oper8.compute(i,forLoop2.getiterationCount()))
    	{
    		forLoopCount++;
    		snapshotCount++;
			resultInt = operPrint.compute(state.getnum1(), i);
			finalSum += resultInt;
			resultArray.add(resultInt);
			snapshotCount++;
    		count = count + state.getcountIter();
    		snapshotCount++;	
    		snapshotCount++;	
    		if(oper10.testing(oper9.compute(count,ifState3.getperLine()), ifState3.getTermNum()))  
    		{
    			snapshotCount++;
    			line7Count++;
    			booleanArray.add(true);
    			wrongBooleanArray.add(false);
    		}else{
    			booleanArray.add(false);
    			wrongBooleanArray.add(true);
    		}
    		snapshotCount++;
    		snapshotCount++;
    	}
    	
    	forLoop2.setCount(forLoopCount);
    	//_________________________________________________________________________________________________
    	
    	int questionCount = 0;
    	count = ifState3.getConstant();
    	
    	askOnceQuest = questClass.generateAskOnceQuestionsT3();
    	int listIndex = 0;
    	
    	writeSnapshot( 1, null,forLoop2.getinitialNum(), count);//prints the code. Line 1 is highlighted
    	for(int i = forLoop2.getinitialNum(); oper7.testing(i,forLoop2.getTerminalNum()); i = oper8.compute(i,forLoop2.getiterationCount()))
    	{
    		writeSnapshot( 2, questClass.generateNextIntQuestion(resultArray, questionCount),i, count);
    		withColor = finalStr + "\\#EE0000" + operPrint.compute(state.getnum1(), i);
			output.setText(withColor);
			finalStr += operPrint.compute(state.getnum1(), i) + "\t";
			cursor = finalStr + " | ";
			if(questionCount == 0 && experiencedUser == true)
    			writeSnapshot(3, questClass.executionOfLineX(line7Count, 7),i, count);
    		else
    			writeSnapshot(3,null,i,count);
    		count = count + state.getcountIter();
    		output.setText(cursor);
    		if(questionCount == 1 && experiencedUser == true)
    			writeSnapshot(4, questClass.sumQuestion(finalSum),i, count);
    		else
    			writeSnapshot(4,null,i,count);
    		writeSnapshot(5, questClass.generateBooleanResultQuestion(booleanArray, wrongBooleanArray, questionCount),i, count);	
    		if(oper10.testing(oper9.compute(count,ifState3.getperLine()), ifState3.getTermNum()))  
    		{
    			finalStr += "\n";
    			cursor = finalStr + " | ";
    			output.setText(cursor); 
    			writeSnapshot(6, null,i, count);
    		}
    		if(questionCount < (resultArray.size()-3) && experiencedUser == true)
    			writeSnapshot(7, questClass.futureInterationQuestions(resultArray, questionCount),i, count);
    		else
    			writeSnapshot(7,null,i,count);
    		if (askOnceQuest.isEmpty()){
    			writeSnapshot(1, null,oper8.compute(i,forLoop2.getiterationCount()), count);
        	}else{
        		listIndex = randInt.nextInt(askOnceQuest.size());  
        		writeSnapshot(1, askOnceQuest.get(listIndex),oper8.compute(i,forLoop2.getiterationCount()), count);
            	askOnceQuest.remove(listIndex);
        	}
    		questionCount++;
    	}
    	writeSnapshot(8, null,-1, count);
    }// template3execute
    
    public class SingleForLoopQuestions
    {
    	private int id = 0;
    	private Random random;
    	
    	public SingleForLoopQuestions()
    	{
    		random = new Random();
    	}
    	
    	public ArrayList<question> generateAskOnceQuestions()
    	{
    		ArrayList<question> q = new ArrayList<question>();
    		
    		XMLfibQuestion question1;
            XMLtfQuestion question2;
            XMLmcQuestion question3;
    		
            id++;
            int questionNum = random.nextInt(7);
		    
		    switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the for loop's counter variable?");
	            	question1.setAnswer(forLoop1.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 4:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 5:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 6:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the for loop's counter variable?");
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
	            	question2.setQuestionText("The for loop's counter variable is incremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("+"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The for loop's counter variable is decremented during the execution of the loop.");
	            	if(forLoop1.getdir().equals("-"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
		    }
		    
		    id++;
    		questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
				case 0:
		        	question1 = new XMLfibQuestion( show, id + "" );
		    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the for loop executed?" );
		    		question1.setAnswer( "" + forLoop1.getCount() );
		    		q.add(question1);break;
				case 1:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop1.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
				case 2:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop1.getCount()+1) + " time(s)." );
		    		question2.setAnswer( false );
		    		q.add(question2);break;
				case 3:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop1.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
				case 4:
					question2 = new XMLtfQuestion( show, id + "" );
		    		if(forLoop1.getCount() != 0)
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop1.getCount()-1) + " time(s)." );
			    	else
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop1.getCount()+2) + " time(s)." );
		    		question2.setAnswer( false );
		    		q.add(question2);break;
				case 5:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop1.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
			}				
		    
        	return q;
				
    	}// ask once questions
    	
    	public ArrayList<question> generateAskOnceQuestionsT3()
    	{
    		ArrayList<question> q = new ArrayList<question>();
    		
    		XMLfibQuestion question1;
            XMLtfQuestion question2;
            XMLmcQuestion question3;
    		
            id++;
            int questionNum = random.nextInt(7);
		    
            switch (questionNum) {
	            case 0:
	            	question1 = new XMLfibQuestion(show, id + "");
	            	question1.setQuestionText("What is the name of the for loop's counter variable?");
	            	question1.setAnswer(forLoop2.getVariableName() + "");
	            	q.add(question1);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 2:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + ifState.getArrayName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 3:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 4:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop1.getVariableName() + ".");
	            	question2.setAnswer(false);
	            	q.add(question2);break;
	            case 5:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The name of the for loop's counter variable is " + forLoop2.getVariableName() + ".");
	            	question2.setAnswer(true);
	            	q.add(question2);break;
	            case 6:
	            	question3 = new XMLmcQuestion(show, id + "");
	            	question3.setQuestionText("What is the name of the for loop's counter variable?");
	            	question3.addChoice(ifState.getArrayName() + "");
	            	question3.addChoice(forLoop1.getVariableName() + "");
	            	question3.addChoice(forLoop2.getVariableName() + "");
	            	question3.setAnswer(3);
	            	q.add(question3);break;
		    }
        	
		    id++;
		    questionNum = random.nextInt(2);
		    
		    switch (questionNum) {		    
	            case 0:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The for loop's counter variable is incremented during the execution of the loop.");
	            	if(forLoop2.getdir().equals("+") || forLoop2.getdir().equals("*"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
	            case 1:
	            	question2 = new XMLtfQuestion(show, id + "");
	            	question2.setQuestionText("The for loop's counter variable is decremented during the execution of the loop.");
	            	if(forLoop2.getdir().equals("-") || forLoop2.getdir().equals("/"))
	            		question2.setAnswer(true);
	            	else
	            		question2.setAnswer(false);
	            	q.add(question2);break;
		    }
		    
		    id++;
    		questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
				case 0:
		        	question1 = new XMLfibQuestion( show, id + "" );
		    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is the body of the for loop executed?" );
		    		question1.setAnswer( "" + forLoop2.getCount() );
		    		q.add(question1);break;
				case 1:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop2.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
				case 2:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop2.getCount()+1) + " time(s)." );
		    		question2.setAnswer( false );
		    		q.add(question2);break;
				case 3:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop2.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
				case 4:
					question2 = new XMLtfQuestion( show, id + "" );
		    		if(forLoop2.getCount() != 0)
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop2.getCount()-1) + " time(s)." );
			    	else
			    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + (forLoop2.getCount()+2) + " time(s)." );
		    		question2.setAnswer( false );
		    		q.add(question2);break;
				case 5:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, the body of the for loop is executed " + forLoop2.getCount() + " time(s)." );
		    		question2.setAnswer( true );
		    		q.add(question2);break;
			}
		    
        	return q;
				
    	}// ask once questions T3
    	
    	public question executionOfLineX(int line, int i)
    	{
    		XMLfibQuestion question1;
            XMLtfQuestion question2;
            
    		id++;
    		int questionNum = random.nextInt(6);
		    
		    switch (questionNum) {
				case 0:
		        	question1 = new XMLfibQuestion( show, id + "" );
		    		question1.setQuestionText( "From beginning to end of the execution of this program, how many times is line " + i + " executed?" );
		    		question1.setAnswer( line + "");
		    		question1.setMustBeAsked(true);
		    		return question1;
				case 1:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + line + " time(s)." );
		    		question2.setAnswer( true );
		    		question2.setMustBeAsked(true);
		    		return question2;
				case 2:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + (line+1) + " time(s)." );
		    		question2.setAnswer( false );
		    		question2.setMustBeAsked(true);
		    		return question2;
				case 3:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + line + " time(s)." );
		    		question2.setAnswer( true );
		    		question2.setMustBeAsked(true);
		    		return question2;
				case 4:
					question2 = new XMLtfQuestion( show, id + "" );
		    		if(line != 0)
			    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + (line-1) + " time(s)." );
			    	else
			    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + (line+2) + " time(s)." );
		    		question2.setAnswer( false );
		    		question2.setMustBeAsked(true);
		    		return question2;
				case 5:
		    		question2 = new XMLtfQuestion( show, id + "" );
		    		question2.setQuestionText( "From beginning to end of the execution of this program, line " + i +" is executed " + line + " time(s)." );
		    		question2.setAnswer( true );
		    		question2.setMustBeAsked(true);
		    		return question2;
		    	default:
		    		question1 = new XMLfibQuestion( show, id + "" );
	    			question1.setQuestionText( "From beginning to end of the execution of this program, how many times is line " + i +" executed?" );
	    			question1.setAnswer( line + "");
	    			question1.setMustBeAsked(true);
	    			return question1;
		    }
    	}
    	
    	public question generateNextResultQuestion(ArrayList<String> result, ArrayList<String> wrongResult, int i)
    	{
    		int questionNumber = random.nextInt(3);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLmcQuestion question2 = new XMLmcQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
        	
        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What will the variable finalString be equal to at the end of this iteration?");
	            	question1.setAnswer(result.get(i));
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i) + " at the end of this iteration.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("The variable finalString will be equal to " + wrongResult.get(i) + " at the end of this iteration.");
	        		if(result.get(i).equals(wrongResult.get(i)))
	        			question3.setAnswer(true);
	        		else
	        			question3.setAnswer(false);
	        		return question3;
	        	case 3:
	        		question2.setQuestionText("What will the variable finalString be equal to at the end of this iteration?");
	        		question2.addChoice(result.get(i));
	        		question2.addChoice(wrongResult.get(i));
	        		question2.setAnswer(1);
	        		return question2;
	        	default: 
	            	question1.setQuestionText("What will the variable finalString be equal to at the end of this iteration?");
	            	question1.setAnswer(result.get(i));
	            	return question1;
        	}
    	}
    	
    	public question generateBooleanResultQuestion(ArrayList<Boolean> result, ArrayList<Boolean> wrongResult, int i)
    	{
    		int questionNumber = random.nextInt(4);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLmcQuestion question2 = new XMLmcQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
        	
        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What will the boolean expression be evaluated to?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("The boolean expression will be evaluated to " + result.get(i) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("The boolean expression will be evaluated to " + wrongResult.get(i) + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 3:
	        		question2.setQuestionText("What will the boolean expression be evaluated to?");
	        		question2.addChoice(result.get(i) + "");
	        		question2.addChoice(wrongResult.get(i) + "");
	        		question2.setAnswer(1);
	        		return question2;
	        	default: 
	            	question1.setQuestionText("What will the boolean expression be evaluated to?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
        	}
    	}
    	
    	public question generateNextIntQuestion(ArrayList<Integer> result, int i)
    	{
    		int questionNumber = random.nextInt(7);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLmcQuestion question2 = new XMLmcQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");
        	
        	int randomInt = result.get(i) - random.nextInt(forLoop2.getiterationCount()) - 1;
        	int randInt2 = result.get(i) + random.nextInt(forLoop2.getiterationCount()) + 1;
        	int randInt3 = result.get(i) - random.nextInt(forLoop2.getiterationCount()) - 1;
        	int randInt4 = result.get(i) + random.nextInt(forLoop2.getiterationCount()) + 1;
        	
        	if(randomInt == randInt3)
        		randInt3--;
        	if(randInt2 == randInt4)
        		randInt4++;

        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What value will be printed next?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("The next value to be printed is " + result.get(i) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("The next value to be printed is " + randomInt + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 3:
	        		question3.setQuestionText("The next value to be printed is " + result.get(i) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 4:
	        		question3.setQuestionText("The next value to be printed is " + randInt2 + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 5:
	        		question2.setQuestionText("What value will be printed next?");
	        		question2.addChoice(result.get(i) + "");
	        		if(randomInt != result.get(i))
	        			question2.addChoice(randomInt + "");
	        		if(randInt2 != result.get(i))
	        			question2.addChoice(randInt2 + "" );
	        		if(randInt3 != result.get(i))
	        			question2.addChoice(randInt3 + "" );
	        		question2.addChoice("None of these values");
	        		question2.setAnswer(1);
	        		return question2;
	        	case 6:
	        		question2.setQuestionText("What value will be printed next?");
	        		question2.addChoice("None of these values");
	        		if(randomInt != result.get(i))
	        			question2.addChoice(randomInt + "");
	        		if(randInt2 != result.get(i))
	        			question2.addChoice(randInt2 + "" );
	        		if(randInt3 != result.get(i))
	        			question2.addChoice(randInt3 + "" );
	        		if(randInt4 != result.get(i))
	        			question2.addChoice(randInt4 + "" );
	        		question2.setAnswer(1);
	        		return question2;
	        	default: 
	            	question1.setQuestionText("What value will be printed next?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
        	}
    	}
    	
    	public question sumQuestion(int i)
    	{
    		int questionNumber = random.nextInt(6);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");

        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("At the end of the execution of this program, what will the sum of all the printed integers be?");
	            	question1.setAnswer(i + "");
	            	question1.setMustBeAsked(true);
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("At the end of the execution of this program, the sum of all the printed integers is " + i + ".");
	        		question3.setAnswer(true);
	        		question3.setMustBeAsked(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("At the end of the execution of this program, the sum of all the printed integers is " + (i-forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		question3.setMustBeAsked(true);
	        		return question3;
	        	case 3:
	        		question3.setQuestionText("At the end of the execution of this program, the sum of all the printed integers is " + i + ".");
	        		question3.setAnswer(true);
	        		question3.setMustBeAsked(true);
	        		return question3;
	        	case 4:
	        		question3.setQuestionText("At the end of the execution of this program, the sum of all the printed integers is " + (i+forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		question3.setMustBeAsked(true);
	        		return question3;
	        	case 5:
	        		question3.setQuestionText("At the end of the execution of this program, the sum of all the printed integers is " + i + ".");
	        		question3.setAnswer(true);
	        		question3.setMustBeAsked(true);
	        		return question3;
	        	default: 
	            	question1.setQuestionText("At the end of the execution of this program, what will the sum of all the printed integers be?");
	            	question1.setAnswer(i + "");
	            	question1.setMustBeAsked(true);
	            	return question1;
        	}
    	}
    	
    	public question futureInterationQuestions(ArrayList<Integer> result, int i)
    	{
    		int questionNumber = random.nextInt(12);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");

        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What integer will be printed 3 iterations from now?");
	            	question1.setAnswer(result.get(i+3) + "");
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("The integer that will be printed 3 iterations from now is " + result.get(i+3) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("The integer that will be printed 3 iterations from now is " + (result.get(i+3)-forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 3:
	        		question3.setQuestionText("The integer that will be printed 3 iterations from now is " + result.get(i+3) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 4:
	        		question3.setQuestionText("The integer that will be printed 3 iterations from now is " + (result.get(i+3)+forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 5:
	        		question3.setQuestionText("The integer that will be printed 3 iterations from now is " + result.get(i+3) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 6: 
	            	question1.setQuestionText("What will be printed 2 iterations from now?");
	            	question1.setAnswer(result.get(i+2) + "");
	            	return question1;
	        	case 7:
	        		question3.setQuestionText("The integer that will be printed 2 iterations from now is " + result.get(i+2) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 8:
	        		question3.setQuestionText("The integer that will be printed 2 iterations from now is " + (result.get(i+2)-forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 9:
	        		question3.setQuestionText("The integer that will be printed 2 iterations from now is " + result.get(i+2) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 10:
	        		question3.setQuestionText("The integer that will be printed 2 iterations from now is " + (result.get(i+2)+forLoop2.getiterationCount()) + ".");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 11:
	        		question3.setQuestionText("The integer that will be printed 2 iterations from now is " + result.get(i+2) + ".");
	        		question3.setAnswer(true);
	        		return question3;
	        	default: 
	            	question1.setQuestionText("What value will be printed next?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
        	}
    	}
    	
    	public question futureInterationQuestions(ArrayList<String> result, ArrayList<String> wrongResult, int i)
    	{
    		int questionNumber = random.nextInt(12);
    		
        	id++;
    		XMLfibQuestion question1 = new XMLfibQuestion(show, id + "");
        	id++;
        	XMLtfQuestion question3 = new XMLtfQuestion(show, id + "");

        	switch (questionNumber) {
	        	case 0: 
	            	question1.setQuestionText("What will the variable finalString be equal to 3 iterations from now?");
	            	question1.setAnswer(result.get(i+3) + "");
	            	return question1;
	        	case 1:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+3) + " 3 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 2:
	        		question3.setQuestionText("The variable finalString will be equal to " + wrongResult.get(i+3) + " 3 iterations from now.");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 3:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+3) + " 3 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 4:
	        		question3.setQuestionText("The variable finalString will be equal to " + (result.get(i+2)+forLoop1.getiterationCount()) + " 3 iterations from now.");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 5:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+3) + " 3 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 6: 
	            	question1.setQuestionText("What will the variable finalString be equal to 2 iterations from now?");
	            	question1.setAnswer(result.get(i+2) + "");
	            	return question1;
	        	case 7:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+2) + " 2 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 8:
	        		question3.setQuestionText("The variable finalString will be equal to " + wrongResult.get(i+3) + " 2 iterations from now.");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 9:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+2) + " 2 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	case 10:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+3) + " 2 iterations from now.");
	        		question3.setAnswer(false);
	        		return question3;
	        	case 11:
	        		question3.setQuestionText("The variable finalString will be equal to " + result.get(i+2) + " 2 iterations from now.");
	        		question3.setAnswer(true);
	        		return question3;
	        	default: 
	            	question1.setQuestionText("What will the variable finalString be equal to at the end of this iteration?");
	            	question1.setAnswer(result.get(i) + "");
	            	return question1;
        	}
    	}
    }//Inner Class - Questions
}// NestedForLoopsPrintLn