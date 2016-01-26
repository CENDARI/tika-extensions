package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.DublinCore;
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
public class MODSParser extends AbstractXMLParser {
    /**
   * 
   */
  private static final long serialVersionUID = 522581315138961225L;
    //private static final String NAMESPACE_URI_OAI_PMH = "http://www.openarchives.org/OAI/2.0";
    
    private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("mods+xml"));
    
    private static final String NAMESPACE_URI_MODS = "http://www.loc.gov/mods/v3";

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    
    private static ContentHandler getHandler(
            Metadata metadata, Property property, String element, 
            String ...context) {
        return new ContextualElementMetadataHandler(
            NAMESPACE_URI_MODS, element,
                metadata, property, context);
    }

    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
        
        return new TeeContentHandler(
                defaultContentHandler,
                getHandler(metadata, CendariProperties.TITLE, "title"),
                getHandler(metadata, CendariProperties.PLACE, "placeTerm"),
                getHandler(metadata, CendariProperties.DESCRIPTION, "recordContentSource"),
                getHandler(metadata, CendariProperties.REFERENCE, "url", "location"),
                getHandler(metadata, CendariProperties.DATE, "dateCreated", "originInfo")
                //NO NERD FIELDS ARE POPULATED as the MAPPING IS WEAK , THIS WILL BE AUTOMATICALLY TAKEN FROM DEFAULT PArsER FOR NERDING
            );
    }
}
