<%@ page import="com.piramide.elwis.web.catalogmanager.el.Functions" %>
<%@ page import="net.java.dev.strutsejb.web.DefaultForm" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>

<style type="text/css">
    TD.message {
        font-family: Verdana, Helvetica, sans-serif;
        font-size: 11px;
        vertical-align: middle;
        padding: 3px 4px 3px 4px;
        height: 25px;
        text-align: left;
        border: #4646A3 1px solid;
        background-color: #FFFFCC;
        color: #000000;
    }

    TD.show{
        display:table-cell;
    }
</style>

<%
    String formName = (String) request.getAttribute("formName");
    DefaultForm defaultForm = (DefaultForm) request.getAttribute(formName);
    Map defaulFormMap = defaultForm.getDtoMap();
    String params = Functions.buildAJAXRequest(defaulFormMap, request);
    request.setAttribute("params", params);
%>
<app2:jScriptUrl
        url="${ajaxAction}?formName=${formName}&operation=${operation}&downloadAction=${app2:encodeDownloadAction(downloadAction)}&labelWidth=${labelWidth}&containWidth=${containWidth}&generalWidth=${generalWidth}"
        var="jsReadSubCategoriesUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(categoryValueId)" value="obj.options[obj.selectedIndex].value"/>
    <app2:jScriptUrlParam param="elementCounter" value="obj.tabIndex"/>
    <app2:jScriptUrlParam param="dto(categoryId)" value="childrenCategoryId"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="${ajaxAction}?formName=${formName}&operation=${operation}&downloadAction=${app2:encodeDownloadAction(downloadAction)}&labelWidth=${labelWidth}&containWidth=${containWidth}&generalWidth=${generalWidth}"
        var="jsReadSelectedSubCategoriesUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(categoryValueId)" value="categoryValueId"/>
    <app2:jScriptUrlParam param="dto(categoryId)" value="childrenCategoryId"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
function selectedOption_II(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);
    var i = 0;
    while (i < sourceBox.options.length) {
        var opt = sourceBox.options[i];
        if (opt.selected) {
            var nOpt = new Option();
            nOpt.text = opt.text;
            nOpt.value = opt.value;
            targetBox.options[targetBox.options.length] = nOpt;
            sourceBox.remove(i);
        } else {
            i = i + 1;
        }
    }
}

function makeHttpRequestPOST(url, callback_function, categoryId, return_xml, errorprocess_function)
{
    var http_request = false;

    if (window.XMLHttpRequest) { // Mozilla, Safari,...
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    } else if (window.ActiveXObject) { // IE
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
            }
        }
    }

    if (!http_request) {
        alert('Unfortunatelly you browser doesn\'t support this feature.');
        return false;
    }
    http_request.onreadystatechange = function() {
        if (http_request.readyState == 4) {
            if (http_request.status == 200) {
                if (return_xml) {
                    eval(callback_function + '(http_request.responseXML)');
                } else {
                    eval(callback_function + '(http_request.responseText, categoryId)');
                }
            } else {
                if (errorprocess_function != null && errorprocess_function != undefined) {
                    eval(errorprocess_function + '(http_request.status, categoryId)');
                } else {
                    alert('There was a problem with the request.(Code: ' + http_request.status + ')');
                }
            }
        }
    }
      parameters = ${params};
      http_request.open('POST', url, true);
      http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      http_request.setRequestHeader("Content-length", parameters.length);
      http_request.setRequestHeader("Connection", "close");
      http_request.setRequestHeader("isAjaxRequest", "true");
      http_request.send(parameters);
}

function makeHttpRequest(url, callback_function, categoryId, return_xml, errorprocess_function)
{
    var http_request = false;

    if (window.XMLHttpRequest) { // Mozilla, Safari,...
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    } else if (window.ActiveXObject) { // IE
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
            }
        }
    }

    if (!http_request) {
        alert('Unfortunatelly you browser doesn\'t support this feature.');
        return false;
    }
    http_request.onreadystatechange = function() {
        if (http_request.readyState == 4) {
            if (http_request.status == 200) {
                if (return_xml) {
                    eval(callback_function + '(http_request.responseXML)');
                } else {
                    eval(callback_function + '(http_request.responseText, categoryId)');
                }
            } else {
                if (errorprocess_function != null && errorprocess_function != undefined) {
                    eval(errorprocess_function + '(http_request.status, categoryId)');
                } else {
                    alert('There was a problem with the request.(Code: ' + http_request.status + ')');
                }
            }
        }
    }
    http_request.open('GET', url, true);
    http_request.setRequestHeader("isAjaxRequest", "true");
    http_request.send(null);
}

