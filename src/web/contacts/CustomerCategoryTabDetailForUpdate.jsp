<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.customer.categoryTab" scope="request"/>

<fmt:message var="title" key="Customer.information" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Customer/CategoryTab/Update.do?dto(addressId)=${param.contactId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}"
       scope="request"/>
<c:set var="ajaxAction" value="/contacts/Customer/ReadSubCategories.do" scope="request"/>
<c:set var="downloadAction" value="/contacts/Customer/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}&contactId=${param.contactId}&categoryTabId=${param.categoryTabId}&dto(categoryTabId)=${param.categoryTabId}&index=${param.index}"
       scope="request"/>
<c:set var="showCancelButton" value="true" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="Customer.information" scope="request"/>

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