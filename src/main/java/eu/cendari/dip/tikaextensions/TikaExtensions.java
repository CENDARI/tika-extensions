package eu.cendari.dip.tikaextensions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;

import eu.cendari.dip.tikaextensions.tika.CendariProperties;
import eu.cendari.dip.tikaextensions.tika.ExcludeCendariIndexer;
import eu.cendari.dip.tikaextensions.utils.LocalDateParser;
import eu.cendari.dip.tikaextensions.utils.TextCleaner;

/**
 * Singleton class, find metadata and text contents in documents.
 * 
 * TikaExtension is a Facade to Tika for improved metadata extraction in documents.
 */
public class TikaExtensions {
    private static final Logger logger = Logger.getLogger(TikaExtensions.class.getName());
    private static TikaExtensions instance_;
    private final Tika tika = new Tika();
    
    /**
     * @return the Indexer singleton instance, creating it if necessary. 
     */
    public static TikaExtensions instance() {
        if (instance_ == null) {
            instance_ = new TikaExtensions();
        }
        return instance_;
    }

    private TikaExtensions() {
    }

    /**
     * Parse a specified document using Tika and returns the related DocumentInfo. 
     * 
     * @param name document name
     * @param contentType document type or null if unknown
     * @param content document as a byte array
     * @param maxLength maximum length to parse or -1 to parse all
     * @return a org.apache.tika.metadata.Metadata structure filled with the right contents or null if tika has not been able to parse it.
     */
    public Metadata parseDocument(String name, String contentType, byte[] content, int maxLength) {
        return parseDocument(name, contentType, new ByteArrayInputStream(content), maxLength);
    }

    /**
     * Parse a specified document using Tika and returns the related DocumentInfo. 
     * 
     * @param name document name
     * @param contentType document type or null if unknown
     * @param content document stream, which will be closed at the end of the call
     * @param maxLength maximum length to parse or -1 to parse all
     * @return org.apache.tika.metadata.Metadata structure filled with the right contents or null if tika has not been able to parse it.
     */
    public Metadata parseDocument(String name, String contentType, InputStream content, int maxLength) {
      

        Metadata metadata = new Metadata();


        if (name != null) {
            metadata.add(Metadata.RESOURCE_NAME_KEY, name);
        }
        
     
        if (contentType != null) {
            metadata.add(Metadata.CONTENT_TYPE, contentType);
        }
        
        try {
          String parsedContent = tika.parseToString(content, metadata, maxLength);
          
          metadata = shuflleAllMetadata(metadata);
          
          if ( !ExcludeCendariIndexer.shouldExclude(metadata.getValues(CendariProperties.PROVIDER))){
                
                String nerdString =
                    metadata.getValues(CendariProperties.NERD).length > 0 ?
                       getString(metadata.getValues(CendariProperties.NERD)) :"";
                //URLs and JSON will be only striped from NERD and text fields
                if (!nerdString.equals("")) {
                   nerdString = TextCleaner.cleanupFull(nerdString);
                   metadata.set(CendariProperties.NERD, nerdString);
                   metadata.add("text", nerdString);
                }
                else
                {
                 
                   metadata.add("text", TextCleaner.cleanupFull(parsedContent));
                }
          
           }
            else
            {
              metadata.add("text", "");
              metadata.set(CendariProperties.NERD, "");
            }
            
                        
            metadata = cleanReferences(metadata);
            
            //Now create date string in real date format, from extracted date strings in various formats
            //Parsers may return range-strings for dates e.g. 01.02.2001 - 05.06.2001
            metadata = LocalDateParser.parseDatesInMetadata(metadata);
            
            //Remove duplicate values
            metadata = shuffleDoubleMetadataValues (metadata);

        }
        catch (TikaException e) {
            logger.error("Tika parse exception for document "+name, e);
            return null;
        }
        catch (Exception e) {
            logger.error("Exception for document "+name, e);
            return null;
        }
        return metadata;
    }
    
    private String getString(String [] nerdArray){

      StringBuilder builder = new StringBuilder();
      for (String value : nerdArray) {
          builder.append(value+" ");
      }
      String text = builder.toString();
      return text;
    }
    
    private Metadata shuffleMetadata (Metadata metadata, String name, Property property) {
      if (metadata.getValues(name) != null && metadata.getValues(name).length>0) {
           for (int i= 0; i<metadata.getValues(name).length;i++){
               //to not add duplicate values (somtimes tika internals do it even for same values) 
               if (! ArrayUtils.contains(metadata.getValues(property), metadata.getValues(name)[i])) {
                   metadata.add(property, metadata.getValues(name)[i]);
               }
           }
           metadata.remove(name);
      }
      
      return metadata;
      
    }

