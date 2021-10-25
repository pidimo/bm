<%@ page import="com.jatun.titus.reportgenerator.util.ReportGeneratorConstants" %>
<%@ page import="com.piramide.elwis.utils.ReportConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%
    request.setAttribute("KEY_SEPART", ReportConstants.KEY_SEPARATOR);
    request.setAttribute("KEY_LABEL", ReportConstants.KEY_SEPARATOR_LABEL);
    request.setAttribute("KEY_VALUE", ReportConstants.KEY_SEPARATOR_VALUE);
    request.setAttribute("KEY_OP", ReportConstants.KEY_SEPARATOR_OP);
    request.setAttribute("KEY_SHOWVIEW", ReportConstants.KEY_SEPARATOR_SHOWVIEW);
    request.setAttribute("KEY_NOT_OP", ReportConstants.KEY_NOT_OPERATOR);
    request.setAttribute("KEY_ISDATETYPE", ReportConstants.KEY_ISDATETYPE);
    request.setAttribute("KEY_ISNUMERICTYPE", ReportConstants.KEY_ISNUMERICTYPE);
    //show views
    request.setAttribute("FVALUE_SEPARATOR", ReportConstants.FILTERVALUE_SEPARATOR);
    request.setAttribute("ONEBOX", ReportConstants.SHOW_ONE_BOX);
    request.setAttribute("TWOBOX", ReportConstants.SHOW_TWO_BOX);
    request.setAttribute("ONESELECT", ReportConstants.SHOW_SELECT);
    request.setAttribute("MSELECT", ReportConstants.SHOW_MULTIPLESELECT);
    request.setAttribute("EMPTY", ReportConstants.SHOW_EMPTY);
    request.setAttribute("POPUP", ReportConstants.SHOW_POPUP);
%>

<c:set var="CSV_FORMAT" value="<%=ReportGeneratorConstants.REPORT_FORMAT_CSV%>"/>

<script language="JavaScript" type="text/javascript">
<!--
//popup import functions
function putPopupValue(idValue, value, filterId) {
    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    lib_getObj("dto(popupIdValue" + keyPostfix + ")").value = idValue;
    lib_getObj("dto(popupValue" + keyPostfix + ")").value = unescape(value);
    searchWindow.close();
}

//this method was have rewrite of tag select popup
function selectPopup(url, searchName, submitOnSelect, w, h, scroll) {
    autoSubmit = submitOnSelect;
    var winx = (screen.width) / 2;
    var winy = (screen.height) / 2;
    var posx = winx - w / 2;
    var posy = winy - h / 2;

    //to popup import data, the popup name is the filterId
    var keyPostfix = (searchName != null && searchName != undefined) ? searchName : "";
    //add parameters to url
    filterPath = lib_getObj("dto(path" + keyPostfix + ")").value;

    //decode
    filterPath = filterPath.replace(/&lt;/g, "<");
    filterPath = filterPath.replace(/&gt;/g, ">");

    url = url + '?filterPath=' + filterPath;

    searchWindow = window.open(url, searchName, 'resizable=yes,width=' + w + ',height=' + h + ',status,left=' + posx + ',top=' + posy + ',scrollbars=' + scroll);
    searchWindow.focus();
}

function clearSelectPopup(keyFieldId, nameFieldId, submitOnClear, clearName) {
    document.getElementById(keyFieldId).value = '';
    document.getElementById(nameFieldId).value = '';
}

//common filter functions
//get object by id or name
function lib_getObj(id, d) {
    var i,x;
    if (!d) d = document;
    if (!(x = d[id]) && d.all) x = d.all[id];
    for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][id];
    for (i = 0; !x && d.layers && i < d.layers.length; i++) x = lib_getObj(id, d.layers[i].document);
    if (!x && document.getElementById) x = document.getElementById(id);
    return x;
}

function getValueByKey(text, key) {
    var res = null;
    var firstPos = text.indexOf(key);
    var lastPos = text.lastIndexOf(key);
    if (firstPos != lastPos) {
        res = text.substring(firstPos + key.length, lastPos);
    }
    ///alert(res);
    return res;
}

