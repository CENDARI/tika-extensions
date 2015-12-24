package fr.inria.aviz.tikaextensions.tika.xml;

import org.apache.tika.sax.TextContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CendariTextContentHandler extends TextContentHandler {

  private static final char[] SPACE = new char[] {' '};
  private final ContentHandler delegateT;
 // private final boolean addSpaceBetweenElements;

  public CendariTextContentHandler(ContentHandler delegateP) {
    super(delegateP, true);
    delegateT = delegateP;
    
}

//  public CendariTextContentHandler(ContentHandler delegate, 
//              boolean addSpaceBetweenElements) 
//  {
//    super(delegate, addSpaceBetweenElements);
//  }

  
  @Override
  public void endElement (String uri, String localName, String qName)
      throws SAXException
  {
      System.out.println("ADDING SPACE AFTER ELEMENT "+localName);
      delegateT.characters(SPACE, 0, SPACE.length);
  }

  @Override
  public void characters(char[] ch, int start, int length)
          throws SAXException {
//     System.out.println("CHARACHERTS ");
//     for (char cc:ch){
//       System.out.print(cc);
//     }
//     System.out.println("END CHAR");
//      delegateT.characters(ch, start, length);
  }
}