    //Otherwise double values are created in the property strange for Tika (?)
    private Metadata shuffleDoubleMetadataValues (Metadata metadata) {
      for (String propertyName : metadata.names()) {
      if (metadata.getValues(propertyName) != null && metadata.getValues(propertyName).length>0) {
           List<String> values = new ArrayList<String>();
           for (int i= 0; i<metadata.getValues(propertyName).length;i++){
               //to not add duplicate values (somtimes tika internals do it even for same values)
               if (!values.contains(metadata.getValues(propertyName)[i]))
                   values.add(metadata.getValues(propertyName)[i]);
               }
           Property myProp = Property.get(propertyName);
           if (myProp!= null) {
             //Check above necessary because of the "text" field
             metadata.remove(propertyName);
             metadata.set(myProp, values.toArray(new String[0]));
           }
          }
      }
      
      return metadata;
     }
    
    private Metadata shuflleAllMetadata (Metadata metadata) {
      
      Metadata myMD=metadata;
      
      myMD = shuffleMetadata(metadata, "publisher", CendariProperties.PUBLISHER);
      myMD = shuffleMetadata(metadata, "dc:publisher", CendariProperties.PUBLISHER);
      myMD = shuffleMetadata(metadata, "creator", CendariProperties.CREATOR);
      myMD = shuffleMetadata(metadata, "Author", CendariProperties.CREATOR);
      myMD = shuffleMetadata(metadata, "dc:creator", CendariProperties.CREATOR);
      myMD = shuffleMetadata(metadata, "contributor", CendariProperties.CONTRIBUTOR);
      myMD = shuffleMetadata(metadata, "dc:contributor", CendariProperties.CONTRIBUTOR);
      myMD = shuffleMetadata(metadata, "date", CendariProperties.DATE);
      myMD = shuffleMetadata(metadata, "created", CendariProperties.DATE);
      myMD = shuffleMetadata(metadata, "dc:date", CendariProperties.DATE);
      myMD = shuffleMetadata(metadata, "dcterms:created", CendariProperties.DATE);
      myMD = shuffleMetadata(metadata, "Creation-Date", CendariProperties.DATE);
      myMD = shuffleMetadata(metadata, "dc:format", CendariProperties.FORMAT);
      myMD = shuffleMetadata(metadata, "dc:type", CendariProperties.TYPE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.COVERAGE.getName(), CendariProperties.COVERAGE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.DESCRIPTION.getName(), CendariProperties.DESCRIPTION);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.KEYWORDS.getName(), CendariProperties.KEYWORDS);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.FORMAT.getName(), CendariProperties.FORMAT);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.TYPE.getName(), CendariProperties.TYPE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.IDENTIFIER.getName(), CendariProperties.IDENTIFIER);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.LATITUDE.getName(), CendariProperties.LATITUDE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.LONGITUDE.getName(), CendariProperties.LONGITUDE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.SOURCE.getName(), CendariProperties.SOURCE);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.LANGUAGE.getName(), CendariProperties.LANG);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.PUBLISHER.getName(), CendariProperties.PUBLISHER);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.RELATION.getName(), CendariProperties.RELATION);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.RIGHTS.getName(), CendariProperties.RIGHTS);
      myMD = shuffleMetadata(metadata, TikaCoreProperties.TITLE.getName(), CendariProperties.TITLE);
      
      return myMD;
    }

    //This method must make sure that there is maximum one single reference
    private Metadata cleanReferences (Metadata metadataInput) {
      
      Metadata metadata = metadataInput;
      
      if (metadata.get(CendariProperties.REFERENCE) == null) {
        metadata.add(CendariProperties.REFERENCE, metadata.get(CendariProperties.POTENTIAL_REFERENCE));
        }
    
        metadata.remove(CendariProperties.POTENTIAL_REFERENCE.getName());
        //Check CENDARI REFERENCES for URL
        String firstUrl="";

        for (String value:metadata.getValues(CendariProperties.REFERENCE)) {
                if (TextCleaner.isURL(value)){
                    //In addition here URL needs some "custom" decode, as some URLs were noticed to have "&amp;" instead of "&" as value
                    //http://ais.ra.ee/index.php?module=202&amp;op=4&amp;tyyp=2&amp;kokku=1&amp;id=200100002962
                    //URLDecoder does not catch this case
                    firstUrl = value.replaceAll("&amp;", "&");
                    break;
                }
    
         }
            
         // If It is not a URL then remove it completely
         if (firstUrl.equals("")) {
              metadata.remove(CendariProperties.REFERENCE.getName());
         }
         else
         {
            metadata.set(CendariProperties.REFERENCE, firstUrl);
         }
         return metadata;
     }
    
}
