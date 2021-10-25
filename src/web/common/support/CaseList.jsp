<%@ include file="/Includes.jsp" %>
<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="3" cellspacing="0" width="100%" class="container" align="center">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="SupportCase.title.search"/>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/${action}" focus="parameter(caseField)">
            <td class="contain">
                <html:text property="parameter(caseField)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                <c:if test="${sessionScope.user.valueMap.userType != 0}">
                    <html:link action="/Case/AdvancedSearch.do?t=a">&nbsp;<fmt:message key="Common.advancedSearch"/>
                    </html:link>
                </c:if>
            </td>
        </html:form>
    </tr>
    <!-- choose alphbet to simple and advanced search  -->
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="${action}" parameterName="caseTitleAlpha"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br>
            <app2:checkAccessRight functionality="CASE" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Case/Forward/Create.do"
                                 addModuleParams="false" var="newCaseUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newCaseUrl}'"/>
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fmt:message var="datePattern" key="datePattern"/>

            <app2:checkAccessRight functionality="CASE" permission="DELETE" var="canDelete"/>
            <app2:checkAccessRight functionality="CASE" permission="VIEW" var="canUpdate"/>

            <fanta:table width="100%" id="case" action="${action}" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="${edit}?dto(caseTitle)=${app2:encode(case.caseTitle)}&caseId=${case.id}&index=0"/>
                <c:set var="deleteAction"
                       value="${delete}?caseId=${case.id}&dto(caseTitle)=${app2:encode(case.caseTitle)}&index=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <c:if test="${canUpdate}">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                            headerStyle="listHeader" image="${baselayout}/img/edit.gif" width="50%"/>
                    </c:if>
                    <c:if test="${canDelete && sessionScope.user.valueMap.userType!=0}">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif" width="50%"/>
                    </c:if>
                </fanta:columnGroup>
                <fanta:dataColumn name="number" action="${editAction}" styleClass="listItem" title="Common.number"
                                  headerStyle="listHeader" width="4%" orderable="true"/>

                <fanta:dataColumn name="caseTitle" action="${editAction}" styleClass="listItem" title="Common.title"
                                  headerStyle="listHeader" width="16%" orderable="true" maxLength="30"/>

                <fanta:dataColumn name="typeName" styleClass="listItem" title="CaseType.title"
                                  headerStyle="listHeader" width="6%" orderable="true"/>

                <fanta:dataColumn name="priorityName" styleClass="listItem" title="Priority.title"
                                  headerStyle="listHeader" width="6%" orderable="true"/>

                <fanta:dataColumn name="severityName" styleClass="listItem" title="CaseSeverity.title"
                                  headerStyle="listHeader" width="6%" orderable="true"/>

                <fanta:dataColumn name="stateName" styleClass="listItem" title="State.title"
                                  headerStyle="listHeader" width="6%" orderable="true"/>

                <fanta:dataColumn name="openByUser" styleClass="listItem" title="Common.openBy"
                                  headerStyle="listHeader" width="13%" orderable="true"/>

                <fanta:dataColumn name="toUser" styleClass="listItem" title="Common.assignedTo"
                                  headerStyle="listHeader" width="13%" orderable="true"/>

                <fanta:dataColumn name="contact" styleClass="listItem" title="Contact.title"
                                  headerStyle="listHeader" width="12%" orderable="true"/>

                <fanta:dataColumn name="productName" styleClass="listItem" title="Product.title"
                                  headerStyle="listHeader" width="6%" orderable="true"/>

                <fanta:dataColumn name="openDate" styleClass="listItem2" title="Common.openDate"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(case.openDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
            </fanta:table>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </td>
    </tr>
</table>
