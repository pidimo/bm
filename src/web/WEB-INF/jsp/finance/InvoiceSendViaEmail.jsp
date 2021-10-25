<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/campaign/Campaign/ResponsibleTelecom.do" var="jsAjaxUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="senderEmployeeId" value="senderEmployee_id"/>
    <app2:jScriptUrlParam param="telecomTypeId" value="telecomType_id"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">

    function getSenderEmployeeTelecoms() {
        var telecomType_id = $("#idTelecomType").val();
        var senderEmployee_id = $("#idUserAddress").val();

        if (senderEmployee_id != undefined && telecomType_id != undefined) {
            makeAjaxReadEmployeeTelecoms(${jsAjaxUrl}, "");
        }
    }

    function makeAjaxReadEmployeeTelecoms(urlAction, parameters) {
        $.ajax({
            async:true,
            type: "GET",
            dataType: "html",
            data:parameters,
            url:urlAction,
            success: function(htmlResponse) {
                processAjaxTelecomsResponse(htmlResponse);
            },
            error: function(ajaxRequest) {
                processAjaxTelecomsResponse("");
            }
        });
    }

    function processAjaxTelecomsResponse(tagSelect) {
        if (tagSelect == null || tagSelect == undefined) {
            tagSelect = "";
        }
        $("#divTelecom").html(tagSelect);
    }
</script>

<html:form action="/Invoice/Send/ViaEmail" focus="dto(salutationLabel)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <c:choose>
                    <c:when test="${not empty invoiceIdsToSend}">
                        <html:hidden property="dto(sendViaEmailInvoices)" value="${invoiceIdsToSend}"/>
                        <html:hidden property="dto(totalSendViaEmailInvoices)" value="${totalInvoiceIds}"/>
                    </c:when>
                    <c:otherwise>
                        <html:hidden property="dto(sendViaEmailInvoices)"/>
                        <html:hidden property="dto(totalSendViaEmailInvoices)"/>
                    </c:otherwise>
                </c:choose>

                <html:hidden property="dto(userAddressId)" styleId="idUserAddress" value="${sessionScope.user.valueMap['userAddressId']}"/>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Invoice.sendViaEmail.totalInvoicesToSend"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <c:set var="totalVar" value="${totalInvoiceIds}"/>
                        <c:if test="${empty totalVar}">
                            <c:set var="totalVar"
                                   value="${invoiceSendViaEmailForm.dtoMap['totalSendViaEmailInvoices']}"/>
                        </c:if>

                        <c:out value="${totalVar}"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="idTelecomType">
                        <fmt:message key="TelecomType.type.mail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                        <html:select property="dto(telecomTypeId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     styleId="idTelecomType"
                                     tabindex="2"
                                     readonly="${readonly}">
                            <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="mailAccountId_id">
                        <fmt:message key="Invoice.sendViaEmail.mailAccount"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <fanta:select property="dto(mailAccountId)"
                                      styleId="mailAccountId_id"
                                      listName="mailAccountList"
                                      labelProperty="email"
                                      valueProperty="mailAccountId"
                                      firstEmpty="true"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/webmail"
                                      tabIndex="3">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <%--<div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="employeeMail_id">
                        <fmt:message key="Invoice.sendViaEmail.senderEmail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <div id="divTelecom">
                            <c:set var="senderEmployeeEmails"
                                   value="${app2:getSenderEmployeeEmails(null, sessionScope.user.valueMap['userAddressId'], invoiceSendViaEmailForm.dtoMap['telecomTypeId'],pageContext.request)}"/>
                            <html:select property="dto(employeeMail)"
                                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                         readonly="${readonly}"
                                         styleId="employeeMail_id"
                                         tabindex="3">
                                <html:options collection="senderEmployeeEmails" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>--%>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="EXECUTE" functionality="MAIL" styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Invoice.sendViaEmail.button.sendEmails"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} cancel">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="invoiceSendViaEmailForm" isValidate="true"/>