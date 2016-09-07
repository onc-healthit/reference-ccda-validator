'use strict';

angular.module('referenceValidator').controller('ValidationController', function($scope, $http, Upload, ValidatorService, blockUI) {
    var senderGitHubUrl = 'https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/contents/Sender SUT Test Data';
    var receiverGitHubUrl = 'https://api.github.com/repos/siteadmin/2015-Certification-C-CDA-Test-Data/contents/Receiver SUT Test Data';
    var validationError;
    var self = this;
    self.senderRecieverValidationObjectives = [];
    self.validationModel = {objective:'', referenceFileName:'', file:''};
    self.toggleMessageType = toggleMessageType;
    self.getReferenceFiles = getReferenceFiles;
    self.validate = validate;
    $scope.radioModel = 'sender';

    function validate(){
        if ($scope.validationModel.file) {
            blockUI.start('VALIDATING...');
            uploadFile($scope.validationModel.file);
            blockUI.stop();
        }
    }

    function uploadFile(file) {
        Upload.upload({
            url: '/referenceccdaservice/',
            fields: {'validationObjective': $scope.validationModel.objective, 'referenceFileName': $scope.validationModel.referenceFileName, 'ccdaFile': file}
        }).then(function (resp) {
            console.log('Success ' + 'uploaded. Response: ' + resp.data);
            showValidationResults(resp.data)
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        });
    }

    function showValidationResults(data){
        showValidationResultsModalButtons();
        var tabHtml1 = '';
        if(data.resultsMetaData.serviceError){
            tabHtml1 = buildValidationResultErrorHtml(data.resultsMetaData.serviceErrorMessage);
        }else{
            var resultsMap = buildCcdaResultMap(data);
            tabHtml1 = buildCcdaValidationResultsHtml(data);
            buildCCDAXMLResultsTab(data);
            highlightCcdaXMLResults(resultsMap);
        }
        showResults(tabHtml1);
        removeProgressModal();
    }

    function showResults(resultsHtml){
        $("#ValidationResult .tab-content #tabs-1").html(resultsHtml);
        $("#resultModal").modal("show");
        $("#resultModalTabs a[href='#tabs-1']").tab("show");
        //$("#resultModalTabs a[href='#tabs-2']").tab("show");
        $("#resultModalTabs a[href='#tabs-3']").hide();
        if(Boolean(validationError)){
            $(".smartCCDAValidationBtn").hide();
            $(".saveResultsBtn").hide();
        }
    }

    function removeProgressModal(){
        if(typeof window.validationpanel != 'undefined')
            window.validationpanel.unblock();

        window.setTimeout(function() {
            $('#progress').fadeOut(400, function() {
                $('#progress .progress-bar').css('width', '0%');

            });
        }, 1000);
    }

    function showValidationResultsModalButtons() {
        $("#resultModalTabs a[href='#tabs-1']").show();
        $("#resultModalTabs a[href='#tabs-2']").show();
        $('.saveResultsBtn').show();
        $('.smartCCDAValidationBtn').show();
    }

    function buildValidationResultErrorHtml(errorMessage){
        var errorHtml = ['<title>Validation Results</title>',
            '<h1 align="center">Validation Results</h1>',
            '<p>An error occurred during validation with the following details:</br>',
            '<b>' + errorMessage + '</b></br>',
            'If possible, please fix the error and try again. If this error persists, please contact the system administrator',
            '</p>'];
        hideValidationResultsModalButtons();
        return errorHtml;
    }

    function hideValidationResultsModalButtons() {
        $("#resultModalTabs a[href='#tabs-1']").hide();
        $("#resultModalTabs a[href='#tabs-2']").hide();
        $('.saveResultsBtn').hide();
        $('.smartCCDAValidationBtn').hide();
    }

    function buildCcdaResultMap(data){
        var ccdaValidationResultsMap = {};
        var resultTypeMapValue = '';
        $.each(data.ccdaValidationResults, function(ccdaValidationResults,result){
            if(result.expectedValueSet != null){
                resultTypeMapValue = result.description + '<br/>Expected Valueset(s): ' + result.expectedValueSet.replace(/,/g , " or ");
            }else{
                resultTypeMapValue = result.description;
            }
            if(ccdaValidationResultsMap[result.documentLineNumber] != undefined){
                var resultTypeMap = ccdaValidationResultsMap[result.documentLineNumber];
                if(resultTypeMap[result.type] != undefined){
                    resultTypeMap[result.type].push(resultTypeMapValue);
                    ccdaValidationResultsMap[result.documentLineNumber] = resultTypeMap;
                }else{
                    resultTypeMap[result.type] = [resultTypeMapValue];
                    ccdaValidationResultsMap[result.documentLineNumber] = resultTypeMap;
                }
            }else{
                var ccdaTypeMap = {};
                ccdaTypeMap[result.type] = [resultTypeMapValue];
                ccdaValidationResultsMap[result.documentLineNumber] = ccdaTypeMap;
            }
        });
        return ccdaValidationResultsMap;
    }

    function buildCcdaValidationResultsHtml(data){
        var tabHtml1 = buildCcdaValidationSummary(data);
        tabHtml1 += buildCcdaValidationResults(data);
        return tabHtml1;
    }

    function buildCcdaValidationSummary(data){
        var uploadedFileName = data.resultsMetaData.ccdaFileName;
        var docTypeSelected = data.resultsMetaData.ccdaDocumentType;
        var numberOfTypesOfErrorsPerGroup = 3; //error, warning, info
        var resultTypeCount = 0;
        var resultGroup = '';
        var resultCountBadgeColorClass = '';
        var resultGroupHTMLHeader = '<div class="panel panel-primary col-md-2 col-lg-2"><div><div class="list-group">';
        var resultGroupHTMLEnd = '</div></div></div>';
        /* if(documentTypeIsNonSpecific(docTypeSelected)){
         docTypeSelected = buildValidationHeaderForNonSpecificDocumentType(docTypeSelected);
         } */

        var tabHtml1 = buildValidationResultsHeader(uploadedFileName, docTypeSelected).join('\n');
        tabHtml1 += '<br/><div class="row">';

        $.each(data.resultsMetaData.resultMetaData, function(resultMetaData, metaData){
            if(metaData.type.toLowerCase().indexOf("error") >= 0){
                resultCountBadgeColorClass = 'label-danger';
            }else if(metaData.type.toLowerCase().indexOf("warn") >= 0){
                resultCountBadgeColorClass = ' label-warning';
            }else{
                resultCountBadgeColorClass = 'label-info';
            }
            resultGroup += '<div class="list-group-item"><span class="label label-as-badge ' + resultCountBadgeColorClass + '">'+metaData.count+'</span><a href="#'+metaData.type+'">' + metaData.type + '</a></div>';
            resultTypeCount++;
            if(resultTypeCount % numberOfTypesOfErrorsPerGroup === 0){
                tabHtml1 += '<div id="'+metaData.type.split(' ').join('_') +'_SUMMARY">' + resultGroupHTMLHeader + resultGroup + resultGroupHTMLEnd + '</div>';
                resultGroup = "";
            }
        });
        tabHtml1 += '</div>';
        return tabHtml1;
    }

    function buildCcdaValidationResults(data){
        var resultList = [];
        var currentResultType;
        $.each(data.ccdaValidationResults, function(ccdaValidationResults,result){
            if(result.type.toLowerCase().indexOf("error") >= 0){
                resultList.push('<font color="red">');
            }else if(result.type.toLowerCase().indexOf("warn") >= 0){
                resultList.push('<font color="orange">');
            }else{
                resultList.push('<font color="#5bc0de">');
            }

            if(currentResultType != result.type.toLowerCase()){
                resultList.push('<a href="#" name="'+ result.type + '"></a>');
            }

            var errorDescription = ['<li>' + result.type + '<ul class="">',
                '<li class="">Description: '+ result.description + '</li>',
                '<li class="">xPath: '+ result.xPath + '</li>',
                '<li class="">Document Line Number (approximate): '+ result.documentLineNumber + '</li>',
                '</ul></li>'];
            resultList = resultList.concat(errorDescription);
            if(result.expectedValueSet != null){
                var expectedValueSets = ['<a href=">' + result.expectedValueSet + '</a>'];
                resultList = resultList.concat(expectedValueSets);
            }
            resultList.push('</font>');
            resultList.push('<hr/><div class="pull-right"><a href="#validationResults" title="top">^</a></div>');
            currentResultType = result.type.toLowerCase();
        });
        return (resultList.join('\n'));
    }

    function documentTypeIsNonSpecific(documentType){
        return(documentType.lastIndexOf('Non-specific') !== -1);
    }

    function buildValidationHeaderForNonSpecificDocumentType(docTypeSelected){
        docTypeSelected = docTypeSelected.replace("Non-specific", "");
        if(docTypeSelected.lastIndexOf('R2') === -1){
            docTypeSelected = docTypeSelected.trim();
            docTypeSelected = docTypeSelected.slice(5);
            docTypeSelected = 'CCDA R1.1 ' + docTypeSelected;
        }
        return docTypeSelected;
    }

    function buildValidationResultsHeader(uploadedFileName){
        var header =  ['<title>Validation Results</title>',
            '<a name="validationResults"/>',
            '<div class="row">',
            '<b>Upload Results:</b> '+uploadedFileName+' was uploaded successfully.'];
        header.push(['</div>']);
        return header;
    }

    function createResultListPopoverHtml(results){
        var htmlList = '<ul>';
        for(var i = 0; i < results.length; i++){
            htmlList += '<li>' + results[i] + '</li>'
        }
        htmlList += '</ul>';
        return htmlList;
    }

    function buildCCDAXMLResultsTab(data){
        jQuery("#ccdaXML").remove();
        jQuery("#tabs-2").empty();
        var uploadedFileName = data.resultsMetaData.ccdaFileName;
        //var docTypeSelected = getSelectedDocumentType(data);
        var resultsHeader = buildValidationResultsHeader(uploadedFileName);
        if(data.resultsMetaData.ccdaFileContents.match(/\n/gm) == null || data.resultsMetaData.ccdaFileContents.match(/\n/gm).length < 10){
            jQuery('#tabs-2').html(resultsHeader.join(" ") + "<div class='row alert alert-warning'><b>WARNING!</b> Detected an XML document that may not be formatted for this validation display. For example, the XML may be on a single line.</div>");
        }else{
            //$('#tabs-2').html(resultsHeader.join(" "));
            jQuery('<pre/>',{
                id:'ccdaXML',
                class:'brush: xml; toolbar: false',
                text:data.resultsMetaData.ccdaFileContents
            }).appendTo('#tabs-2');
            SyntaxHighlighter.defaults['auto-links'] = false;
            SyntaxHighlighter.highlight();
        }
    }

    /* function getSelectedDocumentType(data){
     var docTypeSelected = '';
     if(data.result.body.ccdaResults != undefined){
     docTypeSelected =  data.result.body.ccdaResults.report.docTypeSelected;
     }else{
     docTypeSelected =  data.result.body.resultsMetaData.ccdaDocumentType
     }
     return docTypeSelected;
     }
     */
    function highlightCcdaXMLResults(resultsMap){
        if($.map(resultsMap, function(n, i) { return i; }).length > 0){
            for (var resultLineNumber in resultsMap){
                var lineNum = resultLineNumber;
                var resultTypesMap = resultsMap[resultLineNumber];
                for (var resultType in resultTypesMap){
                    var type = resultType;
                    var descriptions = resultTypesMap[resultType];
                    var descriptionsLength = descriptions.length;
                    var popoverTemplate = '<span class="popover resultpopover"><div class="clearfix"><span>Line Number: '+lineNum+'</span><span aria-hidden="true" class="glyphicon glyphicon-arrow-up" style="float:right !important; cursor:pointer" title="go to previous result"></span></div><span class="arrow"></span><h3 class="popover-title result-title"></h3><div class="popover-content"></div><div class="clearfix"><span aria-hidden="true" class="glyphicon glyphicon-arrow-down" style="float:right !important; cursor:pointer" title="go to next result"></span></div></span>';
                    var popOverContent = createResultListPopoverHtml(descriptions);
                    if (typeof jQuery(".code .container .line.number" + lineNum).data('bs.popover') !== "undefined") {
                        var title;
                        if(type.toLowerCase().indexOf("error") >= 0){
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-exclamation-sign alert-danger' aria-hidden='true' style='font-size: .8em;'></span>" );
                            title = "<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)";
                        }else if(type.toLowerCase().indexOf("warn") >= 0){
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-warning-sign alert-warning' aria-hidden='true' style='font-size: .8em;'></span>" );
                            title = "<span class='glyphicon glyphicon-warning-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)";
                        }else{
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-info-sign alert-info' aria-hidden='true' style='font-size: .8em;'></span>" );
                            title = "<span class='glyphicon glyphicon-info-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)";
                        }
                        jQuery(".code .container .line.number" + lineNum).data('bs.popover').options.content += "<h3 class='popover-title result-title'>" + title + "</h3>" + popOverContent;
                    }else{
                        if(type.toLowerCase().indexOf("error") >= 0){
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-exclamation-sign alert-danger' aria-hidden='true' style='font-size: .8em;'></span>" );
                            jQuery(".code .container .line.number" + lineNum).addClass("ccdaErrorHighlight").popover(
                                {
                                    title: "<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)",
                                    html: true,
                                    content: popOverContent,
                                    trigger: "focus",
                                    placement: "auto",
                                    template:popoverTemplate
                                });
                        }else if(type.toLowerCase().indexOf("warn") >= 0){
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-warning-sign alert-warning' aria-hidden='true' style='font-size: .8em;'></span>" );
                            jQuery(".code .container .line.number" + lineNum).addClass("ccdaWarningHighlight").popover(
                                {
                                    title: "<span class='glyphicon glyphicon-warning-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)",
                                    html: true,
                                    content: popOverContent,
                                    trigger: "focus",
                                    placement: "auto",
                                    template: popoverTemplate
                                });
                        }else{
                            jQuery(".code .container .line.number" + lineNum).prepend( "<span class='glyphicon glyphicon-info-sign alert-info' aria-hidden='true' style='font-size: .8em;'></span>" );
                            jQuery(".code .container .line.number" + lineNum).addClass("ccdaInfoHighlight").popover(
                                {
                                    title: "<span class='glyphicon glyphicon-info-sign' aria-hidden='true'></span> " + descriptionsLength + " " + type + "(s)",
                                    html: true,
                                    content:  popOverContent,
                                    trigger: "focus",
                                    placement: "auto",
                                    template: popoverTemplate
                                });
                        }

                    }
                }

            }
        }

        addTitleAttributeToHighlightedDivs();
    }

    function addTitleAttributeToHighlightedDivs(){
        $("div[class$='Highlight']").attr('title', 'Show validation result details for this line.');
        $("div[class$='Highlight']").attr('tabindex', '0');
    }

    function toggleMessageType(){
        if($scope.radioModel == 'sender'){
            getTestDocuments(senderGitHubUrl);
        }else{
            getTestDocuments(receiverGitHubUrl);
        }
    }

    function getTestDocuments(endpointToDocuments){
        $("#CCDAR2_refdocsfordocumenttype").find("option").remove();
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
    }

    $('#resultModal').on('click', '.glyphicon-arrow-down', function(){
        var $elem = jQuery(this).parent().parent().nextAll('.ccdaErrorHighlight, .ccdaWarningHighlight, .ccdaInfoHighlight').first();
        $('#resultModal').animate({
            scrollTop: $elem.position().top
        }, 2000, function(){
            $elem.focus();
        });
    });

    $('#resultModal').on('click', '.glyphicon-arrow-up', function(){
        var $elem = jQuery(this).parent().parent().prevAll('.ccdaErrorHighlight, .ccdaWarningHighlight, .ccdaInfoHighlight')[1];
        $('#resultModal').animate({
            scrollTop: $($elem).position().top
        }, 2000, function(){
            $elem.focus();
        });
    });

    $('#resultModalTabs a').on('click', function(){
        var href = $(this).attr('href');
        if(href == '#tabs-2'){
            $('.saveResultsBtn').hide();
        }else{
            $('.saveResultsBtn').show();
        }
    });

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
    }
});