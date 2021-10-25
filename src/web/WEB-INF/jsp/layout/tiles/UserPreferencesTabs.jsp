<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<c:set var="tabHeaderLabel" value="User.user" scope="request"/>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightUserList" module="/admin" patron="0 (1)" columnOrder="userName,login">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
    </fanta:label>
</c:set>


<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <c:set target="${tabItems}" property="User.Preferences"
               value="/UserPreferences/User.do?dto(userId)=${user.valueMap['userId']}"/>
        <c:set target="${tabItems}" property="User.password"
               value="/UserPreferences/UserPassword.do?dto(userId)=${user.valueMap['userId']}"/>
        <%--para preferencias de Scheduler--%>
        <app2:checkAccessRight functionality="PREFERENCE" permission="UPDATE">
            <c:set target="${tabItems}" property="Scheduler.configuration" value="/Configuration/Forward/Update.do"/>
        </app2:checkAccessRight>

        <%--*   Notifications- remindes   *--%>
        <c:set target="${tabItems}" property="Admin.Notification" value="/Notification/Forward/Update.do?view=true"/>

        <app2:checkAccessRight functionality="USERINTERFACE" permission="VIEW">
            <c:set var="listSect" value="${app2:getSectionListOfXmlFile(pageContext.request)}"/>
            <c:set var="firstSection" value="${listSect[0].value}"/>
            <c:set target="${tabItems}" property="UIManager.Styles"
                   value="/UIManager/Forward/UserStyleSheet.do?paramSection=${app2:encode(firstSection)}"/>
        </app2:checkAccessRight>

    </c:when>
</c:choose>


