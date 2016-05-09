package org.sitenv.referenceccda.controllers;

import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.services.VocabularyService;
import org.sitenv.vocabularies.validation.entities.VsacValueSet;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@RestController
public class ReferenceCCDAValidationController {

	@Autowired
	ReferenceCCDAValidationService referenceCcdaValidationService;
	@Autowired
	VocabularyService vocabularyService;
	@Autowired
	VocabularyValidationService validationManager;

	@RequestMapping(value = "/", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public ValidationResultsDto doValidation(
			@RequestParam(value = "validationObjective", required = true) String validationObjective,
			@RequestParam(value = "referenceFileName", required = true) String referenceFileName,
			@RequestParam(value = "ccdaFile", required = true) MultipartFile ccdaFile) {
		return referenceCcdaValidationService.validateCCDA(validationObjective, referenceFileName, ccdaFile);
	}

	@RequestMapping(value = "/getvaluesetsbyoids", method = RequestMethod.GET)
	public List<VsacValueSet> getValuesetsByOids(@RequestParam(value = "oids", required = true) String[] valuesetOids){
		return vocabularyService.getValuesetsByOids(Arrays.asList(valuesetOids));
	}

	@RequestMapping(value = "/iscodeandisplaynameincodesystem", method = RequestMethod.GET)
	public boolean isCodeAndDisplayNameFoundInCodeSystems(@RequestParam(value = "code", required = true)String code, @RequestParam(value = "displayName", required = true)String displayName, @RequestParam(value = "codeSystems", required = true) String[] codeSystems){
		return vocabularyService.isCodeAndDisplayNameFoundInCodeSystems(code, displayName, Arrays.asList(codeSystems));
	}

	@RequestMapping(value = "/iscodeincodesystem", method = RequestMethod.GET)
	public boolean isCodeFoundInCodeSystems(@RequestParam(value = "code", required = true)String code, @RequestParam(value = "codeSystems", required = true) String[] codeSystems){
		return vocabularyService.isCodeFoundInCodesystems(code, Arrays.asList(codeSystems));
	}

	@RequestMapping(value = "/iscodeinvalueset", method = RequestMethod.GET)
	public boolean isCodeFoundInValuesetOids(@RequestParam(value = "code", required = true)String code, @RequestParam(value = "valuesetOids", required = true) String[] valuesetOids){
		return vocabularyService.isCodeFoundInValuesetOids(code, Arrays.asList(valuesetOids));
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
