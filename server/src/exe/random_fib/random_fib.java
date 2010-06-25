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


package exe.random_fib;

import java.io.*;
import java.util.Random;
import exe.*;

import java.net.*;
public class random_fib{

    static final String TITLE = "Random_Fibonacci";
    static GAIGSstack stack;
    static GAIGStree tree;
    static TreeNode treeNode;
    static int id = 0;
    
    static int questionCount = 6;
    static int counter = 0;
    static int toCompare = 0;
	static int toCompare1 = 0;
	static int toCompare2 = 0;
	static boolean flag = true;
   
    private static Random gen = new Random();
    private static Random gen1=new Random();
    public final static int f = gen.nextInt(3)+1;
    public final static int k = gen1.nextInt(3)+1;
    public final static int num = (gen.nextInt(3))+3;
    public final static int ranNum = (gen.nextInt(5));
    public final static int ranNum2 = (gen.nextInt(num+1))+1;
    static GAIGStext text = new GAIGStext(0.5,0,"This is Random fibonacci...");
    public static String calculation = "calculation = (";

    public static void main(String args[]) throws IOException
    {
        random_fib myrandFib=new random_fib();
        ShowFile show = new ShowFile(args[0]+".sho");
        stack = new GAIGSstack("Stack View","#999999", 0.1, 0.1, 0.55, 0.9,0.07);
        tree = new GAIGStree(true,"Tree View","#999999",0.6, 0.1, 0.99, 0.9,0.07);
        treeNode = new TreeNode(num+"");
        tree.setRoot(treeNode);

        myrandFib.printRand();
        
        XMLmcQuestion mc1 = new XMLmcQuestion(show, id + "");
        mc1.setQuestionText("How many times will the method rafib() be called through this example("+num+" being the initial value)?");
        id++;
        
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-15,-15,-15),stack,tree,text);
        
        int fibNum = raFib(num,show,treeNode,mc1);
        
        mc1.addChoice(counter-2+""); 
        mc1.addChoice(counter-1+"");
        mc1.addChoice(counter+"");
        mc1.addChoice(counter+1+"");
        mc1.addChoice(counter+2+""); 
        
        mc1.setAnswer(3);
        
        System.out.println("counter= "+counter);
        
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(9,-15,-15,-15),stack,tree,text);
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(10,-15,-15,-15),stack,tree,text);
        
        
        tree.getRoot().setValue(tree.getRoot().getValue()+"|"+fibNum);
        tree.getRoot().setHexColor("#FF0000");
        text.setColor("#FF0000");
        text.setText(" total is = "+fibNum);
        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-15,-15,-15),stack,tree,text);

        show.close();
    }
    public void printRand(){
    System.out.println("num= "+num);
    System.out.println("f= "+f);
    System.out.println("k= "+k);
    System.out.println("ranNum= "+ranNum);
    System.out.println("ranNum2= "+ranNum2);
    }
    public static int raFib(int j,ShowFile show,TreeNode treeNode,XMLmcQuestion mc1)
    {
        text.setText(calculation);
        if(j == num)
        	stack.push(j);
        else if(treeNode.isLeftChild())
        	stack.push(j+"|6");
        else
        	stack.push(j+"|7");
        stack.setColor(0,"#FF0000");
        treeNode.setHexColor("#FF0000");
        counter++;
        try{
	        show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(0,j,-15,-15),stack,tree,text);
	        }catch(IOException e)
	    {}
        stack.setColor(0,"#999999");
        treeNode.setHexColor("#999999");
	    try{
		    show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(1,j,-15,-15),stack,tree,text);
		    }catch(IOException e)
		{}
        XMLmcQuestion mc = new XMLmcQuestion(show, id + "");
        mc.setQuestionText("What will be the value of j on the next recursive call?");
           
        mc.addChoice("4"); 
        mc.addChoice("3");
        mc.addChoice("2");
        mc.addChoice("1");
        mc.addChoice("None of them");
       
        id++;
        
        XMLfibQuestion fib2 = new XMLfibQuestion(show, id + "");
        fib2.setQuestionText("What value will be assigned to the current temp variable?");
        id++;
        
        XMLfibQuestion fib3 = new XMLfibQuestion(show, id + "");
        fib3.setQuestionText("What line number are we about to return to in the pseudo-code?");
        id++;

        if (j <= 0)
        {
            stack.pop();
            treeNode.setHexColor("#999999");
            calculation += "0)";
            text.setText(calculation);
            text.setColor("#FF0000");
            try{
            show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(2,j,-15,-15),stack,tree,text);
            }catch(IOException e)
            {}
            text.setColor("#000000");
            return 0;
        }
        else if (j == 1)
        {
        	if(flag==true)
        	{
                try{
                    show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,-15,-15,-15),mc1,stack,tree,text);
                    }catch(IOException e)
                    {}
                id++;
        	}
        	flag = false;
            try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(3,j,-15,-15),stack,tree,text);
                }catch(IOException e)
                {}
                treeNode.setHexColor("#999999");
                calculation += "1)";
                text.setText(calculation);
                text.setColor("#FF0000");
                try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(4,j,-15,-15),fib3,stack,tree,text);
                }catch(IOException e)
                {}
                id++;
                if(treeNode.isLeftChild())
                	fib3.setAnswer(6+"");
                else
                	fib3.setAnswer(7+"");                
                id++;
                text.setColor("#000000");
                
                stack.pop();
                fib3 = new XMLfibQuestion(show, id + "");
                fib3.setQuestionText("What line number are we about to return to in the pseudo-code?");
               
                return 1;
        }
        else
        {
            try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(3,j,-15,-15),stack,tree,text);
                }catch(IOException e)
                {}
            try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(5,j,-15,-15),stack,tree,text);
                }catch(IOException e)
                {}

                text.setColor("#FF0000");
                if(treeNode.isRightChild())
                {
                	if(j>1)
                	{
	                	try{
	    	                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(6,j,-15,-15),stack,tree,text);
	    	                }catch(IOException e)
	    	                {}
                	}
                	else
                	{
	                	try{
	    	                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(7,j,-15,-15),stack,tree,text);
	    	                }catch(IOException e)
	    	                {}
                	}
                }
                else
                {
                	try{
    	                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(6,j,-15,-15),stack,tree,text);
    	                }catch(IOException e)
    	                {}
                }

                treeNode.insertLeftChild(new TreeNode((j-1)+""));
                
                int temp1 = 0;
                if(ranNum == 1)
                {
                	calculation += "(" +f+"+";
                	temp1 = (f+(raFib(j-1,show,treeNode.getLeftChild(),mc1)));
                }
                else if(ranNum == 2)
                {
                	calculation += "(" +f+"*";
                	temp1 = (f*(raFib(j-1,show,treeNode.getLeftChild(),mc1)));
                }
                else if(ranNum == 3)
                {
                	calculation += "(" +k+"+";
                	temp1 = (k+(raFib(j-1,show,treeNode.getLeftChild(),mc1)));
                }
                else if(ranNum == 4)
                {
                	calculation += "(" +k+"-";
                	temp1 = (k-(raFib(j-1,show,treeNode.getLeftChild(),mc1)));
                }
                else
                {
                	calculation += "(" +f+"*";
                	temp1 = (f*(raFib(j-1,show,treeNode.getLeftChild(),mc1)));
                }
               
                try{
                    show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(-1,j,-15,-15),fib2,stack,tree,text);
                }catch(IOException e)
                {}
                id++;  
                
                fib2.setAnswer(temp1+"");
                id++;
                fib2 = new XMLfibQuestion(show,id+"");
                fib2.setQuestionText("What value will be assigned to the current temp variable?");
                
                treeNode.getLeftChild().setValue(treeNode.getLeftChild().getValue()+"|"+temp1);
            
                text.setText(calculation);
                text.setColor("#FF0000");
                treeNode.getLeftChild().setHexColor("#FF0000");

                try{
                	show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(6,j,temp1,-15),stack,tree,text);
                }catch(IOException e)
                        {}
            treeNode.getLeftChild().setHexColor("#999999");
            text.setColor("#000000");
            
            try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(7,j,temp1,-15),mc,stack,tree,text);
                }catch(IOException e)
                {}
            id++;

            treeNode.insertRightChild(new TreeNode((j-ranNum2)+""));
            
            if((j-ranNum2) == 4)
            	mc.setAnswer(1);
            else if((j-ranNum2) == 3)
            	mc.setAnswer(2);
            else if((j-ranNum2) == 2)
            	mc.setAnswer(3);
            else if((j-ranNum2) == 1)
            	mc.setAnswer(4);
            else 
            	mc.setAnswer(5); 
            id++;
            mc = new XMLmcQuestion(show, id + "");
            mc.setQuestionText("What will be the value of j on the next recursive call?");
            mc.addChoice("4"); 
            mc.addChoice("3");
            mc.addChoice("2");
            mc.addChoice("1");
            mc.addChoice("None of them");

                int temp2 = 0;
                if(ranNum == 1)
                {
                	calculation += "+"+k+"*";
                	temp2 = (k*(raFib(j-ranNum2,show,treeNode.getRightChild(),mc1)));
                }
                else if(ranNum == 2)
                {
                	calculation += "+"+k+"-";
                	temp2 = (k-(raFib(j-ranNum2,show,treeNode.getRightChild(),mc1)));
                }
                else if(ranNum == 3)
                {
                	calculation += "+"+f+"*";
                	temp2 = (f*(raFib(j-ranNum2,show,treeNode.getRightChild(),mc1)));
                }
                else if(ranNum == 4)
                {
                	calculation += "+"+k+"+";
                	temp2 = (k+(raFib(j-ranNum2,show,treeNode.getRightChild(),mc1)));
                }
                else
                {
                	calculation += "+"+k+"*";
                	temp2 = (k*(raFib(j-ranNum2,show,treeNode.getRightChild(),mc1)));
                }

                treeNode.getRightChild().setValue(treeNode.getRightChild().getValue()+"|"+temp2);
                
                treeNode.getRightChild().setHexColor("#FF0000");
                try{
                	show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(7,j,temp1,temp2),stack,tree,text);
                        }catch(IOException e)
                        {}
                treeNode.getLeftChild().setHexColor("#FF0000");      
                
                try{
                show.writeSnap(TITLE,0.07,doc_uri(),pseudo_uri(8,j,temp1,temp2),stack,tree,text);
                }catch(IOException e)
                {}
                stack.pop();
                
                treeNode.getLeftChild().setHexColor("#999999");
                treeNode.getRightChild().setHexColor("#999999");
                return temp1+temp2;
        }
    }
    private static String doc_uri()
    {
    	String content = "<html><head><title>Randon_Fibonacci</title></head><body><h1>Random Fibonacci</h1>The famous Fibonacci number sequence with our own random variation added!</body></html>";
    	URI uri = null;
    	try
    	{
    		uri = new URI("str",content,"");
    	}catch(java.net.URISyntaxException e)
    	{}
    	return uri.toASCIIString();
    	
    }
    private static String pseudo_uri(int line_num,int j,int temp1,int temp2)
    {
    	String lessThan = "%26lt;";
    	String lessThanInUri = "%2526lt;";
    	String theF = f+"";
    	String theK = k+"";
    	String theRanNum2 = ranNum2+"";
    	
    	String pseudo [] = {
    			"<pre>0. public static int raFib(int j) {</pre>",
    			"<pre>1.	if (j " +lessThan+ "= 0)</pre>",
    			"<pre>2.		return 0;</pre>",
    			"<pre>3.	else if(j == 1)</pre>",
    			"<pre>4.		return 1;</pre>",
    			"<pre>5.	else{</pre>",
    			"<pre></pre>",
    			"<pre></pre>",
    			"<pre>8.	 	return temp1 + temp2;</pre>",
    			"<pre>9.	}</pre>",
    			"<pre>10. }</pre>"
    	};
        if(ranNum == 1)
        {
        	pseudo[6] = "<pre>6.		temp1 = ("+theF+"+(raFib(j-1)));</pre>";
        	pseudo[7] = "<pre>7.		temp2 = ("+theK+"*(raFib(j-"+theRanNum2+")));</pre>";
        }
        else if(ranNum == 2)
        {
        	pseudo[6] = "<pre>6.		temp1 = ("+theF+"*(raFib(j-1)));</pre>";
        	pseudo[7] = "<pre>7.		temp2 = ("+theK+"-(raFib(j-"+theRanNum2+")));</pre>";
        }	
        else if(ranNum == 3)
        {
        	pseudo[6] = "<pre>6.		temp1 = ("+theK+"+(raFib(j-1)));</pre>";
        	pseudo[7] = "<pre>7.		temp2 = ("+theF+"*(raFib(j-"+theRanNum2+")));</pre>";
        }
        else if(ranNum == 4)
        {
        	pseudo[6] = "<pre>6.		temp1 = ("+theK+"-(raFib(j-1)));</pre>";
        	pseudo[7] = "<pre>7.		temp2 = ("+theK+"+(raFib(j-"+theRanNum2+")));</pre>";
        }
        else
        {
        	pseudo[6] = "<pre>6.		temp1 = ("+theF+"*(raFib(j-1)));</pre>";
        	pseudo[7] = "<pre>7.		temp2 = ("+theK+"*(raFib(j-"+theRanNum2+")));</pre>";
        }   
    	String variable [] = {
    			"<pre>Variables: </pre>",
    			"j = "+ j+ "<pre></pre>",
    			"temp1 = "+ temp1+"<pre></pre>",
    			"temp2 = "+ temp2+"<pre></pre>",
    	};
    	if(j == -15)
    		variable[1] = "<pre>j = - </pre>";
    	if(temp1 == -15)
    		variable[2] = "<pre>temp1 = - </pre>";
    	if(temp2 == -15)
    		variable[3] = "<pre>temp2 = - </pre>";
    	String content = "<html><head><title>Random_Fibonacci</title></head><body><h1>Random Fibonacci</h1>";
    	for(int i = 0;i < pseudo.length;i++)
    	{
    		if(i == line_num)
    			content = content + "<div style=\"color:red\">" + pseudo[i] + "</div>";
    		else
    			content = content + pseudo[i];
    	}
    	content += "<div style=\"color:blue\">" + variable[0] + "</div>";
    	if(toCompare == j || j == -15)
    		content += variable[1];
    	else
    		content += "<div style=\"color:red\">" + variable[1] + "</div>";
    	if(toCompare1 == temp1 || temp1 == -15)
    		content += variable[2];
    	else
    		content += "<div style=\"color:red\">" + variable[2] + "</div>";
    	if(toCompare2 == temp2 || temp2 == -15)
    		content += variable[3];
    	else
    		content += "<div style=\"color:red\">" + variable[3] + "</div>";
    	toCompare1 = temp1;
    	toCompare2 = temp2;
    	toCompare = j;
    	
    	content = content + "</body></html>";
    	URI uri = null;
    	try
    	{
    		uri = new URI("str",content,"");
    	}catch(java.net.URISyntaxException e)
    	{}
    		
    	String ret_str = uri.toASCIIString().replaceAll(lessThanInUri, lessThan);
    	return ret_str;
    	
    }

}