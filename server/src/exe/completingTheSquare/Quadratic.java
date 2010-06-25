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

package exe.completingTheSquare;

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
import java.util.Iterator;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Quadratic
{
    private static int SMALL_SKIP = 2;
    private static int MED_SKIP = 6;
    private static int BIG_SKIP = 10;
    private static int HUGE_SKIP = 20;
    private static int BASELINE = 300;
    private static int LEFT = 20;

    private  static String pseudocodeURL1;
    private  static String pseudocodeURL2;

    private static java.util.Hashtable<String,Visual> ht;
    private static Element initial;
    private static Element animation;

    public static void main(String[] args)
    {
	int[] values = new int[3];

	String filename = args[0];


	if (args.length == 4)
        {
	    for(int i=0; i<3; i++)
		values[i] = Integer.parseInt( args[i+1] );
	}
	else
	    assignValues(values);

	int a = values[0];
	int b = values[1];
	int c = values[2];



	/*	

	a = Math.abs(a);
	b = Math.abs(b);
	c = Math.abs(c);

	if ( (b==0) && (c==0) )
	    System.out.println();
	else if (b==0) 
	    if (gcd(a,c)>1)
		System.out.println(" ===========================> ");
	    else
		System.out.println();
	else if (c==0)
	    if (gcd(a,b)>1)
		System.out.println(" ===========================> ");
	    else
		System.out.println();
	else if ( gcd( gcd(a,b), gcd(b,c) ) > 1 )
	    System.out.println(" ===========================> ");
	else
	    System.out.println();
	*/
	
	boolean findVALUES = false;

	if (findVALUES)
	{
	for(a=-9; a<=9; a++)
          if (a!=0)
	  for(b=-9; b<=9; b++)
	    if (b!=0)
	    for(c=-9; c<=9; c++)
	      if (c!=0)
	      {	       		
		  int disc = b*b - 4*a*c;
		  int den = 4*a*a;

		  boolean RHSisINT = false;
		  if (disc % den == 0)
		      RHSisINT = true;
		  
		  if (RHSisINT)
		      System.out.println( "\t"+ a + "\t" + b + "\t" + c +
					  "\t" + "RHS = " + (disc/den));
		  else  // RHS is a rational
		  {
		      Rational r = new Rational( disc, 4*a*a);
		      disc = r.getNum();
		      int d = r.getDen();
		      int RHS = r.getNum() / r.getDen();
		      boolean discIsSquare = false;
		      boolean RHSsimplifies = false;
		      int discSQRT = 0;
		      if (disc>0)
		      {
			  discSQRT = (int) Math.sqrt( disc );
			  if (discSQRT * discSQRT == disc)
			      discIsSquare = true;
		      }
		      if (!discIsSquare && (d != 4*a*a))
			  RHSsimplifies = true;

		      if ( (discIsSquare) || (RHSsimplifies))
		      {
			  System.out.print( "\t"+ a + "\t" + b + "\t" + c +
					    "\t");
			  if (discIsSquare)
			      System.out.println(  "RHS " + (b*b-4*a*c) +
						  " / " + (4*a*a) + 
						   "\tdisc is square: " +
						  disc + " / " + r.getDen());
			  else
			  if (RHSsimplifies)
			      System.out.println( "RHS " + (b*b-4*a*c) +
						  " / " + (4*a*a) + 
						  "\tsimplifies: " +
						  r.getNum() + " / " +
						  r.getDen() );

			  //			  System.out.println();
		      }
		  }
	      }
	}
	else
	    
	    createScript(filename, a,b,c);

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

    private static void createScript(String filename, int a, int b, int c)
    {
	pseudocodeURL1 =  "completingTheSquare.php?;amp;line=";
	pseudocodeURL2 = 
	    ";amp;aval=" + a + ";amp;bval=" + b + 
	    ";amp;cval=" + c;
	//Vlist list = new Vlist(LEFT, BASELINE);
	ht = new java.util.Hashtable<String,Visual>( 50 );
	initial = new Element( "initial");

	String A, B, C;
	String A2, A3, A2b, A2c;
	String signA, signB, signC;
	String X2 = "x";      
	String SQ = "2";
	String X1 = "x";
	String EQ = "=";
	String ZERO = "0";
	String oppSignB, oppSignC;
	String CoverA = -c/a + "";
	String BoverA = Math.abs(b/a) + "";
	String Bover2A = Math.abs(b/(2*a)) + "";
	String abs2A = Math.abs(2*a) + "";
	boolean CoverAisINT = false;
	boolean BoverAisINT = false;
	boolean Bover2ASQisINT = false;	
	boolean Bover2AisINT = false;
	boolean RHSisINT = false;
	int RHS_int = -1;
	int SQRTdisc = 0;
	String LPAREN = "(";
	String RPAREN = ")";
	String X = "x";
	String SQ2 = "2";

	double raise = 0.4; // fraction of fontsize (for superscript)

	System.out.println( "\t"+ a + "\t" + b + "\t" + c );


	// compute the string values of a, b, an c
	if (Math.abs(a)<2) A = ""; else A = Math.abs(a) + "";
	if (Math.abs(b)<2) B = ""; else B = Math.abs(b) + "";
	if (c==0) C = ""; else C = Math.abs(c) + "";

	// compute the string value of the signs
	if (a==-1) {
	    signA = "-";
	    A2 = A2b = A2c = A3 = "-1";
	}
	else if (a==1) {
	    signA = "";
	    A2 = A2b = A2c = A3 = "1";
	}
	else if (a<0) {
	    signA = "-"; 
	    A2 = A2b = A2c = A3 = "-" + A;
	}
       	else {  // a>1
	    signA = ""; 
	    A2 = A2b = A2c = A3 = A;
	}

	if (b<0) {
	    signB = "-"; 
	    oppSignB = "+"; 
	}
	else if (b==0) {
	    signB = ""; 
	    oppSignB = ""; 
	}
	else {
	    signB = "+";
	    oppSignB = "-";
	}

	if (c<0) signC = "-"; 
	else if (c==0) signC = ""; 
	else signC = "+";
 
        if (signC.equals("+"))
	    oppSignC = "-";
	else if (signC.equals("-"))
	    oppSignC = "+";
	else oppSignC = "";

	int fontsize = 30;   // everything but superscript
	int supersize = 20;  // superscript

	Graphics g = (new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)).getGraphics();
	g.setFont( new Font("Serif",Font.PLAIN,fontsize) );
	FontMetrics fm = g.getFontMetrics();
	g.setFont( new Font("Serif",Font.PLAIN,supersize) );
	FontMetrics fmsuper = g.getFontMetrics();
	
	int correction = fm.stringWidth("+") - fm.stringWidth("-");
	
	// Create the root element
	Element root = new Element("xaal","http://www.cs.hut.fi/Research/SVG/XAAL");
	root.setAttribute( "version", "1.0");
	
	//root.setAttribute( "xmlns:xsi", 
	//         "http://www.w3.org/2001/XMLSchema-instance");
	//root.setAttribute( "xsi:schemaLocation", 
	//     "http://www.cs.hut.fi/Research/SVG/XAAL xaal.xsd");

	//create the document
	Document myDocument = new Document(root);

	/*******************************************
         * first snapshot, AKA the "initial" element
         *******************************************/

	Text t;
	Line l;

	addText( "signA", signA, LEFT, BASELINE, fontsize, fm, "black", false);

	addText("A", A, right("signA"),BASELINE,
		fontsize, fm, "black", false );
	addText("X2", X2, right("A"), BASELINE,
		fontsize, fm, "black", false );
	addText("SQ", SQ, right("X2"), 
		BASELINE-(int)(raise*fontsize),
		supersize, fmsuper, "black", false );
	addText("signB", signB, right("SQ")+MED_SKIP,
		BASELINE, fontsize, fm, "black", false );
	if (a<0)
	    addText("oppSignB", oppSignB, right("SQ")+ SMALL_SKIP,
		    BASELINE, fontsize, fm, "black", true );
	addText("B", B, right("signB") + MED_SKIP, BASELINE,
		fontsize, fm, "black", false );
	addText("X1", X1, right("B"),
		BASELINE, fontsize, fm, "black", false );
	addText("signC", signC, right("X1") + MED_SKIP,
		BASELINE, fontsize, fm, "black", false );
	addText("C", C, right("signC") + MED_SKIP, BASELINE,
		fontsize, fm, "black", false );
	addText("EQ", EQ, right("C") + HUGE_SKIP, BASELINE,
		fontsize, fm, "black", false );
	addText("ZERO", ZERO, right("EQ") + HUGE_SKIP,
		BASELINE, fontsize, fm, "black", false );
	addText("oppSignC", oppSignC, left("EQ"), BASELINE-4*fontsize,
		fontsize, fm, "red", true );
	addLine("div1", LEFT, BASELINE + (int) (0.1*fontsize),
		right("X1"),  BASELINE + (int) (0.1*fontsize),
		"red", true);
	addText("A2", A2, LEFT +
		((Line) ht.get("div1")).getWidth()/2 - fm.stringWidth(A2)/2,
		BASELINE + (int) (1.05*fontsize),
		fontsize, fm, "red", true );

	// translation of C and its sign to the right, above the = sign
	int dx_signC = left( "oppSignC" ) - left( "signC" );
	int dx1_C = right( "oppSignC" ) + SMALL_SKIP - left("C");
	int dy = ((Text) ht.get("oppSignC")).getY() - BASELINE;

	// translation of C and its sign to the right, back to the baseline
	int dx2_C = right( "ZERO" ) + MED_SKIP - left( "oppSignC" );

	// translation of C and its sign to the left, on the baseline
	int dx3_C = // - ((Text)ht.get("ZERO")).getWidth() - SMALL_SKIP;
	    - (width("ZERO") + MED_SKIP);
	int dx = 0;

	if (oppSignC.equals("-"))
	    ht.get( "oppSignC" ).translate( dx2_C + dx3_C, 0 );
	else
	    ht.get( "oppSignC" ).translate( dx2_C , 0 );

	ht.get( "C" ).translate( dx1_C + dx2_C + dx3_C, 0 );

	if (oppSignC.equals("-"))
	    addLine("div2", left("oppSignC"), BASELINE + (int) (0.1*fontsize),
		    right("C"),  BASELINE + (int) (0.1*fontsize), "red", true);
	else
	    addLine("div2", left("C"), BASELINE + (int) (0.1*fontsize),
		    right("C"),  BASELINE + (int) (0.1*fontsize), "red", true);

	addText("A3", A3, right("C") - fm.stringWidth( A3 ),
		BASELINE + (int) (1.05*fontsize),
		fontsize, fm, "red", true );

	addLine("div4", left("B"), BASELINE + (int) (0.1*fontsize),
		right("X1"),  BASELINE + (int) (0.1*fontsize),
		"red", true);

	// horiz. translation of A2, to the right
	int dx_A2 = left("B") + (width("div4") - width("A2"))/2 - left("A2");

	Rational r = new Rational( b, a);

	int dx_X1 = 0;

	// b over a
	if (r.equalsOne())
	{
	    BoverAisINT = true;

	    // no need to add a rational
	}
	else if (r.isInteger())
	{
	    BoverAisINT = true;
	    addText("BoverA_int", BoverA, right("signB")+ MED_SKIP, 
		    BASELINE, fontsize, fm, "red", true );
	}

	else  
	{
	    addRational("BoverA", Math.abs(r.getNum()),r.getDen(), 
			right("signB")+ MED_SKIP, 
			BASELINE+(int)(0.1*fontsize), fontsize, fm, 
			"red", true );
	    // translation of X1 to make room for rational
	    dx_X1 = right("BoverA") - left("X1") + MED_SKIP;
	    if (dx_X1>0)
		ht.get( "X1" ).translate( dx_X1, 0 );		
	}


	// -c over a
	r = new Rational( -c, a);

	int adjustment = SMALL_SKIP;
	if (-c*a>0)   // a and c have opposite signs
	    adjustment +=  width("oppSignC");
	else if (a<0) // a and b are both negative
	    adjustment += correction;

	if (r.isInteger())
	{
	    CoverAisINT = true;
	    addText("CoverA_int", CoverA, 
		    left("ZERO") + adjustment,
		    BASELINE, fontsize, fm, "red", true );
	}
	else
	    addRational("CoverA", r.getNum(),r.getDen(),
			left("ZERO") + adjustment,
			BASELINE+(int)(0.1*fontsize), fontsize, fm, 
			"red", true );

	// adding (b/(2a))^2
	r = new Rational( b*b, 4*a*a);


	// first add constant to the LHS
	addText("PLUS_LHS","+", right("X1") + MED_SKIP, BASELINE, 
		fontsize, fm,"red", true);

	int dx_EQ = 0; 	// move = and RHS if necessary
	if (r.isInteger())
	{
	    Bover2ASQisINT = true;
	    addText("Bover2ASQ_LHS_int", b*b/(4*a*a) + "", 
		    right("PLUS_LHS") + MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );

	    if (right("Bover2ASQ_LHS_int") + HUGE_SKIP > left("EQ"))
		dx_EQ = right("Bover2ASQ_LHS_int") + HUGE_SKIP - left("EQ");
	}
	else
	{
	    addRational("Bover2ASQ_LHS", r.getNum(),r.getDen(),
			right("PLUS_LHS") + MED_SKIP,
			BASELINE - (int)(0.3*fontsize), fontsize, fm, 
				"red", true );

	    if (right("Bover2ASQ_LHS") + HUGE_SKIP > left("EQ"))
		dx_EQ = right("Bover2ASQ_LHS") + HUGE_SKIP - left("EQ");
	}


	// move RHS if necessary
	ht.get("EQ").translate( dx_EQ, 0);
	if (CoverAisINT)
	    ht.get("CoverA_int").translate( dx_EQ, 0);
	else
	{
	    ht.get("CoverA").translate( dx_EQ, 0);
	}

	// then add constant to the RHS
	if (CoverAisINT)
	    addText("PLUS_RHS","+", right("CoverA_int") + MED_SKIP, BASELINE, 
		    fontsize, fm,"red", true);
	else
	    addText("PLUS_RHS","+", right("CoverA") + MED_SKIP, BASELINE, 
		    fontsize, fm,"red", true);

	if (Bover2ASQisINT)
	    addText("Bover2ASQ_RHS_int", b*b/(4*a*a) + "", 
		    right("PLUS_RHS") + MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );
	else
	    addRational("Bover2ASQ_RHS", r.getNum(),r.getDen(),
			right("PLUS_RHS") + MED_SKIP,
			BASELINE - (int)(0.3*fontsize), fontsize, fm, 
				"red", true );


	// factor the left side

	addText("LPAREN", LPAREN, right("SQ"), BASELINE, 
		fontsize, fm,"red", true);
	addText("X", X, right("LPAREN")+ MED_SKIP, 
		BASELINE, fontsize, fm,"red", true);
	if (a<0)
	    addText("signFactor", oppSignB, right("X")+ MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );
	else
	    addText("signFactor", signB, right("X")+ MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );


	// b/(2a) when factoring
	r = new Rational( Math.abs(b), Math.abs(2*a));

	if (r.isInteger())
	{
	    Bover2AisINT = true;
	    addText("Bover2A_int",Bover2A, 
		    right("signFactor") + MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );

	    addText("RPAREN", RPAREN, right("Bover2A_int")+ MED_SKIP, 
		    BASELINE, fontsize, fm,"red", true);
	}
	else
	{
	    addRational("Bover2A", r.getNum(),r.getDen(),
			right("signFactor") + MED_SKIP,
			BASELINE - (int)(0.3*fontsize), fontsize, fm, 
				"red", true );

	    addText("RPAREN", RPAREN, right("Bover2A")+ MED_SKIP, 
		    BASELINE, fontsize, fm,"red", true);
	}

	addText("SQ2", SQ2, right("RPAREN"), 
		BASELINE-(int)(raise*fontsize),
		supersize, fmsuper, "red", true );

	// simplify the right side
	r = (new Rational( -c, a)).add( new Rational(b*b,4*a*a) );
	if (r.isInteger())
	{
	    RHSisINT = true;
	    RHS_int = r.getNum();
	    addText("RHS_int", r.getNum() + "",
		    right("EQ") + HUGE_SKIP,
		    BASELINE, fontsize, fm, "red", true );
	}
	else
	{
	    addRational("RHS", r.getNum(),r.getDen(),
			right("EQ") + HUGE_SKIP,
			BASELINE - (int)(0.3*fontsize), fontsize, fm, 
				"red", true );

	}
	int cache = right("EQ") + HUGE_SKIP;

	// *******************************************************
	// take square root of each side
	// *******************************************************

	// nothing to add for LHS

	//int dx2_EQ = right("RPAREN") + MED_SKIP - left( "EQ" );
	//if (dx2_EQ<0)  // move = to the left
	//    ht.get("EQ").translate( dx2_EQ, 0);

	// square root of RHS
	int disc = b*b - 4 * a * c;
	boolean 
	    discIsSquare = false, 
	    RHSisSquare = false,
	    denomIsSquare = false;
	
	if (disc>0)
	{
	    SQRTdisc = (int) Math.sqrt( disc );
	    discIsSquare = (SQRTdisc * SQRTdisc == disc);
	}
	if (RHSisINT)
	{

	    int SQRT_RHS = (int) Math.sqrt( Math.abs( RHS_int) );
	    RHSisSquare = (SQRT_RHS * SQRT_RHS == RHS_int);
	}

	if (RHSisINT)
	{
	    if (RHS_int==0)
	    {
		// do nothing (no need to add +/- in front of 0)
	    }
	    else // RHS is an int but not 0
	    {
		// plus-minus sign
		addLine("PM1", cache, 
			BASELINE - (int) (0.1*fontsize),
			cache + (int)(0.6*fontsize),  
			BASELINE - (int) (0.1*fontsize),
			"red", true);	

		addLine("PM2", cache, 
			BASELINE - (int) (0.6*fontsize),
			cache + (int)(0.6*fontsize),  
			BASELINE - (int) (0.6*fontsize),
			"red", true);	

		addLine("PM3", cache + (int)(0.3*fontsize), 
			BASELINE - (int) (0.9*fontsize),
			cache + (int)(0.3*fontsize),  
			BASELINE - (int) (0.3*fontsize),
			"red", true);	
		
		if (RHSisSquare)
		{
		     addText("RHS2", ((int)Math.sqrt(RHS_int)) + "",
			     right("PM1") + (int)(0.2*fontsize),
			     BASELINE - (int)(0.0*fontsize), 
			     fontsize, fm, "red", true );
		}
		else // RHS is an int but not a perfect square
		{
		    int x[] = new int[4];
		    int y[] = new int[5];

		    addText("RHS2", Math.abs(RHS_int) + "",
			    right("PM1") + (int)(1.5*fontsize),
			    BASELINE - (int)(0.0*fontsize), 
			    fontsize, fm, "red", true );

		    x[0] = right("PM1") + (int)(0.6*fontsize);
		    y[0] = BASELINE - (int)(0.5*fontsize);
		    x[1] = x[0] + (int)(0.3*fontsize);
		    y[1] = BASELINE - (int)(0.0*fontsize);
		    x[2] = x[1] + (int) (0.6*fontsize);
		    y[2] = BASELINE - (int)(0.9*fontsize);
		    x[3] = right("RHS2");
		    y[3] = y[2];
		    addPolyline("SQRT_RHS", x,y, "red", true );
		}// RHS is an int but not a perfect square
	    } // RHS is an int but not 0
	}// RHS is an int
	else // RHS is a rational whose numerator is disc
	{
	    // plus-minus sign
	    addLine("PM1", cache, 
		    BASELINE - (int) (0.1*fontsize),
		    cache + (int)(0.6*fontsize),  
		    BASELINE - (int) (0.1*fontsize),
		    "red", true);	

	    addLine("PM2", cache, 
		    BASELINE - (int) (0.6*fontsize),
		    cache + (int)(0.6*fontsize),  
		    BASELINE - (int) (0.6*fontsize),
		    "red", true);	

	    addLine("PM3", cache + (int)(0.3*fontsize), 
		    BASELINE - (int) (0.9*fontsize),
		    cache + (int)(0.3*fontsize),  
		    BASELINE - (int) (0.3*fontsize),
		    "red", true);	
		
	    if (discIsSquare) // RHS is a rational whose num. is a square
	    {
		r = new Rational( SQRTdisc, Math.abs(2*a));
		// r must be a rational (cannot be an int, since otherwise
		// RHS would already be an int

		addRational("RHS2", r.getNum(),r.getDen(),
			    right("PM1") + (int)(0.2*fontsize),
			    BASELINE - (int)(0.3*fontsize), fontsize, fm, 
			    "red", true );		
	    }
	    else // RHS is a rational whose num. is NOT a perfect square
	    {
		r = new Rational( disc, 4*a*a);
		int sqrt = (int)Math.sqrt( r.getDen() );
		if ( sqrt*sqrt == r.getDen() )
		    denomIsSquare = true;

		if (denomIsSquare)
		{
		    addText("disc", r.getNum() + "",
			    right("PM1") + (int)(1.5*fontsize),
			    BASELINE - (int)(0.4*fontsize), 
			    fontsize, fm, "red", true );

		    int x[] = new int[4];
		    int y[] = new int[5];
		    x[0] = right("PM1") + (int)(0.6*fontsize);
		    y[0] = BASELINE - (int)(0.9*fontsize);
		    x[1] = x[0] + (int)(0.3*fontsize);
		    y[1] = BASELINE - (int)(0.4*fontsize);
		    x[2] = x[1] + (int) (0.6*fontsize);
		    y[2] = BASELINE - (int)(1.4*fontsize);
		    x[3] = right("disc");
		    y[3] = y[2];
		    addPolyline("SQRTdisc", x,y, "red", true );

		    addLine("div5", left("SQRTdisc"), 
			    BASELINE - (int) (0.3*fontsize),
			    right("SQRTdisc"), BASELINE - (int) (0.3*fontsize),
			    "red", true);

		    String tmp = ((int)Math.sqrt(r.getDen())) + "";
		    addText("2A", tmp ,
			    left("div5") + 
			    (width("div5") - fm.stringWidth(tmp))/2,
			    BASELINE + (int)(0.6*fontsize),
			fontsize, fm, "red", true);
		}// denominator is square
		else
		{
		    addRational("RHS2", r.getNum(),r.getDen(),
				right("PM1") + (int)(1.7*fontsize),
				BASELINE - (int)(0.3*fontsize), fontsize, fm, 
				"red", true );

		    int x[] = new int[4];
		    int y[] = new int[5];
		    x[0] = right("PM1") + (int)(0.6*fontsize);
		    y[0] = BASELINE - (int)(0.1*fontsize);
		    x[1] = x[0] + (int)(0.3*fontsize);
		    y[1] = BASELINE + (int)(0.6*fontsize);
		    x[2] = x[1] + (int) (0.6*fontsize);
		    y[2] = BASELINE - (int)(1.4*fontsize);
		    x[3] = right("RHS2");
		    y[3] = y[2];
		    addPolyline("SQRT_RHS", x,y, "red", true );
		}

	    }// RHS is a rational whose num. is NOT a perfect square
	}// RHS is a rational whose numerator is disc


	// move factor to the right side
	int dx1_factor = right("X") - left("signFactor") + HUGE_SKIP;
	int dx2_EQ = right("X") + HUGE_SKIP - left("EQ");

	ht.get("EQ").translate( dx2_EQ, 0);

	if (a<0)
	    addText("oppSignFactor", signB, 
		    left("EQ")+ (signB.equals("-") ? MED_SKIP : 0),
		    BASELINE+dy, fontsize, fm, "red", true );
	else
	    addText("oppSignFactor", oppSignB,
		    left("EQ")+ (oppSignB.equals("-") ? MED_SKIP : 0),
		    BASELINE+dy, fontsize, fm, "red", true );

	int dx2_factor = right("EQ") + HUGE_SKIP - left("oppSignFactor");
	
 	ht.get("oppSignFactor").translate( dx2_factor, -dy);

	if (Bover2AisINT)
	    ht.get("Bover2A_int").translate( dx1_factor + dx2_factor, 0);
	else
	    ht.get("Bover2A").translate( dx1_factor + dx2_factor, 0);
	
	// add a plus in case the RHS is positive
	
	/*
	if ( (RHSisINT) && (RHS_int==0) )
	    addText("extraPlus", "+", 
		    left("RHS_int") -  fm.stringWidth("+") - MED_SKIP,
		    BASELINE, fontsize, fm, "red", true );
	*/
	root.addContent( initial );

	/*******************************************
         *               second snapshot 
         *******************************************/

	animation = new Element("animation");

	Element par;
	Element[] pars;
	Element seq;

	if (c != 0)
	{
	    pars = new Element[ 25 ];
	    pars[0] = makePar1(makeChangeStyle( new String[]{"signC","C"}, 
						"red" ));
	    pars[1] = makePar(new Element[]{makeMove(new String[]{"signC"},
						     dx_signC,dy),
					    makeMove(new String[]{"C"},
						     dx1_C,dy) });
	    pars[2] = makePar1( makeWait(300) );
	    pars[3] = makePar1( makeShowHide("hide",new String[]{"signC"}) );
	    pars[4] = makePar1( makeWait(300) );
	    pars[5] = makePar1( makeShowHide("show",new String[]{"signC"}) );
	    pars[6] = makePar1( makeWait(300) );
	    pars[7] = makePar1( makeShowHide("hide",new String[]{"signC"}) );
	    pars[8] = makePar1( makeWait(300) );
	    pars[9] = makePar1( makeShowHide("show",new String[]{"signC"}) );
	    pars[10] = makePar1( makeWait(300) );
	    pars[11] = makePar1( makeShowHide("hide",new String[]{"signC"}) );
	    pars[12] = makePar1( makeWait(300) );
	    pars[13] = makePar1( makeShowHide("show",new String[]{"signC"}) );
	    pars[14] = makePar1( makeWait(300) );
	    pars[15] = makePar1( makeShowHide("hide",new String[]{"signC"}) );
	    pars[16] = makePar1( makeWait(300) );
	    pars[17] = makePar1( makeShowHide("show",new String[]{"oppSignC"}) );
	    pars[18] = makePar1( makeWait(500) );
	    pars[19] = makePar1( makeMove(new String[]{"oppSignC","C"},     
					  dx2_C, -dy));
	    pars[20] = makePar1( makeWait(200) );
	    pars[21] = makePar1( makeShowHide("hide",new String[]{"ZERO"}) );
	    if (oppSignC.equals("+"))
	    {
		pars[22] = makePar1( makeShowHide("hide",new String[]{"oppSignC"}) );
	    
		pars[23] = makePar1( makeMove(new String[]{"C"},dx3_C,0)); //***
		pars[24] = makePar1( makeChangeStyle( new String[]{"oppSignC","C"}, "black" ));
	    }
	    else
	    {
		// dummy operation 
		pars[22] = makeNoop();
		pars[23] = makePar1( makeMove(new String[]{"oppSignC","C"},
					      dx3_C,0));
		pars[24] = makePar1( makeChangeStyle( new String[]{"oppSignC","C"}, 
						      "black" ));
	    }
	    seq = makeSeq( pseudocodeURL1 + "0" + pseudocodeURL2, pars );
	    //seq = makeSeq( "Move c to the right side", pars );
	    animation.addContent( seq );
	}

	/*******************************************
         *               third snapshot 
         *******************************************/

	if (a==1)
	{
	    pars = new Element[ 1 ];
	    pars[0] = makePar1( makeWait(0) );
	}
	else
	{
	    pars = new Element[ 26 ];
	    pars[0] = makePar1( makeShowHide("show",new String[]{"div1"}) );
	    pars[1] = makePar1( makeShowHide("show",new String[]{"A2"}) );
	    pars[2] = makePar1( makeShowHide("show",new String[]{"div2"}) );
	    pars[3] = makePar1( makeShowHide("show",new String[]{"A3"}) );
	    pars[4] = makePar1( makeWait(1000) );
	    pars[5] = makePar1( makeChangeStyle( new String[]{"div2","A3"}, 
						 "black" ));
	    pars[6] = makePar1( makeChangeStyle( new String[]{"signA","A"}, 
						 "red" ));
	    pars[7] = makePar1( makeWait(1000) );
	    pars[8] = makePar1( makeShowHide("hide",new String[]{"signA", "A",
								 "div1"}) );
	    pars[9] = makePar1( makeShowHide("show",new String[]{"div4"}) );
	    pars[10] = makePar1( makeMove(new String[]{"A2"},dx_A2,0));
	    pars[11] = makePar1( makeChangeStyle( new String[]{"B"}, "red" ));
	    pars[12] = makePar1( makeWait(1000) );
	    if (dx_X1 > 0)
		pars[13] = makePar1( makeMove(new String[]{"X1"},dx_X1,0));
	    else
		pars[13] = makeNoop();
	    pars[14] = makePar1( makeShowHide("hide",new String[]{	
		"B", "div4", "A2"}) );

	    if (BoverAisINT)
		if (Math.abs(b/a) == 1)
		    pars[15] = makeNoop();
		else
		    pars[15] = makePar1( makeShowHide("show",new String[]{
			"BoverA_int" }) );
	    else
		pars[15] = makePar1( makeShowHide("show",new String[]{
		    "BoverA_num", "BoverA_div", "BoverA_den" }) );

	    if (a<0)
		pars[16] = makePar(new Element[]{
		    makeShowHide("hide",new String[]{"signB"}),
		    makeShowHide("show",new String[]{"oppSignB"}) });
	    else
		pars[16] = makeNoop();
	    pars[17] = makePar1( makeChangeStyle( new String[]{
		"BoverA_num", "BoverA_div", "BoverA_den" }, "black" ));
	    pars[18] = makePar1( makeChangeStyle( new String[]{
		"oppSignC", "C", "div2", "A3" }, "red" ));
	    pars[19] = makePar1( makeWait(1000) );
	    if (ht.get("oppSignC").isVisible())
		pars[20] = makePar1( makeShowHide("hide",new String[]{
		     "oppSignC", "C", "div2", "A3" }) );
	    else
		pars[20] = makePar1( makeShowHide("hide",new String[]{
		   "C", "div2", "A3" }) );
	    if (CoverAisINT)
		pars[21] = makePar1( makeShowHide("show",new String[]{
		"CoverA_int" }) );
	    else
		pars[21] = makePar1( makeShowHide("show",new String[]{
		    "CoverA_num", "CoverA_div", "CoverA_den" }) );
	    pars[22] = makePar1( makeWait(1000) );
	    pars[23] = makePar1( makeChangeStyle( new String[]{
		"BoverA_num", "BoverA_div", "BoverA_den" }, "red" ));
	    if (!BoverAisINT || !CoverAisINT)
		pars[24] = makePar1( makeMove(new String[]{	
		    "BoverA_num","BoverA_div","BoverA_den",
		    "CoverA_num","CoverA_div","CoverA_den"},
					      0,- (int)(0.4*fontsize)));
	    else
		pars[24] = makeNoop();

	    pars[25] = makePar1( makeChangeStyle( new String[]{
		"BoverA_num","BoverA_div","BoverA_den",
		"CoverA_num","CoverA_div","CoverA_den",
		"BoverA_int", "CoverA_int"}, "black" ));
	}

	seq = makeSeq( pseudocodeURL1 + "1" + pseudocodeURL2, pars );
	//seq = makeSeq( "Divide both sides by a and simplify", pars );

	animation.addContent( seq );

	/*******************************************
         *               fourth snapshot 
         *******************************************/

	pars = new Element[ 4 ];

	if (dx_EQ > 0) // if necessary, move RHS to make room for constant
	    if (CoverAisINT)
		pars[0] = makePar1( makeMove(new String[]{	
		    "EQ","CoverA_int"}, dx_EQ,0) );		
	    else
		pars[0] = makePar1( makeMove(new String[]{	
		    "EQ","CoverA_num","CoverA_div","CoverA_den" }, 
					     dx_EQ,0) );		
	else
	    pars[0] = makeNoop();

	
	if (Bover2ASQisINT)
	    pars[1] = makePar1( makeShowHide("show",new String[]{
		"PLUS_LHS","Bover2ASQ_LHS_int", "PLUS_RHS",
		"Bover2ASQ_RHS_int"}) );
	else
	    pars[1] = makePar1( makeShowHide("show",new String[]{
		"PLUS_LHS","Bover2ASQ_LHS_num","Bover2ASQ_LHS_div",
		"Bover2ASQ_LHS_den", "PLUS_RHS", "Bover2ASQ_RHS_num",
		"Bover2ASQ_RHS_div", "Bover2ASQ_RHS_den"}) );

	pars[2] = makePar1( makeWait(1000) );

	if (Bover2ASQisINT)
	    pars[3] = makePar1( makeChangeStyle( new String[]{
		"PLUS_LHS","Bover2ASQ_LHS_int", "PLUS_RHS",
		"Bover2ASQ_RHS_int"}, "black") );	
	else
	    pars[3] = makePar1( makeChangeStyle( new String[]{
		"PLUS_LHS","Bover2ASQ_LHS_num","Bover2ASQ_LHS_div",
		"Bover2ASQ_LHS_den", "PLUS_RHS", "Bover2ASQ_RHS_num",
		"Bover2ASQ_RHS_div", "Bover2ASQ_RHS_den"}, "black") );

	seq = makeSeq( pseudocodeURL1 + "2" + pseudocodeURL2, pars );
	//seq = makeSeq( "Add a well-chosen constant to both sides", pars );


	animation.addContent( seq );

	/*******************************************
         *               fifth snapshot 
         *******************************************/

	pars = new Element[ 13 ];

	if (a==1)
	    pars[0] = makePar1( makeChangeStyle(new String[]{
		"X2","SQ", "signB", "B", "X1", "PLUS_LHS"}, "red" ) );
	else if (a<0)
	    pars[0] = makePar1( makeChangeStyle(new String[]{
		"X2","SQ", "oppSignB", "X1", "PLUS_LHS"}, "red" ) );
	else
	    pars[0] = makePar1( makeChangeStyle(new String[]{
		"X2","SQ", "signB", "X1", "PLUS_LHS"}, "red" ) );
	    
	if (BoverAisINT)
	    pars[1] = makePar1( makeChangeStyle(new String[]{
		"BoverA_int"}, "red") );
	else
	    pars[1] = makePar1( makeChangeStyle(new String[]{
		"BoverA_num","BoverA_div", "BoverA_den"}, "red") );

	if (Bover2ASQisINT)
	    pars[2] = makePar1( makeChangeStyle(new String[]{
		"Bover2ASQ_LHS_int"}, "red") );
	else
	    pars[2] = makePar1( makeChangeStyle(new String[]{
		"Bover2ASQ_LHS_num","Bover2ASQ_LHS_div", "Bover2ASQ_LHS_den"},
						"red") );
	pars[3] = makePar1( makeWait(1000) );

	if (a==1)
	    pars[4] = makePar1( makeShowHide( "hide", new String[]{
		"X2","SQ", "signB", "B", "X1", "PLUS_LHS"}) );
	else if (a<0)
	    pars[4] = makePar1( makeShowHide( "hide", new String[]{
		"X2","SQ", "oppSignB", "X1", "PLUS_LHS"}) );
	else
	    pars[4] = makePar1( makeShowHide( "hide", new String[]{
		"X2","SQ", "signB", "X1", "PLUS_LHS"} ) );

	if (BoverAisINT)
	    if (Math.abs(b/a) != 1)
		pars[5] = makePar1( makeShowHide( "hide", new String[]{
		    "BoverA_int"} ) );
	    else 
		pars[5] = makeNoop();
	else
	    pars[5] = makePar1( makeShowHide( "hide", new String[]{
		"BoverA_num","BoverA_div", "BoverA_den"} ) );

	if (Bover2ASQisINT)
	    pars[6] = makePar1( makeShowHide( "hide", new String[]{
		"Bover2ASQ_LHS_int"} ) );
	else
	    pars[6] = makePar1( makeShowHide( "hide" ,new String[]{
		"Bover2ASQ_LHS_num","Bover2ASQ_LHS_div", "Bover2ASQ_LHS_den"} ) );
	pars[7] = makePar1( makeWait(300) );

	pars[8] = makePar1( makeShowHide( "show" ,new String[]{
	    "LPAREN", "X", "signFactor", "RPAREN", "SQ2"} ));

	if (Bover2AisINT)
	    pars[9] = makePar1( makeShowHide( "show" ,new String[]{
		"Bover2A_int"} ));
	else
	    pars[9] = makePar1( makeShowHide( "show" ,new String[]{
		"Bover2A_num", "Bover2A_div", "Bover2A_den"} ));

	pars[10] = makePar1( makeWait(1000) );

	pars[11] = makePar1( makeChangeStyle(new String[]{
	    "LPAREN", "X", "signFactor", "RPAREN", "SQ2"}, "black" ) );

	if (Bover2AisINT)
	    pars[12] = makePar1( makeChangeStyle( new String[]{
		"Bover2A_int"}, "black" ));
	else
	    pars[12] = makePar1( makeChangeStyle( new String[]{
		"Bover2A_num", "Bover2A_div", "Bover2A_den"}, "black" ));

	seq = makeSeq( pseudocodeURL1 + "3" + pseudocodeURL2, pars );
	//seq = makeSeq( "Factor the left side", pars );


	animation.addContent( seq );


	/*******************************************
         *               sixth snapshot 
         *******************************************/

	pars = new Element[ 9 ];

	if (a==1)
	    pars[0] = makePar1( makeChangeStyle(new String[]{
		"oppSignC", "C"}, "red") );
	else
	    if (CoverAisINT)
		pars[0] = makePar1( makeChangeStyle(new String[]{
		    "CoverA_int"}, "red") );
	    else
		pars[0] = makePar1( makeChangeStyle(new String[]{
		    "CoverA_num","CoverA_div", "CoverA_den"}, "red") );

	if (Bover2ASQisINT)
	    pars[1] = makePar1( makeChangeStyle(new String[]{
		"PLUS_RHS", "Bover2ASQ_RHS_int"}, "red") );
	else
	    pars[1] = makePar1( makeChangeStyle(new String[]{
		"PLUS_RHS", "Bover2ASQ_RHS_num",
		"Bover2ASQ_RHS_div", "Bover2ASQ_RHS_den"},
						"red") );
	pars[2] = makePar1( makeWait(1000) );

	if (a==1)
	    pars[3] = makePar1( makeShowHide( "hide", new String[]{
		"oppSignC", "C"} ) );
	else if (CoverAisINT)
	    pars[3] = makePar1( makeShowHide( "hide", new String[]{
		"CoverA_int"} ) );
	else
	    pars[3] = makePar1( makeShowHide( "hide", new String[]{
		"CoverA_num","CoverA_div", "CoverA_den"} ) );

	if (Bover2ASQisINT)
	    pars[4] = makePar1( makeShowHide( "hide", new String[]{
		"PLUS_RHS", "Bover2ASQ_RHS_int"} ) );
	else
	    pars[4] = makePar1( makeShowHide( "hide", new String[]{
		"PLUS_RHS", "Bover2ASQ_RHS_num",
		"Bover2ASQ_RHS_div", "Bover2ASQ_RHS_den"} ) );

	pars[5] = makePar1( makeWait(300) );

	if (RHSisINT)
	    pars[6] = makePar1( makeShowHide( "show", new String[]{
		"RHS_int"} ) );
	else
	    pars[6] = makePar1( makeShowHide( "show", new String[]{
		"RHS_num", "RHS_div", "RHS_den", } ) );

	pars[7] = makePar1( makeWait(1000) );

	if (RHSisINT)
	    pars[8] = makePar1( makeChangeStyle( new String[]{
		"RHS_int"}, "black" ) );
	else
	    pars[8] = makePar1( makeChangeStyle( new String[]{
		"RHS_num", "RHS_div", "RHS_den", }, "black" ) );

	seq = makeSeq( pseudocodeURL1 + "4" + pseudocodeURL2, pars );
	//seq = makeSeq( "Simplify the right side", pars );


	animation.addContent( seq );

	/*******************************************
         *               seventh snapshot 
         *******************************************/

	pars = new Element[ 11 ];

	pars[0] = makePar1( makeChangeStyle(new String[]{
	    "LPAREN", "RPAREN", "SQ2"}, "red") );

	pars[1] = makePar1( makeWait(1000) );

	pars[2] = makePar1( makeShowHide( "hide", new String[]{
	    	    "LPAREN", "RPAREN", "SQ2"} ) );

	pars[3] = makePar1( makeWait(1000) );

	if ((RHSisINT) && (RHS_int==0))
	    for(int i=4; i<pars.length; i++)
		pars[i] = makeNoop();
	else	
        {  // RHS is not 0

	    if (RHSisINT)
	    {				
		pars[4] = makePar1( makeChangeStyle(new String[]{
		    "RHS_int"}, "red" ) );
		pars[5] = makePar1( makeWait(1000) );
		pars[6] = makePar1( makeShowHide( "hide", new String[]{
		    "RHS_int"} ) );
	    }
	    else
	    {
		pars[4] = makePar1( makeChangeStyle(new String[]{
		    "RHS_num", "RHS_div", "RHS_den" }, "red" ) );
		pars[5] = makePar1( makeWait(1000) );
		pars[6] = makePar1( makeShowHide( "hide", new String[]{
		    "RHS_num", "RHS_div", "RHS_den" } ) );
	    }
	    pars[7] = makePar1( makeShowHide( "show", new String[]{
		"PM1", "PM2", "PM3"} ) );
	    
	    if (RHSisINT)
	    {
		if (RHSisSquare)
		{
		    pars[8] = makePar1( makeShowHide( "show", new String[]{
			"RHS2"} ) );
		    pars[9] = makePar1( makeWait(1000) );
		    
		    pars[10] = makePar1( makeChangeStyle(  new String[]{
			"PM1", "PM2", "PM3", "RHS2",}, "black" ) );
		}
		else // RHS is an int but not a perfect square
		{
		    pars[8] = makePar1( makeShowHide( "show", new String[]{
				"RHS2", "SQRT_RHS"} ) );
		    pars[9] = makePar1( makeWait(1000) );
		    
		    pars[10] = makePar1( makeChangeStyle(  new String[]{
				"PM1", "PM2", "PM3", "RHS2", "SQRT_RHS"}, "black" ) );
		} // RHS is an int but not a perfect square
	    }// RHS is an int
	    else
	    {     // RHS is a rational whose numerator is disc

		if (discIsSquare) // RHS is a rational whose num. is a square
		{
		    pars[8] = makePar1( makeShowHide( "show", new String[]{
			"RHS2_num", "RHS2_div", "RHS2_den"} ) );

		    pars[9] = makePar1( makeWait(1000) );
		
		    pars[10] = makePar1( makeChangeStyle(  new String[]{
			"PM1", "PM2", "PM3", 
			"RHS2_num", "RHS2_div", "RHS2_den"}, "black" ) );
		}
		else // RHS is a rational whose num. is NOT a square
		    if (denomIsSquare)
		    {		
			pars[8] = makePar1( makeShowHide( "show", new String[]{
			    "SQRTdisc", "disc", "div5", "2A"} ) );
		
			pars[9] = makePar1( makeWait(1000) );
		
			pars[10] = makePar1( makeChangeStyle(  new String[]{
			    "PM1", "PM2", "PM3", "disc", "SQRTdisc", "div5", 
			    "2A"}, "black" ) );
		    }// denom is square

		    else
		    {
			pars[8] = makePar1( makeShowHide( "show", new String[]{
			    "RHS2_num", "RHS2_div", "RHS2_den","SQRT_RHS"} ) );
		
			pars[9] = makePar1( makeWait(1000) );
		
			pars[10] = makePar1( makeChangeStyle(  new String[]{
			    "PM1", "PM2", "PM3", "RHS2_num", "RHS2_div",
			    "RHS2_den", "SQRT_RHS"} , "black" ) );
		    }
		
	    }// RHS is a rational whose numerator is disc
	}// RHS is not 0

	seq = makeSeq( pseudocodeURL1 + "5" + pseudocodeURL2, pars );
	//seq = makeSeq( "Take the square root of each side", pars );

	animation.addContent( seq );

	/*******************************************
         *               eighth snapshot 
         *******************************************/

	pars = new Element[ 25 ];

	if (Bover2AisINT)
	{
	    pars[0] = makePar1( makeChangeStyle(  new String[]{
		"signFactor", "Bover2A_int"}, "red" ) );

	    pars[1] = makePar( new Element[] {
		makeMove( new String[]{"signFactor", "Bover2A_int" }, 
			  dx1_factor, dy),
		makeMove( new String[]{"EQ" }, dx2_EQ, 0) }  );
	}
	else
	{
	    pars[0] = makePar1( makeChangeStyle(  new String[]{
		"signFactor", "Bover2A_num", "Bover2A_div",
		"Bover2A_den"}, "red" ) );

	    pars[1] = makePar( new Element[] {
		makeMove( new String[]{"signFactor", "Bover2A_num",
				       "Bover2A_div", "Bover2A_den"  }, 
			  dx1_factor, dy),
		makeMove( new String[]{"EQ" }, dx2_EQ, 0) }  );
	}
	
	pars[2] = makePar1( makeWait(300) );
	pars[3] = makePar1( makeShowHide("hide",new String[]{"signFactor"}) );
	pars[4] = makePar1( makeWait(300) );
	pars[5] = makePar1( makeShowHide("show",new String[]{"signFactor"}) );
	pars[6] = makePar1( makeWait(300) );
	pars[7] = makePar1( makeShowHide("hide",new String[]{"signFactor"}) );
	pars[8] = makePar1( makeWait(300) );
	pars[9] = makePar1( makeShowHide("show",new String[]{"signFactor"}) );
	pars[10] = makePar1( makeWait(300) );
	pars[11] = makePar1( makeShowHide("hide",new String[]{"signFactor"}) );
	pars[12] = makePar1( makeWait(300) );
	pars[13] = makePar1( makeShowHide("show",new String[]{"signFactor"}) );
	pars[14] = makePar1( makeWait(300) );
	pars[15] = makePar1( makeShowHide("hide",new String[]{"signFactor"}) );
	pars[16] = makePar1( makeWait(300) );
	pars[17] = makePar1( makeShowHide("show",new String[]{"signFactor"}) );
	pars[18] = makePar1( makeWait(300) );
	pars[19] = makePar1( makeShowHide("hide",new String[]{"signFactor"}) );
	pars[20] = makePar1( makeShowHide("show",
					  new String[]{"oppSignFactor"}) );

	if (Bover2AisINT)
	    pars[21] = makePar1( makeMove( new String[]{
		"oppSignFactor", "Bover2A_int" }, dx2_factor, -dy) );
	else
	    pars[21] = makePar1( makeMove( new String[]{
		"oppSignFactor", "Bover2A_num",
		"Bover2A_div", "Bover2A_den"  }, dx2_factor, -dy) );

	if ((-1.0)*b /(2*a) >0) // hide the plus sign in front of -b/2a
	    pars[22] = makePar1( makeShowHide( "hide", new String[]{
		"oppSignFactor" } )  );
	else // make the minus sign black for final display
	    pars[22] = makePar1( makeChangeStyle(  new String[]{
		"oppSignFactor" }, "black" )  );

	if (Bover2AisINT) // final display of -b/2a
	    pars[23] = makePar1( makeChangeStyle(  new String[]{
		"Bover2A_int"}, "black" ) );
	else
	    pars[23] = makePar1( makeChangeStyle(  new String[]{
		"Bover2A_num", "Bover2A_div", "Bover2A_den" }, "black" ) );

	// finalize the +/- sqrt(disc) / 2a
	if (RHS_int==0)   // simply hide the zero
	    pars[24] = makePar1( makeShowHide("hide",new String[]{
		"RHS_int"}) );
	else // move it next to the -b/2a
	{
	    int x = ( Bover2AisINT ? right("Bover2A_int") : right("Bover2A") );

	    int dx1 = x - left("PM1") + BIG_SKIP;

	    if (RHSisINT)    
		
		if (RHSisSquare)    
		    pars[24] = makePar1( makeMove( new String[]{
			"PM1", "PM2", "PM3", "RHS2" }, dx1, 0 ) );

		else // RHS is an int but not a perfect square
		    pars[24] = makePar1( makeMove( new String[]{ 
			"PM1", "PM2", "PM3", "RHS2", "SQRT_RHS"  },
						   dx1, 0) );
	    else // RHS is a fraction
		if (discIsSquare)
		    pars[24] = makePar1( makeMove( new String[]{
			"PM1", "PM2", "PM3", "RHS2_num",
			"RHS2_div", "RHS2_den"}, dx1, 0 ) );
		else if (denomIsSquare)
		    pars[24] = makePar1( makeMove( new String[]{
			"PM1", "PM2", "PM3", "disc", "SQRTdisc",
			"div5", "2A"}, dx1, 0 ) );
		else
		    pars[24] = makePar1( makeMove( new String[]{
			"PM1", "PM2", "PM3", "SQRT_RHS", "RHS2_num",
			"RHS2_div", "RHS2_den"}, dx1, 0 ) );
	}
	    
	seq = makeSeq( pseudocodeURL1 + "6" + pseudocodeURL2, pars );
	//seq = makeSeq( "Move the highlighted fraction to the right" +
	//	       " and finalize the answer", pars );

	animation.addContent( seq );

	root.addContent( animation );

	try {
	    XMLOutputter outputter = 
		new XMLOutputter(Format.getPrettyFormat());
	    FileWriter writer = new FileWriter(filename);
	    outputter.output(myDocument, writer);
	    writer.close(); 
	} catch (java.io.IOException e) {
	    e.printStackTrace();
	}


    	// initial.addContent( makeLine( "div1",true, 40, 305, 170,305,"red") );


	/*
	int[] x = {200,300,400,500};
	int[] y = {200,100,200,100};
	initial.addContent( makePolyline( "P",false, x,y,"blue") );
	*/



    }// createScript method

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

    // narrative is not displayed any longer

    private static Element makeSeq(String narrative, Element[] par)
    {
	Element seq = new Element( "seq" );
	seq.addContent( new Element("narrative").addContent(narrative));
	for(int p=0; p<par.length; p++)
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

     private static void assignValues(int[] values)
     {
	 Random rand = new Random();
	 int a = rand.nextInt( 9 ) + 1; // a cannot be zero
	 int b = rand.nextInt( 9 ) + 1; 
	 int c = rand.nextInt( 9 ) + 1;

	 if ( (b!=0) || (c!=0) )
	 {
	     if (b==0)  /* but c != 0 */
	     {
		 int gcd = gcd(a,c);
		 if (gcd>1) { 
		     a /= gcd; 
		     c /= gcd; 
		 }
	     } else if (c==0)  /* but b != 0 */
	     {
		 int gcd = gcd(a,b);
		 if (gcd>1) { 
		     a /= gcd; 
		     b /= gcd; 
		 }
	     }
	     else /* b != 0 and c != 0 */
	     {
		 int gcd1 = gcd( a, b );
		 int gcd2 = gcd( b, c );
		 if ( (gcd1>1) && (gcd2>1) )
		 {
		     int gcd3 = gcd( gcd1, gcd2 );
		     if (gcd3>1)
		     {
			 a /= gcd3;
			 b /= gcd3;
			 c /= gcd3;
		     }
		 }
		 if ((b != 0) && (rand.nextBoolean())) b *= -1;
		 if ((c != 0) && (rand.nextBoolean())) c *= -1;
	     }
	 }
	 else { /* b == c == 0 */ }

	 if (rand.nextBoolean()) a *= -1;
	 
	 values[0] = a;
	 values[1] = b;
	 values[2] = c;
     }// assignValues method
}
