package eu.cendari.dip.tikaextensions.tika.xml;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.xml.AttributeDependantMetadataHandler;
import org.xml.sax.Attributes;

import eu.cendari.dip.tikaextensions.utils.TextCleaner;


/**
 * @author natasab
 * Handler which may be used to extract a value from particular XML element which has an attribute with provided specific value.
 * The value may be extracted also from sub-element where the attribute is at the parent element.
 *  
 // TODO: Make it more generic (Comment: no time)   
 *
 */
public class ElementByAttributeValueHandler extends AttributeDependantMetadataHandler {
  

      private final String nameHoldingAttribute;

      private String name;
      private String attributeValueToMatch;
      private String namePrefix = "";

      private final StringBuilder buffer = new StringBuilder();
      private String targetPropertyInMetadata = "";
      private String sourcingElement = "";


      private final Metadata metadata;
      public ElementByAttributeValueHandler (Metadata metadata, String elementName, String nameOfTheAttribute, String attributeNameValueMatch, Property targetProperty) {
        
        super(metadata, nameOfTheAttribute, "");
        
        this.metadata = metadata;
        this.nameHoldingAttribute = nameOfTheAttribute;
        this.attributeValueToMatch = attributeNameValueMatch;
        this.targetPropertyInMetadata = targetProperty.getName();
        this.sourcingElement = elementName;
    }
      
    
      @Override
      public void addMetadata(String value) {
        
          if(name == null || name.length() == 0 || !name.equals(attributeValueToMatch)) {
             // We didn't find the attribute which holds the name and has value which we need
             return;
          }
          metadata.add(targetPropertyInMetadata, TextCleaner.cleanup(value));
      }

     @Override
     public void endElement(String uri, String localName, String name) {
            //Metadata will be added only if the localName fits (attribute may be inherited from parent element in XML)
            if (localName.equals(sourcingElement)) {
                addMetadata(buffer.toString());
            }
            buffer.setLength(0);
          
      }

      @Override
      public void startElement(
              String uri, String localName, String name, Attributes attributes) {
          String rawName = attributes.getValue(nameHoldingAttribute);
          if (rawName != null) {
             if (namePrefix == null) {
                this.name = rawName;
             } else {
                this.name = namePrefix + rawName;
             }
          }
          // All other attributes are ignored
      }

      
      @Override
      public void characters(char[] ch, int start, int length) {
          buffer.append(ch, start, length);
      }

 
}
