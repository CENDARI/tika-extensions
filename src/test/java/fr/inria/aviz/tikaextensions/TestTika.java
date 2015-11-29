package fr.inria.aviz.tikaextensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.junit.Test;

import fr.inria.aviz.tikaextensions.tika.CendariProperties;

/**
 * Class TestTika
 */
public class TestTika extends TestCase {

    static final String[] fileName = {
        "/data/errors/books-from-ww1-period-kept-by-state-library-of-berlin.ead.xml",
        "/data/oai-pmh.xml",
        "/data/frlacinemathequedetoulouse.eag.xml",
        "/data/library-of-castle-mikulov_draft;ead.xml",
        "/data/eadarhiveshub.ead.xml",
        "/data/B360446201_B343_2_tei.xml",
        "/data/sloveniaeag.eag.xml",
	"/data/D9.1.docx",
	"/data/D9.1.pdf",
    };

    /**
     * Test the Tika indexer.
     * @throws FileNotFoundException 
     */
    @Test
    public void test() throws FileNotFoundException {
        TikaExtensions tika = TikaExtensions.instance();
        
        assertNotNull(tika);
      /*  
        for (String name : fileName) {
            byte[] content;
            try {
                content = IOUtils.toByteArray(getClass().getResource(name));
                Metadata info = tika.parseDocument(name, null, content, -1);
//                String text = info.getText();
//                if (text.length() > 100)
//                    info.setText(text.substring(0, 100));
                if (info != null) {
                    for (String key : info.names()) {
                        System.out.println(key+": "+info.get(key));
                    }
                }
            }
            catch(IOException e) {
                fail("Cannot load file "+name); 
            }
        }*/
        
        for (String name : fileName) {
          //InputStream content
          //Files.r
          
          InputStream in =  getClass().getResourceAsStream(name);
          
          Metadata info = tika.parseDocument(name, null, in, -1);
//              String text = info.getText();
//              if (text.length() > 100)
//                  info.setText(text.substring(0, 100));
          if (info != null) {
              for (String key : info.names()) {
                 
                  System.out.println(key+": ");
                  String[] myValues = info.getValues(key);
                  for (String myValue:myValues){
                        System.out.println(myValue);
                  }
                      
              }
          }
      }
    }
    
    
    
}
