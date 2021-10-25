<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.contactPerson.categoryTab" scope="request"/>


<fmt:message var="title" key="ContactPerson" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action"
       value="/ContactPerson/CategoryTab/Update.do?dto(contactPersonId)=${param.contactPersonId}&dto(addressId)=${param.contactId}&contactId=${param.contactId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}"
       scope="request"/>
<c:set var="ajaxAction" value="/contacts/ContactPerson/ReadSubCategories.do" scope="request"/>
<c:set var="downloadAction"
       value="/contacts/ContactPerson/DownloadCategoryFieldValue.do?dto(contactPersonId)=${param.contactPersonId}&contactPersonId=${param.contactPersonId}&dto(addressId)=${param.contactId}&contactId=${param.contactId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}&index=${param.index}"
       scope="request"/>
<c:set var="showCancelButton" value="true" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="moduleTable" value="<%=ContactConstants.CONTACTPERSON_CATEGORY%>" scope="request"/>

<c:set var="windowTitle" value="Contact.person" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>