<%@ include file="/Includes.jsp" %>

<html:form action="/Campaign/Cascade/DeleteByRange" styleClass="form-horizontal">

  <div class="${app2:getFormClasses()}">

    <html:hidden property="dto(op)" value="cascadeDeleteByRange"/>
    <html:hidden property="dto(startRecordDate)"/>
    <html:hidden property="dto(endRecordDate)"/>

    <fmt:message key="datePattern" var="datePattern"/>
    <fmt:formatDate var="startDate" value="${app2:intToDate(campaignDeleteByRangeForm.dtoMap['startRecordDate'])}" pattern="${datePattern}"/>
    <fmt:formatDate var="endDate" value="${app2:intToDate(campaignDeleteByRangeForm.dtoMap['endRecordDate'])}" pattern="${datePattern}"/>

    <div class="${app2:getFormPanelClasses()}">
      <fieldset>
        <legend class="title">
          <fmt:message key="Campaign.cascadeDelete.confirmation.title"/>
        </legend>

        <div class="alert alert-warning">
          <fmt:message key="Campaign.cascadeDelete.byRange.confirmation">
            <fmt:param value="${startDate}"/>
            <fmt:param value="${endDate}"/>
          </fmt:message>
        </div>
      </fieldset>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
      <app2:securitySubmit operation="DELETE" functionality="CAMPAIGNCASCADEDELETE"
                           styleClass="${app2:getFormButtonClasses()}" tabindex="3">
        <fmt:message key="Common.delete"/>
      </app2:securitySubmit>

      <app:url var="urlCampaignList" value="/List.do"/>
      <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlCampaignList}'" tabindex="5">
        <fmt:message key="Common.cancel"/>
      </html:button>
    </div>
  </div>
</html:form>
