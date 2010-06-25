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

 


/**
 * Write a description of class dynamichashing here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

 package exe.schmitz_dynamichash;
 
import java.util.*;
import java.io.*;
import java.lang.Math;
import exe.*;


public class dynamichashing
{
    // instance variables - replace the example below with your own

public static void main( String[] args ) {
    dynamichashing cool = new dynamichashing(args);


}
    /**
     * Constructor for objects of class dynamichashing
     * 

     * 
     * 
     */

    public dynamichashing(String [ ] args)
    {
        // initialise instance variables
        try {
            runy(args);
        } catch(IOException ioex) { }
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */

        String[] thecolors = new String[15]; 
        boolean[] usedcolor = new boolean[15]; 
        int randomquestion = 0;
public void runy(String [ ] args) throws IOException
    {
        //usage:  main(pathname for sho file, percent chance of asking each question, "random" or list of integers to add and remove
        if (args.length <= 1) {
            System.out.println("Correct usage: main(pathname, percent chance of each question asked, keyword \"random\" or list of integers to add or remove");
            System.out.println("Example: main(\"C:\\dynamichashing.sho\", \"50\", \"random\")");
          System.out.println("Example: main(\"C:\\dynamichashing.sho\", \"75\",  \"10\", \"20\", \"30\", \"d20\")");            
            return;
        }
        
        randomquestion = Integer.parseInt(args[1]);
 GAIGStext textthing = null; 
 //try {


 
     if ((args[2].toUpperCase()).equals("RANDOM")) {
            Random randomGenerator = new Random();       
            String[] tempargs = new String[17];
            tempargs[0] = args[0];
            tempargs[1] = args[1];
            tempargs[2] = randomGenerator.nextInt(31-0+1) +0 + "";
            tempargs[3] = randomGenerator.nextInt(159-128+1)+128 + "";
            tempargs[4] = randomGenerator.nextInt(127-64+1) + 64 + "";
            tempargs[5] = randomGenerator.nextInt(255-192+1) + 192 + "";
            tempargs[6] = randomGenerator.nextInt(63-32+1) + 32 + "";
            tempargs[7] = randomGenerator.nextInt(191-160+1) + 160 + "";
            tempargs[8] = "d" + tempargs[7]; 
            tempargs[9] = "d" + tempargs[6];
            tempargs[10] = "d" + tempargs[5];
            tempargs[11] = "d" + tempargs[4];
            tempargs[12] = "d" + tempargs[3];
            tempargs[13] = "d" + tempargs[2];
             tempargs[14] = randomGenerator.nextInt(31-0+1) +0 + "";
            tempargs[15] = randomGenerator.nextInt(159-128+1)+128 + "";
            tempargs[16] = randomGenerator.nextInt(127-64+1) + 64 + "";
            args = new String[17];
            for (int i = 0; i < 17; i++) {
                //System.out.print(args[i] + " ");
                args[i] = tempargs[i];
                
            }
        
    }
ShowFile show = new ShowFile(args[0], 10, 20);
       // } catch (IOException ex) {
        //    System.out.println("hey");
       // }
        //show.close();
        
        int id = 0;
        XMLmcQuestion mc;
        GAIGSarray items, items2;
         int totalbuckets = 0;
        int numberentered=0;
        int numbertoinput = 0;
        int actionnumber = 2;
        directory maindirectory = new directory();
        String inputa;
        for (int i = 0; i < 15; i++)
            usedcolor[i]=false;
        thecolors[0] = "#008000";
         thecolors[1] = "#0000FF";
        thecolors[2] = "#FF0000";
           thecolors[3] = "#008080";
         thecolors[4] = "#FF00FF";
         thecolors[5] = "#00FFFF";
         thecolors[6] = "#800000";
         thecolors[7] = "#000080";
         thecolors[8] = "#C0C0C0";
         thecolors[9] = "#00FF00";
         thecolors[10] = "#800080";         
        thecolors[11] = "#808000";
         thecolors[12] = "#FFFFFF";
         thecolors[13] = "#FFFF00";
         thecolors[14] = "#808080";
        //System.out.println("length is " + args.length);      
         
        boolean wantmore = true;
        BufferedReader stdin =
                         new BufferedReader(
                                 new InputStreamReader(System.in)
                                         );
                                         
                                         
                        displaydata(maindirectory);
       
           while ( true ) {

            mc = new XMLmcQuestion(show, id + "");
            id++;           
            
            
            
            
                if (args.length == 0 ) {
                    System.out.println("0: to add an element");
                    System.out.println("1: to remove an element");
                    System.out.println("2: add a random number");
                    System.out.println("-1: to stop");
                    System.out.print("option: ");
                    try
                    {
                        inputa = stdin.readLine();
                        numberentered = Integer.parseInt(inputa);
                    }
                    catch(IOException ioex)
                    {
                        System.out.println("Input error");
                        System.exit(1);
                    }
                    catch ( NumberFormatException e)
                    {
                        System.out.println (e.getMessage() + " is not a valid format for an integer.");
                        System.exit(1);
                    }
                
            
  
                
                
                

                
                
                
                    if (numberentered == 0 || numberentered == 1) {             
                
                        System.out.print ("Number to add: ");
                        try
                        {
                            inputa = stdin.readLine();
                            numbertoinput = Integer.parseInt(inputa);
                        }
                        catch(IOException ioex)
                        {
                            System.out.println("Input error");
                            System.exit(1);
                        }
                        catch ( NumberFormatException e)
                        {
                            System.out.println (e.getMessage() + " is not a valid format for an integer.");
                            System.exit(1);
                        }                
                
                    } else if (numberentered == 2) {
                    
                      Random randomGenerator = new Random();
                      int numbertoaddy = randomGenerator.nextInt(1024);
                      numbertoinput = numbertoaddy;
                    
                    
                    } else if (numberentered == -1) {
                    
                       
                    
                    }

            }  else { //actions from main parameters
                
               //System.out.println("ok " + actionnumber + " " + args.length );
                if (actionnumber >= args.length) { 
                   // System.out.println("leaving now");
                    numberentered = -1;
                   // show.close();
                   // return;      
                }
                else if (args[actionnumber].charAt(0) == 'd') {
                    numberentered = 1;
                    numbertoinput = Integer.parseInt(args[actionnumber].substring(1, args[actionnumber].length()  ) ) ;
                }
                else {
                    numberentered = 0;
                    numbertoinput = Integer.parseInt(args[actionnumber]);
                    
                }
                 actionnumber++;    
                
                
            }
                
                
                
                
                
                
                items = new GAIGSarray((int) Math.pow(2, maindirectory.dd), false, null, "#000000", 0.2, 0.2, 1.0, 1.0, 0.07);
                for (int i = 0; i < (int) Math.pow(2, maindirectory.dd); i++ ) {
                    if (i == 0)
                    items.setRowLabel("Directory (dd=" + maindirectory.dd + ") " + decimaltobinary(i, maindirectory.dd), i);   
                    else
                     items.setRowLabel(decimaltobinary(i, maindirectory.dd), i);                   
                    
                    }//label rows

     //set colors of blocks ====================
                for (int i = 0; i < 15; i++)
                    usedcolor[i]=false;  
            for (int i = 0; i < (int) Math.pow(2, maindirectory.dd); i++) {
                if (maindirectory.pointers[i] != null)
                        if (maindirectory.pointers[i].thecolor != -1)
                            usedcolor[maindirectory.pointers[i].thecolor] = true;  //establishing which colors are already used
                            
            }
            for (int i = 0; i < (int) Math.pow(2, maindirectory.dd); i++) {
                if (maindirectory.pointers[i] != null)
                        if (maindirectory.pointers[i].thecolor == -1) {
                            maindirectory.pointers[i].thecolor = nextavailablecolor();  //establishing which colors are already used
                            usedcolor[maindirectory.pointers[i].thecolor] = true;
                        }    
            }                
  
            
      //===========================================                    
                    
                    
                    
                    
                    
                    
                 totalbuckets = 0;
                if (/*maindirectory.pointers[0] != null*/ maindirectory.isused[0] ) {
                    totalbuckets++;
  

                    items.setColor(0, thecolors[maindirectory.pointers[0].thecolor]);
                    items.set("B " + totalbuckets, 0);
                } else {
                    
                    items.setColor(0, "#000000");   
                    
                }
                for (int i = 0; i < (int) Math.pow(2, maindirectory.dd) - 1; i++ ) {
                   
                    
                    if (/*maindirectory.pointers[i] == null*/ !maindirectory.isused[i] ) {
                        items.setColor(i, "#000000");
                        if (/* maindirectory.pointers[i+1] != null */ maindirectory.isused[i+1]) {  
                            totalbuckets++;
                    
                        }
                    } else {

                        items.set("B " + totalbuckets, i);
                        items.setColor(i, thecolors[maindirectory.pointers[i].thecolor]);
                        if ( maindirectory.pointers[i+1] != maindirectory.pointers[i] ) {
                            if ( /* maindirectory.pointers[i+1] != null*/ maindirectory.isused[i+1] ) {   
                                totalbuckets++;
                                
                            }
                        }
                        
                        
                    }
                    
                }
   

   
      
            if (/* maindirectory.pointers[(int) Math.pow(2, maindirectory.dd)-1] == null */ !maindirectory.isused[(int) Math.pow(2, maindirectory.dd)-1]) {
                   items.setColor((int) Math.pow(2, maindirectory.dd)-1, "#000000");   

                } else {
                              items.setColor((int) Math.pow(2, maindirectory.dd)-1, thecolors[maindirectory.pointers[(int) Math.pow(2, maindirectory.dd)-1].thecolor]);          
 
                    items.set("B " + totalbuckets, (int) Math.pow(2, maindirectory.dd)-1);
                }                   
                if (totalbuckets > 0)
                items2 = new GAIGSarray(2*totalbuckets, false, null, "#000000", 0.65, 0.2, 1.4, 1.0,  0.06);   
                else
                items2 = new GAIGSarray(2, false, null, "#000000", 0.65, 0.2, 1.4, 1.0,  0.06);  
  
                
                
                
          
            

            
                totalbuckets = 0;
                if (/* maindirectory.pointers[0] != null*/ maindirectory.isused[0]) {
                    items2.setRowLabel("B" + (totalbuckets+1)+ " (bd=" + maindirectory.pointers[0].bd + ")",0);
                    items2.setColor(0, thecolors[maindirectory.pointers[0].thecolor]);
                    items2.setColor(1, thecolors[maindirectory.pointers[0].thecolor]);
                    if (maindirectory.pointers[0].isused[0] )
                        items2.set(maindirectory.pointers[0].x[0]  ,0);
                    else
                        items2.set("Empty",0);
                    if (maindirectory.pointers[0].isused[1] )
                        items2.set(maindirectory.pointers[0].x[1],1);
                    else
                        items2.set("Empty",1);        
                   // items2.setColor(0, thecolors[0]);
                    //items2.setColor(1, thecolors[0]);
                      totalbuckets++;  
                } 
                
                for (int i = 0; i < (int) Math.pow(2, maindirectory.dd) - 1; i++ ) {
                   
                    
                    if (/* maindirectory.pointers[i] == null || */ !maindirectory.isused[i]) {
                        
                        if (/* maindirectory.pointers[i+1] != null &&*/ maindirectory.isused[i+1]) {  
 
                            totalbuckets++;
                            
                                 items2.setRowLabel( "B" + (totalbuckets) + " (bd=" + maindirectory.pointers[i+1].bd + ")" ,totalbuckets*2-2 );
                                items2.setColor(totalbuckets*2-2, thecolors[maindirectory.pointers[i+1].thecolor]);
                                items2.setColor(totalbuckets*2-1, thecolors[maindirectory.pointers[i+1].thecolor]);
                                if (maindirectory.pointers[i+1].isused[0] )
                                    items2.set(maindirectory.pointers[i+1].x[0]  ,totalbuckets*2-2);
                                 else
                                    items2.set("Empty",totalbuckets*2-2);
                                if (maindirectory.pointers[i+1].isused[1] )
                                    items2.set(maindirectory.pointers[i+1].x[1],totalbuckets*2 + 1-2);
                                else
                                    items2.set("Empty",totalbuckets*2 + 1-2);   
                            
                            
                        }
                    } else {

                        if ( maindirectory.pointers[i+1] != maindirectory.pointers[i] ) {
                            if ( /* maindirectory.pointers[i+1] != null */ maindirectory.isused[i+1] ) {   
     

                                totalbuckets++;
                                items2.setRowLabel( "B" + (totalbuckets) + " (bd=" + maindirectory.pointers[i+1].bd + ")" ,totalbuckets*2-2 );
 
                                items2.setColor(totalbuckets*2-2, thecolors[maindirectory.pointers[i+1].thecolor]);
                                items2.setColor(totalbuckets*2-1, thecolors[maindirectory.pointers[i+1].thecolor]);
                                if (maindirectory.pointers[i+1].isused[0] )
                                    items2.set(maindirectory.pointers[i+1].x[0]  ,totalbuckets*2-2);
                                 else
                                    items2.set("Empty",totalbuckets*2-2);
                                if (maindirectory.pointers[i+1].isused[1] )
                                    items2.set(maindirectory.pointers[i+1].x[1],totalbuckets*2 + 1-2);
                                else
                                    items2.set("Empty",totalbuckets*2 + 1-2);   
                                
                                }
                            }
                        
                        
                        }
                    
                }
           

                

                
                
                if (numberentered == 0 || numberentered == 2) {
                        textthing = new GAIGStext(0.2, 0.0, 0, 0, 0.06, "#000000", "Going to add " + numbertoinput + "\nhashfunction(" + numbertoinput + ")=" + maindirectory.hashfunction(numbertoinput) + " or " + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits));
                        show.writeSnap("" , "info.html", "dynamichashing.php?line=1&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA",  items, items2, textthing);
                        //show.writeSnap(null ,   0.07, items, items2, textthing);
                }
                else if (numberentered == 1) {
                    textthing = new GAIGStext(0.2, 0.0, 0, 0, 0.06, "#000000", "Going to remove " + numbertoinput + "\nhashfunction(" + numbertoinput + ")=" + maindirectory.hashfunction(numbertoinput) + " or " + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits));
                    show.writeSnap("" , "info.html", "dynamichashing2.php?line=1&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) ,  items, items2, textthing);
                    //show.writeSnap(null, 0.07, items, items2, textthing);
                }                 
  //========================================
                      int[] otheranswers = new int[5];
            if (numberentered == 0 || numberentered == 2) {
                    mc.setQuestionText("Which directory element will be used to load the bucket before adding " + numbertoinput + "?");

                    mc.addChoice(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd) + "");
                    if (maindirectory.dd == 1) {
                        if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("0"))
                            mc.addChoice("1");
                        else
                            mc.addChoice("0");
                    } else if (maindirectory.dd == 2) {
                        if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("00")) {
                            mc.addChoice("01");
                            mc.addChoice("10");
                            mc.addChoice("11");
                        }  else if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("01")) {
                            mc.addChoice("00");            
                            mc.addChoice("10"); 
                            mc.addChoice("11"); 
                            
                        } else if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("10")) {
                            mc.addChoice("00");            
                            mc.addChoice("01"); 
                            mc.addChoice("11");                         
                        } else {
                             mc.addChoice("00");            
                            mc.addChoice("01"); 
                            mc.addChoice("10");                          
                        
                        }
                    
                    } else {
                        
                      Random randomGenerator = new Random();

                      boolean hasdups = true; 
                      
                        // int[] otheranswers = new int[5];
                         otheranswers[1] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                            otheranswers[2] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                            otheranswers[3] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                             otheranswers[4] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                             otheranswers[0] = maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd));
                      while (hasdups) {

                             hasdups = false;
                             for (int yupy = 0; yupy < 5; yupy++)
                                for (int nopey = yupy+1; nopey < 5; nopey++)
                                    if (otheranswers[yupy] == otheranswers[nopey]) {
                                        hasdups = true;
                                        otheranswers[nopey] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                                    }
                      }
                     mc.addChoice(decimaltobinary(otheranswers[1], maindirectory.dd));
                     mc.addChoice(decimaltobinary(otheranswers[2], maindirectory.dd));
                      mc.addChoice(decimaltobinary(otheranswers[3], maindirectory.dd));
                      mc.addChoice(decimaltobinary(otheranswers[4], maindirectory.dd));
                    }

                    
                    
                    mc.setAnswer(1);
                    mc.shuffle();
                    
                    if (willask())
                            if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))])
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", mc, items, items2, textthing);
                            else
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=3&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", mc, items, items2, textthing);
                    else
                        if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))])
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", items, items2, textthing);
                            else
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=3&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", items, items2, textthing);
                    mc = new XMLmcQuestion(show, id + "");
                    id++;      
                     mc.setQuestionText("A new bucket will be created");
                     if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))]  || maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].isfull()) {
                         mc.addChoice("True");
                         mc.addChoice("False");
                         
                     } else {
                          mc.addChoice("False");
                         mc.addChoice("True");                        
                         
                         
                        }
                     mc.setAnswer(1);
                    mc.shuffle();
                    if (willask()) 
                    {
                        if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))])
                            show.writeSnap(null, 0.07, null, "dynamichashing.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", mc, items, items2, textthing);
                        else
                            if (maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].isfull())
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=5&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=" + maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[0] + "&amp;var[secondhash]=" + decimaltobinary(maindirectory.hashfunction(maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[0]), maindirectory.maxbits) + "&amp;var[thirdvalue]=" + maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[1] + "&amp;var[thirdhash]=" + decimaltobinary(maindirectory.hashfunction(maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[1]), maindirectory.maxbits), mc, items, items2, textthing);
                            else
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=4&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", mc, items, items2, textthing);
                    }
                    else
                    {
                        if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))])
                            show.writeSnap(null, 0.07, null, "dynamichashing.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", items, items2, textthing);
                        else
                            if (maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].isfull())
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=5&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=" + maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[0] + "&amp;var[secondhash]=" + decimaltobinary(maindirectory.hashfunction(maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[0]), maindirectory.maxbits) + "&amp;var[thirdvalue]=" + maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[1] + "&amp;var[thirdhash]=" + decimaltobinary(maindirectory.hashfunction(maindirectory.pointers[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))].x[1]), maindirectory.maxbits), items, items2, textthing);
                            else
                                show.writeSnap(null, 0.07, null, "dynamichashing.php?line=4&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits) + "&amp;var[secondvalue]=NA&amp;var[secondhash]=NA&amp;var[thirdvalue]=NA&amp;var[thirdhash]=NA", items, items2, textthing);
                        
                        
                    }
            }  else if (numberentered == 1) {
                    mc.setQuestionText("Which directory element will be used to load the bucket before removing " + numbertoinput + "?");
                
                   /*  mc.addChoice("nothing");
                    mc.addChoice("nothing1");
                    mc.addChoice("nothing2");
                    mc.addChoice("shit");
                    mc.addChoice(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd) + "");  
                    mc.setAnswer(4); */
                    mc.addChoice(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd) + "");
                    if (maindirectory.dd == 1) {
                        if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("0"))
                            mc.addChoice("1");
                        else
                            mc.addChoice("0");
                    } else if (maindirectory.dd == 2) {
                        if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("00")) {
                            mc.addChoice("01");
                            mc.addChoice("10");
                            mc.addChoice("11");
                        }  else if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("01")) {
                            mc.addChoice("00");            
                            mc.addChoice("10"); 
                            mc.addChoice("11"); 
                            
                        } else if (decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd).equals("10")) {
                            mc.addChoice("00");            
                            mc.addChoice("01"); 
                            mc.addChoice("11");                         
                        } else {
                             mc.addChoice("00");            
                            mc.addChoice("01"); 
                            mc.addChoice("10");                          
                        
                        }
                    
                    } else {
                        
                      Random randomGenerator = new Random();
  
                      boolean hasdups = true; 
                      
                            otheranswers[1] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                            otheranswers[2] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                            otheranswers[3] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                             otheranswers[4] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                             otheranswers[0] = maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd));
                      while (hasdups) {

                             hasdups = false;
                             for (int yupy = 0; yupy < 5; yupy++)
                                for (int nopey = yupy+1; nopey < 5; nopey++)
                                    if (otheranswers[yupy] == otheranswers[nopey]) {
                                        hasdups = true;
                                        otheranswers[nopey] = randomGenerator.nextInt((int)Math.pow(2, maindirectory.dd));
                                    }
                      }
                     mc.addChoice(decimaltobinary(otheranswers[1], maindirectory.dd));
                     mc.addChoice(decimaltobinary(otheranswers[2], maindirectory.dd));
                      mc.addChoice(decimaltobinary(otheranswers[3], maindirectory.dd));
                      mc.addChoice(decimaltobinary(otheranswers[4], maindirectory.dd));
                    }
 
                    mc.setAnswer(1);
                    mc.shuffle();
                    if (willask())
                        if (maindirectory.isused[otheranswers[0]]) 
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), mc, items, items2, textthing);
                        else
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=3&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), mc, items, items2, textthing);
                    else
                        if (maindirectory.isused[otheranswers[0]]) 
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), items, items2, textthing);
                        else
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=3&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), items, items2, textthing);
                      
                    mc = new XMLmcQuestion(show, id + "");
                    id++;      
                     mc.setQuestionText("A bucket will be destroyed");
                     otheranswers[0] = maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd));
                     //!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))]
                     if ( maindirectory.isused[otheranswers[0]] && ((maindirectory.pointers[otheranswers[0]].isused[0] && maindirectory.pointers[otheranswers[0]].x[0] == numbertoinput && !maindirectory.pointers[otheranswers[0]].isused[1]) 
                        || (maindirectory.pointers[otheranswers[0]].isused[1] && 
                            (maindirectory.pointers[otheranswers[0]].x[1] == numbertoinput) && 
                                !maindirectory.pointers[otheranswers[0]].isused[0] )) )  {
                         mc.addChoice("True");
                         mc.addChoice("False");
                         
                     } else {
                          mc.addChoice("False");
                         mc.addChoice("True");                        
                         
                         
                        }
                     mc.setAnswer(1);
                    mc.shuffle();
                    if (willask())
                         if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))]) 
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), mc, items, items2, textthing);
                        else
                            if ( ((maindirectory.pointers[otheranswers[0]].isused[0] && maindirectory.pointers[otheranswers[0]].x[0] == numbertoinput && !maindirectory.pointers[otheranswers[0]].isused[1]) 
                        || (maindirectory.pointers[otheranswers[0]].isused[1] && 
                            (maindirectory.pointers[otheranswers[0]].x[1] == numbertoinput) && 
                                !maindirectory.pointers[otheranswers[0]].isused[0] )) ) 
                                show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=5&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), mc, items, items2, textthing);                
                            else
                                show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=4&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), mc, items, items2, textthing);  
                                
                        else
                        if (!maindirectory.isused[maindirectory.binarytodecimal(decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits).substring(0, maindirectory.dd))]) 
                            show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=2&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), items, items2, textthing);
                        else
                            if ( ((maindirectory.pointers[otheranswers[0]].isused[0] && maindirectory.pointers[otheranswers[0]].x[0] == numbertoinput && !maindirectory.pointers[otheranswers[0]].isused[1]) 
                        || (maindirectory.pointers[otheranswers[0]].isused[1] && 
                            (maindirectory.pointers[otheranswers[0]].x[1] == numbertoinput) && 
                                !maindirectory.pointers[otheranswers[0]].isused[0] )) ) 
                                show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=5&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), items, items2, textthing);                
                            else
                                show.writeSnap(null, 0.07, "info.html", "dynamichashing2.php?line=4&amp;var[firstvalue]=" + numbertoinput + "&amp;var[firsthash]=" + decimaltobinary(maindirectory.hashfunction(numbertoinput), maindirectory.maxbits), items, items2, textthing);   
 
                    
  }
            
            
                
