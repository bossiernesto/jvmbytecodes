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

package exe.slopeIntercept;

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
import java.util.Arrays;
import java.util.Iterator;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Point;

import javax.swing.JPanel;

public class slopeIntercept
{

    static abstract class S{}
    static class TS extends S { String t; TS(String t) { this.t=t; } }
    static class FS extends S { String t; FS(String t) { this.t=t; } }


    public static String  ftName = "Lucida Bright"; 
    public static int num_snaps;
    private static Random rand = new Random();

    private static int SMALL_SKIP = 2;
    private static int MED_SKIP = 6;
    private static int BIG_SKIP = 10;
    private static int HUGE_SKIP = 20;
    private static int BASELINE = 300;
    private static int LEFT= -20;
    private static int MIDDLE= 250;
    private static int LEFT_EQ1= -20;
    private static int LEFT_EQ2= 250;

    private  static String pseudocodeURL1;
    private  static String pseudocodeURL2;

    private static java.util.Hashtable<String,Visual> ht;
    private static Element initial;
    private static Element animation;

    private static Rational m, b;
    private static boolean m_is_int;
    private static String mstr;           // String value of m
    private static String negmstr; // String value of -m
    private static String invmstr; // string value of 1/m
    private static Collection questions1 = new ArrayList<Q>(100);
    private static int x1, y1, x2, y2;
    private static ArrayList<Point> onLine, offLine;

