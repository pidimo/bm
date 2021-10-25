<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="entered_ProjectStatus" value="<%=ProjectConstants.ProjectStatus.ENTERED.getAsString()%>"/>
<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Project.Title.search"/>
            <br>
        </td>
    </tr>
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/Project/List.do" focus="parameter(projectName)">
            <td class="contain" nowrap>
                <html:text property="parameter(projectName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </html:form>
    </TR>
    <TR>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Project/List.do" parameterName="projectNameAlpha"/>
        </td>
    </TR>
    <TR>
        <td colspan="2" align="center">
            <br/>
            <app2:checkAccessRight functionality="PROJECT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Project/Forward/Create.do?dto(status)=${entered_ProjectStatus}"
                                 addModuleParams="false" var="newProjectProcessUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newProjectProcessUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            
            <c:out value="${newButtonsTable}" escapeXml="false"/>

            <fmt:message var="numberFormat" key="numberFormat.1DecimalPlaces"/>
            <fmt:message var="datePattern" key="datePattern"/>
            <fanta:table list="projectList" width="100%" id="project" action="Project/List.do"
                         imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="Project/Forward/Update.do?projectId=${project.projectId}&dto(projectId)=${project.projectId}&dto(name)=${app2:encode(project.projectName)}&index=0"/>
                <c:set var="deleteLink"
                       value="Project/Forward/Delete.do?projectId=${project.projectId}&dto(withReferences)=true&dto(projectId)=${project.projectId}&dto(name)=${app2:encode(project.projectName)}&index=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PROJECT" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PROJECT" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="projectName" action="${editLink}" styleClass="listItem"
                                  title="Project.name" headerStyle="listHeader" width="20%"
                                  orderable="true"/>
                <fanta:dataColumn name="customerName" styleClass="listItem" title="Project.customer"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Project.responsible"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
                <fanta:dataColumn name="startDate" styleClass="listItem" title="Project.startDate"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false"
                                  nowrap="nowrap">
                    <fmt:formatDate value="${app2:intToDate(project.startDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="endDate" styleClass="listItem" title="Project.endDate"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false"
                                  nowrap="nowrap">
                    <fmt:formatDate value="${app2:intToDate(project.endDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="plannedInvoice" styleClass="listItemRight" title="Project.plannedInvoice"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatNumber value="${project.plannedInvoice}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="totalInvoice" styleClass="listItemRight" title="Project.totalInvoice"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatNumber value="${project.totalInvoice}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="plannedNoInvoice" styleClass="listItemRight" title="Project.plannedNoInvoice"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatNumber value="${project.plannedNoInvoice}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="totalNoInvoice" styleClass="listItemRight" title="Project.totalNoInvoice"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatNumber value="${project.totalNoInvoice}" type="number" pattern="${numberFormat}"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="status" styleClass="listItem2" title="Project.status"
                                  headerStyle="listHeader" width="10%" orderable="false" renderData="false">
                    ${app2:getProjectStatusLabel(pageContext.request, project.status)}
                </fanta:dataColumn>
            </fanta:table>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </TD>
    </tr>
</table>

