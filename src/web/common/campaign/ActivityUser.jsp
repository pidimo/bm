<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(userName)">


    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(version)"/>

        <!--only to preserve automatic assign criteria-->
        <html:hidden property="dto(homogeneously)"/>
        <html:hidden property="dto(customerPriority)"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
        <tr>
            <td colspan="4" class="title">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="17%">
                <fmt:message key="Campaign.activity.campContact.assign.responsible"/>
            </td>
            <td class="contain" width="33%">
                <app:text property="dto(userName)" styleClass="middleText" maxlength="40" tabindex="1" view="true"/>
            </td>
            <td class="label" width="17%">
                <fmt:message key="Campaign.activity.user.percent"/>
            </td>
            <td class="contain" width="33%">
                <app:text property="dto(percent)" styleClass="numberText" maxlength="40" tabindex="2" view="${op == 'delete'}"/>
                <fmt:message key="Common.probabilitySymbol"/>
            </td>
        </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
        <tr>
            <td class="button">
                <html:submit property="dto(${op})" styleClass="button" tabindex="3">
                    <c:out value="${button}"/>
                </html:submit>
                <html:cancel styleClass="button" tabindex="4">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>