function loadSubCategories(obj, categoryId) {
    var categoryValueId = obj.options[obj.selectedIndex].value;

    var oldCategoryValueId = document.getElementById('oldValue_' + categoryId).value;

    if ('' != oldCategoryValueId) {
        var oldRelationIds = document.getElementById('categoryRelations_' + categoryId + '_' + oldCategoryValueId).value;
        var oldChildrenCategories = oldRelationIds.split(',');
        for (i = 0; i < oldChildrenCategories.length; i++) {
            element = oldChildrenCategories[i];
            elementArray = element.split(':');
            var oldChildrenCategoryId = elementArray[0];
            var oldChildrenGroupId = '';
            if (elementArray.length == 2) {
                oldChildrenGroupId = elementArray[1];
            }
            var divToHidde = document.getElementById(categoryId + '_' + oldChildrenCategoryId);
            if (undefined != divToHidde) {
                document.getElementById('tr_' + categoryId + '_' + oldChildrenCategoryId).style.display = 'none';
                divToHidde.innerHTML = '';
                divToHidde.style.display = 'none';
                deleteCategoryFromGroupCounter(oldChildrenGroupId, oldChildrenCategoryId);
            }
        }
    }

    if (undefined != document.getElementById('oldValue_' + categoryId)) {
        document.getElementById('oldValue_' + categoryId).value = categoryValueId;
    }
    if ('' != categoryValueId) {
        var relationIds = document.getElementById('categoryRelations_' + categoryId + '_' + categoryValueId).value;
        var childrenCategories = relationIds.split(',');
        for (i = 0; i < childrenCategories.length; i++) {
            element = childrenCategories[i];
            elementArray = element.split(':');
            var childrenCategoryId = elementArray[0];
            var childrenGroupId = '';
            if (elementArray.length == 2) {
                childrenGroupId = elementArray[1];
            }
            if ('' != childrenGroupId) {
                addCategoryToGroupCounter(childrenGroupId, childrenCategoryId);
            }

            if (undefined != document.getElementById(categoryId + '_' + childrenCategoryId) &&
                document.getElementById(categoryId + '_' + childrenCategoryId).tagName.toLocaleLowerCase() == 'div') {
                document.getElementById(categoryId + '_' + childrenCategoryId).style.display = 'inline';
                document.getElementById('tr_' + categoryId + '_' + childrenCategoryId).style.display = '';
                setLoadMessage(categoryId + '_' + childrenCategoryId);
                makeHttpRequest(${jsReadSubCategoriesUrl},
                        'setSubCategories',
                        categoryId + '_' + childrenCategoryId, false, 'ajaxErrorsOnLoadSubCategories');
            }
        }
    }
}

function addCategoryToGroupCounter(groupId, categoryId) {
    var groupCounter = document.getElementById('groupCounter_' + groupId);
    if (undefined != groupCounter) {
        var actualValue = groupCounter.value;
        actualValue = actualValue + ',' + categoryId;
        if (undefined != document.getElementById('groupCounter_' + groupId)) {
            document.getElementById('groupCounter_' + groupId).value = actualValue;
        }
        var groupLabelDiv = document.getElementById('groupId_' + groupId);
        if (undefined != groupLabelDiv) {
            groupLabelDiv.style.display = 'inline';
        }
    }
}

function deleteCategoryFromGroupCounter(groupId, categoryId) {
    var groupCounter = document.getElementById('groupCounter_' + groupId);
    if (undefined != groupCounter) {
        var actualValue = groupCounter.value;
        if (actualValue.indexOf(',' + categoryId) != -1) {
            actualValue = actualValue.replace(',' + categoryId, '');
        }
        if (undefined != document.getElementById('groupCounter_' + groupId)) {
            document.getElementById('groupCounter_' + groupId).value = actualValue;
        }
        if (actualValue.length == 0) {
            var groupLabelDiv = document.getElementById('groupId_' + groupId);
            if (undefined != groupLabelDiv) {
                groupLabelDiv.style.display = 'none';
            }
        }
    }
}

function loadSelectedSubCategories(categoryValueId, childrenCategoryId, categoryId) {
    var groupHiddenElement = document.getElementById('childrenGroupId_' + childrenCategoryId);
    if (undefined != groupHiddenElement) {
        var groupId = groupHiddenElement.value;
        addCategoryToGroupCounter(groupId, childrenCategoryId);
    }
    if (undefined != document.getElementById(categoryId + '_' + childrenCategoryId) &&
        document.getElementById(categoryId + '_' + childrenCategoryId).tagName.toLocaleLowerCase() == 'div') {
        document.getElementById(categoryId + '_' + childrenCategoryId).style.display = 'inline';
        document.getElementById('tr_' + categoryId + '_' + childrenCategoryId).style.display = '';
        setLoadMessage(categoryId + '_' + childrenCategoryId);
        makeHttpRequestPOST(${jsReadSelectedSubCategoriesUrl}, 'setSubCategories', categoryId + '_' + childrenCategoryId, false, 'ajaxErrorsOnLoadSubCategories');
    }
}

function ajaxErrorsOnLoadSubCategories(status, categoryId) {
    if (status == 404) {
        document.getElementById(categoryId).innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
    } else {
        document.getElementById(categoryId).innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
    }
}

function setSubCategories(htmlText, categoryId) {
    document.getElementById(categoryId).innerHTML = htmlText
}

function setLoadMessage(categoryId) {
    document.getElementById(categoryId).innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
}

function fillMultipleSelectValues() {
    var selectElements = document.getElementsByTagName('select');

    for (i = 0; i <= selectElements.length; i++) {
        var select = selectElements[i];
        if(null != select){
        var selectId = select.id;
            if (selectId.indexOf('selected_') == 0) {
                fillHiddenSelect(selectId);
            }
        }
    }
}
function fillHiddenSelect(styleId) {
    var box = document.getElementById(styleId);
    var ids = '';
    for (j = 0; j < box.options.length; j++) {
        ids = ids + box.options[j].value + ',';
    }
    document.getElementById('hidden_' + styleId).value = ids;
}

function hideAttachDownloadDiv(categoryId) {
    document.getElementById('downloadLinkId_' + categoryId).style.visibility = "hidden";
    document.getElementById('attachId_' + categoryId).value = "";
}
</script>