package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.ElementMetadataHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.TextContentHandler;
import org.xml.sax.ContentHandler;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;

/**
 * Class EAGParser
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class EDMParser extends AbstractXMLParser {
/**
   * 
   */
  private static final long serialVersionUID = -7394174337764559046L;
private static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
private static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("rdf+xml"));
    
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

    private static ContentHandler getTermsHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            DublinCore.NAMESPACE_URI_DC_TERMS, element,
            metadata, property, context);
    }

    private static ContentHandler getEDMHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            NAMESPACE_URI_EDM, element,
            metadata, property, context);
    }
    private static ContentHandler getLocalHandler(String namespaceUri, 
        Metadata metadata, Property property, String element) {
    return new ElementMetadataHandler(namespaceUri, element, metadata, property);
    }

    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler,   
true);
        return new TeeContentHandler(
                defaultContentHandler,
                getHandler(metadata, CendariProperties.TITLE, "title"),
                getHandler(metadata, CendariProperties.CONTRIBUTOR, "contributor"),
                getHandler(metadata, CendariProperties.CREATOR, "creator"), 
                getHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                getHandler(metadata, CendariProperties.IDENTIFIER, "identifier"),
                getHandler(metadata, CendariProperties.RIGHTS, "rights"),
                getHandler(metadata, CendariProperties.DATE, "date"),
                getHandler(metadata, CendariProperties.DATE, "year"),
                getTermsHandler(metadata, CendariProperties.DATE, "created"),
                getHandler(metadata, CendariProperties.SOURCE, "source"),
                getHandler(metadata, CendariProperties.KEYWORDS , "subject"),
                getHandler(metadata, CendariProperties.COVERAGE, "coverage"),
                getHandler(metadata, CendariProperties.TYPE, "type"),
                getHandler(metadata, CendariProperties.TYPE, "type"),
                getTermsHandler(metadata, CendariProperties.RELATION, "isPartOf"),
                getTermsHandler(metadata, CendariProperties.FORMAT, "medium"),
                getHandler(metadata, CendariProperties.LANG, "language"),
                getLocalHandler(NAMESPACE_URI_EDM, metadata, CendariProperties.PLACE, "country"),
                getLocalHandler(NAMESPACE_URI_EDM, metadata, CendariProperties.PROVIDER, "dataProvider"),
                new ElementAttributeMetadataHandler(
                    NAMESPACE_URI_RDF, "about",
                    metadata, CendariProperties.REFERENCE, "ProvidedCHO"),  
                //NERD
                getHandler(metadata, CendariProperties.NERD, "title"),
                getHandler(metadata, CendariProperties.NERD, "contributor"),
                getHandler(metadata, CendariProperties.NERD, "creator"), 
                getHandler(metadata, CendariProperties.NERD, "date"),
                getEDMHandler(metadata, CendariProperties.NERD, "year"),
                getHandler(metadata, CendariProperties.NERD , "subject"),
                getHandler(metadata, CendariProperties.NERD, "coverage"),
                getTermsHandler(metadata, CendariProperties.NERD, "created"),
                getEDMHandler(metadata, CendariProperties.NERD, "country"),
                
                /*<dcterms:publisher rdf:resource="http://data.theeuropeanlibrary.org/Agent/The_European_Library"/>*/
                
                getTermsHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                getTermsHandler(metadata, CendariProperties.DATE, "issued"),
                getTermsHandler(metadata, CendariProperties.PLACE, "spatial"),
                //Hack for TEL LOD to exclude from Elastic and NERD 
                new ElementAttributeMetadataHandler(
                    NAMESPACE_URI_RDF, "resource",
                    metadata, CendariProperties.PROVIDER, "publisher")
            );

    }     
}
