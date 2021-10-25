<%@ page import="com.jatun.titus.listgenerator.structure.StructureConstants" %>
<%@ page import="com.piramide.elwis.utils.ReportConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<!--this tag not is required-->
<%--<tags:initSelectPopup/> --%>

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

    //query param operator keys
    request.setAttribute("OP_EQUAL", StructureConstants.FilterTitusType.OP_EQUAL);
    request.setAttribute("OP_IS", StructureConstants.FilterTitusType.OP_IS);
    request.setAttribute("OP_ON", StructureConstants.FilterTitusType.OP_ON);
%>

<script language="JavaScript">
    <!--

    function putPopupValue(idValue, value) {
        lib_getObj("dto(popupIdValue)").value = idValue;
        lib_getObj("dto(popupValue)").value = unescape(value);
        hideBootstrapPopup();
    }
    function customLaunchSelectPopup(styleId, url, searchName, submitOnSelect) {

        //add parameters to url
        filterPath = lib_getObj('dto(path)').value;

        //decode
        filterPath = filterPath.replace(/&lt;/g, "<");
        filterPath = filterPath.replace(/&gt;/g, ">");

        url = url + '?filterPath=' + filterPath;

        launchBootstrapPopup(styleId, url, searchName, submitOnSelect);
    }
    //get object by id or name
    function lib_getObj(id, d) {
        var i, x;
        if (!d) d = document;
        if (!(x = d[id]) && d.all) x = d.all[id];
        for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][id];
        for (i = 0; !x && d.layers && i < d.layers.length; i++) x = lib_getObj(id, d.layers[i].document);
        if (!x && document.getElementById) x = document.getElementById(id);
        return x;
    }

    function putBasicData(columnLabel, column, columnType, table, path, filterType) {
        //lib_getObj("labelColumn").value = columnLabel;
        lib_getObj("labelColumn").innerHTML = columnLabel;
        lib_getObj("dto(label)").value = columnLabel;
        lib_getObj("dto(columnReference)").value = column;
        lib_getObj("dto(columnType)").value = columnType;
        lib_getObj("dto(tableReference)").value = table;
        lib_getObj("dto(path)").value = path;
        lib_getObj("dto(filterType)").value = filterType;
    }

    function cleanBasicData() {
        //lib_getObj("labelColumn").value = columnLabel;
        lib_getObj("labelColumn").innerHTML = "";
        lib_getObj("dto(label)").value = "";
        lib_getObj("dto(columnReference)").value = "";
        lib_getObj("dto(columnType)").value = "";
        lib_getObj("dto(tableReference)").value = "";
        lib_getObj("dto(path)").value = "";
        lib_getObj("dto(filterType)").value = "";
    }

    function emptyShowView() {
        lib_getObj("dto(value)").value = "";
        lib_getObj("dto(value1)").value = "";
        lib_getObj("dto(value2)").value = "";

        setAllUnSelected(lib_getObj("dto(select1)"));
        setAllUnSelected(lib_getObj("multipleSelect"));

        lib_getObj("dto(date)").value = "";
        lib_getObj("dto(date1)").value = "";
        lib_getObj("dto(date2)").value = "";
        lib_getObj("dto(popupIdValue)").value = "";
        lib_getObj("dto(popupValue)").value = "";
    }

    function putOperatorSelect(operatorData, selectedOp) {
        //alert(selectedOp);
        var opBox = lib_getObj("dto(opSelect)");
        var opArray = operatorData.split('${KEY_SEPART}');
        var keyLabel = '${KEY_LABEL}';
        var keyValue = '${KEY_VALUE}';
        //clean options
        cleanSelectOption(opBox);

        var opIndex = 0;
        for (var i = 0; i < opArray.length; i++) {
            var opt = new Option();
            opt.text = getValueByKey(opArray[i], keyLabel);
            opt.value = getValueByKey(opArray[i], keyValue);
            /*if(opt.value == selectedOp){
             opt.selected = true;
             }*/

            if (isValidQueryParamOperator(opt.value)) {
                opBox.options[opIndex] = opt;
                opIndex++;
            }
        }
        //show view value
        showView(opBox);
    }

    function isValidQueryParamOperator(opBoxValue) {
        var isValid = false;
        var opeConstant = getValueByKey(opBoxValue, '${KEY_OP}');

        if ('${OP_EQUAL}' == opeConstant || '${OP_IS}' == opeConstant || '${OP_ON}' == opeConstant) {
            isValid = true;
        }
        return isValid;
    }

    /*clean all options*/
    function cleanSelectOption(opBox) {
        for (var i = 0; i < opBox.options.length; i++) {
            opBox.options[i].value = "";
            opBox.options[i].text = "";
        }
        opBox.options.length = 0;
    }

    function putSelectData(data, selectName, selectedValue, isFirstBlank) {
        var selectBox = lib_getObj(selectName);
        var opArray = data.split('${KEY_SEPART}');
        var keyLabel = '${KEY_LABEL}';
        var keyValue = '${KEY_VALUE}';
        //clean options
        cleanSelectOption(selectBox);

        var previousOpt = 0;
        if (isFirstBlank) {
            var opt = new Option();
            opt.text = "";
            opt.value = "";
            selectBox.options[0] = opt;
            previousOpt = 1;
        }

        for (var i = 0; i < opArray.length; i++) {
            var opt = new Option();
            opt.text = getValueByKey(opArray[i], keyLabel);
            opt.value = getValueByKey(opArray[i], keyValue);
            if (selectedValue != null && opt.value == selectedValue) {
                opt.selected = true;
            }
            if (opt.value != "" && opt.text != "")
                selectBox.options[i + previousOpt] = opt;
        }
    }

    function showView(opBox, fValue) {
        for (var i = 0; i < opBox.options.length; i++) {
            if (opBox.options[i].selected && opBox.options[i].value != "") {
                var keyIsDateType = '${KEY_ISDATETYPE}';
                var keyView = '${KEY_SHOWVIEW}';
                var showView = getValueByKey(opBox.options[i].value, keyView);

                if (existThisKey(opBox.options[i].value, '${KEY_ISNUMERICTYPE}')) {
                    numericTypeAlign();
                } else {
                    stringTypeAlign();
                }

                //always display true
                lib_getObj("bodyViewValue").style.display = '';
                switch (showView) {
                    case '${ONEBOX}':
                        if (existThisKey(opBox.options[i].value, keyIsDateType)) {
                            lib_getObj("oneBox").style.display = 'none';
                            lib_getObj("oneBoxDate").style.display = '';
                            //set value
                            if (fValue != null)
                                lib_getObj("dto(date)").value = fValue;
                        } else {
                            lib_getObj("oneBox").style.display = '';
                            lib_getObj("oneBoxDate").style.display = 'none';
                            //set value
                            if (fValue != null)
                                lib_getObj("dto(value)").value = fValue;
                        }

                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = 'none';
                        lib_getObj("mSelect").style.display = 'none';
                        lib_getObj("popup").style.display = 'none';
                        lib_getObj("viewValue").style.display = '';
                        break
                    case '${TWOBOX}':
                        if (existThisKey(opBox.options[i].value, keyIsDateType)) {
                            lib_getObj("twoBox").style.display = 'none';
                            lib_getObj("twoBoxDate").style.display = '';
                            //set value
                            if (fValue != null) {
                                var v = fValue.split('${FVALUE_SEPARATOR}');
                                if (v.length > 0)
                                    lib_getObj("dto(date1)").value = v[0];
                                if (v.length > 1)
                                    lib_getObj("dto(date2)").value = v[1];
                            }
                        } else {
                            lib_getObj("twoBox").style.display = '';
                            lib_getObj("twoBoxDate").style.display = 'none';
                            //set value
                            if (fValue != null) {
                                var v = fValue.split('${FVALUE_SEPARATOR}');
                                if (v.length > 0)
                                    lib_getObj("dto(value1)").value = v[0];
                                if (v.length > 1)
                                    lib_getObj("dto(value2)").value = v[1];
                            }
                        }

                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = 'none';
                        lib_getObj("mSelect").style.display = 'none';
                        lib_getObj("popup").style.display = 'none';
                        lib_getObj("viewValue").style.display = '';
                        break
                    case '${ONESELECT}':
                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = 'none';
                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = '';
                        lib_getObj("mSelect").style.display = 'none';
                        lib_getObj("popup").style.display = 'none';
                        lib_getObj("viewValue").style.display = '';
                        break
                    case '${MSELECT}':
                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = 'none';
                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = 'none';
                        lib_getObj("mSelect").style.display = '';
                        lib_getObj("popup").style.display = 'none';
                        lib_getObj("viewValue").style.display = '';
                        //set selected values
                        if (fValue != null) {
                            selectedValuesToMultipleSelect(fValue);
                        }
                        break
                    case '${POPUP}':
                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = 'none';
                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = 'none';
                        lib_getObj("mSelect").style.display = 'none';
                        lib_getObj("popup").style.display = '';
                        lib_getObj("viewValue").style.display = '';
                        //set value
                        if (fValue != null) {
                            lib_getObj("dto(popupIdValue)").value = getValueByKey(fValue, '${KEY_VALUE}');
                            lib_getObj("dto(popupValue)").value = unescape(getValueByKey(fValue, '${KEY_LABEL}'));
                        }
                        break
                    case '${EMPTY}':
                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = 'none';
                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = 'none';
                        lib_getObj("oneSelect").style.display = 'none';
                        lib_getObj("mSelect").style.display = 'none';
                        lib_getObj("popup").style.display = 'none';
                        lib_getObj("viewValue").style.display = 'none';
                        lib_getObj("bodyViewValue").style.display = 'none';
                        break
                    default:
                        alert('blanck');
                }
            }
        }
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

    function selectedValuesToMultipleSelect(composeValue) {
        var selectBox = lib_getObj("multipleSelect");
        var valueArray = composeValue.split('${FVALUE_SEPARATOR}');

        for (var i = 0; i < selectBox.options.length; i++) {
            for (var j = 0; j < valueArray.length; j++) {
                if (selectBox.options[i].value == valueArray[j]) {
                    selectBox.options[i].selected = true;
                }
            }
        }
    }

    function setAllUnSelected(box) {
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].selected) {
                box.options[i].selected = false;
            }
        }
    }

    function numericTypeAlign() {
        lib_getObj("dto(value)").style.textAlign = "right";
        lib_getObj("dto(value1)").style.textAlign = "right";
        lib_getObj("dto(value2)").style.textAlign = "right";
    }

    function stringTypeAlign() {
        lib_getObj("dto(value)").style.textAlign = "left";
        lib_getObj("dto(value1)").style.textAlign = "left";
        lib_getObj("dto(value2)").style.textAlign = "left";
    }

    /*ajax management*/
    //call through ajax from TreeColumn.jsp
    function ajaxReadXmlFilter(xmldoc) {

        //hide tooltip, this method may be find in TreeColumn.jsp
        hideToolTip();

        if (xmldoc != null && xmldoc != undefined) {
            var filter = xmldoc.getElementsByTagName('filter');
            if (filter.length == 0) {
                //show fail message
                var failNodes = xmldoc.getElementsByTagName('failcolumn');
                if (failNodes.length > 0) {
                    cleanBasicData();
                    emptyShowView();
                    lib_getObj("filterView").style.display = 'none';
                    lib_getObj("msgDynanicColumn").style.display = '';
                }
                return false;
            } else {
                lib_getObj("msgDynanicColumn").style.display = 'none';
            }
        } else {
            return false;
        }

        var label = unescape(xmldoc.getElementsByTagName('label').item(0).firstChild.nodeValue);
        var column = xmldoc.getElementsByTagName('column').item(0).firstChild.nodeValue;
        var columnType = xmldoc.getElementsByTagName('columntype').item(0).firstChild.nodeValue;
        var table = xmldoc.getElementsByTagName('table').item(0).firstChild.nodeValue;
        var path = xmldoc.getElementsByTagName('path').item(0).firstChild.nodeValue;
        var operator = unescape(xmldoc.getElementsByTagName('operator').item(0).firstChild.nodeValue);
        var filterType = xmldoc.getElementsByTagName('filtertype').item(0).firstChild.nodeValue;

        var filterConstantValue = null;
        var dbfilterValue = null;
        if (xmldoc.getElementsByTagName('filterconstant').item(0) != null) {
            filterConstantValue = unescape(xmldoc.getElementsByTagName('filterconstant').item(0).firstChild.nodeValue);
        }

        if (xmldoc.getElementsByTagName('dbfiltervalue').item(0) != null) {
            dbfilterValue = unescape(xmldoc.getElementsByTagName('dbfiltervalue').item(0).firstChild.nodeValue);
        }

        var str = "label:" + label;
        str += "<br>";
        str += "column:" + column;
        str += "<br>";
        str += "columntype:" + columnType;
        str += "<br>";
        str += "table:" + table;
        str += "<br>";
        str += "path:" + path;
        str += "<br>";
        str += "operator:" + operator;
        str += "<br>";

        var mydiv = document.getElementById("divView");
        //mydiv.innerHTML = str;

        if (operator != '${KEY_NOT_OP}') {
            //clean show view
            emptyShowView();
            //set filter basic data
            putBasicData(label, column, columnType, table, path, filterType);
            //set operator data
            putOperatorSelect(operator);

            //set filter constant data
            if (filterConstantValue != null) {
                putSelectData(filterConstantValue, "dto(select1)", null, true);
                putSelectData(filterConstantValue, "multipleSelect", null, false); //multiple select
            }

            //set dbfilter data values
            if (dbfilterValue != null) {
                putSelectData(dbfilterValue, "dto(select1)", null, true);
                putSelectData(dbfilterValue, "multipleSelect", null, false); //multiple select
            }
            //view filter
            lib_getObj("filterView").style.display = '';
        } else {
            cleanBasicData();
            //view filter
            lib_getObj("filterView").style.display = 'none';
        }
    }
    //-->
