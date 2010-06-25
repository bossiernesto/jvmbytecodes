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

package exe.quadhashing;

import java.io.*;
import java.util.*;
import exe.*;

public class QuadraticHashing{
    private PrintWriter out;
    private int tableSize;
    private int numKeys;
    private int maxKey = 100;
    private Vector Keys;
    private Vector Table;
    private questionCollection Questions;
    private int qIndex = 0;
    private Random r;
    //private DocumentationGroup dg;
    private int docCount = 0;
    
    public QuadraticHashing(String GaigsFile, String tableSizeStr, String numKeysStr){
        
        tableSize = (new Integer(tableSizeStr)).intValue();
        numKeys = (new Integer(numKeysStr)).intValue();
        if(numKeys > tableSize)
            numKeys = tableSize;
        Keys = new Vector(numKeys);
        Table = new Vector(tableSize*2);
        r = new Random();
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(GaigsFile)));
            //dg = new DocumentationGroup(GaigsFile, out);
            //dg.setHtmlTemplate("exe/quadhashing/temp.html");
            Questions = new questionCollection(out);
            InitializeTable();
            GenerateKeys();
            InsertKeys();
            RetrieveKeys();
            Questions.writeQuestionsAtEOSF();
            out.close();
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from construct QuadraticHashing");
            try{
                }catch(Exception f){
                    out.println(e.toString()+" thrown from construct QuadraticHashing");
                    out.flush();
                    out.close();
                    System.out.println(f.toString()+" thrown from construct QuadraticHashing");
                    f.printStackTrace();
                }
            e.printStackTrace();
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
        captionVect.addElement("We have "+numKeys+" keys to Insert:");
        String caption = "";
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
        int collisions = 0;
        int currentKey, hashCode, origHashCode;
        fibQuestion quest;
        for(int x = 0; x < numKeys; x++){
            collisions = 0;
            currentKey = ((Integer)Keys.elementAt(x)).intValue();
            origHashCode = currentKey%tableSize;
            hashCode = origHashCode;
            quest = new fibQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("At what index will the key "+currentKey+" be inserted?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize);
            captionVect.addElement("H("+currentKey+") = "+ hashCode);
            captionVect.addElement(""+origHashCode+" is the hash code for key "+currentKey+".");
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Insert key "+currentKey+" into slot "+hashCode+".");
            boolean add = true;
            while(!(((arraySlot)Table.elementAt(hashCode)).contents).equals(".")){
                collisions++;
                captionVect.addElement("COLLISION!!");
                if(!add)
                    captionVect.addElement("The number of collisions so far is "+((int)tableSize/2+collisions)+".");
                else
                    captionVect.addElement("The number of collisions so far is "+collisions+".");
                ((arraySlot)Table.elementAt(hashCode)).color = "\\R";
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements();
                if(((collisions > ((int)tableSize/2)))&&add){
                    collisions = 1;
                    add = false;
                    captionVect.addElement("Since the number of collisions is greater than half the table size,");
                    captionVect.addElement("start to insert into the table by subracting the square of the");
                    captionVect.addElement("number of collisions minus half the table size from the hash code.");
                    createSnapShot(-1, captionVect, true);
                    captionVect.removeAllElements(); 
                }
                if((collisions > ((int)tableSize/2)))
                    if(!add) break;
                captionVect.addElement("Square the number of collisions,");
                if(add){
                    captionVect.addElement("add that to the origional hash code and try again.");
                    captionVect.addElement("Hash code = "+origHashCode+" + "+collisions+"*"+collisions+" = "+(origHashCode+(collisions*collisions))+".");
                }
                else{
                    captionVect.addElement("subtract half the table size,");
                    captionVect.addElement("subtract that from the origional hash code and try again.");
                    captionVect.addElement("Hash code = "+origHashCode+" - "+collisions+"*"+collisions+" = "+(origHashCode-(collisions*collisions))+".");
                }
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements();
                if(add)
                    hashCode = origHashCode + (collisions*collisions);
                else
                    hashCode = origHashCode - (collisions*collisions);
                if(hashCode >= tableSize){
                    captionVect.addElement("Since "+hashCode+" is greater than the highest indexed table slot,");
                    captionVect.addElement("mod out by the size of the hash table.  hash code = "+hashCode+"%"+tableSize+" = "+(hashCode%tableSize)+".");
                    hashCode = hashCode%tableSize;
                }
                if(hashCode < 0){
                    int m = 1;
                    while(m*tableSize + hashCode < 0)
                        m++; 
                    captionVect.addElement("Since "+hashCode+" is less than zero, add a multiple");
                    captionVect.addElement("of the size of the hash table that makes the hash code non-negative.");
                    captionVect.addElement("hash code = "+hashCode+"+"+(m*tableSize)+" = "+(hashCode+(m*tableSize))+".");
                    hashCode = hashCode+(m*tableSize);
                }
                captionVect.addElement("Insert key "+currentKey+" into slot "+hashCode+".");
                if(collisions > 50) break;
            }
            Table.setElementAt(new arraySlot("\\M", currentKey), hashCode);
            quest.setAnswer((new Integer(hashCode)).toString());
            qIndex++;
            createSnapShot(-1, captionVect, true);
            captionVect.removeAllElements();
            for(int y = 0; y < tableSize; y++)
                if(((arraySlot)Table.elementAt(y)).color.equals("\\R")||((arraySlot)Table.elementAt(y)).color.equals("\\M"))
                    ((arraySlot)Table.elementAt(y)).color = "\\Y";
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
        int currentKey, origHashCode, hashCode;
        for(int x = 0; x < searchKeys.size(); x++){
            currentKey = ((Integer)searchKeys.elementAt(x)).intValue();
            origHashCode = currentKey%tableSize;
            hashCode = origHashCode;
            mcQuestion quest = new mcQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("How many hash table slots will have to be searched to determine whether the key "+currentKey+" is in the table?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize);
            captionVect.addElement("H("+currentKey+") = "+ hashCode);
            captionVect.addElement(""+hashCode+" is the hash code for key "+currentKey+".");
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Search for the key "+currentKey+" in hash table slot "+hashCode+".");
            boolean add = true;
            boolean found = false;
            while((!((arraySlot)Table.elementAt(hashCode)).contents.equals((new Integer(currentKey)).toString()))&&
                (!((String)((arraySlot)Table.elementAt(hashCode)).contents).equals("."))){
                numCollisions++;
                captionVect.addElement("The key "+(String)(((arraySlot)Table.elementAt(hashCode)).contents)
                    +" is not equal to the key we're searching for, "+currentKey+".");
                captionVect.addElement("The number of times we've searched");
                if(add)
                    captionVect.addElement("a hash table slot unsuccessfully is "+numCollisions+".");
                else
                    captionVect.addElement("a hash table slot unsuccessfully is "+(tableSize/2 +numCollisions)+".");
                ((arraySlot)Table.elementAt(hashCode)).color = "\\R";
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements();
                if((numCollisions > tableSize/2)&&add){
                    numCollisions = 1;
                    add = false;
                    captionVect.addElement("Since the number of collisions is greater than half the table size,");
                    captionVect.addElement("start to search the table by subracting the square of the");
                    captionVect.addElement("number of collisions minus half the table size from the hash code.");
                    createSnapShot(-1, captionVect, true);
                    captionVect.removeAllElements(); 
                }
                if(numCollisions > tableSize/2){
                    if(!add)
                        break;
                }
                if(add){
                    captionVect.addElement("Square the number of collisions,");
                    captionVect.addElement("add that to the hash code and try again.");
                    captionVect.addElement("Hash code = "+origHashCode+" + "+numCollisions+"*"+numCollisions+" = "+(origHashCode+(numCollisions*numCollisions))+".");
                }
                else{
                    captionVect.addElement("Subtract half the table size from the number of collisions,");
                    captionVect.addElement("square the quantity,");
                    captionVect.addElement("subtract that from the hash code and try again.");
                    captionVect.addElement("Hash code = "+origHashCode+" - "+numCollisions+"*"+numCollisions+" = "+(origHashCode-(numCollisions*numCollisions))+".");
                }
                createSnapShot(-1, captionVect, true);
                captionVect.removeAllElements();
                if(add)
                    hashCode = origHashCode + (numCollisions*numCollisions);
                else
                    hashCode = origHashCode - (numCollisions*numCollisions);
                if(hashCode >= tableSize){
                    captionVect.addElement("Since "+hashCode+" is greater than the highest indexed table slot,");
                    captionVect.addElement("mod out by the size of the hash table.  hash code = "+hashCode+"%"+tableSize+" = "+(hashCode%tableSize)+".");
                    hashCode = hashCode%tableSize;
                }
                if(hashCode < 0){
                    int m = 1;
                    while(m*tableSize + hashCode < 0)
                        m++; 
                    captionVect.addElement("Since "+hashCode+" is less than zero, add a multiple");
                    captionVect.addElement("of the size of the hash table that makes the hash code non-negative.");
                    captionVect.addElement("hash code = "+hashCode+"+"+(m*tableSize)+" = "+(hashCode+(m*tableSize))+".");
                    hashCode = hashCode+(m*tableSize);
                }
                captionVect.addElement("Search for the key "+currentKey+" in hash table slot "+hashCode+".");
                if(numCollisions > 50) break;
            }
            if(((arraySlot)Table.elementAt(hashCode)).contents.equals((new Integer(currentKey)).toString())){
                captionVect.addElement("Key "+currentKey+" found in hash table slot "+hashCode+".");
                ((arraySlot)Table.elementAt(hashCode)).color = "\\M";
                found = true;
            }
            else if(!((arraySlot)Table.elementAt(hashCode)).contents.equals(".")){
                captionVect.addElement("Done searching, key "+currentKey+" not found.");
                ((arraySlot)Table.elementAt(hashCode)).color = "\\R";
            }
            else{
                Table.setElementAt(new arraySlot("\\X",
                    ((arraySlot)Table.elementAt(hashCode)).contents), hashCode);
                if(numCollisions > tableSize){
                    captionVect.addElement("We're back to where we started with hash table slot "+hashCode+".");
                    captionVect.addElement("Key "+currentKey+" is not in the table, since we've searched the entire table.");
                }
                else
                    captionVect.addElement("Key "+currentKey+" is not in the table, since we've found an empty slot.");
            }
            if((numCollisions < tableSize/2)&&add){
                quest.addChoice((new Integer(numCollisions + 1)).toString());
                quest.addChoice((new Integer(numCollisions + 3)).toString());
                quest.addChoice((new Integer(numCollisions + 2)).toString());
                if(numCollisions == 0)
                    quest.addChoice((new Integer(numCollisions + 4)).toString());
                else
                    quest.addChoice((new Integer(numCollisions)).toString());
            }
            else if((numCollisions < tableSize/2)||(!found)){
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions + 1)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions + 3)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions + 2)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions)).toString());
            }
            else{
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions + 1)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions - 1)).toString());
                quest.addChoice((new Integer((tableSize/2 - 1)+numCollisions - 2)).toString());
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
                        ((arraySlot)Table.elementAt(y)).color = "\\W";
                    else
                        ((arraySlot)Table.elementAt(y)).color = "\\Y";
                }    
            }
        }
        captionVect.addElement("We're done searching for keys.");
        createSnapShot(-1, captionVect, true);
    }
    private void createSnapShot(int questionIndex, Vector captionVect, boolean drawArray){
        try{
            //dg.makeAndInsertDoc((new Integer(docCount)).toString(), (new Integer(questionIndex)).toString());
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
        new QuadraticHashing(args[0], args[1], args[2]);
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
