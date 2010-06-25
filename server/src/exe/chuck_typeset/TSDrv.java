
package exe.chuck_typeset;

import java.io.*;
import java.util.*;
import exe.*;
import exe.ShowFile;

public class TSDrv {
    public static void main(String[] args) throws IOException {
        //the arguments for the args array are the showFile name,
        //a String that contains word lengths, one integer for each word, 
        //a String containing the maximum number of characters in a line Llen, 
        // and a String containing the space between words s in a line
        
        ShowFile show = new ShowFile(args[0]);
        
        String cStr = args[1];
        StringTokenizer tokens = new StringTokenizer(cStr);
        int cnt = tokens.countTokens();
        int[] l = new int[cnt];
        int idx = 0;
        while (tokens.hasMoreTokens()) {
            l[idx] = Integer.parseInt(tokens.nextToken());
            idx++;
        }
        int d = Integer.parseInt(args[2]);
        int s = Integer.parseInt(args[3]);
        TypeSet ts = new TypeSet(show, l, s, d);
        ts.recur(0,l.length-1);
        ts.solveTS();
        show.close();
    }
}