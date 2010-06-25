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

package exe.finnish_hyphenation;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import exe.*;
import exe.ShowFile;

public class Hyphen {

    static final String TITLE = null;	// no title
    static int arraySize;	// # of items to sort
    static GAIGSarray word;	// the array of items
    static GAIGStext word_label;  
    static String BGCOLOR = "Blue";
    static String URLBASE = "http://www.cs.tut.fi/~ahoniemt/hyphen/vuokaavio_o_";
    
    static String VOKAALIT = "AEIOUYÄÖ";
//	static String DIFTONGIT[] = { "au", "eu", "ey", "ie", "iu", "ou", "uo", "yö", 
//    "äy", "öy", "ai", "ei", "oi", "ui", "yi", "äi", "öi" };		
	static String DIFTONGIT[] = { "AU", "EU", "EY", "IE", "IU", "OU", "UO", "YÖ", 
	    "ÄY", "ÖY", "AI", "EI", "OI", "UI", "YI", "ÄI", "ÖI" };		

	static int KOKO = DIFTONGIT.length;
    
    
	static GAIGSarray convert( String A, double left_bound, double bottom_bound, 
			double top_bound, double right_bound, double font, String label,
			String color ) {
		GAIGSarray tmp = new GAIGSarray( 1, A.length(), label, color, 
				left_bound, bottom_bound, top_bound, right_bound, font );
		
		 // Add the letters to the array
	      for( int i = 0; i < A.length(); ++i ) {
	       tmp.set( A.charAt(i), 0, i ); 	   
	      }
		
		return tmp;
	}
    
    public static void main(String args[]) throws IOException {
    	
       // Only one word given:
       if ( args.length < 2 ) {
    	   throw new IOException( "Input atleast one word.");
       }
       // process program parameters and create the show file object
       ShowFile show = new ShowFile(args[0]);
       //arraySize = Integer.parseInt(args[1]);
       
       for( int wordnumber = 1; wordnumber < args.length; ++wordnumber ) {
       
    	   arraySize = args[wordnumber].length();
       
       
       
       // define the two structures in the show snapshots
       //items = new GAIGSarray(arraySize, true, "BubbleSort", 
       //                       "#999999", 0.1, 0.1, 0.9, 0.9, 0.07);
       
       // The word to be hyphenated
	   //       word = convert( args[wordnumber], 0.3, 0.0, 0.6, 0.4, 0.2, "tarkasteltava", BGCOLOR  );
       word = convert( args[wordnumber], 0.3, 0.0, 0.6, 0.4, 0.2, "", BGCOLOR  );
       word_label = new GAIGStext(0.45,0.2,GAIGStext.HCENTER,GAIGStext.VCENTER,0.08,"#000000","tarkasteltava");

       String hyphenated = tavuta(args[wordnumber], show );
       
       
       	// initialize the array to be sorted & show it   
       	
       }
     //  for (int pass = 1; pass < arraySize; pass++)
     //    for (int i = 0; i < arraySize-pass; i++)
     //      if ((Integer)(items.get(i)) > (Integer)(items.get(i+1)))
     //        swap(i, i+1);
       
       // visualization is done
       show.close();                    
    }

    static boolean vokaali( char c ) {
    	String tmp ="#";
    	tmp = tmp.replace('#', c);
    	return VOKAALIT.contains(tmp);
	}

	static boolean konsonantti ( char c ) {  
		// TODO: non-alphabetical chars are now accepted
		return !vokaali( c );
	}


	static boolean diftongi( char c1, char c2 ) {
	   for( int i = 0; i < KOKO; ++i ) 
	   {
	      if( c1 == DIFTONGIT[ i ].charAt( 0 ) && c2 == DIFTONGIT[ i ].charAt( 1 ) ) 
	      { 
	         return true;
	      }
	   }
	   return false;
	}

