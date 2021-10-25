<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>


<c:set var="finished_projectStatus" value="<%=ProjectConstants.ProjectStatus.FINISHED.getAsString()%>"/>
<c:set var="invoiced_projectStatus" value="<%=ProjectConstants.ProjectStatus.INVOICED.getAsString()%>"/>
<c:set var="entered_status" value="<%=ProjectConstants.ProjectTimeStatus.ENTERED.getAsString()%>"/>
<c:set var="released_status" value="<%=ProjectConstants.ProjectTimeStatus.RELEASED.getAsString()%>"/>
<c:set var="confirmed_status" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="not_confirmed_status" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="sessionUserId" value="${sessionScope.user.valueMap['userId']}"/>
<c:set var="sessionUserAddressId" value="${sessionScope.user.valueMap['userAddressId']}"/>

<c:if test="${empty projectId}">
    <c:set var="projectId" value="${param.projectId}"/>
</c:if>

<c:if test="${empty operation}">
    <c:set var="operation" value="forwardToCreate"/>
</c:if>
<c:if test="${empty tabIndex}">
    <c:set var="tabIndex" value="20" scope="request"/>
</c:if>

<c:set var="canEditPage" value="${false}" scope="request"/>

<c:if test="${projectStatus != finished_projectStatus && projectStatus != invoiced_projectStatus}">

<c:if test="${'forwardToCreate' == operation && app2:hasProjectUserPermission(projectId,'NEW',pageContext.request)}">
    <html:form action="/ProjectTime/Forward/Create.do">
        <td class="button">
            <html:submit styleClass="button" property="newButton">
                <fmt:message key="Common.new"/>
            </html:submit>
        </td>
    </html:form>
</c:if>

<c:if test="${'create' == operation && app2:hasProjectUserPermission(projectId,'NEW',pageContext.request)}">
    <c:set var="canEditPage" value="${true}" scope="request"/>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         property="newButton"
                         styleClass="button"
                         tabindex="${tabIndex}">
        ${button}
    </app2:securitySubmit>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         styleClass="button"
                         property="SaveAndNew"
            tabindex="${tabIndex}">
        <fmt:message   key="Common.saveAndNew"/>
    </app2:securitySubmit>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         property="releaseButton"
                         styleClass="button"
                         tabindex="${tabIndex}">
        <fmt:message key="ProjectTime.button.release"/>
    </app2:securitySubmit>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         property="releaseAndNewButton"
                         styleClass="button"
                         tabindex="${tabIndex}">
        <fmt:message key="ProjectTime.button.releaseAndNew"/>
    </app2:securitySubmit>
</c:if>


<c:if test="${'update' == operation}">
<c:choose>
<c:when test="${entered_status == projectTimeStatus && (sessionUserId == userId || sessionUserAddressId == assigneeId)}">
    <c:set var="canEditPage" value="${true}" scope="request"/>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         property="editButton"
                         styleClass="button"
                         tabindex="${tabIndex}">
        ${button}
    </app2:securitySubmit>
    <app2:securitySubmit operation="${operation}"
                         functionality="PROJECTTIME"
                         property="releaseButton"
                         styleClass="button"
                         tabindex="${tabIndex+1}">
        <fmt:message key="ProjectTime.button.release"/>
    </app2:securitySubmit>
</c:when>
<c:when test="${released_status == projectTimeStatus}">
    <c:if test="${sessionUserAddressId == assigneeId || sessionUserId ==  userId || app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
        <c:set var="canEditPage" value="${true}" scope="request"/>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="editButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            ${button}
        </app2:securitySubmit>
    </c:if>
    <c:if test="${sessionUserAddressId == assigneeId || sessionUserId == userId}">
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="disableButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            <fmt:message key="ProjectTime.button.disable"/>
        </app2:securitySubmit>
    </c:if>
    <c:if test="${app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="confirmButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            <fmt:message key="ProjectTime.button.confirm"/>
        </app2:securitySubmit>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="noConfirmButton"
                             styleClass="button"
                             tabindex="${tabIndex+1}">
            <fmt:message key="ProjectTime.button.noConfirm"/>
        </app2:securitySubmit>
    </c:if>
</c:when>
<c:when test="${confirmed_status == projectTimeStatus}">
    <c:if test="${app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
        <c:set var="canEditPage" value="${true}" scope="request"/>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="editButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            ${button}
        </app2:securitySubmit>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="noConfirmButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            <fmt:message key="ProjectTime.button.noConfirm"/>
        </app2:securitySubmit>
    </c:if>
    <c:if test="${app2:hasProjectUserPermission(projectId,'ADMIN',pageContext.request) && 'true' == toBeInvoiced}">
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="invoiceButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            <fmt:message key="ProjectTime.button.invoice"/>
        </app2:securitySubmit>
    </c:if>
</c:when>
<c:when test="${not_confirmed_status == projectTimeStatus}">
    <c:if test="${sessionUserAddressId == assigneeId || sessionUserId == userId  || app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
        <c:set var="canEditPage" value="${true}" scope="request"/>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="editButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            ${button}
        </app2:securitySubmit>
    </c:if>
    <c:if test="${app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
        <c:set var="canEditPage" value="${true}" scope="request"/>
        <app2:securitySubmit operation="${operation}"
                             functionality="PROJECTTIME"
                             property="confirmButton"
                             styleClass="button"
                             tabindex="${tabIndex}">
            <fmt:message key="ProjectTime.button.confirm"/>
        </app2:securitySubmit>
    </c:if>
</c:when>
</c:choose>
</c:if>

<c:if test="${'delete' == operation}">
    <c:choose>
        <c:when test="${entered_status == projectTimeStatus && sessionUserId == userId}">
            <c:set var="canEditPage" value="${true}" scope="request"/>
            <app2:securitySubmit operation="${operation}"
                                 functionality="PROJECTTIME"
                                 property="editButton"
                                 styleClass="button"
                                 tabindex="${tabIndex}">
                ${button}
            </app2:securitySubmit>
        </c:when>
        <c:when test="${released_status == projectTimeStatus}">
            <c:if test="${(sessionUserId ==  userId) || app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
                <c:set var="canEditPage" value="${true}" scope="request"/>
                <app2:securitySubmit operation="${operation}"
                                     functionality="PROJECTTIME"
                                     property="editButton"
                                     styleClass="button"
                                     tabindex="${tabIndex}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
        </c:when>
        <c:when test="${confirmed_status == projectTimeStatus}">
            <c:if test="${app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
                <c:set var="canEditPage" value="${true}" scope="request"/>
                <app2:securitySubmit operation="${operation}"
                                     functionality="PROJECTTIME"
                                     property="editButton"
                                     styleClass="button"
                                     tabindex="${tabIndex}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
        </c:when>
        <c:when test="${not_confirmed_status == projectTimeStatus}">
            <c:if test="${sessionUserId ==  userId  || app2:hasProjectUserPermission(projectId,'CONFIRM',pageContext.request)}">
                <c:set var="canEditPage" value="${true}" scope="request"/>
                <app2:securitySubmit operation="${operation}"
                                     functionality="PROJECTTIME"
                                     property="editButton"
                                     styleClass="button"
                                     tabindex="${tabIndex}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
        </c:when>
    </c:choose>
</c:if>
</c:if>