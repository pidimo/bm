<%@ include file="/Includes.jsp" %>

<%--tab header--%>
<c:set var="tabHeaderLabel" value="Common.company" scope="request"/>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightCompanyList" module="/admin" patron="0 (1)" columnOrder="companyName,login">
        <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
    </fanta:label>
</c:set>


<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <app2:checkAccessRight functionality="COMPANYPREFERENCES" permission="VIEW">
            <c:set target="${tabItems}" property="Common.preferences"
                   value="/Company/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&dto(addressId)=${sessionScope.user.valueMap['companyId']}&flagCompany=true&isLogoCompany=null"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="COMPANYINTERFACE" permission="VIEW">
            <c:set var="listSect" value="${app2:getSectionListOfXmlFile(pageContext.request)}"/>
            <c:set var="firstSection" value="${listSect[0].value}"/>
            <c:set target="${tabItems}" property="UIManager.Styles"
                   value="/UIManager/Forward/CompanyStyleConfigurable.do?paramSection=${app2:encode(firstSection)}"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="COMPANYLOGO" permission="VIEW">
            <c:set target="${tabItems}" property="UIManager.Logo"
                   value="/CompanyLogo/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&dto(addressId)=${sessionScope.user.valueMap['companyId']}&flagCompany=true&isLogoCompany=true"/>
        </app2:checkAccessRight>
    </c:when>
</c:choose>

