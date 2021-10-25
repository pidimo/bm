<%@ include file="/Includes.jsp" %>

<c:if test="${empty downloadAttachmentUrl}">
    <c:set var="downloadAttachmentUrl" value="/webmail/Mail/Download.do" scope="request"/>
</c:if>

<c:set var="UNKNOWN_NAME" value="unknown_name."/>

<c:set var="containOnlyEmbeddedAttachments" value="true"/>
<c:if test="${null != attachments && not empty attachments}">
    <c:forEach var="attach" items="${attachments}">
        <c:if test="${false == attach.visible}">
            <c:set var="containOnlyEmbeddedAttachments" value="false"/>
        </c:if>
    </c:forEach>
</c:if>

<c:if test="${empty labelWidthAttach}">
    <c:set var="labelWidthAttach" value="15%" scope="request"/>
</c:if>
<c:if test="${empty containWidthAttach}">
    <c:set var="containWidthAttach" value="85%" scope="request"/>
</c:if>

<c:if test="${(false == containOnlyEmbeddedAttachments)}">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td class="label" width="${labelWidthAttach}">
                <div style="float: left">
                    <fmt:message key="Mail.attaches"/>
                </div>
                <div style="display: inline-block; float: right; vertical-align: top; text-align: right;">
                    <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
                </div>
            </td>
            <td class="contain" width="${containWidthAttach}">
                <c:if test="${null != attachments && not empty attachments}">
                    <ul style="padding: 0; margin: 0; list-style: none; display: inline;">
                        <c:forEach var="attach" items="${attachments}">
                            <c:if test="${false == attach.visible}">
                                <li style="display: inline; padding-right: 10px;">
                                    <app:link action="${downloadAttachmentUrl}?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${attach.attachFile}"
                                              contextRelative="true">
                                        <c:choose>
                                            <c:when test="${fn:contains(attach.attachFile,UNKNOWN_NAME)}">
                                                <fmt:message key="Webmail.Attach.unknownFile"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${attach.attachFile}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </app:link>

                                    <c:choose>
                                        <c:when test="${attach.size < 1024}">
                                            (${1} <fmt:message key="Webmail.mailTray.Kb"/>)
                                        </c:when>
                                        <c:otherwise>
                                            (<fmt:formatNumber
                                                    value="${fn:substringBefore(attach.size/1024,'.')}"
                                                    type="number" pattern="${numberFormat}"/>
                                            <fmt:message key="Webmail.mailTray.Kb"/>)
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </c:if>
            </td>
        </tr>
    </table>
</c:if>