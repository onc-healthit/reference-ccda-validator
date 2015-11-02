package org.sitenv.referenceccda.controllers;

import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReferenceCCDAValidationController {

	@Autowired
	ReferenceCCDAValidationService referenceCcdaValidationService;

	@RequestMapping(value = "/", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public ValidationResultsDto doValidation(
			@RequestParam(value = "validationObjective", required = true) String validationObjective,
			@RequestParam(value = "referenceFileName", required = true) String referenceFileName,
			@RequestParam(value = "ccdaFile", required = true) MultipartFile ccdaFile) {
		return referenceCcdaValidationService.validateCCDA(validationObjective, referenceFileName, ccdaFile);
	}

}
