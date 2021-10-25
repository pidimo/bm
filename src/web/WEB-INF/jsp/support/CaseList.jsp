<%@ include file="/Includes.jsp" %>
<%@ include file="/Includes.jsp" %>

<legend class="title">
    <fmt:message key="SupportCase.title.search"/>
</legend>

<html:form action="/${action}" focus="parameter(caseField)" styleClass="form-horizontal">
    <fieldset>
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(caseField)" styleClass="largeText ${app2:getFormInputClasses()}"/>
                        <span class="input-group-btn">
                            <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                                    key="Common.go"/></html:submit>
                        </span>
                </div>
            </div>
            <c:if test="${sessionScope.user.valueMap.userType != 0}">
                <div class="pull-left">
                    <html:link action="/Case/AdvancedSearch.do?t=a" styleClass="btn btn-link">&nbsp;<fmt:message
                            key="Common.advancedSearch"/>
                    </html:link>
                </div>
            </c:if>
        </div>
    </fieldset>
</html:form>

<!-- choose alphbet to simple and advanced search -->

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="${action}" mode="bootstrap" parameterName="caseTitleAlpha"/>
</div>


<app2:checkAccessRight functionality="CASE" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/Case/Forward/Create.do"
                     addModuleParams="false" var="newCaseUrl"/>
            <input type="button" class="button ${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                   onclick="window.location ='${newCaseUrl}'"/>
        </div>

    </c:set>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>
<fmt:message var="datePattern" key="datePattern"/>

<app2:checkAccessRight functionality="CASE" permission="DELETE" var="canDelete"/>
<app2:checkAccessRight functionality="CASE" permission="VIEW" var="canUpdate"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%"
                 styleClass="${app2:getFantabulousTableLargeClases()}"
                 id="case"
                 action="${action}"
                 imgPath="${baselayout}">
        <c:set var="editAction"
               value="${edit}?dto(caseTitle)=${app2:encode(case.caseTitle)}&caseId=${case.id}&index=0"/>
        <c:set var="deleteAction"
               value="${delete}?caseId=${case.id}&dto(caseTitle)=${app2:encode(case.caseTitle)}&index=0"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <c:if test="${canUpdate}">
                <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}" width="50%"/>
            </c:if>
            <c:if test="${canDelete && sessionScope.user.valueMap.userType!=0}">
                <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}" width="50%"/>
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
</div>

<c:out value="${newButtonsTable}" escapeXml="false"/>
