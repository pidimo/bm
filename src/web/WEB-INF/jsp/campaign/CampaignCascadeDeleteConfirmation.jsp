<%@ include file="/Includes.jsp" %>

<html:form action="/Campaign/Cascade/Delete" styleClass="form-horizontal">

  <div class="${app2:getFormClasses()}">

    <html:hidden property="dto(op)" value="campaignCascadeDelete"/>
    <html:hidden property="dto(campaignId)"/>
    <html:hidden property="dto(campaignName)"/>

    <div class="${app2:getFormPanelClasses()}">
      <fieldset>
        <legend class="title">
          <c:out value="${title}"/>
        </legend>

        <div class="alert alert-warning">
          <fmt:message key="Campaign.cascadeDelete.confirmation">
            <fmt:param value="${form.dtoMap['campaignName']}"/>
          </fmt:message>
        </div>
      </fieldset>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
      <app2:securitySubmit operation="DELETE" functionality="CAMPAIGNCASCADEDELETE"
                           styleClass="${app2:getFormButtonClasses()}" tabindex="3">
        <fmt:message key="Common.delete"/>
      </app2:securitySubmit>

      <app:url var="urlCampaignUpdate" value="/Campaign/Forward/Update.do?dto(campaignId)=${form.dtoMap['campaignId']}&dto(campaignName)=${app2:encode(form.dtoMap['campaignName'])}&dto(operation)=update"/>
      <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlCampaignUpdate}'" tabindex="5">
        <fmt:message key="Common.cancel"/>
      </html:button>
    </div>
  </div>
</html:form>
