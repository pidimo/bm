<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>

<c:set var="addressMap" value="${app2:getAddressMap(auditAddressId)}"/>

<fieldset>
    <legend class="title">
        <fmt:message key="Contact.audit.information"/>
    </legend>
    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}">
                <fmt:message key="Contact.audit.createdBy"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <fanta:label var="createUserName"
                             listName="lightUserList"
                             module="/admin"
                             patron="0"
                             columnOrder="userName">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userId"
                                     value="${empty addressMap.recordUserId ? 0 : addressMap.recordUserId}"/>
                </fanta:label>
                <c:out value="${createUserName}"/>
            </div>
        </div>

        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}">
                <fmt:message key="Contact.audit.updatedBy"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <fanta:label var="updateUserName"
                             listName="lightUserList"
                             module="/admin"
                             patron="0"
                             columnOrder="userName">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userId"
                                     value="${empty addressMap.lastModificationUserId ? 0 : addressMap.lastModificationUserId}"/>
                </fanta:label>
                <c:out value="${updateUserName}"/>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}">
                <fmt:message key="Contact.audit.createdOn"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <fmt:formatDate var="createdDate"
                                value="${app2:intToDate(addressMap.recordDate)}"
                                pattern="${datePattern}"/>
                <c:out value="${createdDate}"/>
            </div>
        </div>

        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}">
                <fmt:message key="Contact.audit.updatedOn"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <fmt:formatDate var="updateDate"
                                value="${app2:intToDate(addressMap.lastModificationDate)}"
                                pattern="${datePattern}"/>
                <c:out value="${updateDate}"/>
            </div>
        </div>
    </div>
</fieldset>