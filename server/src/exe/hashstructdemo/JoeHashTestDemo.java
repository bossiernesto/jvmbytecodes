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

package exe.hashstructdemo;

import exe.*;
import java.io.*;

public class JoeHashTestDemo
{
	public static void main(String args[]) throws IOException
	{
		ShowFile show = new ShowFile(args[0]);
		GAIGShashStruct hash = new GAIGShashStruct(0);
		show.writeSnap("Hash Table Test Slide 1",hash);
		String[] temp = {"black","red","blue","green"};
		hash = new GAIGShashStruct("","red",0.0,0.0,1.0,1.0,0.5,4,temp);
		show.writeSnap("Hash Table Test Slide 2",hash);
		hash = new GAIGShashStruct(4);
		show.writeSnap("Hash Table Test Slide 3",hash);
		hash.add(0,"tom");
		show.writeSnap("Hash Table Test Slide 4",hash);
		hash.add(0,"nate");
		show.writeSnap("Hash Table Test Slide 5",hash);
		hash.add(0,"andrew","red");
		show.writeSnap("Hash Table Test Slide 6",hash);
		hash.add(1,"mike","green");
		show.writeSnap("Hash Table Test Slide 7",hash);
		hash.add(3,"joe","blue");
		show.writeSnap("Hash Table Test Slide 8",hash);
		hash.add(3,"myles");
		show.writeSnap("Hash Table Test Slide 9",hash);
		show.close();
	}
}