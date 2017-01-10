package org.sitenv.referenceccda.validators.content;

import org.sitenv.contentvalidator.dto.ContentValidationResult;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.MDHTResultDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brian on 8/15/2016.
 */
@Component
public class ReferenceContentValidator extends BaseCCDAValidator implements CCDAValidator {
    private ContentValidatorService contentValidatorService;

    @Autowired
    public ReferenceContentValidator(ContentValidatorService contentValidatorService) {
        this.contentValidatorService = contentValidatorService;
    }

    @Override
    public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile) throws SAXException {
        ArrayList<RefCCDAValidationResult> results = null;
        if (ccdaFile != null) {
            results = doValidation(validationObjective, referenceFileName, ccdaFile);
        }
        return results;
    }

    private ArrayList<RefCCDAValidationResult> doValidation(String validationObjective, String referenceFileName, String ccdaFile) throws SAXException {
        List<ContentValidationResult> validationResults = contentValidatorService.validate(validationObjective, referenceFileName, ccdaFile);
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
        
        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), null, null, type, "0", 
        		new MDHTResultDetails(false, false, false, false))
                .build();
    }
}
