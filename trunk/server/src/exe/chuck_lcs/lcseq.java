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

package exe.chuck_lcs;

import java.io.*;
import exe.*;
import exe.ShowFile;
import exe.GAIGStree;
import exe.TreeNode;
import exe.GAIGSarray;
import exe.GAIGStext;
import exe.XMLmcQuestion;
import exe.XMLfibQuestion;
import java.util.*;

public class lcseq {
    private ShowFile show;
    private final String docURL = "lcsInfo.htm";
    private GAIGSarray lcsMat;
    private GAIGStext message;
    private GAIGStree vizTree;
    private XMLfibQuestion fibQ;
    private XMLmcQuestion mcQ;
    private int qId;
    private char[] s1;
    private char[] s2;
    private int[][] lcs;
    private final String TITLE = "Longest Common Subsequence Problem";
    private String pc;

    public lcseq(char[] seq1, char[] seq2, ShowFile s) {
        s1 = seq1;  
        s2 = seq2;
        qId = 0;
        lcs = new int[s1.length+1][s2.length+1];
        show = s;
    }

    public void recur(int i, int j) throws IOException {    
        vizTree = new GAIGStree(false, " ", "#FFFF00", 0.1, 0.3, 0.9, 0.9, 0.06);
        TreeNode root = new TreeNode(i + "  " + j);
        vizTree.setRoot(root);
        message = new GAIGStext(0.1, -0.7, "Recursive Call Tree for the LCS Problem");
        message.setFontsize(0.07);
        message.setColor("#730063");
        vizTree.setSpacing(1.0, 1.75);
        recurLCS(i,j, root);
        //String audio = "The visualization illustrates the recursive call tree for this problem";
        show.writeSnap(TITLE, 0.1, docURL, pc, message, vizTree);
    }
        
    public void recurLCS(int i, int j, TreeNode parent) {
        TreeNode newNode = null;
        String labelNode = "";
        if (i == -1 || j == -1) 
            labelNode = "";
        else
            if (s1[i] == s2[j]) {
                if (i >0 && j>0) {
                    labelNode = (i-1) + "  " + (j-1);
                    newNode = new TreeNode(labelNode);
                    parent.setChildWithEdge(newNode);
                }
                recurLCS(i-1, j-1, newNode);
            }
            else {
                if (i >0) {
                    newNode = new TreeNode(i-1 + "  " + j);
                    parent.setChildWithEdge(newNode);
                }
                recurLCS(i-1, j, newNode);
            
                if (j > 0) {
                    newNode = new TreeNode(i + "  " + (j-1));
                    parent.setChildWithEdge(newNode);
                }
                recurLCS(i, j-1, newNode);   
            }
    }

