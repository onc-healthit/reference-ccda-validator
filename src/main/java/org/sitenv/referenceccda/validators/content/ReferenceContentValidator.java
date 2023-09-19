package org.sitenv.referenceccda.validators.content;

import java.util.ArrayList;
import java.util.List;

import org.sitenv.contentvalidator.dto.ContentValidationResult;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.vocabularies.constants.VocabularyConstants.SeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.xml.sax.SAXException;


/**
 * Created by Brian on 8/15/2016.
 */
@RequestScope
@Component
public class ReferenceContentValidator extends BaseCCDAValidator implements CCDAValidator {
    private ContentValidatorService contentValidatorService;

    @Autowired
    public ReferenceContentValidator(ContentValidatorService contentValidatorService) {
        this.contentValidatorService = contentValidatorService;
    }

	@Override
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile) throws SAXException {
		return validateFile(validationObjective, referenceFileName, ccdaFile, 
				false, false, false, 
				SeverityLevel.INFO);
	}
    
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile, boolean curesUpdate, boolean svap2022, boolean svap2023, SeverityLevel severityLevel) throws SAXException {
		ArrayList<RefCCDAValidationResult> results = null;
		if (ccdaFile != null) {
			results = doValidation(validationObjective, referenceFileName, ccdaFile, 
					curesUpdate, svap2022, svap2023,
					severityLevel);
		}
		return results;
	}

	private ArrayList<RefCCDAValidationResult> doValidation(String validationObjective, String referenceFileName,
			String ccdaFile, boolean curesUpdate, boolean svap2022, boolean svap2023, SeverityLevel severityLevel) throws SAXException {
		org.sitenv.contentvalidator.dto.enums.SeverityLevel userSeverityLevelForContentValidation = 
				org.sitenv.contentvalidator.dto.enums.SeverityLevel.valueOf(severityLevel.name());
		
		List<ContentValidationResult> validationResults = contentValidatorService.validate(
				validationObjective, referenceFileName, ccdaFile, 
				curesUpdate, svap2022, svap2023,
				userSeverityLevelForContentValidation);
		
		ArrayList<RefCCDAValidationResult> results = new ArrayList<>();
		for (ContentValidationResult result : validationResults) {
			results.add(createValidationResult(result));
		}
		
		return results;
	}

    private RefCCDAValidationResult createValidationResult(ContentValidationResult result) {
        ValidationResultType type;
        switch(result.getContentValidationResultLevel()){
            case ERROR: type = ValidationResultType.REF_CCDA_ERROR;
                break;
            case WARNING: type = ValidationResultType.REF_CCDA_WARN;
                break;
            default: type = ValidationResultType.REF_CCDA_INFO;
                break;
        }
        
        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), null, null, type, "0")
                .build();
    }
}
