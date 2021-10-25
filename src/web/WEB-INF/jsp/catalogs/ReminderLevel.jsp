<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<html:form action="${action}"
           focus="dto(name)"
           enctype="multipart/form-data"
           styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(reminderLevelId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="ReminderLevel.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(name)"
                                  styleClass="${app2:getFormInputClasses()} text"
                                  maxlength="99"
                                  styleId="name_id"
                                  view="${'delete' == op}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="level_id">
                        <fmt:message key="ReminderLevel.level"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(level)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="5"
                                        styleId="level_id"
                                        numberType="integer"
                                        view="${'delete' == op}" tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="numberOfDays_id">
                        <fmt:message key="ReminderLevel.numberOfDays"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(numberOfDays)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="5"
                                        styleId="numberOfDays_id"
                                        numberType="integer"
                                        view="${'delete' == op}" tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="fee_id">
                        <fmt:message key="ReminderLevel.fee"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(fee)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="5"
                                        styleId="fee_id"
                                        numberType="decimal"
                                        maxInt="2"
                                        maxFloat="2"
                                        view="${'delete' == op}"
                                        tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${'create' == op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="ReminderLevel.language"/>
                        </label>
                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(languageId)"
                                          listName="languageList"
                                          labelProperty="name"
                                          styleId="languageId_id"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} filestyle"
                                          tabIndex="6">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="ReminderLevel.template"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <tags:bootstrapFile property="dto(file)"
                                                accept="application/msword"
                                                tabIndex="5"
                                                styleId="file_id"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="${op}"
                                     functionality="REMINDERLEVEL"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="7">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="REMINDERLEVEL"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew"
                                         tabindex="8">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="9">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="reminderLevelForm"/>

<c:if test="${'update' == op}">
    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <c:set var="reminderLevelId" value="${reminderLevelForm.dtoMap['reminderLevelId']}"/>
        <c:set var="name" value="${reminderLevelForm.dtoMap['name']}"/>

        <iframe class="embed-responsive-item" name="frame1"
                src="<app:url
                    value="/ReminderText/List.do?reminderLevelId=${reminderLevelId}&dto(reminderLevelId)=${reminderLevelId}&dto(name)=${name}"/>"
                scrolling="no"
                frameborder="0" id="iFrameId">
        </iframe>

    </div>
</c:if>