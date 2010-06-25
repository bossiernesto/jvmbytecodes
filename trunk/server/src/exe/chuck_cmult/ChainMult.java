
package exe.chuck_cmult;

import java.util.*;
import java.io.*;
import exe.*;
import exe.ShowFile;
import exe.GAIGStree;
import exe.TreeNode;
import exe.GAIGSarray;
import exe.GAIGStext;

public class ChainMult {
    private ShowFile show;
    private GAIGStree vizTree;
    private GAIGStext message;
    private GAIGStext message1;
    private GAIGStext message2;
    private GAIGStext message3;
    private GAIGStext message4;
    private GAIGStext[] message5;
    private GAIGStext message6, message7;
    private GAIGSarray chainmat, tsmat;
    private GAIGSarray smat;
    private final String TITLE = "Dynamic Programming - Chain Multiplication Problem";
    private TreeNode root;
    private String audio, pc;
    private String docURL = "cmInfo.htm";
    private int[][] m;
    private int[][] sM;
    private int[] p;
    private int right, n;
    //private int min;

	/**
	 * Constructor for objects of class ChainMult
	 */
	public ChainMult(int n, int[] dim, ShowFile s) {
		this.n = n;
		show = s;
		m = new int[n+1][n+1];
        sM = new int[n+1][n+1];
        p = new int[n+1];
        //min = 999999;
        for (int k = 0; k <= n; k++) {
            p[k] = dim[k];
          	for (int right = 0; right <= n; right++) {
                	m[k][right] = 0;
                	sM[k][right] = 0;
           	}
        }
        pc ="";
    }	
    
    public void recur() throws IOException {
        vizTree = new GAIGStree(false, "", "#FFFF00", 0.0,0.3,0.3,0.7,0.1);
        TreeNode root = new TreeNode(1 + " " + n);
        vizTree.setRoot(root);
        message = new GAIGStext(0.0, -0.05, "Recursive Call Tree");
        message.setFontsize(0.06);
        message.setColor("#730063");
        message2 = new GAIGStext(0.2, -0.1, "Node labels contain indices for the multiplication");
        message2.setFontsize(0.06);
        message2.setColor("#730063");
        vizTree.setSpacing(1.1, 1.75);
       // vizTree.setSpacing(0.5,1.25);
        int mults = cmRecur(1, n, root);
        show.writeSnap(TITLE, 0.07, docURL, message, message2, vizTree);
        System.out.println(mults);
        //DFS(root);
    }
    
    private int cmRecur(int lft, int rght, TreeNode parent) {
        TreeNode newNode = null;
        int min = 0;
        String labelNode = "";
        if (lft == rght) 
            return 0;
        else {
            if (rght == lft+1)
                return p[lft-1]*p[lft]*p[lft+1];
            else {
                min = 999999;
                for (int k = lft; k < rght; k++) {
                    //min = 999999;
                    labelNode = lft + " " + k;
                    TreeNode newNode1 = new TreeNode(labelNode);
                    parent.setChildWithEdge(newNode1);
                    int fst = k + 1;
                    labelNode = fst + " " + rght;
                    TreeNode newNode2 = new TreeNode(labelNode);
                    parent.setChildWithEdge(newNode2);
                    int q = cmRecur(lft, k, newNode1);
                    q += cmRecur(k+1, rght, newNode2);
                    q += p[lft-1]*p[k]*p[rght];
                    if (q > 0 && q < min )
                        min = q;
                }
            }
            return min;
            
        }
    }
    
