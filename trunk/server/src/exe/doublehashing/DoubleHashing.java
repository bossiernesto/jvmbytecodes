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

package exe.doublehashing;

import java.io.*;
import java.util.*;
import exe.*;

public class DoubleHashing{
    private PrintWriter out;
    private int tableSize;
    private int numKeys;
    private int maxKey = 100;
    private Vector Keys;
    private Vector Table;
    private questionCollection Questions;
    private int qIndex = 0;
    private Random r;
    
    public DoubleHashing(String GaigsFile, String tableSizeStr, String numKeysStr){
        tableSize = (new Integer(tableSizeStr)).intValue();
        numKeys = (new Integer(numKeysStr)).intValue();
        if(numKeys > tableSize)
            numKeys = tableSize;
        Keys = new Vector(numKeys);
        Table = new Vector(tableSize*2);
        r = new Random();
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(GaigsFile)));
            Questions = new questionCollection(out);
            InitializeTable();
            GenerateKeys();
            InsertKeys();
            RetrieveKeys();
            Questions.writeQuestionsAtEOSF();
            out.close();
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from construct DoubleHashing");
            e.printStackTrace();
            try{
                out.close();
            }catch(Exception f){}
        }    
    }
    private void InitializeTable(){
        for(int x = 0; x < tableSize; x++)
            Table.addElement(new arraySlot("\\W", "."));
    }
    private void GenerateKeys(){
        for(int x = 0; x < numKeys; x++){
            Integer key = new Integer(r.nextInt(maxKey));
            while(Keys.indexOf(key) != -1)
                key = new Integer(r.nextInt(maxKey));
            Keys.addElement(key);
            System.out.println(key);
        }
        Vector captionVect = new Vector();
        String caption = "We have "+numKeys+" keys to insert:  ";
        for(int x = 0; x < numKeys; x++){
            caption = caption + Keys.elementAt(x);
            if(x < numKeys -1)
                caption = caption + ", ";
        }
        captionVect.addElement(caption);
        captionVect.addElement("into a hash table with " + tableSize + " slots.");
        createSnapShot(-1, captionVect, true);
        captionVect.removeAllElements();
    }
    
    private void InsertKeys(){
        Vector captionVect = new Vector();
        for(int x = 0; x < numKeys; x++){
            int currentKey = ((Integer)Keys.elementAt(x)).intValue();
            int hashCode = currentKey%tableSize;
	    int incrementForProbe = tableSize - 2 - currentKey % (tableSize - 2); 
            fibQuestion quest = new fibQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("At what index will the key "+currentKey+" be inserted?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize+" = "+hashCode);
	    captionVect.addElement("The rehashing (secondary) function for this key is:");
	    captionVect.addElement("Hash2(key) = TableSize - 2 - (key % (TableSize - 2))");
	    captionVect.addElement("So Hash2("+currentKey+") = "+ incrementForProbe);
	    //            captionVect.addElement("H("+currentKey+") = "+ hashCode);
            //captionVect.addElement(""+hashCode+" is the hash code for key "+currentKey+".");
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Insert key "+currentKey+" into slot "+hashCode+".");
            while(!(((arraySlot)Table.elementAt(hashCode)).contents).equals(".")){
                captionVect.addElement("COLLISION!!");
                Table.setElementAt(new arraySlot("\\R", ((arraySlot)Table.elementAt(hashCode)).contents), hashCode);
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements(); 
		captionVect.addElement("Use rehashing function to determine the increment");
		captionVect.addElement("Hash2(key) = TableSize - 2 - (key % (TableSize - 2))");
		captionVect.addElement("So Hash2("+currentKey+") = "+ incrementForProbe);
		hashCode = (hashCode + incrementForProbe) % tableSize;
                captionVect.addElement("Insert key "+currentKey+" into slot "+hashCode+".");
            }
            Table.setElementAt(new arraySlot("\\Y", currentKey), hashCode);
            quest.setAnswer((new Integer(hashCode)).toString());
            qIndex++;
            createSnapShot(-1, captionVect, true);
            captionVect.removeAllElements();
            for(int y = 0; y < tableSize; y++)
                if(((arraySlot)Table.elementAt(y)).color.equals("\\R"))
                    Table.setElementAt(new arraySlot("\\Y", 
						     ((arraySlot)Table.elementAt(y)).contents), y);
        }
        captionVect.addElement("We're done inserting keys.");
        createSnapShot(-1, captionVect, true);
    }
    private void RetrieveKeys(){
        int inc = 3;
        int numCollisions = 0;
        Vector captionVect = new Vector();
        Vector searchKeys = new Vector();
        String caption = "Let's search for the following keys: ";
        for(int x = 0; x < numKeys; x+=inc){
            caption = caption + Keys.elementAt(x) + ", ";
            searchKeys.addElement(Keys.elementAt(x));
            Integer missingkey = new Integer(r.nextInt(maxKey));
            while(Keys.indexOf(missingkey) != -1)
                missingkey = new Integer(r.nextInt(maxKey));
            searchKeys.addElement(missingkey);
            caption = caption + missingkey;
            if(x + inc < numKeys)
                caption = caption + ", ";
            else{
                caption = caption + ".";
                captionVect.addElement(caption);
            }
        }
        captionVect.addElement("Note that some of these keys are not in the hash table.");
        createSnapShot(-1, captionVect, true);
        captionVect.removeAllElements();
        for(int x = 0; x < searchKeys.size(); x++){
            int currentKey = ((Integer)searchKeys.elementAt(x)).intValue();
            int hashCode = currentKey%tableSize;
	    int incrementForProbe = tableSize - 2 - currentKey % (tableSize - 2); 
            mcQuestion quest = new mcQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("How many hash table slots will have to be searched to determine whether the key "+currentKey+" is in the table?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize+" = "+hashCode);
	    captionVect.addElement("The rehashing (secondary) function for this key is:");
	    captionVect.addElement("Hash2(key) = TableSize - 2 - (key % (TableSize - 2))");
	    captionVect.addElement("So Hash2("+currentKey+") = "+ incrementForProbe);
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Search for the key "+currentKey+" in hash table slot "+hashCode+".");
            while((!((arraySlot)Table.elementAt(hashCode)).contents.equals((new Integer(currentKey)).toString()))&&
		  (!((String)((arraySlot)Table.elementAt(hashCode)).contents).equals("."))&&(numCollisions<tableSize)){
                captionVect.addElement("The key "+(String)(((arraySlot)Table.elementAt(hashCode)).contents)
				       +" is not equal to the key we're searching for, "+currentKey+".");
                Table.setElementAt(new arraySlot("\\R",
						 ((arraySlot)Table.elementAt(hashCode)).contents), hashCode);
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements();
		captionVect.addElement("Use rehashing function to determine the increment");
		captionVect.addElement("Hash2(key) = TableSize - 2 - (key % (TableSize - 2))");
		captionVect.addElement("So Hash2("+currentKey+") = "+ incrementForProbe);
		hashCode = (hashCode + incrementForProbe) % tableSize;
                captionVect.addElement("Search for the key "+currentKey+" in hash table slot "+hashCode+".");
                numCollisions++;
            }
            if(((arraySlot)Table.elementAt(hashCode)).contents.equals((new Integer(currentKey)).toString())){
                captionVect.addElement("Key "+currentKey+" found in hash table slot "+hashCode+".");
                Table.setElementAt(new arraySlot("\\M",
						 ((arraySlot)Table.elementAt(hashCode)).contents), hashCode);
            }
            else{
                Table.setElementAt(new arraySlot("\\X",
						 ((arraySlot)Table.elementAt(hashCode)).contents), hashCode);
                if(numCollisions == tableSize){
                    //captionVect.addElement("We're back to where we started with hash table slot "+hashCode+".");
                    captionVect.addElement("Key "+currentKey+" is not in the table, since we've searched the entire table.");
                }
                else
                    captionVect.addElement("Key "+currentKey+" is not in the table, since we've found an empty slot.");
            }
            if(numCollisions < tableSize){
                quest.addChoice((new Integer(numCollisions + 1)).toString());
                quest.addChoice((new Integer(numCollisions + 3)).toString());
                quest.addChoice((new Integer(numCollisions + 2)).toString());
                if(numCollisions == 0)
                    quest.addChoice((new Integer(numCollisions + 4)).toString());
                else
                    quest.addChoice((new Integer(numCollisions)).toString());
            }
            else{
                quest.addChoice((new Integer(numCollisions)).toString());
                quest.addChoice((new Integer(numCollisions + 1)).toString());
                quest.addChoice((new Integer(numCollisions - 1)).toString());
                quest.addChoice((new Integer(numCollisions - 2)).toString());
            }
            quest.setAnswer(1);
            qIndex++;
            createSnapShot(-1, captionVect, true);
            numCollisions = 0;
            captionVect.removeAllElements();
            for(int y = 0; y < tableSize; y++){
                if((((arraySlot)Table.elementAt(y)).color.equals("\\R"))||(((arraySlot)Table.elementAt(y)).color.equals("\\M"))
		   ||(((arraySlot)Table.elementAt(y)).color.equals("\\X"))){
		    if(((arraySlot)Table.elementAt(y)).contents.equals("."))
			Table.setElementAt(new arraySlot("\\W", "."), y);
		    else
			Table.setElementAt(new arraySlot("\\Y", 
							 ((arraySlot)Table.elementAt(y)).contents), y);
                }    
            }
        }
        captionVect.addElement("We're done searching for keys.");
        createSnapShot(-1, captionVect, true);
    }
    private void createSnapShot(int questionIndex, Vector captionVect, boolean drawArray){
        try{
            out.println("VIEW WINDOWS 1");
            if(questionIndex != -1)
                Questions.insertQuestion(questionIndex);
            out.println("MD_Array 0.015 0.015 HeavyBold");
            out.println("1");
            for(int x = 0; x < captionVect.size(); x++)
                out.println((String)captionVect.elementAt(x));
            out.println("***\\***");
            if(drawArray){
                out.println(tableSize);
                out.println("1");
                for(int x = 0; x < tableSize; x++){
                    out.println(x);
                    out.println("1");
                    out.println(((arraySlot)Table.elementAt(x)).color+" "+((arraySlot)Table.elementAt(x)).contents);
                }
            }
            else{
                out.println("0");
                out.println("0");
            }
            out.println("***^***");
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from createSnapShot");
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        new DoubleHashing(args[0], args[1], args[2]);
        System.exit(0);
    }
    class arraySlot{
        String color, contents;
        public arraySlot(String color, String contents){
            this.color = color;
            this.contents = contents;
        }
        public arraySlot(String color, int contents){
            this.color = color;
            this.contents = (new Integer(contents)).toString();
        }
        public arraySlot(String color, Integer contents){
            this.color = color;
            this.contents = contents.toString();
        }
    }
}
