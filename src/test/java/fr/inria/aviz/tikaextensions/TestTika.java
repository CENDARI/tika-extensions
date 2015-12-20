package fr.inria.aviz.tikaextensions;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Class TestTika
 */
public class TestTika extends TestCase {

 static final String[] fileName = {
//"/data/errors/books-from-ww1-period-kept-by-state-library-of-berlin.ead.xml",
//"/data/someoai.xml",
   "/data/europeana.xml",
   //       "/data/oai-pmh.xml",
  //     "/data/frlacinemathequedetoulouse.eag.xml",
  //    "/data/library-of-castle-mikulov_draft;ead.xml",
 // "/data/eadarhiveshub.ead.xml",
   //     "/data/B360446201_B343_2_tei.xml",
    //     "/data/sloveniaeag.eag.xml",
   //   "/data/titleeag.eag.xml",
   // "/data/D9.1.docx",
   // "/data/D9.1.pdf",
   //   "/data/package.json",
   // "/data/newjsonfile.json",
   //   "/data/crispienartur.ead.xml",
   };

    /**
     * Test the Tika indexer.
     * @throws TikaException 
     * @throws SAXException 
     * @throws IOException 
     */
    @Test
    public void test() throws IOException, SAXException, TikaException {
        TikaExtensions tika = TikaExtensions.instance();
        
        assertNotNull(tika);
        for (String name : fileName) {
          //InputStream content
          //Files.r
          
          InputStream in =  getClass().getResourceAsStream(name);
          
          Metadata info = tika.parseDocument(name, null, in, -1);
          if (info != null) {
              for (String key : info.names()) {
                 System.out.println("ELEMENT "+key.toUpperCase()+":"+ info.get (key));
           }
              
          }
        }
 }
 
}
    


    

