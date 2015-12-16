package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.AttributeMetadataHandler;
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
private static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
private static final String NAMESPACE_URI_ORE = "http://www.openarchives.org/ore/terms/";

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
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
        return new TeeContentHandler(
                defaultContentHandler,
                getHandler(metadata, TikaCoreProperties.TITLE, "title"),
                getHandler(metadata, TikaCoreProperties.CONTRIBUTOR, "contributor"),
                getHandler(metadata, TikaCoreProperties.CREATOR, "creator"), 
                getHandler(metadata, TikaCoreProperties.PUBLISHER, "publisher"),
                getHandler(metadata, TikaCoreProperties.IDENTIFIER, "identifier"),
                getHandler(metadata, TikaCoreProperties.RIGHTS, "rights"),
                getHandler(metadata, TikaCoreProperties.CREATED, "date"),
                getHandler(metadata, TikaCoreProperties.CREATED, "year"),
                getHandler(metadata, TikaCoreProperties.SOURCE, "source"),
                getHandler(metadata, TikaCoreProperties.KEYWORDS , "subject"),
                getHandler(metadata, TikaCoreProperties.IDENTIFIER, "identifier"),
                getHandler(metadata, TikaCoreProperties.COVERAGE, "coverage"),
                getHandler(metadata, TikaCoreProperties.TYPE, "type"),
                getTermsHandler(metadata, TikaCoreProperties.CREATED, "created"),
                getTermsHandler(metadata, TikaCoreProperties.RELATION, "isPartOf"),
                getTermsHandler(metadata, TikaCoreProperties.FORMAT, "medium"),
                getHandler(metadata, TikaCoreProperties.LANGUAGE, "language"),
                getLocalHandler(NAMESPACE_URI_EDM, metadata, CendariProperties.PLACE, "country"),
                //new AttributeMetadataHandler(NAMESPACE_URI_EDM, "landingPage", metadata, CendariProperties.REFERENCE),
                new AttributeMetadataHandler("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource", metadata, 
                    CendariProperties.REFERENCE) {
                      protected void addMetadata(String url) {
                        String cendariUrl = url;
                        if (url.contains("http://europeana.eu/portal/record/")){
                          super.addMetadata(url);
                        }
                        
                      };
                    },
                

                //NERD
                getHandler(metadata, CendariProperties.NERD, "title"),
                getHandler(metadata, CendariProperties.NERD, "contributor"),
                getHandler(metadata, CendariProperties.NERD, "publisher"), 
                getHandler(metadata, CendariProperties.NERD, "date"),
                getEDMHandler(metadata, CendariProperties.NERD, "year"),
                getHandler(metadata, CendariProperties.NERD , "subject"),
                getHandler(metadata, CendariProperties.NERD, "coverage"),
                getTermsHandler(metadata, CendariProperties.NERD, "created"),
                getEDMHandler(metadata, CendariProperties.NERD, "country")

            );

    }     
}
