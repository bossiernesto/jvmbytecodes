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

package exe.huffmanDecoding;

import java.io.*;
import java.util.*;
import exe.*;

public class HuffmanTrees{
    private PrintWriter out;
    private String word;
    private questionCollection Questions;
    private Vector trees;
    private int qIndex = 0;
    private String code;
    private final int MAX_NUM_DIF_CHARS = 10;
    private final String phraseFile = "exe/huffmanDecoding/PhrasesToDecode.txt";
    private Random r;
    
    public HuffmanTrees(String GaigsFile, String wordSource){
        trees = new Vector();
        r = new Random();
        try{
            if(wordSource.trim().equalsIgnoreCase("randomly"))
                GenerateRandomWord();
            else
                ChooseFromFile();
            out = new PrintWriter(new BufferedWriter(new FileWriter(GaigsFile)));
            Questions = new questionCollection(out);
            GetFrequenies();
            CreateHuffmanTree();
            EncodeWord();
            DecodeWord();
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
    private void GenerateRandomWord(){
        int l;
        word = "";
        for(int x = 0; x < 10; x++){
            l = r.nextInt()%6;
            if(l < 0) l *= -1;
            if(l == 0) word = word + "a";
            else if(l == 1) word = word + "b";
            else if(l == 2) word = word + "c";
            else if(l == 3) word = word + "d";
            else if(l == 4) word = word + "e";
            else if(l == 5) word = word + "f";
            else word = word + "g";
        }
    }
    private void ChooseFromFile(){
        Vector phrases = new Vector();
        try{
            BufferedReader in = new BufferedReader(new FileReader(phraseFile));
            String temp = in.readLine();
            while(temp != null){
                if(!temp.trim().equals(""))
                    phrases.addElement(temp.trim());
                temp = in.readLine();
            }
            int w = r.nextInt();
            if(w < 0) w *= -1;
            w %= phrases.size();
            word = (String)phrases.elementAt(w);
            
            in.close();
        }catch(Exception e){
            System.out.println(e.toString() + " thrown from ChooseFromFile()");
            e.printStackTrace();
            try{
                System.in.read();
            }catch(Exception t){}
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
        sortVector();
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
            if(trees.size() > 1){
                sortVector();
            }
        }
    }
    private void EncodeWord(){
        code = "";
        String lettercode;
        char c;
        for(int x = 0; x < word.length(); x++){
            c = word.charAt(x);
            lettercode = ((binTree)trees.elementAt(0)).getCode(c);
            code = code + lettercode;
        }
    }
    private void DecodeWord(){
        Vector captionVect = new Vector();
        String c;
        int place = 0;
        String decodeStr = "";
        char l;
        captionVect.addElement("Here is a Huffman Tree.");
        captionVect.addElement("You must decode the following bit string:");
        captionVect.addElement(code);
        createSnapShot(-1, captionVect, false, '\0', null);
        captionVect.removeAllElements();
        fibQuestion q = new fibQuestion(out, (new Integer(qIndex)).toString());
        q.setQuestionText("What letter will be decoded next?");
        Questions.addQuestion(q);
        captionVect.addElement("Decode the bit string: ");
        captionVect.addElement(code);
        createSnapShot(qIndex, captionVect, false, '\0', null);
        captionVect.removeAllElements();
        for(int x = 0; x < code.length(); x++){
            c = code.substring(place, x + 1);
            if(((binTree)trees.elementAt(0)).codeIsOfALetter(c)){
                l = ((binTree)trees.elementAt(0)).getLetter(c);
                q.setAnswer(""+l);
                qIndex++;
                if(x < code.length() - 1){
                    q = new fibQuestion(out, (new Integer(qIndex)).toString());
                    q.setQuestionText("What letter will be decoded next?");
                    Questions.addQuestion(q);
                }
                captionVect.addElement("Decode the bit string:");
                captionVect.addElement(code.substring(place, code.length()));
                captionVect.addElement("The code '"+c+"' is for the letter '"
                    +l+"'.");
                decodeStr = decodeStr + l;
                captionVect.addElement("The decoded string is: "+decodeStr);
                createSnapShot(-1, captionVect, false, '\0', c);
                captionVect.removeAllElements();
                place = x+1;
                if(x < code.length() - 1){
                    captionVect.addElement("Decode the bit string:");
                    captionVect.addElement(code.substring(place, code.length()));
                    createSnapShot(qIndex, captionVect, false, '\0', null);
                    captionVect.removeAllElements();
                }
            }
            else{
                captionVect.addElement("Decode the bit string:");
                captionVect.addElement(code.substring(place, code.length()));
                captionVect.addElement("The code '"+c+"' is not for a letter, so search down the tree.");
                captionVect.addElement("The decoded string is: "+decodeStr);
                createSnapShot(-1, captionVect, false, '\0', c);
                captionVect.removeAllElements();
            }
        }
        captionVect.addElement("Done decoding.");
        captionVect.addElement("The decoded string is: "+decodeStr);
        createSnapShot(-1, captionVect, false, '\0', null);
        captionVect.removeAllElements();
    }
    private void createSnapShot(int questionIndex, Vector captionVect, boolean drawTopNode, char flagChar, String traceCode){
        try{
            out.println("VIEW WINDOWS 1");
            out.println("VIEW DOCS huffmanDecoding.html");
            if(questionIndex != -1)
                Questions.insertQuestion(questionIndex);
            out.println("GeneralTree 0.01 0.01");
            out.println("4 1.15 1.15");
            for(int x = 0; x < captionVect.size(); x++)
                out.println((String)captionVect.elementAt(x));
            out.println("***\\***");
            if(drawTopNode){
                out.println("0");
                out.println("\\XThe");
                out.println("Huffman");
                out.println("Codes");
                out.println(" ");
                for(int x = 0; x < trees.size(); x++){
                    ((binTree)trees.elementAt(x)).preOrderTraversal(out, 1, flagChar, traceCode);
                } 
            }
            else{
                for(int x = 0; x < trees.size(); x++){
                    ((binTree)trees.elementAt(x)).preOrderTraversal(out, 0, flagChar, traceCode);
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
