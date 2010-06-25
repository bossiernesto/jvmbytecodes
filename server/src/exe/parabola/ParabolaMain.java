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

package exe.parabola;

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

import javax.swing.JPanel;
import java.awt.Point;

public class ParabolaMain
{
    static abstract class S{}
    static class TS extends S { String t; TS(String t) { this.t=t; } }
    static class FS extends S { String t; FS(String t) { this.t=t; } }
    private static Collection questions1 = new ArrayList<Q>(100);
    public static int num_snaps;

    public static String  ftName = "Lucida Bright";
    private static Random rand = new Random();

    private static int SMALL_SKIP = 2;
    private static int MED_SKIP = 6;
    private static int BIG_SKIP = 10;
    private static int HUGE_SKIP = 20;
    private static int BASELINE = 300;
    private static int LEFT= -40;

    private  static String pseudocodeURL1;
    private  static String pseudocodeURL2;    
    private  static int a, b, c;
    private  static Rational minusBover2A;
    private  static Rational yvertex;
    private  static int disc;
    private  static boolean discIsSquare;
    private  static Rational x1, x2;
    private  static double x1d, x2d;

    private static java.util.Hashtable<String,Visual> ht;
    private static Element initial;
    private static Element animation;



    private static FontMetrics reg_fm, sup_fm;
    private static int reg_size, sup_size;

    public static void main(String[] args)
    {
	String filename = args[0];
	int[] values = new int[3];

	if (args.length == 4)
        {
	    for(int i=0; i<3; i++)
		values[i] = Integer.parseInt( args[i+1] );
	}
	else
	    assignValues(values);

	a = values[0];
	b = values[1];
	c = values[2];

	num_snaps = 7;
	createScript(filename);

    }// main method


    /*********************************************************
     * generate random values for the coefficients a, b, and c
     *********************************************************/

     private static void assignValues(int[] values)
     {
	 Random rand = new Random();
	 int a = rand.nextInt( 9 ) + 1; // a cannot be zero
	 int b = rand.nextInt( 10 ); 
	 int c = rand.nextInt( 10 );

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

	 //a=-1; b=1; c=0;
	 
	 values[0] = a;
	 values[1] = b;
	 values[2] = c;

	 minusBover2A = new Rational( -b, 2*a);
	 System.out.println("\ta=" + a +"\tb=" + b + "\tc=" + c);
     }// assignValues method


    private static String s(int n)
    {
	return (n>=0 ? "+" : "-");
    }