    public void dynProgLCS() throws IOException {
        //qId++;
        int cnt1 = 0;
	int cnt2 = 0;
        int m = s1.length;
        int n = s2.length;
        String audio = "The matrix with the empty sequence initializations";
        lcsMat = new GAIGSarray(m+1, n+1,"LCS Dynamic Programming Matrix","#FFFFFF",0.2, 0.1, 0.8, 0.6, 0.1);
        message = new GAIGStext(0.4, -0.3, "Matrix after initialization");
        message.setFontsize(0.06);
        message.setColor("#730063");
        int b = 0;
        // initial row and column of zeros
        for (int i = 0; i <= m; i++) {
            lcs[i][0] = 0;
            lcsMat.set(lcs[i][0], i, 0);
        }
        for (int j = 0; j <= n; j++) {
            lcs[0][j] = 0;
            lcsMat.set(lcs[0][j], 0, j);
        }
        // add first subsequence as row labels
        for (int i1 = 1; i1 <= m; i1++) {
            String lbl = s1[i1-1] + "";
            lcsMat.setRowLabel(lbl,i1);
        }   
        // add second subsequence as column labels
        for (int j1 = 1; j1 <= n; j1++) {
            String lbl = s2[j1-1] + "";
            lcsMat.setColLabel(lbl,j1);
        }
	pc = genPseudo(1, 1);
        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, lcsMat);
        boolean mcqSet = false;
        boolean fibqSet = false;    
        for (int r = 1; r <= m; r++) 
            for (int c = 1; c <= n; c++) {
                double rand;
                rand = Math.random();
                if(rand < .1) { 
		    mcQ = new XMLmcQuestion(show, qId + ""); 
                    buildmcQues(mcQ, r, c);
		    qId++;
                    mcqSet = true;
                }
                if(rand > .9) { 
		    fibQ = new XMLfibQuestion(show, qId + ""); 
                    buildfibQues(fibQ, r);
                    qId++;
                    fibqSet = true;
                }    
                String l = "Matching " + s1[r-1];
                if (s1[r-1] == s2[c-1]) {
		    pc = genPseudo(1, 5);
                    lcs[r][c] = lcs[r-1][c-1] + 1;
                    lcsMat.setColor(r-1,c-1,"#C6EFF7");
		    
                    audio = "the sequences match with the character " + s1[r-1];
                    message.setText(l + ": match - add 1 to value on diagonal above");
                    b=1;
                }
                else {
		    pc = genPseudo(1, 7);
                    lcsMat.setColor(r-1,c,"#C6EFF7");
                    lcsMat.setColor(r,c-1,"#C6EFF7");
		    
                    audio = "the character " + s2[c-1] + " in sequence 2 does not match " + s1[r-1];
                    message.setText(l + ": no match - max of cell above and cell to left");
                    b=2;
                    lcs[r][c] = Math.max(lcs[r-1][c], lcs[r][c-1]);
                }  
                lcsMat.set(lcs[r][c], r, c,"#FFFF9C");
                if (mcqSet || fibqSet) {
                    if ((r == 1 || r == m) && c == (c/2 *2))
                        if (mcqSet) 
                            show.writeSnap(TITLE, 0.1, docURL, pc, audio, mcQ, message, lcsMat);
                        else 
                            show.writeSnap(TITLE, 0.1, docURL, pc, fibQ, message, lcsMat);
                    else
                        if (mcqSet)
                            show.writeSnap(TITLE, 0.1, docURL, pc, mcQ, message, lcsMat);
                        else
                            show.writeSnap(TITLE, 0.1, docURL, pc, fibQ, message, lcsMat);
                    mcqSet = false;
                    fibqSet = false;
                }
                else
                   if ((r == 1 || r == m) && c == (c/2 *2))
                        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, lcsMat);
                   else     
                        show.writeSnap(TITLE, 0.1,docURL, pc, message, lcsMat);
                lcsMat.setColor(r,c, "#FFFFFF");
                if (b == 2) {
                    lcsMat.setColor(r-1,c,"#FFFFFF");
                    lcsMat.setColor(r,c-1,"#FFFFFF");
                }
                else 
                    lcsMat.setColor(r-1,c-1,"#FFFFFF");
            }
            message.setText("The length of longest subsequence is in the bottom right cell");
            audio = "The longest common subsequence has length " + lcs[m][n];
            lcsMat.setColor(m,n,"#FF0000");
            show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, lcsMat);
	    if (lcs[m][n] > 0)
           	 LCSoln();
    }

        private void buildmcQues(XMLmcQuestion mcQ, int r, int c) {
            mcQ.setQuestionText("The subproblem currently being solved is?");
            mcQ.addChoice(buildStr(r-1, c));
            mcQ.addChoice(buildStr(r, c-1));
            mcQ.addChoice(buildStr(r-1, c-1));
            mcQ.addChoice(buildStr(r, c));
            mcQ.setAnswer(4);
    }

    private void buildfibQues(XMLfibQuestion fibQ, int row) {
       fibQ.setQuestionText("The index of the current character from seq1 being matched is ___");
       String str = ""+ (row-1);
       fibQ.setAnswer(str);
    }
    
    private String buildStr(int x, int  y) {
        String ch = "LCS[" + x + "][" + y + "]";
        return ch;
    }

    private String genPseudo(int algo, int line) {
        if (algo == 1)
            return "index.php?line=" + line;
        else
            return "lcsSoln.php?line=" + line;
   }

    public void LCSoln() throws IOException {
	pc = genPseudo(2, 1);
        System.out.println("The solution is ");
        String audio = "Locating the characters that make up the longest common subsequence";
        int m = s1.length;
        int n = s2.length;
        int idx = lcs[m][n];
        char[] soln = new char[idx];
        show.writeSnap(TITLE, 0.1,  docURL, pc, audio, message, lcsMat);
        while (lcs[m][n] >= 1) {            
            int val = lcs[m][n];
            while(lcs[m-1][n] == val) {
                lcsMat.setColor(m,n,"#FFB573");
                m--;
                message.setText("Searching for component of solution");
		pc = genPseudo(2, 4);
                show.writeSnap(TITLE, 0.1, docURL, pc, message, lcsMat);
            }
            lcsMat.setColor(m,n,"#FFB573");
            message.setText("Searching for component of solution");
            show.writeSnap(TITLE, 0.1, docURL, pc, message, lcsMat);
            while (n>0 && lcs[m][n-1] == val){
                n--;
                lcsMat.setColor(m,n,"#FFB573");
		pc = genPseudo(2, 6);
                show.writeSnap(TITLE, 0.1, docURL, pc, message, lcsMat);
            }
            soln[idx-1] = s1[m-1];
            idx--;
            lcsMat.setColor(m,n,"#C6EFF7");
            message.setText("Found an element of the solution");
	    pc = genPseudo(2, 7);
            show.writeSnap(TITLE, 0.1, docURL, pc, message, lcsMat);
            // move up to next possible matching character
            m--;
            lcsMat.setColor(m,n,"#FFB573");
	    pc = genPseudo(2, 8);
            show.writeSnap(TITLE, 0.1, docURL, pc, message, lcsMat);
            while (n>0 && lcs[m][n-1] == val-1){
                n--;
                lcsMat.setColor(m,n,"#FFB573");
		pc = genPseudo(2, 10);
                show.writeSnap(TITLE, 0.1,  docURL, pc, message, lcsMat);
           }
        }
        String sln = "[";
        //audio = "The solution is ";
        for(int q = 0; q < soln.length-1; q++) {
            sln += soln[q]+", ";
            audio += soln[q] + "  ";
        }
        sln += soln[soln.length-1] +"]";
        //audio += soln[soln.length-1] + "  ";
        //audio += "it is marked in blue on the screen    ";
	audio = "The solution is marked in blue on the screen    ";
        System.out.println(sln);
        message.setText("Solution = " + sln + " - marked in blue");
        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, lcsMat);      
    }               
}
				
