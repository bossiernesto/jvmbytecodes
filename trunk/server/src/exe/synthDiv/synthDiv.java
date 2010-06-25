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

package exe.synthDiv;

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

public class synthDiv
{
    public static String  ftName = "Lucida Bright";
    private static Random rand = new Random();

    private static int SMALL_SKIP = 2;
    private static int MED_SKIP = 6;
    private static int BIG_SKIP = 10;
    private static int HUGE_SKIP = 20;
    private static int BASELINE = 300;
    private static int LEFT= 0;


    private  static String pseudocodeURL1;

    private static java.util.Hashtable<String,Visual> ht;
    private static Element initial;
    private static Element animation;
    private static Element questions;


    public static final int LARGE_INT = 999999999;
    public static final int MAX_DEGREE = 5;
    public static final int MAX_COEFF = 10;
    private static int dvd_degree, dvs_degree, quo_degree, rem_degree;
    private static int num_steps;
    private static int[][] table;
    private static int[] dvd_c;
    private static int[] dvs_c;
    private static int[] quo_c;
    private static int c;

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

	num_steps = dvd_degree;

	table = new int[3][dvd_degree+1];
	for(int col=0; col<=dvd_degree; col++)
	    table[0][col] = dvd_c[col];
	table[1][dvd_degree] = 0; // unused
	table[2][dvd_degree] = dvd_c[dvd_degree]; // simply brought down
	for(int step=1; step<=num_steps; step++)
	{
	    int col = dvd_degree - step;
	    table[1][col] = c * table[2][col+1];
	    table[2][col] = table[0][col] + table[1][col];
	}

	System.out.println("c = " + c);
	for(int row=0; row<table.length; row++)
	{
	    for(int col=table[0].length-1; col>=0; col--)
		if ((row==1) && (col==dvd_degree))
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
	dvd_c[2] = 0;
	dvd_c[1] = 1;	
	dvd_c[0] = 0;

	dvs_c[1] = 1;
	dvs_c[0] = 1;

	*/

	dvd_degree = 2 + rand.nextInt(MAX_DEGREE-1);
	dvs_degree = 1;
	quo_degree = dvd_degree - dvs_degree;

	dvd_c = new int[ dvd_degree+1 ];
	dvs_c = new int[ dvs_degree+1 ];
	quo_c = new int[ quo_degree+1 ];

	for(int c=0; c<dvd_degree; c++)
	    dvd_c[c] = rand.nextInt(MAX_COEFF+1); 
	dvd_c[dvd_degree] = 1+rand.nextInt(MAX_COEFF); 

	dvs_c[1] = 1;
	dvs_c[0] = rand.nextInt(MAX_COEFF+1); 

	// only accept 0 as a value for c in 5% of the cases
	if (dvs_c[0]==0)
	    if (rand.nextInt(100)<95)
		dvs_c[0] += 1 + rand.nextInt(MAX_COEFF);


	// signs
	for(int c=0; c<=dvd_degree; c++)
	    if (rand.nextBoolean())
		dvd_c[c] *= -1;

	if (rand.nextBoolean())
	    dvs_c[0] *= -1;

	c = - dvs_c[0];

    }// assignCoefficients method
 
    private static String s(int n)
    {
	return (n>=0 ? "+" : "-");
    }

