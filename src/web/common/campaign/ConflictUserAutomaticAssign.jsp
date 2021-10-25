<%@ page import="com.piramide.elwis.utils.CampaignConstants"%>
<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center">
<html:form action="/Activity/CampContact/User/Confirmation/AutomaticAssign.do" focus="c1">

<html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
<html:hidden property="dto(activityId)" value="${param.activityId}"/>
    <c:if test="${not empty conflictAssignForm.dtoMap['homogeneously']}">
        <html:hidden property="dto(homogeneously)"/>
    </c:if>
    <c:if test="${not empty conflictAssignForm.dtoMap['customerPriority']}">
        <html:hidden property="dto(customerPriority)"/>
    </c:if>

    <tr>
        <td class="title"><fmt:message key="Activity.user.automaticAssign.conflict.confirmTitle"/></td>
    </tr>
    <tr>
        <td class="contain"><fmt:message key="Activity.user.automaticAssign.conflict.confirmMsg"/></td>
    </tr>
    <tr>
        <td class="contain">
            <html:radio property="dto(conflictCriteria)" styleId="c1" value="<%=CampaignConstants.CONFLICTASSIGN_ALL_CUSTOMERS%>" styleClass="radio" tabindex="1">
                <fmt:message key="Activity.user.automaticAssign.conflict.assignAll"/>
            </html:radio>
            <br/>
            <html:radio property="dto(conflictCriteria)" styleId="c2" value="<%=CampaignConstants.CONFLICTASSIGN_JUST_WHAT_GETS%>" styleClass="radio" tabindex="2">
                <fmt:message key="Activity.user.automaticAssign.conflict.assignJustWhatGet"/>
            </html:radio>
        </td>
    </tr>
    <tr>
        <td  class="button">
            <html:submit property="assign" styleClass="button" tabindex="3">
                <fmt:message key="Campaign.activity.user.assign"/>
            </html:submit>
            <html:cancel styleClass="button" tabindex="4"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</html:form>
</table>
