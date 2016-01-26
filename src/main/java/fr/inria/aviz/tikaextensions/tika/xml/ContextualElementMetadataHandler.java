package fr.inria.aviz.tikaextensions.tika.xml;

import java.util.ArrayList;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.xml.ElementMetadataHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.inria.aviz.tikaextensions.utils.TextCleaner;

/**
 * Class ContextualElementMetadataHandler
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class ContextualElementMetadataHandler extends ElementMetadataHandler {
    private String[] context;
    private ArrayList<String> stack = new ArrayList<String>();
    private static final char[] SPACE = new char[] {' '};

    /**
     * @param uri
     * @param localName
     * @param metadata
     * @param targetProperty
     * @param context
     */
    public ContextualElementMetadataHandler(
            String uri,
            String localName,
            Metadata metadata,
            Property targetProperty,
            String...context) {
        super(uri, localName, metadata, targetProperty);
        this.context = context;
    }

    /**
     * @param uri
     * @param localName
     * @param metadata
     * @param targetProperty
     * @param allowDuplicateValues
     * @param allowEmptyValues
     * @param context
     */
    public ContextualElementMetadataHandler(
            String uri,
            String localName,
            Metadata metadata,
            Property targetProperty,
            boolean allowDuplicateValues,
            boolean allowEmptyValues,
            String...context) {
        super(
                uri,
                localName,
                metadata,
                targetProperty,
                allowDuplicateValues,
                allowEmptyValues);
        this.context = context;
    }
    
    protected boolean isMatchingStack() {
        int cn = context.length,
            sn = stack.size();
        
        if (cn > sn)
            return false;
        for (int i = 0; i < cn; i++) {
            if (! context[cn-i-1].equals(stack.get(sn-i-1)))
                return false; 
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMatchingParentElement(String uri, String localName) {
        return true; // to increment parentMatchLevel in superclass
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMatchingElement(String uri, String localName) {
        return super.isMatchingElement(uri, localName) &&
                isMatchingStack();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void startDocument() throws SAXException {
        stack.clear();
        super.startDocument();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(
            String uri,
            String localName,
            String name,
            Attributes attributes) {
        super.startElement(uri, localName, name, attributes);
        stack.add(localName);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(String arg0, String arg1, String arg2) {
        //Hack for stupid inline EAD line break element (lb)
        // will not do any harm, only adds a space
        if (stack.get(stack.size()-1).equals("lb")){
          characters(SPACE, 0, SPACE.length);
        }
        stack.remove(stack.size()-1);
        super.endElement(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void addMetadata(String value) {
        super.addMetadata(TextCleaner.cleanup(value));
    }
    
    
   /* @Override
    public void characters(char[] ch, int start, int length) {
      boolean ampersandExists = false;
      for (char cc:ch){
         if (String.valueOf(cc).equals("&") ){
            ampersandExists = true;
            break;
         }
        }
      char[] ch1 = ch;
       if (ampersandExists) {
         String str = String.copyValueOf(ch);
         str.replaceAll("&", "&amp;");
         ch1 = str.toCharArray();
       }
       
       super.characters(ch1, start, length);
    }
*/
}

