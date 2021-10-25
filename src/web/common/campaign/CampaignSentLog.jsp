<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message   var="dateTimePattern" key="dateTimePattern"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD>
            <html:form action="${action}">
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(generationId)"/>

                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(campaignSentLogId)"/>
                </c:if>

                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(campaignSentLogId)"/>
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>

                <table class="container" align="center" width="55%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="button" colspan="2">
                            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CAMPAIGNSENTLOG"
                                                 styleClass="button">
                                ${button}
                            </app2:securitySubmit>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                    <tr>
                        <td class="title" colspan="2">
                                ${title}
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%">
                            <fmt:message key="CampaignSentLog.generationTime"/>
                        </td>
                        <td class="contain" width="70%">
                            <c:out value="${app2:getDateWithTimeZone(dto.generationTime, timeZone, dateTimePattern)}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%">
                            <fmt:message key="CampaignSentLog.user"/>
                        </td>
                        <td class="contain" width="70%">
                            <c:out value="${dto.userName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%">
                            <fmt:message key="CampaignSentLog.activity"/>
                        </td>
                        <td class="contain" width="70%">
                            <c:out value="${dto.activityName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%">
                            <fmt:message key="CampaignSentLog.summary"/>
                        </td>
                        <td class="contain" width="70%">
                            <c:out value="${dto.totalSuccess}/${dto.totalSent}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CAMPAIGNSENTLOG"
                                                 styleClass="button" tabindex="4">
                                ${button}
                            </app2:securitySubmit>

                            <html:cancel styleClass="button" tabindex="5">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>
        </TD>
    </TR>
</table>