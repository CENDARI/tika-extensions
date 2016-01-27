package fr.inria.aviz.tikaextensions.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/** LocalDateParserTextCleaner
 * 
 * @version $Revision$
 */
public class LocalDateParser {
   
          public static Date parseDate(String dateStr) {
            // optionally change the separator
            dateStr = dateStr.replaceAll("[\\?\\[\\]]", "");
            /*GOOD
            String formatPatterns = 
                "dd/MM/yy,yyyy/MM/dd,dd/MM/yyyy,dd.MM.yyyy,dd-MM-yyyy,yyyyMMdd,MM.yyyy,yyyy.MM,yyyy-MM,dd-dd.MM.yyyy,dd MMMM yyyy,yyyy,yyyy-yyyy,yy..-yy..,yyy.-yyy."+
                "EEE MMM dd HH:mm:ss zzzz yyyy,EEE MMM dd HH:mm:ss zzz yyyy";
             */
            
            String formatPatterns = 
                "dd/MM/yy,yyyy/MM/dd,dd/MM/yyyy,dd.MM.yyyy,dd-MM-yyyy,yyyyMMdd,dd-dd.MM.yyyy,dd MMMM yyyy,MM.yyyy,yyyy.MM,yyyy-MM,yyyy,yyyy-yyyy"+
                "EEE MMM dd HH:mm:ss zzzz yyyy,EEE MMM dd HH:mm:ss zzz yyyy";

            
            for (String fmt : formatPatterns.split(",")) {
                //Do this for English, German or Italian etc.. 
                SimpleDateFormat sdf =  new SimpleDateFormat(fmt);
                sdf. setLenient(false);
                //Some date formats need to be parsed with various locales
                if (fmt.contains("EEE") || fmt.contains("MMM")){
                    Locale[] supportedLocales = SimpleDateFormat.getAvailableLocales();
                    for (Locale currL : supportedLocales){
                       sdf =  new SimpleDateFormat(fmt, currL);
                       sdf. setLenient(false);
                      try {
                        return sdf.parse(dateStr);
                      } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //System.out.println("For PATTERN= "+fmt);
                      }
                    }
                }
                else
                {
                 try {
                   
                   return sdf.parse(dateStr);
                     
                 } catch (ParseException e) {
                 }
                }
            }
            //throw new IllegalArgumentException("Unable to parse date '" + dateStr + "'");
            return null;
        }
}
