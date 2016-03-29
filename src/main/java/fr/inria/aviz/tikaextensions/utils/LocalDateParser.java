package fr.inria.aviz.tikaextensions.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tika.metadata.Metadata;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;


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
                "dd/MM/yy,yyyy/MM/dd,dd/MM/yyyy,dd.MM.yyyy,yyyy-MM-dd,dd-MM-yyyy,yyyyMMdd,dd-dd.MM.yyyy,dd MMMM yyyy,MM.yyyy,yyyy.MM,yyyy-MM,yyyy,yyyy-yyyy"+
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
          
          public static List<Date> parseDates(String dateStr) {
            // optionally change the separator
            List<Date> parsedDates = new ArrayList<Date>();
            
            dateStr = dateStr.replaceAll("[\\?\\[\\]]", "").trim().replaceAll("^--?", "").trim();
            //if the string length is 4 and not all digits then return immediately
            if (dateStr.replaceAll( "[^\\d]", "" ).length() <4 ) {
              return parsedDates;
            }
            
            //based on tested formats, remove leading - and -- dashes             
            /*GOOD
            String formatPatterns = 
                "dd/MM/yy,yyyy/MM/dd,dd/MM/yyyy,dd.MM.yyyy,dd-MM-yyyy,yyyyMMdd,MM.yyyy,yyyy.MM,yyyy-MM,dd-dd.MM.yyyy,dd MMMM yyyy,yyyy,yyyy-yyyy,yy..-yy..,yyy.-yyy."+
                "EEE MMM dd HH:mm:ss zzzz yyyy,EEE MMM dd HH:mm:ss zzz yyyy";
             */
            
            String formatPatterns = 
                "dd/MM/yy,yyyy/MM/dd,dd/MM/yyyy,dd.MM.yyyy,yyyy-MM-dd,dd-MM-yyyy,yyyyMMdd,dd-dd.MM.yyyy,dd MMMM yyyy,MM.yyyy,yyyy.MM,yyyy-MM,yyyy,yyyy-yyyy"+
                "EEE MMM dd HH:mm:ss zzzz yyyy,EEE MMM dd HH:mm:ss zzz yyyy";

            ParsePosition pos = new ParsePosition(0);
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
                        Date myDateWithPO = (Date) sdf.parseObject(dateStr, pos);
                        if (myDateWithPO != null) {
                          parsedDates.add(myDateWithPO);
                        }
                      }
                }
                else
                {
                  Date myDateWithPO1 = (Date) sdf.parseObject(dateStr, pos);
                   if (myDateWithPO1 != null){
                     parsedDates.add(myDateWithPO1);
                   }
                } 
            }
            if (pos.getIndex() > 0 && pos.getIndex() < (dateStr.length()) && dateStr.substring(pos.getIndex(), dateStr.length()).length()>3 ){
              parsedDates.addAll(parseDates(dateStr.substring(pos.getIndex(), dateStr.length())));
            }
            return parsedDates;

          }
          
          public static Metadata parseDatesInMetadata (Metadata metadata){
          String[] extractedDates = metadata.getValues(CendariProperties.DATE);
          String fmt = "yyyy-MM-dd";
          SimpleDateFormat sdf =  new SimpleDateFormat(fmt);
          if (extractedDates.length>0){
                  metadata.remove(CendariProperties.DATE.getName());
                  for (String dateStrings : extractedDates) {
                      //do not get parse dates with less than 4 characters (year minimum) 
                      if (dateStrings.length()<4){
                        continue;
                      }
                      //Split some values again (if these still exists, e.g. , )
                      String[] splittedDateStr = dateStrings.split("[,/;]");
                      for (String dateStr : splittedDateStr ){
                          if (dateStr.length() <4){
                            continue;
                          }
                          List<Date> dateDateList = LocalDateParser.parseDates(dateStr);
                          for (Date dateDate:dateDateList){
                              metadata.add(CendariProperties.DATE, sdf.format(dateDate));
                          }
                      }
                  }
            }
          
           return metadata;
          }
}
