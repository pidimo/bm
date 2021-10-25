<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants" %>
<%@ page import="com.piramide.elwis.utils.ReportConstants" %>

<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>

<%
    request.setAttribute("KEY_SEPART", ReportConstants.KEY_SEPARATOR);
    request.setAttribute("KEY_LABEL", ReportConstants.KEY_SEPARATOR_LABEL);
    request.setAttribute("KEY_VALUE", ReportConstants.KEY_SEPARATOR_VALUE);
    request.setAttribute("KEY_EMPTY", ReportConstants.KEY_EMPTY);

    request.setAttribute("KEY_SERIE", ReportConstants.KEY_CHARTVIEW_SERIE);
    request.setAttribute("KEY_XVALUE", ReportConstants.KEY_CHARTVIEW_XVALUE);
    request.setAttribute("KEY_YVALUE", ReportConstants.KEY_CHARTVIEW_YVALUE);
    request.setAttribute("KEY_ZVALUE", ReportConstants.KEY_CHARTVIEW_ZVALUE);
    request.setAttribute("KEY_CATEGORY", ReportConstants.KEY_CHARTVIEW_CATEGORY);
    request.setAttribute("KEY_XLABEL", ReportConstants.KEY_CHARTVIEW_XLABEL);
    request.setAttribute("KEY_YLABEL", ReportConstants.KEY_CHARTVIEW_YLABEL);
    request.setAttribute("KEY_NONEORIENTATION", ReportConstants.KEY_CHARTVIEW_NONE_ORIENTATION);

    //default values
    request.setAttribute("DEFAULT_POSITION", String.valueOf(CustomReportGeneratorConstants.CHART_POSITION_START));
    request.setAttribute("DEFAULT_ORIENTATION", String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_VERTICAL));

    //constant messages
    request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
    request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
    request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
%>

<app2:jScriptUrl url="/reports/Report/Chart/LoadType.do?reportId=${param.reportId}" var="jsAjaxUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="type" value="chartType"/>
    <app2:jScriptUrlParam param="orientation" value="orientation"/>
    <app2:jScriptUrlParam param="onlyOrientation" value="onlyOrient"/>
    <app2:jScriptUrlParam param="date" value="dateTime"/>
</app2:jScriptUrl>

