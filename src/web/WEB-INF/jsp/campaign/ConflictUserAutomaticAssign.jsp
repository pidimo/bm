<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<html:form action="/Activity/CampContact/User/Confirmation/AutomaticAssign.do" focus="c1" styleClass="">

    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    <html:hidden property="dto(activityId)" value="${param.activityId}"/>
    <c:if test="${not empty conflictAssignForm.dtoMap['homogeneously']}">
        <html:hidden property="dto(homogeneously)"/>
    </c:if>
    <c:if test="${not empty conflictAssignForm.dtoMap['customerPriority']}">
        <html:hidden property="dto(customerPriority)"/>
    </c:if>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Activity.user.automaticAssign.conflict.confirmTitle"/>
        </legend>
        <div class="${app2:getFormContainClasses(null)}">
            <fmt:message key="Activity.user.automaticAssign.conflict.confirmMsg"/>
        </div>
        <div class="${app2:getFormContainClasses(null)}">
            <div class="radiocheck">
                <div class="radio radio-default">
                    <html:radio property="dto(conflictCriteria)"
                                styleId="c1" value="<%=CampaignConstants.CONFLICTASSIGN_ALL_CUSTOMERS%>"
                                styleClass="radio" tabindex="1"/>
                    <label for="c1">
                        <fmt:message key="Activity.user.automaticAssign.conflict.assignAll"/>
                    </label>
                </div>
            </div>
            <div class="radiocheck">
                <div class="radio radio-default">
                    <html:radio property="dto(conflictCriteria)" styleId="c2"
                                value="<%=CampaignConstants.CONFLICTASSIGN_JUST_WHAT_GETS%>"
                                styleClass="radio" tabindex="2"/>
                    <label for="c2">
                        <fmt:message key="Activity.user.automaticAssign.conflict.assignJustWhatGet"/>
                    </label>
                </div>
            </div>

        </div>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit property="assign" styleClass="button ${app2:getFormButtonClasses()}" tabindex="3">
            <fmt:message key="Campaign.activity.user.assign"/>
        </html:submit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="4"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>
</html:form>
