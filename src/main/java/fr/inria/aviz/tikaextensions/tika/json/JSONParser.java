package fr.inria.aviz.tikaextensions.tika.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TaggedInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.json.simple.parser.ParseException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * JSON parser
 */
public class JSONParser extends AbstractParser {
    /**
   * 
   */
  private static final long serialVersionUID = -8127078373229137155L;
    private static final Set<MediaType> SUPPORTED_TYPES = Collections
                                                                .singleton(MediaType
                                                                        .application("json"));

    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(
            InputStream stream,
            ContentHandler handler,
            Metadata metadata,
            ParseContext context) throws IOException, SAXException,
            TikaException {
        TaggedInputStream tagged = new TaggedInputStream(stream);
        try {
            metadata.add(Metadata.CONTENT_TYPE, "application/json");
            metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            final XHTMLContentHandler xhtml = new XHTMLContentHandler(
                    handler,
                    metadata);
            xhtml.startDocument();

            xhtml.startElement("p");
            org.json.simple.parser.ContentHandler contentHandler = new org.json.simple.parser.ContentHandler() {
                @Override
                public boolean startObjectEntry(String arg0)
                        throws ParseException, IOException {
                    return true;
                }

                @Override
                public boolean startObject() throws ParseException,
                        IOException {
                    // TODO Auto-generated method stub
                    return true;
                }

                @Override
                public void startJSON() throws ParseException,
                        IOException {
                }

                @Override
                public boolean startArray() throws ParseException,
                        IOException {
                    return true;
                }

                @Override
                public boolean primitive(Object o)
                        throws ParseException, IOException {
                    if (o != null) try {
                        xhtml.characters(o.toString()+ " ");
                    }
                    catch (SAXException e) {
                        //ignore
                    }
                    return true;
                }

                @Override
                public boolean endObjectEntry() throws ParseException,
                        IOException {
                    return true;
                }

                @Override
                public boolean endObject() throws ParseException,
                        IOException {
                    return true;
                }

                @Override
                public void endJSON() throws ParseException,
                        IOException {
                }

                @Override
                public boolean endArray() throws ParseException,
                        IOException {
                    return true;
                }
            };
            parser.parse(new InputStreamReader(tagged), contentHandler);
            xhtml.endElement("p");

            xhtml.endDocument();

        } catch (Exception e) {
            tagged.throwIfCauseOf(e);
            throw new TikaException("Error parsing a JSON document", e);
        }
    }
}