<script>
    var requestCount = 0;

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

    function emptyShowView() {
        setAllUnSelected(lib_getObj("dto(serieId)"));
        setAllUnSelected(lib_getObj("dto(valueXId)"));
        setAllUnSelected(lib_getObj("dto(valueYId)"));
        setAllUnSelected(lib_getObj("dto(valueZId)"));
        setAllUnSelected(lib_getObj("dto(categoryId)"));

        lib_getObj("dto(axisXLabel)").value = "";
        lib_getObj("dto(axisYLabel)").value = "";
    }

    function getSelectedValue(box) {
        var res = "";
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].selected) {
                res = box.options[i].value;
            }
        }
        return res;
    }

    function getSelectedLabel(box) {
        var res = "";
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].selected) {
                res = box.options[i].text;
            }
        }
        return res;
    }

    function configurationDisplayNone() {
        lib_getObj("trConf").style.display = 'none';
        lib_getObj("trSerie").style.display = 'none';
        lib_getObj("trXValue").style.display = 'none';
        lib_getObj("trYValue").style.display = 'none';
        lib_getObj("trZValue").style.display = 'none';
        lib_getObj("trCategory").style.display = 'none';
        lib_getObj("trXLabel").style.display = 'none';
        lib_getObj("trYLabel").style.display = 'none';
    }

    function showView(viewValue) {

        configurationDisplayNone();
        lib_getObj("trConf").style.display = '';

        lib_getObj("dto(orientation)").disabled = false;

        if (existThisKey(viewValue, '${KEY_SERIE}')) {
            lib_getObj("trSerie").style.display = '';
            lib_getObj("LSerie").innerHTML = getValueByKey(viewValue, '${KEY_SERIE}');
        }
        if (existThisKey(viewValue, '${KEY_XVALUE}')) {
            lib_getObj("trXValue").style.display = '';
            lib_getObj("LXValue").innerHTML = getValueByKey(viewValue, '${KEY_XVALUE}');
        }
        if (existThisKey(viewValue, '${KEY_YVALUE}')) {
            lib_getObj("trYValue").style.display = '';
            lib_getObj("LYValue").innerHTML = getValueByKey(viewValue, '${KEY_YVALUE}');
        }
        if (existThisKey(viewValue, '${KEY_ZVALUE}')) {
            lib_getObj("trZValue").style.display = '';
            lib_getObj("LZValue").innerHTML = getValueByKey(viewValue, '${KEY_ZVALUE}');
        }
        if (existThisKey(viewValue, '${KEY_CATEGORY}')) {
            lib_getObj("trCategory").style.display = '';
            lib_getObj("LCategory").innerHTML = getValueByKey(viewValue, '${KEY_CATEGORY}');
        }
        if (existThisKey(viewValue, '${KEY_XLABEL}')) {
            lib_getObj("trXLabel").style.display = '';
            lib_getObj("LXLabel").innerHTML = getValueByKey(viewValue, '${KEY_XLABEL}');
        }
        if (existThisKey(viewValue, '${KEY_YLABEL}')) {
            lib_getObj("trYLabel").style.display = '';
            lib_getObj("LYLabel").innerHTML = getValueByKey(viewValue, '${KEY_YLABEL}');
        }
        if (existThisKey(viewValue, '${KEY_NONEORIENTATION}')) {
            lib_getObj("dto(orientation)").disabled = true;
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

    function setAllUnSelected(box) {
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].selected) {
                box.options[i].selected = false;
            }
        }
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

        if ('${KEY_EMPTY}' != opArray[0]) {
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
    }

    /*clean all options*/
    function cleanSelectOption(opBox) {
        for (var i = 0; i < opBox.options.length; i++) {
            opBox.options[i].value = "";
            opBox.options[i].text = "";
        }
        opBox.options.length = 0;
    }

    function typeSelected() {
        var typeBox = lib_getObj("dto(chartType)");
        var orientBox = lib_getObj("dto(orientation)");
        var typeValue = getSelectedValue(typeBox);

        if (typeValue != "") {
            //clean data
            emptyShowView();
            doSomething(typeValue, getSelectedValue(orientBox), false);
        } else {
            emptyShowView();
            configurationDisplayNone();
        }
    }

    function orientaionSelected() {
        var typeBox = lib_getObj("dto(chartType)");
        var orientBox = lib_getObj("dto(orientation)");
        var typeValue = getSelectedValue(typeBox);
        var orientValue = getSelectedValue(orientBox);

        if (orientValue != "" && typeValue != "") {
            //ajax call
            doSomething(typeValue, orientValue, true);
        }
    }

    function setXYLabels(propertyName) {
        var selectBox = lib_getObj(propertyName);
        var label = "";
        var isXLabel = null;
        var orientation = getSelectedValue(lib_getObj("dto(orientation)"));
        var isDefaultOrient = (orientation == '${DEFAULT_ORIENTATION}');

        if (selectBox != null)
            label = parserXYLabel(getSelectedLabel(selectBox));

        if (propertyName == "dto(categoryId)") {
            isXLabel = isDefaultOrient;
        } else if (propertyName == "dto(valueXId)") {
            isXLabel = isDefaultOrient;
        } else if (propertyName == "dto(valueYId)") {
            isXLabel = !(isDefaultOrient);
        }

        //set label value
        if (isXLabel != null) {
            if (isXLabel) {
                lib_getObj("dto(axisXLabel)").value = label;
            } else {
                lib_getObj("dto(axisYLabel)").value = label;
            }
        }
    }

    function parserXYLabel(value) {
        var res = value;
        var separator = value.indexOf(":");
        if (separator != -1) {
            res = value.substring(separator + 1);
        }
        return res;
    }

    function showToolTip(message) {
        var msgDiv = document.getElementById("msgId");
        msgDiv.innerHTML = unescape(message);
        msgDiv.style.visibility = "visible";
    }

    function hideToolTip() {
        var msgDiv = document.getElementById("msgId");
        requestCount--;
        if (requestCount <= 0) {
            msgDiv.style.visibility = "hidden";
            requestCount = 0;
        } else {
            showToolTip('${LOADING}');
        }
    }

    /*ajax management*/
    function doSomething(chartType, orientation, onlyOrientation) {
        var dateTime = new Date().getTime();
        var onlyOrient = "";

        if (onlyOrientation) {
            onlyOrient = "true";
        }
        if (chartType != "") {
            //hidden configuration view
            configurationDisplayNone();
            lib_getObj("trConf").style.display = '';
            showToolTip('${LOADING}');

            var url = ${jsAjaxUrl};
            requestCount++;
            makeHttpRequest(url, 'ajaxReadXmlType', true, 'ajaxErrorProcess');
        }
    }

    function ajaxReadXmlType(xmldoc) {

        //hide tooltip, this method may be find in TreeColumn.jsp
        hideToolTip();

        if (xmldoc != null && xmldoc != undefined) {
            var chart = xmldoc.getElementsByTagName('chart');
            if (chart.length == 0) return false;
        } else {
            return false;
        }

        //report fail
        if (xmldoc.getElementsByTagName('reportfail').item(0) != null) {
            <c:url var="reportList" value="Report/List/AddError.do"/>
            window.location = "${reportList}";
            return false;
        }

        var viewValue = unescape(xmldoc.getElementsByTagName('view').item(0).firstChild.nodeValue);
        var totalizers = null;
        var columnGroups = null;
        var timeGroup = null;

        if (xmldoc.getElementsByTagName('totalize').item(0) != null) {
            totalizers = unescape(xmldoc.getElementsByTagName('totalize').item(0).firstChild.nodeValue);
            columnGroups = unescape(xmldoc.getElementsByTagName('columngroup').item(0).firstChild.nodeValue);

            if (xmldoc.getElementsByTagName('timegroup').item(0) != null) {
                timeGroup = unescape(xmldoc.getElementsByTagName('timegroup').item(0).firstChild.nodeValue);
            }

            //clean data
            emptyShowView();

            //set data
            putSelectData(columnGroups, "dto(serieId)", null, true);
            putSelectData(totalizers, "dto(valueXId)", null, true);
            putSelectData(totalizers, "dto(valueYId)", null, true);
            putSelectData(totalizers, "dto(valueZId)", null, true);

            if (timeGroup != null) {
                putSelectData(timeGroup, "dto(categoryId)", null, true);
            } else {
                putSelectData(columnGroups, "dto(categoryId)", null, true);
            }
        }

        showView(viewValue);
    }

    /*server response error process*/
    function ajaxErrorProcess(requestStatusCode) {
        requestCount--;
        if (requestStatusCode == 404) { //session expired http request status code
            showToolTip('${EXPIRED}');
        } else {
            showToolTip('${ERROR}');
        }
    }
