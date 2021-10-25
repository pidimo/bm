<%@ page import="com.jatun.titus.listgenerator.Titus,
                 com.jatun.titus.listgenerator.structure.Module,
                 com.jatun.titus.listgenerator.structure.StructureManager,
                 com.jatun.titus.listgenerator.structure.Table,
                 com.piramide.elwis.utils.ReportConstants" %>
<%@ page import="com.piramide.elwis.utils.SortUtils"%>
<%@ page import="java.util.*" %>
<%@ include file="/Includes.jsp"%>
<c:set var="reportTypeList" value="<%=JSPHelper.getReportType(request)%>"/>
<c:set var="actualModule" value="${reportJrxmlForm.dtoMap['module']}" scope="request"/>
<c:set var="SOURCETYPE_JRXML" value="<%=ReportConstants.SourceType.JRXML.getConstantAsString()%>"/>


<%
    String actualModule = (String) request.getAttribute("actualModule");
    StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
    Iterator it = structureManager.getModulesIterator();

    List moduleList = new ArrayList();
    List tablesList = new ArrayList();
    while (it.hasNext()) {
        Map moduleMap = new HashMap();

        Module module = (Module) it.next();

        moduleMap.put("name", module.getName());
        moduleMap.put("resource", JSPHelper.getMessage(request,module.getResource()));

        moduleList.add(moduleMap);

        if (module.getName().equals(actualModule)) {
            Iterator tIt = module.getTableNamesIterator();
            while (tIt.hasNext()) {
                Map tableMap = new HashMap();

                String tableName = (String) tIt.next();

                Table table = structureManager.getTable(tableName);
                tableMap.put("name", table.getName());
                tableMap.put("resource", JSPHelper.getMessage(request, table.getResource()));
                tablesList.add(tableMap);
            }
        }
    }


    request.setAttribute("modules", SortUtils.orderByPropertyMap(moduleList,"resource"));
    request.setAttribute("tables", SortUtils.orderByPropertyMap(tablesList,"resource"));

    List reportStatusSelect = JSPHelper.getReportStatus(request);
    request.setAttribute("reportStatusSelect", reportStatusSelect);

%>


<script language="JavaScript">
    <!--
    function submitForm(obj){
        if (obj.options[obj.selectedIndex].value != "") {
            document.getElementById('tableId').selectedIndex = 0;
            document.forms[0].submit();
        }
    }

    function companySubmitForm(){
        document.getElementById('changeCompany_id').value = 'true';
        document.forms[0].submit();
    }
    //-->
</script>


<html:form action="${action}" enctype="multipart/form-data" focus="dto(name)">

    <html:hidden property="dto(sourceType)" value="${SOURCETYPE_JRXML}"/>
    <html:hidden property="dto(isChangeCompany)" value="false" styleId="changeCompany_id"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${op=='update' || op=='delete'}">
        <html:hidden property="dto(reportId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>


<html:hidden property="dto(formMsgCounter)" value="${msgCounter}"/>

<c:if test="${haveConfirmate == true}">
    <html:hidden property="dto(haveConfirmate)" value="${haveConfirmate}"/>
    <fmt:message key="Common.saveAnyway" var="button" scope="request"/>
</c:if>
<c:if test="${null == haveConfirmate || false == haveConfirmate}">
    <html:hidden property="dto(haveConfirmate)" value="false"/>
</c:if>

<c:if test="${haveChangeType == true}">
    <html:hidden property="dto(haveChangeType)" value="true"/>
</c:if>
<c:if test="${haveChangeColumn == true}">
    <html:hidden property="dto(haveChangeColumn)" value="true"/>
</c:if>


<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <c:if test="${not empty reportJrxmlButtonsPath}">
                <c:set var="buttonsTabIndex" value="1" scope="request"/>
                <c:import url="${reportJrxmlButtonsPath}"/>
            </c:if>

            <html:cancel styleClass="button" tabindex="3"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>

