'use strict';

angular.module('referenceValidator').controller('ValidationController', ['$scope', '$http', 'Upload', 'ValidatorService', function($scope, $http, Upload, ValidatorService) {
    var senderGitHubUrl = 'https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/contents/Sender SUT Test Data';
    var receiverGitHubUrl = 'https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/contents/Receiver SUT Test Data';
    var self = this;
    self.senderRecieverValidationObjectives = [];
    self.validationModel = {objective:'', referenceFileName:'', file:''};
    self.toggleMessageType = toggleMessageType;
    self.getReferenceFiles = getReferenceFiles;
    self.validate = validate;
    $scope.radioModel = 'sender';

    function validate(){
        if ($scope.validationModel.file) {
            uploadFile($scope.validationModel.file);
        }
        //ValidatorService.validate($scope.validationModel);
    }

    function uploadFile(file) {
        Upload.upload({
            url: '/referenceccdaservice',
            method: 'POST',
            data: {'ccdaFile': file, 'validationObjective': $scope.validationModel.objective, 'referenceFileName': $scope.validationModel.referenceFileName}
        }).then(function (resp) {
            console.log('Success ' + 'uploaded. Response: ' + resp.data);
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ');
        });
    };

    function toggleMessageType(){
        if($scope.radioModel == 'sender'){
            getTestDocuments(senderGitHubUrl);
        }else{
            getTestDocuments(receiverGitHubUrl);
        }
    };

    function getTestDocuments(endpointToDocuments){
        $("#CCDAR2_refdocsfordocumenttype option").remove();
        $("#validation_objectives option").remove().append(
            $http.get(endpointToDocuments).then(function(data){
                angular.forEach(data.data, function(item){
                    $("#validation_objectives").append(
                        $("<option></option>")
                            .text(item.name)
                            .val(item.url));
                });
            }));
        $("#validation_objectives").prepend(
            $("<option></option>")
                .text('-- select one ---')
                .val(''));
    };

    function getReferenceFiles(){
        if($scope.validationModel.objective != ''){
            $.getJSON($scope.validationModel.objective + '&callback=?', function(data){
                $("#CCDAR2_refdocsfordocumenttype option").remove();
                $("#CCDAR2_refdocsfordocumenttype").append(
                    $("<option></option>")
                        .text('-- select one ---')
                        .val(''));
                $.each(data.data, function(index, item){
                    var optionText = item.name;
                    var documentDownloadUrl = item.html_url;
                    $("#CCDAR2_refdocsfordocumenttype").append(
                        $("<option></option>")
                            .text(optionText)
                            .val(optionText));
                });
                $("#CCDAR2_refdocsfordocumenttype").append(
                    $("<option></option>")
                        .text('No Scenario File')
                        .val('noscenariofile'));
            });
        }else{
            $("#CCDAR2_refdocsfordocumenttype option").remove();
        }
    };
}]);