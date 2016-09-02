/**
 * Created by Brian on 8/29/2016.
 */
'use strict';
angular.module('referenceValidator').factory('ValidatorService', ['$http', '$q', function($http, $q){

    var factory = {
        validate: validate,
        senderRecieverValidationObjectives: senderRecieverValidationObjectives
    };
    return factory;

    function validate(validationModel) {
       alert("Im gonna validate! " + validationModel.objective + ' and ' + validationModel.referenceFileName);
    }

    function senderRecieverValidationObjectives(){
        return $http.get('senderreceivervalidationobjectivesandreferencefiles').data;
    }
}]);