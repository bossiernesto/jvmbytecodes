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

//Ben Tidman
//program that writes a script file for an animation of the union find algorithm

package exe.unionfindmyles;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import exe.*;

public class unionFind
{
    private static int fc1, fc2, first, second, counter, answerv;
    private static DecimalFormat myFormat = new DecimalFormat("#.00");
    private static boolean color = false, wfa, nq, path = false, weight = false, random = false;
    private static PrintWriter out;
    private static char commandType;
    private static questionCollection Questions;
    private static fibQuestion fibQuest;
    
    public static void main(String[] args) throws IOException
    {
	int x = 6;
	boolean moreCommands = true;
	String viewType, s;
	Integer convert;
	counter = 0;
	fc1 = 0;
	fc2 = 0;
	wfa = false;
	nq = false;
	answerv = 0;

	out = new PrintWriter(new BufferedWriter(new FileWriter(args[0])));

	//set up all the different data sets
	s = args[1];
	Vector rep = new Vector();
	int[] list = new int[Integer.parseInt(s)];
	int[] weights = new int[list.length];
	for(int i = 0; i < list.length; i++)
	{
	    convert = new Integer(i);
	    list [i] = i;
	    weights [i] = 1;
	    rep.add(convert);
	}
	viewType = args[2];
	out.println("view scale .85");	

	//get rest of input
	s = args[3];
	if(s.toUpperCase().equals("T"))
	    path = true;

	s = args[4];
	if(s.toUpperCase().equals("T"))
	    weight  = true;
	
	s = args[5];
	if(s.toUpperCase().equals("T"))
	    random  = true;

	//if array view is specified
 	if(viewType.toUpperCase().equals("ARRAY"))
	{     
	    Questions = new questionCollection(out, 10);
	    while(x < args.length || random)
	    {
		out.flush();
		if(!random)
		    commandType = args[x].toUpperCase().charAt(0);
		else
		    makeCommand(rep, list.length);
		if(commandType == 'U')
		{
		    if(!random)
		    {
			first = Integer.parseInt(args[x + 1]);
			second = Integer.parseInt(args[x + 2]);
		    }
		    aUnion(list, rep, weights, first, second, weight);
		    x += 3;
		}
		else if(commandType == 'F')
		{
		    if(!random)
			first = Integer.parseInt(args[x + 1]);
		    aFind(list, weights, first);
		    x += 2;
		}
		if(rep.size() <= 1)
		    random = false;		    
	    }
	    aSnapShot(list, weights, -1, -1, -1, 0, 0);
	}
	//if tree view is specified
	else if(viewType.toUpperCase().equals("TREE"))
	{
	    Questions = new questionCollection(out, 10);
	    while(x < args.length || random)
	    {
		out.flush();
		if(!random)
		    commandType = args[x].toUpperCase().charAt(0);
		else
		    makeCommand(rep, list.length);
		if(commandType == 'U')
		{
		    if(!random)
		    {
			first = Integer.parseInt(args[x + 1]);
			second = Integer.parseInt(args[x + 2]);
		    }
		    tUnion(list, rep, weights, first, second, weight);
		    x += 3;
		}
		else if(commandType == 'F')
		{
		    if(!random)
			first = Integer.parseInt(args[x + 1]);
		    fc1++;
		    if(!path)
		    	tFind(list, weights, first, first);
		    else
		    	tFindP(list, weights, first, first);
		    x += 2;
		}
		if(rep.size() <= 1)
		    random = false;
	    }
	    tSnapShot(list, weights, -1, -1, -1, 0, 0);
	}
	Questions.writeQuestionsAtEOSF();
	out.close();
    }

    
    //Generates a new random command
    public static void makeCommand(Vector rep, int length)
    {
	double rand = Math.random();
	int round;
	
	if(rand <= .5)
	{
	    commandType = 'U';
	    rand = Math.random();
	    rand *= (rep.size() - 1);
	    round = Math.round((float)rand);
	    first = Integer.parseInt((rep.elementAt(round)).toString());
	    do
	    {
		rand = Math.random();
		rand *= (rep.size() - 1);
		round = Math.round((float)rand);
		second = Integer.parseInt((rep.elementAt(round)).toString());
	    }while(first == second);
	}
	else
	{
	    commandType = 'F';
	    rand = Math.random();
	    rand *= (length - 1);
	    round = Math.round((float)rand);
	    first = round;
	}
    }


