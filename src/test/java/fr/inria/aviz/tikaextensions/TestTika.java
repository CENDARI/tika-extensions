package fr.inria.aviz.tikaextensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
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
   "/data/ajaloarchiv-ead.xml",
   "/data/B360446201_B343_2_tei.xml",
   "/data/bundesarchiv.ead.xml",
   "/data/crispienartur.ead.xml",
   "/data/D9.1.docx",
   "/data/D9.1.pdf",
   "/data/eadarhiveshub.ead.xml",
   "/data/europeana.xml",
   "/data/frlacinemathequedetoulouse.eag.xml",
   "/data/jdc-items-instead-ead.xml",
   "/data/kalliope_mods.mods.xml",
   "/data/kalliope-dc.dc.xml",
   "/data/kalliope-ead-ns.xml",
   "/data/landesarchiv-nrw.ead.xml",
   "/data/library-of-castle-mikulov_draft;ead.xml",
   "/data/newjsonfile.json",
   "/data/oai-pmh.xml",
   "/data/package.json",
   "/data/providedCHO_1.rdf",
   "/data/providedCHO_2.rdf",
   "/data/providedCHO_3.rdf",
   "/data/providedCHO_4.rdf",
   "/data/providedCHO_5.rdf",
   "/data/sloveniaeag.eag.xml",
   "/data/someoai.xml",
   "/data/tallinna-linnaarhiiv-ead.xml",
   "/data/tel-europeana-1.edm.rdf",
   "/data/tel-europeana-2-lod.rdf",
   "/data/titleeag.eag.xml",
   };

 
   public static List<String> getFileNames(List<String> fileNames, Path dir) {
       try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
           for (Path path : stream) {
               if(path.toFile().isDirectory()) {
                   getFileNames(fileNames, path);
               } else {
                   fileNames.add(path.toAbsolutePath().toString());
               }
           }
       } catch(IOException e) {
           e.printStackTrace();
       }
       return fileNames;
 
   }
    /**
     * Test the Tika indexer.
     * @throws TikaException 
     * @throws SAXException 
     * @throws IOException 
     */
    @Test
    public void test() throws IOException, SAXException, TikaException {
      
        List<String> allKeys = new ArrayList();
        TikaExtensions tika = TikaExtensions.instance();
      
        assertNotNull(tika);
        
        for (String name : fileName) {
          System.out.println("PARSING "+name);
          
          InputStream in =  getClass().getResourceAsStream(name);
          Metadata info = tika.parseDocument(name, null, in, -1);
          if (info != null) {
              for (String key : info.names()) {
                if (!allKeys.contains(key) && info.get(key) != "" && info.get(key) != null && !key.equals("text")) {
                    allKeys.add(key);
                }
                // System.out.println("ELEMENT VALUE "+key.toUpperCase()+":"+" "+info.get(key));
                if (!key.equals("text") && !key.equals("cendari:nerd")){
                  String printStr = Arrays.asList(info.getValues(key)).toString();
                  
                  System.out.println("ELEMENT VALUES "+key.toUpperCase() +":   "+ ( printStr.length()<250 ? printStr:printStr.substring(0, 249)));
                  if (printStr.length()>249){
                    for (int i= 0; i< printStr.length(); i= i+249){
                        System.out.println(printStr.substring(i, ( printStr.length()> i+249?i+249:printStr.length()-1)));
                    }
                   }
                  for ( int i=0; i<info.getValues(key).length ; i++ ){
                     System.out.println("=>"+(info.getValues(key))[i] + ",");
                  }
               }
              
            }
        
          }
        }
        System.out.println("THIS IS WHAT I POPULATE FROM TIKA");
        Collections.sort(allKeys);
        for (String keyS : allKeys ){
            System.out.println(keyS);
        }
 
}
    
    
    /*public void test1() throws IOException, SAXException, TikaException {
      
      List<String> fileNames = new ArrayList<String>();
      fileNames = getFileNames(fileNames, Paths.get("C:\\Users\\natasab\\Documents\\Cendari\\Design\\Data Services\\TikaIndexerSamples") );
      fileNames = getFileNames(fileNames, Paths.get("C:/Users/natasab/Documents/Cendari/Design/Data Services/TikaIndexerSamples") );

      List<String> allKeys = new ArrayList();
      TikaExtensions tika = TikaExtensions.instance();
      
        assertNotNull(tika);
        
        //Delete previously generated files
        
        for (String name:fileNames){
            System.out.println("Checking "+name);
            if (name.endsWith(".t.TXT")) {
              
              System.out.println("Deleting "+name);
              
              FileUtils.deleteQuietly(new File(name));
            }
            
        }

        fileNames = new ArrayList<String>();
        fileNames = getFileNames(fileNames, Paths.get("C:\\Users\\natasab\\Documents\\Cendari\\Design\\Data Services\\TikaIndexerSamples") );
        fileNames = getFileNames(fileNames, Paths.get("C:/Users/natasab/Documents/Cendari/Design/Data Services/TikaIndexerSamples") );

        for (String name:fileNames){
          
          InputStream in = Files.newInputStream(Paths.get(name));  
          if (in != null){
              System.out.println("PARSING "+name +" start = "+new Date(System.currentTimeMillis()));
              Metadata info = tika.parseDocument(name, null, in, -1);
              PrintWriter out = new PrintWriter(name+".t.TXT");
              out.println(info.toString());
              System.out.println("PARSING "+name +" end = "+new Date(System.currentTimeMillis()));
              out.close();
              
              for (String key : info.names()) {
                if (!allKeys.contains(key) && info.get(key) != "" && info.get(key) != null && !key.equals("text")) {
                  allKeys.add(key);
                 }
              }
          }
          else
          {
              System.out.println("No INPUT STREAM for "+name);
          }
          
        }
        
        System.out.println("THIS IS WHAT I POPULATE FROM TIKA");
        Collections.sort(allKeys);
        for (String keyS : allKeys ){
          System.out.println(keyS);
        }
}
*/
}
    


    