<table id="Report.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
    <TR>
        <TD colspan="4" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label" width="20%">
            <fmt:message key="Reports.report.reportName"/>
        </TD>
        <TD class="contain" width="30%">
            <app:text property="dto(name)" styleClass="mediumText" maxlength="150" view="${isReadOnly or op == 'delete'}"
                      tabindex="4"/>
        </TD>
        <TD class="label" width="20%">
            <fmt:message key="Reports.report.reportStatus"/>
        </TD>
        <TD class="contain" width="30%">
            <html:select property="dto(state)" styleClass="mediumSelect" tabindex="9" readonly="${isReadOnly or op=='delete'}">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="reportStatusSelect" property="value" labelProperty="label"/>
            </html:select>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Reports.report.reportModule"/>
        </TD>
        <TD class="contain">
            <html:select property="dto(module)" styleClass="mediumSelect" tabindex="6" readonly="${isReadOnly or op=='delete'}"
                         onchange="submitForm(this)">
                <html:option value=""></html:option>
                <c:forEach var="item" items="${modules}">
                    <html:option value="${item.name}">${item.resource}</html:option>
                </c:forEach>
            </html:select>
        </TD>
        <td class="label">
            <fmt:message key="ReportGenerator.reportFormat"/>
        </td>
        <td class="contain">
            <c:set var="reportFormats" value="${app2:getReportFormats(pageContext.request)}" scope="request"/>
            <html:select property="dto(reportFormat)" styleId="format" styleClass="mediumSelect" tabindex="12" readonly="${isReadOnly or op=='delete'}">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="reportFormats" property="reportFormat" labelProperty="reportFormatName"/>
            </html:select>
        </td>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Reports.report.reportFunctionality"/>
        </TD>
        <TD class="contain">
            <html:select property="dto(initialTableReference)" styleClass="mediumSelect" tabindex="7"
                         readonly="${isReadOnly or op=='delete'}" styleId="tableId">
                <html:option value=""></html:option>
                <c:forEach var="item" items="${tables}">
                    <html:option value="${item.name}">${item.resource}</html:option>
                </c:forEach>
            </html:select>
        </TD>
        <TD class="label">
            <fmt:message key="Report.jrxml.file"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(jrxmlFileName)" write="true"/>
            <br/>
            <c:if test="${op!='delete' && !isReadOnly}">
                <html:file property="dto(file)" accept=".jrxml" tabindex="13"/>
            </c:if>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Reports.report.employee"/></TD>
        <TD class="contain">
            <fanta:select property="dto(employeeId)" listName="employeeBaseList"
                          labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                          readOnly="${isReadOnly or op=='delete'}"
                          value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts" tabIndex="8">
                <fanta:parameter field="companyId" value="${not empty reportJrxmlForm.dtoMap['companyId'] ? reportJrxmlForm.dtoMap['companyId'] : -1}"/>
            </fanta:select>
        </TD>


        <c:choose>
            <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
                <TD class="label">
                    <fmt:message key="Report.jrxml.company"/>
                </TD>
                <TD class="contain">
                    <fanta:select property="dto(companyId)"
                                  listName="lightCompanyList"
                                  module="/admin"
                                  labelProperty="companyName"
                                  valueProperty="companyId"
                                  firstEmpty="true"
                                  styleClass="mediumSelect"
                                  onChange="companySubmitForm()"
                                  tabIndex="13"
                                  readOnly="${isReadOnly or op=='delete'}">
                        <fanta:parameter field="active" value="1"/>
                    </fanta:select>
                </TD>
            </c:when>
            <c:otherwise>
                <td class="contain" colspan="2">
                    <html:hidden property="dto(companyId)"/>
                    &nbsp;
                </td>
            </c:otherwise>
        </c:choose>
    </TR>
    <TR>
        <TD class="topLabel" colspan="4">
            <fmt:message key="Reports.report.reportDescription"/><br/>

            <html:textarea property="dto(descriptionText)" styleClass="middleDetail" readonly="${isReadOnly or op == 'delete'}"
                           tabindex="13" style="height:120px;width:99%;"/>
        </TD>
    </TR>


</table>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <c:if test="${not empty reportJrxmlButtonsPath}">
                <c:set var="buttonsTabIndex" value="14" scope="request"/>
                <c:import url="${reportJrxmlButtonsPath}"/>
            </c:if>

            <html:cancel styleClass="button" tabindex="16"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>
</html:form>