<%@ include file="/Includes.jsp" %>

<c:set var="productEventListName" value="productEventList" scope="request"/>
<c:set var="productEventListResult" value="${productEventList.result}" scope="request"/>

<c:set var="body" value="/bmapp/common/product/ProductEventJsonList.jsp" scope="request"/>
<c:import url="/bmapp/layout/json/main.jsp"/>
