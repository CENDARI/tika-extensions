package eu.cendari.dip.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

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
                getHandler(metadata, CendariProperties.TITLE, "titleInfo"),
                getHandler(metadata, CendariProperties.PLACE, "placeTerm"),
                getHandler(metadata, CendariProperties.PUBLISHER, "recordContentSource"),
                getHandler(metadata, CendariProperties.REFERENCE, "url", "location"),
                getHandler(metadata, CendariProperties.DATE, "dateCreated", "originInfo"),
                getHandler(metadata, CendariProperties.DATE, "dateIssued", "originInfo"),
                getHandler(metadata, CendariProperties.PROVIDER, "publisher"),
                getHandler(metadata, CendariProperties.TYPE, "typeOfResource"),
                getHandler(metadata, CendariProperties.IDENTIFIER, "shelfLocator"),
                getHandler(metadata, CendariProperties.KEYWORDS, "topic", "subject"),
                getHandler(metadata, CendariProperties.KEYWORDS, "hierarchicalGeographic", "subject"),
                getHandler(metadata, CendariProperties.LANG, "languageTerm", "languageOfCataloging"),
                //getHandler(metadata, CendariProperties.PERSON, "namePart", "name"),
                getHandler(metadata, CendariProperties.NERD, "titleInfo"),
                getHandler(metadata, CendariProperties.NERD, "namePart"),
                getHandler(metadata, CendariProperties.NERD, "placeTerm"),
                getHandler(metadata, CendariProperties.NERD, "dateCreated", "originInfo"),
                getHandler(metadata, CendariProperties.NERD, "dateIssued", "originInfo"),
                getHandler(metadata, CendariProperties.NERD, "topic", "subject"),
                getHandler(metadata, CendariProperties.NERD, "hierarchicalGeographic", "subject"),
                new ElementByAttributeValueHandler(metadata, "namePart", "type", "personal", CendariProperties.PERSON)


            );
    }
}
