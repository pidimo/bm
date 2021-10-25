<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:initBootstrapSelectPopup />

<%@ attribute name="countryIdField" required="true" %>
<%@ attribute name="cityIdField" required="true"%>
<%@ attribute name="zipField" required="true"%>
<%@ attribute name="cityField" required="true"%>
<%@ attribute name="beforeZip" required="true"%>
<%@ attribute name="message" required="true"%>
<%@ attribute name="iframeId" required="true"%>
<%@ attribute name="submitOnSelect" required="true"%>
<%@ attribute name="tabIndex" %>
<%@ attribute name="modalTitleKey" %>
<%@ attribute name="titleKey" %>

<app2:jScriptUrl url="/contacts/SearchCity.do" var="jsUrl1" addModuleParams="false">
    <app2:jScriptUrlParam param="actionMessage" value="message"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/SearchCity.do" var="jsUrl2" addModuleParams="false">
    <app2:jScriptUrlParam param="actionMessage" value="message"/>
    <app2:jScriptUrlParam param="parameter(countryId)" value="_countryId.value"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/SearchCity.do" var="jsUrl3" addModuleParams="false">
    <app2:jScriptUrlParam param="actionMessage" value="message"/>
    <app2:jScriptUrlParam param="parameter(countryId)" value="_countryId.value"/>
    <app2:jScriptUrlParam param="parameter(zip)" value="_zip.value"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/SearchCity.do" var="jsUrl4" addModuleParams="false">
    <app2:jScriptUrlParam param="actionMessage" value="message"/>
    <app2:jScriptUrlParam param="parameter(countryId)" value="_countryId.value"/>
    <app2:jScriptUrlParam param="parameter(cityName)" value="_city.value"/>
</app2:jScriptUrl>
<app2:jScriptUrl url="/contacts/SearchCity.do" var="jsUrl5" addModuleParams="false">
    <app2:jScriptUrlParam param="actionMessage" value="message"/>
    <app2:jScriptUrlParam param="parameter(countryId)" value="_countryId.value"/>
    <app2:jScriptUrlParam param="parameter(zip)" value="_zip.value"/>
    <app2:jScriptUrlParam param="parameter(cityName)" value="_city.value"/>
</app2:jScriptUrl>

<%--Auto complete actions --%>
<app2:jScriptUrl url="/contacts/SearchCity/ByCountryZip/Ajax.do" var="jsSearchByCountryZipUrl">
    <app2:jScriptUrlParam param="countryId" value="countryIdVar"/>
    <app2:jScriptUrlParam param="cityZip" value="cityZipVar"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/contacts/SearchCity/ByCityName/Ajax.do" var="jsSearchByNameUrl">
    <app2:jScriptUrlParam param="countryId" value="countryIdVar"/>
    <app2:jScriptUrlParam param="cityZip" value="cityZipVar"/>
</app2:jScriptUrl>


<%--typeahead src--%>
<link href="<c:url value="/js/cacheable/bootstrap/typeahead/css/customTypeahead-1.0.0.css"/>" rel="stylesheet" />
<script src="<c:url value="/js/cacheable/bootstrap/typeahead/0.11.1/typeahead.bundle.min.js"/>" type="text/javascript"></script>