	static String tavuta( String s, ShowFile show ) throws IOException {
//		   Vector< boolean > viivat( s.size(), false );
		   String hyphenated = new String(s);
		   String HYPHEN = "-";
		   String node = "0";	   
		   GAIGSarray hyphenArray = convert( hyphenated, 0.3, 0.4, 0.6, 0.8, 0.2, "lopputulos",
				   "White");
		   
		   		   
		   int k = 0; // For calculating the offset caused by hyphens
		   
		   node = "0";
		   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		   
		   
		   word.setColor(0, 0, "Red");
		   node = "1";
		   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		   
		   
		   node = "1_5";
		   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		   
		   // Go through each letter
		   for(  int i = 0; i < s.length(); ++i ) {

			   
			   // Color the right letter

		      // Jos käsiteltävä kirjain on vokaali ja sen perässä on toinen kirjain
			   
			  node = "3";
			  show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
			  
		      if( vokaali( s.charAt( i ) ) && i+1 < s.length() ) {
		         
		    	 node = "5";
		    	 word.setColor(0, i+1, "Yellow");
			 show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		         if( konsonantti( s.charAt( i+1 ) ) ) { // Vokaalin perässä konsonantti
		            
		        	 
		        	 word.setColor(0, i+1, BGCOLOR );
		        	 node = "6_1";
		        	 show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		        	 // -> Konsonanttisääntö
		            int j = 1;
	
		            while( i+j < s.length() && konsonantti( s.charAt( i+j ) ) ) { 
		            	
		            	++j;
		            }
		            
		            if( i+j < s.length() ) {
		            	word.setColor(0, i+j, "Yellow" );
		            	show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		            	word.setColor(0,i+j, BGCOLOR );
		            }
	            	
	            	if( i+j < s.length() && vokaali( s.charAt( i+j ) ) ) {
		            	node = "6_2";
				show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
			        	hyphenArray = convert( hyphenated, 0.3, 0.4, 0.6, 0.8, 0.2, "lopputulos",
		        		 "White" );
	            	   hyphenArray.setColor(0, k+i+j-2, "Green" );
	            	   hyphenArray.setColor(0, k+i+j-1, "Green" );
	            	   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		            	hyphenated = hyphenated.substring(0, k+i+j-1).concat(HYPHEN).concat(hyphenated.substring(k+i+j-1));
		               ++k;
//		            	viivat.at( i+j-2 ) = true;
		              
		            }
		            
		         } else { // Vokaalin perässä toinen vokaali
		            
		        	 // word.setColor(0, i+1, BGCOLOR );
		        	 node = "7";
		        	 show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		        	 
		        	 word.setColor(0, i+1, BGCOLOR );
		        	 // Tuplavokaali tai diftongi
		            if( ( s.charAt( i ) == s.charAt( i+1 ) ) 
		                || diftongi( s.charAt( i ), s.charAt( i+1 ) ) ) {
		               // -> Diftongisääntö
		            	
		            	node = "9";
				show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		            	
		               if( i+2 < s.length() && vokaali( s.charAt( i+2 ) ) ) { 
		            	   hyphenArray = convert( hyphenated, 0.3, 0.4, 0.6, 0.8, 0.2, "lopputulos",
			        		 "White" );
		            	   hyphenArray.setColor(0, k+i+1, "Green" );
		            	   hyphenArray.setColor(0, k+i+2, "Green" );
		            	   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		            	   hyphenated = hyphenated.substring(0, k+i+2).concat(HYPHEN).concat(hyphenated.substring(k+i+2));
		            	   ++k;
		            	   //viivat.charAt( i+1 ) = true;
		               }
		             
		            } else { // Ei tuplavokaali eikä diftongi      
		               // -> Vokaalisääntö
		            	
		            	node = "8";
				show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
			        	hyphenArray = convert( hyphenated, 0.3, 0.4, 0.6, 0.8, 0.2, "lopputulos",
		        		 "White" );
	            	   hyphenArray.setColor(0, k+i, "Green" );
	            	   hyphenArray.setColor(0, k+i+1, "Green" );
	            	   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		            	hyphenated = hyphenated.substring(0, k+i+1).concat(HYPHEN).concat(hyphenated.substring(k+i+1));
		               ++k;
		            	//viivat.charAt( i ) = true;
		            	
		            }
		           
		         }
		        
		         
		         hyphenArray = convert( hyphenated, 0.3, 0.4, 0.6, 0.8, 0.2, "lopputulos",
		        		 "White" );
			 show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		         
		      }
		    
		   
		    
		    
		    
		    if( i+1 < s.length() ) {
		    	word.setColor(0, i, "Yellow" );
		    	word.setColor(0, i+1, "Red" );
		    }
		    
		    node = "4";	        
		    show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
	        
	        word.setColor( 0, i, BGCOLOR );
	        node = "2";
	        show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		      
		   }
		   node = "10";
		   show.writeSnap(TITLE, URLBASE+node+".html", word, word_label, hyphenArray );
		   //System.out.print( hyphenated +"\n");
		  return hyphenated;
	}
	
}
