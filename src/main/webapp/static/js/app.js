'use strict';
var app = angular.module('referenceValidator', ['ui.bootstrap', 'ngFileUpload', 'blockUI']).config(function(blockUIConfig){
    blockUIConfig.message = 'Validating ...';
});