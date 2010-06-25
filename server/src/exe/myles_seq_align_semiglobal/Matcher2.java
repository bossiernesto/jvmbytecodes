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

package exe.myles_seq_align_semiglobal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import exe.*;

public class Matcher2 {
	
    static final String TITLE = "Sequence Alignment";
    static final double TITLE_FONT_SIZE = 0.07;
    static final String CELL_COLOR = "#444466";
    static final String UNINIT_CELL_COLOR = "#cccccc";
    static final String HIGHLIGHT_COLOR = "#111166";
	
    static final int GAP_PENALTY = -1;
    static final int MATCH_SCORE = +1;
    static final int MISMATCH_SCORE = 0;
	
    ShowFile show;
    String matchTextBase;
    GAIGStext matchText;
    GAIGStext seqs;
    String seqsText;

    /**
     * @param args
     */
    public static void main(String[] args)  throws IOException {
	Matcher2 matchIt = new Matcher2(args);
		                                                 
    }
	
    public Matcher2 (String[] args)  throws IOException {
	show = new ShowFile(args[0]);
      
	//		char [] S1  = {'A','A','A'};
	//		char [] S2 = {'A', 'A'};
		
// 	char [] S1  = {'A','C','A','G','T','A','G'};
// 	char [] S2 = {'A','C','T','C','G'};
// 		
// 	String temp1 = new String(S1, 0, S1.length);
// 	String temp2 = new String(S2, 0, S2.length);

	String temp1 = args[1];
	String temp2 = args[2];

	char [] S1  = temp1.toCharArray();
	char [] S2 = temp2.toCharArray();
		
	matchTextBase = "\\#333355Aligning \\#0000cc" + temp1 + " \\#333355with \\#0000cc" + temp2;
	matchText = new GAIGStext(0.5,0.05,GAIGStext.HCENTER,GAIGStext.VTOP, 0.06, "#000000", matchTextBase + "\\#333355 - Allocate Matrix");
		
	GAIGSarray table = new GAIGSarray(S1.length+1, S2.length+1, null, UNINIT_CELL_COLOR, -0.2, 0.05, 0.8, 1.05, 0.045);
	initGAIGSarray(table, S1, S2);
	setTableLabels(table, S1, S2);
	show.writeSnap(TITLE, TITLE_FONT_SIZE, table, matchText);

	computeTable(table, S1, S2);


	//     show.writeSnap(TITLE, TITLE_FONT_SIZE, table, matchText);
		
	int len = S1.length + S2.length;
	table.setColor(S1.length, S2.length,HIGHLIGHT_COLOR);
	seqsText = "\\#333355Alignments\n";
	seqs = new GAIGStext(1.1,0.7,GAIGStext.HRIGHT,GAIGStext.VTOP, 0.06, "#333333", seqsText);
	matchText.setText(matchTextBase + "\\#333355 - Retrieve Alignments");
	show.writeSnap(TITLE, TITLE_FONT_SIZE, table, matchText, seqs);
	printMatches(table, S1, S2, S1.length, S2.length, len, new char[len], new char[len] );
	table.setColor(S1.length, S2.length,CELL_COLOR);
	seqs.setText(seqsText);
	show.writeSnap(TITLE, TITLE_FONT_SIZE, table, matchText, seqs);
	show.close();
    }
	
    public void initGAIGSarray(GAIGSarray A, char [] S1, char [] S2){
	for (int r = 0; r < S1.length+1; r++)
	    for (int c = 0; c < S2.length+1; c++) 
		A.set("0", r, c, UNINIT_CELL_COLOR);
    }
	
    public void setTableLabels(GAIGSarray A, char [] S1, char [] S2){
	String s;
	for (int r = 0; r < S1.length; r++) {
	    s = new String(S1, r, 1);
	    A.setRowLabel( s, r+1);
	}
	for (int c = 0; c < S2.length; c++) {
	    s = new String(S2, c, 1);
	    A.setColLabel( s, c+1);
	}
    }
	
    public void computeTable(GAIGSarray A, char [] S1, char [] S2) throws IOException{
		
	matchText.setText(matchTextBase + "\\#333355 - Initialize Matrix");
	A.set(0,0,0, CELL_COLOR);
	show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign1.php?line=4", A, matchText);
	for (int r = 1; r < S1.length+1; r++) {
	    A.set( 0, r, 0, CELL_COLOR);
	    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign1.php?line=6", A, matchText);
	}
	for (int c = 1; c < S2.length+1; c++) {
	    A.set( 0, 0, c, CELL_COLOR);
	    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign1.php?line=8", A, matchText);
	}

	matchText.setText(matchTextBase + "\\#333355 - Compute Matrix Match Values");
	for (int r = 1; r < S1.length+1; r++){		
	    for (int c = 1; c < S2.length+1; c++){
		int vertScore = ( ( c < S2.length ) ? ( (Integer)A.get(r-1, c) + GAP_PENALTY ) : (Integer)A.get(r-1, c) );
		int horzScore = ( ( r < S1.length ) ? ( (Integer)A.get(r, c-1) + GAP_PENALTY ) : (Integer)A.get(r, c-1) );
		int diaScore = (Integer)A.get(r-1, c-1) + (S1[r-1] == S2[c-1] ? MATCH_SCORE : MISMATCH_SCORE);
		A.set( Math.max(vertScore, Math.max(horzScore, diaScore)), r, c, CELL_COLOR);
		show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign1.php?line=15", A, matchText);
	    }
	}
    }
	
