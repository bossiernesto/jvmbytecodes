
package exe.chuck_typeset;

import java.util.*;
import java.io.*;
import exe.*;
import exe.ShowFile;
import exe.GAIGStree;
import exe.TreeNode;
import exe.GAIGSarray;
import exe.GAIGStext;

public class TypeSet {
    private int[][] L; // length of the line containing words i thru j
    private int s;  //the amount of space between words
    private int Llen;  //the maximum number of characters per line
    private int[][] M;  //the misfit, max number of character per line - natural length of words i thru j
    private int[][] C;  //the minimum cost 
    private int[] l;  //the length of the words
    private final String docURL = "typeSetInfo.htm";
    private String pc;
    private ShowFile show;
    private GAIGSarray mMat;
    private GAIGSarray cMat;
    private GAIGStext message;
    private GAIGStext message1;
    private GAIGStext message2;
    private GAIGStext message3;
    private GAIGStree vizTree;
    private final String TITLE = "Typesetting Problem";
    private ArrayList<TreeNode> nLst;
    private ArrayList<String> tLst;
    private String[] colorArr = {"#00FFFF",  "#CCFFFF", "#FF00FF",  "#FFCCFF",  "#FFFF00", "#FFFFCC",
                                    "#FFCCFF", "#FFFF44", "#FFFF88", "#44FFFF", "#88FFFF","#FF44FF"};
    private String audio;
    
    public TypeSet(ShowFile sf, int[] lng, int sp, int size) {
        show = sf;
        s = sp;
        Llen = size;
        l = lng;
        audio = "";
        nLst = new ArrayList<TreeNode>();
    }
 
