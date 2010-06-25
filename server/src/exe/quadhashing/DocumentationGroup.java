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

package exe.quadhashing;

import java.io.*;
import java.util.*;
import java.net.*;

public class DocumentationGroup{
    
    private String docPath = "";
    private String folderPath = "";
    private Vector linesOfTempFile;
    private Hashtable HtmlFileNamesKeyedByIds;
    private PrintWriter outScript;
    private int fileCounter;
    private String visType;
    
    public final String DELIM = "\0";
    public final String BLANK = "**BLANK**";
    
    public DocumentationGroup(String scriptFileName, PrintWriter outToScriptFile){
        outScript = outToScriptFile;
        HtmlFileNamesKeyedByIds = new Hashtable();
        fileCounter = 0;
        linesOfTempFile = new Vector();
        boolean goodInput = true;
        String uid = "";
        String folderName = "";
        String fullFolderName = "";
        int dotIndex = scriptFileName.lastIndexOf(".");
        int slashIndex = scriptFileName.lastIndexOf(File.separator);
        if(dotIndex != -1){
            System.out.println(scriptFileName.substring(dotIndex+1).trim());
            if((scriptFileName.substring(dotIndex+1).trim()).equalsIgnoreCase("sho"))
                visType = "gaigs";
            else if((scriptFileName.substring(dotIndex+1).trim()).equalsIgnoreCase("sam"))
                visType = "samba";
            else
                visType = "animal";
            System.out.println(visType);
            if(slashIndex != -1)
                uid = scriptFileName.substring(slashIndex + 1, dotIndex);
        }
        else goodInput = false;
        int uidIndex = scriptFileName.indexOf("uid");
        if(uidIndex != -1)
            folderPath = scriptFileName.substring(0, uidIndex);
        else goodInput = false;
        folderName = uid+"_docs";
        fullFolderName = folderPath+"doc"+File.separator+folderName;
        System.out.println("scriptFileName: "+scriptFileName);
        System.out.println("dotIndex: "+dotIndex);
        System.out.println("slashIndex: "+slashIndex);
        System.out.println("uid: "+uid);
        System.out.println("uidIndex: "+uidIndex);
        System.out.println("folderPath: "+folderPath);
        System.out.println("folderName: "+folderName);
        System.out.println("fullFolderName: "+fullFolderName);
        if(goodInput){
            try{
                (new File(fullFolderName)).mkdir();
                docPath = folderName + File.separator;
            }
            catch (Exception e){
                System.out.println(e.toString() + " thrown from construct DocumentationGroup");
                e.printStackTrace();
            }  
        }
    }
    
    public boolean setHtmlTemplate(String fullFileNameOfHtmlTemplateFile){
        linesOfTempFile.removeAllElements();
        try{
            BufferedReader in = new BufferedReader(new FileReader(fullFileNameOfHtmlTemplateFile));
            String temp = in.readLine();
            while(temp != null){
                linesOfTempFile.addElement(temp);
                temp = in.readLine();
            }
            in.close();
            if(linesOfTempFile.size() > 0)
                return true;
        } catch(Exception e){
            System.out.println(e.toString() + " thrown from setHtmlTemplate("+fullFileNameOfHtmlTemplateFile+")");
            e.printStackTrace();
        }
        return false;
    }
    public void insertDocHere(String docId){
        String htmlFileName = docPath+(new Integer(fileCounter)).toString()+".html";
        HtmlFileNamesKeyedByIds.put(docId, htmlFileName);
        try{
            if(visType.equals("samba"))
                outScript.println("documentation "+htmlFileName);
            else if(visType.equals("gaigs"))
                outScript.println("VIEW DOCS "+htmlFileName);
            else
                outScript.println("documentation \""+htmlFileName+"\"");
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from insertDocHere("+docId+")");
            e.printStackTrace();
        }
        fileCounter++;
    }
    public void makeDoc(String docId, String replaceBlanksWithTheseStrings){
        StringTokenizer stzr = new StringTokenizer(replaceBlanksWithTheseStrings, DELIM);
        PrintWriter out;
        int blankLoc;
        String newLine = "";
        try{
            File htmlFile = new File(folderPath+"doc"+File.separator+(String)HtmlFileNamesKeyedByIds.get(docId));
            htmlFile.createNewFile();
            out = new PrintWriter(new FileOutputStream(htmlFile));
            for(int x = 0; x < linesOfTempFile.size(); x++){
                newLine = (String)linesOfTempFile.elementAt(x);
                blankLoc = newLine.indexOf(BLANK);
                while(blankLoc != -1){
                    if(!stzr.hasMoreTokens())
                        throw(new Exception("too many blanks, too few tokens")); 
                    newLine = newLine.substring(0, blankLoc)+stzr.nextToken()
                        +newLine.substring(blankLoc+BLANK.length());
                    blankLoc = newLine.indexOf(BLANK);
                }
                out.println(newLine);
            }
            out.close();
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from makeDoc("+docId+", ...)");
            e.printStackTrace();
        }
    }
    public void makeAndInsertDoc(String docId, String replaceBlanksWithTheseStrings){
        String htmlFileName = docPath+(new Integer(fileCounter)).toString()+".html";
        HtmlFileNamesKeyedByIds.put(docId, htmlFileName);
        try{
            if(visType.equals("samba"))
                outScript.println("documentation "+htmlFileName);
            else if(visType.equals("gaigs"))
                outScript.println("VIEW DOCS "+htmlFileName);
            else
                outScript.println("documentation \""+htmlFileName+"\"");
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from makeAndInsertDoc("+docId+", ...)");
            e.printStackTrace();
        }
        fileCounter++;
        StringTokenizer stzr = new StringTokenizer(replaceBlanksWithTheseStrings, DELIM);
        PrintWriter out;
        int blankLoc;
        String newLine = "";
        try{
            File htmlFile = new File(folderPath+"doc"+File.separator+(String)HtmlFileNamesKeyedByIds.get(docId));
            htmlFile.createNewFile();
            out = new PrintWriter(new FileOutputStream(htmlFile));
            for(int x = 0; x < linesOfTempFile.size(); x++){
                newLine = (String)linesOfTempFile.elementAt(x);
                blankLoc = newLine.indexOf(BLANK);
                while(blankLoc != -1){
                    if(!stzr.hasMoreTokens())
                        throw(new Exception("too many blanks, too few tokens")); 
                    newLine = newLine.substring(0, blankLoc)+stzr.nextToken()
                        +newLine.substring(blankLoc+BLANK.length());
                    blankLoc = newLine.indexOf(BLANK);
                }
                out.println(newLine);
            }
            out.close();
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from makeAndInsertDoc("+docId+", ...)");
            e.printStackTrace();
        }
    }
    public void showThisDocAgain(String docId){
        try{
            outScript.println("documentation "+(String)HtmlFileNamesKeyedByIds.get(docId));
        }catch(Exception e){
            System.out.println(e.toString()+" thrown from showThisDocAgain("+docId+")");
            e.printStackTrace();
        }
    }
}
