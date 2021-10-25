<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.create" scope="request"/>

<fmt:message var="title" key="SalesProcess.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="isSalesProcess" value="${false}" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Title.new" scope="request"/>
<c:set var="subCategoriesAjaxAction" value="/contacts/SalesProcess/Category/ReadChildCategory.do" scope="request"/>

<c:set var="downloadAttachmentCategoryAction"
       value="/contacts/SalesProcess/Category/DownloadAttachment.do?dto(contactId)=${param.contactId}&contactId=${param.contactId}&dto(processId)=${param.processId}&processId=${param.processId}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/SalesProcess.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/SalesProcess.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
