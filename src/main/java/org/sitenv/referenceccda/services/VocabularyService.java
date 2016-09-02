package org.sitenv.referenceccda.services;

import org.sitenv.referenceccda.services.wrappers.GithubResponseWrapper;
import org.sitenv.referenceccda.services.wrappers.TestDataTreeWrapper;
import org.sitenv.vocabularies.validation.entities.Code;
import org.sitenv.vocabularies.validation.entities.VsacValueSet;
import org.sitenv.vocabularies.validation.services.VocabularyCodeService;
import org.sitenv.vocabularies.validation.services.VocabularyValuesetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by Brian on 2/23/2016.
 */
@Service
public class VocabularyService {
    private VocabularyValuesetService vocabularyValuesetService;
    private VocabularyCodeService vocabularyCodeService;
    private static final String GITHUB_URL = "https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/git/trees/master?recursive=1";

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

    public Map<String, Map<String, List<String>>> getMapOfSenderAndRecieverValidationObjectivesWithReferenceFiles(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GithubResponseWrapper> responseEntity = restTemplate.exchange(GITHUB_URL, HttpMethod.GET, null, new ParameterizedTypeReference<GithubResponseWrapper>() {
        });

        Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap = new HashMap<>();
        for(TestDataTreeWrapper testDataTreeWrapper : responseEntity.getBody().getTree()){
            if(!(testDataTreeWrapper.getPath().equalsIgnoreCase("license") || testDataTreeWrapper.getPath().equalsIgnoreCase("README.md"))){
                if(isMessageTypeInMap(messageTypeValidationObjectiveReferenceFilesMap, testDataTreeWrapper)){
                    if(isValidationObjectiveInMap(messageTypeValidationObjectiveReferenceFilesMap, testDataTreeWrapper)){
                        addReferenceFileNameToListInValidationObjectiveMap(messageTypeValidationObjectiveReferenceFilesMap, testDataTreeWrapper);
                    }else{
                        addValidationObjectiveToMap(messageTypeValidationObjectiveReferenceFilesMap, testDataTreeWrapper);
                    }
                }else{
                    addMessageTypeToMap(messageTypeValidationObjectiveReferenceFilesMap, testDataTreeWrapper);
                }
            }
        }
        return messageTypeValidationObjectiveReferenceFilesMap;
    }

    private void addReferenceFileNameToListInValidationObjectiveMap(Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap, TestDataTreeWrapper testDataTreeWrapper) {
        messageTypeValidationObjectiveReferenceFilesMap.get(testDataTreeWrapper.getMessageType()).get(testDataTreeWrapper.getValidationObjective()).add(testDataTreeWrapper.getReferenceFileName());
    }

    private void addValidationObjectiveToMap(Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap, TestDataTreeWrapper testDataTreeWrapper) {
        messageTypeValidationObjectiveReferenceFilesMap.get(testDataTreeWrapper.getMessageType()).put(testDataTreeWrapper.getValidationObjective(), new ArrayList<String>());
    }

    private boolean isValidationObjectiveInMap(Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap, TestDataTreeWrapper testDataTreeWrapper) {
        return messageTypeValidationObjectiveReferenceFilesMap.get(testDataTreeWrapper.getMessageType()).containsKey(testDataTreeWrapper.getValidationObjective());
    }

    private void addMessageTypeToMap(Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap, TestDataTreeWrapper testDataTreeWrapper) {
        messageTypeValidationObjectiveReferenceFilesMap.put(testDataTreeWrapper.getMessageType(), new HashMap<String, List<String>>());
    }

    private boolean isMessageTypeInMap(Map<String, Map<String, List<String>>> messageTypeValidationObjectiveReferenceFilesMap, TestDataTreeWrapper testDataTreeWrapper) {
        return messageTypeValidationObjectiveReferenceFilesMap.containsKey(testDataTreeWrapper.getMessageType());
    }
}
