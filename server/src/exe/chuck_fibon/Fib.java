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

package exe.chuck_fibon;

import exe.*;
import exe.ShowFile;
import exe.GAIGStree;
import exe.TreeNode;
import exe.GAIGSarray;
import exe.GAIGStext;
import java.io.*;
import java.util.*;
import javax.swing.*;
import exe.XMLfibQuestion;

public class Fib {
    private int n;
    private ShowFile show;
    private GAIGSarray fibMat;
    private GAIGStext message;
    private GAIGStext message2;
    private GAIGStree vizTree;
    private final String TITLE = "Fibonacci Numbers";
    private int[] f;
    private ArrayList<TreeNode> nLst;
    private String audio;
    private String docURL = "FibonacciInfo.htm";
    private String pc;
    
    public Fib(int n1, ShowFile s) {
        n = n1;
        show = s;
        f = new int[n+1];
        nLst = new ArrayList<TreeNode>();
        audio = "";
	pc = "";
    }
    
    public void solve() throws IOException {
        fibMat = new GAIGSarray(n+1, false, "","#FFFFFF",0.8, 0.3, 1.2, 0.9, 0.09);
        message = new GAIGStext(1.3, -0.2, "Initializing f[0]");
        message.setFontsize(0.06);
        message.setColor("#730063");
        double place = (-n/20.0);
        message2 = new GAIGStext(0.05, place, "Recursive Call Tree for computing fib["+n+"]");
        message2.setFontsize(0.06);
        message2.setColor("#730063");
        message.setHalign(2);
        for (int j=0; j <= n; j++) {
            f[j] = -1;
            fibMat.set(f[j],j);
            String lbl = j + "";
            fibMat.setRowLabel(lbl, j);
        }
        f[0] = 1;
        f[1] = 1;
//         String resp = JOptionPane.showInputDialog("If you want to change the base case values enter y for yes");
//         if (resp.charAt(0) == 'y' || resp.charAt(0) == 'Y') {
//             String b1 = JOptionPane.showInputDialog("Enter value for f[0]");
//             f[0] = Integer.parseInt(b1);
//             b1 = JOptionPane.showInputDialog("Enter value for f[1]");
//             f[1] = Integer.parseInt(b1);
//         }
        fibMat.set(f[0], 0);
        pc = "index.php?line=1";
        show.writeSnap(TITLE, 0.08, docURL, pc,  message, vizTree, fibMat);
        fibMat.set(f[1], 1);
        pc = "index.php?line=2";
        message = new GAIGStext(1.3, -0.2, "Initializing f[1]");
        show.writeSnap(TITLE, 0.08, docURL,pc,  message, vizTree, fibMat);
        
        message.setText("The dynamic programming matrix with initializations");
        audio = "In the dynamic program you use the two previous cells of the dynamic programming matrix to compute the next fibonacci number - the two previous cells are colored blue in the matrix and the new value in yellow";
        show.writeSnap(TITLE, 0.08, docURL, pc, audio, message, message2, vizTree, fibMat);
        audio = " simultaneously in the recursive call tree the nodes corresponding to calls for the two previous cells of the matrix are colored blue to illustrate the number of calls you are saving";
        show.writeSnap(TITLE, 0.08, docURL, pc, audio, message, message2, vizTree, fibMat);
        int qId = 0;
        XMLfibQuestion fibQ = new XMLfibQuestion(show, qId + "");
        for (int k = 2; k <= n; k++) {
	    pc = "index.php?line=3";
	    message2.setText("");
	    show.writeSnap(TITLE, 0.08, docURL, pc,  message, message2, vizTree, fibMat); 
            f[k] = f[k-1] + f[k-2];
            pc = "index.php?line=4";
            fibMat.setColor(k-1, "#C6EFF7");
            fibMat.setColor(k-2, "#C6EFF7");
            if (k ==3 && 3 < n-1) {
                fibQ.setQuestionText("The next fibonacci number is ");
                fibQ.setAnswer(f[3]+"");
                show.writeSnap(TITLE, 0.08, docURL, pc, fibQ, message, message2, vizTree, fibMat);
                qId++;
            }
            if (k ==n-1 && 3 < n-1) {
                fibQ.setQuestionText("The next fibonacci number is ");
                fibQ.setAnswer(f[n-1]+"");
                show.writeSnap(TITLE, 0.08, docURL, pc, fibQ, message, message2, vizTree, fibMat);
                qId++;
            }
            fibMat.set(f[k], k, "#FFFF9C"); 
            int s = k-1;
            int t = k-2;
            String fText = "f[" + k + "]= " + "f[" + s + "]+ " + "f[" + t + "] ";
            message.setText(fText);
            String labelx = (k-1)+"";
            colorList(labelx);
            String labely = (k-2)+"";
            colorList(labely);
            message2.setText("Highlighted in blue are the recursive calls made to " + labelx + " and " + labely);
            show.writeSnap(TITLE, 0.08, docURL, pc, message, message2, vizTree, fibMat);
            fibMat.setColor(k, "#FFFFFF");
            fibMat.setColor(k-1, "#FFFFFF");
            fibMat.setColor(k-2, "#FFFFFF");
            uncolorList(labelx);
            uncolorList(labely);
        }
        message.setText("The fibonacci number f[" + n + "] is " + f[n]);
        message2.setText("Recursive Call Tree for computing fib["+n+"]");
        fibMat.setColor(n,"#FF0000");
        audio = "The value of the fibonacci number  " + n;
        audio += "  computes to be " + f[n];
        show.writeSnap(TITLE, 0.08, docURL, pc, audio, message, message2, vizTree, fibMat);
    }
    
    public void recur(int n) throws IOException {
        vizTree = new GAIGStree(false, "", "#FFFF00", -0.225, 0.4, 0.25, 0.9, 0.09);
        TreeNode root = new TreeNode(n + "");
        vizTree.setRoot(root);
        message2 = new GAIGStext(0.05, -0.3, "Recursive Call Tree for computing fib["+n+"]");
        message2.setFontsize(0.06);
        message2.setColor("#730063");
        vizTree.setSpacing(1.1, 1.75);
        audio = "The recursive call tree for the finding the fibonacci number " + n;
        fibRecur(n, root);
        show.writeSnap(TITLE, 0.08, docURL, null, audio, message2, vizTree);
        DFS(root);
    }
    
    private void fibRecur(int n, TreeNode parent) {
        TreeNode newNode = null;
        String labelNode = "";
        if (n == 1 || n == 0)
            labelNode = "";
        else {
            labelNode = (n-1) + "";
            newNode = new TreeNode(labelNode);
            parent.setChildWithEdge(newNode);
            fibRecur(n-1, newNode);
            labelNode = (n-2) + "";
            newNode = new TreeNode(labelNode);
            parent.setChildWithEdge(newNode);
            fibRecur(n-2, newNode);
        }
    }       
   
    private void DFS(TreeNode src) {
        
        if (src != null) {
           nLst.add(src);
           TreeNode p = src.getChild();
           DFS(p);
           while (src.getSibling() != null) {
              src = src.getSibling();
              DFS(src);
          }
        }
    }
    
    private void colorList(String target) {
        for (int j = 0; j < nLst.size(); j++) {
           TreeNode p = nLst.get(j);
           if (p.getValue().equals(target))
               p.setHexColor("#C6EFF7");
        }
    }
     
    private void uncolorList(String target) {
        for (int j = 0; j < nLst.size(); j++) {
           TreeNode p = nLst.get(j);
           if (p.getValue().equals(target))
               p.setHexColor("#FFFFFE");
        }
    }
}