function existThisKey(text, key) {
    var res = false;
    if (text.indexOf(key) != -1)
        res = true;
    return res;
}

function showView(operatorValue, fValue, filterId) {
    var keyIsDateType = '${KEY_ISDATETYPE}';
    var keyView = '${KEY_SHOWVIEW}';
    var showView = getValueByKey(operatorValue, keyView);

    if (existThisKey(operatorValue, '${KEY_ISNUMERICTYPE}')) {
        numericTypeAlign(filterId);
    } else {
        stringTypeAlign(filterId);
    }

    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    //view elements keys
    var oneBox_eKey = "oneBox" + keyPostfix;
    var oneBoxDate_eKey = "oneBoxDate" + keyPostfix;
    var twoBox_eKey = "twoBox" + keyPostfix;
    var twoBoxDate_eKey = "twoBoxDate" + keyPostfix;
    var oneSelect_eKey = "oneSelect" + keyPostfix;
    var mSelect_eKey = "mSelect" + keyPostfix;
    var popup_eKey = "popup" + keyPostfix;

    //dto attribute keys
    var date_dtoKey = "dto(date" + keyPostfix + ")";
    var value_dtoKey = "dto(value" + keyPostfix + ")";
    var date1_dtoKey = "dto(date1" + keyPostfix + ")";
    var date2_dtoKey = "dto(date2" + keyPostfix + ")";
    var value1_dtoKey = "dto(value1" + keyPostfix + ")";
    var value2_dtoKey = "dto(value2" + keyPostfix + ")";
    var select1_dtoKey = "dto(select1" + keyPostfix + ")";
    var popupIdValue_dtoKey = "dto(popupIdValue" + keyPostfix + ")";
    var popupValue_dtoKey = "dto(popupValue" + keyPostfix + ")";

    switch (showView) {
        case '${ONEBOX}':
            if (existThisKey(operatorValue, keyIsDateType)) {
                lib_getObj(oneBox_eKey).style.display = 'none';
                lib_getObj(oneBoxDate_eKey).style.display = '';
                //set value
                if (fValue != null)
                    lib_getObj(date_dtoKey).value = fValue;
            } else {
                lib_getObj(oneBox_eKey).style.display = '';
                lib_getObj(oneBoxDate_eKey).style.display = 'none';
                //set value
                if (fValue != null)
                    lib_getObj(value_dtoKey).value = fValue;
            }

            lib_getObj(twoBox_eKey).style.display = 'none';
            lib_getObj(twoBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = 'none';
            lib_getObj(mSelect_eKey).style.display = 'none';
            lib_getObj(popup_eKey).style.display = 'none';
            break
        case '${TWOBOX}':
            if (existThisKey(operatorValue, keyIsDateType)) {
                lib_getObj(twoBox_eKey).style.display = 'none';
                lib_getObj(twoBoxDate_eKey).style.display = '';
                //set value
                if (fValue != null) {
                    var v = fValue.split('${FVALUE_SEPARATOR}');
                    if (v.length > 0)
                        lib_getObj(date1_dtoKey).value = v[0];
                    if (v.length > 1)
                        lib_getObj(date2_dtoKey).value = v[1];
                }
            } else {
                lib_getObj(twoBox_eKey).style.display = '';
                lib_getObj(twoBoxDate_eKey).style.display = 'none';
                //set value
                if (fValue != null) {
                    var v = fValue.split('${FVALUE_SEPARATOR}');
                    if (v.length > 0)
                        lib_getObj(value1_dtoKey).value = v[0];
                    if (v.length > 1)
                        lib_getObj(value2_dtoKey).value = v[1];
                }
            }

            lib_getObj(oneBox_eKey).style.display = 'none';
            lib_getObj(oneBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = 'none';
            lib_getObj(mSelect_eKey).style.display = 'none';
            lib_getObj(popup_eKey).style.display = 'none';
            break
        case '${ONESELECT}':
            lib_getObj(oneBox_eKey).style.display = 'none';
            lib_getObj(oneBoxDate_eKey).style.display = 'none';
            lib_getObj(twoBox_eKey).style.display = 'none';
            lib_getObj(twoBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = '';
            lib_getObj(mSelect_eKey).style.display = 'none';
            lib_getObj(popup_eKey).style.display = 'none';
            if (fValue != null) {
                markAsSelected(select1_dtoKey, fValue);
            }
            break
        case '${MSELECT}':
            lib_getObj(oneBox_eKey).style.display = 'none';
            lib_getObj(oneBoxDate_eKey).style.display = 'none';
            lib_getObj(twoBox_eKey).style.display = 'none';
            lib_getObj(twoBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = 'none';
            lib_getObj(mSelect_eKey).style.display = '';
            lib_getObj(popup_eKey).style.display = 'none';
            //set selected values
            if (fValue != null) {
                selectedValuesToMultipleSelect(fValue, filterId);
            }
            break
        case '${POPUP}':
            lib_getObj(oneBox_eKey).style.display = 'none';
            lib_getObj(oneBoxDate_eKey).style.display = 'none';
            lib_getObj(twoBox_eKey).style.display = 'none';
            lib_getObj(twoBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = 'none';
            lib_getObj(mSelect_eKey).style.display = 'none';
            lib_getObj(popup_eKey).style.display = '';
            //set value
            if (fValue != null) {
                lib_getObj(popupIdValue_dtoKey).value = getValueByKey(fValue, '${KEY_VALUE}');
                lib_getObj(popupValue_dtoKey).value = unescape(getValueByKey(fValue, '${KEY_LABEL}'));
            }
            break
        case '${EMPTY}':
            lib_getObj(oneBox_eKey).style.display = 'none';
            lib_getObj(oneBoxDate_eKey).style.display = 'none';
            lib_getObj(twoBox_eKey).style.display = 'none';
            lib_getObj(twoBoxDate_eKey).style.display = 'none';
            lib_getObj(oneSelect_eKey).style.display = 'none';
            lib_getObj(mSelect_eKey).style.display = 'none';
            lib_getObj(popup_eKey).style.display = 'none';
            break
        default:
            alert('has not process');
    }
}
//specific execute filter functions
function numericTypeAlign(filterId) {
    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    lib_getObj("dto(value" + keyPostfix + ")").style.textAlign = "right";
    lib_getObj("dto(value1" + keyPostfix + ")").style.textAlign = "right";
    lib_getObj("dto(value2" + keyPostfix + ")").style.textAlign = "right";
}

function stringTypeAlign(filterId) {
    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    lib_getObj("dto(value" + keyPostfix + ")").style.textAlign = "left";
    lib_getObj("dto(value1" + keyPostfix + ")").style.textAlign = "left";
    lib_getObj("dto(value2" + keyPostfix + ")").style.textAlign = "left";
}

function selectedValuesToMultipleSelect(composeValue, filterId) {
    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    var selectBox = lib_getObj("multipleSelect" + keyPostfix);
    var valueArray = composeValue.split('${FVALUE_SEPARATOR}');

    for (var i = 0; i < selectBox.options.length; i++) {
        for (var j = 0; j < valueArray.length; j++) {
            if (selectBox.options[i].value == valueArray[j]) {
                selectBox.options[i].selected = true;
            }
        }
    }
    // set selected values in hidden
    setSelectMultipleValuesInHidden(selectBox, filterId);
}

function markAsSelected(boxKey, value) {
    var selectBox = lib_getObj(boxKey);

    //if is db filter, get id value
    var sValue = getValueByKey(value, '${KEY_VALUE}');
    if (sValue == null) {
        sValue = value;
    }

    for (var i = 0; i < selectBox.options.length; i++) {
        if (selectBox.options[i].value == sValue) {
            selectBox.options[i].selected = true;
            break;
        }
    }
}

function setSelectMultipleValuesInHidden(selectBox, filterId) {
    var keyPostfix = (filterId != null && filterId != undefined) ? filterId : "";
    var selectedValues = "";
    for (var i = 0; i < selectBox.options.length; i++) {
        if (selectBox.options[i].selected) {
            if (selectedValues.length > 0) {
                selectedValues = selectedValues + '${FVALUE_SEPARATOR}' + selectBox.options[i].value;
            } else {
                selectedValues = selectBox.options[i].value;
            }
        }
    }
    //set value
    lib_getObj("idMultiple" + keyPostfix).value = selectedValues;
}


//report format funtinos
function changeReportFormat(selectBox) {
    var value = selectBox.value;

    if(value == '${CSV_FORMAT}') {
        lib_getObj('csvSettingId').style.display = '';
    } else {
        lib_getObj('csvSettingId').style.display = 'none';
    }
}

//-->
</script>

<%--isExternalModule is definede in the template header of every page in its respective module--%>
<c:if test="${isExternalModule}">
    <c:choose>
        <c:when test="${not empty param['reportId']}">
            <c:set var="urlReportId" value="?reportId=${param['reportId']}"/>
        </c:when>
        <c:otherwise>
            <c:set var="urlReportId" value="?reportId=${param['dto(reportId)']}"/>
        </c:otherwise>
    </c:choose>
</c:if>


<html:form action="${action}${urlReportId}" focus="dto(reportFormat)">

<c:choose>
    <c:when test="${isExternalModule}">
        <c:choose>
            <c:when test="${not empty param['reportId']}">
                <c:set var="idReport" value="${param['reportId']}"/>
            </c:when>
            <c:otherwise>
                <c:set var="idReport" value="${param['dto(reportId)']}"/>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <c:set var="idReport" value="${param['reportId']}"/>
    </c:otherwise>
</c:choose>

<%--########reportId:${idReport}--%>
<html:hidden property="dto(reportId)" value="${idReport}"/>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <tr>
        <td class="button">
            <c:choose>
                <c:when test="${isExternalModule}">
                    <html:submit styleClass="button" tabindex="1">
                        <fmt:message key="EXECUTE"/>
                    </html:submit>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="EXECUTE" functionality="${reportFunctionalityKey}" styleClass="button"
                                         tabindex="1">
                        <fmt:message key="EXECUTE"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

<table id="Filter.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
    <tr>
        <td colspan="4" class="title">
            <fmt:message key="ReportGenerator.reportOptions"/>
            <c:if test="${isExternalModule}">
                <html:hidden property="dto(name)"
                             value="${not empty param['dto(name)'] ? param['dto(name)'] : dto['name']}"/>
                <c:out value=": ${not empty param['dto(name)'] ? param['dto(name)'] : dto['name']}"/>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="label" width="20%">
            <fmt:message key="ReportGenerator.reportFormat"/>
        </td>
        <td class="contain" colspan="3" width="30%">
            <c:set var="reportFormats" value="${app2:getReportFormats(pageContext.request)}" scope="request"/>
            <html:select property="dto(reportFormat)" styleId="format" styleClass="mediumSelect" tabindex="4"
                    onchange="changeReportFormat(this)"
                    onkeyup="changeReportFormat(this)">
                <html:options collection="reportFormats" property="reportFormat" labelProperty="reportFormatName"/>
            </html:select>
        </td>
    </tr>

    <tr id="csvSettingId" ${reportJrxmlExecuteForm.dtoMap['reportFormat'] eq CSV_FORMAT ? "" : "style=\"display: none;\""}>
        <td class="label">
            <fmt:message key="ReportGenerator.charset"/>
        </td>
        <td class="contain">
            <c:set var="reportCharsetList" value="${app2:getReportCharsetList(pageContext.request)}" scope="request"/>
            <html:select property="dto(reportCharset)" styleClass="mediumSelect" tabindex="3">
                <html:options collection="reportCharsetList" property="value" labelProperty="label"/>
            </html:select>
        </td>
        <td class="label">
            <fmt:message key="ReportGenerator.csvDelimiter"/>
        </td>
        <td class="contain">
            <c:set var="csvFieldDelimiters" value="${app2:getCsvFieldDelimiters()}" scope="request"/>
            <html:select property="dto(csvDelimiter)" styleClass="mediumSelect" tabindex="5">
                <html:options collection="csvFieldDelimiters" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
</table>
<!--report filters section-->
<c:set var="filterDtoList" value="${app2:getAllReportFilters(idReport, pageContext.request)}"/>
<c:if test="${not empty filterDtoList}">
    <table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
        <tr>
            <td colspan="3" class="title">
                <fmt:message key="Report.execute.reoprtFilters"/>
            </td>
        </tr>
        <fmt:message var="datePattern" key="datePattern"/>
        <c:forEach var="filter" items="${filterDtoList}" varStatus="x">
            <c:if test="${not empty filter.filterValue}">
                <c:set var="filterValueParsed"
                       value="${app2:parserFilterValue(filter.filterValue,filter.path,filter.filterType,false,pageContext.request)}"/>
            </c:if>

            <c:set var="idF" value="${filter.filterId}" scope="request"/>
            <c:set var="operator" value="${filter.operator}"/>
            <c:set var="tableRef" value="${filter.tableReference}"/>
            <c:set var="columnRef" value="${filter.columnReference}"/>
            <c:set var="columnPath" value="${app2:decodeFieldPath(filter.path)}"/>
            <c:set var="operatorView"
                   value="${app2:composeOperatorValue(operator, tableRef, columnRef, pageContext.request)}"/>
            <c:set var="columnLabel" value="${filter.label}"/>


            <html:hidden property="dto(keyFilter${idF})" value="${filter.filterId}"/>
            <html:hidden property="dto(columnType${idF})" value="${filter.columnType}"/>
            <html:hidden property="dto(path${idF})" styleId="pathId${idF}" value="${filter.path}"/>
            <html:hidden property="dto(opSelect${idF})" value="${operatorView}"/>
            <html:hidden property="dto(columnLabel${idF})" value="${columnLabel}"/>
            <html:hidden property="dto(filterType${idF})" value="${filter.filterType}"/>

            <tr>
                <td class="label" width="35%">
                    <c:out value="${columnLabel}"/>
                </td>
                <td class="contain" width="20%" style="text-align:center;">
                    <c:out value="${app2:getOperatorMenssage(operator, pageContext.request)}"/>
                </td>
                <td class="contain" width="55%">
                    <table id="oneBox${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <app:text property="dto(value${idF})" styleClass="text" tabindex="5" maxlength="100"/>
                            </td>
                        </tr>
                    </table>
                    <table id="twoBox${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <fmt:message key="Report.filter.from"/>&nbsp;
                                <app:text property="dto(value1${idF})" styleClass="shortText" tabindex="5"
                                          maxlength="100"/>
                            </td>
                            <td>
                                &nbsp;
                                <fmt:message key="Report.filter.to"/>
                                &nbsp;
                            </td>
                            <td>
                                <app:text property="dto(value2${idF})" styleClass="shortText" tabindex="5"
                                          maxlength="100"/>
                            </td>
                        </tr>
                    </table>
                    <c:if test="${not empty tableRef && not empty columnRef && not empty columnPath}">
                        <c:choose>
                            <c:when test="${app2:isConstantFilter(tableRef, columnRef, pageContext.request)}">
                                <c:set var="listOfItems"
                                       value="${app2:getFilterConstantValues(tableRef, columnRef, pageContext.request)}"/>
                            </c:when>
                            <c:when test="${app2:isDbFilterInSelect(columnPath,pageContext.request)}">
                                <c:set var="listOfItems"
                                       value="${app2:getValuesOfDbFilterToSelect(columnPath,pageContext.request)}"/>
                            </c:when>
                        </c:choose>
                    </c:if>

                    <table id="oneSelect${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <!--MMM-->
                                <html:select property="dto(select1${idF})" styleClass="select" tabindex="5">
                                    <html:option value="">&nbsp;</html:option>
                                    <c:if test="${not empty listOfItems}">
                                        <c:forEach var="listOption" items="${listOfItems}">
                                            <html:option value="${listOption.value}"><c:out
                                                    value="${listOption.label}"/></html:option>
                                        </c:forEach>
                                    </c:if>
                                </html:select>
                            </td>
                        </tr>
                    </table>

                    <table id="mSelect${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <html:hidden property="dto(multipleSelect${idF})" styleId="idMultiple${idF}"/>
                                <!--MMM-->
                                <html:select property="multipleSelect${idF}" styleId="mSelectId${idF}" value=""
                                             styleClass="multipleSelectFilter" multiple="true" tabindex="5"
                                             onchange="javascript:setSelectMultipleValuesInHidden(this.form.mSelectId${idF},'${idF}')"
                                             onkeyup="javascript:setSelectMultipleValuesInHidden(this.form.mSelectId${idF},'${idF}')">
                                    <c:if test="${not empty listOfItems}">
                                        <c:forEach var="listOption" items="${listOfItems}">
                                            <html:option value="${listOption.value}"><c:out
                                                    value="${listOption.label}"/></html:option>
                                        </c:forEach>
                                    </c:if>
                                </html:select>
                            </td>
                        </tr>
                    </table>
                    <table id="oneBoxDate${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <app:dateText property="dto(date${idF})" styleId="date${idF}" calendarPicker="true"
                                              datePatternKey="${datePattern}"
                                              styleClass="text" maxlength="10" tabindex="5"/>
                            </td>
                        </tr>
                    </table>
                    <table id="twoBoxDate${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <fmt:message key="Report.filter.from"/>&nbsp;
                                <app:dateText property="dto(date1${idF})" styleId="date1${idF}" calendarPicker="true"
                                              datePatternKey="${datePattern}" styleClass="shortText"
                                              maxlength="10" tabindex="5"/>
                            </td>
                            <td>
                                &nbsp;
                                <fmt:message key="Report.filter.to"/>
                                &nbsp;
                            </td>
                            <td>
                                <app:dateText property="dto(date2${idF})" styleId="date2${idF}" calendarPicker="true"
                                              datePatternKey="${datePattern}" styleClass="shortText"
                                              maxlength="10" tabindex="5"/>
                            </td>
                        </tr>
                    </table>
                    <table id="popup${idF}" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <html:hidden property="dto(popupIdValue${idF})" styleId="popupIdValue${idF}"/>
                                <app:text property="dto(popupValue${idF})" styleId="popupValue${idF}" styleClass="text"
                                          readonly="true"
                                          tabindex="5"/>
                                <tags:selectPopup url="/reports/Report/Filter/ImportDBFilter.do?reportId=${idReport}" name="${idF}"
                                                  titleKey="Report.filter.searchFilterValue"
                                                  width="755" heigth="480" tabindex="5"/>
                                <tags:clearSelectPopup keyFieldId="popupIdValue${idF}" nameFieldId="popupValue${idF}"
                                                       titleKey="Common.clear"
                                                       tabindex="5"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <c:choose>
                <c:when test="${not empty dto and (not empty filter.filterValue)}">
                    <script language="JavaScript">
                        <!--
                        showView('${operatorView}', '${filterValueParsed}', '${idF}');
                        //-->
                    </script>
                </c:when>
                <c:otherwise>
                    <script language="JavaScript">
                        <!--
                        showView('${operatorView}', null, '${idF}');
                        //-->
                    </script>
                </c:otherwise>
            </c:choose>

            <c:set var="valueMSelect" value='<%=request.getAttribute("mSelectValue" + request.getAttribute("idF"))%>'/>
            <c:if test="${not empty valueMSelect}">
                <script language="JavaScript">
                    <!--
                    selectedValuesToMultipleSelect('${valueMSelect}', '${idF}');
                    //-->
                </script>
            </c:if>
        </c:forEach>
    </table>
</c:if>
<!--end report filters section-->
<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <tr>
        <td class="button">
            <c:choose>
                <c:when test="${isExternalModule}">
                    <html:submit styleClass="button" tabindex="6">
                        <fmt:message key="EXECUTE"/>
                    </html:submit>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="EXECUTE" functionality="${reportFunctionalityKey}" styleClass="button"
                                         tabindex="7">
                        <fmt:message key="EXECUTE"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
</html:form>