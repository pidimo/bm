<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

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

<!--
/*** Contacts Search city JavaScript functions **/
var _countryId = new Object();
var _zip = new Object();
var _cityId = new Object();
var _city = new Object();
var _beforeZip = new Object();

function openCitySearch(countryIdField, cityIdField, zipField, cityField, beforeZip, message)
{   _countryId = document.getElementById(countryIdField);
    _cityId = document.getElementById(cityIdField);
    _zip = document.getElementById(zipField);
    _city = document.getElementById(cityField);
    _beforeZip = document.getElementById(beforeZip);
     if(message == 'empty')
     {
        if(_countryId.value == '')
            searchWindow=window.open(${jsUrl1}, 'searchCity', 'resizable=yes,width=630,height=480,status,left=170,top=150,scrollbars=1');
        else
            searchWindow=window.open(${jsUrl2}, 'searchCity', 'resizable=yes,width=630,height=480,left=170,top=150,status,scrollbars=1');
     }
     else
     {
    if(_countryId.value == '' && _zip.value == '' && _city.value == '')
        searchWindow=window.open(${jsUrl1}, 'searchCity', 'resizable=yes,width=630,height=480,status,left=170,top=150,scrollbars=1');
    else if(_zip.value != '' && _city.value != '')
        searchWindow=window.open(${jsUrl3}, 'searchCity', 'resizable=yes,width=630,height=480,left=170,top=150,status,scrollbars=1');
    else if(_zip.value != '' && _city.value == '')
        searchWindow=window.open(${jsUrl3}, 'searchCity', 'resizable=yes,width=630,height=480,left=170,top=150,status,scrollbars=1');
    else if(_zip.value == '' && _city.value != '')
        searchWindow=window.open(${jsUrl4}, 'searchCity', 'resizable=yes,width=630,height=480,left=170,top=150,status,scrollbars=1');
    else
        searchWindow=window.open(${jsUrl2}, 'searchCity', 'resizable=yes,width=630,height=480,left=170,top=150,status,scrollbars=1');
     }
    searchDocument = searchWindow.document;
    searchWindow.focus();
}

function selectCity(countryId, cityId, zip, cityName) {
    _countryId.value = countryId;
    _cityId.value = cityId;
    _zip.value = zip;
    _beforeZip.value = zip;
    _city.value = unescape(cityName);
    searchWindow.close();
    //document.getElementById("saveButtonId").click();
}
//-->