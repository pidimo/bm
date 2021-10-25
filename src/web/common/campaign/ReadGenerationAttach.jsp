<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<c:choose>
    <c:when test="${not empty attachsTableWidthPercent}">
        <c:set var="attachsWidth" value="${attachsTableWidthPercent}"/>
    </c:when>
    <c:otherwise>
        <c:set var="attachsWidth" value="100"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${null != formName}">
        <c:set var="attachs_" value="${mainCommunicationForm.dtoMap.genAttachs}"/>
    </c:when>
    <c:otherwise>
        <c:set var="attachs_" value="${dto.genAttachs}"/>
    </c:otherwise>
</c:choose>
<c:set var="UNKNOWN_NAME" value="unknown_name."/>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr>
        <td class="title" width="100%"><fmt:message   key="Mail.attaches"/></td>
    </tr>
    <tr>
        <td class="label" width="100%">
            <table cellpadding="0" cellspacing="0" border="0" width="${attachsWidth}%">
                <c:forEach var="attachs" items="${attachs_}">
                    
                    <c:if test="${attachs.attachVisibility!=true}">
                        <c:set var="oneAttachNotImage" value="true"/>
                        <tr>
                            <td width="5%">

                                <app:link action="/campaign/Download/GenerationAttach.do?dto(freeTextId)=${attachs.freeTextId}&dto(attachName)=${app2:encode(attachs.attachName)}" contextRelative="true">
                                    <img src="${baselayout}/img/webmail/paperclip.gif" border="0" alt=""/>
                                </app:link>
                            </td>
                            <td width="95%">

                                <app:link action="/campaign/Download/GenerationAttach.do?dto(freeTextId)=${attachs.freeTextId}&dto(attachName)=${app2:encode(attachs.attachName)}" contextRelative="true">
                                    <c:choose>
                                        <c:when test="${fn:contains(attachs.attachName,UNKNOWN_NAME)}">
                                            <fmt:message key="Webmail.Attach.unknownFile"/>
                                        </c:when>
                                        <c:otherwise>
                                                ${attachs.attachName}
                                        </c:otherwise>
                                    </c:choose>
                                </app:link>
                                &nbsp;
                            </td>
                            <td  class="label" align="right" style="padding:0;border:0;margin:0; right:auto;" nowrap>
                                <c:choose>
                                    <c:when test="${attachs.attachSize<1024}">
                                        ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${fn:substringBefore(attachs.attachSize/1024,'.')}"
                                                          type="number" pattern="${numberFormat}"/>
                                        &nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach><c:if test="${oneAttachNotImage==null}">&nbsp;</c:if>
            </table>
        </td>
    </tr>
</table>
