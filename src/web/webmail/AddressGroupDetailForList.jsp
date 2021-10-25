<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.contactGroup.contact.list" scope="request"/>

<fmt:message var="title" key="Webmail.addressGroup.edit" scope="request"/>
<fmt:message var="button" key="Webmail.addressGroup.addContact" scope="request"/>

<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Webmail.addressGroup.edit" scope="request"/>
<c:set var="pagetitle" value="Webmail.addressGroup.plural" scope="request"/>

<c:set var="optionTitle" value="Webmail.addressGroup.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/AddressGroupList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/webmail/AddressGroupList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>