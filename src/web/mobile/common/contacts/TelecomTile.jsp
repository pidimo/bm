<%@ include file="/Includes.jsp" %>
${app2:sortTelecomsByPosition(telecomItems)}
<c:forEach var="item" items="${telecomItems}" varStatus="i">
    <c:if test="${item.value.size > 0}">

        <c:forEach var="telecom" items="${item.value.telecoms}">
            <c:choose>
                <c:when test="${'PHONE' == item.value.telecomTypeType}">
                    <c:url value="/layout/ui/img/mobile.gif" var="imgUrl"/>
                </c:when>
                <c:when test="${'FAX' == item.value.telecomTypeType}">
                    <c:url value="/layout/ui/img/phone2.gif" var="imgUrl"/>
                </c:when>
                <c:when test="${'EMAIL' == item.value.telecomTypeType}">
                    <c:url value="/layout/ui/img/arrobaB.gif" var="imgUrl"/>
                </c:when>
                <c:when test="${'LINK' == item.value.telecomTypeType}">
                    <c:url value="/layout/ui/img/link.gif" var="imgUrl"/>
                </c:when>
                <c:otherwise>
                    <c:url value="/layout/ui/img/notes.gif" var="imgUrl"/>
                </c:otherwise>
            </c:choose>
            <img src="${imgUrl}" alt="${item.value.telecomTypeName}"
                 title="${item.value.telecomTypeName}" border="0" width=""/>

            <c:choose>
                <c:when test="${item.value.telecomTypeType eq 'PHONE'}">
                    <a href="tel:${app2:replaceAll(telecom.data, '[\\(\\)\\.\\-\\ \\/\\\\]', '')}"><c:out
                            value="${telecom.data}"/></a>
                </c:when>
                <c:otherwise>
                    <c:out value="${telecom.data}"/>
                </c:otherwise>
            </c:choose>
            <br/>
        </c:forEach>
    </c:if>
</c:forEach>