    private static void createScript(String filename)
    {
	pseudocodeURL1 =  "synthDiv.php?;amp;line=";
	questions = makeQuestions();

	ht = new java.util.Hashtable<String,Visual>( 50 );
	initial = new Element( "initial");

	reg_size = 20;
	sup_size = 14;
	Graphics g= (new BufferedImage(1,1, 
                           BufferedImage.TYPE_INT_RGB)).getGraphics();
	g.setFont( new Font(ftName,Font.PLAIN,reg_size) );
	reg_fm = g.getFontMetrics();
	g.setFont( new Font(ftName,Font.PLAIN,sup_size) );
	sup_fm = g.getFontMetrics();
	g.setFont( new Font(ftName,Font.BOLD,reg_size) );
	FontMetrics bold_fm = g.getFontMetrics();

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

	int[] bX = new int[5];
	int[] bY = new int[5];
	int dvd_line = reg_size;
	int dvs_line = dvd_line + (int)(1.5*reg_size);
	int yrow0 = dvs_line + 5*reg_size;
	int yrow1 = yrow0 + (int)(1.5*reg_size);
	int yrow2 = yrow1 + (int)(2.0*reg_size);
	int mult_row = yrow1 + (int)(6.0*reg_size);
	int frow1 = mult_row + reg_size;
	int frow2 = frow1 + (int)(1.5*reg_size);
	int frow3 = frow2 + (int)(1.5*reg_size);
	int wp = reg_fm.stringWidth("+");
	int wm = reg_fm.stringWidth("-");
	int wx = reg_fm.stringWidth("x");
	int[] wc = new int[dvd_degree+1];  // width of columns in table
	for(int c=0; c<dvd_degree+1; c++)
	{
	    int max = -1;
	    for(int r=0; r<table.length; r++)
		if (table[r][c] != LARGE_INT)
		    wc[c] = 
			Math.max(wc[c], 
				 reg_fm.stringWidth(""+table[r][c]));
	}
	int[] rc = new int[ dvd_degree + 1 ]; // right x coord of columns 
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
	addText( "XminusC", "x ", right("DVD_label")+BIG_SKIP, 
		 dvs_line, false);
	addText( "XminusCa", "- c", right("XminusC"),dvs_line,
		 reg_size, bold_fm, "orange",false, ftName, false, true);
	addText( "cDown", "c", 
		 left("XminusCa")+bold_fm.stringWidth("- "),dvs_line,
		 reg_size, bold_fm, "red",true, ftName, false, true);
	addText( "XminusCb", " = x", right("XminusCa")+MED_SKIP,dvs_line,false);
	x = right("XminusCb");
	//x = addTerm("dvs_"+dvs_degree, dvs_c[dvs_degree], dvs_degree, 
	//    x, dvs_line, false);
	addText( "DVS_sign0", s(dvs_c[0]),x,dvs_line,
		 reg_size, reg_fm, "orange",false, ftName, false, true);
	x += width("DVS_sign0") + MED_SKIP;
	addText("dvs_0",""+Math.abs(dvs_c[0]),x, dvs_line, 
		    reg_size, reg_fm, "orange",false, ftName, false, true);


	// c + copies
	addText( "c", c+"", LEFT,yrow0,reg_size, reg_fm,"red", true);
	for(int step=1; step<=num_steps; step++)
	    addText( "c"+step, c+"", LEFT,yrow0,reg_size, reg_fm,"red", true);
	
	addLine("cline1",LEFT,yrow0+5,right("c")+5,yrow0+5,"red",true);
	addLine("cline2",right("cline1"),yrow0+5, right("cline1"),
		yrow0-reg_size-2,"red",true);

	x = right("cline1") + 20; // left boundary of table

	// first row of table
	for(int c=table[0].length-1; c>=0; c--)
	{
	    x += wc[c]; // columns are right-aligned
	    rc[c] = x;
	    int v = table[0][c];
	    addText("t0_"+c, v+"", x-reg_fm.stringWidth(v+""),yrow0,true);
	    if (c==table[0].length-1)
		addText("t0_"+c+"copy", v+"", x-reg_fm.stringWidth(v+""),
			yrow0,reg_size,reg_fm,"red",true);
	    x += BIG_SKIP;
	}

	// translation of DVD coeffs for initial setup (needs rc[])
	int[] dx = new int[dvd_degree+1];
	for(int i=0; i<=dvd_degree; i++)
	    if (dvd_c[i] != 0)
		dx[i] = rc[i] - width("dvd"+i+"copy") - left("dvd"+i+"copy");

	int dy = yrow0 - dvd_line;

	/*
	// third row of table (the first one, which will be brought down)
	x = right("t0_" + dvd_degree) + BIG_SKIP;
	for(int c=table[0].length-2; c>=0; c--)
	{
	    x += wc[c]; // columns are right-aligned
	    int v = table[2][c];
	    addText("t2_"+c, v+"", x-reg_fm.stringWidth(v+""),yrow2,false);
	    x += BIG_SKIP;
	}
	*/

	// third row of table (the first element will be brought down)
	x = left("t0_" + dvd_degree);
	for(int c=table[0].length-1; c>=0; c--)
	{
	    x += wc[c]; // columns are right-aligned
	    int v = table[2][c];
	    //if (c<table[0].length-1) // see comment above
	    addText("t2_"+c, v+"", x-reg_fm.stringWidth(v+""),yrow2,
		    reg_size,reg_fm,"red",true);
	    addText("t2_"+c+"copy1", v+"", x-reg_fm.stringWidth(v+""),yrow2,
		    reg_size,reg_fm,"red",true);
	    addText("t2_"+c+"copy2", v+"", x-reg_fm.stringWidth(v+""),yrow2,
		    reg_size,reg_fm,"red",true);

	    x += BIG_SKIP;
	}

	// line on top of third row
	int yvline =  yrow2-(int)(1.2*reg_size);
	addLine("hline",left("t0_"+dvd_degree),yvline, 
		right("t0_0"), yvline,"red",true);
	addLine("vline",left("t2_0")-5, yvline, 
		left("t2_0")-5, yrow2+2,"red",true);


	

	// multiplication line: one '*' but multiple '=' 
	addText("*","*",LEFT-40 + width("c") + MED_SKIP, mult_row,
		reg_size, reg_fm, "red", true);

	// translation of coeffs after the '*'
	int[] dx2 = new int[dvd_degree+1];
	for(int i=1; i<=dvd_degree; i++) // rightmost coeff does not move
	    dx2[i] =  right("*")+MED_SKIP +
		(table[2][i]<0 ? reg_fm.stringWidth("(") : 0) - 
		 left("t2_"+ i +"copy1");

	int dy2 = mult_row - yrow2;

	for(int step=1; step<=num_steps; step++)
	{
	    int col = dvd_degree-step+1;
	    int xeq = 0;



	    if (table[2][col]<0)
	    {
		addText("("+step,"(", right("*")+MED_SKIP, mult_row, 
			reg_size, reg_fm, "red", true);

		ht.get("t2_"+ col +"copy1").translate(dx2[col], dy2);

		addText(")"+step,")", right("t2_"+ col +"copy1"), mult_row, 
			reg_size, reg_fm, "red", true);

		addText("="+step,"=", right(")"+step)+MED_SKIP, mult_row, 
			reg_size, reg_fm, "red", true);
	    }
	    else
	    {

		xeq = LEFT-40+width("c")+3*MED_SKIP+width("*")+ 
		    		    width("t2_"+ col +"copy1");

		addText("="+step,"=", xeq, mult_row, reg_size, reg_fm, "red", true);
	    }


	    addText("t1_" + (col-1),table[1][col-1]+"",right("="+step)+MED_SKIP, 
		    mult_row, reg_size, reg_fm, "red", true);
	}
	

	// display of final solution
	addText("frow1","Dividend = (x - c)(Quotient) + R    where:",
		LEFT, frow1,true);


	addText("q","Quotient = ", LEFT+13, frow2,true);

	int[] dx3 = new int[dvd_degree+1];
	int dy3 = frow2 - yrow2;
	int dy4 = frow3 - yrow2;	
	x = right("q") + MED_SKIP;

	// display the quotient polynomial
	dx3[dvd_degree] = x - left("t2_"+dvd_degree+"copy2");
	x = addTerm("QUO"+(dvd_degree-1), table[2][dvd_degree], dvd_degree-1, 
		    x, frow2, true);

	for(int i=dvd_degree-1; i>0; i--)
	{
	    addText( "QUOsign"+(i-1), s(table[2][i]), x+MED_SKIP,frow2,
		     true);
	    if (table[2][i]<0)
		dx3[i] = x+MED_SKIP - left("t2_"+i+"copy2");
	    else
		dx3[i] = x+wp+MED_SKIP - left("t2_"+i+"copy2");
	    x = addTerm("QUO"+(i-1),Math.abs(table[2][i]),i-1,x+wp+MED_SKIP,
			frow2, true);
	}

	addText("and","and", LEFT+13, frow3,true);
	addText("r","R = ", right("q")-reg_fm.stringWidth("R = "), frow3,true);
	dx3[0] = right("r") + MED_SKIP - left("t2_0copy2");

	for(int i=dvd_degree; i>=0; i--)
	    ht.get("t2_"+i+"copy2").translate(dx3[i], dy3);

	bX[0] = bX[1] = bX[4] = left("q") - 2;
	bX[2] = bX[3] = right("t2_1copy2") + 4;
	bY[0] = bY[3] = bY[4] = frow2 + 2;
	bY[1] = bY[2] = frow2 - (int)(1.0*reg_size);
	addPolyline("boxQUO", bX, bY, "red", true);

	bX[0] = bX[1] = bX[4] = left("r") - 2;
	bX[2] = bX[3] = right("t2_0copy2") + 2;
	bY[0] = bY[3] = bY[4] = frow3 + 2;
	bY[1] = bY[2] = frow3 - (int)(1.0*reg_size);
	addPolyline("boxREM", bX, bY, "red", true);
	root.addContent( initial );

	/*******************************************
         *  animation 
         *******************************************/

	animation = new Element("animation");

	Element par;
	Element[] pars;
	Element seq = null;
	int c = 0; // counter for parallel ops in the snapshot


	//**********************************************************
	// set up the table
	//**********************************************************

	pars = new Element[ 50 ];	    
	pars[c++] = show( "cDown" );
	pars[c++] = move( "cDown", LEFT-left("cDown"), yrow0-dvs_line);
	pars[c++] = wait(1000);
	pars[c++] = hide( "cDown" );
	pars[c++] = show( new String[] { "c" } );
	pars[c++] = wait(1000);
	pars[c++] = show( new String[] { "cline1", "cline2" } );
	pars[c++] = wait(1000);
	pars[c++] = black( new String[] { "c", "cline1", "cline2" } );
	pars[c++] = wait(1000);

	ArrayList<Element> listE = new ArrayList<Element>( 10 );
	ArrayList<String> listS = new ArrayList<String>( 10 );
	ArrayList<String> listS2 = new ArrayList<String>( 10 );
	for(int i=0; i<=dvd_degree; i++)
	{
	    listS2.add( "t0_"+i );
		
	    if (dvd_c[i] != 0)
	    {
		listS.add( "dvd"+i+"copy" );
		listE.add( move1("dvd"+i+"copy", dx[i], dy) );
	    }
	}
	pars[c++] = show( listS.toArray( new String[] { "dummy" } ));
	pars[c++] = makePar( listE.toArray( new Element[] 
                                             { new Element("dummy")} ));
	pars[c++] = wait(1000);
	pars[c++] = hide( listS.toArray( new String[] { "dummy" } ));
	pars[c++] = show( listS2.toArray( new String[] { "dummy" } ));

	//addQuestion( makeTFQuestion("1","You ok?","true") );
	seq = makeSeq( pseudocodeURL1 + "0",  pars, c );
	animation.addContent( seq );

	//**********************************************************
	// bring down the first coefficient of the dividend 
	//**********************************************************

	pars = new Element[ 10 ];
	c= 0;	    
	pars[c++] = red( "t0_"+dvd_degree );
	pars[c++] = wait(1000);
	pars[c++] = black( "t0_"+dvd_degree );
	pars[c++] = show( "t0_"+dvd_degree+"copy" );
	pars[c++] = move( "t0_"+dvd_degree+"copy", 0, yrow2-yrow0);
	pars[c++] = black( "t0_"+dvd_degree+"copy" );
	pars[c++] = show( "hline" );
	pars[c++] = wait(1000);
	pars[c++] = black( "hline" );
	

	
	//addQuestion( makeTFQuestion("1","You ok?","true") );
	seq = makeSeq( pseudocodeURL1 + "1",  pars, c );
	animation.addContent( seq );

	for(int step = 1; step <= num_steps; step++)
	{
	    
	    //**********************************************************
	    // multiply c and the next coeff 
	    //**********************************************************

	    pars = new Element[ 20 ];	    
	    c=0; 

	    int col = dvd_degree - step + 1;
	    String coeff = "t2_"+col;
	    String ccoeff = "t2_"+col + "copy1";

	    if (step==1)
	    {
		pars[c++] = show( coeff );                     // extra
		pars[c++] = hide( "t0_"+dvd_degree+"copy" );   // extra
		pars[c++] = red( new String[] { "c",coeff });
		pars[c++] = wait(1000);
		pars[c++] = black( new String[] { "c",coeff }); 
		pars[c++] = hide(coeff);                       // extra
		pars[c++] = show( "t0_"+dvd_degree+"copy" );   // extra
		pars[c++] = show( new String[] { "c"+step, ccoeff });
		pars[c++] = makePar( new Element[] 
		    {
			move1( "c"+step, -40, mult_row - yrow0),
			move1( ccoeff, dx2[col], dy2),
		    } );
		if (table[2][col]<0)
		    pars[c++] = show( new String[] { 
			    "*", "("+step, ")"+step } );	      		
		else
		    pars[c++] = show( "*" );	      		
	    }
	    else
	    {
		pars[c++] = red( new String[] { "c",coeff });
		pars[c++] = wait(1000);
		pars[c++] = black( new String[] { "c",coeff });
		pars[c++] = show( new String[] { "c"+step, ccoeff });
		pars[c++] = makePar( new Element[] 
		    {
			move1( "c"+step, -40, mult_row - yrow0),
			move1( ccoeff, dx2[col], dy2),
		    } );
		if (table[2][col]<0)
		    pars[c++] = show( new String[] { 
			    "*", "("+step, ")"+step } );	      		
		else
		    pars[c++] = show( "*" );	      		
		for(int i=0; i<4; i++)
		    pars[c++] = makeNoop();
	    }
	    
	    String nc = "t1_" + (col-1);
	    pars[c++] = wait(700);
	    pars[c++] = show( "=" + step);
	    pars[c++] = wait(700);
	    pars[c++] = show( nc );
	    pars[c++] = wait(1000);
	    if (table[2][col]<0)
		pars[c++] = hide( new String[] { "c"+step, "*", "("+step,
						 ")"+step, ccoeff , "="+step} );
	    else
		pars[c++] = hide( new String[] { "c"+step, "*",  
						 ccoeff , "="+step} );
	    pars[c++] = move( nc,   
			      rc[col-1]-width(nc) - left(nc), 
			      yrow1 - mult_row);
	    pars[c++] = black( nc );

	    seq = makeSeq( pseudocodeURL1 + "2",  pars, c );
	    animation.addContent( seq );

	    //**********************************************************
	    // add the two coefficients
	    //**********************************************************

	    pars = new Element[ 20 ];	    
	    c=0; 


	    pars[c++] = red( new String[] { nc, "t0_" + (col-1) } );
	    pars[c++] = wait(1000);
	    pars[c++] = show( "t2_" + (col-1));

	    pars[c++] = wait(1000);
	    pars[c++] = black(new String[] { "t0_" + (col-1), nc,
					     "t2_" + (col-1) } );


	    if (step == num_steps-1)
	    {
		pars[c++] = show( "vline" );
		pars[c++] = wait(1000);
		pars[c++] = black( "vline" );
	    }

	    seq = makeSeq( pseudocodeURL1 + "3",  pars, c );
	    animation.addContent( seq );

	}

	//**********************************************************
	// display the result of the division
	//**********************************************************

	pars = new Element[ 20 ];	    
		c=0; 


	pars[c++] = show("frow1");
	pars[c++] = wait(1000);
	pars[c++] = show("q");

	pars[c++] = wait(1000);
	listS = new ArrayList<String>( 10 );
        for(int i=dvd_degree; i>0; i--)
	    listS.add( "t2_"+i+"copy2");
	pars[c++] = show( listS.toArray( new String[] { "" } ));
	

	listE = new ArrayList<Element>( 10 );
	for(int i=dvd_degree; i>0; i--)
	    listE.add( move1("t2_"+i+"copy2", dx3[i], dy3) );

	pars[c++] = makePar( listE.toArray( new Element[] 
                                             { new Element("dummy")} ));

	pars[c++] = hide(listS.toArray( new String[] { "" } ));
	listS2 = new ArrayList<String>( 20 );
	listS2.add( "QUO"+(dvd_degree-1));
	if (dvd_degree>2)
	    listS2.add( "QUO"+(dvd_degree-1)+"_p");
        for(int i=dvd_degree-2; i>=0; i--)
	{
	    listS2.add( "QUOsign"  + i);
	    listS2.add( "QUO"  + i);
	    if (i>1)
		listS2.add( "QUO"  + i + "_p");
	}
	pars[c++] = show( listS2.toArray( new String[] { "" } ));



	pars[c++] = wait(1000);
	pars[c++] = show(new String[]{ "and", "r"});
	


	pars[c++] = wait(1000);
	pars[c++] = show( "t2_0copy2" );
	pars[c++] = move( "t2_0copy2", dx3[0], dy4);
	pars[c++] = black( "t2_0copy2" );

	pars[c++] = show( new String[]{ "boxQUO", "boxREM" } );

	seq = makeSeq( pseudocodeURL1 + "4",  pars, c );
	animation.addContent( seq );

	root.addContent( animation );
	root.addContent( questions );
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
	
	/* begin: copy for initial setup of coefficients in the table */
	int v =  dvd_c[pow];
	int correction = 0;
	if (pow==dvd_degree)
	{
	    if (v==1)
		correction = 2*reg_fm.stringWidth("x")/3;
	    else if (v==-1)
		correction = 2*reg_fm.stringWidth("x")/3; // reg_fm.stringWidth("-");
	    //else if (v<0)
	    //	correction = reg_fm.stringWidth("-");
	} else if (pow==0)
	{
	    if (v==-1)
		correction = reg_fm.stringWidth("-");
	    else if (v<0)
		correction = reg_fm.stringWidth("-");
	} else 	if (v==-1)
	    correction = reg_fm.stringWidth("-") + 
		2*reg_fm.stringWidth("x")/3;
	else if (v==1)
	    correction = 2*reg_fm.stringWidth("x")/3;
	else if (v<0)
	    correction = reg_fm.stringWidth("-");

	addText(label+"copy", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	/* end: copy for initial setup of coefficients in the table */



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
				     String red, String green, String orrange)
    { 
	Polyline l = new Polyline( id, x, y, color, hidden, red, green, orrange);

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

    static Element makeQuestions()
    {
	return new Element("questions");
    }

    static Element makeTFQuestion(String snap, String text, String correct)
    {
	Element q = new Element( "question");
	q.setAttribute("type", "TFQUESTION");
	q.setAttribute("id", snap);
	Element t = new Element( "question_text" );
	t.addContent( text );
	q.addContent( t );
	Element a = new Element( "answer_option" );
	a.setAttribute( "is_correct" , "yes" );
	a.addContent( correct );
	q.addContent( a );
	return q;
    }

    static void addQuestion(Element q)
    {
	questions.addContent( q );
    }
}