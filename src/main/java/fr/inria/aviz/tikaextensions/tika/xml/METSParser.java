package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.TextContentHandler;
import org.xml.sax.ContentHandler;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;

/**
 * Class OAIPMHParser
 * 
 * @version $Revision$
 */
public class METSParser extends AbstractXMLParser {
    /**
   * 
   */
  private static final long serialVersionUID = 522581315138961225L;
    //private static final String NAMESPACE_URI_OAI_PMH = "http://www.openarchives.org/OAI/2.0";
    
    private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("mets+xml"));
    //Using only MODS and DC from METS
    //private static final String NAMESPACE_URI_METS = "http://www.loc.gov/METS/";
    private static final String NAMESPACE_URI_MODS = "http://www.loc.gov/mods/v3";
    private static final String NAMESPACE_URI_DC = "http://purl.org/dc/elements/1.1/";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    
    private static ContentHandler getMODSHandler(
            Metadata metadata, Property property, String element, 
            String ...context) {
        return new ContextualElementMetadataHandler(
            NAMESPACE_URI_MODS, element,
                metadata, property, context);
    }

    private static ContentHandler getDCHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
        NAMESPACE_URI_DC, element,
            metadata, property, context);
    }
    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
        
        return new TeeContentHandler(
                defaultContentHandler,
                getDCHandler(metadata, CendariProperties.TITLE, "title"),
                getDCHandler(metadata, CendariProperties.IDENTIFIER, "identifier"),
                getMODSHandler(metadata, CendariProperties.DESCRIPTION, "titleInfo"),
                getMODSHandler(metadata, CendariProperties.PLACE, "placeTerm"),
                getMODSHandler(metadata, CendariProperties.SOURCE, "recordContentSource"),
                getMODSHandler(metadata, CendariProperties.DATE, "dateCreated", "originInfo"),
                getMODSHandler(metadata, CendariProperties.DATE, "dateIssued", "originInfo"),
                getMODSHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                
                getMODSHandler(metadata, CendariProperties.NERD, "titleInfo")
                //NO NERD FIELDS ARE POPULATED as the MAPPING IS WEAK , THIS WILL BE AUTOMATICALLY TAKEN FROM DEFAULT PArsER FOR NERDING
            );
    }
}
