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

/**
 *  JhaveStableMarriage.java: This program generages an XML
 * file for use of JHAVE visualization of the Stable Marriage
 * Problem Algorithm. The number of matches must be between
 * 3 and 6 for a good graphical display.
 *
 * @author	Benjamin Vollbrecht
 * @date	Oct 31, 2006
 */

package exe.vollbrecht;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import exe.GAIGSarray;
import exe.GAIGSgraph;
import exe.ShowFile;
import exe.XMLtfQuestion;

public class JhaveStableMarriage
{
	private int[][] menArray;
	private GAIGSarray menGArray ;
	private int[][] womenArray;
	private GAIGSarray womenGArray;
	private int[][] matches;
	private GAIGSgraph graph;
	private ShowFile showFile;

	// Member variables so they can be used in ShowFile's pseudocode
	private int currentMan;
	private int woman;
	
	// IDs for questions
	private int questionID = 0;
	
	// Location of the pseudocode and documentation for the ShowFile
	private final String pseudocodeURL = "fhtemp.php";
	private final String docURL = "doc.html";
	
	public static void main(String[] args) throws IOException
	{
		new JhaveStableMarriage().stableMarriage(args);
	}
	
	/**
	 * @param args Arguments passed into through command line
	 * 		arg[0] - filename of the script
	 * 		arg[1] - number of matches to be made. 
	 * 				 Must be between 3 and 6
	 */
	public void stableMarriage(String[] args) throws IOException
	{
		this.showFile = new ShowFile(args[0]);
		final int numMatches	= Integer.parseInt(args[1]);
		
		this.menArray	= new int[numMatches][numMatches];
		this.menGArray	= new GAIGSarray(numMatches, numMatches, "Men", "#0000FF",
										0.0, 0.74, 0.3, 0.99, 0.15*(4.0/numMatches));
		this.womenArray	= new int[numMatches][numMatches];
		this.womenGArray = new GAIGSarray(numMatches, numMatches, "Women", "#0000FF",
										0.7, 0.74, 1.0, 0.99, 0.15*(4.0/numMatches));
		this.matches	= new int[2][numMatches];
		this.graph 		= new GAIGSgraph(true, true, false, "Matches", "#0000FF",
										0.2, 0.0, 0.8, 0.7, 0.075);
		
		// Make an array for random choices
		ArrayList<Integer> choices = new ArrayList<Integer>();
		for(int i = 1; i <= numMatches; i++)
		{
			choices.add(i);
		}
		
		// Fill in men's graphics
		for(int i = 0; i < numMatches; i++)
		{
			this.menGArray.setRowLabel(String.valueOf(i+1), i);
			graph.addNode((char)(i+49), 0.0, 
					0.6*((double)(numMatches-(i+1))/(numMatches-1)), "#0033FF");
		}
		
		// Fill in the men's choices randomly
		for(int i = 0; i < numMatches; i++)
		{
			Collections.shuffle(choices);
			for(int j = 0; j < numMatches; j++)
			{
				this.menArray[i][j] = choices.get(j).intValue();
				this.menGArray.set((char)(choices.get(j).intValue() + 64), i, j);
			}
		}
		
		// Fill in women's graphics
		for(int i = 0; i < numMatches; i++)
		{
			this.womenGArray.setRowLabel(String.valueOf((char)(i+65)), i);
			this.graph.addNode((char)(i+65), 1.0, 
					0.6*((double)(numMatches-(i+1))/(numMatches-1)), "#0033FF");
		}

		// Fill in the women's choices randomly
		for(int i = 0; i < numMatches; i++)
		{
			Collections.shuffle(choices);
			for(int j = 0; j < numMatches; j++)
			{
				this.womenArray[i][j] = choices.get(j).intValue();
				this.womenGArray.set(choices.get(j).intValue(), i, j);
			}
		}
		
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(0, 0, 0),
								this.menGArray, this.womenGArray, this.graph);
		
		// Put men in 1st column of matches array
		for(int i = 0; i < numMatches; i++)
		{
			this.matches[0][i] = i + 1;
		}
		
		// Arrays are filled, now match them together
		while(!isAllMatched())
		{
			currentMan = nextUnmatched();
			woman = nextPotentialMatch(currentMan);
			if(isAlreadyMatched(woman))
			{
				if(prefers(woman, currentMan))
				{
					unassign(woman);
					tentativelyAssign(woman, currentMan, true);
				}
			}
			else
			{
				tentativelyAssign(woman, currentMan, false);
			}
		}
		