    private static void createScript(String filename)
    {
	pseudocodeURL1 =  "parabola.php?;amp;line=";
	pseudocodeURL2 = 
	    ";amp;aval=" + a + ";amp;bval=" + b + 
	    ";amp;cval=" + c;


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
         * first snapshot AKA the "initial" element
         *******************************************/

	Text t;
	Line l;
	int x = LEFT + 300;
	int line1 = reg_size;
	int line2 = (int)(1.2*reg_size);
	int line3 = line2 + (int)(1.2*reg_size);
	int line4 = line3 + (int)(1.2*reg_size);
	int line5 = line4 + (int)(1.5*reg_size);
	int line6 = line5 + (int)(1.5*reg_size);
	int line7 = line6 + (int)(2.0*reg_size);
	int line8 = line7 + (int)(1.5*reg_size);
	int line9 = line8 + (int)(1.8*reg_size);
	int line10 = line9 + (int)(1.3*reg_size);
	int line11 = line10 + (int)(1.7*reg_size);
	int line12 = line11 + (int)(2.3*reg_size);
	int line13 = line12 + (int)(2.3*reg_size);
	int line14 = line13 + (int)(1.2*reg_size);

	int wp = reg_fm.stringWidth("+");
	// first line: equation
	addText( "f(x)","y = f(x) = ", x, line1, reg_size, reg_fm, 
		 "blue",false);
	x = right("f(x)") + BIG_SKIP;
	x = addTerm("a", a,2, x, line1, false);
	if (b!=0)
	{
	    addText( "signb", s(b), x+MED_SKIP,line1,reg_size, reg_fm, 
		 "blue",false);
	    x = addTerm("b",Math.abs(b),1,x+wp+MED_SKIP,line1, false);
	}
	if (c!=0)
	{
	    addText( "signc", s(c), x+MED_SKIP,line1,reg_size, reg_fm, 
		     "blue",false);
	    x = addTerm("c",Math.abs(c),0,x+wp+MED_SKIP,line1, false);
	}
	

	// second step (curvature): line2 and line3
	String text2, text3, text4;
	if (a>0)
	{
	    text2 = "1. Since a>0, the parabola is";
	    text3 = "convex/points up, and the";
	    text4 = "vertex is a global minimum";
	}
	else
	{
	    text2 = "1. Since a<0, the parabola is";
	    text3 = "concave/points down, and";
	    text4 = "vertex is a global maximum";
	}
	addText( "text2",text2, LEFT,line2,reg_size, reg_fm, "red",true);
	addText( "text3",text3, LEFT+20,line3,reg_size, reg_fm, "red",true);
	addText( "text4",text4, LEFT+20,line4,reg_size, reg_fm, "red",true);

	// third step (x-coord of vertex): line5 and line6
	String x_vertex = "2. x-coordinate of vertex:";
	int xbox[] = new int[5];
	int ybox[] = new int[5];
	xbox[0] = xbox[1] = xbox[4] = 140;
	xbox[2] = xbox[3] = 210;
	ybox[0] = ybox[3] = ybox[4] = line5+4;
	ybox[1] = ybox[2] = line5 - (int)(0.8*reg_size);
	addPolyline("vbox",xbox,ybox,"black",true, "0", "0", "255");
	addText( "x_vertex",x_vertex, LEFT,line5,reg_size, reg_fm, "red",true);

	int wb = reg_fm.stringWidth("b");	
	int w2a = reg_fm.stringWidth("2a");
	int w2 = reg_fm.stringWidth("2");
	addText( "minus1","-", LEFT+20,line6,reg_size, reg_fm, "red",true);
	x = right("minus1") + SMALL_SKIP;
	addText( "stringb","b", x+(w2a-wb)/2,
		 line6-(int)(0.4*reg_size),reg_size, reg_fm, "red",true);
	addLine("div1", x, line6 - (int) (0.3*reg_size),
		x+w2a, line6 - (int) (0.3*reg_size), "red", true);
	addText( "2a","2a", x,
		 line6+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);
	x = right("div1") + MED_SKIP;
	addText( "eq1","=", x,line6,reg_size, reg_fm, "red",true);
	x = right("eq1") + SMALL_SKIP;
	addText( "minus2","-", x,line6,reg_size, reg_fm, "red",true);
	x = right("minus2") + SMALL_SKIP;

	int wbval = b==0 ? reg_fm.stringWidth("0") : width("bcopy1");
	int w2aval = reg_fm.stringWidth("2()") + width("acopy1");
	addLine("div2", x, line6 - (int) (0.3*reg_size),
		x+w2aval, line6 - (int) (0.3*reg_size), "red", true);

	// value of b in b/2a
	x += (w2aval-wbval)/2;
	int dx_b1 = 0;
	int dy_1 = 0;
	if (b!=0) 
	{
	    dx_b1 = x - left("bcopy1");
	    dy_1 = line6-(int)(0.4*reg_size) - line1;
	}
	else
	    addText("b0","0",x,line6-(int)(0.4*reg_size),reg_size, reg_fm, 
		    "red",true);

	// value of 2a in b/2a

	x = left( "div2" );
	addText("2(","2(",x,line6+(int)(0.6*reg_size),reg_size, reg_fm, 
		"red",true);
	addText(")",")",x+width("2(")+width("acopy1"),line6+(int)(0.6*reg_size),
		reg_size, reg_fm, "red",true);
	int dx_a1 = right("2(") - left("acopy1");
	int dy_2 = line6+(int)(0.6*reg_size) - line1;

	x = right("div2") + MED_SKIP;
	addText( "eq2","=", x,line6,reg_size, reg_fm, "black",true);


	// value of -b/2a
	x = right("eq2") + MED_SKIP;
	if (minusBover2A.isInteger())
	{
	    addText("minusBover2A",minusBover2A.getNum()+"",x,
		    line6, reg_size, reg_fm, "red", true);
	    addText("minusBover2Acopy1",minusBover2A.getNum()+"",x,
		    line6, reg_size, reg_fm, "red", true);
	    addText("minusBover2Acopy2",minusBover2A.getNum()+"",x,
		    line6, reg_size, reg_fm, "red", true);
	    addText("minusBover2Acopy3",minusBover2A.getNum()+"",x,
		    line6, reg_size, reg_fm, "red", true);
	    addText("minusBover2Acopy4",minusBover2A.getNum()+"",x,
		    line6, reg_size, reg_fm, "red", true);
	}
	else
	{
	    addRational("minusBover2A",minusBover2A.getNum(),
			minusBover2A.getDen(),x,line6-(int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	    addRational("minusBover2Acopy1",minusBover2A.getNum(),
			minusBover2A.getDen(),x,line6-(int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	    addRational("minusBover2Acopy2",minusBover2A.getNum(),
			minusBover2A.getDen(),x,line6-(int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	    addRational("minusBover2Acopy3",minusBover2A.getNum(),
			minusBover2A.getDen(),x,line6-(int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	    addRational("minusBover2Acopy4",minusBover2A.getNum(),
			minusBover2A.getDen(),x,line6-(int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	}

	// fourth step (y-coord of vertex): line7 and line8
	String y_vertex = "3. y-coordinate of vertex:";
	addText( "y_vertex",y_vertex, LEFT,line7,reg_size, reg_fm, "red",true);

	addText( "f(", "f(", LEFT+20, line8, reg_size, reg_fm, "red",true);
	x = right( "f(" );
	addText( "minus3","-", x,line8,reg_size, reg_fm, "red",true);
	x = right("minus3") + SMALL_SKIP;
	addText( "stringb2","b", x+(w2a-wb)/2,
		 line8-(int)(0.4*reg_size),reg_size, reg_fm, "red",true);
	addLine("div3", x, line8 - (int) (0.3*reg_size),
		x+w2a, line8 - (int) (0.3*reg_size), "red", true);
	addText( "2a2","2a", x,
		 line8+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);
	x = right("div3") + SMALL_SKIP;
	addText( "eq3",") =", x,line8,reg_size, reg_fm, "red",true);
	x = right("eq3") + SMALL_SKIP;

	// value of a in ax^2 + bx + c
	int dx_a2 = 0;
	int dy_3 = 0;
	dx_a2 = x+reg_fm.stringWidth("(") - left("acopy2");
	dy_3 = line8 - line1;
	ht.get( "acopy2" ).translate(dx_a2, dy_3);

	addText("(a","(", right("acopy2"), line8,reg_size, reg_fm, "red",true);
	addText(")a",")", right("(a")+width("minusBover2A"), 
		line8,reg_size, reg_fm, "red",true);

	// copy of -b/2a to move down
	int dx_b2a = right("(a") - left("minusBover2A");
	int dy_4 = line8 - line6;

	x = right(")a");
	addText("sq","2",x, line8-(int)(0.35*reg_size),sup_size,
		sup_fm, "red",true);

	x = right("sq") + SMALL_SKIP;

	// value of b in ax^2 + bx + c
	int dx_b2 = 0;
	if (b>0)
	{
	    addText("+b","+",x,line8,reg_size, reg_fm, "red",true);
	    dx_b2 = right("+b") + SMALL_SKIP - left( "bcopy2" );
	}
	else if (b!=0)
	    dx_b2 = right("sq") + SMALL_SKIP - left( "bcopy2" );

	if (b!=0)
	    ht.get("bcopy2").translate(dx_b2,dy_3);

	int dx_b2a2 = 0;
	if (b!=0)
	{
	    addText("(b","(", right("bcopy2"), 
		    line8,reg_size, reg_fm, "red",true);
	    addText(")b",")", right("(b")+width("minusBover2A"), 
		    line8,reg_size, reg_fm, "red",true);
	    // second copy of -b/2a to move down
	    dx_b2a2 = right("(b") - left("minusBover2A");
	    x = right(")b");
	}
	
	// value of c in ax^2 + bx + c
	int dx_c = 0;
	if (c>0)
	{
	    addText("+c","+",x,line8,reg_size, reg_fm, "red",true);
	    dx_c = right("+c") + SMALL_SKIP - left( "ccopy2" );
	}
	else if (c!=0)
	    dx_c = x + SMALL_SKIP - left( "ccopy2" );

	if (c!=0)
	    ht.get("ccopy2").translate(dx_c,dy_3);

	// value of y-coord of vertex
	Rational r = minusBover2A;
	yvertex = (new Rational(a)).mult(r).mult(r).add(
		(new Rational(b)).mult(r)).add( new Rational(c) );

	if (yvertex.isInteger())
	{
	    addText("yvertex",yvertex.getNum()+"",right("eq3") + MED_SKIP,
		    line8, reg_size, reg_fm, "red", true);
	}
	else
	{
	    addRational("yvertex",yvertex.getNum(),yvertex.getDen(),
			right("eq3") + MED_SKIP, line8 - (int)(0.3*reg_size), 
			reg_size, reg_fm, "red", true);
	}

	// fifth step (x-intercepts): line9-line12
	addText("since","4. Since b",LEFT,line9,reg_size, reg_fm, "red", true);
	addText("disc_sq","2",right("since"), line9-(int)(0.35*reg_size),
		sup_size, sup_fm, "red",true);
	addText("4ac","-4ac =",right("disc_sq"),line9,
		reg_size, reg_fm, "red", true);
	addText("disc_(1","(",right("4ac")+MED_SKIP,line9,
		reg_size, reg_fm, "red", true);
	int dx_b3 = 0;
	int dy_5 = line9 - line1;

	if (b!=0)
	{
	    dx_b3 = right("disc_(1") - left("bcopy3");
	    ht.get("bcopy3").translate(dx_b3,dy_5);
	    addText("disc_)1",")",right("bcopy3"),line9,
		    reg_size, reg_fm, "red", true);
	}
	else
	    addText("disc_)1","0)",right("disc_(1"),line9,
		    reg_size, reg_fm, "red", true);

	addText("disc_sq2","2",right("disc_)1"), line9-(int)(0.35*reg_size),
		sup_size, sup_fm, "red",true);
	addText("disc_minus"," -4(",right("disc_sq2"),line9,
		reg_size, reg_fm, "red", true);
	int dx_a3 = right("disc_minus") - MED_SKIP - left("acopy3");
	ht.get("acopy3").translate(dx_a3,dy_5);
	addText("disc_)2",")(",right("acopy3"),line9,
		reg_size, reg_fm, "red", true);

	int dx_c3 = 0;

	if (c!=0)
	{
	    dx_c3 = right("disc_)2") - left("ccopy3");
	    ht.get("ccopy3").translate(dx_c3,dy_5);
	    addText("disc_)3",")",right("ccopy3"),line9,
		    reg_size, reg_fm, "red", true);
	}
	else
	    addText("disc_)3","0)",right("disc_)2"),line9,
		    reg_size, reg_fm, "red", true);

	disc = b*b - 4*a*c;

	addText("disc",disc+"", right("4ac")+MED_SKIP, line9,
		 reg_size, reg_fm, "red",true);
	addText("disc_copy1",disc+"", right("4ac")+MED_SKIP, line9,
		 reg_size, reg_fm, "red",true);
	addText("disc_copy2",disc+"", right("4ac")+MED_SKIP, line9,
		 reg_size, reg_fm, "red",true);

	if (disc!=0)

	    if (disc>0)
	    	addText(">0","> 0", right("disc")+MED_SKIP, line9,
			reg_size, reg_fm, "red",true);
	    else
	    	addText("<0","< 0", right("disc")+MED_SKIP, line9,
			reg_size, reg_fm, "red",true);
	String xtext1="", xtext2="";
	if (disc==0)
	{
	    xtext1 = "the vertex is the";
	    xtext2 = "only x-intercept";
	}
	else if (disc<0)
	{
	    xtext1 = "there are no";
	    xtext2 = "x-intercepts";
	}
	else
	{
	    xtext1 = "the x-intercepts are";
	}

	if (disc>0)
	{
	    xbox[0] = xbox[1] = xbox[4] = 13;
	    xbox[2] = xbox[3] = 142;
	    ybox[0] = ybox[3] = ybox[4] = line10+5;
	    ybox[1] = ybox[2] = line10 - (int)(0.8*reg_size);
	    addPolyline("xbox",xbox,ybox,"black",true, "255", "0", "0");
	}
	addText("xtext1",xtext1, LEFT+20, line10,reg_size, reg_fm, "red",true);
	if (disc<=0)
	{
	    addText("xtext2",xtext2, LEFT+20, line11+-(int)(0.4*reg_size),reg_size, reg_fm, "red",true);

	}

	int dx_x1_b2a = 0;
	int dx_x1_disc = 0;
	int dx_x1_a = 0;
	int dy_6a = 0;
	int dy_6b = 0;
	int dy_6c = 0;

	int dx_x2_b2a = 0;
	int dx_x2_disc = 0;
	int dx_x2_a = 0;
	int dy_7a = 0;
	int dy_7b = 0;
	int dy_7c = 0;

	// superpose on vertex by default for drawing
	x1 = x2 = minusBover2A;

	if (disc<=0)
	{
	    line14 = line13-(int)(1.5*reg_size);
	    line13 = line12-(int)(1.0*reg_size);

	}
	else  // disc > 0
	{ 
	    // x_1 = ...
	    addText("x1","x", LEFT+20, line11,reg_size, reg_fm, "red",true);
	    addText("1","1", right("x1"), line11+(int)(0.3*reg_size),
		    sup_size, sup_fm, "red",true);
	    addText("=1","=", right("1"), line11,reg_size, reg_fm, "red",true);

	    addText( "x1_minus1","-", right("=1"),line11,
		     reg_size, reg_fm, "red",true);
	    x = right("x1_minus1") + SMALL_SKIP;
	    addText( "x1_b","b", x+(w2a-wb)/2,
		     line11-(int)(0.4*reg_size),reg_size, reg_fm, "red",true);
	    addLine("x1_div1", x, line11 - (int) (0.3*reg_size),
		    x+w2a, line11 - (int) (0.3*reg_size), "red", true);
	    addText( "x1_2a1","2a", x,
		     line11+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);
	    addText( "x1_minus2","-", right("x1_div1")+SMALL_SKIP,line11,
		     reg_size, reg_fm, "red",true);

	    addText( "x1_b2","b", 
		     right("x1_minus2")+(int)(1.1*reg_size),
		     line11-(int)(0.4*reg_size),
		     reg_size, reg_fm, "red",true);

	    addText( "x1_SQ","2", right("x1_b2"),line11-(int)(0.7*reg_size),
		     sup_size, sup_fm, "red",true);
	    addText( "x1_4ac","-4ac", 
		     right("x1_SQ"), line11-(int)(0.4*reg_size),
		     reg_size, reg_fm, "red",true);

	    int xsq[] = new int[4];
	    int ysq[] = new int[4];
	    xsq[0] = right("x1_minus2")+(int)(0.2*reg_size);
	    ysq[0] = line11 - (int)(0.9*reg_size);
	    xsq[1] = xsq[0] + (int)(0.3*reg_size);
	    ysq[1] = line11 - (int)(0.4*reg_size);
	    xsq[2] = xsq[1] + (int)(0.4*reg_size);
	    ysq[2] = line11 - (int)(1.3*reg_size);
	    xsq[3] = right("x1_4ac");
	    ysq[3] = ysq[2];
	    addPolyline("x1_SQRT", xsq,ysq, "red", true );

	    addLine("x1_div2", left("x1_SQRT"), line11 - (int) (0.3*reg_size),
		    right("x1_4ac"), line11 - (int) (0.3*reg_size), 
		    "red", true);
	    x = left("x1_SQRT")+ (width("x1_SQRT")-w2a)/2;
	    addText( "x1_2a2","2a", x,
		     line11+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);


	    dy_6a = line11 - line6;
	    dy_6b = line11 - line9 - (int)(0.4*reg_size);
	    dy_6c = line11 + (int)(0.6*reg_size) - line1;
	    int w = right("x1_div1") - left("x1_minus1");
	    int xto = left("x1_minus1") +(w-width("minusBover2A"))/2;
	    dx_x1_b2a = xto -  left("minusBover2Acopy3");
	    w = right("x1_4ac") - left("x1_b2");
	    xto = left("x1_b2") + (w-width("disc"))/2;
	    dx_x1_disc = xto - left("disc_copy1");
	    ht.get("disc_copy1").translate(dx_x1_disc, dy_6b);
	    dx_x1_a = left("x1_2a2") + w2 - left("acopy5");
	    
	    w = reg_fm.stringWidth( (2*a)+"" );
	    xto = left("disc_copy1") + (width("disc_copy1")-w)/2;
	    addText( "x1_2aval",2*a+"", xto,
		     line11+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);

	    int sqrtDisc = (int) Math.sqrt( disc );
	    if (sqrtDisc*sqrtDisc == disc )
		discIsSquare = true;
	    else
		discIsSquare = false;

	    if (discIsSquare)
	    {
		x1 = minusBover2A.sub( new Rational(sqrtDisc,2*a));

		addText("=1b","=", right("x1_div2")+MED_SKIP, line11,
			    reg_size, reg_fm, "red",true);
		if (x1.isInteger())
		    addText("x1_val",x1.getNum()+"", 
			    right("=1b")+MED_SKIP, line11,
			    reg_size, reg_fm, "red",true);
		else
		    addRational("x1_val",x1.getNum(), x1.getDen(), 
			    right("=1b")+MED_SKIP, line11-(int)(0.3*reg_size), 
			    reg_size, reg_fm, "red",true);
	    }
	    else
	    {
		x1d = minusBover2A.toReal() - Math.sqrt((double)disc)/2/a;
		x2d = minusBover2A.toReal() + Math.sqrt((double)disc)/2/a;
	    }

	    // x_2 = ...
	    addText("x2","x", LEFT+20, line12,reg_size, reg_fm, "red",true);
	    addText("2","2", right("x2"), line12+(int)(0.3*reg_size),
		    sup_size, sup_fm, "red",true);
	    addText("=2","=", right("2"), line12,reg_size, reg_fm, "red",true);
	    addText( "x2_minus1","-", right("=2"),line12,
		     reg_size, reg_fm, "red",true);
	    x = right("x2_minus1") + SMALL_SKIP;
	    addText( "x2_b","b", x+(w2a-wb)/2,
		     line12-(int)(0.4*reg_size),reg_size, reg_fm, "red",true);
	    addLine("x2_div1", x, line12 - (int) (0.3*reg_size),
		    x+w2a, line12 - (int) (0.3*reg_size), "red", true);
	    addText( "x2_2a1","2a", x,
		     line12+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);
	    addText( "x2_plus1","+", right("x2_div1")+SMALL_SKIP,line12,
		     reg_size, reg_fm, "red",true);

	    addText( "x2_b2","b", 
		     right("x2_plus1")+(int)(0.9*reg_size),
		     line12-(int)(0.4*reg_size),
		     reg_size, reg_fm, "red",true);

	    addText( "x2_SQ","2", right("x2_b2"),line12-(int)(0.7*reg_size),
		     sup_size, sup_fm, "red",true);
	    addText( "x2_4ac","-4ac", 
		     right("x2_SQ"), line12-(int)(0.4*reg_size),
		     reg_size, reg_fm, "red",true);


	    xsq[0] = right("x2_plus1");
	    ysq[0] = line12 - (int)(0.9*reg_size);
	    xsq[1] = xsq[0] + (int)(0.3*reg_size);
	    ysq[1] = line12 - (int)(0.4*reg_size);
	    xsq[2] = xsq[1] + (int)(0.4*reg_size);
	    ysq[2] = line12 - (int)(1.3*reg_size);
	    xsq[3] = right("x2_4ac");
	    ysq[3] = ysq[2];
	    addPolyline("x2_SQRT", xsq,ysq, "red", true );

	    addLine("x2_div2", left("x2_SQRT"), line12 - (int) (0.3*reg_size),
		    right("x2_4ac"), line12 - (int) (0.3*reg_size), 
		    "red", true);
	    x = left("x2_SQRT")+ (width("x2_SQRT")-w2a)/2;
	    addText( "x2_2a2","2a", x,
		     line12+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);

	    dy_7a = line12 - line6;
	    dy_7b = line12 - line9 - (int)(0.4*reg_size);
	    dy_7c = line12 + (int)(0.6*reg_size) - line1;
	    w = right("x2_div1") - left("x2_minus1");
	    xto = left("x2_minus1") +(w-width("minusBover2A"))/2;
	    dx_x2_b2a = xto -  left("minusBover2Acopy4");
	    w = right("x2_4ac") - left("x2_b2");
	    xto = left("x2_b2") + (w-width("disc"))/2;
	    dx_x2_disc = xto - left("disc_copy2");
	    ht.get("disc_copy2").translate(dx_x2_disc, dy_7b);
	    dx_x2_a = left("x2_2a2") + w2 - left("acopy6");
	    
	    w = reg_fm.stringWidth( (2*a)+"" );
	    xto = left("disc_copy2") + (width("disc_copy2")-w)/2;
	    addText( "x2_2aval",2*a+"", xto,
		     line12+(int)(0.6*reg_size),reg_size, reg_fm, "red",true);

	    if (discIsSquare)
	    {
		x2 = minusBover2A.add( new Rational(sqrtDisc,2*a));

		addText("=2b","=", right("x2_div2")+MED_SKIP, line12,
			    reg_size, reg_fm, "red",true);
		if (x2.isInteger())
		    addText("x2_val",x2.getNum()+"", 
			    right("=2b")+MED_SKIP, line12,
			    reg_size, reg_fm, "red",true);
		else
		    addRational("x2_val",x2.getNum(), x2.getDen(), 
			    right("=2b")+MED_SKIP, line12-(int)(0.3*reg_size), 
			    reg_size, reg_fm, "red",true);
	    }


	}// disc>0
	// sixth step (y-intercept): line13-line14

	String y_intercept = "5. The y-intercept is:";
	xbox[0] = xbox[1] = xbox[4] = 23;
	xbox[2] = xbox[3] = 140;
	ybox[0] = ybox[3] = ybox[4] = line13+5;
	ybox[1] = ybox[2] = line13 - (int)(0.8*reg_size);
	addPolyline("ybox",xbox,ybox,"black",true, "0", "255", "0");
	addText( "y_intercept",y_intercept, LEFT,line13,
		 reg_size, reg_fm, "red",true);
	addText( "f(0)","f(0) =", LEFT+20,line14,
		 reg_size, reg_fm, "red",true);
	addText( "c2","c", right("f(0)")+MED_SKIP,line14,
		 reg_size, reg_fm, "red",true);
	addText( "eq4","=", right("c2")+MED_SKIP,line14,
		 reg_size, reg_fm, "red",true);
	
	int dx_c2 = 0;
	if (c!=0)
	    dx_c2 = right("eq4") + MED_SKIP - left( "ccopy1" );
	int dy_8 = line14 - line1;

	if (c==0)
	    addText("c0","0", right("eq4")+MED_SKIP, line14,
		 reg_size, reg_fm, "red",true);


	// seventh step: draw the parabola

	// vertex = (vx,vy)
	double vy = yvertex.toReal();

	// y-intercept = (0,c)

	// x-intercepts (if any) = (x1,0) and (x2,0)
        double xmin = Math.min( x1d, x2d );
	double xmax = Math.max( x1d, x2d );
	// at this point: xmin <= vx <= xmax
	xmin = Math.min( xmin, 0.0);
	xmax = Math.max( xmax, 0.0);

	double vx = -1.0 * b/2/a;
	double half_width = Math.max( Math.abs(xmax-vx),
				      Math.abs(xmax-vx) );
	xmin = vx - half_width - 2;
	xmax = vx + half_width + 2;


	double ymin;
	double ymax;

	if (a>0)
	{
	    ymin = Math.min(0, vy);
	    ymax = Math.max(0, 2*c);
	}
	else // a<0
	{
	    ymin = Math.min(0, 2*c);
	    ymax = Math.max(0, vy);
	}

	ymin -= 2;
	ymax += 2;


	addParabola("p", a, b, c, 
		    (int)xmin, (int)xmax, (int)ymin, (int)ymax, 
		    right("text4")+5, line14, 250, (int)(0.95*line14-line2));

	root.addContent( initial );

	/*******************************************
         *  animation 
         *******************************************/

	animation = new Element("animation");

	Element par;
	Element[] pars;
	Element seq = null;

	/*******************************************
         *  snapshot 1: quadratic equation
         *******************************************/
	int d;

	pars = new Element[ 1 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	pars[d++] = makeNoop();
	seq = makeSeq( pseudocodeURL1 + "-1" + pseudocodeURL2, pars,d );
	animation.addContent( seq );

	/*******************************************
         *  snapshot 2: curvature
         *******************************************/
	
	pars = new Element[ 3 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	pars[d++] = show( new String[]{ "text2", "text3", "text4"} );
	pars[d++] = wait(1000);
	pars[d++] = black( new String[]{ "text2", "text3", "text4"} );
	seq = makeSeq( pseudocodeURL1 + "0" + pseudocodeURL2, pars,d);
	animation.addContent( seq );

	/*******************************************
         *  snapshot 3: x-coordinate of vertex
         *******************************************/
	
	pars = new Element[ 50 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	pars[d++] = show( "x_vertex" );
	pars[d++] = wait(1000);
	pars[d++] = black( "x_vertex" );
	pars[d++] = show( new String[]{"minus1","stringb","div1","2a","eq1"} );
	pars[d++] = wait(1000);
	pars[d++] = black( new String[]{"minus1","stringb","div1","2a","eq1"} );
	pars[d++] = show( new String[] {"minus2","div2"} );

	if (b==0)
	{
	    pars[d++] = show("b0");
	    pars[d++] = makeNoop();
	}
	else
	{
	    pars[d++] = show("bcopy1");
	    pars[d++] = move("bcopy1",dx_b1,dy_1);
	}
	pars[d++] = wait(1000);
	pars[d++] = show( new String[] {"acopy1","2(",")"} );
	pars[d++] = move("acopy1",dx_a1,dy_2);
	if (b==0)
	    pars[d++] = black( 
	      new String[]{"minus2","b0","div2","2(","acopy1",")"} );
	else
	    pars[d++] = black( 
	      new String[]{"minus2","bcopy1","div2","2(","acopy1",")"} );
	pars[d++] = show( "eq2" );
	pars[d++] = wait(1000);
	if (minusBover2A.isInteger())
	{
	    pars[d++] = show( "minusBover2A" );
	    pars[d++] = wait(1000);
	    pars[d++] = black( "minusBover2A" );
	}
	else
	{
	    pars[d++] = show( new String[]{ "minusBover2A_num",
	    "minusBover2A_div", "minusBover2A_den" } );
	    pars[d++] = wait(1000);
	    pars[d++] = black( new String[]{ "minusBover2A_num",
	    "minusBover2A_div", "minusBover2A_den" } );
	}
	seq = makeSeq( pseudocodeURL1 + "1" + pseudocodeURL2, pars,d);
	animation.addContent( seq );



	/*******************************************
         *  snapshot 4: y-coordinate of vertex
         *******************************************/
	
	pars = new Element[ 50 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	pars[d++] = show( "y_vertex" );
	pars[d++] = wait(1000);
	pars[d++] = black( "y_vertex" );
	pars[d++] = show( new String[]{
		"f(", "minus3","stringb2","div3","2a2","eq3"} );
	pars[d++] = wait(1000);
	pars[d++] = black( new String[]{
		"f(", "minus3","stringb2","div3","2a2","eq3"} );

	ArrayList<String> terms = new ArrayList<String>( 15 );		
	pars[d++] = show("acopy2");
	pars[d++] = move("acopy2",dx_a2,dy_3);
	terms.add("acopy2");
	terms.add("(a");
	terms.add(")a");
	pars[d++] = show(new String[]{ "(a", ")a" } );

	pars[d++] = wait(1000);
	if (minusBover2A.isInteger())
	{
	    pars[d++] = show("minusBover2Acopy1");
	    pars[d++] = move("minusBover2Acopy1",dx_b2a,dy_4);
	    terms.add("minusBover2Acopy1");
	}
	else
	{
	    pars[d++] = show(new String[]{ "minusBover2Acopy1_num",
		   "minusBover2Acopy1_div", "minusBover2Acopy1_den" });
	    pars[d++] = move(new String[]{ "minusBover2Acopy1_num",
      		   "minusBover2Acopy1_div", "minusBover2Acopy1_den" },
		dx_b2a,dy_4);
	    terms.add("minusBover2Acopy1_num");
	    terms.add("minusBover2Acopy1_div");
	    terms.add("minusBover2Acopy1_den");

	}

	pars[d++] = wait(1000);
	pars[d++] = show("sq");
	terms.add("sq");
	pars[d++] = wait(1000);

	if (b>0)   
	{
	    pars[d++] = show("+b");
	    terms.add("+b");
	}
	if (b!=0)
	{
	    pars[d++] = show("bcopy2");
	    pars[d++] = move("bcopy2",dx_b2,dy_3);
	    terms.add("bcopy2");
	    terms.add("(b");
	    terms.add(")b");
	    pars[d++] = show(new String[]{ "(b", ")b" } );
	    pars[d++] = wait(1000);
	    if (minusBover2A.isInteger())
	    {
		pars[d++] = show("minusBover2Acopy2");
		pars[d++] = move("minusBover2Acopy2",dx_b2a2,dy_4);
		terms.add("minusBover2Acopy2");
	    }
	    else
	    {
		pars[d++] = show(new String[]{ "minusBover2Acopy2_num",
		       "minusBover2Acopy2_div", "minusBover2Acopy2_den" });
		pars[d++] = move(new String[]{ "minusBover2Acopy2_num",
      		   "minusBover2Acopy2_div", "minusBover2Acopy2_den" },
		dx_b2a2,dy_4);
		terms.add("minusBover2Acopy2_num");
		terms.add("minusBover2Acopy2_div");
		terms.add("minusBover2Acopy2_den");

	    }
	}

	pars[d++] = wait(1000);

	if (c>0)   
	{
	    pars[d++] = show("+c");
	    terms.add("+c");
	}

	if (c!=0)
	{
	    pars[d++] = show("ccopy2");
	    pars[d++] = move("ccopy2",dx_c,dy_3);
	    terms.add("ccopy2");

	}

	pars[d++] = wait(1000);
	pars[d++] = hide( terms.toArray( new String[1]) );

	if (yvertex.isInteger())
	{
	    pars[d++] = show( "yvertex" );
	    pars[d++] = wait(1000);
	    pars[d++] = black( "yvertex" );
	}
	else
	{
	    pars[d++] = show( new String[]{ "yvertex_num",
			      "yvertex_div", "yvertex_den" });
	    pars[d++] = wait(1000);
	    pars[d++] = black( new String[]{ "yvertex_num",
			      "yvertex_div", "yvertex_den" });
	}
	seq = makeSeq( pseudocodeURL1 + "2" + pseudocodeURL2, pars,d);
	animation.addContent( seq );

	/*******************************************
         *  snapshot 5: x-intercepts
         *******************************************/
	
	pars = new Element[ 200 ];	    
	d=0; // counter for parallel ops in the snapshot

	pars[d++] = show( new String[]{"since","disc_sq","4ac", "disc_(1"});
	terms = new ArrayList<String>( 15 );		
	terms.add( "disc_(1" );
	pars[d++] = wait(1000);
	if (b!=0)
	{
	    pars[d++] = show("bcopy3");
	    pars[d++] = move( "bcopy3", dx_b3, dy_5);
	    terms.add( "bcopy3" );
	}
	pars[d++] = show(new String[]{"disc_)1","disc_sq2", "disc_minus"});
	terms.add( "disc_)1" );
	terms.add( "disc_sq2" );
	terms.add( "disc_minus" );
	pars[d++] = wait(1000);
	pars[d++] = show("acopy3");
	pars[d++] = move( "acopy3", dx_a3, dy_5);
	terms.add( "acopy3" );
	pars[d++] = show("disc_)2");
	terms.add( "disc_)2" );
	pars[d++] = wait(1000);
	if (c!=0)
	{
	    pars[d++] = show("ccopy3");
	    pars[d++] = move( "ccopy3", dx_c3, dy_5);
	    terms.add( "ccopy3" );
	}
	pars[d++] = show("disc_)3");
	terms.add( "disc_)3" );
	pars[d++] = wait(1000);
	pars[d++] = black( new String[]{ "since", "disc_sq","4ac"});
	pars[d++] = wait(1000);
	pars[d++] = hide( terms.toArray( new String[1]) );
	pars[d++] = show( "disc" );
	pars[d++] = wait(1000);
	if (disc!=0)
	{	
	    if (disc>0)
	    {
		pars[d++] = show( ">0" );
		pars[d++] = wait(1000);
		pars[d++] = black( ">0" );
	    }
	    else
	    {
		pars[d++] = show( "<0" );
		pars[d++] = wait(1000);
		pars[d++] = black( "<0" );
	    }
	}
	pars[d++] = black( "disc" );
	//pars[d++] = wait(1000);
	pars[d++] = show( "xtext1" );
	if (disc<=0)
	{
	    pars[d++] = show( "xtext2" );
	}
	pars[d++] = wait(1000);
	pars[d++] = black( "xtext1");
	if (disc<=0)
	{
	    pars[d++] = black( "xtext2" );
	}
	else
	{
	    // x1 = ...
	    pars[d++] = show( new String[] { "x1","1","=1"} );
	    pars[d++] = show( new String[] { 
		    "x1_minus1","x1_b","x1_div1", "x1_2a1", "x1_minus2",
		    "x1_SQRT", "x1_b2", "x1_SQ", "x1_4ac", "x1_div2",
		    "x1_2a2"} );
	    pars[d++] = wait(1000);
	    pars[d++] = black( new String[] { "x1","1","=1",
		    "x1_minus1","x1_b","x1_div1", "x1_2a1", "x1_minus2",
		    "x1_SQRT", "x1_b2", "x1_SQ", "x1_4ac", "x1_div2",
		    "x1_2a2"} );
	    pars[d++] = wait(1000);
	    pars[d++] = red( new String[] { 
		    "x1_minus1","x1_b","x1_div1", "x1_2a1" } );

	    if (minusBover2A.isInteger())
	    {
		pars[d++] = show( "minusBover2Acopy3" );
		pars[d++] = move("minusBover2Acopy3",dx_x1_b2a,dy_6a);
	    }
	    else
	    {
		pars[d++] = show( new String[]{ "minusBover2Acopy3_num",
			"minusBover2Acopy3_div", "minusBover2Acopy3_den" } );
		pars[d++] = move(new String[]{ "minusBover2Acopy3_num",
			"minusBover2Acopy3_div", "minusBover2Acopy3_den" },
		    dx_x1_b2a,dy_6a);
	    }

	    pars[d++] = hide( new String[] { 
		    "x1_minus1","x1_b","x1_div1", "x1_2a1" } );
	    pars[d++] = wait(1000);
	    if (minusBover2A.isInteger())
		pars[d++] = black( "minusBover2Acopy3" );
	    else
		pars[d++] = black( new String[]{ "minusBover2Acopy3_num",
			"minusBover2Acopy3_div", "minusBover2Acopy3_den" } );

	    pars[d++] = red( new String[] { "x1_b2", "x1_SQ", "x1_4ac" } );
	    pars[d++] = show( "disc_copy1" );
	    pars[d++] = move("disc_copy1",dx_x1_disc,dy_6b);
	    pars[d++] = hide( new String[] { "x1_b2", "x1_SQ", "x1_4ac" });
	    pars[d++] = wait(1000);
	    pars[d++] = black( "disc_copy1" );
	    pars[d++] = show( "acopy5" );
	    pars[d++] = red( new String[]{"x1_2a2","acopy5"});
	    pars[d++] = move("acopy5",dx_x1_a,dy_6c);
	    pars[d++] = hide( new String[]{ "x1_2a2", "acopy5" });
	    pars[d++] = show( "x1_2aval" );
	    pars[d++] = wait(1000);
	    pars[d++] = black( "x1_2aval");

	    if (discIsSquare)
	    {
		pars[d++] = wait(1000);
		if (x1.isInteger())
		{
		    pars[d++] = show( new String[]{ "=1b","x1_val"}  );
		    pars[d++] = wait(1000);
		    pars[d++] = black( new String[]{ "=1b", "x1_val"} );
		}
		else
		{
		    pars[d++] = show( new String[]{ "=1b","x1_val_num",
			    "x1_val_div", "x1_val_den"} );
		    pars[d++] = wait(1000);
		    pars[d++] = black( new String[]{ "=1b", "x1_val_num",
			    "x1_val_div", "x1_val_den"} );
		}
	    }

	    pars[d++] = wait(2000);


	    pars[d++] = show( new String[] { "x2","2","=2"} );
	    pars[d++] = show( new String[] { 
		    "x2_minus1","x2_b","x2_div1", "x2_2a1", "x2_plus1",
		    "x2_SQRT", "x2_b2", "x2_SQ", "x2_4ac", "x2_div2",
		    "x2_2a2"} );

	    pars[d++] = wait(1000);
	    pars[d++] = black( new String[] {  "x2","2","=2",
		    "x2_minus1","x2_b","x2_div1", "x2_2a1", "x2_plus1",
		    "x2_SQRT", "x2_b2", "x2_SQ", "x2_4ac", "x2_div2",
		    "x2_2a2"} );
	    pars[d++] = wait(1000);
	    pars[d++] = red( new String[] { 
		    "x2_minus1","x2_b","x2_div1", "x2_2a1" } );

	    if (minusBover2A.isInteger())
	    {
		pars[d++] = show( "minusBover2Acopy4" );
		pars[d++] = move("minusBover2Acopy4",dx_x2_b2a,dy_7a);
	    }
	    else
	    {
		pars[d++] = show( new String[]{ "minusBover2Acopy4_num",
			"minusBover2Acopy4_div", "minusBover2Acopy4_den" } );
		pars[d++] = move(new String[]{ "minusBover2Acopy4_num",
			"minusBover2Acopy4_div", "minusBover2Acopy4_den" },
		    dx_x2_b2a,dy_7a);
	    }

	    pars[d++] = hide( new String[] { 
		    "x2_minus1","x2_b","x2_div1", "x2_2a1" } );
	    pars[d++] = wait(1000);
	    if (minusBover2A.isInteger())
		pars[d++] = black( "minusBover2Acopy4" );
	    else
		pars[d++] = black( new String[]{ "minusBover2Acopy4_num",
			"minusBover2Acopy4_div", "minusBover2Acopy4_den" } );

	    pars[d++] = red( new String[] { "x2_b2", "x2_SQ", "x2_4ac" } );
	    pars[d++] = show( "disc_copy2" );
	    pars[d++] = move("disc_copy2",dx_x2_disc,dy_7b);
	    pars[d++] = hide( new String[] { "x2_b2", "x2_SQ", "x2_4ac" });
	    pars[d++] = wait(1000);
	    pars[d++] = black( "disc_copy2" );
	    pars[d++] = show( "acopy6" );
	    pars[d++] = red( new String[]{"x2_2a2","acopy6"});
	    pars[d++] = move("acopy6",dx_x2_a,dy_7c);
	    pars[d++] = hide( new String[]{ "x2_2a2", "acopy6" });
	    pars[d++] = show( "x2_2aval" );
	    pars[d++] = wait(1000);
	    pars[d++] = black( "x2_2aval");

	    if (discIsSquare)
	    {
		pars[d++] = wait(1000);
		if (x2.isInteger())
		{
		    pars[d++] = show( new String[]{ "=2b","x2_val"}  );
		    pars[d++] = wait(1000);
		    pars[d++] = black( new String[]{ "=2b", "x2_val"} );
		}
		else
		{
		    pars[d++] = show( new String[]{ "=2b","x2_val_num",
			    "x2_val_div", "x2_val_den"} );
		    pars[d++] = wait(1000);
		    pars[d++] = black( new String[]{ "=2b", "x2_val_num",
			    "x2_val_div", "x2_val_den"} );
		}
	    }

	}
	seq = makeSeq( pseudocodeURL1 + "3" + pseudocodeURL2, pars,d);
	animation.addContent( seq );

	/*******************************************
         *  snapshot 6: y-intercept
         *******************************************/
	
	pars = new Element[ 50 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	pars[d++] = show( "y_intercept" );
	pars[d++] = wait(1000);
	pars[d++] = black( "y_intercept" );
	pars[d++] = show( "f(0)" );
	pars[d++] = wait(1000);
	pars[d++] = black( "f(0)" );
	pars[d++] = show( "c2" );
	pars[d++] = wait(1000);
	pars[d++] = black( "c2" );
	pars[d++] = show( "eq4" );
	pars[d++] = wait(1000);
	pars[d++] = black( "eq4" );

	if (c!=0)
	{
	    pars[d++] = show( "ccopy1" );
	    pars[d++] = move("ccopy1",dx_c2,dy_8);
	    pars[d++] = wait(1000);
	    pars[d++] = black( "ccopy1" );
	}
	else
	{
	    pars[d++] = show( "c0" );
	    pars[d++] = wait(1000);
	    pars[d++] = black( "c0" );
	}
	seq = makeSeq( pseudocodeURL1 + "4" + pseudocodeURL2, pars,d);
	animation.addContent( seq );

	/*******************************************
         *  snapshot 7: draw the parabola
         *******************************************/
	
	pars = new Element[ 2 ];	    
	d=0; // counter for parallel ops in the snapshot
	
	if (disc>0)
	    pars[d++] = show( new String[]{ "p", "vbox", "ybox", "xbox"} );
	else
	    pars[d++] = show( new String[]{ "p", "vbox", "ybox"} );
	seq = makeSeq( pseudocodeURL1 + "5" + pseudocodeURL2, pars,d);
	animation.addContent( seq );


	root.addContent( animation );
	
	root.addContent( createQuestions() );

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


    static Element createQuestions()
    {
	Element questions = new Element( "questions" );

	Q q = pickCurvatureQuestion();
	q.setId( "1" );
	questions.addContent( q.toXML() );

	q = pickXVertexQuestion();
	q.setId( "2" );
	questions.addContent( q.toXML() );

	q = pickYVertexQuestion();
	q.setId( "3" );
	questions.addContent( q.toXML() );

	q = pickXInterceptQuestion();
	q.setId( "4" );
	questions.addContent( q.toXML() );


	q = pickYInterceptQuestion();
	q.setId( "5" );
	questions.addContent( q.toXML() );

	return questions;
    }


    static Q pickCurvatureQuestion()
    {
	ArrayList<String> tOptions = generateTrueStatementsForCurvature();
	ArrayList<String> fOptions = generateFalseStatementsForCurvature();

	int type = 1 + rand.nextInt(3);

	return pickGenericQuestion( type, tOptions, fOptions, null, null  );

    }

    static Q pickXVertexQuestion()
    {
	ArrayList<String> tOptions = generateTrueStatementsForXVertex();
	ArrayList<String> fOptions = generateFalseStatementsForXVertex();
	fOptions.addAll( generateIncorrectXVertexValues() );
	int type = 1 + rand.nextInt(4);
	return pickGenericQuestion( type, tOptions, fOptions,  
		  "What is the value of the x-coordinate of the vertex?",
				    minusBover2A.toString() );
    }

    static Q pickYVertexQuestion()
    {
	ArrayList<String> tOptions = generateTrueStatementsForYVertex();
	ArrayList<String> fOptions = generateFalseStatementsForYVertex();
	fOptions.addAll( generateIncorrectYVertexValues() );
	int type = 1 + rand.nextInt(4);
	return pickGenericQuestion( type, tOptions, fOptions,
		  "What is the value of the y-coordinate of the vertex?",
				    yvertex.toString() );
    }

    static Q pickXInterceptQuestion()
    {
	ArrayList<String> tOptions = generateTrueStatementsForXIntercept();
	ArrayList<String> fOptions = generateFalseStatementsForXIntercept();
	fOptions.addAll( generateIncorrectXInterceptValues() );
	fOptions.addAll( generateIncorrectDiscValues() );
	int type = 1 + rand.nextInt(3);
	return pickGenericQuestion( type, tOptions, fOptions,  null, null);
    }


    static Q pickYInterceptQuestion()
    {
	ArrayList<String> tOptions = generateTrueStatementsForYIntercept();
	ArrayList<String> fOptions = generateFalseStatementsForYIntercept();
	fOptions.addAll( generateIncorrectYInterceptValues() );
	int type = 1 + rand.nextInt(4);
	return pickGenericQuestion( type, tOptions, fOptions, 
		  "What is the value of the y-intercept?", c+"");
    }

    static Q pickGenericQuestion( int type, ArrayList<String> tOptions,
				  ArrayList<String> fOptions, 
				  String prompt, String correct)
    {
	Q q = null;
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
		l.add( new TS( tOpt ));
		for(int i=0; i<fOpt.length; i++)
		    l.add( new FS( fOpt[i] ));
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
		l.add( new TS( fOpt ));
		for(int i=0; i<tOpt.length; i++)
		    l.add( new FS( tOpt[i] ));
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
		int n = 2 + rand.nextInt(4);
		int k = rand.nextInt(n+1);
		String[] tOpt = pickOptions(k,tOptions);
		String[] fOpt = pickOptions(n-k,fOptions);

		// shuffle them
	        ArrayList<S> l = new ArrayList<S>( 5 ); 
		for(int i=0; i<tOpt.length; i++)
		    l.add( new TS( tOpt[i] ));
		for(int i=0; i<fOpt.length; i++)
		    l.add( new FS( fOpt[i] ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions( q, l);

	    }
	    else  // correct answers is(are) false statement(s)
	    {
		q = new Q(Q.MS,"M", "Select ALL the FALSE statements, if any.",
			  "", Arrays.asList( 1 ) );
		
		// pick false and true options
		int n = 2 + rand.nextInt(4);
		int k = rand.nextInt(n+1);
		String[] fOpt = pickOptions(k,fOptions);
		String[] tOpt = pickOptions(n-k,tOptions);

		// shuffle them
	        ArrayList<S> l = new ArrayList<S>( 6 ); 
		for(int i=0; i<fOpt.length; i++)
		    l.add( new TS( fOpt[i] ));
		for(int i=0; i<tOpt.length; i++)
		    l.add( new FS( tOpt[i] ));
		for(int i=0; i<10; i++)
		    java.util.Collections.shuffle( l );
		addOptions( q, l);
	    }
	    break;


	case Q.FB: // ************* FB question ********************
	    q = new Q(Q.FB,"M", prompt + " (your answer may be an integer, e.g., 1 or -2, or a rational number, e.g., 1/2 or -2/3, with no white space)",
		     correct, Arrays.asList( 1 ) );

	    break;

	}// switch statement


	return q;
    }// pickCurvatureQuestion

    static ArrayList<String> generateTrueStatementsForCurvature()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 5 );

	if (a<0)
	{
	    tOptions.add( "The parabola is concave.");
	    tOptions.add( "The graph of the parabola points down.");	    
	    tOptions.add( "The parabola is concave because the value of a in the equation of the parabola is negative.");
	    tOptions.add( "The graph of the parabola points down because the value of a in the equation of the parabola is negative.");
	    tOptions.add( "The parabola is concave, which means that the graph of the parabola points down.");
	}
	else //  a>0
	{
	    tOptions.add( "The parabola is convex.");
	    tOptions.add( "The graph of the parabola points up.");	    
	    tOptions.add( "The parabola is convex because the value of a in the equation of the parabola is positive.");
	    tOptions.add( "The graph of the parabola points up because the value of a in the equation of the parabola is positive.");
	    tOptions.add( "The parabola is convex, which means that the graph of the parabola points up.");
	}


	tOptions.add( "The curvature of any parabola is determined by the sign of the coefficient a in the equation of the parabola.");

	return tOptions;
    }// generateTrueStatementsForCurvature




    static ArrayList<String> generateFalseStatementsForCurvature()
    {
	ArrayList<String> fOptions = new ArrayList<String>( 7 );

	if (a>0)
	{
	    fOptions.add( "The parabola is concave.");
	    fOptions.add( "The graph of the parabola points down.");	    
	    fOptions.add( "The parabola is concave because the value of a in the equation of the parabola is positive.");
	    fOptions.add( "The graph of the parabola points down because the value of a in the equation of the parabola is positive.");
	    fOptions.add( "The parabola is concave, which means that the graph of the parabola points down.");
	    fOptions.add( "The parabola is concave, which means that the graph of the parabola points up.");
	    fOptions.add( "The parabola is convex, which means that the graph of the parabola points down.");
	}
	else //  a<0
	{
	    fOptions.add( "The parabola is convex.");
	    fOptions.add( "The graph of the parabola points up.");	    
	    fOptions.add( "The parabola is convex because the value of a in the equation of the parabola is negative.");
	    fOptions.add( "The graph of the parabola points up because the value of a in the equation of the parabola is negative.");
	    fOptions.add( "The parabola is convex, which means that the graph of the parabola points up.");
	    fOptions.add( "The parabola is convex, which means that the graph of the parabola points down.");
	    fOptions.add( "The parabola is concave, which means that the graph of the parabola points up.");
	}

	fOptions.add( "The curvature of any parabola is determined by the sign of the coefficient b in the equation of the parabola.");

	fOptions.add( "The curvature of any parabola is determined by the sign of the coefficient c in the equation of the parabola.");

	return fOptions;
    }// generateFalseStatementsForCurvature


    static ArrayList<String> generateTrueStatementsForXVertex()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );
	
	if (! minusBover2A.equalsZero())
	    tOptions.add( "The x-coordinate of the vertex is equal to "
			  + minusBover2A);
	
	if (minusBover2A.isInteger())
	{
	    tOptions.add( "The x-coordinate of the vertex is an integer.");
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  (minusBover2A.getNum() - 1) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  (minusBover2A.getNum() - 5) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  (minusBover2A.getNum() + 1) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  (minusBover2A.getNum() + 5) );
	}
	else
	{
	    tOptions.add( "The x-coordinate of the vertex is NOT an integer.");
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  ((int)(b/(2.0*a))-1) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  ((int)(b/(2.0*a))-5) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  ((int)(b/(2.0*a))+1) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  ((int)(b/(2.0*a))+5) );

	}
	if (minusBover2A.isPositive())
	{
	    tOptions.add( "The x-coordinate of the vertex is positive.");
	    tOptions.add( "The vertex lies to the right of the y-axis.");
	}
	else if (minusBover2A.isNegative())
	{
	    tOptions.add( "The x-coordinate of the vertex is negative.");
	    tOptions.add( "The vertex lies to the left of the y-axis.");
	}
	else
	{
	    tOptions.add( "The x-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies on the y-axis.");
	}

	return tOptions;
    }// generateTrueStatementsForXVertex


    static ArrayList<String> generateFalseStatementsForXVertex()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 20 );
	
	//tOptions.add( "The x-coordinate of the vertex is equal to "
	//	      + minusBover2A);
	
	if (minusBover2A.isInteger())
	{
	    tOptions.add( "The x-coordinate of the vertex is NOT an integer.");
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  (minusBover2A.getNum() - 1) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  (minusBover2A.getNum() - 5) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  (minusBover2A.getNum() + 1) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  (minusBover2A.getNum() + 5) );
	}
	else
	{
	    tOptions.add( "The x-coordinate of the vertex is an integer.");
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  ((int)(b/(2.0*a))-1) );
	    tOptions.add( "The x-coordinate of the vertex is smaller than " +
			  ((int)(b/(2.0*a))-5) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  ((int)(b/(2.0*a))+1) );
	    tOptions.add( "The x-coordinate of the vertex is larger than " +
			  ((int)(b/(2.0*a))+5) );

	}
	if (minusBover2A.isPositive())
	{
	    tOptions.add( "The x-coordinate of the vertex is negative.");
	    tOptions.add( "The x-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies to the left of the y-axis.");
	    tOptions.add( "The vertex lies on the y-axis.");
	}
	else if (minusBover2A.isNegative())
	{
	    tOptions.add( "The x-coordinate of the vertex is positive.");
	    tOptions.add( "The x-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies to the right of the y-axis.");
	    tOptions.add( "The vertex lies on the y-axis.");
	}
	else
	{
	    tOptions.add( "The x-coordinate of the vertex is positive.");
	    tOptions.add( "The x-coordinate of the vertex is negative.");
	    tOptions.add( "The vertex lies to the right of the y-axis.");
	    tOptions.add( "The vertex lies to the left of the y-axis.");
	}

	return tOptions;
    }// generateFalseStatementsForXVertex

    static ArrayList<String> generateTrueStatementsForXIntercept()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );

	tOptions.add( "The value of the discriminant is " + disc );

	if (disc>0)
	{
	    tOptions.add( "The parabola has two distinct x-intercepts." );
	    tOptions.add( "The parabola intersects the x-axis at two distinct points." );
	    tOptions.add( "The discriminant of the equation is positive." );
	    if (x1.isPositive() && x2.isPositive())
		tOptions.add( "Both x-intercepts are positive." );
	    else  if (x1.isNegative() && x2.isNegative())
		tOptions.add( "One x-intercept is positive, while the other is negative ." );
	    if (discIsSquare)
	    {
		tOptions.add( "The square root of the discriminant is an integer.");
		tOptions.add( "One x-intercept is " + x1);
		tOptions.add( "One x-intercept is " + x2);
		tOptions.add( "The x-intercepts are " + x1 + " and " + x2);
		tOptions.add( x1 + " is one of the x-intercepts, and the other x-intercept has a different value.");
		tOptions.add( x2 + " is one of the x-intercepts, and the other x-intercept has a different value.");
	    }
	    else
	    {
		tOptions.add( "The square root of the discriminant is NOT an integer.");
		tOptions.add( "The x-intercepts are NOT rational numbers.");
	    }
	}
	else if (disc<0)
	{
	    tOptions.add( "The parabola has no x-intercepts." );
	    tOptions.add( "The discriminant of the equation is negative." );
	    tOptions.add( "The parabola does not intersect the x-axis." );
	    if (a>0)
		tOptions.add( "The parabola lies entirely above the x-axis." );
	    else
		tOptions.add( "The parabola lies entirely below the x-axis." );
	}
	else // disc = 0
	{
	    tOptions.add( "The parabola has only one x-intercept." );
	    tOptions.add( "The discriminant of the equation is equal to 0." );
	    tOptions.add( "The parabola intersects the x-axis at a unique point." );
	    tOptions.add( "The parabola intersects the x-axis at the vertex.");
	    tOptions.add( "The square root of the discriminant is an integer.");
	    tOptions.add( "The x-intercept is equal to " + minusBover2A );
	}
	return tOptions;
    }// generateTrueStatementsForXIntercept


    static ArrayList<String> generateFalseStatementsForXIntercept()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );

	if (disc>0)
	{
	    tOptions.add( "The parabola has only one x-intercept." );
	    tOptions.add( "The discriminant of the equation is equal to 0." );
	    tOptions.add( "The parabola intersects the x-axis at a unique point." );
	    tOptions.add( "The parabola intersects the x-axis at the vertex.");
	    tOptions.add( "The x-intercept is equal to " + minusBover2A );
	    tOptions.add( "The parabola has no x-intercepts." );	    
	    tOptions.add( "The parabola does not intersect the x-axis.");
	    

	    if (x1.isPositive() && x2.isPositive())
	    {
		tOptions.add( "One x-intercept is positive, while the other is negative ." );
		tOptions.add( "Both x-intercepts are negative." );
	    }
	    else  if (x1.isNegative() && x2.isNegative())
	    {
		tOptions.add( "One x-intercept is positive, while the other is negative ." );
		tOptions.add( "Both x-intercepts are positive." );
	    } 
	    else
	    {
		tOptions.add( "Both x-intercepts are negative." );
		tOptions.add( "Both x-intercepts are positive." );
	    }

	    if (discIsSquare)
	    {
		tOptions.add( "The square root of the discriminant is NOT an integer.");
		tOptions.add( "The x-intercepts are NOT rational numbers.");

		tOptions.add( x1 + " is NOT one of the x-intercepts.");
		tOptions.add( x2 + " is NOT one of the x-intercepts.");
		tOptions.add( x1 + " is the only x-intercept.");
		tOptions.add( x2 + " is the only x-intercept.");
	    }
	    else
	    {
		tOptions.add( "The square root of the discriminant is an integer.");
		tOptions.add( "The x-intercepts are rational numbers.");
	    }
	}
	else if (disc<0)
	{
	    tOptions.add( "The parabola has two distinct x-intercepts." );
	    tOptions.add( "The parabola has only one x-intercept." );
	    tOptions.add( "The discriminant of the equation is equal to 0." );
	    tOptions.add( "The discriminant of the equation is positive." );
	    tOptions.add( "The parabola intersects the x-axis at a unique point." );
	    tOptions.add( "The parabola intersects the x-axis at the vertex.");
	    tOptions.add( "The x-intercept is equal to " + minusBover2A );
	    tOptions.add( "The parabola intersects the x-axis at two distinct points." );	    

	    if (a>0)
		tOptions.add( "The parabola lies entirely below the x-axis." );
	    else
		tOptions.add( "The parabola lies entirely above the x-axis." );
	}
	else // disc = 0
	{
	    tOptions.add( "The parabola has two distinct x-intercepts." );
	    tOptions.add( "The discriminant of the equation is positive." );
	    tOptions.add( "The discriminant of the equation is negative." );
	    tOptions.add( "The square root of the discriminant is NOT an integer.");
	    tOptions.add( "The parabola intersects the x-axis at two distinct points." );

	    tOptions.add( "One x-intercept is positive, while the other is negative ." );

	    tOptions.add( minusBover2A + " is one of the x-intercepts, and the other x-intercept has a different value.");
	    tOptions.add( "The parabola has no x-intercepts." );
	    tOptions.add( "The parabola does not intersect the x-axis." );
	}
	return tOptions;
    }// generateFalseStatementsForXIntercept


    static ArrayList<String> generateTrueStatementsForYIntercept()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );

	tOptions.add( "The y-intercept is " + c );
	tOptions.add( "The y-intercept is the value of c." );
	tOptions.add( "The y-intercept is the value of f(0)." );
	tOptions.add( "The y-intercept is greater than " + (c-1) );
	tOptions.add( "The y-intercept is greater than " + (c-2) );
	tOptions.add( "The y-intercept is smaller than " + (c+1) );
	tOptions.add( "The y-intercept is smaller than " + (c+2) );
	if (c>0)
	    tOptions.add( "The y-intercept is positive.");
	else if (c<0)
	    tOptions.add( "The y-intercept is negative.");

	return tOptions;
    }// generateTrueStatementsForYIntercept

