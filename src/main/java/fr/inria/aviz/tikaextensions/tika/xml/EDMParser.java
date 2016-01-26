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
 * @version $Revision$
 */
public class EDMParser extends AbstractXMLParser {
/**
   * 
   */
  private static final long serialVersionUID = -7394174337764559046L;
private static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
private static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
private static final String NAMESPACE_URI_FOAF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

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
    
    private static ContentHandler getFoafHandler(
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            NAMESPACE_URI_FOAF, element,
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
    
    
    private static ContentHandler getLocalContextHandler( String namespaceUri,
        Metadata metadata, Property property, String element, 
        String ...context) {
    return new ContextualElementMetadataHandler(
            namespaceUri, element,
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
                getHandler(metadata, CendariProperties.DESCRIPTION, "description"),
                getTermsHandler(metadata, CendariProperties.DESCRIPTION, "provenance"),
                getFoafHandler(metadata, CendariProperties.ORGANIZATION, "Organization"),
                
                //getHandler(metadata, CendariProperties.DATE, "date"),
                //getHandler(metadata, CendariProperties.DATE, "year"),
                //getTermsHandler(metadata, CendariProperties.DATE, "created"),
                
                new ElementMetadataHandler(
                      DublinCore.NAMESPACE_URI_DC, "date",
                          metadata, CendariProperties.DATE) {
                    protected void addMetadata(String date) {
                      String[] dates = date.split("/");
                      for (String d : dates) 
                          super.addMetadata(d);
                    };
                },
                    
                new ElementMetadataHandler(
                    DublinCore.NAMESPACE_URI_DC, "year",
                        metadata, CendariProperties.DATE) {
                  protected void addMetadata(String date) {
                      String[] dates = date.split("/");
                      for (String d : dates) 
                          super.addMetadata(d);
                    };
              },  
              
              new ElementMetadataHandler(
                  DublinCore.NAMESPACE_URI_DC_TERMS, "created",
                      metadata, CendariProperties.DATE) {
                  protected void addMetadata(String date) {
                    String[] dates = date.split("/");
                    for (String d : dates) 
                        super.addMetadata(d);
                  };
                  },  
            
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
                new ElementAttributeMetadataHandler(NAMESPACE_URI_RDF, "about", metadata, CendariProperties.REFERENCE, "ProvidedCHO"),

                //NOT REALLY IN EDM DATA, but in data wchich use EDM ontology
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.PLACE, "prefLabel", "Place"),
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.DESCRIPTION, "note", "Place"),
                
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.EVENT, "prefLabel", "Event"),

                getEDMHandler( metadata, CendariProperties.DATE, "begin", "Timespan"),
                getEDMHandler( metadata, CendariProperties.DATE, "end", "Timespan"),

                //LocalContext Handler is important for following reasons: It always has to provide the namespace of the elemnt
                // (namespace of the parent element is never checked) - only the local name of the parent element is checked
                getLocalContextHandler( NAMESPACE_URI_EDM, metadata, CendariProperties.DATE, "begin", "Description"),
                getLocalContextHandler( NAMESPACE_URI_EDM, metadata, CendariProperties.DATE, "end", "Description"),

                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.DESCRIPTION, "note", "Event"),
                
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.KEYWORDS, "prefLabel", "Concept"),
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.DESCRIPTION, "note", "Concept"),
                
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.PERSON, "prefLabel", "Person"),
                
                
                //NERD
                getHandler(metadata, CendariProperties.NERD, "title"),
                getHandler(metadata, CendariProperties.NERD, "contributor"),
                getHandler(metadata, CendariProperties.NERD, "creator"), 
                getHandler(metadata, CendariProperties.NERD, "date"),
                getHandler(metadata, CendariProperties.NERD, "description"),
                getEDMHandler(metadata, CendariProperties.NERD, "year"),
                getHandler(metadata, CendariProperties.NERD , "subject"),
                getHandler(metadata, CendariProperties.NERD, "coverage"),
                getTermsHandler(metadata, CendariProperties.NERD, "created"),
                getEDMHandler(metadata, CendariProperties.NERD, "country"),
                getEDMHandler(metadata, CendariProperties.NERD, "Event"),
                getEDMHandler(metadata, CendariProperties.NERD, "Place"),
                getFoafHandler(metadata, CendariProperties.NERD, "Organization"),
                getLocalContextHandler("http://www.w3.org/2004/02/skos/core#", metadata, CendariProperties.NERD, "prefLabel", "Concept"),
                
                getTermsHandler(metadata, CendariProperties.PUBLISHER, "publisher"),
                new ElementMetadataHandler(
                    DublinCore.NAMESPACE_URI_DC_TERMS, "issued",
                        metadata, CendariProperties.DATE) {
                      protected void addMetadata(String date) {
                        String[] dates = date.split("/");
                        for (String d : dates) 
                            super.addMetadata(d);
                      };
                      },  

                getTermsHandler(metadata, CendariProperties.PLACE, "spatial"),
                //Hack for TEL LOD to exclude from Elastic and NERD
                //<dcterms:publisher rdf:resource="http://data.theeuropeanlibrary.org/Agent/The_European_Library"/>
                new ElementAttributeMetadataHandler(
                    NAMESPACE_URI_RDF, "resource",
                    metadata, CendariProperties.PROVIDER, "publisher"),
                    
                //Hack for Cendari Ontology <structural> to exclude from Elastic and NERD
                new ElementAttributeMetadataHandler(
                      NAMESPACE_URI_RDF, "about",
                        metadata, CendariProperties.PROVIDER, "Ontology")
            );

    }     
}
