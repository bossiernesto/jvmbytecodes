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

package exe.refstructdemo;

import exe.*;
import java.io.*;

public class JoeRefStructDemo
{
	public static void main(String args[]) throws IOException
	{
		ShowFile show = new ShowFile(args[0]);
		// This array is the root RefBox
		RefBox[] rootarr1 = {new RefBox("segment")};
		show.writeSnap("Ref Struct Test Slide 1",null,"index.php?line=1",new GAIGSRefStruct(rootarr1));
		
		// The arr1 array will represent the RefStruct below the root
		RefBox[] arr1 = {new RefBox("start"),new RefBox("finish")};
		// A new root array must be created with the new reference to arr1
		RefBox[] rootarr2 = {new RefBox("segment",new GAIGSRefStruct(arr1))};
		show.writeSnap("Ref Struct Test Slide 2",null,"index.php?line=2",new GAIGSRefStruct(rootarr2));
		
		RefBox[] arr2 = {new RefBox("x"),new RefBox("y")};
		// A new start RefBox is created with a reference to arr2
		RefBox[] arr3 = {new RefBox("start",new GAIGSRefStruct(arr2)),new RefBox("finish")};
		RefBox[] rootarr3 = {new RefBox("segment",new GAIGSRefStruct(arr3))};
		show.writeSnap("Ref Struct Test Slide 3",null,"index.php?line=3",new GAIGSRefStruct(rootarr3));
		
		RefBox[] arr4 = {new RefBox("start",new GAIGSRefStruct(arr2)),new RefBox("finish",new GAIGSRefStruct(arr2))};
		RefBox[] rootarr4 = {new RefBox("segment",new GAIGSRefStruct(arr4))};
		show.writeSnap("Ref Struct Test Slide 4",null,"index.php?line=4",new GAIGSRefStruct(rootarr4));
	
		/*
		 * Note here that if you are passing a String as data to the RefBox you must type cast it
		 * to an Object in order for the data to be printed correctly.  This is due to a conflict
		 * with constructors in the RefBox class.
		 */
		/*
		 * For each of the following slides since new data is being added a new RefStruct is created
		 * at each new slide that contians the new data associated with each RefBox.  Note that as the
		 * slides go on past arrays are reused as they RefStruct does not change at all.
		 */
		RefBox[] arr5 = {new RefBox("x",/*TYPE CAST MUST BE HERE--->*/(Object)"2"),new RefBox("y")};
		RefBox[] arr6 = {new RefBox("start",new GAIGSRefStruct(arr5)),new RefBox("finish",new GAIGSRefStruct(arr2))};
		RefBox[] rootarr5 = {new RefBox("segment",new GAIGSRefStruct(arr6))};
		show.writeSnap("Ref Struct Test Slide 5",null,"index.php?line=5",new GAIGSRefStruct(rootarr5));
		
		RefBox[] arr7 = {new RefBox("x",(Object)"2"),new RefBox("y",(Object)"3")};
		RefBox[] arr8 = {new RefBox("start",new GAIGSRefStruct(arr7)),new RefBox("finish",new GAIGSRefStruct(arr2))};
		RefBox[] rootarr6 = {new RefBox("segment",new GAIGSRefStruct(arr8))};
		show.writeSnap("Ref Struct Test Slide 6",null,"index.php?line=6",new GAIGSRefStruct(rootarr6));
		
		RefBox[] arr9 = {new RefBox("x",(Object)"3"),new RefBox("y")};
		RefBox[] arr10 = {new RefBox("start",new GAIGSRefStruct(arr7)),new RefBox("finish",new GAIGSRefStruct(arr9))};
		RefBox[] rootarr7 = {new RefBox("segment",new GAIGSRefStruct(arr10))};
		show.writeSnap("Ref Struct Test Slide 7",null,"index.php?line=7",new GAIGSRefStruct(rootarr7));
		
		RefBox[] arr11 = {new RefBox("x",(Object)"3"),new RefBox("y",(Object)"5")};
		RefBox[] arr12 = {new RefBox("start",new GAIGSRefStruct(arr7)),new RefBox("finish",new GAIGSRefStruct(arr11))};
		RefBox[] rootarr8 = {new RefBox("segment",new GAIGSRefStruct(arr12))};
		show.writeSnap("Ref Struct Test Slide 8",null,"index.php?line=8",new GAIGSRefStruct(rootarr8));
		
		show.close();
	}
}