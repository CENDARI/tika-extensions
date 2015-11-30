package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
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
    private static final String NAMESPACE_URI_EAD = "";
    
    private static final Set<MediaType> SUPPORTED_TYPES =
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
        return new ContextualElementMetadataHandler(
                NAMESPACE_URI_EAD, element,
                metadata, property, context);
    }

    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, ParseContext context) {
        ContentHandler defaultContentHandler = new TextContentHandler(handler, true);
        return new TeeContentHandler(
                defaultContentHandler,
                getEADHandler(metadata, TikaCoreProperties.TITLE, 
                        "titleproper", "titlestmt"),
                //TODO check the specific EAD elements
                getEADHandler(metadata, TikaCoreProperties.KEYWORDS, 
                        "term", "keywords"),
                //getTEIHandler(metadata, TikaCoreProperties.CREATOR, "creator"),
                getEADHandler(metadata, TikaCoreProperties.DESCRIPTION, 
                        "description"),
                //getEADHandler(metadata, TikaCoreProperties.PUBLISHER, "publisher"),
                getEADHandler(metadata, TikaCoreProperties.CONTRIBUTOR, 
                        "name", "titleStmt", "respStmt"),
                //getEADHandler(metadata, CendariProperties.DATE, "date"),
                new ElementAttributeMetadataHandler(
                        NAMESPACE_URI_EAD, "normal",
                            metadata, CendariProperties.DATE, "unitdate") {
                    protected void addMetadata(String date) {
                        String[] dates = date.split("/");
                        for (String d : dates) 
                            super.addMetadata(d);
                    };
                },
                //getEADHandler(metadata, TikaCoreProperties.CREATED, "date"),
                //getEADHandler(metadata, TikaCoreProperties.TYPE, "type"),
                //getEADHandler(metadata, TikaCoreProperties.FORMAT, "format"),
                //getEADHandler(metadata, TikaCoreProperties.IDENTIFIER, "identifier"),
                getEADHandler(metadata, TikaCoreProperties.RIGHTS, "licence"),
                getEADHandler(metadata, CendariProperties.NERD, "did"),
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
                getEADHandler(metadata, CendariProperties.NERD, "div"),
                getEADHandler(metadata, CendariProperties.NERD, "dsc"),
                getEADHandler(metadata, CendariProperties.NERD, "dscgrp"),
                getEADHandler(metadata, CendariProperties.NERD, "odd"),
                getEADHandler(metadata, CendariProperties.NERD, "relatedmaterial"),
                getEADHandler(metadata, CendariProperties.NERD, "scopecontent"),
                getEADHandler(metadata, CendariProperties.NERD, "separatedmaterial"),
                getEADHandler(metadata, CendariProperties.NERD, "table"),
                getEADHandler(metadata, CendariProperties.NERD, "relatedmaterial"),
                getEADHandler(metadata, CendariProperties.NERD, "blockquote"),
                getEADHandler(metadata, CendariProperties.NERD, "note"),
                getEADHandler(metadata, CendariProperties.NERD, "publicationstmt"),
                getEADHandler(metadata, CendariProperties.NERD, "seriesstmt"),
                getEADHandler(metadata, CendariProperties.NERD, "titlepage"),
                getEADHandler(metadata, CendariProperties.NERD, "titlestmt"),
                getEADHandler(metadata, CendariProperties.NERD, "abstract"),
                getEADHandler(metadata, CendariProperties.NERD, "date", "creation"),
                getEADHandler(metadata, CendariProperties.NERD, "event"),
                getEADHandler(metadata, CendariProperties.NERD, "item"),
                getEADHandler(metadata, CendariProperties.NERD, "label"),
                getEADHandler(metadata, CendariProperties.NERD, "origination"),
                getEADHandler(metadata, CendariProperties.NERD, "titlestmt"),
                getEADHandler(metadata, CendariProperties.NERD, "name", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "geogname", "controlaccess"),
                getEADHandler(metadata, CendariProperties.NERD, "subject", "controlaccess"),
                
                new AttributeMetadataHandler(NAMESPACE_URI_XML, "lang", metadata, 
                        CendariProperties.LANG),
                new AttributeMetadataHandler(NAMESPACE_URI_EAD, "langcode", metadata, 
                        CendariProperties.LANG),
                new AttributeMetadataHandler("", "url", metadata, 
                            CendariProperties.REFERENCE) {
                  protected void addMetadata(String url) {
                    String cendariUrl = url;
                    if (url.contains(CendariProperties.DOMAIN_CENDARI)){
                      super.addMetadata(url);
                    }
                    
                  };

                }
        );
    }

}
