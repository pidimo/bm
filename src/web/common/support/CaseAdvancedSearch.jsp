<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<script>
    function myReset() {
        var form = document.caseListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<table border="0" cellpadding="3" cellspacing="0" width="100%" class="container" align="center">
<tr>
    <td class="title" colspan="4"><fmt:message key="Case.Title.advancedSearch"/></td>
</tr>
<fmt:message key="datePattern" var="datePattern"/>

<html:form action="/Case/AdvancedSearch.do?t=a" focus="parameter(number)">
<tr>
    <TD width="15%" class="label"><fmt:message key="Common.number"/></TD>
    <TD class="contain" width="35%">
        <html:text property="parameter(number)" styleClass="largeText" tabindex="1"/>
    </TD>
    <TD width="15%" class="label"><fmt:message key="Contact.title"/></TD>
    <TD width="35%" class="contain">
        <html:text property="parameter(contact_name1@_contact_name2@_contact_name3@_contact_searchName)"
                   styleClass="largeText" tabindex="8"/>
    </TD>
</tr>
<tr>
    <td class="label"><fmt:message key="Article.title"/></TD>
    <td class="contain">
        <html:text property="parameter(caseTitle)" styleClass="largeText" tabindex="2"/>
    </TD>
    <TD class="label">
        <fmt:message key="Article.productName"/>
    </td>
    <td class="contain">
        <html:text property="parameter(productName)" styleClass="largeText" tabindex="9"/>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Common.keywords"/></TD>
    <TD class="contain">
        <html:text property="parameter(keywords)" styleClass="largeText" tabindex="3"/>
    </TD>
    <TD class="label">
        <fmt:message key="Common.totalHours"/>
    </td>
    <TD class="contain">
        <html:text property="parameter(totalHours)" styleClass="numberText" tabindex="10"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="CaseType.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(caseTypeId)" listName="caseTypeList" labelProperty="name" tabIndex="4"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Common.openDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(openDateFrom)" maxlength="10" tabindex="11" styleId="openDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(openDateTo)" maxlength="10" tabindex="12" styleId="openDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Priority.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(priorityId)" listName="selectPriorityList" labelProperty="name" tabIndex="5"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SUPPORT"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Common.expireDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(expireDateFrom)" maxlength="10" tabindex="13" styleId="expireDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(expireDateTo)" maxlength="10" tabindex="14" styleId="expireDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="CaseSeverity.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(severityId)" listName="caseSeverityList" labelProperty="name" tabIndex="6"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Common.closeDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(closeDateFrom)" maxlength="10" tabindex="15" styleId="closeDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(closeDateTo)" maxlength="10" tabindex="16" styleId="closeDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="State.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(stateId)" listName="stateBaseList" labelProperty="name" tabIndex="7"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="1"/>
        </fanta:select>
    </TD>
    <TD colspan="2" class="label" style="text-align:right">
        <html:submit styleClass="button" tabindex="17"><fmt:message key="Common.go"/></html:submit>
        <html:button property="reset" tabindex="18" styleClass="button" onclick="myReset()">
            <fmt:message key="Common.clear"/></html:button>
    </TD>
</TR>
<!-- choose alphbet to simple and advanced search -->
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="Case/AdvancedSearch.do?t=a" parameterName="caseTitleAlpha"/>
    </td>
</tr>
</html:form>
<tr>
    <td colspan="4">
        <br>
        <app2:checkAccessRight functionality="CASE" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url value="/Case/Forward/Create.do?advancedListForward=CaseAdvancedSearch"
                             addModuleParams="false" var="newCaseUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newCaseUrl}'"/>
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
        <app2:checkAccessRight functionality="CASE" permission="DELETE" var="canDelete"/>
        <app2:checkAccessRight functionality="CASE" permission="VIEW" var="canUpdate"/>
        <fanta:table width="100%" id="case" action="Case/AdvancedSearch.do?t=a" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="${edit}?dto(caseTitle)=${app2:encode(case.caseTitle)}&caseId=${case.id}&index=0&advancedListForward=CaseAdvancedSearch"/>
            <c:set var="deleteAction"
                   value="${delete}?caseId=${case.id}&dto(caseTitle)=${app2:encode(case.caseTitle)}&index=0&advancedListForward=CaseAdvancedSearch"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <c:if test="${canUpdate}">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                </c:if>
                <c:if test="${canDelete && sessionScope.user.valueMap.userType!=0}">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        image="${baselayout}/img/delete.gif"/>
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

            <fanta:dataColumn name="toUser" styleClass="listItem" title="Common.assignedTo"
                              headerStyle="listHeader" width="10%" orderable="true"/>

            <fanta:dataColumn name="openByUser" styleClass="listItem" title="Common.openBy"
                              headerStyle="listHeader" width="10%" orderable="true"/>

            <fanta:dataColumn name="contact" styleClass="listItem" title="Contact.title"
                              headerStyle="listHeader" width="10%" orderable="true"/>

            <fanta:dataColumn name="productName" styleClass="listItem" title="Product.title"
                              headerStyle="listHeader" width="6%" orderable="true"/>

            <fanta:dataColumn name="openDate" styleClass="listItem" title="Common.openDate"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatDate value="${app2:intToDate(case.openDate)}" pattern="${datePattern}"/>
            </fanta:dataColumn>

            <fanta:dataColumn name="closeDate" styleClass="listItem2" title="Common.closeDate"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatDate value="${app2:intToDate(case.closeDate)}" pattern="${datePattern}"/>&nbsp;
            </fanta:dataColumn>

        </fanta:table>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </td>
</tr>
</table>
