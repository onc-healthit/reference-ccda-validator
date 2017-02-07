package org.sitenv.referenceccda.controllers;

import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.services.VocabularyService;
import org.sitenv.vocabularies.validation.entities.Code;
import org.sitenv.vocabularies.validation.entities.VsacValueSet;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ReferenceCCDAValidationController {
	@Autowired
	ReferenceCCDAValidationService referenceCcdaValidationService;
	@Autowired
	VocabularyService vocabularyService;
	@Autowired
	VocabularyValidationService validationManager;

	private static final String GITHUB_URL = "https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/git/trees/master?recursive=1";

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

	@RequestMapping(value = "/getbycodeincodesystem", method = RequestMethod.GET)
	public List<Code> getByCodeInCodeSystems(@RequestParam(value = "code", required = true)String code, @RequestParam(value = "codeSystems", required = true) String[] codeSystems){
		return vocabularyService.getByCodeInCodesystems(code, Arrays.asList(codeSystems));
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

	@Cacheable("messagetypeValidationObjectivesAndReferenceFilesMap")
	@RequestMapping(value = "/senderreceivervalidationobjectivesandreferencefiles", method = RequestMethod.GET)
	public Map<String, Map<String, List<String>>> getMapOfSenderAndRecieverValidationObjectivesWithReferenceFiles(){
		return vocabularyService.getMapOfSenderAndRecieverValidationObjectivesWithReferenceFiles();
	}

}
