<%@ include file="/Includes.jsp" %>

<c:set var="REPORT_TYPE_SUMMARY" value="<%=com.piramide.elwis.utils.ReportConstants.SUMMARY_TYPE%>"/>
<c:set var="REPORT_TYPE_MATRIX" value="<%=com.piramide.elwis.utils.ReportConstants.MATRIX_TYPE%>"/>

<html:form action="/Report/Jrxml/List.do" focus="parameter(reportName)" styleClass="form-horizontal">
    <fielset>
        <legend class="title">
            <fmt:message key="Reports.search"/>
        </legend>
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(reportName)" styleClass="${app2:getFormInputClasses()} largeText"
                               maxlength="40" tabindex="1"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </fielset>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Report/Jrxml/List.do" parameterName="reportNameAlpha" mode="bootstrap"/>
</div>

<div id="ReportList.jsp">
    <app2:checkAccessRight functionality="JRXMLREPORT" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <tags:buttonsTable>
                <app:url value="/Report/Jrxml/Forward/Create.do"
                         addModuleParams="false" var="newReportUrl"/>
                <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newReportUrl}'"/>
            </tags:buttonsTable>
        </c:set>
    </app2:checkAccessRight>
    <div class="${app2:getFormGroupClasses()}">
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="reportJrxmlList"
                     styleClass="${app2:getFantabulousTableClases()}" width="100%" id="reports"
                     action="Report/Jrxml/List.do"
                     imgPath="${baselayout}" align="center">

            <c:set value="Report/Jrxml/Forward/Update.do?reportId=${reports.reportId}&dto(reportId)=${reports.reportId}&dto(name)=${app2:encode(reports.reportName)}&reportType=${reports.reportType}&index=0"
                   var="urlUpdate"/>

            <c:set value="Report/Jrxml/Forward/Delete.do?reportId=${reports.reportId}&dto(reportId)=${reports.reportId}&dto(name)=${app2:encode(reports.reportName)}&reportType=${reports.reportType}&index=0"
                   var="urlDelete"/>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="JRXMLREPORT" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                        headerStyle="listHeader"
                                        action="${urlDelete}"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="reportName" action="${urlUpdate}"
                              styleClass="listItem" title="Reports.report.reportName" headerStyle="listHeader"
                              orderable="true" maxLength="30" width="30%"/>

            <fanta:dataColumn name="reportModule" styleClass="listItem" title="Reports.report.reportModule"
                              headerStyle="listHeader" orderable="false" maxLength="30" width="15%"
                              renderData="false">
                <fmt:message key="${app2:getModuleResource(reports.reportModule, pageContext.request)}"/>

            </fanta:dataColumn>

            <fanta:dataColumn name="reportFunctionality" styleClass="listItem"
                              title="Reports.report.reportFunctionality" headerStyle="listHeader" orderable="false"
                              maxLength="30" width="15%" renderData="false">
                <fmt:message
                        key="${app2:getFunctionalityResource(reports.reportFunctionality, pageContext.request)}"/>
            </fanta:dataColumn>

            <fanta:dataColumn name="companyName"
                              styleClass="listItem" title="Report.jrxml.company" headerStyle="listHeader"
                              orderable="true" maxLength="30" width="15%"/>


            <fanta:dataColumn name="employeeName" styleClass="listItem2"
                              title="Reports.report.employee" headerStyle="listHeader" orderable="true"
                              maxLength="30" width="20%"/>
        </fanta:table>
    </div>
    <div class="${app2:getFormGroupClasses()}">
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </div>
</div>