    public void solveTS() throws IOException{
        String target = "";
        int n = l.length;
        mMat = new GAIGSarray(n, n,"Misfit  Matrix","#FFFFFF",0.7, 0.6, 1.2, 1.0, 0.09);
        cMat = new GAIGSarray(n, n,"","#FFFFFF",0.7, 0.1, 1.2, 0.5, 0.09);
        
        message3 = new GAIGStext(0.0, -0.25, "Recursive calls are colored blue");
        message3.setFontsize(0.05);
        message3.setColor("#730063");
       
       L = new int[n][n];
       for (int i = 0; i < n; i++) {
            L[i][i] = l[i];
            for (int j = i+1; j < n; j++)
                L[i][j] = L[i][j-1] + l[j];
       }
       for (int i2 = 0; i2 < n; i2++) {
            String lb2 = i2 + "";
            mMat.setColLabel(lb2, i2);
            mMat.setRowLabel(lb2, i2);
        }
       M = new int[n][n];
       for (int i = 0; i < n; i++) 
            for (int j = i; j < n; j++){
                if (L[i][j] <= Llen){
                    M[i][j] = Math.abs(Llen - L[i][j] - (j-i)*s);
                    mMat.set(M[i][j], i, j);
                }
                else {
                   // M[i][j] = Integer.MAX_VALUE;
                    M[i][j] = 50000;
                    mMat.set("inf", i, j);
                }
                
            }
            pc = genPseudo(1, 2);
            audio = "The misfit matrix for the typesetting problem.  The rows are the index of the first word in the sequence and the columns the last.";
            show.writeSnap(TITLE, 0.05, docURL, pc, audio, message, message2, vizTree, mMat);
            C = new int[n][n];
            for (int i2 = 0; i2 < n; i2++) {
            String lb2 = i2 + "";
            cMat.setColLabel(lb2, i2);
            cMat.setRowLabel(lb2, i2);
            C[i2][i2] = M[i2][i2];
            cMat.set(C[i2][i2], i2, i2);
        }
       audio = "The dynamic programming matrix  initialized";
       pc =genPseudo(1, 3);
       show.writeSnap(TITLE, 0.05, docURL, pc, audio, message, message2, vizTree, mMat, cMat);
       for (int j = 0; j < n; j++) {
          // C[j][j] = M[j][j];
           for (int i = j-1; i >= 0; i--) {
               int min = M[i][j];
               mMat.setColor(i, j, colorArr[0]);
               tLst = new ArrayList<String>();
               for (int k = i; k < j; k++) {
                   int tmp = M[i][k] + C[k+1][j];
                   target = (k+1) + "  " + j;
                   colorList(target);
                   tLst.add(target);
                   mMat.setColor(i, k, colorArr[k-i+1]);
                   cMat.setColor(k+1, j, colorArr[k-i+1]);
                   if (tmp < min)
                      min = tmp;
               }
               message1 = new GAIGStext(0.9, -0.1, "");
               message1.setFontsize(0.06);
               message1.setColor("#730063");
               String msg = "min of M["+i+"]["+j+"] and M["+i+"][k]+C[k+1]["+j+"]";
               message1.setText(msg); 
               audio = "The current entry is colored yellow, the subproblems are colored identically";
               C[i][j] = min;
               cMat.set(C[i][j], i, j);
               cMat.setColor(i, j, "#FFFF9C");
               pc =genPseudo(1, 11);
               show.writeSnap(TITLE, 0.05, docURL, pc, audio, message1, message, message2, vizTree, mMat, cMat);
               for (int q = 0; q < n; q++) 
                    for (int r = q; r < n; r++) {
                   mMat.setColor(q, r, "#FFFFFF");
                   cMat.setColor(q, r, "#FFFFFF");
               }
               for (int z = 0; z < tLst.size(); z++)
                uncolorList(tLst.get(z));
               tLst.clear();
          }
          
        }
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++)
                System.out.print(C[r][c] + "  ");
            System.out.println();
        }
        cMat.setColor(0, n-1, "#FF0000");
        audio = "the solution to the typesetting problem is shown in red";
        pc =genPseudo(1, 12);
        show.writeSnap(TITLE, 0.05, docURL, pc, audio, message, message2, vizTree, mMat, cMat);
        findSoln();
    }
    
    public void recur(int i, int j) throws IOException {
        vizTree = new GAIGStree(false, "", "#FFFF00", -0.25, 0.3, 0.25, 0.9, 0.09);
        TreeNode root = new TreeNode(i + "  " + j);
        vizTree.setRoot(root);
        message = new GAIGStext(0.0, -0.25, "Recursive Call Tree for the Typesetting Problem");
        message.setFontsize(0.06);
        message.setColor("#730063");
        message2 = new GAIGStext(0.0, -0.3, "Node labels contain indices of word sequences");
        message2.setFontsize(0.06);
        message2.setColor("#730063");
        vizTree.setSpacing(1.1, 1.75);
        int space = recurTS(i,j, root);
        System.out.println(space);
        audio = "The recursive call tree";
        show.writeSnap(TITLE, 0.05, docURL, pc, audio, message, message2, vizTree);
        DFS(root);
    }
  
    private int LW(int i, int j) {
        if (i==j)
            return l[i];
        return LW(i, j-1) + l[j];
    }
    
    private int M(int i, int j) {
        if (Llen >= LW(i, j))
            return Math.abs(Llen - LW(i, j) - (j-i)*s);
        return Integer.MAX_VALUE;
    }
    
    private int recurTS(int i, int j, TreeNode parent) {
        if (i == j)
            return M(i,i);
        else
            if (M(i, j) == Integer.MAX_VALUE)
                return V(i, j, i, parent);
            else {
                String labelNode = i + "  " + j;
                TreeNode newNode = new TreeNode(labelNode);
                parent.setChildWithEdge(newNode);
                return Math.min(M(i, j), V(i, j, i, parent));
            }
    }
    
    private int V(int i, int j, int k, TreeNode parent) {
        if (j == k)
            return M(i,j);
        else
            if (M(i, k) == Integer.MAX_VALUE)
                return V(i, j, k+1, parent);
            else {
                String labelNode = (k+1) + "  " + j;
                TreeNode newNode = new TreeNode(labelNode);
                parent.setChildWithEdge(newNode);
                return Math.min(M(i, k) + recurTS(k+1,j, newNode), V(i, j, k+1, parent));
            }
    }
    
        private void DFS(TreeNode src) {    
        if (src != null) {
           //System.out.println("Visited node " + src.getValue());
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
            return "index.php?line=" + line;
        else
            return "typesetSoln.php?line=" + line;
   }
   
    private void findSoln() throws IOException {
        message1 = new GAIGStext(0.7, -0.25, "");
        message1.setFontsize(0.05);
        message1.setColor("#730063");
        Queue<Pair> q = new Queue<Pair>();
        ArrayList<Pair> pLst = new ArrayList<Pair>();
        int k = 0;
        int curr = l.length-1;
        q.enqueue(new Pair(k, curr));
        cMat.setColor(k, curr, "#FF0000");
        audio = "Finding the optimal solution of the typesetting problem";
        message1.setText("Searching for subproblems that lead to the optimal solution");
        pc =genPseudo(2,1);
        show.writeSnap(TITLE, 0.05, docURL,  pc, audio, message1, cMat);
        while (!q.isEmpty()) {
            Pair p = q.dequeue();
            k = p.getX();
            curr = p.getY();
            int i = k;
            int targetVal = C[k][curr];
            pc =genPseudo(2, 3);
            while (i < curr && C[k][i] + C[i+1][curr] != targetVal) {
                cMat.setColor(k, i, "#FFFF9C");
                cMat.setColor(i+1, curr, "#FFFF9C");
                String txt = "The sum " + C[k][i] +" + " + C[i+1][curr] + " does not equal " + targetVal;
                message1.setText(txt);
                audio = "The minimal value is in red, the current rejected candidate subproblems are shown in yellow";
                show.writeSnap(TITLE, 0.05, docURL, pc, audio, message1, cMat);
                cMat.setColor(k, i, "#FFFFFF");
                cMat.setColor(i+1, curr, "#FFFFFF");
                i++;
            }
            
            if (i < curr) {
                cMat.setColor(k, i, "#FFFF9C");
                cMat.setColor(i+1, curr, "#FFFF9C");
                audio = "The subproblems have been identified";
                show.writeSnap(TITLE, 0.05, docURL, pc, audio, cMat);
                cMat.setColor(k, i, "#FFFFFF");
                cMat.setColor(i+1, curr, "#FFFFFF");
                p = new Pair(k, i);
                cMat.setColor(k, i, "#FFFF9C");
                if (L[k][i] <= Llen) {
                    pLst.add(p);
                    audio = "the subproblem colored in yellow is a line in the solution";
                    pc =genPseudo(2, 5);
                }
                else {
                    q.enqueue(p);
                    audio = "the subproblem colored in yellow must be further partitioned";
                    pc =genPseudo(2, 7);
                }
               show.writeSnap(TITLE, 0.05, docURL, pc, audio, cMat);
                cMat.setColor(i+1, curr, "#FFFF9C"); 
                p = new Pair(i+1, curr);
                if (L[i+1][curr] <= Llen) {
                    pLst.add(p);
                    audio = "this subproblem colored in yellow is a line in the solution";
                    pc =genPseudo(2, 9);
                }
                else {
                    q.enqueue(p);
                    audio = "this subproblem colored in yellow must be further partitioned";
                    pc =genPseudo(2, 11);
                }
                show.writeSnap(TITLE, 0.05, docURL, pc, audio, cMat);
                if (L[k][i] > Llen)
                    cMat.setColor(k, i, "#FFFFFF");
                if (L[i+1][curr] > Llen)
                    cMat.setColor(i+1, curr, "#FFFFFF");
            }
            else {
                audio = "A line in the solution has been identified and is shown in yellow";
                cMat.setColor(i+1, curr, "#FFFF9C");
                pc =genPseudo(2, 13);
                show.writeSnap(TITLE, 0.05, docURL, pc, audio, cMat);     
            }
            
        }
        for (int i = 0; i < pLst.size(); i++) {
            Pair p = pLst.get(i);
            System.out.println(p.getX() + "  " + p.getY() + "  " + M[p.getX()][p.getY()]);
        }
        
    }    
}
