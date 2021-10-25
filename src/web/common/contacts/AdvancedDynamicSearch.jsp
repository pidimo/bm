<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<c:choose>
    <c:when test="${'contactSearchName' eq fieldAlias}">
        <html:text property="parameter(contactSearchName)" styleClass="largeText"/>
    </c:when>
    <c:when test="${'countryId' eq fieldAlias}">
        <fanta:select property="parameter(countryId)" listName="countryBasicList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="mediumSelect">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </c:when>
    <c:when test="${'code' eq fieldAlias}">
        <html:select property="parameter(code)" styleClass="mediumSelect">
            <html:option value=""/>
            <html:options collection="contactTypeList" property="value" labelProperty="label"/>
        </html:select>
    </c:when>
    <c:when test="${'active' eq fieldAlias}">
        <html:select property="parameter(active)" styleClass="mediumSelect">
            <html:options collection="activeList" property="value" labelProperty="label"/>
        </html:select>
    </c:when>
    <c:when test="${'addressTypeA1' eq fieldAlias}">
        <html:select property="parameter(addressTypeA1)" styleClass="mediumSelect">
            <html:option value=""/>
            <html:options collection="addressTypeList" property="value" labelProperty="label"/>
        </html:select>
    </c:when>
    <c:when test="${'employeeId' eq fieldAlias}">
        <fanta:select property="parameter(employeeId)" listName="employeeBaseList"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                      module="/contacts" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </c:when>
    <c:when test="${'customerTypeId' eq fieldAlias}">
        <app:catalogSelect property="parameter(customerTypeId)" catalogTable="customertype" idColumn="customertypeid"
                           labelColumn="customertypename" styleClass="mediumSelect"/>
    </c:when>
    <c:when test="${'priorityId' eq fieldAlias}">
        <fanta:select property="parameter(priorityId)" listName="priorityList"
                      labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                      module="/catalogs" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="CUSTOMER"/>
        </fanta:select>
    </c:when>
    <c:when test="${'branchId' eq fieldAlias}">
        <app:catalogSelect property="parameter(branchId)" catalogTable="branch" idColumn="branchid"
                           labelColumn="branchname" styleClass="mediumSelect"/>
    </c:when>
    <c:when test="${'sourceId' eq fieldAlias}">
        <app:catalogSelect property="parameter(sourceId)" catalogTable="addresssource" idColumn="sourceid"
                           labelColumn="sourcename" styleClass="mediumSelect"/>
    </c:when>
    <c:when test="${'payConditionId' eq fieldAlias}">
        <app:catalogSelect property="parameter(payConditionId)" catalogTable="paycondition" idColumn="payconditionid"
                           labelColumn="conditionname" styleClass="mediumSelect"/>
    </c:when>
    <c:when test="${'payMoralityId' eq fieldAlias}">
        <app:catalogSelect property="parameter(payMoralityId)" catalogTable="paymorality" idColumn="paymoralityid"
                           labelColumn="paymoralityname" styleClass="mediumSelect"/>
    </c:when>
    <c:when test="${'partnerId' eq fieldAlias}">
        <html:hidden property="parameter(partnerId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(partnerName)" styleClass="mediumText" maxlength="40" readonly="true"
                  styleId="fieldAddressName_id"/>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchPartner" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
    </c:when>
    <c:when test="${'departmentId' eq fieldAlias}">
        <fanta:select property="parameter(departmentId)" listName="departmentBaseList" firstEmpty="true" labelProperty="name"
                      valueProperty="departmentId" styleClass="mediumSelect">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </c:when>
    <c:when test="${'personTypeId' eq fieldAlias}">
        <fanta:select property="parameter(personTypeId)" listName="personTypeList" firstEmpty="true" labelProperty="name"
                      module="/catalogs" valueProperty="id" styleClass="mediumSelect">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </c:when>
</c:choose>
