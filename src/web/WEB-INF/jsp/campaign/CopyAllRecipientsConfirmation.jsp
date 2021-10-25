<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    <!--
    //this is called in onLoad body property
    function goToParent() {
        parent.selectedSubmit();
        parent.hideBootstrapPopup();
    }

    function goClose() {
        parent.hideBootstrapPopup();
    }
    //-->
</script>


<html:form action="/Campaign/Activity/CampContact/Create/AllFromRecipients.do" focus="addAll" styleClass="form-horizontal">

    <c:set var="op" value="createAllRecipients"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    <html:hidden property="dto(activityId)" value="${param.activityId}"/>
    <div class="${app2:getFormPanelClasses()}">
        <div class="${app2:getFormGroupClasses()}">
            <div class="${app2:getFormContainClasses(null)}">
                <fmt:message key="Campaign.activity.copyAllContact.confirmation"/>
            </div>
        </div>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit property="addAll" styleId="addAll" styleClass="button ${app2:getFormButtonClasses()}" tabindex="1"><fmt:message
                key="Campaign.activity.campaignRecipients.addAll"/></html:submit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="2"><fmt:message
                key="Common.cancel"/></html:cancel>
    </div>
</html:form>
