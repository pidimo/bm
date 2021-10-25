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

<c:choose>
    <c:when test="${not empty modeLabel}">
        <c:set var="modeLabelAttachRead" value="${app2:getFormLabelClasses()}" scope="request"/>
        <c:set var="modeContainAttachRead" value="${app2:getFormContainClasses(true)}" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="modeLabelAttachRead" value="control-label col-sm-2 label-left"/>
        <c:set var="modeContainAttachRead" value="col-sm-9 form-control-static"/>
    </c:otherwise>
</c:choose>

<c:if test="${(false == containOnlyEmbeddedAttachments)}">
    <div class="row col-xs-12">
        <label class="${modeLabelAttachRead}">
            <fmt:message key="Mail.attaches"/>
            <span class="${app2:getClassGlyphPaperClip()} pull-right"></span>
        </label>
        <div class="${modeContainAttachRead}">
            <c:if test="${null != attachments && not empty attachments}">
                <ul style="padding: 0; margin: 0; list-style: none; display: inline;">
                    <c:forEach var="attach" items="${attachments}">
                        <c:if test="${false == attach.visible}">
                            <li style="display: inline; padding-right: 10px;">
                                <app:link
                                        action="${downloadAttachmentUrl}?dto(attachId)=${attach.attachId}&dto(freeTextId)=${attach.freeTextId}&dto(attachName)=${attach.attachFile}"
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
        </div>
    </div>
</c:if>