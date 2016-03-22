package org.sitenv.referenceccda.services;

import org.sitenv.vocabularies.validation.entities.VsacValueSet;
import org.sitenv.vocabularies.validation.services.VocabularyValuesetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by Brian on 2/23/2016.
 */
@Service
public class VocabularyService {
    VocabularyValuesetService vocabularyValuesetService;

    @Autowired
    public VocabularyService(VocabularyValuesetService vocabularyValuesetService) {
        this.vocabularyValuesetService = vocabularyValuesetService;
    }

    public java.util.List<VsacValueSet> getValuesetsByOids(java.util.List<String> valuesetOids){
        return vocabularyValuesetService.getValuesetsByOids(new HashSet<>(valuesetOids));
    }
}
