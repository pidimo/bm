<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/calendar.tld" prefix="calendar" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%@ attribute name="dynamicSearchName" required="true" %>
<%@ attribute name="customSearchConfigUrl" required="false" %>

<%--set in request--%>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<c:set var="dynamicSearchName" value="${dynamicSearchName}" scope="request"/>
<c:set var="customSearchConfigUrl" value="${customSearchConfigUrl}" scope="request"/>

<c:if test="${not empty customSearchConfigUrl}">
    <c:set var="configUrlParam" value="&configUrlParam=${customSearchConfigUrl}"/>
</c:if>

<app2:jScriptUrl
        url="/contacts/DynamicSearch/Ajax/FillOperator.do?dynamicSearchName=${dynamicSearchName}&isFromAjaxRequest=true"
        var="jsFillOperatorUrl">
    <app2:jScriptUrlParam param="fieldProperty" value="fieldProperty"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="/contacts/DynamicSearch/Ajax/FillValue.do?dynamicSearchName=${dynamicSearchName}&isFromAjaxRequest=true${configUrlParam}"
        var="jsFillValueUrl">
    <app2:jScriptUrlParam param="fieldProperty" value="fieldProperty"/>
    <app2:jScriptUrlParam param="operatorProperty" value="operatorProperty"/>
</app2:jScriptUrl>

<c:set var="addImgVar">
    <span class="glyphicon glyphicon-plus clsAdd btn-link fa-lg"></span>
</c:set>

<c:set var="removeImgVar">
    <span class="glyphicon glyphicon-trash clsRemove btn-link"></span>
</c:set>


<script type="text/javascript">

    function makeAjaxSetResponse(urlAddress, parameters, element) {
        $.ajax({
            async: true,
            type: "POST",
            dataType: "html",
            data: parameters,
            url: urlAddress,
            success: function (data) {
                processResponseHtml(element, data);
            },
            error: function (ajaxRequest) {
                alert("error:" + ajaxRequest);
            }
        });
    }

    function makeAjaxForSearchProperty(urlAddress, parameters, element) {
        $.ajax({
            async: true,
            type: "POST",
            dataType: "html",
            data: parameters,
            url: urlAddress,
            success: function (data) {
                processResponseHtml(element, data);
                var operatorBox = findOperatorSelectElement(element);
                if (operatorBox != null) {
                    fillValueByOperator($(operatorBox));
                }
            },
            error: function (ajaxRequest) {
                alert("error:" + ajaxRequest);
            }
        });
    }


    $(function () {
        //search property onchange
        $(document).on("change", ".clsSearchProperty", function () {
            var fieldProperty = $(this).val();
            var trParent = findParentTr($(this));

            emptyTdElement(trParent, 1);
            emptyTdElement(trParent, 2);
            emptyTdElement(trParent, 3);

            if (fieldProperty != '') {
                var tdOperator = findTdOperator(trParent);
                makeAjaxForSearchProperty(${jsFillOperatorUrl}, "", tdOperator);
            }
        });

        $(document).on("change", ".clsOperator", function () {
            fillValueByOperator($(this));
        });

        $(document).on("click", ".clsAdd", function () {
            var trParent = findParentTr($(this));

            putRemoveImg(trParent);
            redefineSearchField(trParent);
            putRowBaseInTable();
        });

        $(document).on("click", ".clsRemove", function () {
            var trParent = findParentTr($(this));
            $(trParent).remove();

            updateLastTrRow();
        });
    });

    function findOperatorSelectElement(parentElement) {
        var operatorSelect = $(parentElement).find(".clsOperator");
        if ($(operatorSelect).length > 0) {
            return $(operatorSelect);
        } else {
            return null;
        }
    }

    function fillValueByOperator(operatorSelect) {
        var operatorProperty = $(operatorSelect).val();
        var trParent = findParentTr($(operatorSelect));

        var fieldProperty = getSearchFieldProperty(trParent);

        emptyTdElement(trParent, 2);
        //emptyTdElement(trParent, 3);

        if (operatorProperty != '') {
            var tdValue = findTdValue(trParent);
            makeAjaxSetResponse(${jsFillValueUrl}, "", tdValue);
        }

        //find for remove image, if this has been added
        var hasRemoveImg = $(trParent).find(".clsRemove").length;
        if (hasRemoveImg == 0) {
            putAddImg(trParent);
        }
    }

    function findParentTr(element) {
        var tdParent = $(element).parents().get(0);
        var trParent = $(tdParent).parents().get(0);
        return trParent;
    }

    function processResponseHtml(element, data) {
        var fixedHtml = fixHtmlResponseDeleteFormTag(data);
        elementInnerHtml(element, fixedHtml);
    }

    function fixHtmlResponseDeleteFormTag(htmlData) {
        var fixedHtml = htmlData;
        var mainTagName = $(htmlData).prop('tagName');

        if ('FORM' == mainTagName || 'form' == mainTagName) {
            fixedHtml = $(htmlData).html();
        } else {
            //find form tag in sibling
            var formList = $(htmlData).siblings('form');
            if (formList.length > 0) {
                fixedHtml = $(formList).eq(0).html();
            }
        }

        return fixedHtml;
    }

    function elementInnerHtml(elm, data) {
        //alert("inner data:" + data);
        $(elm).html(data);
    }

    function emptyTdElement(trElm, tdIndex) {
        $(trElm).children().eq(tdIndex).empty();
    }

    function findTdField(trElm) {
        return $(trElm).children().get(0);
    }
    function findTdOperator(trElm) {
        return $(trElm).children().get(1);
    }
    function findTdValue(trElm) {
        return $(trElm).children().get(2);
    }
    function findTdAdd(trElm) {
        return $(trElm).children().get(3);
    }

    function getSearchFieldProperty(trElm) {

        var tdElm = findTdField(trElm);

        //alert("td ELM:"+$(tdElm).prop('tagName'));
        //alert("td ELM:"+$(tdElm).html());

        return $(tdElm).children().eq(0).val();
    }

    function getSearchFieldText(trElm) {

        var tdElm = findTdField(trElm);

        //alert("select TEXT:"+$(tdElm).children().eq(0).find('option:selected').text());

        return $(tdElm).children().eq(0).find('option:selected').text();
    }

    function redefineSearchField(trElm) {
        var tdField = findTdField(trElm);
        var fieldText = getSearchFieldText(trElm);
        var fieldValue = getSearchFieldProperty(trElm);
        var hiddenSearchProperty = "<input name='searchProperties' type='hidden' value='" + fieldValue + "'/>";

        $(tdField).empty();
        $(tdField).append(hiddenSearchProperty);
        $(tdField).append("<label class='control-label text-right'>"+fieldText+"</label>");
    }

    function putAddImg(trElm) {
        var tdImg = findTdAdd(trElm);
        elementInnerHtml(tdImg, '${addImgVar}');
    }

    function putRemoveImg(trElm) {
        var tdImg = findTdAdd(trElm);
        elementInnerHtml(tdImg, '${removeImgVar}');
    }

    function putRowBaseInTable() {
        //$("#dynamicSearchTable tr:eq(0)").clone().removeAttr("style").appendTo("#dynamicSearchTable");

        var cloneTr = $("#dynamicSearchTable tr:eq(0)").clone().removeAttr("style");

        //remove already search property registered
        $(cloneTr).find(".clsSearchProperty option").each(function () {
            if (alreadyRegistered($(this).val())) {
                $(this).remove();
            }
        });

        $(cloneTr).appendTo("#dynamicSearchTable");
    }

    function updateLastTrRow() {
        var lastTr = $("#dynamicSearchTable tr:last");
        var propertyValue = $(lastTr).find(".clsSearchProperty").val();

        //update only if search property is empty
        if (propertyValue == "") {
            $(lastTr).remove();
            putRowBaseInTable();
        }
    }

    function alreadyRegistered(propertyName) {
        var exist = false;
        $("input[name|='searchProperties']").each(function () {
            if ($(this).val() == propertyName) {
                exist = true;
            }
        });
        return exist;
    }

    function clearDynamicSearch() {

        $("form:first input:text").each(function () {
            $(this).val("");
        });

        $("form:first select").each(function () {
            var attrName = $(this).attr('name');
            if (!(attrName.indexOf("operator") >= 0 || attrName.indexOf("searchProperties") >= 0)) {
                $(this).val("");
            }
        });
    }

