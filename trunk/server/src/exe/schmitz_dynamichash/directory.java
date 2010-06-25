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
 * Write a description of class directory here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

package exe.schmitz_dynamichash;

import java.lang.Math;
public class directory
{
    // instance variables - replace the example below with your own
    public int dd;
    public bucket[] pointers;
    public boolean[] isused;
    int hashvalue=(int)Math.pow(2, 8);
    /*final */int maxbits = 8;
    /**
     * Constructor for objects of class directory
     */
    public directory()
    {
        // initialise instance variables
        
        dd = 1;
        pointers = new bucket[2];
        isused = new boolean[2];
        pointers[0] = new bucket();
        pointers[1] = new bucket();
        isused[0] = true;
        isused[1] = true;
        /* System.out.println("x[0] " + pointers[0].x[0]);
        System.out.println("is full? " + (pointers[0]).isfull());
        System.out.println("created the buckets" + pointers[0].isfull()); */
        
    }
    public directory(int newdd)
    {
        // initialise instance variables
        
        dd = newdd;
        bucket[] pointers = new bucket[(int) Math.pow(2, dd)];
        
    }
    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    
    public void changedirectorydepth( int newdd, bucket bucketfromsplit, int otherbucket , int oldbd) {
        
        bucket[] temppointers = new bucket[(int) Math.pow(2, newdd)];
            boolean[] tempisused = new boolean[(int) Math.pow(2, newdd)];
        int olddirectorylength = (int) Math.pow(2, dd);
        int newdirectorylength = (int) Math.pow(2, newdd);
        String indexofsplitbucket = "";
        String indexofsplitbucket2 = "";
        if (newdd >= dd) {

            //bucket temppointer = null;
 
          if (bucketfromsplit.isused[0]) 
            indexofsplitbucket = decimaltobinary(hashfunction(bucketfromsplit.x[0]), maxbits).substring(0, newdd);
          else
             indexofsplitbucket = decimaltobinary(hashfunction(bucketfromsplit.x[1]), maxbits).substring(0, newdd);   
 
           if (pointers[otherbucket].isused[0]) 
            indexofsplitbucket2 = decimaltobinary(hashfunction(pointers[otherbucket].x[0]), maxbits).substring(0, newdd);
          else
             indexofsplitbucket2 = decimaltobinary(hashfunction(pointers[otherbucket].x[1]), maxbits).substring(0, newdd); 
          for (int i = 0; i < newdirectorylength; i++) {
                tempisused[i] = false;
          }

          String tempstring= "";
            for (int i = 0; i < olddirectorylength; i++) {
                if (isused[i] == true ) {
                     
                         if ( pointers[i].isused[0] ) {
                                tempstring = decimaltobinary(hashfunction(pointers[i].x[0]), maxbits).substring(0,pointers[i].bd);

                                for (int j = 0; j < newdirectorylength; j++) {
                                   if (tempstring.equals(decimaltobinary(j,newdd).substring(0, pointers[i].bd))) {
                                    //System.out.println("comparing " + tempstring + " with " + decimaltobinary(j,maxbits).substring(0, pointers[i].bd));
                                        temppointers[j] = pointers[i]; 
                                        tempisused[j] = true;
                                    }
                               }
                          } else if (pointers[i].isused[1]) {
                                tempstring = decimaltobinary(hashfunction(pointers[i].x[1]), maxbits).substring(0,pointers[i].bd);
                                for (int j = 0; j < newdirectorylength; j++) {
                                   if (tempstring.equals(decimaltobinary(j,newdd).substring(0, pointers[i].bd))) {
                                      //  System.out.println("comparing " + tempstring + " with " + decimaltobinary(j,maxbits).substring(0, pointers[i].bd));
                                        temppointers[j] = pointers[i]; 
                                        tempisused[j] = true;
                                    }
                               }                             
                              
                            }
                    
                     
                }
                
            }

                
                if (bucketfromsplit.isused[0] == true) {
                               tempstring = decimaltobinary(hashfunction(bucketfromsplit.x[0]), maxbits).substring(0, bucketfromsplit.bd);
                                for (int j = 0; j < newdirectorylength; j++) {
                                    //System.out.println("comparing (split bucket bd = " + bucketfromsplit.bd + " " + tempstring + " with " + decimaltobinary(j,bucketfromsplit.bd).substring(0, bucketfromsplit.bd));
                                    if (tempstring.equals(decimaltobinary(j, newdd).substring(0, bucketfromsplit.bd))) {
                                        temppointers[j] = bucketfromsplit;  
                                        tempisused[j] = true;
                                       // System.out.println( "temppointers[" + j + "] points to split bucket");
                                        
                                        
                                    }
                    
                    }
                    
                } else if (bucketfromsplit.isused[1] == true) {
                                tempstring = decimaltobinary(hashfunction(bucketfromsplit.x[1]), maxbits).substring(0, bucketfromsplit.bd);
                    for (int j = 0; j < newdirectorylength; j++) {
                                   // System.out.println("comparing (split bucket bd = " + bucketfromsplit.bd + " " + tempstring + " with " + decimaltobinary(j,bucketfromsplit.bd).substring(0, bucketfromsplit.bd));
                                    if (tempstring.equals(decimaltobinary(j, newdd).substring(0, bucketfromsplit.bd))) {
                                        temppointers[j] = bucketfromsplit;    
                                        tempisused[j] = true;
                                                                         //  System.out.println( "temppointers[" + j + "] points to split bucket");     
                                        
                                    }
                    
                    }          
                
            }
          pointers = new bucket[(int) Math.pow(2, newdd)];
          isused = new boolean[(int) Math.pow(2, newdd)];
            for (int i = 0; i < newdirectorylength; i++ ) {
                pointers[i] = temppointers[i];   
                isused[i] = tempisused[i];
                
            }//replace with new pointers   
            

          dd = newdd; // replace
        }  else {
            //when merging the buckets
          String tempstring= "";
            for (int i = 0; i < olddirectorylength; i++) {
                if (isused[i] == true ) {
                     
                         if ( pointers[i].isused[0] ) {
                                tempstring = decimaltobinary(hashfunction(pointers[i].x[0]), maxbits).substring(0,pointers[i].bd);

                                for (int j = 0; j < newdirectorylength; j++) {
                                   if (tempstring.equals(decimaltobinary(j,newdd).substring(0, pointers[i].bd))) {
                                    //System.out.println("comparing " + tempstring + " with " + decimaltobinary(j,maxbits).substring(0, pointers[i].bd));
                                        temppointers[j] = pointers[i]; 
                                        tempisused[j] = true;
                                    }
                               }
                          } else if (pointers[i].isused[1]) {
                                tempstring = decimaltobinary(hashfunction(pointers[i].x[1]), maxbits).substring(0,pointers[i].bd);
                                for (int j = 0; j < newdirectorylength; j++) {
                                   if (tempstring.equals(decimaltobinary(j,newdd).substring(0, pointers[i].bd))) {
                                      //  System.out.println("comparing " + tempstring + " with " + decimaltobinary(j,maxbits).substring(0, pointers[i].bd));
                                        temppointers[j] = pointers[i]; 
                                        tempisused[j] = true;
                                    }
                               }                             
                              
                            }
                    
                     
                }
                
            }
          pointers = new bucket[(int) Math.pow(2, newdd)];
          isused = new boolean[(int) Math.pow(2, newdd)];
            for (int i = 0; i < newdirectorylength; i++ ) {
                pointers[i] = temppointers[i];   
                isused[i] = tempisused[i];
                
            }//replace with new pointers   
            

          dd = newdd; // replace
            
           
            
        }
      return;   
        
    }
    public void addelement(int y)
    {
        int bucketindex = binarytodecimal(decimaltobinary(hashfunction(y), maxbits).substring(0, dd));
        bucket buckettemp = new bucket();
        int newbd = 1;
        boolean continueloop=true;
       // System.out.println("going to check pointers[" + bucketindex + "]");
        if ( isused[bucketindex]==false ) {
           /* newbd = 1;
            while ( newbd <= dd ) ) {
                
                newbd++; //finding new bucket depth
                           //System.out.println("While (newbd) " + newbd + " <= (maxbits) " + maxbits + " and " + (decimaltobinary(hashfunction(pointers[bucketindex].x[0]), maxbits).substring(0, newbd)) + " = " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " and " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " = " + decimaltobinary(hashfunction(y), maxbits).substring(0, newbd));
            }*/
            buckettemp.bd = dd+1;
            continueloop = true;
            while (continueloop) {
                         for (int i = 0; i < (int) Math.pow(2, dd); i++) {
                             if ( decimaltobinary(i, dd).substring(0, buckettemp.bd-1).equals(decimaltobinary(bucketindex,dd).substring(0, buckettemp.bd-1)) && isused[i])
                                    continueloop=false;
                             
                             
                            }
                            if (buckettemp.bd == 1) 
                                continueloop = false;
                            if (continueloop) 
                                buckettemp.bd=buckettemp.bd-1;
                            else {
                                //System.out.println("new bucket without expansion with bd of " + buckettemp.bd);
                                           for (int i = 0; i <(int) Math.pow(2, dd); i++) {
                                                if ( decimaltobinary(i, dd).substring(0, buckettemp.bd).equals(decimaltobinary(bucketindex,dd).substring(0, buckettemp.bd)) && !isused[i]) {
                                                    isused[i] = true;
                                                    pointers[i] = buckettemp;
                                                    
                                                }
                                                    
                             
                             
                            }              
                            }
             }
            //pointers[bucketindex] = buckettemp;
            //isused[bucketindex] = true;
            
        }
        
        if ( (this.pointers[bucketindex]).isfull() == false )     
            pointers[bucketindex].add_element(y);
        else {
           // System.out.println("expanding directory newbd = " + newbd + " maxbits " + maxbits);
            //System.out.println("While (newbd) " + newbd + " <= (maxbits) " + maxbits + " and " + (decimaltobinary(hashfunction(pointers[bucketindex].x[0]), maxbits).substring(0, newbd)) + " = " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " and " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " = " + decimaltobinary(hashfunction(y), maxbits).substring(0, newbd));
            newbd = 1;
            while ( newbd <= maxbits && (decimaltobinary(hashfunction(pointers[bucketindex].x[0]), maxbits).substring(0, newbd)).equals(decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd)) && (decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd)).equals(decimaltobinary(hashfunction(y), maxbits).substring(0, newbd)) ) {
 
                newbd++; //finding new bucket depth
                          // System.out.println("While (newbd) " + newbd + " <= (maxbits) " + maxbits + " and " + (decimaltobinary(hashfunction(pointers[bucketindex].x[0]), maxbits).substring(0, newbd)) + " = " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " and " + decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits).substring(0, newbd) + " = " + decimaltobinary(hashfunction(y), maxbits).substring(0, newbd));
            }
            if (maxbits <= newbd-1) {
                
                maxbits = newbd;
                hashvalue = (int)Math.pow(2, maxbits); 
            }
            if ((decimaltobinary(hashfunction(pointers[bucketindex].x[0]), maxbits)).charAt(newbd-1) == '1') {
                                      pointers[bucketindex].isused[0] = false;
                if (buckettemp.isused[0]) {
                      buckettemp.x[1] = pointers[bucketindex].x[0];
                      buckettemp.isused[1] = true;
                    }
                else {
                      buckettemp.x[0] = pointers[bucketindex].x[0];
                      buckettemp.isused[0] = true;
                    }
            }
             if ((decimaltobinary(hashfunction(pointers[bucketindex].x[1]), maxbits)).charAt(newbd -1) == '1') {
                                      pointers[bucketindex].isused[1] = false;
                if (buckettemp.isused[0]) {
                      buckettemp.x[1] = pointers[bucketindex].x[1];
                      buckettemp.isused[1] = true;
                    }
                else {
                      buckettemp.x[0] = pointers[bucketindex].x[1];
                      buckettemp.isused[0] = true;
                    }
            }
             if ((decimaltobinary(hashfunction(y), maxbits)).charAt(newbd-1) == '1') {

                if (buckettemp.isused[0]) {
                      buckettemp.x[1] = y;
                      buckettemp.isused[1] = true;
                    }
                else {
                      buckettemp.x[0] = y;
                      buckettemp.isused[0] = true;
                    }
                
            } else
                pointers[bucketindex].add_element(y);
           // System.out.print("Bucket 0 to be split: [");
           /* if (pointers[bucketindex].isused[0])
                System.out.print(pointers[bucketindex].x[0] + " ");
             if (pointers[bucketindex].isused[1])
                System.out.print(pointers[bucketindex].x[1]);
             System.out.println("]"); 
             
             System.out.print("Bucket 1 to be split: [");
            if (buckettemp.isused[0])
                System.out.print(buckettemp.x[0] + " ");
             if (buckettemp.isused[1])
                System.out.print(buckettemp.x[1]);
             System.out.println("]"); */
             
