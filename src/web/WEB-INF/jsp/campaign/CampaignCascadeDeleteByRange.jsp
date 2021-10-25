<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>

<html:form action="/Campaign/DeleteByRange/Confirmation" styleClass="form-horizontal">

  <div class="${app2:getFormClasses()}">


    <div class="${app2:getFormPanelClasses()}">
      <fieldset>
        <legend class="title">
          <fmt:message key="Campaign.cascadeDelete.byRange.title"/>
        </legend>

        <div class="${app2:getFormGroupClasses()}">
          <label class="${app2:getFormLabelClasses()}" for="startDate">
            <fmt:message key="Campaign.dateCreation"/>
          </label>

          <div class="${app2:getFormContainClasses(false)}">
            <div class="row">
              <div class="col-sm-6 marginButton">
                <fmt:message key="datePattern" var="datePattern"/>
                <fmt:message key="Common.from" var="from"/>
                <div class="input-group date">
                  <app:dateText property="dto(startRecordDate)"
                                maxlength="10" tabindex="1" styleId="startDate"
                                calendarPicker="true"
                                mode="bootstrap"
                                placeHolder="${from}"
                                datePatternKey="${datePattern}"
                                styleClass="dateText ${app2:getFormInputClasses()}"
                                convert="true"/>
                </div>
              </div>

              <div class="col-sm-6 marginButton">
                <fmt:message key="Common.to" var="to"/>
                <div class="input-group date">
                  <app:dateText property="dto(endRecordDate)"
                                maxlength="10" tabindex="2" styleId="endDate"
                                calendarPicker="true"
                                mode="bootstrap"
                                placeHolder="${to}"
                                datePatternKey="${datePattern}"
                                styleClass="dateText ${app2:getFormInputClasses()}"
                                convert="true"/>
                </div>
              </div>
            </div>
          </div>
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
