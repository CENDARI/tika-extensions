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
 * Class EAGParser
 * @version $Revision$
 */
public class EncyclopediaParser extends AbstractXMLParser {
/**
   * 
   */
  private static final long serialVersionUID = 6552586922540495419L;
/**
   * 
   */
  private static final String NAMESPACE_URI_ENC = "http://encyclopedia.1914-1918-online.net/lod/schema#";
  private static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  private static final String NAMESPACE_URI_DC = "http://purl.org/dc/terms/";

private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("enc+xml"));
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    private static ContentHandler getDCHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            NAMESPACE_URI_DC, element,
            metadata, property, context);
    }
    
       private static ContentHandler getENCHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            NAMESPACE_URI_ENC, element,
            metadata, property, context);
    }
    
    
   
    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);

        
        //Note: this parser works for following articles (Encyclopedia Data) 
        //e1418:E1418_Regional_Thematic_Article 
        //E1418_Encyclopedic_Entry_Persons
        //E1418_Encyclopedic_Entry_Events
        //E1418_Encyclopedic_Entry_Objects
        //E1418_Encyclopedic_Entry_Organizations
        //E1418_Encyclopedic_Entry_Spaces
        //E1418_Survey_Article_Regional
        //E1418_Survey_Article_Thematic
        //E1418_Encyclopedic_Entry_Concepts_Practices_and_Policies
        
        
        
        return new TeeContentHandler(
                defaultContentHandler,
                getDCHandler(metadata, CendariProperties.TITLE, "title"),
                getDCHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                getDCHandler(metadata, CendariProperties.LANG, "language"),
                getDCHandler(metadata, CendariProperties.DESCRIPTION, "abstract"),
                
                getDCHandler(metadata, CendariProperties.RIGHTS, "rights"),
                getDCHandler(metadata, CendariProperties.RIGHTS, "license"),
                
                getENCHandler(metadata, CendariProperties.CONTRIBUTOR, "E1418_P_Author"),
                getENCHandler(metadata, CendariProperties.CONTRIBUTOR, "E1418_P_SectionEditor"),
                getENCHandler(metadata, CendariProperties.CONTRIBUTOR, "E1418_P_Translator"),

                      
                getENCHandler(metadata, CendariProperties.IDENTIFIER, "E1418_P_DOI_URN"),
                
                getENCHandler(metadata, CendariProperties.KEYWORDS, "E1418_P_DdcSubjectCatalogue"),
                getENCHandler(metadata, CendariProperties.KEYWORDS, "E1418_P_AuthorKeyword"),    
                getENCHandler(metadata, CendariProperties.KEYWORDS, "E1418_P_Related_Theme"),
                getENCHandler(metadata, CendariProperties.KEYWORDS, "E1418_P_Related_Subject"),    
                
                
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_Related_Place"),
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_Related_Region"),
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_DdcGeographicAreaCode"),
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_PlaceOfBirth"),
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_PlaceOfDeath"),
                
                getENCHandler(metadata, CendariProperties.DATE, "E1418_P_DateOfBirth"),
                getENCHandler(metadata, CendariProperties.DATE, "E1418_P_DateOfDeath"),
                
                getENCHandler(metadata, CendariProperties.PLACE, "E1418_P_DdcGeographicAreaCode"),
                
                getENCHandler(metadata, CendariProperties.PERSON, "E1418_P_KeyPerson"),
                
                new AttributeMetadataHandler(
                    NAMESPACE_URI_RDF, "about",
                      metadata, CendariProperties.REFERENCE),
                
                //In Encyclopedia minimum of data is NERDed, as all other entities are taken directly from the metadata, thus considering completely trusted source 
                getDCHandler(metadata, CendariProperties.NERD, "title"),
                getDCHandler(metadata, CendariProperties.NERD, "abstract")
          
            );

    }     
}
