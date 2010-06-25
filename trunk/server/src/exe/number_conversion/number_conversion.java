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


package exe.number_conversion;

import java.io.*;
import java.util.Random;
import exe.*;

import java.net.*;

public class number_conversion{

    static String TITLE = "";
    static GAIGSstack stack;
    static int id = 0;
    static int decimal_num = 0;
    static int counter = 0;
    static String s = "";
    static String ss = "";
    static Random gen = new Random();
    static GAIGStext new_number = new GAIGStext(0,0.05);
    static GAIGStext old_number = new GAIGStext(0,0.30);
    static GAIGStext the_base  = new GAIGStext(0,0.40);
    
    static String string_old_number = "num: ";
    static String string_new_number = "new_number: ";
    

    public static void main(String args[]) throws IOException
    {
        ShowFile show = new ShowFile(args[0]+".sho");
        stack = new GAIGSstack("Stack View","#999999", 0.6, 0.15, 1, 0.75,0.10);
	decimal_num = gen.nextInt(400)+1;
	int base_number = gen.nextInt(8)+2;
	int random = gen.nextInt(6)+2;
	TITLE = "The Base Conversion of Decimal Number "+decimal_num;
		
	System.out.println(decimal_num+" and the base is: "+base_number);
	string_old_number += decimal_num; 
	old_number.setText(string_old_number);
	old_number.setFontsize(0.06);
	the_base.setText("the base is: "+base_number);
	the_base.setFontsize(0.06);
	new_number.setText(string_new_number); 
	new_number.setFontsize(0.09);
	
	new_number.setHalign(GAIGStext.HLEFT);
		
	XMLtfQuestion tf = new XMLtfQuestion(show,id + "");
	tf.setQuestionText("Will the method decimalTo() be called a total of "+random+" times?(This is a " +
			"hard question, because it requires pre-evaluation of the program to get to the answer, so if" +
			" it is the first time you see the visualization, feel free to skip it)");
	id++;
        XMLmcQuestion mc1 = new XMLmcQuestion(show, id + "");
        mc1.setQuestionText("Of the total number of times the method decimalTo() gets called, how many " +
			    "times will it be evaluated to else{}?");
		
		
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-1,-1),stack,old_number,new_number,the_base);
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-1,-1),tf,stack,old_number,new_number,the_base);
        id++;
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-1,-1),mc1,stack,old_number,new_number,the_base);
        id++;
        
        String result = decimalTo(decimal_num,base_number,show);
        
        mc1.addChoice(counter-2+""); 
        mc1.addChoice(counter-1+"");
        mc1.addChoice(counter+"");
        mc1.addChoice(counter+1+"");
        mc1.addChoice(counter+2+""); 
        
        mc1.setAnswer(2);
        
        System.out.println("random is: "+ random);
        System.out.println("counter is: "+ counter);
        if(random == counter)
	    tf.setAnswer(true);
        else
	    tf.setAnswer(false);
        
        show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(7,0,base_number),stack,stack,old_number,new_number,the_base);
        show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(8,0,base_number),stack,stack,old_number,new_number,the_base);
        
        string_new_number = result+ " is the new number of base "+base_number;
        new_number.setText(string_new_number);
        new_number.setFontsize(0.07);
        new_number.setColor("#7D2252");

        show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(-1,0,base_number),stack,stack,old_number,new_number,the_base);

        show.close();
    }
    public static String decimalTo(int num,int base,ShowFile show)
    {
	XMLfibQuestion fib = new XMLfibQuestion(show, id + "");
	fib.setQuestionText("What will be the value of num on the next recursive call?");
	id++;
        XMLmcQuestion mc = new XMLmcQuestion(show, id + "");
        mc.setQuestionText("When we return, what value will be added to the new_number variable?");
           
        mc.addChoice("0 or 9"); 
        mc.addChoice("1 or 8");
        mc.addChoice("2 or 7");
        mc.addChoice("3 or 6");
        mc.addChoice("4 or 5");      
        id++;
		
		
	counter++;
        stack.push(num+" | "+base);
	string_old_number = "num: " + num;
	old_number.setText(string_old_number);
		
    	old_number.setColor("#FF0000");
	stack.setColor(0,"#FF0000");
        try{
            show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(0,num,base),stack,old_number,new_number,the_base);
	}catch(IOException e)
	    {}
        old_number.setColor("#000000");
        stack.setColor(0,"#999999");
        try{
            show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(1,num,base),stack,old_number,new_number,the_base);
	}catch(IOException e)
	    {}
        if(num == 0)
	    {
		stack.setColor(0,"#347C17");
	    	string_new_number += "'"+ss+"'"+"+";
	    	new_number.setText(string_new_number);
	    	new_number.setColor("#347C17");
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(2,num,base),stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}
		stack.pop();

		if(stack.size() > 0)
		    stack.setColor(0,"#999999");
		new_number.setColor("#000000");
	        return "";
	    }
	else
	    {
	        try{
	            show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(3,num,base),stack,old_number,new_number,the_base);
		}catch(IOException e)
		    {}
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(4,num,base),stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(-1,num,base),fib,stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}                
                
		decimalTo(num/base,base,show);
            
		fib.setAnswer(num/base + "");
		id++;
    		fib = new XMLfibQuestion(show, id + "");
    		fib.setQuestionText("What will be the value of num on the next recursive call?");
			
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(5,num,base),stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}
    		
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(-1,num,base),mc,stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}   
		if(num%base == 0 || num%base == 9 )    
		    mc.setAnswer(1);
		else if(num%base == 1 || num%base == 8)
		    mc.setAnswer(2);
		else if(num%base == 2 || num%base == 7)
		    mc.setAnswer(3);
		else if(num%base == 3 || num%base == 6)
		    mc.setAnswer(4);
		else
		    mc.setAnswer(5);
		id++;
		mc = new XMLmcQuestion(show, id + "");
		mc.setQuestionText("When we return, what value will be added to the new_number variable?");
               
        mc.addChoice("0 or 9"); 
        mc.addChoice("1 or 8");
        mc.addChoice("2 or 7");
        mc.addChoice("3 or 6");
        mc.addChoice("4 or 5");  
                
    		s += Integer.toString(num%base);
		ss = Integer.toString(num%base);
			
		if(num == decimal_num)
		    string_new_number += "'"+ss+"'";
		else
		    string_new_number += "'"+ss+"'"+"+";
	    	
	    	new_number.setText(string_new_number);
		if(stack.size() > 0)
		    stack.setColor(0,"#347C17");
	    	new_number.setColor("#347C17");
		try{
		    show.writeSnap(TITLE, 0.07,doc_uri(),pseudo_uri(6,num,base),stack,old_number,new_number,the_base);
                }catch(IOException e)
		    {}
                
		stack.pop();  
                
		if(stack.size() > 0)
		    stack.setColor(0,"#999999");
		new_number.setColor("#000000");
		System.out.println(s);
		return s;
	    }
    }
    private static String doc_uri()
    {
    	String content = "<html><head><title>Base_Conversion</title></head><body><h1>Base Conversion" +
	    "</h1>This visualization is designed to help students learn about recursion by showing " +
	    "an example of it: convering a randomly generated decimal number from 1 to 400 to a new number " +
	    "of base 2-9(also randomly generated).</body></html>";
    	URI uri = null;
    	try
	    {
    		uri = new URI("str",content,"");
	    }catch(java.net.URISyntaxException e)
	    {}
    	return uri.toASCIIString();
    	
    }
    private static String pseudo_uri(int line_num,int num,int base)
    {	
    	String pseudo [] = {
	    "<pre>0. public static String decimalTo(num, base) {</pre>",
	    "<pre>1.	if (num == 0)</pre>",
	    "<pre>2.		return empty string;</pre>",
	    "<pre>3.	else{</pre>",
	    "<pre>4.		decimalTo(num/base, base);</pre>",
	    "<pre>5.		new_number += Integer.toString(num%base);</pre>",
	    "<pre>6.		return new_number;</pre>",
	    "<pre>7.	}</pre>",
	    "<pre>8. }</pre>",
    	};
    	String variable [] = {
	    "<pre>Variables: </pre>",
	    "num = "+ num+"<pre></pre>",
	    "base = "+ base
    	};
    	if(num < 0)
	    variable[1] = "<pre>num = - </pre>";
    	if(base < 0)
	    variable[2] = "<pre>base = -</pre>";
    	
    	String content = "<html><head><title>Base_Conversion</title></head><body><h1>Base Conversion</h1>";
    	for(int i = 0;i < pseudo.length;i++)
	    {
    		if(i == line_num)
		    content = content + "<div style=\"color:red\">" + pseudo[i] + "</div>";
    		else
		    content = content + pseudo[i];
	    }
    	
    	content += "<div style=\"color:blue\">" + variable[0] + "</div>";
    	content += variable[1];
    	content += variable[2];
    	
    	content = content + "</body></html>";
    	URI uri = null;
    	try
	    {
    		uri = new URI("str",content,"");
	    }catch(java.net.URISyntaxException e)
	    {}
    	return uri.toASCIIString();
    	
    }
    
}