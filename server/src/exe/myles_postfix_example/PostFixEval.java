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

package exe.myles_postfix_example;

import java.io.*;
import java.util.Scanner;
import exe.*;

/**
 * <p>This is a simple program designed to demonstrate the basic use of 
 * some of the support classes for generating GAIGS XML scripts,
 * namely <code>ShowFile</code>, <code>GAIGSstack</code>, and
 * <code>GAIGSqueue</code>.  The program creates a script file for the
 * visualization of postfix expression evaluation.  It first stores the
 * expression in a queue, then processes the expression, showing the
 * queue and the evaluation stack.  The queue is shown on the left and
 * the stack on the right.</p>
 * 
 * <p>This program expects two command line parameters.  The first should be
 * the name of the <code>ShowFile</code> to be created.  The second should
 * be a postfix expression, with its tokens separated by one or more spaces.</p>
 * 
 * <p>No expression error checking in this simple example!</p>
 * 
 * <p>Example Command Line Parameters: "postfix.sho","6 5 + 2 / 3 * 4 %"</p>
 * 
 * @author Myles McNally 
 * @version 5/28/06
 */

public class PostFixEval {

    static final String title = "Evaluation of Postfix Expressions";
    static final double titleFontSize = 0.05;
            
    public static void main(String args[]) throws IOException {
        
   //-------- Initialize the various data structures ------------------------------------------
   
        ShowFile show = new ShowFile(args[0]);
        GAIGSqueue queue = new GAIGSqueue("Remaining Formula", "#333333", 0, 0.2, 0.5, 0.7, 0.1);
        GAIGSstack stack = new GAIGSstack("Evaluation Stack", "#333333", 0.5, 0.2, 1, 0.7, 0.1);
                
   //-------- Load the queue with the postfix expression and create initial snapshot -----------
        
        Scanner scan = new Scanner(args[1]);
        while (scan.hasNext()) {
            queue.enqueue(scan.next());
        }       
        show.writeSnap(title, titleFontSize, queue, stack);
        
   //-------- Process postfix expression creating a snapshot for each item processed -------------

        scan = new Scanner(args[1]);            // scan the input again to process the expression
        while (scan.hasNext()) {                // - it's just easier this way because of "hasNextInt"!
            
            if (scan.hasNextInt()) {            // handle an integer
                stack.push(new Integer(scan.nextInt()));
                queue.dequeue();
             }
            
            else {                              // assume next item is an operator & handle
                int v2 = (Integer) stack.pop();
                int v1 = (Integer) stack.pop();
                queue.dequeue();
                String s = scan.next();
                switch (s.charAt(0)) {
                    case '+': stack.push(v1 + v2, "#CC0000"); break;
                    case '-': stack.push(v1 - v2, "#CC0000"); break;
                    case '*': stack.push(v1 * v2, "#CC0000"); break;
                    case '/': stack.push(v1 / v2, "#CC0000"); break;
                    case '%': stack.push(v1 % v2, "#CC0000"); break;
                }
            }
            
            show.writeSnap(title, titleFontSize, queue, stack);
         }
         
   //-------- Close the show file ---------------------------------------------------------------

         show.close();                    
    }
}