    public static void main(String[] args)
    {
	int[] values = new int[4];
	String filename = args[0];

	//	for(int test=0; test<10000; test++)
	//{
	    if (args.length == 5)
	    {
		for(int i=0; i<4; i++)
		    values[i] = Integer.parseInt( args[i+1] );
	    }
	    else
		assignValues(values);
	    
	    x1 = values[0];
	    y1 = values[1];
	    x2 = values[2];
	    y2 = values[3];
	    
	    num_snaps = 5;
	    //System.out.println( x1 + " " +  y1 + " " +  x2 + " " +  y2 );
	

	    createScript(filename);
	    //}
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

    private static void createScript(String filename)
    {
	pseudocodeURL1 =  "slopeIntercept.php?;amp;line=";
	//pseudocodeURL2 = 
	//    ";amp;aval=" + a + ";amp;bval=" + b + 
	//    ";amp;cval=" + c;


	ht = new java.util.Hashtable<String,Visual>( 50 );
	initial = new Element( "initial");

	String X1 = x1 + ""; 
	String Y1 = y1 + ""; 
	String X2 = x2 + ""; 
	String Y2 = y2 + ""; 

	double lower = 0.2; // fraction of fontsize (for subscript)

	//System.out.println( "\t"+ a + "\t" + b + "\t" + c );

	int fontsize = 20;   // everything but superscript
	int subfontsize = 15;  // subscript


	Graphics g= (new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)).getGraphics();
	g.setFont( new Font(ftName,Font.PLAIN,fontsize) );
	FontMetrics fm = g.getFontMetrics();
	g.setFont( new Font(ftName,Font.PLAIN,subfontsize) );
	FontMetrics fmsub = g.getFontMetrics();
	//g.setFont( new Font("Symbol",Font.BOLD,fontsize) );
	//FontMetrics fmgreek = g.getFontMetrics();

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

	int yline1 = fontsize;
	int yline2 = yline1 + 3 * fontsize;
	int yline2a = yline2 - (int)(0.7*fontsize);
	int yline2b = yline2 + (int)(0.6*fontsize);

	int yline3 = yline1 + 6 * fontsize;
	int yline4 = yline1 + (int)(7.3 * fontsize);
	int yline5 = yline1 + 10 * fontsize;
	int yline6 = yline1 + 12 * fontsize;
	int yline7 = yline1 + 16 * fontsize;

	addText( "LPAREN1", "(x", LEFT, yline1, fontsize, fm, "black", false);
	addText( "ONEx", "1", 
		 right("LPAREN1"), yline1+(int)(lower*fontsize), 
		 subfontsize, fmsub, "black", false);
	addText( "C1", ", y", right("ONEx"), yline1, 
		 fontsize, fm, "black", false);
	addText( "ONEy", "1", 
		 right("C1"), yline1+(int)(lower*fontsize), 
		 subfontsize, fmsub, "black", false);
	addText( "RPAREN1", ") = (", right("ONEy"), yline1, 
		 fontsize, fm, "black", false);
	addText( "X1", X1, right("RPAREN1"), yline1, 
		 fontsize, fm, "black", false);
	addText( "C2", ", ", right("X1"), yline1, 
		 fontsize, fm, "black", false);
	addText( "Y1", Y1, right("C2"), yline1, 
		 fontsize, fm, "black", false);

	addText( "LPAREN2", ")     (x", right("Y1"), yline1, 
		 fontsize, fm, "black", false);
	addText( "TWOx", "2", 
		 right("LPAREN2"), yline1+(int)(lower*fontsize), 
		 subfontsize, fmsub, "black", false); 
	addText( "C3", ", y", right("TWOx"), yline1, 
		 fontsize, fm, "black", false);
	addText( "TWOy", "2", 
		 right("C3"), yline1+(int)(lower*fontsize), 
		 subfontsize, fmsub, "black", false);
	addText( "RPAREN2", ") = (", right("TWOy"), yline1, 
		 fontsize, fm, "black", false);
	addText( "X2", X2, right("RPAREN2"), yline1, 
		 fontsize, fm, "black", false);
	addText( "C4", ", ", right("X2"), yline1, 
		 fontsize, fm, "black", false);
	addText( "Y2", Y2, right("C4"), yline1, 
		 fontsize, fm, "black", false);
	addText( "RPAREN3", ")", right("Y2"), yline1, 
		 fontsize, fm, "black", false);

	// Elements for second snapshot
	addText( "m", "m", LEFT+50, yline2, fontsize, fm, "red", true, null,
		 true, false);

	addText( "EQ1", "=", right("m")+MED_SKIP, yline2, fontsize, fm, 
		 "red", true);
	int x[] = new int[4];
	int y[] = new int[4];
	x[0] = right("EQ1") + MED_SKIP;
	y[0] = yline2a;
	x[1] = x[0] + (int)(0.7*fontsize);
	y[1] = y[0];
	x[2] = x[0] + (x[1]-x[0])/2;
	y[2] = y[0] - (int)(0.7*fontsize);
	x[3] = x[0];
	y[3] = y[0];
	addPolyline( "deltay", x,y, "red", true);
	addText("y","y", x[1]+SMALL_SKIP,y[0], fontsize, fm, "red", true);
	addLine("div1", x[0], yline2a+(int)(0.4*fontsize),right("y"), 
		yline2a+(int)(0.4*fontsize), "red", true);

	y[0] = yline2b;
	y[1] = y[0];
	y[2] = y[0] - (int)(0.7*fontsize);
	y[3] = y[0];
	addPolyline( "deltax", x,y, "red", true);
	addText("x","x", x[1]+SMALL_SKIP,y[0], fontsize, fm, "red", true);

	addText( "EQ2", "=", right("div1")+MED_SKIP, yline2, fontsize, fm, 
		 "red", true);
	addText( "ynum2", "y", 
		 right("EQ2")+MED_SKIP, yline2a, 
		 fontsize, fm, "red", true);
	addText( "ysub2", "2", 
		 right("ynum2"), yline2a+(int)(lower*fontsize), 
		 subfontsize, fmsub, "red", true);
	addText( "minusnum", "-", right("ysub2")+MED_SKIP, yline2a, 
		 fontsize, fm, "red", true);
	addText( "ynum1", "y", 
		 right("minusnum")+MED_SKIP, yline2a, 
		 fontsize, fm, "red", true);
	addText( "ysub1", "1", 
		 right("ynum1"), yline2a+(int)(lower*fontsize), 
		 subfontsize, fmsub, "red", true);
	addLine("div2", left("ynum2"), yline2a+(int)(0.4*fontsize),
		right("ysub1"), 
		yline2a+(int)(0.4*fontsize), "red", true);
	addText( "xnum2", "x", 
		 right("EQ2")+MED_SKIP, yline2b, 
		 fontsize, fm, "red", true);
	addText( "xsub2", "2", 
		 right("xnum2"), yline2b+(int)(lower*fontsize), 
		 subfontsize, fmsub, "red", true);
	addText( "minusden", "-", right("xsub2")+MED_SKIP, yline2b, 
		 fontsize, fm, "red", true);
	addText( "xnum1", "x", 
		 right("minusden")+MED_SKIP, yline2b, 
		 fontsize, fm, "red", true);
	addText( "xsub1", "1", 
		 right("xnum1"), yline2b+(int)(lower*fontsize), 
		 subfontsize, fmsub, "red", true);

	addText( "EQ3", "=", right("div2")+MED_SKIP, yline2, fontsize, fm, 
		 "red", true);

	addText( "Y2copy", Y2, right("C4"), yline1, 
		 fontsize, fm, "red", true);
	addText( "Y1copy", Y1, right("C2"), yline1, 
		 fontsize, fm, "red", true);
	addText( "X2copy", X2, right("RPAREN2"), yline1, 
		 fontsize, fm, "red", true);
	addText( "X1copy", X1, right("RPAREN1"), yline1, 
		 fontsize, fm, "red", true);

	int num_width = (int)fm.getStringBounds(y2+"-"+y1, g).getWidth()+2*MED_SKIP;
	if (y1<0) num_width += fm.stringWidth("()");
	int den_width = fm.stringWidth(x2+"-"+x1)+2*MED_SKIP;
	if (x1<0) den_width += fm.stringWidth("()");
	int width = Math.max( num_width, den_width);
	int left_fraction = right("EQ3") + MED_SKIP;
	int left_num = left_fraction;
	int left_den = left_fraction;
	if (num_width < width) left_num += (width-num_width)/2;
	if (den_width < width) left_den += (width-den_width)/2;

	int dy_num = yline2a - yline1 + (int)(0.2*fontsize); // vertical shift to numerator
	int dy_den = yline2b - yline1; // vertical shift to denominator
	int dx_y2 =  left_num - left("Y2copy");
	
	ht.get("Y2copy").translate(dx_y2, dy_num);

	addText( "minusnum2", "-", right("Y2copy") + MED_SKIP, 
		 yline2a + (int)(0.2*fontsize), 
		 fontsize, fm, "red", true);

	int dx_y1 = right("minusnum2") + MED_SKIP - left( "Y1copy" );;

	int right_div = left_fraction + width;
	if (y1<0)
	{
	    addText( "(num", "(", right("minusnum2") + MED_SKIP, 
		     yline2a + (int)(0.2*fontsize),
		     fontsize, fm, "red", true);
	    dx_y1 += width("(num");
	    addText( ")num", ")", right("(num") + width("Y1copy"), 
		     yline2a + (int)(0.2*fontsize), 
		     fontsize, fm, "red", true);
	    ht.get("Y1copy").translate(dx_y1, dy_num);
	}
	else
	{
	    ht.get("Y1copy").translate(dx_y1, dy_num);
	}


	int dx_x2 = left_den - left("X2copy");
	ht.get("X2copy").translate(dx_x2, dy_den);

	addText( "minusden2", "-", right("X2copy") + MED_SKIP, yline2b, 
		 fontsize, fm, "red", true);

	int dx_x1 = right("minusden2") + MED_SKIP - left( "X1copy" );

	if (x1<0)
	{
	    addText( "(den", "(", right("minusden2") + MED_SKIP, yline2b, 
		     fontsize, fm, "red", true);
	    dx_x1 += width("(den");
	    addText( ")den", ")", right("(den") + width("X1copy"), yline2b, 
		     fontsize, fm, "red", true);
	    ht.get("X1copy").translate(dx_x1, dy_den);
	}
	else
	{
	    ht.get("X1copy").translate(dx_x1, dy_den);
	}



	addLine("div3", left_fraction, yline2a+(int)(0.4*fontsize),
		right_div, 
		yline2a+(int)(0.4*fontsize), "red", true);

	addText( "EQ4", "=", right("div3")+MED_SKIP, yline2, fontsize, fm, 
		 "red", true);

	m = new Rational( y2-y1, x2-x1);
	m_is_int = false;
	int mwidth = 0;
	if (m.isInteger())
	{
	    m_is_int = true;
	    addText("mval_int", m.getNum()+"", right("EQ4")+MED_SKIP,
		    yline2, fontsize, fm, "red", true );
	    addText("mval_copy1_int", m.getNum()+"", right("EQ4")+MED_SKIP,
		    yline2, fontsize, fm, "red", true );
	    addText("mval_copy2_int", m.getNum()+"", right("EQ4")+MED_SKIP,
		    yline2, fontsize, fm, "red", true );
	    //    addText("mval_copy3_int", m.getNum()+"", right("EQ4")+MED_SKIP,
	    //	    yline2, fontsize, fm, "red", true );
	    mwidth = width("mval_int");
	}
	else
	{
	    addRational("mval", m.getNum(), m.getDen(), 
			right("EQ4")+MED_SKIP,
			yline2-(int)(0.3*fontsize), fontsize, fm, "red", true );
	    addRational("mval_copy1", m.getNum(), m.getDen(), 
			right("EQ4")+MED_SKIP,
			yline2-(int)(0.3*fontsize), fontsize, fm, "red", true );
	    addRational("mval_copy2", m.getNum(), m.getDen(), 
			right("EQ4")+MED_SKIP,
			yline2-(int)(0.3*fontsize), fontsize, fm, "red", true );
	    //addRational("mval_copy3", m.getNum(), m.getDen(), 
	    //		right("EQ4")+MED_SKIP,
	    //		yline2-(int)(0.3*fontsize), fontsize, fm, "red", true );
	    mwidth = width("mval");
	}

	// Elements for third snapshot

	/*****************
	 *  equation #1
	 ****************/
	
	addText( "Y2copy2", Y2, right("C4"), yline1, 
		 fontsize, fm, "red", true);
	addText( "Y1copy2", Y1, right("C2"), yline1, 
		 fontsize, fm, "red", true);
	addText( "X2copy2", X2, right("RPAREN2"), yline1, 
		 fontsize, fm, "red", true);
	addText( "X1copy2", X1, right("RPAREN1"), yline1, 
		 fontsize, fm, "red", true);

	addText("header1a", "If you choose to plug in",
		LEFT,yline3, fontsize, fm, "red", true);

	addText("header1b","the first point:",
		LEFT,yline4, fontsize, fm, "red", true);

	int tmp = LEFT_EQ1 + Math.max( width("Y1copy2") - fm.stringWidth( "y" ),
				       0);
	addText( "yEQ1", "y", tmp, yline6, fontsize, fm, "red", true);

	tmp = right("yEQ1") + MED_SKIP;
	addText( "=EQ1", "=", tmp, yline6, fontsize, fm, "black", true);

	tmp = right("=EQ1") + MED_SKIP +  
	    Math.max( mwidth -  fm.stringWidth( "m" ), 0);

	addText( "mEQ1", "m", tmp, yline6, fontsize, fm, "black", true,
		 null, true, false);

	addText( "xEQ1", "x", right("mEQ1")+SMALL_SKIP, 
		 yline6, fontsize, fm, "black", true);

	addText( "+EQ1", "+", left("xEQ1") + 
		 width("X1copy2") + fm.stringWidth("()") + MED_SKIP, 
		 yline6, fontsize, fm, "black", true);

	addText( "bEQ1", "b", right("+EQ1")+MED_SKIP, 
		 yline6, fontsize, fm, "black", true,
		 null, true, false);
	
	int dy_EQ = yline6 - yline1;
	int dy_EQm = yline6 - yline2;
	int dx_y1copy2 = right("yEQ1") - width("Y1copy2") - left("Y1copy2");
	ht.get("Y1copy2").translate(dx_y1copy2, dy_EQ);

	int dx_m1 = 0;
	if (m.isInteger())
	    dx_m1 = right("mEQ1") - width("mval_int") - left("mval_copy1_int");
	else
	    dx_m1 = right("mEQ1") - width("mval_copy1") - left("mval_copy1");

	addText( "(x1", "(", right("mEQ1")+SMALL_SKIP, 
		 yline6, fontsize, fm, "red", true);
	addText( ")x1", ")", right("(x1")+width("X1copy2"), 
		 yline6, fontsize, fm, "red", true);

	int dx_x1copy2 = right("(x1") - left("X1copy2");

	Rational s1 = m.mult( new Rational(x1) );
	int s1width = 0;
	if (s1.isInteger())
	{
	    s1width = fm.stringWidth(s1.getNum()+"");
	    addText("s1", s1.getNum()+"", left("+EQ1")-MED_SKIP-s1width,
		    yline6, fontsize, fm, "red", true );
	}
	else
	{
	    s1width = Math.max(fm.stringWidth(s1.getNum()+""),
			       fm.stringWidth(s1.getDen()+"") );
	    addRational("s1", s1.getNum(), s1.getDen(), 
			left("+EQ1")-MED_SKIP-s1width,
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );
	}


	int mid1, dx_s1a, dx_equal1a, dy_s1, dx_s1b, dx_equal1b;

	if (s1.equalsZero())
	{
	    mid1 = (left("+EQ1") + right("=EQ1") ) /2;
	    dx_equal1a = mid1 - left("=EQ1");
	    ht.get("=EQ1").translate(dx_equal1a, 0);
	    dx_s1a = dy_s1 = dx_s1b =  dx_equal1b = 0; // unused
	}
	else
	{
	    mid1 = Math.min( left("s1")-width("=EQ1")-SMALL_SKIP,
			     (left("+EQ1") + right("=EQ1") ) /2);
	    
	    addText("minusEQ1", "-", mid1,yline6-2*fontsize, 
		    fontsize, fm, "red", true );
	    
	    dx_s1a = mid1+width("=EQ1") - left("s1");
	    dx_equal1a = mid1 - left("=EQ1");
	    dy_s1 = -2*fontsize;
	    // e.g., -5 moves to the RHS and becomes -(-5)
	    if ((s1.isInteger()) && (s1.isNegative()) )
	    {
		dx_s1a = fm.stringWidth("(");
		addText("(s1", "(", mid1+width("=EQ1"),yline6-2*fontsize, 
			fontsize, fm, "red", true );
		addText(")s1", ")", right("(s1")+width("s1"),yline6-2*fontsize, 
			fontsize, fm, "red", true );
	    }
	    
	    ht.get("s1").translate(dx_s1a, dy_s1);
	    ht.get("=EQ1").translate(dx_equal1a, 0);
	    
	    
	    dx_s1b = right("Y1copy2")+MED_SKIP - left("minusEQ1");
	    dx_equal1b = left("+EQ1") - left("=EQ1");
	    ht.get("s1").translate(dx_s1b, -dy_s1);
	    ht.get("minusEQ1").translate(dx_s1b, -dy_s1);
	    ht.get("=EQ1").translate(dx_equal1b, 0);
	    if ((s1.isInteger()) && (s1.isNegative()) )
	    {
		ht.get("(s1").translate(dx_s1b, -dy_s1);
		ht.get(")s1").translate(dx_s1b, -dy_s1);
	    }
	}// s1 is not zero

	Rational b = (new Rational(y1)).sub(s1);
	int bwidth;

	int finalWidth = 0;
	if (b.isInteger())
	{
	    bwidth = fm.stringWidth(b.getNum()+"");
	    if (s1.equalsZero())
		addText("bval1", b.getNum()+"", left("Y1copy2"),
			yline6, fontsize, fm, "red", true );
	    else
		addText("bval1", b.getNum()+"", left("=EQ1")-MED_SKIP-bwidth,
			yline6, fontsize, fm, "red", true );

	    if (b.equalsZero())
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y = 0");
		else
		    finalWidth = fm.stringWidth("y =x") + MED_SKIP + mwidth;
	    else if ((b.isInteger()) && (b.isNegative()) )
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y =") + MED_SKIP + bwidth;
		else
		    finalWidth = fm.stringWidth("y =") + mwidth + 
			fm.stringWidth("x") + bwidth + 2*MED_SKIP;
	    else
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y =") + MED_SKIP + bwidth;
		else
		    finalWidth = fm.stringWidth("y =") + mwidth + 
			fm.stringWidth("x") + fm.stringWidth("+") + 
			bwidth + 2*MED_SKIP;

	    int[] boxx = new int[5];
	    int[] boxy = new int[5];
	    boxx[0] =  MIDDLE-20-(finalWidth/2) - 20;
	    boxy[0] = yline7 - (int)(1.7*fontsize);
	    boxx[1] = boxx[0] + finalWidth + 45;
	    boxy[1] = boxy[0];
	    boxx[2] = boxx[1];
	    boxy[2] = yline7 + (int)(1.2*fontsize);
	    boxx[3] = boxx[0];
	    boxy[3] = boxy[2];
	    boxx[4] = boxx[0];
	    boxy[4] = boxy[0];
	    addPolyline( "frame", boxx,boxy, "black", true, 
			 "200", "200", "200");

	}
	else
	{
	    bwidth = Math.max(fm.stringWidth(b.getNum()+""),
			       fm.stringWidth(b.getDen()+"") );

	    if (b.equalsZero())
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y = 0");
		else
		    finalWidth = fm.stringWidth("y =x") + MED_SKIP + mwidth;
	    else if ((b.isInteger()) && (b.isNegative()) )
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y =") + MED_SKIP + bwidth;
		else
		    finalWidth = fm.stringWidth("y =") + mwidth + 
			fm.stringWidth("x") + bwidth + 2*MED_SKIP;
	    else
		if (m.equalsZero())
		    finalWidth = fm.stringWidth("y =") + MED_SKIP + bwidth;
		else
		    finalWidth = fm.stringWidth("y =") + mwidth + 
			fm.stringWidth("x") + fm.stringWidth("+") + 
			bwidth + 2*MED_SKIP;

	    int[] boxx = new int[5];
	    int[] boxy = new int[5];
	    boxx[0] =  MIDDLE-20-(finalWidth/2) - 20;
	    boxy[0] = yline7 - (int)(1.7*fontsize);
	    boxx[1] = boxx[0] + finalWidth + 45;
	    boxy[1] = boxy[0];
	    boxx[2] = boxx[1];
	    boxy[2] = yline7 + (int)(1.2*fontsize);
	    boxx[3] = boxx[0];
	    boxy[3] = boxy[2];
	    boxx[4] = boxx[0];
	    boxy[4] = boxy[0];
	    addPolyline( "frame", boxx,boxy, "black", true, 
			 "200", "200", "200");

	    addRational("bval1", b.getNum(), b.getDen(), 
			left("=EQ1")-MED_SKIP-bwidth,
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );
	}
	
	if (m.isInteger())
	    addText("mval_copy3", m.getNum()+"", right("EQ4")+MED_SKIP,
		    yline2, fontsize, fm, "red", true );
	else
	    addRational("mval_copy3", m.getNum(), m.getDen(), 
			right("EQ4")+MED_SKIP,
			yline2-(int)(0.3*fontsize), fontsize, fm, "red", true );

	// swap of b and its value around the equal sign
	int dy_swap = 2*fontsize; // common to both stages

	// first stage
	int dx_bEQ1a = left("=EQ1") - left("bEQ1");
	ht.get("bEQ1").translate(dx_bEQ1a, dy_swap);
	int dx_bval1a = left("=EQ1") - left("bval1");
	ht.get("bval1").translate(dx_bval1a, -dy_swap);

	//second stage
	int dx_bEQ1b = left("=EQ1") - MED_SKIP - width("bEQ1") - left("bEQ1");
	ht.get("bEQ1").translate(dx_bEQ1b, -dy_swap);
	int dx_bval1b = right("=EQ1") + MED_SKIP - left("bval1");
	ht.get("bval1").translate(dx_bval1b, dy_swap);


	if (b.isInteger())
	    addText("bval1_copy", b.getNum()+"", left("bval1"),
		    yline6, fontsize, fm, "red", true );
	else
	    addRational("bval1_copy", b.getNum(), b.getDen(), left("bval1"),
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );

	/*****************
	 *  equation #2
	 ****************/
	
	addLine("middle",MIDDLE-20, yline3-fontsize, MIDDLE-20,yline6,
		"black", true);

	addText("header2a", "If you choose to plug in",
		MIDDLE,yline3, fontsize, fm, "red", true);

	addText("header2b","the second point:",
		MIDDLE,yline4, fontsize, fm, "red", true);


	tmp = LEFT_EQ2 + Math.max( width("Y2copy2") - fm.stringWidth( "y" ),
				   0);
	addText( "yEQ2", "y", tmp, yline6, fontsize, fm, "red", true);

	tmp = right("yEQ2") + MED_SKIP;
	addText( "=EQ2", "=", tmp, yline6, fontsize, fm, "black", true);

	tmp = right("=EQ2") + MED_SKIP +  
	    Math.max( mwidth -  fm.stringWidth( "m" ), 0);

	addText( "mEQ2", "m", tmp, yline6, fontsize, fm, "black", true,
		 null, true, false);

	addText( "xEQ2", "x", right("mEQ2")+SMALL_SKIP, 
		 yline6, fontsize, fm, "black", true);

	addText( "+EQ2", "+", left("xEQ2") + 
		 width("X2copy2") + fm.stringWidth("()") + MED_SKIP, 
		 yline6, fontsize, fm, "black", true);

	addText( "bEQ2", "b", right("+EQ2")+MED_SKIP, 
		 yline6, fontsize, fm, "black", true,
		 null, true, false);
	
	//int dy_EQ = yline6 - yline1;
	//int dy_EQm = yline6 - yline2;
	int dx_y2copy2 = right("yEQ2") - width("Y2copy2") - left("Y2copy2");
	ht.get("Y2copy2").translate(dx_y2copy2, dy_EQ);

	int dx_m2 = 0;
	if (m.isInteger())
	    dx_m2 = right("mEQ2") - width("mval_int") - left("mval_copy2_int");
	else
	    dx_m2 = right("mEQ2") - width("mval_copy2") - left("mval_copy2");

	addText( "(x2", "(", right("mEQ2")+SMALL_SKIP, 
		 yline6, fontsize, fm, "red", true);
	addText( ")x2", ")", right("(x2")+width("X2copy2"), 
		 yline6, fontsize, fm, "red", true);

	int dx_x2copy2 = right("(x2") - left("X2copy2");

	Rational s2 = m.mult( new Rational(x2) );
	int s2width = 0;
	if (s2.isInteger())
	{
	    s2width = fm.stringWidth(s2.getNum()+"");
	    addText("s2", s2.getNum()+"", left("+EQ2")-MED_SKIP-s2width,
		    yline6, fontsize, fm, "red", true );
	}
	else
	{
	    s2width = Math.max(fm.stringWidth(s2.getNum()+""),
			       fm.stringWidth(s2.getDen()+"") );
	    addRational("s2", s2.getNum(), s2.getDen(), 
			left("+EQ2")-MED_SKIP-s2width,
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );
	}


	int mid2, dx_s2a, dx_equal2a, dy_s2, dx_s2b, dx_equal2b;

	if (s2.equalsZero())
	{
	    mid2 = (left("+EQ2") + right("=EQ2") ) /2;
	    dx_equal2a = mid2 - left("=EQ2");
	    ht.get("=EQ2").translate(dx_equal2a, 0);
	    dx_s2a = dy_s2 = dx_s2b =  dx_equal2b = 0; // unused
	}
	else
	{
	    mid2 = Math.min( left("s2")-width("=EQ2")-SMALL_SKIP,
			     (left("+EQ2") + right("=EQ2") ) /2);
	    
	    addText("minusEQ2", "-", mid2,yline6-2*fontsize, 
		    fontsize, fm, "red", true );
	    
	    dx_s2a = mid2+width("=EQ2") - left("s2");
	    dx_equal2a = mid2 - left("=EQ2");
	    dy_s2 = -2*fontsize;
	    // e.g., -5 moves to the RHS and becomes -(-5)
	    if ((s2.isInteger()) && (s2.isNegative()) )
	    {
		dx_s2a = fm.stringWidth("(");
		addText("(s2", "(", mid2+width("=EQ2"),yline6-2*fontsize, 
			fontsize, fm, "red", true );
		addText(")s2", ")", right("(s2")+width("s2"),yline6-2*fontsize, 
			fontsize, fm, "red", true );
	    }
	    
	    ht.get("s2").translate(dx_s2a, dy_s2);
	    ht.get("=EQ2").translate(dx_equal2a, 0);
	    
	    
	    dx_s2b = right("Y2copy2")+MED_SKIP - left("minusEQ2");
	    dx_equal2b = left("+EQ2") - left("=EQ2");
	    ht.get("s2").translate(dx_s2b, -dy_s2);
	    ht.get("minusEQ2").translate(dx_s2b, -dy_s2);
	    ht.get("=EQ2").translate(dx_equal2b, 0);
	    if ((s2.isInteger()) && (s2.isNegative()) )
	    {
		ht.get("(s2").translate(dx_s2b, -dy_s2);
		ht.get(")s2").translate(dx_s2b, -dy_s2);
	    }
	}// s2 is not zero

	Rational b2 = (new Rational(y2)).sub(s2);
	int b2width;
	if (b2.isInteger())
	{
	    b2width = fm.stringWidth(b2.getNum()+"");

	    if (s2.equalsZero())
		addText("bval2", b2.getNum()+"", left("Y2copy2"),
			yline6, fontsize, fm, "red", true );
	    else
		addText("bval2", b2.getNum()+"", left("=EQ2")-MED_SKIP-b2width,
			yline6, fontsize, fm, "red", true );
	}
	else
	{
	    b2width = Math.max(fm.stringWidth(b2.getNum()+""),
			       fm.stringWidth(b2.getDen()+"") );
	    addRational("bval2", b2.getNum(), b2.getDen(), 
			left("=EQ2")-MED_SKIP-b2width,
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );
	}
	
	// swap of b and its value around the equal sign
	//int dy_swap = 2*fontsize; // common to both stages

	// first stage
	int dx_bEQ2a = left("=EQ2") - left("bEQ2");
	ht.get("bEQ2").translate(dx_bEQ2a, dy_swap);
	int dx_bval2a = left("=EQ2") - left("bval2");
	ht.get("bval2").translate(dx_bval2a, -dy_swap);

	//second stage
	int dx_bEQ2b = left("=EQ2") - MED_SKIP - width("bEQ2") - left("bEQ2");
	ht.get("bEQ2").translate(dx_bEQ2b, -dy_swap);
	int dx_bval2b = right("=EQ2") + MED_SKIP - left("bval2");
	ht.get("bval2").translate(dx_bval2b, dy_swap);


	if (b2.isInteger())
	    addText("bval2_copy", b2.getNum()+"", left("bval2"),
		    yline6, fontsize, fm, "red", true );
	else
	    addRational("bval2_copy", b2.getNum(), b2.getDen(), 
			left("bval2"),
			yline6-(int)(0.3*fontsize), fontsize, fm, "red", true );

	// final equation
	

	addText("finalY","y =", MIDDLE-20-(finalWidth/2), yline7,fontsize,fm,
		"red", true);

	int dy_m3 = yline7 - yline2;
	int dx_m3;

	if (m.isInteger())
	{
	    dx_m3 = right("finalY") + MED_SKIP - left("mval_copy3");
	    ht.get("mval_copy3").translate(dx_m3, dy_m3);
	}
	else
	{
	    dx_m3 = right("finalY") + MED_SKIP - left("mval_copy3");
	    ht.get("mval_copy3").translate(dx_m3, dy_m3);
	}

	addText("finalX","x", right("mval_copy3")+SMALL_SKIP, 
		yline7,fontsize,fm,"red", true);


	if (m.equalsMinusOne())
	    addText("final-","-", 
		    left("finalX")-fm.stringWidth("-")-SMALL_SKIP, 
		    yline7,fontsize,fm,"red", true);


	addText("final+","+", right("finalX")+MED_SKIP, 
		yline7,fontsize,fm,"red", true);

	int dy_bfinal = yline7 - yline6;
	int dx_bfinal1;
	int dx_bfinal2;

	if (m.equalsZero())
	    dx_bfinal1 = right("finalY") + MED_SKIP - left("bval1_copy");
	else if ((b.isInteger()) && (b.isNegative()))
	    dx_bfinal1 = right("finalX") + MED_SKIP - left("bval1_copy");
	else
	    dx_bfinal1 = right("final+") + MED_SKIP - left("bval1_copy");

	if (m.equalsZero()) 
	    dx_bfinal2 = right("finalY") + MED_SKIP - left("bval2_copy");
	else if ((b.isInteger()) && (b.isNegative()))
	    dx_bfinal2 = right("finalX") + MED_SKIP - left("bval2_copy");
	else
	    dx_bfinal2 = right("final+") + MED_SKIP - left("bval2_copy");

	if ((b.equalsZero()) && (m.equalsZero()) )
	    addText( "final0", "0", right("finalY") + MED_SKIP, yline7,
		     fontsize, fm , "black", true);

	root.addContent( initial );

	/*******************************************
         * "first" snapshot (to allow for a question on m)
         *******************************************/

	animation = new Element("animation");

	Element par;
	Element[] pars;
	Element seq = null;


	pars = new Element[ 1 ];
	pars[0] = makeNoop();
	seq = makeSeq( pseudocodeURL1 + "0",  pars );
	animation.addContent( seq );

	/*******************************************
         *  second snapshot: compute the value of m
         *******************************************/

	int c; // counter for parallel ops in the snapshot

	pars = new Element[ 40 ];
	pars[0] = makePar1( makeShowHide("show",new String[]{
		    "m", "EQ1", "deltay", "y", "div1", "deltax","x"} ));
	pars[1] = makePar1( makeWait( 1000 ));
	pars[2] = makePar1( makeChangeStyle( new String[]{
		    "EQ1", "deltax","x","div1","deltay","y"}, "black"));
	pars[3] = makePar1( makeShowHide("show", new String[]{
		    "EQ2","ynum2","ysub2","minusnum","ynum1","ysub1", 
		    "div2", "xnum2","xsub2","minusden","xnum1","xsub1" }) );
	pars[4] = makePar1( makeWait( 1000 ));
	pars[5] = makePar1( makeChangeStyle( new String[]{
		    "EQ2","ynum2","ysub2","minusnum","ynum1","ysub1", 
		    "div2", "xnum2","xsub2","minusden","xnum1","xsub1"},
		"black") );
	pars[6] = makePar1( makeWait( 1000 ));
	pars[7] = makePar1( makeShowHide("show",new String[]{ 
		    "EQ3"}));
	pars[8] = makePar1( makeWait( 1000 ));
	pars[9] = makePar1( makeShowHide("show",new String[]{ 
		    "Y2copy"}));
	pars[10] = makePar1( makeWait( 1000 ));
	pars[11] = makePar1( makeMove( new String[]{ "Y2copy" }, 
				       dx_y2, dy_num) );
	pars[12] = makePar1( makeWait( 1000 ));
	pars[13] = makePar1( makeShowHide("show",new String[]{ 
		    "minusnum2"}));
	pars[14] = makePar1( makeWait( 1000 ));
	pars[15] = makePar1( makeShowHide("show",new String[]{ "Y1copy"}));
	pars[16] = makePar1( makeWait( 1000 ));
	pars[17] = makePar1( makeMove( new String[]{ "Y1copy" }, 
				       dx_y1, dy_num) );
	if (y1<0)
	    pars[18] = makePar1(makeShowHide("show", new String[]{
			"(num",")num" } ) );
	else
	    pars[18] = makeNoop();

	pars[19] = makePar1( makeShowHide("show",new String[]{"div3"}));
	pars[20] = makePar1( makeWait( 1000 ));
	pars[21] = makePar1( makeShowHide("show",new String[]{"X2copy"}));
	pars[22] = makePar1( makeWait( 1000 ));
	pars[23] = makePar1( makeMove( new String[]{ "X2copy" }, 
				       dx_x2, dy_den) );
	pars[24] = makePar1( makeWait( 1000 ));
	pars[25] = makePar1( makeShowHide("show",new String[]{ 
		    "minusden2"}));
	pars[26] = makePar1( makeWait( 1000 ));
	pars[27] = makePar1( makeShowHide("show",new String[]{ "X1copy"}));
	pars[28] = makePar1( makeWait( 1000 ));
	pars[29] = makePar1( makeMove( new String[]{ "X1copy" }, 
				       dx_x1, dy_den) );
	if (x1<0)
	    pars[30] = makePar1(makeShowHide("show", new String[]{
			"(den",")den" } ) );
	else
	    pars[30] = makeNoop();

	pars[31] = makePar1( makeWait( 1000 ));

	pars[32] = makePar1( makeChangeStyle( new String[]{
		    "EQ3","Y2copy","minusnum2","Y1copy", "div3", 
		    "X2copy","minusden2","X1copy"}, "black") );

	if (y1<0)
	    pars[33] = makePar1( makeChangeStyle( new String[]{
			"(num",")num"}, "black") );
	else
	    pars[33] = makeNoop();

	if (x1<0)
	    pars[34] = makePar1( makeChangeStyle( new String[]{
			"(den",")den"}, "black") );
	else
	    pars[34] = makeNoop();

	pars[35] = makePar1( makeWait( 1000 ));

	pars[36] = makePar1( makeShowHide("show",new String[]{ "EQ4"}));

	if (m_is_int)
	    pars[37] = makePar1( makeShowHide("show",new String[]{"mval_int"}));
	else
	    pars[37] = makePar1( makeShowHide("show",new String[]{
			"mval_num", "mval_div", "mval_den" }));


	pars[38] = makePar1( makeWait( 1000 ));

	if (m_is_int)
	    pars[39] = makePar1( makeChangeStyle( new String[]{
			"m", "EQ4","mval_int"}, "black") );
	else
	    pars[39] = makePar1( makeChangeStyle( new String[]{
			"m", "EQ4","mval_num","mval_div","mval_den"}, "black") );

	seq = makeSeq( pseudocodeURL1 + "1",  pars );
	animation.addContent( seq );


	/*******************************************
         * third snapshot: explain the choice for b 
         *******************************************/

	pars = new Element[ 1 ];
	pars[0] = makeNoop();
	seq = makeSeq( pseudocodeURL1 + "2",  pars );
	animation.addContent( seq );

	/*******************************************
         *  fourth snapshot: compute b using first point 
         *******************************************/


	pars = new Element[ 42 ];

	pars[0] = makePar1( makeShowHide( "show", new String[]{ 
		    "header1a", "header1b", "yEQ1", "=EQ1", "mEQ1", "xEQ1",
		    "+EQ1", "bEQ1"}));

	pars[1] = makePar1( makeShowHide( "show", new String[]{"Y1copy2"}));
	pars[2] = makePar1( makeWait( 1000 ));
	pars[3] = makePar1( makeMove(new String[]{"Y1copy2"},dx_y1copy2,dy_EQ));
	pars[4] = makePar1( makeShowHide( "hide", new String[]{"yEQ1"}));
	pars[5] = makePar1( makeChangeStyle(new String[]{"Y1copy2"},"black"));

	if (m.isInteger())
	{
	    pars[6] = makePar1( makeShowHide("show", new String[]{
			"mval_copy1_int"}));
	    pars[7] = makePar1( makeChangeStyle( new String[]{
			"mEQ1"}, "red"));
	    pars[8] = makePar1( makeWait( 1000 ));
	    pars[9] = makePar1( makeMove(new String[]{"mval_copy1_int"},
					 dx_m1,dy_EQm));
	    pars[10] = makePar1( makeShowHide( "hide", new String[]{"mEQ1"}));
	    pars[11] = makePar1( makeChangeStyle(new String[]{
			"mval_copy1_int"},"black"));
	}
	else
	{
	    pars[6] = makePar1( makeShowHide("show", new String[]{
			"mval_copy1_num","mval_copy1_div","mval_copy1_den"}));
	    pars[7] = makePar1( makeChangeStyle( new String[]{
			"mEQ1"}, "red"));
	    pars[8] = makePar1( makeWait( 1000 ));
	    pars[9] = makePar1( makeMove(new String[]{
			"mval_copy1_num","mval_copy1_div","mval_copy1_den"},
		    dx_m1,dy_EQm));
	    pars[10] = makePar1( makeShowHide( "hide", new String[]{"mEQ1"}));
	    pars[11] = makePar1( makeChangeStyle(new String[]{
			"mval_copy1_num","mval_copy1_div","mval_copy1_den"},
		    "black"));
	}

	
	pars[12] = makePar( new Element[]
	    {
		makeShowHide( "show", new String[]{"X1copy2"}),
		makeChangeStyle(new String[]{
			"xEQ1"}, "red")
	    });
	pars[13] = makePar1( makeWait( 1000 ));
	pars[14] = makePar1( makeMove(new String[]{"X1copy2"},
				      dx_x1copy2,dy_EQ));
	pars[15] = makePar( new Element[]
	    {
		makeShowHide( "hide", new String[]{"xEQ1"}),
		makeShowHide( "show", new String[]{"(x1", ")x1"})
	    });
	pars[16] = makePar1( makeWait( 1000 ));
	
	if (m.isInteger())
	    pars[17] = makePar1( makeChangeStyle( new String[]{
			"mval_copy1_int"}, "red"));
	else
	    pars[17] = makePar1( makeChangeStyle(new String[]{
			"mval_copy1_num","mval_copy1_div","mval_copy1_den"},
		    "red"));
	
	pars[18] = makePar1( makeWait( 1000 ));
	pars[19] = makePar1( makeShowHide( "hide", new String[]{
		    "X1copy2", "(x1", ")x1"}));

	if (m.isInteger())
	    pars[20] = makePar1( makeShowHide( "hide", new String[]{
			"mval_copy1_int"}));
	else
	    pars[20] = makePar1( makeShowHide( "hide", new String[]{
			"mval_copy1_num","mval_copy1_div","mval_copy1_den"}));

	if (s1.equalsZero())
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{ "s1"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeShowHide( "hide", 
					       new String[]{ "s1", "+EQ1"}));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{"=EQ1"},"red"));
	    pars[26] = makePar1( makeMove(new String[]{"=EQ1"}, dx_equal1a,0));
	    pars[27] = makePar1( makeChangeStyle( new String[]{"=EQ1"},
						  "black"));
	    pars[28] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y1copy2"}));
	    pars[29] = makePar1(makeShowHide("show", new String[]{"bval1"}));

	    for(int i=30; i<38; i++)
		pars[i] = makeNoop();

	}
	else if (s1.isInteger())
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{ "s1"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeChangeStyle( new String[]{
			"s1"}, "black" ));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{
			"s1", "=EQ1"}, "red" ));
	    pars[26] = makePar1( makeShowHide( "hide", new String[]{ "+EQ1"}));
	    pars[27] = makePar( new Element[]
		{
		    makeMove(new String[]{"=EQ1"}, dx_equal1a,0),
		    makeMove(new String[]{"s1"}, dx_s1a,dy_s1)
		});
	    if (s1.isNegative())  // s1 is a negative integer
	    {
		pars[28] =  makePar1( makeShowHide( "show", new String[]{
			    "(s1", ")s1"}));
		pars[29] =  makePar1( makeShowHide( "show", 
						    new String[]{ "minusEQ1"}));
		pars[30] = makePar1( makeWait( 1000 ));
		pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ1"}, dx_equal1b,0),
			makeMove(new String[]{
				"minusEQ1","s1","(s1",")s1"}, dx_s1b,-dy_s1)
		    });
		pars[32] = makePar( new Element[]
		    {
			makeChangeStyle( new String[]{"Y1copy2"}, "red" ),
			makeChangeStyle( new String[]{"=EQ1"}, "black" )
		    });
		pars[33] = makePar1( makeWait( 1000 ));
		pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y1copy2","minusEQ1","(s1",")s1","s1"}));
		pars[35] = makePar1(makeShowHide("show", new String[]{"bval1"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval1"}, "black" ));
	    }
	    else // s1 is a positive integer
	    {
		pars[28] = makeNoop();
		pars[29] = makePar1( makeShowHide( "show", 
						    new String[]{ "minusEQ1"}));
		pars[30] = makePar1( makeWait( 1000 ));
		pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ1"}, dx_equal1b,0),
			makeMove(new String[]{"minusEQ1","s1"}, dx_s1b,-dy_s1)
		    });
		pars[32] = makePar( new Element[]
		    {
			makeChangeStyle( new String[]{"Y1copy2"}, "red" ),
			makeChangeStyle( new String[]{"=EQ1"}, "black" )
		    });
		pars[33] = makePar1( makeWait( 1000 ));
		pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y1copy2","minusEQ1","s1"}));
		pars[35] = makePar1(makeShowHide("show", new String[]{"bval1"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval1"}, "black" ));
	    }// s1 is a positive integer
	}// s1 is a non-zero integer
	else // s1 is a non-zero, non-integer rational
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{
			"s1_num","s1_div","s1_den"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeChangeStyle( new String[]{
			"s1_num","s1_div","s1_den"}, "black"));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{
			"s1_num", "s1_div", "s1_den", "=EQ1"}, "red" ));
	    pars[26] = makePar1( makeShowHide( "hide", new String[]{ "+EQ1"}));
	    pars[27] = makePar( new Element[]
		{
		    makeMove(new String[]{"=EQ1"}, dx_equal1a,0),
		    makeMove(new String[]{"s1","s1_num","s1_div","s1_den"}, 
			     dx_s1a,dy_s1)
		});
	    pars[28] = makeNoop();
	    pars[29] =  makePar1( makeShowHide( "show", 
						new String[]{ "minusEQ1"}));
	    pars[30] = makePar1( makeWait( 1000 ));
	    pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ1"}, dx_equal1b,0),
			makeMove(new String[]{ "minusEQ1","s1_num",
				      "s1_div","s1_den"}, dx_s1b,-dy_s1)
		    });
	    pars[32] = makePar( new Element[]
		{
		    makeChangeStyle( new String[]{"Y1copy2"}, "red" ),
		    makeChangeStyle( new String[]{"=EQ1"}, "black" )
		});
	    pars[33] = makePar1( makeWait( 1000 ));
	    pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			"Y1copy2","minusEQ1","s1_num", "s1_div","s1_den"}));
	    if (b.isInteger())
	    {
		pars[35] = makePar1(makeShowHide("show", new String[]{
			    "bval1"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval1"}, "black" ));
	    }
	    else
	    {
		pars[35] = makePar1(makeShowHide("show", new String[]{
			    "bval1_num","bval1_div","bval1_den"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval1_num","bval1_div","bval1_den"}, "black" ));
	    }
	}// s is a non-zero, non-integer rational
	

	if (b.isInteger())
	{
	    pars[38] = makePar1( makeChangeStyle( new String[]{
			"bEQ1", "bval1" }, "red"));
	    pars[39] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ1"}, dx_bEQ1a,dy_swap),
			makeMove(new String[]{ "bval1"}, dx_bval1a,-dy_swap)
		    });

	    pars[40] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ1"}, dx_bEQ1b,-dy_swap),
			makeMove(new String[]{ "bval1"}, dx_bval1b,dy_swap)
		    });

	    pars[41] = makePar1( makeChangeStyle( new String[]{
			"bEQ1", "bval1", "header1a","header1b" } ,  "black"));

	}
	else
	{
	    pars[38] = makePar1( makeChangeStyle( new String[]{
			"bEQ1", "bval1_num","bval1_div","bval1_den" }, "red"));
	    pars[39] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ1"}, dx_bEQ1a,dy_swap),
			makeMove(new String[]{ "bval1_num", "bval1_div",
					       "bval1_den"}, dx_bval1a,-dy_swap)
		    });

	    pars[40] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ1"}, dx_bEQ1b,-dy_swap),
			makeMove(new String[]{ "bval1_num", "bval1_div",
					       "bval1_den"}, dx_bval1b,dy_swap)
		    });

	    pars[41] = makePar1( makeChangeStyle( new String[]	{
			"bEQ1", "bval1_num","bval1_div","bval1_den",
			"header1a","header1b" } ,  "black"));
	}

	seq = makeSeq( pseudocodeURL1 + "3",  pars );
	animation.addContent( seq );

	/*******************************************
         *  fifth snapshot: compute b using 2nd point 
         *******************************************/

	pars = new Element[ 42 ];


	pars[0] = makePar1( makeShowHide( "show", new String[]{ 
		    "middle", "header2a", "header2b", "yEQ2", "=EQ2", "mEQ2", 
		    "xEQ2", "+EQ2", "bEQ2"}));

	pars[1] = makePar1( makeShowHide( "show", new String[]{"Y2copy2"}));
	pars[2] = makePar1( makeWait( 1000 ));
	pars[3] = makePar1( makeMove(new String[]{"Y2copy2"},dx_y2copy2,dy_EQ));
	pars[4] = makePar1( makeShowHide( "hide", new String[]{"yEQ2"}));
	pars[5] = makePar1( makeChangeStyle(new String[]{"Y2copy2"},"black"));

	if (m.isInteger())
	{
	    pars[6] = makePar1( makeShowHide("show", new String[]{
			"mval_copy2_int"}));
	    pars[7] = makePar1( makeChangeStyle( new String[]{
			"mEQ2"}, "red"));
	    pars[8] = makePar1( makeWait( 1000 ));
	    pars[9] = makePar1( makeMove(new String[]{"mval_copy2_int"},
					 dx_m2,dy_EQm));
	    pars[10] = makePar1( makeShowHide( "hide", new String[]{"mEQ2"}));
	    pars[11] = makePar1( makeChangeStyle(new String[]{
			"mval_copy2_int"},"black"));
	}
	else
	{
	    pars[6] = makePar1( makeShowHide("show", new String[]{
			"mval_copy2_num","mval_copy2_div","mval_copy2_den"}));
	    pars[7] = makePar1( makeChangeStyle( new String[]{
			"mEQ2"}, "red"));
	    pars[8] = makePar1( makeWait( 1000 ));
	    pars[9] = makePar1( makeMove(new String[]{
			"mval_copy2_num","mval_copy2_div","mval_copy2_den"},
		    dx_m2,dy_EQm));
	    pars[10] = makePar1( makeShowHide( "hide", new String[]{"mEQ2"}));
	    pars[11] = makePar1( makeChangeStyle(new String[]{
			"mval_copy2_num","mval_copy2_div","mval_copy2_den"},
		    "black"));
	}

	
	pars[12] = makePar( new Element[]
	    {
		makeShowHide( "show", new String[]{"X2copy2"}),
		makeChangeStyle(new String[]{
			"xEQ2"}, "red")
	    });
	pars[13] = makePar1( makeWait( 1000 ));
	pars[14] = makePar1( makeMove(new String[]{"X2copy2"},
				      dx_x2copy2,dy_EQ));
	pars[15] = makePar( new Element[]
	    {
		makeShowHide( "hide", new String[]{"xEQ2"}),
		makeShowHide( "show", new String[]{"(x2", ")x2"})
	    });
	pars[16] = makePar1( makeWait( 1000 ));
	
	if (m.isInteger())
	    pars[17] = makePar1( makeChangeStyle( new String[]{
			"mval_copy2_int"}, "red"));
	else
	    pars[17] = makePar1( makeChangeStyle(new String[]{
			"mval_copy2_num","mval_copy2_div","mval_copy2_den"},
		    "red"));
	
	pars[18] = makePar1( makeWait( 1000 ));
	pars[19] = makePar1( makeShowHide( "hide", new String[]{
		    "X2copy2", "(x2", ")x2"}));

	if (m.isInteger())
	    pars[20] = makePar1( makeShowHide( "hide", new String[]{
			"mval_copy2_int"}));
	else
	    pars[20] = makePar1( makeShowHide( "hide", new String[]{
			"mval_copy2_num","mval_copy2_div","mval_copy2_den"}));

	if (s2.equalsZero())
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{ "s2"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeShowHide( "hide", 
					       new String[]{ "s2", "+EQ2"}));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{"=EQ2"},"red"));
	    pars[26] = makePar1( makeMove(new String[]{"=EQ2"}, dx_equal2a,0));
	    pars[27] = makePar1( makeChangeStyle( new String[]{"=EQ2"},
						  "black"));

	    pars[28] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y2copy2"}));
	    pars[29] = makePar1(makeShowHide("show", new String[]{"bval2"}));


	    for(int i=30; i<38; i++)
		pars[i] = makeNoop();

	}
	else if (s2.isInteger())
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{ "s2"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeChangeStyle( new String[]{
			"s2"}, "black" ));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{
			"s2", "=EQ2"}, "red" ));
	    pars[26] = makePar1( makeShowHide( "hide", new String[]{ "+EQ2"}));
	    pars[27] = makePar( new Element[]
		{
		    makeMove(new String[]{"=EQ2"}, dx_equal2a,0),
		    makeMove(new String[]{"s2"}, dx_s2a,dy_s2)
		});
	    if (s2.isNegative())  // s2 is a negative integer
	    {
		pars[28] =  makePar1( makeShowHide( "show", new String[]{
			    "(s2", ")s2"}));
		pars[29] =  makePar1( makeShowHide( "show", 
						    new String[]{ "minusEQ2"}));
		pars[30] = makePar1( makeWait( 1000 ));
		pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ2"}, dx_equal2b,0),
			makeMove(new String[]{
				"minusEQ2","s2","(s2",")s2"}, dx_s2b,-dy_s2)
		    });
		pars[32] = makePar( new Element[]
		    {
			makeChangeStyle( new String[]{"Y2copy2"}, "red" ),
			makeChangeStyle( new String[]{"=EQ2"}, "black" )
		    });
		pars[33] = makePar1( makeWait( 1000 ));
		pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y2copy2","minusEQ2","(s2",")s2","s2"}));
		pars[35] = makePar1(makeShowHide("show", new String[]{"bval2"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval2"}, "black" ));
	    }
	    else // s2 is a positive integer
	    {
		pars[28] = makeNoop();
		pars[29] = makePar1( makeShowHide( "show", 
						    new String[]{ "minusEQ2"}));
		pars[30] = makePar1( makeWait( 1000 ));
		pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ2"}, dx_equal2b,0),
			makeMove(new String[]{"minusEQ2","s2"}, dx_s2b,-dy_s2)
		    });
		pars[32] = makePar( new Element[]
		    {
			makeChangeStyle( new String[]{"Y2copy2"}, "red" ),
			makeChangeStyle( new String[]{"=EQ2"}, "black" )
		    });
		pars[33] = makePar1( makeWait( 1000 ));
		pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			    "Y2copy2","minusEQ2","s2"}));
		pars[35] = makePar1(makeShowHide("show", new String[]{"bval2"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval2"}, "black" ));
	    }// s2 is a positive integer
	}// s2 is a non-zero integer
	else // s2 is a non-zero, non-integer rational
	{
	    pars[21] = makePar1( makeShowHide( "show", new String[]{
			"s2_num","s2_div","s2_den"}));
	    pars[22] = makePar1( makeWait( 1000 ));
	    pars[23] = makePar1( makeChangeStyle( new String[]{
			"s2_num","s2_div","s2_den"}, "black"));
	    pars[24] = makePar1( makeWait( 1000 ));
	    pars[25] = makePar1( makeChangeStyle( new String[]{
			"s2_num", "s2_div", "s2_den", "=EQ2"}, "red" ));
	    pars[26] = makePar1( makeShowHide( "hide", new String[]{ "+EQ2"}));
	    pars[27] = makePar( new Element[]
		{
		    makeMove(new String[]{"=EQ2"}, dx_equal2a,0),
		    makeMove(new String[]{"s2","s2_num","s2_div","s2_den"}, 
			     dx_s2a,dy_s2)
		});
	    pars[28] = makeNoop();
	    pars[29] =  makePar1( makeShowHide( "show", 
						new String[]{ "minusEQ2"}));
	    pars[30] = makePar1( makeWait( 1000 ));
	    pars[31] = makePar( new Element[]
		    {
			makeMove(new String[]{"=EQ2"}, dx_equal2b,0),
			makeMove(new String[]{ "minusEQ2","s2_num",
				      "s2_div","s2_den"}, dx_s2b,-dy_s2)
		    });
	    pars[32] = makePar( new Element[]
		{
		    makeChangeStyle( new String[]{"Y2copy2"}, "red" ),
		    makeChangeStyle( new String[]{"=EQ2"}, "black" )
		});
	    pars[33] = makePar1( makeWait( 1000 ));
	    pars[34] = makePar1( makeShowHide( "hide", new String[]{ 
			"Y2copy2","minusEQ2","s2_num", "s2_div","s2_den"}));
	    if (b2.isInteger())
	    {
		pars[35] = makePar1(makeShowHide("show", new String[]{
			    "bval2"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval2"}, "black" ));
	    }
	    else
	    {
		pars[35] = makePar1(makeShowHide("show", new String[]{
			    "bval2_num","bval2_div","bval2_den"}));
		pars[36] = makePar1( makeWait( 1000 ));
		pars[37] = makePar1( makeChangeStyle( new String[]{
			    "bval2_num","bval2_div","bval2_den"}, "black" ));
	    }
	}// s2 is a non-zero, non-integer rational
	

	if (b2.isInteger())
	{
	    pars[38] = makePar1( makeChangeStyle( new String[]{
			"bEQ2", "bval2" }, "red"));
	    pars[39] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ2"}, dx_bEQ2a,dy_swap),
			makeMove(new String[]{ "bval2"}, dx_bval2a,-dy_swap)
		    });

	    pars[40] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ2"}, dx_bEQ2b,-dy_swap),
			makeMove(new String[]{ "bval2"}, dx_bval2b,dy_swap)
		    });

	    pars[41] = makePar1( makeChangeStyle( new String[]{
			"bEQ2", "bval2", "header2a","header2b" } ,  "black"));

	}
	else
	{
	    pars[38] = makePar1( makeChangeStyle( new String[]{
			"bEQ2", "bval2_num","bval2_div","bval2_den" }, "red"));
	    pars[39] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ2"}, dx_bEQ2a,dy_swap),
			makeMove(new String[]{ "bval2_num", "bval2_div",
					       "bval2_den"}, dx_bval2a,-dy_swap)
		    });

	    pars[40] = makePar( new Element[]
		    {
			makeMove(new String[]{"bEQ2"}, dx_bEQ2b,-dy_swap),
			makeMove(new String[]{ "bval2_num", "bval2_div",
					       "bval2_den"}, dx_bval2b,dy_swap)
		    });

	    pars[41] = makePar1( makeChangeStyle( new String[]	{
			"bEQ2", "bval2_num","bval2_div","bval2_den",
			"header2a","header2b" } ,  "black"));
	}


	seq = makeSeq( pseudocodeURL1 + "4",  pars );
	animation.addContent( seq );

	/*******************************************
         *  sixth snapshot: display the final equation 
         *******************************************/

	pars = new Element[ 100 ];
	c = 0;

	// empty equation
	pars[c++] = show("finalY");
	pars[c++] = wait(1000);
	pars[c++] = black("finalY");
	pars[c++] = red("m");

	// y = 
	if (m.isInteger())
	{
	    pars[c++] = show("mval_copy3");
	    pars[c++] = wait(1000);
	    pars[c++] = move("mval_copy3", dx_m3, dy_m3);
	    pars[c++] = black(new String[]{"m","mval_copy3"});
	}
	else
	{
	    pars[c++] = show(new String[]{"mval_copy3_num","mval_copy3_div",
					  "mval_copy3_den"});  
	    pars[c++] = wait(1000);
	    pars[c++] = move(new String[]{"mval_copy3_num","mval_copy3_div",
					  "mval_copy3_den"}, dx_m3, dy_m3);
	    pars[c++] = black(new String[]{"m","mval_copy3_num",
					   "mval_copy3_div","mval_copy3_den"});
	}

	pars[c++] = show("finalX");
	pars[c++] = makePar1( makeWait( 1000 ));

	// y = mx
	if (m.equalsOne())	
	{
	    pars[c++] = hide("mval_copy3");
	    pars[c++] = black("finalX");
	}
	else if (m.equalsZero())
	{
	    pars[c++] = hide(new String[]{"mval_copy3","finalX"});
	    pars[c++] = makeNoop();
	}
	else if (m.equalsMinusOne())
	{
	    pars[c++] = makePar( new Element[]{
                                     hide1("mval_copy3"), show1("final-") });
	    pars[c++] = black( new String[]{"finalX","final-"} );
	}
	else 
	{
	    pars[c++] = black("finalX");
	    pars[c++] = makeNoop();
	}

	if ( (m.equalsZero()) || (b.isInteger() && b.isNegative()) ) 
	    for(int i=0; i<3; i++)
		pars[c++] = makeNoop();  // no need for a '+'
	else
	{
	    pars[c++] = show("final+");
	    pars[c++] = wait(1000);
	    pars[c++] = black("final+");
	}

	// y = mx +
	pars[c++] = red( new String[]{"bEQ1","bEQ2"});	

	if (b.isInteger())
	{
	    pars[c++] = show( new String[]{"bval1_copy","bval2_copy"});	    
	    pars[c++] = wait(1000);
	    pars[c++] = makePar( new Element[] {
		    move1("bval1_copy", dx_bfinal1, dy_bfinal),
		    move1("bval2_copy", dx_bfinal2, dy_bfinal) } );
	    pars[c++] = black( new String[]{ "bEQ1","bEQ2", "bval1_copy",
					     "bval2_copy"});
	}
	else
	{
	    pars[c++] = show( new String[]{"bval1_copy_num","bval1_copy_div",
					   "bval1_copy_den","bval2_copy_num",
					   "bval2_copy_div","bval2_copy_den"});
	    pars[c++] = wait(1000);
	    pars[c++] = makePar( new Element[] {
		    move1(new String[]{ "bval1_copy_num","bval1_copy_div",
				       "bval1_copy_den"}, dx_bfinal1,dy_bfinal),
		    move1(new String[]{"bval2_copy_num","bval2_copy_div",
				      "bval2_copy_den"},dx_bfinal2,dy_bfinal)});
	    pars[c++] = black(new String[]{ "bEQ1","bEQ2","bval1_copy_num",
					    "bval1_copy_div","bval1_copy_den",
					    "bval2_copy_num","bval2_copy_div",
					    "bval2_copy_den"});
	}

	// y = mx + b
	if (b.equalsZero())

	    if (m.equalsZero())
		pars[c++] = makePar( new Element[] {
			hide1( new String[]{ "bval1_copy","bval2_copy"}),
			show1("final0") } );
	    else
		pars[c++] = hide( new String[]{ "final+","bval1_copy",
						"bval2_copy"});
	else pars[c++] = makeNoop();
	
	pars[c++] = makePar1(makeShowHide("show", new String[]{"frame"} ));

	seq = makeSeq( pseudocodeURL1 + "5",  pars, c );

	animation.addContent( seq );

	root.addContent( animation );

	root.addContent( createQuestions() );


	try {
	    XMLOutputter outputter = 
		new XMLOutputter(Format.getPrettyFormat());
	    FileWriter writer = new FileWriter(filename);
	    outputter.output(myDocument, writer);
	    writer.close(); 
	} catch (java.io.IOException e) {
	    e.printStackTrace();
	}


    }// createScript method

    static Element createQuestions()
    {
	Element questions = new Element( "questions" );

	Q q = pickSlopeQuestion();
	q.setId( "1" );
	questions.addContent( q.toXML() );

	Q[] qs = pickQuestions();
	qs[0].setId( "3" ); // B question
	if (rand.nextBoolean())
	    qs[1].setId( "4" );
	else
	    qs[1].setId( "5" );
	questions.addContent( qs[0].toXML() );
	questions.addContent( qs[1].toXML() );
	return questions;
    }

    static Q pickSlopeQuestion()
    {
	Q q = null;

	computeStringValues();
	ArrayList<String> tOptions = generateTrueStatementsForSlope();
	ArrayList<String> fOptions = generateFalseStatementsForSlope();

	int type = 1 + rand.nextInt(4);

	switch (type) 
	{
	case Q.TF: // ************* T/F question ********************

	    if (rand.nextBoolean())  // answer is true
		q = new Q(Q.TF,"M",(pickOptions(1,tOptions))[0],"true",
			  Arrays.asList( 1 ) );
	    else
		q = new Q(Q.TF,"M",(pickOptions(1,fOptions))[0],"false",
			  Arrays.asList( 1 ) );		
	    break;


	case Q.MC: // ************* MC question ********************
	    if (rand.nextBoolean())  // correct answer is a true statement
	    {
		String tOpt = (pickOptions(1,tOptions))[0];
		String[] fOpt = pickOptions(2+rand.nextInt(3),fOptions);
		q = new Q(Q.MC,"M",
			  "Which ONE of the following statements is TRUE?",
			  "", Arrays.asList( 1 ) );

		// shuffle the options
	        ArrayList<S> l = new ArrayList<S>( 10 ); 
		for(int i=0; i<fOpt.length; i++)
		    l.add( new FS( fOpt[i] ));
		l.add( new TS( tOpt ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions( q, l);

	    }
	    else   // correct answer is a false statement
	    {
		String fOpt = (pickOptions(1,fOptions))[0];
		String[] tOpt = pickOptions(2+rand.nextInt(3),tOptions);
		q = new Q(Q.MC,"M",
			  "Which ONE of the following statements is FALSE?",
			  "", Arrays.asList( 1 ) );
		// shuffle the options
	        ArrayList<S> l = new ArrayList<S>( 10 ); 
		for(int i=0; i<tOpt.length; i++)
		    l.add( new FS( tOpt[i] ));
		l.add( new TS( fOpt ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions( q, l);
	    }
	    break;

	case Q.MS: // ************* MS question ********************

	    if (rand.nextBoolean()) // correct answers is(are) true statements
	    {
		q = new Q(Q.MS,"M", "Select ALL the TRUE statements, if any.",
			  "", Arrays.asList( 1 ) );
		
		// pick false and true options
		int n = 3 + rand.nextInt(4);
		int k = rand.nextInt(n+1);
		String[] tOpt = pickOptions(k,tOptions);
		String[] fOpt = pickOptions(n-k,fOptions);

		// shuffle them
	        ArrayList<S> l = new ArrayList<S>( 6 ); 
		for(int i=0; i<tOpt.length; i++)
		    l.add( new TS( tOpt[i] ));
		for(int i=0; i<fOpt.length; i++)
		    l.add( new FS( fOpt[i] ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions(q,l);
	    }
	    else  // correct answers is(are) false statement(s)
	    {
		q = new Q(Q.MS,"M", "Select ALL the FALSE statements, if any.",
			  "", Arrays.asList( 1 ) );
		
		// pick false and true options
		int n = 3 + rand.nextInt(4);
		int k = rand.nextInt(n+1);
		String[] fOpt = pickOptions(k,fOptions);
		String[] tOpt = pickOptions(n-k,tOptions);

		// shuffle them
	        ArrayList<S> l = new ArrayList<S>( 6 ); 
		for(int i=0; i<tOpt.length; i++)
		    l.add( new FS( tOpt[i] ));
		for(int i=0; i<fOpt.length; i++)
		    l.add( new TS( fOpt[i] ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions(q,l);
	    }
	    break;

	case Q.FB: // ************* FB question ********************
	    q = new Q(Q.FB,"M", "What is the value m of the slope?    (your answer may be an integer, e.g., 1 or -2, or a rational number, e.g., 1/2 or -2/3, with no white space)",
		      mstr, Arrays.asList( 1 ) );

	    break;
	}// switch statement
	q.setId("1");
	return q;
    }


    static void computeStringValues()
    {
	if (m.isInteger())
	{
	    mstr = m.getNum() + "";
	    negmstr = (- m.getNum()) + "";
	    if ( (!m.equalsZero()) && (Math.abs(m.getNum())!=1) )
		if (m.isNegative())
		    invmstr = "-1/" + (-m.getNum());
		else
		    invmstr = "1/"+ mstr;
	}
	else
	{
	    mstr = m.getNum() + "/" + m.getDen();
	    negmstr = (-m.getNum()) + "/" + m.getDen();
	    if (Math.abs(m.getNum())==1)
		invmstr = (m.isNegative() ? "-" : "") + m.getDen(); 
	    else
		invmstr = (m.isNegative() ? "-" : "") + m.getDen() 
		    + "/" + (m.isNegative() ? - m.getNum() : m.getNum() );

	}

    }
    static String computeStringValue(Rational r)
    {
	if (r.isInteger())
	    return r.getNum() + "";
	else
	    return r.getNum() + "/" + r.getDen();
    }

    //***************************************************
    //                false options
    //***************************************************


    static ArrayList<String> generateFalseStatementsForSlope()
    {
	ArrayList<String> fOptions = new ArrayList<String>( 20 );

	fOptions.add( "The line passing through the two points is vertical." );	    
	if (m.isNegative()) 
	{
	    fOptions.add( "The slope m is NOT negative." );
	    fOptions.add( "The slope m is equal to 0." );	    
	    fOptions.add( "The line passing through the two points is horizontal." );	    
	    fOptions.add( "The slope m is positive." );	    
	} else if (m.isPositive()) 
	{
	    fOptions.add( "The slope m is NOT positive." );
	    fOptions.add( "The slope m is equal to 0." );	    
	    fOptions.add( "The line passing through the two points is horizontal." );	    
	    fOptions.add( "The slope m is negative." );	    
	} else if (m.equalsZero()) 
	{
	    fOptions.add( "The slope m is NOT equal to 0." );
	    fOptions.add( "The line passing through the two points is NOT horizontal." );
	    fOptions.add( "The slope m is positive." );
	    fOptions.add( "The slope m is negative." );
	}
	if (m.isInteger())       
	{
	    fOptions.add( "The value of the slope m is NOT an integer." );
	    if (m.isNegative()) 
	    {
		fOptions.add( "The value of the slope m is a NOT negative integer." );
		fOptions.add( "The value of the slope m is a positive integer." );
	    } else if (m.isPositive()) 
	    {
		fOptions.add( "The value of the slope m is NOT a positive integer." );
		fOptions.add( "The value of the slope m is a negative integer." );
	    } else if (m.equalsZero()) 
	    {
		fOptions.add( "The slope m is a positive integer." );
		fOptions.add( "The slope m is a negative integer." );
	    }
	}


	fOptions.add( "The value of the slope m is NOT equal to " + mstr + "." );
	if (!m.equalsZero())
	    fOptions.add( "The value of the slope m is equal to " + negmstr + "." );

	if ( invmstr != null )
	    fOptions.add( "The value of the slope m is equal to " + invmstr + "." );

	return fOptions;
    }


    //***************************************************
    //                true options
    //***************************************************
	
    static ArrayList<String> generateTrueStatementsForSlope()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 20 );
	tOptions.add( "The line passing through the two points is NOT vertical." );	    
	if (m.isNegative()) 
	{
	    tOptions.add( "The slope m is negative." );
	    tOptions.add( "The slope m is NOT equal to 0." );	    
	    tOptions.add( "The line passing through the two points is NOT horizontal." );	    
	    tOptions.add( "The slope m is NOT positive." );	    
	} else if (m.isPositive()) 
	{
	    tOptions.add( "The slope m is positive." );
	    tOptions.add( "The slope m is NOT equal to 0." );	    
	    tOptions.add( "The line passing through the two points is NOT horizontal." );	    
	    tOptions.add( "The slope m is NOT negative." );	    
	} else if (m.equalsZero()) 
	{
	    tOptions.add( "The slope m is equal to 0." );
	    tOptions.add( "The line passing through the two points is horizontal." );
	    tOptions.add( "The slope m is NOT positive." );
	    tOptions.add( "The slope m is NOT negative." );
	}
	if (m.isInteger())       
	{
	    tOptions.add( "The value of the slope m is an integer." );
	    if (m.isNegative()) 
	    {
		tOptions.add( "The value of the slope m is a negative integer." );
		tOptions.add( "The value of the slope m is NOT a positive integer." );
	    } else if (m.isPositive()) 
	    {
		tOptions.add( "The value of the slope m is a positive integer." );
		tOptions.add( "The value of the slope m is NOT a negative integer." );
	    } else if (m.equalsZero()) 
	    {
		tOptions.add( "The slope m is NOT a positive integer." );
		tOptions.add( "The slope m is NOT a negative integer." );
	    }
	}

	tOptions.add( "The value of the slope m is equal to " + mstr + "." );
	if (!m.equalsZero())
	    tOptions.add( "The value of the slope m is NOT equal to " + negmstr + "." );

	if ( invmstr != null )
	    tOptions.add( "The value of the slope m is NOT equal to " + invmstr + "." );

	return tOptions;
    }// generateTrueStatementsForSlope method

    static Pair generatePoints()
    {
	ArrayList<Point> onLine = new ArrayList<Point>(400);
	ArrayList<Point> offLine = new ArrayList<Point>(400);

	/*
	  x1 = y1 = 0;
	  x2 = 1; 
	  y2 = 80;
	  m = new Rational( 79 );
	*/
	boolean largeSlope = Math.abs( (double)m.getNum() / m.getDen() ) > 20;
	Rational intercept = computeIntercept( x1, y1 );

	if (m.isInteger())	    
	{
	    for(int x=-200; x<=200; x++)
	    {
		int y = (m.mult( new Rational(x) ).add( intercept )).getNum(); 
		int randY = y + 
		    (rand.nextBoolean() ? -1 : 1) * (1 + rand.nextInt(10) );
		if ((x != x1) && (x != x2)) // don't add original points
		{  
		    if (largeSlope)
			if (Math.abs(y)<1000)
			    onLine.add( new Point(x,y) );
			else
			    offLine.add( 
					new Point(x, 
						  (rand.nextBoolean() ? -1 : 1) *
						  rand.nextInt(100) ) );
 
		    else
			if (rand.nextBoolean())
			    onLine.add( new Point(x,y) );
			else
			    offLine.add( new Point(x,randY) );

		}
		else
		    offLine.add( new Point(x,randY ) );
	    }
	} else // m is not an integer

	    for(int x=-200; x<=200; x++)
	    {
		Rational y = m.mult( new Rational(x) ).add( intercept ); 
		if ( (x != x1) && (x != x2)  &&
		     (y.isInteger()) && (Math.abs(y.getNum())<1000) )
		    onLine.add( new Point(x,y.getNum()) );
		else
		    offLine.add( new Point(x,-200 + rand.nextInt(401) ) );
	    }

	/*
	  System.out.println( onLine.size() + " + " + offLine.size() + " = " +
	  (onLine.size() + offLine.size()) );

	  if ( (onLine.size() ==0) || (offLine.size() ==0) )
	  System.out.println( " ***** " + x1 + "," + y1 +
	  "  " + x2 + "," + y2 );
	*/
	return new Pair( onLine, offLine );
    }


    static Q[] pickQuestions()
    {
	Q[] qs = new Q[2];

	Pair points = generatePoints();
	onLine = points.getOnLine();
	offLine = points.getOffLine();

	qs[0] = pickBQuestion();
	qs[1] = pickPointQuestion();

	return qs;
    }//	pickQuestions method


    static Q pickBQuestion()
    {
	Q q = null;

	b = (new Rational(y1)).sub( m.mult( new Rational(x1) ) );
	String bstr = computeStringValue( b );
	ArrayList<String> fOptions = generateIncorrectBValues();
	boolean noIncorrect = fOptions.size() == 0;

	int type;
	if (noIncorrect)
	    type = 1 + rand.nextInt(2); // no MC or MS question
	else
	    type = 1 + rand.nextInt(3);     // no MS question

	//type = Q.FB;

	switch (type) 
	{
	case Q.TF: // ************* T/F question ********************

	    if (rand.nextBoolean())  // correct answer is to click "True"
		
		if (noIncorrect ||      // affirmative statement
		    rand.nextBoolean() )  
		    q = new Q(Q.TF,"B", "The value of b is " + bstr,"true",
			      Arrays.asList( 1 ) );
		else                     // negative statement
		    q = new Q(Q.TF,"B","The value of b is NOT "+ 
			      (pickOptions(1,fOptions))[0],"true",
			      Arrays.asList( 1 ) );		
	    
	    else
		
		// correct answer is to click "False"
		
		if ( !noIncorrect &&
		     rand.nextBoolean() )  // affirmative statement
		    q = new Q(Q.TF,"B", "The value of b is " +
			      (pickOptions(1,fOptions))[0],"false",
			      Arrays.asList( 1 ) );
		else                     // negative statement
		    q = new Q(Q.TF,"B","The value of b is NOT "+ 
			      bstr,"false", Arrays.asList( 1 ) );		
	    break;

	    
	case Q.MC: // ************* MC question ********************

	    String[] fOpt = pickOptions(Math.min( fOptions.size(),
						  2+rand.nextInt(3)),
					fOptions);
	    q = new Q(Q.MC,"B",
		      "Which ONE of the following values is b equal to?",
		      "", Arrays.asList( 1 ) );

	    // shuffle the options
	    ArrayList<S> l = new ArrayList<S>( 6 ); 
	    for(int i=0; i<fOpt.length; i++)
		l.add( new FS( fOpt[i] ));
	    l.add( new TS( bstr ));
	    for(int i=0; i<10; i++)
		java.util.Collections.shuffle( l );
	    addOptions(q,l);

	    break;
				      
	case Q.FB: // ************* FB question ********************
	    q = new Q(Q.FB,"M", "What is the value of b?     (your answer may be an integer, e.g., 1 or -2, or a rational number, e.g., 1/2 or -2/3, with no white space)",
		      bstr, Arrays.asList( 1 ) );

	
	}// switch statement

	q.setId("3");
	return q;
    }

    static ArrayList<String> generateIncorrectBValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);


	Rational next;
	    
	used.add( b );

	Rational X1 = new Rational( x1 );
	Rational Y1 = new Rational( y1 );
	Rational X2 = new Rational( x2 );
	Rational Y2 = new Rational( y2 );

	next = m.mult(X1).add(Y1);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = m.mult(X1).sub(Y1);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = m.mult(Y1).sub(X1);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = X1.sub( m.mult(Y1) );
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = m.mult(X2).add(Y2);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = m.mult(X2).sub(Y2);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = m.mult(Y2).sub(X2);
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	next = X2.sub( m.mult(Y2) );
	if (!myContains(used, next))
	{
	    used.add( next );
	    values.add( computeStringValue( next ) );
	}

	if (b.getNum() != 0)
	{
	    next = new Rational( b.getDen(), b.getNum() );
	    if (!myContains(used, next))
	    {
		used.add( next );
		values.add( computeStringValue( next ) );
	    }
	}
	return values;
    }// generateIncorrectBValues method

    static private Q pickEqQuestion(int num_eq)
    {
	Q q = null;
	String prompt;
	ArrayList<String> wrongEQ = generateWrongEQ(num_eq);
	int n = wrongEQ.size();
	int r;   // no MS or FB question in this case: 0 -> T/F,  1 -> MC
	    
	if (n<2)  r = 0;
	else      r = rand.nextInt(2);

	switch (r) 
	{
	case 0:  // T/F question
	    if (num_eq==1)
		prompt = "The equation obtained by plugging in the "
		    + "coordinates of the first point is: ";
	    else
		prompt = "The equation obtained by plugging in the "
		    + "coordinates of the second point is: ";

	    if (rand.nextBoolean())
		q = new Q(Q.TF,"EQ", prompt + generateCorrectEQ(num_eq), 
			      "true", Arrays.asList( 3 ) );
	    else
		q = new Q(Q.TF,"EQ",
			  prompt + (String)wrongEQ.get(rand.nextInt(n)), 
			  "false",  Arrays.asList( 3 ) );
	    break;

	case 1: // MC question
	    
	    String tOpt = generateCorrectEQ(num_eq);
	    int k;
	    if (n<4) k = n;
	    else k = 2 + rand.nextInt(n-1);
	    String[] fOpt = pickOptions( k, wrongEQ );

	    if (num_eq==1)
		prompt = "Which equation is obtained by plugging in "
		    + "the coordinates of the first point?"; 
		else
		    prompt = "Which equation is obtained by plugging in "
			+ "the coordinates of the second point?"; 
	    q = new Q(Q.MC,"EQ", prompt, "", Arrays.asList( 3 ) );
	    
	    // shuffle the options
	    ArrayList<S> l = new ArrayList<S>( 6 ); 
	    l.add( new TS( tOpt ));
	    for(int i=0; i<fOpt.length; i++)
		l.add( new FS( fOpt[i] ));
	    for(int i=0; i<10; i++)
		java.util.Collections.shuffle( l );
	    addOptions(q,l);
		
	    break;
		
	}// switch statement

	return q;
    }//pickEqQuestion method

    static private Q pickPointQuestion()
    {
	Q q = null;
	String prompt;

	int type = 1 + rand.nextInt(4);

	//type = 2;
	int n_on = onLine.size();
	int n_off = offLine.size();
	int k, n;

	if (n_on==0) 
	    type = Q.TF;

	//System.out.println( n_on + " " + n_off);
	switch(type) {
	    
	case Q.TF: // ****************** TF question *************************

	    n = 1 + rand.nextInt(3);
	    if (n>onLine.size()) n = onLine.size();

	    if (rand.nextBoolean())
	    {   
		// ------ correct answer is 'true' ------

		if (rand.nextBoolean())
		{    // point(s) is(are) on the line
		    String pointsStr = string( pickOptions(n,onLine) );
		    if (n==1)
			prompt = "The point " + pointsStr + " is on the line.";
		    else if (n==2)
			prompt = "The points " + pointsStr + 
			    " are BOTH on the line.";
		    else
			prompt = "The points " + pointsStr + 
			    " are ALL on the line.";
		    q = new Q(Q.TF,"POINT", prompt, "true", Arrays.asList(1));
		}
		else
		{   // point(s) is(are) NOT on the line
		    String pointsStr = string( pickOptions(n,offLine) );
		    if (n==1)
			prompt = "The point " + pointsStr + 
			    " is NOT on the line.";
		    else if (n==2)
			prompt = "NEITHER of the points " + pointsStr + 
			    " is on the line.";
		    else
			prompt = "NONE of the points " + pointsStr + 
			    " are on the line.";
		    q = new Q(Q.TF,"POINT", prompt, "true", Arrays.asList(1));
		}
	    }
	    else
	    {   
		// ------ correct answer is 'false' ------

		if (rand.nextBoolean())
		{    // point(s) is(are) on the line
		    String pointsStr = string( pickOptions(n,onLine) );
		    if (n==1)
			prompt = "The point " + pointsStr + " is NOT on the line.";
		    else if (n==2)
			prompt = "NEITHER of the points " + pointsStr + 
			    " is on the line.";
		    else
			prompt = "NONE of the points " + pointsStr + 
			    " are on the line.";
		    q = new Q(Q.TF,"POINT", prompt, "false", Arrays.asList(1));
		}
		else
		{   // point(s) is(are) NOT on the line
		    String pointsStr = string( pickOptions(n,offLine) );
		    if (n==1)
			prompt = "The point " + pointsStr + 
			    " is on the line.";
		    else if (n==2)
			prompt = "Both points " + pointsStr + 
			    " are on the line.";
		    else
			prompt = "The points " + pointsStr + 
			    " are ALL on the line.";
		    q = new Q(Q.TF,"POINT", prompt, "false", Arrays.asList(1));
		}

	    }
	    break;

	case Q.MC: // ****************** MC question *************************

	    if (rand.nextBoolean())  // correct answer is a point on the line
	    {
		Point on = (pickOptions(1,onLine))[0];
		Point[] off = pickOptions(2+rand.nextInt(3),offLine);
		q = new Q(Q.MC,"POINT",
			  "Which ONE of the following points is on the line?",
			  "", Arrays.asList( 3 ) );

		// shuffle the options
		ArrayList<S> l = new ArrayList<S>( 6 ); 
		l.add( new TS( string(on) ));
		for(int i=0; i<off.length; i++)
		    l.add( new FS( string(off[i])) );
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions(q,l);

	    }
	    else   // correct answer is a point OFF the line
	    {
		Point[] on = pickOptions(Math.min(n_on,2+rand.nextInt(3)),
					 onLine);
		Point off = (pickOptions(1,offLine))[0];
		q = new Q(Q.MC,"POINT",
			  "Which ONE of the following points is NOT on the line?",
			  "", Arrays.asList( 3 ) );

		// shuffle the options
		ArrayList<S> l = new ArrayList<S>( 6 ); 
		l.add( new TS( string(off) ));
		for(int i=0; i<on.length; i++)
		    l.add( new FS( string(on[i])) );
		for(int i=0; i<10; i++)		    
		    java.util.Collections.shuffle( l );
		addOptions(q,l);
	    }
	    break;

	case Q.MS: // ****************** MS question *************************

	    if (rand.nextBoolean())  // correct answer is points on the line
	    {
		k = Math.min(n_on,rand.nextInt(4));
		n = Math.min(n_off,1+rand.nextInt(3));
		//System.out.println( k + " " + n);
		Point[] on = pickOptions(k,onLine);
		Point[] off = pickOptions(n,offLine);
		q = new Q(Q.MS,"POINT",
			  "Select ALL the points that ARE on the line, if any.",
			  "", Arrays.asList( 3 ) );

		// shuffle the options
		ArrayList<S> l = new ArrayList<S>( 6 ); 
		for(int i=0; i<off.length; i++)
		    l.add( new FS( string(off[i])) );
		for(int i=0; i<on.length; i++)
		    l.add( new TS( string(on[i])) );
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions(q,l);

	    }
	    else   // correct answer is points OFF the line
	    {
		n = Math.min(n_on,rand.nextInt(4));
		k = 2+Math.min(n_off,rand.nextInt(2));
		//System.out.println( k + " " + n);
		Point[] on = pickOptions(n,onLine);
		Point[] off = pickOptions(k,offLine);
		q = new Q(Q.MS,"POINT",
			  "Select ALL the points that are NOT on the line, if any.",
			  "", Arrays.asList( 3 ) );

		// shuffle the options
		ArrayList<S> l = new ArrayList<S>( 6 ); 
		for(int i=0; i<on.length; i++)
		    l.add( new FS( string(on[i])) );
		for(int i=0; i<off.length; i++)
		    l.add( new TS( string(off[i])) );
		for(int i=0; i<10; i++)		    
		    java.util.Collections.shuffle( l );
		addOptions(q,l);

	    }
	    break;

	case Q.FB: // ****************** FB question *************************
	    
	    Point p = onLine.get( rand.nextInt( onLine.size() ) );
	    int x = (int) p.getX();
	    int y = (int) p.getY();

	    if (rand.nextBoolean())
	    {
		prompt = "What is the y-coordinate of the point lying on the " +
		    " line and whose x-coordinate is " + x + "?";

		q = new Q(Q.FB,"POINT", prompt, y+"", Arrays.asList( 1 ) );


	    }
	    else
	    {
		prompt = "What is the x-coordinate of the point lying on the " +
		    " line and whose y-coordinate is " + y + "?";
		q = new Q(Q.FB,"POINT", prompt, x+"", Arrays.asList( 1 ) );
	    }
	    break;
	}//switch on type of question
	return q;
    }// pickPointQuestion method

    static String string( Point p )
    {
	return "(" + (int)p.getX() + "," + (int)p.getY() + ")";
    }

    // pts.length = 1, 2, or 3
    static String string( Point[] pts )
    {
	String s = string(pts[0]);
	if (pts.length==2)
	    return s + " and " + string( pts[1] );
	else if (pts.length==3)
	    return s + ", "  + string(pts[1]) + ", and " + string( pts[2] );
	else 
	    return s;
    }

    static String generateCorrectEQ(int eq)
    {
	String Xpart, Ypart;
	if (eq==1)
	{

	    if (x1==0)     Xpart = "x";
	    else if (x1<0) Xpart = "(x + " + (-x1) + ")"; 
	    else           Xpart = "(x - " + x1 + ")"; 

	    if (y1==0)     Ypart = "y";
	    else if (y1<0) Ypart = "y + " + (-y1);
	    else           Ypart = "y - " + y1;

	    return Ypart + " = m" + Xpart;
	}
	else
	{
	    if (x2==0)     Xpart = "x";
	    else if (x2<0) Xpart = "(x + " + (-x2) + ")"; 
	    else           Xpart = "(x - " + x2 + ")"; 

	    if (y2==0)     Ypart = "y";
	    else if (y2<0) Ypart = "y + " + (-y2);
	    else           Ypart = "y - " + y2;

	    return Ypart + " = m" + Xpart;
	}
    }// generatCorrectEQ method

    static String generateEQ(int x, int y)
    {
	String Xpart, Ypart;

	if (x==0)     Xpart = "x";
	else if (x<0) Xpart = "(x + " + (-x) + ")"; 
	else          Xpart = "(x - " + x + ")"; 

	if (y==0)     Ypart = "y";
	else if (y<0) Ypart = "y + " + (-y);
	else          Ypart = "y - " + y;

	return Ypart + " = m" + Xpart;
    }

    static Rational computeIntercept(int x1, int y1)
    {
	return (new Rational(y1)).sub( m.mult( new Rational(x1) ) );
    }


    static boolean myContains(ArrayList<Rational> a, Rational r)
    {
	Iterator<Rational> it = a.iterator();;
	while ( it.hasNext() )
	    if (r.equals( it.next() )) return true;

	return false;

    }

    static ArrayList<String> generateWrongEQ(int num_eq)
    {
	ArrayList<String> eq = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);

	if (num_eq==1)
	{
	    //x1 = y1 = 1;
	    //x2 = 2;
	    //y2 = 2;
	    //m = new Rational( y2-y1, x2-x1);
	    //String minusX1 = x1<0 ? " + " + (-x1) : " - " + x1; 
	    //String minusY1 = y1<0 ? " + " + (-y1) : " - " + y1; 
	    //String plusX1 = x1<0 ? " - " + (-x1) : " + " + x1; 
	    //String plusY1 = y1<0 ? " - " + (-y1) : " + " + y1; 
	    Rational intercept = computeIntercept( x1, y1 );
	    Rational next;
	    
	    used.add( intercept );

	    next = computeIntercept( -x1, y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x1,y1) );
	    }


	    next = computeIntercept( -x1, -y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x1,-y1) );
	    }

	    
	    next = computeIntercept( -x1, x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x1,x1) );
	    }


	    next = computeIntercept( -x1, -x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x1,-x1) );
	    }
	    
	    
	    next = computeIntercept( y1, y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y1,y1) );
	    }


	    next = computeIntercept( y1, -y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y1,-y1) );
	    }


	    next = computeIntercept( y1, x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y1,x1) );
	    }


	    next = computeIntercept( y1, -x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y1,-x1) );
	    }


	    next = computeIntercept( -y1, y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y1,y1) );
	    }


	    next = computeIntercept( -y1, -y1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y1,-y1) );
	    }


	    next = computeIntercept( -y1, x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y1,x1) );
	    }


	    next = computeIntercept( -y1, -x1);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y1,-x1) );
	    }
	}

	else
	{    // second equation
	    Rational intercept = computeIntercept( x2, y2 );
	    Rational next;
	    
	    used.add( intercept );

	    next = computeIntercept( -x2, y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x2,y2) );
	    }


	    next = computeIntercept( -x2, -y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x2,-y2) );
	    }

	    
	    next = computeIntercept( -x2, x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x2,x2) );
	    }


	    next = computeIntercept( -x2, -x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-x2,-x2) );
	    }
	    
	    
	    next = computeIntercept( y2, y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y2,y2) );
	    }


	    next = computeIntercept( y2, -y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y2,-y2) );
	    }


	    next = computeIntercept( y2, x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y2,x2) );
	    }


	    next = computeIntercept( y2, -x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(y2,-x2) );
	    }


	    next = computeIntercept( -y2, y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y2,y2) );
	    }


	    next = computeIntercept( -y2, -y2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y2,-y2) );
	    }


	    next = computeIntercept( -y2, x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y2,x2) );
	    }


	    next = computeIntercept( -y2, -x2);
	    if (!myContains( used, next)) 
	    {
		used.add( next );
		eq.add( generateEQ(-y2,-x2) );
	    }
	    
	}

	    /*
	      System.out.println( x1 + "," + y1 + "  " + x2 + "," + y2 );
	      Iterator it = eq.iterator();
	      while (it.hasNext())
	      System.out.println( it.next() );
	    */
	    return eq;
	}// generateWrongEQ method

	static String[] pickOptions(int k, Collection<String> c)
	{
	    String[] a = c.toArray( new String[]{""});
	    boolean[] chosen = new boolean[ a.length ];
	    int count = 0;

	    if (k > a.length)
	    {	     
		System.out.println("Error: Requesting too many options: asked to pick " +
				   k + " out of " + a.length + " strings.");
		//throw new RuntimeException( "");
		return new String[] {""};
	    }
	    else
	    {
		while (count<k)
		{
		    int index = rand.nextInt( a.length ); 
		    if (!chosen[index])
		    {
			chosen[index] = true;
			count++;
		    }
		}
		String[] out = new String[k];
		count = 0;
		for(int i=0; i<a.length; i++)
		    if (chosen[i])
			out[count++] = a[i];
		return out;
	    }
	}

    // terrible code duplication
    static Point[] pickOptions(int k, ArrayList<Point> pts)
        {
	    Point[] points = pts.toArray( new Point[]{ new Point(1,1) });
	    boolean[] chosen = new boolean[ points.length ];
	    int count = 0;

	    if (k > points.length)
	    {
		System.out.println("Error: Requesting too many options: asked to pick " +
				   k + " out of " + points.length + " points");
		//throw new RuntimeException( "");
		return new Point[] { new Point(0,0)};
	    }
	    else
	    {
		while (count<k)
		{
		    int index = rand.nextInt( points.length ); 
		    if (!chosen[index])
		    {
			chosen[index] = true;
			count++;
		    }
		}
		Point[] out = new Point[k];
		count = 0;
		for(int i=0; i<points.length; i++)
		    if (chosen[i])
			out[count++] = points[i];
		return out;
	    }
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


    private static Element makeSeq(String narrative, Element[] par)
    {
	Element seq = new Element( "seq" );
	seq.addContent( new Element("narrative").addContent(narrative));
	for(int p=0; p<par.length; p++)
	    seq.addContent( par[p] );
	return seq;
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

    private static void assignValues(int[] values)
    {
	int x1 = rand.nextInt() % 100;
	int y1 = rand.nextInt() % 100;
	int x2 = rand.nextInt() % 100;
	int y2 = rand.nextInt() % 100;

	    while ( // the slope cannot be infinite (this also avoids
		   // generating the same point twice)
		   (x1==x2)      
		
		   // having the two points close enough ensures that we can
		   // find at least one more point on the line for questions
		   // in the x-interval [-200, 200]
		   || (Math.abs(x1-x2)>80)
		   || (Math.abs(y1-y2)>80) )
	    {
		x1 = rand.nextInt() % 100;
		x2 = rand.nextInt() % 100;
		y1 = rand.nextInt() % 100;
		y2 = rand.nextInt() % 100;
	    }

	values[0] = x1;
	values[1] = y1;
	values[2] = x2;
	values[3] = y2;
    }// assignValues method



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


    // add options to the question object
    static void addOptions( Q q, ArrayList<S> l)
    {
	for(int i=0; i<l.size(); i++)
	{
	    S s = l.get(i);
	    if (s instanceof TS)
		q.addOption( ((TS)s).t, true);
	    else
		q.addOption( ((FS)s).t, false); 
	}		
    }

}


    class Q
    {
	public static final int TF = 1;
	public static final int FB = 2;
	public static final int MC = 3;
	public static final int MS = 4;

	public static final int NUM_SNAPS = slopeIntercept.num_snaps;
	public String id;
	public int type;
	public String category;
	public String text;
	public String correct;
	public ArrayList<String> options;
	public ArrayList<Boolean> truth;
	public boolean[] snaps;   // snapshots at which the question may be asked

	Q(int type, String category, String text, String correct, Collection snaps)
	{
	    this.id = "";
	    if ((type<1) || (type>4))
		System.out.println("Invalid question type: " + type);
	    this.type = type;
	    if ((type==MS) || (type==MC))
	    {
		options = new ArrayList<String>();
		truth = new ArrayList<Boolean>();
	    }
	    this.category = category;
	    this.text = text;
	    this.correct = correct;
	    this.snaps = new boolean[ NUM_SNAPS + 1 ];
	    Iterator it = snaps.iterator();
	    while (it.hasNext())
		this.snaps[ (Integer) it.next() ] = true;
	
	}

	void setId(String id)
	{
	    this.id = id;
	}

	void addOption(String option, boolean isCorrect)
	{
	    options.add(option);
	    truth.add( isCorrect );
	}

	boolean fits(int snap)
	{
	    return snaps[snap];
	}

	Element toXML()
	{
	    Element q = new Element( "question");
	    String typeAtt;
	    switch (type) 
	    {
	    case MC: typeAtt = "MCQUESTION"; break;
	    case MS: typeAtt = "MSQUESTION"; break;	
	    case FB: typeAtt = "FIBQUESTION"; break;
	    default: typeAtt = "TFQUESTION";
	    }
	    q.setAttribute("type", typeAtt);
	    q.setAttribute("id", id);
	
	    q.addContent( (new Element( "question_text" )).addContent(text) );

	    Element a;
	    switch (type)	
	    {
	    case MC: case MS:  
		Iterator it1 = options.iterator();
		Iterator it2 = truth.iterator();
		while (it1.hasNext())
		{
		    String v = (String) it1.next();
		    boolean b = (Boolean) it2.next();
		    a = new Element( "answer_option" );
		    a.setAttribute( "is_correct" , (b ? "yes" : "no") );
		    a.addContent( v );
		    q.addContent( a );
		}
		break;

	    case FB:  
		a = new Element( "answer_option" );
		a.addContent( correct );
		q.addContent( a );
		break;
	    default:
		a = new Element( "answer_option" );
		a.setAttribute( "is_correct" , "yes" );
		a.addContent( correct );
		q.addContent( a );
		break;
	    }
	    return q;
	}

    }

    class Pair
    {
	private ArrayList<Point> onLine;
	private ArrayList<Point> offLine;

	Pair( ArrayList<Point> on, ArrayList<Point> off )
	{
	    onLine = on;
	    offLine = off;
	}

	public ArrayList<Point> getOnLine()  { return onLine;  }
	public ArrayList<Point> getOffLine() { return offLine; } 
    }