    public void compute() throws IOException {
        
        message = new GAIGStext(0.7, 0.25, "Matrix after initialization");
        int c1 =0;
        int c2=0;
        chainmat = new GAIGSarray(n+1,n+1,"Chain Multiplication Matrix","#FFFFFF",0.4, 0.4, 0.9, 0.85, 0.11);
        message5 = new GAIGStext[p.length-1];
        message3 = new GAIGStext(0.000001, 0.8, "--------Matrices--------");
        message4 = new GAIGStext(0.000001, 0.7, "MATRIX----ROWS----COLUMNS");
        message6 = new GAIGStext(0.7,0.2," ");
        message7 = new GAIGStext(0.7, 0.1, " ");
        double pos = 0.6;
        for(int x = 0; x<p.length-1;x++){
            message5[x] = new GAIGStext(0.000001, pos, "--M"+(x+1)+"-------"+p[x]+"----X---"+p[x+1]+"--");
            pos = pos-0.1;
        }
        
        for (int k = 0; k <= n; k++) {
            for (int right = 0; right <= n; right++) {
                    chainmat.set(m[k][right],k,right);
            }
        }
        getMessages();
        //the algorithm - compute the bands
        genPseudo(1,1);
        for (int band = 1; band < n; band++) {
            genPseudo(1,2);
            for (int left = 1; left <= n - band; left++) {
               genPseudo(1,3);
               right = left + band;
               genPseudo(1,4);
               m[left][right] = 9999999;
               genPseudo(1,5);
               for (int k = left; k < right; k++) {
                   genPseudo(1,6);
                   int q = m[left][k] + m[k+1][right] + p[left-1]*p[k]*p[right];
                   //System.out.println(q);
                   message.setText("Finding the number of scalar multiplications for "+getProductString(left,right));
                   message6.setText("Trying multiplication pattern "+getIntermediateProductString(left, k, right));
                   String m7Str = "Multiplications = " + m[left][k] + " + " + m[k+1][right] + " + " + p[left-1]*p[k]*p[right] + " = " + q;
                   message7.setText(m7Str);
                   System.out.println("q = " + q);
                   getMessages();
                   genPseudo(1,7);
                   if (q < m[left][right]) {
                       genPseudo(1,8);
                       m[left][right] = q;
                       chainmat.setColor(c1,c2,"#FFFF00");
                       chainmat.set(m[left][right],left,right);
                       c1=left;
                       c2=right;
                       genPseudo(1,9);
                       sM[left][right] = k;
                    }
                   
                }
                message6.setText(" ");
                message7.setText(" ");
                 message.setText("Found minimum number of scalar multiplications for " + getProductString(left,right));
                chainmat.setColor(c1,c2,"#FF0000");
                getMessages();
                chainmat.setColor(c1,c2,"#FFFF00");
            }
        }
        //Print out m[][]
        System.out.println("The matrix m = ");
        for (int left = 1; left <= n; left++) {
            for (right = 1; right <= n; right ++)
                System.out.print(m[left][right] + "   ");
            System.out.println();
        }
        
        //Print out the matrix s
        System.out.println("The matrix s = ");
        for (int left = 1; left <= n; left++) {
            for (right = 1; right <= n; right ++)
                System.out.print(sM[left][right] + "   ");
            System.out.println();
        }
        String optStr = solveIt(1, n);
        System.out.println(optStr);
        chainmat.setColor(c1,c2,"#FF0000");
        message6.setText("The optimal ordering for the multiplication is " + optStr);
        message7.setText("The number of scalar multiplications is " + m[1][n]);
        getMessages();
    }
    
    public String solveIt(int a, int b) throws IOException {
        smat = new GAIGSarray(n+1,n+1,"Multiplication Order Matrix","#FFFFFF",0.4, 0.4, 0.9, 0.85, 0.11);
        for (int k = 0; k <= n; k++) {
            for (int right = 0; right <= n; right++) {
                    smat.set(sM[k][right],k,right);
            }
        }
        message6.setText(" ");
        message.setText(" ");
        genPseudo(2,1);
        if (a == b) {
            genPseudo(2, 2);
            return "M" + a;
        }
        genPseudo(2,3);
        if ( a + 1 == b) {
            genPseudo(2,4);
            return "(M"+a+" * M" + b +")";
        }
        smat.setColor(a,b,"#FF0000");
        message6.setText("checking matrix multiplication for matrices " + a + " to " + b);
        message.setText("optimal multiplication point is " + sM[a][b]);
        getMessages2();
        genPseudo(2,5);
        int y = sM[a][b];
        genPseudo(2,6);
        String str1 = solveIt(a, y);
        genPseudo(2,7);
        String str2 = solveIt(y+1, b);
        genPseudo(2,8);
        return "("+str1+ " * " + str2 +")";
    }
    
    public void getMessages() throws IOException {
            if(p.length-1==3){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message6,message7,chainmat);
            }
            if(p.length-1==4){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message6,message7,chainmat);
            }
            if(p.length-1==5){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message5[4],message6,message7,chainmat);
            }
            if(p.length-1==6){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message5[4],message5[5],message6,message7,chainmat);
            }
   }
   
   public void getMessages2() throws IOException {
            if(p.length-1==3){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2], message6, message7,smat);
            }
            if(p.length-1==4){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message6, message7,smat);
            }
            if(p.length-1==5){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message5[4],message6,message7,smat);
            }
            if(p.length-1==6){
                show.writeSnap(TITLE, 0.08, docURL, pc, message,message3,message4, message5[0],message5[1],message5[2],message5[3],message5[4],message5[5],message6,message7,smat);
            }
   }
   public String getProductString(int l, int r) {
        String pStr="";
        if (l < r) 
            pStr = "(M"+l;
        else
            pStr = "M"+l;
        for (int j = l+1; j <= r; j++) 
            pStr += "*M"+j;
        if (l < r)
            pStr += ")";
        return pStr;
    }
    
    public String getIntermediateProductString(int l, int k, int r) {
        String pStr="";
        if (l < k) 
            pStr = "(M"+l;
        else
            pStr = "M"+l;
        for (int j = l+1; j <= k; j++) 
            pStr += "*M"+j;
        if (l < k)
            pStr += ")";
        int l2 = k+1;
        if (l2 < r)
            pStr += "*(M"+l2;
        else
            pStr += "*M" + l2;
        for (int t = l2+1; t <=r; t++)
            pStr += "*M"+t;
        if (l2 < r)
            pStr += ")";
        return pStr;
    }
    
    private String genPseudo(int algo, int line) {
        if (algo == 1)
            return "chainMult.php?line=" + line;
        else
            return "chainMultSoln.php?line=" + line;
   }
}
