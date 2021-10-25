<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(active)" styleClass="form-horizontal">


    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(version)"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(campaignContactId)"/>
        <html:hidden property="dto(campaignId)"/>
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(addressId)"/>
        <html:hidden property="dto(hasTask)"/>
    </c:if>
    <c:set var="withTask" value="${form.dtoMap['hasTask']}"/>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${title}"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactName_id">
                    <fmt:message key="Campaign.company"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                    <app:text property="dto(contactName)" styleId="contactName_id"
                              styleClass="middleText" maxlength="40" tabindex="1" view="true"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                    <fmt:message key="Common.active"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(active)"
                                           styleId="active_id"
                                           styleClass="radio" tabindex="3" disabled="${op == 'delete'}"/>
                            <label for="active_id"></label>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.contactPerson"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                    <app:text property="dto(contactPersonName)" styleClass="middleText" maxlength="40" tabindex="2"
                              view="true"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="userId_id">
                    <fmt:message key="Campaign.activity.campContact.responsible"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || withTask)}">
                    <fanta:select property="dto(userId)"
                                  styleId="userId_id"
                                  listName="activityUserList" firstEmpty="true"
                                  labelProperty="userName" valueProperty="userId"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  tabIndex="4"
                                  module="/campaign"
                                  readOnly="${op == 'delete' || withTask}">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="activityId" value="${form.dtoMap['activityId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit property="dto(${op})" styleClass="button ${app2:getFormButtonClasses()}" tabindex="7">
            <c:out value="${button}"/>
        </html:submit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="8">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>

<tags:jQueryValidation formName="form"/>
