package org.sitenv.referenceccda.services;

import org.sitenv.vocabularies.validation.entities.Code;
import org.sitenv.vocabularies.validation.entities.VsacValueSet;
import org.sitenv.vocabularies.validation.services.VocabularyCodeService;
import org.sitenv.vocabularies.validation.services.VocabularyValuesetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Brian on 2/23/2016.
 */
@Service
public class VocabularyService {
    VocabularyValuesetService vocabularyValuesetService;
    VocabularyCodeService vocabularyCodeService;

    @Autowired
    public VocabularyService(VocabularyValuesetService vocabularyValuesetService, VocabularyCodeService vocabularyCodeService) {
        this.vocabularyValuesetService = vocabularyValuesetService;
        this.vocabularyCodeService = vocabularyCodeService;
    }

    public java.util.List<VsacValueSet> getValuesetsByOids(List<String> valuesetOids){
        return vocabularyValuesetService.getValuesetsByOids(new HashSet<>(valuesetOids));
    }

    public boolean isCodeAndDisplayNameFoundInCodeSystems(String code, String displayName, List<String> codeSystems){
        return vocabularyCodeService.isFoundByCodeAndDisplayNameInCodeSystems(code, displayName, new HashSet<>(codeSystems));
    }

    public boolean isCodeFoundInCodesystems(String code, List<String> codeSystems){
        return vocabularyCodeService.isFoundByCodeInCodeSystems(code, new HashSet<>(codeSystems));
    }

    public boolean isCodeFoundInValuesetOids(String code, List<String> valuesetOids){
        return vocabularyValuesetService.isFoundByCodeInValuesetOids(code, new HashSet<>(valuesetOids));
    }

    public List<Code> getByCodeInCodesystems(String code, List<String> codeSystems){
        return vocabularyCodeService.getByCodeInCodeSystems(code, codeSystems);
    }
}
