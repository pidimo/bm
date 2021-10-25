<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.salesProcess.categoryTab" scope="request"/>

<fmt:message var="title" key="SalesProcess.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action"
       value="/CategoryTab/Update.do?dto(processId)=${param.processId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}"
       scope="request"/>
<c:set var="ajaxAction" value="/sales/ReadSubCategories.do" scope="request"/>
<c:set var="downloadAction"
       value="/sales/DownloadCategoryFieldValue.do?dto(processId)=${param.processId}&processId=${param.processId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}&index=${param.index}"
       scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="SalesProcess.plural" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Title.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