//==================== question               
                
                
                
                
                
                if (numberentered == 0) {
                
                
                
                   /* System.out.print ("Number to add: ");
                    try
                    {
                        inputa = stdin.readLine();
                        numbertoinput = Integer.parseInt(inputa);
                    }
                    catch(IOException ioex)
                    {
                        System.out.println("Input error");
                        System.exit(1);
                    }
                    catch ( NumberFormatException e)
                    {
                        System.out.println (e.getMessage() + " is not a valid format for an integer.");
                        System.exit(1);
                    }*/


                maindirectory.addelement(numbertoinput);
                
               } else if (numberentered == 1) {
                     /*System.out.print ("Number to remove: ");
                    try
                    {
                        inputa = stdin.readLine();
                        numbertoinput = Integer.parseInt(inputa);
                    }
                    catch(IOException ioex)
                    {
                        System.out.println("Input error");
                        System.exit(1);
                    }
                    catch ( NumberFormatException e)
                    {
                        System.out.println (e.getMessage() + " is not a valid format for an integer.");
                        System.exit(1);
                    }*/


                maindirectory.removeelement(numbertoinput);                  
                   
                } else if (numberentered == -1  ) {
  textthing = new GAIGStext(0.2, 0.0, 0, 0, 0.06, "#000000", "Finished");
                 show.writeSnap(null, 0.07,   items, items2, textthing);     
                    show.close();
                    return;   
                    
                } else if (numberentered == 2) {
                    //  Random randomGenerator = new Random();
                      //int numbertoaddy = randomGenerator.nextInt(1024);
                      //numbertoinput = numbertoaddy;
                     // System.out.println("random number: " + randomGenerator.nextInt(1000));
                   if (!maindirectory.elementexists(numbertoinput))
                     maindirectory.addelement(numbertoinput);    
                    else System.out.println("already exists");
                }
 

                
 
                 
                 
                //displaydata(maindirectory);
                //System.out.println("Total Buckets = " + totalbuckets );
                
                
        }

        // put your code here
        //return;
    }
    public void displaydata(directory cool) {
        //visualizaations can be out here
        return;
        /*System.out.println("Directory Depth: " + cool.dd);
        System.out.println("Index BucketAddress BucketDepth BucketContents");
        int directorylength = (int) Math.pow(2, cool.dd); 
        for (int i = 0; i < directorylength; i++) {
            System.out.print( decimaltobinary(i, cool.dd) + " ");
            if ( cool.isused[i] == false)
                System.out.println("NULL NOBD NOCONTENTS");
             else {
                System.out.print(cool.pointers[i] + " " + (cool.pointers[i]).bd + " [");
                if (cool.pointers[i].isused[0])
                    System.out.print(cool.pointers[i].x[0] + " ");
                 if (cool.pointers[i].isused[1])
                    System.out.print(cool.pointers[i].x[1]); 
                  System.out.println("]");
            
                
            }
            
        }
        System.out.println(""); */
    }
    public String decimaltobinary(int number, int totalbits) {
       String result = "";
        for (int position = 0; position < totalbits; position++) {
            result = (number % 2) + result;
            number = number / 2;
            
        }
        
        
        return result;
    }
    
    public int nextavailablecolor() {
        
        for (int i=0; i < 15; i++)
            if (!usedcolor[i])
                return i;
                
         return 0;
        
    }
    
    public boolean willask() {
        int randomy=0;
        Random randomGenerator = new Random();   
        randomy =  randomGenerator.nextInt(100);
        //System.out.println("will ask if " + randomy + " is less than " + randomquestion);
        if (randomy < randomquestion)
            return true;
        else 
            return false;
    }
    
}