             int oldbd = pointers[bucketindex].bd;
             String tempstring = "";
            buckettemp.bd = newbd;
            pointers[bucketindex].bd = newbd;
            if (newbd > dd) {
               // System.out.println("old dd " + dd + " new dd " + newbd);
                changedirectorydepth(newbd, buckettemp, bucketindex, oldbd);
                //change directory length   
            } else {
               if (buckettemp.isused[0] == true) {
                               tempstring = decimaltobinary(hashfunction(buckettemp.x[0]), maxbits).substring(0, buckettemp.bd);
                                for (int j = 0; j < (int) Math.pow(2, dd); j++) {
                                    //System.out.println("comparing (split bucket bd = " + bucketfromsplit.bd + " " + tempstring + " with " + decimaltobinary(j,bucketfromsplit.bd).substring(0, bucketfromsplit.bd));
                                    if (tempstring.equals(decimaltobinary(j, dd).substring(0, buckettemp.bd))) {
                                        pointers[j] = buckettemp;  
                                        isused[j] = true;
                            //            System.out.println( "temppointers[" + j + "] points to split bucket");
                                        
                                        
                                    }
                    
                    }
                    
                } else  if (buckettemp.isused[1] == true) {
                               tempstring = decimaltobinary(hashfunction(buckettemp.x[1]), maxbits).substring(0, buckettemp.bd);
                                for (int j = 0; j < (int) Math.pow(2, dd); j++) {
                                    //System.out.println("comparing (split bucket bd = " + bucketfromsplit.bd + " " + tempstring + " with " + decimaltobinary(j,bucketfromsplit.bd).substring(0, bucketfromsplit.bd));
                                    if (tempstring.equals(decimaltobinary(j, dd).substring(0, buckettemp.bd))) {
                                        pointers[j] = buckettemp;  
                                        isused[j] = true;
                              //          System.out.println( "temppointers[" + j + "] points to split bucket");
                                        
                                        
                                    }
                    
                    }
                    
                }
                
                
                for (int i = 0; i < (int) Math.pow(2, dd); i++) {
                    if (isused[i]) {
                        if (pointers[i].isused[0]) {
                               if (!decimaltobinary(i, dd).substring(0, pointers[i].bd).equals(decimaltobinary(hashfunction(pointers[i].x[0]), maxbits).substring(0,  pointers[i].bd))) {
                                    isused[i] = false; 
                                  //  System.out.println( "made false since " + decimaltobinary(i, dd).substring(0, pointers[i].bd) + " does not equal " + decimaltobinary(hashfunction(pointers[i].x[0]), pointers[i].bd));
                                }
 
                            } else if (!decimaltobinary(i, dd).substring(0, pointers[i].bd).equals(decimaltobinary(hashfunction(pointers[i].x[1]), maxbits).substring(0,  pointers[i].bd))) {             
                                if (!decimaltobinary(i, dd).substring(0, pointers[i].bd).equals(decimaltobinary(hashfunction(pointers[i].x[1]), maxbits))) {
                                    isused[i] = false;
                                    //System.out.println( "made false since " + decimaltobinary(i, dd).substring(0, pointers[i].bd) + " does not equal " + decimaltobinary(hashfunction(pointers[i].x[1]), pointers[i].bd));
                                }
                            }
                    
                
                    }
                }
            }
            //if (pointers[bucketindex].x[0] % Math.pow(2, newbd) 
            //split

        }
        return;
    }
    public void removeelement(int y)
    {
        int bucketindex = binarytodecimal(decimaltobinary(hashfunction(y), maxbits).substring(0, dd));
        int directorylengthy = (int) Math.pow(2, dd);
        boolean getout = false;
        int largestdd = 1;
        if (isused[bucketindex]) {
            pointers[bucketindex].remove_element(y);
            if ( pointers[bucketindex].isempty() == true ) {
            //merge
                
               // System.out.println("Bucket should be deleted here");
                getout = false;
                for (int i = 0; i < directorylengthy && (!getout) ; i++) {

                    if (isused[i]) {
                    if (!(pointers[bucketindex].equals(pointers[i])) && (pointers[i].bd == pointers[bucketindex].bd) && decimaltobinary(bucketindex,dd).substring(0, pointers[bucketindex].bd-1).equals(decimaltobinary(i,dd).substring(0, pointers[i].bd-1)) ) {
                        if (pointers[bucketindex].bd  > 1 ) {
                             pointers[bucketindex].bd -= 1;
                             pointers[i].bd -= 1;
                       }
                        getout = true;

                        
                        //for (int j = i; pointers[j].equals(pointers[bucketindex]) && isused[j]; j++)
                         //   pointers[j] = pointers[i];
                        for (int j = 0; j < directorylengthy; j++)
                            if ( bucketindex != j && pointers[bucketindex].equals(pointers[j]))
                                pointers[j] = pointers[i];
                        pointers[bucketindex] = pointers[i]; 
                    }    
                }
                }
                if (getout == false) {
                    for (int i = 0; i < directorylengthy; i++)
                        if (isused[i]) {
                        if (pointers[i].equals(pointers[bucketindex]))
                            isused[i] = false;
                        }

                }

 //lower any bd 

            for (int i = 0; i < directorylengthy; i++) {
                if (isused[i]) {
                        if (i == 0 || !isused[i-1] || !(isused[i-1] && pointers[i-1].equals(pointers[i])))  
                        while (possiblenewbucketdepth(i, pointers[i].bd-1)) {
                            pointers[i].bd -= 1;   
                          //  System.out.println("ok so changing pointers[" + i + "].bd to " + pointers[i].bd);
                        }
                
               }
                
            }
                
                for (int i = 0; i < directorylengthy; i++) {
                    if (isused[i]) 
                        if (pointers[i].bd > largestdd && !pointers[i].isempty()) {
                            largestdd = pointers[i].bd;
                            //System.out.println("pointers[" + i + "] bd is " + pointers[i].bd);
                        }
                    
                }
               // System.out.println("so the largest bd now is: " + largestdd);
                if (largestdd < dd) {
                    changedirectorydepth( largestdd, null, 0 , 0);
                    
                }
                
            }   
        }
    }
    
    public boolean possiblenewbucketdepth(int bucketindex, int newbd) {
        int directorylength = (int) Math.pow(2, dd);
        if (newbd < 1)
            return false;
         
        boolean returnvalue = true;
        for (int i = 0; i < directorylength; i++) {
            if (isused[i] && !pointers[i].equals(pointers[bucketindex])  ) {
               // System.out.println("checking index i = " + i + " and comparing with " + decimaltobinary(i,dd).substring(0,dd) + " to " + decimaltobinary(bucketindex,dd).substring(0, dd) + " with depth of " + newbd );
                if (decimaltobinary(i,dd).substring(0,newbd).equals(decimaltobinary(bucketindex,dd).substring(0, newbd)) && !pointers[i].isempty()) {
                   returnvalue = false;   
                 //  System.out.println("nope");
                    
                } else {
                   // System.out.println("yup");   
                    
                }
                
            }
            
            
        }
        
       return returnvalue; 
    }
    public boolean elementexists(int y)
    {
  
        
        int bucketindex = (int) binarytodecimal(decimaltobinary(hashfunction(y), maxbits).substring(0, dd));
        if (isused[bucketindex]==false)
            return false;
        if ( pointers[bucketindex].isused[0] )
            if ( pointers[bucketindex].x[0] == y )
                return true;
        
         if ( pointers[bucketindex].isused[1] )
            if ( pointers[bucketindex].x[1] == y )
                return true;
                
         return false;
               
    }
    
    public int hashfunction(int y)
    {
           return y%hashvalue;
    }
    
    public String decimaltobinary(int number, int totalbits) {
       String result = "";
        for (int position = 0; position < totalbits; position++) {
            result = (number % 2) + result;
            number = number / 2;
            
        }
        
        
        return result;
    }
    public int binarytodecimal(String number) {
        int theresult = 0;
        int powervalue = 1;
        for (int i = number.length()-1; i >= 0; i--) {
            if ( number.charAt(i) == '1' )
                theresult += powervalue;
             powervalue = powervalue * 2;
            
        }
        return theresult;
        
    }
    

}