<script language="JavaScript">
    /*** Contacts Search city JavaScript functions **/
    var _countryId = new Object();
    var _zip = new Object();
    var _cityId = new Object();
    var _city = new Object();
    var _beforeZip = new Object();
    var _styleId = new Object();

    function openCitySearch(countryIdField, cityIdField, zipField, cityField, beforeZip, message,styleId,submitOnSelect)
    {   _countryId = document.getElementById(countryIdField);
        _cityId = document.getElementById(cityIdField);
        _zip = document.getElementById(zipField);
        _city = document.getElementById(cityField);
        _beforeZip = document.getElementById(beforeZip);
        _styleId = styleId;
        if(message == 'empty')
        {
            if(_countryId.value == '')
                launchBootstrapPopup(styleId,${jsUrl1},styleId,submitOnSelect);
            else
                launchBootstrapPopup(styleId,${jsUrl2},styleId,submitOnSelect);
        }
        else
        {
            if(_countryId.value == '' && _zip.value == '' && _city.value == '')
                launchBootstrapPopup(styleId,${jsUrl1},styleId,submitOnSelect);
            else if(_zip.value != '' && _city.value != '')
                launchBootstrapPopup(styleId,${jsUrl5},styleId,submitOnSelect);
            else if(_zip.value != '' && _city.value == '')
                launchBootstrapPopup(styleId,${jsUrl3},styleId,submitOnSelect);
            else if(_zip.value == '' && _city.value != '')
                launchBootstrapPopup(styleId,${jsUrl4},styleId,submitOnSelect);
            else
                launchBootstrapPopup(styleId,${jsUrl2},styleId,submitOnSelect);
        }
    }

    function selectCity(countryId, cityId, zip, cityName) {
        _countryId.value = countryId;
        _cityId.value = cityId;
        _zip.value = zip;
        _beforeZip.value = zip;
        _city.value = unescape(cityName);
        hideBootstrapPopup();

        //document.getElementById("saveButtonId").click();
    }


    //Autocomplete part
    function findCityByZip() {
        var countryIdVar = $('#' + '${countryIdField}').val();
        var cityZipVar = $('#' + '${zipField}').val();

        makeAjaxSearchCityByZip(${jsSearchByCountryZipUrl}, "");
    }

    function makeAjaxSearchCityByZip(urlAction, parameters) {
        $.ajax({
            async:true,
            type: "GET",
            dataType: "json",
            data:parameters,
            url:urlAction,
            success: function(dataJson) {
                processAjaxFindByZipResponse(dataJson);
            },
            error: function(ajaxRequest) {
                alert("error:" + ajaxRequest);
            }
        });
    }

    function processAjaxFindByZipResponse(responseJson) {
        if("SuccessCitySearch" == responseJson.ajaxForward) {
            $('#' + '${cityIdField}').val(responseJson.cityId);
            $('#' + '${cityField}').val(responseJson.cityName);
        }
    }

    /*
    * Zip keyUp event
    */
    $(document).ready(function() {
        $('#' + '${zipField}').keyup(function() {
            findCityByZip();
        });

    });


    /*
    * Typeahead plugin to autocomplete by char query
    */
    $('#' + '${cityField}').typeahead({
        highlight: true,
        hint: true,
        minLength : 1
    } , {
        name: "ajaxDataSet",
        source: function (query, syncResults, asyncResults) {

            var countryIdVar = $('#' + '${countryIdField}').val();
            var cityZipVar = $('#' + '${zipField}').val();

            $.ajax({
                url: ${jsSearchByNameUrl},
                type: "GET",
                data: "cityNameToken=" + query,
                dataType: "json",
                async: true,
                success: function (responseJson) {

                    var resultJsonArray = [];
                    if("Success" == responseJson.ajaxForward) {
                        resultJsonArray = responseJson.resultDataArray;
                    }
                    //load in plugin
                    asyncResults(resultJsonArray);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("Status: " + textStatus);
                    console.log("Error: " + errorThrown);
                }
            });
        },
        display: "cityName",
        async: true,
        limit: 10
    });

    //typeahead events
    $('#' + '${cityField}').bind('typeahead:select', function(ev, suggestion) {
        //console.log('Selection: ' + suggestion.cityName);
        //console.log('Selection: ' + suggestion.cityId);
        //console.log('Selection: ' + suggestion.cityZip);

        $('#' + '${cityIdField}').val(suggestion.cityId);
        $('#' + '${zipField}').val(suggestion.cityZip);

    });

    $('#' + '${cityField}').bind('typeahead:autocomplete', function(ev, suggestion) {
        $('#' + '${cityIdField}').val(suggestion.cityId);
        $('#' + '${zipField}').val(suggestion.cityZip);
    });

</script>

<a href='javascript:openCitySearch( "${countryIdField}", "${cityIdField}", "${zipField}", "${cityField}", "${beforeZip}","${message}","${iframeId}","${submitOnSelect}");'
   tabindex="${tabIndex}" class="${app2:getFormButtonClasses()}" title="<fmt:message   key="${titleKey}"/>">
    <span class="glyphicon glyphicon-search"></span>
</a>

<tags:bootstrapModalPopup styleId="${iframeId}" isLargeModal="true" modalTitleKey="${modalTitleKey}"/>