    public void printMatches (GAIGSarray A, char [] S1, char [] S2, int r, int c, int pos, char [] S1new, char [] S2new) throws IOException{

	if (r == 0 && c == 0) {
	    String temp1 = new String(S1new, pos, S1new.length-(pos));
	    String temp2 = new String(S2new, pos, S2new.length-(pos));					
	    seqsText = seqsText + "\n----------\n" + temp1 + '\n'+ temp2;
	} else {
	    if (c > 0 && r < S1.length) {
		if ((Integer)A.get(r, c) == (Integer)A.get(r,c-1) + GAP_PENALTY) {
		    S1new[pos-1] = '-';
		    S2new[pos-1] = S2[c-1];
		    A.setColor(r,c-1,HIGHLIGHT_COLOR);
		    String temp1 = new String(S1new, pos-1, S1new.length-(pos-1));
		    String temp2 = new String(S2new, pos-1, S2new.length-(pos-1));					
		    seqs.setText(seqsText + "\n----------\n\\#0000cc" + temp1 + "\n\\#0000cc"+ temp2);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=6&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		    printMatches(A, S1, S2, r, c-1, pos-1, (char []) S1new.clone(), (char []) S2new.clone());
		    A.setColor(r,c-1,CELL_COLOR);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=8&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		}
	    }
				
	    if (c > 0 && r==S1.length) {
		if ((Integer)A.get(r, c) == (Integer)A.get(r,c-1)) {
		    S1new[pos-1] = '-';
		    S2new[pos-1] = S2[c-1];
		    A.setColor(r,c-1,HIGHLIGHT_COLOR);
		    String temp1 = new String(S1new, pos-1, S1new.length-(pos-1));
		    String temp2 = new String(S2new, pos-1, S2new.length-(pos-1));					
		    seqs.setText(seqsText + "\n----------\n\\#0000cc" + temp1 + "\n\\#0000cc"+ temp2);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=6&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		    printMatches(A, S1, S2, r, c-1, pos-1, (char []) S1new.clone(), (char []) S2new.clone());
		    A.setColor(r,c-1,CELL_COLOR);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=8&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		}
	    }
				
	    if (r > 0 && c < S2.length) {
		if ((Integer)A.get(r, c) == (Integer)A.get(r-1,c) + GAP_PENALTY) {
		    S1new[pos-1] = S1[r-1];
		    S2new[pos-1] = '-';
		    A.setColor(r-1,c,HIGHLIGHT_COLOR);
		    String temp1 = new String(S1new, pos-1, S1new.length-(pos-1));
		    String temp2 = new String(S2new, pos-1, S2new.length-(pos-1));					
		    seqs.setText(seqsText + "\n----------\n\\#0000cc" + temp1 + "\n\\#0000cc"+ temp2);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=11&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		    printMatches(A, S1, S2, r-1, c, pos-1, (char []) S1new.clone(), (char []) S2new.clone());
		    A.setColor(r-1,c,CELL_COLOR);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=13&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		}
	    }
				
	    if (r > 0 && c==S2.length) {
		if ((Integer)A.get(r, c) == (Integer)A.get(r-1,c)) {
		    S1new[pos-1] = S1[r-1];
		    S2new[pos-1] = '-';
		    A.setColor(r-1,c,HIGHLIGHT_COLOR);
		    String temp1 = new String(S1new, pos-1, S1new.length-(pos-1));
		    String temp2 = new String(S2new, pos-1, S2new.length-(pos-1));					
		    seqs.setText(seqsText + "\n----------\n\\#0000cc" + temp1 + "\n\\#0000cc"+ temp2);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=11&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		    printMatches(A, S1, S2, r-1, c, pos-1, (char []) S1new.clone(), (char []) S2new.clone());
		    A.setColor(r-1,c,CELL_COLOR);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=13&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		}
	    }
				
	    if (r > 0 && c > 0){
		if (((S1[r-1] == S2[c-1]) && ((Integer)A.get(r, c) == (Integer)A.get(r-1,c-1) + MATCH_SCORE))
		    || ((S1[r-1] != S2[c-1]) && ((Integer)A.get(r, c) == (Integer)A.get(r-1,c-1) + MISMATCH_SCORE))) {
		    S1new[pos-1] = S1[r-1];
		    S2new[pos-1] = S2[c-1];
		    A.setColor(r-1,c-1,HIGHLIGHT_COLOR);
		    String temp1 = new String(S1new, pos-1, S1new.length-(pos-1));
		    String temp2 = new String(S2new, pos-1, S2new.length-(pos-1));					
		    seqs.setText(seqsText + "\n----------\n\\#0000cc" + temp1 + "\n\\#0000cc"+ temp2);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=17&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		    printMatches(A, S1, S2, r-1, c-1, pos-1, (char []) S1new.clone(), (char []) S2new.clone());
		    A.setColor(r-1,c-1,CELL_COLOR);
		    show.writeSnap(TITLE, TITLE_FONT_SIZE, null, "seqalign2.php?line=19&var[r]="+r+"&var[c]="+c, A, matchText, seqs);
		}		
	    }		
	}
    }
	
}
