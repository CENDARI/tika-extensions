Tika extensions for CENDARI
=================================

Extensions of Tika to parse files and return textual contents plus metadata. 

Right now, the following XML files are supported:

* EAD
* EAG
* TEI
* OAI-PMH (with embedded dc, ead, mets/mods)
* MODS
* Europeana - EDM
* The European Library (TEL) - LOD daza
* Encyclopedia 1914-1918 data format
* NERD-Only (no special metadata extraction)
**PDF (any format)
**.DOC (any format)
**JSON (any schema) 



To create a specific parser for a new XML schema, do the following steps:

1.  Create a Parser class, just like `eu.cendari.dip.tikaextensions.tika.xml.EADParser.java`
2.  Declare (add) the new class name in the file `src/main/resources/org/apache/tika/META-INF/services/org.apache.tika.parser.Parser`
3.  Optionally add an XSLT transform in `src/main/resources/eu/cendari/dip/tikaextensions/tika/xml/` to cleanup the XML file. See `TEIParser.xsl` for an example (and `eu.cendari.dip.tikaextensions.tika.xml.TEIParser.java`)
4.  Add a test case in `src/test/java/eu/cendari/dip/tikaextensions/TestTika.java` 

Note: Not all tests in TestTika are for general testing, take a look and add your own for specific testing purpose. Annotate the test with @Ignore if you do not want to run it.


Tipp for NERD: If you wish to exclude NERD for some file-types:

* populate the field CendariProperties.PROVIDER from your parser with a particular value of your choice
* add this value to the excludeList in `src/main/java/eu/cendari/dip/tikaextensions/tika/ExcludeCendariIndexer.java
