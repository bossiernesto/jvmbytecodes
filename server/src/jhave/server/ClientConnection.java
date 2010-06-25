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

package jhave.server;

import jhave.core.TransactionCodes;
import jhave.Algorithm;
import jhave.server.parser.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import java.util.logging.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.input.JDOMParseException;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * The thread for a single client connected to the server.  Every client connection
 * is entirely represented by a ClientConnection object.  When a client connects to
 * the server, a new ClientConnection is created, and when the client disconnects,
 * the ClientConnection for that user is destroyed.
 *
 * @author JRE
 */
public abstract class ClientConnection extends Thread implements TransactionCodes {
    /** Logger */
    Logger logger = Logger.getLogger(ClientConnection.class.getName());
    
    protected Socket sock;            /* The socket associated with this client */
    protected int uid;                /* This client's unique identifier */
    protected ObjectInputStream inStream;   /* Stream for receiving info from client */
    protected ObjectOutputStream outStream; /* Stream for sending info to client */
    protected String category;        /* The category for the client.  This is usually
     * based on the course name the student is taking,
     * but may be any name for which an algorithm listing
     * exists.
     */
    protected Vector algoList;        /* List of Algorithms in the current category */
    protected jhave.Algorithm lastAlgo;     /* The last Algorithm viewed */
    public java.util.Date pongTime; /* The time at which the last pong was received from
     * the client.
     */
    protected boolean scored;
    protected String login, ext;
    protected String givenInput;
    protected int current_num_id = -1;
    protected int curQuestion = 0;    /* The current question in the current scored script */
    protected int numQuestions;       /* The number of questions in the current scored script */
    protected int numQuestionsCorrect = 0;/* The number of questions answered correctly */
    protected Vector questions;       /* list of questions associated with the current scored script */
    protected Hashtable questionHash; /* hash of questions by hash id */
    protected String userLogin;       /* The scoredscript login for the user assoc with this connection */
    protected StringBuffer transcript;/* A copy of the test for accounting purposes */
    protected Connection db;          /* SQL Connection handle to the RDBMS */
    public boolean running;         /* Am I running or have I finished? */
    public boolean _running;        /* Internal version of running state flag */
    
    public static /*final*/ String WEBROOT/* = "http://143.44.66.146/gaigs2/"*/;
    /* The base URL for gaigs */
    public static final String HTMLROOT = "html_root" + File.separator;

    abstract void cmd_requestQuizLogin(StringTokenizer at) throws InvalidCommandException;
    abstract void cmd_quizCompleted(String xmlString);

    /*
     * Creates a new ClientConnection.
     * @param s The existing Socket for this client's connection
     * @param uid An identifier unique to this connection
     */
    public ClientConnection(Socket s, int uid) {
        WEBROOT = GaigsServer.WEBROOT;
        
        try {
            sock = s;
            this.uid = uid;
            pongTime = new java.util.Date();
            lastAlgo = null;
            inStream = new ObjectInputStream(s.getInputStream());
            outStream = new ObjectOutputStream(s.getOutputStream());
            outStream.flush();
            running = true;
            _running = true;
	    givenInput = new String("*");	// * used as flag when no parameters are specified
        } catch (StreamCorruptedException e) {
            logger.log(Level.SEVERE, "Constructor", e);
            die();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Constructor", e);
            die();
        }
    }
    
    /* Overload the run method of the Thread class.  We just sit here waiting for
     * the client to say something.  When it does, parse the request and figure out
     * what we're supposed to do.
     */
    public void run() {
        while (_running) {
            String cmd = (String)getObject();
            try { parseCommand(cmd); } catch (InvalidCommandException e) {
                logger.warning("Received an unknown command: " + cmd);
                e.printStackTrace();
                //FIXME: do we send an error message back or ignore it?
            }
        }
    }
    
    protected Object getObject() {
        try {
            Object obj = inStream.readObject();
            return obj;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "getObject", e);
            killConnection("You speak Greek; I don't");
        } catch (OptionalDataException e) {
            logger.log(Level.SEVERE, "getObject", e);
            killConnection("You are spewing garbage into my beautiful stream.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "getObject", e);
            killConnection("Communication Error.");
        }
        return null; //This should only happen if an exception occured.
    }
    
    /**
     * Parses a String containing a command and takes appropriate action (i.e., executes
     * the command specified in the String).  Valid commands are documented in
     * the file commprot.java.  Each command is a String containing single-word
     * tokens each separated by whitespace.  The first token is always a number
     * indicating the type of message being sent.  The rest of the tokens, if any,
     * depend on what numeric token the message begins with.
     *
     * @param cmd The String containing the command to be parsed.
     * @exception java.lang.InvalidCommandException if the cmd is unrecognized or has
     * bad parameters.
     */
    public synchronized void parseCommand(String cmd) throws InvalidCommandException {
        if (cmd == null) return; // Is this a bad thing? (FIXME?)
        pongTime = new java.util.Date();   // update the pongTime so we don't ping an active client
        
        try {
            StringTokenizer st = new StringTokenizer(cmd, " \t");
            String token = st.nextToken();
            int numeric = Integer.parseInt(token);
            logger.info(cmd);
	    switch (numeric) {
                case 1:  cmd_finalizeConnection(st); break;
                case 2:  cmd_updatePong(st); break;
                case 3:  cmd_sendScript(st); break;
                case 4:  cmd_generateScript(st); break;
                case 5:  cmd_scoreQuestion(st); break;
	        case 6:  cmd_requestQuizLogin(st); break;
		case 7:  cmd_quizCompleted(cmd.substring(2)); break;
		/*case 6:  cmd_instructorLogin(st); break;
                case 7:  cmd_getStudentList(st); break;
                case 8:  cmd_getQuizList(st); break;
                case 9:  cmd_getAnswerAndVisualType(st);break;
                case 10: cmd_acceptConnection(); break;
                case 11: cmd_changePassword(st); break;
                case 12: cmd_addStudent(st); break;
                case 13: cmd_addQuiz(st); break;
                case 14: cmd_addCourse(st); break;
                case 15: cmd_deleteStudent(st); break;
                case 16: cmd_deleteQuiz(st); break;
                case 17: cmd_deleteCourse(st); break;
                case 18: cmd_getQuizGrades(st); break;
                case -99: ping(); break;*/
                default: throw new InvalidCommandException(numeric +": Unknown numeric");
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "parseCommand", e);
            throw new InvalidCommandException("empty/incomplete command?");
        }
    }
    
