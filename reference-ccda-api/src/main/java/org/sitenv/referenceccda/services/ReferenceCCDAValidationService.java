package org.sitenv.referenceccda.services;

import org.apache.commons.io.IOUtils;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReferenceCCDAValidationService {

    private ReferenceCCDAValidator referenceCCDAValidator;
    private VocabularyCCDAValidator vocabularyCCDAValidator;

    @Autowired
    public ReferenceCCDAValidationService(ReferenceCCDAValidator referenceCCDAValidator, VocabularyCCDAValidator vocabularyCCDAValidator) {
        this.referenceCCDAValidator = referenceCCDAValidator;
        this.vocabularyCCDAValidator = vocabularyCCDAValidator;
    }

    public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName, MultipartFile ccdaFile) {
        ValidationResultsDto resultsDto = new ValidationResultsDto();

        List<RefCCDAValidationResult> validatorResults = runValidators(validationObjective, referenceFileName, ccdaFile);
        ValidationResultsMetaData resultsMetaData = buildValidationMedata(validatorResults, validationObjective);

        resultsDto.setResultsMetaData(resultsMetaData);
        resultsDto.setCcdaValidationResults(validatorResults);
        return resultsDto;
    }

    private List<RefCCDAValidationResult> runValidators(String validationObjective, String referenceFileName,
                                                        MultipartFile ccdaFile) {
        List<RefCCDAValidationResult> validatorResults = new ArrayList<RefCCDAValidationResult>();
        String ccdaFileContents;
        InputStream fileIs = null;
        try {
            fileIs = ccdaFile.getInputStream();
            ccdaFileContents = IOUtils.toString(ccdaFile.getInputStream());
            validatorResults.addAll(doSchemaValidation(validationObjective, referenceFileName, ccdaFileContents));
            if (shouldRunVocabularyValidation(validatorResults)) {
                validatorResults.addAll(DoVocabularyValidation(validationObjective, referenceFileName, ccdaFileContents));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting CCDA contents from provided file", e);
        } finally {
            closeFileInputStream(fileIs);
        }
        return validatorResults;
    }

    private boolean shouldRunVocabularyValidation(List<RefCCDAValidationResult> validatorResults) {
        if(validatorResults.isEmpty()){
            return true;
        }else if(isTheFirstResultASchemaWarning(validatorResults)){
            return true;
        }
        return false;
    }

    private boolean isTheFirstResultASchemaWarning(List<RefCCDAValidationResult> validatorResults) {
        return validatorResults.get(0).getType().getTypePrettyName().equals(ValidationResultType.CCDA_IG_CONFORMANCE_WARN.getTypePrettyName());
    }

    private ArrayList<RefCCDAValidationResult> DoVocabularyValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws IOException {
        return vocabularyCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }

    private List<RefCCDAValidationResult> doSchemaValidation(String validationObjective, String referenceFileName, String ccdaFileContents) {
        return referenceCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }

    private ValidationResultsMetaData buildValidationMedata(List<RefCCDAValidationResult> validatorResults, String ccdaDocType) {
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
}
