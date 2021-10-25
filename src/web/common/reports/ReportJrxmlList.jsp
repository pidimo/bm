<%@ include file="/Includes.jsp"%>


<c:set var="REPORT_TYPE_SUMMARY" value="<%=com.piramide.elwis.utils.ReportConstants.SUMMARY_TYPE%>"/>
<c:set var="REPORT_TYPE_MATRIX" value="<%=com.piramide.elwis.utils.ReportConstants.MATRIX_TYPE%>"/>


<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <html:form action="/Report/Jrxml/List.do" focus="parameter(reportName)">
        <tr>
            <td height="20" class="title" colspan="2">
                <fmt:message key="Reports.search"/>
            </td>
        </tr>
        <TR>
            <td class="label"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain">
                <html:text property="parameter(reportName)" styleClass="largeText"
                           maxlength="40" tabindex="1"/>
                &nbsp;
                <html:submit styleClass="button" tabindex="3"><fmt:message key="Common.go"/></html:submit>
            </td>
        </TR>
    </html:form>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Report/Jrxml/List.do" parameterName="reportNameAlpha"/>
        </td>
    </tr>
</table>
<br/>

<TABLE id="ReportList.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center">
    <tr>
        <td>
            <app2:checkAccessRight functionality="JRXMLREPORT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Report/Jrxml/Forward/Create.do"
                                 addModuleParams="false" var="newReportUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newReportUrl}'"/>
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fanta:table list="reportJrxmlList" width="100%" id="reports" action="Report/Jrxml/List.do"
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
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="JRXMLREPORT" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                            headerStyle="listHeader"
                                            action="${urlDelete}"
                                            image="${baselayout}/img/delete.gif"/>
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
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </td>
    </tr>
</table>