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

package exe.indextest;

import exe.*;
import java.io.*;

public class JoeIndexTest
{
	public static void main (String args[]) throws IOException
	{
		ShowFile show = new ShowFile(args[0]);
		GAIGSNameValueIndex index = new GAIGSNameValueIndex();
		show.writeSnap("Index Test Slide 1",index);
		index.add("tom",new Integer(14));
		show.writeSnap("Index Test Slide 2",index);
		index.add("joe",new Integer(72));
		show.writeSnap("Index Test Slide 3",index);
		index.add("mike",new Integer(89));
		show.writeSnap("Index Test Slide 4",index);
		show.close();
	}
}