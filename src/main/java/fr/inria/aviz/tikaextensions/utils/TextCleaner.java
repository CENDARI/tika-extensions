package fr.inria.aviz.tikaextensions.utils;

import java.util.regex.Pattern;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


/**
 * Class TextCleaner
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class TextCleaner {
    private static final Pattern SPACES = Pattern.compile("[ \t]+");
    private static final Pattern LINES = Pattern.compile("(\r?\n[ \t])+");
    private static final Pattern AMPERSAND = Pattern.compile("&");
    //private static final Pattern URL = Pattern.compile ("((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)");
    private static final Pattern URL = Pattern.compile ("(((https?|ftp|gopher|telnet|file|Unsure|http):)*((/)|(//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)");
    private static final Pattern BRACKETS = Pattern.compile("[\\[|\\]]");
    private static final Pattern LT_GT = Pattern.compile("(<|>)");
    

    /**
     * Cleanup a specified string, removing extra spaces
     * @param value a string
     * @return its cleaned-up version
     */
    public static String cleanup(String value) {
        value = value.trim();
        value = SPACES.matcher(value).replaceAll(" "); 
        value = LINES.matcher(value).replaceAll("\n");
        value = AMPERSAND.matcher(value).replaceAll("&amp;");
        value = BRACKETS.matcher(value).replaceAll(" ");
        value = LT_GT.matcher(value).replaceAll(" ");
        value = Jsoup.clean(value, Whitelist.none());
        
        return value;
    }
    
    public static String removeUrl(String commentstr)
    {
        commentstr.trim();
        commentstr =  URL.matcher(commentstr).replaceAll("");
        return commentstr;
    }
    
    //Cleans the characters
    //Cleans URLs
    //Cleans JSON (if string is JSON returns empty string
    public static String cleanupFull(String value){
         value = cleanup(value);
         value = removeUrl(value);
         value = cleanJSON(value);
         return value;
    }
    
    
    //check JSON in text
    //This is necessary to clean-up JSON content without properly sent filename 
    //if the file contains JSON, but the name does not end with ".json" 
    //this will prevent sending JSON to NERD or elsewhere
    public static String cleanJSON (String value){
        JSONParser parser = new JSONParser();
        try{
          parser.parse(value);
          return "";
          //Reset the text to nothing if the string is JSON !!! 
          }
          catch(ParseException pe){
             return value;
             //Do Nothing here it is fine if string is not JSON
          }                
    }

}
