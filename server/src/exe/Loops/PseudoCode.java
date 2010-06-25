package exe.Loops;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class PseudoCode {
	
	public PseudoCode()
	{
				
	}
	
	public String pseudo_uri(int lineNum, ArrayList<String> pseudo, String con) throws IOException{
	    //Now we will build up the HTML content of the page
	    String content = con;
	    
	     for ( int i = 0; i < pseudo.size(); i++){
	    		if( i == lineNum){
	    			content = content + "<div style=\"color:red\">" + (String)pseudo.get(i) + "</div>";
	    		}
	    		else
	              content = content + (String)pseudo.get(i);
	    }
	    content = content + "</body></html>";
	    //Convert the content to a Java URI that can actually be displayed in the pseudo tab
	    URI uri = null;
	    try{
	        uri = new URI("str", content, "");
	    }
	    catch (java.net.URISyntaxException e){
	    }
	    //A kludge to appropriately escape the less than sign
	    String ret_str = uri.toASCIIString().replaceAll("%2526lt;", "%26lt;");
	    return ret_str;
	    }

}
