/**
 * Created by Brian on 8/29/2016.
 */
'use strict';
angular.module('referenceValidator').factory('ValidatorService', ['$http', '$q', function($http, $q){

    var factory = {
    };
    return factory;

    function senderRecieverValidationObjectives(){
        return $http.get('senderreceivervalidationobjectivesandreferencefiles').data;
    }
}]);