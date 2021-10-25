<%@ page import="com.piramide.elwis.utils.ReportConstants"%>
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
    //show views
    request.setAttribute("ONEBOX", ReportConstants.SHOW_ONE_BOX);
    request.setAttribute("TWOBOX", ReportConstants.SHOW_TWO_BOX);
    request.setAttribute("ONESELECT", ReportConstants.SHOW_SELECT);
    request.setAttribute("MSELECT", ReportConstants.SHOW_MULTIPLESELECT);
    request.setAttribute("EMPTY", ReportConstants.SHOW_EMPTY);
%>


<script language="JavaScript">
<!--
//get object by id or name
function lib_getObj(id,d){
    var i,x;  if(!d) d=document;
    if(!(x=d[id])&&d.all) x=d.all[id];
    for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][id];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=lib_getObj(id,d.layers[i].document);
    if(!x && document.getElementById) x=document.getElementById(id);
    return x;
}

function putBasicData(columnLabel,column,columnType,table,path,jsNodeId){
    //lib_getObj("labelColumn").value = columnLabel;
    lib_getObj("labelColumn").innerHTML = columnLabel;
    lib_getObj("dto(columnReference)").value = column;
    lib_getObj("dto(columnType)").value = columnType;
    lib_getObj("dto(tableReference)").value = table;
    lib_getObj("dto(path)").value = path;
    lib_getObj("dto(jsNodeId)").value = jsNodeId;
}

function cleanBasicData(){
    //lib_getObj("labelColumn").value = columnLabel;
    lib_getObj("labelColumn").innerHTML = "";
    lib_getObj("dto(columnReference)").value = "";
    lib_getObj("dto(columnType)").value = "";
    lib_getObj("dto(tableReference)").value = "";
    lib_getObj("dto(path)").value = "";
    lib_getObj("dto(jsNodeId)").value = "";
}

function emptyShowView(){
    lib_getObj("dto(value)").value = "";
    lib_getObj("dto(value1)").value = "";
    lib_getObj("dto(value2)").value = "";
}

function putOperatorSelect(operatorData, selectedOp){
    //alert(selectedOp);
    var opBox = lib_getObj("dto(opSelect)");
    var opArray = operatorData.split('${KEY_SEPART}');
    var keyLabel = '${KEY_LABEL}';
    var keyValue = '${KEY_VALUE}';
    //clean options
    cleanOperatorSelect(opBox);

    for(var i=0; i<opArray.length; i++){
        var opt = new Option();
        opt.text = getValueByKey(opArray[i],keyLabel);
        opt.value = getValueByKey(opArray[i],keyValue);
        /*if(opt.value == selectedOp){
            opt.selected = true;
        }*/
        opBox.options[i] = opt;
    }

    //show view value
    showView(opBox);
}

/*clean all options*/
function cleanOperatorSelect(opBox){
    for(var i=0; i < opBox.options.length; i++) {
        opBox.options[i].value = "";
        opBox.options[i].text = "";
    }
    opBox.options.length = 0;
}