		this.showFile.close();
	} // main()
	
	/**
	 * Determines if everyone has been matched or not
	 * 
	 * @param matches Array that holds the matches
	 * @return True if all are matched, false otherwise
	 */
	private boolean isAllMatched() throws IOException
	{
		boolean allMatched = true;
		
		for(int i = 0; i < this.matches[1].length; i++)
		{
			if(this.matches[1][i] == 0)
			{
				allMatched = false;
				break;
			}
		}
		
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(1, this.currentMan, this.woman),
				this.menGArray, this.womenGArray, this.graph);

		return allMatched;
	} // isAllMatched()
	
	/**
	 * Returns the index of the next unmatched man
	 * 
	 * @return The index of the next unmatched man
	 */
	private int nextUnmatched() throws IOException
	{
		int index = -1;
		
		for(int i = 0; i < this.matches[0].length; i++)
		{
			if(this.matches[1][i] == 0)
			{
				index = i;
				break;
			}
		}
		
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(2, index, this.woman),
				this.menGArray, this.womenGArray, this.graph);
		
		return index;
	} // nextUnmatched()
	
	/**
	 * Returns the index value of the next potential woman 
	 * for a specific man
	 * 
	 * @param man Index of the man
	 * @return The value of the next potential woman
	 */
	private int nextPotentialMatch(int man) throws IOException
	{
		int potentialWoman = -1;
		
		for(int i = 0; i < this.menArray[man].length; i++)
		{
			if(this.menArray[man][i] != 0)
			{
				potentialWoman = this.menArray[man][i];
				this.menArray[man][i] = 0;
				this.menGArray.setColor(man, i, "#0088FF");
				this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(3, man, potentialWoman),
										this.menGArray, this.womenGArray, this.graph);
				break;
			}
		}
		
		return potentialWoman;
	} // nextPotentialMatch()
	
	/**
	 * Checks if a woman is already matched with someone
	 * 
	 * @param woman Value of a woman
	 * @return True if woman is matched, false otherwise
	 */
	private boolean isAlreadyMatched(int woman) throws IOException
	{
		boolean isMatched = false;
		
		for(int i = 0; i < this.matches[1].length; i++)
		{
			if(this.matches[1][i] == woman)
				isMatched = true;
		}
		
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(4, this.currentMan, woman),
								this.menGArray, this.womenGArray, this.graph);

		return isMatched;
	}
	
	/**
	 * Determines if a woman prefers this man
	 * compared to the one she's currently assigned to.
	 * 
	 * @param woman Value of a woman
	 * @param indexOfMan Index of the new man
	 * @return true if woman prefers new man, false if she prefers current man
	 */
	private boolean prefers(int woman, int indexOfMan) throws IOException
	{
		boolean prefersNew = true;
		
		// The current match's number
		int currentMatch = 0;
		// The indexes in the woman's preference list
		int currentMatchIndex = 0;
		int newManIndex = 0;
		
		for(int i = 0; i < this.matches[1].length; i++)
		{
			if(this.matches[1][i] == woman)
			{
				currentMatch = this.matches[0][i];
				break;
			}
		}
		
		for(int i = 0; i < this.womenArray[woman-1].length; i++)
		{
			if(currentMatchIndex != 0 && newManIndex != 0)
				break;
			
			if(this.womenArray[woman-1][i] == currentMatch)
			{
				currentMatchIndex = i;
				continue;
			}
			
			if(this.womenArray[woman-1][i] == (indexOfMan+1))
			{
				newManIndex = i;
			}
		}
		
		this.womenGArray.setColor(woman-1, currentMatchIndex, "#0088FF");
		this.womenGArray.setColor(woman-1, newManIndex, "#0088FF");
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(5, this.currentMan, woman),
								this.menGArray, this.womenGArray, this.graph);
		
		prefersNew = (currentMatchIndex > newManIndex);
		
		XMLtfQuestion preferQ = new XMLtfQuestion(this.showFile, this.questionID + "preferQuestion");
		this.questionID++;
		preferQ.setQuestionText("The woman will stay with man " + currentMatch);
		preferQ.setAnswer(!prefersNew);
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(5, this.currentMan, woman),
								preferQ, this.menGArray, this.womenGArray, this.graph);
		
		if(prefersNew)
			this.womenGArray.setColor(woman-1, newManIndex, "#8800FF");
		else
			this.womenGArray.setColor(woman-1, currentMatchIndex, "#8800FF");
		
		this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(5, this.currentMan, woman),
								this.menGArray, this.womenGArray, this.graph);
		
		this.womenGArray.setColor(woman-1, currentMatchIndex, "#0000FF");
		this.womenGArray.setColor(woman-1, newManIndex, "#0000FF");
		
		return prefersNew;
	}
	
	/**
	 * Unmatch a woman from a man
	 * 
	 * @param woman Value of the woman
	 */
	private void unassign(int woman) throws IOException
	{
		for(int i = 0; i < this.matches[1].length; i++)
		{
			if(this.matches[1][i] == woman)
			{
				this.matches[1][i] = 0;
				this.graph.removeEdge((char)(i + 49), (char)(woman + 64));
				this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(6, this.currentMan, woman),
										this.menGArray, this.womenGArray, this.graph);
				break;
			}
		}
	} // unassign()
	
	/**
	 * Tentatively match a woman with a man
	 * 
	 * @param woman Value of the woman
	 * @param man Index of the man
	 * @param womanWasMatched Used solely for determining what line to show in the ShowFile
	 */
	private void tentativelyAssign(int woman, int man, boolean womanWasMatched) throws IOException
	{
		this.matches[1][man] = woman;
		char manChar = (char)(man + 49);
		char womanChar = (char)(woman + 64);
		this.graph.addEdge(manChar, womanChar, 2.5, "#00FF33");
		if (womanWasMatched)
			this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(7, man, woman),
									this.menGArray, this.womenGArray, this.graph);
		else
			this.showFile.writeSnap(null, this.docURL, this.getPseudocodeURL(9, man, woman),
									this.menGArray, this.womenGArray, this.graph);
	} // tentativelyAssign()
	
	/**
	 * Generates the path to the ShowFile pseudocode path that shows
	 * the specified line number, current man value, and current woman value.
	 * 
	 * @param lineNum Line number in the pseudocode
	 * @param currentMan Value to show for the current man
	 * @param woman Value to show for the woman
	 * @return String of the path to the pseudocode file
	 */
	private String getPseudocodeURL(int lineNum, int currentMan, int woman)
	{
		String url = this.pseudocodeURL + "?line=" + lineNum
										+ "&var[currentMan]=" + (currentMan + 1);
		
		url	+= "&var[woman]=" + woman;
		if (woman != 0)
			url += "%20(" + (char)(woman + 64) + ")";
		
		return url;
	} // getPseudocodeLocation()
}
