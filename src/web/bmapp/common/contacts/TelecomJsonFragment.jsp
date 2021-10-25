<%@ include file="/Includes.jsp" %>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL %>" scope="page"/>

<c:set var="indexFlag" value="${0}" scope="request"/>

"telecoms":
[
<c:forEach var="item" items="${telecomItems}" varStatus="itemStatus">

    <c:if test="${item.value.size > 0}">
        <c:if test="${indexFlag > 0}">, </c:if>
        {
            "telecomTypeId" : "${item.value.telecomTypeId}",
            "telecomTypeName" : "${item.value.telecomTypeName}",
            "telecomTypeType" : "${item.value.telecomTypeType}",
            "telecomTypePosition" : "${item.value.telecomTypePosition}",
            "predeterminedIndex" : "${item.value.predeterminedIndex}",
            "size" : "${item.value.size}",
            "telecomList" : [
            <c:forEach var="telecom" items="${item.value.telecoms}" varStatus="i">
                <c:if test="${i.index > 0}">, </c:if>
                {
                "telecomId" : "${telecom.telecomId}",
                "data" : "${app2:escapeJSON(telecom.data)}",
                "description" : "${app2:escapeJSON(telecom.description)}",
                "predetermined" : "${telecom.predetermined}",
                "index" : "${i.index}"
                }
            </c:forEach>
            ]
        }

        <c:set var="indexFlag" value="${1}" scope="request"/>
    </c:if>
</c:forEach>
]
