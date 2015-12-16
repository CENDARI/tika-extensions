package fr.inria.aviz.tikaextensions.utils;

import java.util.regex.Pattern;

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
        if (!Jsoup.isValid(value, Whitelist.simpleText()) ){
            value =  Jsoup.clean(value, Whitelist.simpleText());
        }
        return value;
    }
}
