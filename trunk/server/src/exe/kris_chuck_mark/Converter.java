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

package exe.kris_chuck_mark;

import java.io.IOException;
import exe.*;
import java.io.*;

public class Converter {
  private GAIGSstack opStack;
  private GAIGSqueue inQueue, postQueue;
  private int qId = 0;
  GAIGSlabel currentToken;  // for status messages
  GAIGSarray cToken;         // the array of items
  GAIGSlabel tokenType;     // for status messages
  GAIGSarray tToken;         // the array of items  
  GAIGSlabel actionTaken;   // for status messages
  ShowFile show;
  char[] ansArr;
  int ansIdx = 0;
  int mcFlag = 2;
         
  String actionInitial = "Action Taken";
  String actionOperand = "Enqueue onto Postfix Queue";    
  String actionOpenParen = "Push ( onto Operator Stack";   
  String actionCloseParen = "Pop items from Operator Stack up to and including (. \n" +
  		"Enqueue operators onto Postfix Queue.";
  String actionEmptyInfix = "Pop all remaining items from Operator Stack. \n" +
  		"Enqueue operators onto Postfix Queue.";
  String actionHigherPrec = "Push onto Operator Stack (higher precedence).";
  String actionEmptyStack = "Push onto Operator Stack (empty stack).";
  String actionLowerPrec = "Push on Operator Stack after removing higher or equal\n" +
  		"precedence operators and enqueueing onto Postfix Queue.";
  private String inpt;
  String TITLE = "Infix to Postfix Conversion\n";
  int loopCnt = 0;
  

  public Converter(String str) throws IOException {
    inpt = str;
    ansArr = new char[inpt.length()];
    ansIdx = 0;
    TITLE = TITLE + inpt;   // no title
    opStack = new GAIGSstack("Operator stack", "#999999", 0.35, 0.4,0.65, 0.8,0.07);
    inQueue = new GAIGSqueue("Infix Queue", "#999999", 0, 0.4,.3, 0.8,0.07);
    postQueue = new GAIGSqueue("Postfix queue", "#999999", 0.7, 0.4,1, 0.8, 0.07);
//    currentToken = new GAIGSlabel( "currentToken",
//                         0, 0, 0.3, 0.30, 0.07);
    cToken = new GAIGSarray(1, false, "Current Token", 
                         "#999999", 0, 0, 0.3, 0.30, 0.07);                          
//    tokenType = new GAIGSlabel( "tokenType", 
//                         .35, -0, 0.65, 0.3, 0.07 );
    tToken = new GAIGSarray(4, false, "Token Type", 
                         "#999999", .3, -0, 0.65, 0.3, 0.07);
    tToken.set( "Operand", 0 );
    tToken.set( "Operator", 1 );
    tToken.set( "(", 2 );
    tToken.set( ")", 3 );
                                        
    actionTaken = new GAIGSlabel( "Action Taken",
                         .7,0,1, .3,0.07 );

            
    for (int i = 0; i < inpt.length(); i++) {
    	char ch = inpt.charAt(i);
    	inQueue.enqueue(ch);
    }
    show = new ShowFile("InfixToPostfix.sho");
  }

