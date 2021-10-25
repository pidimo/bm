<%@ include file="/Includes.jsp" %>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>


{
<c:set var="fantaListName" value="mailTrayList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="mailList" items="${mailTrayList.result}" varStatus="statusVar">

    <c:set var="mailAddressInfoMap" value="${app2:getMailAddressInfo(mailList.MAILID, mailList.FOLDERTYPE, mailList.MAILFROM, pageContext.request)}"/>
    <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${mailAddressInfoMap['imageId']}"/>

    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "mailId" : "${mailList.MAILID}",
    "mailIndex" : "${mailList.MAILINDEX}",
    "folderId" : "${mailTrayForm.dto.folderId}",
    "inOut" : "${mailList.INOUT}",
    "mailState" : "${mailList.MAILSTATE}",
    "hasReadState" : "${app2:hasMailStateRead(mailList.MAILSTATE)}",
    "hasAnsweredState" : "${app2:hasMailStateAnswered(mailList.MAILSTATE)}",
    "hasForwardState" : "${app2:hasMailStateForward(mailList.MAILSTATE)}",
    "mailToFrom" : "${app2:escapeJSON(mailList.MAILTOFROM)}",
    "hasAttach" : "${mailList.MAILHASATTACH}",
    "mailPriority" : "${mailList.MAILPRIORITY}",
    "subject" : "${app2:escapeJSON(mailList.MAILSUBJECT)}",
    "sentDateTimeMillis" : "${mailList.SENTDATETIME}",
    "sentDateTime" : "${app2:getDateWithTimeZone(mailList.SENTDATETIME, timeZone, dateTimePattern)}",
    "mailIconType" : "${mailAddressInfoMap['mailIconType']}",
    "fromImageId" : "${mailAddressInfoMap['imageId']}",
    "fromImageUrl" : "${(not empty mailAddressInfoMap['imageId']) ? imageUrlVar : ""}",

    <c:choose>
        <c:when test="${mailList.MAILSIZE < 1024}">
            "mailSize" : "${1}"
        </c:when>
        <c:otherwise>
            <fmt:formatNumber var="sizeKb" value="${fn:substringBefore(mailList.MAILSIZE/1024,'.')}"
                              type="number" pattern="${numberFormat}"/>
            "mailSize" : "${sizeKb}"
        </c:otherwise>
    </c:choose>
    }
</c:forEach>
]
}

