<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.product.categoryTab" scope="request"/>

<fmt:message var="title" key="Product.Title.edit" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/CategoryTab/Update.do?dto(productId)=${param.productId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}"
       scope="request"/>
<c:set var="ajaxAction" value="/products/ReadSubCategories.do" scope="request"/>
<c:set var="downloadAction" value="/products/DownloadCategoryFieldValue.do?dto(productId)=${param.productId}&productId=${param.productId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}&index=${param.index}"
       scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="Product.plural" scope="request"/>
<c:set var="windowTitle" value="Product.Title.edit" scope="request"/>



<c:choose>
       <c:when test="${sessionScope.isBootstrapUI}">
              <c:set var="body" value="/WEB-INF/jsp/catalogs/CategoryTabUtil.jsp" scope="request"/>
              <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
              <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
       </c:when>
       <c:otherwise>
              <c:set var="body" value="/common/catalogs/CategoryTabUtil.jsp" scope="request"/>
              <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
              <c:import url="${sessionScope.layout}/main.jsp"/>
       </c:otherwise>
</c:choose>