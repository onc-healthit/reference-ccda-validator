package org.sitenv.referenceccda.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.dto.ResultMetaData;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDATypes;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.schema.ValidationObjectives;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.sitenv.vocabularies.validation.utils.CCDADocumentNamespaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Service
public class ReferenceCCDAValidationService {
    private static Logger logger = Logger.getLogger(ReferenceCCDAValidationService.class);

    private ReferenceCCDAValidator referenceCCDAValidator;
    private VocabularyCCDAValidator vocabularyCCDAValidator;
    private ReferenceContentValidator goldMatchingValidator;
    
    private static final String ERROR_GENERAL_PREFIX = "The service has encountered ";
    private static final String ERROR_PARSING_PREFIX = ERROR_GENERAL_PREFIX + "an error parsing the document. ";
    private static final String ERROR_FOLLOWING_ERROR_POSTFIX = "the following error: ";
    private static final String ERROR_IO_EXCEPTION = ERROR_GENERAL_PREFIX + "the following input/output error: ";
	private static final String ERROR_CLASS_CAST_EXCEPTION = ERROR_PARSING_PREFIX
			+ "Please verify the document is valid against schema and "
			+ "contains a v3 namespace definition: ";
	private static final String ERROR_SAX_PARSE_EXCEPTION = ERROR_PARSING_PREFIX
			+ "Please verify the document does not contain in-line XSL styling and/or address " + ERROR_FOLLOWING_ERROR_POSTFIX;
	private static final String ERROR_GENERIC_EXCEPTION = ERROR_GENERAL_PREFIX + ERROR_FOLLOWING_ERROR_POSTFIX;
	
	@Resource(name = "documentBuilderFactory")
	DocumentBuilderFactory documentBuilderFactory;
	