    //Make set1 and set2 into one set.  If weight is true merge smaller set
    //into the larger one.
    public static void aUnion(int [] list, Vector rep, int [] weights, int set1, int set2, boolean weight)
    {
	int sum1 = 0, sum2 = 0, badCommand = 0, bc1 = 1, bc2 = 1;
	int algo = 0;
	Integer convert;
        Holder stuff = new Holder();
	stuff.one = "" + set1;	
	stuff.two = "" + set2;

	for(int x = 0; x < list.length; x++)
	{
	    if(list[x] == set1)
		bc1 = 0;
	    else if(list[x] == set2)
		bc2 = 0;	 
	}
	if(bc1 == 1 || bc2 == 1)
	    badCommand = 1;
	
	if(badCommand == 0)
	{
	    for(int x = 0; x < list.length; x++)
	    {	     	  
		if(weight)
		{
		    algo = 2;
		    genPseudo(2, 1, stuff);
                    aSnapShot(list, weights, x-1, set1, set2, 1, badCommand);
		    stuff.three = "" + x;
		    genPseudo(2, 2, stuff);
                    aSnapShot(list, weights, x, set1, set2, 1, badCommand);
		    if(weights[set1] >= weights[set2])
		    {
			convert = new Integer(set2);
			rep.remove(convert);
		        genPseudo(2, 3, stuff);
                        aSnapShot(list, weights, x, set1, set2, 1, badCommand);
			if(list[x] == set2)
			{
		    	    genPseudo(2, 4, stuff);
                    	    aSnapShot(list, weights, x, set1, set2, 1, badCommand);
			    list[x] = set1;
			    weights[set1]++;
			}
		    }
		    else
		    {
		        genPseudo(2, 5, stuff);
                        aSnapShot(list, weights, x, set1, set2, 1, badCommand);
			convert = new Integer(set1);
			rep.remove(convert);
		    	genPseudo(2, 6, stuff);
                    	aSnapShot(list, weights, x, set1, set2, 1, badCommand);
			if(list[x] == set1)
			{
		    	    genPseudo(2, 7, stuff);
                    	    aSnapShot(list, weights, x, set1, set2, 1, badCommand);
			    list[x] = set2;
			    weights[set2]++;
			}
		    }
		}
		else 
		{
		    algo = 1;
		    genPseudo(1, 1, stuff);
                    aSnapShot(list, weights, x-1, set1, set2, 1, badCommand);
		    stuff.three = "" + x;
		    genPseudo(1, 2, stuff);
                    aSnapShot(list, weights, x, set1, set2, 1, badCommand);
		    if(list[x] == set2)
		    {
			    genPseudo(1, 3, stuff);
			    aSnapShot(list, weights, x, set1, set2, 2, badCommand);
			    convert = new Integer(set2);
			    rep.remove(convert);
			    list[x] = set1;
			    weights[set1]++;
		    }
		}
	    }

	    if(algo == 1)
	    {
		genPseudo(1, 4, stuff);
		aSnapShot(list, weights, -1, set1, set2, 2, badCommand);
	    }
	    else
    	    {
		genPseudo(2, 8, stuff);
		aSnapShot(list, weights, -1, set1, set2, 2, badCommand);
	    }
	}
	else
	    aSnapShot(list, weights, -1, set1, set2, 1, badCommand);
    }