</script>


<!--this jsp only to update or delete filter-->
<html:form action="${action}" styleClass="form-horizontal">

    <html:hidden property="dto(reportQueryParamId)"/>
    <html:hidden property="dto(companyId)"/>

    <html:hidden property="dto(reportId)" value="${param['reportId']}"/>
    <html:hidden property="dto(initTable)" value="${initReportTable}"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(op)" value="${op}"/>

    <c:set var="tableWidth" value="90%"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(filterId)"/>
        <html:hidden property="dto(aliasCondition)"/>
        <c:set var="tableWidth" value="65%"/>
    </c:if>

    <c:set var="container" value="false"/>
    <c:if test="${hideTree}">
        <c:set var="container" value="true"/>
    </c:if>

    <html:hidden property="dto(columnReference)" styleId="columnReferenceId"/>
    <html:hidden property="dto(columnType)" styleId="columnTypeId"/>
    <html:hidden property="dto(tableReference)" styleId="tableReferenceId"/>
    <html:hidden property="dto(operator)" styleId="operatorId"/>
    <html:hidden property="dto(filterValue)" styleId="filterValueId"/>
    <html:hidden property="dto(filterType)" styleId="filterTypeId"/>
    <html:hidden property="dto(path)" styleId="pathId"/>

    <!--get dtos from form-->
    <c:set var="tableRef" value="${queryParamForm.dtoMap['tableReference']}"/>
    <c:set var="columnRef" value="${queryParamForm.dtoMap['columnReference']}"/>
    <c:set var="columnPath" value="${app2:decodeFieldPath(queryParamForm.dtoMap['path'])}"/>
    <c:set var="operator" value="${queryParamForm.dtoMap['operator']}"/>
    <c:set var="columnType" value="${queryParamForm.dtoMap['columnType']}"/>
    <c:set var="filterType" value="${queryParamForm.dtoMap['filterType']}"/>

    <c:if test="${not empty dto.filterValue}">
        <c:set var="filterValueParsed"
               value="${app2:parserFilterValue(dto.filterValue,dto.path,filterType,false,pageContext.request)}"/>
    </c:if>

    <c:if test="${container=='true'}">
        <div class="${app2:getFormClassesLarge()}">
    </c:if>
    <div class="${app2:getFormButtonWrapperClasses()}">

        <app2:securitySubmit operation="${op}" functionality="${queryParamFunctionality}"
                             styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>

        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${title}"/>
        </legend>
        <c:if test="${container=='false'}">
        <div>
            <div class="table-responsive table-responsive-filer">
                <table width="100%" align="center"
                       class="${app2:getFantabulousTableClases()}">
                    </c:if>
                    <tr>

                        <c:set var="createView" value=""/>
                        <c:set var="containTop" value="containTop"/>
                        <c:if test="${hideTree}">
                            <c:set var="createView" value="display:none"/>
                            <c:set var="containTop" value=""/>
                        </c:if>
                        <c:if test="${container=='false'}">
                            <td style="${createView}" width="25%">
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="col-xs-12 control-label label-left">
                                        <fmt:message key="Report.columns"/>
                                    </label>

                                    <div class="col-xs-12">
                                        <c:set var="ajaxCallJavaScript" value="ajaxReadXmlFilter" scope="request"/>
                                        <c:set var="ajaxUrlExecute" value="/reports/Report/Filter/TreeColumns.do"
                                               scope="request"/>
                                        <c:import url="/WEB-INF/jsp/reports/TreeColumn.jsp"/>
                                    </div>
                                </div>
                            </td>
                        </c:if>
                        <td class="${containTop}" width="75%" style=" vertical-align: super">
                            <c:choose>
                                <c:when test="${ not empty hasErrors || op != 'create'}">
                                    <c:set var="displayFilter" value=""/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="displayFilter" value="display:none"/>
                                </c:otherwise>
                            </c:choose>

                            <div id="msgDynanicColumn" style="display:none;">
                                <div>
                                    <div class="">
                                        <fmt:message key="Report.columns.dynamicColumn.deleted"/>
                                    </div>
                                </div>
                            </div>


                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelClasses()}">
                                    <fmt:message key="Report.queryParam.parameterName"/>
                                </label>

                                <div class="${app2:getFormContainClasses(true)}">
                                    <html:hidden property="dto(parameterName)" write="true"/>
                                </div>
                            </div>


                            <div id="filterView" style="${displayFilter}">
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="Report.filter.field"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses(true)}">
                                        <div id="labelColumn">
                                            <c:if test="${not empty columnPath}">
                                                <c:out value="${app2:composeColumnLabelByTitusPath(app2:decodeFieldPath(columnPath), null, pageContext.request)}"/>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="Report.filter.label"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses('delete'==op)}">
                                        <app:text property="dto(label)" styleClass="text ${app2:getFormInputClasses()}" tabindex="2"
                                                  maxlength="150"
                                                  view="${op == 'delete'}"/>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelClasses()}" for="isParameter_id">
                                        <fmt:message key="Report.filter.parameterFilter"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses(null)}">
                                        <div class="radiocheck">
                                            <div class="checkbox checkbox-default">
                                                <html:checkbox property="dto(isParameter)"
                                                               styleId="isParameter_id"
                                                               styleClass="" disabled="${op == 'delete'}"
                                                               tabindex="2"/>
                                                <label for="isParameter_id"></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="Report.filter.operator"/>
                                    </label>

                                    <div class="${app2:getFormContainClasses('delete'==op)}">
                                        <c:if test="${not empty tableRef && not empty columnRef}">
                                            <c:set var="operatorValue"
                                                   value="${app2:composeOperatorValue(operator, tableRef, columnRef, pageContext.request)}"/>
                                        </c:if>

                                        <html:select property="dto(opSelect)" styleId="idOpBox" value="${operatorValue}"
                                                     styleClass="select ${app2:getFormSelectClasses()}"
                                                     onchange="javascript:showView(this.form.idOpBox)"
                                                     onkeyup="javascript:showView(this.form.idOpBox)" readonly="${op == 'delete'}"
                                                     tabindex="2">
                                            <c:if test="${not empty tableRef && not empty columnRef}">
                                                <c:forEach var="listOp"
                                                           items="${app2:getReportQueryParamOperators(tableRef, columnRef, pageContext.request)}">
                                                    <html:option value="${listOp.value}"><c:out
                                                            value="${listOp.label}"/></html:option>
                                                </c:forEach>
                                            </c:if>
                                        </html:select>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label id="viewValue" class="${app2:getFormLabelClasses()}">
                                        <fmt:message key="Report.filter.value"/>
                                    </label>

                                    <div id="bodyViewValue" class="${app2:getFormContainClasses('delete'==op)}">
                                        <c:if test="${op == 'delete'}">
                                            <c:set var="showMessage"
                                                   value="${app2:composeFilterValueToView(dto.filterValue,operator,tableRef,columnRef,dto.path,columnType,filterType,pageContext.request)}"/>
                                            <c:choose>
                                                <c:when test="${app2:isDbFilter(tableRef,columnRef,pageContext.request) and (empty showMessage)}">
                                <span style="color:#FF0000">
                                    <fmt:message key="Report.filter.error.removedReference"/>
                                </span>
                                                </c:when>
                                                <c:when test="${empty showMessage}">
                                                    <script type="text/javascript" language="JavaScript">
                                                        <!--
                                                        lib_getObj("viewValue").style.display = 'none';
                                                        lib_getObj("bodyViewValue").style.display = 'none';
                                                        //-->
                                                    </script>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${showMessage}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <div id="oneBox" style="display:none;">
                                            <app:text property="dto(value)" styleClass="text ${app2:getFormInputClasses()}"
                                                      tabindex="3" maxlength="100"/>
                                        </div>
                                        <div id="twoBox" style="display:none;">
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                                    <label class="control-label" for="">
                                                        <fmt:message key="Report.filter.from"/>
                                                    </label>
                                                    <app:text property="dto(value1)"
                                                              styleClass="shortText shortText numberText wrapperButton numberInputWidth ${app2:getFormInputClasses()}" tabindex="3"
                                                              maxlength="100"/>
                                                </div>
                                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                                    <label class="control-label" for="">
                                                        <fmt:message key="Report.filter.to"/>
                                                    </label>
                                                    <app:text property="dto(value2)"
                                                              styleClass="shortText shortText numberText wrapperButton numberInputWidth ${app2:getFormInputClasses()}" tabindex="4"
                                                              maxlength="100"/>
                                                </div>
                                            </div>
                                        </div>
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

                                        <div id="oneSelect" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                                            <html:select property="dto(select1)" value="${dto.filterValue}"
                                                         styleClass="select ${app2:getFormSelectClasses()}" tabindex="3">
                                                <html:option value="">&nbsp;</html:option>
                                                <c:if test="${not empty listOfItems}">
                                                    <c:forEach var="listOption" items="${listOfItems}">
                                                        <html:option value="${listOption.value}"><c:out
                                                                value="${listOption.label}"/></html:option>
                                                    </c:forEach>
                                                </c:if>
                                            </html:select>
                                        </div>

                                        <div id="mSelect" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                                            <html:select property="multipleSelect"
                                                         styleClass="multipleSelectFilter ${app2:getFormSelectClasses()}"
                                                         multiple="true" tabindex="3">
                                                <c:if test="${not empty listOfItems}">
                                                    <c:forEach var="listOption" items="${listOfItems}">
                                                        <html:option value="${listOption.value}"><c:out
                                                                value="${listOption.label}"/></html:option>
                                                    </c:forEach>
                                                </c:if>
                                            </html:select>
                                        </div>
                                        <fmt:message var="datePattern" key="datePattern"/>
                                        <div id="oneBoxDate" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                                            <div class="input-group date">
                                                <app:dateText property="dto(date)" styleId="date" calendarPicker="true"
                                                              datePatternKey="${datePattern}"
                                                              mode="bootstrap"
                                                              styleClass="text ${app2:getFormInputClasses()}"
                                                              view="${op == 'delete'}" maxlength="10"
                                                              tabindex="3"/>
                                            </div>
                                        </div>
                                        <div id="twoBoxDate" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                                    <fmt:message var="from" key="Report.filter.from"/>
                                                    <div class="input-group date">
                                                        <app:dateText property="dto(date1)" styleId="date1"
                                                                      calendarPicker="true"
                                                                      mode="bootstrap"
                                                                      placeHolder="${from}"
                                                                      datePatternKey="${datePattern}"
                                                                      styleClass="shortText ${app2:getFormInputClasses()}"
                                                                      view="${op == 'delete'}"
                                                                      maxlength="10" tabindex="3"/>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                                    <fmt:message var="to" key="Report.filter.to"/>
                                                    <div class="input-group date">
                                                        <app:dateText property="dto(date2)" styleId="date2"
                                                                      calendarPicker="true"
                                                                      mode="bootstrap"
                                                                      placeHolder="${to}"
                                                                      datePatternKey="${datePattern}"
                                                                      styleClass="shortText ${app2:getFormInputClasses()}"
                                                                      view="${op == 'delete'}"
                                                                      maxlength="10" tabindex="4"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="popup" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                                            <div class="input-group">
                                                <app:text property="dto(popupValue)" styleId="popupValue"
                                                          styleClass="text ${app2:getFormInputClasses()}"
                                                          readonly="true"
                                                          tabindex="3"/>
                                                <html:hidden property="dto(popupIdValue)" styleId="popupIdValue"/>
                                    <span class="input-group-btn">
                                        <tags:bootstrapSelectPopup
                                                styleId="popupId_id"
                                                url="/reports/Report/Filter/ImportDBFilter.do?reportId=${param['reportId']}"
                                                name="popupId"
                                                isLargeModal="true"
                                                customLaunchMethodName="customLaunchSelectPopup"
                                                titleKey="Report.filter.searchFilterValue"
                                                tabindex="4"/>
                                        <tags:clearBootstrapSelectPopup keyFieldId="popupIdValue"
                                                                        nameFieldId="popupValue"
                                                                        titleKey="Common.clear"
                                                                        hide="${op == 'delete'}" tabindex="5"/>
                                    </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <c:if test="${container=='false'}">
                </table>
            </div>
        </div>
        </c:if>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">

        <app2:securitySubmit operation="${op}" functionality="${queryParamFunctionality}"
                             tabindex="10"
                             styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>

        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>
    <c:if test="${container=='true'}">
        </div>
    </c:if>
</html:form>
<c:choose>
    <c:when test="${not empty dto.filterValue and op != 'delete'}">
        <script language="JavaScript">
            <!--
            var opBoxs = lib_getObj("idOpBox");
            showView(opBoxs, '${filterValueParsed}');
            //-->
        </script>
    </c:when>
    <c:when test="${op != 'delete'}">
        <script language="JavaScript">
            <!--
            var opBoxs = lib_getObj("idOpBox");
            showView(opBoxs);
            //-->
        </script>
    </c:when>
</c:choose>

<style>
    .table-responsive-filer {
        overflow-x: visible;
    }
</style>
