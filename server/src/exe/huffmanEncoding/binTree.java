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

import java.io.PrintWriter;
import java.util.Vector;
import exe.*;

public class binTree{
    public char letter;
    public int frequency;
    public String code;
    public binTree leftChild = null;
    public binTree rightChild = null;
    public binTree parent = null;
    public Vector letters;
    
    public binTree(char c, int f, binTree lc, binTree rc){
        letter = c;
        frequency = f;
        code = "";
        leftChild = lc;
        rightChild = rc;
        letters = new Vector();
        
        if(leftChild != null){
            leftChild.parent = this;
            leftChild.updateCodes("0");
        }
        if(rightChild != null){
            rightChild.parent = this;
            rightChild.updateCodes("1");
        }
    }
    public void updateCodes(String oneORzero){
        code = oneORzero + code;
        if(leftChild != null)
            leftChild.updateCodes(oneORzero);
        if(rightChild != null)
            rightChild.updateCodes(oneORzero);
    }
    public String getCode(char l){
        if(l == letter)
            return code;
        String c;
        if(leftChild != null){
            c = leftChild.getCode(l);
            if(!c.equals("."))
                return c;
        }
        if(rightChild != null){
            c = rightChild.getCode(l);
            if(!c.equals("."))
                return c;
        }
        return ".";
    }
    public char getLetter(String c){
        if(code.equals(c))
            return letter;
        char l;
        if(leftChild != null){
            l = leftChild.getLetter(c);
            if(l != ' ')
                return l;
        }
        if(rightChild != null){
            l = rightChild.getLetter(c);
            if(l != ' ')
                return l;
        }
        return ' ';
    }
    public boolean codeIsOfALetter(String c){
        if(c == null)
            return false;
        if(c.equals(code)&&(letter != ' '))
            return true;
        else if((leftChild != null)&&leftChild.codeIsOfALetter(c))
            return true;
        else if((rightChild != null)&&rightChild.codeIsOfALetter(c))
            return true;
        else return false;
    }
    public boolean contains(String c){
        if(c == null)
            return false;
        if(code.equals(c))
            return true;
        else if((leftChild != null)&&leftChild.contains(c))
            return true;
        else if((rightChild != null)&&rightChild.contains(c))
            return true;
        return false;
    }
    public String getDescription(){
        letters.removeAllElements();
        fillLettersVect();
        if(letters.size() == 1)
            return ("the tree that contains '"+(String)letters.elementAt(0))+"'";
        else if(letters.size() == 2)
            return ("the tree that contains '"+(String)letters.elementAt(0)+
                    "' and '"+(String)letters.elementAt(1)+"'");
        else{
            String desc = "the tree that contains ";
            for(int x = 0; x < letters.size(); x++){
                if(x == letters.size()-1)
                    desc = desc + " and '"+(String)letters.elementAt(x)+"'";
                else
                    desc = desc + " '"+(String)letters.elementAt(x)+"',";
            }
            return desc;
        }
    }
    public void fillLettersVect(){
        if(letter != ' ')
            letters.addElement(""+letter);
        if(rightChild != null){
            rightChild.fillLettersVect();
            for(int x = 0; x < rightChild.letters.size(); x++){
                if(!letters.contains(rightChild.letters.elementAt(x)))
                    letters.addElement(rightChild.letters.elementAt(x));
            }
        }
        if(leftChild != null){
            leftChild.fillLettersVect();
            for(int x = 0; x < leftChild.letters.size(); x++){
                if(!letters.contains(leftChild.letters.elementAt(x)))
                    letters.addElement(leftChild.letters.elementAt(x));
            }
        }
    }
    public void preOrderTraversal(PrintWriter out, int level, char flagChar, boolean encodeMode){
        try{
            out.println(level);
            if(!encodeMode){
                if(letter == ' ')
                    out.println("\\W.");
                else
                    out.println("\\Y"+letter);
                out.println("freq: "+frequency);
                out.println(code+" ");
                out.println(" ");
            }
            else{
                if(letter == flagChar){
                    out.println("\\R.");
                    if(letter == ' ')
                        out.println("\\W.");
                    else
                        out.println(""+letter);
                }
                else{
                    if(letter == ' ')
                        out.println("\\W.");
                    else
                        out.println("\\Y.");
                    out.println(""+letter);
                }
                out.println(" ");
            }
            if(leftChild != null)
                leftChild.preOrderTraversal(out, level+1, flagChar, encodeMode);
            if(rightChild != null)
                rightChild.preOrderTraversal(out, level+1, flagChar, encodeMode);
        }
        catch(Exception e){
            System.out.println(e.toString() + " thrown from preOrderTraversal.");
            e.printStackTrace();
        }
    }
}
        