function showView(opBox){

    for(var i=0; i< opBox.options.length; i++) {
        if(opBox.options[i].selected && opBox.options[i].value != "") {
            var keyIsDateType = '${KEY_ISDATETYPE}';
            var keyView = '${KEY_SHOWVIEW}';
            var showView = getValueByKey(opBox.options[i].value, keyView);
            switch(showView){
                case '${ONEBOX}':
                    if(existThisKey(opBox.options[i].value, keyIsDateType)){
                        lib_getObj("oneBox").style.display = 'none';
                        lib_getObj("oneBoxDate").style.display = '';
                    }else{
                        lib_getObj("oneBox").style.display = '';
                        lib_getObj("oneBoxDate").style.display = 'none';
                    }

                    lib_getObj("twoBox").style.display = 'none';
                    lib_getObj("twoBoxDate").style.display = 'none';
                    lib_getObj("oneSelect").style.display = 'none';
                    lib_getObj("mSelect").style.display = 'none';
                    lib_getObj("viewValue").style.display = '';
                    break
                case '${TWOBOX}':
                    if(existThisKey(opBox.options[i].value, keyIsDateType)){
                        lib_getObj("twoBox").style.display = 'none';
                        lib_getObj("twoBoxDate").style.display = '';
                    }else{
                        lib_getObj("twoBox").style.display = '';
                        lib_getObj("twoBoxDate").style.display = 'none';
                    }
                    lib_getObj("oneBox").style.display = 'none';
                    lib_getObj("oneBoxDate").style.display = 'none';
                    lib_getObj("oneSelect").style.display = 'none';
                    lib_getObj("mSelect").style.display = 'none';
                    lib_getObj("viewValue").style.display = '';
                    break
                case '${ONESELECT}':
                    lib_getObj("oneBox").style.display = 'none';
                    lib_getObj("oneBoxDate").style.display = 'none';
                    lib_getObj("twoBox").style.display = 'none';
                    lib_getObj("twoBoxDate").style.display = 'none';
                    lib_getObj("oneSelect").style.display = '';
                    lib_getObj("mSelect").style.display = 'none';
                    lib_getObj("viewValue").style.display = '';
                    break
                case '${MSELECT}':
                    lib_getObj("oneBox").style.display = 'none';
                    lib_getObj("oneBoxDate").style.display = 'none';
                    lib_getObj("twoBox").style.display = 'none';
                    lib_getObj("twoBoxDate").style.display = 'none';
                    lib_getObj("oneSelect").style.display = 'none';
                    lib_getObj("mSelect").style.display = '';
                    lib_getObj("viewValue").style.display = '';
                    break
                case '${EMPTY}':
                    lib_getObj("oneBox").style.display = 'none';
                    lib_getObj("oneBoxDate").style.display = 'none';
                    lib_getObj("twoBox").style.display = 'none';
                    lib_getObj("twoBoxDate").style.display = 'none';
                    lib_getObj("oneSelect").style.display = 'none';
                    lib_getObj("mSelect").style.display = 'none';
                    lib_getObj("viewValue").style.display = 'none';
                    break
                default:
                    alert('blanck');
            }
        }
    }
}

function getValueByKey(text, key){
    var res=null;
    var firstPos = text.indexOf(key);
    var lastPos = text.lastIndexOf(key);
    if(firstPos != lastPos){
        res = text.substring(firstPos + key.length, lastPos);
    }
    ///alert(res);
    return res;
}

function existThisKey(text, key){
    var res = false;
    if(text.indexOf(key) != -1)
        res = true;
    return res;
}

/*ajax management*/
//call through ajax from TreeColumn.jsp
function ajaxReadXmlFilter(xmldoc) {

    var filter = xmldoc.getElementsByTagName('filter').item(0);

    var label = xmldoc.getElementsByTagName('label').item(0).firstChild.nodeValue;
    var column = xmldoc.getElementsByTagName('column').item(0).firstChild.nodeValue;
    var columnType = xmldoc.getElementsByTagName('columntype').item(0).firstChild.nodeValue;
    var table = xmldoc.getElementsByTagName('table').item(0).firstChild.nodeValue;
    var path = xmldoc.getElementsByTagName('path').item(0).firstChild.nodeValue;
    var operator = xmldoc.getElementsByTagName('operator').item(0).firstChild.nodeValue;
    var nodeId = xmldoc.getElementsByTagName('nodeid').item(0).firstChild.nodeValue;

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
    str += "jsNodeId:" + nodeId;

    var mydiv = document.getElementById("divView");
    mydiv.innerHTML = str;

    if(operator != '${KEY_NOT_OP}'){
        //set filter basic data
        putBasicData(label,column,columnType,table,path,nodeId);
        //set operator data
        putOperatorSelect(operator);
        //view filter
        lib_getObj("filterView").style.display = '';
    }else{
        cleanBasicData();
        //view filter
        lib_getObj("filterView").style.display = 'none';
    }
}
//-->
</script>




<html:form action="${action}">
    <html:hidden property="dto(reportId)" value="${param['reportId']}"/>

    <html:hidden property="dto(columnReference)"/>
    <html:hidden property="dto(columnType)"/>
    <html:hidden property="dto(tableReference)"/>
    <html:hidden property="dto(path)"/>
    <html:hidden property="dto(jsNodeId)"/>

    <!--get dtos from form-->
    <c:set var="tableRef" value="${filterCreateForm.dtoMap['tableReference']}"/>
    <c:set var="columnRef" value="${filterCreateForm.dtoMap['columnReference']}"/>
    <c:set var="operator" value="${filterCreateForm.dtoMap['operator']}"/>


