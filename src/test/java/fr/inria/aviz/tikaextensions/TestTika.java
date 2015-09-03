package fr.inria.aviz.tikaextensions;

import java.io.IOException;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.junit.Test;

/**
 * Class TestTika
 */
public class TestTika extends TestCase {

    static final String[] fileName = {
//        "/data/errors/books-from-ww1-period-kept-by-state-library-of-berlin.ead.xml",
        "/data/oai-pmh.xml",
        "/data/frlacinemathequedetoulouse.eag.xml",
        "/data/library-of-castle-mikulov_draft;ead.xml",
        "/data/B360446201_B343_2_tei.xml",
        "/data/package.json",
	"/data/D9.1.docx",
	"/data/D9.1.pdf",
    };

    /**
     * Test the Tika indexer.
     */
    @Test
    public void test() {
        TikaExtensions tika = TikaExtensions.instance();
        
        assertNotNull(tika);
        
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
        }
    }
}
