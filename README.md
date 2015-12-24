Tika extensions for CENDARI
=================================

Extensions of Tika to parse files and return textual contents plus metadata. 

Right now, the following XML files are supported:
* EAD
* EAG
* TEI
* OAI-PMH
* Europeana - EDM
* TEL - LOD (excude mode)


To create a specific parser for a new XML schema, do the following steps:

1.  Create a Parser class, just like `fr.inria.aviz.elasticindexer.tika.xml.TEIParser`
2.  Declare the new class in (at the end of) `src/main/resources/org/apache/tika/META-INF/services/org.apache.tika.parser.Parser`
3.  Optionally add an XSLT transform in `src/main/resources/fr/inria/aviz/elasticindexer/tika/xml/` to cleanup the XML file. See `TEIParser.xsl` for an example.
4.  Add a test case in `src/test/java/fr/inria/aviz/elasticindex/TextTika`

