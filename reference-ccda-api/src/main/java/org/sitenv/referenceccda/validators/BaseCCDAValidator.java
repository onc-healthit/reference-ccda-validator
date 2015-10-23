package org.sitenv.referenceccda.validators;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;

public abstract class BaseCCDAValidator {
    protected static void trackXPathsInXML(XPathIndexer xpathIndexer, String xmlString) {
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(xpathIndexer);
            try {
                InputSource inputSource = new InputSource(new StringReader(xmlString));
                parser.parse(inputSource);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error In Line Number Routine: Bad filename, path or invalid document.");
            }
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("Error In Line Number Routine: Unable to parse document for location data.");
        }
    }

}
