package fr.inria.aviz.tikaextensions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Class TestTika
 */
public class TestTika extends TestCase {

 static final String[] fileName = {
//"/data/errors/books-from-ww1-period-kept-by-state-library-of-berlin.ead.xml",
"/data/someoai.xml",
   "/data/europeana.xml",
   "/data/tel-europeana-1.xml",
   "/data/tel-europeana-2-lod.xml",
          "/data/oai-pmh.xml",
       "/data/frlacinemathequedetoulouse.eag.xml",
 
/*   "/data/library-of-castle-mikulov_draft;ead.xml",
  "/data/eadarhiveshub.ead.xml",
        "/data/B360446201_B343_2_tei.xml",
        "/data/sloveniaeag.eag.xml",
      "/data/titleeag.eag.xml",
//    "/data/D9.1.docx",
//    "/data/D9.1.pdf",
      "/data/package.json",
    "/data/newjsonfile.json",
      "/data/crispienartur.ead.xml",
*/   };

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
        
        List<String> allKeys = new ArrayList();
        
        for (String name : fileName) {
          System.out.println("PARSING "+name);
          System.out.println("==============================================================================");
          //InputStream content
          //Files.r
          
          InputStream in =  getClass().getResourceAsStream(name);
          
          Metadata info = tika.parseDocument(name, null, in, -1);
          if (info != null) {
              for (String key : info.names()) {
                if (!allKeys.contains(key) && info.get(key) != "" && info.get(key) != null && !key.equals("text")) {
                    allKeys.add(key);
                }
                // System.out.println("ELEMENT VALUE "+key.toUpperCase()+":"+" "+info.get(key));
//                if (!key.equals("text")){
                   System.out.println("ELEMENT VALUES "+key.toUpperCase()+":"+" "+Arrays.asList(info.getValues(key)));
//                 for ( int i=0; i<info.getValues(key).length ; i++ ){
//                     System.out.println("=>"+(info.getValues(key))[i] + ",");
//                 }
              }
              
          }
        }
        
        
        System.out.println("THIS IS WHAT I POPULATE FROM TIKA");
        Collections.sort(allKeys);
        for (String keyS : allKeys ){
            System.out.println(keyS);
        }
 }
 
}
    


    

