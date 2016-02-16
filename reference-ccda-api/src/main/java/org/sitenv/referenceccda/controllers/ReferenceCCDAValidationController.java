package org.sitenv.referenceccda.controllers;

import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class ReferenceCCDAValidationController {

	@Autowired
	ReferenceCCDAValidationService referenceCcdaValidationService;
	@Autowired
	VocabularyValidationService validationManager;

	@RequestMapping(value = "/", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public ValidationResultsDto doValidation(
			@RequestParam(value = "validationObjective", required = true) String validationObjective,
			@RequestParam(value = "referenceFileName", required = true) String referenceFileName,
			@RequestParam(value = "ccdaFile", required = true) MultipartFile ccdaFile) {
		return referenceCcdaValidationService.validateCCDA(validationObjective, referenceFileName, ccdaFile);
	}

    @RequestMapping(value = "/downloadreferenceccdaartifact/{artifactType}", method = RequestMethod.GET)
	public void downloadReferenceCCDAArtifact(HttpServletResponse httpServletResponse, @PathVariable("artifactType") String artifactType) throws IOException{
        File fileToDownload = new File("referenceccdaservice-bundle." + artifactType);
        String mimeType = "application/octet-stream";

        httpServletResponse.setContentType(mimeType);
        httpServletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileToDownload.getName()));
        httpServletResponse.setContentLength((int)fileToDownload.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(fileToDownload));

        FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());
    }
}
