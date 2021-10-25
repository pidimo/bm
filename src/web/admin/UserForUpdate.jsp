<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.user.edit" scope="request"/>

<fmt:message   var="title" key="Contact.Organization.User.edit" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/User/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>


<c:set var="pagetitle" value="Admin.title" scope="request"/>
<c:set var="windowTitle" value="Contact.Organization.User.edit" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserInfo.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/UserInfo.jsp" scope="request"/>
        <c:set var="tabs" value="/UserTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>