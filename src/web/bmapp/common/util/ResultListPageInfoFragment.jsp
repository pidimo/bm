<%@ include file="/Includes.jsp" %>

<c:if test="${not empty fantaListName}">
    <c:set var="resultInfo" value="${app2:getFantabulousListResultInfo(fantaListName, pageContext.request)}" />

    "pageInfo": {
            "pageNumber" : "${resultInfo.pageNumber}",
            "pageSize" : "${resultInfo.pageSize}",
            "totalPages" : "${resultInfo.totalPages}",
            "listSize" : "${resultInfo.listSize}"
    },
</c:if>
