<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>

<c:set var="addressMap" value="${app2:getAddressMap(auditAddressId)}"/>

<tr>
    <td colspan="4" class="title">
        <fmt:message key="Contact.audit.information"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contact.audit.createdBy"/>
    </td>
    <td class="contain">
        <fanta:label var="createUserName" listName="lightUserList" module="/admin" patron="0" columnOrder="userName" >
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="userId" value="${empty addressMap.recordUserId ? 0 : addressMap.recordUserId}" />
        </fanta:label>
        <c:out value="${createUserName}"/>
    </td>
    <td class="label">
        <fmt:message key="Contact.audit.updatedBy"/>
    </td>
    <td class="contain">
        <fanta:label var="updateUserName" listName="lightUserList" module="/admin" patron="0" columnOrder="userName" >
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="userId" value="${empty addressMap.lastModificationUserId ? 0 : addressMap.lastModificationUserId}" />
        </fanta:label>
        <c:out value="${updateUserName}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contact.audit.createdOn"/>
    </td>
    <td class="contain">
        <fmt:formatDate var="createdDate" value="${app2:intToDate(addressMap.recordDate)}" pattern="${datePattern}"/>
        <c:out value="${createdDate}"/>
    </td>
    <td class="label">
        <fmt:message key="Contact.audit.updatedOn"/>
    </td>
    <td class="contain">
        <fmt:formatDate var="updateDate" value="${app2:intToDate(addressMap.lastModificationDate)}" pattern="${datePattern}"/>
        <c:out value="${updateDate}"/>
    </td>
</tr>
