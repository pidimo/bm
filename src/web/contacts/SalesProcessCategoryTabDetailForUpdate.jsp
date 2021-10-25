<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.salesProcess.categoryTab" scope="request"/>

<fmt:message var="title" key="SalesProcess.Title.update" scope="request"/>

<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action"
       value="/SalesProcess/Category/Tab/Update.do?dto(processId)=${param.processId}&processId=${param.processId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}&dto(addressId)=${param.contactId}"
       scope="request"/>

<c:set var="ajaxAction" value="/contacts/SalesProcess/Category/Tab/ReadChildCategory.do" scope="request"/>

<c:set var="downloadAction"
       value="/contacts/SalesProcess/Category/Tab/DownloadAttachment.do?dto(processId)=${param.processId}&processId=${param.processId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}&dto(addressId)=${param.contactId}&contactId=${param.contactId}&index=${param.index}"
       scope="request"/>

<c:set var="showCancelButton" value="true" scope="request"/>

<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="SalesProcess.Title.update" scope="request"/>

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
