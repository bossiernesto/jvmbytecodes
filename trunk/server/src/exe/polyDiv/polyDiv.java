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

package exe.polyDiv;

import org.jdom.Element;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Comment;
import org.jdom.Attribute;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

import java.io.*;
import java.util.Random;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class polyDiv
{
    public static String  ftName = "Lucida Bright";
    private static Random rand = new Random();

    private static int SMALL_SKIP = 2;
    private static int MED_SKIP = 6;
    private static int BIG_SKIP = 10;
    private static int HUGE_SKIP = 20;
    private static int BASELINE = 300;
    private static int LEFT= -20;


    private  static String pseudocodeURL1;

    private static java.util.Hashtable<String,Visual> ht;
    private static Element initial;
    private static Element animation;


    public static final int LARGE_INT = 999999999;
    public static final int MAX_DEGREE = 4;
    public static final int MAX_COEFF = 10;
    private static int dvd_degree, dvs_degree, quo_degree, rem_degree;
    private static int num_steps;
    private static int[][] table;
    private static int[] dvd_c;
    private static int[] dvs_c;
    private static int[] quo_c;

    private static FontMetrics reg_fm, sup_fm;
    private static int reg_size, sup_size;

    public static void main(String[] args)
    {
	String filename = args[0];

	assignCoefficients();
	performFullDivision();
	createScript(filename);
    }// main method


    static void performFullDivision()
    {
	/*
	System.out.print("Dividend: ");
	for(int i=dvd_degree; i>=0; i--)
	    System.out.print("\t" + dvd_c[i]);
	*/
	/*
	  System.out.print("\nDivisor:  ");
	for(int i=dvs_degree; i>=0; i--)
	    System.out.print("\t" + dvs_c[i]);
	System.out.println();
	System.out.println();
	*/

	num_steps = dvd_degree - dvs_degree + 1;

	table = new int[2*num_steps+1][dvd_degree+1];
	for(int col=0; col<=dvd_degree; col++)
	    table[0][col] = dvd_c[col];
	for(int row=1; row<table.length; row++)
	    for(int col=0; col<table[0].length; col++)
		table[row][col] = LARGE_INT;
	for(int step = 1; step <= num_steps; step++)
	{
	    int r = 2*(step-1);         // row of current dividend
	    int c = dvd_degree-step+1;  // col of 1st term in current dividend

	    //System.out.println("Step " + step + "\tr = " + r + "\t c = " + c);
	    // compute the coeff of the next term in quotient
	    quo_c[quo_degree-step+1] = table[r][c] / dvs_c[dvs_degree];

	    // fill in the next row, i.e. the polynomial to be subtracted
	    table[r+1][c] = table[r][c];
	    int lastc = c - dvs_degree; // last col for this step

	    //System.out.println( dvd_degree-step + " -> " + lastc);
	    int count = 1;
	    for(int i=c-1; i>=lastc; i--)
	    {
		//System.out.println(c + " " +  (quo_degree-count));
		table[r+1][i] = 
		    quo_c[quo_degree-step+1] * dvs_c[dvs_degree-count];
		count++;
	    }
	    
	    // fill in the result of the subtraction
	    for(c=dvd_degree-step; c>=lastc; c--)
		table[r+2][c] = table[r][c] - table[r+1][c];

	    // bring down the next term of dividend
	    if (step<num_steps)
		table[r+2][lastc-1] = table[0][lastc-1];
	}


	for(int row=0; row<table.length; row++)
	{
	    for(int col=table[0].length-1; col>=0; col--)
		if (table[row][col]==LARGE_INT)
		    System.out.print("\t");
	    else
		    System.out.print("\t"+table[row][col]);
	    System.out.println();
	}

    }// performFullDivision method

    private static void assignCoefficients()
    {
	/*
	dvd_degree = 3;
	dvs_degree = 1;
	quo_degree = 2;

	dvd_c = new int[ dvd_degree+1 ];
	dvs_c = new int[ dvs_degree+1 ];
	quo_c = new int[ quo_degree+1 ];

	dvd_c[3] = -5;
	dvd_c[2] = -5;
	dvd_c[1] = 0;	
	dvd_c[0] = -10;

	dvs_c[1] = 1;
	dvs_c[0] = 1;
	*/

	dvd_degree = 2 + rand.nextInt(MAX_DEGREE-1);
	dvs_degree = 1 +rand.nextInt(dvd_degree-1);
	quo_degree = dvd_degree - dvs_degree;

	dvd_c = new int[ dvd_degree+1 ];
	dvs_c = new int[ dvs_degree+1 ];
	quo_c = new int[ quo_degree+1 ];

	for(int c=0; c<dvd_degree; c++)
	    dvd_c[c] = rand.nextInt(MAX_COEFF+1); 
	dvd_c[dvd_degree] = 1+rand.nextInt(MAX_COEFF); 

	for(int c=0; c<dvs_degree; c++)
	    dvs_c[c] = rand.nextInt(MAX_COEFF+1); 
	dvs_c[dvs_degree] = 1; 

	// signs
	for(int c=0; c<=dvd_degree; c++)
	    if (rand.nextBoolean())
		dvd_c[c] *= -1;

	for(int c=0; c<=dvs_degree; c++)
	    if (rand.nextBoolean())
		dvs_c[c] *= -1;

    }// assignCoefficients method
 
    private static String s(int n)
    {
	return (n>=0 ? "+" : "-");
    }

    private static void createScript(String filename)
    {
	pseudocodeURL1 =  "polyDiv.php?;amp;line=";

	ht = new java.util.Hashtable<String,Visual>( 50 );
	initial = new Element( "initial");

	double lower = 0.2; // fraction of fontsize (for subscript)

	reg_size = 20;
	sup_size = 14;
	Graphics g= (new BufferedImage(1,1, 
                           BufferedImage.TYPE_INT_RGB)).getGraphics();
	g.setFont( new Font(ftName,Font.PLAIN,reg_size) );
	reg_fm = g.getFontMetrics();
	g.setFont( new Font(ftName,Font.PLAIN,sup_size) );
	sup_fm = g.getFontMetrics();
	
	// Create the root element
	Element root = new Element("xaal",
				   "http://www.cs.hut.fi/Research/SVG/XAAL");
	root.setAttribute( "version", "1.0");
	Document myDocument = new Document(root);

	/*******************************************
         * first snapshot, AKA the "initial" element
         *******************************************/

	Text t;
	Line l;

	int dvd_line = reg_size;
	int dvs_line = dvd_line + (int)(1.5*reg_size);
	int first_row = dvs_line + 4*reg_size;
	int wp = reg_fm.stringWidth("+");
	int wm = reg_fm.stringWidth("-");
	int wx = reg_fm.stringWidth("x");
	int[] w = new int[ dvd_degree+1 ];  // width of powers
	for(int i=dvd_degree; i>1; i--)
	    w[i] = sup_fm.stringWidth(i+"");
	int[] wc = new int[dvd_degree+1];  // width of columns in table
	for(int c=0; c<dvd_degree+1; c++)
	{
	    int max = -1;
	    for(int r=0; r<table.length; r++)
		if (table[r][c] != LARGE_INT)
		    wc[c] = 
			Math.max(wc[c], 
				 reg_fm.stringWidth(""+Math.abs(table[r][c])));
	}


	/*
	// precompute the width (for horizontal centering)
	int total_width = reg_fm.stringWidth( "" + dvs_c[dvs_degree]) + wx;
	if (dvs_degree>1)
	    total_width += sup_fm.stringWidth( "" + dvs_degree );
	for(int i=dvs_degree-1; i>=0; i--)
	    if (dvs_c[i] != 0)
	    {
		total_width += MED_SKIP + wp + MED_SKIP;
		total_width += sup_fm.stringWidth( "" + Math.abs(dvs_c[i]) );
		if (i>0) total_width += wx;
		if (i>1) total_width += sup_fm.stringWidth( "" + i );
	    }
	*/

	int x, y;
	
	// first line
	addText( "DVD_label", "Dividend:", LEFT, dvd_line, false);
	x = right("DVD_label") + BIG_SKIP;
	x = addTerm("dvd"+dvd_degree, dvd_c[dvd_degree], dvd_degree, 
		    x, dvd_line, false);
	for(int i=dvd_degree-1; i>=0; i--)
	    if (dvd_c[i] != 0)
	    {
		addText( "DVD_sign"+i, s(dvd_c[i]), x+MED_SKIP,dvd_line,
			 false);
		x = addTerm("dvd"+i,Math.abs(dvd_c[i]),i,x+wp+MED_SKIP,
			    dvd_line, false);
	    }

	// second line
	addText( "DVS_label", "Divisor:", LEFT, dvs_line, false);
	x = right("DVD_label") + BIG_SKIP;
	x = addTerm("dvs_"+dvs_degree, dvs_c[dvs_degree], dvs_degree, 
		    x, dvs_line, false);
	for(int i=dvs_degree-1; i>=0; i--)
	    if (dvs_c[i] != 0)
	    {
		addText( "DVS_sign"+i, s(dvs_c[i]), x+MED_SKIP,dvs_line,
			 false);
		x = addTerm("dvs_"+i,Math.abs(dvs_c[i]),i,x+wp+MED_SKIP,
			    dvs_line, false);
	    }

	// divisor at left of table
	int ldvs, rdvs, rdvs1=0;
	ldvs = LEFT - 3;
	x = addTerm("dvs"+dvs_degree, dvs_c[dvs_degree], dvs_degree, 
		    LEFT , first_row, false);
	rdvs1 = x+1;

	for(int i=dvs_degree-1; i>=0; i--)
	    if (dvs_c[i] != 0)
	    {
		addText( "DVS_sign"+i, s(dvs_c[i]), x+MED_SKIP,first_row,
			 false);
		x = addTerm("dvs"+i,Math.abs(dvs_c[i]),i,x+wp+MED_SKIP,
			    first_row, false);
	    }
	rdvs = x + 3;             // box around divisor
	int[] bX = new int[5];
	int[] bY = new int[5];
	bX[0] = ldvs;
	bY[0] = first_row + 1;
	bX[1] = bX[0];
	bY[1] = first_row - (int)(1.0*reg_size);
	bX[2] = rdvs;
	bY[2] = bY[1];
	bX[3] = bX[2];
	bY[3] = bY[0];
	bX[4] = bX[0];
	bY[4] = bY[0];
	addPolyline("boxDVS",bX,bY,"blue", true);

	// box around first term of divisor
	bX[2] = rdvs1;
	bX[3] = bX[2];
	addPolyline("boxDVS1",bX,bY,"red", true);

	int middle = x + reg_size/3;
	int[] bottom = new int[table.length];
	x += 2*reg_size/3;
	
	// quotient
	int left_boxQUO = x-1;
	int yquot = first_row - (int)(1.5*reg_size);
	bX[0] = x-1;
	bY[0] = yquot + 1;
	bX[1] = bX[0];
	bY[1] = yquot - (int)(1.0*reg_size);
	x = addTerm("QUO"+quo_degree, quo_c[quo_degree], quo_degree, 
		    x, yquot,  reg_size, reg_fm,"red",true);
	bX[2] = x;
	bY[2] = bY[1];
	bX[3] = bX[2];
	bY[3] = bY[0];
	bX[4] = bX[0];
	bY[4] = bY[0];
	addPolyline("boxQUO"+quo_degree,bX,bY,"red", true);

	for(int i=quo_degree-1; i>=0; i--)
	    //if (quo_c[i] != 0)
	    {
		bX[0] = x+MED_SKIP - 1;
		bX[1] = bX[0];
		addText( "QUO_sign"+i, s(quo_c[i]), x+MED_SKIP,yquot,
			 reg_size, reg_fm,"red",true);
		x = addTerm("QUO"+i,Math.abs(quo_c[i]),i,x+wp+MED_SKIP,yquot,
			    reg_size, reg_fm,"red", true);
		bX[2] = x+1;
		bX[3] = bX[2];
		bX[4] = bX[0];
		bY[4] = bY[0];
		addPolyline("boxQUO"+i,bX,bY,"red", true);
	    }
	int right_boxQUO = x + 1;
	bX[0] = bX[1] = bX[4] = left_boxQUO;
	bX[2] = bX[3] = right_boxQUO;
	bY[0] = bY[3] = bY[4] = yquot + 1;
	bY[1] = bY[2] = yquot - (int)(1.0*reg_size);

	addPolyline("boxQUO",bX,bY,"red", true);
	x = middle + reg_size;
	int left_remainder=0;
	// table
	for(int c=table[0].length-1; c>=0; c--)
	{
	    y = first_row;

	    x += wc[c]+wx+w[c]; // columns are right-aligned
	    for(int r=0; r<table.length; r++)
	    {
		int coeff = Math.abs(table[r][c]);
		if (coeff != LARGE_INT)
		{
		    if ((c==table[0].length-1) ||
			(table[r][c+1]==LARGE_INT)) // first term on its row
			coeff = table[r][c]; // restore the negative sign if any
		    
		    int xtmp = x-reg_fm.stringWidth(getCoeff(coeff,c))-w[c];

		    if ( (r==table.length-1) && (c==dvd_degree-num_steps) )
			left_remainder = xtmp;


		    if (r==0)
		    {
			addTerm("t"+r+"_"+c,coeff,c,xtmp,y, r!=0);
			addTerm("t"+r+"_"+c+"copy",coeff,c,xtmp,y,reg_size,
				reg_fm,	"red", true);
		    }
		    else
			addTerm("t"+r+"_"+c,coeff,c,xtmp,y,reg_size,reg_fm,
				"red", true);
		    
		    if ( (r%2 == 0) && (r!=2*num_steps) &&
			 ( (c==dvd_degree) || (table[r][c+1]==LARGE_INT) ) )
		    {
			// box around first term of remaining dividend
			// only on even-numbered row except the last one
			bX[0] = xtmp - 1;
			bY[0] = y + 1;
			bX[1] = bX[0];
			bY[1] = y - (int)(1.0*reg_size);
			bX[2] = x + 1;
			bY[2] = bY[1];
			bX[3] = bX[2];
			bY[3] = bY[0];
			bX[4] = bX[0];
			bY[4] = bY[0];
			addPolyline("boxDVD1_"+r,bX,bY,"blue", true);
		    }
		    if ( (c>0) && (table[r][c-1]!=LARGE_INT))
		    {
			// add the sign to the right of the column
			addText("s"+r+"_"+c,s(table[r][c-1]),x+MED_SKIP,y, 
				reg_size,reg_fm, 
				(r>0 ? "red" : "black"), r!=0); 
			if (r==0)
			    addText("s"+r+"_"+c+"copy",s(table[r][c-1]),
				    x+MED_SKIP,y, reg_size,reg_fm, "red", true);
		    }
		}
		bottom[r] = y;
		if (r % 2 == 1)
		    y += (int)(0.2*reg_size);
		y += (int)(1.2*reg_size);

	    }
	    x += wp+MED_SKIP;
	}
	// add division polyline
	int[] xl = new int[3];
	int[] yl = new int[3];
	xl[0] = middle;
	yl[0] = first_row + (int)(0.2*reg_size);
	xl[1] = xl[0];
	yl[1] = first_row - (int)(1.2*reg_size);
	xl[2] = right("t0_0");
	yl[2] = yl[1];
	addPolyline("line",xl,yl,"black", false);


	// add remainder box
	bX[0] = bX[1] = bX[4] = left_remainder - 1;
	bX[2] = bX[3] = right("t0_0");
	bY[0] = bY[3] = bY[4] = bottom[2*num_steps] + 2;
	bY[1] = bY[2] = bY[0] - (int)(1.0*reg_size) - 1;
	addPolyline("boxREM",bX,bY,"blue", true);



	// subtraction lines and boxes
	for(int step=1; step<=num_steps; step++)
	{
	    int row = 2*step-1;
	    int lcol = dvd_degree-step+1;
	    int rcol = lcol-dvs_degree;
	    int x1 = left("t"+row+"_"+lcol);
	    int x2;
	    if (rcol>1)
		x2 = right("t"+row+"_"+rcol+"_p"); 
	    else
		x2 = right("t"+row+"_"+rcol);
	    int y1 = bottom[2*step-1] + (int)(0.2*reg_size);
	    addLine("line"+step,x1,y1,x2,y1,"black",true);

	    bX[0] = bX[1] = bX[4] = x1 - 1;
	    bX[2] = bX[3] = x2 + 1;
	    bY[0] = bY[3] = bY[4] = bottom[2*step-1] + 2;
	    bY[1] = bY[2] = bY[0] - (int)(1.0*reg_size) - 1 ;
	    addPolyline("boxSUBbot"+step,bX,bY,"blue",true);

	    bY[0] = bY[3] = bY[4] = bottom[2*step-2] + 2 - 1;
	    bY[1] = bY[2] = bY[0] - (int)(1.0*reg_size);
	    addPolyline("boxSUBtop"+step,bX,bY,"red",true);
	}

	root.addContent( initial );

	/*******************************************
         *  animation 
         *******************************************/

	animation = new Element("animation");

	Element par;
	Element[] pars;
	Element seq = null;


	for(int step = 1; step <= num_steps; step++)
	{

	    //**********************************************************
	    // divide the first term of the divisor into the first term
	    // of the remaining dividend
	    //**********************************************************

	    pars = new Element[ 15 ];	    
	    int c=0; // counter for parallel ops in the snapsho

	    pars[c++] = show( new String[] { "boxDVS1", 
					     "boxDVD1_"+(2*step-2) });
	    pars[c++] = wait(1000);
	    int power = quo_degree-step+1;
	    String quo_term = "QUO"+ power;
	    String quo_term_sign = "QUO_sign" + power;
	    String quo_term_power = quo_term + "_p"; 
	    pars[c++] = show(quo_term);
	    if (step>1)
		if (power>1)
		    pars[c++] = show(new String[]{ quo_term_sign, 
						   quo_term, quo_term_power});
		else
		    pars[c++] = show(new String[]{ quo_term_sign, quo_term});
	    else // first column of quotient: no need to show separate sign
		if (power>1)
		    pars[c++] = show(new String[]{ quo_term, quo_term_power} );
		else
		    pars[c++] = show( quo_term );
	    pars[c++] = wait(1000);
	    pars[c++] = hide( new String[] { "boxDVS1", 
					     "boxDVD1_"+(2*step-2) });
	    pars[c++] = wait(1000);
	    if (step>1)
		if (power>1)
		    pars[c++] = black(new String[]{ quo_term_sign, 
						   quo_term, quo_term_power});
		else
		    pars[c++] = black(new String[]{ quo_term_sign, quo_term});
	    else // first column of quotient: no need to show separate sign
		if (power>1)
		    pars[c++] = black(new String[]{ quo_term, quo_term_power} );
		else
		    pars[c++] = black( quo_term );

	    seq = makeSeq( pseudocodeURL1 + "0",  pars, c );
	    animation.addContent( seq );

	    //**********************************************************
	    // multiply the newly found quotient term by the divisor
	    //**********************************************************

	    pars = new Element[ 50 ];	    
	    c=0; // counter for parallel ops in the snapshot

	    String[] boxes = new String[]{ "boxDVS", 
					   "boxQUO" + (quo_degree-step+1)};
	    pars[c++] = show( boxes );
	    pars[c++] = wait(1000);
	    
	    ArrayList<String> terms = new ArrayList<String>( 15 );		
	    for(int i=dvd_degree-step+1; i>=dvd_degree-step+1-dvs_degree; i--)
	    {
		terms.add( "t"+ (2*step-1) + "_" + i );
		if (i<dvd_degree-step+1)
		    terms.add( "s"+ (2*step-1) + "_" + (i+1) );
		if (i>1)
		    terms.add( "t"+ (2*step-1) + "_" + i + "_p" );
	    }
	    //terms.add("t"+(2*step-1)+ "_" + (dvd_degree-step+1-dvs_degree) );
	    pars[c++] = show( terms.toArray( new String[1]) );

	    pars[c++] = wait(1000);
	    pars[c++] = hide( boxes);
	    pars[c++] = wait(1000);
	    pars[c++] = black( terms.toArray( new String[1]) );

	    seq = makeSeq( pseudocodeURL1 + "1",  pars, c );
	    animation.addContent( seq );


	    //**********************************************************
	    // subtract the newly obtained polynomial from the dividend
	    //**********************************************************

	    pars = new Element[ 50 ];	    
	    c=0; // counter for parallel ops in the snapshot


	    pars[c++] = show( new String[]{"boxSUBtop"+step, "boxSUBbot"+step});
	    pars[c++] = wait(1000);
	    pars[c++] = hide( new String[]{"boxSUBtop"+step, "boxSUBbot"+step});
	    pars[c++] = show( "line" + step );
	    terms = new ArrayList<String>( 15 );		
	    for(int i=dvd_degree-step; i>=dvd_degree-step+1-dvs_degree; i--)
	    {
		terms.add( "t"+ (2*step) + "_" + i );
		if (i<dvd_degree-step)
		    terms.add( "s"+ (2*step) + "_" + (i+1) );
		if (i>1)
		    terms.add( "t"+ (2*step) + "_" + i + "_p" );
	    }
	    pars[c++] = show( terms.toArray( new String[1]) );
	    pars[c++] = wait(1000);
	    pars[c++] = black( terms.toArray( new String[1]) );

	    seq = makeSeq( pseudocodeURL1 + "2",  pars, c );
	    animation.addContent( seq );

	    //**********************************************************
	    // bring down the next dividend
	    //**********************************************************

	    if (step < num_steps)
	    {
		pars = new Element[ 50 ];	    
		c=0; // counter for parallel ops in the snapshot
		
		int down_index = dvd_degree - step -dvs_degree;
		terms = new ArrayList<String>( 15 );
		terms.add( "s0_"+ (down_index+1)+"copy" );
		terms.add( "t0_"+ down_index+"copy" );
		if (down_index>1)
		    terms.add( "t0_"+ down_index+"copy_p" );
		pars[c++] = show( terms.toArray( new String[1]) );
		pars[c++] = wait( 1000 );
		pars[c++] = move( terms.toArray( new String[1]), 
				  0, bottom[2*step]-first_row);
		pars[c++] = black( terms.toArray( new String[1]));

		seq = makeSeq( pseudocodeURL1 + "3",  pars, c );
		animation.addContent( seq );
	    }
	}// main division loop

	
	//**********************************************************
	// highlight the quotient and remainder
	//**********************************************************

	pars = new Element[ 50 ];	    
	int c=0; // counter for parallel ops in the snapshot
	
	pars[c++] = show( new String[]{ "boxQUO","boxREM" } );
	seq = makeSeq( pseudocodeURL1 + "4",  pars, c );
	animation.addContent( seq );
	
	root.addContent( animation );
	
	try {
	    XMLOutputter outputter = 
		new XMLOutputter(Format.getPrettyFormat());
	    FileWriter writer = new FileWriter(filename);
	    outputter.output(myDocument, writer);
	    writer.close(); 
	} catch (java.io.IOException e) 
	{
	    e.printStackTrace();
	}


    }// createScript method

    private static String getCoeff( int coeff, int pow)
    {
	String term;
	if (pow==0)
	    term = coeff + "";
	else
	    switch (coeff) 
	    {
	    case 1 : term = "x"; break;
	    case -1: term = "-x"; break;
	    default: term = coeff +"x"; break;
	    }
	return term;
    }

    private static int addTerm(String label, int coeff, int pow, int x, int y,
			       boolean hidden)
    {
	String term = getCoeff(coeff, pow);

	addText(label, term, x, y, hidden);
	x += reg_fm.stringWidth(term);

	if (pow>1)
	{
	    addText(label+"_p", pow+"", x, y-(int)(0.35*reg_size),sup_size,
		    sup_fm, "black",hidden);
	    x += sup_fm.stringWidth(pow+"");
	}
	return x;
    }


    private static int addTerm(String label, int coeff, int pow, int x, int y,
			       int ftsize, FontMetrics fm, String color,
			       boolean hidden)
    {
	String term = getCoeff(coeff, pow);

	addText(label, term, x, y, ftsize, fm, color,hidden);
	x += reg_fm.stringWidth(term);

	if (pow>1)
	{
	    addText(label+"_p", pow+"", x, y-(int)(0.35*reg_size),sup_size,
		    sup_fm, color,hidden);
	    x += sup_fm.stringWidth(pow+"");
	}
	return x;
    }

    private static int right(String s)
    {
	return ht.get(s).getRight();
    }

    private static int left(String s)
    {
	return ht.get(s).getLeft();
    }


    private static int width(String s)
    {
	return ht.get(s).getWidth();
    }

    private static void addText( String id, String text, int x, int y, 
				 int ftsize, 
				 FontMetrics fm, String color, boolean hidden)
    { 
	Text t = new Text( id, text, x, y, ftsize, fm, color, hidden);
	ht.put( id, t );
	initial.addContent( t.makeElement() );
    }


    private static void addText( String id, String text, int x, int y, 
				 boolean hidden)
    { 
	Text t = new Text( id, text, x, y, reg_size, reg_fm, "black", hidden);
	ht.put( id, t );
	initial.addContent( t.makeElement() );
    }

    private static void addText( String id, String text, int x, int y, 
				 int ftsize, FontMetrics fm, String color, 
				 boolean hidden, String family,
				 boolean italic, boolean bold)
    { 
	Text t = new Text( id, text, x, y, ftsize, fm, color, hidden,
			   family,italic, bold);
	ht.put( id, t );
	initial.addContent( t.makeElement() );
    }

    private static void addLine( String id, int x1, int y1, int x2, int y2, 
				 String color, boolean hidden)
    { 
	Line l = new Line( id, x1, y1, x2, y2, color, hidden);
	ht.put( id, l );
	initial.addContent( l.makeElement() );
    }


    private static void addPolyline( String id, 
				     int[] x, int[] y,
				     String color, boolean hidden)
    { 
	Polyline l = new Polyline( id, x, y, color, hidden);

	ht.put( id, l );
	initial.addContent( l.makeElement() );
    }


    private static void addPolyline( String id, 
				     int[] x, int[] y,
				     String color, boolean hidden,
				     String red, String green, String blue)
    { 
	Polyline l = new Polyline( id, x, y, color, hidden, red, green, blue);

	ht.put( id, l );
	initial.addContent( l.makeElement() );
    }


    private static void addRational( String id, int num, int den, int x, int y,
				     int ftsize, FontMetrics fm, 
				     String color, boolean hidden)
    { 
	Rational r = new Rational( id, num, den, x, y, ftsize, fm, 
				   color, hidden);
	ht.put( id, r );
	Element[] elts = r.makeElements();
	for(int index=0; index < elts.length; index++)
	    initial.addContent( elts[index] );
    }


    /*********************************************************
     * generate XML elements
     *********************************************************/

    private static Element makeText(String id, boolean hidden, 
				    int x, int y, String s,
				    String colorname, int fontSize)
    {
	Element text = new Element("text");
	text.setAttribute("id",id);
	if (hidden) text.setAttribute( "hidden", "true" );
	Element coord = new Element("coordinate");
	coord.setAttribute("x",x+"");
	coord.setAttribute("y",y+"");
	Element contents = new Element("contents");
	contents.addContent(s);
	Element style = new Element( "style" );
	Element color = new Element( "color" );
	color.setAttribute("name",colorname);
	style.addContent( color );
	Element font = new Element( "font" );
	font.setAttribute( "size", fontSize + "");
	style.addContent( font );

	text.addContent(coord);
	text.addContent(contents);
	text.addContent(style);
	return text;
    }

    private static Element makeLine(String id, boolean hidden, 
				    int x1, int y1, int x2, int y2, 
				    String colorname)
    {
	Element line = new Element("line");
	line.setAttribute("id",id);
	if (hidden) line.setAttribute( "hidden", "true" );
	Element coord1 = new Element("coordinate");
	coord1.setAttribute("x",x1+"");
	coord1.setAttribute("y",y1+"");
	line.addContent(coord1);
	Element coord2 = new Element("coordinate");
	coord2.setAttribute("x",x2+"");
	coord2.setAttribute("y",y2+"");
	line.addContent(coord2);
	Element style = new Element( "style" );
	Element color = new Element( "color" );
	color.setAttribute("name",colorname);
	style.addContent( color );
	line.addContent(style);

	return line;
    }

    private static Element makePolyline(String id, boolean hidden, 
					int[] x, int[] y, String colorname)
    {
	Element line = new Element("polyline");
	line.setAttribute("id",id);
	if (hidden) line.setAttribute( "hidden", "true" );
	for(int i=0; i<x.length; i++)
	{
	    Element coord = new Element("coordinate");
	    coord.setAttribute("x",x[i]+"");
	    coord.setAttribute("y",y[i]+"");
	    line.addContent(coord);
	}


	Element style = new Element( "style" );
	Element color = new Element( "color" );
	color.setAttribute("name",colorname);
	style.addContent( color );
	line.addContent(style);

	return line;
    }


    private static Element makeChangeStyle(String[] ids, String colorname)
    {
	Element ch = new Element("change-style");
	for(int i=0; i<ids.length; i++)
	{
	    Element obj = new Element("object-ref");
	    obj.setAttribute("id",ids[i]);
	    ch.addContent(obj);
	}
	Element style = new Element( "style" );
	Element color = new Element( "color" );
	color.setAttribute("name",colorname);
	style.addContent( color );
	ch.addContent(style);

	return ch;
    }


    private static Element makeMove(String[] ids, int dx, int dy)
    {
	Element move = new Element("move");
	move.setAttribute("type","translate");
	Element coord = new Element("coordinate");
	coord.setAttribute("x",dx+"");
	coord.setAttribute("y",dy+"");
	move.addContent( coord );
	for(int i=0; i<ids.length; i++)
	{
	    Element obj = new Element("object-ref");
	    obj.setAttribute("id",ids[i]);
	    move.addContent(obj);
	}

	return move;
    }


    private static Element makeWait(int ms)
    {
	Element move = new Element("move");
	move.setAttribute("type","move");
	Element coord = new Element("coordinate");
	coord.setAttribute("x",ms+"");
	coord.setAttribute("y","0");
	move.addContent( coord );

	return move;
    }


    private static Element makeNoop()
    {
	return new Element( "noop" );
    }


    private static Element makeShowHide(String show_hide, String[] ids)
    {
	Element op = new Element(show_hide);
	op.setAttribute("type","selected");
	for(int i=0; i<ids.length; i++)
	{
	    Element obj = new Element("object-ref");
	    Visual v = ht.get( ids[i] );
	    boolean showOp = show_hide.equals( "show" );
	    if (v == null)
	    {
		if (ids[i].endsWith("_num") ||  // rational
		    ids[i].endsWith("_div") ||
		    ids[i].endsWith("_den") )
		    ht.get(ids[i].substring(0,ids[i].length()-4)).setVisible(showOp);
		else
		    System.out.println("In makeShowHide: " + show_hide + 
				       " null element: " + ids[i]);

	    }
	    else
	    {
		v.setVisible( showOp );
		//System.out.println(ids[i]);
	    }
	    obj.setAttribute("id",ids[i]);
	    op.addContent(obj);
	}

	return op;
    }


    private static Element makeSeq(String narrative, Element[] par, int n)
    {
	Element seq = new Element( "seq" );
	seq.addContent( new Element("narrative").addContent(narrative));
	for(int p=0; p<n; p++)
	    seq.addContent( par[p] );
	return seq;
    }


    private static Element makePar1(Element op)
    {
	Element par = new Element( "par" );
	par.addContent( op );
	return par;
    }

    private static Element makePar(Element[] ops)
    {
	Element par = new Element( "par" );
	for(int op=0; op<ops.length; op++)
	    if (!(ops[op].getName().equals("noop")))
		par.addContent( ops[op] );
	return par;
    }



    /*********************************************************
     * generate random values for the coefficients a, b, and c
     *********************************************************/

    private static int gcd(int a, int b)  /* a>0 and b>0 */
    {
	int min = Math.min(a,b);
	int max = Math.max(a,b);
	int rem = max % min;
       
	if ( rem == 0 ) return min;
	else return gcd( min, rem );
    }





    static Element show1( String s )
    {
	return makeShowHide("show", new String[]{ s });
    }


    static Element show1( String[] s )
    {
	return makeShowHide("show", s);
    }

    static Element show( String s )
    {
	return makePar1(show1(s));
    }


    static Element show( String[] s )
    {
	return makePar1(show1(s));
    }

    static Element hide1( String s )
    {
	return makeShowHide("hide", new String[]{ s });
    }


    static Element hide( String s )
    {
	return makePar1(hide1(s));
    }


    static Element hide( String[] s )
    {
	return makePar1(hide1(s));
    }
    static Element hide1( String[] s )
    {
	return makeShowHide("hide", s);
    }


    static Element wait( int n )
    {
	return makePar1( makeWait( n ));
    }


    static Element red1( String s )
    {
	return	makeChangeStyle( new String[]{s},"red");
    }

    static Element red1( String[] s )
    {
	return  makeChangeStyle( s,"red");
    }

    static Element red( String s )
    {
	return	makePar1( red1(s) );
    }

    static Element red( String[] s )
    {
	return	makePar1( red1(s));
    }


    static Element black1( String s )
    {
	return	makeChangeStyle( new String[]{s},"black");
    }

    static Element black1( String[] s )
    {
	return	makeChangeStyle( s,"black");
    }

    static Element black( String s )
    {
	return	makePar1( black1(s));
    }

    static Element black( String[] s )
    {
	return	makePar1( black1(s));
    }



    // standalone move (as part of a <par> op)
    static Element move( String s, int dx, int dy )
    {
	return makePar1(move1(s, dx, dy));
    }

    // standalone move (as part of a <par> op)
    static Element move( String[] s, int dx, int dy )
    {
	return makePar1(move1(s, dx, dy));
    }

    // when move is used inside a <par> op, among other ops
    static Element move1( String s, int dx, int dy )
    {
	return makeMove(new String[]{s}, dx, dy);
    }

    // when move is used inside a <par> op, among other ops
    static Element move1( String[] s, int dx, int dy )
    {
	return makeMove(s, dx, dy);
    }
}