</script>

<html:hidden property="parameter(dynamicSearchName)" value="${dynamicSearchName}"/>

<div class="table-responsive">
    <table id="dynamicSearchTable" width="100%" class="table-condensed">
        <tr style="display:none;">
            <td width="20%" style="min-width:150px" class="text-right">
                <c:import url="/WEB-INF/jsp/dynamicsearch/SearchFilterPropertiesSelect.jsp"/>
            </td>
            <td width="35%" style="min-width:150px"></td>
            <td width="35%" style="min-width:300px"></td>
            <td width="10%" style="min-width:50px"></td>
        </tr>

        <c:if test="${not empty searchFieldsUIList}">
            <c:forEach var="item" items="${searchFieldsUIList}" varStatus="index">
                <c:set var="fieldAlias" value="${item.fieldAlias}" scope="request"/>
                <c:set var="fieldOperator" value="${item.operator}" scope="request"/>
                <tr>
                    <td width="20%" class="text-right">
                        <html:hidden property="searchProperties" value="${fieldAlias}" styleId="hidden_${index.count}"/>
                        <label class="control-label">
                            <c:out value="${item.label}"/>
                        </label>
                    </td>
                    <td width="15%">
                        <c:import url="/WEB-INF/jsp/dynamicsearch/SearchFilterOperatorSelect.jsp"/>
                    </td>
                    <td width="25%">
                        <c:import url="/WEB-INF/jsp/dynamicsearch/SearchFilterValues.jsp"/>
                    </td>
                    <td width="35%">
                        <c:out value="${removeImgVar}" escapeXml="false"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

    </table>
</div>

<script type="text/javascript">
    //insert the first search property
    putRowBaseInTable();
</script>