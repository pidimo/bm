<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<c:set var="UNKNOWN_NAME" value="unknown_name."/>
<c:set var="indexFlag" value="${0}" scope="request"/>

"attachmentArray": [

<c:if test="${null != attachments && not empty attachments}">
    <c:forEach var="attach" items="${attachments}">

        <c:if test="${false == attach.visible}">

            <c:set var="downloadAttachmentUrl" value="/webmail/Mail/Download.do?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${app2:escapeJSON(attach.attachFile)}" scope="request"/>

            <c:choose>
                <c:when test="${fn:contains(attach.attachFile,UNKNOWN_NAME)}">
                    <fmt:message var="fileNameVar" key="Webmail.Attach.unknownFile"/>
                </c:when>
                <c:otherwise>
                    <c:set var="fileNameVar" value="${attach.attachFile}"/>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${attach.size < 1024}">
                    <c:set var="fileSizeVar" value="${1}"/>
                </c:when>
                <c:otherwise>
                    <fmt:formatNumber var="fileSizeVar" value="${fn:substringBefore(attach.size/1024,'.')}"
                                      type="number" pattern="${numberFormat}"/>
                </c:otherwise>
            </c:choose>

            <c:if test="${indexFlag > 0}">, </c:if>
            {
            "attachId" : "${attach.attachId}",
            "freeTextId" : "${attach.freeTextId}",
            "fileName" : "${app2:escapeJSON(fileNameVar)}",
            "fileSize" : "${app2:escapeJSON(fileSizeVar)}",
            "visible" : "${attach.visible}",
            "downloadUrl" : "${downloadAttachmentUrl}"
            }

            <c:set var="indexFlag" value="${1}" scope="request"/>
        </c:if>
    </c:forEach>
</c:if>

]