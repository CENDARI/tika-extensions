package fr.inria.aviz.tikaextensions;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;
import fr.inria.aviz.tikaextensions.tika.ExcludeCendariIndexer;
import fr.inria.aviz.tikaextensions.utils.LocalDateParser;
import fr.inria.aviz.tikaextensions.utils.TextCleaner;

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
     * @return a org.apache.tika.metadata.Metadata structure filled with the right contents of null if tika has not been able to parse it.
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
     * @return org.apache.tika.metadata.Metadata structure filled with the right contents of null if tika has not been able to parse it.
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
          
          shuffleMetadata(metadata, "publisher", CendariProperties.PUBLISHER);
          shuffleMetadata(metadata, "dc:publisher", CendariProperties.PUBLISHER);
          shuffleMetadata(metadata, "creator", CendariProperties.CREATOR);
          shuffleMetadata(metadata, "Author", CendariProperties.CREATOR);
          shuffleMetadata(metadata, "dc:creator", CendariProperties.CREATOR);
          shuffleMetadata(metadata, "contributor", CendariProperties.CONTRIBUTOR);
          shuffleMetadata(metadata, "dc:contributor", CendariProperties.CONTRIBUTOR);
          shuffleMetadata(metadata, "date", CendariProperties.DATE);
          shuffleMetadata(metadata, "created", CendariProperties.DATE);
          shuffleMetadata(metadata, "dc:date", CendariProperties.DATE);
          shuffleMetadata(metadata, "dcterms:created", CendariProperties.DATE);
          shuffleMetadata(metadata, "Creation-Date", CendariProperties.DATE);
          shuffleMetadata(metadata, "dc:format", CendariProperties.FORMAT);
          shuffleMetadata(metadata, "dc:type", CendariProperties.TYPE);
          shuffleMetadata(metadata, TikaCoreProperties.COVERAGE.getName(), CendariProperties.COVERAGE);
          shuffleMetadata(metadata, TikaCoreProperties.DESCRIPTION.getName(), CendariProperties.DESCRIPTION);
          shuffleMetadata(metadata, TikaCoreProperties.KEYWORDS.getName(), CendariProperties.KEYWORDS);
          shuffleMetadata(metadata, TikaCoreProperties.FORMAT.getName(), CendariProperties.FORMAT);
          shuffleMetadata(metadata, TikaCoreProperties.TYPE.getName(), CendariProperties.TYPE);
          shuffleMetadata(metadata, TikaCoreProperties.IDENTIFIER.getName(), CendariProperties.IDENTIFIER);
          shuffleMetadata(metadata, TikaCoreProperties.LATITUDE.getName(), CendariProperties.LATITUDE);
          shuffleMetadata(metadata, TikaCoreProperties.LONGITUDE.getName(), CendariProperties.LONGITUDE);
          shuffleMetadata(metadata, TikaCoreProperties.SOURCE.getName(), CendariProperties.SOURCE);
          shuffleMetadata(metadata, TikaCoreProperties.LANGUAGE.getName(), CendariProperties.LANG);
          shuffleMetadata(metadata, TikaCoreProperties.PUBLISHER.getName(), CendariProperties.PUBLISHER);
          shuffleMetadata(metadata, TikaCoreProperties.RELATION.getName(), CendariProperties.RELATION);
          shuffleMetadata(metadata, TikaCoreProperties.RIGHTS.getName(), CendariProperties.RIGHTS);
          shuffleMetadata(metadata, TikaCoreProperties.TITLE.getName(), CendariProperties.TITLE);
          
          
          //System.out.println("REALLY PARSING 2 "+name);
          
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
            
            if (metadata.get(CendariProperties.REFERENCE) == null) {
                metadata.add(CendariProperties.REFERENCE, metadata.get(CendariProperties.POTENTIAL_REFERENCE));
            }
            metadata.remove(CendariProperties.POTENTIAL_REFERENCE.getName());
            
            //Now parse date string at once
            //Parsers may return more strings for dates
            //In addition, returned string may be in format date1/date2 or date1-date2
            //Here additional processing for all DATES irrelevant of parser invoked
            LocalDateParser.parseDatesInMetadata(metadata);
            
            //Remove duplicate values
            shuffleDoubleMetadataValues (metadata);
            

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
    
    private void shuffleMetadata (Metadata metadata, String name, Property property) {
      if (metadata.getValues(name) != null && metadata.getValues(name).length>0) {
           for (int i= 0; i<metadata.getValues(name).length;i++){
               //to not add duplicate values (somtimes tika internals do it even for same values) 
               if (! ArrayUtils.contains(metadata.getValues(property), metadata.getValues(name)[i])) {
                   metadata.add(property, metadata.getValues(name)[i]);
               }
           }
           metadata.remove(name);
      }
      
    }

    //Otherwise double values are created in the property strange for Tika (?)
    private void shuffleDoubleMetadataValues (Metadata metadata) {
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
     }
      
    
}
