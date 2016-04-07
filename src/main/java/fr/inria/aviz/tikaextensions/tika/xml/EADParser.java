package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.AttributeMetadataHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.TextContentHandler;
import org.xml.sax.ContentHandler;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;

/**
 * Class EADParser
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class EADParser extends AbstractXMLParser {
    /**
   * 
   */
  private static final long serialVersionUID = 7171484731068477630L;

    private static final String NAMESPACE_URI_EAD = "urn:isbn:1-931666-22-9";
    private static final String NAMESPACE_URI_EAD_EMPTY = "";
    
    private static final Set<MediaType> SUPPORTED_TYPES =  
        //new HashSet<MediaType>(Arrays.asList( new MediaType[]{MediaType.application("ead+xml"), MediaType.application("xml")}));;
        Collections.singleton(MediaType.application("ead+xml"));
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    private static ContentHandler getEADHandler(
            Metadata metadata, Property property, String element, 
            String ...context) {
           return  new ContextualElementMetadataHandler(
               NAMESPACE_URI_EAD, element,
                metadata, property, context);
    }

  private static ContentHandler getEADEmptyHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
       return   new ContextualElementMetadataHandler(
            NAMESPACE_URI_EAD_EMPTY, element,
            metadata, property, context);
}
      protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {

      ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
       return new TeeContentHandler(
                defaultContentHandler,
                
                getEADHandler(metadata, CendariProperties.TITLE, "titleproper", "titlestmt"),
                getEADHandler(metadata, CendariProperties.KEYWORDS, "term", "keywords"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "abstract"),
                getEADHandler(metadata, CendariProperties.CONTRIBUTOR, "name", "titlestmt", "respStmt"),
                getEADHandler(metadata, CendariProperties.CONTRIBUTOR, "author", "titlestmt"),
                getEADHandler(metadata, CendariProperties.PUBLISHER, "publicationstmt"),
                getEADHandler(metadata, CendariProperties.PUBLISHER, "repository"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "head", "scopecontent"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "p", "scopecontent"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "head", "arrangement"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "p", "arrangement"),
                getEADHandler(metadata, CendariProperties.DESCRIPTION, "unittitle"),
                getEADHandler(metadata, CendariProperties.RIGHTS, "licence"),
                getEADHandler(metadata, CendariProperties.PLACE, "geogname", "controlaccess"),
                getEADHandler(metadata, CendariProperties.PERSON, "persname", "controlaccess"),
                getEADHandler(metadata, CendariProperties.KEYWORDS, "subject", "controlaccess"),
                getEADHandler(metadata, CendariProperties.DATE, "unitdate", "did"),
                
                getEADHandler(metadata, CendariProperties.NERD, "archref"),
                getEADHandler(metadata, CendariProperties.NERD, "eventgrp"),
                getEADHandler(metadata, CendariProperties.NERD, "chronitem"),
                getEADHandler(metadata, CendariProperties.NERD, "acqinfo"),
                getEADHandler(metadata, CendariProperties.NERD, "appraisal"),
                getEADHandler(metadata, CendariProperties.NERD, "bibliography"),
                getEADHandler(metadata, CendariProperties.NERD, "bioghist"),
                getEADHandler(metadata, CendariProperties.NERD, "c"),
                getEADHandler(metadata, CendariProperties.NERD, "c01"),
                getEADHandler(metadata, CendariProperties.NERD, "c02"),
                getEADHandler(metadata, CendariProperties.NERD, "c03"),
                getEADHandler(metadata, CendariProperties.NERD, "c04"),
                getEADHandler(metadata, CendariProperties.NERD, "c05"),
                getEADHandler(metadata, CendariProperties.NERD, "c06"),
                getEADHandler(metadata, CendariProperties.NERD, "c07"),
                getEADHandler(metadata, CendariProperties.NERD, "c08"),
                getEADHandler(metadata, CendariProperties.NERD, "c09"),
                getEADHandler(metadata, CendariProperties.NERD, "c10"),
                getEADHandler(metadata, CendariProperties.NERD, "c11"),
                getEADHandler(metadata, CendariProperties.NERD, "c12"),
                getEADHandler(metadata, CendariProperties.NERD, "custodhist"),
                getEADHandler(metadata, CendariProperties.NERD, "daodesc"),
                getEADHandler(metadata, CendariProperties.NERD, "descgrp"),
                getEADHandler(metadata, CendariProperties.NERD, "dscgrp"),
                getEADHandler(metadata, CendariProperties.NERD, "relatedmaterial"),
                getEADHandler(metadata, CendariProperties.NERD, "head", "scopecontent"),
                getEADHandler(metadata, CendariProperties.NERD, "p", "scopecontent"),
                getEADHandler(metadata, CendariProperties.NERD, "separatedmaterial"),
                getEADHandler(metadata, CendariProperties.NERD, "table"),
                getEADHandler(metadata, CendariProperties.NERD, "blockquote"),
                getEADHandler(metadata, CendariProperties.NERD, "note"),
                getEADHandler(metadata, CendariProperties.NERD, "seriesstmt"),
                getEADHandler(metadata, CendariProperties.NERD, "titlepage"),
                //getEADHandler(metadata, CendariProperties.NERD, "titlestmt"),
                getEADHandler(metadata, CendariProperties.NERD, "abstract"),
                getEADHandler(metadata, CendariProperties.NERD, "event"),
                getEADHandler(metadata, CendariProperties.NERD, "label"),
                getEADHandler(metadata, CendariProperties.NERD, "origination"),
                getEADHandler(metadata, CendariProperties.NERD, "name", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "geogname", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "subject", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "persname", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "unittitle"),
             
                //COMMON ATTRIBUTES for both empty and URN Namespace elements
                new AttributeMetadataHandler(NAMESPACE_URI_XML, "lang", metadata, CendariProperties.LANG),
                new AttributeMetadataHandler(NAMESPACE_URI_EAD, "langcode", metadata, CendariProperties.LANG),
                new ElementAttributeMetadataHandler(NAMESPACE_URI_EAD, "url", metadata, CendariProperties.REFERENCE, "eadid"),
                new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD, "normal",metadata, CendariProperties.DATE, "unitdate") 
                {
                    protected void addMetadata(String date) {
                        String[] dates = date.split("/");
                        for (String d : dates) 
                            super.addMetadata(d);
                    };
                },
                
                new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD, "from",metadata, CendariProperties.DATE, "unitdate"),
                new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD, "to",metadata, CendariProperties.DATE, "unitdate"), 
                  
                  //NOW WITHOUT EAD URN NAMESPACE  
                getEADEmptyHandler(metadata, CendariProperties.TITLE, "titleproper", "titlestmt"),
                getEADEmptyHandler(metadata, CendariProperties.KEYWORDS,"term", "keywords"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION,"abstract"),
                getEADEmptyHandler(metadata, CendariProperties.CONTRIBUTOR, "name", "titlestmt", "respStmt"),
                getEADEmptyHandler(metadata, CendariProperties.CONTRIBUTOR, "author", "titlestmt"),
                getEADEmptyHandler(metadata, CendariProperties.PUBLISHER, "publicationstmt"),
                getEADEmptyHandler(metadata, CendariProperties.PUBLISHER, "repository"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION, "head", "scopecontent"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION, "p", "scopecontent"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION, "head", "arrangement"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION, "p", "arrangement"),
                getEADEmptyHandler(metadata, CendariProperties.DESCRIPTION, "unittitle"),
                getEADEmptyHandler(metadata, CendariProperties.RIGHTS, "licence"),
                getEADEmptyHandler(metadata, CendariProperties.PLACE, "geogname", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.KEYWORDS, "subject", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.PERSON, "persname", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.DATE, "unitdate", "did"),
                
                getEADEmptyHandler(metadata, CendariProperties.NERD, "archref"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "eventgrp"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "chronitem"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "acqinfo"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "appraisal"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "bibliography"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "bioghist"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c01"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c02"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c03"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c04"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c05"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c06"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c07"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c08"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c09"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c10"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c11"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "c12"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "custodhist"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "daodesc"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "descgrp"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "dscgrp"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "relatedmaterial"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "head", "scopecontent"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "p", "scopecontent"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "separatedmaterial"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "table"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "blockquote"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "note"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "seriesstmt"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "titlepage"),
                //getEADEmptyHandler(metadata, CendariProperties.NERD, "titlestmt"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "abstract"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "event"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "label"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "origination"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "name", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "geogname", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "subject", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "persname", "controlaccess"),
                getEADEmptyHandler(metadata, CendariProperties.NERD, "unittitle"),

             
                new AttributeMetadataHandler(NAMESPACE_URI_XML, "lang", metadata, CendariProperties.LANG),
                new AttributeMetadataHandler(NAMESPACE_URI_EAD_EMPTY, "langcode", metadata, CendariProperties.LANG),
                new ElementAttributeMetadataHandler(NAMESPACE_URI_EAD_EMPTY, "url", metadata, CendariProperties.REFERENCE, "eadid"),
                new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD_EMPTY, "normal",metadata, CendariProperties.DATE, "unitdate") 
                {
                      protected void addMetadata(String date) {
                          String[] dates = date.split("/");
                          for (String d : dates) {
                              super.addMetadata(d);
                          }
                      };
                  },
                  new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD_EMPTY, "from",metadata, CendariProperties.DATE, "unitdate"),
                  new ElementAttributeMetadataHandler( NAMESPACE_URI_EAD_EMPTY, "to",metadata, CendariProperties.DATE, "unitdate")
            );
    }
}
                