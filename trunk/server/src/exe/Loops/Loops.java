package exe.Loops;

import java.io.IOException;
import exe.*;
import java.lang.*;
import java.util.*;

public class Loops {
	
    public static void main(String args[]) throws IOException
    {

	String[] params = new String[2];
	params [0] = args[0];	// File name still here
	boolean simple_loops = true;

	if (args.length == 3 && args[2] != null) {
	    Hashtable hash = XMLParameterParser.parseToHash(args[2]);
	
	    // User has chosen experienced as level of difficulty
	    if("Experienced".equals((String)hash.get("Level Of Difficulty")))
		{
		    params[1] = "E";
		}
	    // Otherwise novice
	    else
		{
		    params[1] = "N";
		}

	
	    // User has chosen experienced as level of difficulty
	    if("Simple For Loops".equals((String)hash.get("Type of Algorithm")))
		{
		    simple_loops = true;
		}
	    // Otherwise novice
	    else
		{
		    simple_loops = false;
		}
	}

	//	NestedForLoopsWithArrays.main( args );   Old version used args

	if (!simple_loops) {
	    NestedForLoopsWithArrays.main( params ); // Now we pass in params instead.   
                                                 // In NestedForLoopWithArrays, you would test args[1] for "E" or "N" to determine experienced or novice.   
                                                 // When you run in "local mode" instead of off of the server at jhave.org, you would skip Loops entirely 
	                                         // and just run NestedForLoopsWithArrays from your ant build script, passing in "E" or "N" as the second parameter
	}
	else {
	    SingleForLoopPrintLn.main (params);
	}
    }

}