	@Resource(name = "xPathFactory")
	XPathFactory xPathFactory;

	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		return documentBuilderFactory.newDocumentBuilder();
	}	

    @Autowired
    public ReferenceCCDAValidationService(ReferenceCCDAValidator referenceCCDAValidator, VocabularyCCDAValidator vocabularyCCDAValidator, 
    		ReferenceContentValidator goldValidator) {
        this.referenceCCDAValidator = referenceCCDAValidator;
        this.vocabularyCCDAValidator = vocabularyCCDAValidator;
        this.goldMatchingValidator = goldValidator;
    }
    
    public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName, 
			MultipartFile ccdaFile, String severityLevel) {
    	return validateCCDA(validationObjective, referenceFileName, ccdaFile, severityLevel, true, null, null);
    }

	public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName, 
			MultipartFile ccdaFile, String severityLevel, boolean performContentValidation, 
			String defaultR21ValidationObjective, String defaultR11ValidationObjective) {
		ValidationResultsDto resultsDto = new ValidationResultsDto();
		ValidationResultsMetaData resultsMetaData = new ValidationResultsMetaData();
		List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
		
		try {
			validatorResults = runValidators(validationObjective, referenceFileName, ccdaFile, severityLevel,
					performContentValidation, defaultR21ValidationObjective, defaultR11ValidationObjective);
			resultsMetaData = buildValidationMedata(validatorResults, validationObjective);
			resultsMetaData.setCcdaFileName(ccdaFile.getName());
			resultsMetaData.setCcdaFileContents(new String(ccdaFile.getBytes()));
			
			// ------------------------- INTERNAL CODE CHANGE START
			setDocumentValidity(resultsMetaData);
			// ------------------------- INTERNAL CODE CHANGE END
		} catch (IOException ioE) {
			processValidateCCDAException(resultsMetaData, 
	    			ERROR_IO_EXCEPTION, validationObjective, ioE);
		} catch (SAXException saxE) {
			processValidateCCDAException(resultsMetaData, 
	    			ERROR_SAX_PARSE_EXCEPTION, validationObjective, saxE);
			// ------------------------- INTERNAL CODE CHANGE START
//			resultsMetaData.setServiceError(false);
//			resultsMetaData.setServiceErrorMessage("No service error occurred but recieved an invalid XML document. Error Message :- " + saxE.getMessage());
//			resultsMetaData.setValid(false);
//			resultsMetaData.setCdaSchemaValidationErrorMessage(saxE.getMessage());
			// ------------------------- INTERNAL CODE CHANGE END
//			resultsMetaData.setCcdaDocumentType(validationObjective);
		} catch (ClassCastException ccE) {
			processValidateCCDAException(resultsMetaData, 
					ERROR_CLASS_CAST_EXCEPTION, validationObjective, ccE);
		} catch (Exception catchAllE) {
			processValidateCCDAException(resultsMetaData, 
					ERROR_GENERIC_EXCEPTION, validationObjective, catchAllE);
	    }    
		resultsDto.setResultsMetaData(resultsMetaData);
		resultsDto.setCcdaValidationResults(validatorResults);
		return resultsDto;
	}

	protected XPath getNewXpath(final Document doc) {
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				String nameSpace;
				if (CCDADocumentNamespaces.sdtc.name().equals(prefix)) {
					nameSpace = CCDADocumentNamespaces.sdtc.getNamespace();
				} else {
					nameSpace = CCDADocumentNamespaces.defaultNameSpaceForCcda.getNamespace();
				}
				return nameSpace;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				return null;
			}

			@Override
			public Iterator getPrefixes(String namespaceURI) {
				return null;
			}
		});
		return xpath;
	}	
	
	private static void processValidateCCDAException(ValidationResultsMetaData resultsMetaData, 
			String serviceErrorStart, String validationObjective, Exception exception) {
		resultsMetaData.setServiceError(true);
		if(exception.getMessage() != null) {
			String fullError = serviceErrorStart + exception.getMessage();
			logger.error(fullError);
			resultsMetaData.setServiceErrorMessage(fullError);
		} else {
			String fullError = serviceErrorStart + ExceptionUtils.getStackTrace(exception);
			logger.error(fullError);
			resultsMetaData.setServiceErrorMessage(fullError);
		}
		resultsMetaData.setCcdaDocumentType(validationObjective);
	}
	
	private List<RefCCDAValidationResult> runValidators(String validationObjective, String referenceFileName,
			MultipartFile ccdaFile, String severityLevel, boolean performContentValidation, 
			String defaultR21ValidationObjective, String defaultR11ValidationObjective) 
					throws SAXException, Exception {
		
		List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
		InputStream ccdaFileInputStream = null;
		try {
			ccdaFileInputStream = ccdaFile.getInputStream();
			String ccdaFileContents = IOUtils.toString(new BOMInputStream(ccdaFileInputStream));
			
			validationObjective = defaultValidationObjectiveIfEmpty(validationObjective, defaultR21ValidationObjective,
					defaultR11ValidationObjective, ccdaFileContents);
			
			List<RefCCDAValidationResult> mdhtResults = doMDHTValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
			if (mdhtResults != null && !mdhtResults.isEmpty()) {
            	logger.info("Adding MDHT results");
				validatorResults.addAll(mdhtResults);
			}
			 
            boolean isSchemaErrorInMdhtResults = mdhtResultsHaveSchemaError(mdhtResults);
            boolean isObjectiveAllowingVocabularyValidation = objectiveAllowsVocabularyValidation(validationObjective);

            if (isSchemaErrorInMdhtResults) {
				logger.warn("Doc has schemaError, still do Vocab Validation");
			}

			List<RefCCDAValidationResult> vocabResults = doVocabularyValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
			if (vocabResults != null && !vocabResults.isEmpty()){
                logger.info("Adding Vocabulary results");
				validatorResults.addAll(vocabResults);
			}
			
			// ETT TODO? Pass defaultValidationObjective to doContentValidation(...)
        	if(performContentValidation && objectiveAllowsContentValidation(validationObjective)) {
				List<RefCCDAValidationResult> contentResults = doContentValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
	        	if (contentResults != null && !contentResults.isEmpty()) {
		            		logger.info("Adding Content results");
	            	validatorResults.addAll(contentResults);
	        	}
        	} else {
            	logger.info("Skipping Content validation due to: "
            			+ "validationObjective (" + (validationObjective != null ? validationObjective : "null objective") 
            			+ ") is not relevant or valid for Content validation");        		
        	}
		} catch (IOException e) {
			logger.error("runValidators failed: " + e);
			throw new RuntimeException("Error getting CCDA contents from provided file", e);
		} finally {
			if(ccdaFileInputStream != null) {
				closeFileInputStream(ccdaFileInputStream);
			}
		}
		return validatorResults;
	}

	private String defaultValidationObjectiveIfEmpty(String validationObjective, String defaultR21ValidationObjective,
			String defaultR11ValidationObjective, String ccdaFileContents)
			throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

		if(StringUtils.isEmpty(validationObjective) && (!StringUtils.isEmpty(defaultR11ValidationObjective) 
				|| !StringUtils.isEmpty(defaultR21ValidationObjective))) {
			
			if(defaultR11ValidationObjective.equalsIgnoreCase(defaultR21ValidationObjective)) {
				validationObjective = defaultR21ValidationObjective;
			} else {
			
				InputStream inputStream = IOUtils.toInputStream(ccdaFileContents, "UTF-8");
				
				Document doc = getDocumentBuilder().parse(inputStream);
				
				XPath xpath = getNewXpath(doc);			
				boolean isR11Doc = isDocumentR11CCDA(doc, xpath);			
							
				validationObjective = isR11Doc ? defaultR11ValidationObjective : defaultR21ValidationObjective;
				
				logger.debug("defaultR21ValidationObjective = " + defaultR21ValidationObjective);
				logger.debug("defaultR11ValidationObjective = " + defaultR11ValidationObjective);
							
				StringBuilder msg = new StringBuilder("The validationObjective given is ");
				msg.append(validationObjective == null ? "null" : "empty");
				msg.append(", isR11Doc = ");
				msg.append(isR11Doc);
				msg.append(", setting validationObjective to ");
				msg.append(validationObjective);
				logger.warn(msg.toString());
			}
		}
		
		logger.debug("validationObjective = " + validationObjective);
		return validationObjective;
	}

	private boolean isDocumentR11CCDA(Document doc, XPath xpath) throws XPathExpressionException {
		boolean isR11CCDA = false;
		Number num = (Number) xpath
				.compile("count(/v3:ClinicalDocument/v3:templateId[@root='2.16.840.1.113883.10.20.22.1.1']/@extension)")
				.evaluate(doc, XPathConstants.NUMBER);

		if (num.intValue() == 0) {// MU2 CCDA R1.1 document
			logger.debug("Validating an MU2 CCDA R1.1 document");
			isR11CCDA = true;
		} else {
			logger.debug("Validating an MU3 CCDA R2.1 document");
		}
		return isR11CCDA;
	}

	private boolean mdhtResultsHaveSchemaError(List<RefCCDAValidationResult> mdhtResults) {
        for(RefCCDAValidationResult result : mdhtResults){
            if(result.isSchemaError()){
                return true;
            }
        }
		return false;
	}

    private boolean objectiveAllowsVocabularyValidation(String validationObjective) {
        return !validationObjective.equalsIgnoreCase(ValidationObjectives.Sender.C_CDA_IG_ONLY) 
        		&& !referenceCCDAValidator.isValidationObjectiveMu2Type() 
        		&& !validationObjective.equalsIgnoreCase(CCDATypes.NON_SPECIFIC_CCDA);
    }
	
	private boolean objectiveAllowsContentValidation(String validationObjective) {
		return ReferenceCCDAValidator.isValidationObjectiveACertainType(validationObjective, 
				ValidationObjectives.ALL_UNIQUE_CONTENT_ONLY);
	}

	private List<RefCCDAValidationResult> doMDHTValidation(String validationObjective, String referenceFileName,
			String ccdaFileContents, String severityLevel) 
					throws SAXException, Exception {
		logger.info("Attempting MDHT validation...");
		return referenceCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
	}

	private ArrayList<RefCCDAValidationResult> doVocabularyValidation(String validationObjective,
			String referenceFileName, String ccdaFileContents, String severityLevel) 
					throws SAXException, ParserConfigurationException {
    	logger.info("Attempting Vocabulary validation...");
    	return vocabularyCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
	}

    private List<RefCCDAValidationResult> doContentValidation(String validationObjective, String referenceFileName, String ccdaFileContents,
    		String severityLevel) throws SAXException {
    	logger.info("Attempting Content validation...");
    	return goldMatchingValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
    }

	private ValidationResultsMetaData buildValidationMedata(List<RefCCDAValidationResult> validatorResults,
			String ccdaDocType) {
		ValidationResultsMetaData resultsMetaData = new ValidationResultsMetaData();
		for (RefCCDAValidationResult result : validatorResults) {
			resultsMetaData.addCount(result.getType());
		}
		resultsMetaData.setCcdaDocumentType(ccdaDocType);
		return resultsMetaData;
	}

	private void closeFileInputStream(InputStream fileIs) {
		if (fileIs != null) {
			try {
				fileIs.close();
			} catch (IOException e) {
				throw new RuntimeException("Error closing CCDA file input stream", e);
			}
		}
	}

	/*
	 * Following Method is added to support CCDA validation on file reference.
	 * ccdaReferenceFileName parameter will contains the file reference path to
	 * the CCDA file cache. The file cache must be shared and accessible to the
	 * service.
	 */
	// ------------------------- INTERNAL CODE CHANGE START
	// --------------------------

	public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName,
			String ccdaReferenceFileName, String severityLevel, boolean performContentValidation,
			String defaultR21ValidationObjective, String defaultR11ValidationObjective) {

		MockMultipartFile multipartFile;
		try {
			File file = new File(ccdaReferenceFileName);
			InputStream targetStream = new FileInputStream(file);
			multipartFile = new MockMultipartFile("ccdaFile", targetStream);

			// MockMultipartFile is purposely used to support for file to
			// Multipart
			// conversion rather than the code specified in the comment.
			// The commented code is producing a fatal exception '[Fatal Error]
			// :1:1: Premature end of file'.

			/*
			 * 
			 * DiskFileItem fileItem = new DiskFileItem("file", "text/xml",
			 * false, file.getName(), (int) file.length(),
			 * file.getParentFile()); try { fileItem.getOutputStream(); } catch
			 * (IOException e) { throw new RuntimeException(
			 * "Error getting CCDA contents from provided file", e); }
			 * MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
			 * 
			 */

		} catch (Exception e) {
			logger.error("runValidators failed: " + e);
			throw new RuntimeException("Error getting CCDA contents from provided file", e);
		}
		return validateCCDA(validationObjective, referenceFileName, multipartFile, severityLevel, 
				performContentValidation, defaultR21ValidationObjective, defaultR11ValidationObjective);
	}
	
	/*
	 * This is an overloaded method that accepts multipartFile for CCDA validation. 
	 * Additionally it will accept severityLevel. The results are filtered based on the
	 * serverityLevel passed.
	 */
	public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName,
			MultipartFile ccdaFile, String severityLevel, boolean performMDHTValidation, boolean performVocabularyValidation,
			boolean performContentValidation, String defaultR21ValidationObjective, String defaultR11ValidationObjective) {

		ValidationResultsDto resultsDto = new ValidationResultsDto();
		
		ValidationResultsMetaData resultsMetaData = new ValidationResultsMetaData();
		List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
		
	    String threadId = Thread.currentThread().getName() + "-" + System.currentTimeMillis() + "-" + ccdaFile.getSize();
	    StringBuilder out = new StringBuilder(threadId);
		
		try {		    
			long startTime = System.currentTimeMillis();
			
			validatorResults = runValidators(validationObjective, referenceFileName, ccdaFile, severityLevel, 
					performMDHTValidation, performVocabularyValidation, performContentValidation, out, 
					defaultR21ValidationObjective, defaultR11ValidationObjective);

			long stopTime = System.currentTimeMillis();
			
		    out.append(" runValidators: " + (stopTime - startTime) + " ms," );
			
		    startTime = stopTime;
		    
		    resultsMetaData = buildValidationMedata(validatorResults, validationObjective);

		    resultsMetaData.setCcdaFileName(ccdaFile.getOriginalFilename());
			resultsMetaData.setCcdaFileContents(new String(ccdaFile.getBytes()));

			stopTime = System.currentTimeMillis();	
			out.append(" buildValidationMedata: " + (stopTime - startTime) + " ms");

			setDocumentValidity(resultsMetaData);
			logger.info(out.toString());

		} catch (SAXException e) {
			resultsMetaData.setServiceError(false);
			resultsMetaData.setServiceErrorMessage(null);
			resultsMetaData.setCcdaDocumentType(validationObjective);
			resultsMetaData.setValid(false);
			resultsMetaData.setCdaSchemaValidationErrorMessage(e.getMessage());
		} catch (Exception e) {
			resultsMetaData.setServiceError(false);
			resultsMetaData.setServiceErrorMessage(null);
			resultsMetaData.setCcdaDocumentType(validationObjective);
			resultsMetaData.setValid(false);
			resultsMetaData.setCdaSchemaValidationErrorMessage(e.getMessage());			
		}
		
		resultsDto.setResultsMetaData(resultsMetaData);
		resultsDto.setCcdaValidationResults(validatorResults);
		
		return resultsDto;
	}
	
	private void setDocumentValidity(ValidationResultsMetaData resultsMetaData) {
		boolean isDocValid = true;
		List<ResultMetaData> resultMetaData = resultsMetaData.getResultMetaData();
		for (Iterator<ResultMetaData> iterator = resultMetaData.iterator(); iterator.hasNext();) {
			ResultMetaData eachResultMetaData = (ResultMetaData) iterator.next();
			if(StringUtils.containsIgnoreCase(eachResultMetaData.getType(), ERROR) && eachResultMetaData.getCount() > 0) {
				isDocValid = false;
				break;
			}
		}
		resultsMetaData.setValid(isDocValid);
	}

	public class ZippedFileInputStream extends InputStream {

	    private ZipInputStream is;

	    public ZippedFileInputStream(ZipInputStream is){
	        this.is = is;
	    }

	    @Override
	    public int read() throws IOException {
	        return is.read();
	    }

	    @Override
	    public void close() throws IOException {
	        is.closeEntry();
	    }
	}
	
	private List<RefCCDAValidationResult> runValidators(String validationObjective, String referenceFileName,
			MultipartFile ccdaFile, String severityLevel,boolean performMDHTValidation, boolean performVocabularyValidation,
			boolean performContentValidation, StringBuilder out, String defaultR21ValidationObjective, String defaultR11ValidationObjective) throws SAXException, Exception {
		
		List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
		List<RefCCDAValidationResult> filterResults = new ArrayList<RefCCDAValidationResult>();
		String ccdaFileContents="";
	    		
		try {		    
			long startTime = System.currentTimeMillis();
		    			
			if (ccdaFile.getOriginalFilename().toLowerCase().endsWith(".zip")) {
				 ZipInputStream zis = new ZipInputStream(ccdaFile.getInputStream());
				 while (zis.getNextEntry() != null && ccdaFileContents.length()==0) {
					 ccdaFileContents = IOUtils.toString(new BOMInputStream(new ZippedFileInputStream(zis)));
				 }
			} else {
				ccdaFileContents = IOUtils.toString(new BOMInputStream(ccdaFile.getInputStream()));
			}
			
			long stopTime = System.currentTimeMillis();
			
			out.append(" DoctoString:" + (stopTime - startTime) + " ms, ");
			
			validationObjective = defaultValidationObjectiveIfEmpty(validationObjective, defaultR21ValidationObjective,
					defaultR11ValidationObjective, ccdaFileContents);
							
            boolean isObjectiveAllowingVocabularyValidation = objectiveAllowsVocabularyValidation(validationObjective);
            boolean isSchemaErrorInMdhtResults = false;
			if (performMDHTValidation) {

				startTime = System.currentTimeMillis();
				
			    List<RefCCDAValidationResult> mdhtResults = doMDHTValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
	            if(mdhtResults != null && !mdhtResults.isEmpty()) {
	            	logger.info("Adding MDHT results");
	            	validatorResults.addAll(mdhtResults);
	            }
	            
				stopTime = System.currentTimeMillis();
				out.append(" MDHT:" + (stopTime - startTime) + " ms, ");

	            isSchemaErrorInMdhtResults = mdhtResultsHaveSchemaError(mdhtResults);

				if (isSchemaErrorInMdhtResults) {
					logger.warn("Doc has schemaError, still do Vocab Validation");
				}
			}
			
			if (performVocabularyValidation || objectiveAllowsContentValidation(validationObjective)) {

				startTime = System.currentTimeMillis();
                
                if (performVocabularyValidation) {
                	List<RefCCDAValidationResult> vocabResults = doVocabularyValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
               
	            	if(vocabResults != null && !vocabResults.isEmpty()) {
	            		logger.info("Adding Vocabulary results");
	            		validatorResults.addAll(vocabResults);
	            	}
                }
					
				stopTime = System.currentTimeMillis();
				out.append(" Vocab:" + (stopTime - startTime) + " ms, " );
				
            	if (objectiveAllowsContentValidation(validationObjective) && performContentValidation) {
	                List<RefCCDAValidationResult> contentResults = doContentValidation(validationObjective, referenceFileName, ccdaFileContents, severityLevel);
	            	if(contentResults != null && !contentResults.isEmpty()) {
	            		logger.info("Adding Content results");
	                	validatorResults.addAll(contentResults);
	            	}
            	} else {
                	logger.info("Skipping Content validation due to: "
                			+ "validationObjective (" + (validationObjective != null ? validationObjective : "null objective") 
                			+ ") is not relevant or valid for Content validation "
                			+ (!performContentValidation ? " (suppressed by performContentValidation=false)" : ""));            		
            	}
				
			} else {
            	String separator = !isObjectiveAllowingVocabularyValidation && isSchemaErrorInMdhtResults ? " and " : "";
            	logger.info("Skipping Vocabulary (and thus Content) validation due to: " 
            			+ (isObjectiveAllowingVocabularyValidation ? "" : "validationObjective POSTed: " 
            			+ (validationObjective != null ? validationObjective : "null objective") + separator) 
            			+ (isSchemaErrorInMdhtResults ? "C-CDA Schema error(s) found" : ""));
			}
			
		    startTime = System.currentTimeMillis();
		    
			filterResults = filterResultsOnSeverity(validatorResults, severityLevel);
			
			stopTime = System.currentTimeMillis();
			out.append(" Filter:" + (stopTime - startTime) + " ms");
			
			} catch (IOException e) {
			logger.error("runValidators failed: " + e);
			throw new RuntimeException("Error getting CCDA contents from provided file", e);
		}
		
		return filterResults;
	}
	
	private static final String WARNING = "Warning";
	private static final String ERROR = "Error";
	
	private List<RefCCDAValidationResult> filterResultsOnSeverity(final List<RefCCDAValidationResult> validatorResults, final String filterByseverityLevel) {
			
			List<RefCCDAValidationResult> filteredFinalList = new ArrayList<RefCCDAValidationResult>();
			
			for (Iterator<RefCCDAValidationResult> iterator = validatorResults.iterator(); iterator
					.hasNext();) {
				
				RefCCDAValidationResult eachActualValidationResult = (RefCCDAValidationResult) iterator
						.next();
				
				ValidationResultType actualType = eachActualValidationResult.getType();
				
				String validationResultType = actualType.getTypePrettyName();
				
				if(filterByseverityLevel.equalsIgnoreCase(ERROR)) {
					if(StringUtils.contains(validationResultType, ERROR)) {
						filteredFinalList.add(eachActualValidationResult);
					}
				} else if (filterByseverityLevel.equalsIgnoreCase(WARNING)) {
					if (StringUtils.contains(validationResultType, ERROR)
							|| StringUtils.contains(validationResultType, WARNING)) {
						filteredFinalList.add(eachActualValidationResult);
					}
				} else {
					filteredFinalList = validatorResults;
				}
			}
			return filteredFinalList;
		}
		
	
	// ------------------------- INTERNAL CODE CHANGE END --------------------------
}