    /* Finalizes the negotiation phase between a client and the server.  Once this
     * method has been executed, an active connection exists between the two parties.
     * Upon receipt of this message, we immediately read a Vector of Algorithms containing
     * all the algorithms the client expects us to be aware of.
     */
    protected void cmd_finalizeConnection(StringTokenizer st) throws InvalidCommandException {
        logger.entering(getClass().getName(), "cmd_finalizeConnection");
        
        try {
            category = st.nextToken();
	    userLogin = st.nextToken();
	    algoList = (Vector)getObject();
            sendtoClient("100 Client and server connected with user login " + userLogin ); //connection acknowledgement (FIXME)
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "cmd_finalizeConnection", e);
            throw new InvalidCommandException("No category specified");
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /* Updates the last recorded pongTime to be now. */
    protected void cmd_updatePong(StringTokenizer st) throws InvalidCommandException {
        logger.entering(getClass().getName(), "cmd_updatePong");
        
        //note: we ignore any parameters sent in the pong.  so what?
        pongTime = new java.util.Date();
    }
    
    /* sends the requested pre-existing script to the client */
    protected void cmd_sendScript(StringTokenizer st) throws InvalidCommandException {
        logger.entering(getClass().getName(), "cmd_sendScript");
        
        try {
            String scriptname = st.nextToken();
            String visualizer = getAlgo(scriptname).GetVisualizerType();
            String script = getScript(scriptname, visualizer);
            if (script != null){
                transcript = null;
                if (st.hasMoreTokens()) {
                    String type = st.nextToken();
                    if (type.equals("scored")) {
                        scored = true;
                        if ((script = prepareScoredScript(script, scriptname + st.nextToken("\r\n")))
                        == null)
                            return;
                    } else scored = false;
                }
                sendtoClient("300 " + scriptname + " " + visualizer);
                sendtoClient(script);
            } else {
                sendtoClient("301 Unable to locate specified script ("
                        + scriptname + "); check the file name in the list file.");
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "cmd_sendScript", e);
            throw new InvalidCommandException("No scriptname specified");
        }
    }
    protected void CheckNameAndPass(String nameAndPass) throws InvalidNameOrPassException{
        try{
            StringTokenizer st = new StringTokenizer(nameAndPass);
            String login = st.nextToken();
            String passwd = st.nextToken();
            
            //register with and open a connection to the RDBMS
            Class.forName(GaigsServer.DBDRIVER);
            db = DriverManager.getConnection(GaigsServer.DBURL,
                    GaigsServer.DBLOGIN,
                    GaigsServer.DBPASSWD);
            
            Statement stmt = db.createStatement();
            
            ResultSet rs = stmt.executeQuery(
                    "select password from student where login = '"+ login + "' ");
            
            if (rs == null) {
                // find user in the instructor table. (It's messy this way but union syntax is not implemented in this version of mysql yet. It's in mysql 4.0.0)
                rs = stmt.executeQuery(
                        "select password from instructor where login = '"+ login + "' ");
                
                if (rs == null) {
                    throw new InvalidNameOrPassException();
                }
            }
            
            rs.next();
            String dbpass = rs.getString(1);
            if (!dbpass.equals(passwd)) {
                throw new InvalidNameOrPassException();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "CheckNameAndPass", e);
            sendtoClient("407 " + e.getMessage());
            throw new InvalidNameOrPassException();
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "CheckNameAndPass", e);
            sendtoClient("408 No database driver available; unable to record scores.");
            throw new InvalidNameOrPassException();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "CheckNameAndPass", e);
            sendtoClient("408 Couldn't prepare visualization for scoring.");
            throw new InvalidNameOrPassException();
        }
    }
    /* evaluates a command to generate a new script for a specified algorithm */
    protected void cmd_generateScript(StringTokenizer st) throws InvalidCommandException {
        logger.entering(getClass().getName(), "cmd_generateScript");
        
        try {
            String algoname = st.nextToken();
            String visualizer = getAlgo(algoname).GetVisualizerType();
            String datafile;
            String paramlst = null;
            String nameAndPass = null;
            boolean genData;
            
            //Check if we should use an existing data file or create a new one
            String tok = st.nextToken();
            if (tok.equalsIgnoreCase("new")) {
                genData = true;
                scored = false;
            } else if (tok.equalsIgnoreCase("scored")) {
                genData = scored = true;
            } else {
                genData = scored = false;
            }
            if (!genData) datafile = ".dat";
            else datafile = null;
            
            //Does the algorithm specified have any special input?
            if(scored){
                if ((st.hasMoreTokens())&&(nameAndPass == null))
                    nameAndPass = st.nextToken();
                if(st.hasMoreTokens())
                    nameAndPass += " " + st.nextToken();
            }
            while(st.hasMoreTokens()){
                if(paramlst == null)
                    paramlst = st.nextToken();
                else paramlst += " "+ st.nextToken();
            }
            if (!genData) {
                /* Make sure that the datafile is acceptable */
                try {
                    jhave.Algorithm algo = getAlgo(algoname);
                    if (!algo.IsAlgoFriend(lastAlgo))
                        sendtoClient(
                                "404 Existing data set doesn't or is incompatible with this algorithm");
                } catch (Exception e) {
                    sendtoClient("404 Data!  I need data!");
                }
            }
            
            try{
                String script;
                /* In the case of a scored script, the nameAndPass is used for authentication
                 * information rather than parameters to the script generator.
                 */
                if (scored) CheckNameAndPass(nameAndPass);
//                 script = genScript(Algorithm.GetAlgoNameMinusIngen(algoname), visualizer, datafile, paramlst);
                script = genScript(algoname, visualizer, datafile, paramlst);
                
                if (script != null) {
                    transcript = null;
                    if (scored)
//                         if ((script = prepareScoredScript(script,  Algorithm.GetAlgoNameMinusIngen(algoname)+" "+nameAndPass+" "+paramlst)) == null)
                        if ((script = prepareScoredScript(script,  algoname+" "+nameAndPass+" "+paramlst)) == null)
                            return;
		    //		    sendtoClient("400 " + Algorithm.GetAlgoNameMinusIngen(algoname) + " " + visualizer);
		    sendtoClient("400 " + algoname + " " + visualizer);
                    sendtoClient(script);
                } else {
                    sendtoClient("402 An error occured trying to run the requested algorithm.");
                }
            } catch (AlgorithmRequiresInputException e) {
                logger.log(Level.SEVERE, "cmd_generateScript", e);
                sendtoClient("401 " + algoname);
                sendtoClient(WEBROOT + "ingen/" + algoname + ".igs");
            } catch (InvalidNameOrPassException e) {
                logger.log(Level.SEVERE, "cmd_generateScript", e);
                sendtoClient("406 " + algoname);
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "cmd_generateScript", e);
            throw new InvalidCommandException("Incomplete parameter list");
        }
    }
    
    protected void cmd_scoreQuestion(StringTokenizer st) throws InvalidCommandException {
        try {
            String algoname = st.nextToken().trim();
            String qid = st.nextToken().trim();
            String response = st.nextToken("\r\n").trim();
            Statement stmt = db.createStatement();
            int num;
            
            if (curQuestion == 0) {
                /* The 0th question is actually the start of the script.  This question is what starts
                 * the timer and gets the test going.  Eventually this will send the key that unlocks
                 * everything up through the first question of the test, but since that functionality
                 * is not yet in place, we just send an acknowledgement.
                 */
                int count = stmt.executeUpdate(
                        "insert into scores (test_name, user_login, start_time, num_questions) values ('"
                        + algoname + "', '" + userLogin + "', now(), '" + numQuestions +"')");
                
                transcript.append("BEGINRESPONSES\n");
                
                if (count == -1){
                    sendtoClient("503 Score recording unavailable.");
                } else {
                    ResultSet rs = stmt.executeQuery(
                            "select unique_id from scores where user_login = '"+login+"' and test_name = '"+algoname+"' order by unique_id desc");
                    
                    if((rs != null)&&(rs.next()))
                        current_num_id = rs.getInt(1);
                    sendtoClient("501 theKey 1"); //FIXME
                    
                    _exec("cp html_root/uid/" + uid + ext + " StudentQuizzes/"+login+"/"+algoname+current_num_id+ext);
                }
                
            } else {
                /* We now are scoring a real question.  First we determine whether the response is correct.
                 * If it is, we increment the num_correct counter in the DB.  Otherwise, we have no
                 * bookkeeping to do. //FIXME we really ought to record the number of questions to which
                 * the user has responded in case the world comes to an end or something else goes wrong.
                 */
                
                
                transcript.append("QUESTION " + qid + "\n");
                transcript.append("<- " + response + "\n");
                num = ((Integer)questionHash.get(qid)).intValue();
                QuestionParser qp = (QuestionParser)questions.elementAt(num);
                if (qp.isCorrect(response)) {
                    logger.info("User " + uid + "answered correctly");
                    ++numQuestionsCorrect;
                    /*int count = stmt.executeUpdate(
                                             "update scores set num_correct = " + numQuestionsCorrect
                                             + " where test_name = '" + algoname
                                             + "' and user_login = '" + userLogin
                                             + "' and unique_id = " + current_num_id);*/
                    
                    transcript.append("RESULT 1\n");
                    /*if (count == -1){
                        sendtoClient("503 Couldn't record score.");
                    } else*/
                    if (curQuestion == numQuestions){
                        sendtoClient("502 theKey "+numQuestions+" "+numQuestionsCorrect+" 1");
                    } else {
                        sendtoClient("501 theKey 1");
                    }
                } else {
                    logger.info("User " + uid + " answered incorrectly.");
                    transcript.append("RESULT 0\n");
                    if (curQuestion == numQuestions)
                        sendtoClient("502 theKey "+numQuestions+" "+numQuestionsCorrect+" 0");
                    else
                        sendtoClient("501 theKey 0");
                }
                transcript.append("-> "+qp.getAnswer()+'\n');
                
                // Update respond transcript and number of correct questions.
                // Everytime a student answer the question
                int count = stmt.executeUpdate(
                        "update scores set num_correct = " + numQuestionsCorrect
                        + ", transcript = '" + transcript.toString()
                        + "' where test_name = '" + algoname
                        + "' and user_login = '" + userLogin
                        + "' and unique_id = " + current_num_id);
                
                if (count == -1)
                    sendtoClient("503 Couldn't record score.");
            }
            
            if (curQuestion == numQuestions) {
                transcript.append("ENDRESPONSES\n");
                
                //escape 's
                int idx = 0;
                while ( (idx = transcript.toString().indexOf('\'', idx)) < transcript.length()
                && idx != -1) {
                    transcript.insert('\'', idx);
                    idx += 2;
                }
                
                int count = stmt.executeUpdate(
                        "update scores set end_time = now(), "
                        + "transcript = '" + transcript.toString()
                        + "' where test_name = '" + algoname
                        + "' and user_login = '" + userLogin
                        + "' and unique_id = "+ current_num_id);
                
                stmt.close();
                db.close();
                db = null;
                
                //copy the script file to a permanent directory
                File scriptFile = new File("html_root/uid/" + uid + ext);
                scriptFile.renameTo(new File("StudentQuizzes/"+login+"/"+algoname+current_num_id+ext));
                
                
                logger.info("html_root/uid/" + uid + ext
                        + " should be renamed to "+"StudentQuizzes/" + login
                        + "/" + algoname+current_num_id + ext);
            }
            curQuestion++;
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "cmd_scoreQuestion", se);
            sendtoClient("503 " + se.getMessage());
        }
    }
    
    
    /* Prepare a scored script to be sent to the client and open connection to the RDBMS.
     * Return the URL at which the transcript can be found. */
    protected String prepareScoredScript(String scriptURL, String params) throws InvalidCommandException {
        /* Eventually this method will prepare the script by encrypting each question (including
         * the first) with a different key.  After the response to each question is received, the
         * key for the next question can be sent to the client, preventing a modified client from
         * allowing the user to fast-forward in the animation.  For the time being, however, no
         * encryption occurs.  This is a security flaw, but one which is acceptible for the
         * time being. FIXME
         */
        
        /* We'll first parse the script and extract the questions from it.  Then we
         * try to establish a connection and verify that the user/login pair is valid and that
         * the requested test is indeed available to be taken.  If anything fails along the way,
         * we return false, or throw an exception.
         */
        
        logger.fine("prepareScoredScript(" + scriptURL + ", " + params + ")");
        
        try {
            StringTokenizer st = new StringTokenizer(params);
            String algoname = st.nextToken();
            login = st.nextToken();
            String passwd = st.nextToken();
            
            
            // First we'll parse the script, but to do that, we'll need to get it from the scriptURL.
            StringBuffer scriptbuf = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(scriptURL).openStream()));
            String tmp;
            while ((tmp = in.readLine()) != null)
                scriptbuf.append(tmp + "\n");
            String script = scriptbuf.toString();
            
            numQuestions = 0;
            questions = new Vector();
            questionHash = new Hashtable();
            userLogin = login;
            transcript = new StringBuffer();
            String line = null;
            
            StringTokenizer scriptTkzr = new StringTokenizer(script, "\r\n");
            while ( scriptTkzr.hasMoreTokens() ) {
                line = scriptTkzr.nextToken();
                if (line.toUpperCase().startsWith("STARTQUESTIONS"))
                    break;
                if (line.toUpperCase().startsWith("MCQUESTION")
                || line.toUpperCase().startsWith("FIBQUESTION")
                || line.toUpperCase().startsWith("TFQUESTION")){
                    String qid = line.substring(line.indexOf((int)' ') + 1);
                    questionHash.put(qid, new Integer(numQuestions));
                    numQuestions++;
                }
            }
            questions.setSize(numQuestions);
            
            
            if (line == null || ! line.toUpperCase().startsWith("STARTQUESTIONS")){
                logger.finer(line);
                sendtoClient("405 Test contains no questions.");
                return null;
            }
            
            logger.finer("... " + line);
            while (scriptTkzr.hasMoreTokens() && ! line.equals("ENDQUESTIONS")) {
                line = scriptTkzr.nextToken();
                if (line.toUpperCase().startsWith("FIBQUESTION ")) {
                    String qid = line.substring(line.indexOf((int)' ') + 1);
                    int num = ((Integer)questionHash.get(qid)).intValue();
                    questions.set(num, new FIBQuestionParser(scriptTkzr));
                } else if (line.toUpperCase().startsWith("MCQUESTION")) {
                    String qid = line.substring(line.indexOf((int)' ') + 1);
                    int num = ((Integer)questionHash.get(qid)).intValue();
                    questions.set(num, new MCQuestionParser(scriptTkzr));
                } else if (line.toUpperCase().startsWith("TFQUESTION")) {
                    String qid = line.substring(line.indexOf((int)' ') + 1);
                    int num = ((Integer)questionHash.get(qid)).intValue();
                    questions.set(num, new TFQuestionParser(scriptTkzr));
                } else {
                    logger.finer(">>> " + line);
                    sendtoClient("405 Could not grok questions.");
                    return null;
                }
            }
            
            String visualizer = getAlgo(algoname).GetVisualizerType();
            ext = (String)GaigsServer.visualizerExtensionTab.get(visualizer.toLowerCase());
            
            //register with and open a connection to the RDBMS
            Class.forName(GaigsServer.DBDRIVER);
            db = DriverManager.getConnection(GaigsServer.DBURL,
                    GaigsServer.DBLOGIN,
                    GaigsServer.DBPASSWD);
            
            Statement stmt = db.createStatement();
            
            //Now let's just verify that the test exists
            ResultSet rs = stmt.executeQuery(
                    "select num_questions from test where name = '" + algoname + "'");
            
            if (rs == null) {
                sendtoClient("405 Test not scorable");
                return null;
            }
            
            rs.next();
            stmt.close();
            
            //If we're still here, login was successful; do some bookkeeping
            curQuestion = 0;
            numQuestionsCorrect = 0;
            // return WEBROOT + "uid/" + uid + ext; ERROR -- MUST RETURN SCRIPT URL
            return scriptURL;
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "prepareScoredScript", e);
            throw new InvalidCommandException("No login/password provided for scored script");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "prepareScoredScript", e);
            sendtoClient("407 " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "prepareScoredScript", e);
            sendtoClient("408 No database driver available; unable to record scores.");
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "prepareScoredScript", e);
            sendtoClient("408 Couldn't prepare visualization for scoring.");
            return null;
        }
    }
    
    /* Sends the client a ping */
    protected void ping() {
        sendtoClient("200 Ping? Pong!");
    }
    
    /* Kills connection due to pong out */
    public void pongOut() {
        killConnection("101 Ping timeout.");
    }
    
    /* Cleanly terminates a connection to a client, sending message as the reason for
     * termination.
     */
    protected void killConnection(String message) {
        logger.fine("killConnection: " + message);
        
        try {
            sendtoClient("101 " + message);
        } catch (Exception e) {
            //The connection's probably already been sufficiently toasted.
        }
        die();
    }
    
    protected void die(String message) {
        System.err.println("\n" + message + "\n");
        die();
    }
    
    protected void die() {
        logger.fine("Killing client " + uid);
        
        _running = false; //set internal flag to indicate we're dying
        
        try {
            outStream.close();
        } catch (Exception e) {
            //We've got one fubar'd connection.  Oh well.
        }
        try {
            inStream.close();
        } catch (Exception e) {}
        /* Well, at least we've closed all our streams; now let's clean up our mess in the
         * filesystem.  The next few lines mercilessly wipe out anything starting with our
         * uid.
         */
        
        File uiddir = new File("html_root/uid");
        String files[] = uiddir.list(new uidFileFilter());
        if(files != null)
            for (int i = 0; i < files.length; i++)
                new File(uiddir, files[i]).delete();
        
        File DocsDir = new File("html_root/doc/"+uid+"_docs");
        String uidDocDir[] = DocsDir.list();
        for (int i = 0; i < uidDocDir.length; i++)
            new File(DocsDir, uidDocDir[i]).delete();
        if(DocsDir != null)
            DocsDir.delete();
        running = false; //we're done.  Now we wait for the next ping run for Gaigs to purge us
    }
    
    // File Filter to match /$uid\..*/
    class uidFileFilter implements FilenameFilter {
        public boolean accept(File dir, String file) {
            if (file.startsWith(uid + ".")) return true; //accept the file
            return false;
        }
    }
    
    /* Sends obj to the client at the other end of the line.
     */
    protected void sendtoClient(Object obj) {
        if (obj.getClass().getName().equals("java.lang.String")) {
            logger.fine("sendtoClient - " + (String)obj);
        } else {
            logger.fine("sendtoClient - " + obj.getClass().getName());
        }
        
        try {
            outStream.writeObject(obj);
            outStream.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "sendtoClient", e);
            die("Write error to client " + uid + "."); //Terminate connection to client
        }
    }
    
    /**
     * Wrapper for sendtoClient(Object obj) that supports logging.
     *
     * @param str - the message to send to the client
     * @param log - the buffer to which to record the message sent (with a newline tacked on)
     */
    protected void sendtoClient(String str, StringBuffer log) {
        sendtoClient(str);
        log.append(str + "\n");
    }
    
    /* Returns a URL to the specified script which can be viewed with
     * the specified visualizer (actually, the URL is dependent on the visualizer
     * specified).  If something has gone disastrously wrong and the script
     * for that visualizer does not exist, then a thousand thousand black holes
     * will open within the atmosphere of the earth, our heavenly body will tear
     * herself apart, and generally bad things will happen.  Or maybe we'll just
     * return a null pointer instead.
     */
    protected String getScript(String script, String visualizer) {

        String userDir = System.getProperty("jhave.server.userdir");
        String baseparam = HTMLROOT + "script" + File.separator;

        logger.fine("getScript(" + script + ", " + visualizer + ") - ");
        
        if ( !sanitized(script) ) {
            /* Ack!  script contains nasty characters; claim it doesn't exist */
            logger.warning("Script contains characters that cannot be parsed");
            return null;
        }
        String ext = "";
        if(!visualizer.equalsIgnoreCase("animal") && !visualizer.equalsIgnoreCase("animalscript"))
            ext = (String)GaigsServer.visualizerExtensionTab.
                    get(visualizer.toLowerCase());

//         File prefab = new File("../html_root" + File.separator + "script" + File.separator
//                 + script + ext);
//         if (prefab.exists()) {
//             logger.fine(WEBROOT + "script/" + script + ext);
//             return WEBROOT + "script/" + script + ext;
//         }
//         

	File prefab = new File(userDir + baseparam + script + ext);
        if (prefab.exists()) {
            logger.fine(WEBROOT + HTMLROOT + "script/" + script + ext);
            return WEBROOT + HTMLROOT + "script/" + script + ext;
        }
        
        logger.warning("Static script could not be found: " + prefab.getAbsolutePath());
        return null;
    }
    
    /* Returns a URL to the script for the specified visualizer.  The script is created
     * by running the algorithm specified in algo with a specified datafile, or, if
     * datafile is null, a generated datafile.  If params is not null, it will be used as
     * input to the algorithm; otherwise the algorithm will be executed without any input.
     * If an invalid algorithm name is provided or if for any other reason a scriptfile
     * cannot be generated, then null is returned. If the specified algorithm requires
     * input and datafile is null or does not exist, then an
     * AlgorithmRequiresInputException is thrown.
     */
    protected String genScript(String algo, String visualizer, String datafile, String params)
    throws AlgorithmRequiresInputException {
        logger.entering(getClass().getName(), "genScript");
        
        String userDir = System.getProperty("jhave.server.userdir");
        String paramlstparam = "*";
        String datafileparam = "*";
        String baseparam = HTMLROOT + "uid" + File.separator + uid;
        
        /* First, we check to see if we have an input generator for this algorithm.  If
         * we do, then this algo requires input, so we had better make sure we have input.
         * If we don't, throw an AlgorithmRequiresInputException (good thing we almost
         * never use it, since it's a beast to type).  If we do have input, then we pass it
         * in to the algo.
         *
         * If we don't have an input generator, then we can safely set the 3rd parameter to
         * the algo to '*'.  If datafile is null, then we set the second param to the algo
         * to '*'.  If datafile is defined, we simply pass it in as the 2nd param.  The first
         * param must be the root to our files (i.e. "html_root/uid/myuid").
         *
         * Once we have our parameters assembled and have executed the algo, we make sure that
         * we got a scriptfile out of the process.  If we did, return a URL to it.  If not,
         * return null to indicate that no scriptfile was able to be created.
         */
        
        if (! sanitized(algo) ) {
            /* algo contains nasties; bad client!  no cookie for you */
            logger.warning("Algorithm is not sanitized");
            return null;
        }
        
        File ingen = new File("html_root" + File.separator + "ingen" + File.separator
                + algo + ".igs"); // igs == input generator script
        if (ingen.exists() && params == null) {
            if (getAlgo(algo).GetAlwaysNeedsInputGenerator() || datafile == null) {
                //algorithm needs input, but none provided
                throw new AlgorithmRequiresInputException();
            }
        }
        
	givenInput = "*";

        if (params != null) 
	{
	    paramlstparam = params;
	    givenInput = paramlstparam;
	}
        if (datafile != null) datafileparam = datafile;
        try {
	    logger.info(userDir+baseparam+" "+datafileparam);
            exec("exe" + File.separator + Algorithm.GetAlgoNameMinusIngen(algo) + File.separator, Algorithm.GetAlgoNameMinusIngen(algo)/* + ".exe"*/,
                    userDir + baseparam + " " + datafileparam + " " + paramlstparam);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "genScript", e);
            return null;
        }catch(Exception error){
	    logger.log(Level.SEVERE, "error", error);
	    return null;
	}
        
        /* We should now have a script file handy.  Let's make sure and return it if
         * we indeed do.  If not, well, you can't win them all.
         */
        String ext = (String)GaigsServer.visualizerExtensionTab.get(visualizer.toLowerCase());
        
        File script = new File(userDir + HTMLROOT + File.separator + "uid"
                + File.separator + uid + ext);
        logger.fine("Loading dynamic script: " + script.getAbsoluteFile());
