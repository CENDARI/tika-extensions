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
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class EAGParser extends AbstractXMLParser {
/**
   * 
   */
  private static final long serialVersionUID = -7483213014696616781L;

private static final String NAMESPACE_URI_EAG = "http://www.ministryculture.es/";
    
    private static final Set<MediaType> SUPPORTED_TYPES =
            Collections.singleton(MediaType.application("eag+xml"));
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    private static ContentHandler getEAGHandler(
            Metadata metadata, Property property, String element, 
            String ...context) {
        return new ContextualElementMetadataHandler(
                NAMESPACE_URI_EAG, element,
                metadata, property, context);
    }

    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
        return new TeeContentHandler(
                defaultContentHandler,
                getEAGHandler(metadata, CendariProperties.TITLE, "autform", "identity"),
                getEAGHandler(metadata, CendariProperties.TITLE,"parform", "identity"),
                getEAGHandler(metadata, CendariProperties.KEYWORDS,"subject", "controlaccess"),
                getEAGHandler(metadata, CendariProperties.DESCRIPTION,"description"),
                getEAGHandler(metadata, CendariProperties.DESCRIPTION,"repositorhist"),
                getEAGHandler(metadata, CendariProperties.IDENTIFIER, "repositorid"),
                getEAGHandler(metadata, CendariProperties.PLACE, "country", "location"),
                getEAGHandler(metadata, CendariProperties.PLACE, "municipality", "location"),
                getEAGHandler(metadata, CendariProperties.PLACE, "street", "location"),
                TEIParser.getTEIHandler(metadata, CendariProperties.CONTRIBUTOR,"persName", "respevent", "person"),
                getEAGHandler(metadata, CendariProperties.ORGANIZATION, "autform", "identity"),
                getEAGHandler(metadata, CendariProperties.NERD, "repositorhist"),
                getEAGHandler(metadata, CendariProperties.NERD, "holdings"),
                getEAGHandler(metadata, CendariProperties.NERD, "autform"),
                getEAGHandler(metadata, CendariProperties.NERD, "parform"),
                //getEAGHandler(metadata, CendariProperties.REFERENCE, "source"),
                new AttributeMetadataHandler(NAMESPACE_URI_XML, "lang", metadata,CendariProperties.LANG),
                new ElementAttributeMetadataHandler("", "url", metadata, CendariProperties.REFERENCE, "eagid"),
                new ElementAttributeMetadataHandler("", "href",metadata, CendariProperties.POTENTIAL_REFERENCE, "webpage")
        );

    }     
}
