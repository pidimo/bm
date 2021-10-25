<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>

<app2:jScriptUrl url="/finance/Download/InvoiceReminder/Document.do" var="jsDownloadUrl">
    <app2:jScriptUrlParam param="dto(freeTextId)" value="id_freeText"/>
</app2:jScriptUrl>
<script language="JavaScript" type="text/javascript">
    <!--
    function downloadReminder(id_freeText) {
        location.href = ${jsDownloadUrl};
    }
    //-->
</script>

<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="${'create' == op ? 'dto(reminderLevelId)' : 'dto(date)' }"
           styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(invoiceId)" value="${param.invoiceId}"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(reminderId)"/>
        <html:hidden property="dto(remindelLevelName)" styleId="3"/>
        <html:hidden property="dto(level)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(documentId)"/>
        <c:set var="documentId" value="${invoiceReminderForm.dtoMap['documentId']}"/>
        <c:if test="${not empty msgConfirmation}">
            <html:hidden property="dto(reGenerate)" value="true"/>
        </c:if>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <div class="${app2:getFormClasses()}">
        <div>
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICEREMINDER"
                                 styleClass="${app2:getFormButtonClasses()} marginButton"
                                 property="save" tabindex="8">
                ${button}
            </app2:securitySubmit>

            <c:if test="${'update' == op or 'create' == op}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEREMINDER"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     property="generate" tabindex="9">
                    <fmt:message key="InvoiceReminder.generate"/>
                </app2:securitySubmit>

                <c:if test="${not empty documentId}">
                    <app:url var="urlDownload"
                             value="/Download/InvoiceReminder/Document.do?dto(freeTextId)=${documentId}"/>
                    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="location.href='${urlDownload}'"
                                     tabindex="10">
                            <fmt:message key="InvoiceReminder.open"/>
                        </html:button>
                    </app2:checkAccessRight>
                </c:if>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="11">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="reminderLevelId_id">
                        <fmt:message key="InvoiceReminder.level"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || 'update' == op)}">
                        <fanta:select property="dto(reminderLevelId)"
                                      styleId="reminderLevelId_id"
                                      listName="reminderLevelList"
                                      labelProperty="name"
                                      valueProperty="reminderLevelId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/catalogs"
                                      firstEmpty="true"
                                      readOnly="${'delete' == op || 'update' == op}"
                                      tabIndex="1">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="InvoiceReminder.date"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(date)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="text ${app2:getFormInputClasses()}"
                                          mode="bootstrap"
                                          maxlength="10"
                                          currentDate="true"
                                          view="${'delete' == op}"
                                          tabindex="2"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="text_id">
                        <fmt:message key="InvoiceReminder.text"/>
                    </label>

                    <div class="t${app2:getFormContainClasses('delete'== op)}">
                        <html:textarea property="dto(text)"
                                       styleId="text_id"
                                       styleClass="divmediumDetailHigh ${app2:getFormInputClasses()}"
                                       rows="5"
                                       readonly="${'delete'== op}" tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div>
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICEREMINDER"
                                 styleClass="${app2:getFormButtonClasses()} marginButton"
                                 property="save" tabindex="4">
                ${button}
            </app2:securitySubmit>

            <c:if test="${'update' == op  or 'create' == op}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEREMINDER"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     property="generate" tabindex="5">
                    <fmt:message key="InvoiceReminder.generate"/>
                </app2:securitySubmit>

                <c:if test="${not empty documentId}">
                    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="location.href='${urlDownload}'"
                                     tabindex="6">
                            <fmt:message key="InvoiceReminder.open"/>
                        </html:button>
                    </app2:checkAccessRight>
                </c:if>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="invoiceReminderForm"/>