  public void doTrans() throws IOException {
  	XMLfibQuestion fib = new XMLfibQuestion(show, qId + "");
    qId++;
    fib.setQuestionText("What will be the size of the final postfix queue? (enter an integer)");
    int finSize = countNonParens();
    fib.setAnswer(finSize + "");
    show.writeSnap( TITLE, null, null, fib, inQueue, opStack, postQueue, 
                           cToken, tToken, tToken, actionTaken );
    
    
    
    while ( !inQueue.isEmpty() ) {
      char ch = ((Character)inQueue.dequeue()).charValue();
      cToken.set( ch, 0 );
      String ques1 = "Will the current token " + ch +" be placed on the operator stack?";
     
      XMLtfQuestion tf = new XMLtfQuestion(show, qId + "");
      qId++;
      tf.setQuestionText(ques1);
      tf.setAnswer(onOpStack(ch));
      
      String ques3 = "What action will be taken with the current token " + ch +"?";
     
      XMLmcQuestion mc = new XMLmcQuestion(show, qId + "");
      qId++;
      mc.setQuestionText(ques3);
      mc.addChoice("Enqueue onto postfix queue");
      mc.addChoice("Push immediately onto operator stack");
      mc.addChoice("Peek at operator on top of operator stack");
      
      XMLfibQuestion fib2 = new XMLfibQuestion(show, qId + "");
      qId++;
      String ques2 = "What will be the content of the postfix queue after processing the next token " + ch+"? (place a blank between elements)";
      fib2.setQuestionText(ques2);
      if ((loopCnt + 1) % 3 == 0) {
      		if ((loopCnt + 1) % 2 == 0)
      			show.writeSnap( TITLE, null, null, tf, inQueue, opStack, postQueue, 
                           cToken, tToken, tToken, actionTaken );
            else
      			show.writeSnap( TITLE, null, null, mc, inQueue, opStack, postQueue, 
                           cToken, tToken, tToken, actionTaken );
      }
      if (loopCnt % 5 == 0) 
      		show.writeSnap( TITLE, null, null, fib2, inQueue, opStack, postQueue, 
                           cToken, tToken, tToken, actionTaken );
      
      //cToken.set( ch, 0 );
      resetTokenTypeColor();
      resetPostQueueColor();
      resetOpStackColor();      
      switch (ch) {
        case '+': 
        case '-':
           gotOper(ch, 1);
           tToken.setColor(1,"blue");
           show.writeSnap( TITLE,  inQueue, opStack, postQueue, 
                               cToken, tToken, actionTaken );         
           break; //   (precedence 1)
        case '*': // it's * or /
        case '/':
           gotOper(ch, 2); // go pop operators
           tToken.setColor(1,"blue");           
           show.writeSnap( TITLE,  inQueue, opStack, postQueue, 
                               cToken, tToken, actionTaken );             
           break; //   (precedence 2)
        case '(': // it's a left paren
           opStack.push(ch,"yellow"); // push it
           mcFlag = 2;
           tToken.setColor(2,"yellow"); 
           actionTaken.setLabel(actionInitial + "\n" + actionOpenParen);          
           show.writeSnap( TITLE,  inQueue, opStack, postQueue, 
                               cToken, tToken, actionTaken );           
           break;
        case ')': // it's a right paren
           gotParen(ch); // go pop operators
           tToken.setColor(3,"red");  
           actionTaken.setLabel(actionInitial + "\n" + actionCloseParen);       
           show.writeSnap( TITLE,  inQueue, opStack, postQueue, 
                               cToken, tToken, actionTaken );           
           break;
        default: // must be an operand
           mcFlag = 1;
           postQueue.enqueue(ch,"green"); // write it to output
           ansArr[ansIdx] =ch;
           ansIdx++;
           tToken.setColor(0,"green");
           actionTaken.setLabel(actionInitial + "\n" + actionOperand);
           show.writeSnap( TITLE, inQueue, opStack, postQueue, 
                               cToken, tToken, actionTaken );           
           break;
      }
      if (loopCnt % 5 == 0) {
        String ansStr = "";
        for (int k = 0; k < ansIdx; k++) {
      	   ansStr = ansStr + ansArr[k];
      	if (k != ansIdx - 1)
      		ansStr = ansStr + " ";
        }
        System.out.println(ansStr);
        fib2.setAnswer(ansStr);
        //ansStr = "";
      }
      loopCnt++;
      mc.setAnswer(mcFlag);
    }
    resetTokenTypeColor();
    resetPostQueueColor();
    
    cToken.set( "null", 0 );
	cToken.setColor(0,"magenta");
	actionTaken.setLabel(actionInitial + "\n" + actionEmptyInfix);
    while (!opStack.isEmpty()) {
      char ch = ((Character)opStack.pop()).charValue();
	  postQueue.enqueue(ch);
    }
    //printOut()
    show.writeSnap( TITLE, inQueue, opStack, postQueue, 
                           cToken, tToken, actionTaken );
    show.close();
  }

  public int countNonParens() {
  	int cnt = 0;
  	for (int j=0; j<inpt.length(); j++) {
  		char ch = inpt.charAt(j);
  		if (ch != '(' && ch !=')')
  			cnt++;
  	}
  	return cnt;
  }
  
  
   

  public void resetTokenTypeColor() {
       for (int i = 0; i < 4; i++)
          tToken.setColor(i,"#999999");  
  }

  public void resetPostQueueColor() {
       for (int i = 0; i < postQueue.size(); i++)
          postQueue.setColor(i,"#999999");  
  }
  
  public void resetOpStackColor() {
       for (int i = 0; i < opStack.size(); i++)
          opStack.setColor(i,"#999999");  
  }
  
  public void gotOper(char opThis, int prec1) {
  	int flag = 0;		// to determine if loop through Op stack
   	actionTaken.setLabel(actionInitial + "\n" + actionEmptyStack);
    while (!opStack.isEmpty()) {
      mcFlag = 3;
      char opTop = ((Character)opStack.pop()).charValue();;
      if (opTop == '(') {
        opStack.push(opTop);
         actionTaken.setLabel(actionInitial + "\n" + actionHigherPrec);
        break;
      }// it's an operator
      else {// precedence of new op
        int prec2;
        if (opTop == '+' || opTop == '-')
          prec2 = 1;
        else
          prec2 = 2;        
        if (prec2 < prec1) // if prec of opTop less
        { //    than prec opThis
          opStack.push(opTop); // save newly-popped op
          if (flag == 0)
          	 actionTaken.setLabel(actionInitial + "\n" + actionHigherPrec);
          else  actionTaken.setLabel(actionInitial + "\n" + actionLowerPrec);
          break;
        } else
        {
          // prec of new not less
          postQueue.enqueue(opTop, "blue");
          ansArr[ansIdx] = opTop;
          ansIdx++;
          flag = 1;
         }
          
      }
    }
    if (flag == 1)
          	 actionTaken.setLabel(actionInitial + "\n" + actionLowerPrec);
    opStack.push(opThis, "blue");
  }

  public void gotParen(char ch){ 
    while (!opStack.isEmpty()) {
      char chx = ((Character)opStack.pop()).charValue();
      if (chx == '(') 
        break; 
      else {
        postQueue.enqueue(chx, "red"); 
        ansArr[ansIdx] = chx;
        ansIdx++;
      }
    }
  }
  
  public void printOut() {
  	while (!postQueue.isEmpty()) 
  		System.out.print(((Character)postQueue.dequeue()).charValue());
  	System.out.println();
 }

   private boolean onOpStack(char ch) {
	   if (ch == '+' || ch == '-' || ch =='*' || ch == '/' || ch=='(')
		   return true;
	   return false;
   }
   
  public static void main(String[] args) throws IOException {
    String inpt = "1+4/2-(3+5)*6";
    Converter theTrans = new Converter(inpt);
    theTrans.doTrans();

  }
  

} 
