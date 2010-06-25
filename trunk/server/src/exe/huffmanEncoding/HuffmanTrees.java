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

package exe.huffmanEncoding;

import java.io.*;
import java.util.*;
import exe.*;

public class HuffmanTrees{
    private PrintWriter out;
    private String word;
    private questionCollection Questions;
    private mcQuestion q;
    private Vector trees;
    private int qIndex = 0;
    private String code;
    private final int MAX_NUM_DIF_CHARS = 10;
    private Random r;
    
    public HuffmanTrees(String GaigsFile, String word){
        this.word = word;
        trees = new Vector();
        r = new Random();
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(GaigsFile)));
            Questions = new questionCollection(out);
            GetFrequenies();
            CreateHuffmanTree();
            EncodeWord();
            Questions.writeQuestionsAtEOSF();
            out.close();
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from construct HuffmanTrees");
            e.printStackTrace();
            try{
                out.close();
            }catch(Exception f){}
        }    
    }
    private void GetFrequenies(){
        boolean foundletter;
        int difchars = 0;
        for(int x = 0; x < word.length(); x++){
            foundletter = false;
            for(int y = 0; y < trees.size() && !foundletter; y++){
                if( ((binTree)trees.elementAt(y)).letter == word.charAt(x) ){
                    foundletter = true;
                    ((binTree)trees.elementAt(y)).frequency++;
                }
            }
            if(!foundletter){
                difchars++;
                if(difchars > MAX_NUM_DIF_CHARS){
                    word = word.substring(0, x);
                    break;
                }
                trees.addElement(new binTree(word.charAt(x), 1, null, null));
            }
        }
        Vector captionVect = new Vector();
        
        captionVect.addElement("Count the frequencies of each letter");
        captionVect.addElement("in the string:  "+word);
        mcQuestion q = new mcQuestion(out, (new Integer(qIndex)).toString());
        if(trees.size()>2){
            Questions.addQuestion(q);
            q.setQuestionText("Which two trees will be joined next?");
            createSnapShot(qIndex, captionVect, false, '\0');
        }
        else
            createSnapShot(-1, captionVect, false, '\0');
        sortVector();
        captionVect.removeAllElements();
        captionVect.addElement("Sort from least common letter to most common.");
        createSnapShot(-1, captionVect, false, '\0');
        if(trees.size()>2){
            int freq0 = ((binTree)trees.elementAt(0)).frequency;
            int freq1 = ((binTree)trees.elementAt(1)).frequency;
            q.addChoice( ((binTree)trees.elementAt(0)).getDescription()+" and "
                +((binTree)trees.elementAt(1)).getDescription() );
            Vector WrongChoices = new Vector();
            for(int x = 0; x < trees.size(); x++){
                for(int y = x+1; y < trees.size(); y++){
                    if( (y>1)&&
                        !( ((((binTree)trees.elementAt(y)).frequency == freq0)
                            &&(((binTree)trees.elementAt(x)).frequency == freq1))||
                            ((((binTree)trees.elementAt(x)).frequency == freq0)
                            &&(((binTree)trees.elementAt(y)).frequency == freq1)) ))
                        WrongChoices.addElement( ((binTree)trees.elementAt(y)).getDescription()+
                            " and "+((binTree)trees.elementAt(x)).getDescription() );
                }
            }
            q.setAnswer(1);
            int i;
            for(int x = 0; (WrongChoices.size() > 0)&&(x < 4); x++){
                if(WrongChoices.size() > 1){
                    i = r.nextInt()%WrongChoices.size();
                    if(i < 0) i *= -1;
                }
                else i = 0;
                q.addChoice((String)WrongChoices.elementAt(i));
                WrongChoices.removeElementAt(i);    
            }
            qIndex++;
        }
    }
    private void sortVector(){
        int n = trees.size() - 1;
        boolean swapped = true;
        for(int x = 0; x < trees.size() && swapped; x++){
            swapped = false;
            for(int y = 0; y < n; y++){
                if( ((binTree)trees.elementAt(y)).frequency > ((binTree)trees.elementAt(y+1)).frequency ){
                    swapped = true;
                    binTree temp = (binTree)trees.elementAt(y);
                    trees.setElementAt(trees.elementAt(y+1), y);
                    trees.setElementAt(temp, y+1);
                }
            }
            n--;
        }
    }
    private void CreateHuffmanTree(){
        
        while(trees.size() > 1){
            binTree left = (binTree)trees.elementAt(0);
            binTree right = (binTree)trees.elementAt(1);
            trees.removeElementAt(0);
            trees.setElementAt(new binTree(' ', left.frequency + right.frequency, left, right), 0);
            Vector captionVect = new Vector();
            captionVect.addElement("Join the two nodes with the smallest frequencies.");
            captionVect.addElement("Append '0' to the codes for the letters in the left subtree.");
            captionVect.addElement("Append '1' to the codes for the letters in the right subtree.");
            mcQuestion q = new mcQuestion(out, (new Integer(qIndex)).toString());
            if(trees.size()>2){
                Questions.addQuestion(q);
                q.setQuestionText("Which two trees will be joined next?");
                createSnapShot(qIndex, captionVect, false, '\0');
            }
            else
                createSnapShot(-1, captionVect, false, '\0');
            if(trees.size() > 1){
                sortVector();
                if(trees.size()>2){
                    int freq0 = ((binTree)trees.elementAt(0)).frequency;
                    int freq1 = ((binTree)trees.elementAt(1)).frequency;
                    q.addChoice( ((binTree)trees.elementAt(0)).getDescription()+" and "
                        +((binTree)trees.elementAt(1)).getDescription() );
                    Vector WrongChoices = new Vector();
                    for(int x = 0; x < trees.size(); x++){
                        for(int y = x+1; y < trees.size(); y++){
                            if( (y>1)&& 
                            !( ((((binTree)trees.elementAt(y)).frequency == freq0)
                            &&(((binTree)trees.elementAt(x)).frequency == freq1))||
                            ((((binTree)trees.elementAt(x)).frequency == freq0)
                            &&(((binTree)trees.elementAt(y)).frequency == freq1)) ))
                                WrongChoices.addElement( ((binTree)trees.elementAt(y)).getDescription()+
                                    " and "+((binTree)trees.elementAt(x)).getDescription() );
                        }
                    }
                    q.setAnswer(1);
                    int i;
                    for(int x = 0; (WrongChoices.size() > 0)&&(x < 4); x++){
                        if(WrongChoices.size() > 1){
                            i = r.nextInt()%WrongChoices.size();
                            if(i < 0) i *= -1;
                        }
                        else i = 0;
                        q.addChoice((String)WrongChoices.elementAt(i));
                        WrongChoices.removeElementAt(i);    
                    }
                    qIndex++;
                }
                captionVect.removeAllElements();
                captionVect.addElement("Sort according to frequencies.");
                createSnapShot(-1, captionVect, false, '\0');
            }
        }
    }
    private void EncodeWord(){
        code = "";
        String lettercode;
        char c;
        Vector seenChars = new Vector();
        fibQuestion q = new fibQuestion(out, (new Integer(qIndex)).toString());
        Questions.addQuestion(q);
        Vector captionVect = new Vector();
        captionVect.addElement("The Huffman Tree is complete.");
        captionVect.addElement("Now compute the code for the string: "+word);
        createSnapShot(qIndex, captionVect, true, '\0');
        captionVect.removeAllElements();
        for(int x = 0; x < word.length(); x++){
            c = word.charAt(x);
            if(!seenChars.contains(""+c)){
                seenChars.addElement(""+c);
                q.setQuestionText("What will be the bit string code for the letter '"+c+"'?");
                lettercode = ((binTree)trees.elementAt(0)).getCode(c);
                q.setAnswer(lettercode);
                captionVect.addElement("The code for '"+c+"' is: "+lettercode);
                code = code + lettercode;
                captionVect.addElement("The code for the string '"
                        +word.substring(0, x+1)+"' is:");
                captionVect.addElement(""+code);
                if(x == word.length()-1){
                    captionVect.addElement("Done encoding.");
                    createSnapShot(-1, captionVect, true, c);
                }
                else{
                    if(!seenChars.contains(""+word.charAt(x+1))){
                        qIndex++;
                        q = new fibQuestion(out, (new Integer(qIndex)).toString());
                        Questions.addQuestion(q);
                        createSnapShot(qIndex, captionVect, true, c);
                    }
                    else
                        createSnapShot(-1, captionVect, true, c);
                }   
                captionVect.removeAllElements();
            }
            else{
                lettercode = ((binTree)trees.elementAt(0)).getCode(c);
                captionVect.addElement("The code for '"+c+"' is: "+lettercode);
                code = code + lettercode;
                captionVect.addElement("The code for the string '"
                        +word.substring(0, x+1)+"' is:");
                captionVect.addElement(""+code);
                
                if(x == word.length()-1){
                    captionVect.addElement("Done encoding.");
                    createSnapShot(-1, captionVect, true, c);
                }
                else{
                    if(!seenChars.contains(""+word.charAt(x+1))){
                        qIndex++;
                        q = new fibQuestion(out, (new Integer(qIndex)).toString());
                        Questions.addQuestion(q);
                        createSnapShot(qIndex, captionVect, true, c);
                    }
                    else
                        createSnapShot(-1, captionVect, true, c);
                }
                captionVect.removeAllElements();
            }
        }
    }
    private void createSnapShot(int questionIndex, Vector captionVect, boolean encodeMode, char flagChar){
        try{
            out.println("VIEW WINDOWS 1");
            out.println("VIEW DOCS huffmanEncoding.html");
            if(questionIndex != -1)
                Questions.insertQuestion(questionIndex);
            out.println("GeneralTree 0.01 0.01");
            if(!encodeMode){
                out.println("4 1.15 1.15");
                for(int x = 0; x < captionVect.size(); x++)
                    out.println((String)captionVect.elementAt(x));
                out.println("***\\***");
                out.println("0");
                out.println("\\XThe");
                out.println("Huffman");
                out.println("Codes");
                out.println(" ");
                for(int x = 0; x < trees.size(); x++){
                    ((binTree)trees.elementAt(x)).preOrderTraversal(out, 1, flagChar, encodeMode);
                } 
            }
            else{
                out.println("3 1.15 1.15");
                for(int x = 0; x < captionVect.size(); x++)
                    out.println((String)captionVect.elementAt(x));
                out.println("***\\***");
                for(int x = 0; x < trees.size(); x++){
                    ((binTree)trees.elementAt(x)).preOrderTraversal(out, 0, flagChar, encodeMode);
                }
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
        System.out.println(args[0]);
        System.out.println(args[1]);
        new HuffmanTrees(args[0], args[1]);
        System.exit(0);
    }
}