    //finds the "name" of the set that list[index] is in
    public static void aFind(int [] list, int [] weights, int index)
    {	
        Holder stuff = new Holder();
	stuff.one = "" + index;
	if(index >= 0 && index < list.length)
	{
	    genPseudo(3, 1, stuff);
	    aSnapShot(list, weights, index, -1, -1, 3, 0);
	}	
	else
	{
	    genPseudo(3, 1, stuff);
	    aSnapShot(list, weights, index, -1, -1, 3, 1);
	}
    }

    //Make set1 and set2 into one set.  If weight is true merge smaller set
    //into the larger one.
    public static void tUnion(int [] list, Vector rep, int [] weights, int set1, int set2, boolean weight)
    {
        Holder stuff = new Holder();
	stuff.one = "" + set1;
	stuff.two = "" + set2;
	Integer convert;

	if(weight)
	{
	    if(weights[set1] >= weights[set2])
	    {
	    	genPseudo(5, 1, stuff);
		tSnapShot(list, weights, -2, set1, set2, 1, 0);
		if(list[set2] == set2 && list[set1] == set1)
		{
		    convert = new Integer(set2);
		    rep.remove(convert);
	    	    genPseudo(5, 2, stuff);
		    tSnapShot(list, weights, -2, set1, set2, 1, 0);
		    list[set2] = set1;
		    weights[set1] += weights[set2];
		    weights[set2] = 0;
	    	    genPseudo(5, 5, stuff);
		    tSnapShot(list, weights, -2, set1, set2, 2, 0);
		}
		else
		    tSnapShot(list, weights, -2, set1, set2, 1, 1);
	    }
	    else
	    {
		genPseudo(5, 3, stuff);
		if(list[set2] == set2 && list[set1] == set1)
		{
		    convert = new Integer(set1);
		    rep.remove(convert);
		    genPseudo(5, 4, stuff);
		    tSnapShot(list, weights, -3, set1, set2, 1, 0);
		    list[set1] = set2;
		    weights[set2] += weights[set1];
		    weights[set1] = 0;
		    genPseudo(5, 5, stuff);
		    tSnapShot(list, weights, -3, set1, set2, 2, 0);
		}
		else
		     tSnapShot(list, weights, -3, set1, set2, 1, 1);
	    }
	}
	else
	{
	    if(list[set2] == set2 && list[set1] == set1)
	    {
		convert = new Integer(set2);
		rep.remove(convert);
	        genPseudo(4, 1, stuff);
		tSnapShot(list, weights, -1, set1, set2, 1, 0);
		list[set2] = set1;
	        genPseudo(4, 2, stuff);
		tSnapShot(list, weights, -1, set1, set2, 2, 0);
	    }
	    else
		tSnapShot(list, weights, -3, set1, set2, 1, 1);
	}		
    }

