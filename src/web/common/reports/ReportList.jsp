<%@ page import="com.piramide.elwis.utils.ReportConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="REPORT_TYPE_SUMMARY" value="<%=com.piramide.elwis.utils.ReportConstants.SUMMARY_TYPE%>"/>
<c:set var="REPORT_TYPE_MATRIX" value="<%=com.piramide.elwis.utils.ReportConstants.MATRIX_TYPE%>"/>

<c:set var="SOURCETYPE_JRXML" value="<%=ReportConstants.SourceType.JRXML.getConstantAsString()%>"/>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <html:form action="/Report/List.do" focus="parameter(reportName)">
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
            <fanta:alphabet action="Report/List.do" parameterName="reportNameAlpha"/>
        </td>
    </tr>
</table>
<br/>

<TABLE id="ReportList.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center">
    <tr>
        <td>
            <app2:checkAccessRight functionality="REPORT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Report/Forward/Create.do"
                                 addModuleParams="false" var="newReportUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newReportUrl}'"/>
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fanta:table list="userReportList" width="100%" id="reports" action="Report/List.do"
                         imgPath="${baselayout}" align="center">

                <c:set var="isJrxmlSourceType" value="${reports.sourceType eq SOURCETYPE_JRXML}"/>

                <c:choose>
                    <c:when test="${isJrxmlSourceType}">
                        <c:set value="Report/Jrxml/Forward/Update.do?reportId=${reports.reportId}&dto(reportId)=${reports.reportId}&dto(name)=${app2:encode(reports.reportName)}&reportType=${reports.reportType}&index=0"
                               var="urlUpdate"/>
                    </c:when>
                    <c:otherwise>
                        <c:set value="Report/Forward/Update.do?reportId=${reports.reportId}&dto(reportId)=${reports.reportId}&dto(name)=${app2:encode(reports.reportName)}&reportType=${reports.reportType}&index=0"
                               var="urlUpdate"/>
                    </c:otherwise>
                </c:choose>

                <c:set value="Report/Forward/Delete.do?reportId=${reports.reportId}&dto(reportId)=${reports.reportId}&dto(name)=${app2:encode(reports.reportName)}&reportType=${reports.reportType}&index=0"
                       var="urlDelete"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="REPORT" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${urlUpdate}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="REPORT" permission="DELETE">
                        <c:choose>
                            <c:when test="${isJrxmlSourceType}">
                                <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader">
                                     &nbsp;
                                </fanta:actionColumn>
                            </c:when>
                            <c:otherwise>
                                <fanta:actionColumn name="delete" title="Common.delete" styleClass="listItem"
                                                    headerStyle="listHeader"
                                                    action="${urlDelete}"
                                                    image="${baselayout}/img/delete.gif"/>
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="reportName" action="${urlUpdate}"
                                  styleClass="listItem" title="Reports.report.reportName" headerStyle="listHeader"
                                  orderable="true" maxLength="30" width="25%"/>

                <fanta:dataColumn name="reportType" styleClass="listItem" title="Reports.report.reportType"
                                  headerStyle="listHeader" orderable="true" width="10%" renderData="${false}">

                    <c:choose>
                        <c:when test="${reports.reportType == REPORT_TYPE_SUMMARY}">
                            <fmt:message key="Report.type.summary"/>
                        </c:when>
                        <c:when test="${reports.reportType == REPORT_TYPE_MATRIX}">
                            <fmt:message key="Report.type.matrix"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="Report.type.single"/>
                        </c:otherwise>
                    </c:choose>

                </fanta:dataColumn>
                <fanta:dataColumn name="reportModule" styleClass="listItem" title="Reports.report.reportModule"
                                  headerStyle="listHeader" orderable="false" maxLength="30" width="20%"
                                  renderData="false">
                    <fmt:message key="${app2:getModuleResource(reports.reportModule, pageContext.request)}"/>

                </fanta:dataColumn>

                <fanta:dataColumn name="reportFunctionality" styleClass="listItem"
                                  title="Reports.report.reportFunctionality" headerStyle="listHeader" orderable="false"
                                  maxLength="30" width="15%" renderData="false">
                    <fmt:message
                            key="${app2:getFunctionalityResource(reports.reportFunctionality, pageContext.request)}"/>
                </fanta:dataColumn>

                <fanta:dataColumn name="sourceType" styleClass="listItem"
                                  title="Reports.report.sourceType" headerStyle="listHeader" orderable="true"
                                  maxLength="30" width="5%" renderData="false">

                    <c:choose>
                        <c:when test="${isJrxmlSourceType}">
                            <fmt:message key="Report.sourceType.jrxml"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="Report.sourceType.internal"/>
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>

                <fanta:dataColumn name="employeeName" styleClass="listItem2"
                                  title="Reports.report.employee" headerStyle="listHeader" orderable="true"
                                  maxLength="30" width="20%"/>
            </fanta:table>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </td>
    </tr>
</table>