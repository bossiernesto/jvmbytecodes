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

package exe.stringManip;
import java.io.*;
import java.util.*;
import java.net.URI;
import java.util.Random;
import exe.*;
public class stringManip{
	private static String Title="String Manipulation with Recursion";
	private int id=0;
	private ArrayList<String> charsCopied = new ArrayList<String>();
	private ArrayList<String> wrong=new ArrayList<String>();
	
	public GAIGSarray fill(int len, String in){
		GAIGSarray ar=new GAIGSarray(1, len, "", "#999999", 0.1, 0.4, 0.6, 1.2,0.09);
		for(int i=0; i<1; i++){
			for(int j=0; j<len; j++){
				ar.set(in.substring(j, j+1),i, j);
			}
		}
		return ar;
	}  
	public String rands(int inLen){
		String inp="";
		Random gen = new Random();

		String[] ranAr={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(int i=0;i<inLen;i++){
			int ran=gen.nextInt(26);
			inp+=ranAr[ran];
		}
		return inp;
		}

    public void stringManipulation(GAIGSarray array, GAIGSarray newArray,GAIGSstack stack,GAIGStext text, ShowFile show, int len, int start, int jump, int end, int count,XMLmsQuestion hard){
    	String temp="";
    	int temp2=0;
    	
        
    	XMLfibQuestion fib2 = new XMLfibQuestion(show, id + "");
        fib2.setQuestionText("What will be the next value added to the newArray?");
        id++;
   
    
        XMLmcQuestion mc1 = new XMLmcQuestion(show, id + "");
        mc1.setQuestionText("How large will the call stack grow during the program's execution?");
        id++;
        
        XMLmcQuestion mc2 = new XMLmcQuestion(show, id + "");
        mc2.setQuestionText("What will the value of start be at the beginning of funct's next call?");
        id++;
                
        XMLtfQuestion tf2= new XMLtfQuestion(show, id+"");
        tf2.setQuestionText("Is the local variable temp and its value stored on the call stack");
        id++;
                
        hard.setQuestionText("Of the following options which letters will never be added to the newArray, during the program's execution?");
        id++;

                
        tf2.setAnswer(true);
        id++;
        
        mc1.addChoice(end-2+""); 
        mc1.addChoice(end-1+"");
        mc1.addChoice(end+"");
        mc1.addChoice(end+1+"");
        mc1.addChoice(end+2+""); 
        
        mc1.setAnswer(4);


        if(count==5){
            mc1.addChoice(start-3+""); 
            mc1.addChoice(start-1+"");
            mc1.addChoice(start+"");
            mc1.addChoice(start+1+"");
            mc1.addChoice(start+jump+""); 
            mc1.setAnswer(3);
        }
    	stack.push("funct(),V");
    	stack.setColor(0,"#FF0000");
    	System.out.println("length= "+len+ " start= "+ start+" jump= "+jump+ " end= "+end+" count= "+count);
    	text=new GAIGStext(0.5,0,"V=(len="+len+ ", start="+ start+", jump="+jump+ ", end="+end+", count="+count+", temp="+temp+")");
    	text.setColor("#0000FF");
    	text.setFontsize(0.06); 
		try{
	        show.writeSnap(Title,0.08,null,pseudo_uri(0), array, newArray,stack,text);
	 	}
	 	catch(IOException e){
     	}
	 	text.setColor("#000000");
    	stack.setColor(0,"#999999");
		try{
	        show.writeSnap(Title,0.08,null,pseudo_uri(2), array, newArray,stack,text);
	 	}
	 	catch(IOException e){
     	}
        if(count%2==0 && count>0){
        	temp2=start;
        	if(temp2>(len-1)){
    			temp2=(temp2%len);
        	}
    		temp2=temp2+jump;
    		System.out.println(temp2);
    		
            mc2.addChoice(temp2-jump+6+""); 
            mc2.addChoice(temp2+jump+"");
            mc2.addChoice(temp2+"");
            mc2.addChoice(temp2-jump-3+"");
            mc2.addChoice(temp2+jump+1+""); 
            
            mc2.setAnswer(3);
            try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(2),mc2, array, newArray,stack,text);
    	 	}
    	 	catch(IOException e){
         	}
    			
        	
        }
		try{
	        show.writeSnap(Title,0.08,null,pseudo_uri(3), array, newArray,stack,text);
	 	}
	 	catch(IOException e){
     	}
    	if(count >=end){
//    	 	stack.pop();
    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(5), array, newArray,stack,text);
    	 	}
    	 	catch(IOException e){
	     	}
    	 	stack.pop();
    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(-5), array, newArray,stack,text);
    	 	}
    	 	catch(IOException e){
	     	}
    	 	while(!stack.isEmpty()){
//    	 		stack.pop();
        		try{
        	        show.writeSnap(Title,0.08,null,pseudo_uri(13), array, newArray,stack);
        	 	}
        	 	catch(IOException e){
    	     	}
        	 	stack.pop();
        		try{
        	        show.writeSnap(Title,0.08,null,pseudo_uri(-13), array, newArray,stack);
        	 	}
        	 	catch(IOException e){
    	     	}
    	 	}
    	 	for(int i=0; i<1; i++){
    			for(int j=0; j<end; j++){
    				newArray.setColor(i,j,"#236B8E");
    			}
    		}
    	 	
    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(-1), array, newArray,stack);
    	 	}
    	 	catch(IOException e){
	     	}
    		return;
    	}
    	else{

    		try{
    			show.writeSnap(Title,0.08,null,pseudo_uri(7), array, newArray,stack,text);
    		}
    		
    		catch(IOException e){
    		}
    		if(count==1){
        		try{
        			show.writeSnap(Title,0.08,null,pseudo_uri(-1), tf2,array, newArray,stack,text);
        		}
        		
        		catch(IOException e){
        		}
        		id++;
    		}
    		if(count==2){
           		try{
        			show.writeSnap(Title,0.08,null,pseudo_uri(-1), hard,array, newArray,stack,text);
        		}
        		catch(IOException e){
        		}
        		id++;	
    		}
    		if(count==3){
    			try{
        			show.writeSnap(Title,0.08,null,pseudo_uri(-1), mc1,array, newArray,stack,text);
        		}
        		
        		catch(IOException e){
        		}
        		id++;
       		}
/*    		if(count%2==1){
    			try{
    				show.writeSnap(Title,0.08,null,pseudo_uri(-1), fib2,array, newArray,stack,text);
    			}catch(IOException e){
    			}
    			id++;
    		}*/

    		text=new GAIGStext(0.5,0,"V=(len="+len+ ", start="+ start+", jump="+jump+ ", end="+end+", count="+count+", temp="+temp+")");
	    	text.setColor("#0000FF");
	    	text.setFontsize(0.06); 

    	 	text.setColor("#000000");
    		/*if(charsCopied.contains(temp)==false){
    			charsCopied.add(temp);
    		}
    		fib2.setAnswer(temp.toLowerCase());
    		id++;
    		
    		fib2 = new XMLfibQuestion(show, id + "");
            fib2.setQuestionText("What will be the next value added to the newArray?");
            */
			text=new GAIGStext(0.5,0,"V=(len="+len+ ", start= "+ start+", jump="+jump+ ", end="+end+", count="+count+", temp="+temp+")");
	    	text.setColor("#0000FF");
	    	text.setFontsize(0.06);
    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(9), array, newArray,stack,text);
    	 	}
    	 	catch(IOException e){
	     	}
    		if(count%2==1){
    			try{
    				show.writeSnap(Title,0.08,null,pseudo_uri(9), fib2,array, newArray,stack,text);
    			}catch(IOException e){
    			}
    			id++;
    		}
    		temp=(array.get(0,start=start%len)).toString();
    		array.setColor(0,start,"#FF0000");    		
    		if(charsCopied.contains(temp)==false){
    			charsCopied.add(temp);
    		}
    		fib2.setAnswer(temp.toLowerCase());
    		id++;
    		
    		fib2 = new XMLfibQuestion(show, id + "");
            fib2.setQuestionText("What will be the next value added to the newArray?");


	    	

    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(10), array, newArray,stack,text);
    	 	}
    	 	catch(IOException e){
	     	}
            text=new GAIGStext(0.5,0,"V=(len="+len+ ", start= "+ start+", jump="+jump+ ",end="+end+", count="+count+", temp="+temp+")");
 		    text.setColor("#0000FF");
            text.setFontsize(0.06); 
    	 	text.setColor("#000000");
    	 	array.setColor(0,start,"#999999");
    		newArray.set(temp, 0, count);
    		newArray.setColor(0,count,"#FF0000");
    		try{
    	        show.writeSnap(Title,0.08,null,pseudo_uri(11), array, newArray,stack,text);

    	 	}
    	 	catch(IOException e){
	     	}
    	 	newArray.setColor(0,count,"#999999");
    	 	try{
		        show.writeSnap(Title,0.08,null,pseudo_uri(12), array, newArray,stack,text);
        
		 	}
		 	catch(IOException e){
	     	}
    	 	stringManipulation(array, newArray,stack, text,show, len,start+jump,jump,end,count+1,hard);
    	 	text=new GAIGStext(0.5,0,"");
    	 	return;
    	}	
    }
	public static void main(String[] args)throws IOException{
		stringManip cl=new stringManip();//instance of class
		Random gen = new Random();
		int inLen= gen.nextInt(5)+5;//length of input
		String in=cl.rands(inLen);//method that generates values for array
		int len=in.length();
		int start;
		int jump;
		int end=gen.nextInt(4)+5;//length of newArray
		int count=0;//counter variable
		GAIGStext text=new GAIGStext(0.5,0,"V=");//declaration and creation of text
    	text.setColor("#0000FF");//set text to blue
    	text.setFontsize(0.06);
		GAIGSarray arr;//original string in array
		GAIGSarray newArray= new GAIGSarray(1, end, "", "#999999",0.1, 0.1, 0.5, 0.9,0.09);
		GAIGSstack stack=new GAIGSstack("","#999999",0.85, 0.1, 1.3, 1.1,0.07);
        ShowFile show = new ShowFile(args[0]+".sho");
        XMLmsQuestion hard=new XMLmsQuestion(show,cl.id+"");;
        arr=cl.fill(len, in);//put input into an array one character at a time
        show.writeSnap(Title,0.08,null,pseudo_uri(-1), arr, newArray,stack,text);
		cl.stringManipulation(arr, newArray,stack, text,show, len,  start=gen.nextInt(len-1),jump=gen.nextInt(len-2)+1,end, count,hard);//calls the recursive method stringManipulation
		int size=cl.charsCopied.size();
		int none=0;
		
		for(int j=0; j<len; j++){
    		if(cl.wrong.contains(arr.get(0,j))==false  && (cl.charsCopied.contains(arr.get(0,j))==false)){
    			cl.wrong.add(arr.get(0,j).toString());
    		}
		}
		for(int k=0; k<cl.wrong.size(); k++){
				hard.addChoice(cl.wrong.get(k));
				hard.setAnswer(k+1);
				
		}
		for(int i=0; i<size; i++){
			hard.addChoice(cl.charsCopied.get(i));
			System.out.println(cl.charsCopied.get(i));
		}
		hard.addChoice("None");
		if(cl.wrong.isEmpty()){
			hard.setAnswer(size+1);
		}
		hard.shuffle();

		show.close(); //ends array.sho 
	}
    private static String pseudo_uri(int line_num)
    {
    	
    	String pseudo [] = {
    			      "0.    public void funct(array,newArray,len,start,jump,end,count)",
    			      "1.    {",
    			      "2.    temp='';",	
    			      "3.        if (count>=end)//base case",
    			      "4.        {",
    			      "5.            return;",
    			      "6.       }",
    			      "7.       else//recursive case",
    			      "8.       {",
    			      "9.            start=(start mod len)",
	    		      "10.           temp=array[start];",
    			      "11.           newArray[count]=temp;",
    			      "12.           funct(array,newArray,len,start+jump,jump,end,count+1);",
    			      "13.           return;",
    			      "14.       }",
    			      "15.   }"
    	};
    	String content = "<html><head><title>StringManipulation</title></head><body><pre>";
    	for(int i = 0;i < pseudo.length;i++)
    	{
    		if(i == line_num)
    			content = content + "<span style=\"color:red\">" + pseudo[i] + "</span>\n";
    		else
    			content = content + pseudo[i]+"\n";
    	}
    	content = content + "</pre></body></html>";
    	URI uri = null;
    	try
    	{
    		uri = new URI("str",content,"");
    	}catch(java.net.URISyntaxException e)
    	{}
    		
    	String ret_str = uri.toASCIIString();
    	return ret_str;
    	
    }
	  
}