    //This used to be an elegant little recursive method, but has since been 
    //transformed into an inferior non-recursive method.
    public static void tFind(int [] list, int [] weights, int index, int start)
    {
	boolean found = false;
	int x = index, root = 0, temp;

        Holder stuff = new Holder();
	stuff.one = "" + index;

	if(index < 0 || index >= list.length)
	    tSnapShot(list, weights, x, start, -2, 3, 1);
	else
	{
	    //loop that finds the represetative of the set
	    while(!found)
	    {
		fc2++;
	        genPseudo(6, 1, stuff);
	        tSnapShot(list, weights, x, start, -2, 4, 0);
	        genPseudo(6, 2, stuff);
		tSnapShot(list, weights, x, start, -2, 3, 0);
		if(list[x] != x)
		{
	            genPseudo(6, 3, stuff);
		    tSnapShot(list, weights, x, start, -2, 3, 0);
		    x = list[x];
	    	    stuff.one = "" + x;
		}
		else
		{
	            genPseudo(6, 4, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
	            genPseudo(6, 5, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
		    root = x;
		    found = true;
		}
	    }
	}
    }

    //This used to be an elegant little recursive method, but has since been 
    //transformed into an inferior non-recursive method.
    public static void tFindP(int [] list, int [] weights, int index, int start)
    {
	boolean found = false;
	int x = index, root = 0, temp;
        Holder stuff = new Holder();
	stuff.one = "" + index;

	if(index < 0 || index >= list.length)
	    tSnapShot(list, weights, x, start, -2, 3, 1);
	else
	{
	    genPseudo(7, 1, stuff);
	    tSnapShot(list, weights, x, start, -2, 4, 0);
	    stuff.two = "" + x;

	    //loop that finds the represetative of the set
	    while(!found)
	    {
		fc2++;
	        genPseudo(7, 2, stuff);
	        tSnapShot(list, weights, x, start, -2, 4, 0);
	        genPseudo(7, 3, stuff);
		tSnapShot(list, weights, x, start, -2, 3, 0);
		if(list[x] != x)
		{
	            genPseudo(7, 4, stuff);
		    tSnapShot(list, weights, x, start, -2, 3, 0);
		    x = list[x];
	    	    stuff.two = "" + x;
		}
		else
		{
	            genPseudo(7, 5, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
	            genPseudo(7, 6, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
		    root = x;
	            stuff.three = "" + root;
		    found = true;
		}
	    }

	    //make all nodes on the path of find point at the root.
	    genPseudo(7, 7, stuff);
	    tSnapShot(list, weights, x, start, -2, 4, 0);
	    x = index;
	    stuff.two = "" + x;
	    found = false;
	    genPseudo(7, 8, stuff);
	    tSnapShot(list, weights, x, start, -2, 4, 0);
	    while(!found)
	    {
		if(list[x] == root)
		{
		    found = true;
		}
		else
		{
	    	    genPseudo(7, 9, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
		    temp = list[x];
	            stuff.four = "" + temp;
	    	    genPseudo(7, 10, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
		    list[x] = root;
	    	    genPseudo(7, 11, stuff);
		    tSnapShot(list, weights, x, start, -2, 4, 0);
		    x = temp;
	            stuff.two = "" + x;	  
	            genPseudo(7, 8, stuff);
	            tSnapShot(list, weights, x, start, -2, 4, 0);
		}
	    }
	    genPseudo(7, 12, stuff);
	    tSnapShot(list, weights, x, start, -2, 4, 0);
	}
    }

    //method used to generate appropriate pseudo code for bubble sort
    public static void genPseudo(int algo, int line, Holder stuff)
    {
	switch (algo)
	{
            case 1: 	
		out.println("VIEW ALGO AU.php?line=" + line + "&var[set1]=" + stuff.one + "&var[set2]=" + stuff.two + "&var[x]=" + stuff.three);
		break;
            case 2: 	
		out.println("VIEW ALGO AUW.php?line=" + line + "&var[set1]=" + stuff.one + "&var[set2]=" + stuff.two + "&var[x]=" + stuff.three);
		break;
            case 3: 	
		out.println("VIEW ALGO AF.php?line=" + line + "&var[index]=" + stuff.one);
		break;
            case 4: 	
		out.println("VIEW ALGO TU.php?line=" + line + "&var[set1]=" + stuff.one + "&var[set2]=" + stuff.two);
		break;
            case 5: 	
		out.println("VIEW ALGO TUW.php?line=" + line + "&var[set1]=" + stuff.one + "&var[set2]=" + stuff.two);
		break;
            case 6: 	
		out.println("VIEW ALGO TF.php?line=" + line + "&var[index]=" + stuff.one + "&var[x]=" + stuff.two);
		break;
            case 7: 	
		out.println("VIEW ALGO TFP.php?line=" + line + "&var[index]=" + stuff.one + "&var[x]=" + stuff.two + "&var[representative]=" + stuff.three + "&var[temp]=" + stuff.four);
		break;
	}
    }


    //creates snapshot for the array view of union find
    public static void aSnapShot(int [] list, int [] weights, int value, int set1, int set2, int snap, int badCommand)
    {
	int scount1 = 0, scount2 = 0;

	fibQuest = new fibQuestion(out, ("" + counter));
	counter++;

	if(value == 0 && snap == 1 && !wfa && !nq)
	{
	    fibQuest.setQuestionText("The new representative for set " + set1 + " and set " + set2 + " will be ___.");
	    if(weight)
	    {
		if(weights[set1] >= weights[set2])
		    fibQuest.setAnswer("" + set1);
		else
		    fibQuest.setAnswer("" + set2);
	    }
	    else
		fibQuest.setAnswer("" + set1);
	    
	    Questions.addQuestion(fibQuest);
	    Questions.insertQuestion(fibQuest.getID());
	}
	else if(snap == 1 && !wfa && !nq)
	{
	    if(weight)
	    {
		if(weights[set1] >= weights[set2])
		    fibQuest.setQuestionText("How many elements will be moved from set " + set2 + " to set " + set1 + " from this point till the end of the pass?");
		else
		    fibQuest.setQuestionText("How many elements will be moved from set " + set1 + " to set " + set2 + " from this point till the end of the pass?");
	    }
	    else
		fibQuest.setQuestionText("How many elements will be moved from set " + set2 + " to set " + set1 + " from this point till the end of the pass?");	    
	    Questions.addQuestion(fibQuest);
	    Questions.insertQuestion(fibQuest.getID());
	    wfa = true;
	    nq = true;
	}
	     
	out.println("UF_Array");
	out.println("1");
	out.println("Union Find");
	out.println("***\\***");
	out.println(list.length);
	out.println("1");
	if(weight)
	    out.println(0);
	else
	    out.println(1);
	out.println(badCommand);
	for(int x = 0; x < list.length; x++)
	{
	    out.println(x);
	    out.println("1");
	    
	    if(x == value)
	    {
		out.println("\\B" + list[x]);
		if(list[x] == set1)
		    scount1++;
		else if(list[x] == set2)
		    scount2++;
	    }
	    else if(list[x] == set1 && snap < 3 )
	    {
		out.println("\\R" + list[x]);
		scount1++;
	    }
	    else if(list[x] == set2 && snap < 3)
	    {
		out.println("\\G" + list[x]);
		scount2++;
	    }	    
	    else
		out.println("" + list[x]);
	}
	
	if(wfa)
	{
	    if(weight)
	    {
		if(weights[set1] >= weights[set2])
		    fibQuest.setAnswer("" + scount2);
		else
		    fibQuest.setAnswer("" + scount1);
	    }
	    else
		fibQuest.setAnswer("" + scount2);
	    
	    wfa = false;	    
	}

        if(nq && snap == 1 && value == (list.length - 1))
	    nq = false;

	out.println("1"); 
	out.println(list.length);
	
	if(snap == 1 || snap == 2 || snap == 6)
	    out.println("Union sets " + set1 + " and " + set2);
	else if(snap == 3 || snap == 4)
	    out.println("Find " + value);
	else if(snap == 0 && badCommand == 0)
	    out.println("Finished");

	out.println("***^***");
    }

    //method that creats the snapshot for the tree view of union find
    public static void tSnapShot(int [] list, int [] weights, int value, int set1, int set2, int snap, int badCommand)
    {
	int count = 0, count2 = 0, send;
	int [] temp = new int[list.length];

	if(snap == 1 && !wfa)
	{
	    fibQuest = new fibQuestion(out, ("" + counter));
	    fibQuest.setQuestionText("The new representative for set " + set1 + " and set " + set2 + " will be ___.");
	    if(weight)
	    {
		if(weights[set1] >= weights[set2])
		    fibQuest.setAnswer("" + set1);
		else
		    fibQuest.setAnswer("" + set2);
	    }
	    else
		fibQuest.setAnswer("" + set1);
	    
	    Questions.addQuestion(fibQuest);
	    Questions.insertQuestion(fibQuest.getID());    
	    counter++;
	}
	else if(!wfa && snap == 3 && value == set1)
	{
	    fibQuest = new fibQuestion(out, ("" + counter));
	    fibQuest.setQuestionText("How many more comparisions must be made to find the representative of the set?");
	    Questions.addQuestion(fibQuest);
	    Questions.insertQuestion(fibQuest.getID());
	    answerv = fc2;
	    wfa = true;
	}
	
	if(wfa && snap == 3 && list[value] == value)
	{
	    fibQuest.setAnswer("" + (fc2 - answerv));
	    wfa = false;
	    counter++;
	}

	for(int i = 0; i < temp.length; i++)
	    temp[i] = list [i];

	out.println("UFTree ");
	out.println("1");
	out.println("Union Find");
	out.println("***\\***");
	out.println(list.length);
	if(path)
	    out.println(0);
	else
	    out.println(1);
	if(weight)
	    out.println(0);
	else
	    out.println(1);
	out.println(badCommand);
	while(count < list.length)
	{
	    //looks for root of tree
	    if(temp[count] >= 0 && temp[count] == count)
	    {
		out.println("\\tree");
		out.println("0");
		if((count == value || count == set1) && (snap == 3 || snap == 4))
		    out.println("\\B" + temp[count]);
		else if((snap == 1 || snap == 2) && count == set2)
		{
		    out.println("\\B" + temp[count]);
		    value = set1;
		}
		else if((snap == 1 || snap == 2) && count == set1)
		{
		    out.println("\\G" + temp[count]);
		    value = set2;
		}
		else
		    out.println("" + temp[count]);
		
		//used for union snap to color tree that is being added into other set
		
		if(!((snap == 1 || snap == 2) &&  (count == set1 || count == set2)) && snap != 3 && snap != 4)
		    value = -1;
		
		//all values that have been viewed are marked
		temp[count] = -1;
		color = false;
		preOrder(temp, value, count, set1, set2, 1, snap);
	    }
	    count++;
	} 
	if(fc1 == 0)
	    out.println("0.00");
	else
	    out.println(myFormat.format((double)fc2 / (double)fc1));

	out.println("1");

	if(snap == 1 || snap == 2)
	    out.println("Union sets " + set1 + " and " + set2);
	else if(snap == 3 || snap == 4 || snap == 6)
	    out.println("Find set for " + set1);
	else if(snap == 0 && badCommand == 0)
	    out.println("Finished");

	out.println("***^***");
    }

    //Recursive method that does a preorder traversal of each tree coloring
    // appropriate nodes.
    public static void preOrder(int [] list, int value, int root, int set1, int set2, int level, int snap)
    {
	int x, store = 0;
	//linear search through the tree to find next child to draw	
	for(x = 0; x < list.length; x++)
	{
	    if(list[x] == root)
	    {
		out.println(level);
		if((snap == 1 || snap == 2) && value == set2)
		{
		    if(x == value || list[x] == value || color)
		    {
			out.println("\\B" + x);
			color = true;
		    }
		    else
			out.println("\\G" + x);	
		}
		else if((snap == 1 || snap == 2) && value == set1)
		{
		    if(x == value || list[x] == value || color)
		    {
			out.println("\\G" + x);
			color = true;
		    }
		    else
			out.println("\\B" + x);
		}
		else if(x == value && (snap == 3 || snap == 4))
		    out.println("\\B" + x);
		else
		    out.println("" + x);
		//all values that have been viewed are marked
		list[x] = -1;
		preOrder(list, value, x, set1, set2, (level + 1), snap);		
		if(x == value || list[x] == value)
		    color = false;
	    }
	}
    }
}

//class used to hold all the variable values to send to pseudo code window
class Holder
{
    String one;
    String two;
    String three;
    String four;
    public Holder()
    {
	one = "-";
	two = "-";
	three = "-";
	four = "-";
    }
}