    static ArrayList<String> generateFalseStatementsForYIntercept()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );

	tOptions.add( "The y-intercept is the value of a." );	
	tOptions.add( "The y-intercept is the value of b." );
	tOptions.add( "The y-intercept is the value of f(c)." );
	tOptions.add( "The y-intercept is greater than " + (c+1) );
	tOptions.add( "The y-intercept is greater than " + (c+2) );
	tOptions.add( "The y-intercept is smaller than " + (c-1) );
	tOptions.add( "The y-intercept is smaller than " + (c-2) );
	if (c<0)
	    tOptions.add( "The y-intercept is positive.");
	else if (c<0)
	    tOptions.add( "The y-intercept is negative.");

	return tOptions;
    }// generateFalseStatementsForYIntercept


    static ArrayList<String> generateIncorrectXVertexValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);

	Rational next;
	String text = "The x-coordinate of the vertex is equal to ";
	used.add( minusBover2A );

	next = new Rational( b, 2*a);
	if (!myContains(used,next))
	{
	    used.add( next );
	    values.add( text+next );
	}
	if (b!=0)
	{
	    next = new Rational( a, 2*b);
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	    
	    next = new Rational( -a, 2*b);
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}

	next = new Rational( 2*b, a);
	if (!myContains(used,next))
	{
	    used.add( next );
	    values.add( text+next );
	}


	next = new Rational( -2*b, a);
	if (!myContains(used,next))
	{
	    used.add( next );
	    values.add( text+next );
	}

	if (c!=0) // to avoid duplicate wrong answer from
	    // false statements in other method
	{
	    next = new Rational( c );
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	    
	    next = new Rational( -c, 2*a);
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}
	    return values;
    }// generateIncorrectXVertexValues method



    static ArrayList<String> generateIncorrectYVertexValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);

	Rational next;
	String text = "The y-coordinate of the vertex is equal to ";
	used.add( yvertex );

	for(int i=0; i<10; i++)
	{
	    int sign = (rand.nextBoolean() ? 1 : -1);
	    next = new Rational( sign * rand.nextInt(100),
				 1 + rand.nextInt(100) );
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}
	return values;
    }// generateIncorrectYVertexValues method


    static ArrayList<String> generateIncorrectXInterceptValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);

	if ((disc>0) && discIsSquare)
	{
	    used.add( x1 );
	    used.add( x2 );
	} else if (disc==0)
	    used.add( minusBover2A );
	Rational next;
	String text = "One x-intercept is ";

	for(int i=0; i<10; i++)
	{
	    int sign = (rand.nextBoolean() ? 1 : -1);
	    next = new Rational( sign * rand.nextInt(20),
				 1+rand.nextInt(20) );
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}
	return values;
    }// generateIncorrectXInterceptValues method


    static ArrayList<String> generateIncorrectYInterceptValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);


	Rational next;
	String text = "The y-intercept is ";
	used.add( new Rational(c) );

	for(int i=0; i<10; i++)
	{
	    int sign = (rand.nextBoolean() ? 1 : -1);
	    next = new Rational( sign * rand.nextInt(20),
				 1+rand.nextInt(20) );
	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}
	return values;
    }// generateIncorrectYInterceptValues method





    static ArrayList<String> generateIncorrectDiscValues()
    {
	ArrayList<String> values = new ArrayList<String>(12);
	ArrayList<Rational> used = new ArrayList<Rational>(12);

	used.add( new Rational(disc) );
	Rational next;
	String text = "The value of the discriminant is ";

	for(int i=0; i<10; i++)
	{
	    int sign = (rand.nextBoolean() ? 1 : -1);
	    next = new Rational( sign * rand.nextInt(100) );

	    if (!myContains(used,next))
	    {
		used.add( next );
		values.add( text+next );
	    }
	}
	return values;
    }// generateIncorrectXDiscValues method

    static ArrayList<String> generateTrueStatementsForYVertex()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 10 );
	
	if (! yvertex.equalsZero())
	    tOptions.add( "The y-coordinate of the vertex is equal to "
			  + yvertex);
	
	if (yvertex.isInteger())
	{
	    tOptions.add( "The y-coordinate of the vertex is an integer.");
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  (yvertex.getNum() - 1) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  (yvertex.getNum() - 5) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  (yvertex.getNum() + 1) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  (yvertex.getNum() + 5) );
	}
	else
	{
	    tOptions.add( "The y-coordinate of the vertex is NOT an integer.");
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  ((int)(yvertex.toReal())-1) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  ((int)(yvertex.toReal())-5) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  ((int)(yvertex.toReal())+1) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  ((int)(yvertex.toReal())+5) );

	}
	if (yvertex.isPositive())
	{
	    tOptions.add( "The y-coordinate of the vertex is positive.");
	    tOptions.add( "The vertex lies above the x-axis.");
	}
	else if (yvertex.isNegative())
	{
	    tOptions.add( "The y-coordinate of the vertex is negative.");
	    tOptions.add( "The vertex lies below the y-axis.");
	}
	else
	{
	    tOptions.add( "The y-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies on the x-axis.");
	}

	return tOptions;
    }// generateTrueStatementsForYVertex


    static ArrayList<String> generateFalseStatementsForYVertex()
    {
	ArrayList<String> tOptions = new ArrayList<String>( 20 );
	
	//tOptions.add( "The x-coordinate of the vertex is equal to "
	//	      + minusBover2A);
	
	if (yvertex.isInteger())
	{
	    tOptions.add( "The y-coordinate of the vertex is NOT an integer.");
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  (yvertex.getNum() - 1) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  (yvertex.getNum() - 5) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  (yvertex.getNum() + 1) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  (yvertex.getNum() + 5) );
	}
	else
	{
	    tOptions.add( "The y-coordinate of the vertex is an integer.");
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  ((int)(yvertex.toReal())-1) );
	    tOptions.add( "The y-coordinate of the vertex is smaller than " +
			  ((int)(yvertex.toReal())-5) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  ((int)(yvertex.toReal())+1) );
	    tOptions.add( "The y-coordinate of the vertex is larger than " +
			  ((int)(yvertex.toReal()) +5) );

	}
	if (yvertex.isPositive())
	{
	    tOptions.add( "The y-coordinate of the vertex is negative.");
	    tOptions.add( "The y-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies below the x-axis.");
	    tOptions.add( "The vertex lies on the x-axis.");
	}
	else if (yvertex.isNegative())
	{
	    tOptions.add( "The y-coordinate of the vertex is positive.");
	    tOptions.add( "The y-coordinate of the vertex is equal to 0.");
	    tOptions.add( "The vertex lies above the x-axis.");
	    tOptions.add( "The vertex lies on the x-axis.");
	}
	else
	{
	    tOptions.add( "The y-coordinate of the vertex is positive.");
	    tOptions.add( "The y-coordinate of the vertex is negative.");
	    tOptions.add( "The vertex lies above the x-axis.");
	    tOptions.add( "The vertex lies below the x-axis.");
	}

	return tOptions;
    }// generateFalseStatementsForYVertex

 
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

	addText(label, term, x, y, reg_size, reg_fm, "blue",hidden);
	
	/* begin: copies of coefficients */
	int v;
	switch(pow) {
	case 2: v = a; break;
	case 1: v = b; break;
	default: v = c; break;
	}
	int correction = 0;
	if (pow==2)
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

	addText(label+"copy1", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	addText(label+"copy2", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	addText(label+"copy3", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	addText(label+"copy4", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	addText(label+"copy5", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	addText(label+"copy6", v+"", 
		x-correction , y, reg_size, reg_fm, "red",true);
	/* end: copies of coefficients */



	x += reg_fm.stringWidth(term);

	if (pow>1)
	{
	    addText(label+"_p", pow+"", x, y-(int)(0.35*reg_size),sup_size,
		    sup_fm, "blue",hidden);
	    x += sup_fm.stringWidth(pow+"");
	}
	return x;
    }

    /*
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
    */

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

    private static void addParabola(String id, int a, int b, int c,
				    int xmin, int xmax, int ymin, int ymax,
				    int x, int  y, int width, int height)
    {
	Parabola p = new Parabola(id,a,b,c,xmin,xmax,ymin,ymax,
				  x,y,width,height);
	ht.put( id, p );
	initial.addContent( p.makeElement() );
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
				    String colorname, int fontsize)
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
	font.setAttribute( "size", fontsize + "");
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

    // terrible code duplication
    static Point[] pickOptions(int k, ArrayList<Point> pts)
        {
	    Point[] points = pts.toArray( new Point[]{ new Point(1,1) });
	    boolean[] chosen = new boolean[ points.length ];
	    int count = 0;

	    if (k > points.length)
	    {
		System.out.println("Error: Requesting too many options.");
		return new Point[] { new Point(1,1)};
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

	static String[] pickOptions(int k, Collection<String> c)
	{
	    String[] a = c.toArray( new String[]{""});
	    boolean[] chosen = new boolean[ a.length ];
	    int count = 0;

	    if (k > a.length)
	    {
		System.out.println("Error: Requesting too many options.");
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

    static boolean myContains(ArrayList<Rational> a, Rational r)
    {
	Iterator<Rational> it = a.iterator();;
	while ( it.hasNext() )
	    if (r.equals( it.next() )) return true;

	return false;

    }

}



class Q
    {
	public static final int TF = 1;
	public static final int MC = 2;
	public static final int MS = 3;
	public static final int FB = 4;

	public static final int NUM_SNAPS = ParabolaMain.num_snaps;
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
