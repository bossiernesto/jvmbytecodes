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

package exe.chuck_knap; 
 
import exe.*;
import exe.ShowFile;
import exe.GAIGStree;
import exe.TreeNode;
import exe.GAIGSarray;
import exe.GAIGStext;
import exe.XMLfibQuestion;
import exe.XMLtfQuestion;
import java.io.*;
import java.util.*;

public class Knapsack {   
    private int[] c;
    private int[] a;
    private int[][] P;
    private int b, n, qId;
    private ShowFile show;
    private GAIGSarray knapMat;
    private String docURL = "knapInfo.htm";
    private String pc;
    private GAIGStext message;
    private GAIGStext message3;
    private GAIGStext message2;
    private GAIGStree vizTree;
    private final String TITLE = "0-1 Knapsack Problem";
    private ArrayList<TreeNode> nLst;
    private String audio;
    
    public Knapsack(int[] c1, int[] a1, int b1, ShowFile s){
        c = c1;
        a = a1;
        b = b1;
        show = s;
        n = c.length + 1;
        P = new int[n][b];
        audio = "";
        pc = "";
        qId = 0;
        for (int j = 0; j <b; j++) 
            P[0][j] = 0;
        nLst = new ArrayList<TreeNode>();
    }

    public void solve() throws IOException {
        knapMat = new GAIGSarray(n, b,"Knapsack Dynamic Programming Matrix","#FFFFFF",0.7, 0.1, 1.2, 0.6, 0.09);
        message = new GAIGStext(1.4, -0.2, "Matrix after initialization");
        message.setFontsize(0.05);
        message.setColor("#730063");
        message2 = new GAIGStext(0.9, -0.1, "");
        message2.setFontsize(0.05);
        message2.setColor("#730063");
        message.setHalign(2);
        message3 = new GAIGStext(0.0, -0.25, "Recursive calls are colored blue");
        message3.setFontsize(0.05);
        message3.setColor("#730063");
        for (int i1 = 0; i1 <b; i1++) {
            P[0][i1] = 0;
            knapMat.set(P[0][i1], 0, i1);
            int i11 = i1 + 1;
            String lbl = i11 + "";
            knapMat.setColLabel(lbl, i1);
        }
        for (int i2 = 1; i2 < n; i2++) {
            String lb2 = i2 + "";
            knapMat.setRowLabel(lb2, i2);
        }
        String pc = genPseudo(1, 1);
        show.writeSnap(TITLE, 0.08, docURL, pc, message, vizTree, knapMat);      
        double rand;
        for (int k = 1; k <= c.length; k++) {
            for (int w = 1; w <= b; w++) {
                rand = Math.random();
                message2.setText("a["+k+"]="+a[k-1]+" c["+k+"]="+c[k-1]+" y="+w);
                int u = 0;
                knapMat.setColor(k-1,w-1,"#C6EFF7");
                String labelx = (k-1) + "  " + w;
                String labely = "";
                colorList(labelx);
                if (w - a[k-1] >= 0) {
                    if (w - a[k-1] > 0)
                        knapMat.setColor(k-1,w-1-a[k-1],"#C6EFF7");
                    String wStr = (w-a[k-1])+"";
                    labely = (k-1)+"  " +wStr ;
                    colorList(labely);
                    u = 1;
                    if (w - a[k-1] > 0) {
                        P[k][w-1] = Math.max(P[k-1][w-1- a[k-1]] + c[k-1], P[k-1][w-1]);
                        pc = genPseudo(1, 5);
                    }
                    else {
                        P[k][w-1] = Math.max(c[k-1], P[k-1][w-1]);
                        u = 2;
                        pc = genPseudo(1, 8);
                    }
                    message.setText("Comparing ("+P[k-1][w - a[k-1]] +"+"+ c[k-1]+") with " + P[k-1][w-1] + " - place item " + k + " in this knapsack");
                }
                else {
                    message.setText("Weight of item " + k + " too large for this knapsack");
                    P[k][w-1] = P[k-1][w-1];
                    pc = genPseudo(1, 10);
                }
                
                if(rand > .9) { 
                    XMLfibQuestion fibQ = new XMLfibQuestion(show, qId + "");
                    qId++;
                    buildfibQues(fibQ, P[k][w-1]);
                    show.writeSnap(TITLE, 0.1, docURL, pc, fibQ, vizTree, message, message2, message3, knapMat);
                } 
                if(rand < .1) {
                    XMLtfQuestion tfQ = new XMLtfQuestion(show, qId + "");
                    qId++;
                    boolean ans = (w >= a[k-1]);
                    buildtfQues(tfQ, ans);
                    show.writeSnap(TITLE, 0.1, docURL, pc, tfQ, vizTree, message, message2, message3, knapMat);
                } 
                
                message3.setText("Recursive calls are colored blue in tree");
                knapMat.set(P[k][w-1], k, w-1,"#FFFF9C");
                if (((w - a[k-1] > 0) && P[k-1][w -1 - a[k-1]] + c[k-1] > P[k-1][w-1])||(w - a[k-1] == 0)&&(c[k-1] > P[k-1][w-1])) {
                        audio = "item " + k; 
                        audio += " is selected for inclusion in the knapsack for weight " + w;
                        show.writeSnap(TITLE, 0.08, docURL, pc, audio, vizTree, message, message2, message3, knapMat);
                }
                else
                    show.writeSnap(TITLE, 0.08, docURL, pc, vizTree, message, message2, message3, knapMat);
                knapMat.setColor(k, w-1, "#FFFFFF");
                knapMat.setColor(k-1, w-1, "#FFFFFF");
                if (u == 1) 
                    knapMat.setColor(k-1,w-1-a[k-1],"#FFFFFF");
                u = 0;
                uncolorList(labelx);
                uncolorList(labely);
            }
        }
        audio = "The maximum value for this knapsack problem is " + P[n-1][b-1] + " shown in red in the visualization";
        System.out.println("The maximum value of the objective function is " +P[c.length][b-1]);
        knapMat.setColor(c.length, b-1, "#FF0000");
        message.setText("The optimal value is in the bottom right cell");
        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, knapMat);
        solnKnap(P[n-1][b-1], b);
    }
    
    public void recur(int n, int b) throws IOException {
        vizTree = new GAIGStree(false, "", "#FFFF00", 0.0,0.3,0.3,0.7,0.1);
        TreeNode root = new TreeNode(n + "  " + b);
        vizTree.setRoot(root);
        message = new GAIGStext(0.0, -0.25, "Recursive Call Tree - note depth and complexity");
        message.setFontsize(0.06);
        message.setColor("#730063");
        message2 = new GAIGStext(0.0, -0.3, "Node labels contain the pair - item weight");
        message2.setFontsize(0.06);
        message2.setColor("#730063");
        vizTree.setSpacing(1.1, 1.75);
       // vizTree.setSpacing(0.5,1.25);
        knapRecur(n, b, root);
        audio = "The visualization begins with the display of recursive call tree for this instance of the knapsack problem";
        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, message2, vizTree);
        DFS(root);
    }
    
    private void knapRecur(int n, int b, TreeNode parent) {
        TreeNode newNode = null;
        String labelNode = "";
        if (n == 0 || b == 0) 
            labelNode = "";
        else {
            labelNode = (n-1) + "  " + b;
            newNode = new TreeNode(labelNode);
            parent.setChildWithEdge(newNode);
            knapRecur(n-1,b, newNode);
            if (b-a[n-1]>=0) {
                labelNode = (n-1) + "  " + (b-a[n-1]);
                newNode = new TreeNode(labelNode);
                parent.setChildWithEdge(newNode);
                knapRecur(n-1, b-a[n-1],newNode);
            }
        }
    }
    
    private void solnKnap(int val, int wt) throws IOException {
        pc = genPseudo(2, 1);
        int[] soln = new int[c.length];
        for (int z = 0; z < c.length; z++)
            soln[z] = 0;
        audio = "Locating the items selected for an optimal solution to the knapsack problem";
        show.writeSnap(TITLE, 0.1,  docURL, pc, audio, message, knapMat);
        int curr = n-1;
        audio = "The items selected for the optimal solution are shown in blue";
        while (curr > 0) {
            while (val == P[curr-1][wt-1]) {
                pc = genPseudo(2, 5);
                knapMat.setColor(curr,wt-1,"#FFB573");
                curr--;
                message.setText("Searching for component of solution");
                show.writeSnap(TITLE, 0.1, docURL, pc, message, knapMat);
            }
            knapMat.setColor(curr,wt-1,"#C6EFF7");
            message.setText("Found item in solution - marked in blue");
            pc = genPseudo(2, 6);
            show.writeSnap(TITLE, 0.1, docURL, pc, message, knapMat);
            soln[curr-1] = 1;
            int nextWt = wt - a[curr-1];
            if (nextWt > 0) {
            while (wt > nextWt) {
                wt--;
                knapMat.setColor(curr,wt-1,"#FFB573");
                int v = curr-1;
                message.setText("Adjusting column for weight of item " + v);
                pc = genPseudo(2, 7);
                show.writeSnap(TITLE, 0.1, docURL, pc, message, knapMat);
            }
            if (curr > 0) {
                wt = nextWt;
                val = P[curr-1][nextWt-1];
            }
        }
        curr--;
        pc = genPseudo(2, 8);
        show.writeSnap(TITLE, 0.1, docURL, pc, message, knapMat);
     }
        
        String sln = "[";
        for(int q = 0; q < soln.length-1; q++) 
            sln += soln[q]+", ";
        sln += soln[soln.length-1] +"]";
        System.out.println(sln);
        message.setText("Solution = " + sln + " - marked in blue");
        show.writeSnap(TITLE, 0.1, docURL, pc, audio, message, knapMat);
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
    
    private String genPseudo(int algo, int line) {
        if (algo == 1)
            return "knap01.php?line=" + line;
        else
            return "knap01Soln.php?line=" + line;
   }
  
   private void buildfibQues(XMLfibQuestion fibQ, int val) {
       fibQ.setQuestionText("The value of the current entry of the matrix is ___");
       String str = ""+ val;
       fibQ.setAnswer(str);
    }
 
    private void buildtfQues(XMLtfQuestion tfQ, boolean ans) {
       tfQ.setQuestionText("The current item will fit into the knapsack at the current weight.");
       tfQ.setAnswer(ans);
    }
}
