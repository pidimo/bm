<%@ include file="/Includes.jsp" %>

<html:form
        action="/Campaign/AditionalCriteria.do?module=campaign&index=${param.index}&campaignId=${param.campaignId}&question=true"
        styleClass="form-horizontal">

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <fmt:message key="Campaign.campaignContact.delete"/>
                </legend>

                <p>
                    <fmt:message key="Campaign.AditionalCriteria.ConfirmDeleteMessage"/>
                </p>

                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                    <html:hidden property="dto(companyId)"
                                 value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="dto(recordUserId)"
                                 value="${sessionScope.user.valueMap['userId']}"/>
                    <html:hidden property="addressType"/>
                    <html:hidden property="includePartner"/>
                    <html:hidden property="contactType"/>
                    <html:hidden property="isDouble"/>
                    <html:hidden property="deletePrevius"/>
                    <html:hidden property="hasEmail"/>
                    <html:hidden property="hasEmailTelecomType"/>


                    <app2:securitySubmit operation="delete" property="dto(delete)"
                                         functionality="CAMPAIGNCONTACTS" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="10">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>

                    <c:url var="urlCancel"
                           value="/campaign/Campaign/AditionalCriteria.do?index=${param.index-1}&campaignId=${param.campaignId}&redirect=cancel"/>
                    <html:button onclick="location.href='${urlCancel}'" property="dto(cancel)"
                                 styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="11">
                        <fmt:message key="Common.cancel"/>
                    </html:button>
                </div>

            </fieldset>
        </div>
    </div>
</html:form>