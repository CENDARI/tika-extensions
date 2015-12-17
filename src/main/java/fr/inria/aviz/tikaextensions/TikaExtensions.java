package fr.inria.aviz.tikaextensions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;
import fr.inria.aviz.tikaextensions.utils.TextCleaner;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

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
            String nerdString =
                metadata.getValues(CendariProperties.NERD).length > 0 ?
                   getString(metadata.getValues(CendariProperties.NERD)) :"";
            
            if (!nerdString.equals("")) {
               nerdString = TextCleaner.cleanup(nerdString);
               metadata.set(CendariProperties.NERD, nerdString);
               metadata.add("text", nerdString);
            }
            else
            {
               metadata.add("text", TextCleaner.cleanup(parsedContent));
              
            }
            
            if (metadata.get(CendariProperties.REFERENCE) == null) {
                metadata.add(CendariProperties.REFERENCE, metadata.get(CendariProperties.POTENTIAL_REFERENCE));
                metadata.remove(CendariProperties.POTENTIAL_REFERENCE.getName());
            }
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

}