<table id="FilterCreate.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td class="contain">
            <table>
                <tr>
                    <td>
                        <fmt:message key="Report.columns"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="overflow:scroll; width:400; height:250;">
                            <c:set var="ajaxCallJavaScript" value="ajaxReadXmlFilter" scope="request"/>
                            <c:set var="ajaxUrlExecute" value="/reports/Report/Filter/TreeColumns.do" scope="request"/>
                            <c:import url="/common/reports/TreeColumn.jsp"/>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
        <td class="containTop" width="60%">
            <c:choose>
                <c:when test="${ not empty errorJsNodeId}">
                    <c:set var="displayFilter" value=""/>
                </c:when>
                <c:otherwise>
                    <c:set var="displayFilter" value="display:none"/>
                </c:otherwise>
            </c:choose>
            <table id="filterView" style="${displayFilter}" border="0" cellpadding="0" cellspacing="0" width="100%" class="container">
                <tr>
                    <td colspan="2" align="left">
                        <fmt:message key="Report.filter"/>
                    </td>
                </tr>
                <tr>
                    <td class="label" width="20%">
                        <fmt:message key="Report.filter.field"/>
                    </td>
                    <td class="contain">
                        <div id="labelColumn">
                            <c:if test="${not empty tableRef && not empty columnRef}">
                                <%--<c:out value="${app2:composeColumnLabel(tableRef, columnRef, null, pageContext.request)}"/>--%>
                            </c:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <fmt:message key="Report.filter.operator"/>
                    </td>
                    <td class="contain">
                        <c:if test="${not empty tableRef && not empty columnRef}">
                            <c:set var="operatorValue" value="${app2:composeOperatorValue(operator, tableRef, columnRef, pageContext.request)}"/>
                        </c:if>

                        <html:select property="dto(opSelect)" styleId="idOpBox" value="${operatorValue}" styleClass="select" onchange="javascript:showView(this.form.idOpBox)" readonly="${op == 'delete'}">
                            <c:if test="${not empty tableRef && not empty columnRef}">
                                <c:forEach var="listOp" items="${app2:getReportFilterOperators(tableRef, columnRef, pageContext.request)}">
                                    <html:option value="${listOp.value}"><c:out value="${listOp.label}"/></html:option>
                                </c:forEach>
                            </c:if>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td id="viewValue" class="label">
                        <fmt:message key="Report.filter.value"/>
                    </td>
                    <td class="contain">
                        <table id="oneBox" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <app:text property="dto(value)" styleClass="shortText"/>
                                </td>
                            </tr>
                        </table>
                        <table id="twoBox" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <app:text property="dto(value1)" styleClass="shortText"/>
                                </td>
                                <td>
                                    &nbsp;
                                    <fmt:message key="Common.and"/>
                                    &nbsp;
                                </td>
                                <td>
                                    <app:text property="dto(value2)" styleClass="shortText"/>
                                </td>
                            </tr>
                        </table>
                        <table id="oneSelect" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <html:select property="dto(select1)" value="bbb" styleClass="shortSelect">
                                        <html:option value="equal">person</html:option>
                                    </html:select>
                                </td>
                            </tr>
                        </table>
                        <table id="mSelect" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <html:select property="dto(mSelect1)" value="bbb" styleClass="shortSelect" multiple="true">
                                        <html:option value="equal">person</html:option>
                                    </html:select>
                                </td>
                            </tr>
                        </table>
                        <fmt:message   var="datePattern" key="datePattern"/>
                        <table id="oneBoxDate" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <app:dateText property="dto(date)" styleId="date" calendarPicker="true" datePatternKey="${datePattern}" styleClass="shortText" view="${op == 'delete'}" maxlength="10"/>
                                </td>
                            </tr>
                        </table>
                        <table id="twoBoxDate" style="display:none;" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <app:dateText property="dto(date1)" styleId="date1" calendarPicker="true" datePatternKey="${datePattern}" styleClass="shortText" view="${op == 'delete'}" maxlength="10"/>
                                </td>
                                <td>
                                    &nbsp;
                                    <fmt:message key="Common.and"/>
                                    &nbsp;
                                </td>
                                <td>
                                    <app:dateText property="dto(date2)" styleId="date2" calendarPicker="true" datePatternKey="${datePattern}" styleClass="shortText" view="${op == 'delete'}" maxlength="10"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <tr>
        <td class="button">
            <html:submit styleClass="button" tabindex="10">${button}</html:submit>
<%--
            <app2:securitySubmit operation="${op}" functionality="WEBMAILFOLDER" styleClass="button" tabindex="2">
                <c:out value="${button}"/></app2:securitySubmit>
--%>
            <html:cancel styleClass="button" tabindex="11" ><fmt:message   key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>
</html:form>
<c:choose>
    <c:when test="${not empty dto.filterValue and op != 'delete'}">
        <script language="JavaScript">
        <!--
            var opBoxs = lib_getObj("idOpBox");
            showView(opBoxs,'${dto.filterValue}');
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