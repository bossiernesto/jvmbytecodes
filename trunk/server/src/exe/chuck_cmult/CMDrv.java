
package exe.chuck_cmult;

import java.io.*;
import java.util.*;
import exe.*;
import exe.ShowFile;

public class CMDrv {
    public static void main(String[] args) throws IOException {
        // the arguments are the showFile name and the array p
        //the array elements are presented as a string separated by a blank space
        // such as "5 10 20 2 50"
        String pStr = args[1];
        StringTokenizer tokens = new StringTokenizer(pStr);
        int cnt = tokens.countTokens();
        int[] p = new int[cnt];
        int idx = 0;
        while (tokens.hasMoreTokens()) {
            p[idx] = Integer.parseInt(tokens.nextToken());
            idx++;
        }
        int sz = p.length-1;
        
        ShowFile show = new ShowFile(args[0]);
        ChainMult cm = new ChainMult(sz, p, show);
        cm.recur();
        cm.compute();
        show.close();
    }
}