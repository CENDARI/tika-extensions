package eu.cendari.dip.tikaextensions.tika.xml;

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

import eu.cendari.dip.tikaextensions.tika.CendariProperties;

/**
 * Class OAIPMHParser
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class OAIPMHParser extends AbstractXMLParser {
    /**
   * 
   */
  private static final long serialVersionUID = 522581315138961225L;
    //private static final String NAMESPACE_URI_OAI_PMH = "http://www.openarchives.org/OAI/2.0";
    
    private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("oai-pmh+xml"));
    
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
            DublinCore.NAMESPACE_URI_DC, element,
                metadata, property, context);
    }
    
    private static ContentHandler getMODSHandler(
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
                getHandler(metadata, CendariProperties.DESCRIPTION, "description"), 
                getHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                getHandler(metadata, CendariProperties.DATE, "date"),
                getHandler(metadata, CendariProperties.TYPE, "type"),
                getHandler(metadata, CendariProperties.FORMAT, "format"),
                getHandler(metadata, CendariProperties.IDENTIFIER, "identifier"),
                getHandler(metadata, CendariProperties.RIGHTS, "rights"),
                getHandler(metadata, CendariProperties.LANG, "language"),
                getHandler(metadata, CendariProperties.COVERAGE, "coverage"),
                getHandler(metadata, CendariProperties.SOURCE, "source"),
                getHandler(metadata, CendariProperties.KEYWORDS, "subject"),
                //a very strange stuff, but appears obviously in Heidelberg library- data
                getHandler(metadata, CendariProperties.RELATION, "ispartof"),
                getHandler(metadata, CendariProperties.RELATION, "relation"),

                //
                getHandler(metadata, CendariProperties.NERD, "title"),
                getHandler(metadata, CendariProperties.NERD, "description"), 
                getHandler(metadata, CendariProperties.NERD, "subject"), 
                getHandler(metadata, CendariProperties.NERD, "date"),
                getHandler(metadata, CendariProperties.NERD, "coverage"),
                getHandler(metadata, CendariProperties.NERD, "creator"),
                getHandler(metadata, CendariProperties.NERD, "source"),                
                
                //METS in OAI (HA!)
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
