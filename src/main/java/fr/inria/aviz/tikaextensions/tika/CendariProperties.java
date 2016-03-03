package fr.inria.aviz.tikaextensions.tika;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;

/**
 * Class Cendari
 * 
 * A collection of Metadata properties recognized by Cendari indexer
 * 
 * @author Jean-Daniel Fekete
 */
public class CendariProperties {
    private static final String PREFIX_CENDARI = "cendari";
    
    public static final String DOMAIN_CENDARI = "cendari.dariah.eu";

    
    /**
     * A language of the intellectual content of the resource. Recommended
     * best practice is to use RFC 3066 [RFC3066], which, in conjunction
     * with ISO 639 [ISO639], defines two- and three-letter primary language
     * tags with optional subtags. Examples include "en" or "eng" for English,
     * "akk" for Akkadian, and "en-GB" for English used in the United Kingdom.
     */
    public static final Property LANG = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "lang");
    
    /**
     * 
     */
    public static final Property PLACE = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "place");

    /**
     * 
     */
    public static final Property PERSON = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "person");
    
    /**
     * 
     */
    public static final Property DATE = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "date");
    
    public static final Property TYPE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "type");

    /**
     * Event type
     */
    public static final Property EVENT = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "event");
    
    
    /**
     * Name of organizations
     */
    public static final Property ORGANIZATION = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "organization");
    
    /**
     * External reference such as URI or DOI.
     */
    public static final Property REFERENCE = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "reference");

    /**
     * External reference such as URI or DOI.
     */
    public static final Property POTENTIAL_REFERENCE = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "potential_reference");

    /**
     * Tag names.
     */
    public static final Property TAG = Property.internalTextBag(
            PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "tag");
    
    /**
     * NERDing names.
     */
    public static final Property NERD =  Property.internalTextBag(
          PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "nerd");
    
    public static final Property PROVIDER =  Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "provider");

    public static final Property RIGHTS = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "rights");

    public static final Property TITLE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "title");
    public static final Property DESCRIPTION = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "description");
    public static final Property PUBLISHER = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "publisher");
    public static final Property CONTRIBUTOR = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "contributor");
    public static final Property CREATOR = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "creator");
    public static final Property KEYWORDS = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "keywords");
    public static final Property FORMAT = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "format");
    public static final Property IDENTIFIER = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "identifier");
    public static final Property RELATION = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "relation");

    public static final Property COVERAGE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "coverage");
    public static final Property SOURCE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "source");
    
    public static final Property LATITUDE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "latitude");
 
    public static final Property LONGITUDE = Property.internalTextBag(
        PREFIX_CENDARI + Metadata.NAMESPACE_PREFIX_DELIMITER + "longitude");
 
}