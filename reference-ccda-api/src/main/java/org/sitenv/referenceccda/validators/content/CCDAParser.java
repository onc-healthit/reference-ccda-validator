package org.sitenv.referenceccda.validators.content;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CCDAParser {
	
	private static Logger log = Logger.getLogger(CCDAParser.class.getName());
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder        builder;
	private Document               doc;
	
	public void initDoc(String ccdaFile) throws ParserConfigurationException, SAXException, IOException {
		
		log.info("Initializing Document ");
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		doc = builder.parse(new BOMInputStream(IOUtils.toInputStream(ccdaFile, StandardCharsets.UTF_8.name())));
	}
	
	public CCDARefModel parse(String ccdaFile) {		
		
		try {
			initDoc(ccdaFile);
			CCDAConstants.getInstance();
		
			log.info("Creating Model");
			CCDARefModel model = new CCDARefModel();
					
			// NodeList nodeList = (NodeList)CCDAConstants.PATIENT_ROLE_EXP.evaluate(doc, XPathConstants.NODESET);
			
			model.setPatient(CCDAHeaderParser.getPatient(doc));
			CCDABodyParser.parseBody(doc, model);
		
			log.info("Returning Parsed Model");
			model.log();
			
			return model;
		} catch (ParserConfigurationException e1) {
			
			System.out.println("Caught Parser config Excep");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			
			System.out.println("Caught SAX Excep");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			
			System.out.println("Caught IO  Excep");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XPathExpressionException e) {
			
			System.out.println("Caught Xpath Expression ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return null;
	}
	
	

}
