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

package exe.chainedhashing;

import java.io.*;
import java.util.*;
import exe.*;

public class LinkedHashing{
    private PrintWriter out;
    private int tableSize;
    private int numKeys;
    private int maxKey = 100;
    private Vector Keys;
    private Vector Table;
    private questionCollection Questions;
    private int qIndex = 0;
    private Random r;
    
    public LinkedHashing(String GaigsFile, String tableSizeStr, String numKeysStr){
        tableSize = (new Integer(tableSizeStr)).intValue();
        numKeys = (new Integer(numKeysStr)).intValue();
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
            System.out.println(e.toString()+" thrown from construct LinearHashing");
            e.printStackTrace();
            try{
                out.close();
            }catch(Exception f){}
        }    
    }
    private void InitializeTable(){
        for(int x = 0; x < tableSize; x++)
            Table.addElement(new arraySlot("\\W"));
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
            fibQuestion quest = new fibQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("At what index will the key "+currentKey+" be inserted?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize);
            captionVect.addElement("H("+currentKey+") = "+ hashCode);
            captionVect.addElement(""+hashCode+" is the hash code for key "+currentKey+".");
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Add key "+currentKey+" to the end of the linked list in hash table slot "+hashCode+".");
            ((arraySlot)Table.elementAt(hashCode)).color = "\\Y"; 
            ((arraySlot)Table.elementAt(hashCode)).list.addNode(currentKey);
            quest.setAnswer((new Integer(hashCode)).toString());
            qIndex++;
            createSnapShot(-1, captionVect, true);
            captionVect.removeAllElements();
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
            mcQuestion quest = new mcQuestion(out, (new Integer(qIndex)).toString());
            quest.setQuestionText("How many linked list nodes will have to be searched to determine wheither the key "+currentKey+" is in the table?");
            Questions.addQuestion(quest);
            captionVect.addElement("Compute hashcode for key "+currentKey+".");
            captionVect.addElement("H(key) = key % TableSize");
            captionVect.addElement("H("+currentKey+") = "+currentKey+" % "+tableSize);
            captionVect.addElement("H("+currentKey+") = "+ hashCode);
            captionVect.addElement(""+hashCode+" is the hash code for key "+currentKey+".");
            createSnapShot(qIndex, captionVect, true);
            captionVect.removeAllElements();
            captionVect.addElement("Search for the key "+currentKey+" in hash table slot "+hashCode+".");
            ((arraySlot)Table.elementAt(hashCode)).color = "\\B";
            linkedList list = ((arraySlot)Table.elementAt(hashCode)).list;
            int y;
            for(y = 0; y < list.numNodes; y++){
                
                if(((Integer)list.nodes.elementAt(y)).intValue() == currentKey)
                    break;
                else{
                    captionVect.addElement("The key "+currentKey+" does not match the key in the linked");
                    captionVect.addElement("list of hash table slot "+hashCode+" at position "+y+".");
                    captionVect.addElement("Search the next postion in the linked list.");
                    createSnapShot(-1, captionVect, true);
                    captionVect.removeAllElements();
                }
            }
            if(y == list.numNodes){
                ((arraySlot)Table.elementAt(hashCode)).color = "\\R";
                captionVect.addElement("Since there are no more nodes in the linked list at hash table slot "+hashCode+",");
                captionVect.addElement("the key "+currentKey+" is not in the hash table.");
            }
            else{
                ((arraySlot)Table.elementAt(hashCode)).color = "\\M";
                captionVect.addElement("The key "+currentKey+" matches the key in the linked list of hash table slot "+hashCode);
                captionVect.addElement(" at position "+y+".  We've found the key!");
            }
            quest.addChoice((new Integer(y + 1)).toString());
            quest.addChoice((new Integer(y + 2)).toString());
            quest.addChoice((new Integer(y + 3)).toString());
            if(y == 0)
                quest.addChoice((new Integer(y + 4)).toString());
            else
                quest.addChoice((new Integer(y)).toString());
            quest.setAnswer(1);
            qIndex++;
            createSnapShot(-1, captionVect, true);
            captionVect.removeAllElements();
            for(int z = 0; z < tableSize; z++){
                if((((arraySlot)Table.elementAt(z)).color.equals("\\R"))||(((arraySlot)Table.elementAt(z)).color.equals("\\M"))
                    ||(((arraySlot)Table.elementAt(z)).color.equals("\\B"))){
                        if(((arraySlot)Table.elementAt(z)).list.numNodes == 0)
                            ((arraySlot)Table.elementAt(z)).color = "\\W";
                        else
                            ((arraySlot)Table.elementAt(z)).color = "\\Y";
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
                    out.println(((arraySlot)Table.elementAt(x)).color+" "+((arraySlot)Table.elementAt(x)).list.toString());
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
            try{
                out.close();
            }catch(Exception f){}
        }
    }
    public static void main(String args[]){
        new LinkedHashing(args[0], args[1], args[2]);
        System.exit(0);
    }
    class arraySlot{
        String color;
        linkedList list;
        public arraySlot(String color){
            this.color = color;
            list = new linkedList();
        }
    }
    class linkedList{
        Vector nodes;
        int numNodes = 0;
        public linkedList(){
            nodes = new Vector();
        }
        public String toString(){
            String theStr = "";
            for(int x = 0; x < numNodes; x++){
                theStr = theStr + (Integer)nodes.elementAt(x);
                theStr = theStr + " -> ";       
            }
            theStr = theStr + "nil";
            return theStr;
        }
        public void addNode(int node){
            nodes.addElement(new Integer(node));
            numNodes++;
        }
    }
}