//         if (script.exists()) {
//             logger.fine(WEBROOT + "uid/" + uid + ext);
//             lastAlgo = getAlgo(algo);
//             return WEBROOT + "uid/" + uid + ext;
        if (script.exists()) {
            logger.fine(WEBROOT + HTMLROOT + "uid/" + uid + ext);
            lastAlgo = getAlgo(algo);
            return WEBROOT + HTMLROOT + "uid/" + uid + ext;
        } else {
            logger.warning("Could not locate the generated script: "
                    + script.getAbsolutePath());
            return null;
        }
        //This is the end, my friend.
    }
    
    /* Executes the first executable program found with the prefix exe in the
     * directory dir.  If dir is "/algos" and exe is "foobar_dat" then the first
     * program found of "/algos/foobar_dat.exe", "/algos/foobar_dat.class", and
     * "/algos/foobar_dat.jar" will be run (the latter two cases through the
     * java interpreter. Returns the exit code of the executed program.

     * The premise here is that exe contains the name of the program
     * to execute.  If the original algo contains an input generator
     * spec, a pre-condition to calling this is that the input get has
     * been stripped by called Algorithm.GetAlgoNameMinusIngen. (TN 9/8/07)
     */
    protected void exec(String dir, String exe, String param) throws IOException {
        // This is ugly, but it'll have to do
        File root = new File(System.getProperty("jhave.server.scriptroot"));
        
        logger.fine("Up front with user " + " " + userLogin + " looking for -- " + dir + exe);
        File algo = new File(root, dir + File.separator + exe + ".exe");
        if (algo.exists()) {
            logger.finer("Executing -- " + dir + exe + ".exe " + param);
            _exec(dir + exe + ".exe " + param); //normal executables
            return;
        }
	algo = new File(root, dir + exe + ".pl");
	if (algo.exists()) {
            logger.finer("Executing -- " + "perl " + dir + exe + ".pl" + " " + param);
            _exec("perl " + dir + exe + ".pl" + " " + param); //perl scripts
            return;
	}
	algo = new File(root, dir + exe + ".class");
        if (algo.exists()) {
	    // Replace '/' characters with '.' characters because Java uses
	    // '.' characters.
	    String dotdir = dir.replace('/', '.');
	    String dotexe = exe.replace('/', '.');

	    int xml_index;

	    if((xml_index = param.indexOf("<?xml version")) != -1){
		String file = param.trim();
		int index = file.indexOf(' ');
		file = file.substring(0, index) + ".xml";

		String xml_file = param.substring(xml_index);

		PrintWriter xmlout;
		try{
		    xmlout = new PrintWriter
			(new BufferedWriter(new FileWriter(file)));
		    // Write the XML input generator to the temporary file.
		    xmlout.print(xml_file);
		    xmlout.close();
		}catch(IOException bad){
		    logger.log(Level.SEVERE, 
			       "Problem opening temporary XML file: ", bad);
		}

		param = param.substring(0, xml_index) + file;
	    }


	    /*	    if(paramlst != null && paramlst.indexOf("<?xml version") != -1){
		PrintWriter xmlout;

		}*/

	    // Classpath inserted for parsing XML input generators.
            logger.finer("Executing -- " + "java -classpath ../../lib/jdom.jar:../../lib/jaxen.jar:. " + dotdir + dotexe + " " + param);
            _exec("java -classpath ../../lib/jdom.jar:../../lib/jaxen.jar:. " + dotdir +  dotexe + " " + param); //Java .class files
            return;
        }
        algo = new File(root, dir + exe + ".jar");
        if (algo.exists()) {
            logger.finer("Executing -- " + "java " + dir + exe + " " + param);
            _exec("java " + dir + exe + " " + param); //Java archives
            return;
        }
        algo = new File(root, dir + exe + ".bat");
        if (algo.exists()) {
            logger.finer("Executing -- " + dir + exe + ".bat" + " " + param);
            _exec(dir + exe + ".bat" + " " + param); //batch files
            return;
        }
        algo = new File(root, dir + exe);
        if (algo.exists()) {
            logger.finer("Executing -- " + dir + exe + " " + param);
            _exec(dir + exe + " " + param); //suffix-less executables (#!/bin/sh)
            return;
        }
        throw new IOException("Could not find executable");
    }
    
    /* Protected method that executes a command.  This is where the real execution of a program
     * occurs.  exec() is merely a wrapper to this method; for most purposes in this application,
     * exec() ought to be called.  This method should probably not be called directly, though
     * nothing terribly catastrophic will happen if it is.  Caveat executor.
     */
    protected int _exec(String cmd) {
        String root = System.getProperty("jhave.server.scriptroot");
        Runtime rt = Runtime.getRuntime();
	StringBuffer outBuf = null;
	InputStream inStream = null;
	int ch;
        try {
            logger.fine("exec(" + cmd + ")" + "\n..........with script root " + root);
            

            Process prog = rt.exec(cmd, null, new File(root));
            logger.fine("Process started");
	    // This loop is for nasty script producers that write a
	    // lot of debugging output to the console.  Without it
	    // there is interaction between that ouput and the waitFor
	    // that causes the Process to never finish from waitFor's
	    // perspective.
	    inStream = prog.getInputStream();
	    outBuf = new StringBuffer();
	    while ((ch = inStream.read()) != -1)
		{
		    outBuf.append((char)ch + "");
		}
	    //	    System.out.println(outBuf.toString());
	    // End of loop for nasty script producers
            prog.waitFor(); //wait until the program has finished execution
            logger.fine("And completed");
            return prog.exitValue();
        }
	catch (InterruptedException e) { logger.fine(e.toString()); System.err.println(e.toString()); } 
        catch (IOException e) { logger.fine(e.toString()); System.err.println(e.toString()); }
	catch (SecurityException e) {
            System.err.println("Security Exception caught trying to run " + cmd + ": "
                    + e.getMessage());
	    logger.fine(e.toString()); System.err.println(e.toString());
        }
	catch (Exception e) { logger.fine(e.toString()); System.err.println(e.toString()); }

        return -999; //hopefully this value will never be returned (we could only be so lucky)
    }
    
    /* returns true if the file is sanitized, or false otherwise.  A file is considered to
     * be sanitized if it doesn't contain '/', or '\'.  This is probably a sub-optimal check.
     * If you know of a better better way, please let me know (FIXME?)
     * -JRE 25/jul/99
     */
    protected boolean sanitized(String file) {
//        if (file.indexOf('/') != -1 || file.indexOf('\\') != -1)
//            return false;
//        return true;
//         return file.indexOf('/') == -1 && file.indexOf('\\') == -1;
	return true;		// With the scheme for multiple input generators, we now allow path separators
    }
    
    /* Returns an Algorithm from the name of the algorithm (algo) */
    protected jhave.Algorithm getAlgo(String algo) throws NoSuchElementException {
        for (int i = 0; i < algoList.size(); i++) {
	    //	    System.out.println(((jhave.Algorithm)algoList.elementAt(i)).GetAlgoName() + " comparing to " + algo);
            if (((jhave.Algorithm)algoList.elementAt(i)).GetAlgoName().equalsIgnoreCase(algo))
                return (jhave.Algorithm)algoList.elementAt(i);
	}
        /* If we're still here, we didn't find what we were looking for.  C'est la vie */
        throw new NoSuchElementException();
    }
    
    
    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    /* Code in between the lines is addtion to ClientConnection.java to handle instructor mode client*/
    
    protected DBTransaction instDB = new DBTransaction();		// create an instance of DBTransaction class
    
    /**
     * Login instructor.
     * Sends back 600 to client if login successfull or 601 if login fail.
     * @param namePass a stringTokenizer containing the instructor's name and password
     * @see DBTransaction.instructorLogin
     */
    protected void cmd_instructorLogin(StringTokenizer namePass) {
        String name = namePass.nextToken();
        String pass = namePass.nextToken();
        Vector courseList;
        
        try {
            instDB.instructorLogin(name, pass);
            courseList = instDB.getCourseList(name);
            sendtoClient("600");
            sendtoClient(courseList);
        } catch (InvalidDBRequestException e) {
            sendtoClient("601 "+e.getMessage());
        }
    }
    
    /**
     * Get the quiz URL, student answer, unique id, number of questions, number of questions answered correctly and visualization type
     * of the quiz that was taken by the student
     * (Quiz name and student's user name are sent by the client)
     * Send back to client 900 if succeed or 901 if fails
     * @param studentQuiz a student tokenizer that contains the studen's user name, the quiz name, and the time the student started the quiz. Use '@' as token to retrieve the start time.
     * @see DBTransaction.getAnswerAndVisualType
     */
    protected void cmd_getAnswerAndVisualType(StringTokenizer studentQuiz) {
        Integer clientID  = new Integer(uid);
        String student    = studentQuiz.nextToken().trim();
        String quiz       = studentQuiz.nextToken().trim();
        String startTime  = studentQuiz.nextToken("@").trim();
        String answer;
        String uniqueID;
        String numQuestions;
        String numCorrect;
        String visualType;
        String visualURL;
        StringTokenizer quizInfo;
        
        try {
            quizInfo = instDB.getAnswerAndVisualType(student, quiz, startTime);
            answer = quizInfo.nextToken();
            uniqueID = quizInfo.nextToken();
            numQuestions = quizInfo.nextToken();
            numCorrect = quizInfo.nextToken();
            visualType = quizInfo.nextToken();
            
            // construct quiz URL
            visualURL = WEBROOT+"uid/"+clientID.toString().trim();
            if (visualType.equalsIgnoreCase("Gaigs")) {
                visualURL = visualURL + ".sho";
                _exec("cp StudentQuizzes/"+student+"/"+quiz+uniqueID+".sho html_root/uid/"+clientID.toString().trim()+ ".sho");
            } else if (visualType.equalsIgnoreCase("Samba")) {
                visualURL = visualURL + ".sam";
                _exec("cp StudentQuizzes/"+student+"/"+quiz+uniqueID+".sam html_root/uid/"+clientID.toString().trim()+".sam");
            } else if (visualType.equalsIgnoreCase("AnimalScript")) {
                visualURL = visualURL + ".asu";
                _exec("cp StudentQuizzes/"+student+"/"+quiz+uniqueID+".asu html_root/uid/"+clientID.toString().trim()+ ".asu");
            } else if (visualType.equalsIgnoreCase("Animal")) {
                visualURL = visualURL + ".aml";
                _exec("cp StudentQuizzes/"+student+"/"+quiz+uniqueID+".aml html_root/uid/"+clientID.toString().trim()+ ".aml");
            }
            
            sendtoClient("900 "+visualURL+"@"+numQuestions+" "+numCorrect+" "+answer+"@"+visualType);
        } catch (InvalidDBRequestException e) {
            sendtoClient("901 "+e.getMessage());
        }
    }
    
    /**
     * Create a list of quizzes for the instructor.
     * Send back 800 if succeed or 801 if fails.
     * @param instructorStudent a string tokenizer consists of the instructor's user name, or the instructor's user name and a student's user name
     * @see DBTransaction.getQuizList
     */
    protected void cmd_getQuizList(StringTokenizer instructorStudent) {
        String instructor = instructorStudent.nextToken().trim();
        String student;
        Vector quizList;
        
        if (instructorStudent.hasMoreTokens())
            student = instructorStudent.nextToken().trim();
        else
            student = new String();
        
        try {
            quizList = instDB.getQuizList(instructor, student);
            sendtoClient("800");
            sendtoClient(quizList);
        } catch (InvalidDBRequestException e) {
            sendtoClient("801 "+e.getMessage());
        }
    }
    
    /**
     * Create a list of students for the instructor.
     * Send back 700 if succeed or 701 if fails.
     * @param instructorQuiz a string tokenizer consists of the instructor's user name, or the instructor's user name and a quiz name
     * @see DBTransaction.getStudentList
     */
    protected void cmd_getStudentList(StringTokenizer instructorQuiz) {
        String instructor = instructorQuiz.nextToken().trim();
        String quiz;
        Vector studentList;
        
        if (instructorQuiz.hasMoreTokens())
            quiz = instructorQuiz.nextToken().trim();
        else
            quiz = new String();
        
        try {
            studentList = instDB.getStudentList(instructor, quiz);
            sendtoClient("700");
            sendtoClient(studentList);
        } catch (InvalidDBRequestException e) {
            sendtoClient("701 "+e.getMessage());
            return;
        }
    }
    
    /**
     * Add a student to a course
     * @param studentInfo a string tokenizer consists of the student's user name and the course id, or the student's user name, the course id, student's last name, first name, and initial password
     * @see DBTransaction.addStudent
     */
    protected void cmd_addStudent(StringTokenizer studentInfo) {
        String username = studentInfo.nextToken();
        String courseID = studentInfo.nextToken();
        String lastName;
        String firstName;
        String initPass;
        
        //if not specified, instatiate and initialize to empty string
        if (studentInfo.hasMoreTokens()) {
            lastName = studentInfo.nextToken();
            firstName = studentInfo.nextToken();
            if (studentInfo.hasMoreTokens())
                initPass = studentInfo.nextToken();
            else
                initPass = new String();
        } else {
            lastName = new String();
            firstName = new String();
            initPass = new String();
        }
        try {
            instDB.addStudent(courseID, username, lastName, firstName, initPass);
            sendtoClient("1200");
        } catch(InvalidDBRequestException e) {
            sendtoClient("1201 "+e.getMessage());
        } catch(FileFailureException e) {
            sendtoClient("1202 Error in creating new folder for student: "+username+" Please inform administrator!");
        }
    }
    
    /**
     * Add a quiz to a course
     * @param quizInfo a string tokenizer consists of the quiz name and the course id
     * @see DBTransaction.addQuiz
     */
    protected void cmd_addQuiz(StringTokenizer quizInfo) {
        String quizName = quizInfo.nextToken();
        String courseID = quizInfo.nextToken();
        
        try {
            instDB.addQuiz(quizName, courseID);
            sendtoClient("1300");
        } catch(InvalidDBRequestException e) {
            sendtoClient("1301 "+e.getMessage());
        } catch(FileFailureException e) {
            sendtoClient("1301 Fail to rewrite the menu for course: "+courseID);
        }
    }
    
    /**
     * Add a course to database
     * @param courseInfo a string tokenizer consists of the course number, course name, and instructor name, seperated with '@'
     * @see DBTransaction.addCourse
     */
    protected void cmd_addCourse(StringTokenizer courseInfo) {
        String courseNum  = courseInfo.nextToken("@").trim();
        String courseName = courseInfo.nextToken("@").trim();
        String instructor = courseInfo.nextToken("@").trim();
        Vector courseList;
        
        try {
            instDB.addCourse(courseNum, courseName, instructor);
            courseList = instDB.getCourseList(instructor);
            sendtoClient("1400");
            sendtoClient(courseList);
        } catch(InvalidDBRequestException e) {
            sendtoClient("1401 "+e.getMessage());
        }
    }
    
    /**
     * remove a student from a course
     * @param studentInfo a string tokenizer consists of the student's user name and course id
     * @see DBTransaction.deleteStudent
     */
    protected void cmd_deleteStudent(StringTokenizer studentInfo) {
        String username = studentInfo.nextToken();
        String courseID = studentInfo.nextToken();
        
        try {
            instDB.deleteStudent(username, courseID);
            sendtoClient("1500");
        } catch(InvalidDBRequestException e) {
            sendtoClient("1501 "+e.getMessage());
        }
    }
    
    /**
     * remove a quiz from a course
     * @param quizInfo a string tokenizer consists of the quiz name and course id
     * @see DBTransaction.deleteQuiz
     */
    protected void cmd_deleteQuiz(StringTokenizer quizInfo) {
        String quizName = quizInfo.nextToken();
        String courseID = quizInfo.nextToken();
        
        try {
            instDB.deleteQuiz(quizName, courseID);
            sendtoClient("1600");
        } catch(InvalidDBRequestException e) {
            sendtoClient("1601 "+e.getMessage());
        } catch(FileFailureException e) {
            sendtoClient("1601 Fail to rewrite the menu for course: "+courseID);
        }
    }
    
    /**
     * remove a course from the database
     * @param courseInfo a string tokenizer consists of the course number and instructor's user name
     * @see DBTransaction.deleteCourse
     */
    protected void cmd_deleteCourse(StringTokenizer courseInfo) {
        String courseNum  = courseInfo.nextToken().trim();
        String instructor = courseInfo.nextToken().trim();
        Vector courseList;
        
        try {
            instDB.deleteCourse(courseNum, instructor);
            courseList = instDB.getCourseList(instructor);
            sendtoClient("1700");
            sendtoClient(courseList);
        } catch(InvalidDBRequestException e) {
            sendtoClient("1701 "+e.getMessage());
        }
    }
    
    /**
     * query for a course students grades on a particular quiz
     * @param quizInfo a string tokenizer consists of the course number and quiz name
     * @see DBTransaction.getQuizGrades
     */
    protected void cmd_getQuizGrades(StringTokenizer quizInfo) {
        String courseId = quizInfo.nextToken();
        String quizName = quizInfo.nextToken();
        Vector grades;
        try {
            grades = instDB.getQuizGrades(courseId, quizName);
            sendtoClient("1800");
            sendtoClient(grades);
        } catch (InvalidDBRequestException e) {
            sendtoClient("1801 "+e.getMessage());
        }
    }
    
    
    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
// The code under this line are to handle password change request.
    
    
    /**
     * Send connection confirmation (1000) to the client (passwordApp)
     */
    protected void cmd_acceptConnection() {
        sendtoClient("1000");
    }
    
    /**
     * Change a user's password and send response to client
     * @param nameAndPasswords a string tokenizer consists of user's user name, password, and new password
     * @see DBTransaction.changePassword
     */
    protected void cmd_changePassword(StringTokenizer nameAndPasswords) {
        String name = nameAndPasswords.nextToken();		// username
        String pass = nameAndPasswords.nextToken();		// password
        String newPass = nameAndPasswords.nextToken();	// new password
        
        try {
            instDB.changePassword(name, pass, newPass);
            sendtoClient("1100");				// success
        } catch (InvalidNameException e)			// catch wrong username
        {
            sendtoClient("1101");
        } catch (InvalidPassException e)			// catch wrong password
        {
            sendtoClient("1102");
        } catch (InvalidDBRequestException e)			// catch other exception in select/update statement
        {
            sendtoClient("1103 "+e.getMessage());
        }
    }
}