</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/ChartType.do" focus="dto(title)" styleClass="form-horizontal">

        <html:hidden property="dto(reportId)" value="${param.reportId}"/>
        <html:hidden property="dto(version)"/>

        <c:set var="op" value="create"/>
        <c:if test="${not empty chartForm.dtoMap['chartId']}">
            <c:set var="op" value="update"/>
            <html:hidden property="dto(chartId)"/>
        </c:if>
        <html:hidden property="dto(op)" value="${op}"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="CHART" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="1">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>
            <c:if test="${op == 'update'}">
                <app2:securitySubmit property="dto(delete)" operation="UPDATE" functionality="CHART"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <fmt:message key="Report.ChartType"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Report.chart.enable"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(isEnable)" styleId="isEnable_id" tabindex="1"/>
                                    <label for="isEnable_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Report.chart.showOnlyChart"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(showOnlyChart)" styleId="showOnlyChart_id"
                                                   tabindex="2"/>
                                    <label for="showOnlyChart_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                            <fmt:message key="Report.chart.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <app:text property="dto(title)" styleId="title_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="50" tabindex="3"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="position_id">
                            <fmt:message key="Report.chart.position"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <c:if test="${empty chartForm.dtoMap['position']}">
                                <!--default position value-->
                                <c:set target="${chartForm.dtoMap}" property="position" value="${DEFAULT_POSITION}"/>
                            </c:if>

                            <c:set var="positionList" value="${app2:getChartPositionList(pageContext.request)}"/>
                            <html:select property="dto(position)" styleId="position_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="4">
                                <html:options collection="positionList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="chartType_id">
                            <fmt:message key="Report.Chart.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <c:set var="typeList" value="${app2:getChartTypeList(pageContext.request)}"/>
                            <html:select property="dto(chartType)" styleId="chartType_id"
                                         styleClass="${app2:getFormSelectClasses()} middleSelect"
                                         onchange="javascript:typeSelected()" onkeyup="javascript:typeSelected()"
                                         tabindex="5">
                                <html:option value=""/>
                                <html:options collection="typeList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" id="tdOrient1" for="orientation_id">
                            <fmt:message key="Report.chart.orientation"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}" id="tdOrient2">
                            <c:if test="${empty chartForm.dtoMap['orientation']}">
                                <!--default orientation value-->
                                <c:set target="${chartForm.dtoMap}" property="orientation"
                                       value="${DEFAULT_ORIENTATION}"/>
                            </c:if>

                            <c:set var="orientationList" value="${app2:getChartOrientationList(pageContext.request)}"/>
                            <html:select property="dto(orientation)" styleId="orientation_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         onchange="javascript:orientaionSelected()"
                                         onkeyup="javascript:orientaionSelected()" tabindex="6">
                                <html:options collection="orientationList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                    <%--display configuration--%>
                <div id="trConf" style="display:none;">
                    <legend class="title">
                        <fmt:message key="Report.chart.configuration"/>
                        <div style="position:relative;">
                            <div id="msgId" class="messageToolTip"
                                 style="visibility:hidden; position:absolute; top:2px; left:-5px">
                                <fmt:message key="Common.message.loading"/>
                            </div>
                        </div>
                    </legend>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <div id="trSerie" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LSerie" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="serieId_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <c:choose>
                                        <c:when test="${totalizeList != null}">
                                            <c:set var="listSerie"
                                                   value="${app2:getChartColumnGroupsWithResource(columnList,null,pageContext.request)}"/>
                                            <c:set var="listTotalize"
                                                   value="${app2:getChartTotalizersWithResource(totalizeList,pageContext.request)}"/>
                                            <c:set var="listCategory"
                                                   value="${app2:getChartColumnGroupsWithResource(columnList,chartForm.dtoMap['chartType'],pageContext.request)}"/>
                                        </c:when>
                                    </c:choose>
                                    <html:select property="dto(serieId)" styleId="serieId_id"
                                                 styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="7">
                                        <html:option value=""/>
                                        <html:options collection="listSerie" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trXValue" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LXValue" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="valueXId_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <html:select property="dto(valueXId)" styleId="valueXId_id"
                                                 styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="8"
                                                 onchange="javascript:setXYLabels('dto(valueXId)')"
                                                 onkeyup="javascript:setXYLabels('dto(valueXId)')">
                                        <html:option value=""/>
                                        <html:options collection="listTotalize" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trYValue" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LYValue" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="valueYId_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <html:select property="dto(valueYId)" styleId="valueYId_id"
                                                 styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="9"
                                                 onchange="javascript:setXYLabels('dto(valueYId)')"
                                                 onkeyup="javascript:setXYLabels('dto(valueYId)')">
                                        <html:option value=""/>
                                        <html:options collection="listTotalize" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trZValue" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LZValue" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="valueZId_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <html:select property="dto(valueZId)" styleId="valueZId_id"
                                                 styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="10">
                                        <html:option value=""/>
                                        <html:options collection="listTotalize" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trCategory" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LCategory" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="categoryId_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <html:select property="dto(categoryId)" styleId="categoryId_id"
                                                 styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="11"
                                                 onchange="javascript:setXYLabels('dto(categoryId)')"
                                                 onkeyup="javascript:setXYLabels('dto(categoryId)')">
                                        <html:option value=""/>
                                        <html:options collection="listCategory" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trXLabel" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LXLabel" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="axisXLabel_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <app:text property="dto(axisXLabel)" styleId="axisXLabel_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="50"
                                              tabindex="12"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div id="trYLabel" style="display:none;">
                            <div class="wrapperSearch">
                                <label id="LYLabel" class="${app2:getFormLabelClassesTwoColumns()}"
                                       for="axisYLabel_id"></label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <app:text property="dto(axisYLabel)" styleId="axisYLabel_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="50"
                                              tabindex="13"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="CHART" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="15">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>
            <c:if test="${op == 'update'}">
                <app2:securitySubmit property="dto(delete)" operation="UPDATE" functionality="CHART"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="16">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
        </div>
    </html:form>
</div>
<c:if test="${not empty chartForm.dtoMap['chartType']}">
    <script language="JavaScript" type="text/javascript">
        <!--
        <c:set var="type" value="${chartForm.dtoMap['chartType']}"/>
        <c:set var="orientation" value="${chartForm.dtoMap['orientation']}"/>
        <c:set var="chartTypeViewValue" value="${app2:composeChartTypeView(type,orientation,pageContext.request)}"/>

        configurationDisplayNone();
        showView('${chartTypeViewValue}');
        //-->
    </script>
</c:if>

<tags:jQueryValidation formName="chartForm"/>


