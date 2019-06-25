package org.sitenv.referenceccda.validators;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public abstract class BaseCCDAValidator {
    public static final String UTF8_BOM = "\uFEFF";
    private static Logger logger = Logger.getLogger(BaseCCDAValidator.class);
	
    protected static void trackXPathsInXML(XPathIndexer xpathIndexer, String xmlString) throws SAXException{    	    	
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(xpathIndexer);
        try {
        	xmlString = ifHasUtf8BomThenRemove(xmlString);
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            parser.parse(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error In Line Number Routine: Bad filename, path or invalid document.");
        }
    }
    
    private static String ifHasUtf8BomThenRemove(String xml) {
        if (xml.startsWith(UTF8_BOM)) {
        	logger.warn("Found UTF-8 BOM, removing...");
        	xml = xml.substring(1);
        }
        return xml;
    }
}
