package exe.jvmbytecodes;

import java.io.*;
import java.util.*;

/*
 * Compares two objects
 */
public class Compare implements Comparator {
	
	/*
	 * Compares two String objects
	 */
	public int compare(Object obj1, Object obj2) {
		String[] x, y;
		x = (String[]) obj1;
		y = (String[]) obj2;
		if (Integer.parseInt(x[0]) < Integer.parseInt(y[0]))
			return -1;
		else
			return 1;
	}
}
