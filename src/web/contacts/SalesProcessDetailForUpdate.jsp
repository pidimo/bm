<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.edit" scope="request"/>

<fmt:message var="title" key="SalesProcess.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="subCategoriesAjaxAction" value="/contacts/SalesProcess/Category/ReadChildCategory.do" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Title.update" scope="request"/>
<c:set var="isSalesProcess" value="${false}" scope="request"/>

<c:set var="enableTabLinks" value="${true}" scope="request"/>
<c:set var="tabLinkUrl"
       value="/contacts/SalesProcess/Category/Tab/Forward/Update.do?processId=${param.processId}&dto(processId)=${param.processId}&contactId=${param.contactId}&dto(addressId)=${param.contactId}"
       scope="request"/>

<c:set var="downloadAttachmentCategoryAction"
       value="/contacts/SalesProcess/Category/DownloadAttachment.do?dto(processId)=${param.processId}&processId=${param.processId}&dto(addressId)=${param.contactId}&contactId=${param.contactId}&index=${param.index}"
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